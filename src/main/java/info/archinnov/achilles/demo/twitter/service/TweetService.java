package info.archinnov.achilles.demo.twitter.service;

import info.archinnov.achilles.demo.twitter.entity.TweetIndex;
import info.archinnov.achilles.demo.twitter.entity.User;
import info.archinnov.achilles.demo.twitter.entity.compound.TagKey;
import info.archinnov.achilles.demo.twitter.entity.compound.TweetIndexKey;
import info.archinnov.achilles.demo.twitter.entity.compound.TweetIndexTagKey;
import info.archinnov.achilles.demo.twitter.entity.compound.TweetKey;
import info.archinnov.achilles.demo.twitter.entity.index.TweetFavoriteLineIndex;
import info.archinnov.achilles.demo.twitter.entity.index.TweetMentionLineIndex;
import info.archinnov.achilles.demo.twitter.entity.index.TweetTagLineIndex;
import info.archinnov.achilles.demo.twitter.entity.index.TweetTimeLineIndex;
import info.archinnov.achilles.demo.twitter.entity.line.tweet.FavoriteLine;
import info.archinnov.achilles.demo.twitter.entity.line.tweet.MentionLine;
import info.archinnov.achilles.demo.twitter.entity.line.tweet.TagLine;
import info.archinnov.achilles.demo.twitter.entity.line.tweet.TimeLine;
import info.archinnov.achilles.demo.twitter.entity.line.tweet.UserLine;
import info.archinnov.achilles.demo.twitter.entity.line.user.FollowerLoginLine;
import info.archinnov.achilles.demo.twitter.model.Tweet;
import info.archinnov.achilles.entity.manager.ThriftEntityManager;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import org.springframework.stereotype.Service;

/**
 * TweetAchillesService
 * 
 * @author DuyHai DOAN
 * 
 */
@Service
public class TweetService
{

    @Inject
    private ThriftEntityManager em;

    @Inject
    private UserService userService;

    @Inject
    private TweetParsingService tweetParsingService;

    public UUID createTweet(String authorLogin, String content)
    {
        User author = em.find(User.class, authorLogin);

        if (author == null)
        {
            throw new IllegalArgumentException("User with login '" + authorLogin
                    + "' does no exist");
        }

        UUID uuid = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

        Tweet tweet = new Tweet(uuid, author, content);

        // Initialize tweetIndex entity
        TweetIndex tweetIndex = em.find(TweetIndex.class, tweet.getId());
        if (tweetIndex == null)
        {
            tweetIndex = em.merge(new TweetIndex(tweet.getId(), tweet));
        }

        spreadTweetCreation(tweet, author.getLogin());
        spreadTags(tweet);
        spreadUserMention(tweet);

        author.getTweetsCounter().incr();

        return uuid;
    }

    public Tweet getTweet(String tweetId)
    {
        Tweet tweet = null;
        TweetIndex tweetIndex = em.find(TweetIndex.class, UUID.fromString(tweetId));
        if (tweetIndex != null)
        {
            tweet = tweetIndex.getTweet();
            tweet.setFavoritesCount(tweetIndex.getFavoritesCount().get());
        }
        return tweet;
    }

    public TweetIndex fetchTweetIndex(String tweetId)
    {
        TweetIndex tweetIndex = em.find(TweetIndex.class, UUID.fromString(tweetId));
        if (tweetIndex == null)
        {
            throw new IllegalArgumentException("Cannot find tweet with id '" + tweetId
                    + "' to remove");
        }
        return tweetIndex;
    }

    public Tweet removeTweet(String tweetId)
    {

        TweetIndex tweetIndex = fetchTweetIndex(tweetId);

        Tweet tweet = tweetIndex.getTweet();
        UUID id = tweet.getId();

        User user = em.find(User.class, tweet.getAuthor());
        em.removeById(UserLine.class, new TweetKey(tweet.getAuthor(), id));
        user.getTweetsCounter().decr();
        spreadTweetRemoval(id);

        em.remove(tweetIndex);

        return tweet;
    }

    private void spreadTweetCreation(Tweet tweet, String userLogin)
    {

        // Add current tweet to user timeline & userline
        em.persist(new TimeLine(userLogin, tweet));
        em.persist(new UserLine(userLogin, tweet));

        em.persist(new TweetTimeLineIndex(tweet.getId(), userLogin));

        Iterator<FollowerLoginLine> followersIter = em.sliceQuery(FollowerLoginLine.class)
                .partitionKey(userLogin)
                .iterator(100);

        while (followersIter.hasNext())
        {
            String followerLogin = followersIter.next().getId().getLogin();

            // Add tweet to follower timeline
            em.persist(new TimeLine(followerLogin, tweet));

            // Index current tweet as in follower timeline
            em.persist(new TweetTimeLineIndex(tweet.getId(), followerLogin));
        }
    }

    private void spreadTags(Tweet tweet)
    {
        Set<String> extractedTags = tweetParsingService.extractTags(tweet.getContent());

        for (String tag : extractedTags)
        {
            em.persist(new TweetTagLineIndex(tweet.getId(), tag));

            // Add tweet to tagline for each found tag
            em.persist(new TagLine(tag, tweet));
        }
    }

    private void spreadUserMention(Tweet tweet)
    {
        Set<String> extractedLogins = tweetParsingService.extractLogins(tweet.getContent());

        for (String userLogin : extractedLogins)
        {
            User user = em.find(User.class, userLogin);
            if (user != null)
            {
                em.persist(new TweetMentionLineIndex(tweet.getId(), userLogin));

                // Add tweet to user's mention line for each login found
                em.persist(new MentionLine(userLogin, tweet));

                // Increment user mention counter
                user.getMentionsCounter().incr();
            }
        }
    }

    private void spreadTweetRemoval(UUID tweetId)
    {

        // Remove from all users timeline
        Iterator<TweetTimeLineIndex> timelineIter = em.sliceQuery(TweetTimeLineIndex.class)
                .partitionKey(tweetId)
                .iterator(100);

        //KeyValueIterator<String, String> timelineUsers = tweetIndex.getTimelineUsers().iterator();
        while (timelineIter.hasNext())
        {
            String userLogin = timelineIter.next().getId().getLogin();
            em.removeById(TimeLine.class, new TweetKey(userLogin, tweetId));
            em.removeById(TweetTimeLineIndex.class, new TweetIndexKey(tweetId, userLogin));
        }

        // Remove from all users favoriteline
        Iterator<TweetFavoriteLineIndex> favoritelineIter = em.sliceQuery(TweetFavoriteLineIndex.class)
                .partitionKey(tweetId)
                .iterator(100);

        while (favoritelineIter.hasNext())
        {
            String userLogin = favoritelineIter.next().getId().getLogin();
            em.removeById(FavoriteLine.class, new TweetKey(userLogin, tweetId));
            em.removeById(TweetFavoriteLineIndex.class, new TweetIndexKey(tweetId, userLogin));
        }

        // Remove from all users mentionline
        Iterator<TweetMentionLineIndex> mentionlineIter = em.sliceQuery(TweetMentionLineIndex.class)
                .partitionKey(tweetId)
                .iterator(100);
        while (mentionlineIter.hasNext())
        {
            String userLogin = mentionlineIter.next().getId().getLogin();
            em.removeById(MentionLine.class, new TweetKey(userLogin, tweetId));
            em.removeById(TweetMentionLineIndex.class, new TweetIndexKey(tweetId, userLogin));
        }

        // Remove from all taglines
        Iterator<TweetTagLineIndex> taglineIter = em.sliceQuery(TweetTagLineIndex.class)
                .partitionKey(tweetId)
                .iterator(100);
        while (taglineIter.hasNext())
        {
            String tag = taglineIter.next().getId().getTag();
            em.removeById(TagLine.class, new TagKey(tag, tweetId));
            em.removeById(TweetTagLineIndex.class, new TweetIndexTagKey(tweetId, tag));
        }

    }
}
