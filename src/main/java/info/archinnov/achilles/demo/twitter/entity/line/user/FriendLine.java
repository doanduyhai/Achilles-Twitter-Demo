package info.archinnov.achilles.demo.twitter.entity.line.user;

import info.archinnov.achilles.demo.twitter.entity.User;
import info.archinnov.achilles.demo.twitter.entity.compound.UserKey;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * FriendlineEntity
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity
@Table(name = "friends")
public class FriendLine extends AbstractUserLine {

    public FriendLine() {
    }

    public FriendLine(String userLogin, User friend) {
        this.id = new UserKey(userLogin, friend.getLogin());
        this.user = friend;
    }
}
