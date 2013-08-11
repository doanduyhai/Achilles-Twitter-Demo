package info.archinnov.achilles.demo.twitter.entity;

import info.archinnov.achilles.demo.twitter.model.TweetModel;
import info.archinnov.achilles.type.Counter;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TweetIndex
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity
@Table(name = "tweet")
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

    public TweetModel getTweet() {
        return tweetModel;
    }

    public void setTweet(TweetModel tweetModel) {
        this.tweetModel = tweetModel;
    }

    public Counter getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(Counter favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

}
