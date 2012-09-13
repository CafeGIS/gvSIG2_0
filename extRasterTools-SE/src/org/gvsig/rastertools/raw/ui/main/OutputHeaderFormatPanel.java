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
package org.gvsig.rastertools.raw.ui.main;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gvsig.rastertools.raw.tools.VRTFormatOptions;

import com.iver.andami.PluginServices;
import com.iver.utiles.swing.JComboBox;
/**
 * This class implements one part of the open raw file panel. It contains the
 * combobox to select the "output header format".
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class OutputHeaderFormatPanel extends JPanel {
  private static final long serialVersionUID = 1244938224958397171L;
	private JPanel    labelsPanel            = null;
	private JPanel    fieldsPanel            = null;
	private JPanel    OutputFormatTextPanel  = null;
	private JPanel    outputFormatLabelPanel = null;
	private JLabel    outputFormatLabel      = null;
	private JComboBox outputFormatText       = null;
	private final int WIDTH                  = 510;
	private final int HEIGHT                 = 25;
	private final int LABELS_PANEL_WIDTH     = 250;
	private final int TEXTS_PANEL_WIDTH      = 250;
	private final int LABELS_WIDTH           = 245;
	private final int LABELS_HEIGHT          = 19;
	private final int TEXTS_WIDTH            = 245;
	private final int TEXTS_HEIGHT           = 19;
	
	/**
	 * This is the default constructor
	 */
	public OutputHeaderFormatPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.add(getLabelsPanel(), new GridBagConstraints());
		this.add(getFieldsPanel(), new GridBagConstraints());
	}

	/**
	 * This method initializes jPanel3	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getOutputFormatLabelPanel() {
		if (outputFormatLabelPanel == null) {
			outputFormatLabel = new JLabel();
			outputFormatLabel.setText(PluginServices.getText(this, "output_header_format"));
			FlowLayout flowLayout4 = new FlowLayout();
			flowLayout4.setHgap(0);
			flowLayout4.setAlignment(FlowLayout.LEFT);
			flowLayout4.setVgap(1);
			outputFormatLabelPanel = new JPanel();
			outputFormatLabelPanel.setPreferredSize(new Dimension(LABELS_WIDTH, LABELS_HEIGHT));
			outputFormatLabelPanel.setLayout(flowLayout4);
			outputFormatLabelPanel.add(outputFormatLabel, null);
		}
		return outputFormatLabelPanel;
	}

	/**
	 * This method initializes jPanel	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getLabelsPanel() {
		if (labelsPanel == null) {
			labelsPanel = new JPanel();
			labelsPanel.setPreferredSize(new Dimension(LABELS_PANEL_WIDTH, HEIGHT));
			labelsPanel.add(getOutputFormatLabelPanel(), null);
		}
		return labelsPanel;
	}

	/**
	 * This method initializes jPanel	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getFieldsPanel() {
		if (fieldsPanel == null) {
			fieldsPanel = new JPanel();
			fieldsPanel.setPreferredSize(new Dimension(TEXTS_PANEL_WIDTH, HEIGHT));
			fieldsPanel.add(getOutputFormatTextPanel(), null);
		}
		return fieldsPanel;
	}

	/**
	 * This method initializes jPanel	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getOutputFormatTextPanel() {
		if (OutputFormatTextPanel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setHgap(0);
			flowLayout.setAlignment(FlowLayout.LEFT);
			flowLayout.setVgap(1);
			OutputFormatTextPanel = new JPanel();
			OutputFormatTextPanel.setPreferredSize(new Dimension(TEXTS_WIDTH, TEXTS_HEIGHT));
			OutputFormatTextPanel.setLayout(flowLayout);
			OutputFormatTextPanel.add(getOutputFormatText(), null);
		}
		return OutputFormatTextPanel;
	}

	/**
	 * This method initializes pronunciacionText	
	 * @return javax.swing.JTextField	
	 */
	private JComboBox getOutputFormatText() {
		if (outputFormatText == null) {
			outputFormatText = new JComboBox(VRTFormatOptions.getOutputHeaderFormats());
			outputFormatText.setPreferredSize(new Dimension(TEXTS_WIDTH, TEXTS_HEIGHT));
		}
		return outputFormatText;
	}

	/**
	 * gets the selected outputformat
	 * @return
	 */
	public String getOutputHeaderFormat() {
		return ((VRTFormatOptions.UIOption) getOutputFormatText().getSelectedItem()).getVrtOptionName();
	}
}