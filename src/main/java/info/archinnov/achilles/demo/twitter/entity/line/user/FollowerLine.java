package info.archinnov.achilles.demo.twitter.entity.line.user;

import info.archinnov.achilles.demo.twitter.entity.User;
import info.archinnov.achilles.demo.twitter.entity.compound.UserKey;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * FollowerlineEntity
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity
@Table(name = "followers")
public class FollowerLine extends AbstractUserLine {

    public FollowerLine() {
    }

    public FollowerLine(String userLogin, User follower) {
        this.id = new UserKey(userLogin, follower.getLogin());
        this.user = follower;
    }

}
