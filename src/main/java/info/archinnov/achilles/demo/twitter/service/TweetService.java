package info.archinnov.achilles.demo.twitter.service;

import info.archinnov.achilles.demo.twitter.entity.TagLine;
import info.archinnov.achilles.demo.twitter.entity.TweetIndex;
import info.archinnov.achilles.demo.twitter.entity.TweetLine;
import info.archinnov.achilles.demo.twitter.entity.TweetLine.TweetKey;
import info.archinnov.achilles.demo.twitter.entity.User;
import info.archinnov.achilles.demo.twitter.model.Tweet;
import info.archinnov.achilles.entity.manager.ThriftEntityManager;
import info.archinnov.achilles.entity.type.KeyValue;
import info.archinnov.achilles.entity.type.KeyValueIterator;
import info.archinnov.achilles.entity.type.WideMap;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

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

    private static final Pattern TAG_PATTERN = Pattern.compile("#(\\w+)");

    public UUID createTweet(String authorLogin, String content) {
        User author = em.find(User.class, authorLogin);

        if (author == null) {
            throw new IllegalArgumentException("User with login '" + authorLogin + "' does no exist");
        }

        UUID uuid = TimeUUIDUtils.getUniqueTimeUUIDinMillis();

        String digest = buildDigest(authorLogin, uuid);
        String partitionKey = extractPartitionKey(digest);

        Tweet tweet = new Tweet(uuid, author, content, digest);

        TweetLine tweetline = em.find(TweetLine.class, partitionKey);
        tweetline.getTweets().insert(new TweetKey(digest, authorLogin, uuid), tweet);

        TweetIndex tweetIndex = em.find(TweetIndex.class, tweet.getId());
        if (tweetIndex == null) {
            tweetIndex = em.merge(new TweetIndex(tweet.getId()));
        }

        spreadTweet(tweet, author, tweetIndex);
        spreadTags(tweet, tweetIndex);

        author.getTweetsCounter().incr();

        return uuid;
    }

    public Tweet getTweet(String digest) {

        TweetLine tweetline = em.find(TweetLine.class, extractPartitionKey(digest));
        TweetKey key = new TweetKey(digest);
        List<Tweet> found = tweetline.getTweets().findValues(key, key, 1);

        return found.size() > 0 ? found.get(0) : null;
    }

    public Tweet removeTweet(String digest) {

        TweetLine tweetline = em.find(TweetLine.class, extractPartitionKey(digest));
        TweetKey key = new TweetKey(digest);
        WideMap<TweetKey, Tweet> tweets = tweetline.getTweets();
        List<KeyValue<TweetKey, Tweet>> keyValues = tweets.find(key, key, 1);

        if (keyValues.size() == 0) {
            throw new IllegalArgumentException("Cannot find tweet with digest '" + digest + "' to remove");
        }

        KeyValue<TweetKey, Tweet> found = keyValues.get(0);
        key = found.getKey();
        tweets.remove(key);

        String userLogin = key.getAuthor();
        UUID tweetId = key.getId();

        User user = em.find(User.class, userLogin);
        user.getUserline().remove(tweetId);
        user.getTweetsCounter().decr();
        spreadRemove(tweetId);

        return found.getValue();
    }

    public List<Tweet> getTagLine(String tag, int length) {
        TagLine tagLine = em.find(TagLine.class, tag);
        return tagLine.getTagline().findLastValues(length);
    }

    Set<String> extractTag(String content) {
        Set<String> tagSet = new HashSet<String>();

        Matcher matcher = TAG_PATTERN.matcher(content);
        while (matcher.find()) {
            String tag = matcher.group(1);
            assert tag != null && !tag.isEmpty() && !tag.contains("#");
            if (!tagSet.contains(tag)) {
                tagSet.add(tag);
            }
        }
        return tagSet;
    }

    private void spreadTweet(Tweet tweet, User user, TweetIndex tweetIndex) {
        WideMap<String, User> timelineUsers = tweetIndex.getTimelineUsers();
        timelineUsers.insert(user.getLogin(), user);
        user.getTimeline().insert(tweet.getId(), tweet);
        user.getUserline().insert(tweet.getId(), tweet);

        KeyValueIterator<String, User> followers = user.getFollowers().iterator(10);
        while (followers.hasNext()) {
            User follower = followers.nextValue();
            follower.getTimeline().insert(tweet.getId(), tweet);
            timelineUsers.insert(follower.getLogin(), follower);
        }
    }

    private void spreadTags(Tweet tweet, TweetIndex tweetIndex) {
        Set<String> extractedTags = this.extractTag(tweet.getContent());
        for (String tag : extractedTags) {
            tweetIndex.getTags().insert(tag, "");
            TagLine tagLine = em.find(TagLine.class, tag);
            tagLine.getTagline().insert(tweet.getId(), tweet);
        }
    }

    private void spreadRemove(UUID tweetId) {

        TweetIndex tweetIndex = em.find(TweetIndex.class, tweetId);
        KeyValueIterator<String, User> users = tweetIndex.getTimelineUsers().iterator();
        while (users.hasNext()) {
            User user = users.nextValue();
            if (user != null) {
                user.getTimeline().remove(tweetId);
            }
        }

        KeyValueIterator<String, String> tags = tweetIndex.getTags().iterator();
        while (tags.hasNext()) {
            String tag = tags.nextKey();
            if (StringUtils.isNotBlank(tag)) {
                TagLine tagline = em.find(TagLine.class, tag);
                tagline.getTagline().remove(tweetId);
            }
        }
        em.remove(tweetIndex);
    }

    private String buildDigest(String author, UUID id) {
        try {
            return DigestUtils.md5DigestAsHex((author + id.toString()).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Cannot compute digest for user '" + author + "' and uuid '" + id
                    + "'", e);
        }
    }

    private String extractPartitionKey(String digest) {
        return StringUtils.left(digest, 5);
    }
}
