package com.jasmine.javax.xmleventreader;

import static org.apache.camel.builder.PredicateBuilder.not;

import java.util.Collection;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.freemarker.FreemarkerConstants;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.spi.InterceptStrategy;

public class XMLTransformationRouteBuilder extends RouteBuilder {

	private String inputdir1;
	private String inputdir2;
	private String inputdir3;
	private String inputdir4;
	private String inputfileregex;
	@Override
	public void configure() throws Exception {
		from("file:"+inputdir1+"?noop=true&delay=10000")
			.to("vm:processEP");
		from("file:"+inputdir2+"?noop=true&delay=10000")
		.to("vm:processEP");
		/*from("file:"+inputdir3+"?noop=true&delay=10000")
		.to("vm:processEP");
		from("file:"+inputdir4+"?noop=true&delay=10000")
		.to("vm:processEP");*/
		
			from("vm:processEP")
			.to("log:XMLTransformationRouteBuilder?level=DEBUG&showHeaders=true")
			.to("bean:InputStreamToXMLEvents?method=getAllXMLEvents")
			.to("log:XMLTransformationRouteBuilder?level=DEBUG&showHeaders=true")
			.processRef("XMLEventsProcessor")
			.to("bean:XMLEventsToByteArray?method=convert")
			.to("log:XMLTransformationRouteBuilder?level=DEBUG&showHeaders=true")
			
			//.setHeader(FreemarkerConstants.FREEMARKER_TEMPLATE, simple("file:${headers.CamelFileParent}out"))
			.to("log:XMLTransformationRouteBuilder?level=DEBUG&showHeaders=true")
			.recipientList(simple("file:${headers.CamelFileParent}out"));
	}
	
	public String getInputdir1() {
		return inputdir1;
	}

	public void setInputdir1(String inputdir1) {
		this.inputdir1 = inputdir1;
	}

	public String getInputdir2() {
		return inputdir2;
	}

	public void setInputdir2(String inputdir2) {
		this.inputdir2 = inputdir2;
	}

	public String getInputdir3() {
		return inputdir3;
	}

	public void setInputdir3(String inputdir3) {
		this.inputdir3 = inputdir3;
	}

	public String getInputdir4() {
		return inputdir4;
	}

	public void setInputdir4(String inputdir4) {
		this.inputdir4 = inputdir4;
	}

	public String getInputfileregex() {
		return inputfileregex;
	}
	public void setInputfileregex(String inputfileregex) {
		this.inputfileregex = inputfileregex;
	}
}