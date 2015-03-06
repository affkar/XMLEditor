package com.jasmine.javax.xmleventreader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.DTD;
import javax.xml.stream.events.XMLEvent;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class XMLEventProcessor implements Processor,ApplicationContextAware {

	private ApplicationContext context;
	
	private static final String THIS_IS_DOCUMENTATION_ADDED_BY_CODE = "This is documentation added by code.";

	private QName qNameDocumentation = new QName(
			"http://schemas.xmlsoap.org/wsdl/", "documentation");
	
	private XMLEvent currentElement;
	private XMLEvent nextEvent;
	
	public Collection<XMLEvent> process(Queue<XMLEvent> xmlEvents) throws Exception {
		Collection<XMLEvent> xmlEventsToReturn=new ArrayList<XMLEvent>();
		
		XMLEventFactory xmlEventFactory = context.getBean("XMLEventFactory", XMLEventFactory.class);
		DTD newLine=context.getBean("newLineDTD", DTD.class);
		DTD tab=context.getBean("tabDTD",DTD.class);
		
		while(!xmlEvents.isEmpty()){
			currentElement = xmlEvents.poll();
			nextEvent=null;
			if(!xmlEvents.isEmpty())
				nextEvent = xmlEvents.peek();
			
			xmlEventsToReturn.add(currentElement);
			if (currentElement.getEventType() == XMLStreamConstants.START_ELEMENT
					&& currentElement.asStartElement().getName()
							.equals(new QName("http://schemas.xmlsoap.org/wsdl/", "operation"))) {
				addCharacterEventsAndFastForward(xmlEvents, xmlEventsToReturn);
				if (nextEvent.getEventType() == XMLStreamConstants.START_ELEMENT
						&& nextEvent.asStartElement().getName()
								.equals(new QName("http://schemas.xmlsoap.org/wsdl/","documentation"))) {
						xmlEventsToReturn.add(nextEvent);
						fastForward(xmlEvents);
						addCharacterEventsAndFastForward(xmlEvents, xmlEventsToReturn);
						Characters characters = xmlEventFactory
								.createCharacters(THIS_IS_DOCUMENTATION_ADDED_BY_CODE);
						xmlEventsToReturn.add(characters);
						if (nextEvent.getEventType() == XMLStreamConstants.END_ELEMENT) {
							xmlEventsToReturn.add(nextEvent);
							fastForward(xmlEvents);
						}
				} else {
					xmlEventsToReturn.add(xmlEventFactory.createStartElement(
							"wsdl", qNameDocumentation.getNamespaceURI(), qNameDocumentation.getLocalPart()));
					xmlEventsToReturn.add(xmlEventFactory
							.createCharacters(THIS_IS_DOCUMENTATION_ADDED_BY_CODE));
					xmlEventsToReturn.add(xmlEventFactory.createEndElement("wsdl", qNameDocumentation.getNamespaceURI(), qNameDocumentation.getLocalPart()));
					xmlEventsToReturn.add(newLine);
					xmlEventsToReturn.add(tab);
				}
			}

		}
		return xmlEventsToReturn;
	}

	private void addCharacterEventsAndFastForward(Queue<XMLEvent> xmlEvents,
			Collection<XMLEvent> xmlEventsToReturn) {
		while (nextEvent.getEventType() == XMLStreamConstants.CHARACTERS) {
			xmlEventsToReturn.add(nextEvent);
			fastForward(xmlEvents);
		}
	}

	private void fastForward(Queue<XMLEvent> xmlEvents) {
		//Done because we have added the nextEvent to our Processed list
		currentElement=xmlEvents.poll();
		nextEvent=xmlEvents.peek();
	}

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.context=arg0;
	}

	public void process(Exchange exchange) throws Exception {
		exchange.getIn().setBody(process((Queue<XMLEvent>) exchange.getIn().getBody()));
	}
	
}
