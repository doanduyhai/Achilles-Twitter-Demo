package info.archinnov.achilles.demo.twitter.json;

import javax.annotation.PostConstruct;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

/**
 * CustomJacksonMessageConverter
 * 
 * @author DuyHai DOAN
 * 
 */
public class CustomJacksonMessageConverter extends MappingJacksonHttpMessageConverter
{
	@Autowired
	private ObjectMapper mapper;

	@PostConstruct
	public void init()
	{
		super.setObjectMapper(mapper);
	}
}
