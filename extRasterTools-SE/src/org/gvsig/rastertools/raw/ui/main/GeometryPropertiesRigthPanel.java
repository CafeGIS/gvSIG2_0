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
import javax.swing.JTextField;

import org.gvsig.rastertools.raw.tools.VRTFormatOptions;

import com.iver.andami.PluginServices;
import com.iver.utiles.swing.JComboBox;
/**
 * This panel contains the rigth panel of the "Set Geometry"
 * panel. It is the imput to write the file size in bytes,
 * the data type, the byte order and the type of
 * interleaving.
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class GeometryPropertiesRigthPanel extends JPanel{
  private static final long serialVersionUID = 1L;

	private JPanel     byteOrderLabelPanel    = null;
	private JPanel     interleavingLabelPanel = null;
	private JPanel     dataTypeLabelPanel     = null;
	private JLabel     dataTypeLabel          = null;
	private JPanel     labelsPanel            = null;
	private JPanel     fieldsPanel            = null;
	private JPanel     fileSizeTextPanel      = null;
	private JPanel     dataTypeTextPanel      = null;
	private JPanel     fileSizeLabelPanel     = null;
	private JLabel     fileSizeLabel          = null;
	private JLabel     byteOrderLabel         = null;
	private JPanel     byteOrderTextPanel     = null;
	private JPanel     interleavingTextPanel  = null;
	private JTextField fileSizeText           = null;
	private JLabel     interleavingLabel      = null;
	private JComboBox  dataTypeText           = null;
	private JComboBox  byteOrderText          = null;
	private JComboBox  interleavingText       = null;

	private final int  WIDTH                  = 245;
	private final int  HEIGHT                 = 110;
	private final int  LABELS_PANEL_WIDTH     = 120;
	private final int  TEXTS_PANEL_WIDTH      = 125;
	private final int  LABELS_WIDTH           = 120;
	private final int  LABELS_HEIGHT          = 19;
	private final int  TEXTS_WIDTH            = 120;
	private final int  TEXTS_HEIGHT           = 19;
	
	/**
	 * This is the default constructor
	 */
	public GeometryPropertiesRigthPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {

		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new java.awt.Dimension(WIDTH, HEIGHT));
		this.add(getLabelsPanel(), new GridBagConstraints());
		this.add(getFieldsPanel(), new GridBagConstraints());
	}

	/**
	 * This method initializes pX	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getByteOrderLabelPanel() {
		if (byteOrderLabelPanel == null) {
			byteOrderLabel = new JLabel();
			byteOrderLabel.setText(PluginServices.getText(this, "byte_order"));
			FlowLayout flowLayout2 = new FlowLayout();
			flowLayout2.setAlignment(java.awt.FlowLayout.LEFT);
			byteOrderLabelPanel = new JPanel();
			byteOrderLabelPanel.setLayout(flowLayout2);
			byteOrderLabelPanel.setPreferredSize(new java.awt.Dimension(LABELS_WIDTH, LABELS_HEIGHT));
			byteOrderLabelPanel.add(byteOrderLabel, null);
			flowLayout2.setVgap(1);
			flowLayout2.setHgap(0);
		}
		return byteOrderLabelPanel;
	}

	/**
	 * This method initializes pY	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getInterleavingLabelPanel() {
		if (interleavingLabelPanel == null) {
			interleavingLabel = new JLabel();
			interleavingLabel.setText(PluginServices.getText(this, "type_of_interleaving"));
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setAlignment(java.awt.FlowLayout.LEFT);
			interleavingLabelPanel = new JPanel();
			interleavingLabelPanel.setLayout(flowLayout1);
			interleavingLabelPanel.setPreferredSize(new java.awt.Dimension(LABELS_WIDTH, LABELS_HEIGHT));
			interleavingLabelPanel.add(interleavingLabel, null);
			flowLayout1.setHgap(0);
			flowLayout1.setVgap(1);
		}
		return interleavingLabelPanel;
	}

	/**
	 * This method initializes pLatitud	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDataTypeLabelPanel() {
		if (dataTypeLabelPanel == null) {
			dataTypeLabel = new JLabel();
			dataTypeLabel.setText(PluginServices.getText(this, "data_type"));
			FlowLayout flowLayout3 = new FlowLayout();
			flowLayout3.setAlignment(java.awt.FlowLayout.LEFT);
			dataTypeLabelPanel = new JPanel();
			dataTypeLabelPanel.setLayout(flowLayout3);
			dataTypeLabelPanel.setPreferredSize(new java.awt.Dimension(LABELS_WIDTH, LABELS_HEIGHT));
			flowLayout3.setHgap(0);
			flowLayout3.setVgap(1);
			dataTypeLabelPanel.add(dataTypeLabel, null);
		}
		return dataTypeLabelPanel;
	}

	/**
	 * This method initializes jPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getFileSizeLabelPanel() {
		if (fileSizeLabelPanel == null) {
			fileSizeLabel = new JLabel();
			fileSizeLabel.setText(PluginServices.getText(this, "file_size_in_bytes"));
			FlowLayout flowLayout4 = new FlowLayout();
			flowLayout4.setHgap(0);
			flowLayout4.setAlignment(java.awt.FlowLayout.LEFT);
			flowLayout4.setVgap(1);
			fileSizeLabelPanel = new JPanel();
			fileSizeLabelPanel.setPreferredSize(new java.awt.Dimension(LABELS_WIDTH, LABELS_HEIGHT));
			fileSizeLabelPanel.setLayout(flowLayout4);
			fileSizeLabelPanel.add(fileSizeLabel, null);
		}
		return fileSizeLabelPanel;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getLabelsPanel() {
		if (labelsPanel == null) {
			labelsPanel = new JPanel();
			labelsPanel.setPreferredSize(new java.awt.Dimension(LABELS_PANEL_WIDTH, HEIGHT));
			labelsPanel.add(getFileSizeLabelPanel(), null);
			labelsPanel.add(getDataTypeLabelPanel(), null);
			labelsPanel.add(getByteOrderLabelPanel(), null);
			labelsPanel.add(getInterleavingLabelPanel(), null);
		}
		return labelsPanel;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getFieldsPanel() {
		if (fieldsPanel == null) {
			fieldsPanel = new JPanel();
			fieldsPanel.setPreferredSize(new java.awt.Dimension(TEXTS_PANEL_WIDTH, HEIGHT));
			fieldsPanel.add(getFileSizeTextPanel(), null);
			fieldsPanel.add(getDataTypeTextPanel(), null);
			fieldsPanel.add(getByteOrderTextPanel(), null);
			fieldsPanel.add(getInterleavingTextPanel(), null);
		}
		return fieldsPanel;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getFileSizeTextPanel() {
		if (fileSizeTextPanel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setHgap(0);
			flowLayout.setAlignment(java.awt.FlowLayout.LEFT);
			flowLayout.setVgap(1);
			fileSizeTextPanel = new JPanel();
			fileSizeTextPanel.setPreferredSize(new java.awt.Dimension(TEXTS_WIDTH, TEXTS_HEIGHT));
			fileSizeTextPanel.setLayout(flowLayout);
			fileSizeTextPanel.add(getFileSizeText(), null);
		}
		return fileSizeTextPanel;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDataTypeTextPanel() {
		if (dataTypeTextPanel == null) {
			FlowLayout flowLayout5 = new FlowLayout();
			flowLayout5.setHgap(0);
			flowLayout5.setAlignment(java.awt.FlowLayout.LEFT);
			flowLayout5.setVgap(1);
			dataTypeTextPanel = new JPanel();
			dataTypeTextPanel.setPreferredSize(new java.awt.Dimension(TEXTS_WIDTH, TEXTS_HEIGHT));
			dataTypeTextPanel.setLayout(flowLayout5);
			dataTypeTextPanel.add(getDataTypeText(), null);
		}
		return dataTypeTextPanel;
	}

	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getInterleavingTextPanel() {
		if (interleavingTextPanel == null) {
			FlowLayout flowLayout8 = new FlowLayout();
			flowLayout8.setHgap(0);
			flowLayout8.setAlignment(FlowLayout.LEFT);
			flowLayout8.setVgap(1);
			interleavingTextPanel = new JPanel();
			interleavingTextPanel.setPreferredSize(new java.awt.Dimension(TEXTS_WIDTH, TEXTS_HEIGHT));
			interleavingTextPanel.setLayout(flowLayout8);
			interleavingTextPanel.add(getInterleavingText(), null);
		}
		return interleavingTextPanel;
	}

	/**
	 * This method initializes oficialTextPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getByteOrderTextPanel() {
		if (byteOrderTextPanel == null) {
			FlowLayout flowLayout9 = new FlowLayout();
			flowLayout9.setHgap(0);
			flowLayout9.setAlignment(FlowLayout.LEFT);
			flowLayout9.setVgap(1);
			byteOrderTextPanel = new JPanel();
			byteOrderTextPanel.setPreferredSize(new java.awt.Dimension(TEXTS_WIDTH, TEXTS_HEIGHT));
			byteOrderTextPanel.setLayout(flowLayout9);
			byteOrderTextPanel.add(getByteOrderText(), null);
		}
		return byteOrderTextPanel;
	}

	/**
	 * This method initializes pronunciacionText	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getFileSizeText() {
		if (fileSizeText == null) {
			fileSizeText = new JTextField();
			fileSizeText.setPreferredSize(new Dimension(TEXTS_WIDTH, TEXTS_HEIGHT));
			fileSizeText.setText("0");
			fileSizeText.setEnabled(false);
		}
		return fileSizeText;
	}

	/**
	 * This method initializes etimologyText	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JComboBox getDataTypeText() {
		if (dataTypeText == null) {
			dataTypeText = new JComboBox(VRTFormatOptions.getDataTypes());
			dataTypeText.setPreferredSize(new Dimension(TEXTS_WIDTH, TEXTS_HEIGHT));
		}
		return dataTypeText;
	}

	/**
	 * This method initializes bandsText	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JComboBox getByteOrderText() {
		if (byteOrderText == null) {
			byteOrderText = new JComboBox(VRTFormatOptions.getByteOrder());
			byteOrderText.setPreferredSize(new Dimension(TEXTS_WIDTH, TEXTS_HEIGHT));
		}
		return byteOrderText;
	}

	/**
	 * This method initializes headerSizeText	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JComboBox getInterleavingText() {
		if (interleavingText == null) {
			interleavingText = new JComboBox(VRTFormatOptions.getInterleaving());
			interleavingText.setPreferredSize(new Dimension(TEXTS_WIDTH, TEXTS_HEIGHT));
		}
		return interleavingText;
	}

	/**
	 * Sets the file size into the text field
	 * @param fileSize
	 */
	public void setFileSize(long fileSize) {
		getFileSizeText().setText(String.valueOf(fileSize));
	}

	/**
	 * Gets the data type
	 * @return
	 */
	public VRTFormatOptions.UIOption getDataType() {
		return (VRTFormatOptions.UIOption) getDataTypeText().getSelectedItem();
	}

	/**
	 * Gets the byte order
	 * @return
	 */
	public String getByteOrder() {
		return ((VRTFormatOptions.UIOption) getByteOrderText().getSelectedItem()).getVrtOptionName();
	}

	/**
	 * Gets the type of interleaving
	 * @return
	 */
	public String getInterleaving() {
		return ((VRTFormatOptions.UIOption) getInterleavingText().getSelectedItem()).getVrtOptionName();
	}
}