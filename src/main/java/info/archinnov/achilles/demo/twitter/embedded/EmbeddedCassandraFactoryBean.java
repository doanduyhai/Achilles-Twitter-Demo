package info.archinnov.achilles.demo.twitter.embedded;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import info.archinnov.achilles.embedded.CQLEmbeddedServerBuilder;
import info.archinnov.achilles.entity.manager.CQLPersistenceManager;

public class EmbeddedCassandraFactoryBean extends AbstractFactoryBean<CQLPersistenceManager> {
    private static CQLPersistenceManager manager;

    static {
        manager = CQLEmbeddedServerBuilder
                .withEntityPackages("info.archinnov.achilles.demo.twitter.entity")
                .withKeyspaceName("achilles_twitter")
                .cleanDataFilesAtStartup()
                .withCQLPort(9041)
                .buildPersistenceManager();
    }

    @Override
    public Class<?> getObjectType() {
        return CQLPersistenceManager.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    protected CQLPersistenceManager createInstance() throws Exception {
        return manager;
    }
}
