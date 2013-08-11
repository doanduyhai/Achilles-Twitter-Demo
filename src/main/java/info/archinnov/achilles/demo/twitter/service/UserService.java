package info.archinnov.achilles.demo.twitter.service;

import info.archinnov.achilles.demo.twitter.entity.User;
import info.archinnov.achilles.demo.twitter.entity.compound.UserKey;
import info.archinnov.achilles.demo.twitter.entity.line.user.AbstractUserLine;
import info.archinnov.achilles.demo.twitter.entity.line.user.FollowerLine;
import info.archinnov.achilles.demo.twitter.entity.line.user.FollowerLoginLine;
import info.archinnov.achilles.demo.twitter.entity.line.user.FriendLine;
import info.archinnov.achilles.entity.manager.ThriftEntityManager;
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
    private ThriftEntityManager em;

    private Function<AbstractUserLine, User> userLineToUser = new Function<AbstractUserLine, User>() {

        @Override
        public User apply(AbstractUserLine userLine)
        {
            return userLine.getUser();
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

        FriendLine foundFriend = em.find(FriendLine.class, new UserKey(userLogin, friendLogin));

        if (foundFriend == null)
        {
            em.persist(new FriendLine(userLogin, friend));
            user.getFriendsCounter().incr();

            em.persist(new FollowerLine(friendLogin, user));
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

        FriendLine foundFriend = em.find(FriendLine.class, new UserKey(userLogin, friendLogin));

        if (foundFriend != null)
        {
            em.remove(foundFriend);
            user.getFriendsCounter().decr();

            em.removeById(FollowerLine.class, new UserKey(friendLogin, userLogin));
            em.removeById(FollowerLoginLine.class, new UserKey(friendLogin, userLogin));
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
        List<FriendLine> friendLines = em.sliceQuery(FriendLine.class)
                .partitionKey(userLogin)
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
        List<FollowerLine> followerLines = em.sliceQuery(FollowerLine.class)
                .partitionKey(userLogin)
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
        user.setTweetsCount(user.getTweetsCounter().get());
        user.setFriendsCount(user.getFriendsCounter().get());
        user.setFollowersCount(user.getFollowersCounter().get());
        user.setMentionsCount(user.getMentionsCounter().get());
    }

    private void initCountersForSerialization(List<User> users)
    {
        for (User user : users)
        {
            initCountersForSerialization(user);
        }
    }
}
