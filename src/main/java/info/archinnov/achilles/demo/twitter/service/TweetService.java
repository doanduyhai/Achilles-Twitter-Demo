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
import info.archinnov.achilles.persistence.PersistenceManager;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.cassandra.utils.UUIDGen;
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
	private PersistenceManager manager;

	@Inject
	private UserService userService;

	@Inject
	private TweetParsingService tweetParsingService;

	public UUID createTweet(String authorLogin, String content) {
		User author = manager.find(User.class, authorLogin);

		if (author == null) {
			throw new IllegalArgumentException("User with login '" + authorLogin + "' does no exist");
		}

		UUID uuid = UUIDGen.getTimeUUID();

		TweetModel tweetModel = new TweetModel(uuid, author, content);

		// Initialize tweetIndex entity
		Tweet tweet = manager.find(Tweet.class, tweetModel.getId());
		if (tweet == null) {
			manager.insert(new Tweet(tweetModel.getId(), tweetModel));
		}

		spreadTweetCreation(tweetModel, author.getLogin());
		spreadTags(tweetModel);
		spreadUserMention(tweetModel);

		author.getTweetsCounter().incr();

        manager.update(author);

		return uuid;
	}

	public TweetModel getTweet(String tweetId) {
		TweetModel tweetModel = null;
		Tweet tweet = manager.find(Tweet.class, UUID.fromString(tweetId));
		if (tweet != null) {
			tweetModel = tweet.getTweetModel();
			tweetModel.setFavoritesCount(tweet.getFavoritesCount().get());
		}
		return tweetModel;
	}

	public Tweet fetchTweetIndex(String tweetId) {
		Tweet tweet = manager.find(Tweet.class, UUID.fromString(tweetId));
		if (tweet == null) {
			throw new IllegalArgumentException("Cannot find tweet with id '" + tweetId + "' to remove");
		}
		return tweet;
	}

	public TweetModel removeTweet(String tweetId) {

		Tweet tweet = fetchTweetIndex(tweetId);

		TweetModel tweetModel = tweet.getTweetModel();
		UUID id = tweetModel.getId();

		User user = manager.find(User.class, tweetModel.getAuthor());
		manager.removeById(Tweet.class, id);

        user.getTweetsCounter().decr();
        manager.update(user);

        spreadTweetRemoval(id);

        manager.remove(tweet);

		return tweetModel;
	}

	private void spreadTweetCreation(TweetModel tweetModel, String userLogin) {

		// Add current tweet to user timeline & userline
		manager.insert(new TweetLine(userLogin, USERLINE, tweetModel));
		manager.insert(new TweetLine(userLogin, TIMELINE, tweetModel));

		manager.insert(new TweetIndex(tweetModel.getId(), USERLINE, userLogin));
		manager.insert(new TweetIndex(tweetModel.getId(), TIMELINE, userLogin));

		Iterator<FollowerLoginLine> followersIter = manager.sliceQuery(FollowerLoginLine.class)
                .forIteration()
                .withPartitionComponents(userLogin)
				.iterator(100);

		while (followersIter.hasNext()) {
			String followerLogin = followersIter.next().getId().getLogin();

			// Add tweet to follower timeline
			manager.insert(new TweetLine(followerLogin, TIMELINE, tweetModel));

			// Index current tweet as in follower timeline
			manager.insert(new TweetIndex(tweetModel.getId(), TIMELINE, followerLogin));
		}
	}

	private void spreadTags(TweetModel tweetModel) {
		Set<String> extractedTags = tweetParsingService.extractTags(tweetModel.getContent());

		for (String tag : extractedTags) {
			// Add tweet to tagline for each found tag
			manager.insert(new TweetLine(tag, TAGLINE, tweetModel));

			manager.insert(new TweetIndex(tweetModel.getId(), TAGLINE, tag));
		}
	}

	private void spreadUserMention(TweetModel tweetModel) {
		Set<String> extractedLogins = tweetParsingService.extractLogins(tweetModel.getContent());

		for (String userLogin : extractedLogins) {
			User user = manager.find(User.class, userLogin);
			if (user != null) {
				manager.insert(new TweetIndex(tweetModel.getId(), MENTIONLINE, userLogin));

				// Add tweet to user's mention line for each login found
				manager.insert(new TweetLine(userLogin, MENTIONLINE, tweetModel));

				// Increment user mention counter
				user.getMentionsCounter().incr();
                manager.update(user);
			}
		}
	}

	private void spreadTweetRemoval(UUID tweetId) {
		// Retrieve tweet indexes
		Iterator<TweetIndex> tweetIndexIter = manager.sliceQuery(TweetIndex.class)
                .forIteration()
                .withPartitionComponents(tweetId)
                .iterator(100);

		while (tweetIndexIter.hasNext()) {
			TweetIndexKey tweetIndex = tweetIndexIter.next().getId();
			LineType type = tweetIndex.getType();
			String userLoginOrTag = tweetIndex.getLoginOrTag();

			switch (type) {
			case USERLINE:
				manager.removeById(TweetLine.class, new TweetKey(userLoginOrTag, USERLINE, tweetId));
				manager.removeById(TweetIndex.class, new TweetIndexKey(tweetId, USERLINE, userLoginOrTag));
				break;

			case TIMELINE:
				manager.removeById(TweetLine.class, new TweetKey(userLoginOrTag, TIMELINE, tweetId));
				manager.removeById(TweetIndex.class, new TweetIndexKey(tweetId, TIMELINE, userLoginOrTag));
				break;

			case FAVORITELINE:
				manager.removeById(TweetLine.class, new TweetKey(userLoginOrTag, FAVORITELINE, tweetId));
				manager.removeById(TweetIndex.class, new TweetIndexKey(tweetId, FAVORITELINE, userLoginOrTag));
				break;

			case MENTIONLINE:
				manager.removeById(TweetLine.class, new TweetKey(userLoginOrTag, MENTIONLINE, tweetId));
				manager.removeById(TweetIndex.class, new TweetIndexKey(tweetId, MENTIONLINE, userLoginOrTag));
				break;

			case TAGLINE:
				manager.removeById(TweetLine.class, new TweetKey(userLoginOrTag, TAGLINE, tweetId));
				manager.removeById(TweetIndex.class, new TweetIndexKey(tweetId, TAGLINE, userLoginOrTag));
				break;
			}
		}

	}
}
