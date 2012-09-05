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
package com.iver.cit.gvsig.gui.styling;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.rendering.symbols.IFillSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.gvsig.fmap.mapcontext.rendering.symbols.styles.IMask;
import org.gvsig.gui.beans.swing.GridBagLayoutPanel;
import org.gvsig.gui.beans.swing.JButton;
import org.gvsig.gui.beans.swing.JIncrementalNumberField;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.legend.gui.ISymbolSelector;

/**
 * Implements a tab to modify attributes of a mask as style,size and
 * symbol (to represent a point in the map)which can be applied to
 * symbols like simple text, simple marker,picture marker and character marker.<p>
 * <p>
 * This tab is used several times in different places in our applicattion .For
 * this reason, in order to avoid the repetition of code, this class has been
 * created (instead of treat it like a simple tab). With this solution, the user
 * only has to refer it to use it (and do not need to create a tab and fill it again
 * and so on).
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class Mask extends JPanel {
	private JButton btnHaloSymbol;
	private JRadioButton rdBtnHalo;
	private JRadioButton rdBtnNone;
	private JIncrementalNumberField txtHaloSize;
	private IFillSymbol fill;
	private AbstractTypeSymbolEditor owner;
	private ActionListener action = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			owner.fireSymbolChangedEvent();
		};
	};
	/**
	 * Constructor method that initializes the parameters to create a tab to modify
	 * attributes of a mask for points such as style,size and symbol (to represent a point
	 * in the map).
	 * @param owner
	 */
	public Mask(AbstractTypeSymbolEditor owner) {
		super();
		setName(PluginServices.getText(this, "mask"));

		this.owner = owner;

		GridBagLayoutPanel aux = new GridBagLayoutPanel();
		aux.setBorder(BorderFactory.createTitledBorder(PluginServices.getText(this, "style")));
		JPanel stylePanel = new JPanel(new GridLayout(2, 1));
		stylePanel.add(getRdNone());
		stylePanel.add(getRdHalo());
		aux.addComponent(stylePanel);
		ButtonGroup group = new ButtonGroup();
		group.add(getRdNone());
		group.add(getRdHalo());

		JPanel aux2 = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 5));
		aux2.add(new JLabel(PluginServices.getText(this, "size")+":"));
		aux2.add(getTxtHaloSize());
		aux2.add(getBtnHaloSymbol());

		getRdNone().addActionListener(action);
		getRdHalo().addActionListener(action);
		getTxtHaloSize().addActionListener(action);

		add(aux);
		add(aux2);
	}
	/**
	 * Obbtains the size for the text halo.This size is taken from a
	 * JIncrementalNumberField. If this component does not exist,
	 * a new JIncrementalNumberField is created to specify it.
	 * @return
	 */
	private JIncrementalNumberField getTxtHaloSize() {
		if (txtHaloSize == null) {
			txtHaloSize = new JIncrementalNumberField(String.valueOf(0), 5, 0, Double.MAX_VALUE, 1);
		}

		return txtHaloSize;
	}

	/**
	 * Creates the button that allows the user to select the symbol that will substitute
	 * a point in the map.
	 * @return
	 */
	private JButton getBtnHaloSymbol() {
		if (btnHaloSymbol == null) {
			btnHaloSymbol = new JButton(PluginServices.getText(this, "symbol"));
			btnHaloSymbol.addActionListener(new ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ISymbolSelector symSel = SymbolSelector.createSymbolSelector(
							fill, Geometry.TYPES.SURFACE);
					PluginServices.getMDIManager().addCentredWindow(symSel);
					fill = (IFillSymbol) symSel.getSelectedObject();
				};
			});
		}

		return btnHaloSymbol;
	}

	/**
	 * Determines if the halo style is selected.If the Radio button
	 * that determines this information does not exist, a new radio button
	 * is created for this purpose.
	 * @return
	 */
	private JRadioButton getRdHalo() {
		if (rdBtnHalo == null) {
			rdBtnHalo = new JRadioButton(PluginServices.getText(this, "halo"));
		}

		return rdBtnHalo;
	}

	/**
	 * Determines if there will be no defined style for a point (without halo).
	 * If the Radio button that determines this information does not exist,
	 * a new radio button is created for this purpose.
	 * @return
	 */
	private JRadioButton getRdNone() {
		if (rdBtnNone == null) {
			rdBtnNone = new JRadioButton(PluginServices.getText(this, "none"));
			rdBtnNone.setSelected(true);
		}

		return rdBtnNone;
	}
	/**
	 * Sets the graphical component that shows the properties of the model.
	 * @param mask
	 */
	public void setModel(IMask mask) {
		if (mask != null) {
			getTxtHaloSize().setDouble(mask.getSize());
			fill = mask.getFillSymbol();
		}
		getRdHalo().setSelected(mask != null);
	}
	/**
	 * Returns an IMask or null depending on the option
	 * that the user had decided (if he wants a mask or not)in the tab "mask" inside
	 * the panel to edit the properities of a symbol (SymbolEditor).
	 * If the user
	 * wants it, a new IMask is created.
	 * @return
	 */
	public IMask getMask() {
		if (!getRdHalo().isSelected()) return null;

		IMask mask = new IMask.BasicMask();
		if (fill == null) {
			fill = SymbologyFactory.createDefaultFillSymbol();
		}
		mask.setFillSymbol(fill);
		mask.setSize(getTxtHaloSize().getDouble());
		return mask;
	}

}
