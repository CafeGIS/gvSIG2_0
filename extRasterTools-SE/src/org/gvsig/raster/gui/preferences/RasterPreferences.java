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
package org.gvsig.raster.gui.preferences;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.gvsig.raster.gui.preferences.panels.PreferenceCache;
import org.gvsig.raster.gui.preferences.panels.PreferenceGeneral;
import org.gvsig.raster.gui.preferences.panels.PreferenceLoadLayer;
import org.gvsig.raster.gui.preferences.panels.PreferenceNoData;
import org.gvsig.raster.gui.preferences.panels.PreferenceOverviews;
import org.gvsig.raster.gui.preferences.panels.PreferenceTemporal;

import com.iver.andami.PluginServices;
import com.iver.andami.preferences.AbstractPreferencePage;
import com.iver.andami.preferences.StoreException;
/**
 * RasterPreferences es un panel de preferencias situado a nivel de las
 * preferencias de gvSIG que engloba todas las opciones configurables de Raster
 *
 * @version 11/12/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class RasterPreferences extends AbstractPreferencePage {
	private static final long serialVersionUID = -1689657253810393874L;

	protected static String     id        = RasterPreferences.class.getName();
	private ImageIcon           icon;

	private PreferenceNoData    noData    = null;
	private PreferenceOverviews overviews = null;
	private PreferenceCache     cache     = null;
	private PreferenceTemporal  temporal  = null;
	private PreferenceGeneral   general   = null;
	private PreferenceLoadLayer loadLayer = null;

	/**
	 * Constructor de la clase RasterPreferences
	 */
	public RasterPreferences() {
		super();
		icon = PluginServices.getIconTheme().get("pref-raster-icon");
		initialize();
	}

	/**
	 * Inicializacion del panel de preferencias.
	 */
	private void initialize() {
		setTitle("Frame");

		GridBagConstraints gridBagConstraints;

		JScrollPane scrollPane = new JScrollPane();

		scrollPane.getVerticalScrollBar().setUnitIncrement(20);

		JPanel panel = new JPanel();

		panel.setLayout(new GridBagLayout());

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		panel.add(getPreferenceGeneral(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		panel.add(getPreferenceLoadLayer(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		panel.add(getPreferenceNoData(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		panel.add(getPreferenceTemporal(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		panel.add(getPreferenceOverviews(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		panel.add(getPreferenceCache(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		panel.add(new JPanel(), gridBagConstraints);

		panel.setBorder(new EmptyBorder(5, 5, 5, 5));

		scrollPane.setViewportView(panel);

		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * Devuelve el panel de configuracion para los valores NoData
	 * @return
	 */
	private PreferenceNoData getPreferenceNoData() {
		if (noData == null) {
			noData = new PreferenceNoData();
		}
		return noData;
	}

	/**
	 * Devuelve el panel de configuracion general de Raster. Aqui es donde se
	 * define si se visualiza automaticamente Raster, el numero de clases, si se
	 * pregunta la reproyeccion, etc...
	 * @return
	 */
	private PreferenceGeneral getPreferenceGeneral() {
		if (general == null) {
			general = new PreferenceGeneral();
		}
		return general;
	}

	/**
	 * Devuelve un panel de configuracion para las overviews de Raster.
	 * @return
	 */
	private PreferenceOverviews getPreferenceOverviews() {
		if (overviews == null) {
			overviews = new PreferenceOverviews();
		}
		return overviews;
	}

	/**
	 * Devuelve el panel de preferencias para las opciones de cache de Raster.
	 * Aquí se podrán definir todos los parametros configurables respecto a la
	 * cache.
	 * @return
	 */
	private PreferenceCache getPreferenceCache() {
		if (cache == null) {
			cache = new PreferenceCache();
		}
		return cache;
	}

	
	private PreferenceTemporal getPreferenceTemporal() {
		if (temporal == null) {
			temporal = new PreferenceTemporal();
		}
		return temporal;
	}

	private PreferenceLoadLayer getPreferenceLoadLayer() {
		if (loadLayer == null) {
			loadLayer = new PreferenceLoadLayer();
		}
		return loadLayer;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.preferences.IPreference#initializeValues()
	 */
	public void initializeValues() {
		getPreferenceNoData().initializeValues();
		getPreferenceOverviews().initializeValues();
		getPreferenceCache().initializeValues();
		getPreferenceTemporal().initializeValues();
		getPreferenceGeneral().initializeValues();
		getPreferenceLoadLayer().initializeValues();
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.preferences.AbstractPreferencePage#storeValues()
	 */
	public void storeValues() throws StoreException {
		getPreferenceNoData().storeValues();
		getPreferenceOverviews().storeValues();
		getPreferenceCache().storeValues();
		getPreferenceTemporal().storeValues();
		getPreferenceGeneral().storeValues();
		getPreferenceLoadLayer().storeValues();
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.preferences.IPreference#initializeDefaults()
	 */
	public void initializeDefaults() {
		getPreferenceNoData().initializeDefaults();
		getPreferenceOverviews().initializeDefaults();
		getPreferenceCache().initializeDefaults();
		getPreferenceTemporal().initializeDefaults();
		getPreferenceGeneral().initializeDefaults();
		getPreferenceLoadLayer().initializeDefaults();
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.preferences.AbstractPreferencePage#isResizeable()
	 */
	public boolean isResizeable() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.preferences.IPreference#getID()
	 */
	public String getID() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.preferences.IPreference#getIcon()
	 */
	public ImageIcon getIcon() {
		return icon;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.preferences.IPreference#getPanel()
	 */
	public JPanel getPanel() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.preferences.IPreference#getTitle()
	 */
	public String getTitle() {
		return "Raster";
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.preferences.IPreference#isValueChanged()
	 */
	public boolean isValueChanged() {
		return true;
	}

	public void setChangesApplied() {}
}