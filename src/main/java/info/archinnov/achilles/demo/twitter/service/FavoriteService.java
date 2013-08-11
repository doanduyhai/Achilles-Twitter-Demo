package info.archinnov.achilles.demo.twitter.service;

import static info.archinnov.achilles.demo.twitter.entity.compound.TweetKey.LineType.FAVORITELINE;
import info.archinnov.achilles.demo.twitter.entity.Tweet;
import info.archinnov.achilles.demo.twitter.entity.TweetIndex;
import info.archinnov.achilles.demo.twitter.entity.TweetLine;
import info.archinnov.achilles.demo.twitter.entity.compound.TweetIndexKey;
import info.archinnov.achilles.demo.twitter.entity.compound.TweetKey;
import info.archinnov.achilles.entity.manager.CQLEntityManager;
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
    private CQLEntityManager em;

    public void addTweetToFavorite(String userLogin, String tweetId) {

        Tweet tweet = tweetService.fetchTweetIndex(tweetId);
        UUID id = UUID.fromString(tweetId);

        TweetLine favoriteLine = em.find(TweetLine.class, new TweetKey(userLogin, FAVORITELINE, id));

        if (favoriteLine == null) {
            em.persist(new TweetLine(userLogin, FAVORITELINE, tweet.getTweetModel()));
            em.persist(new TweetIndex(id, FAVORITELINE, userLogin));
            tweet.getFavoritesCount().incr();
        }
    }

    public void removeTweetFromFavorite(String userLogin, String tweetId) {

        Tweet tweet = tweetService.fetchTweetIndex(tweetId);
        UUID id = tweet.getId();

        TweetLine favoriteLine = em.find(TweetLine.class, new TweetKey(userLogin, FAVORITELINE, id));

        if (favoriteLine != null) {
            em.remove(favoriteLine);
            em.removeById(TweetIndex.class, new TweetIndexKey(id, FAVORITELINE, userLogin));
            tweet.getFavoritesCount().decr();
        }
    }
}
