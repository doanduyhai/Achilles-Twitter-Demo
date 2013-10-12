package info.archinnov.achilles.demo.twitter.service;

import javax.inject.Inject;
import org.springframework.stereotype.Service;
import com.datastax.driver.core.Session;
import info.archinnov.achilles.entity.manager.CQLPersistenceManager;

@Service
public class DbService {

    @Inject
    private CQLPersistenceManager manager;

    public void resetDb() {

        final Session session = manager.getNativeSession();
        session.execute("TRUNCATE user");
        session.execute("TRUNCATE user_relation");
        session.execute("TRUNCATE tweet_line");
        session.execute("TRUNCATE tweet_index");
        session.execute("TRUNCATE tweet");
        session.execute("TRUNCATE followers_login");
    }
}
