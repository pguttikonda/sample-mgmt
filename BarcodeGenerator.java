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
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

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
			try {
				qrCode = QRcodeWriter.encode(contents, format, width, height);
			}
			catch (WriterException we) {
				return null;
			}
		}
		return qrCode;
		
	}
	
	public String generateBarcodeImage (BitMatrix qrCodeMatrix) throws IOException {
		//BufferedImage qrCodeImage = null;
				
		//qrCodeImage = MatrixToImageWriter.toBufferedImage(qrCodeMatrix);
		String generatedImagePath = "barcode.jpg";
		MatrixToImageWriter.writeToPath(qrCodeMatrix, "JPEG", new File(generatedImagePath).toPath());
		return generatedImagePath;
	}

}
