package com.kenss.utilities;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.StreamPrintService;
import javax.print.StreamPrintServiceFactory;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Date;


public class PrintReceipt
{


	PageFormat format = new PageFormat();
	Paper paper = new Paper();
	
	double paperWidth = 3;//3.25
	double paperHeight = 500.69;//11.69
	double leftMargin = 0.12;
	double rightMargin = 0.10;
	double topMargin = 0;
	double bottomMargin = 0.01;

	public void PrintReceipt() {
		paper.setSize(paperWidth * 200, paperHeight * 200);
		paper.setImageableArea(leftMargin * 200, topMargin * 200,
		     (paperWidth - leftMargin - rightMargin) * 200,
		     (paperHeight - topMargin - bottomMargin) * 200);
		
		format.setPaper(paper);
		
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		aset.add(OrientationRequested.PORTRAIT);
		//testing 
		
		PrinterJob printerJob = PrinterJob.getPrinterJob();
		Printable printable = new ReceiptPrint();
		
		format = printerJob.validatePage(format);
		printerJob.setPrintable(printable, format);
		try {
		   printerJob.print(aset);
		}
		catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	class ReceiptPrint implements Printable {
	SimpleDateFormat df = new SimpleDateFormat();
	String  receiptDetailLine ;
	public static final String pspace="               ";//15-spaces
	
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)  
	throws PrinterException 
	{
	
	df.applyPattern("dd/MM/yyyy HH:mm:ss");
	String strText =null; 
	final String LF = "\n";// text string to output
	int lineStart;           // start index of line in textarea
	int lineEnd;             // end index of line in textarea
	int lineNumber;   
	int lineCount;
	final String SPACE = "          ";//10 spaces
	final String SPACES = "         ";//9
	final String uline ="________________________________________";
	final String dline ="----------------------------------------";
	String greetings ="THANKS FOR YOUR VISIT";
	
	   Graphics2D g2d = (Graphics2D) graphics;
	Font font = new Font("MONOSPACED",Font.BOLD, 9);
	
	if (pageIndex < 0 || pageIndex >= 1) {
	      return Printable.NO_SUCH_PAGE;
	  }
	 JTextArea textarea = new JTextArea(10,40);
	
	 textarea.append(uline+"\n");
	 textarea.append("Order Ref:" +"   "+receiptDetailLine+"\n");
	 textarea.append(dline+"\n");
	 textarea.append(" Qty     Description"+SPACES+"  Price"+LF);
	 textarea.append(dline+"\n");
	
	System.out.println(2);
	
	textarea.append(LF+"       SubTotal:"+pspace+"XXXX.YY"+LF);
	textarea.append("            Gratuity:______________________"+LF);//12 space
	textarea.append("              Total:_______________________"+LF);
	textarea.append("            Signature:_____________________"+LF);
	
	textarea.append(LF+SPACES+"   Your Reciept\n"+ SPACE+greetings+LF);
	textarea.append(df.format(new Date()) + LF);
	textarea.setEditable(false);
	
	  g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
	
	
	  g2d.setFont(font);
	  lineNumber = 0;
	  lineCount = textarea.getLineCount();
	   strText = textarea.getText();
	   /*MediaTracker mt = new MediaTracker(textarea);
	   URL imageURL = null;
	   try {
	
	     imageURL = new URL(mainDirectory+"quindell.png");
	   } catch (MalformedURLException me) {
	     me.printStackTrace();
	   }
	
	 //--- Load the image and wait for it to load
	   Image image = Toolkit.getDefaultToolkit().getImage(imageURL);
	   mt.addImage(image, 0);
	   */
	
	  while (lineCount!=0){
	  try {
	
	        lineStart = textarea.getLineStartOffset(lineNumber);
	        lineEnd = textarea.getLineEndOffset(lineNumber);
	        strText = textarea.getText(lineStart, lineEnd-lineStart);
	        } catch( Exception exception) 
	        {
	            System.out.println("Printing error:" + exception);                  // have to catch BadLocationException
	        }
	
	                   g2d.drawString(strText,1,(lineNumber + 1) *18);
	                   //spacing    between lines
	                    lineNumber = lineNumber + 1;
	                     lineCount--;
	         }
	         return Printable.PAGE_EXISTS;
	     }
    }
}
