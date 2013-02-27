package info.archinnov.achilles_twitter_like.factory;

import static org.apache.commons.lang.StringUtils.isBlank;
import info.archinnov.achilles.entity.manager.ThriftEntityManager;
import info.archinnov.achilles.entity.manager.ThriftEntityManagerFactoryImpl;

import javax.annotation.PostConstruct;

import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;

import org.springframework.beans.factory.FactoryBean;

/**
 * AchillesEntityManagerFactory
 * 
 * @author DuyHai DOAN
 * 
 */
public class AchillesEntityManagerFactory implements FactoryBean<ThriftEntityManager>
{

	private Cluster cluster;
	private Keyspace keyspace;

	private String cassandraHost;
	private String clusterName;
	private String keyspaceName;
	private String entityPackages;
	private boolean forceColumnFamilyCreation = false;

	private ThriftEntityManager em;

	@PostConstruct
	public void initialize()
	{
		Cluster cluster;
		Keyspace keyspace;
		if (this.cluster != null)
		{
			cluster = this.cluster;
		}
		else
		{
			if (isBlank(cassandraHost) || isBlank(clusterName))
			{
				throw new IllegalArgumentException(
						"Either a Cassandra cluster or hostname:port & clusterName should be provided");
			}
			cluster = HFactory.getOrCreateCluster(clusterName, new CassandraHostConfigurator(
					cassandraHost));
		}

		if (this.keyspace != null)
		{
			keyspace = this.keyspace;
		}
		else
		{
			if (isBlank(keyspaceName))
			{
				throw new IllegalArgumentException(
						"Either a Cassandra keyspace or keyspaceName should be provided");
			}
			keyspace = HFactory.createKeyspace(keyspaceName, cluster);
		}

		if (isBlank(entityPackages))
		{
			throw new IllegalArgumentException(
					"Entity packages should be provided for entity scanning");
		}
		ThriftEntityManagerFactoryImpl factory = new ThriftEntityManagerFactoryImpl(cluster,
				keyspace, entityPackages, forceColumnFamilyCreation);
		em = (ThriftEntityManager) factory.createEntityManager();
	}

	@Override
	public ThriftEntityManager getObject() throws Exception
	{
		return em;
	}

	@Override
	public Class<?> getObjectType()
	{
		return ThriftEntityManager.class;
	}

	@Override
	public boolean isSingleton()
	{
		return true;
	}

	public void setCluster(Cluster cluster)
	{
		this.cluster = cluster;
	}

	public void setKeyspace(Keyspace keyspace)
	{
		this.keyspace = keyspace;
	}

	public void setCassandraHost(String cassandraHost)
	{
		this.cassandraHost = cassandraHost;
	}

	public void setClusterName(String clusterName)
	{
		this.clusterName = clusterName;
	}

	public void setKeyspaceName(String keyspaceName)
	{
		this.keyspaceName = keyspaceName;
	}

	public void setEntityPackages(String entityPackages)
	{
		this.entityPackages = entityPackages;
	}

	public void setForceColumnFamilyCreation(boolean forceColumnFamilyCreation)
	{
		this.forceColumnFamilyCreation = forceColumnFamilyCreation;
	}
}