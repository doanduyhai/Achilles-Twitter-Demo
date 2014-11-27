package info.archinnov.achilles.demo.twitter.entity.compound;

import info.archinnov.achilles.annotations.Column;
import info.archinnov.achilles.annotations.Order;
import info.archinnov.achilles.demo.twitter.entity.compound.TweetKey.LineType;

import java.util.UUID;

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
    private LineType type;

    @Column
    @Order(3)
    private String loginOrTag;

    public TweetIndexKey() {
    }

    public TweetIndexKey(UUID tweetId, LineType type, String loginOrTag) {
        this.tweetId = tweetId;
        this.type = type;
        this.loginOrTag = loginOrTag;
    }

    public UUID getTweetId() {
        return tweetId;
    }

    public void setTweetId(UUID tweetId) {
        this.tweetId = tweetId;
    }

    public LineType getType() {
        return type;
    }

    public void setType(LineType type) {
        this.type = type;
    }

    public String getLoginOrTag() {
        return loginOrTag;
    }

    public void setLoginOrTag(String loginOrTag) {
        this.loginOrTag = loginOrTag;
    }

}
