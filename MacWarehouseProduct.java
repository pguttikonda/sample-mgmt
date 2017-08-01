package com.inventory.macwarehouse;

import java.util.Iterator;

import com.opencsv.bean.CsvBindByName;

public class MacWarehouseProduct implements java.lang.Iterable<MacWarehouseProduct>{

	public MacWarehouseProduct() {
		super();
		SKU = "";
		this.productName = "";
		this.productClass = "";
		this.productCategory = "";
		this.productSubCategory = "";
		this.modelNumber = "";
		this.processor = "";
		this.processorDescription = "";
		this.processorSpeed = "";
		this.screenSize = "";
		this.ramSize = "";
		this.resolution = "";
		GPU = "";
		VRAM = "";
		this.hardDriveSize = "";
		this.hardDriveType = "";
		this.productColor = "";
		this.carrier = "";
		this.productYear = "";
		this.modelID = "";
		UPC = "";
		this.productType = "";
		this.specString = "";
		this.charger = "";
		this.noOfCores = 0;
		this.osVersion = "";
		this.serialNumber = "";
	}

	@CsvBindByName(column = "SKU", required = true)
	private String SKU;
	
	@CsvBindByName(column = "Product Name")
	private String productName;
	
	@CsvBindByName(column = "Product Class")
	private String productClass;
	
	@CsvBindByName(column = "Product Category")
	private String productCategory;
	
	@CsvBindByName(column = "Product Subcategory")
	private String productSubCategory;
	
	@CsvBindByName(column = "Product Model#")
	private String modelNumber;
	
	@CsvBindByName(column = "Processor")
	private String processor;
	
	@CsvBindByName(column = "Processor Description")
	private String processorDescription;
	
	@CsvBindByName(column = "GHz")
	private String processorSpeed;
	
	@CsvBindByName(column = "Screen Size")
	private String screenSize;
	
	@CsvBindByName(column = "RAM")
	private String ramSize;
	
	@CsvBindByName(column = "Resolution")
	private String resolution;
	
	@CsvBindByName(column = "GPU")
	private String GPU;
	
	@CsvBindByName(column = "VRAM")
	private String VRAM;
	
	@CsvBindByName(column = "HD")
	private String hardDriveSize;
	
	@CsvBindByName(column = "HD Type")
	private String hardDriveType;
	
	@CsvBindByName(column = "Color")
	private String productColor;
	
	@CsvBindByName(column = "Carrier")
	private String carrier;
	
	@CsvBindByName(column = "Year")
	private String productYear;
	
	@CsvBindByName(column = "Model ID")
	private String modelID;
	
	@CsvBindByName(column = "UPC")
	private String UPC;
	
	@CsvBindByName(column = "Type")
	private String productType;
	
	@CsvBindByName(column = "Spec")
	private String specString;
	
	@CsvBindByName(column = "Charger")
	private String charger;
	
	private int noOfCores;
	
	private String osVersion;
	
	private String serialNumber;

	public String getSKU() {
		return SKU;
	}

	public void setSKU(String sKU) {
		SKU = sKU;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductClass() {
		return productClass;
	}

	public void setProductClass(String productClass) {
		this.productClass = productClass;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getProductSubCategory() {
		return productSubCategory;
	}

	public void setProductSubCategory(String productSubCategory) {
		this.productSubCategory = productSubCategory;
	}

	public String getModelNumber() {
		return modelNumber;
	}

	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public String getProcessorDescription() {
		return processorDescription;
	}

	public void setProcessorDescription(String processorDescription) {
		this.processorDescription = processorDescription;
	}

	public String getProcessorSpeed() {
		return processorSpeed;
	}

	public void setProcessorSpeed(String processorSpeed) {
		this.processorSpeed = processorSpeed;
	}

	public String getScreenSize() {
		return screenSize;
	}

	public void setScreenSize(String screenSize) {
		this.screenSize = screenSize;
	}

	public String getRamSize() {
		return ramSize;
	}

	public void setRamSize(String ramSize) {
		this.ramSize = ramSize;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getGPU() {
		return GPU;
	}

	public void setGPU(String gPU) {
		GPU = gPU;
	}

	public String getVRAM() {
		return VRAM;
	}

	public void setVRAM(String vRAM) {
		VRAM = vRAM;
	}

	public String getHardDriveSize() {
		return hardDriveSize;
	}

	public void setHardDriveSize(String hardDriveSize) {
		this.hardDriveSize = hardDriveSize;
	}

	public String getHardDriveType() {
		return hardDriveType;
	}

	public void setHardDriveType(String hardDriveType) {
		this.hardDriveType = hardDriveType;
	}

	public String getProductColor() {
		return productColor;
	}

	public void setProductColor(String productColor) {
		this.productColor = productColor;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getProductYear() {
		return productYear;
	}

	public void setProductYear(String productYear) {
		this.productYear = productYear;
	}

	public String getModelID() {
		return modelID;
	}

	public void setModelID(String modelID) {
		this.modelID = modelID;
	}

	public String getUPC() {
		return UPC;
	}

	public void setUPC(String uPC) {
		UPC = uPC;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getSpecString() {
		return specString;
	}

	public void setSpecString(String specString) {
		this.specString = specString;
	}

	public String getCharger() {
		return charger;
	}

	public void setCharger(String charger) {
		this.charger = charger;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	@Override
	public Iterator<MacWarehouseProduct> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
		
		return new StringBuffer(this.getModelID())
				.append(" ")
				.append(this.getScreenSize())
				.append("   ")
				.append(this.hardDriveSize)
				.toString();
	}

	public int getNoOfCores() {
		return noOfCores;
	}

	public void setNoOfCores(int noOfCores) {
		this.noOfCores = noOfCores;
	}
	
	
	
	
}
