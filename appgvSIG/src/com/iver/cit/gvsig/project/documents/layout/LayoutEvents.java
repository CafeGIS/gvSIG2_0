/*
 * Created on 27-jul-2004
 *
 */
/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package com.iver.cit.gvsig.project.documents.layout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.gvsig.fmap.mapcontrol.tools.BehaviorException;

import com.iver.cit.gvsig.project.documents.layout.gui.FPopupMenu;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.utiles.exceptionHandling.ExceptionHandlingSupport;
import com.iver.utiles.exceptionHandling.ExceptionListener;


/**
 * Eventos que se realizan sobre el Layout.
 *
 * @author Vicente Caballero Navarro
 */
public class LayoutEvents implements ActionListener, ComponentListener,
    MouseMotionListener, MouseListener{
    private Layout layout = null;
    private ExceptionHandlingSupport exceptionHandlingSupport = new ExceptionHandlingSupport();

    /**
     * Crea un nuevo EventsHandler.
     *
     * @param l Referencia al Layout.
     */
    public LayoutEvents(Layout l) {
        layout = l;
    }

    /**
     * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
     */
    public void componentHidden(ComponentEvent arg0) {
    }

    /**
     * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
     */
    public void componentMoved(ComponentEvent arg0) {
    }

    /**
     * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
     */
    public void componentResized(ComponentEvent arg0) {
        layout.getLayoutControl().fullRect();
    }

    /**
     * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
     */
    public void componentShown(ComponentEvent arg0) {
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {
        layout.repaint();
    }

    /**
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent e) {
    	if (e.getButton() != MouseEvent.BUTTON3) {
            layout.getLayoutControl().setLastPoint();
            layout.repaint();
        }
    	try {
			layout.getLayoutControl().getCurrentLayoutTool().mouseDragged(e);
		} catch (BehaviorException t) {
			throwException(t);
		}
        layout.getLayoutControl().setPosition(e.getPoint());
    }

    /**
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    public void mouseMoved(MouseEvent e) {
    	try {
			layout.getLayoutControl().getCurrentLayoutTool().mouseMoved(e);
		} catch (BehaviorException t) {
			throwException(t);
		}
        layout.getLayoutControl().setPosition(e.getPoint());
        layout.repaint();
    }

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {
    	try {
			layout.getLayoutControl().getCurrentLayoutTool().mouseClicked(e);
		} catch (BehaviorException t) {
			throwException(t);
		}
    }

    /**
	 * Añade un listener de tipo ExceptionListener.
	 *
	 * @param o ExceptionListener.
	 */
	public void addExceptionListener(ExceptionListener o) {
		exceptionHandlingSupport.addExceptionListener(o);
	}

	/**
	 * Borra la ExceptioListener que se pasa como parámetro.
	 *
	 * @param o ExceptionListener.
	 *
	 * @return True si se borra correctamente.
	 */
	public boolean removeExceptionListener(ExceptionListener o) {
		return exceptionHandlingSupport.removeExceptionListener(o);
	}

	/**
	 * Lanza una Excepción.
	 *
	 * @param t Excepción.
	 */
	protected void throwException(Throwable t) {
		exceptionHandlingSupport.throwException(t);
	}

	/**
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e) {
       layout.getLayoutControl().clearMouseImage();
       try {
			layout.getLayoutControl().getCurrentLayoutTool().mouseEntered(e);
		} catch (BehaviorException t) {
			throwException(t);
		}
    }

    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) {
    	try {
			layout.getLayoutControl().getCurrentLayoutTool().mouseExited(e);
		} catch (BehaviorException t) {
			throwException(t);
		}
    }

    /**
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
        	layout.getLayoutControl().setPointAnt();
            layout.getLayoutControl().setFirstPoint();
        	try {
    			layout.getLayoutControl().getCurrentLayoutTool().mousePressed(e);
    		} catch (BehaviorException t) {
    			throwException(t);
    		}
    	} else
    		if (e.getButton() == MouseEvent.BUTTON3) {
    			FPopupMenu popmenu = new FPopupMenu(layout);
				layout.add(popmenu);
				popmenu.show(e.getComponent(), e.getX(), e.getY());
    		}
    }

    /**
     * @see java.awt.event.MouseListener#mouseReleassed(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON3) {
            layout.getLayoutControl().setLastPoint();
        }

        if (e.getButton() == MouseEvent.BUTTON1) {
        	try {
    			layout.getLayoutControl().getCurrentLayoutTool().mouseReleased(e);
    		} catch (BehaviorException t) {
    			throwException(t);
    		}
            layout.getLayoutControl().setCancelDrawing(false);
        } else if (e.getButton() == MouseEvent.BUTTON3) {
        }


    }
}
