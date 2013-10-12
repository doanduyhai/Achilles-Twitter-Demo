package info.archinnov.achilles.demo.twitter.entity.compound;

import info.archinnov.achilles.annotations.Column;
import info.archinnov.achilles.annotations.Order;

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
	private Relationship relationship;

	@Column
	@Order(3)
	private String login;

	public UserKey() {
	}

	public UserKey(String userLogin, Relationship relationship, String login) {
		this.userLogin = userLogin;
		this.relationship = relationship;
		this.login = login;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public Relationship getRelationship() {
		return relationship;
	}

	public void setRelationship(Relationship relationship) {
		this.relationship = relationship;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public static enum Relationship {
		FRIEND, FOLLOWER;
	}
}
