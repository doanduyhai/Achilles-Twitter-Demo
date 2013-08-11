package info.archinnov.achilles.demo.twitter.service;

import info.archinnov.achilles.demo.twitter.entity.line.tweet.AbstractTweetLine;
import info.archinnov.achilles.demo.twitter.entity.line.tweet.FavoriteLine;
import info.archinnov.achilles.demo.twitter.entity.line.tweet.MentionLine;
import info.archinnov.achilles.demo.twitter.entity.line.tweet.TagLine;
import info.archinnov.achilles.demo.twitter.entity.line.tweet.TimeLine;
import info.archinnov.achilles.demo.twitter.entity.line.tweet.UserLine;
import info.archinnov.achilles.demo.twitter.model.Tweet;
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

    private Function<AbstractTweetLine, Tweet> lineToTweet = new Function<AbstractTweetLine, Tweet>() {

        @Override
        public Tweet apply(AbstractTweetLine line)
        {
            return line.getTweet();
        }
    };

    private Function<TagLine, Tweet> tagLineToTweet = new Function<TagLine, Tweet>() {

        @Override
        public Tweet apply(TagLine line)
        {
            return line.getTweet();
        }
    };

    public List<Tweet> getTimeline(String userLogin, int length)
    {
        List<TimeLine> timeline = fetchData(TimeLine.class, userLogin, length);

        return FluentIterable
                .from(timeline)
                .transform(lineToTweet)
                .toImmutableList();
    }

    public List<Tweet> getUserline(String userLogin, int length)
    {
        List<UserLine> userline = fetchData(UserLine.class, userLogin, length);

        return FluentIterable
                .from(userline)
                .transform(lineToTweet)
                .toImmutableList();
    }

    public List<Tweet> getFavoriteLine(String userLogin, int length)
    {
        List<FavoriteLine> favoriteline = fetchData(FavoriteLine.class, userLogin, length);

        return FluentIterable
                .from(favoriteline)
                .transform(lineToTweet)
                .toImmutableList();
    }

    public List<Tweet> getMentionLine(String userLogin, int length)
    {
        List<MentionLine> mentionline = fetchData(MentionLine.class, userLogin, length);

        return FluentIterable
                .from(mentionline)
                .transform(lineToTweet)
                .toImmutableList();
    }

    public List<Tweet> getTagLine(String tag, int length)
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
