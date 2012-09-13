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
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

import org.gvsig.georeferencing.ui.zoom.CanvasZone;
import org.gvsig.georeferencing.ui.zoom.IGraphicLayer;
import org.gvsig.georeferencing.ui.zoom.tools.ToolEvent;
import org.gvsig.georeferencing.ui.zoom.tools.ToolListener;

/**
 * Capa gráfica que se dibuja sobre una vista de zoom un cursor
 * rectangular que representa una ventanta de zoom sobre la vista
 * 22/12/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class ZoomCursorGraphicLayer implements IGraphicLayer {
	//Operaciones sobre el cursor gráfico
	private static final int    NONE             = -1;
	private static final int    REDIM_LEFT       = 0;
	private static final int    REDIM_RIGHT      = 1;
	private static final int    REDIM_UP         = 2;
	private static final int    REDIM_DOWN       = 3;
	private static final int    MOVE_UR          = 4;
	private static final int    MOVE_UL          = 5;
	private static final int    MOVE_LR          = 6;
	private static final int    MOVE_LL          = 7;

	private final int           MIN_CURSOR_SIZE  = 10;
	private int                 operation        = NONE;

	private int                 wCursor          = 0;
	private int                 hCursor          = 0;
	private int                 posX             = 0;
	private int                 posY             = 0;
	private Image               iconHoriz        = null;
	private Image               iconVert         = null;
	private Image               iconMove         = null;
	private CanvasZone          canvas           = null;
	//Memoria temporal de las posiciones en X y en Y previas a una operación
	private int                 prevX, prevY;
	//Listener para que objetos externos sean informados de las acciones de la capa
	private ToolListener        listener         = null;

	private boolean             active           = true;
	private boolean             sleepActive      = true;
    private Color               cursorColor      = Color.RED;

	/**
	 * Constructor. Asigna el ancho y alto del rectangulo del cursor y la
	 * posición en la inicialización.
	 * @param pX Posición en X del cursor en la vista
	 * @param pY Posición en Y del cursor en la vista
	 * @param w Ancho del cursor en la vista
	 * @param h Alto del cursor en la vista
	 * @param listener Listener para acciones de finalización de la operación de zoom
	 */
	public ZoomCursorGraphicLayer(int pX, int pY, int w, int h, ToolListener listener) {
		wCursor = w;
		hCursor = h;
		posX = pX;
		posY = pY;
		this.listener = listener;
		try {
			iconHoriz = new ImageIcon(getClass().getClassLoader().getResource("images/FlechaHorizCursor.gif")).getImage();
			iconVert = new ImageIcon(getClass().getClassLoader().getResource("images/FlechaVertCursor.gif")).getImage();
			iconMove = new ImageIcon(getClass().getClassLoader().getResource("images/FlechaMoveCursor.gif")).getImage();
		} catch (NullPointerException e) {

		}
	}

	/**
	 * Asigna el canvas
	 * @param canvas
	 */
	public void setCanvas(CanvasZone canvas) {
		this.canvas = canvas;
		canvas.addMouseMotionListener(this);
		canvas.addMouseListener(this);
	}

	/**
	 * Asigna la posición del cursor en el canvas
	 * @param x Posición en X
	 * @param y Posición en Y
	 */
	public void setCursorPosition(int x, int y) {
		this.posX = x;
		this.posY = y;
	}

	/**
	 * Asigna el tamaño del cursor en pixeles del canvas
	 * @param w Ancho
	 * @param h Alto
	 */
	public void setCursorSize(int w, int h) {
		this.wCursor = w;
		this.hCursor = h;
	}

	/**
	 * Obtiene las coordenadas de la ventana de zoom. Las coordenadas son devueltas
	 * en referencia a la vista.
	 * @return
	 */
	public Rectangle2D getCursorViewCoordinates() {
		return new Rectangle2D.Double(posX - (wCursor >> 1), posY - (hCursor >> 1), wCursor, hCursor);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.georeferencing.ui.zoom.IGraphicLayer#draw(java.awt.Graphics2D, org.gvsig.raster.datastruct.Extent, int, int)
	 */
	public void draw(Graphics2D g, Rectangle2D ext, int w, int h) {
		g.setColor(cursorColor);
		wCursor = Math.max(wCursor, MIN_CURSOR_SIZE);
		hCursor = Math.max(hCursor, MIN_CURSOR_SIZE);
		g.drawRect(posX - (wCursor >> 1), posY - (hCursor >> 1), wCursor, hCursor);
		g.drawLine(posX, posY - (hCursor >> 1), posX, 0);
		g.drawLine(posX, posY + (hCursor >> 1), posX, h);
		g.drawLine(0, posY, posX - (wCursor >> 1), posY);
		g.drawLine(posX + (wCursor >> 1), posY, w, posY);
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		prevX = e.getX();
		prevY = e.getY();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		if(!isActive()) {
			return;
		}
		if(getOperation() != NONE) {
			setOperation(NONE);
			if(listener != null) {
				listener.endAction(new ToolEvent(this));
			}
		}
	}


	/**
	 * Cuando se pincha y se arrastra en los contornos se redimensiona el marco.
	 */
	public void mouseDragged(MouseEvent e) {
		if(!isActive()) {
			return;
		}
		if(getOperation() == MOVE_UR) {
			posX += (e.getX() - (wCursor >> 1)) - posX;
			posY += (e.getY() + (hCursor >> 1)) - posY;
			return;
		}
		if(getOperation() == MOVE_UL) {
			posX += (e.getX() + (wCursor >> 1)) - posX;
			posY += (e.getY() + (hCursor >> 1)) - posY;
			return;
		}
		if(getOperation() == MOVE_LR) {
			posX += (e.getX() - (wCursor >> 1)) - posX;
			posY += (e.getY() - (hCursor >> 1)) - posY;
			return;
		}
		if(getOperation() == MOVE_LL) {
			posX += (e.getX() + (wCursor >> 1)) - posX;
			posY += (e.getY() - (hCursor >> 1)) - posY;
			return;
		}
		if(getOperation() == REDIM_LEFT) {
			wCursor += prevX - e.getX();
			posX = e.getX() + (wCursor >> 1);
			prevX = e.getX();
			return;
		}
		if(getOperation() == REDIM_RIGHT) {
			int prevULX = posX - (wCursor >> 1);
			wCursor += e.getX() - prevX;
			posX = prevULX + (wCursor >> 1);
			prevX = e.getX();
			return;
		}
		if(getOperation() == REDIM_UP) {
			hCursor += prevY - e.getY();
			posY = e.getY() + (hCursor >> 1);
			prevY = e.getY();
			return;
		}
		if(getOperation() == REDIM_DOWN) {
			int prevULY = posY - (hCursor >> 1);
			hCursor += e.getY() - prevY;
			posY = prevULY + (hCursor >> 1);
			prevY = e.getY();
			return;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
		if(!isActive()) {
			return;
		}
		int pxLeft = posX - (wCursor >> 1);
		int pxRight = posX + (wCursor >> 1);
		int pyUp = posY - (hCursor >> 1);
		int pyDown = posY + (hCursor >> 1);

		//Si estamos fuera del área del cuadrado + 2 píxeles ponemos el cursor por defecto y no hacemos nada
		if(e.getX() < (pxLeft - 2) || e.getX() > (pxRight + 2) || e.getY() < (pyUp - 2) || e.getY() > (pyDown + 2)) {
			setOperation(NONE);
			if(canvas.getCursor().getType() != Cursor.DEFAULT_CURSOR) {
				canvas.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				listener.offTool(new ToolEvent(this));
			}
			return;
		}

		if(e.getX() >= (pxRight - 2) && e.getY() <= (pyUp + 2)) {
			if(iconMove != null) {
				canvas.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(iconMove, new Point(16, 16), ""));
			}
			setOperation(MOVE_UR);
			return;
		}
		if(e.getX() <= (pxLeft + 2) && e.getY() <= (pyUp + 2)) {
			if(iconMove != null) {
				canvas.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(iconMove, new Point(16, 16), ""));
			}
			setOperation(MOVE_UL);
			return;
		}
		if(e.getX() <= (pxLeft + 2) && e.getY() >= (pyDown - 2)) {
			if(iconMove != null) {
				canvas.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(iconMove, new Point(16, 16), ""));
			}
			setOperation(MOVE_LL);
			return;
		}
		if(e.getX() >= (pxRight - 2) && e.getY() >= (pyDown - 2)) {
			if(iconMove != null) {
				canvas.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(iconMove, new Point(16, 16), ""));
			}
			setOperation(MOVE_LR);
			return;
		}
		if(e.getX() <= (pxLeft + 1)) {
			if(iconHoriz != null) {
				canvas.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(iconHoriz, new Point(16, 16), ""));
			}
			setOperation(REDIM_LEFT);
			return;
		}
		if(e.getX() >= (pxRight - 1)) {
			if(iconHoriz != null) {
				canvas.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(iconHoriz, new Point(16, 16), ""));
			}
			setOperation(REDIM_RIGHT);
			return;
		}
		if(e.getY() <= (pyUp + 1)) {
			if(iconVert != null) {
				canvas.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(iconVert, new Point(16, 16), ""));
			}
			setOperation(REDIM_UP);
			return;
		}
		if(e.getY() >= (pyDown - 1)) {
			if(iconVert != null) {
				canvas.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(iconVert, new Point(16, 16), ""));
			}
			setOperation(REDIM_DOWN);
			return;
		}
		setOperation(NONE);
		if(canvas.getCursor().getType() != Cursor.DEFAULT_CURSOR) {
			canvas.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			listener.offTool(new ToolEvent(this));
		}
	}

	/**
	 * Obtiene la operación sobre el cursor que hay seleccionada
	 * @return Entero que representa a la operación
	 */
	public int getOperation() {
		return operation;
	}

	/**
	 * Asigna la operación sobre el cursor que hay seleccionada
	 * @param op
	 */
	private void setOperation(int op) {
		operation = op;
		if(op != NONE) {
			listener.onTool(new ToolEvent(this));
		}
	}

	/**
	 * Desactiva la herramienta temporalmente. Guarda el estado en el que estaba
	 * para restaurarlo cuando se invoque a awake
	 */
	public void sleep() {
		sleepActive = active;
		active = false;
	}

	/**
	 * Recupera el estado de activación que tenía antes de la última invocación
	 * de sleep
	 */
	public void awake() {
		active = sleepActive;
	}

	/**
	 * Consulta si es posible interactuar con el la capa de cursor de zoom
	 * @return
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Asigna el flag que activa y desactiva la interactuación con capa de control de zoom
	 * @param activeEvent
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Obtiene el color del cursor
	 * @return Color
	 */
	public Color getColor() {
		return cursorColor;
	}

	/**
	 * Asigna el color del cursor
	 * @param color
	 */
	public void setColor(Color color) {
		this.cursorColor = color;
	}
}

