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
 */
package org.gvsig.rastertools.generictoolbar;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JToolBar;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.gui.beans.comboboxconfigurablelookup.DefaultComboBoxConfigurableLookUpModel;
import org.gvsig.gui.beans.comboboxconfigurablelookup.JComboBoxConfigurableLookUp;
import org.gvsig.gui.beans.controls.combobutton.ComboButton;
import org.gvsig.gui.beans.controls.combobutton.ComboButtonEvent;
import org.gvsig.gui.beans.controls.combobutton.ComboButtonListener;
import org.gvsig.raster.gui.IGenericToolBarMenuItem;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
/**
 * Componente que contiene los objetos visuales de la barra de herramientas
 * generica
 *
 * @version 13/02/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class GenericToolBarPanel extends JToolBar implements ComboButtonListener, ActionListener, MouseListener, ItemListener {
	private static final long serialVersionUID = -4382962282970490523L;
	private JComboBoxConfigurableLookUp jCBCLU = null;
	private ComboButton buttonGroup = new ComboButton();
	private ComboButton buttonMenu = new ComboButton();
	private FLayers lastLayers = null;

	public GenericToolBarPanel() {
		super("GenericToolBarPanel");
		initialize();
	}

	/**
	 * Especifica que las capas de la vista han cambiado.
	 * @param layers
	 */
	public void setLayers(FLayers layers) {
		lastLayers = layers;

		getComboBoxConfigurableLookUp().setBlockPopupHided(true);
		getComboBoxConfigurableLookUp().removeAllItems();

		ArrayList lyrs = RasterToolsUtil.getLayerList(layers, null);
		for (int i = 0; i < lyrs.size(); i++)
			getComboBoxConfigurableLookUp().addItem(((FLayer)lyrs.get(i)).getName());

		if (layers.getActives().length > 0)
			getComboBoxConfigurableLookUp().setSelectedItem(layers.getActives()[0].getName());
		else if (layers.getLayersCount() > 0)
			getComboBoxConfigurableLookUp().setSelectedItem(layers.getLayer(0).getName());
		reloadSubMenu();
		getComboBoxConfigurableLookUp().setBlockPopupHided(false);
	}

	/**
	 * Devuelve un combo de busqueda de items.
	 * @return
	 */
	private JComboBoxConfigurableLookUp getComboBoxConfigurableLookUp() {
		if (jCBCLU == null) {
			jCBCLU = new JComboBoxConfigurableLookUp();
			jCBCLU.setOnlyOneColorOnText(true);
			jCBCLU.setPrototypeDisplayValue(" ");
			if (jCBCLU.getModel() instanceof DefaultComboBoxConfigurableLookUpModel) {
				((DefaultComboBoxConfigurableLookUpModel) jCBCLU.getModel()).setShowAllItemsInListBox(false);
				((DefaultComboBoxConfigurableLookUpModel) jCBCLU.getModel()).setLookUpAgent(new BinarySearch());
				((DefaultComboBoxConfigurableLookUpModel) jCBCLU.getModel()).setCaseSensitive(false);
				jCBCLU.setToForceSelectAnItem(true);
				jCBCLU.setDisplayAllItemsWithArrowButton(true);
			}
			jCBCLU.addActionListener(this);
		}
		return jCBCLU;
	}

	private void initialize() {
		buttonGroup.addComboButtonClickedListener(this);
		buttonGroup.setName("Menu principal");
		buttonGroup.setMargin(new Insets(0, 0, 0, 0));
		buttonGroup.setAlwaysMenuOnClick(true);
		reloadMenuGroup();
		add(buttonGroup);

		buttonMenu.addComboButtonClickedListener(this);
		buttonMenu.setName("Submenus");
		buttonMenu.addMouseListener(this);
		buttonMenu.setMargin(new Insets(0, 0, 0, 0));
		reloadMenuGroup();
		add(buttonMenu);

		add(getComboBoxConfigurableLookUp());
		this.setRollover(true);
	}

	/**
	 * Recarga los items del menu global dejando seleccionado el item que habia
	 * previamente, en caso de que exista
	 */
	public void reloadMenuGroup() {
		FLayer[] layers = new FLayer[] { getLayerSelected() };
		ButtonItems buttonItems = new ButtonItems(buttonGroup, layers);

		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("GenericToolBarGroup");
		Iterator iterator = point.iterator();
		while (iterator.hasNext()) {
			ExtensionPoint.Extension extension = (ExtensionPoint.Extension) iterator
					.next();
			Object object;
			try {
				object = extension.create();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if (object instanceof IGenericToolBarMenuItem)
				buttonItems.addButton((IGenericToolBarMenuItem) object, extension
						.getName());
		}
		buttonItems.refresh();
		reloadSubMenu();
	}

	/**
	 * Recarga los items del submenu dejando seleccionado el item que habia
	 * previamente, en caso de que exista
	 */
	public void reloadSubMenu() {
		FLayer[] layers = new FLayer[] { getLayerSelected() };
		ButtonItems buttonItems = new ButtonItems(buttonMenu, layers);
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.add("GenericToolBarMenu");
		Iterator iterator = point.iterator();
		while (iterator.hasNext()) {
			ExtensionPoint.Extension entry = (ExtensionPoint.Extension) iterator
					.next();
			Object object;
			try {
				object = entry.create();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if (object instanceof IGenericToolBarMenuItem) {
				IGenericToolBarMenuItem item = (IGenericToolBarMenuItem) object;
				if (!buttonGroup.getActionCommand().equals(item.getGroup()))
					continue;
				buttonItems.addButton(item, entry.getName());
			}
		}
		buttonItems.refresh();
	}

	/**
	 * Devuelve el layer seleccionado en el combobox
	 * @return
	 */
	public FLayer getLayerSelected() {
		if (lastLayers == null)
			return null;
		ArrayList lyrs = RasterToolsUtil.getLayerList(lastLayers, null);
		for (int i = 0; i < lyrs.size(); i++)
			if (((FLayer)lyrs.get(i)).getName().equals(getComboBoxConfigurableLookUp().getEditor().getItem()))
				return (FLayer)lyrs.get(i);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getComboBoxConfigurableLookUp()) {
//			selectLayerInTOC(lastLayers, (String) getComboBoxConfigurableLookUp().getSelectedItem());
		}
	}

		/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionComboButtonClicked(ComboButtonEvent e) {
		if (e.getSource() == buttonGroup) {
			reloadSubMenu();
			return;
		}
		if (e.getSource() == buttonMenu) {
			ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
			ExtensionPoint point=extensionPoints.add("GenericToolBarMenu");
			Iterator iterator = point.iterator();
			while (iterator.hasNext()) {
				ExtensionPoint.Extension entry = (ExtensionPoint.Extension) iterator
						.next();
				if (!entry.getName().equals(
						((JButton) e.getSource()).getActionCommand()))
					continue;
				Object object;
				try {
					object = entry.create();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					throw new RuntimeException(e1);
				}
				if (object instanceof IGenericToolBarMenuItem) {
					IGenericToolBarMenuItem item = (IGenericToolBarMenuItem) object;

					FLayer[] layers = new FLayer[] { getLayerSelected() };
					if (!item.isVisible(null, layers))
						continue;
					if (!item.isEnabled(null, layers))
						continue;

					if (getLayerSelected() != null) {
						item.execute(null, new FLayer[] { getLayerSelected() });
						reloadSubMenu();
					}
					return;
				}
			}
			return;
		}
	}

	/**
	 * Selecciona una capa en el TOC
	 * @param layers
	 * @param nameLayer
	 */
//	private void selectLayerInTOC(FLayers layers, String nameLayer) {
//		if (layers == null)
//			return;
//		for (int i = 0; i < layers.getLayersCount(); i++) {
//			if (layers.getLayer(i) instanceof FLayers) {
//				selectLayerInTOC((FLayers) layers.getLayer(i), nameLayer);
//			} else {
//				if (layers.getLayer(i) instanceof FLayer) {
//					FLayer layer = ((FLayer) layers.getLayer(i));
//					if (layer.getName().equals(nameLayer) && !layer.isActive()) {
//						layer.setActive(true);
//					}
//				}
//			}
//		}
//	}

	/**
	 * Recargo el menu cada vez que entra el raton en el boton, para ahorrar tiempo en
	 * la visualizacion del mismo
	 */
	public void mouseEntered(MouseEvent e) {
		this.reloadSubMenu();
	}

	public void itemStateChanged(ItemEvent e) {
//		if (e.getStateChange() != ItemEvent.SELECTED)
//			return;
//
//		FLayer layer = getLayerSelected();
//
//		if (layer == null)
//			return;
//
//		FLayers layers = lastLayers;
//		for(int i = 0; i < layers.getLayersCount(); i++)
//			layers.getLayer(i).setActive(false);
//
//		layer.setActive(true);
//		View view = (View) PluginServices.getMDIManager().getActiveWindow();
//		JScrollBar verticalBar = view.getTOC().getJScrollPane().getVerticalScrollBar();
//		double widthPerEntry = verticalBar.getMaximum() / layer.getMapContext().getLayers().getLayersCount();
//		verticalBar.setValue((int)widthPerEntry * (layer.getMapContext().getLayers().getLayersCount() - pos - 1));
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

}