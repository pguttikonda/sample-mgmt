package com.kenss.utilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
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
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.PrinterResolution;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.inventory.macwarehouse.BarcodeGenerator;
import com.inventory.macwarehouse.MacWarehouseProduct;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Arrays;

	public class PrintUtility {

    public static final String PRINTERNAME = "DYMO LabelWriter";
	//Test printer
	//public static final String PRINTERNAME = "Canon MX920 series";
    /**
     * true if you want a menu to select the printer
     */
    public static final boolean PRINTMENU = false;
    public static String printThis[] = new String[11];
    PrinterJob printerJob = PrinterJob.getPrinterJob();
    PageFormat pageFormat = printerJob.defaultPage();
    Paper paper = new Paper();

    public void printLabel(MacWarehouseProduct receivedProduct, String barcodeFilePath) {


        final double widthPaper = (2.2* 72);
        final double heightPaper = (3.5 * 72);

        paper.setSize(widthPaper, heightPaper);
        paper.setImageableArea(0, 0, widthPaper, heightPaper);

        pageFormat.setPaper(paper);

        //pageFormat.setOrientation(PageFormat.LANDSCAPE);
        pageFormat.setOrientation(PageFormat.PORTRAIT);

     // Build a set of attributes
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        // aset.add(new Copies(2));
        aset.add(new MediaPrintableArea(0, 0, 2.3f, 3.8f,
                MediaPrintableArea.INCH));
        aset.add(PrintQuality.HIGH);
        aset.add(new PrinterResolution(300, 600, PrinterResolution.DPI));
        
        PrintService[] printService = PrinterJob.lookupPrintServices();

        for (int i = 0; i < printService.length; i++) {
            System.out.println(printService[i].getName());

            if (printService[i].getName().contains(PRINTERNAME)) {
                try {
                    printerJob.setPrintService(printService[i]);
                    printerJob.setPrintable(new Printable() {
                        @Override
                        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
                                throws PrinterException {
                            if (pageIndex < getPageNumbers()) {
                                Graphics2D g = (Graphics2D) graphics;
                                //X,Y starting position - 0,0 is top left corner
                                g.translate(0, 0);

                                String value = "";

                                int x = 0;
                                int y = 0;
                                
                                BarcodeGenerator barcodeGen = new BarcodeGenerator();
                                BufferedImage img = null;
                                try{
                                	img = ImageIO.read(new File(barcodeFilePath));
                                }
                                catch (Exception e){}
                                
                                System.out.println("Font: " + g.getFont().getFontName() + ",  Style: " + g.getFont().getStyle());
                                // Model NUmber at the top
                                g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, 13));
                                value = receivedProduct.getModelNumber();
                                //g.drawString("" + value, x + 70, y + 10);
                                g.drawString("" + value, x, y + 13);

                                // Product name
                                g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, 12));
                                value = receivedProduct.getProductType();
                                g.drawString("" + value, x, y + 28);
                                
                                //Specs - Proc speed, RAM & HD size
                                g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, 12));
                                value = receivedProduct.getProcessor() + " " + receivedProduct.getProcessorSpeed() + "/" +
                                			receivedProduct.getRamSize() + "/" + receivedProduct.getHardDriveSize();
                                g.drawString("" + value, x+5, y + 41);

                                /**********Barcode has to go here***********************/
                                //TODO add barcode
                                //g.drawImage(img, x+5, y+40, img.getWidth(), img.getHeight(), null);

                                
                                // Barcode string
                                g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 7));
                                value = receivedProduct.getSerialNumber();
                                g.drawString(value, x + 25, y + 54);
                                g.drawLine(x, y+55, x+200, y+55);
                                
                                //TODO - JDK bug is preventing images from being printed
                                //g.drawBytes(barcodeByteArray, 0, barcodeByteArray.length, x+5, y+66);
                                //g.drawImage(img, x+5, y+66, img.getWidth(), img.getHeight(), null);
                                
                                
                                // Condition strings
                                g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 10));
                                value = "Hard Drive";
                                g.drawLine(x, y + 77, x + 20, y + 77);
                                g.drawString(value, x+22, y + 77);
                                
                                value = "Speakers//Mic";
                                g.drawLine(x, y + 88, x + 20, y + 88);
                                g.drawString(value, x+22, y + 88);
                                
                                value = "Bluetooth";
                                g.drawLine(x, y + 99, x + 20, y + 99);
                                g.drawString(value, x+22, y + 99);
                                
                                value = "Wifi";
                                g.drawLine(x, y + 110, x + 20, y + 110);
                                g.drawString(value, x+22, y + 110);
                                
                                value = "Camera";
                                g.drawLine(x, y + 121, x + 20, y + 121);
                                g.drawString(value, x+22, y + 121);
                                
                                value = "USB Ports";
                                g.drawLine(x, y + 132, x + 20, y + 132);
                                g.drawString(value, x+22, y + 132);
                                
                                value = "Optical Drive";
                                g.drawLine(x, y + 143, x + 20, y + 143);
                                g.drawString(value, x+22, y + 143);
                                
                                value = "Clean OS Install";
                                g.drawLine(x, y + 154, x + 20, y + 154);
                                g.drawString(value, x+22, y + 154);
                                
                                value = "Keyboard";
                                g.drawLine(x, y + 165, x + 20, y + 165);
                                g.drawString(value, x+22, y + 165);
                                
                                value = "Trackpad";
                                g.drawLine(x, y + 176, x + 20, y + 176);
                                g.drawString(value, x+22, y + 176);
                                
                                value = "Battery,";
                                g.drawLine(x, y + 187, x + 20, y + 187);
                                g.drawString(value, x+22, y + 187);
                                
                                value = "cycles";
                                g.drawString(value, x+70, y + 187);
                                g.drawLine(x+100, y + 187, x + 110, y + 187);
                                
                                value = "Health%";
                                g.drawString(value, x+111, y + 187);
                                g.drawLine(x+150, y + 187, x + 160, y + 187);
                                
                                value = "MRI Passed";
                                g.drawLine(x, y + 198, x + 20, y + 198);
                                g.drawString(value, x+22, y + 198);
                                
                                value = "VST";
                                g.drawLine(x, y + 209, x + 20, y + 209);
                                g.drawString(value, x+22, y + 209);
                                
                                g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 8));
                                value = "Cosmetic Issues";
                                g.drawString(value, x, y + 220);
                                g.drawLine(x + 40, y + 220, x + 70, y + 220);
                                
                                value = "Functional Issues";
                                g.drawString(value, x, y + 230);
                                g.drawLine(x + 40, y + 230, x + 70, y + 230);

                                /*                                
                                // date
                                g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 7));
		                        value = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US).format(new Date());
                                value = text10;
                                g.drawString(value, x, y + 40);

                                // Customer Account Number
                                g.setFont(new Font(g.getFont().getFontName(), g.getFont().getStyle(), 7));
                                value = text11;
                                g.drawString("" + value, x + 35, y + 42);


                                InputStream ttf = this.getClass().getResourceAsStream("3of9.TTF");

                                Font font = null;
                                try {
                                    font = Font.createFont(Font.TRUETYPE_FONT, ttf);
                                } catch (FontFormatException | IOException ex) {
                                    Logger.getLogger(PrintUtility.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                g.setFont(font);

                                g.setFont(new Font(g.getFont().getFontName(), g.getFont().getStyle(), 24));
                                value = text0;

                                g.drawString(value, x, y);
                                */

                                return PAGE_EXISTS;
                            } else {
                                return NO_SUCH_PAGE;
                            }
                        }
                    }, pageFormat); // The 2nd param is necessary for printing into a label width a right landscape format.
                    printerJob.print(aset);
                } catch (PrinterException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
    public int getPageNumbers() {
        return 1;
    }
    
    public void printLabelAsPNG(MacWarehouseProduct receivedProduct) {

        final double widthPaper = (2.2* 72);
        final double heightPaper = (3.5 * 72);

        paper.setSize(widthPaper, heightPaper);
        paper.setImageableArea(0, 0, widthPaper, heightPaper);
        pageFormat.setPaper(paper);
        pageFormat.setOrientation(PageFormat.PORTRAIT);

        PrintService[] printService = PrinterJob.lookupPrintServices();
        
        BufferedImage formattedLabelImage = new BufferedImage(300, 450, BufferedImage.TYPE_INT_RGB);
        Graphics2D gPrime = formattedLabelImage.createGraphics();
        
        gPrime.setColor(Color.WHITE);
        gPrime.fillRect(0, 0, formattedLabelImage.getWidth(), formattedLabelImage.getHeight());
        
        gPrime.setColor(Color.BLACK);
        //X,Y starting position - 0,0 is top left corner
        gPrime.translate(0, 0);

        String value = "";

        int x = 0;
        int y = 0;
        
        BarcodeGenerator barcodeGen = new BarcodeGenerator();
        BufferedImage img = null;
        try{
        	BitMatrix barcode39 = barcodeGen.generateCode39Barcode(receivedProduct.getSerialNumber());
        	String barcodeImagePath = barcodeGen.generateBarcodeImage(barcode39, receivedProduct.getSerialNumber());
        	
        	img = ImageIO.read(new File(barcodeImagePath));
        }
        catch (Exception e){}
        
        
        
        // Model NUmber at the top
        gPrime.setFont(new Font("HELVETICA", Font.BOLD, 13));
        value = receivedProduct.getModelNumber();
        //g.drawString("" + value, x + 70, y + 10);
        gPrime.drawString("    " + value, x, y + 13);

        // Product name
        //g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, 12));
        value = receivedProduct.getProductType();
        gPrime.drawString("       " + value, x, y + 28);
        
        //Specs - Proc speed, RAM & HD size
        //g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, 12));
        value = receivedProduct.getProcessor() + " " + receivedProduct.getProcessorSpeed() + "/" +
        			receivedProduct.getRamSize() + "/" + receivedProduct.getHardDriveSize();
        gPrime.drawString("" + value, x+5, y + 41);

        /**********Barcode has to go here***********************/
        //TODO add barcode
        //g.drawRect(x+5, y+40, img.getWidth(), img.getHeight());
    	gPrime.drawImage(img, x+5, y+52, img.getWidth(), img.getHeight(), null);

        
        // Barcode string
        //g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 7));
        gPrime.setFont(new Font("HELVETICA", Font.BOLD, 7));
        value = receivedProduct.getSerialNumber();
        gPrime.drawString(value, x + 75, y + 84);
        gPrime.drawLine(x, y+85, x+200, y+85);
        
        //g.drawBytes(barcodeByteArray, 0, barcodeByteArray.length, x+5, y+66);
        gPrime.drawImage(img, x+5, y+87, img.getWidth(), img.getHeight(), null);
        
        
        // Condition strings
        //g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 10));
        gPrime.setFont(new Font("HELVETICA", Font.BOLD, 10));
        value = "Hard Drive";
        gPrime.drawLine(x+30, y + 110, x + 40, y + 110);
        gPrime.drawString(value, x+42, y + 110);
        
        value = "Speakers//Mic";
        gPrime.drawLine(x+30, y + 122, x + 40, y + 122);
        gPrime.drawString(value, x+42, y + 122);
        
        value = "Bluetooth";
        gPrime.drawLine(x+30, y + 133, x + 40, y + 133);
        gPrime.drawString(value, x+42, y + 133);
        
        value = "Wifi";
        gPrime.drawLine(x+30, y + 144, x + 40, y + 144);
        gPrime.drawString(value, x+42, y + 144);
        
        value = "Camera";
        gPrime.drawLine(x+30, y + 155, x + 40, y + 155);
        gPrime.drawString(value, x+42, y + 155);
        
        value = "USB Ports";
        gPrime.drawLine(x+30, y + 166, x + 40, y + 166);
        gPrime.drawString(value, x+42, y + 166);
        
        value = "Optical Drive";
        gPrime.drawLine(x+30, y + 177, x + 40, y + 177);
        gPrime.drawString(value, x+42, y + 177);
        
        value = "Clean OS Install";
        gPrime.drawLine(x+30, y + 188, x + 40, y + 188);
        gPrime.drawString(value, x+42, y + 188);
        
        value = "Keyboard";
        gPrime.drawLine(x+30, y + 199, x + 40, y + 199);
        gPrime.drawString(value, x+42, y + 199);
        
        value = "Trackpad";
        gPrime.drawLine(x+30, y + 210, x + 40, y + 210);
        gPrime.drawString(value, x+42, y + 210);
        
        value = "Battery,";
        gPrime.drawLine(x+30, y + 221, x + 40, y + 221);
        gPrime.drawString(value, x+42, y + 221);
        
        value = "cycles";
        gPrime.drawString(value, x+70, y + 221);
        gPrime.drawLine(x+100, y + 221, x + 110, y + 221);
        
        value = "Health%";
        gPrime.drawString(value, x+111, y + 221);
        gPrime.drawLine(x+150, y + 221, x + 160, y + 221);
        
        value = "MRI Passed";
        gPrime.drawLine(x+30, y + 232, x + 40, y + 232);
        gPrime.drawString(value, x+42, y + 232);
        
        value = "VST";
        gPrime.drawLine(x+30, y + 243, x + 40, y + 243);
        gPrime.drawString(value, x+42, y + 243);
        
        //g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 8));
        value = "Cosmetic Issues";
        gPrime.drawString(value, x+30, y + 255);
        gPrime.drawLine(x + 120, y + 255, x + 290, y + 255);
        
        value = "Functional Issues";
        gPrime.drawString(value, x+30, y + 266);
        gPrime.drawLine(x + 120, y + 266, x + 290, y + 266);
        
        gPrime.drawImage(img, x+120, y+280, img.getWidth(), img.getHeight(), null);

        gPrime.dispose();

        try {
			ImageIO.write(formattedLabelImage, "jpg", new File("test.jpg"));
			ImageIO.write(formattedLabelImage, "png", new File("test.png"));
			JFrame frameFromPNG = new JFrame();
			frameFromPNG.add(new JLabel(new ImageIcon(ImageIO.read(new File("test.png")))));
			printFrame(frameFromPNG);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        for (int i = 0; i < printService.length; i++) {
            System.out.println(printService[i].getName());

            if (printService[i].getName().contains(PRINTERNAME)) {
                try {
                    printerJob.setPrintService(printService[i]);
                    printerJob.setPrintable(new Printable() {
                        @Override
                        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
                                throws PrinterException {
                            if (pageIndex < getPageNumbers()) {
                                                                return PAGE_EXISTS;
                            } else {
                                return NO_SUCH_PAGE;
                            }
                        }
                    }, pageFormat); // The 2nd param is necessary for printing into a label width a right landscape format.
                    //printerJob.print(aset);
                } catch (PrinterException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	

    public void printLabelFromXML(String[] args) throws PrintException, IOException {
        PrintService[] ps = PrinterJob.lookupPrintServices();
        
        FileOutputStream outstream;
        StreamPrintService psPrinter;
        String psMimeType = "application/postscript";
        PrinterJob pj = PrinterJob.getPrinterJob();
        StringBuilder labelXML = new StringBuilder();
        
        try {
        	    //Socket echoSocket = new Socket("localhost", 41951);
        	    //PrintWriter out =
        	        //new PrintWriter(echoSocket.getOutputStream(), false);
        	    BufferedReader in =
        	        new BufferedReader(new FileReader("MW.label"));
        	    String labelLine = null;
        	    while((labelLine = in.readLine())!= null) {
        	    	labelXML.append(labelLine);
        	    }
        	    
        	    //out.flush();
        	    
        	    in.close();
        	    //out.close();
        	    //echoSocket.close();
        }
        catch (Exception e){
        	
        }
       
        PrintService myService = null;
        for (PrintService printService : ps) {
            if (printService.getName().contains("RAW")) {
                myService = printService;
                break;
            }
        }
        
        final double widthPaper = (1.821* 72);
        final double heightPaper = (3.5 * 72);

        paper.setSize(widthPaper, heightPaper);
        paper.setImageableArea(0, 0, widthPaper, heightPaper);

        pageFormat.setPaper(paper);
        pageFormat.setOrientation(PageFormat.PORTRAIT);

        if (myService == null) {
            throw new IllegalStateException("Printer not found");
        }

        //DocFlavor myFormat = new DocFlavor(DocFlavor., "UTF-8");
        DocFlavor myFormat = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        PageFormat format = new PageFormat();
        format.setOrientation(PageFormat.PORTRAIT);

        // Build a set of attributes
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        // aset.add(new Copies(2));
        aset.add(new MediaPrintableArea(0, 0, 2.3f, 3.8f,
                MediaPrintableArea.INCH));
        aset.add(PrintQuality.HIGH);
        aset.add(new PrinterResolution(300, 600, PrinterResolution.DPI));
        //Doc labelDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
        Doc labelDoc = new SimpleDoc(labelXML.toString().getBytes(), myFormat, null);
        DocPrintJob printJob = myService.createPrintJob();
        printJob.print(labelDoc, aset);
    }
    
    public void printFrame(JFrame frame) {
    	final double widthPaper = (1.821* 72);
        final double heightPaper = (3.5 * 72);

        paper.setSize(widthPaper, heightPaper);
        paper.setImageableArea(0, 0, widthPaper, heightPaper);

        pageFormat.setPaper(paper);

        //pageFormat.setOrientation(PageFormat.LANDSCAPE);
        pageFormat.setOrientation(PageFormat.PORTRAIT);


        PrintService[] printService = PrinterJob.lookupPrintServices();

        for (int i = 0; i < printService.length; i++) {
            System.out.println(printService[i].getName());

            if (printService[i].getName().contains(PRINTERNAME)) {

                try {
                    printerJob.setPrintService(printService[i]);
                    printerJob.setPrintable(new Printable() {
                        @Override
                        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
                                throws PrinterException {
                            if (pageIndex < getPageNumbers()) {
                                Graphics2D g = (Graphics2D) graphics;
                                //g.translate(20, 10);
                                g.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                                // Print the entire visible contents of a
                                // java.awt.Frame.
                                frame.printAll(g);
                                /*
                                int x = 10;
                                int y = 15;

                                InputStream ttf = this.getClass().getResourceAsStream("3of9.TTF");

                                Font font = null;
                                try {
                                    font = Font.createFont(Font.TRUETYPE_FONT, ttf);
                                } catch (FontFormatException | IOException ex) {
                                    Logger.getLogger(PrintUtility.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                g.setFont(font);

                                g.setFont(new Font(g.getFont().getFontName(), g.getFont().getStyle(), 24));
								*/
                                return PAGE_EXISTS;
                            } else {
                                return NO_SUCH_PAGE;
                            }
                        }
                    }, pageFormat); // The 2nd param is necessary for printing into a label width a right landscape format.
                    printerJob.print();
                } catch (PrinterException e) {
                    e.printStackTrace();
                }
            }
        }

    	
    }
}
