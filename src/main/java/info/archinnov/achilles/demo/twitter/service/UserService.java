package info.archinnov.achilles.demo.twitter.service;

import static info.archinnov.achilles.demo.twitter.entity.compound.UserKey.Relationship.*;
import info.archinnov.achilles.demo.twitter.entity.FollowerLoginLine;
import info.archinnov.achilles.demo.twitter.entity.User;
import info.archinnov.achilles.demo.twitter.entity.UserRelation;
import info.archinnov.achilles.demo.twitter.entity.compound.UserKey;
import info.archinnov.achilles.persistence.PersistenceManager;

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
    private PersistenceManager manager;

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
        manager.insert(user);
        return user;
    }

    public User addFriend(String userLogin, String friendLogin) {
        User user = loadUser(userLogin);
        User friend = loadUser(friendLogin);

        UserRelation foundFriend = manager.find(UserRelation.class, new UserKey(userLogin, FRIEND, friendLogin));

        if (foundFriend == null) {
            User rawFiend = manager.removeProxy(friend);
            User rawUser = manager.removeProxy(user);

            manager.insert(new UserRelation(userLogin, FRIEND, rawFiend));
            user.getFriendsCounter().incr();
            manager.update(user);

            friend.getFollowersCounter().incr();
            manager.update(friend);

            manager.insert(new UserRelation(friendLogin, FOLLOWER, rawUser));
            manager.insert(new FollowerLoginLine(friendLogin, user));

            return manager.initAndRemoveProxy(friend);
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
            manager.delete(foundFriend);
            user.getFriendsCounter().decr();
            manager.update(user);

            friend.getFollowersCounter().decr();
            manager.update(friend);

            manager.deleteById(UserRelation.class, new UserKey(friendLogin, FOLLOWER, userLogin));
            manager.deleteById(FollowerLoginLine.class, new UserKey(friendLogin, FOLLOWER, userLogin));

            return manager.initAndRemoveProxy(friend);
        } else {
            throw new IllegalArgumentException("User with login '" + userLogin + "' does not have '" + friendLogin
                    + "' as friend");
        }

    }

    public List<User> getFriends(String userLogin, int length) {
        List<UserRelation> friendLines = manager.sliceQuery(UserRelation.class)
                .forSelect()
                .withPartitionComponents(userLogin)
                .fromClusterings(FRIEND).toClusterings(FRIEND)
                .orderByAscending()
                .get(length);

        return FluentIterable.from(friendLines).transform(userLineToUser).toList();

    }

    public List<User> getFollowers(String userLogin, int length) {
        List<UserRelation> followerLines = manager.sliceQuery(UserRelation.class)
                .forSelect()
                .withPartitionComponents(userLogin)
                .orderByAscending()
                .getFirstMatching(length, FOLLOWER);

        return FluentIterable.from(followerLines).transform(userLineToUser).toList();

    }

    public User getUser(String userLogin) {

        User user = loadUser(userLogin);
        User unwrap = manager.initAndRemoveProxy(user);
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
