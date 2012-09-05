package org.gvsig.compat.lang;

import java.awt.Graphics2D;
import java.awt.Font;

import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;

import org.gvsig.compat.print.PrintAttributes;

public interface GraphicsUtils {
	
	public BufferedImage createBufferedImage(int w, int h, int type);
	public BufferedImage copyBufferedImage(BufferedImage img);
	
	public Font deriveFont(Font srcfont, float newheight);
	
	public int getScreenDPI();

	public void translate(Graphics2D g, double x, double y);
	
	public PrintAttributes createPrintAttributes();
}
