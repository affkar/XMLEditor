package com.jasmine.javax.xmleventreader;

import static org.apache.camel.builder.PredicateBuilder.not;

import java.util.Collection;

import org.apache.camel.builder.RouteBuilder;

public class XMLTransformationRouteBuilder extends RouteBuilder {

	private String inputdir;
	private String outputdir;
	private String inputfileregex;
	@Override
	public void configure() throws Exception {
		from("file:"+inputdir+"?noop=true")
			.to("log:XMLTransformationRouteBuilder?level=DEBUG&showHeaders=true")
			.to("bean:InputStreamToXMLEvents?method=getAllXMLEvents")
			.to("log:XMLTransformationRouteBuilder?level=DEBUG&showHeaders=true")
			.processRef("XMLEventsProcessor")
			/*.choice()
			.when(not((body().isInstanceOf(Collection.class))))
				.to("direct:writeep")
			.when(body().isInstanceOf(Collection.class))
					.split(body()).to("direct:writeep")					
			.end();*/
			.to("bean:XMLEventsToByteArray?method=convert")
			.to("log:XMLTransformationRouteBuilder?level=DEBUG&showHeaders=true")
			//.setHeader("CamelFileName",header("CamelFileName").regexReplaceAll(".*", "out"))
			.to("log:XMLTransformationRouteBuilder?level=DEBUG&showHeaders=true")
			.to("file:"+outputdir);
	}
	public String getInputdir() {
		return inputdir;
	}
	public void setInputdir(String inputdir) {
		this.inputdir = inputdir;
	}
	public String getOutputdir() {
		return outputdir;
	}
	public void setOutputdir(String outputdir) {
		this.outputdir = outputdir;
	}
	public String getInputfileregex() {
		return inputfileregex;
	}
	public void setInputfileregex(String inputfileregex) {
		this.inputfileregex = inputfileregex;
	}
}