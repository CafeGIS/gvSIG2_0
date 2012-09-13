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
package org.gvsig.rastertools.raw.ui.main;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.iver.andami.PluginServices;
import com.iver.utiles.StringUtilities;
/**
 * This panel contains the left panel of the "Set Geometry" panel. It is the
 * imput to write the image width, the heigth, the number of bands and the image
 * header size.
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class GeometryPropertiesLeftPanel extends JPanel{
  private static final long serialVersionUID = 1254390565246946002L;
	private JPanel              bandsLabelPanel      = null;
	private JPanel              headerSizeLabelPanel = null;
	private JPanel              heigthLabelPanel     = null;
	private JLabel              heigthLabel          = null;
	private JPanel              labelsPanel          = null;
	private JPanel              fieldsPanel          = null;
	private JPanel              widthTextPanel       = null;
	private JPanel              heigthTextPanel      = null;
	private JPanel              widthLabelPanel      = null;
	private JLabel              widthLabel           = null;
	private JLabel              bandsLabel           = null;
	private JPanel              bandsTextPanel       = null;
	private JPanel              headerSizeTextPanel  = null;
	private JFormattedTextField widthText            = null;
	private JLabel              headerSizeLabel      = null;
	private JFormattedTextField heigthText           = null;
	private JFormattedTextField bandsText            = null;
	private JFormattedTextField headerSizeText       = null;

	private final int           WIDTH                = 245;
	private final int           HEIGHT               = 110;
	private final int           LABELS_PANEL_WIDTH   = 120;
	private final int           TEXTS_PANEL_WIDTH    = 125;
	private final int           LABELS_WIDTH         = 120;
	private final int           LABELS_HEIGHT        = 19;
	private final int           TEXTS_WIDTH          = 120;
	private final int           TEXTS_HEIGHT         = 19;
	
	/**
	 * This is the default constructor
	 */
	public GeometryPropertiesLeftPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
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
	 * @return javax.swing.JPanel
	 */
	private JPanel getBandsLabelPanel() {
		if (bandsLabelPanel == null) {
			bandsLabel = new JLabel();
			bandsLabel.setText(PluginServices.getText(this, "bands_number"));
			FlowLayout flowLayout2 = new FlowLayout();
			flowLayout2.setAlignment(java.awt.FlowLayout.LEFT);
			bandsLabelPanel = new JPanel();
			bandsLabelPanel.setLayout(flowLayout2);
			bandsLabelPanel.setPreferredSize(new java.awt.Dimension(LABELS_WIDTH, LABELS_HEIGHT));
			bandsLabelPanel.add(bandsLabel, null);
			flowLayout2.setVgap(1);
			flowLayout2.setHgap(0);
		}
		return bandsLabelPanel;
	}

	/**
	 * This method initializes pY
	 * @return javax.swing.JPanel
	 */
	private JPanel getHeaderSizeLabelPanel() {
		if (headerSizeLabelPanel == null) {
			headerSizeLabel = new JLabel();
			headerSizeLabel.setText(PluginServices.getText(this, "header_Size"));
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setAlignment(java.awt.FlowLayout.LEFT);
			headerSizeLabelPanel = new JPanel();
			headerSizeLabelPanel.setLayout(flowLayout1);
			headerSizeLabelPanel.setPreferredSize(new java.awt.Dimension(LABELS_WIDTH, LABELS_HEIGHT));
			headerSizeLabelPanel.add(headerSizeLabel, null);
			flowLayout1.setHgap(0);
			flowLayout1.setVgap(1);
		}
		return headerSizeLabelPanel;
	}

	/**
	 * This method initializes pLatitud
	 * @return javax.swing.JPanel
	 */
	private JPanel getHeigthLabelPanel() {
		if (heigthLabelPanel == null) {
			heigthLabel = new JLabel();
			heigthLabel.setText(PluginServices.getText(this, "alto"));
			FlowLayout flowLayout3 = new FlowLayout();
			flowLayout3.setAlignment(java.awt.FlowLayout.LEFT);
			heigthLabelPanel = new JPanel();
			heigthLabelPanel.setLayout(flowLayout3);
			heigthLabelPanel.setPreferredSize(new java.awt.Dimension(LABELS_WIDTH, LABELS_HEIGHT));
			flowLayout3.setHgap(0);
			flowLayout3.setVgap(1);
			heigthLabelPanel.add(heigthLabel, null);
		}
		return heigthLabelPanel;
	}

	/**
	 * This method initializes jPanel3
	 * @return javax.swing.JPanel
	 */
	private JPanel getWidthLabelPanel() {
		if (widthLabelPanel == null) {
			widthLabel = new JLabel();
			widthLabel.setText(PluginServices.getText(this, "ancho"));
			FlowLayout flowLayout4 = new FlowLayout();
			flowLayout4.setHgap(0);
			flowLayout4.setAlignment(java.awt.FlowLayout.LEFT);
			flowLayout4.setVgap(1);
			widthLabelPanel = new JPanel();
			widthLabelPanel.setPreferredSize(new java.awt.Dimension(LABELS_WIDTH, LABELS_HEIGHT));
			widthLabelPanel.setLayout(flowLayout4);
			widthLabelPanel.add(widthLabel, null);
		}
		return widthLabelPanel;
	}

	/**
	 * This method initializes jPanel
	 * @return javax.swing.JPanel
	 */
	private JPanel getLabelsPanel() {
		if (labelsPanel == null) {
			labelsPanel = new JPanel();
			labelsPanel.setPreferredSize(new java.awt.Dimension(LABELS_PANEL_WIDTH, HEIGHT));
			labelsPanel.add(getWidthLabelPanel(), null);
			labelsPanel.add(getHeigthLabelPanel(), null);
			labelsPanel.add(getBandsLabelPanel(), null);
			labelsPanel.add(getHeaderSizeLabelPanel(), null);
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
			fieldsPanel.setPreferredSize(new java.awt.Dimension(TEXTS_PANEL_WIDTH, HEIGHT));
			fieldsPanel.add(getWidthTextPanel(), null);
			fieldsPanel.add(getHeigthTextPanel(), null);
			fieldsPanel.add(getBandsTextPanel(), null);
			fieldsPanel.add(getHeaderSizeTextPanel(), null);
		}
		return fieldsPanel;
	}

	/**
	 * This method initializes jPanel
	 * @return javax.swing.JPanel
	 */
	private JPanel getWidthTextPanel() {
		if (widthTextPanel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setHgap(0);
			flowLayout.setAlignment(java.awt.FlowLayout.LEFT);
			flowLayout.setVgap(1);
			widthTextPanel = new JPanel();
			widthTextPanel.setPreferredSize(new java.awt.Dimension(TEXTS_WIDTH, TEXTS_HEIGHT));
			widthTextPanel.setLayout(flowLayout);
			widthTextPanel.add(getWidthText(), null);
		}
		return widthTextPanel;
	}

	/**
	 * This method initializes jPanel1
	 * @return javax.swing.JPanel
	 */
	private JPanel getHeigthTextPanel() {
		if (heigthTextPanel == null) {
			FlowLayout flowLayout5 = new FlowLayout();
			flowLayout5.setHgap(0);
			flowLayout5.setAlignment(java.awt.FlowLayout.LEFT);
			flowLayout5.setVgap(1);
			heigthTextPanel = new JPanel();
			heigthTextPanel.setPreferredSize(new java.awt.Dimension(TEXTS_WIDTH, TEXTS_HEIGHT));
			heigthTextPanel.setLayout(flowLayout5);
			heigthTextPanel.add(getHeigthText(), null);
		}
		return heigthTextPanel;
	}

	/**
	 * This method initializes jPanel2
	 * @return javax.swing.JPanel
	 */
	private JPanel getHeaderSizeTextPanel() {
		if (headerSizeTextPanel == null) {
			FlowLayout flowLayout8 = new FlowLayout();
			flowLayout8.setHgap(0);
			flowLayout8.setAlignment(FlowLayout.LEFT);
			flowLayout8.setVgap(1);
			headerSizeTextPanel = new JPanel();
			headerSizeTextPanel.setPreferredSize(new java.awt.Dimension(TEXTS_WIDTH, TEXTS_HEIGHT));
			headerSizeTextPanel.setLayout(flowLayout8);
			headerSizeTextPanel.add(getHeaderSizeText(), null);
		}
		return headerSizeTextPanel;
	}

	/**
	 * This method initializes oficialTextPanel
	 * @return javax.swing.JPanel
	 */
	private JPanel getBandsTextPanel() {
		if (bandsTextPanel == null) {
			FlowLayout flowLayout9 = new FlowLayout();
			flowLayout9.setHgap(0);
			flowLayout9.setAlignment(FlowLayout.LEFT);
			flowLayout9.setVgap(1);
			bandsTextPanel = new JPanel();
			bandsTextPanel.setPreferredSize(new java.awt.Dimension(TEXTS_WIDTH, TEXTS_HEIGHT));
			bandsTextPanel.setLayout(flowLayout9);
			bandsTextPanel.add(getBandsText(), null);
		}
		return bandsTextPanel;
	}

	/**
	 * This method initializes pronunciacionText
	 * @return javax.swing.JTextField
	 */
	private JFormattedTextField getWidthText() {
		if (widthText == null) {
			widthText = new JFormattedTextField(NumberFormat.getIntegerInstance());
			widthText.setPreferredSize(new Dimension(TEXTS_WIDTH, TEXTS_HEIGHT));
			widthText.setText("0");
		}
		return widthText;
	}

	/**
	 * This method initializes etimologyText
	 * @return javax.swing.JTextField
	 */
	private JFormattedTextField getHeigthText() {
		if (heigthText == null) {
			heigthText = new JFormattedTextField(NumberFormat.getIntegerInstance());
			heigthText.setPreferredSize(new Dimension(TEXTS_WIDTH, TEXTS_HEIGHT));
			heigthText.setText("0");
		}
		return heigthText;
	}

	/**
	 * This method initializes bandsText
	 * @return javax.swing.JTextField
	 */
	private JFormattedTextField getBandsText() {
		if (bandsText == null) {
			bandsText = new JFormattedTextField(NumberFormat.getIntegerInstance());
			bandsText.setPreferredSize(new Dimension(TEXTS_WIDTH, TEXTS_HEIGHT));
			bandsText.setText("0");
		}
		return bandsText;
	}

	/**
	 * This method initializes headerSizeText
	 * @return javax.swing.JTextField
	 */
	private JFormattedTextField getHeaderSizeText() {
		if (headerSizeText == null) {
			headerSizeText = new JFormattedTextField(NumberFormat.getIntegerInstance());
			headerSizeText.setPreferredSize(new Dimension(TEXTS_WIDTH, TEXTS_HEIGHT));
			headerSizeText.setText("0");
		}
		return headerSizeText;
	}

	/**
	 * gets the image width
	 * @return
	 */
	public int getImageWidth() {
		try {
			String buffer = StringUtilities.replace(getWidthText().getText(), ".", "");
			buffer = StringUtilities.replace(buffer, ",", "");
			return Integer.parseInt(buffer);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * gets the image height
	 * @return
	 */
	public int getImageHeight() {
		try {
			String buffer = StringUtilities.replace(getHeigthText().getText(), ".", "");
			buffer = StringUtilities.replace(buffer, ",", "");
			return Integer.parseInt(buffer);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * gets the number of bands
	 * @return
	 */
	public int getNumberOfBands() {
		try {
			return Integer.valueOf(getBandsText().getText()).intValue();
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * gets the header size
	 * @return
	 */
	public int getHeaderSize() {
		try {
			return Integer.valueOf(getHeaderSizeText().getText()).intValue();
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}