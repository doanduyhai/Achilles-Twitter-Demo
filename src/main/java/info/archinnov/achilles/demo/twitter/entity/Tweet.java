package info.archinnov.achilles.demo.twitter.entity;

import info.archinnov.achilles.annotations.Column;
import info.archinnov.achilles.annotations.Entity;
import info.archinnov.achilles.annotations.Id;
import info.archinnov.achilles.demo.twitter.model.TweetModel;
import info.archinnov.achilles.type.Counter;

import java.util.UUID;

/**
 * TweetIndex
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity(table = "tweet")
public class Tweet {

	@Id
	private UUID id;

	@Column
	private Counter favoritesCount;

	@Column
	private TweetModel tweetModel;

	public Tweet() {
	}

	public Tweet(UUID id, TweetModel tweetModel) {
		this.id = id;
		this.tweetModel = tweetModel;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public TweetModel getTweetModel() {
		return tweetModel;
	}

	public void setTweetModel(TweetModel tweetModel) {
		this.tweetModel = tweetModel;
	}

	public Counter getFavoritesCount() {
		return favoritesCount;
	}

	public void setFavoritesCount(Counter favoritesCount) {
		this.favoritesCount = favoritesCount;
	}

}
