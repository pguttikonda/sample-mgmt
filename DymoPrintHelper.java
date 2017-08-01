package com.inventory.macwarehouse;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.dymo.label.framework.Framework;
import com.dymo.label.framework.Label;
import com.dymo.label.framework.LabelWriterPrintParams;
import com.dymo.label.framework.LabelWriterPrinter;
import com.dymo.label.framework.PrintJob;
import com.dymo.label.framework.PrintParams;
import com.dymo.label.framework.Printer;
//import com.dymo.label.sdk.samples.PrintLabel.ConnectivityManager;
//import com.dymo.label.sdk.samples.PrintLabel.NetworkInfo;

import jdk.nashorn.internal.runtime.Context;

public class DymoPrintHelper {
	
	private static ScriptEngine engine = null;
	private static String dymoJSAPIFile = null;  
	private static final String dymoJSFilelocation = "script.js";
	private static Framework framework_;// = new Framework(new android.context.Context());
	//private static final String dymoJSFilelocation = "http://labelwriter.com/software/dls/sdk/js/DYMO.Label.Framework.latest.js";
	//This works as well.
	//http://www.labelwriter.com/software/dls/sdk/js/DYMO.Label.Framework.2.0.2.js

	public static void printLabel(String barcodeImage, String[] additionalFields) throws ScriptException {
		
		if (engine == null)
			engine = new ScriptEngineManager().getEngineByName("nashorn");
		
		try
        {
			Printer currentPrinter = null;
			if (framework_.getPrinters().iterator().hasNext())
	        {
				
				currentPrinter = framework_.getPrinters().iterator().next();
				
	            /*
				resId = R.string.noPrinters_WifiOff;

	            ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
	            NetworkInfo wifiNetworkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	            if (wifiNetworkInfo != null && wifiNetworkInfo.isConnected()) 
	                resId = R.string.noPrinters;
	                */                        
	        }

            //final PrintLabelManager m = getManager();
            //Printer currentPrinter = m.getCurrentPrinter();
            
            // printer should be selected
            if (currentPrinter == null)
            {
                System.out.println("NO dymo printer found");
                return;
            }

            /*
            // WiFi should be turned on
            ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo wifiNetworkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiNetworkInfo == null || !wifiNetworkInfo.isConnected()) 
            {
                showMessage(getString(R.string.checkWifi), "");
                return;
            }
            */
            
            // load label depends on the printer type
            String labelName;
            String objectName;
            PrintParams printParams = null; 
            if (currentPrinter instanceof LabelWriterPrinter)
            {
                labelName = "Address.label";
                objectName = "Address";

                LabelWriterPrintParams pp = framework_.createLabelWriterPrintParams();
                //pp.setPrintQuality();
                //pp.setTwinTurboRoll(m.getTwinTurboRoll());
                printParams = pp;
            }
            else
                throw new RuntimeException("Unhandled printer type");

            Label label = framework_.openLabel(labelName);
            
            // set data
            label.setObjectText(objectName, "sfasfasfasfasf\nasdfaskfhjkhka\nasdkfadhskhfkja");
           
            // print
            final PrintJob job = label.print(currentPrinter, printParams);
            
        } catch (Exception e)
        {
            System.out.println("Exception in using dymo jar: " + e.getMessage());
        }

		/*
		if (dymoJSAPIFile == null) {
			loadDymoJavascriptFileFromURL();
		}
		
		engine.eval(dymoJSAPIFile);
		Invocable invocable = (Invocable) engine;
		
		*/
		/*
		FileReader jsFile = null;
		try {
			jsFile = new FileReader("editeddymofw.js");
		}
		catch (Exception e) {}

		try {
			engine.eval(jsFile);
			Invocable invocable = (Invocable) engine;
			Object result = invocable.invokeFunction("check1");
			System.out.println(result);
			System.out.println(result.getClass());
			/*
			//Object result = invocable.invokeFunction("dymo.label.framework.init");
			Object result = invocable.invokeFunction("dymo.label.framework.openLabelFile", "/Users/pguttikonda/Downloads/MC508 4_500.label");
			System.out.println(result);
			System.out.println(result.getClass());
			
			result = invocable.invokeFunction("dymo.label.framework.getPrinters");
			System.out.println(result);
			System.out.println(result.getClass());
			
			result = invocable.invokeFunction("dymo.label.framework.renderLabel", "/Users/pguttikonda/Downloads/MC508 4_500.label", "", "");
			System.out.println(result);
			System.out.println(result.getClass());

		}
		catch (Exception e) {}*/
		
	}
	
	public static boolean loadDymoJavascriptFileFromURL () {
		//Load the javascript file from URL;
		HttpURLConnection connection = null;
		try {
		    //Create connection
			URL url = new URL(dymoJSFilelocation);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			//connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36"); // Do as if you're using Chrome 41 on Windows 7.
			
			//Get Response  
			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
			String line;
			int status = connection.getResponseCode();
			while ((line = rd.readLine()) != null) {
				  response.append(line);
				  response.append('\r');
			}
		    rd.close();
		    System.out.println(response);
		    dymoJSAPIFile = response.toString();
		}
		catch(IOException ioe){
			System.out.println("IO Exception connecting to Apple support URL: " + ioe.getMessage());
		}
		finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		return true;
	}
	
	public static boolean loadDymoLabelFormatFromFile () {
		return true;
	}
	
}
