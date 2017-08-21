package com.inventory.macwarehouse;

import java.awt.Color;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;

import org.apache.commons.lang3.StringUtils;

import com.kenss.utilities.ColorUtils;
import com.kenss.utilities.Constants;
import com.kenss.utilities.ListSelector;
import com.kenss.utilities.StringUtilities;
import com.opencsv.bean.CsvToBeanBuilder;

public class MacWarehouseSKU {
	
	private static List<MacWarehouseProduct> allProducts = null;
	
	private MacWarehouseProduct cleanUpAdjustData(MacWarehouseProduct receivedProduct){
		
		//Known data inconsistencies
		
		//Clean up HD size - eliminate decimals, normalize to 8/16/32/64/ & so on
		receivedProduct.setHardDriveSize(StringUtilities.adjustHDSize(receivedProduct.getHardDriveSize()));
		
		//Map Hex colors to human readable Apple product colors
		if(StringUtils.indexOf(receivedProduct.getProductColor(), '#') > -1)
		{
			ColorUtils colorUtils = new ColorUtils();
			boolean hexColorFound = colorUtils.checkHexColorMapping(receivedProduct);
			if (!hexColorFound)
				receivedProduct.setProductColor(colorUtils.getColorNameFromColor(Color.decode(receivedProduct.getProductColor())));
			
			System.out.println("Mapped color for serial #: " + receivedProduct.getSerialNumber() + " : " + receivedProduct.getProductColor());
		}
		
		if (receivedProduct.getProcessorSpeed().contains("GHz")){
			String adjustedProcSpeed = receivedProduct.getProcessorSpeed();
			adjustedProcSpeed = StringUtils.substring(adjustedProcSpeed, 0, StringUtils.indexOf(adjustedProcSpeed, "GHz")).trim();
			receivedProduct.setProcessorSpeed(adjustedProcSpeed);
		}
		
		receivedProduct = normalizeProcessorInfoCode(receivedProduct);

		return receivedProduct;
	}
	
	private MacWarehouseProduct normalizeProcessorInfoCode (MacWarehouseProduct receivedProduct) {
		String processorDescription = StringUtils.deleteWhitespace(receivedProduct.getProcessorDescription());
		if (processorDescription.contains("Intel") || processorDescription.contains("INTEL") || processorDescription.contains("intel")) {
			int noOfCores = receivedProduct.getNoOfCores();
			if (processorDescription.contains("i3") || processorDescription.contains("I3")){
				switch (noOfCores){
				case 2:
					receivedProduct.setProcessorDescription(Constants.DUALCOREI3);
					receivedProduct.setProcessor(Constants.DUALCOREI3CODE);
					break;
				case 4:
					receivedProduct.setProcessorDescription(Constants.QUADCOREI3);
					receivedProduct.setProcessor(Constants.QUADCOREI3CODE);
				}
			}
			else if(processorDescription.contains("i5") || processorDescription.contains("I5")){
				switch (noOfCores){
				case 2:
					receivedProduct.setProcessorDescription(Constants.DUALCOREI5);
					receivedProduct.setProcessor(Constants.DUALCOREI5CODE);
					break;
				case 4:
					receivedProduct.setProcessorDescription(Constants.QUADCOREI5);
					receivedProduct.setProcessor(Constants.QUADCOREI5CODE);
				}
			}
			else if(processorDescription.contains("i7") || processorDescription.contains("I7")){
				switch (noOfCores){
				case 2:
					receivedProduct.setProcessorDescription(Constants.DUALCOREI7);
					receivedProduct.setProcessor(Constants.DUALCOREI7CODE);
					break;
				case 4:
					receivedProduct.setProcessorDescription(Constants.QUADCOREI7);
					receivedProduct.setProcessor(Constants.QUADCOREI7CODE);
				}
			}
			else if (processorDescription.contains("Core2Duo") || processorDescription.contains("core2duo")){
				receivedProduct.setProcessorDescription(Constants.CORE2DUO);
				receivedProduct.setProcessor(Constants.CORE2DUOCODE);
			}
			else if (processorDescription.contains("CoreDuo") || processorDescription.contains("coreduo")){
				receivedProduct.setProcessorDescription(Constants.COREDUO);
				receivedProduct.setProcessor(Constants.COREDUOCODE);
			}
			else if (processorDescription.contains("C2Extreme") || processorDescription.contains("C2extreme") || processorDescription.contains("c2extreme")){
				receivedProduct.setProcessorDescription(Constants.C2EXTREME);
				receivedProduct.setProcessor(Constants.C2EXTREMECODE);
			}
			else if (processorDescription.contains("Xeon6Core") || processorDescription.contains("xeon6core")){
				receivedProduct.setProcessorDescription(Constants.QUADXEON6CORE);
				receivedProduct.setProcessor(Constants.QUADXEON6CORECODE);
			}
			else if (processorDescription.contains("CoreM") || processorDescription.contains("coreM") || processorDescription.contains("corem")){
				switch (noOfCores) {
				case 2:
					receivedProduct.setProcessorDescription(Constants.DUALCOREM);
					receivedProduct.setProcessor(Constants.DUALCOREMCODE);
					break;
				case 4:
					receivedProduct.setProcessorDescription(Constants.QUADCOREM);
					receivedProduct.setProcessor(Constants.QUADCOREMCODE);
					break;
				}
			}
			else if (processorDescription.contains("Xeon8Core") || processorDescription.contains("Xeon8core") || processorDescription.contains("xeon8core")){
				receivedProduct.setProcessorDescription(Constants.XEON8CORE);
				receivedProduct.setProcessor(Constants.XEON8CORECODE);
			}
			else if (processorDescription.contains("CoreiXeon") || processorDescription.contains("coreIXeon") || processorDescription.contains("coreixeon")){
				receivedProduct.setProcessorDescription(Constants.QUADCOREIXEON);
				receivedProduct.setProcessor(Constants.QUADCOREIXEONCODE);
			}
			
		}
		else if (processorDescription.contains("A4")){
			receivedProduct.setProcessorDescription(Constants.A4CHIP);
			receivedProduct.setProcessor(Constants.A4CHIPCODE);
		}
		else if (processorDescription.contains("A5")){
			if (processorDescription.contains("A5X")){
				receivedProduct.setProcessorDescription(Constants.A5XCHIP);
				receivedProduct.setProcessor(Constants.A5XCHIPCODE);
			}
			else {
				receivedProduct.setProcessorDescription(Constants.A5CHIP);
				receivedProduct.setProcessor(Constants.A5CHIPCODE);
			}
		}
		else if (processorDescription.contains("A6")){
			if (processorDescription.contains("A6X")){
				receivedProduct.setProcessorDescription(Constants.A6XCHIP);
				receivedProduct.setProcessor(Constants.A6XCHIPCODE);
			}
			else {
				receivedProduct.setProcessorDescription(Constants.A6CHIP);
				receivedProduct.setProcessor(Constants.A6CHIPCODE);
			}
		}
		else if (processorDescription.contains("A7")){
			if (processorDescription.contains("A7X")){
				receivedProduct.setProcessorDescription(Constants.A7XCHIP);
				receivedProduct.setProcessor(Constants.A7XCHIPCODE);
			}
			else {
				receivedProduct.setProcessorDescription(Constants.A7CHIP);
				receivedProduct.setProcessor(Constants.A7CHIPCODE);
			}
		}
		else if (processorDescription.contains("A8")){
			if (processorDescription.contains("A8X")){
				receivedProduct.setProcessorDescription(Constants.A8XCHIP);
				receivedProduct.setProcessor(Constants.A8XCHIPCODE);
			}
			else {
				receivedProduct.setProcessorDescription(Constants.A8CHIP);
				receivedProduct.setProcessor(Constants.A8CHIPCODE);
			}
		}
		else if (processorDescription.contains("A9")){
			if (processorDescription.contains("A9X")){
				receivedProduct.setProcessorDescription(Constants.A9XCHIP);
				receivedProduct.setProcessor(Constants.A9XCHIPCODE);
			}
			else {
				receivedProduct.setProcessorDescription(Constants.A9CHIP);
				receivedProduct.setProcessor(Constants.A9CHIPCODE);
			}
		}
		else if (processorDescription.contains("S1")){
			if (processorDescription.contains("S1P")){
				receivedProduct.setProcessorDescription(Constants.S1PCHIP);
				receivedProduct.setProcessor(Constants.S1PCHIPCODE);
			}
			else {
				receivedProduct.setProcessorDescription(Constants.S1CHIP);
				receivedProduct.setProcessor(Constants.S1CHIPCODE);
			}
		}
		else if (processorDescription.contains("PowerPC") || (processorDescription.contains("Power PC"))){
			receivedProduct.setProcessorDescription(Constants.POWERPC970FX);
			receivedProduct.setProcessor(Constants.POWERPC970FXCODE);
		}
		
		
		return receivedProduct;
	}
	
	private MacWarehouseProduct adjustProcessorSpeedString(MacWarehouseProduct receivedProduct) {
		if (receivedProduct.getProcessorSpeed().contains("GHz")){
			String adjustedProcSpeed = receivedProduct.getProcessorSpeed();
			adjustedProcSpeed = StringUtils.substring(adjustedProcSpeed, 0, StringUtils.indexOf(adjustedProcSpeed, "GHz")).trim();
			receivedProduct.setProcessorSpeed(adjustedProcSpeed);
		}
		return receivedProduct;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public void determineInternalSKUNumber(MacWarehouseProduct receivedProduct) throws Exception
	{	
		if (receivedProduct == null)
			throw new Exception("No information for product received");
	
		//Read the master internal product database to compare the received product against and assign a SKU
		if (allProducts == null) {
			//allProducts = new CsvToBeanBuilder<MacWarehouseProduct>(new FileReader("product_list.csv")).withType(MacWarehouseProduct.class).build().parse();
			allProducts = new CsvToBeanBuilder<MacWarehouseProduct>(new FileReader(Constants.MASTER_PRODUCT_FILE_NAME)).withType(MacWarehouseProduct.class).build().parse();
		}
		
		if (allProducts != null) {
			List<MacWarehouseProduct> tmpProductList = new ArrayList<MacWarehouseProduct>();
			
			//Filter 'B' skus - products NOT in GOOD cosmetic condition
			for (MacWarehouseProduct tmp : allProducts) {
				if (tmp.getSKU().endsWith("A")){
					tmpProductList.add(tmp);
				}
			}
			allProducts = tmpProductList;
		}
		
		//Remove all products in the Master list that are for less-than=perfect body condition (so-called "B Skus")

		ArrayList<MacWarehouseProduct> possibleProductMatchesFromMasterList = new ArrayList<MacWarehouseProduct>();
		
		for (MacWarehouseProduct oneProduct : allProducts) {
			if (oneProduct.getModelID() != null && oneProduct.getModelID().contains(receivedProduct.getModelID())){
					possibleProductMatchesFromMasterList.add(oneProduct);
			}
		}
		
		if (possibleProductMatchesFromMasterList.isEmpty()) {
			System.out.println("No entries in master list with the current model ID of : " + receivedProduct.getModelID());
			System.out.println("Product needs to be created and added to the Master List **********************************");
			System.out.println("Specs:    " + receivedProduct.toString() + ", Color:  " + receivedProduct.getProductColor() + ",   Processor: " + receivedProduct.getProcessorDescription() +
					", Hard Drive Type: " + receivedProduct.getHardDriveType());
		}
		else {
			//There are multiple matches for this particular Model ID - find the best match by matching additional data items.
			//Maker sure the data items are in the same format as the data in the master product list spreadsheet
			cleanUpAdjustData(receivedProduct);
			
			ArrayList<MacWarehouseProduct> shortenedList = new ArrayList<MacWarehouseProduct>();
			//Do different things based on device Type
			if (receivedProduct.getOsVersion().toLowerCase().contains("mac")) {
				//Model ID matched. Narrow down by screen size, resolution, CPU, RAM, HD  & finally video card/screen resolution
				for (MacWarehouseProduct match : possibleProductMatchesFromMasterList) {
					if (doesScreenSizeMatch(receivedProduct, match)) {
						if (doesRAMSizeMatch(receivedProduct, match)) {
							if(doesHardDriveTypeMatch(receivedProduct, match)) {
								if(doesHardDriveSizeMatch(receivedProduct, match)) {
									if (match.getProcessor() != null && match.getProcessor().equalsIgnoreCase(receivedProduct.getProcessor())) {
										if (match.getProcessorSpeed() != null && match.getProcessorSpeed().equals(receivedProduct.getProcessorSpeed())) {
											shortenedList.add(match);
										}
									}
								}
							}
						}
					}
					
				}
			}//if OS = MacOS
			else {
				for (MacWarehouseProduct match : possibleProductMatchesFromMasterList) {
					if (doesHardDriveSizeMatch(receivedProduct, match)){
						if (match.getProductColor() != null && match.getProductColor().equalsIgnoreCase(receivedProduct.getProductColor())) {
							shortenedList.add(match);
						}
					}
				}
			}//else OS = IOS
			
			if (shortenedList.size() == 1){
				receivedProduct = this.setMatchingProductDataFields(receivedProduct, shortenedList.get(0));
				/*
				//Found an exact match. Assign the rest of the data fields!!!!!
				receivedProduct.setSKU(shortenedList.get(0).getSKU());
				receivedProduct.setCarrier(shortenedList.get(0).getCarrier());
				receivedProduct.setCharger(shortenedList.get(0).getCharger());
				receivedProduct.setGPU(shortenedList.get(0).getGPU());
				receivedProduct.setModelNumber(shortenedList.get(0).getModelNumber());
				receivedProduct.setProductCategory(shortenedList.get(0).getProductCategory());
				receivedProduct.setProductClass(shortenedList.get(0).getProductClass());
				receivedProduct.setProductName(shortenedList.get(0).getProductName());
				receivedProduct.setProductSubCategory(shortenedList.get(0).getProductSubCategory());
				receivedProduct.setProductType(shortenedList.get(0).getProductType());
				receivedProduct.setProductYear(shortenedList.get(0).getProductYear());
				receivedProduct.setScreenSize(shortenedList.get(0).getScreenSize());
				receivedProduct.setSpecString(shortenedList.get(0).getSpecString());
				//Set HD size, type to what's on the master list overwriting what was read from the device - this is to ensure that
				//when generated barcode is scanned, it matches the item on the master list.
				receivedProduct.setHardDriveSize(shortenedList.get(0).getHardDriveSize());
				receivedProduct.setHardDriveType(shortenedList.get(0).getHardDriveType());
				*/
				System.out.println("Specs:    " + receivedProduct.getSKU() + "  " + receivedProduct.getModelNumber() + "        " + receivedProduct.toString() + "       " + receivedProduct.getProductName() + "    " + receivedProduct.getProductYear());
			}
			else {
				//Found more than 1 match - Need human selection of appropriate item match. Launch the selection box
				ListSelector listSelector = new ListSelector();
				listSelector.showList(shortenedList, receivedProduct);
			}
		}
		
	}
	
	public MacWarehouseProduct setMatchingProductDataFields(MacWarehouseProduct receivedProduct, MacWarehouseProduct matchedItemFromMaster) {
		
		receivedProduct.setSKU(matchedItemFromMaster.getSKU());
		receivedProduct.setCarrier(matchedItemFromMaster.getCarrier());
		receivedProduct.setCharger(matchedItemFromMaster.getCharger());
		receivedProduct.setGPU(matchedItemFromMaster.getGPU());
		receivedProduct.setModelNumber(matchedItemFromMaster.getModelNumber());
		receivedProduct.setProductCategory(matchedItemFromMaster.getProductCategory());
		receivedProduct.setProductClass(matchedItemFromMaster.getProductClass());
		receivedProduct.setProductName(matchedItemFromMaster.getProductName());
		receivedProduct.setProductSubCategory(matchedItemFromMaster.getProductSubCategory());
		receivedProduct.setProductType(matchedItemFromMaster.getProductType());
		receivedProduct.setProductYear(matchedItemFromMaster.getProductYear());
		receivedProduct.setScreenSize(matchedItemFromMaster.getScreenSize());
		receivedProduct.setSpecString(matchedItemFromMaster.getSpecString());
		//Set HD size, type to what's on the master list overwriting what was read from the device - this is to ensure that
		//when generated barcode is scanned, it matches the item on the master list.
		receivedProduct.setHardDriveSize(matchedItemFromMaster.getHardDriveSize());
		receivedProduct.setHardDriveType(matchedItemFromMaster.getHardDriveType());
		
		return receivedProduct;
	}

	private boolean doesHardDriveSizeMatch(MacWarehouseProduct receivedProduct, MacWarehouseProduct match) {
		if (match.getHardDriveSize() != null && match.getHardDriveSize().equalsIgnoreCase(receivedProduct.getHardDriveSize()))
			return true; //Values match, nothing more to do than return!
		
		//If the hard drive type is SSD, the drive capacity reported by MacOS is way lower than expected - 122GB vs 128GB actual, for example
		//In this case, see if transforming the value into 2 power of 'n' notation helps
		try {
			//Get the units - last 2 characters of the value - 'MB', 'GB', 'TB', etc.
			String memoryUnit = StringUtils.right(receivedProduct.getHardDriveSize(), 2);
			//Capacity value starts at 0th position & ends at length - 2 (2 chars for memory unit at the end)
			float capacity = Float.parseFloat(StringUtils.substring(receivedProduct.getHardDriveSize(), 0, receivedProduct.getHardDriveSize().length()-2));
			
			if (receivedProduct.getHardDriveType().equalsIgnoreCase("SSD")){
				
				if (capacity > 0) {
					double log = Math.log(capacity) / Math.log(2);
					long roundLog = Math.round(log);
					capacity = (int) Math.pow(2, roundLog);
				}
				String adjustedHDSize = capacity + memoryUnit;
				
				if (match.getHardDriveSize() != null && match.getHardDriveSize().equalsIgnoreCase(adjustedHDSize)){
					//SSD capacities are usually exponents of 2, but the values are underreported. Reset the value of HD size and return true as a match has been found
					return true;
				}
			}
			
			//If an exact match has not been found, check to see if the difference in capacities are acceptable - this is again due to how the 
			//HD capacity value is reported by Mac & iOS - reported value is always lower than actual advertised HD capacity
			String spreadsheetItemMemoryUnit = StringUtils.right(match.getHardDriveSize(), 2);
			float spreadSheetItemCapacity = Float.parseFloat(StringUtils.substring(match.getHardDriveSize(), 0, match.getHardDriveSize().length()-2));
			
			//Read the original value of receivedProduct's hard drive size
			capacity = Float.parseFloat(StringUtils.substring(receivedProduct.getHardDriveSize(), 0, receivedProduct.getHardDriveSize().length()-2));
			
			if (memoryUnit.equalsIgnoreCase(spreadsheetItemMemoryUnit)) {
				//This assumes a 'tolerance' value of 10%
				if(Math.abs(capacity - spreadSheetItemCapacity) <= capacity/10f)
					return true;
			}
			
		}
		catch (Exception e) {
			System.out.println("Error while trying to adjust HD data to match master spreadsheet : " + e.getMessage());
		}
		
		return false;
	}

	private boolean doesHardDriveTypeMatch(MacWarehouseProduct receivedProduct, MacWarehouseProduct match) {
		if (match.getHardDriveType() != null && match.getHardDriveType().trim().equalsIgnoreCase(receivedProduct.getHardDriveType().trim()))
			return true;
		
		//Sometimes data in the spreadsheet for hard drive type has values like "SSD+No SD", and when comparing a type of SSD
		//doesn't match. Checking to see if the type of HDD or SSD against the spreadsheet values will fix that.
		if (match.getHardDriveType() != null && match.getHardDriveType().trim().contains((receivedProduct.getHardDriveType())))
			return true;
		
		//SSD & Flash are used interchangeably. This app only uses HDD or SSD for hard drive type
		if (receivedProduct.getHardDriveType().equalsIgnoreCase("SSD")){
			if (match.getHardDriveType().toLowerCase().contains("flash"))
				//Both have the same hard drive type. one has a value of SSD, the other "Flash". 
				return true;
		}
			
		return false;
	}

	private boolean doesRAMSizeMatch(MacWarehouseProduct receivedProduct, MacWarehouseProduct match) {
		// TODO Auto-generated method stub
		return (match.getRamSize() != null && match.getRamSize().equalsIgnoreCase(receivedProduct.getRamSize()));
	}

	private boolean doesScreenSizeMatch(MacWarehouseProduct receivedProduct, MacWarehouseProduct match) {

		if (match.getScreenSize() != null && match.getScreenSize().trim().equalsIgnoreCase(receivedProduct.getScreenSize()))
			return true;
		
		//Sometimes there are discrepencies between what is listed in the master spreadsheet & what Apple uses in the product name
		//For example, MacBook Pro 13" has a screen size of 13.3" in the spreadsheet. 13" != 13.3", but in reality, they should match!
		int matchScreenSizeValue = (int) Float.parseFloat(match.getScreenSize());
		int thisProductScreenSize = (int) Float.parseFloat(receivedProduct.getScreenSize());
		
		if (matchScreenSizeValue == thisProductScreenSize)
			return true;
		
		return false;
	}

}
