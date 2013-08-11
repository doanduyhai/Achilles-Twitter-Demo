package info.archinnov.achilles.demo.twitter.entity.index;

import java.util.UUID;
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
public class TweetFavoriteLineIndex extends AbstractTweetLineIndex {

    public TweetFavoriteLineIndex() {
    }

    public TweetFavoriteLineIndex(UUID tweetId, String login) {
        super(tweetId, login);
    }
}
