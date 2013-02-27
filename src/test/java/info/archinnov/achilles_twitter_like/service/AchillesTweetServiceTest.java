package info.archinnov.achilles_twitter_like.service;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * TweetAchillesServiceTest
 * 
 * @author DuyHai DOAN
 * 
 */

@RunWith(MockitoJUnitRunner.class)
public class AchillesTweetServiceTest
{
	@InjectMocks
	private AchillesTweetService service = new AchillesTweetService();

	@Test
	public void should_extract_tags_from_tweet_content() throws Exception
	{

		String content = "This is a sample tweet for #Achilles and #Cassandra";

		Set<String> tags = service.extractTag(content);

		assertThat(tags).hasSize(2);
		assertThat(tags).contains("Achilles", "Cassandra");

	}
}
