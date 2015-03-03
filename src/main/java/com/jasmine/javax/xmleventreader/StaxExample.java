package com.jasmine.javax.xmleventreader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.DTD;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang.StringUtils;

public class StaxExample {

	private static final String THIS_IS_DOCUMENTATION_ADDED_BY_CODE = "This is documentation added by code.";
	private static XMLEventFactory xmlEventFactory=XMLEventFactory.newInstance();
	private static DTD newLine = xmlEventFactory.createDTD("\n");
	private static DTD tab = xmlEventFactory.createDTD("\t");
	
	
	private static QName qNameDocumentation = new QName("http://www.example.org/GolfCountryClub", "Documentation");
	private static String pathname1 = "../XMLExamples/GolfCountryClub/GolfCountryClub.xml";
	private static String pathname2 = "../XMLExamples/GolfCountryClub/GolfCountryClub1.xml";
	private static File file1 = new File(pathname1);
	private static File file2 = new File(pathname2);
	
	private static Reader reader;
	private static Writer writer;
	
	static {
		try {
			reader=new FileReader(file1);
			writer=new FileWriter(file2);
		} catch (FileNotFoundException e) {
			System.out.println("Fatal- file doesn't exist "+e.toString());
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("Fatal- file can't be read IOException "+e.toString());
			System.exit(-2);
		}
		
	}
	
	public static void main(String[] args) throws XMLStreamException, FactoryConfigurationError, IOException {
		XMLEventReader xmlEventReader = XMLInputFactory.newInstance().createXMLEventReader(reader);
		XMLEventWriter xmlEventWriter =  XMLOutputFactory.newInstance().createXMLEventWriter(writer);
		while(xmlEventReader.hasNext()) {			
			XMLEvent next = (XMLEvent)xmlEventReader.next();
			xmlEventWriter.add(next);
			if(next.getEventType()==XMLStreamConstants.START_ELEMENT && next.asStartElement().getName().equals(new QName("", "GolfCourse"))){
				addIfBlankCharacters(xmlEventReader, xmlEventWriter);
				if(xmlEventReader.hasNext() && xmlEventReader.peek().getEventType() == XMLStreamConstants.START_ELEMENT && xmlEventReader.peek().asStartElement().getName().equals(new QName("Documentation"))){
					updateExistingDocumentation(xmlEventWriter, xmlEventReader, THIS_IS_DOCUMENTATION_ADDED_BY_CODE);
				}else{
					addNewDocumentation(xmlEventWriter);
				}
			}
		}
		xmlEventWriter.close();
	}

	private static void updateExistingDocumentation(XMLEventWriter xmlEventWriter, XMLEventReader xmlEventReader, String additionalData) throws XMLStreamException {
		while(xmlEventReader.hasNext()){
			if(xmlEventReader.peek().getEventType() ==XMLStreamConstants.START_ELEMENT){
				XMLEvent next = (XMLEvent)xmlEventReader.next();
				xmlEventWriter.add(next);
			}
			if(xmlEventReader.peek().getEventType() ==XMLStreamConstants.CHARACTERS){
				XMLEvent next = (XMLEvent)xmlEventReader.next();
				xmlEventWriter.add(next);
				xmlEventWriter.add(newLine);
				Characters characters = xmlEventFactory.createCharacters(additionalData);
				xmlEventWriter.add(characters);
			}
			if(xmlEventReader.peek().getEventType() ==XMLStreamConstants.END_ELEMENT){
				XMLEvent next = (XMLEvent)xmlEventReader.next();
				xmlEventWriter.add(next);
				return;
			}
		}
	}

	private static void addNewDocumentation(XMLEventWriter xmlEventWriter) throws XMLStreamException {
		xmlEventWriter.add(xmlEventFactory.createStartElement(qNameDocumentation, null, null));
		xmlEventWriter.add(xmlEventFactory.createCharacters(THIS_IS_DOCUMENTATION_ADDED_BY_CODE));
		xmlEventWriter.add(xmlEventFactory.createEndElement(qNameDocumentation, null));
		xmlEventWriter.add(newLine);
		xmlEventWriter.add(tab);
	}
	
	private static void addIfBlankCharacters(XMLEventReader xmlEventReader, XMLEventWriter xmlEventWriter) throws XMLStreamException{
		if(xmlEventReader.peek().getEventType()==XMLStreamConstants.CHARACTERS && ( StringUtils.isEmpty(xmlEventReader.peek().toString()) || StringUtils.isBlank(xmlEventReader.peek().toString())) ){
			xmlEventWriter.add((XMLEvent) xmlEventReader.next());
		}
	}
}