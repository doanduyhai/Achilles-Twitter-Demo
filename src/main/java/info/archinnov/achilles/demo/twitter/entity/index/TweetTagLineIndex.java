package info.archinnov.achilles.demo.twitter.entity.index;

import info.archinnov.achilles.demo.twitter.entity.compound.TweetIndexTagKey;
import java.util.UUID;
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

    public TweetTagLineIndex() {
    }

    public TweetIndexTagKey getId() {
        return id;
    }

    public TweetTagLineIndex(UUID tweetId, String tag) {
        this.id = new TweetIndexTagKey(tweetId, tag);
    }

    public void setId(TweetIndexTagKey id) {
        this.id = id;
    }
}
