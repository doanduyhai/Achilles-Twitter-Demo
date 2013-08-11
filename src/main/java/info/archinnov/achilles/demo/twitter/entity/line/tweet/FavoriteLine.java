package info.archinnov.achilles.demo.twitter.entity.line.tweet;

import info.archinnov.achilles.demo.twitter.entity.compound.TweetKey;
import info.archinnov.achilles.demo.twitter.model.TweetModel;
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

    public FavoriteLine(String login, TweetModel tweetModel) {
        this.id = new TweetKey(login, tweetModel.getId());
        this.tweetModel = tweetModel;
    }

}
