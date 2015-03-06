package com.jasmine.javax.xmleventreader;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class InputStreamToXMLEvents implements ApplicationContextAware{

	

	private ApplicationContext context;
	
	public Queue<XMLEvent> getAllXMLEvents(InputStream is) throws XMLStreamException{
		XMLEventReader xmlEventReader=
		context.getBean(XMLEventReader.class, is);
		
		
		Queue<XMLEvent> eventQueue=new LinkedList<XMLEvent>();
		while (xmlEventReader.hasNext()) {
			eventQueue.add(xmlEventReader.nextEvent());
		}
		return eventQueue;
	}

	
	
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.context=context;
	}

	
	
}
