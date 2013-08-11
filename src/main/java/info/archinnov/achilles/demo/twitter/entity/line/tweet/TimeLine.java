package info.archinnov.achilles.demo.twitter.entity.line.tweet;

import info.archinnov.achilles.demo.twitter.entity.compound.TweetKey;
import info.archinnov.achilles.demo.twitter.model.Tweet;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TimelineEntity
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity
@Table(name = "timeline")
public class TimeLine extends AbstractTweetLine {

    public TimeLine() {
    }

    public TimeLine(String login, Tweet tweet) {
        this.id = new TweetKey(login, tweet.getId());
        this.tweet = tweet;
    }
}
