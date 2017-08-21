package com.inventory.macwarehouse;

import static org.junit.Assert.*;

import org.junit.Test;

import com.kenss.utilities.ColorUtils;

public class ColorCodeValuesTest {

	@Test
	public void test() {
		ColorUtils colorUtils = new ColorUtils();
		String[] names = {"Space Gray", "Silver", "Rose gold", "Gold", "Jet Black", "Blue"};
		System.out.println(names.toString());
		int[] iPhoneColors = {0x3b3b3c, 0xe4e7e8, 0xb76e79, 0xF7E7CE, 0x050002, 0x0000FF};
		for (int i : iPhoneColors) {
			System.out.println(colorUtils.getColorNameFromHex(i));
		}
		
	}

}
