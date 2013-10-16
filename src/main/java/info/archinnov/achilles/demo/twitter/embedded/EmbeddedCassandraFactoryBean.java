package info.archinnov.achilles.demo.twitter.embedded;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import info.archinnov.achilles.entity.manager.CQLPersistenceManager;
import info.archinnov.achilles.junit.AchillesCQLResource;
import info.archinnov.achilles.junit.AchillesCQLResourceBuilder;

public class EmbeddedCassandraFactoryBean extends AbstractFactoryBean<CQLPersistenceManager> {
    private static CQLPersistenceManager manager;

    private void initialize() {
        final AchillesCQLResource cqlResource = AchillesCQLResourceBuilder
                .withEntityPackages("info.archinnov.achilles.demo.twitter.entity")
                .build();

        manager = cqlResource.getPersistenceManager();
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
        synchronized (this) {
            if (manager == null) {
                initialize();
            }
        }
        return manager;
    }
}
