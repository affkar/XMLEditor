package com.jasmine.javax.xmleventreader;

import javax.xml.stream.events.XMLEvent;

public class CurrentAndNext {

	private XMLEvent current;
	private XMLEvent next;
	public XMLEvent getCurrent() {
		return current;
	}
	
	public XMLEvent getNext() {
		return next;
	}

	public CurrentAndNext(XMLEvent current, XMLEvent next) {
		super();
		this.current = current;
		this.next = next;
	}
	
	
	
	
	
}
