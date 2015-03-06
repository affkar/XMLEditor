package com.jasmine.javax.xmleventreader;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XMLEditor {
	public static void main(String[] args) throws InterruptedException {
		ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("application-context.xml");
		context.start();
		Thread.sleep(10000000);
		context.stop();
		context.close();
	}
}