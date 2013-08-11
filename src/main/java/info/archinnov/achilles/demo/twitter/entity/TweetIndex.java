package info.archinnov.achilles.demo.twitter.entity;

import info.archinnov.achilles.demo.twitter.model.Tweet;
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
@Table(name = "tweet_index")
public class TweetIndex {

    @Id
    private UUID id;

    @Column
    private Counter favoritesCount;

    @Column
    private Tweet tweet;

    public TweetIndex() {
    }

    public TweetIndex(UUID id, Tweet tweet) {
        this.id = id;
        this.tweet = tweet;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }

    public Counter getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(Counter favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

}
