package info.archinnov.achilles.demo.twitter.service;

import static info.archinnov.achilles.demo.twitter.entity.compound.UserKey.Relationship.*;
import info.archinnov.achilles.demo.twitter.entity.FollowerLoginLine;
import info.archinnov.achilles.demo.twitter.entity.User;
import info.archinnov.achilles.demo.twitter.entity.UserRelation;
import info.archinnov.achilles.demo.twitter.entity.compound.UserKey;
import info.archinnov.achilles.entity.manager.CQLPersistenceManager;
import info.archinnov.achilles.type.OrderingMode;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

/**
 * UserService
 * 
 * @author DuyHai DOAN
 * 
 */
@Service
public class UserService {

	@Inject
	private CQLPersistenceManager manager;

	private Function<UserRelation, User> userLineToUser = new Function<UserRelation, User>() {

		@Override
		public User apply(UserRelation userRelation) {
			return userRelation.getUser();
		}
	};

	public User createUser(User lightUser) {
		if (manager.find(User.class, lightUser.getLogin()) != null) {
			throw new IllegalArgumentException("User with login '" + lightUser.getLogin() + "' already exists");
		}

		User user = new User(lightUser.getLogin(), lightUser.getFirstname(), lightUser.getLastname());
		manager.persist(user);
		return user;
	}

	public User addFriend(String userLogin, String friendLogin) {
		User user = loadUser(userLogin);
		User friend = loadUser(friendLogin);

		UserRelation foundFriend = manager.find(UserRelation.class, new UserKey(userLogin, FRIEND, friendLogin));

		if (foundFriend == null) {
			User rawFiend = manager.unwrap(friend);
			User rawUser = manager.unwrap(user);

			manager.persist(new UserRelation(userLogin, FRIEND, rawFiend));
			user.getFriendsCounter().incr();

			manager.persist(new UserRelation(friendLogin, FOLLOWER, rawUser));
			manager.persist(new FollowerLoginLine(friendLogin, user));
			friend.getFollowersCounter().incr();

			return manager.initAndUnwrap(friend);
		} else {
			throw new IllegalArgumentException("User with login '" + userLogin + "' has '" + friendLogin
					+ "' as friend already");
		}
	}

	public User removeFriend(String userLogin, String friendLogin) {
		User user = loadUser(userLogin);
		User friend = loadUser(friendLogin);

		UserRelation foundFriend = manager.find(UserRelation.class, new UserKey(userLogin, FRIEND, friendLogin));

		if (foundFriend != null) {
			manager.remove(foundFriend);
			user.getFriendsCounter().decr();

			manager.removeById(UserRelation.class, new UserKey(friendLogin, FOLLOWER, userLogin));
			manager.removeById(FollowerLoginLine.class, new UserKey(friendLogin, FOLLOWER, userLogin));
			friend.getFollowersCounter().decr();

			return manager.initAndUnwrap(friend);
		} else {
			throw new IllegalArgumentException("User with login '" + userLogin + "' does not have '" + friendLogin
					+ "' as friend");
		}

	}

	public List<User> getFriends(String userLogin, int length) {
		List<UserRelation> friendLines = manager.sliceQuery(UserRelation.class).partitionComponents(userLogin)
				.fromClusterings(FRIEND).toClusterings(FRIEND).ordering(OrderingMode.ASCENDING).get(length);

		return FluentIterable.from(friendLines).transform(userLineToUser).toImmutableList();

	}

	public List<User> getFollowers(String userLogin, int length) {
		List<UserRelation> followerLines = manager.sliceQuery(UserRelation.class).partitionComponents(userLogin)
				.ordering(OrderingMode.ASCENDING).getFirst(length, FOLLOWER);

		return FluentIterable.from(followerLines).transform(userLineToUser).toImmutableList();

	}

	public User getUser(String userLogin) {

		User user = loadUser(userLogin);
		User unwrap = manager.initAndUnwrap(user);
		return unwrap;
	}

	public User loadUser(String userLogin) {
		User user = manager.find(User.class, userLogin);

		if (user == null) {
			throw new IllegalArgumentException("User with login '" + userLogin + "' does no exist");
		}
		return user;
	}

}
