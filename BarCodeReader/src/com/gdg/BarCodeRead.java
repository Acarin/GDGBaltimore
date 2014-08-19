package com.gdg;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class BarCodeRead {
	public String getCode(String fileName) {
		try {
			
			InputStream barCodeInputStream = new FileInputStream(fileName);
			BufferedImage barCodeBufferedImage = ImageIO.read(barCodeInputStream);
			
			LuminanceSource source = new BufferedImageLuminanceSource(barCodeBufferedImage);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			
			Reader reader = new MultiFormatReader();
			
			Result result = reader.decode(bitmap);
			
			return result.getText();
		} catch (IOException | ChecksumException | FormatException e) {
			e.printStackTrace();
		} catch ( NotFoundException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
}
