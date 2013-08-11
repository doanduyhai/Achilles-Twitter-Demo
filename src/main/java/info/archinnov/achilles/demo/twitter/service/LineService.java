package info.archinnov.achilles.demo.twitter.service;

import info.archinnov.achilles.demo.twitter.entity.line.tweet.AbstractTweetLine;
import info.archinnov.achilles.demo.twitter.entity.line.tweet.FavoriteLine;
import info.archinnov.achilles.demo.twitter.entity.line.tweet.MentionLine;
import info.archinnov.achilles.demo.twitter.entity.line.tweet.TagLine;
import info.archinnov.achilles.demo.twitter.entity.line.tweet.TimeLine;
import info.archinnov.achilles.demo.twitter.entity.line.tweet.UserLine;
import info.archinnov.achilles.demo.twitter.model.TweetModel;
import info.archinnov.achilles.entity.manager.ThriftEntityManager;
import info.archinnov.achilles.type.OrderingMode;
import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

@Service
public class LineService
{

    @Inject
    private UserService userService;

    @Inject
    private ThriftEntityManager em;

    private Function<AbstractTweetLine, TweetModel> lineToTweet = new Function<AbstractTweetLine, TweetModel>() {

        @Override
        public TweetModel apply(AbstractTweetLine line)
        {
            return line.getTweet();
        }
    };

    private Function<TagLine, TweetModel> tagLineToTweet = new Function<TagLine, TweetModel>() {

        @Override
        public TweetModel apply(TagLine line)
        {
            return line.getTweet();
        }
    };

    public List<TweetModel> getTimeline(String userLogin, int length)
    {
        List<TimeLine> timeline = fetchData(TimeLine.class, userLogin, length);

        return FluentIterable
                .from(timeline)
                .transform(lineToTweet)
                .toImmutableList();
    }

    public List<TweetModel> getUserline(String userLogin, int length)
    {
        List<UserLine> userline = fetchData(UserLine.class, userLogin, length);

        return FluentIterable
                .from(userline)
                .transform(lineToTweet)
                .toImmutableList();
    }

    public List<TweetModel> getFavoriteLine(String userLogin, int length)
    {
        List<FavoriteLine> favoriteline = fetchData(FavoriteLine.class, userLogin, length);

        return FluentIterable
                .from(favoriteline)
                .transform(lineToTweet)
                .toImmutableList();
    }

    public List<TweetModel> getMentionLine(String userLogin, int length)
    {
        List<MentionLine> mentionline = fetchData(MentionLine.class, userLogin, length);

        return FluentIterable
                .from(mentionline)
                .transform(lineToTweet)
                .toImmutableList();
    }

    public List<TweetModel> getTagLine(String tag, int length)
    {
        List<TagLine> tagline = fetchData(TagLine.class, tag, length);

        return FluentIterable
                .from(tagline)
                .transform(tagLineToTweet)
                .toImmutableList();

    }

    private <T> List<T> fetchData(Class<T> lineClass, String partitionKey, int length) {
        List<T> timeline = em.sliceQuery(lineClass)
                .partitionKey(partitionKey)
                .ordering(OrderingMode.DESCENDING)
                .get(length);
        return timeline;
    }
}
