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
package org.gvsig.georeferencing.ui.zoom.layers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.gvsig.raster.datastruct.GeoPoint;

/**
 * Punto de control gráfico. Tiene la funcionalidad de dibujado de la coordenada
 * pixel de un punto de control. Un punto de control tiene coordenada pixel de imagen
 * de origen y coordenada de destino en coordenadas geográficas. Un GPGraphic tiene asociado
 * un GeoPoint que es el que tiene la información de georreferencia.
 *  
 * 22/12/2007
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class GPGraphic {
	public static final int         PIXEL = 0;
	public static final int         MAP = 1;
	/**
	 * Diametro del centro de la cruz
	 */
	private final int               DIAM_CIRCLE = 18;
	private boolean                 showNumber = true;
	private boolean                 showLabel = true;
	private int                     type = PIXEL;
	private GeoPoint                geoPoint = null;
	/**
	 * Coordenadas de dibujado del punto en número de píxeles del dispositivo.
	 * En este caso la vista.
	 */
	private Point2D                 drawedCoords = null;
	private boolean                 draw = true;
	private long                    id = -1;
	
	/**
	 * Tipo de gráfico en el punto
	 * @param type El valor de "type" viene definido por las constantes en esta clase
	 */
	public GPGraphic(int type) {
		this.type = type;
	}
	
	/**
	 * Tipo de gráfico en el punto
	 * @param type El valor de "type" viene definido por las constantes en esta clase
	 */
	public GPGraphic(int type, GeoPoint gp) {
		this.type = type;
		this.geoPoint = gp;
	}
	
	/**
	 * Dibuja sobre el graphics pasado la cruz del punto que marca
	 * el pixel de la imagen.
	 * @param g Graphics sobre el que se pinta
	 * @param p Punto 
	 */
	public void draw(Graphics2D g) {
		if(!draw )
			return;
		if(geoPoint != null)
			draw(g, drawedCoords, geoPoint.number, geoPoint.label);
	}
	
	/**
	 * Dibuja sobre el graphics pasado la cruz del punto que marca
	 * el pixel de la imagen.
	 * @param g Graphics sobre el que se pinta
	 * @param p Punto del graphics que coincide con el centro del GP
	 * @param pointNumber Numero de punto a mostrar
	 * @param label Etiqueta del punto
	 */
	public void draw(Graphics2D g, Point2D p, int pointNumber, String label) {
		switch (type) {
		case PIXEL:
			drawPixelCrux(g, p, pointNumber, label);
			break;
		case MAP:
			drawMapCrux(g, p, pointNumber, label);
			break;
		}
	}
	
	/**
	 * Dibuja sobre el graphics pasado la cruz del punto que marca
	 * el pixel de la imagen.
	 * @param g Graphics sobre el que se pinta
	 * @param p Punto del graphics que coincide con el centro del GP
	 * @param pointNumber Numero de punto a mostrar
	 * @param label Etiqueta del punto
	 */
	private void drawPixelCrux(Graphics2D g, Point2D p, int pointNumber, String label) {
		int dpto = (DIAM_CIRCLE >> 1);
		int incr = 5;
		g.setColor(Color.WHITE);
		g.drawOval(	(int)p.getX() - dpto + 1,
					(int)p.getY() - dpto + 1,
					DIAM_CIRCLE - 2,
					DIAM_CIRCLE - 2);
		g.drawLine((int)p.getX() - incr, (int)p.getY() - 1, (int)p.getX() - 1, (int)p.getY() - 1);
		g.drawLine((int)p.getX() - incr, (int)p.getY() + 1, (int)p.getX() - 1, (int)p.getY() + 1);

		g.drawLine((int)p.getX() + incr, (int)p.getY() - 1, (int)p.getX() + 1, (int)p.getY() - 1);
		g.drawLine((int)p.getX() + incr, (int)p.getY() + 1, (int)p.getX() + 1, (int)p.getY() + 1);

		g.drawLine((int)p.getX() - 1, (int)p.getY() - incr, (int)p.getX() - 1, (int)p.getY() - 1);
		g.drawLine((int)p.getX() + 1, (int)p.getY() - incr, (int)p.getX() + 1, (int)p.getY() - 1);

		g.drawLine((int)p.getX() - 1, (int)p.getY() + incr, (int)p.getX() - 1, (int)p.getY() + 1);
		g.drawLine((int)p.getX() + 1, (int)p.getY() + incr, (int)p.getX() + 1, (int)p.getY() + 1);

		g.setColor(Color.red);
		g.drawOval(	(int)p.getX() - dpto,
					(int)p.getY() - dpto,
					DIAM_CIRCLE,
					DIAM_CIRCLE);
		g.drawLine((int)p.getX(), (int)p.getY() - dpto - incr, (int)p.getX(), (int)p.getY() + dpto + incr);
		g.drawLine((int)p.getX() - dpto - incr, (int)p.getY(), (int)p.getX() + dpto + incr, (int)p.getY());

		if(showNumber) {
			String pt = String.valueOf(pointNumber );
			int ptX = (int)(p.getX() + dpto + 1);
			int ptY = (int)(p.getY() + dpto - 1);
			g.setColor(Color.WHITE);
			for (int i= -1; i<2; i++)
				for (int j= -1; j<2; j++)
					g.drawString(pt, ptX + i, ptY + j );
			g.setColor(Color.red);
			g.drawString(pt, ptX, ptY );
		}
		if(showLabel) {
			//TODO: FUNCIONALIDAD mostrar etiqueta
		}
	}

	/**
	 * Dibuja sobre el graphics pasado la cruz del punto que marca
	 * el pixel de la imagen.
	 * @param g Graphics sobre el que se pinta
	 * @param p Punto del graphics que coincide con el centro del GP
	 * @param pointNumber Numero de punto a mostrar
	 * @param label Etiqueta del punto
	 */
	private void drawMapCrux(Graphics2D g, Point2D p, int pointNumber, String label){
		int dpto = (DIAM_CIRCLE >> 1);
		int incr = 5;
		g.setColor(Color.WHITE);
		g.drawRect(	(int)p.getX() - dpto + 1,
					(int)p.getY() - dpto + 1,
					DIAM_CIRCLE - 2,
					DIAM_CIRCLE - 2);
		g.drawLine((int)p.getX() - incr, (int)p.getY() - 1, (int)p.getX() - 1, (int)p.getY() - 1);
		g.drawLine((int)p.getX() - incr, (int)p.getY() + 1, (int)p.getX() - 1, (int)p.getY() + 1);

		g.drawLine((int)p.getX() + incr, (int)p.getY() - 1, (int)p.getX() + 1, (int)p.getY() - 1);
		g.drawLine((int)p.getX() + incr, (int)p.getY() + 1, (int)p.getX() + 1, (int)p.getY() + 1);

		g.drawLine((int)p.getX() - 1, (int)p.getY() - incr, (int)p.getX() - 1, (int)p.getY() - 1);
		g.drawLine((int)p.getX() + 1, (int)p.getY() - incr, (int)p.getX() + 1, (int)p.getY() - 1);

		g.drawLine((int)p.getX() - 1, (int)p.getY() + incr, (int)p.getX() - 1, (int)p.getY() + 1);
		g.drawLine((int)p.getX() + 1, (int)p.getY() + incr, (int)p.getX() + 1, (int)p.getY() + 1);

		g.setColor(new Color(45, 8 , 165));
		g.drawRect(	(int)p.getX() - dpto,
					(int)p.getY() - dpto,
					DIAM_CIRCLE,
					DIAM_CIRCLE);
		g.drawLine((int)p.getX(), (int)p.getY() - dpto - incr, (int)p.getX(), (int)p.getY() + dpto + incr);
		g.drawLine((int)p.getX() - dpto - incr, (int)p.getY(), (int)p.getX() + dpto + incr, (int)p.getY());
		if(showNumber){
			String pt = String.valueOf(pointNumber);
			int ptX = (int)(p.getX() + dpto + 1);
			int ptY = (int)(p.getY() - dpto - 1);
			g.setColor(Color.WHITE);
			for (int i= -1; i<2; i++)
				for (int j= -1; j<2; j++)
					g.drawString(pt, ptX + i, ptY + j );
			g.setColor(new Color(45, 8 , 165));
			g.drawString(String.valueOf(pointNumber), (int)(p.getX() + dpto + 1), (int)(p.getY() - dpto - 1) );
		}
		if(showLabel) {
			//TODO: FUNCIONALIDAD mostrar etiqueta
		}
	}

	/**
	 * Asigna el flag que muestra u oculta la etiqueta del punto
	 * @param showLabel true para mostrar la etiqueta y false para ocultarla
	 */
	public void setShowLabel(boolean showLabel) {
		this.showLabel = showLabel;
	}

	/**
	 * Asigna el flag que muestra u oculta el número del punto
	 * @param showLabel true para mostrar el punto y false para ocultarlo
	 */
	public void setShowNumber(boolean showNumber) {
		this.showNumber = showNumber;
	}

	/**
	 * Asigna el tipo de gráfico para el punto
	 * @param type Tipo de punto definido en las constantes de clase
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * Obtiene el punto de control asociado al este objeto gráfico..
	 * @return GeoPoint
	 */
	public GeoPoint getGeoPoint() {
		return geoPoint;
	}
	
	/**
	 * Asigna las coordenadas de dibujado al punto.
	 * @param drawedCoords
	 */
	public void setDrawCoords(Point2D drawedCoords) {
		this.drawedCoords = drawedCoords;
	}
	
	/**
	 * Obtiene las coordenadas de dibujado del punto
	 * @return Point2D
	 */
	public Point2D getDrawCoords() {
		return drawedCoords;
	}
	
	/**
	 * Obtiene el tipo de punto.
	 * @return Constante definida en GPGraphic
	 */
	public int getType() {
		return type;
	}

	/**
	 * Consulta el flag que informa sobre si se está pintando el punto
	 * de control o está oculto
	 * @return true si se pinta el punto de control y false si está oculto
	 */
	public boolean isDraw() {
		return draw;
	}

	/**
	 * Asigna el flag que informa sobre si se está pintando el punto
	 * de control o está oculto
	 * @param draw true para pintar el punto de control y false para ocultarlo
	 */
	public void setDraw(boolean draw) {
		this.draw = draw;
	}

	/**
	 * Obtiene el identificador de punto
	 * @return
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Asigna el identificador de punto
	 * @return
	 */
	public void setId(long id) {
		this.id = id;
	}
}
