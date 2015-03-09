package com.jasmine.javax.xmleventreader;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.events.XMLEvent;

public class WSDL {

	private static final QName wsdlDefinition = new QName(
			"http://schemas.xmlsoap.org/wsdl/", "definitions");
	private static final QName serviceElement = new QName(
			"http://schemas.xmlsoap.org/wsdl/", "service");
	private static final QName portElement = new QName(
			"http://schemas.xmlsoap.org/wsdl/", "port");
	private static final QName wsdloperationElement = new QName(
			"http://schemas.xmlsoap.org/wsdl/", "operation");

	private List<XMLEvent> xmlEvents;

	private String targetNamespace;

	private String serviceName;
	private String portName;

	private List<String> operations;

	public WSDL(List<XMLEvent> xmlEvents) {
		this.xmlEvents = xmlEvents;
	}

	public Queue<XMLEvent> getXMLEvents() {
		return new LinkedList<XMLEvent>(xmlEvents);
	}

	public String getTargetNamespace() {
		if (targetNamespace == null) {
			for (Iterator<XMLEvent> iterator = xmlEvents.iterator(); iterator
					.hasNext();) {
				XMLEvent xmlEvent = (XMLEvent) iterator.next();
				if (xmlEvent.getEventType() == XMLStreamConstants.START_ELEMENT
						&& xmlEvent.asStartElement().getName()
								.equals(wsdlDefinition)) {
					targetNamespace = xmlEvent.asStartElement()
							.getAttributeByName(new QName("targetNamespace"))
							.getValue();
				}
			}
		}
		return targetNamespace;
	}

	public String getServiceName() {
		if (serviceName == null) {
			for (Iterator<XMLEvent> iterator = xmlEvents.iterator(); iterator
					.hasNext();) {
				XMLEvent xmlEvent = (XMLEvent) iterator.next();
				if (xmlEvent.getEventType() == XMLStreamConstants.START_ELEMENT
						&& xmlEvent.asStartElement().getName()
								.equals(serviceElement)) {
					serviceName = xmlEvent.asStartElement()
							.getAttributeByName(new QName("name")).getValue();
				}
			}
		}
		return serviceName;
	}

	public String getPortName() {
		if (portName == null) {
			for (Iterator<XMLEvent> iterator = xmlEvents.iterator(); iterator
					.hasNext();) {
				XMLEvent xmlEvent = (XMLEvent) iterator.next();
				if (xmlEvent.getEventType() == XMLStreamConstants.START_ELEMENT
						&& xmlEvent.asStartElement().getName()
								.equals(serviceElement)) {
					if (iterator.hasNext()) {
						xmlEvent = (XMLEvent) iterator.next();
						if (xmlEvent.getEventType() == XMLStreamConstants.START_ELEMENT
								&& xmlEvent.asStartElement().getName()
										.equals(portElement)) {
							portName = xmlEvent.asStartElement()
									.getAttributeByName(new QName("name"))
									.getValue();
						}
					}
				}
			}
		}
		return portName;
	}

	public List<String> getOperations() {
		if (operations.size() == 0) {
			for (Iterator<XMLEvent> iterator = xmlEvents.iterator(); iterator
					.hasNext();) {
				XMLEvent xmlEvent = (XMLEvent) iterator.next();
				if (xmlEvent.getEventType() == XMLStreamConstants.START_ELEMENT
						&& xmlEvent.asStartElement().getName()
								.equals(wsdloperationElement)) {
					operations.add(xmlEvent.asStartElement()
							.getAttributeByName(new QName("name")).getValue());
				}
			}
		}
		return operations;
	}

	public String getEquivalentPortClass() {
		return getPackageNameFor(getTargetNamespace()) + getPortName();
	}

	public String getEquivalentServiceClass() {
		return getPackageNameFor(getTargetNamespace()) + getServiceName();
	}

	public static String getPackageNameFor(String targetNamespace) {
		String packageName="";
		// http://www.example.org/RegisterDirectCustomerService/ translates to
		// org.example.registerdirectcustomerservice
		String[] split = targetNamespace.substring("http://".length()).split(
				"/");
		String[] splitDomain = split[0].split("\\.");
		for (int i = splitDomain.length-1; i >=0 ; i--) {
			if(packageName.length()>0){
				packageName.concat(".");
			}
			packageName.concat(splitDomain[i]);
		}
		for( int j=split.length; j>0 ; j++){
			if(packageName.length()>0){
				packageName.concat(".");
			}
			packageName.concat(split[j]);
		}
		return packageName;
	}

	public String getFullyQualifiedPortClassName() {
		return getPackageNameFor(getTargetNamespace()).concat(".")+getPortName();
	}
	
}
