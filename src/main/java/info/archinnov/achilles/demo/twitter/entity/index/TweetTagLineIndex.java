package info.archinnov.achilles.demo.twitter.entity.index;

import info.archinnov.achilles.demo.twitter.entity.compound.TweetIndexTagKey;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TweetTagLineIndex
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity
@Table(name = "tweet_tagline_index")
public class TweetTagLineIndex {

    @EmbeddedId
    private TweetIndexTagKey id;

    @Column
    private String dummy;

    public TweetTagLineIndex() {
    }

    public TweetIndexTagKey getId() {
        return id;
    }

    public TweetTagLineIndex(UUID tweetId, String tag) {
        this.id = new TweetIndexTagKey(tweetId, tag);
        this.dummy = "";
    }

    public void setId(TweetIndexTagKey id) {
        this.id = id;
    }

    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }
}
