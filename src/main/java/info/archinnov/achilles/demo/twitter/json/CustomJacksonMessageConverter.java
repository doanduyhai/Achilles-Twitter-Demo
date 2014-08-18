package info.archinnov.achilles.demo.twitter.json;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

/**
 * CustomJacksonMessageConverter
 * 
 * @author DuyHai DOAN
 * 
 */
public class CustomJacksonMessageConverter extends MappingJackson2HttpMessageConverter
{
	@Autowired
	private ObjectMapper mapper;

	@PostConstruct
	public void init()
	{
		super.setObjectMapper(mapper);
	}
}
