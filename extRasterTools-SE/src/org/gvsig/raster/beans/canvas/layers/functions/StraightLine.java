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
package org.gvsig.raster.beans.canvas.layers.functions;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;

import org.gvsig.raster.beans.canvas.GCanvas;
import org.gvsig.raster.beans.canvas.layers.InfoLayer;
import org.gvsig.raster.util.MathUtils;
/**
 * Representa una linea recta con puntos de arrastre para la ecualización de
 * un histograma y realce lineales y dencity slicing.
 *
 * 14-oct-2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class StraightLine extends BaseFunction {

	/**
	 * Representa un punto seleccionado sobre el canvas. Este es
	 * dibujado como un pequeño cuadrado.
	 *
	 * 14-oct-2007
	 * @author Nacho Brodin (nachobrodin@gmail.com)
	 */
	class Square {
		private double x      = 0.0D;
		private double y      = 0.0D;
		private int    width  = 6;
		private Color  color  = Color.WHITE;
		
		
		/**
		 * Constructor. Calcula las esquinas del cuadrado
		 * @param p
		 */
		public Square(double x, double y) {
			setPosition(x, y);
		}

		/**
		 * Constructor. Calcula las esquinas del cuadrado
		 * @param p
		 */
		public Square(int x, int y) {
			setPosition(x, y);
		}
		
		/**
		 * Asigna una nueva posición al punto
		 * @param x
		 */
		public void setPosition(double x, double y) {
			this.x = x;
			this.y = y;

			if (this.x > 1.0D) this.x = 1.0D;
			if (this.x < 0.0D) this.x = 0.0D;
			if (this.y > 1.0D) this.y = 1.0D;
			if (this.y < 0.0D) this.y = 0.0D;
		}
		
		/**
		 * Asigna una nueva posición al punto
		 * @param x
		 */
		public void setPosition(int x, int y) {
			setPosition(pixelToValueX(x), pixelToValueY(y));
		}
		
		public int getPixelX() {
			return valueToPixelX(x);
		}

		public int getPixelY() {
			return valueToPixelY(y);
		}
		
		public double getX() {
			return x;
		}
		
		public double getY() {
			return y;
		}
		
		/**
		 * Dibuja el cuadrado
		 * @param g
		 */
		protected void paint(Graphics g) {
			g.setColor(color);
			g.drawRect((int) valueToPixelX(x) - (width >> 1), (int) valueToPixelY(y) - (width >> 1), (int) width, (int)width);
		}

		/**
		 * Dibuja el cursor del raton
		 * @param g
		 */
		protected void paintCursor(Graphics g) {
			g.setColor(Color.white);
			g.drawLine(valueToPixelX(x) - 6, valueToPixelY(y), valueToPixelX(x) + 6, valueToPixelY(y));
			g.drawLine(valueToPixelX(x), valueToPixelY(y) - 6, valueToPixelX(x), valueToPixelY(y) + 6);
		}
		
		/**
		 * Informa de si el punto pasado por parámetro cae dentro del cuadro o no
		 * @param p Punto a comprobar
		 * @return true si el punto está dentro y false si no lo está
		 */
		public boolean isInside(Point p) {
			if (p.getX() < (valueToPixelX(x) - (width >> 1)))
				return false;
			if (p.getX() > (valueToPixelX(x) + (width >> 1)))
				return false;
			if (p.getY() < (valueToPixelY(y) - (width >> 1)))
				return false;
			if (p.getY() > (valueToPixelY(y) + (width >> 1)))
				return false;
			return true;
		}
		
		/**
		 * Asigna el color del cuadrado
		 * @param c Color
		 */
		public void setColor(Color c) {
			this.color = c;
		}
		
		private int valueToPixelX(double value) {
			value = minx + ((maxx - minx) * value);
			return (int) Math.round(canvas.getCanvasMinX() + border + ((canvas.getCanvasMaxX() - canvas.getCanvasMinX() - (border * 2)) * value));
		}

		private int valueToPixelY(double value) {
			value = 1.0D - value;
			return (int) Math.round(canvas.getCanvasMinY() + border + ((canvas.getCanvasMaxY() - canvas.getCanvasMinY() - (border * 2)) * value));
		}

	}
	
	/**
	 * Lista de cuadrados que intersectan con la recta
	 */
	protected ArrayList listSquare        = new ArrayList();
	private int         pointDragged      = -1;
	private Cursor      transparentCursor = null;
	private boolean     showSquares       = true;
	private int         border            = 2;
	
	/**
	 * Constructor. Asigna el color
	 * @param c
	 */
	public StraightLine(Color c) {
		int[] pixels = new int[16 * 16];
		Image image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16, 16, pixels, 0, 16));
		transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0), "invisibleCursor");
		
		setColor(c);
		clearSquares();
		addSquare(0.0D, 0.0D);
		addSquare(1.0D, 1.0D);
	}

	/**
	 * Borra la lista de puntos
	 */
	public void clearSquares() {
		listSquare.clear();
	}

	/**
	 * Añade un punto a la lista de puntos
	 * @param x
	 * @param y
	 */
	public void addSquare(double x, double y) {
		listSquare.add(new Square(x, y));
	}
	
	/**
	 * Dibujado de las líneas y cuadros sobre el canvas
	 */
	protected void paint(Graphics g) {
		super.paint(g);

		g.setColor(color);

		// Dibujamos una línea desde un punto hasta el siguiente
		Square square = null;
		Square lastSquare = null;

		if (listSquare.size() > 0) {
			square = ((Square) listSquare.get(0));
			g.drawLine(canvas.getCanvasMinX(), square.getPixelY(), square.getPixelX(), square.getPixelY());
		}
		for (int i = 0; i < listSquare.size(); i++) {
			lastSquare = square;
			square = ((Square) listSquare.get(i));
			if (lastSquare != null)
				g.drawLine(lastSquare.getPixelX(), lastSquare.getPixelY(), square.getPixelX(), square.getPixelY());
		}
		if (listSquare.size() > 0) {
			square = ((Square) listSquare.get(listSquare.size() - 1));
			g.drawLine(square.getPixelX(), square.getPixelY(), canvas.getCanvasMaxX(), square.getPixelY());
		}

		// Dibujamos los cuadrados de los puntos
		if (showSquares) {
			for (int i = 0; i < listSquare.size(); i++) {
				square = ((Square) listSquare.get(i));
				square.setColor(color);
				if (pointDragged != i)
					square.paint(g);
				else
					square.paintCursor(g);
			}
		}

		if (pointDragged != -1) {
			setInfoPoint(pointDragged);
		}
	}
	
	/**
	 * Visualiza la posicion de un punto en la barra de estado
	 * @param square
	 */
	private void setInfoPoint(int point) {
		ArrayList list = canvas.getDrawableElements(InfoLayer.class);
		if (list.size() > 0) {
			InfoLayer infoLayer = (InfoLayer) list.get(0);
			if (point != -1) {
				Square square = ((Square) listSquare.get(point));
				
				double value = minx + (square.getX() * (maxx - minx));
				value = infoLayer.getMin() + (value * (infoLayer.getMax() - infoLayer.getMin()));
				
				infoLayer.setStatusLeft("In: " + MathUtils.clipDecimals(value, 1));
				infoLayer.setStatusRight("Out: " + Math.round(square.getY() * 255.0D));
			} else {
				infoLayer.setStatusLeft(null);
				infoLayer.setStatusRight(null);
			}
		}
	}

	/**
	 * Asigna el objeto JComponent donde se pintan los elementos. Inicializa los puntos
	 * de inicio y fin de línea y asigna los listener
	 * @param canvas
	 */
	public void setCanvas(GCanvas canvas) {
		super.setCanvas(canvas);
	}
	
	/**
	 * Inserta un elemento (cuadrado) en el array entre los dos cuadrados entre los que se
	 * encuentre el valor de su X, así siempre están ordenados en el array por valor de X. 
	 * @param element
	 * @return Devuelve la posición del elemento insertado, en caso de no poder insertarse
	 *         devolverá -1
	 */
	private int insert(Square element) {
		for (int i = 0; i < (listSquare.size() - 1); i++) {
			double sqX = ((Square) listSquare.get(i)).getX();
			double sqNextX = ((Square) listSquare.get(i + 1)).getX();
			if (element.getX() >= sqX && element.getX() <= sqNextX) {
				listSquare.add(i + 1, element);
				return i + 1;
			}
		}
		return -1;
	}
	
	/**
	 * Se captura el punto a arrastrar, en caso de que no coincida con un punto,
	 * se inserta
	 */
	public boolean mousePressed(MouseEvent e) {
		for (int i = 0; i < listSquare.size(); i++) {
			if (((Square) listSquare.get(i)).isInside(e.getPoint())) {
				if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0) {
					if ((i == 0) || ((i + 1) == listSquare.size()))
						continue;
					pointDragged = -1;
					listSquare.remove(i);
					canvas.repaint();
					return false;
				}
				if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
					pointDragged = i;
					this.mouseDragged(e);
					return false;
				}
			}
		}

		if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == 0)
			return true;
		// En caso de que nadie lo este tratando, osea, un cursor normal
		if (canvas.getCursor().getType() == Cursor.DEFAULT_CURSOR) {
			pointDragged = insert(new Square(e.getX(), e.getY()));
			this.mouseDragged(e);
			return false;
		}
		return true;
	}

	/**
	 * Inicializamos el punto arrastrado a un valor fuera del array
	 */
	public boolean mouseReleased(MouseEvent e) {
		pointDragged = -1;
		setInfoPoint(-1);
		canvas.repaint();
		canvas.callDataChanged("line", this);
		return true;
	}

	/**
	 * Cuando se ha pinchado un punto y se arrastra se define aquí su comportamiento.
	 */
	public boolean mouseDragged(MouseEvent e) {
		if (pointDragged >= 0) {
			canvas.setCursor(transparentCursor);
			int minX = canvas.getCanvasMinX();
			int minY = canvas.getCanvasMinY();
			int maxX = canvas.getCanvasMaxX();
			int maxY = canvas.getCanvasMaxY();

			// Controlamos que no se salga de los límites
			int x = Math.min(Math.max(e.getX(), minX), maxX);
			int y = Math.min(Math.max(e.getY(), minY), maxY);
			Square point = ((Square) listSquare.get(pointDragged));

			try {
				// El primer punto no se desplaza en X
				if (pointDragged == 0) {
					point.setPosition(minX, y);
					return false;
				}
				// El último punto no se desplaza en X
				if (pointDragged == (listSquare.size() - 1)) {
					point.setPosition(maxX, y);
					return false;
				}

				// Puntos centrales
				point.setPosition(x, y);

				// Arrastra a los de abajo si la X es menor que los inferiores
				for (int i = 0; i < pointDragged; i++) {
					Square lowPoint = ((Square) listSquare.get(i));
					if (lowPoint.getPixelX() >= x) {
						lowPoint.setPosition(x, lowPoint.getPixelY());
						for (int j = i + 1; listSquare.get(j) != point;) {
							listSquare.remove(j);
							pointDragged--;
						}
						break;
					}
				}

				// Arrastra a los de arriba si la X es mayor que los superiores
				for (int i = listSquare.size() - 1; i > pointDragged; i--) {
					Square upperPoint = ((Square) listSquare.get(i));
					if (upperPoint.getPixelX() <= x) {
						upperPoint.setPosition(x, upperPoint.getPixelY());
						for (int j = i - 1; listSquare.get(j) != point;) {
							listSquare.remove(j);
							j--;
						}
						break;
					}
				}
			} finally {
				// Siempre repintamos
				canvas.repaint();
				canvas.callDataDragged("line", this);
			}
			return false;
		}
		return true;
	}

	/**
	 * Variable para saber si se ha visualizado alguna informacion
	 */
	boolean last = false;
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.DrawableElement#mouseMoved(java.awt.event.MouseEvent)
	 */
	public boolean mouseMoved(MouseEvent e) {
		for (int i = 0; i < listSquare.size(); i++) {
			if (((Square)listSquare.get(i)).isInside(e.getPoint())) {
				canvas.setCursor(new Cursor(Cursor.MOVE_CURSOR));
				setInfoPoint(i);
				canvas.repaint();
				last = true;
				return false;
			}
		}
		if (last) {
			setInfoPoint(-1);
			canvas.repaint();
			last = false;
		}
		return true;
	}

	/**
	 * Valores de los datos de entrada correspondientes al mínimo y al máximo de 
	 * cada tramo. Estos tendrán un rango entre el mínimo y el máximo en cada
	 * banda de la imagen.
	 * @param min
	 * @param max
	 * @return
	 */
	public double[] getInValues(double min, double max) {
		double[] in = getPercentInValues(); 
		for (int i = 0; i < in.length; i++)
			in[i] = min + (in[i] * (max - min));
		return in;
	}

	/**
	 * Valores de los datos de salida correspondientes al mínimo y al máximo de
	 * cada tramo. Estos tendrán un rango entre 0 y 255.
	 * @return
	 */
	public int[] getOutValues() {
		double[] values = getPercentOutValues(); 
		int[] out = new int[values.length];
		for (int i = 0; i < values.length; i++)
			out[i] = (int) Math.round(values[i] * 255.0D);
		return out;
	}

	/**
	 * Valores de los datos de entrada correspondientes al mínimo y al máximo de 
	 * cada tramo devuelto en forma de porcentaje
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public double[] getPercentInValues() {
		double[] in = new double[listSquare.size() + 2];
		for (int i = 0; i < listSquare.size(); i++) {
			Square square = ((Square) listSquare.get(i));
			in[i + 1] = minx + ((maxx - minx) * square.getX());
		}
		in[0] = 0.0D;
		in[in.length - 1] = 1.0D;
		return in;
	}

	/**
	 * Valores de los datos de salida correspondientes al mínimo y al máximo de
	 * cada tramo, devueltos en forma de porcentaje.
	 * @return
	 */
	public double[] getPercentOutValues() {
		double[] out = new double[listSquare.size() + 2];
		for (int i = 0; i < listSquare.size(); i++) {
			Square square = ((Square) listSquare.get(i));
			out[i + 1] = square.getY();
		}
		out[0] = out[1];
		out[out.length - 1] = out[out.length - 2];
		return out;
	}
	
	public void firstActions() {}
	public void firstDrawActions() {}

	/**
	 * @param showSquares the showSquares to set
	 */
	public void setShowSquares(boolean showSquares) {
		this.showSquares = showSquares;
	}

	protected double pixelToValueX(int pixel) {
		double value = ((double) (pixel - canvas.getCanvasMinX() - border) / (double) (canvas.getCanvasMaxX() - canvas.getCanvasMinX() - (border * 2.0D)));
		
		value = (value - minx) / (maxx - minx);
		
		return value;
	}

	protected double pixelToValueY(int pixel) {
		double div = (double) (canvas.getCanvasMaxY() - canvas.getCanvasMinY() - (border * 2.0D));
		if (div == 0.0D)
			return 0.0D;
		
		return (1.0D - ((double) (pixel - canvas.getCanvasMinY() - border) / div));
	}
	
	/**
	 * Devuelve 0 para indicar que estamos en una funcion lineal.
	 * @return
	 */
	public int getFunctionType() {
		return 0;
	}
	
	/**
	 * En una función lineal da igual lo que devuelva, pero es interesante para sus
	 * clases derivadas
	 * @return
	 */
	public double getValueFunction() {
		return 0.0;
	}
}