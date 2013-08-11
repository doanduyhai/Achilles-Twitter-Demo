package info.archinnov.achilles.demo.twitter.entity;

import info.archinnov.achilles.demo.twitter.entity.compound.UserKey;
import info.archinnov.achilles.demo.twitter.entity.compound.UserKey.Relationship;
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
        this.id = new UserKey(userLogin, Relationship.FOLLOWER, follower.getLogin());
    }

    public UserKey getId() {
        return id;
    }

    public void setId(UserKey id) {
        this.id = id;
    }

}
