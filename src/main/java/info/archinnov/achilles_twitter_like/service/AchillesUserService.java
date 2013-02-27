package info.archinnov.achilles_twitter_like.service;

import info.archinnov.achilles.entity.manager.ThriftEntityManager;
import info.archinnov.achilles_twitter_like.entity.User;
import info.archinnov.achilles_twitter_like.model.Tweet;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

/**
 * UserService
 * 
 * @author DuyHai DOAN
 * 
 */
@Service
public class AchillesUserService
{

	@Inject
	private ThriftEntityManager em;

	public User createUser(User lightUser)
	{
		if (em.find(User.class, lightUser.getLogin()) != null)
		{
			throw new IllegalArgumentException("User with login '" + lightUser.getLogin()
					+ "' already exists");
		}

		User user = new User(lightUser.getLogin(), lightUser.getFirstname(),
				lightUser.getLastname());
		em.persist(user);
		return lightUser;
	}

	public User addFriend(String userLogin, String friendLogin)
	{
		User user = em.find(User.class, userLogin);
		User friend = em.find(User.class, friendLogin);

		if (user == null)
		{
			throw new IllegalArgumentException("User with login '" + userLogin + "' does no exist");
		}
		else if (friend == null)
		{
			throw new IllegalArgumentException("User with login '" + friendLogin
					+ "' does no exist");
		}
		else
		{
			user.getFriends().insert(friendLogin, friend);
			user.setFriendsCount(user.getFriendsCount() + 1);
			em.merge(user);

			friend.getFollowers().insert(userLogin, user);
			friend.setFollowersCount(friend.getFollowersCount() + 1);
			em.merge(friend);

			return em.unproxy(friend);
		}
	}

	public User removeFriend(String userLogin, String friendLogin)
	{
		User user = this.loadUser(userLogin);
		User friend = this.loadUser(friendLogin);

		user.getFriends().remove(friendLogin);
		user.setFriendsCount(user.getFriendsCount() - 1);
		em.merge(user);

		friend.getFollowers().remove(userLogin);
		friend.setFollowersCount(friend.getFollowersCount() - 1);
		em.merge(friend);

		return em.unproxy(friend);

	}

	public User getUser(String userLogin)
	{
		User user = em.find(User.class, userLogin);
		if (user != null)
		{
			em.initialize(user);
			user = em.unproxy(user);
		}

		return user;
	}

	public List<Tweet> getTimeline(String userLogin, int length)
	{
		User user = loadUser(userLogin);
		return user.getTimeline().findLastValues(length);
	}

	public List<Tweet> getUserline(String userLogin, int lenth)
	{
		User user = loadUser(userLogin);
		return user.getUserline().findLastValues(lenth);
	}

	private User loadUser(String userLogin)
	{
		User user = em.find(User.class, userLogin);

		if (user == null)
		{
			throw new IllegalArgumentException("User with login '" + userLogin + "' does no exist");
		}
		return user;
	}
}
