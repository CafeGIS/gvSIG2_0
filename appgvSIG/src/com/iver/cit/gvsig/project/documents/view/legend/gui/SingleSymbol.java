/*
 * Created on 30-abr-2004
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
package com.iver.cit.gvsig.project.documents.view.legend.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontext.rendering.legend.ILegend;
import org.gvsig.fmap.mapcontext.rendering.legend.LegendFactory;
import org.gvsig.fmap.mapcontext.rendering.legend.SingleSymbolLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.ZSort;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.gui.beans.swing.GridBagLayoutPanel;
import org.gvsig.gui.beans.swing.JButton;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.gui.styling.SymbolLevelsWindow;
import com.iver.cit.gvsig.gui.styling.SymbolPreviewer;
import com.iver.cit.gvsig.gui.styling.SymbolSelector;

/**
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class SingleSymbol extends JPanel implements ILegendPanel, ActionListener {
	private JPanel symbolPanel = null;
	private int shapeType;
	private GridBagLayoutPanel legendPanel = null;
	private SymbolPreviewer symbolPreviewComponent;
	private JButton btnOpenSymbolSelector;
	private JTextField txtLabel;
	private JButton btnOpenSymbolLevelsEditor;
	private SingleSymbolLegend legend;
	private ZSort zSort;


	public SingleSymbol() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 *
	 */
	private void initialize() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setSize(new java.awt.Dimension(490,379));
        this.add(getSymbolPanel(), null);
        this.add(getLegendPanel(), null);

	}

	public void setData(FLayer lyr, ILegend legend) {
		try {
			shapeType = ((FLyrVect) lyr).getShapeType();
		} catch (ReadException e) {
			NotificationManager.addError("Could not find out the shape type" ,e);
		}
		if (legend instanceof SingleSymbolLegend) {
			setSymbol(legend.getDefaultSymbol());
			this.legend = (SingleSymbolLegend) legend;

		} else {
			this.legend = (SingleSymbolLegend) LegendFactory.
					createSingleSymbolLegend(shapeType);
		}
		getSymbolPreviewPanel().setSymbol(this.legend.getDefaultSymbol());
		getBtnOpenSymbolLevelsEditor().setEnabled(legend!=null);
		this.txtLabel.setText(legend.getDefaultSymbol().getDescription());
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.gui.legendmanager.panels.ILegendPanel#getLegend()
	 */
	public ILegend getLegend() {
		ISymbol symbol = getSymbolPreviewPanel().getSymbol();
		symbol.setDescription(txtLabel.getText());
		SingleSymbolLegend leg = new SingleSymbolLegend(symbol);
		leg.setZSort(zSort);
		return leg;
	}

	public String getDescription() {
		return PluginServices.getText(this,"Muestra_todos_s_elementos_de_una_capa_usando_el_mismo_simbolo");
	}

	public Class getParentClass() {
		return Features.class;
	}

	public String getTitle() {
		return PluginServices.getText(this,"Simbolo_unico");
	}

	public JPanel getPanel() {
		return this;
	}

	public ImageIcon getIcon() {
		return new ImageIcon(this.getClass().getClassLoader().
				getResource("images/single-symbol.png"));
	}

	public Class getLegendClass() {
		return SingleSymbolLegend.class;
	}
	/**
	 * This method initializes symbolPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getSymbolPanel() {
		if (symbolPanel == null) {
			symbolPanel = new JPanel();
			symbolPanel.setBorder(
					BorderFactory.createTitledBorder(null, "symbol", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
			symbolPanel.add(getSymbolPreviewPanel());
			symbolPanel.add(getBtnOpenSymbolSelector());
			symbolPanel.add(getBtnOpenSymbolLevelsEditor());
		}
		return symbolPanel;
	}

	private JButton getBtnOpenSymbolLevelsEditor() {
		if (btnOpenSymbolLevelsEditor == null) {
			btnOpenSymbolLevelsEditor = new JButton(PluginServices.getText(this, "symbol_levels"));
			btnOpenSymbolLevelsEditor.addActionListener(this);
			btnOpenSymbolLevelsEditor.setEnabled(legend != null);
		}

		return btnOpenSymbolLevelsEditor;
	}
	private JButton getBtnOpenSymbolSelector() {
		if (btnOpenSymbolSelector == null) {
			btnOpenSymbolSelector = new JButton();
			btnOpenSymbolSelector.setText(PluginServices.getText(this, "choose_symbol"));
			btnOpenSymbolSelector.addActionListener(this);
		}
		return btnOpenSymbolSelector;
	}

	private SymbolPreviewer getSymbolPreviewPanel() {
		if (symbolPreviewComponent == null) {
			symbolPreviewComponent = new SymbolPreviewer();
			symbolPreviewComponent.setBorder(BorderFactory.createBevelBorder(1));
			symbolPreviewComponent.setPreferredSize(new Dimension(120, 40));
		}
		return symbolPreviewComponent;
	}
	/**
	 * This method initializes legendPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private GridBagLayoutPanel getLegendPanel() {
		if (legendPanel == null) {
			legendPanel = new GridBagLayoutPanel();
			legendPanel.setBorder(BorderFactory.createTitledBorder(null,
					PluginServices.getText(this, "legend"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
			legendPanel.addComponent(PluginServices.getText(this, "label_text_in_the_TOC") + ":", txtLabel = new JTextField(25));
			txtLabel.addActionListener(this);
		}
		return legendPanel;
	}

	public void setShapeType(int shapeType) {
		this.shapeType = shapeType;
	}

	public void setSymbol(ISymbol symbol) {
		setOnlySymbol(symbol);
		if(symbol.getDescription() != null) {
			txtLabel.setText(symbol.getDescription());
		}
	}

	private void setOnlySymbol(ISymbol symbol){
		getSymbolPreviewPanel().setSymbol(symbol);
		if (legend != null){
			legend.setDefaultSymbol(symbol);
		}
	}


	public ISymbol getSymbol() {
		ISymbol symbol = getSymbolPreviewPanel().getSymbol();
		symbol.setDescription(txtLabel.getText());
		return symbol;
	}
	public boolean isSuitableFor(FLayer layer) {
		return (layer instanceof FLyrVect) ;
	}

	public void actionPerformed(ActionEvent e) {
		JComponent c = (JComponent) e.getSource();
		if (c.equals(getBtnOpenSymbolSelector())){
			ISymbolSelector se = SymbolSelector.createSymbolSelector(getSymbol(), shapeType);
			PluginServices.getMDIManager().addWindow(se);
			ISymbol sym = (ISymbol) se.getSelectedObject();
			if (sym != null) {
				// no symbol, no changes
				setOnlySymbol(sym);
			}
		} else if (c.equals(getBtnOpenSymbolLevelsEditor())){
			if (legend != null) {
				ZSort myZSort = legend.getZSort();
				if (myZSort == null) {
					myZSort = new ZSort(legend);
				}
				SymbolLevelsWindow sl = new SymbolLevelsWindow(myZSort);
				PluginServices.getMDIManager().addWindow(sl);
				zSort = sl.getZSort();
			}
		}else if (c.equals(txtLabel)){
			getSymbolPreviewPanel().getSymbol().setDescription(txtLabel.getText());
		}
	}

}