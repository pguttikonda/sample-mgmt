package com.kenss.utilities;

import java.awt.Color;
import java.util.ArrayList;


public class ColorUtils {

	/**
	 * Initialize the color list that we have.
	 */
	private ArrayList<ColorName> initColorList() {
		ArrayList<ColorName> colorList = new ArrayList<ColorName>();
		colorList.add(new ColorName("White", 0xFA, 0xEB, 0xD7));
		colorList.add(new ColorName("Aqua", 0x00, 0xFF, 0xFF));
		colorList.add(new ColorName("Azure", 0xF0, 0xFF, 0xFF));
		colorList.add(new ColorName("Black", 0x00, 0x00, 0x00));
		colorList.add(new ColorName("Blue", 0x00, 0x00, 0xFF));
		colorList.add(new ColorName("Coral", 0xFF, 0x7F, 0x50));
		colorList.add(new ColorName("DarkGray", 0xA9, 0xA9, 0xA9));
		colorList.add(new ColorName("DarkSlateGray", 0x2F, 0x4F, 0x4F));
		colorList.add(new ColorName("DimGray", 0x69, 0x69, 0x69));
		colorList.add(new ColorName("Gold", 0xFF, 0xD7, 0x00));
		colorList.add(new ColorName("Gray", 0x80, 0x80, 0x80));
		colorList.add(new ColorName("Green", 0x00, 0x80, 0x00));
		colorList.add(new ColorName("LightGray", 0xD3, 0xD3, 0xD3));
		colorList.add(new ColorName("LightSlateGray", 0x77, 0x88, 0x99));
		colorList.add(new ColorName("Orange", 0xFF, 0xA5, 0x00));
		colorList.add(new ColorName("Red", 0xFF, 0x00, 0x00));
		colorList.add(new ColorName("Silver", 0xC0, 0xC0, 0xC0));
		colorList.add(new ColorName("SlateGray", 0x70, 0x80, 0x90));
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
}