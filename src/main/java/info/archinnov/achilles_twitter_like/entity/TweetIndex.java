package info.archinnov.achilles_twitter_like.entity;

import info.archinnov.achilles.annotations.Lazy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * TweetIndex
 * 
 * @author DuyHai DOAN
 * 
 */
@Entity
@Table(name = "tweet_index")
public class TweetIndex implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	private UUID id;

	@JoinColumn
	@ManyToMany
	private List<User> timelineUsers = new ArrayList<User>();

	@Lazy
	@Column
	private List<String> tags = new ArrayList<String>();

	public TweetIndex() {}

	public TweetIndex(UUID id) {
		this.id = id;
	}

	public UUID getId()
	{
		return id;
	}

	public void setId(UUID id)
	{
		this.id = id;
	}

	public List<User> getTimelineUsers()
	{
		return timelineUsers;
	}

	public void setTimelineUsers(List<User> timelineUsers)
	{
		this.timelineUsers = timelineUsers;
	}

	public List<String> getTags()
	{
		return tags;
	}

	public void setTags(List<String> tags)
	{
		this.tags = tags;
	}
}
