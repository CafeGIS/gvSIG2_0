package org.gvsig.compat.se.lang;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import org.gvsig.compat.lang.GraphicsUtils;
import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.compat.se.print.SePrintAttributes;

public class SEGraphUtils implements GraphicsUtils{

	public BufferedImage copyBufferedImage(BufferedImage img) {
		
		BufferedImage image = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		WritableRaster w = image.getRaster();
		img.copyData(w);
		return image;
	}

	public BufferedImage createBufferedImage(int w, int h, int type) {
		BufferedImage res = new BufferedImage(
				w,
				h,
				type);
		return res;
	}

	public PrintAttributes createPrintAttributes() {
		return new SePrintAttributes();
	}

	public Font deriveFont(Font srcfont, float newheight) {
		return srcfont.deriveFont(newheight);
	}

	public int getScreenDPI() {
		// TODO Auto-generated method stub
		return 96;
	}

	public void translate(Graphics2D g, double x, double y) {
		g.translate(x, y);
		
	}

}
