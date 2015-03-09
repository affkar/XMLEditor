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
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class XMLEventConverter implements ApplicationContextAware {

	private ApplicationContext context;
	
	private QName qNameDocumentation = new QName(
			"http://schemas.xmlsoap.org/wsdl/", "documentation");
	
	private XMLEvent previousEvent;
	private XMLEvent currentEvent;
	private XMLEvent nextEvent;
	private String codePath1;
	private String codePath2;
	private String codePath3;
	private String codePath4;
	
	public Collection<XMLEvent> process(Queue<XMLEvent> xmlEvents) throws Exception {
		Collection<XMLEvent> xmlEventsToReturn=new ArrayList<XMLEvent>();
		
		XMLEventFactory xmlEventFactory = context.getBean("XMLEventFactory", XMLEventFactory.class);
		DTD newLine=context.getBean("newLineDTD", DTD.class);
		DTD tab=context.getBean("tabDTD",DTD.class);
		
		while(!xmlEvents.isEmpty()){
			currentEvent = xmlEvents.poll();
			nextEvent=null;
			if(!xmlEvents.isEmpty())
				nextEvent = xmlEvents.peek();
			
			xmlEventsToReturn.add(currentEvent);
			if (currentEvent.getEventType() == XMLStreamConstants.START_ELEMENT
					&& currentEvent.asStartElement().getName()
							.equals(new QName("http://schemas.xmlsoap.org/wsdl/", "operation"))) {
				String operationName = currentEvent.asStartElement().getAttributeByName(new QName("name")).getValue();
				addCharacterEventsAndFastForward(xmlEvents, xmlEventsToReturn);
				if (nextEvent.getEventType() == XMLStreamConstants.START_ELEMENT
						&& nextEvent.asStartElement().getName()
								.equals(new QName("http://schemas.xmlsoap.org/wsdl/","documentation"))) {
						xmlEventsToReturn.add(nextEvent);
						fastForward(xmlEvents);
						addCharacterEventsAndFastForward(xmlEvents, xmlEventsToReturn);
						Characters characters = xmlEventFactory
								.createCharacters(getCharactersFromGrepOutput(operationName));
						xmlEventsToReturn.add(characters);
						if (nextEvent.getEventType() == XMLStreamConstants.END_ELEMENT) {
							xmlEventsToReturn.add(nextEvent);
							fastForward(xmlEvents);
						}
				} else {
					xmlEventsToReturn.add(xmlEventFactory.createStartElement(
							"wsdl", qNameDocumentation.getNamespaceURI(), qNameDocumentation.getLocalPart()));
					xmlEventsToReturn.add(xmlEventFactory
							.createCharacters(getCharactersFromGrepOutput(operationName)));
					xmlEventsToReturn.add(xmlEventFactory.createEndElement("wsdl", qNameDocumentation.getNamespaceURI(), qNameDocumentation.getLocalPart()));
					xmlEventsToReturn.add(newLine);
					xmlEventsToReturn.add(tab);
				}
			}

		}
		return xmlEventsToReturn;
	}
	
	/*private String getCompositionFromOperationName(String operationName){
		
	}*/

	private String getCharactersFromGrepOutput(String operationName) {
		/*Profile customerDir = ProfileBuilder.newBuilder()
                .name(codePath1)
                .filePath(codePath1)
                .onLocalhost()
                .build();
		Profile WMDir = ProfileBuilder.newBuilder()
                .name(codePath2)
                .filePath(codePath2)
                .onLocalhost()
                .build();
		Profile PSDir = ProfileBuilder.newBuilder()
                .name(codePath3)
                .filePath(codePath3)
                .onLocalhost()
                .build();
		Profile SecurityDir = ProfileBuilder.newBuilder()
                .name(codePath4)
                .filePath(codePath4)
                .onLocalhost()
                .build();
		GrepResults results = grep(constantExpression(operationName), on(customerDir,WMDir,PSDir,SecurityDir),
                with(
                        options(
                                recursive(),
                                onlyFilesWhenRecursing("*.java"),
                                excludeDirectoriesWhenRecursing("tests"),
                                excludeDirectoriesWhenRecursing(".metadata"),
                                excludeDirectoriesWhenRecursing(".settings"),
                                excludeDirectoriesWhenRecursing(".svn"),
                                excludeDirectoriesWhenRecursing("target"),
                                filesMatching(),
                                lineNumber()
                                )
                        )
                );
		return results.toString();*/
		return "Documentation added by code";
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
		previousEvent=currentEvent;
		currentEvent=xmlEvents.poll();
		nextEvent=xmlEvents.peek();
	}

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.context=arg0;
	}

	public void process(Exchange exchange) throws Exception {
		exchange.getIn().setBody(process((Queue<XMLEvent>) exchange.getIn().getBody()));
	}

	public String getCodePath1() {
		return codePath1;
	}

	public void setCodePath1(String codePath1) {
		this.codePath1 = codePath1;
	}

	public String getCodePath2() {
		return codePath2;
	}

	public void setCodePath2(String codePath2) {
		this.codePath2 = codePath2;
	}

	public String getCodePath3() {
		return codePath3;
	}

	public void setCodePath3(String codePath3) {
		this.codePath3 = codePath3;
	}

	public String getCodePath4() {
		return codePath4;
	}

	public void setCodePath4(String codePath4) {
		this.codePath4 = codePath4;
	}
	
	
	
}
