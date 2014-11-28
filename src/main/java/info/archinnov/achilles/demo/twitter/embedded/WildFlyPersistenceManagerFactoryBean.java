package info.archinnov.achilles.demo.twitter.embedded;

import com.datastax.driver.core.Cluster;
import info.archinnov.achilles.persistence.PersistenceManager;
import info.archinnov.achilles.persistence.PersistenceManagerFactory;
import info.archinnov.achilles.type.ConsistencyLevel;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Created by ehsavoie on 27/11/14.
 */
public class WildFlyPersistenceManagerFactoryBean extends AbstractFactoryBean<PersistenceManager> {

    private static PersistenceManagerFactory pmFactory = PersistenceManagerFactory.PersistenceManagerFactoryBuilder.builder(
            Cluster.builder().addContactPoints("127.0.0.1").withPort(9042).withClusterName("achilles").build())
            .withEntityPackages("info.archinnov.achilles.demo.twitter.entity")
            .withDefaultReadConsistency(ConsistencyLevel.ONE)
            .withDefaultWriteConsistency(ConsistencyLevel.ONE)
            .withKeyspaceName("achilles")
            .withExecutorServiceMinThreadCount(5)
            .withExecutorServiceMaxThreadCount(10)
            .forceTableCreation(true)
            .withJacksonMapper(new info.archinnov.achilles.demo.twitter.json.ObjectMapperFactoryBean().getObject())
            .build();
    private static final PersistenceManager manager = pmFactory.createPersistenceManager();

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
