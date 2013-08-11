package info.archinnov.achilles.demo.twitter.service;

import static info.archinnov.achilles.demo.twitter.entity.compound.UserKey.Relationship.*;
import info.archinnov.achilles.demo.twitter.entity.FollowerLoginLine;
import info.archinnov.achilles.demo.twitter.entity.User;
import info.archinnov.achilles.demo.twitter.entity.UserRelation;
import info.archinnov.achilles.demo.twitter.entity.compound.UserKey;
import info.archinnov.achilles.entity.manager.CQLEntityManager;
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
public class UserService
{

    @Inject
    private CQLEntityManager em;

    private Function<UserRelation, User> userLineToUser = new Function<UserRelation, User>() {

        @Override
        public User apply(UserRelation userRelation)
        {
            return userRelation.getUser();
        }
    };

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
        return user;
    }

    public User addFriend(String userLogin, String friendLogin)
    {
        User user = loadUser(userLogin);
        User friend = loadUser(friendLogin);

        UserRelation foundFriend = em.find(UserRelation.class, new UserKey(userLogin, FRIEND,
                friendLogin));

        if (foundFriend == null)
        {
            em.persist(new UserRelation(userLogin, FRIEND, friend));
            user.getFriendsCounter().incr();

            em.persist(new UserRelation(friendLogin, FOLLOWER, user));
            em.persist(new FollowerLoginLine(friendLogin, user));
            friend.getFollowersCounter().incr();

            initCountersForSerialization(friend);
            return em.unwrap(friend);
        }
        else
        {
            throw new IllegalArgumentException("User with login '" + userLogin + "' has '"
                    + friendLogin + "' as friend already");
        }
    }

    public User removeFriend(String userLogin, String friendLogin)
    {
        User user = loadUser(userLogin);
        User friend = loadUser(friendLogin);

        UserRelation foundFriend = em.find(UserRelation.class, new UserKey(userLogin, FRIEND, friendLogin));

        if (foundFriend != null)
        {
            em.remove(foundFriend);
            user.getFriendsCounter().decr();

            em.removeById(UserRelation.class, new UserKey(friendLogin, FOLLOWER, userLogin));
            em.removeById(FollowerLoginLine.class, new UserKey(friendLogin, FOLLOWER, userLogin));
            friend.getFollowersCounter().decr();

            initCountersForSerialization(friend);
            return em.unwrap(friend);
        }
        else
        {
            throw new IllegalArgumentException("User with login '" + userLogin
                    + "' does not have '" + friendLogin + "' as friend");
        }

    }

    public List<User> getFriends(String userLogin, int length)
    {
        List<UserRelation> friendLines = em.sliceQuery(UserRelation.class)
                .partitionKey(userLogin)
                .fromClusterings(FRIEND)
                .toClusterings(FRIEND)
                .ordering(OrderingMode.ASCENDING)
                .get(length);

        List<User> friends = FluentIterable
                .from(friendLines)
                .transform(userLineToUser)
                .toImmutableList();

        initCountersForSerialization(friends);
        return em.unwrap(friends);
    }

    public List<User> getFollowers(String userLogin, int length)
    {
        List<UserRelation> followerLines = em.sliceQuery(UserRelation.class)
                .partitionKey(userLogin)
                .fromClusterings(FOLLOWER)
                .toClusterings(FOLLOWER)
                .ordering(OrderingMode.ASCENDING)
                .get(length);

        List<User> followers = FluentIterable
                .from(followerLines)
                .transform(userLineToUser)
                .toImmutableList();

        initCountersForSerialization(followers);
        return em.unwrap(followers);
    }

    public User getUser(String userLogin)
    {

        User user = loadUser(userLogin);
        em.initialize(user);

        initCountersForSerialization(user);
        return em.unwrap(user);
    }

    public User loadUser(String userLogin)
    {
        User user = em.find(User.class, userLogin);

        if (user == null)
        {
            throw new IllegalArgumentException("User with login '" + userLogin + "' does no exist");
        }
        return user;
    }

    private void initCountersForSerialization(User user)
    {
        Long tweetsCount = user.getTweetsCounter().get();
        user.setTweetsCount(tweetsCount == null ? 0 : tweetsCount);
        Long friendsCount = user.getFriendsCounter().get();
        user.setFriendsCount(friendsCount == null ? 0 : friendsCount);
        Long followersCount = user.getFollowersCounter().get();
        user.setFollowersCount(followersCount == null ? 0 : followersCount);
        Long mentionsCount = user.getMentionsCounter().get();
        user.setMentionsCount(mentionsCount == null ? 0 : mentionsCount);
    }

    private void initCountersForSerialization(List<User> users)
    {
        for (User user : users)
        {
            initCountersForSerialization(user);
        }
    }
}
