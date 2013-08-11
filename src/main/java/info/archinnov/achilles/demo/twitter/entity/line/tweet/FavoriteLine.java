package info.archinnov.achilles.demo.twitter.entity.line.tweet;

import info.archinnov.achilles.demo.twitter.entity.compound.TweetKey;
import info.archinnov.achilles.demo.twitter.model.Tweet;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * FavoritelineEntity
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity
@Table(name = "favoriteline")
public class FavoriteLine extends AbstractTweetLine {

    public FavoriteLine() {
    }

    public FavoriteLine(String login, Tweet tweet) {
        this.id = new TweetKey(login, tweet.getId());
        this.tweet = tweet;
    }

}
