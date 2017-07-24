
/**
 * @author Kenss Solutions LLC
 */
package com.inventory.macwarehouse;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.kenss.utilities.GoogleDriveClient;
import com.kenss.utilities.StringUtilities;
 
public class MacWarehouseInventoryManager {
	
	private String productOSType = "MacOS";
	
	
    public String getProductOSType() {
		return productOSType;
	}

    public void setProductOSType(String OSType) {
		this.productOSType = OSType;
	}
    
	public static void main(String[] args) throws IOException
    {
		MacWarehouseInventoryManager masterSKUMatcher = new MacWarehouseInventoryManager();
		
		String classpath = System.getProperty("java.class.path");
		System.out.println(classpath);
		String fileName = "product";
		String fileLocation = "";
		
    	if (args.length > 0) {
    		if (args[0].equalsIgnoreCase("iOS"))
    			masterSKUMatcher.setProductOSType("iOS");
    	}
    	if (args.length > 1) {
    		fileName = args[1];
    	}
    	if (args.length == 3)
    		fileLocation = args[2];
    	
    	//TODO - Uncomment when ready. No reason to hammer the Drive API for every debug/test run.
    	//GoogleDriveClient driveAPI = new GoogleDriveClient();
    	//boolean masterFileDownloaded = driveAPI.downloadFileFromDrive(fileName, fileLocation);

    		
    	MacWarehouseProduct receivedProduct = new MacWarehouseProduct();
    	MacWarehouseSKU macWarehouseSKU = new MacWarehouseSKU();
    	BarcodeGenerator barcodeGenerator = new BarcodeGenerator();
    	MWProductHelper productHelper = new MWProductHelper();
    	/* Get some run time properties for the Java/jvm
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
 
        Map<String, String> systemProperties = runtimeBean.getSystemProperties();
        Set<String> keys = systemProperties.keySet();
        for (String key : keys) {
            String value = systemProperties.get(key);
            System.out.printf("[%s] = %s.\n", key, value);
        }
        */
    	
    	//TODO: Do one thing for MacOS devices, another for iOS devices
    	
    	if (masterSKUMatcher.getProductOSType().equals("MacOS")) {
    		//Invoke system_profiler MacOS command to get system info like serial #, OS version, hardware specs, etc.
    		productHelper.getSystemInformationUsingProfiler(receivedProduct);
    		SystemSpecifications.getSpecs(receivedProduct);
    	}
    	else if (masterSKUMatcher.getProductOSType().equals("iOS")) {
    		//Get mobile device information
    		productHelper.getDeviceInformationFromCFGUtilFile(receivedProduct);
    	}
    		
        
        //TODO: Connect to the internal MacWarehouse item database (Google doc, for example) and determine the sku to be assigned
    	try {
    		macWarehouseSKU.determineInternalSKUNumber(receivedProduct);
    	}
    	catch (Exception e) {
    		System.out.println ("Something went wrong determining internal sku number. Actual error: " + e.getMessage());
    	}
        
        //TODO: Once the sku is identified, determine PO# 
        
        //TODO: Generate 2D barcode (e.g. QR code) using the combination of sku#, serial #, hardware specs, PO #
        //Example string & QR code dimensions
        String contents = "MA878LL/A C2D/2.4GHz/4GB/1.25TB";
        int width = 300;
        int height = 300;
        BitMatrix qrCodeMatrix = barcodeGenerator.generateBarcode(contents, BarcodeFormat.QR_CODE, width, height);
        //System.out.print(qrCodeMatrix);
        
        //Display the generated barcode image on the screen
        //TODO: Print the generated barcode using Dymo label printers
        String generatedImagePath = barcodeGenerator.generateBarcodeImage(qrCodeMatrix);
        BarcodeDisplay.show(generatedImagePath);
        
        //Send the generated barcode to Dymo for label printing
        
    }
     
}