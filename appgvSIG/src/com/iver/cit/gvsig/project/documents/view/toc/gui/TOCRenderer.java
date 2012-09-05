/*
 * Created on 23-feb-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
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
package com.iver.cit.gvsig.project.documents.view.toc.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import org.gvsig.fmap.mapcontext.layers.FLyrDefault;

import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
import com.iver.cit.gvsig.project.documents.view.toc.TocItemBranch;



/**
 * Renderer que actua sobre el TOC.
 *
 * @author vcn
 */
public class TOCRenderer extends JPanel implements TreeCellRenderer {
    public Border bordeSeleccionado = BorderFactory.createEtchedBorder();

    /*public Border bordeSeleccionado = BorderFactory.createBevelBorder(BevelBorder.RAISED,
            SystemColor.black, SystemColor.lightGray, SystemColor.gray,
            SystemColor.lightGray);
    */
    private Border bordeNormal = BorderFactory.createEmptyBorder();
    
	private JCheckBox check;
	private JLabel label;

	private static final Font BIGGER_FONT = 
									  new Font("SansSerif", Font.BOLD, 12);

	GridBagLayout gridbag = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();
    /**
     * Creates a new TOCRenderer object.
     */
    public TOCRenderer() {
        this.setBackground(Color.lightGray);
        // this.setLayout(new BorderLayout());
        this.setLayout(new FlowLayout(FlowLayout.LEADING, 0,0));
		check = new JCheckBox();
		label = new JLabel();
		


		/* this.setLayout(gridbag);

		c.fill = GridBagConstraints.NONE;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.WEST;
		// c.weightx = check.getWidth();
		gridbag.setConstraints(check,c);
		this.add(check);
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(colorPanel,c);
		// c.weightx = colorPanel.getWidth();
		this.add(colorPanel);
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		c.fill = GridBagConstraints.HORIZONTAL;		
		gridbag.setConstraints(label,c);
		this.add(label); */ 



		/* this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(check);
		this.add(colorPanel);
		this.add(label); */
		
		/* this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(check);
		this.add(colorPanel);
		this.add(label); */  

		SpringLayout theLayout = new SpringLayout();		 
		this.setLayout(theLayout);

		//int rows = 1;
		//int cols = 2;
				
		/* this.setLayout(new BorderLayout());
		
		this.add(check, BorderLayout.WEST);
		this.add(label, BorderLayout.CENTER);
		*/ 
		/*
		GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        
        this.setLayout(gridbag);
        this.add(check);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(label, c);
        */
		
//		Adjust constraints for the text field so it's at
//		(<label's right edge> + 5, 5).
		this.add(check);
		this.add(label);  

		theLayout.putConstraint(SpringLayout.WEST, label,
						   5,
						   SpringLayout.EAST, check); 

		this.setBorder(bordeSeleccionado);

        check.setBackground(UIManager.getColor("Button.background"));
        label.setForeground(UIManager.getColor("Tree.textForeground"));
        
        
    }

    /**
     * Método llamado una vez por cada nodo, y todas las veces que se redibuja
     * en pantalla el TOC.
     *
     * @param tree
     * @param value
     * @param isSelected
     * @param expanded
     * @param leaf
     * @param row
     * @param hasFocus
     *
     * @return
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean isSelected, boolean expanded, boolean leaf, int row,
        boolean hasFocus) {
                
        DefaultMutableTreeNode n = (DefaultMutableTreeNode) value;
		String stringValue = "";
		this.setBackground(UIManager.getColor("Button.background"));
		this.label.setFont(tree.getFont());
		// System.out.println("ancho tree=" + tree.getWidth());

		if (n.getUserObject() instanceof ITocItem)
		{
			
	       	ITocItem item = (ITocItem) n.getUserObject();	       	
			stringValue = item.getLabel();
			
	        Dimension sizeNode = item.getSize(); // Se fija en el resize del TOC
	        
			this.setPreferredSize(sizeNode);
			
			if (item instanceof TocItemBranch)
			{
				TocItemBranch branch = (TocItemBranch) item;
				FLyrDefault lyr = (FLyrDefault)branch.getLayer();
				check.setVisible(true);
				check.setSelected(lyr.visibleRequired());
				if (!lyr.isAvailable()) {										
					check.setEnabled(false);
				} else {					
					check.setEnabled(true);
					if (!lyr.isWithinScale(lyr.getMapContext().getScaleView()))
						check.setEnabled(false);
					

					if (lyr.isEditing())
					{
						this.label.setForeground(Color.RED);
					}
					else this.label.setForeground(Color.BLACK);
				}
				if (lyr.isActive())
				{				
					this.setBorder(bordeSeleccionado);
					this.label.setFont(BIGGER_FONT);
				}
				else
				{				
					this.setBorder(bordeNormal);
				}				
	        }
			else
			{
				check.setVisible(false);
				this.setBorder(bordeNormal);			
				
			}
	        label.setText(stringValue);
	        Icon icono = item.getIcon();
	        if (icono != null)
	        {
	        	label.setIcon(icono);
	        	//System.out.println(">>>>>Pongo etiqueta " + stringValue + " con icono " + item.getIcon().toString());
	        }
	        this.setPreferredSize(sizeNode);
		}		
		// this.setPreferredSize(new Dimension(tree.getWidth()-60,24)); // sizeNode);

        if (leaf) {
            // label.setIcon(UIManager.getIcon("Tree.leafIcon"));
            
        } else if (expanded) {
            //label.setIcon(UIManager.getIcon("Tree.openIcon"));
        } else {
            //label.setIcon(UIManager.getIcon("Tree.closedIcon"));
        }

        return this;
    }

	public Rectangle getCheckBoxBounds() {
		return check.getBounds();
	}

}
