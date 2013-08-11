package info.archinnov.achilles.demo.twitter.entity.line.tweet;

import info.archinnov.achilles.demo.twitter.entity.compound.TweetKey;
import info.archinnov.achilles.demo.twitter.model.Tweet;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;

/**
 * AbstractLine
 * 
 * @author DuyHai DOAN
 * 
 */
public class AbstractTweetLine {

    @EmbeddedId
    protected TweetKey id;

    @Column
    protected Tweet tweet;

    public TweetKey getId() {
        return id;
    }

    public void setId(TweetKey id) {
        this.id = id;
    }

    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }
}
