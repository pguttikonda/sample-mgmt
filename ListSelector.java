package com.kenss.utilities;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;

import com.inventory.macwarehouse.MacWarehouseProduct;
 

public class ListSelector extends JFrame{

		public ListSelector() {
			prepareGUI();
		}
	
	   private JFrame mainFrame;
	   private JLabel headerLabel;
	   private JLabel statusLabel;
	   private JPanel controlPanel;
	   boolean waitingForItemSelection = true;
	   
	   private void prepareGUI(){
		   mainFrame = new JFrame("SKU Selector");
		   mainFrame.setSize(1600,400);
		   mainFrame.setLayout(new FlowLayout());
		   //mainFrame.setLayout(new GridLayout(3, 1));
		   mainFrame.setAlwaysOnTop(true);
		   
		  
		   mainFrame.addWindowListener(new WindowAdapter() {
		 
		   public void windowClosing(WindowEvent windowEvent){
		        System.exit(0);
		     }        
		   });    
		  
		   headerLabel = new JLabel("", JLabel.LEFT);        
		   statusLabel = new JLabel("",JLabel.LEFT);    
//		   receivedProductSpecsLabel.setSize(350,100);
		
		   controlPanel = new JPanel();
		   controlPanel.setLayout(new FlowLayout());
		   controlPanel.setSize(1400, 300);
		
		   mainFrame.add(headerLabel);
		  // mainFrame.add(controlPanel);
		   mainFrame.add(statusLabel);
		   //mainFrame.setVisible(true);  
	   }
	   
	   public void showList(ArrayList<MacWarehouseProduct> shortenedList, MacWarehouseProduct receivedProduct) {           
		   
	      headerLabel.setText("This product's Specs\n\n"); 
	      headerLabel.setText("This product's Specs\n\n" + receivedProduct.toString());
	      
	      
	      //Show the shortened product list for tech to choose the right product
		  DefaultListModel<String> listModel = new DefaultListModel<>();
		  if (shortenedList.size()>0) {
			for (int i=0; i<shortenedList.size(); i++)
				listModel.addElement(shortenedList.get(i).toString());
   		  }
	      

	      final JList<String> products = new JList<String>(listModel);
	      
	      products.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	      products.setSelectedIndex(0);
	      products.setVisibleRowCount(shortenedList.size() > 5 ? 5 : shortenedList.size());        
	      //controlPanel.add(products);

	      JButton showButton = new JButton("Select");
	     
	      showButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) { 
	            String data = "";

	            if (products.getSelectedIndex() != -1) {                     
	               data = "Fruits Selected: " + products.getSelectedValue(); 
	               statusLabel.setText(data);
	               waitingForItemSelection=false;
	            }
	            //statusLabel.setText(data);
	         }
	      }); 
	      
	      controlPanel.add(new JScrollPane(products));    
	      controlPanel.add(showButton);    
	      mainFrame.add(controlPanel);
	      //mainFrame.add(new JScrollPane(products));
	      mainFrame.setVisible(true);       
	      
	      while(waitingForItemSelection) {
	    	  try {
	    		  Thread.sleep(1000);
	    	  }
	    	  catch (InterruptedException ie) {
	    		  
	    	  }
	      }
	   }
}
