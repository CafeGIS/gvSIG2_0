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
package com.iver.utiles.console;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

public class JDockPanel extends JPanel {

	private JPanel jPanelNorth = null;
	private JPanel jPanelButtons = null;
	private JComponent centerComponent;
	private JButton jButtonUp = null;
	private JButton jButtonDown = null;
	private JPanel jPanelCenterNorth = null;
	private JSeparator jSeparator1 = null;
	private JDockPanel the;
	private int originalCenterComponentHeight;

	public JDockPanel(JComponent centerComponent) {
		super();	
		the = this;
		initialize();
		this.centerComponent = centerComponent;
		this.originalCenterComponentHeight = centerComponent.getPreferredSize().height;
		this.add(centerComponent, BorderLayout.CENTER);
		
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setLayout(new BorderLayout());
        this.setSize(new java.awt.Dimension(388,225));
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
        this.add(getJPanelNorth(), java.awt.BorderLayout.NORTH);
			
	}

	/**
	 * This method initializes jPanelNorth	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelNorth() {
		if (jPanelNorth == null) {
			jPanelNorth = new JPanel();
			jPanelNorth.setLayout(new BorderLayout());
			jPanelNorth.setPreferredSize(new java.awt.Dimension(100,12));
			jPanelNorth.add(getJPanelCenterNorth(), java.awt.BorderLayout.CENTER);
			jPanelNorth.add(getJPanelButtons(), java.awt.BorderLayout.EAST);
			
			
		}
		return jPanelNorth;
	}

	/**
	 * This method initializes jPanelButtons	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			jPanelButtons.setLayout(null);
			jPanelButtons.setPreferredSize(new java.awt.Dimension(51,12));
			jPanelButtons.add(getJButtonUp(), null);
			jPanelButtons.add(getJButtonDown(), null);
		}
		return jPanelButtons;
	}

	/**
	 * This method initializes jButtonUp	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonUp() {
		if (jButtonUp == null) {
			jButtonUp = new JButton();
			jButtonUp.setCursor(Cursor.getDefaultCursor());
			jButtonUp.setIcon(new ImageIcon(getClass().getResource("/images/up" +
					".png")));
			jButtonUp.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
			jButtonUp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			jButtonUp.setBounds(new java.awt.Rectangle(3,0,20,12));
			jButtonUp.setPreferredSize(new java.awt.Dimension(20,12));
			jButtonUp.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// UP
					centerComponent.setPreferredSize(new Dimension(centerComponent.getWidth(), originalCenterComponentHeight));
					int hTot = centerComponent.getPreferredSize().height + getJPanelNorth().getHeight();
					
					// the.setLocation(0, the.getParent().getHeight()-hTot);
//					the.centerComponent.setLocation(0, getJPanelNorth().getHeight());
					the.reshape(0, the.getParent().getHeight()-hTot, the.getWidth(), hTot);
					the.validate();
					the.doLayout();
//					centerComponent.validate();
//					centerComponent.doLayout();

				}
			});
		}
		return jButtonUp;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonDown() {
		if (jButtonDown == null) {
			jButtonDown = new JButton();
			jButtonDown.setCursor(Cursor.getDefaultCursor());
			jButtonDown.setPreferredSize(new java.awt.Dimension(20,12));
			jButtonDown.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
			jButtonDown.setIcon(new ImageIcon(getClass().getResource("/images/down.png")));
			jButtonDown.setBounds(new java.awt.Rectangle(26,0,20,12));
			jButtonDown.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			jButtonDown.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// DOWN
					centerComponent.setPreferredSize(new Dimension(centerComponent.getWidth(), 0));
					
					int hTot = getJPanelNorth().getHeight();
					// the.setLocation(0, the.getParent().getHeight()-hTot);
					the.reshape(0, the.getParent().getHeight()-hTot, the.getWidth(), hTot);
					// the.setPreferredSize()
					the.validate();
					the.doLayout();
				}
			});
		}
		return jButtonDown;
	}

	/**
	 * This method initializes jPanelCenterNorth	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelCenterNorth() {
		if (jPanelCenterNorth == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new java.awt.Insets(2,0,2,0);
			gridBagConstraints.weightx = 90.0D;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			jPanelCenterNorth = new JPanel();
			jPanelCenterNorth.setLayout(new GridBagLayout());
			jPanelCenterNorth.add(getJSeparator1(), gridBagConstraints);
		}
		return jPanelCenterNorth;
	}

	/**
	 * This method initializes jSeparator1	
	 * 	
	 * @return javax.swing.JSeparator	
	 */
	private JSeparator getJSeparator1() {
		if (jSeparator1 == null) {
			jSeparator1 = new JSeparator();
			jSeparator1.setPreferredSize(new java.awt.Dimension(100,5));
			jSeparator1.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
			jSeparator1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
				public void mouseDragged(java.awt.event.MouseEvent e) {
					Point pExt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), the.getParent());
					Dimension sizeFather = the.getParent().getSize();
					int heightFather = sizeFather.height;
					Dimension newDimension = new Dimension(the.getSize().width, heightFather - pExt.y);
					Dimension newCDimension = new Dimension(the.getSize().width, heightFather - pExt.y - getJPanelNorth().getHeight());
					Point actualOrigin = the.getLocation();
//					the.setSize(newDimension);
//					the.setBounds(actualOrigin.x, pExt.y, newDimension.width, newDimension.height);
					the.reshape(actualOrigin.x, pExt.y, newDimension.width, newDimension.height);
//					centerComponent.reshape(0, newDimension.width, getJPanelNorth().getHeight(), newDimension.height-getJPanelNorth().getHeight());
//					the.doLayout();
//			        ComponentEvent ev = new ComponentEvent(the,
//                            ComponentEvent.COMPONENT_RESIZED);
//			        the.dispatchEvent(ev);					
//					invalidate();
//					repaint();
					originalCenterComponentHeight = newDimension.height;
					centerComponent.setPreferredSize(newCDimension);
					the.validate();
					the.doLayout();
				}
			});
		}
		return jSeparator1;
	}

	public JComponent getCenterComponent() {
		return centerComponent;
	}

	public void setCenterComponent(JComponent centerComponent) {
		if (this.centerComponent != null)
		{
			this.remove(this.centerComponent);
		}
		this.centerComponent = centerComponent;
		this.originalCenterComponentHeight = centerComponent.getPreferredSize().height;
		this.add(centerComponent, BorderLayout.CENTER);

	}

}  //  @jve:decl-index=0:visual-constraint="10,10"


