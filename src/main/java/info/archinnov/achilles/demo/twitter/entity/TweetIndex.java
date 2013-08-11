package info.archinnov.achilles.demo.twitter.entity;

import info.archinnov.achilles.demo.twitter.entity.compound.TweetIndexKey;
import info.archinnov.achilles.demo.twitter.entity.compound.TweetKey.LineType;
import java.util.UUID;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * AbstractTweetLineIndex
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity
@Table(name = "tweet_index")
public class TweetIndex {

    @EmbeddedId
    private TweetIndexKey id;

    public TweetIndex() {
    }

    public TweetIndexKey getId() {
        return id;
    }

    public TweetIndex(UUID tweetId, LineType type, String login) {
        this.id = new TweetIndexKey(tweetId, type, login);
    }

    public void setId(TweetIndexKey id) {
        this.id = id;
    }
}
