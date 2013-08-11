package info.archinnov.achilles.demo.twitter.entity.line.tweet;

import info.archinnov.achilles.demo.twitter.entity.compound.TweetKey;
import info.archinnov.achilles.demo.twitter.model.TweetModel;
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

    public TimeLine(String login, TweetModel tweetModel) {
        this.id = new TweetKey(login, tweetModel.getId());
        this.tweetModel = tweetModel;
    }
}
