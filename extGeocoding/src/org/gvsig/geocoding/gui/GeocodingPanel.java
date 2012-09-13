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
 * 2008 Prodevelop S.L. vsanjaime Programador
 */
package org.gvsig.geocoding.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.geocoding.export.MasiveExportThread;
import org.gvsig.geocoding.extension.GeocodingController;
import org.gvsig.geocoding.gui.address.AbstractAddressPanel;
import org.gvsig.geocoding.gui.settings.SettingsPanel;
import org.gvsig.geocoding.pattern.Patterngeocoding;
import org.gvsig.geocoding.pattern.impl.DefaultPatterngeocoding;
import org.gvsig.geocoding.result.GeocodingResult;
import org.gvsig.geocoding.utils.GeocodingUtils;
import org.gvsig.project.document.table.FeatureTableDocument;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistenceManager;
import org.gvsig.tools.persistence.PersistentState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.SingletonWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * Main panel of geocoding extension
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class GeocodingPanel extends JPanel implements SingletonWindow,
		MouseListener {

	private static final long serialVersionUID = 1L;
	private Logger log = LoggerFactory.getLogger(GeocodingPanel.class);
	private GeocodingController control = null;

	private AbstractAddressPanel aPanel = null;

	private ButtonGroup groupRadio;
	private JButton jButExit;
	private JButton jButLoad;
	private JButton jButNew;
	private JButton jButEdit;
	private JButton jButNext;
	private JButton jButPrevious;
	private JButton jButSearch;
	private JButton jButExport;
	private JComboBox jComboTable;
	private JComboBox jComboType;
	private JLabel jLabPatternPath;
	private JLabel jLabScore;
	private JLabel jLabType;
	private JLabel jLabRow;
	private JPanel jPanButtons;
	private JPanel jPanGeocodingType;
	private JPanel jPanPattern;
	private JPanel jPanResults;
	private JPanel jPanResultsButtons;
	private JPanel jPanSettings;
	private JPanel jPanSimpleTable;
	private JPanel jPanTable;
	private JPanel jPanType;
	private JRadioButton jRadioSimple;
	private JRadioButton jRadioTable;
	private JScrollPane jScrollResults;
	private JTable jTableResults;
	private JTextField jTextPatternPath;
	private SettingsPanel settingsPanel;

	/**
	 * Constructor
	 * 
	 * @param _control
	 */
	public GeocodingPanel(GeocodingController _control) {
		this.control = _control;
		this.initComponents();
		this.setMesages();
		this.setImages();

		// pattern null
		this.control.getGmodel().setPattern(null);
		settingsPanel.setMaxResults(10);
		settingsPanel.activateComponents(false);
		this.jPanGeocodingType.validate();
	}

	/**
	 * set icons
	 */
	private void setImages() {
		PluginServices ps = PluginServices.getPluginServices(this);
		if (ps != null) {
			String baseDir = ps.getClassLoader().getBaseDir();

			jButLoad
					.setIcon(new ImageIcon(baseDir + File.separator + "images"
							+ File.separator + "icons16" + File.separator
							+ "open.png"));
			jButNew.setIcon(new ImageIcon(baseDir + File.separator + "images"
					+ File.separator + "icons16" + File.separator + "new.png"));

			jButEdit
					.setIcon(new ImageIcon(baseDir + File.separator + "images"
							+ File.separator + "icons16" + File.separator
							+ "edit.png"));

			jButNext
					.setIcon(new ImageIcon(baseDir + File.separator + "images"
							+ File.separator + "icons16" + File.separator
							+ "next.png"));

			jButPrevious.setIcon(new ImageIcon(baseDir + File.separator
					+ "images" + File.separator + "icons16" + File.separator
					+ "previous.png"));

			jButSearch.setIcon(new ImageIcon(baseDir + File.separator
					+ "images" + File.separator + "icons16" + File.separator
					+ "search.png"));

			jButExit.setIcon(new ImageIcon(baseDir + File.separator + "images"
					+ File.separator + "icons16" + File.separator + "out.png"));

			jButExport.setIcon(new ImageIcon(baseDir + File.separator
					+ "images" + File.separator + "icons16" + File.separator
					+ "up.png"));
		}
	}

	/**
	 * set labels
	 */
	private void setMesages() {
		PluginServices ps = PluginServices.getPluginServices(this);

		if (ps != null) {
			jPanPattern.setBorder(GeocodingUtils.getTitledBorder(ps
					.getText("pattern")));

			this.jButLoad.setToolTipText(ps.getText("butloadpattern"));

			this.jButNew.setToolTipText(ps.getText("butnewpattern"));

			this.jButEdit.setToolTipText(ps.getText("buteditpattern"));

			this.jLabPatternPath.setText(ps.getText("patternpath"));

			jPanGeocodingType.setBorder(GeocodingUtils.getTitledBorder(ps
					.getText("gtype")));

			this.jRadioSimple.setText(ps.getText("gsimple"));
			this.jRadioSimple.setToolTipText(ps.getText("gsimpletip"));

			this.jRadioTable.setText(ps.getText("gtable"));
			this.jRadioTable.setToolTipText(ps.getText("gtabletip"));

			jPanType.setBorder(GeocodingUtils.getTitledBorder(ps
					.getText("atype")));

			jPanSettings.setBorder(GeocodingUtils.getTitledBorder(ps
					.getText("settings")));

			jPanResults.setBorder(GeocodingUtils.getTitledBorder(ps
					.getText("results")));

			this.jButPrevious.setToolTipText(ps.getText("previoustip"));
			this.jButNext.setToolTipText(ps.getText("nexttip"));

			this.jButExit.setText(ps.getText("exit"));
			this.jButSearch.setText(ps.getText("search"));
			this.jButExport.setText(ps.getText("export"));
		}

	}

	/**
	 * Initialize Panel Components
	 */
	private void initComponents() {

		this.setFocusable(true);
		this.addMouseListener(this);

		java.awt.GridBagConstraints gridBagConstraints;

		groupRadio = new javax.swing.ButtonGroup();
		jPanPattern = new javax.swing.JPanel();
		jLabPatternPath = new javax.swing.JLabel();
		jTextPatternPath = new javax.swing.JTextField();
		jButLoad = new javax.swing.JButton();
		jButNew = new javax.swing.JButton();
		jButEdit = new javax.swing.JButton();
		jPanGeocodingType = new javax.swing.JPanel();
		jPanSimpleTable = new javax.swing.JPanel();
		jRadioSimple = new javax.swing.JRadioButton();
		jPanTable = new javax.swing.JPanel();
		jComboTable = new javax.swing.JComboBox();
		jRadioTable = new javax.swing.JRadioButton();
		jPanType = new javax.swing.JPanel();
		jComboType = new javax.swing.JComboBox();
		jLabType = new javax.swing.JLabel();
		jLabRow = new javax.swing.JLabel();
		jPanSettings = new javax.swing.JPanel();
		settingsPanel = new SettingsPanel(control);
		jLabScore = new javax.swing.JLabel();
		jPanResults = new javax.swing.JPanel();
		jScrollResults = new javax.swing.JScrollPane();
		jTableResults = new javax.swing.JTable();
		jPanResultsButtons = new javax.swing.JPanel();
		jButNext = new javax.swing.JButton();
		jButPrevious = new javax.swing.JButton();
		jPanButtons = new javax.swing.JPanel();
		jButExit = new javax.swing.JButton();
		jButSearch = new javax.swing.JButton();
		jButExport = new javax.swing.JButton();

		setLayout(new java.awt.GridBagLayout());

		jPanPattern.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Pattern"));
		jPanPattern.setLayout(new java.awt.GridBagLayout());

		jLabPatternPath.setText("Name");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 3);
		jPanPattern.add(jLabPatternPath, gridBagConstraints);

		jTextPatternPath.setEditable(false);
		jTextPatternPath.setToolTipText("Pattern file");

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		jPanPattern.add(jTextPatternPath, gridBagConstraints);

		jButLoad.setText("");
		jButLoad.setToolTipText("Load pattern");
		jButLoad.setMaximumSize(new java.awt.Dimension(35, 25));
		jButLoad.setMinimumSize(new java.awt.Dimension(30, 25));
		jButLoad.setPreferredSize(new java.awt.Dimension(30, 25));
		jButLoad.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evLoadPattern(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
		jPanPattern.add(jButLoad, gridBagConstraints);

		jButNew.setText("");
		jButNew.setToolTipText("New");
		jButNew.setMaximumSize(new java.awt.Dimension(35, 25));
		jButNew.setMinimumSize(new java.awt.Dimension(30, 25));
		jButNew.setPreferredSize(new java.awt.Dimension(30, 25));
		jButNew.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evNewPattern(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
		jPanPattern.add(jButNew, gridBagConstraints);

		jButEdit.setText("");
		jButEdit.setToolTipText("Edit");
		jButEdit.setMaximumSize(new java.awt.Dimension(35, 25));
		jButEdit.setMinimumSize(new java.awt.Dimension(30, 25));
		jButEdit.setPreferredSize(new java.awt.Dimension(30, 25));
		jButEdit.setEnabled(false);
		jButEdit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evEditPattern(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
		jPanPattern.add(jButEdit, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		add(jPanPattern, gridBagConstraints);

		jPanGeocodingType.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Geocoding type"));
		jPanGeocodingType.setLayout(new java.awt.GridBagLayout());
		jPanSimpleTable.setLayout(new java.awt.GridBagLayout());

		groupRadio.add(jRadioSimple);
		jRadioSimple.setSelected(true);
		jRadioSimple.setEnabled(false);
		jRadioSimple.setText("Simple geocoding");
		jRadioSimple.setToolTipText("Simple geocoding");
		jRadioSimple.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evSimpleGeocoding(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 50, 0, 10);
		jPanSimpleTable.add(jRadioSimple, gridBagConstraints);

		jPanTable.setLayout(new java.awt.GridBagLayout());

		jComboTable.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "" }));
		jComboTable.setMinimumSize(new java.awt.Dimension(150, 18));
		jComboTable.setPreferredSize(new java.awt.Dimension(200, 22));

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		jComboTable.setEnabled(false);
		jComboTable.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evChangeTable(evt);
			}
		});
		jPanTable.add(jComboTable, gridBagConstraints);

		groupRadio.add(jRadioTable);
		jRadioTable.setText("Table geocoding");
		jRadioTable.setEnabled(false);
		jRadioTable.setToolTipText("Table geocoding");
		jRadioTable.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evTableGeocoding(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 2);
		jPanTable.add(jRadioTable, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		jPanSimpleTable.add(jPanTable, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanGeocodingType.add(jPanSimpleTable, gridBagConstraints);

		jPanType.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Search Type"));
		jPanType.setLayout(new java.awt.GridBagLayout());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanGeocodingType.add(jPanType, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 0.7;
		gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
		add(jPanGeocodingType, gridBagConstraints);

		jPanSettings.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Settings"));
		jPanSettings.setLayout(new java.awt.GridBagLayout());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
		jPanSettings.add(settingsPanel, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		jPanGeocodingType.add(jPanSettings, gridBagConstraints);

		jPanResults.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Results"));
		jPanResults.setLayout(new java.awt.GridBagLayout());
		jTableResults.setEnabled(false);
		jTableResults.setName("jTableResults");
		jTableResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		jTableResults.setModel(new TableResultsModel(control));
		jTableResults.addMouseListener(this);

		jScrollResults.setViewportView(jTableResults);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		jPanResults.add(jScrollResults, gridBagConstraints);

		jPanResultsButtons.setLayout(new java.awt.GridBagLayout());

		jButNext.setText("");
		jButNext.setToolTipText("Next register");
		jButNext.setEnabled(false);
		jButNext.setPreferredSize(new Dimension(35, 25));
		jButNext.setMaximumSize(new Dimension(35, 25));
		jButNext.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evNextButton(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		jPanResultsButtons.add(jButNext, gridBagConstraints);

		jLabRow.setText("");
		jLabRow.setEnabled(false);
		jLabRow.setHorizontalAlignment(SwingConstants.CENTER);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		jPanResultsButtons.add(jLabRow, gridBagConstraints);

		jButPrevious.setText("");
		jButPrevious.setToolTipText("Previous register");
		jButPrevious.setEnabled(false);
		jButPrevious.setMaximumSize(new Dimension(35, 25));
		jButPrevious.setPreferredSize(new Dimension(35, 25));
		jButPrevious.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evPreviousButton(evt);
			}
		});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanResultsButtons.add(jButPrevious, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
		jPanResults.add(jPanResultsButtons, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
		add(jPanResults, gridBagConstraints);

		jPanButtons.setLayout(new java.awt.GridBagLayout());

		jButExit.setText("Exit");
		jButExit.setToolTipText("Exit");
		jButExit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evExit(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
		jPanButtons.add(jButExit, gridBagConstraints);

		jButSearch.setText("Search");
		jButSearch.setToolTipText("Search");
		jButSearch.setEnabled(false);
		jButSearch.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evSearch(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		jPanButtons.add(jButSearch, gridBagConstraints);

		jButExport.setText("Export");
		jButExport.setToolTipText("Export");
		jButExport.setEnabled(false);
		jButExport.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evExport(evt);
			}
		});
		jPanButtons.add(jButExport, new java.awt.GridBagConstraints());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 4, 2);
		add(jPanButtons, gridBagConstraints);
	}

	/**
	 * Load a pattern from file event
	 * 
	 * @param evt
	 */
	private void evLoadPattern(java.awt.event.ActionEvent evt) {
		/* load the pattern */
		boolean ok = control.loadPatternFromXML();

		if (ok) {
			// update the pattern parameters in GUI
			control.updateGeocoGUI();
			jButEdit.setEnabled(true);

			// enable components
			if (control.getPattern() != null) {
				this.enableComponents();
				// insert address panel
				insertAddressPanel();
			}
		}
	}

	/**
	 * create new pattern event
	 * 
	 * @param evt
	 */
	private void evNewPattern(java.awt.event.ActionEvent evt) {
		control.launchNewPatternPanel(null);
	}

	/**
	 * edit current pattern event
	 * 
	 * @param evt
	 * @throws PersistenceException
	 */
	private void evEditPattern(java.awt.event.ActionEvent evt) {

		Patterngeocoding pat = control.getPattern();
		Patterngeocoding copypat = null;
		// Copiar del patrón (Clonar)
		PersistenceManager manager = ToolsLocator.getPersistenceManager();
		PersistentState state = null;
		try {
			state = manager.getState(pat);
			copypat = new DefaultPatterngeocoding();
			copypat.setState(state);
		} catch (PersistenceException e) {
			log.error("Error cloning the pattern",e);
		}
		if(copypat != null){
			control.launchNewPatternPanel(copypat);
		}
		
	}

	/**
	 * Simple geocoding event
	 * 
	 * @param evt
	 */
	private void evSimpleGeocoding(java.awt.event.ActionEvent evt) {
		control.getGmodel().setSimple(true);
		activeTableGUIFeatures(false);
		jButExport.setEnabled(false);
		// insert address panel
		insertAddressPanel();
		// clean table results
		cleanTableResults();
		// clean point on view
		control.clearGeocodingPoint();
	}

	/**
	 * Tables availables event
	 * 
	 * @param evt
	 */
	private void evTableGeocoding(java.awt.event.ActionEvent evt) {
		/* Put the tables names availables in the combo */
		DefaultComboBoxModel comboModel = control.getModelgvSIGTables();
		if (comboModel == null) {
			comboModel = new DefaultComboBoxModel();
		}
		/* save in the model table geocoding */
		jComboTable.setModel(comboModel);
		/* enable components */
		control.getGmodel().setSimple(false);
		jComboTable.setEnabled(true);
		jButExport.setEnabled(false);
		// save descriptor table in the model
		List<String> descs = getListOfDescriptorSelectedTable();
		control.getGmodel().setListDescriptorSelectedTable(descs);
		// insert address panel
		insertAddressPanel();
		// clean results
		cleanTableResults();
		// clean point on view
		control.clearGeocodingPoint();

	}

	/**
	 * Table selected event
	 * 
	 * @param evt
	 */
	private void evChangeTable(java.awt.event.ActionEvent evt) {

		// save descriptor table in the model
		List<String> descs = getListOfDescriptorSelectedTable();
		control.getGmodel().setListDescriptorSelectedTable(descs);
		// insert address panel
		insertAddressPanel();
	}

	/**
	 * next button
	 * 
	 * @param evt
	 */
	private void evNextButton(java.awt.event.ActionEvent evt) {
		List<Set<GeocodingResult>> results = control.getGmodel()
				.getAllResults();
		int row = control.getGmodel().getNumResultShowed();
		int cant = results.size() - 2;
		if (row <= cant) {
			Set<GeocodingResult> res = results.get(row + 1);
			((TableResultsModel) jTableResults.getModel()).setResultSet(res,
					control.getPattern().getSettings().getResultsNumber(),
					control.getPattern().getSettings().getScore());
			jLabRow.setText(control.ROW + Integer.toString(row + 2));
			control.getGmodel().setNumResultShowed(row + 1);

			// Show results position in the gvSIG view
			control.showResultsPositionsOnView(res);

			// select first result element and zoom it
			zoomTo(0);
		}
	}

	/**
	 * Previous event
	 * 
	 * @param evt
	 */
	private void evPreviousButton(java.awt.event.ActionEvent evt) {
		List<Set<GeocodingResult>> results = control.getGmodel()
				.getAllResults();
		int row = control.getGmodel().getNumResultShowed();
		if (row > 0) {
			Set<GeocodingResult> res = results.get(row - 1);
			((TableResultsModel) jTableResults.getModel()).setResultSet(res,
					control.getPattern().getSettings().getResultsNumber(),
					control.getPattern().getSettings().getScore());
			jLabRow.setText(control.ROW + Integer.toString(row));
			control.getGmodel().setNumResultShowed(row - 1);

			// Show results positions in the gvSIG view
			control.showResultsPositionsOnView(res);

			// Select first result element and zoom it
			zoomTo(0);
		}
	}

	/**
	 * geocoding search event
	 * 
	 * @param evt
	 */
	private void evSearch(java.awt.event.ActionEvent evt) {
		// clear points in view
		control.clearGeocodingPoint();
		// clear table results
		cleanTableResults();
		// GEOCODING PROCESS
		control.geocoding();
	}

	/**
	 * export event
	 * 
	 * @param evt
	 */
	private void evExport(java.awt.event.ActionEvent evt) {
		new MasiveExportThread(control).run();
		exit();
	}

	/**
	 * exit event. Close the panel
	 * 
	 * @param evt
	 */
	private void evExit(java.awt.event.ActionEvent evt) {
		exit();
	}

	/**
	 * get window info
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo info = new WindowInfo(WindowInfo.RESIZABLE);
		info.setMinimumSize(this.getMinimumSize());
		info.setWidth(600);
		info.setHeight(530);
		info.setTitle(PluginServices.getText(this, "geocoding"));
		info.setX(100);
		info.setY(150);
		return info;
	}

	/**
	 * get window profile
	 */
	public Object getWindowProfile() {
		return null;
	}

	/**
	 * insert changeable address panel
	 * 
	 * @param pan
	 */
	public void insertAddressPanel() {

		// get the panel depending of on the type of geocoding pattern
		this.aPanel = control.getAddressPanelFromPatternStyle();
		jPanType.removeAll();

		GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;

		if (aPanel != null) {
			jPanType.add(this.aPanel, gridBagConstraints);
			this.aPanel.validate();
			this.setSize(this.getWidth() + 1, this.getHeight());
			jPanType.validate();
			jPanResults.validate();
			jPanPattern.validate();
			this.validate();
		}

		this.setSize(this.getWidth() + 1, this.getHeight());
		jPanType.validate();
		jPanResults.validate();
		jPanPattern.validate();
		this.validate();

	}

	/**
	 * Set maximun results parameter
	 * 
	 * @param results
	 */
	public void setParamMaxresults(int results) {
		settingsPanel.setMaxResults(new Integer(results));
	}

	/**
	 * Set score parameter
	 * 
	 * @param score
	 */
	public void setParamScore(double score) {
		settingsPanel.setScore(new Double(score));

	}

	/**
	 * get the path pattern
	 * 
	 * @return
	 */
	public String getJTextPatternPath() {
		return jTextPatternPath.getText();
	}

	/**
	 * Set path pattern
	 * 
	 * @param path
	 */
	public void setJTextPatternPath(String path) {
		this.jTextPatternPath.setText(path);
		jButEdit.setEnabled(true);
	}

	/**
	 * Enable components when load a pattern or create new pattern
	 */
	public void enableComponents() {
		jRadioSimple.setEnabled(true);
		if (control.getListgvSIGTables().size() > 0) {
			jRadioTable.setEnabled(true);
		}
		jLabType.setEnabled(true);
		jComboType.setEnabled(true);
		jLabScore.setEnabled(true);
		jTableResults.setEnabled(true);
		jButSearch.setEnabled(true);
		settingsPanel.activateComponents(true);
	}

	/**
	 * Active GUI features related with Massive geocoding from table
	 * 
	 * @param active
	 */
	public void activeTableGUIFeatures(boolean active) {
		jComboTable.setEnabled(active);

		if (!jRadioSimple.isSelected() && active) {
			jButNext.setEnabled(active);
			jButPrevious.setEnabled(active);
			jLabRow.setEnabled(active);
			jButExport.setEnabled(active);

		} else {
			jLabRow.setText("");
			jButNext.setEnabled(false);
			jButPrevious.setEnabled(false);
			jButExport.setEnabled(false);
		}
	}

	/**
	 * mouse clicked event
	 */
	public void mouseClicked(MouseEvent e) {
		// nothing to do
	}

	/**
	 * mouse entered event
	 */
	public void mouseEntered(MouseEvent e) {
		if (control.getPattern() != null) {
			List<FeatureTableDocument> tables = control.getListgvSIGTables();
			if (tables.size() > 0) {
				if (control.getPattern() != null) {
					jRadioTable.setEnabled(true);
				}
			} else {
				jRadioTable.setEnabled(false);
			}
		}
	}

	/**
	 * mouse exit event
	 */
	public void mouseExited(MouseEvent e) {
		// nothing to do
	}

	/**
	 * Mouse pressed events
	 */
	public void mousePressed(MouseEvent e) {
		// nothing to do
	}

	/**
	 * Mouse released event
	 */
	public void mouseReleased(MouseEvent e) {
		// mouse released over Table results
		String comp = e.getComponent().getName();
		if (comp != null && comp.compareTo("jTableResults") == 0) {

			int row = jTableResults.getSelectedRow();
			int nshow = control.getGmodel().getNumResultShowed();

			// one click
			// update array selected optional result
			control.getGmodel().getExportElements()[nshow] = row;
			// dynamic zoom
			if (row >= 0) {
				zoomTo(row);
			}
			log.debug("List: " + nshow + "    Selected: " + row);

			// double click
			if (e.getClickCount() == 2) {
				jTableResults.clearSelection();
				control.clearGeocodingPoint();
				// update array selected optional result
				control.getGmodel().getExportElements()[nshow] = -1;
				log.debug("List: " + nshow + "    Selected: -1");
			}
		}
	}

	/**
	 * get selected table
	 * 
	 * @return
	 */
	public FeatureTableDocument getSelectedTable() {
		return (FeatureTableDocument) jComboTable.getModel().getSelectedItem();
	}

	/**
	 * get window model
	 * 
	 * @return
	 */
	public Object getWindowModel() {
		return this.getClass().getCanonicalName();
	}

	/**
	 * get list of fields descriptors of the selected table
	 * 
	 * @return
	 */
	private List<String> getListOfDescriptorSelectedTable() {
		List<String> descs = new ArrayList<String>();
		FeatureTableDocument table = (FeatureTableDocument) jComboTable
				.getSelectedItem();
		FeatureStore store = (FeatureStore) table.getStore();
		control.getGmodel().setSelectedTableStore(store);

		try {
			int nDescs = store.getDefaultFeatureType().size();
			for (int i = 0; i < nDescs; i++) {
				String desc = store.getDefaultFeatureType()
						.getAttributeDescriptor(i).getName();
				descs.add(desc);
			}
		} catch (DataException e) {
			log
					.error(
							"Error getting the fields descriptors of the selected table",
							e);
		}
		return descs;
	}

	/**
	 * get address panel
	 * 
	 * @return
	 */
	public AbstractAddressPanel getAddressPanel() {
		return aPanel;
	}

	/**
	 * get table of results
	 * 
	 * @return
	 */
	public JTable getJTableResults() {
		return jTableResults;
	}

	/**
	 * 
	 * @param string
	 */
	public void setLabRow(String string) {
		jLabRow.setText(string);

	}

	/**
	 * Zoom to result position
	 * 
	 * @param row
	 */
	private void zoomTo(int row) {

		// select first result position
		jTableResults.setRowSelectionInterval(row, row);

		TableResultsModel model = (TableResultsModel) jTableResults.getModel();
		// zoom to geom
		Point pto = model.getGeometry(row, 2, 3);
		try {
			control.zoomToPoint(pto.getX(), pto.getY());
		} catch (Exception e1) {
			log.error("Doing zoom to point", e1);
		}
	}

	/**
	 * Clean table results
	 */
	public void cleanTableResults() {
		jTableResults.setModel(new TableResultsModel(control));
		jTableResults.validate();
	}

	/**
	 * disable all components of main panel
	 */
	private void disableComponentsGUI() {
		jTextPatternPath.setText("");
		jButEdit.setEnabled(false);
		jRadioSimple.setEnabled(false);
		jRadioTable.setEnabled(false);
		jComboTable.setEnabled(false);
		jTableResults.setEnabled(false);
		jButSearch.setEnabled(false);
		jPanType.removeAll();
		jPanType.validate();
	}

	private void exit() {
		control.getGmodel().setSimple(true);
		this.jRadioSimple.setSelected(true);
		this.jPanGeocodingType.validate();
		this.validate();
		// close panel
		this.control.getGmodel().setPattern(null);
		IWindow[] iws = PluginServices.getMDIManager().getAllWindows();
		for (int i = 0; i < iws.length; i++) {
			if (iws[i] instanceof GeocodingPanel) {
				PluginServices.getMDIManager().closeWindow(iws[i]);
			}
		}
		// clean point on view
		control.clearGeocodingPoint();
		// clean table results
		this.cleanTableResults();
		// clean panel
		settingsPanel.activateComponents(false);
		disableComponentsGUI();
		activeTableGUIFeatures(false);
		// select radio simple
		jRadioSimple.setSelected(true);
		insertAddressPanel();
	}

}
