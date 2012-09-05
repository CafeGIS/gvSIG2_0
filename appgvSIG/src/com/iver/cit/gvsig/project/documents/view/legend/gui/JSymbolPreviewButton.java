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

/* CVS MESSAGES:
*
* $Id: JSymbolPreviewButton.java 28370 2009-05-04 16:14:34Z vcaballero $
* $Log$
* Revision 1.4  2007-09-17 09:21:45  jaume
* refactored SymboSelector (added support for multishapedsymbol)
*
* Revision 1.3  2007/08/09 10:39:04  jaume
* first round of found bugs fixed
*
* Revision 1.2  2007/03/09 11:25:00  jaume
* Advanced symbology (start committing)
*
* Revision 1.1.2.4  2007/02/21 07:35:14  jaume
* *** empty log message ***
*
* Revision 1.1.2.3  2007/02/14 10:00:45  jaume
* *** empty log message ***
*
* Revision 1.1.2.2  2007/02/14 09:59:17  jaume
* *** empty log message ***
*
* Revision 1.1.2.1  2007/02/13 16:19:19  jaume
* graduated symbol legends (start commiting)
*
*
*/
package com.iver.cit.gvsig.project.documents.view.legend.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbolDrawingException;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.styling.SymbolSelector;
/**
 * Just a Button that shows an ISymbol instead a text.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
public class JSymbolPreviewButton extends JComponent implements MouseListener {
	private static final long serialVersionUID = -7878718124556977288L;
	private ISymbol prev;
	private boolean pressed;
	private int shapeType;
	private ArrayList<ActionListener> listeners;  //  @jve:decl-index=0:
	private ActionEvent event;
	private String actionCommand;

	/**
	 * @return the actionCommand
	 */
	public String getActionCommand() {
		return actionCommand;
	}



	/**
	 * @param actionCommand the actionCommand to set
	 */
	public void setActionCommand(String actionCommand) {
		this.actionCommand = actionCommand;
	}



	public JSymbolPreviewButton(int shapeType) {
		this(null, shapeType);
	}

	public JSymbolPreviewButton(ISymbol sym,
			int shapeType) {
		super();
		addMouseListener(this);
		mouseExited(null);
		setPreferredSize(new Dimension(150, 20));
		this.shapeType = shapeType;
		this.prev = sym;
   }



	public void paint(Graphics g) {
		Rectangle bounds = getBounds();
		Graphics2D g2 = (Graphics2D) g;
		if (g2 != null) {
			g2.setStroke(new BasicStroke(2));
			g2.setColor(pressed && isEnabled() ? Color.GRAY : Color.WHITE);
			g2.drawLine(0, 0, (int) bounds.getWidth(), 0);
			g2.drawLine(0, 0, 0, (int) bounds.getHeight());
			g2.setColor(pressed && isEnabled() ? Color.WHITE : Color.GRAY);
			g2.drawLine(2, (int) bounds.getHeight(), (int) bounds.getWidth(), (int) bounds.getHeight());
			g2.drawLine((int) bounds.getWidth(), 0, (int) bounds.getWidth(), (int) bounds.getHeight());

			Rectangle2D r = new Rectangle2D.Double(3, 3, bounds.getWidth()-3, bounds.getHeight()-6);

			if (prev!=null) {
				try {
					prev.drawInsideRectangle(g2, g2.getTransform(), r.getBounds(),null);
				} catch (SymbolDrawingException e) {
					if (e.getType() == SymbolDrawingException.UNSUPPORTED_SET_OF_SETTINGS) {
						try {
							SymbologyFactory.getWarningSymbol(SymbolDrawingException.STR_UNSUPPORTED_SET_OF_SETTINGS,
									prev.getDescription(),
									SymbolDrawingException.UNSUPPORTED_SET_OF_SETTINGS
									).drawInsideRectangle(g2, g2.getTransform(), r.getBounds(),null);
						} catch (SymbolDrawingException e1) {
							// IMPOSSIBLE TO REACH THIS
						}
					} else {
						// should be unreachable code
						throw new Error(PluginServices.getText(this, "symbol_shapetype_mismatch"));
					}
				}
			}
		}
	}

	public void setSymbol(ISymbol symbol) {
		this.prev = symbol;
		paintImmediately(getBounds());
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			ISymbolSelector sSelect = SymbolSelector.createSymbolSelector(prev, shapeType);
			PluginServices.getMDIManager().addWindow(sSelect);
			setSymbol((ISymbol) sSelect.getSelectedObject());
			fireActionListeners();
		}
	}

	private void fireActionListeners() {
		if (!isEnabled()) return;
		if (listeners != null) {
			if (event == null) {
				event = new ActionEvent(this, 0, actionCommand);
			}
			for (int i = 0; i < listeners.size(); i++) {
				((ActionListener) listeners.get(i)).actionPerformed(
						event);
			}
		}

	}



	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {
		setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		pressed = false;
		paintImmediately(getBounds());
	}

	public void mousePressed(MouseEvent e) {

		setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		pressed = true;
		paintImmediately(getBounds());
	}

	public void mouseReleased(MouseEvent e) {
		if (pressed)
			mouseClicked(e);
		mouseExited(e);
	}



	public ISymbol getSymbol() {
		return prev;
	}



	public void addActionListener(ActionListener l) {
		if (listeners == null)
			listeners = new ArrayList<ActionListener>();
		listeners.add(l);
	}



	public void setShapeType(int shapeType) {
		this.shapeType = shapeType;
	}


}
