package info.archinnov.achilles.demo.twitter.entity.compound;

import info.archinnov.achilles.annotations.Order;
import java.util.UUID;
import javax.persistence.Column;

/**
 * TagKey
 * 
 * @author DuyHai DOAN
 * 
 */
public class TagKey {

    @Column
    @Order(1)
    private String tag;

    @Column
    @Order(2)
    private UUID tweetId;

    public TagKey() {
    }

    public TagKey(String tag, UUID tweetId) {
        this.tag = tag;
        this.tweetId = tweetId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public UUID getTweetId() {
        return tweetId;
    }

    public void setTweetId(UUID tweetId) {
        this.tweetId = tweetId;
    }

}
