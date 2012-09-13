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
package org.gvsig.rastertools.properties.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.MouseMovementBehavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.PointBehavior;
import org.gvsig.gui.beans.doubleslider.DoubleSliderEvent;
import org.gvsig.gui.beans.doubleslider.DoubleSliderListener;
import org.gvsig.raster.datastruct.TransparencyRange;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.properties.panels.TranspByPixelPanel;
import org.gvsig.rastertools.properties.panels.TranspByPixelRGBInputPanel;
import org.gvsig.rastertools.toolselectrgb.ISelectRGB;
import org.gvsig.rastertools.toolselectrgb.SelectRGBListener;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
/**
 * Maneja los eventos para el panel de transparencia por pixel.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TranspByPixelListener implements ActionListener, ListSelectionListener, DoubleSliderListener, MouseListener, ISelectRGB {
	private ArrayList                  entries        = new ArrayList();
	private JButton                    addButton      = null;
	private JButton                    removeButton   = null;
	private JButton                    selectColorButton = null;
	private JCheckBox                  cbActivar      = null;
	private JList                      list           = null;
	private JRadioButton               andRb          = null;
	private JRadioButton               orRb           = null;
	private TranspByPixelPanel         panel          = null;
	private TranspByPixelRGBInputPanel rgbInputPanel  = null;
	private boolean                    eventsDisabled = false;
	private int                        itemSelected   = -1;
	private String viewName = "";

	/**
	 * This is the default constructor
	 */
	public TranspByPixelListener(TranspByPixelPanel panel) {
		BaseView view = (BaseView) PluginServices.getMDIManager().getActiveWindow();
		viewName = PluginServices.getMDIManager().getWindowInfo(view).getTitle();
		
		this.panel = panel;
		rgbInputPanel = panel.getPRGBInput();
		list = panel.getJList();

		addButton = panel.getBAdd();
		removeButton = panel.getBRemove();
		selectColorButton = panel.getBSelectColor();
		andRb = panel.getPOperation().getRbAnd();
		orRb = panel.getPOperation().getRbOr();
		cbActivar = panel.getActiveCheck();

		addButton.addActionListener(this);
		removeButton.addActionListener(this);
		andRb.addActionListener(this);
		orRb.addActionListener(this);
		cbActivar.addActionListener(this);
		selectColorButton.addActionListener(this);

		list.addListSelectionListener(this);

		panel.getPRGBInput().getTBlue().addValueChangedListener(this);
		panel.getPRGBInput().getTGreen().addValueChangedListener(this);
		panel.getPRGBInput().getTRed().addValueChangedListener(this);
		panel.getPRGBInput().getTAlpha().addValueChangedListener(this);
		list.addMouseListener(this);
	}

	/**
	 * Obtiene el objeto TransparencyRange equivalente a los valores RGBA
	 * seleccionados.
	 *
	 * @return TransparencyRange o null si la selección no es correcta.
	 */
	private TransparencyRange getEntrySelected() {
		TransparencyRange entry = new TransparencyRange();
		entry.setRed(rgbInputPanel.getRangeRed());
		entry.setGreen(rgbInputPanel.getRangeGreen());
		entry.setBlue(rgbInputPanel.getRangeBlue());
		if (rgbInputPanel.getTAlpha().isChecked())
			entry.setAlpha(255 - rgbInputPanel.getTAlpha().getValue());
		else
			entry.setAlpha(0);

		String textR = rgbInputPanel.getTRed().getValue() + "";
		if (!rgbInputPanel.getTRed().isChecked())
			textR = "*";
		String textG = rgbInputPanel.getTGreen().getValue() + "";
		if (!rgbInputPanel.getTGreen().isChecked())
			textG = "*";
		String textB = rgbInputPanel.getTBlue().getValue() + "";
		if (!rgbInputPanel.getTBlue().isChecked())
			textB = "*";

		String strEntry = "";
		String separator = "";
		if (andRb.isSelected()) {
			separator = " & ";
			entry.setAnd(true);
		} else {
			separator = " | ";
			entry.setAnd(false);
		}

		strEntry = textR + separator + textG + separator + textB;
		if (rgbInputPanel.getTAlpha().getValue() != 255)
			strEntry += " (A: " + rgbInputPanel.getTAlpha().getValue() + ")"; 
		entry.setStrEntry(strEntry);

		return entry;
	}

	/**
	 * Gestión del evento de botón de selección de color desde la vista. 
	 * Añade una tool a la vista para la selección de RGB.
	 */
	public void colorToolButton() {
		View theView = null;
		try {
			IWindow[] allViews = PluginServices.getMDIManager().getAllWindows();
			for (int i = 0; i < allViews.length; i++) {
				if (allViews[i] instanceof View &&
						PluginServices.getMDIManager().getWindowInfo((View) allViews[i]).getTitle().equals(viewName))
					theView = (View) allViews[i];
			}
			if (theView == null)
				return;
		} catch (ClassCastException ex) {
			RasterToolsUtil.messageBoxError("error_view_not_found", this, ex);
			return;
		}
		MapControl m_MapControl = theView.getMapControl();
		
		TranspByPixelStatusBarListener sbl = new TranspByPixelStatusBarListener(m_MapControl);
		SelectRGBListener selectRGBListener = new SelectRGBListener(m_MapControl, this);
		m_MapControl.addMapTool("selectColorRaster", new Behavior[] {
				new PointBehavior(selectRGBListener), new MouseMovementBehavior(sbl)
				}
		);

		m_MapControl.setTool("selectColorRaster");
	}
	
	/**
	 * Actualiza el item seleccionado cogiendo los valores del RGB
	 */
	private void updateData() {
		if (itemSelected == -1)
			return;

		TransparencyRange entry = getEntrySelected();
		if (entry != null) {
			if (itemSelected < entries.size()) {
				entries.set(itemSelected, entry);
				panel.getListModel().set(itemSelected, entry.getStrEntry());
				panel.callValueChanged();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {

		if ((e.getSource() == andRb) || (e.getSource() == orRb)) {
			updateData();
			return;
		}

		// Añade elementos a la lista
		if (e.getSource() == addButton) {
			TransparencyRange entry = getEntrySelected();
			if (entry != null) {
				entries.add(entry);
				itemSelected = entries.size() - 1;
				panel.getListModel().addElement(entry.getStrEntry());
				setValues(itemSelected);

				removeButton.setEnabled(true);
				panel.callValueChanged();
			}
			return;
		}

		// Elimina elementos de la lista
		if (e.getSource() == removeButton) {
			deleteSelected();
			return;
		}
		
		if (e.getSource() == selectColorButton) {
			colorToolButton();
			return;
		}

		if (e.getSource() == cbActivar) {
			panel.setControlEnabled(cbActivar.isSelected());
			panel.callValueChanged();
			return;
		}
	}

	private void deleteSelected() {
		if (itemSelected == -1)
			return;

		entries.remove(itemSelected);
		panel.getListModel().remove(itemSelected);

		if (itemSelected >= entries.size()) {
			itemSelected = entries.size() - 1;
		}
		setValues(itemSelected);

		if (entries.size() == 0)
			removeButton.setEnabled(false);
		panel.callValueChanged();
	}

	/**
	 * Establece los valores RGBA a sus componentes segun la seleccion
	 * @param item
	 */
	private void setValues(int item) {

		if (item >= entries.size()) {
			item = entries.size() - 1;
		}

		if (entries.size() == 0) {
			rgbInputPanel.getTRed().setValue(0);
			rgbInputPanel.getTRed().setChecked(true);
			rgbInputPanel.getTGreen().setValue(0);
			rgbInputPanel.getTGreen().setChecked(true);
			rgbInputPanel.getTBlue().setValue(0);
			rgbInputPanel.getTBlue().setChecked(true);
			rgbInputPanel.getTAlpha().setValue(255);
			rgbInputPanel.getTAlpha().setChecked(false);
			andRb.setSelected(true);
			orRb.setSelected(false);
			return;
		}
		TransparencyRange tr = (TransparencyRange) entries.get(item);

		andRb.setSelected(tr.isAnd());
		orRb.setSelected(!tr.isAnd());

		if (tr.getRed() == null) {
			rgbInputPanel.getTRed().setValue(0);
			rgbInputPanel.getTRed().setChecked(false);
		} else {
			rgbInputPanel.getTRed().setValue(tr.getRed()[0]);
			rgbInputPanel.getTRed().setChecked(true);
		}

		if (tr.getGreen() == null) {
			rgbInputPanel.getTGreen().setValue(0);
			rgbInputPanel.getTGreen().setChecked(false);
		} else {
			rgbInputPanel.getTGreen().setValue(tr.getGreen()[0]);
			rgbInputPanel.getTGreen().setChecked(true);
		}

		if (tr.getBlue() == null) {
			rgbInputPanel.getTBlue().setValue(0);
			rgbInputPanel.getTBlue().setChecked(false);
		} else {
			rgbInputPanel.getTBlue().setValue(tr.getBlue()[0]);
			rgbInputPanel.getTBlue().setChecked(true);
		}

		int alpha = 255 - tr.getAlpha();
		rgbInputPanel.getTAlpha().setValue(alpha);
		rgbInputPanel.getTAlpha().setChecked(alpha != 255);

		if (list.getSelectedIndex() != item) {
			list.setSelectedIndex(item);
		}
	}

	/**
	 * Obtiene el array de entradas de valores añadidos a la lista
	 * @return ArrayList
	 */
	public ArrayList getEntries() {
		return entries;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		eventsDisabled = true;
		if (list.getSelectedIndex() != -1)
			itemSelected = list.getSelectedIndex();

		setValues(itemSelected);
		eventsDisabled = false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.doubleslider.DoubleSliderListener#actionValueChanged(org.gvsig.gui.beans.doubleslider.DoubleSliderEvent)
	 */
	public void actionValueChanged(DoubleSliderEvent e) {
		if (eventsDisabled)
			return;
		eventsDisabled = true;
		updateData();
		eventsDisabled = false;
	}

	/**
	 * Habilita o deshabilita la gestion de eventos
	 * @param value
	 */
	public void setEventsDisabled(boolean value) {
		eventsDisabled = value;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == list)
			// Si es doble click borramos el seleccionado
			if (e.getClickCount() == 2)
				deleteSelected();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.toolselectrgb.ISelectRGB#actionRGBSelected(int, int, int)
	 */
	public void actionRGBSelected(int r, int g, int b) {
		rgbInputPanel.getTRed().setValue(r);
		rgbInputPanel.getTGreen().setValue(g);
		rgbInputPanel.getTBlue().setValue(b);
	}

	public void actionValueDragged(DoubleSliderEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}