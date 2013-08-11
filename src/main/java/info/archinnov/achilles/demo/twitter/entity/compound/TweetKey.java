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
    private String loginOrTag;

    @Column
    @Order(2)
    private LineType type;

    @Column
    @Order(3)
    private UUID tweetId;

    public TweetKey() {
    }

    public TweetKey(String loginOrTag, LineType type, UUID tweetId) {
        this.loginOrTag = loginOrTag;
        this.type = type;
        this.tweetId = tweetId;
    }

    public String getLoginOrTag() {
        return loginOrTag;
    }

    public void setLoginOrTag(String login) {
        this.loginOrTag = login;
    }

    public LineType getType() {
        return type;
    }

    public void setType(LineType type) {
        this.type = type;
    }

    public UUID getTweetId() {
        return tweetId;
    }

    public void setTweetId(UUID tweetId) {
        this.tweetId = tweetId;
    }

    public static enum LineType
    {
        USERLINE, TIMELINE, FAVORITELINE, MENTIONLINE, TAGLINE;
    }
}
