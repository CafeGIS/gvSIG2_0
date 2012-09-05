/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.rendering.symbols.ArrowMarkerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.IMarkerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.styles.ArrowDecoratorStyle;
import org.gvsig.gui.beans.DefaultBean;
import org.gvsig.gui.beans.swing.GridBagLayoutPanel;
import org.gvsig.gui.beans.swing.JButton;
import org.gvsig.gui.beans.swing.JIncrementalNumberField;
import org.gvsig.gui.beans.swing.ValidatingTextField;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.legend.gui.ISymbolSelector;

/**
 * Implements a tab to modify attributes of an arrow decorator (size,
 * arrow sharpnss, symbol, number of symbols to draw in the same line and options
 * for flip and rotate the arrow)which allows the user to insert a symbol in the
 * line (for example an arrow to specify its orientation)and to modify it.
 *
 * <p>
 * This tab is used several times in different places in our applicattion .For
 * this reason, in order to avoid the repetition of code, this class has been
 * created (instead of treat it like a simple tab). With this solution, the user
 * only has to refer it to use it (and do not need to create a tab and fill it again
 * and so on).

 * @autor jaume dominguez faus - jaume.dominguez@iver.es
 */
public class ArrowDecorator extends DefaultBean implements ActionListener {

	private JCheckBox chkFlipAll;
	private JCheckBox chkFlipFirst;
	private JRadioButton rdBtnFollowLine;
	private JRadioButton rdBtnFixedAngle;
	private JIncrementalNumberField incrPositionCount;
	private JIncrementalNumberField incrSharpness;
	private JCheckBox chkUseDecorator;
	private IMarkerSymbol marker;
	private JIncrementalNumberField incrSize;
	private JButton btnOpenSymbolSelector;

	public ArrowDecorator() {
		super();
		initialize();
	}

	private void initialize() {
		setName(PluginServices.getText(this, "arrow_decorator"));
		setLayout(new BorderLayout(10, 10));

		chkUseDecorator = new JCheckBox(PluginServices.getText(this, "use_decorator"));

		JPanel aux = new JPanel(new FlowLayout(FlowLayout.LEFT));
		GridBagLayoutPanel pnlTopOptions = new GridBagLayoutPanel();
		pnlTopOptions.addComponent(chkUseDecorator);
		pnlTopOptions.addComponent(PluginServices.getText(this, "size"),
				incrSize = new  JIncrementalNumberField(
						"0",
						5,
						ValidatingTextField.DOUBLE_VALIDATOR,
						ValidatingTextField.NUMBER_CLEANER,
						0,
						Integer.MAX_VALUE,
						1)
		);
		pnlTopOptions.addComponent(PluginServices.getText(this, "arrow_sharpness"),
				incrSharpness = new  JIncrementalNumberField(
						"0",
						5,
						ValidatingTextField.DOUBLE_VALIDATOR,
						ValidatingTextField.NUMBER_CLEANER,
						0,
						Integer.MAX_VALUE,
						1)
		);
		JPanel aux2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		aux2.add(	incrPositionCount = new JIncrementalNumberField(
				"0",
				5,
				ValidatingTextField.INTEGER_VALIDATOR,
				ValidatingTextField.NUMBER_CLEANER,
				0,
				Integer.MAX_VALUE,
				1)
		);
//		JButton btnChooseSymbol = new JButton(PluginServices.getText(this, "symbol"));
//		JPanel aux3 = new JPanel(new FlowLayout(FlowLayout.LEFT,5, 0));
//		aux3.add(btnChooseSymbol);
//		aux2.add(aux3);


		JPanel aux3 = new JPanel(new FlowLayout(FlowLayout.LEFT,5, 0));
		aux3.add(getBtnOpenSymbolSelector());
		aux2.add(aux3);

		pnlTopOptions.addComponent(PluginServices.getText(this, "number_of_positions")+":", aux2);
		aux.add(pnlTopOptions);
		add(aux, BorderLayout.NORTH);

		aux = new JPanel(new BorderLayout(5,5));
		aux2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		GridBagLayoutPanel pnlFlip = new GridBagLayoutPanel();
		pnlFlip.setBorder(BorderFactory.createTitledBorder(PluginServices.getText(this, "flip")+":"));
		pnlFlip.addComponent(chkFlipAll = new JCheckBox(PluginServices.getText(this, "flip_all")));
		pnlFlip.addComponent(chkFlipFirst = new JCheckBox(PluginServices.getText(this, "flip_first")));


		aux2.add(pnlFlip);

		GridBagLayoutPanel pnlRotation = new GridBagLayoutPanel();
		pnlRotation.setBorder(BorderFactory.createTitledBorder(PluginServices.getText(this, "rotation")+":"));
		pnlRotation.addComponent(rdBtnFollowLine = new JRadioButton(PluginServices.getText(this, "rotate_symbol_to_follow_line_angle")));
		pnlRotation.addComponent(rdBtnFixedAngle = new JRadioButton(PluginServices.getText(this, "keep_symbol_at_fixed_angle_to_page")));
		ButtonGroup group = new ButtonGroup();
		group.add(rdBtnFixedAngle);
		group.add(rdBtnFollowLine);
		aux2.add(pnlRotation);

		chkUseDecorator.addActionListener(this);
		incrPositionCount.addActionListener(this);
		incrSharpness.addActionListener(this);
		incrSize.addActionListener(this);
		chkFlipFirst.addActionListener(this);
		chkFlipAll.addActionListener(this);
		rdBtnFixedAngle.addActionListener(this);
		rdBtnFollowLine.addActionListener(this);

		aux.add(aux2, BorderLayout.CENTER);
		add(aux, BorderLayout.CENTER);
	}

	/**
	 * Defines  the attributes that appear in the arrow decorator tab and will
	 * determine the arrow decorator style.If this style has not been created previosly,
	 * it will be done.
	 * @param ads
	 */
	public void setArrowDecoratorStyle(ArrowDecoratorStyle ads) {
		chkUseDecorator.setSelected(ads!=null);

		if (ads == null) {
			ads = new ArrowDecoratorStyle();
		}

		marker = ads.getMarker();
		if (marker instanceof ArrowMarkerSymbol) {
			ArrowMarkerSymbol arrow = (ArrowMarkerSymbol) marker;
			incrSharpness.setDouble(arrow.getSharpness());
		}
		incrSize.setDouble(marker.getSize());
		incrPositionCount.setInteger(ads.getArrowMarkerCount());
		chkFlipAll.setSelected(ads.isFlipAll());
		chkFlipFirst.setSelected(ads.isFlipFirst());
		rdBtnFollowLine.setSelected(ads.isFollowLineAngle());

	}
	/**
	 * Obtains the values of the attributes of an arrow decorator.This attributes
	 * will be different depending on the type of the symbol that the user had selected
	 * (because if for example the arrow is changed into a square the sharpness won't
	 * necessary and so on)
	 *
	 * @return
	 */
	public ArrowDecoratorStyle getArrowDecoratorStyle() {
		if (!chkUseDecorator.isSelected()) return null;

		ArrowDecoratorStyle ads = new ArrowDecoratorStyle();
		if (marker == null) {
			marker = ads.getMarker();
		}

		if (marker instanceof ArrowMarkerSymbol) {
			ArrowMarkerSymbol arrow = (ArrowMarkerSymbol) marker;
			arrow.setSharpness(incrSharpness.getDouble());
		}
		marker.setSize(incrSize.getDouble());
		ads.setMarker(marker);
		ads.setArrowMarkerCount(incrPositionCount.getInteger());
		ads.setFlipAll(chkFlipAll.isSelected());
		ads.setFlipFirst(chkFlipFirst.isSelected());
		ads.setFollowLineAngle(rdBtnFollowLine.isSelected());
		return ads;
	}


	public void actionPerformed(ActionEvent e) {
		JComponent c = (JComponent) e.getSource();
		if (c.equals(getBtnOpenSymbolSelector())) {
			ISymbolSelector se = SymbolSelector.createSymbolSelector(marker, Geometry.TYPES.POINT);
			PluginServices.getMDIManager().addWindow(se);
			marker = (IMarkerSymbol) se.getSelectedObject();

		}
		boolean isArrow = marker instanceof ArrowMarkerSymbol;
		incrSharpness.setEnabled(isArrow);

		callValueChanged(getArrowDecoratorStyle());
	}

	private JButton getBtnOpenSymbolSelector() {
		if (btnOpenSymbolSelector == null) {
			btnOpenSymbolSelector = new JButton();
			btnOpenSymbolSelector.setText(PluginServices.getText(this, "choose_symbol"));
			btnOpenSymbolSelector.addActionListener(this);
		}
		return btnOpenSymbolSelector;
	}
}
