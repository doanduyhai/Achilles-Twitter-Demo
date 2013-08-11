package info.archinnov.achilles.demo.twitter.service;

import info.archinnov.achilles.demo.twitter.entity.Tweet;
import info.archinnov.achilles.demo.twitter.entity.compound.TweetIndexKey;
import info.archinnov.achilles.demo.twitter.entity.compound.TweetKey;
import info.archinnov.achilles.demo.twitter.entity.index.TweetFavoriteLineIndex;
import info.archinnov.achilles.demo.twitter.entity.line.tweet.FavoriteLine;
import info.archinnov.achilles.entity.manager.ThriftEntityManager;
import java.util.UUID;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    @Inject
    private UserService userService;

    @Inject
    private TweetService tweetService;

    @Inject
    private ThriftEntityManager em;

    public void addTweetToFavorite(String userLogin, String tweetId) {

        Tweet tweet = tweetService.fetchTweetIndex(tweetId);
        UUID id = UUID.fromString(tweetId);

        FavoriteLine favoriteLine = em.find(FavoriteLine.class, new TweetKey(userLogin, id));

        if (favoriteLine == null) {
            em.persist(new FavoriteLine(userLogin, tweet.getTweet()));
            em.persist(new TweetFavoriteLineIndex(id, userLogin));
            tweet.getFavoritesCount().incr();
        }
    }

    public void removeTweetFromFavorite(String userLogin, String tweetId) {

        Tweet tweet = tweetService.fetchTweetIndex(tweetId);
        UUID id = tweet.getId();

        FavoriteLine favoriteLine = em.find(FavoriteLine.class, new TweetKey(userLogin, id));

        if (favoriteLine != null) {
            em.remove(favoriteLine);
            em.removeById(TweetFavoriteLineIndex.class, new TweetIndexKey(id, userLogin));
            tweet.getFavoritesCount().decr();
        }
    }
}
