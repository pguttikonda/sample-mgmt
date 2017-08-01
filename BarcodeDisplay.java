package com.inventory.macwarehouse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.LuminanceSource;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.kenss.utilities.PrintUtility;

final class BarcodeDisplay {

	public static void show(String filePath, MacWarehouseProduct receivedProduct) {
		System.out.println("Displaying generated barcode");
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

		topPanel.setAlignmentY(150f);
		
		
		BarcodeGenerator labelBarcodes = new BarcodeGenerator();
		//ImageIcon img = new ImageIcon(labelBarcodes.getBarcodeAsImage(labelBarcodes.generateBarcode(receivedProduct.getSerialNumber(), BarcodeFormat.QR_CODE, 60, 15)));
		ImageIcon img = null;
		try {
		img = new ImageIcon(ImageIO.read(new File(filePath)));
		}
		catch (Exception e){}
		/*
		try {
			img = new ImageIcon(labelBarcodes.generateCode39BarcodeAsImage(receivedProduct.getSerialNumber(), 80, 20));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		JLabel label6 = new JLabel();
		label6.setHorizontalAlignment(SwingConstants.CENTER);
		label6.setVerticalAlignment(SwingConstants.CENTER);
		label6.setText("Serial     			" + receivedProduct.getSerialNumber());
		topPanel.add(label6);
		JLabel label5 = new JLabel(img);
		label5.setHorizontalAlignment(SwingConstants.CENTER);
		label5.setVerticalAlignment(SwingConstants.CENTER);
		topPanel.add(label5, SwingConstants.CENTER);
		JLabel label4 = new JLabel();
		label4.setHorizontalAlignment(SwingConstants.CENTER);
		label4.setVerticalAlignment(SwingConstants.CENTER);
		label4.setText(receivedProduct.getSpecString());
		topPanel.add(label4, SwingConstants.CENTER);
		JLabel label3 = new JLabel();
		label3.setHorizontalAlignment(SwingConstants.CENTER);
		label3.setVerticalAlignment(SwingConstants.CENTER);
		label3.setText(receivedProduct.getProductType());
		topPanel.add(label3, SwingConstants.CENTER);
		JLabel label2 = new JLabel();
		label2.setHorizontalAlignment(SwingConstants.CENTER);
		label2.setVerticalAlignment(SwingConstants.CENTER);
		label2.setText(receivedProduct.getModelNumber());
		topPanel.add(label2, SwingConstants.CENTER);

				
		StringBuilder centerPanelText = new StringBuilder();
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setBackground(Color.WHITE);
		centerPanel.setForeground(Color.WHITE);
		
		JLabel label23 = new JLabel();
		label23.setHorizontalAlignment(SwingConstants.CENTER);
		label23.setText("____ MRI Passed");
		centerPanel.add(label23, SwingConstants.CENTER);

		JLabel label22 = new JLabel();
		label22.setHorizontalAlignment(SwingConstants.CENTER);
		label22.setText("____ Battery,cycles___ Health%___");
		centerPanel.add(label22, SwingConstants.CENTER);

		JLabel label21 = new JLabel();
		label21.setHorizontalAlignment(SwingConstants.CENTER);
		label21.setText("____ Trackpad");
		centerPanel.add(label21, SwingConstants.CENTER);

		JLabel label20 = new JLabel();
		label20.setHorizontalAlignment(SwingConstants.CENTER);
		label20.setText("____ Keyboard");
		centerPanel.add(label20, SwingConstants.CENTER);
		
		JLabel label19 = new JLabel();
		label19.setHorizontalAlignment(SwingConstants.CENTER);
		label19.setText("____ Clean OS Install");
		centerPanel.add(label19, SwingConstants.CENTER);
		
		
		JLabel label18 = new JLabel();
		label18.setHorizontalAlignment(SwingConstants.CENTER);
		label18.setText("____ Optical Drive");
		centerPanel.add(label18, SwingConstants.CENTER);
		
		JLabel label17 = new JLabel();
		label17.setHorizontalAlignment(SwingConstants.CENTER);
		label17.setText("____ USB Ports");
		centerPanel.add(label17, SwingConstants.CENTER);
		
		JLabel label16 = new JLabel();
		label16.setHorizontalAlignment(SwingConstants.CENTER);
		label16.setText("____ Camera");
		centerPanel.add(label16, SwingConstants.CENTER);
		
		JLabel label14 = new JLabel();
		label14.setHorizontalAlignment(SwingConstants.CENTER);
		label14.setText("____ WiFi");
		centerPanel.add(label14, SwingConstants.CENTER);
		
		JLabel label13 = new JLabel();
		label13.setHorizontalAlignment(SwingConstants.CENTER);
		label13.setText("____ Bluetooth");
		centerPanel.add(label13, SwingConstants.CENTER);

		JLabel label12 = new JLabel();
		label12.setHorizontalAlignment(SwingConstants.CENTER);
		label12.setText("____ Speakers/Mic");
		centerPanel.add(label12, SwingConstants.CENTER);
		
		JLabel label11 = new JLabel();
		label11.setHorizontalAlignment(SwingConstants.CENTER);
		label11.setText("____ Hard Drive");
		centerPanel.add(label11, SwingConstants.CENTER);
		
		JLabel label15 = new JLabel(img);
		label15.setHorizontalAlignment(SwingConstants.CENTER);
		//centerPanel.add(label15, SwingConstants.CENTER);

		
		centerPanelText.append("     ____ VST\n");
		centerPanelText.append("Cosmetic Issues: ____________________________ \n");
		centerPanelText.append("Functional Issues: ____________________________ \n");
		//centerPanel.setBounds(x, y, width, height);
		
		try {
			BufferedImage img1 = 
					labelBarcodes.getBarcodeAsImage(labelBarcodes.generateCode39Barcode(receivedProduct.getSKU()));

			//img = new ImageIcon(img1);
			// Rotation information
			double rotationRequired = Math.toRadians (90);
			double locationX = img1.getWidth()/2;
			double locationY = img1.getHeight()/2;
			AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			BufferedImage tmp = op.filter(img1, null);
			img = new ImageIcon(tmp);

			// Drawing the rotated image at the required drawing locations
			//leftPanel.getGraphics().drawImage(tmp, 5, tmp.getHeight(), null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));
		leftPanel.setBackground(Color.WHITE);
		leftPanel.add(new JLabel());
		//leftPanel.add(new JLabel(img));
		
		
		ImageIcon icon = new ImageIcon(filePath);
		JLabel label = new JLabel(icon);
		JLabel imageDetail = new JLabel(receivedProduct.getSpecString());
		imageDetail.setBackground(Color.WHITE);
		imageDetail.setHorizontalAlignment(SwingConstants.CENTER);
		frame.setTitle("Barcode for: " + receivedProduct.getSerialNumber());
		//frame.add(label, BorderLayout.CENTER);
		//frame.add(imageDetail, BorderLayout.PAGE_END);
		//Add all the seperate pieces to the main frame
		frame.add(topPanel, BorderLayout.PAGE_START);
		frame.add(centerPanel, BorderLayout.CENTER);
		//frame.add(leftPanel, BorderLayout.LINE_START);
		//frame.add(imageDetail, BorderLayout.PAGE_END);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.pack();
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
	}
	
}
