package com.in28minutes.soap.webservices.soapcoursemanagement.soap;

import java.util.Collections;
import java.util.List;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.security.xwss.XwsSecurityInterceptor;
import org.springframework.ws.soap.security.xwss.callback.SimplePasswordValidationCallbackHandler;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

//Enable Web Services
@EnableWs
//Spring Configuration
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter{

	//Message Dispatcher Servlet
		//App Context
	//url -> /ws/*
	
	@Bean
	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext appContext) {
		MessageDispatcherServlet messageDispatcherServlet = new MessageDispatcherServlet();
		messageDispatcherServlet.setApplicationContext(appContext);
		messageDispatcherServlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean<>(messageDispatcherServlet, "/ws/*");
	}
	
	//  /ws/courses.wsdl
		//Port Type - CoursePort
		//Namespace - http://in28minutes.com/courses
	// course-details.xsd
	@Bean(name="courses")
	public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema courseSchema) {
		DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
		definition.setPortTypeName("CoursePort");
		definition.setTargetNamespace("http://in28minutes.com/courses");
		definition.setLocationUri("/ws");
		definition.setSchema(courseSchema);
		return definition;
	}
	
	@Bean
	public XsdSchema courseSchema() {
		return new SimpleXsdSchema(new ClassPathResource("course-details.xsd"));
	}
	
	@Bean
	public XwsSecurityInterceptor securityInterceptor() {
		XwsSecurityInterceptor xwsSecurityInterceptor = new XwsSecurityInterceptor();
		xwsSecurityInterceptor.setCallbackHandler(callbackHandler());
		xwsSecurityInterceptor.setPolicyConfiguration(new ClassPathResource("securityPolicy.xml"));
		//Callback Handler -> Simple PasswordValidationCallbackHandler
		//Security Policy -> security.xml
		return xwsSecurityInterceptor;
	}
	
	@Bean
	public SimplePasswordValidationCallbackHandler callbackHandler() {
		SimplePasswordValidationCallbackHandler sPVCallbackHandler  = new SimplePasswordValidationCallbackHandler();
		sPVCallbackHandler.setUsersMap(Collections.singletonMap("user", "password"));
		return sPVCallbackHandler;
	}

	@Override
	public void addInterceptors(List<EndpointInterceptor> interceptors) {
		interceptors.add(securityInterceptor());
	}
	//Interceptors.add -> XwsSecurityInterceptor
}
