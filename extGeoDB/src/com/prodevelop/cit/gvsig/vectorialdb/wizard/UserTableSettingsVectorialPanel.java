/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
* MA  02110-1301, USA.
*
*/

/*
* AUTHORS (In addition to CIT):
* 2009 IVER T.I   {{Task}}
*/

package com.prodevelop.cit.gvsig.vectorialdb.wizard;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.store.db.DBStoreParameters;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.gui.beans.swing.JButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.addlayer.AddLayerDialog;
import com.iver.cit.gvsig.gui.panels.CRSSelectPanel;

public class UserTableSettingsVectorialPanel extends UserTableSettingsPanel {
    private static Logger logger = LoggerFactory
			.getLogger(UserTableSettingsPanel.class.getName());

    private FieldComboItem[] geos;
	private JComboBox geomComboBox = null;
    private JLabel geomLabel = null;
	private JLabel waLabel = null;
	private JLabel topLabel = null;
	private JTextField topTextField = null;
	private JTextField bottomTextField = null;
	private JTextField rightTextField = null;
	private JTextField leftTextField = null;
	private JLabel bottomLabel = null;
	private JLabel rightLabel = null;
	private JLabel leftLabel = null;
	private JButton getviewButton = null;
	private JCheckBox activateWACheckBox = null;
    private MapControl mControl = null;
	private CRSSelectPanel panelProj;
	private IProjection currentProj;

    public UserTableSettingsVectorialPanel(FieldComboItem[] idComboItems,
			FieldComboItem[] geoComboItems, String initialLayerName,
			MapControl mapc, boolean empty, WizardVectorialDB _p,
			DBStoreParameters parameters,
			IProjection curProjection) {
    	super();
		setInitValues(idComboItems, initialLayerName, empty, _p, parameters);

        mControl = mapc;
		geos = geoComboItems;
		currentProj = curProjection;
		initialize(empty);
	}

    protected void initialize(boolean _empty) {
    	super.initialize(_empty);
        setBorder(javax.swing.BorderFactory.createTitledBorder(null,
				PluginServices.getText(this, "specify_table_settings"),
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		leftLabel = new JLabel();
		// leftLabel.setBounds(new java.awt.Rectangle(375, 175, 111, 16));
		leftLabel.setBounds(new java.awt.Rectangle(375, 200, 111, 16));
		leftLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 10));
		leftLabel.setText(PluginServices.getText(this, "xmin"));
		rightLabel = new JLabel();
		rightLabel.setBounds(new java.awt.Rectangle(260, 200, 111, 16));
		rightLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 10));
		rightLabel.setText(PluginServices.getText(this, "xmax"));
		bottomLabel = new JLabel();
		bottomLabel.setBounds(new java.awt.Rectangle(130, 200, 111, 16));
		bottomLabel
				.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 10));
		bottomLabel.setText(PluginServices.getText(this, "ymin"));
		topLabel = new JLabel();
		topLabel.setBounds(new java.awt.Rectangle(15, 200, 111, 16));
		topLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 10));
		topLabel.setText(PluginServices.getText(this, "ymax"));
		waLabel = new JLabel();
		// waLabel.setBounds(new java.awt.Rectangle(40, 145, 131, 21));
		waLabel.setBounds(new java.awt.Rectangle(30, 172, 131, 21));
		waLabel.setText(PluginServices.getText(this, "working_area"));
		geomLabel = new JLabel();
		// geomLabel.setBounds(new java.awt.Rectangle(240, 55, 111, 21));
		geomLabel.setBounds(new java.awt.Rectangle(5, 145, 111, 21));
		geomLabel.setText(PluginServices.getText(this, "geo_field"));

		add(getGeomComboBox(), null);
		add(geomLabel, null);
	    add(waLabel, null);
		add(topLabel, null);
		add(getTopTextField(), null);
		add(getBottomTextField(), null);
		add(getRightTextField(), null);
		add(getLeftTextField(), null);
		add(bottomLabel, null);
		add(rightLabel, null);
		add(leftLabel, null);
		add(getGetviewButton(), null);
		add(getActivateWACheckBox(), null);
		loadValues(_empty);
	}

    private CRSSelectPanel getJPanelProj() {
		if (panelProj == null) {
			panelProj = CRSSelectPanel.getPanel(currentProj);

			panelProj.setTransPanelActive(true);
			panelProj.setLocation(new java.awt.Point(-10, 110));
			panelProj.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (panelProj.isOkPressed()) {
						AddLayerDialog
								.setLastProjection(panelProj.getCurProj());
					}
				}
			});
		}
		return panelProj;
	}


    public boolean hasValidValues() {
		if (!super.hasValidValues()) {
			return false;
		}

		if ((activateWACheckBox.isSelected()) && (getWorkingArea() == null)) {
			return false;
		}

		return true;
	}

    private JComboBox getGeomComboBox() {
		if (geomComboBox == null) {
			geomComboBox = new JComboBox();
			// geomComboBox.setBounds(new java.awt.Rectangle(355, 55, 131, 21));
			geomComboBox.setBounds(new java.awt.Rectangle(120, 145, 118, 21));
		}

		return geomComboBox;
	}

	private JTextField getTopTextField() {
		if (topTextField == null) {
			topTextField = new JTextField();
			topTextField.addKeyListener(this);
			// topTextField.setBounds(new java.awt.Rectangle(15, 190, 111, 21));
			topTextField.setBounds(new java.awt.Rectangle(15, 215, 111, 21));
		}

		return topTextField;
	}

	private JTextField getBottomTextField() {
		if (bottomTextField == null) {
			bottomTextField = new JTextField();
			bottomTextField.addKeyListener(this);
			bottomTextField
					.setBounds(new java.awt.Rectangle(130, 215, 111, 21));
		}

		return bottomTextField;
	}

	private JTextField getRightTextField() {
		if (rightTextField == null) {
			rightTextField = new JTextField();
			rightTextField.addKeyListener(this);
			rightTextField.setBounds(new java.awt.Rectangle(260, 215, 111, 21));
		}

		return rightTextField;
	}

	private JTextField getLeftTextField() {
		if (leftTextField == null) {
			leftTextField = new JTextField();
			leftTextField.addKeyListener(this);
			leftTextField.setBounds(new java.awt.Rectangle(375, 215, 111, 21));
		}

		return leftTextField;
	}

	private JButton getGetviewButton() {
		if (getviewButton == null) {
			getviewButton = new JButton();
			getviewButton.addActionListener(this);
			//getviewButton.setBounds(new java.awt.Rectangle(195, 145, 111, 26));
			getviewButton.setBounds(new java.awt.Rectangle(160, 174, 111, 26));
			getviewButton.setForeground(java.awt.Color.black);
			getviewButton.setText(PluginServices.getText(this, "get_view"));
		}

		return getviewButton;
	}

	private JCheckBox getActivateWACheckBox() {
		if (activateWACheckBox == null) {
			activateWACheckBox = new JCheckBox();
			activateWACheckBox.addActionListener(this);
//			activateWACheckBox
//					.setBounds(new java.awt.Rectangle(15, 145, 21, 21));
			activateWACheckBox
					.setBounds(new java.awt.Rectangle(5, 172, 21, 21));

		}

		return activateWACheckBox;
	}

    protected void loadValues(boolean is_empty) {
		super.loadValues(is_empty);
		if (is_empty) {
			enableAlphaControls(false);
			enableSpatialControls(false);
			getActivateWACheckBox().setSelected(false);
		} else {
			enableAlphaControls(true);
			enableSpatialControls(true);

			getGeomComboBox().removeAllItems();

			for (int i = 0; i < geos.length; i++) {
				getGeomComboBox().addItem(geos[i]);
			}

			add(getJPanelProj(), null);

		}
	}

    public void enableSpatialControls(boolean enable) {
    	super.enableSpatialControls(enable);
		getGeomComboBox().setEnabled(enable);

		getActivateWACheckBox().setEnabled(enable);

		boolean there_is_view = ((mControl != null) && (mControl.getViewPort()
				.getAdjustedExtent() != null));

		getGetviewButton().setEnabled(enable && there_is_view);
		getTopTextField().setEnabled(enable);
		getBottomTextField().setEnabled(enable);
		getRightTextField().setEnabled(enable);
		getLeftTextField().setEnabled(enable);
	}

    public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object src = e.getSource();

        if (src == getviewButton) {
			getViewIntoFourBounds();
			parent.checkFinishable();
		}

		if (src == activateWACheckBox) {
			enableWASettings(activateWACheckBox.isSelected());
			parent.checkFinishable();
		}
	}

    private void enableWASettings(boolean b) {
		getviewButton.setEnabled(b
				&& (mControl.getViewPort().getAdjustedExtent() != null));
		rightTextField.setEnabled(b);
		leftTextField.setEnabled(b);
		topTextField.setEnabled(b);
		bottomTextField.setEnabled(b);
	}

    private void getViewIntoFourBounds() {
		Envelope rect = mControl.getViewPort().getAdjustedExtent();
		topTextField.setText(getFormattedDouble(rect.getMaximum(1)));
		bottomTextField.setText(getFormattedDouble(rect.getMinimum(1)));
		rightTextField.setText(getFormattedDouble(rect.getMaximum(0)));
		leftTextField.setText(getFormattedDouble(rect.getMinimum(0)));
	}

    public Envelope getWorkingArea() {
		if (!activateWACheckBox.isSelected()) {
			return null;
		}

		double maxx;
		double maxy;
		double minx;
		double miny;

		try {
			maxx = Double.parseDouble(rightTextField.getText());
			miny = Double.parseDouble(bottomTextField.getText());
			minx = Double.parseDouble(leftTextField.getText());
			maxy = Double.parseDouble(topTextField.getText());
		} catch (NumberFormatException nfe) {
			logger.error("Not valid value: " + nfe.getMessage());

			return null;
		}
		GeometryManager geoMan = GeometryLocator.getGeometryManager();

		try {
			return geoMan.createEnvelope(minx, miny, maxx, maxy,
					SUBTYPES.GEOM2D);
		} catch (CreateEnvelopeException e) {
			// FIXME Exception
			throw new RuntimeException(e);
		}
	}

    public boolean combosHaveItems() {
		if (!super.combosHaveItems()) {
			return false;
		}

		if (getGeomComboBox().getItemCount() == 0) {
			return false;
		}

		return true;
	}

    public void repaint() {
		super.repaint();
		getGeomComboBox().updateUI();
	}

    public String getGeoFieldName() {
		if (getGeomComboBox().getSelectedItem() == null) {
			return null;
		}
		return getGeomComboBox().getSelectedItem().toString();
	}
    public IProjection getProjection() {
		return panelProj.getCurProj();
	}

}
