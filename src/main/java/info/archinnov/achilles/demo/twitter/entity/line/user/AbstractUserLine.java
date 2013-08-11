package info.archinnov.achilles.demo.twitter.entity.line.user;

import info.archinnov.achilles.demo.twitter.entity.User;
import info.archinnov.achilles.demo.twitter.entity.compound.UserKey;
import javax.persistence.EmbeddedId;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

/**
 * AbstractUserLine
 * 
 * @author DuyHai DOAN
 * 
 */
public class AbstractUserLine {

    @EmbeddedId
    protected UserKey id;

    @JoinColumn
    @ManyToMany
    protected User user;

    public UserKey getId() {
        return id;
    }

    public void setId(UserKey id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User friend) {
        this.user = friend;
    }

}
