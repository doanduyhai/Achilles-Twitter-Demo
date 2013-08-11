package info.archinnov.achilles.demo.twitter.service;

import static info.archinnov.achilles.demo.twitter.entity.compound.TweetKey.LineType.*;
import info.archinnov.achilles.demo.twitter.entity.FollowerLoginLine;
import info.archinnov.achilles.demo.twitter.entity.Tweet;
import info.archinnov.achilles.demo.twitter.entity.TweetIndex;
import info.archinnov.achilles.demo.twitter.entity.TweetLine;
import info.archinnov.achilles.demo.twitter.entity.User;
import info.archinnov.achilles.demo.twitter.entity.compound.TweetIndexKey;
import info.archinnov.achilles.demo.twitter.entity.compound.TweetKey;
import info.archinnov.achilles.demo.twitter.entity.compound.TweetKey.LineType;
import info.archinnov.achilles.demo.twitter.model.TweetModel;
import info.archinnov.achilles.entity.manager.CQLEntityManager;
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
    private CQLEntityManager em;

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
        em.removeById(Tweet.class, id);
        user.getTweetsCounter().decr();
        spreadTweetRemoval(id);

        em.remove(tweet);

        return tweetModel;
    }

    private void spreadTweetCreation(TweetModel tweetModel, String userLogin)
    {

        // Add current tweet to user timeline & userline
        em.persist(new TweetLine(userLogin, USERLINE, tweetModel));
        em.persist(new TweetLine(userLogin, TIMELINE, tweetModel));

        em.persist(new TweetIndex(tweetModel.getId(), USERLINE, userLogin));
        em.persist(new TweetIndex(tweetModel.getId(), TIMELINE, userLogin));

        Iterator<FollowerLoginLine> followersIter = em.sliceQuery(FollowerLoginLine.class)
                .partitionKey(userLogin)
                .iterator(100);

        while (followersIter.hasNext())
        {
            String followerLogin = followersIter.next().getId().getLogin();

            // Add tweet to follower timeline
            em.persist(new TweetLine(followerLogin, TIMELINE, tweetModel));

            // Index current tweet as in follower timeline
            em.persist(new TweetIndex(tweetModel.getId(), TIMELINE, followerLogin));
        }
    }

    private void spreadTags(TweetModel tweetModel)
    {
        Set<String> extractedTags = tweetParsingService.extractTags(tweetModel.getContent());

        for (String tag : extractedTags)
        {
            // Add tweet to tagline for each found tag
            em.persist(new TweetLine(tag, TAGLINE, tweetModel));

            em.persist(new TweetIndex(tweetModel.getId(), TAGLINE, tag));
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
                em.persist(new TweetIndex(tweetModel.getId(), MENTIONLINE, userLogin));

                // Add tweet to user's mention line for each login found
                em.persist(new TweetLine(userLogin, MENTIONLINE, tweetModel));

                // Increment user mention counter
                user.getMentionsCounter().incr();
            }
        }
    }

    private void spreadTweetRemoval(UUID tweetId)
    {
        // Retrieve tweet indexes
        Iterator<TweetIndex> tweetIndexIter = em.sliceQuery(TweetIndex.class)
                .partitionKey(tweetId)
                .iterator(100);

        while (tweetIndexIter.hasNext())
        {
            TweetIndexKey tweetIndex = tweetIndexIter.next().getId();
            LineType type = tweetIndex.getType();
            String userLoginOrTag = tweetIndex.getLoginOrTag();

            switch (type)
            {
                case USERLINE:
                    em.removeById(TweetLine.class, new TweetKey(userLoginOrTag, USERLINE, tweetId));
                    em.removeById(TweetIndex.class, new TweetIndexKey(tweetId, USERLINE, userLoginOrTag));
                    break;

                case TIMELINE:
                    em.removeById(TweetLine.class, new TweetKey(userLoginOrTag, TIMELINE, tweetId));
                    em.removeById(TweetIndex.class, new TweetIndexKey(tweetId, TIMELINE, userLoginOrTag));
                    break;

                case FAVORITELINE:
                    em.removeById(TweetLine.class, new TweetKey(userLoginOrTag, FAVORITELINE, tweetId));
                    em.removeById(TweetIndex.class, new TweetIndexKey(tweetId, FAVORITELINE, userLoginOrTag));
                    break;

                case MENTIONLINE:
                    em.removeById(TweetLine.class, new TweetKey(userLoginOrTag, MENTIONLINE, tweetId));
                    em.removeById(TweetIndex.class, new TweetIndexKey(tweetId, MENTIONLINE, userLoginOrTag));
                    break;

                case TAGLINE:
                    em.removeById(TweetLine.class, new TweetKey(userLoginOrTag, TAGLINE, tweetId));
                    em.removeById(TweetIndex.class, new TweetIndexKey(tweetId, TAGLINE, userLoginOrTag));
                    break;
            }
        }

    }
}
