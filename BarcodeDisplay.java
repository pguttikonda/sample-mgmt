package com.inventory.macwarehouse;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

final class BarcodeDisplay {

	public static void show(String filePath) {
		System.out.println("Displaying generated barcode");
		JFrame frame = new JFrame();
		  ImageIcon icon = new ImageIcon(filePath);
		  JLabel label = new JLabel(icon);
		  frame.add(label);
		  frame.setDefaultCloseOperation
		         (JFrame.EXIT_ON_CLOSE);
		  frame.pack();
		  frame.setVisible(true);
	}
	
}
