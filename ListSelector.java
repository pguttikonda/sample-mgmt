package com.kenss.utilities;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;

import com.inventory.macwarehouse.MacWarehouseProduct;
import com.inventory.macwarehouse.MacWarehouseSKU;
 

public class ListSelector extends JFrame{

		/**
	 * 
	 */
	private static final long serialVersionUID = 6909548072459035497L;

		public ListSelector() {
			prepareGUI();
		}
	
	   private JFrame mainFrame; 
	   private JLabel headerLabel;
	   private JLabel statusLabel = null;
	   private JPanel controlPanel;
	   boolean waitingForItemSelection = true;
	   private JButton itemSelectionButton = new JButton("Select item & Submit");
	   private JButton noMatchOKButton = new JButton("OK");
	   
	   
	   private void prepareGUI(){
		   mainFrame = new JFrame("Choose item from the list below that matches this product's specs..");
		   mainFrame.setSize(1200,300);
		   mainFrame.setLayout(new BorderLayout(5, 10));
		   mainFrame.setLocationByPlatform(false);
		   mainFrame.setAlwaysOnTop(true);
		   mainFrame.setAutoRequestFocus(true);
		  
		   mainFrame.addWindowListener(new WindowAdapter() {
		 
		   public void windowClosing(WindowEvent windowEvent){
		        //System.exit(0);
		     }        
		   });    
		  
		   headerLabel = new JLabel("", JLabel.LEFT);    
		   statusLabel = new JLabel("",JLabel.LEFT);
		
		   controlPanel = new JPanel();
		   controlPanel.setLayout(new BorderLayout(0, 10));
		   controlPanel.setSize(100, 200);
		
		   mainFrame.add(headerLabel, BorderLayout.PAGE_START);
		  // mainFrame.add(controlPanel);
		   //mainFrame.add(statusLabel);
		   //mainFrame.setVisible(true);  
	   }
	   
	   public void showList(ArrayList<MacWarehouseProduct> shortenedList, MacWarehouseProduct receivedProduct) {           
		  
		   MacWarehouseSKU mwSku = new MacWarehouseSKU();
		   StringBuilder itemDescription = new StringBuilder("Product description: ");
			  
		  if (receivedProduct.getOsVersion().toLowerCase().contains("ios")){
			  itemDescription.append("ModelID: ")
							.append(receivedProduct.getModelID())
							.append(", Product Type: ")
							.append(receivedProduct.getProductType())
							.append(", Color: ")
							.append(receivedProduct.getProductColor())
							.append(", Capacity: ")
	  					    .append(receivedProduct.getHardDriveSize());
		 }
		 else {
			  itemDescription.append("Model ID: ")
			  					.append(receivedProduct.getModelID())
			  					.append(", Hard Drive Size: ")
			  					.append(receivedProduct.getHardDriveSize())
			  					.append(", Hard drive type: ")
			  					.append(receivedProduct.getHardDriveType())
			  					.append(", RAM: ")
			  					.append(receivedProduct.getRamSize())
			  					.append(", Screen size: ")
			  					.append(receivedProduct.getScreenSize())
			  					.append(", Product year: ")
			  					.append(receivedProduct.getProductYear())
			  					.append(", CPU: ")
			  					.append(receivedProduct.getProcessorDescription())
			  					.append(", CPU speed: ")
	  		  					.append(receivedProduct.getProcessorSpeed());
		  }
	      headerLabel.setText(itemDescription.toString());
	      headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
	      itemSelectionButton.setEnabled(false);
	      itemSelectionButton.setMaximumSize(new Dimension(50, 20));
	      
	      //Show the shortened product list for tech to choose the right product
		  DefaultListModel<String> listModel = new DefaultListModel<>();
		  if (shortenedList.size()>0) {
			for (int i=0; i<shortenedList.size(); i++) {
				String itemDisplayString = "Specs:    " + shortenedList.get(i).getSKU() + "  " + shortenedList.get(i).getModelNumber() + "        " 
												+ shortenedList.get(i).toString() + "       " + shortenedList.get(i).getProductName() + "    " + shortenedList.get(i).getProductYear();
				listModel.addElement(itemDisplayString);
			}
			
			  final JList<String> products = new JList<String>(listModel);
		      
		      products.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		      products.setSelectedIndex(-1);
		      products.setVisibleRowCount(shortenedList.size() > 5 ? 5 : shortenedList.size());        

		      products.addMouseListener(new MouseListener() {
		    	  public void mouseClicked(MouseEvent e) {
		    		  	//Enable the selection button when an item is selected by mouse click
			            if (products.getSelectedIndex() != -1) {                     
			               itemSelectionButton.setEnabled(true);
			            }
			         }

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
		      });
		      
		      itemSelectionButton.addActionListener(new ActionListener() {
			         public void actionPerformed(ActionEvent e) { 
			            String data = "";

			            if (products.getSelectedIndex() != -1) {                     
			               data = "Item Selected: " + products.getSelectedValue(); 
			               int index = products.getSelectedIndex();
			               
			               mwSku.setMatchingProductDataFields(receivedProduct, shortenedList.get(index));
			               	
							receivedProduct.setSKU(shortenedList.get(index).getSKU());
							receivedProduct.setCarrier(shortenedList.get(index).getCarrier());
							receivedProduct.setCharger(shortenedList.get(index).getCharger());
							receivedProduct.setGPU(shortenedList.get(index).getGPU());
							receivedProduct.setModelNumber(shortenedList.get(index).getModelNumber());
							receivedProduct.setProductCategory(shortenedList.get(index).getProductCategory());
							receivedProduct.setProductClass(shortenedList.get(index).getProductClass());
							receivedProduct.setProductName(shortenedList.get(index).getProductName());
							receivedProduct.setProductSubCategory(shortenedList.get(index).getProductSubCategory());
							receivedProduct.setProductType(shortenedList.get(index).getProductType());
							receivedProduct.setProductYear(shortenedList.get(index).getProductYear());
							receivedProduct.setScreenSize(shortenedList.get(index).getScreenSize());
							receivedProduct.setSpecString(shortenedList.get(index).getSpecString());
							//Set HD size, type to what's on the master list overwriting what was read from the device - this is to ensure that
							//when generated barcode is scanned, it matches the item on the master list.
							receivedProduct.setHardDriveSize(shortenedList.get(index).getHardDriveSize());
							receivedProduct.setHardDriveType(shortenedList.get(index).getHardDriveType());
							
			               statusLabel.setText(data);
			               mainFrame.setAlwaysOnTop(false);
			               waitingForItemSelection=false;

			            }
			         }
			      }); 
			      
		      	  controlPanel.add(new JLabel("Possible matches from the master ITEM DATABASE: ", JLabel.LEFT), BorderLayout.PAGE_START);
			      controlPanel.add(new JScrollPane(products), BorderLayout.CENTER);    
			      //controlPanel.add(itemSelectionButton, BorderLayout.PAGE_END);    
			      mainFrame.add(controlPanel);
			      mainFrame.add(itemSelectionButton, BorderLayout.PAGE_END);

   		  }
		  else {
			  
			  StringBuilder itemDisplayString = new StringBuilder("NO MATCHES. ITEM NEEDS TO BE CREATED AND ADDED TO THE MASTER DATABASE \n");
			  
			  if (receivedProduct.getOsVersion().toLowerCase().contains("ios")){
				  itemDisplayString.append("ModelID: ")
				  					.append(receivedProduct.getModelID())
				  					.append(", Product Type: ")
				  					.append(receivedProduct.getProductType())
				  					.append(", Color: ")
				  					.append(receivedProduct.getProductColor())
				  					.append(", Capacity: ")
				  					.append(receivedProduct.getHardDriveSize());
			  }
			  else {
				  itemDisplayString.append("Model ID: ")
				  					.append(receivedProduct.getModelID())
				  					.append(", Hard Drive Size: ")
				  					.append(receivedProduct.getHardDriveSize())
				  					.append(", Hard drive type: ")
				  					.append(receivedProduct.getHardDriveType())
				  					.append(", RAM: ")
				  					.append(receivedProduct.getRamSize())
				  					.append(", Screen size: ")
				  					.append(receivedProduct.getScreenSize())
				  					.append(", Product year: ")
				  					.append(receivedProduct.getProductYear())
				  					.append(", CPU: ")
				  					.append(receivedProduct.getProcessorDescription())
				  					.append(", CPU speed: ")
				  					.append(receivedProduct.getProcessorSpeed());
			  }
			  
			  statusLabel.setText(itemDisplayString.toString());
			  
			  noMatchOKButton.addActionListener(new ActionListener() {
			         public void actionPerformed(ActionEvent e) { 
			        	 mainFrame.setAlwaysOnTop(false);
			             waitingForItemSelection=false;
			         }
			      });
			  //controlPanel.add(noMatchOKButton);
			  //mainFrame.add(controlPanel);
			  mainFrame.add(statusLabel, BorderLayout.CENTER);
			  mainFrame.add(noMatchOKButton, BorderLayout.PAGE_END);
		  }

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
