package info.archinnov.achilles.demo.twitter.service;

import info.archinnov.achilles.demo.twitter.entity.Tweet;
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
import info.archinnov.achilles.demo.twitter.model.TweetModel;
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

        TweetModel tweetModel = new TweetModel(uuid, author, content);

        // Initialize tweetIndex entity
        Tweet tweet = em.find(Tweet.class, tweetModel.getId());
        if (tweet == null)
        {
            tweet = em.merge(new Tweet(tweetModel.getId(), tweetModel));
        }

        spreadTweetCreation(tweetModel, author.getLogin());
        spreadTags(tweetModel);
        spreadUserMention(tweetModel);

        author.getTweetsCounter().incr();

        return uuid;
    }

    public TweetModel getTweet(String tweetId)
    {
        TweetModel tweetModel = null;
        Tweet tweet = em.find(Tweet.class, UUID.fromString(tweetId));
        if (tweet != null)
        {
            tweetModel = tweet.getTweetModel();
            tweetModel.setFavoritesCount(tweet.getFavoritesCount().get());
        }
        return tweetModel;
    }

    public Tweet fetchTweetIndex(String tweetId)
    {
        Tweet tweet = em.find(Tweet.class, UUID.fromString(tweetId));
        if (tweet == null)
        {
            throw new IllegalArgumentException("Cannot find tweet with id '" + tweetId
                    + "' to remove");
        }
        return tweet;
    }

    public TweetModel removeTweet(String tweetId)
    {

        Tweet tweet = fetchTweetIndex(tweetId);

        TweetModel tweetModel = tweet.getTweetModel();
        UUID id = tweetModel.getId();

        User user = em.find(User.class, tweetModel.getAuthor());
        em.removeById(UserLine.class, new TweetKey(tweetModel.getAuthor(), id));
        user.getTweetsCounter().decr();
        spreadTweetRemoval(id);

        em.remove(tweet);

        return tweetModel;
    }

    private void spreadTweetCreation(TweetModel tweetModel, String userLogin)
    {

        // Add current tweet to user timeline & userline
        em.persist(new TimeLine(userLogin, tweetModel));
        em.persist(new UserLine(userLogin, tweetModel));

        em.persist(new TweetTimeLineIndex(tweetModel.getId(), userLogin));

        Iterator<FollowerLoginLine> followersIter = em.sliceQuery(FollowerLoginLine.class)
                .partitionKey(userLogin)
                .iterator(100);

        while (followersIter.hasNext())
        {
            String followerLogin = followersIter.next().getId().getLogin();

            // Add tweet to follower timeline
            em.persist(new TimeLine(followerLogin, tweetModel));

            // Index current tweet as in follower timeline
            em.persist(new TweetTimeLineIndex(tweetModel.getId(), followerLogin));
        }
    }

    private void spreadTags(TweetModel tweetModel)
    {
        Set<String> extractedTags = tweetParsingService.extractTags(tweetModel.getContent());

        for (String tag : extractedTags)
        {
            em.persist(new TweetTagLineIndex(tweetModel.getId(), tag));

            // Add tweet to tagline for each found tag
            em.persist(new TagLine(tag, tweetModel));
        }
    }

    private void spreadUserMention(TweetModel tweetModel)
    {
        Set<String> extractedLogins = tweetParsingService.extractLogins(tweetModel.getContent());

        for (String userLogin : extractedLogins)
        {
            User user = em.find(User.class, userLogin);
            if (user != null)
            {
                em.persist(new TweetMentionLineIndex(tweetModel.getId(), userLogin));

                // Add tweet to user's mention line for each login found
                em.persist(new MentionLine(userLogin, tweetModel));

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
