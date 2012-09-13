/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 */
package org.gvsig.raster.beans.canvas.layers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.gvsig.raster.beans.canvas.DrawableElement;
import org.gvsig.raster.util.MathUtils;
/**
 * Capa para GCanvas que muestra el maximo y minimo de una banda
 * 
 * @version 04/03/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class InfoLayer extends DrawableElement {
	private Color color = null;
	private double min = 0.0;
	private double max = 1.0;
	private String statusRight = null;
	private String statusLeft = null;

	/**
	 * Creacion de un InfoLayer especificando el color
	 * @param c
	 */
	public InfoLayer(Color c) {
		color = c;
	}

	/**
	 * Definir el borde que va a tener el componente
	 */
	public void firstActions() {
		canvas.addBorder(0, 15, 0, 15);
	}
	
	/**
	 * Establecer los valores maximo y minimo para dicha capa
	 * @param min
	 * @param max
	 */
	public void setLimits(double min, double max) {
		this.min = min;
		this.max = max;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.DrawableElement#paint(java.awt.Graphics)
	 */
	protected void paint(Graphics g) {
		
		Graphics2D graphics2 = (Graphics2D) g;

		RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
		graphics2.setRenderingHints(hints);

		graphics2.setColor(color);
		graphics2.setFont(new java.awt.Font("Times", 1, 12));
		
		String min2 = MathUtils.clipDecimals(min, 1) + "";
		String max2 = MathUtils.clipDecimals(max, 1) + "";

		Rectangle2D rectangle2D = graphics2.getFontMetrics().getStringBounds(max2, g);
		
		graphics2.drawString(min2, canvas.getCanvasMinX(), canvas.getCanvasMinX() + 12);
		graphics2.drawString(max2, (int) (canvas.getWidth() - rectangle2D.getWidth() - canvas.getCanvasMinX()), canvas.getCanvasMinX() + 12);
		
		if (statusRight != null) {
			rectangle2D = graphics2.getFontMetrics().getStringBounds(statusRight, g);
			graphics2.drawString(statusRight, (int) (canvas.getWidth() - rectangle2D.getWidth() - canvas.getCanvasMinX()), (int) canvas.getHeight() - canvas.getCanvasMinX());
		}

		if (statusLeft != null) {
			graphics2.drawString(statusLeft, canvas.getCanvasMinX(), (int) canvas.getHeight() - canvas.getCanvasMinX());
		}
		
		ArrayList list = canvas.getDrawableElements(MinMaxLines.class);
		if (list.size() > 0) {
			MinMaxLines minMaxLines = (MinMaxLines) list.get(0);

			double minP = min + (minMaxLines.getMinDistance() * (max - min));
			double maxP = min + (minMaxLines.getMaxDistance() * (max - min));
			min2 = MathUtils.clipDecimals(minP, 1) + "";
			max2 = MathUtils.clipDecimals(maxP, 1) + "";

			list = canvas.getDrawableElements(GraphicHistogram.class);
			if (list.size() > 0) {
				GraphicHistogram histogram = (GraphicHistogram) list.get(0);
				double[] ds = histogram.getHistogramValues();
				double total = 0.0D;
				double totalmin = 0.0D;
				double totalmax = 0.0D;
				for (int i = 0; i < ds.length; i++) {
					double value = min + (((double) (i * (max - min))) / (double) (ds.length - 1.0D));
					total += ds[i];
					if (minP > value)
						totalmin += ds[i];
					if (maxP < value)
						totalmax += ds[i];
				}

				totalmin = (100.0D * totalmin) / total;
				totalmax = (100.0D * totalmax) / total;

				min2 = min2 + " (" + MathUtils.clipDecimals(totalmin, 2) + "%)";
				max2 = max2 + " (" + MathUtils.clipDecimals(totalmax, 2) + "%)";
			}

			rectangle2D = graphics2.getFontMetrics().getStringBounds(max2, g);

			if (statusLeft == null)
				graphics2.drawString(min2, canvas.getCanvasMinX(), (int) canvas.getHeight() - canvas.getCanvasMinX());

			if (statusRight == null)
				graphics2.drawString(max2, (int) (canvas.getWidth() - rectangle2D.getWidth() - canvas.getCanvasMinX()), (int) canvas.getHeight() - canvas.getCanvasMinX());
		}
	}
	
	/**
	 * @return the min
	 */
	public double getMin() {
		return min;
	}

	/**
	 * @return the max
	 */
	public double getMax() {
		return max;
	}
	
	/**
	 * @param status the status to set
	 */
	public void setStatusRight(String statusRight) {
		this.statusRight = statusRight;
	}
	
	/**
	 * @param statusLeft the statusLeft to set
	 */
	public void setStatusLeft(String statusLeft) {
		this.statusLeft = statusLeft;
	}
	
	public void firstDrawActions() {}
}