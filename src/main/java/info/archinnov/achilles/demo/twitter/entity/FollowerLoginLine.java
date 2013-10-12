package info.archinnov.achilles.demo.twitter.entity;

import info.archinnov.achilles.annotations.EmbeddedId;
import info.archinnov.achilles.annotations.Entity;
import info.archinnov.achilles.demo.twitter.entity.compound.UserKey;
import info.archinnov.achilles.demo.twitter.entity.compound.UserKey.Relationship;

/**
 * FollowerLoginLine
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity(table = "followers_login")
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
