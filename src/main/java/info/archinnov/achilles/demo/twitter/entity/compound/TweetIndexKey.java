package info.archinnov.achilles.demo.twitter.entity.compound;

import info.archinnov.achilles.annotations.Order;
import java.util.UUID;
import javax.persistence.Column;

/**
 * TweetIndexKey
 * 
 * @author DuyHai DOAN
 * 
 */
public class TweetIndexKey {

    @Column
    @Order(1)
    private UUID tweetId;

    @Column
    @Order(2)
    private String login;

    public TweetIndexKey() {
    }

    public TweetIndexKey(UUID tweetId, String login) {
        this.tweetId = tweetId;
        this.login = login;
    }

    public UUID getTweetId() {
        return tweetId;
    }

    public void setTweetId(UUID tweetId) {
        this.tweetId = tweetId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

}
