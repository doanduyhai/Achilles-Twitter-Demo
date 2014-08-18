package info.archinnov.achilles.demo.twitter.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

import org.springframework.beans.factory.FactoryBean;

/**
 * ObjectMapperFactory
 * 
 * @author DuyHai DOAN
 * 
 */
public class ObjectMapperFactoryBean implements FactoryBean<ObjectMapper> {
	private ObjectMapper objectMapper = null;

	public ObjectMapperFactoryBean() {
		this.objectMapper = new ObjectMapper();
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        AnnotationIntrospector primary = new com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector();
        AnnotationIntrospector secondary = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
        this.objectMapper.setAnnotationIntrospector(com.fasterxml.jackson.databind.AnnotationIntrospector.pair(primary, secondary));
	}

	@Override
	public ObjectMapper getObject() throws Exception {
		return objectMapper;
	}

	@Override
	public Class<?> getObjectType() {
		return ObjectMapper.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
