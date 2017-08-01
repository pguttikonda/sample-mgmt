package com.inventory.macwarehouse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.kenss.utilities.Constants;

public class DymoIntegration {

	public DymoIntegration() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean generateDymoJSAPIFile (String serialNumber) {
		
		try {
    		BufferedWriter out = new BufferedWriter (new FileWriter("/Users/pguttikonda/Desktop/MacWarehouse/web/main.js"));
    		
    		
    		
    	    String labelLine = "var printers = dymo.label.framework.getPrinters();\n" +
						    	    "if (printers.length == 0){\n" + 
						    	    	"console.log('No printers found');\n" +
						    	    	"}\n" +
						    	    	"else {\n" +
						    	    	   "var printerName = printers[0].name;\n" +
						    	    	"}\n" +
						    	    	"console.log('Printer name...........');\n" + 
						    	    	"console.log(printerName);\n" +
						    	    	"var labelXML = dymo.label.framework.openLabelFile(\"/Users/pguttikonda/Desktop/MacWarehouse/generated/" + serialNumber + ".label\");\n" +
						    	    	"var labelXml = labelXML.toString();\n"+
										"labelXML.print(printerName);\n" + 
						    	    	"console.log('Did it print????........');\n"; 

	    	out.write(labelLine);
	    	out.write("\n");
	    	out.flush();
			out.close();
		} 
	    catch (FileNotFoundException e) {
			System.out.println("Error writing javascript file: " + e.getMessage());
			return false;
		} catch (IOException e) {
			System.out.println("Error writing javascript file: " + e.getMessage());
			return false;
		}
			
		return true;
		
	}

}
