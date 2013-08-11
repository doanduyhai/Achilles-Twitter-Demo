package info.archinnov.achilles.demo.twitter.entity.compound;

import info.archinnov.achilles.annotations.Order;
import java.util.UUID;
import javax.persistence.Column;

/**
 * TweetKey
 * 
 * @author DuyHai DOAN
 * 
 */
public class TweetKey {

    @Column
    @Order(1)
    private String login;

    @Column
    @Order(2)
    private UUID tweetId;

    public TweetKey() {
    }

    public TweetKey(String login, UUID tweetId) {
        this.login = login;
        this.tweetId = tweetId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public UUID getTweetId() {
        return tweetId;
    }

    public void setTweetId(UUID tweetId) {
        this.tweetId = tweetId;
    }
}
