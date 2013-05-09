package info.archinnov.achilles.demo.twitter.service;

import info.archinnov.achilles.demo.twitter.entity.TweetIndex;
import info.archinnov.achilles.demo.twitter.entity.User;
import info.archinnov.achilles.demo.twitter.entity.widerow.FavoriteLine;
import info.archinnov.achilles.demo.twitter.entity.widerow.MentionLine;
import info.archinnov.achilles.demo.twitter.entity.widerow.TagLine;
import info.archinnov.achilles.demo.twitter.entity.widerow.TimeLine;
import info.archinnov.achilles.demo.twitter.model.Tweet;
import info.archinnov.achilles.entity.manager.ThriftEntityManager;
import info.archinnov.achilles.entity.type.KeyValueIterator;
import info.archinnov.achilles.entity.type.WideMap;
import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * TweetAchillesService
 * 
 * @author DuyHai DOAN
 * 
 */
@Service
public class TweetService {

    @Inject
    private ThriftEntityManager em;

    @Inject
    private UserService userService;

    @Inject
    private TweetParsingService tweetParsingService;

    public UUID createTweet(String authorLogin, String content) {
        User author = em.find(User.class, authorLogin);

        if (author == null) {
            throw new IllegalArgumentException("User with login '" + authorLogin + "' does no exist");
        }

        UUID uuid = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

        Tweet tweet = new Tweet(uuid, author, content);

        //Initialize tweetIndex entity
        TweetIndex tweetIndex = em.find(TweetIndex.class, tweet.getId());
        if (tweetIndex == null) {
            tweetIndex = em.merge(new TweetIndex(tweet.getId(), tweet));
        }

        spreadTweetCreation(tweet, author, tweetIndex);
        spreadTags(tweet, tweetIndex);
        spreadUserMention(tweet, tweetIndex);

        author.getTweetsCounter().incr();

        return uuid;
    }

    public Tweet getTweet(String tweetId) {
        Tweet tweet = null;
        TweetIndex tweetIndex = em.find(TweetIndex.class, UUID.fromString(tweetId));
        if (tweetIndex != null) {
            tweet = tweetIndex.getTweet();
            tweet.setFavoritesCount(tweetIndex.getFavoritesCount().get());
        }
        return tweet;
    }

    public TweetIndex fetchTweetIndex(String tweetId) {
        TweetIndex tweetIndex = em.find(TweetIndex.class, UUID.fromString(tweetId));
        if (tweetIndex == null) {
            throw new IllegalArgumentException("Cannot find tweet with id '" + tweetId + "' to remove");
        }
        return tweetIndex;
    }

    public Tweet removeTweet(String tweetId) {

        TweetIndex tweetIndex = fetchTweetIndex(tweetId);

        Tweet tweet = tweetIndex.getTweet();
        UUID id = tweet.getId();

        User user = em.find(User.class, tweet.getAuthor());
        user.getUserline().remove(id);
        user.getTweetsCounter().decr();
        spreadTweetRemoval(tweetIndex);

        em.remove(tweetIndex);

        return tweet;
    }

    private void spreadTweetCreation(Tweet tweet, User user, TweetIndex tweetIndex) {

        // Add current tweet to user timeline & userline
        user.getTimeline().insert(tweet.getId(), tweet);
        user.getUserline().insert(tweet.getId(), tweet);

        // Index current tweet as in user timeline
        WideMap<String, String> timelineUsers = tweetIndex.getTimelineUsers();
        timelineUsers.insert(user.getLogin(), "");

        KeyValueIterator<String, User> followers = user.getFollowers().iterator(10);
        while (followers.hasNext()) {
            User follower = followers.nextValue();

            //Add tweet to follower timeline
            follower.getTimeline().insert(tweet.getId(), tweet);

            // Index current tweet as in follower timeline
            timelineUsers.insert(follower.getLogin(), "");
        }
    }

    private void spreadTags(Tweet tweet, TweetIndex tweetIndex) {
        Set<String> extractedTags = tweetParsingService.extractTags(tweet.getContent());

        for (String tag : extractedTags) {
            tweetIndex.getTags().insert(tag, "");

            // Add tweet to tagline for each found tag
            TagLine wideRow = em.find(TagLine.class, tag);
            wideRow.getTagline().insert(tweet.getId(), tweet);
        }
    }

    private void spreadUserMention(Tweet tweet, TweetIndex tweetIndex) {
        Set<String> extractedLogins = tweetParsingService.extractLogins(tweet.getContent());

        for (String userLogin : extractedLogins) {
            User user = em.find(User.class, userLogin);
            if (user != null) {
                tweetIndex.getMentionlineUsers().insert(userLogin, "");

                // Add tweet to user's mention line for each login found
                user.getMentionline().insert(tweet.getId(), tweet);

                //Increment user mention counter
                user.getMentionsCounter().incr();
            }
        }
    }

    private void spreadTweetRemoval(TweetIndex tweetIndex) {

        UUID tweetId = tweetIndex.getTweet().getId();

        // Remove from all users timeline
        KeyValueIterator<String, String> timelineUsers = tweetIndex.getTimelineUsers().iterator();
        while (timelineUsers.hasNext()) {
            String userLogin = timelineUsers.nextKey();
            if (StringUtils.isNotBlank(userLogin)) {
                TimeLine wideRow = em.find(TimeLine.class, userLogin);
                wideRow.getTimeline().remove(tweetId);
            }
        }

        // Remove from all users favoriteline
        KeyValueIterator<String, String> favoritelineUsers = tweetIndex.getFavoritelineUsers().iterator();
        while (favoritelineUsers.hasNext()) {
            String userLogin = favoritelineUsers.nextKey();
            if (StringUtils.isNotBlank(userLogin)) {
                FavoriteLine wideRow = em.find(FavoriteLine.class, userLogin);
                wideRow.getFavoriteline().remove(tweetId);
            }
        }

        // Remove from all users mentionline
        KeyValueIterator<String, String> mentionlineUsers = tweetIndex.getMentionlineUsers().iterator();
        while (mentionlineUsers.hasNext()) {
            String userLogin = mentionlineUsers.nextKey();
            if (StringUtils.isNotBlank(userLogin)) {
                MentionLine wideRow = em.find(MentionLine.class, userLogin);
                wideRow.getMentionline().remove(tweetId);
            }
        }

        // Remove from all taglines
        KeyValueIterator<String, String> tags = tweetIndex.getTags().iterator();
        while (tags.hasNext()) {
            String tag = tags.nextKey();
            if (StringUtils.isNotBlank(tag)) {
                TagLine wideRow = em.find(TagLine.class, tag);
                wideRow.getTagline().remove(tweetId);
            }
        }

    }
}
