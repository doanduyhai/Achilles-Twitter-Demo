package info.archinnov.achilles.demo.twitter.entity.line.tweet;

import info.archinnov.achilles.demo.twitter.entity.compound.TweetKey;
import info.archinnov.achilles.demo.twitter.model.TweetModel;
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

    public UserLine(String login, TweetModel tweetModel) {
        this.id = new TweetKey(login, tweetModel.getId());
        this.tweetModel = tweetModel;
    }

}
