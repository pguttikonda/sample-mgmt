/**
 * 
 */
package com.inventory.macwarehouse;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code39Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.kenss.utilities.Constants;

/**
 * @author pguttikonda
 *
 */
public class BarcodeGenerator {

	/**
	 * 
	 */
	public BarcodeGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	public BitMatrix generateBarcode( String contents, BarcodeFormat format, int width, int height) {
		QRCodeWriter QRcodeWriter = new QRCodeWriter();
		BitMatrix qrCode = null;
		
		if (contents != null && !contents.isEmpty()) {
			System.out.println(contents);
			try {
				qrCode = QRcodeWriter.encode(contents, format, width, height);
			}
			catch (WriterException we) {
				return null;
			}
		}
		return qrCode;
		
	}
	
	public String generateBarcodeImage (BitMatrix qrCodeMatrix, String serialNumber) throws IOException {
		//qrCodeImage = MatrixToImageWriter.toBufferedImage(qrCodeMatrix);
		String generatedImagePath = "";
		if (serialNumber == null || serialNumber.isEmpty())
			generatedImagePath = Constants.GENERATED_FILE_PATH+"barcode.jpg";
		else
			generatedImagePath = Constants.GENERATED_FILE_PATH + serialNumber + ".png";
			
		MatrixToImageWriter.writeToPath(qrCodeMatrix, "PNG", new File(generatedImagePath).toPath());
		return generatedImagePath;
	}
	
	public BufferedImage getBarcodeAsImage (BitMatrix barCodeMatrix) {
		return MatrixToImageWriter.toBufferedImage(barCodeMatrix);
	}

	public BitMatrix generateCode39Barcode(String code39String) throws Exception {
		
		Code39Writer code39Writer = new Code39Writer();
		BitMatrix code39 = null;
		if (code39String != null && !code39String.isEmpty()) {
			try {
				code39 = code39Writer.encode(code39String, BarcodeFormat.CODE_39, 80, 20);
			}
			catch (WriterException we) {
				return null;
			}
		}
		return code39;
		
	}
	
}
