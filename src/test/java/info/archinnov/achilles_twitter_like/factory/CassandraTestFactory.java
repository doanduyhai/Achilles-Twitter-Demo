package info.archinnov.achilles_twitter_like.factory;

import info.archinnov.achilles.dao.GenericCompositeDao;
import info.archinnov.achilles.dao.GenericDynamicCompositeDao;
import info.archinnov.achilles.entity.manager.ThriftEntityManager;
import info.archinnov.achilles.entity.manager.ThriftEntityManagerFactoryImpl;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

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
		ThriftEntityManagerFactoryImpl factory = new ThriftEntityManagerFactoryImpl(getCluster(),
				getKeyspace(), ENTITY_PACKAGE, true);
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

	public static <K> GenericDynamicCompositeDao<K> getDynamicCompositeDao(
			Serializer<K> keySerializer, String columnFamily)
	{
		return new GenericDynamicCompositeDao<K>(keyspace, keySerializer, columnFamily);
	}

	public static <K, V> GenericCompositeDao<K, V> getCompositeDao(Serializer<K> keySerializer,
			Serializer<V> valueSerializer, String columnFamily)
	{
		return new GenericCompositeDao<K, V>(keyspace, keySerializer, valueSerializer, columnFamily);
	}
}
