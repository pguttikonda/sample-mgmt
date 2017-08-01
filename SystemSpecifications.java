package com.inventory.macwarehouse;

import java.util.Arrays;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import oshi.SystemInfo;
import oshi.hardware.Baseboard;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.Display;
import oshi.hardware.Firmware;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

public final class SystemSpecifications {

	public static void getSpecs (MacWarehouseProduct receivedProduct) {
	
        SystemInfo si = new SystemInfo();

        HardwareAbstractionLayer hal = si.getHardware();
        OperatingSystem os = si.getOperatingSystem();
        
        ComputerSystem computerSystem = hal.getComputerSystem();
        /*
        receivedProduct.setOsVersion(os.getVersion().toString());
        System.out.println(receivedProduct.getOsVerion());
        receivedProduct.setSerialNumber(computerSystem.getSerialNumber());
        System.out.println(receivedProduct.getSerialNumber());
        receivedProduct.setModelID(computerSystem.getModel());
        System.out.println(receivedProduct.getModelID());
        receivedProduct.setProcessorDescription(hal.getProcessor().getName());
        System.out.println(receivedProduct.getProcessorDescription());
        receivedProduct.setProcessor(hal.getProcessor().getModel());
        System.out.println(receivedProduct.getProcessor());
        receivedProduct.setRamSize( FormatUtil.formatBytes(hal.getMemory().getTotal()).trim());
        System.out.println(receivedProduct.getRamSize());
        */
        HWDiskStore[] diskStores = hal.getDiskStores();
        
        //Display[] displays = hal.getDisplays();
        
        //receivedProduct.setScreenSize(displays[0].toString());
        //System.out.println(receivedProduct.getScreenSize());
        //receivedProduct.setHardDriveSize(new Long( diskStores[0].getSize()).toString());
        if (receivedProduct.getHardDriveSize() == null || receivedProduct.getHardDriveSize().isEmpty()){
        	receivedProduct.setHardDriveSize( diskStores[0].getSize() > 0 ? StringUtils.deleteWhitespace(FormatUtil.formatBytesDecimal(diskStores[0].getSize())) : "0 GB");
        	System.out.println(receivedProduct.getHardDriveSize().trim());
        }
        
        //Set all required member variables (info) of MacWarehouseProduct
        //receivedProduct.setModelNumber(computerSystem.ge));
        /*
        final Firmware firmware = computerSystem.getFirmware();
        final Baseboard baseboard = computerSystem.getBaseboard();
       
        CentralProcessor processor = hal.getProcessor();
 
        System.out.println(processor);
        System.out.println(" " + processor.getPhysicalProcessorCount() + " physical CPU(s)");
        System.out.println(" " + processor.getLogicalProcessorCount() + " logical CPU(s)");

        System.out.println("Identifier: " + processor.getIdentifier());
        System.out.println("ProcessorID: " + processor.getProcessorID());
        System.out.println("Processor Model: " + processor.getModel());
        System.out.println("Processor Name: " + processor.getName());
        System.out.println("Processor Vendor Freq: " + FormatUtil.formatHertz(processor.getVendorFreq()));
        System.out.println("Processor Stepping: " + processor.getStepping());
        */
	}
}
