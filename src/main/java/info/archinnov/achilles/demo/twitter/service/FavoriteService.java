package info.archinnov.achilles.demo.twitter.service;

import info.archinnov.achilles.demo.twitter.entity.TweetIndex;
import info.archinnov.achilles.demo.twitter.entity.User;
import info.archinnov.achilles.demo.twitter.model.Tweet;
import info.archinnov.achilles.type.WideMap;
import java.util.UUID;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    @Inject
    private UserService userService;

    @Inject
    private TweetService tweetService;

    public void addTweetToFavorite(String userLogin, String tweetId) {

        User user = userService.loadUser(userLogin);
        TweetIndex tweetIndex = tweetService.fetchTweetIndex(tweetId);

        WideMap<UUID, Tweet> favoriteline = user.getFavoriteline();
        UUID id = tweetIndex.getId();

        if (favoriteline.get(id) == null) {
            favoriteline.insert(id, tweetIndex.getTweet());
            tweetIndex.getFavoritelineUsers().insert(userLogin, "");
            tweetIndex.getFavoritesCount().incr();
        }
    }

    public void removeTweetFromFavorite(String userLogin, String tweetId) {

        User user = userService.loadUser(userLogin);
        TweetIndex tweetIndex = tweetService.fetchTweetIndex(tweetId);

        WideMap<UUID, Tweet> favoriteline = user.getFavoriteline();
        UUID id = tweetIndex.getId();

        if (favoriteline.get(id) != null) {
            favoriteline.remove(id);
            tweetIndex.getFavoritesCount().decr();
        }
    }
}
