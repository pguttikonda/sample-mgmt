package com.inventory.macwarehouse;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;

import org.apache.commons.lang3.StringUtils;

import com.kenss.utilities.ListSelector;
import com.kenss.utilities.StringUtilities;
import com.opencsv.bean.CsvToBeanBuilder;

public class MacWarehouseSKU {
	
	private static List<MacWarehouseProduct> allProducts = null;
	
	private MacWarehouseProduct cleanUpAdjustData(MacWarehouseProduct receivedProduct){
		
		//Known data inconsistencies
		
		//Clean up HD size - eliminate decimals, normalize to 8/16/32/64/ & so on
		receivedProduct.setHardDriveSize(StringUtilities.adjustHDSize(receivedProduct.getHardDriveSize()));
		
		//Clean up color - change RGB values to actual color values
		
		return receivedProduct;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public void determineInternalSKUNumber(MacWarehouseProduct receivedProduct) throws Exception
	{	
		if (receivedProduct == null)
			throw new Exception("No information for product received");
	
		//Read the master internal product database to compare the received product against and assign a SKU
		if (allProducts == null) {
			allProducts = new CsvToBeanBuilder(new FileReader("product_list.csv")).withType(MacWarehouseProduct.class).build().parse();
		}
		
		if (allProducts != null)
			System.out.println(allProducts.size());

		ArrayList<MacWarehouseProduct> possibleProductMatchesFromMasterList = new ArrayList<MacWarehouseProduct>();
		
		for (MacWarehouseProduct oneProduct : allProducts) {
			if (oneProduct.getModelID() != null && oneProduct.getModelID().contains(receivedProduct.getModelID())){
					possibleProductMatchesFromMasterList.add(oneProduct);
			}
		}
		
		if (possibleProductMatchesFromMasterList.isEmpty()) {
			System.out.println("No entries in master list with the current model ID of : " + receivedProduct.getModelID());
			System.out.print("Product needs to be created and added to the Master List **********************************");
		}
		else if (possibleProductMatchesFromMasterList.size() == 1) {
			System.out.println("Found only one entry in the master product list with the current Model ID: " + receivedProduct.getModelID());
			receivedProduct.setSKU(possibleProductMatchesFromMasterList.get(0).getSKU());
			receivedProduct.setModelNumber(possibleProductMatchesFromMasterList.get(0).getModelNumber());
			receivedProduct.setProductName(possibleProductMatchesFromMasterList.get(0).getProductName());
			receivedProduct.setSpecString(possibleProductMatchesFromMasterList.get(0).getSpecString());
		}
		else {
			//There are multiple matches for this particular Model ID - find the best match by matching additional data items.
			System.out.println("Multiple matches found for this model identifier. Possible choices: " + possibleProductMatchesFromMasterList.size());
			
			//Maker sure the data items are in the same format as the data in the master product list spreadsheet
			cleanUpAdjustData(receivedProduct);
			
			ArrayList<MacWarehouseProduct> shortenedList = new ArrayList<MacWarehouseProduct>();
			//Do different things based on device Type
			switch (receivedProduct.getOsVersion()) {
			case "Mac":
				//Model ID matched. Narrow down by screen size, resolution, CPU, RAM, HD  & finally video card/screen resolution
				for (MacWarehouseProduct match : possibleProductMatchesFromMasterList) {
					if (match.getScreenSize() != null && match.getScreenSize().trim().equalsIgnoreCase(receivedProduct.getScreenSize())){
						if (match.getRamSize() != null && match.getRamSize().equalsIgnoreCase(receivedProduct.getRamSize())) {
							if (match.getProcessorSpeed() != null && match.getProcessorSpeed().equals(StringUtils.getDigits(receivedProduct.getProcessorSpeed()))) {
								System.out.println("Yaaaaaaaaaaaaaayyyyyyyyyyyy!");
								shortenedList.add(match);
							}
						}
					}
					
				}
			case "iOS":
				for (MacWarehouseProduct match : possibleProductMatchesFromMasterList) {
					if (match.getHardDriveSize() != null && match.getHardDriveSize().equalsIgnoreCase(receivedProduct.getHardDriveSize())){
						//if (match.getProductColor() != null && match.getProductColor().equalsIgnoreCase(receivedProduct.getProductColor())) {
							//if (match.getCarrier() != null && match.getCarrier().equals(StringUtils.getDigits(receivedProduct.getProcessorSpeed()))) {
								System.out.println("Yaaaaaaaaaaaaaayyyyyyyyyyyy!");
								shortenedList.add(match);
							//}
						//}
					}
					
				}
			default:
				//Better not get here!!!!
			}//switch
			
			if (shortenedList.isEmpty())
				shortenedList = possibleProductMatchesFromMasterList;
			
			/*
			for (MacWarehouseProduct match : shortenedList)
				System.out.println(match.toString());
			
			
			System.out.println("Please choose the item that matches this product. Enter 1 for the first item, 2 for the second, and so on.");
			System.out.println("If none of the items below match this product, please enter 0 ************ ");
			
			Scanner scanner = new Scanner(System.in);
			
			int position = scanner.nextInt();
			*/
			
			ListSelector listSelector = new ListSelector();
			listSelector.showList(shortenedList, receivedProduct);
		}
		
	}

}
