package com.kenss.utilities;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.inventory.macwarehouse.MacWarehouseProduct;


public class ColorUtils {

	/**
	 * Initialize the color list that we have.
	 */
	private ArrayList<ColorName> initColorList() {
		ArrayList<ColorName> colorList = new ArrayList<ColorName>();
		colorList.add(new ColorName("Black", 0x00, 0x00, 0x00));
		colorList.add(new ColorName("Jet Black", 5, 0, 2));
		colorList.add(new ColorName("Space Gray", 59, 59, 60));
		colorList.add(new ColorName("Silver", 228, 231, 232));
		colorList.add(new ColorName("Rose Gold", 183, 110, 121));
		colorList.add(new ColorName("Gold", 247, 231, 206));
		colorList.add(new ColorName("Green", 0x00, 0x80, 0x00));
		colorList.add(new ColorName("Red", 0xFF, 0x00, 0x00));
		colorList.add(new ColorName("Blue", 0x00, 0x00, 0xFF));
		colorList.add(new ColorName("White", 0xFF, 0xFF, 0xFF));
		return colorList;
	}

	/**
	 * Get the closest color name from our list
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public String getColorNameFromRgb(int r, int g, int b) {
		ArrayList<ColorName> colorList = initColorList();
		ColorName closestMatch = null;
		int minMSE = Integer.MAX_VALUE;
		int mse;
		for (ColorName c : colorList) {
			mse = c.computeMSE(r, g, b);
			if (mse < minMSE) {
				minMSE = mse;
				closestMatch = c;
			}
		}

		if (closestMatch != null) {
			return closestMatch.getName();
		} else {
			return "No matched color name.";
		}
	}

	/**
	 * Convert hexColor to rgb, then call getColorNameFromRgb(r, g, b)
	 * 
	 * @param hexColor
	 * @return
	 */
	public String getColorNameFromHex(int hexColor) {
		int r = (hexColor & 0xFF0000) >> 16;
		int g = (hexColor & 0xFF00) >> 8;
		int b = (hexColor & 0xFF);
		System.out.println(hexColor + " - RGB: " + r + ", " + g + ", " + b);
		return getColorNameFromRgb(r, g, b);
	}

	public int colorToHex(Color c) {
		return Integer.decode("0x"
				+ Integer.toHexString(c.getRGB()).substring(2));
	}

	public String getColorNameFromColor(Color color) {
		return getColorNameFromRgb(color.getRed(), color.getGreen(),
				color.getBlue());
	}

	/**
	 * SubClass of ColorUtils. In order to lookup color name
	 */
	public class ColorName {
		public int r, g, b;
		public String name;

		public ColorName(String name, int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
			this.name = name;
		}

		public int computeMSE(int pixR, int pixG, int pixB) {
			return (int) (((pixR - r) * (pixR - r) + (pixG - g) * (pixG - g) + (pixB - b)
					* (pixB - b)) / 3);
		}

		public int getR() {
			return r;
		}

		public int getG() {
			return g;
		}

		public int getB() {
			return b;
		}

		public String getName() {
			return name;
		}
	}

	public boolean checkHexColorMapping(MacWarehouseProduct receivedProduct) {
		File colors = new File("resources/colors.txt");
    	Map<String, String> colorCodes = new HashMap<String, String>();
    	
    	try {
    	    FileReader reader = new FileReader(colors);
    	    Properties props = new Properties();
    	    props.load(reader);
    	 
    	    Enumeration<String> keys = (Enumeration<String>) props.propertyNames();
    	    String hexColorCode = StringUtils.substring(receivedProduct.getProductColor(), 1);
    	    while(keys.hasMoreElements()) {
    	    	String key = keys.nextElement();
    	    	if(key.trim().equalsIgnoreCase(hexColorCode)){
    	    		receivedProduct.setProductColor(props.getProperty(key));
    	    		return true;
    	    	}
    	    }
    	 
    	    reader.close();
    	} catch (FileNotFoundException ex) {
    	    // file does not exist
    		return false;
    	} catch (IOException ex) {
    	    // I/O error
    		return false;
    	}
    	
		return false;
	}
}