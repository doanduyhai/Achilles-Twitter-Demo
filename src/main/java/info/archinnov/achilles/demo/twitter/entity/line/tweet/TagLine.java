package info.archinnov.achilles.demo.twitter.entity.line.tweet;

import info.archinnov.achilles.demo.twitter.entity.compound.TagKey;
import info.archinnov.achilles.demo.twitter.model.TweetModel;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TagLine
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity
@Table(name = "tagline")
public class TagLine {

    @EmbeddedId
    private TagKey id;

    @Column
    private TweetModel tweetModel;

    public TagLine() {
    }

    public TagLine(String tag, TweetModel tweetModel) {
        this.id = new TagKey(tag, tweetModel.getId());
        this.tweetModel = tweetModel;
    }

    public TagKey getId() {
        return id;
    }

    public void setId(TagKey id) {
        this.id = id;
    }

    public TweetModel getTweetModel() {
        return tweetModel;
    }

    public void setTweetModel(TweetModel tweetModel) {
        this.tweetModel = tweetModel;
    }

}
