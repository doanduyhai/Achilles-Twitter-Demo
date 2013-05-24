package info.archinnov.achilles.demo.twitter.entity;

import info.archinnov.achilles.demo.twitter.model.Tweet;
import info.archinnov.achilles.type.Counter;
import info.archinnov.achilles.type.WideMap;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * TweetIndex
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity
@Table(name = "tweet_index")
public class TweetIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @Column
    private Counter favoritesCount;

    @Column
    private Tweet tweet;

    @ManyToMany
    @Column(table = "tweet_user_timeline_index")
    private WideMap<String, String> timelineUsers;

    @ManyToMany
    @Column(table = "tweet_user_favoriteline_index")
    private WideMap<String, String> favoritelineUsers;

    @Column(table = "tweet_user_mentionline_index")
    private WideMap<String, String> mentionlineUsers;

    @Column(table = "tweet_tag_index")
    private WideMap<String, String> tags;

    public TweetIndex() {
    }

    public TweetIndex(UUID id, Tweet tweet) {
        this.id = id;
        this.tweet = tweet;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }

    public Counter getFavoritesCount() {
        return favoritesCount;
    }

    public WideMap<String, String> getTimelineUsers() {
        return timelineUsers;
    }

    public WideMap<String, String> getTags() {
        return tags;
    }

    public WideMap<String, String> getFavoritelineUsers() {
        return favoritelineUsers;
    }

    public WideMap<String, String> getMentionlineUsers() {
        return mentionlineUsers;
    }
}
