package com.kenss.utilities;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class StringUtilities {

	public StringUtilities() {
		// TODO Auto-generated constructor stub
	}
	
	public Document loadXMLFromString(String xmlInfoAsString) throws ParserConfigurationException, SAXException, IOException
	{
	   DocumentBuilderFactory fctr = DocumentBuilderFactory.newInstance();
	   DocumentBuilder bldr = fctr.newDocumentBuilder();
	   InputSource insrc = new InputSource(new StringReader(xmlInfoAsString));
	   return bldr.parse(insrc);
	}
	
	public static String adjustHDSize(String memorySize) {
		
		StringBuffer memoryUnit = new StringBuffer("");
		String size = new String("");
		
		memoryUnit.append(StringUtils.right(memorySize, 2));
		size += (StringUtils.substring(memorySize, 0, StringUtils.indexOf(memorySize, memoryUnit.toString())));
		
		//Make 1.0/2.0, etc -> 1/2
		if (size.indexOf(".0") > -1)
			size = "" + Integer.parseInt(StringUtils.substring(size, 0, size.indexOf(".0")));
		
		switch (memoryUnit.toString()){
		case "MB":
		case "GB":
			long tempSize=0;
			if (size.indexOf(".") > -1)
				tempSize=Long.parseLong(StringUtils.substring(size, 0, size.indexOf(".")));
			else
				tempSize = Long.parseLong(size);
			
			//Value needs be exponents of 2
			if (tempSize%2 != 0) {
				tempSize += (tempSize%2);
				return (new String (tempSize + memoryUnit.toString()));
			}
			
		default:
			return (size + memoryUnit.toString());
			
		}
	}
}
