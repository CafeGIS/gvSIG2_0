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
package org.gvsig.gui.beans.controls.comboscale;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gvsig.gui.beans.controls.IControl;

public class ComboScale extends JPanel implements IControl {
  private static final long serialVersionUID = 6483498713300082876L;

	private JLabel jLabel = null;

	private JComboBox jComboBox = null;

	private ArrayList actionCommandListeners = new ArrayList();

	// private Long[] scales;
	private boolean bDoCallListeners = true;

	private boolean isScaleCombo;

	static private int eventId = Integer.MIN_VALUE;

	// jaume
	private class ComboScaleItem {
		private long value;

		public ComboScaleItem(long itemScale) {
			this.value = itemScale;
		}

		public String toString() {
			return NumberFormat.getNumberInstance().format(value);
		}

		public boolean equals(Object obj) {
			return obj instanceof ComboScaleItem && ((ComboScaleItem) obj).getValue() == value;
		}

		public long getValue() {
			return value;
		}
	}
	/**
	 * This is the default constructor
	 */
	public ComboScale() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setHgap(0);
		flowLayout.setVgap(0);
		jLabel = new JLabel();
		jLabel.setText("1:");
		this.setLayout(flowLayout);
		this.setSize(155, 16);
		//this.setBorder(javax.swing.BorderFactory.createLineBorder(
		this.add(jLabel, null);
		this.add(getJComboBox(), null);
				//java.awt.Color.gray, 1));
	}

	/**
	 * This method initializes jComboBox
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComboBox() {
		if (jComboBox == null) {
			jComboBox = new JComboBox();
			jComboBox.setPreferredSize(new java.awt.Dimension(130, 16));
			jComboBox.setEditable(true);
			jComboBox.setMaximumRowCount(5);
			jComboBox.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD,
					10));
			jComboBox.setBackground(java.awt.SystemColor.window);
			jComboBox.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
			jComboBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (e.getActionCommand().equals("comboBoxChanged")) {

						// callActionCommandListeners(((Long)jComboBox.getSelectedItem()).longValue());
						// setScale(((Long)jComboBox.getSelectedItem()).longValue());
						Object item = jComboBox.getSelectedItem();
						long scale=0;
						if (item instanceof String) {
							StringBuffer sb = new StringBuffer((String) item);
							// remove any point in the number
							final String digits = "0123456789";
							int i = sb.charAt(0) == '-' ? 1 : 0;
							BitSet deleteChars=new BitSet();
							while (i < sb.length()) {
								if (digits.indexOf(sb.charAt(i))==-1)
									deleteChars.set(i);
								i++;
							}
							for (int k=deleteChars.size();k>=0;k--){
								if (deleteChars.get(k))
									sb.deleteCharAt(k);
							}
							jComboBox.removeItem(item);
							try{
								scale = Long.parseLong(sb.toString());
							}catch (NumberFormatException e1) {
							}
						} else {
							scale = ((ComboScaleItem) jComboBox.getSelectedItem())
							.getValue();
						}
						insertScaleIfNotPresent(scale);
						callActionCommandListeners(scale);
					}
				}
			});
		}
		return jComboBox;
	}

	public void setItems(long[] items) {
		ComboScaleItem[] scales = new ComboScaleItem[items.length];
		for (int i = 0; i < items.length; i++) {
			scales[i] = new ComboScaleItem(items[i]);
		}
		DefaultComboBoxModel newModel = new DefaultComboBoxModel(scales);
		getJComboBox().setModel(newModel);
	}

	/**
	 * This funcion ONLY sets the text in combo. It will NOT call listeners.
	 *
	 * @param scale
	 */
	public void setScale(long item) {
		bDoCallListeners = false;
		getJComboBox().setSelectedItem(new ComboScaleItem(item));
		bDoCallListeners = true;
	}

	/**
	 * @param scale
	 */
	private void insertScaleIfNotPresent(long scale) {
		// Si viene de un setScale, no insertamos la escala en el combo
		if (!bDoCallListeners)
			return;

		DefaultComboBoxModel model = (DefaultComboBoxModel) jComboBox
				.getModel();
		// model=new DefaultComboBoxModel();
		boolean inserted = false;
		for (int i = 0; i < model.getSize(); i++) {
			ComboScaleItem itemScale = (ComboScaleItem) model.getElementAt(i);
			if (scale == itemScale.getValue()) {
				inserted = true;
				break;
			}
		}
		if (!inserted) {
			for (int i = 0; i < model.getSize(); i++) {
				ComboScaleItem itemScale = (ComboScaleItem) model.getElementAt(i);
				if (scale < itemScale.getValue()) {
					model.insertElementAt(new ComboScaleItem(scale), i);
					inserted = true;
					break;
				}
			}
			if (!inserted)
				model.addElement(new ComboScaleItem(scale));
		}
		jComboBox.setSelectedItem(new ComboScaleItem(scale));
		isScaleCombo=true;
	}

	private void callActionCommandListeners(long scale) {
		if (!bDoCallListeners)
			return;

		Iterator acIterator = actionCommandListeners.iterator();
		while (acIterator.hasNext()) {
			ActionListener listener = (ActionListener) acIterator.next();
			listener.actionPerformed(new ActionEvent(this, eventId,
					"CHANGE_SCALE_" + scale));
		}
		eventId++;
	}

	public void addActionListener(ActionListener listener) {
		if (!actionCommandListeners.contains(listener))
			actionCommandListeners.add(listener);
	}

	public void removeActionListener(ActionListener listener) {
		actionCommandListeners.remove(listener);
	}

	/**
	 * Returns the current selected item.
	 *
	 * @return The value of the selected scale, or -1 if there was an invalid
	 *         value (ie. not long value).
	 */
	public long getScale() {
		return ((ComboScaleItem) jComboBox.getSelectedItem()).getValue();
	}

	/**
	 * Sets the label to be displayed on the left of the combo
	 */
	public void setLabel(String label) {
		jLabel.setText(label);
	}

	/**
	 * Gets the label
	 */
	public String getLabel() {
		return jLabel.getText();
	}

	public Object setValue(Object value) {
		if (isScaleCombo) {
			isScaleCombo=false;
			return null;
		}
		try {
			long scale = Long.parseLong((String)value);

			if (scale < 0)
				return null;

			ComboScaleItem item = new ComboScaleItem(scale);
			if (item.equals(jComboBox.getSelectedItem()))
				return item;
			this.setScale(scale);
			return item;
		} catch (NumberFormatException ex) {
			// don't change the status if the provided value was not valid
			return null;
		}
	}


	public void setEnabled(boolean enabled) {
        boolean oldEnabled = jComboBox.isEnabled();
        jComboBox.setEnabled(enabled);
        jComboBox.firePropertyChange("enabled", oldEnabled, enabled);
        if (enabled != oldEnabled) {
            jComboBox.repaint();
        }
    }

} // @jve:decl-index=0:visual-constraint="10,10"
