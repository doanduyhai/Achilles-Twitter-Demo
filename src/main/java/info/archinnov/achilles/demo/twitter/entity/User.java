package info.archinnov.achilles.demo.twitter.entity;

import info.archinnov.achilles.demo.twitter.json.JsonDateDeserializer;
import info.archinnov.achilles.demo.twitter.json.JsonDateSerializer;
import info.archinnov.achilles.demo.twitter.model.Tweet;
import info.archinnov.achilles.entity.type.Counter;
import info.archinnov.achilles.entity.type.WideMap;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * User
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity
@Table(name = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String login;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    @Column
    private Date accountCreationDate;

    @XmlTransient
    @Column
    private Counter tweetsCounter;

    private long tweetsCount;

    @XmlTransient
    @Column
    private Counter friendsCounter;

    private long friendsCount;

    @XmlTransient
    @Column
    private Counter followersCounter;

    private long followersCount;

    @Column(table = "timeline")
    private WideMap<UUID, Tweet> timeline;

    @Column(table = "userline")
    private WideMap<UUID, Tweet> userline;

    @JoinColumn(table = "friends")
    @ManyToMany
    private WideMap<String, User> friends;

    @JoinColumn(table = "followers")
    @ManyToMany
    private WideMap<String, User> followers;

    public User() {
    }

    public User(String login, String firstname, String lastname) {
        this.login = login;
        this.firstname = firstname;
        this.lastname = lastname;
        this.accountCreationDate = new Date();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getAccountCreationDate() {
        return accountCreationDate;
    }

    public void setAccountCreationDate(Date accountCreationDate) {
        this.accountCreationDate = accountCreationDate;
    }

    public Counter getTweetsCounter() {
        return tweetsCounter;
    }

    public Counter getFriendsCounter() {
        return friendsCounter;
    }

    public Counter getFollowersCounter() {
        return followersCounter;
    }

    public WideMap<UUID, Tweet> getTimeline() {
        return timeline;
    }

    public void setTimeline(WideMap<UUID, Tweet> timeline) {
        this.timeline = timeline;
    }

    public WideMap<UUID, Tweet> getUserline() {
        return userline;
    }

    public void setUserline(WideMap<UUID, Tweet> userline) {
        this.userline = userline;
    }

    public WideMap<String, User> getFriends() {
        return friends;
    }

    public void setFriends(WideMap<String, User> friends) {
        this.friends = friends;
    }

    public WideMap<String, User> getFollowers() {
        return followers;
    }

    public void setFollowers(WideMap<String, User> followers) {
        this.followers = followers;
    }

    public long getTweetsCount() {
        return tweetsCount;
    }

    public void setTweetsCount(long tweetsCount) {
        this.tweetsCount = tweetsCount;
    }

    public long getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(long friendsCount) {
        this.friendsCount = friendsCount;
    }

    public long getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(long followersCount) {
        this.followersCount = followersCount;
    }
}
