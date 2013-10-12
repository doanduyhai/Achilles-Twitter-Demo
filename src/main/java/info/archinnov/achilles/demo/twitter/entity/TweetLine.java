package info.archinnov.achilles.demo.twitter.entity;

import info.archinnov.achilles.annotations.Column;
import info.archinnov.achilles.annotations.EmbeddedId;
import info.archinnov.achilles.annotations.Entity;
import info.archinnov.achilles.demo.twitter.entity.compound.TweetKey;
import info.archinnov.achilles.demo.twitter.entity.compound.TweetKey.LineType;
import info.archinnov.achilles.demo.twitter.model.TweetModel;

/**
 * TweetLine
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity(table = "tweet_line")
public class TweetLine {

	@EmbeddedId
	protected TweetKey id;

	@Column
	protected TweetModel tweetModel;

	public TweetLine() {
	}

	public TweetLine(String login, LineType type, TweetModel tweetModel) {
		this.id = new TweetKey(login, type, tweetModel.getId());
		this.tweetModel = tweetModel;
	}

	public TweetKey getId() {
		return id;
	}

	public void setId(TweetKey id) {
		this.id = id;
	}

	public TweetModel getTweetModel() {
		return tweetModel;
	}

	public void setTweetModel(TweetModel tweetModel) {
		this.tweetModel = tweetModel;
	}
}
