package info.archinnov.achilles.demo.twitter.entity.index;

import info.archinnov.achilles.demo.twitter.entity.compound.TweetIndexKey;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TweetFavoriteLineIndex
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity
@Table(name = "tweet_favoriteline_index")
public class TweetFavoriteLineIndex {

    @EmbeddedId
    private TweetIndexKey id;

    @Column
    private String dummy;

    public TweetFavoriteLineIndex() {
    }

    public TweetIndexKey getId() {
        return id;
    }

    public TweetFavoriteLineIndex(UUID tweetId, String login) {
        this.id = new TweetIndexKey(tweetId, login);
        this.dummy = "";
    }

    public void setId(TweetIndexKey id) {
        this.id = id;
    }

    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }
}
