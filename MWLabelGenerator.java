package com.inventory.macwarehouse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.kenss.utilities.Constants;

public class MWLabelGenerator {

	public MWLabelGenerator() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public boolean generate(MacWarehouseProduct receivedProduct) {
		
		if (Constants.LABEL_TEMPLATE == null || Constants.LABEL_TEMPLATE.isEmpty()) {
			System.out.println("No label template specified! Nothing to generate and print ***********************");
			return false;
		}
		
		StringBuilder xmlStringBuilder = new StringBuilder();
        try {
        	    BufferedReader in =
        	        new BufferedReader(new FileReader(Constants.LABEL_TEMPLATE));
        		BufferedWriter out = new BufferedWriter (new FileWriter(Constants.GENERATED_FILE_PATH + receivedProduct.getSerialNumber() + ".label"));
        		
        	    String labelLine = null;
        	    while((labelLine = in.readLine())!= null) {
        	    	if (labelLine.contains("{{")){
        	    		//TODO add actual PO number when that functionality is ready
        	    		labelLine = StringUtils.replace(labelLine, "{{PO_NO}}", "229259");
        	    		labelLine = StringUtils.replace(labelLine, "{{MODEL_NO}}", receivedProduct.getModelNumber());
        	    		labelLine = StringUtils.replace(labelLine, "{{SPEC_STRING}}", receivedProduct.getSpecString());
        	    		labelLine = StringUtils.replace(labelLine, "{{SERIAL_NO}}", receivedProduct.getSerialNumber());
        	    		labelLine = StringUtils.replace(labelLine, "{{FULL_ITEM_STRING}}", receivedProduct.getModelNumber() + "Base Specs " + receivedProduct.getProductCategory()
        	            							+ " " + receivedProduct.getSpecString() + " " + receivedProduct.getProductYear());
        	    		labelLine = StringUtils.replace(labelLine, "{{SKU}}", receivedProduct.getSKU());
        	    		labelLine = StringUtils.replace(labelLine, "{{PRODUCT_SHORT_NAME}}", "APPLE " + receivedProduct.getProductType());
        	    	}
        	    	out.write(labelLine);
        	    	out.write("\n");
        	    	out.flush();
        	    }
        	    
        	    in.close();
        	    out.flush();
    			out.close();
		} 
        catch (FileNotFoundException e) {
			System.out.println("Error reading template file: " + e.getMessage());
			return false;
		} catch (IOException e) {
			System.out.println("Error reading template file: " + e.getMessage());
			return false;
		}
        
 		return true;
	}
}
