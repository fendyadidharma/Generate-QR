package com.flashiz.qrgen;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class QRGenerator {

	public BufferedImage getQR(String invoiceID)
	  {
	    BufferedImage combined = null;
	    try {
	      BufferedImage qrImage = generate(invoiceID);
	      BufferedImage frameImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("com/flashiz/qrgen/flashiz_frame.png"));
	      int deltaHeight = frameImage.getHeight() - qrImage.getHeight();
	      int deltaWidth = frameImage.getWidth() - qrImage.getWidth();

	      combined = new BufferedImage(frameImage.getWidth(), frameImage.getHeight(), 2);
	      Graphics2D g = (Graphics2D)combined.getGraphics();
	      g.drawImage(frameImage, 0, 0, null);
	      g.setComposite(AlphaComposite.getInstance(3, 1.0F));
	      g.drawImage(qrImage, Math.round(deltaWidth / 2), Math.round(deltaHeight / 2) + 230, null);
	    }
	    catch (IOException e) {
	      e.printStackTrace();
	    }

	    return combined;
	  }

	  private BufferedImage generate(String invoiceID) {
	    Map hints = new HashMap();
	    hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
	    hints.put(EncodeHintType.MARGIN, Integer.valueOf(0));

	    QRCodeWriter writer = new QRCodeWriter();
	    BitMatrix bitMatrix = null;
	    BufferedImage combined = null;
	    try
	    {
	      bitMatrix = writer.encode("http://www.flashiz.com/fr/infos/" + invoiceID, BarcodeFormat.QR_CODE, 660, 660, hints);
	      BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
	      BufferedImage logoImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("com/flashiz/qrgen/logo.png"));
	      int deltaHeight = qrImage.getHeight() - logoImage.getHeight();
	      int deltaWidth = qrImage.getWidth() - logoImage.getWidth();
	      combined = new BufferedImage(qrImage.getHeight(), qrImage.getWidth(), 2);
	      Graphics2D g = (Graphics2D)combined.getGraphics();
	      g.drawImage(qrImage, 0, 0, null);
	      g.setComposite(AlphaComposite.getInstance(3, 1.0F));
	      g.drawImage(logoImage, Math.round(deltaWidth / 2), Math.round(deltaHeight / 2), null);
	    }
	    catch (WriterException e)
	    {
	      e.printStackTrace();
	    }
	    catch (FileNotFoundException e) {
	      e.printStackTrace();
	    }
	    catch (IOException e) {
	      e.printStackTrace();
	    }

	    return combined;
	  }
	
}
