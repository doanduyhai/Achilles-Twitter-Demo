package info.archinnov.achilles.demo.twitter.entity.line.tweet;

import info.archinnov.achilles.demo.twitter.entity.compound.TweetKey;
import info.archinnov.achilles.demo.twitter.model.Tweet;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * UserlineEntity
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity
@Table(name = "userline")
public class UserLine extends AbstractTweetLine {

    public UserLine() {
    }

    public UserLine(String login, Tweet tweet) {
        this.id = new TweetKey(login, tweet.getId());
        this.tweet = tweet;
    }

}
