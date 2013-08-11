package info.archinnov.achilles.demo.twitter.entity.line.tweet;

import info.archinnov.achilles.demo.twitter.entity.compound.TweetKey;
import info.archinnov.achilles.demo.twitter.model.Tweet;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * MentionlineEntity
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity
@Table(name = "mentionline")
public class MentionLine extends AbstractTweetLine {

    public MentionLine() {
    }

    public MentionLine(String login, Tweet tweet) {
        this.id = new TweetKey(login, tweet.getId());
        this.tweet = tweet;
    }

}
