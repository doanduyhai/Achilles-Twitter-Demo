package info.archinnov.achilles.demo.twitter.entity.line.tweet;

import info.archinnov.achilles.demo.twitter.entity.compound.TagKey;
import info.archinnov.achilles.demo.twitter.model.Tweet;
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
    private Tweet tweet;

    public TagLine() {
    }

    public TagLine(String tag, Tweet tweet) {
        this.id = new TagKey(tag, tweet.getId());
        this.tweet = tweet;
    }

    public TagKey getId() {
        return id;
    }

    public void setId(TagKey id) {
        this.id = id;
    }

    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }

}
