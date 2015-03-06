package com.jasmine.javax.xmleventreader;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class XMLEventsToByteArray implements ApplicationContextAware{

	private ApplicationContext context;
	
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.context=context;
	}	
	public byte[] convert(List<XMLEvent> xmlEvents) throws XMLStreamException{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		XMLEventWriter xmlEventWriter=(XMLEventWriter) context.getBean("xmlEventWriter", outputStream);
		for(XMLEvent xmlEvent: xmlEvents)
			xmlEventWriter.add(xmlEvent);
		xmlEventWriter.close();
		return outputStream.toByteArray();
	}

}
