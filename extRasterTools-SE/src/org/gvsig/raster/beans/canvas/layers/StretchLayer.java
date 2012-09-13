/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;

import org.gvsig.raster.beans.canvas.DrawableElement;

/**
 * Capa con líneas verticales que seccionan en tramos. Puede definirse el número de tramos 
 * y estos son dibujados equidistantes. La capa es capaz de recibir eventos de ratón 
 * para variar la posición de los tramos pinchando y arrastrando estos.
 * 
 * 07/08/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class StretchLayer extends DrawableElement {
	private StretchLayerDataModel  stretchs       = new StretchLayerDataModel();
	
	/*Configuración del trazo*/
	private int                    border         = 2;
	private BasicStroke            stroke         = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{5.0f}, 0.0f);
	/**
	 * Flag que cuando está a true evita que los extremos puedan ser desplazados
	 */  
	private boolean                fixExtreme     = false;
	/**
	 * Eje seleccionado para su arrastre
	 */
	private int                    axisSelected   = -1;
	/**
	 * Distancia entre el punto pinchado y la posición real del eje en X
	 */
	private double                 distance       = 0;
	/**
	 * Flag que informa de cuando se está arrastrando un eje
	 */
	private boolean                draggingAxis   = false;
	
	/**
	 * Asigna el color de las líneas que representan los tramos
	 * @param c Color de los intervalos sobre el gráfico
	 * @param n Número de intervalos inicial
	 */
	public StretchLayer(Color c, int n) {
		setColor(c); 
		stretchs.generate(n);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.DrawableElement#firstActions()
	 */
	public void firstActions() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.DrawableElement#firstDrawActions()
	 */
	public void firstDrawActions() {
	}

	/**
	 * Asigna el valor del flag que bloquea los extremos para que no puedan ser 
	 * desplazados por el usuario.
	 * @param fe true para fijar los extremos y false para desbloquearlos
	 */
	public void fixExtreme(boolean fe) {
		this.fixExtreme = fe;
	}
	
	/**
	 * Convierte un valor entre 0 y 1 a posición en píxeles
	 * @param value Valor entre 0 y 1
	 * @return Posición en píxeles
	 */
	private int percentToPixel(double value) {
		return (int) Math.round(canvas.getCanvasMinX() + border + ((canvas.getCanvasMaxX() - canvas.getCanvasMinX() - (border * 2)) * value));
	}

	/**
	 * Convierte un valor en píxeles a un rango entre 0 y 1
	 * @param Posición en píxeles
	 * @return  Valor entre 0 y 1
	 */
	private double pixelToPercent(int pixel) {
		return ((double) (pixel - canvas.getCanvasMinX() - border) / (double) (canvas.getCanvasMaxX() - canvas.getCanvasMinX() - (border * 2)));
	}
		
	/**
	 * Obtiene la lista de valores de los tramos en forma de array de double
	 * @return double[]
	 */
	public double[] getDoubleValues() {
		double[] list =  new double[stretchs.size()];
		for (int i = 0; i < stretchs.size(); i++) 
			list[i] = ((Double)stretchs.get(i)).doubleValue();
		return list;
	}
	
	/**
	 * Obtiene la lista de valores de los tramos en forma de ArrayList
	 * @return ArrayList
	 */
	public StretchLayerDataModel getStretchDataModel() {
		return stretchs;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.DrawableElement#paint(java.awt.Graphics)
	 */
	protected void paint(Graphics g) {
		g.setColor(color);

		Graphics2D g2 = (Graphics2D) g;
		Stroke stroke2 = g2.getStroke();
		g2.setStroke(stroke);

		for (int i = 0; i < stretchs.size(); i++) {
			double value = ((Double)stretchs.get(i)).doubleValue();
			double v = percentToPixel(value);
			g2.drawLine((int)v, canvas.getCanvasMinY(), (int)v, canvas.getCanvasMaxY());	
		}

		g2.setStroke(stroke2);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.DrawableElement#mouseMoved(java.awt.event.MouseEvent)
	 */
	public boolean mouseMoved(MouseEvent e) {
		int begin = 0;
		int end =  stretchs.size();
		if(fixExtreme) {
			begin ++;
			end --;
		}
		
		if (e.getY() > canvas.getCanvasMinY() && e.getY() < canvas.getCanvasMaxY()) {
			for (int i = begin; i < end; i++) {
				double value = ((Double)stretchs.get(i)).doubleValue();
				double axisX = percentToPixel(value);
				axisSelected = i;
				if (e.getX() >= (axisX - 3) && e.getX() <= (axisX + 3)) {
					canvas.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
					return false;
				}
				if (e.getX() >= (axisX - 3) && e.getX() <= (axisX + 3)) {
					canvas.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
					return false;
				}
			}
		}
		axisSelected = -1;
		return true;
	}
	
	/**
	 * <P>
	 * Al pulsar botón izquierdo, si cursor está sobre una línea se captura esta 
	 * para su arrastre. Si no está sobre una línea se añade una a la lista en el 
	 * punto que se ha pinchado
	 * </p><p>
	 * Al pulsar botón derecho, si el cursor está sobre una línea esta se elimina.
	 * </p>
	 */
	public boolean mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			if(axisSelected >= 0) {
				double linePoint = ((Double)stretchs.get(axisSelected)).doubleValue();
				distance = pixelToPercent(e.getX()) - linePoint;
				draggingAxis = true;
			} else {
				stretchs.add(new Double(pixelToPercent(e.getX())));
				canvas.repaint();
			}
		}
		if(e.getButton() == MouseEvent.BUTTON3) {
			if(axisSelected >= 0 && axisSelected < stretchs.size()) {
				stretchs.remove(axisSelected);
				canvas.repaint();
			}
		}
		return true;	
	}
	
	/**
	 * Inicializamos la distancia
	 */
	public boolean mouseReleased(MouseEvent e) {
		draggingAxis = false;
		//Ordenamos el array de valores por si se ha desordenado al mover los ejes
		stretchs.sort();
		canvas.callDataChanged("moveline", this);
		return true;
	}

	/**
	 * Cuando se ha pinchado un punto y se arrastra se define aquí su comportamiento.
	 */
	public boolean mouseDragged(MouseEvent e) {
		if(draggingAxis) {
			double linePoint = ((Double)stretchs.get(axisSelected)).doubleValue();
			linePoint = pixelToPercent(e.getX()) + distance;
			stretchs.set(axisSelected, new Double(linePoint));
			canvas.repaint();
		}
		return true;
	}

}
