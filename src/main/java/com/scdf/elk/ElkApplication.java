package com.scdf.elk;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.json.ElkGraphJson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

@SpringBootApplication
public class ElkApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElkApplication.class, args);
	}

//	@Bean
//	public HttpMessageConverters messageConvertersCustom(ObjectProvider<HttpMessageConverter<?>> converters) {
//		return new HttpMessageConverters(elkConverter());
//	}

	
	@Bean
    public HttpMessageConverter<ElkNode> elkConverter() {
	    AbstractHttpMessageConverter<ElkNode> messageConverter = new AbstractHttpMessageConverter<ElkNode>() {
	    	
	    	

			@Override
			protected boolean supports(Class<?> clazz) {
				return ElkNode.class.isAssignableFrom(clazz);
			}

			@Override
			protected ElkNode readInternal(Class<? extends ElkNode> clazz, HttpInputMessage inputMessage)
					throws IOException, HttpMessageNotReadableException {
				String graphStr = IOUtils.toString(inputMessage.getBody());				
				return ElkGraphJson.forGraph(graphStr).toElk();
			}

			@Override
			protected void writeInternal(ElkNode root, HttpOutputMessage outputMessage)
					throws IOException, HttpMessageNotWritableException {
				String json = ElkGraphJson.forGraph(root)
						.omitLayout(false)
						.omitZeroDimension(false)
						.omitZeroPositions(false)
                        .shortLayoutOptionKeys(false)
                        .prettyPrint(true)
                        .toJson();
				outputMessage.getBody().write(json.getBytes());
			}
	    };
	    
	    messageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
		return messageConverter;
	}

}
