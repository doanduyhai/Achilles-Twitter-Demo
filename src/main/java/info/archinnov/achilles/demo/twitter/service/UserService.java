package info.archinnov.achilles.demo.twitter.service;

import info.archinnov.achilles.demo.twitter.entity.User;
import info.archinnov.achilles.demo.twitter.model.Tweet;
import info.archinnov.achilles.entity.manager.ThriftEntityManager;
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
public class UserService {

    @Inject
    private ThriftEntityManager em;

    public User createUser(User lightUser) {
        if (em.find(User.class, lightUser.getLogin()) != null) {
            throw new IllegalArgumentException("User with login '" + lightUser.getLogin() + "' already exists");
        }

        User user = new User(lightUser.getLogin(), lightUser.getFirstname(), lightUser.getLastname());
        em.persist(user);
        return user;
    }

    public User addFriend(String userLogin, String friendLogin) {
        User user = loadUser(userLogin);
        User friend = loadUser(friendLogin);

        if (user.getFriends().get(friendLogin) == null) {
            user.getFriends().insert(friendLogin, friend);
            user.getFriendsCounter().incr();

            friend.getFollowers().insert(userLogin, user);
            friend.getFollowersCounter().incr();

            initCountersForSerialization(friend);
            return em.unproxy(friend);
        } else {
            throw new IllegalArgumentException("User with login '" + userLogin + "' has '" + friendLogin
                    + "' as friend already");
        }
    }

    public User removeFriend(String userLogin, String friendLogin) {
        User user = loadUser(userLogin);
        User friend = loadUser(friendLogin);

        if (user.getFriends().get(friendLogin) != null) {
            user.getFriends().remove(friendLogin);
            user.getFriendsCounter().decr();

            friend.getFollowers().remove(userLogin);
            friend.getFollowersCounter().decr();

            initCountersForSerialization(friend);
            return em.unproxy(friend);
        } else {
            throw new IllegalArgumentException("User with login '" + userLogin + "' does not have '" + friendLogin
                    + "' as friend");
        }

    }

    public User getUser(String userLogin) {

        User user = loadUser(userLogin);
        em.initialize(user);

        initCountersForSerialization(user);
        return em.unproxy(user);
    }

    public List<Tweet> getTimeline(String userLogin, int length) {
        User user = loadUser(userLogin);
        return user.getTimeline().findLastValues(length);
    }

    public List<Tweet> getUserline(String userLogin, int lenth) {
        User user = loadUser(userLogin);
        return user.getUserline().findLastValues(lenth);
    }

    private User loadUser(String userLogin) {
        User user = em.find(User.class, userLogin);

        if (user == null) {
            throw new IllegalArgumentException("User with login '" + userLogin + "' does no exist");
        }
        return user;
    }

    private void initCountersForSerialization(User user) {
        user.setTweetsCount(user.getTweetsCounter().get());
        user.setFriendsCount(user.getFriendsCounter().get());
        user.setFollowersCount(user.getFollowersCounter().get());
    }
}
