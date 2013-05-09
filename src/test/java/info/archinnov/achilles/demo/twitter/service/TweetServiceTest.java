package info.archinnov.achilles.demo.twitter.service;

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
public class TweetServiceTest {
    @InjectMocks
    private TweetService service = new TweetService();

    //	@Test
    //	public void should_extract_tags_from_tweet_content() throws Exception
    //	{
    //
    //		String content = "This is a sample tweet for #Achilles and #Cassandra";
    //
    //		Set<String> tags = service.extractTag(content);
    //
    //		assertThat(tags).hasSize(2);
    //		assertThat(tags).contains("Achilles", "Cassandra");
    //
    //	}
}
