package info.archinnov.achilles_twitter_like.entity;

import info.archinnov.achilles.annotations.Lazy;
import info.archinnov.achilles.entity.type.WideMap;
import info.archinnov.achilles_twitter_like.json.JsonDateDeserializer;
import info.archinnov.achilles_twitter_like.json.JsonDateSerializer;
import info.archinnov.achilles_twitter_like.model.Tweet;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * User
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity
@Table(name = "user")
public class User implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	private String login;

	@Column
	private String firstname;

	@Column
	private String lastname;

	@JsonDeserialize(using = JsonDateDeserializer.class)
	@JsonSerialize(using = JsonDateSerializer.class)
	@Lazy
	@Column
	private Date accountCreationDate;

	@Lazy
	@Column
	private Integer tweetsCount;

	@Lazy
	@Column
	private Integer friendsCount;

	@Lazy
	@Column
	private Integer followersCount;

	@Column
	private WideMap<UUID, Tweet> timeline;

	@Column
	private WideMap<UUID, Tweet> userline;

	@JoinColumn(table = "friends")
	@ManyToMany
	private WideMap<String, User> friends;

	@JoinColumn(table = "followers")
	@ManyToMany
	private WideMap<String, User> followers;

	public User() {}

	public User(String login, String firstname, String lastname) {
		this.login = login;
		this.firstname = firstname;
		this.lastname = lastname;
		this.tweetsCount = 0;
		this.friendsCount = 0;
		this.followersCount = 0;
		this.accountCreationDate = new Date();
	}

	public String getLogin()
	{
		return login;
	}

	public void setLogin(String login)
	{
		this.login = login;
	}

	public String getFirstname()
	{
		return firstname;
	}

	public void setFirstname(String firstname)
	{
		this.firstname = firstname;
	}

	public String getLastname()
	{
		return lastname;
	}

	public void setLastname(String lastname)
	{
		this.lastname = lastname;
	}

	public Date getAccountCreationDate()
	{
		return accountCreationDate;
	}

	public void setAccountCreationDate(Date accountCreationDate)
	{
		this.accountCreationDate = accountCreationDate;
	}

	public Integer getTweetsCount()
	{
		return tweetsCount;
	}

	public void setTweetsCount(Integer tweetsCount)
	{
		this.tweetsCount = tweetsCount;
	}

	public Integer getFriendsCount()
	{
		return friendsCount;
	}

	public void setFriendsCount(Integer friendsCount)
	{
		this.friendsCount = friendsCount;
	}

	public Integer getFollowersCount()
	{
		return followersCount;
	}

	public void setFollowersCount(Integer followersCount)
	{
		this.followersCount = followersCount;
	}

	public WideMap<UUID, Tweet> getTimeline()
	{
		return timeline;
	}

	public void setTimeline(WideMap<UUID, Tweet> timeline)
	{
		this.timeline = timeline;
	}

	public WideMap<UUID, Tweet> getUserline()
	{
		return userline;
	}

	public void setUserline(WideMap<UUID, Tweet> userline)
	{
		this.userline = userline;
	}

	public WideMap<String, User> getFriends()
	{
		return friends;
	}

	public void setFriends(WideMap<String, User> friends)
	{
		this.friends = friends;
	}

	public WideMap<String, User> getFollowers()
	{
		return followers;
	}

	public void setFollowers(WideMap<String, User> followers)
	{
		this.followers = followers;
	}
}
