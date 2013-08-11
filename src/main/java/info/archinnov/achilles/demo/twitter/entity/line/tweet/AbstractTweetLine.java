package info.archinnov.achilles.demo.twitter.entity.line.tweet;

import info.archinnov.achilles.demo.twitter.entity.compound.TweetKey;
import info.archinnov.achilles.demo.twitter.model.TweetModel;
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
    protected TweetModel tweetModel;

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
