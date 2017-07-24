package com.kenss.utilities;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import com.inventory.macwarehouse.MacWarehouseProduct;
 

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
	   private JButton itemSlectionButton = new JButton("Select Item");
	   private JButton noMatchOKButton = new JButton("OK");
	   
	   
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
	      itemSlectionButton.setEnabled(false);
	      
	      //Show the shortened product list for tech to choose the right product
		  DefaultListModel<String> listModel = new DefaultListModel<>();
		  if (shortenedList.size()>0) {
			for (int i=0; i<shortenedList.size(); i++)
				listModel.addElement(shortenedList.get(i).toString());
			
			  final JList<String> products = new JList<String>(listModel);
		      
		      products.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		      products.setSelectedIndex(0);
		      products.setVisibleRowCount(shortenedList.size() > 5 ? 5 : shortenedList.size());        

		      products.addMouseListener(new MouseListener() {
		    	  public void mouseClicked(MouseEvent e) {
		    		  	//Enable the selection button when an item is selected by mouse click
			            if (products.getSelectedIndex() != -1) {                     
			               itemSlectionButton.setEnabled(true);
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
		      
		      itemSlectionButton.addActionListener(new ActionListener() {
			         public void actionPerformed(ActionEvent e) { 
			            String data = "";

			            if (products.getSelectedIndex() != -1) {                     
			               data = "Item Selected: " + products.getSelectedValue(); 
			               statusLabel.setText(data);
			               mainFrame.setAlwaysOnTop(false);
			               waitingForItemSelection=false;
			            }
			         }
			      }); 
			      
			      controlPanel.add(new JScrollPane(products));    
			      controlPanel.add(itemSlectionButton);    
			      mainFrame.add(controlPanel);

   		  }
		  else {
			  statusLabel.setText("NO MATCHES. ITEM NEEDS TO BE CREATED AND ADDED TO THE MASTER DATABASE");
			  noMatchOKButton.addActionListener(new ActionListener() {
			         public void actionPerformed(ActionEvent e) { 
			        	 mainFrame.setAlwaysOnTop(false);
			             waitingForItemSelection=false;
			         }
			      });
			  //controlPanel.add(noMatchOKButton);
			  //mainFrame.add(controlPanel);
			  mainFrame.add(noMatchOKButton);
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
