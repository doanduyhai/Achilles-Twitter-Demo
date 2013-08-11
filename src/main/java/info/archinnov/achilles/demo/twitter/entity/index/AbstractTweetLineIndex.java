package info.archinnov.achilles.demo.twitter.entity.index;

import info.archinnov.achilles.demo.twitter.entity.compound.TweetIndexKey;
import java.util.UUID;
import javax.persistence.EmbeddedId;

/**
 * AbstractTweetLineIndex
 * 
 * @author DuyHai DOAN
 * 
 */
public abstract class AbstractTweetLineIndex {

    @EmbeddedId
    private TweetIndexKey id;

    public AbstractTweetLineIndex() {
    }

    public TweetIndexKey getId() {
        return id;
    }

    public AbstractTweetLineIndex(UUID tweetId, String login) {
        this.id = new TweetIndexKey(tweetId, login);
    }

    public void setId(TweetIndexKey id) {
        this.id = id;
    }
}
