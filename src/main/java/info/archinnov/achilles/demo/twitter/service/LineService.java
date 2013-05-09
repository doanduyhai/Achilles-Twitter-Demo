package info.archinnov.achilles.demo.twitter.service;

import info.archinnov.achilles.demo.twitter.entity.widerow.FavoriteLine;
import info.archinnov.achilles.demo.twitter.entity.widerow.MentionLine;
import info.archinnov.achilles.demo.twitter.entity.widerow.TagLine;
import info.archinnov.achilles.demo.twitter.entity.widerow.TimeLine;
import info.archinnov.achilles.demo.twitter.entity.widerow.UserLine;
import info.archinnov.achilles.demo.twitter.model.Tweet;
import info.archinnov.achilles.entity.manager.ThriftEntityManager;
import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

@Service
public class LineService {

    @Inject
    private UserService userService;

    @Inject
    private ThriftEntityManager em;

    public List<Tweet> getTimeline(String userLogin, int length) {
        TimeLine wideRow = em.find(TimeLine.class, userLogin);
        return wideRow.getTimeline().findLastValues(length);
    }

    public List<Tweet> getUserline(String userLogin, int lenth) {
        UserLine wideRow = em.find(UserLine.class, userLogin);
        return wideRow.getUserline().findLastValues(lenth);
    }

    public List<Tweet> getFavoriteLine(String userLogin, int length) {
        FavoriteLine wideRow = em.find(FavoriteLine.class, userLogin);
        return wideRow.getFavoriteline().findLastValues(length);
    }

    public List<Tweet> getMentionLine(String userLogin, int length) {
        MentionLine wideRow = em.find(MentionLine.class, userLogin);
        return wideRow.getMentionline().findLastValues(length);
    }

    public List<Tweet> getTagLine(String tag, int length) {
        TagLine tagLine = em.find(TagLine.class, tag);
        return tagLine.getTagline().findLastValues(length);
    }

}
