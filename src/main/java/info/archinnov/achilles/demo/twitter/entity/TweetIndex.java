package info.archinnov.achilles.demo.twitter.entity;

import info.archinnov.achilles.annotations.EmbeddedId;
import info.archinnov.achilles.annotations.Entity;
import info.archinnov.achilles.demo.twitter.entity.compound.TweetIndexKey;
import info.archinnov.achilles.demo.twitter.entity.compound.TweetKey.LineType;

import java.util.UUID;

/**
 * AbstractTweetLineIndex
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity(table = "tweet_index")
public class TweetIndex {

	@EmbeddedId
	private TweetIndexKey id;

	public TweetIndex() {
	}

	public TweetIndexKey getId() {
		return id;
	}

	public TweetIndex(UUID tweetId, LineType type, String login) {
		this.id = new TweetIndexKey(tweetId, type, login);
	}

	public void setId(TweetIndexKey id) {
		this.id = id;
	}
}
