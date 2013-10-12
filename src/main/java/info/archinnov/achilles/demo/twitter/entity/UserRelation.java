package info.archinnov.achilles.demo.twitter.entity;

import info.archinnov.achilles.annotations.Column;
import info.archinnov.achilles.annotations.EmbeddedId;
import info.archinnov.achilles.annotations.Entity;
import info.archinnov.achilles.demo.twitter.entity.compound.UserKey;
import info.archinnov.achilles.demo.twitter.entity.compound.UserKey.Relationship;

/**
 * UserRelation
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity(table = "user_relation")
public class UserRelation {

	@EmbeddedId
	protected UserKey id;

	@Column
	protected User user;

	public UserRelation() {
	}

	public UserRelation(String userLogin, Relationship relationship, User relation) {
		this.id = new UserKey(userLogin, relationship, relation.getLogin());
		this.user = relation;
	}

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
