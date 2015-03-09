package com.jasmine.javax.xmleventreader;

import java.util.Queue;

import javax.xml.stream.events.XMLEvent;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class XMLEventsProcessor implements Processor, ApplicationContextAware {

	private ApplicationContext context;
	
	public void process(Exchange exchange) throws Exception {
		com.jasmine.javax.xmleventreader.XMLEventConverter XMLEventConverter = context.getBean("XMLEventConverter", XMLEventConverter.class);
		exchange.getIn().setBody(XMLEventConverter.process((Queue<XMLEvent>) exchange.getIn().getBody()));
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.context=applicationContext;
		
	}

}
