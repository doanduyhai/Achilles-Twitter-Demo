package info.archinnov.achilles.demo.twitter.entity.line.user;

import info.archinnov.achilles.demo.twitter.entity.User;
import info.archinnov.achilles.demo.twitter.entity.compound.UserKey;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * FollowerLoginLine
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity
@Table(name = "followers_login")
public class FollowerLoginLine {

    @EmbeddedId
    private UserKey id;

    @Column
    private String follower;

    public FollowerLoginLine() {
    }

    public FollowerLoginLine(String userLogin, User follower) {
        this.id = new UserKey(userLogin, follower.getLogin());
        this.follower = follower.getLogin();
    }

    public UserKey getId() {
        return id;
    }

    public void setId(UserKey id) {
        this.id = id;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

}
