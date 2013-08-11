package info.archinnov.achilles.demo.twitter.entity.index;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TweetTimeLineIndex
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity
@Table(name = "tweet_timeline_index")
public class TweetTimeLineIndex extends AbstractTweetLineIndex {

    public TweetTimeLineIndex() {
    }

    public TweetTimeLineIndex(UUID tweetId, String login) {
        super(tweetId, login);
    }

}
