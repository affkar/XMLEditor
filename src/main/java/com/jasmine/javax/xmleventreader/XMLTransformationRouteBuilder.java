package com.jasmine.javax.xmleventreader;

import org.apache.camel.builder.RouteBuilder;

public class XMLTransformationRouteBuilder extends RouteBuilder {

	
	@Override
	public void configure() throws Exception {
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
	
}