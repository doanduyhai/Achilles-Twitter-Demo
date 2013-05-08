package info.archinnov.achilles.demo.twitter.json;

import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.springframework.beans.factory.FactoryBean;

/**
 * ObjectMapperFactory
 * 
 * @author DuyHai DOAN
 * 
 */
public class ObjectMapperFactoryBean implements FactoryBean<ObjectMapper>
{
	private ObjectMapper objectMapper = null;

	public ObjectMapperFactoryBean() {
		this.objectMapper = new ObjectMapper();
		this.objectMapper.setSerializationInclusion(Inclusion.NON_NULL);
		this.objectMapper
				.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		AnnotationIntrospector primary = new JacksonAnnotationIntrospector();
		AnnotationIntrospector secondary = new JaxbAnnotationIntrospector();
		AnnotationIntrospector pair = new AnnotationIntrospector.Pair(primary, secondary);

		this.objectMapper.setAnnotationIntrospector(pair);
	}

	@Override
	public ObjectMapper getObject() throws Exception
	{
		return objectMapper;
	}

	@Override
	public Class<?> getObjectType()
	{
		return ObjectMapper.class;
	}

	@Override
	public boolean isSingleton()
	{
		return true;
	}

}
