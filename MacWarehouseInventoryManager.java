
/**
 * @author Kenss Solutions LLC
 */
package com.inventory.macwarehouse;
 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import javax.script.ScriptException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.kenss.utilities.Constants;
import com.kenss.utilities.GoogleDriveClient;
import com.kenss.utilities.PrintUtility;
import com.kenss.utilities.StringUtilities;
 
public class MacWarehouseInventoryManager {
	
	private String productOSType = "MacOS";
	
	
    public String getProductOSType() {
		return productOSType;
	}

    public void setProductOSType(String OSType) {
		this.productOSType = OSType;
	}
    
    @SuppressWarnings("unchecked")
	public static Map<String, String> loadConfigValues() {
    	File configFile = new File("resources/configs.properties");
    	Map<String, String> properties = new HashMap<String, String>();
    	
    	try {
    	    FileReader reader = new FileReader(configFile);
    	    Properties props = new Properties();
    	    props.load(reader);
    	 
    	    Enumeration<String> keys = (Enumeration<String>) props.propertyNames();
    	    while(keys.hasMoreElements()) {
    	    	String key = keys.nextElement();
    	    	switch(key){
    	    	case "master_product_file_name":
    	    		Constants.MASTER_PRODUCT_FILE_NAME = props.getProperty(key);
    	    		break;
    	    	case "download_from_gdrive":
    	    		if (props.getProperty(key).equalsIgnoreCase("true")){
    	    			Constants.DOWNLOAD_FROM_GDRIVE = true;
    	    		}
    	    		else {
    	    			Constants.DOWNLOAD_FROM_GDRIVE = false;
    	    		}
    	    		break;
    	    	case "gdrive_user":
    	    		Constants.GDRIVE_USER_INFO = props.getProperty(key);
    	    		break;
    	    	case "gdrive_cred_file":
    	    		Constants.GDRIVE_CREDS_FILE = props.getProperty(key);
    	    		break;
    	    	case "generated_file_path":
    	    		Constants.GENERATED_FILE_PATH = props.getProperty(key);
    	    		break;
    	    	case "printer_name":
    	    		Constants.PRINTER_NAME = props.getProperty(key);
    	    	case "label_template":
    	    		Constants.LABEL_TEMPLATE = props.getProperty(key);
    	    		
    	    	}
    	    	properties.put(key, props.getProperty(key));
    	    }
    	 
    	    reader.close();
    	    return properties;
    	    
    	    
    	} catch (FileNotFoundException ex) {
    	    // file does not exist
    	} catch (IOException ex) {
    	    // I/O error
    	}
    	return properties;
    }
    
	public static void main(String[] args) throws IOException
    {
		MacWarehouseInventoryManager masterSKUMatcher = new MacWarehouseInventoryManager();
		
		String fileName = "product";
		String iOSData = "";
		String fileLocation = "";
		
		
    	if (args.length > 0) {
    		if (args[0].equalsIgnoreCase("iOS"))
    			masterSKUMatcher.setProductOSType("iOS");
    	}
    	if (args.length > 1) {
    		iOSData = args[1];
    	}
    	if (args.length == 3)
    		fileLocation = args[2];
    	
    	Map<String, String> properties = loadConfigValues();
    	
    	//TODO - Uncomment when ready. No reason to hammer the Drive API for every debug/test run.
    	if (Constants.DOWNLOAD_FROM_GDRIVE) {
    		GoogleDriveClient driveAPI = new GoogleDriveClient();
    		boolean masterFileDownloaded = driveAPI.downloadFileFromDrive(fileName, fileLocation);
    	}

    		
    	MacWarehouseProduct receivedProduct = new MacWarehouseProduct();
    	MacWarehouseSKU macWarehouseSKU = new MacWarehouseSKU();
    	BarcodeGenerator barcodeGenerator = new BarcodeGenerator();
    	MWProductHelper productHelper = new MWProductHelper();

    	//Information gathered from different sources for MacOS & iOS
    	if (masterSKUMatcher.getProductOSType().equals("MacOS")) {
    		//Invoke system_profiler MacOS command to get system info like serial #, OS version, hardware specs, etc.
    		productHelper.getSystemInformationUsingProfiler(receivedProduct);
    		SystemSpecifications.getSpecs(receivedProduct);
    	}
    	else if (masterSKUMatcher.getProductOSType().equals("iOS")) {
    		//Get mobile device information
    		productHelper.getDeviceInformationFromCFGUtilFile(receivedProduct, iOSData);
    	}
    		
        
        //Connect to the internal MacWarehouse item database (Google doc, for example) and determine the sku to be assigned
    	try {
    		macWarehouseSKU.determineInternalSKUNumber(receivedProduct);
    		
    		//TODO: Once the sku is identified, determine PO# 
            
            //Generate QR code using the spec string, ONLY IF a match is found
    		if (receivedProduct.getSKU() != null && !receivedProduct.getSKU().isEmpty()){
    			
	            BitMatrix qrCodeMatrix = barcodeGenerator.generateBarcode(receivedProduct.getSpecString(), BarcodeFormat.QR_CODE, Constants.BARCODE_WIDTH, Constants.BARCODE_HEIGHT);
	            
	            //Generate the barcode and save it to disk using serialNumber as the fileName. Display the generated barcode image on the screen
	            String generatedImagePath = barcodeGenerator.generateBarcodeImage(qrCodeMatrix, receivedProduct.getSerialNumber());
	            BarcodeDisplay.show(generatedImagePath, receivedProduct);
	
	            try {
	            	MWLabelGenerator label = new MWLabelGenerator();
	            	label.generate(receivedProduct);
	            	
	            	DymoIntegration dymo = new DymoIntegration();
	            	dymo.generateDymoJSAPIFile(receivedProduct.getSerialNumber());
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
	            System.out.println("All done with the core functions. Wait for 10 secs before closing windows and proceed to Print the label******************");
	            try {
		    		  Thread.sleep(10000);
		    	}
		    	catch (InterruptedException ie) {
		    		  
		    	}
    		}
    	}
    	catch (Exception e) {
    		System.out.println ("Something went wrong determining internal sku number. Actual error: " + e.getMessage());
    	}
    	finally {
    		//Clean up any generated files - cfgutil files/barcode image files/ etc.........
            //All generated files have product serial number in the name
    		Files.deleteIfExists(Paths.get(Constants.DEFAULT_FILE_PATH + receivedProduct.getSerialNumber() + ".jpg"));
    		Files.deleteIfExists(Paths.get(Constants.DEFAULT_FILE_PATH + receivedProduct.getSerialNumber() + ".txt"));
    	}
    	
    	System.exit(0);
    }
     
}