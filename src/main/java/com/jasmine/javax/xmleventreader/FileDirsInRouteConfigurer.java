package com.jasmine.javax.xmleventreader;

import org.apache.camel.spring.SpringCamelContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class FileDirsInRouteConfigurer implements ApplicationContextAware {

	private String fileDirs;
	private String inputfileregex;
	private ApplicationContext context;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.context=applicationContext;

	}
	
	public FileDirsInRouteConfigurer(String fileDirs, String inputfileregex) {
		super();
		this.fileDirs = fileDirs;
		this.inputfileregex = inputfileregex;
	}

	public void init() throws Exception{
		SpringCamelContext springCamelContext = context.getBean(SpringCamelContext.class);
		String[] dirs = fileDirs.split(",");
		for(int i=0; i<dirs.length; i++){
			springCamelContext.addRoutes(new FileDirsInRouteBuilder(dirs[i], inputfileregex));
		}
	}

}
