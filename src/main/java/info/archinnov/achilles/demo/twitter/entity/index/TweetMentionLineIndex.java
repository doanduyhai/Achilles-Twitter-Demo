package info.archinnov.achilles.demo.twitter.entity.index;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TweetMentionLineIndex
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity
@Table(name = "tweet_mentionline_index")
public class TweetMentionLineIndex extends AbstractTweetLineIndex {

    public TweetMentionLineIndex() {
    }

    public TweetMentionLineIndex(UUID tweetId, String login) {
        super(tweetId, login);
    }

}
