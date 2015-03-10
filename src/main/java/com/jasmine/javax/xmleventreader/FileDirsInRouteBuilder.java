package com.jasmine.javax.xmleventreader;

import org.apache.camel.builder.RouteBuilder;

public class FileDirsInRouteBuilder extends RouteBuilder {

	private String inputdir;
	private String inputfileregex;
	
	@Override
	public void configure() throws Exception {
		from("file:"+inputdir+"?noop=true&delay=10000"+inputfileregex==null?"":("&include="+inputfileregex)).to("vm:processEP");
	}

	public FileDirsInRouteBuilder(String inputdir, String inputfileregex) {
		super();
		this.inputdir = inputdir;
		this.inputfileregex=inputfileregex;
	}
	
}
