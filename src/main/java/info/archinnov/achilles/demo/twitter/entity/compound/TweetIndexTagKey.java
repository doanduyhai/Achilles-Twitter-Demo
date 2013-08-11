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
public class TweetIndexTagKey {

    @Column
    @Order(1)
    private UUID tweetId;

    @Column
    @Order(2)
    private String tag;

    public TweetIndexTagKey() {
    }

    public TweetIndexTagKey(UUID tweetId, String tag) {
        this.tweetId = tweetId;
        this.tag = tag;
    }

    public UUID getTweetId() {
        return tweetId;
    }

    public void setTweetId(UUID tweetId) {
        this.tweetId = tweetId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String login) {
        this.tag = login;
    }

}
