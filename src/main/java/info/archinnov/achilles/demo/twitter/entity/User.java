package info.archinnov.achilles.demo.twitter.entity;

import info.archinnov.achilles.demo.twitter.json.JsonDateDeserializer;
import info.archinnov.achilles.demo.twitter.json.JsonDateSerializer;
import info.archinnov.achilles.type.Counter;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
public class User {

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

    @Column
    private Counter tweetsCounter;

    private long tweetsCount;

    @Column
    private Counter friendsCounter;

    private long friendsCount;

    @Column
    private Counter followersCounter;

    private long followersCount;

    @Column
    private Counter mentionsCounter;

    private long mentionsCount;

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

    public void setTweetsCounter(Counter tweetsCounter) {
        this.tweetsCounter = tweetsCounter;
    }

    public Counter getFriendsCounter() {
        return friendsCounter;
    }

    public void setFriendsCounter(Counter friendsCounter) {
        this.friendsCounter = friendsCounter;
    }

    public Counter getFollowersCounter() {
        return followersCounter;
    }

    public void setFollowersCounter(Counter followersCounter) {
        this.followersCounter = followersCounter;
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

    public long getMentionsCount() {
        return mentionsCount;
    }

    public void setMentionsCount(long mentionsCount) {
        this.mentionsCount = mentionsCount;
    }

    public Counter getMentionsCounter() {
        return mentionsCounter;
    }

    public void setMentionsCounter(Counter mentionsCounter) {
        this.mentionsCounter = mentionsCounter;
    }

}
