package com.inventory.macwarehouse;

import java.util.Map;

import com.google.gson.annotations.SerializedName;
import com.opencsv.bean.CsvBindByName;

public class IOSProductJSONFields {
	
	
	public String Command;
	//public Object Output;
	@SerializedName("Output")
	public Map<String, IOSProductDataFields> outputData;
		
	public String Type;
	public String[] Devices;
	
	public IOSProductJSONFields() {
		// TODO Auto-generated constructor stub
		
	}

}
