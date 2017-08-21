package com.inventory.macwarehouse;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrLookup;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenss.utilities.ColorUtils;
import com.kenss.utilities.Constants;
import com.kenss.utilities.StringUtilities;
import com.opencsv.bean.CsvToBeanBuilder;

import oshi.util.FormatUtil;

@SuppressWarnings("deprecation")
public class MWProductHelper {

	public MWProductHelper() {
		// TODO Auto-generated constructor stub
	}

	public void getSystemInformationUsingProfiler(MacWarehouseProduct receivedProduct) throws IOException {
		
		String systemInfoString;
		try {
			systemInfoString = MacSystemInformation.getHardwareInfo();
			parseHardwareInfo(receivedProduct, systemInfoString);
			//System.out.println(systemInfoString);
			
			systemInfoString = MacSystemInformation.getSoftwareInfo();
			parseSoftwareInfo(receivedProduct, systemInfoString);
			//System.out.println(systemInfoString);
			
			systemInfoString = MacSystemInformation.getDisplaysInfo();
			parseDisplayInfo(receivedProduct, systemInfoString);
			//System.out.println(systemInfoString);
			
			systemInfoString = MacSystemInformation.getSATAStorageInfo();
			parseSATAStorageInfo(receivedProduct, systemInfoString);
			
			systemInfoString = MacSystemInformation.getPATAStorageInfo();
			parsePATAStorageInfo(receivedProduct, systemInfoString);

			if (!receivedProduct.getHardDriveType().contains("HDD") && !(receivedProduct.getHardDriveType().contains("SSD"))) {
				systemInfoString = MacSystemInformation.getStorageInfo();
				parseStorageInfo(receivedProduct, systemInfoString);
				//System.out.println(systemInfoString);
			}
			
			systemInfoString = MacSystemInformation.getBatteryInfo();
			parseBatteryInfo(receivedProduct, systemInfoString);
			
			String descriptiveModelInfo = getProductSpecsFromApple(receivedProduct.getSerialNumber());
			if(!descriptiveModelInfo.isEmpty()) {
				parseDescriptiveModelInfo(receivedProduct, descriptiveModelInfo);
			}
		}
		catch (IOException ioe){
			System.out.println("IOException while invoking system_profiler. Falling back to native oshi calls");
			return;
		}
		catch (Exception e){
			System.out.println("Exception while invoking system_profiler. Falling back to native oshi calls. Error msg: " + e.getMessage());
			return;
		}
	}

	private MacWarehouseProduct parseDescriptiveModelInfo(MacWarehouseProduct receivedProduct, String descriptiveModelInfo) throws ParserConfigurationException, SAXException, IOException {
		if (receivedProduct == null || descriptiveModelInfo == null || descriptiveModelInfo.isEmpty())
			return null;
		
		String info = StringUtils.substringBetween(descriptiveModelInfo, "<configCode>", "</configCode>");
		//Response from Apple's site is different for Mac & IOS serial numbers. So, do different things
		if (receivedProduct.getOsVersion().toLowerCase().contains("mac")){
			info = StringUtils.substringBetween(info, "(", ")");
			String[] infoTokens = info.split(",");
			
			for (int i=0; i<infoTokens.length; i++){
				if (infoTokens[i].trim().indexOf("4K") > -1)
					receivedProduct.setResolution("4K");
				else if (infoTokens[i].trim().indexOf("-inch") > -1)
					receivedProduct.setScreenSize(infoTokens[i].trim().substring(0, infoTokens[i].trim().indexOf("-inch")));
				else 
					receivedProduct.setProductYear(infoTokens[i].trim());
			}
		}
		else if (receivedProduct.getOsVersion().toLowerCase().contains("ios")) {
			receivedProduct.setProductType(info);
			
			if (info.indexOf('(') > -1) {
				String text = StringUtils.substringBetween(info, "(", ")");
				//If "text" has storage info use it to set harddrive size
				if (text.contains("GB") || text.contains("TB"))
					receivedProduct.setHardDriveSize(text);
			}
		}
		return receivedProduct;
		
	}

	private MacWarehouseProduct parseHardwareInfo (MacWarehouseProduct receivedProduct, String hardwareInfo) {
		if (receivedProduct == null || hardwareInfo == null || hardwareInfo.isEmpty())
			return null;
		
		if (!hardwareInfo.contains("Hardware:")) {
			System.out.println("Cannot find hardware info in the returned string from profiler: " + hardwareInfo);
			return null;
		}
		
		
		Map<String, String> propertyMap = convertSystemInfoStringToMap(hardwareInfo);

		if (propertyMap.isEmpty())
			return null;
		
		receivedProduct.setModelID(StrLookup.mapLookup(propertyMap).lookup("Model Identifier").trim());
		receivedProduct.setProcessorDescription(StrLookup.mapLookup(propertyMap).lookup("Processor Name").trim());
		receivedProduct.setProcessorSpeed(StrLookup.mapLookup(propertyMap).lookup("Processor Speed").trim());
		receivedProduct.setNoOfCores(Integer.parseInt((StrLookup.mapLookup(propertyMap).lookup("Total Number of Cores").trim())));
		receivedProduct.setRamSize(StringUtils.deleteWhitespace(StrLookup.mapLookup(propertyMap).lookup("Memory")));
		receivedProduct.setSerialNumber(StrLookup.mapLookup(propertyMap).lookup("Serial Number (system)").trim());
		
		
		return receivedProduct;
	}
	
	private MacWarehouseProduct parseSoftwareInfo (MacWarehouseProduct receivedProduct, String softwareInfo) {
		if (receivedProduct == null || softwareInfo == null || softwareInfo.isEmpty())
			return null;
		
		if (!softwareInfo.contains("Software:")) {
			System.out.println("Cannot find software info in the returned string from profiler: " + softwareInfo);
			return null;
		}
		
		
		Map<String, String> propertyMap = convertSystemInfoStringToMap(softwareInfo);

		if (propertyMap.isEmpty())
			return null;
		
		receivedProduct.setOsVersion(StrLookup.mapLookup(propertyMap).lookup("System Version").trim());

		return receivedProduct;
	}
	
	private MacWarehouseProduct parseSATAStorageInfo (MacWarehouseProduct receivedProduct, String storageInfo) {
		if (receivedProduct == null || storageInfo == null || storageInfo.isEmpty())
			return null;
		
		if (!storageInfo.contains("SATA")) {
			System.out.println("Cannot find SATA storage info in the returned string from profiler: " + storageInfo);
			return null;
		}
		
		
		Map<String, String> propertyMap = convertSystemInfoStringToMap(storageInfo);

		if (propertyMap.isEmpty())
			return null;
		
		try {
			String[] tempHDCapacity = StringUtils.split(StrLookup.mapLookup(propertyMap).lookup("Capacity"), "(");
			receivedProduct.setHardDriveSize(StringUtils.deleteWhitespace(tempHDCapacity[0]));
		}
		catch (Exception e) {}
		
		if (StrLookup.mapLookup(propertyMap).lookup("Model").contains("HDD"))
			receivedProduct.setHardDriveType("HDD");
		else if (StrLookup.mapLookup(propertyMap).lookup("Model").contains("SSD"))
			receivedProduct.setHardDriveType("SSD");
		else
			receivedProduct.setHardDriveType(StrLookup.mapLookup(propertyMap).lookup("Model").trim());
		
		return receivedProduct;

	}
	
	private MacWarehouseProduct parsePATAStorageInfo (MacWarehouseProduct receivedProduct, String storageInfo) {
		if (receivedProduct == null || storageInfo == null || storageInfo.isEmpty())
			return null;
		
		if (!storageInfo.contains("Parallel") || !storageInfo.contains("PATA")) {
			System.out.println("Cannot find Parallel ATA storage info in the returned string from profiler: " + storageInfo);
			return null;
		}
		
		
		Map<String, String> propertyMap = convertSystemInfoStringToMap(storageInfo);

		if (propertyMap.isEmpty())
			return null;
		
		try {
			String[] tempHDCapacity = StringUtils.split(StrLookup.mapLookup(propertyMap).lookup("Capacity"), "(");
			receivedProduct.setHardDriveSize(StringUtils.deleteWhitespace(tempHDCapacity[0]));
		}
		catch (Exception e) {}
		
		if (StrLookup.mapLookup(propertyMap).lookup("Model").contains("HDD"))
			receivedProduct.setHardDriveType("HDD");
		else if (StrLookup.mapLookup(propertyMap).lookup("Model").contains("SSD"))
			receivedProduct.setHardDriveType("SSD");
		else
			receivedProduct.setHardDriveType(StrLookup.mapLookup(propertyMap).lookup("Model").trim());
		
		System.out.println("Parallel ATA HD Size & TYpe" + receivedProduct.getHardDriveSize() + ",   " + receivedProduct.getHardDriveType());

		return receivedProduct;

	}
	
	private MacWarehouseProduct parseStorageInfo (MacWarehouseProduct receivedProduct, String storageInfo) {
		if (receivedProduct == null || storageInfo == null || storageInfo.isEmpty())
			return null;
		
		if (!storageInfo.contains("Storage:")) {
			System.out.println("Cannot find storage info in the returned string from profiler: " + storageInfo);
			return null;
		}
		
		
		Map<String, String> propertyMap = convertSystemInfoStringToMap(storageInfo);

		if (propertyMap.isEmpty())
			return null;
		
		String[] tempHDCapacity = StringUtils.split(StrLookup.mapLookup(propertyMap).lookup("Capacity"), "(");
		System.out.println(StringUtils.getDigits(StringUtils.split(tempHDCapacity[1], " ")[0]));
		receivedProduct.setHardDriveSize(FormatUtil.formatBytesDecimal(Long.parseLong(StringUtils.getDigits(StringUtils.split(tempHDCapacity[1], " ")[0]))));
		if (StrLookup.mapLookup(propertyMap).lookup("Device Name").contains("HDD"))
			receivedProduct.setHardDriveType("HDD");
		else if (StrLookup.mapLookup(propertyMap).lookup("Device Name").contains("SSD"))
			receivedProduct.setHardDriveType("SSD");
		else
			receivedProduct.setHardDriveType(StrLookup.mapLookup(propertyMap).lookup("Device Name").trim());

		return receivedProduct;
	}
	
	private MacWarehouseProduct parseDisplayInfo (MacWarehouseProduct receivedProduct, String displayInfo) {
		if (receivedProduct == null || displayInfo == null || displayInfo.isEmpty())
			return null;
		
		if (!displayInfo.contains("Displays:")) {
			System.out.println("Cannot find displays info in the returned string from profiler: " + displayInfo);
			return null;
		}
		
		
		Map<String, String> propertyMap = convertSystemInfoStringToMap(displayInfo);

		if (propertyMap.isEmpty())
			return null;
		
		receivedProduct.setResolution(StrLookup.mapLookup(propertyMap).lookup("Resolution").trim());
		//receivedProduct.setVRAM(StrLookup.mapLookup(propertyMap).lookup("VRAM").trim());
		receivedProduct.setGPU(StrLookup.mapLookup(propertyMap).lookup("Chipset Model").trim());

		return receivedProduct;
	}
	
	private MacWarehouseProduct parseBatteryInfo (MacWarehouseProduct receivedProduct, String batteryInfo) {
		if (receivedProduct == null || batteryInfo == null || batteryInfo.isEmpty())
			return null;
		
		if (!batteryInfo.contains("Battery:")) {
			//System.out.println("Cannot find battery info - either no battery or this is a desktop!");
			return null;
		}
		
		
		Map<String, String> propertyMap = convertSystemInfoStringToMap(batteryInfo);

		if (propertyMap.isEmpty())
			return null;
		
		receivedProduct.setBatteryCycleCount(Integer.parseInt(StrLookup.mapLookup(propertyMap).lookup("Cycle Count")));
		receivedProduct.setBatteryCondition(StrLookup.mapLookup(propertyMap).lookup("Condition"));
		
		
		receivedProduct.setSerialNumber(StrLookup.mapLookup(propertyMap).lookup("Serial Number (system)").trim());
		
		
		return receivedProduct;
	}
	
	private Map<String, String> convertSystemInfoStringToMap (String subSystemInfo) {
		String[] allProperties = subSystemInfo.split("\n");
		Map<String, String> propertyMap = new HashMap<String, String>();
		
		for (int i = 0; i< allProperties.length; i++) {
			String tempStr = allProperties[i];
			if (!tempStr.isEmpty() && tempStr.indexOf(':') != tempStr.length()-1){
				String[] propertyPair = tempStr.split(":");
				if (propertyPair.length == 2)
					propertyMap.put(propertyPair[0].trim(), propertyPair[1].trim());
			}
		}
		
		return propertyMap;
	}
	
	public String doesTheSpecsSiteWork (String serialNumber) {
		HttpURLConnection connection = null;
		//StringBuilder targetURL = new StringBuilder("https://support.apple.com/specs/C02QX39SGG78");
		StringBuilder targetURL = new StringBuilder("http://www.powerbookmedic.com/identify-mac-serial.php?serial=");
		targetURL.append(serialNumber);
		
		try {
		    //Create connection
			URL url = new URL(targetURL.toString());
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36"); // Do as if you're using Chrome 41 on Windows 7.
			
			//Get Response  
			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
			String line;
			int status = connection.getResponseCode();
			while ((line = rd.readLine()) != null) {
				  response.append(line);
				  response.append('\r');
			}
		    rd.close();
		    System.out.println(response.toString());
		    return response.toString();
		    
		    
		}
		catch(IOException ioe){
			System.out.println("IO Exception connecting to Apple support URL: " + ioe.getMessage());
		}
		finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		
		return "";
	}
	
	public String getProductSpecsFromApple (String productSerialNumber) {
		HttpURLConnection connection = null;
		StringBuilder targetURL = new StringBuilder("http://support-sp.apple.com/sp/product?cc=");
		if (!productSerialNumber.isEmpty()){
			if (productSerialNumber.length()==12)
				targetURL.append(StringUtils.right(productSerialNumber, 4));
			else if(productSerialNumber.length()==11)
				targetURL.append(StringUtils.right(productSerialNumber, 3));
			else {
				System.out.println("Serial number is NOT 11 or 12. Will try with last 3. If that doesn't work, proceed with rest");
				targetURL.append(StringUtils.right(productSerialNumber, 3));
			}
		}
		else
			return "";
		
		targetURL.append("&lang=en_US");
		try {
		    //Create connection
			URL url = new URL(targetURL.toString());
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36"); // Do as if you're using Chrome 41 on Windows 7.
			
			//Get Response  
			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
			String line;
			int status = connection.getResponseCode();
			while ((line = rd.readLine()) != null) {
				  response.append(line);
				  response.append('\r');
			}
		    rd.close();
		    return response.toString();
		}
		catch(IOException ioe){
			System.out.println("IO Exception connecting to Apple support URL: " + ioe.getMessage());
		}
		finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		
		return "";
	}

	private static class MacSystemInformation
	{
	    static String getHardwareInfo() throws IOException
	    {
	        Runtime runtime = Runtime.getRuntime();
	        Process process = runtime.exec("system_profiler -detailLevel full SPHardwareDataType");
	        BufferedReader systemInformationReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	
	        StringBuilder stringBuilder = new StringBuilder();
	        String line;
	
	        while ((line = systemInformationReader.readLine()) != null)
	        {
	            stringBuilder.append(line);
	            stringBuilder.append(System.lineSeparator());
	        }
	
	        return stringBuilder.toString().trim();
	    }
	    
	    static String getSATAStorageInfo() throws IOException {
			Runtime runtime = Runtime.getRuntime();
	        Process process = runtime.exec("system_profiler -detailLevel mini SPSerialATADataType");
	        BufferedReader systemInformationReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	
	        StringBuilder stringBuilder = new StringBuilder();
	        String line;
	
	        while ((line = systemInformationReader.readLine()) != null)
	        {
	            stringBuilder.append(line);
	            stringBuilder.append(System.lineSeparator());
	        }
	
	        return stringBuilder.toString().trim();
		}
	    
	    static String getPATAStorageInfo() throws IOException {
			Runtime runtime = Runtime.getRuntime();
	        Process process = runtime.exec("system_profiler -detailLevel mini SPParallellATADataType");
	        BufferedReader systemInformationReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	
	        StringBuilder stringBuilder = new StringBuilder();
	        String line;
	
	        while ((line = systemInformationReader.readLine()) != null)
	        {
	            stringBuilder.append(line);
	            stringBuilder.append(System.lineSeparator());
	        }
	
	        return stringBuilder.toString().trim();
		}

		static String getSoftwareInfo() throws IOException
	    {
	        Runtime runtime = Runtime.getRuntime();
	        Process process = runtime.exec("system_profiler -detailLevel full SPSoftwareDataType");
	        BufferedReader systemInformationReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	
	        StringBuilder stringBuilder = new StringBuilder();
	        String line;
	
	        while ((line = systemInformationReader.readLine()) != null)
	        {
	            stringBuilder.append(line);
	            stringBuilder.append(System.lineSeparator());
	        }
	
	        return stringBuilder.toString().trim();
	    }
	    
	    static String getDisplaysInfo() throws IOException
	    {
	        Runtime runtime = Runtime.getRuntime();
	        Process process = runtime.exec("system_profiler -detailLevel full SPDisplaysDataType");
	        BufferedReader systemInformationReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	
	        StringBuilder stringBuilder = new StringBuilder();
	        String line;
	
	        while ((line = systemInformationReader.readLine()) != null)
	        {
	            stringBuilder.append(line);
	            stringBuilder.append(System.lineSeparator());
	        }
	
	        return stringBuilder.toString().trim();
	    }
	    
	    static String getStorageInfo() throws IOException
	    {
	        Runtime runtime = Runtime.getRuntime();
	        Process process = runtime.exec("system_profiler -detailLevel full SPSerialATADataType SPParallelATADataType");
	        BufferedReader systemInformationReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	
	        StringBuilder stringBuilder = new StringBuilder();
	        String line;
	
	        while ((line = systemInformationReader.readLine()) != null)
	        {
	            stringBuilder.append(line);
	            stringBuilder.append(System.lineSeparator());
	        }
	
	        return stringBuilder.toString().trim();
	    }
	    
	    static String getBatteryInfo() throws IOException 
	    {
	    	Runtime runtime = Runtime.getRuntime();
	        Process process = runtime.exec("system_profiler -detailLevel full SPPowerDataType");
	        BufferedReader systemInformationReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	
	        StringBuilder stringBuilder = new StringBuilder();
	        String line;
	
	        while ((line = systemInformationReader.readLine()) != null)
	        {
	            stringBuilder.append(line);
	            stringBuilder.append(System.lineSeparator());
	        }
	
	        return stringBuilder.toString().trim();
	    }
	}

	public MacWarehouseProduct getDeviceInformationFromCFGUtilFile(MacWarehouseProduct receivedProduct, String iOSDataFileName) throws Exception {
		
		//For iOS devices, a file is generated with all the device info into folder specified in properties file.
		receivedProduct.setOsVersion("iOS");
		try {
			GsonBuilder builder = new GsonBuilder();
	        Gson gson = builder.create();
	        String filePath = Constants.GENERATED_FILE_PATH + iOSDataFileName;
	        IOSProductJSONFields iOSProduct = gson.fromJson(new FileReader(filePath), IOSProductJSONFields.class);
	        Set<String> keys = iOSProduct.outputData.keySet();
	        for (String key : keys) {
	        	if (!key.equalsIgnoreCase("errors")){
	        		IOSProductDataFields dataFields = iOSProduct.outputData.get(key);
	        		receivedProduct.setModelID(dataFields.deviceType);
	        		receivedProduct.setHardDriveSize(StringUtils.deleteWhitespace(FormatUtil.formatBytesDecimal(dataFields.totalDiskCapacity)));
	        		receivedProduct.setSerialNumber(dataFields.serialNumber);
	        		receivedProduct.setProductColor(dataFields.color);
	        		System.out.println("CFG specs: " + receivedProduct.getModelID() + ", Color: " + receivedProduct.getProductColor());
	        		//TODO - Get other fields
	        		//Determine if this is a cellular device or WiFi only (ICCID? or IMEI works for a carrier check, but what if there is no service)
	        		//What other fields can be used?!?!?
	        	}
	        }
	        
	        String descriptiveModelInfo = getProductSpecsFromApple(receivedProduct.getSerialNumber());
			if(!descriptiveModelInfo.isEmpty()) {
				parseDescriptiveModelInfo(receivedProduct, descriptiveModelInfo);
			}

	        return receivedProduct;
		}
		catch (Exception e) {
			System.out.println("Exception while trying to open CFGUTIL generated files: " + e.getMessage());
			throw e;
		}
		
	}


}
