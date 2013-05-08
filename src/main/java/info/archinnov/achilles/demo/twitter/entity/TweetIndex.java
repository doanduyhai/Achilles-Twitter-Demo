package info.archinnov.achilles.demo.twitter.entity;

import info.archinnov.achilles.entity.type.WideMap;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

    @ManyToMany
    @JoinColumn(table = "tweet_user_index")
    private WideMap<String, User> timelineUsers;

    @Column(table = "tweet_tag_index")
    private WideMap<String, String> tags;

    public TweetIndex() {
    }

    public TweetIndex(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public WideMap<String, User> getTimelineUsers() {
        return timelineUsers;
    }

    public WideMap<String, String> getTags() {
        return tags;
    }
}
