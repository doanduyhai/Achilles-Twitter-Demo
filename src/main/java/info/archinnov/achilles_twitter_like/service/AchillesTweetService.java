package info.archinnov.achilles_twitter_like.service;

import info.archinnov.achilles.entity.manager.ThriftEntityManager;
import info.archinnov.achilles.entity.type.KeyValueIterator;
import info.archinnov.achilles_twitter_like.entity.TagLine;
import info.archinnov.achilles_twitter_like.entity.TweetIndex;
import info.archinnov.achilles_twitter_like.entity.User;
import info.archinnov.achilles_twitter_like.model.Tweet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class AchillesTweetService
{

	@Inject
	private ThriftEntityManager em;

	private static final Pattern TAG_PATTERN = Pattern.compile("#(\\w+)");

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

		author.setTweetsCount(author.getTweetsCount() + 1);
		em.merge(author);

		TweetIndex tweetIndex = new TweetIndex(tweet.getId());

		this.spreadTweet(tweet, author, tweetIndex);
		this.spreadTags(tweet, tweetIndex);

		em.persist(tweetIndex);

		return uuid;
	}

	void spreadTweet(Tweet tweet, User user, TweetIndex tweetIndex)
	{
		tweetIndex.getTimelineUsers().add(user);
		user.getTimeline().insert(tweet.getId(), tweet);
		user.getUserline().insert(tweet.getId(), tweet);

		KeyValueIterator<String, User> followers = user.getFollowers().iterator(10);
		while (followers.hasNext())
		{
			User follower = followers.nextValue();
			follower.getTimeline().insert(tweet.getId(), tweet);
			tweetIndex.getTimelineUsers().add(follower);
		}
	}

	void spreadTags(Tweet tweet, TweetIndex tweetIndex)
	{
		Set<String> extractedTags = this.extractTag(tweet.getContent());
		for (String tag : extractedTags)
		{
			tweetIndex.getTags().add(tag);
			TagLine tagLine = em.find(TagLine.class, tag);
			tagLine.getTagline().insert(tweet.getId(), tweet);
		}
	}

	public Set<String> extractTag(String content)
	{
		Set<String> tagSet = new HashSet<String>();

		Matcher matcher = TAG_PATTERN.matcher(content);
		while (matcher.find())
		{
			String tag = matcher.group(1);
			assert tag != null && !tag.isEmpty() && !tag.contains("#");
			if (!tagSet.contains(tag))
			{
				tagSet.add(tag);
			}
		}
		return tagSet;
	}

	public List<Tweet> getTagLine(String tag, int length)
	{
		TagLine tagLine = em.find(TagLine.class, tag);
		return tagLine.getTagline().findLastValues(length);
	}

	public Tweet getTweet(String userLogin, String tweetId)
	{
		User user = em.find(User.class, userLogin);

		return user.getTimeline().get(UUID.fromString(tweetId));
	}

	public Tweet removeTweet(String userLogin, String tweetId)
	{
		User user = em.find(User.class, userLogin);
		Tweet tweetToRemove = user.getUserline().get(UUID.fromString(tweetId));
		if (tweetToRemove != null)
		{
			user.getUserline().remove(tweetToRemove.getId());
			user.setTweetsCount(user.getTweetsCount() - 1);
			spreadRemove(tweetToRemove.getId());
		}
		em.merge(user);
		return tweetToRemove;
	}

	private void spreadRemove(UUID tweetId)
	{
		TweetIndex tweetIndex = em.find(TweetIndex.class, tweetId);
		for (User user : tweetIndex.getTimelineUsers())
		{
			user.getTimeline().remove(tweetId);
		}
		for (String tag : tweetIndex.getTags())
		{
			TagLine tagline = em.find(TagLine.class, tag);
			tagline.getTagline().remove(tweetId);
		}
		em.remove(tweetIndex);
	}
}
