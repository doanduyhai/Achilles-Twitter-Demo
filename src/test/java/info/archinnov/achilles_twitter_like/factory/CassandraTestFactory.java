package info.archinnov.achilles_twitter_like.factory;

import info.archinnov.achilles.configuration.ConfigurationParameters;
import info.archinnov.achilles.consistency.AchillesConfigurableConsistencyLevelPolicy;
import info.archinnov.achilles.dao.GenericEntityDao;
import info.archinnov.achilles.dao.GenericWideRowDao;
import info.archinnov.achilles.entity.manager.ThriftEntityManager;
import info.archinnov.achilles.entity.manager.ThriftEntityManagerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.factory.HFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.cassandraunit.DataLoader;
import org.cassandraunit.dataset.json.ClassPathJsonDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CassandraDaoTest
 * 
 * @author DuyHai DOAN
 * 
 */
public abstract class CassandraTestFactory
{

	private static final String CASSANDRA_TEST_YAML_CONFIG_TEMPLATE = "src/test/resources/cassandraUnitConfig.yaml";
	private static final String CASSANDRA_TEST_COLUMN_FAMILIES_CONFIG = "columnFamilies.json";
	private static final String CASSANDRA_TEST_CLUSTER_NAME = "Achilles Test Cassandra Cluster";
	private static final String CASSANDRA_KEYSPACE_NAME = "achilles";
	public static final String CASSANDRA_TEST_HOST = "localhost";
	public static final String CASSANDRA_HOST = "cassandraHost";
	public static int CASSANDRA_TEST_PORT = 9160;
	private static final String ENTITY_PACKAGE = "integration.tests.entity";
	private static Cluster cluster;

	private static Keyspace keyspace;

	public static final Logger log = LoggerFactory.getLogger(CassandraTestFactory.class);

	private static ThriftEntityManager em;
	private static AchillesConfigurableConsistencyLevelPolicy policy;

	static
	{
		String cassandraHost = System.getProperty(CASSANDRA_HOST);
		if (StringUtils.isNotBlank(cassandraHost) && cassandraHost.contains(":"))
		{
			CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(
					cassandraHost);
			cluster = HFactory.getOrCreateCluster("achilles", hostConfigurator);
			keyspace = HFactory.createKeyspace(CASSANDRA_KEYSPACE_NAME, cluster);
		}
		else
		{

			try
			{
				final File temporaryCassandraYaml = prepareEmbeddedCassandraConfig();

				EmbeddedCassandraServerHelper.startEmbeddedCassandra(temporaryCassandraYaml,
						EmbeddedCassandraServerHelper.DEFAULT_TMP_DIR);
				Runtime.getRuntime().addShutdownHook(new Thread()
				{
					@Override
					public void run()
					{
						FileUtils.deleteQuietly(temporaryCassandraYaml);
						log.info("Shutting down cassandra");
					}
				});

			}
			catch (Exception e1)
			{
				throw new IllegalStateException("Cannot start cassandra embedded", e1);
			}
			DataLoader dataLoader = new DataLoader(CASSANDRA_TEST_CLUSTER_NAME, CASSANDRA_TEST_HOST
					+ ":" + CASSANDRA_TEST_PORT);
			dataLoader.load(new ClassPathJsonDataSet(CASSANDRA_TEST_COLUMN_FAMILIES_CONFIG));

			cluster = HFactory.getOrCreateCluster("Achilles-cluster", CASSANDRA_TEST_HOST + ":"
					+ CASSANDRA_TEST_PORT);
			keyspace = HFactory.createKeyspace(CASSANDRA_KEYSPACE_NAME, cluster);
		}
		Map<String, Object> configMap = new HashMap<String, Object>();
		configMap.put(ConfigurationParameters.CLUSTER_PARAM, cluster);
		configMap.put(ConfigurationParameters.KEYSPACE_PARAM, keyspace);
		configMap.put(ConfigurationParameters.ENTITY_PACKAGES_PARAM, ENTITY_PACKAGE);
		configMap.put(ConfigurationParameters.FORCE_CF_CREATION_PARAM, true);

		ThriftEntityManagerFactory factory = new ThriftEntityManagerFactory(configMap);
		policy = Whitebox.getInternalState(em, "consistencyPolicy");
		em = (ThriftEntityManager) factory.createEntityManager();
	}

	private static File prepareEmbeddedCassandraConfig() throws IOException
	{

		File cassandraYamlTemplate = new File(CASSANDRA_TEST_YAML_CONFIG_TEMPLATE);
		File temporaryCassandraYaml = File.createTempFile("testCassandra-", ".yaml");
		FileUtils.copyFile(cassandraYamlTemplate, temporaryCassandraYaml);

		log.info(" Temporary cassandra.yaml file = {}", temporaryCassandraYaml.getAbsolutePath());

		String storagePort = "storage_port: " + (7001 + RandomUtils.nextInt(990));
		CASSANDRA_TEST_PORT = 9161 + RandomUtils.nextInt(800);
		String rcpPort = "rpc_port: " + CASSANDRA_TEST_PORT;
		log.info(" Random embedded Cassandra RPC port = {}", CASSANDRA_TEST_PORT);
		FileUtils.writeLines(temporaryCassandraYaml, Arrays.asList(storagePort, rcpPort), true);

		return temporaryCassandraYaml;
	}

	public static Cluster getCluster()
	{
		return cluster;
	}

	public static Keyspace getKeyspace()
	{
		return keyspace;
	}

	public static ThriftEntityManager getEm()
	{
		return em;
	}

	public static <K> GenericEntityDao<K> getEntityDao(Serializer<K> keySerializer,
			String columnFamily)
	{
		return new GenericEntityDao<K>(cluster, keyspace, keySerializer, columnFamily, policy);
	}

	public static <K, V> GenericWideRowDao<K, V> getCompositeDao(Serializer<K> keySerializer,
			Serializer<V> valueSerializer, String columnFamily)
	{
		return new GenericWideRowDao<K, V>(cluster, keyspace, keySerializer, valueSerializer,
				columnFamily, policy);
	}
}
