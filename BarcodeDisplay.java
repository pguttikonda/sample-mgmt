package com.inventory.macwarehouse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.LuminanceSource;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.kenss.utilities.Constants;
import com.kenss.utilities.PrintUtility;

final class BarcodeDisplay {

	public static void show(String filePath, MacWarehouseProduct receivedProduct) {
		JFrame frame = new JFrame();
		frame.setSize(300, 450);
		frame.setLayout(new BorderLayout());
		frame.setBackground(Color.WHITE);
		frame.setForeground(Color.BLACK);
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setSize(frame.getWidth(), 100);
		topPanel.setBackground(Color.WHITE);
		topPanel.setForeground(Color.BLACK);
		
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			 
			   public void windowClosing(WindowEvent windowEvent){
			        //System.exit(0);
			     }        
			   });
		
		ImageIcon img = null;
		try {
		img = new ImageIcon(ImageIO.read(new File(filePath)));
		}
		catch (Exception e){}

		JLabel label6 = new JLabel();
		label6.setHorizontalAlignment(SwingConstants.CENTER);
		label6.setVerticalAlignment(SwingConstants.CENTER);
		label6.setText("Serial     			" + receivedProduct.getSerialNumber());
		topPanel.add(label6);
		JLabel label2 = new JLabel();
		label2.setHorizontalAlignment(SwingConstants.CENTER);
		label2.setVerticalAlignment(SwingConstants.CENTER);
		label2.setText(receivedProduct.getModelNumber());
		topPanel.add(label2, SwingConstants.CENTER);

				
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setBackground(Color.WHITE);
		centerPanel.setForeground(Color.WHITE);
		
		JLabel label15 = new JLabel(img);
		label15.setHorizontalAlignment(SwingConstants.CENTER);
		centerPanel.add(label15, SwingConstants.CENTER);

		
		JLabel imageDetail = new JLabel(receivedProduct.getSpecString());
		imageDetail.setBackground(Color.WHITE);
		imageDetail.setHorizontalAlignment(SwingConstants.CENTER);
		frame.setTitle("Barcode for: " + receivedProduct.getSerialNumber());
		//Add all the seperate pieces to the main frame
		frame.add(topPanel, BorderLayout.PAGE_START);
		frame.add(centerPanel, BorderLayout.CENTER);
		
		JButton okButton = new JButton();
		okButton.setText("Close");
		frame.add(okButton, BorderLayout.PAGE_END);
		
		okButton.addKeyListener(new KeyListener() {
			 
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				Constants.BARCODE_TIMER = 0;
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				frame.setVisible(false);
				Constants.BARCODE_TIMER = 0;
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
			}        
	   });
		
		
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setAlwaysOnTop(true);
		//frame.setFocusable(true);
		frame.setVisible(true);
		
		//Sleep for a second first, and then keep incrementing 10secs every time.
		int timer = 1000;
		while (timer < Constants.BARCODE_TIMER) {
			try {
	  		  Thread.sleep(timer);
	  		  timer += 1000;
		  	}
		  	catch (InterruptedException ie) {
		  		  
		  	}
		}
	}
	
}
