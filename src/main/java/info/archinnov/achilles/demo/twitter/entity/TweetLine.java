package info.archinnov.achilles.demo.twitter.entity;

import info.archinnov.achilles.demo.twitter.entity.compound.TweetKey;
import info.archinnov.achilles.demo.twitter.entity.compound.TweetKey.LineType;
import info.archinnov.achilles.demo.twitter.model.TweetModel;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TweetLine
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity
@Table(name = "tweet_line")
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
