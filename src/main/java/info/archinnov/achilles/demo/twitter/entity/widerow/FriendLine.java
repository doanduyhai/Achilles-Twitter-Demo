package info.archinnov.achilles.demo.twitter.entity.widerow;

import info.archinnov.achilles.annotations.WideRow;
import info.archinnov.achilles.demo.twitter.entity.User;
import info.archinnov.achilles.entity.type.WideMap;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "friends")
@WideRow
public class FriendLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String login;

    @JoinColumn
    private WideMap<String, User> friends;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public WideMap<String, User> getFriends() {
        return friends;
    }
}
