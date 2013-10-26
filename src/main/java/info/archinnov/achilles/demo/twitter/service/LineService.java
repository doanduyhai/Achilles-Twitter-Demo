package info.archinnov.achilles.demo.twitter.service;

import static info.archinnov.achilles.demo.twitter.entity.compound.TweetKey.LineType.*;
import info.archinnov.achilles.demo.twitter.entity.TweetLine;
import info.archinnov.achilles.demo.twitter.entity.compound.TweetKey.LineType;
import info.archinnov.achilles.demo.twitter.model.TweetModel;
import info.archinnov.achilles.entity.manager.CQLPersistenceManager;
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
    private CQLPersistenceManager manager;

    private Function<TweetLine, TweetModel> lineToTweet = new Function<TweetLine, TweetModel>() {

        @Override
        public TweetModel apply(TweetLine line)
        {
            return line.getTweetModel();
        }
    };

    public List<TweetModel> getTimeline(String userLogin, int length)
    {
        List<TweetLine> timeline = fetchData(userLogin, TIMELINE, length);

        return FluentIterable
                .from(timeline)
                .transform(lineToTweet)
                .toImmutableList();
    }

    public List<TweetModel> getUserline(String userLogin, int length)
    {
        List<TweetLine> userline = fetchData(userLogin, USERLINE, length);

        return FluentIterable
                .from(userline)
                .transform(lineToTweet)
                .toImmutableList();
    }

    public List<TweetModel> getFavoriteLine(String userLogin, int length)
    {
        List<TweetLine> favoriteline = fetchData(userLogin, FAVORITELINE, length);

        return FluentIterable
                .from(favoriteline)
                .transform(lineToTweet)
                .toImmutableList();
    }

    public List<TweetModel> getMentionLine(String userLogin, int length)
    {
        List<TweetLine> mentionline = fetchData(userLogin, MENTIONLINE, length);

        return FluentIterable
                .from(mentionline)
                .transform(lineToTweet)
                .toImmutableList();
    }

    public List<TweetModel> getTagLine(String tag, int length)
    {
        List<TweetLine> tagline = fetchData(tag, TAGLINE, length);

        return FluentIterable
                .from(tagline)
                .transform(lineToTweet)
                .toImmutableList();

    }

    private List<TweetLine> fetchData(String partitionKey, LineType type, int length) {
        List<TweetLine> line = manager.sliceQuery(TweetLine.class)
                .partitionComponents(partitionKey)
                .fromClusterings(type)
                .toClusterings(type)
                .ordering(OrderingMode.DESCENDING)
                .get(length);
        return line;
    }
}
