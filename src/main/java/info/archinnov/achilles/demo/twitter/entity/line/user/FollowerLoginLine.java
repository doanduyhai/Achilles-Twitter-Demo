package info.archinnov.achilles.demo.twitter.entity.line.user;

import info.archinnov.achilles.demo.twitter.entity.User;
import info.archinnov.achilles.demo.twitter.entity.compound.UserKey;
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

    public FollowerLoginLine() {
    }

    public FollowerLoginLine(String userLogin, User follower) {
        this.id = new UserKey(userLogin, follower.getLogin());
    }

    public UserKey getId() {
        return id;
    }

    public void setId(UserKey id) {
        this.id = id;
    }

}
