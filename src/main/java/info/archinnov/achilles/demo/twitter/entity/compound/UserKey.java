package info.archinnov.achilles.demo.twitter.entity.compound;

import info.archinnov.achilles.annotations.Order;
import javax.persistence.Column;

/**
 * UserKey
 * 
 * @author DuyHai DOAN
 * 
 */
public class UserKey {

    @Column
    @Order(1)
    private String userLogin;

    @Column
    @Order(2)
    private String login;

    public UserKey() {
    }

    public UserKey(String userLogin, String login) {
        this.userLogin = userLogin;
        this.login = login;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

}
