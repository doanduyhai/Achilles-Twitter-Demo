package info.archinnov.achilles.demo.twitter.embedded;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import info.archinnov.achilles.embedded.CassandraEmbeddedServerBuilder;
import info.archinnov.achilles.persistence.PersistenceManager;

public class EmbeddedCassandraFactoryBean extends AbstractFactoryBean<PersistenceManager> {
    private static PersistenceManager manager;

    static {
        manager = CassandraEmbeddedServerBuilder
                .withEntityPackages("info.archinnov.achilles.demo.twitter.entity")
                .withKeyspaceName("achilles_twitter")
                .cleanDataFilesAtStartup(true)
                .withCQLPort(9041)
                .buildPersistenceManager();
    }

    @Override
    public Class<?> getObjectType() {
        return PersistenceManager.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    protected PersistenceManager createInstance() throws Exception {
        return manager;
    }
}
