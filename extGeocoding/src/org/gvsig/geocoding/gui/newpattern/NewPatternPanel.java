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
 * 2008 Prodevelop S.L. main developer
 */

package org.gvsig.geocoding.gui.newpattern;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.geocoding.address.Literal;
import org.gvsig.geocoding.address.RelationsComponent;
import org.gvsig.geocoding.address.impl.DefaultLiteral;
import org.gvsig.geocoding.address.impl.DefaultRelationsComponent;
import org.gvsig.geocoding.extension.GeocodingController;
import org.gvsig.geocoding.gui.settings.SettingsPanel;
import org.gvsig.geocoding.gui.styles.ComposedStyle;
import org.gvsig.geocoding.gui.styles.DoubleRangeStyle;
import org.gvsig.geocoding.gui.styles.SimpleCentroidStyle;
import org.gvsig.geocoding.gui.styles.SimpleRangeStyle;
import org.gvsig.geocoding.pattern.GeocodingSettings;
import org.gvsig.geocoding.pattern.Patterngeocoding;
import org.gvsig.geocoding.pattern.impl.DefaultPatterngeocoding;
import org.gvsig.geocoding.styles.AbstractGeocodingStyle;
import org.gvsig.geocoding.styles.impl.Composed;
import org.gvsig.geocoding.styles.impl.DoubleRange;
import org.gvsig.geocoding.styles.impl.SimpleCentroid;
import org.gvsig.geocoding.styles.impl.SimpleRange;
import org.gvsig.geocoding.utils.ComboLayerName;
import org.gvsig.geocoding.utils.ComboShowName;
import org.gvsig.geocoding.utils.GeocodingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * New pattern panel
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class NewPatternPanel extends JPanel implements IWindow {

	private static final Logger log = LoggerFactory
			.getLogger(NewPatternPanel.class);
	private static final long serialVersionUID = 1L;

	private Patterngeocoding pattern = null;
	private GeocodingController control = null;

	private SimpleRangePanel srange = null;
	private DoubleRangePanel drange = null;

	private boolean editing = false;

	private JButton jButAdd;
	private JButton jButCancel;
	private JButton jButDel;
	private JButton jButDown;
	private JButton jButSave;
	private JButton jButUp;
	private JComboBox jComboLayers;
	private JComboBox jComboType;
	private JLabel jLabLayers;
	private JLabel jLabType;
	private JTextArea jTextTypeInfo;
	private JPanel jPanButsEle;
	private JPanel jPanButtons;
	private JPanel jPanDataSource;
	private JPanel jPanElements;
	private JPanel jPanRange;
	private JPanel jPanSettings;
	private JPanel jPanType;
	private JScrollPane jScrollElements;
	private JScrollPane jScrollTypeInfo;
	private JTable jTableComponents;

	private SettingsPanel settingsPanel;

	/**
	 * Constructor
	 */
	public NewPatternPanel(GeocodingController _control, Patterngeocoding pat) {
		this.control = _control;

		initComponents();
		setMessages();
		setImages();
		// Fill the combo of geocoding types
		fillTypesCombo();
		// fill the combo of layers
		fillLayersCombo();

		// dynamic panels
		srange = new SimpleRangePanel(control, null);
		drange = new DoubleRangePanel(control, null);

		// fill the table elemnts
		List<String> elements = control.getListAddressComponents();
		List<String> descs = getLayerDescriptors();
		fillComponentsCombos(elements, descs);

		// if modify a pattern
		if (pat != null) {
			editing = true;
			updatePanel(pat);
		} else {
			pattern = new DefaultPatterngeocoding();
			control.getGmodel().setPattern(pattern);

			// put settings parameters
			settingsPanel.setMaxResults(pattern.getSettings()
					.getResultsNumber());
			settingsPanel.setScore(pattern.getSettings().getScore());
		}

	}

	/**
	 * 
	 * @param pat
	 */
	public void updatePanel(Patterngeocoding pat) {

		// update settings
		settingsPanel.setMaxResults(pat.getSettings().getResultsNumber());
		double sco = pat.getSettings().getScore();
		settingsPanel.setScore(sco);

		// update style and update components
		AbstractGeocodingStyle astyle = pat.getSource().getStyle();
		if (astyle instanceof SimpleCentroid) {
			SimpleCentroid simSty = (SimpleCentroid) astyle;
			jComboType.setSelectedIndex(0);
			setLiteralTableComponents(simSty.getRelationsLiteral());
		} else if (astyle instanceof SimpleRange) {
			jComboType.setSelectedIndex(1);
			SimpleRange ransimSty = (SimpleRange) astyle;
			srange.setDescriptorFronMun(((RelationsComponent) ransimSty
					.getFirstNumber()).getValue());
			srange.setDescriptorToMun(((RelationsComponent) ransimSty
					.getLastNumber()).getValue());
			setLiteralTableComponents(ransimSty.getRelationsLiteral());
		} else if (astyle instanceof DoubleRange) {
			jComboType.setSelectedIndex(2);
			DoubleRange randouSty = (DoubleRange) astyle;
			drange.setDescriptorRightFronMun(((RelationsComponent) randouSty
					.getFirstRightNumber()).getValue());
			drange.setDescriptorRightToMun(((RelationsComponent) randouSty
					.getLastRightNumber()).getValue());
			drange.setDescriptorLeftFronMun(((RelationsComponent) randouSty
					.getFirstLeftNumber()).getValue());
			drange.setDescriptorLeftToMun(((RelationsComponent) randouSty
					.getLastLeftNumber()).getValue());
			setLiteralTableComponents(randouSty.getRelationsLiteral());
		} else if (astyle instanceof Composed) {
			Composed comSty = (Composed) astyle;
			jComboType.setSelectedIndex(3);
			setLiteralTableComponents(comSty.getRelationsLiteral());
		}
		// update layer
		DataStore store = pat.getSource().getLayerSource();
		String name = store.getName();
		// TODO falta recargar la layer desde el datasotre para seleccionar la
		// capa correcta en el combo
		// jComboLayers.seS

	}

	/**
	 * Get window info
	 * 
	 * @return
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo info = new WindowInfo(WindowInfo.MODALDIALOG
				| WindowInfo.RESIZABLE);

		info.setMinimumSize(this.getMinimumSize());
		info.setWidth(480);
		info.setHeight(470);
		info.setTitle(PluginServices.getText(this, "new_geocoding_pattern"));
		return info;
	}

	/**
	 * get window profile
	 * 
	 * @return
	 */
	public Object getWindowProfile() {
		return null;
	}

	/**
	 * Initialize panel components
	 */
	@SuppressWarnings("serial")
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		jPanType = new JPanel();
		jLabType = new JLabel();
		jComboType = new JComboBox();
		jTextTypeInfo = new JTextArea();
		jScrollTypeInfo = new JScrollPane();
		jPanDataSource = new JPanel();
		jLabLayers = new JLabel();
		jComboLayers = new JComboBox();
		jPanElements = new JPanel();
		jScrollElements = new JScrollPane();
		jTableComponents = new JTable();
		jPanRange = new JPanel();
		jPanButsEle = new JPanel();
		jButUp = new JButton();
		jButAdd = new JButton();
		jButDel = new JButton();
		jButDown = new JButton();
		jPanSettings = new JPanel();
		jPanButtons = new JPanel();
		jButSave = new JButton();
		jButCancel = new JButton();
		settingsPanel = new SettingsPanel(control);

		setLayout(new java.awt.GridBagLayout());

		jPanType.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Geocoding type"));
		jPanType.setLayout(new java.awt.GridBagLayout());

		jLabType.setIcon(new ImageIcon(PluginServices.getPluginServices(this)
				.getClassLoader().getBaseDir()
				+ File.separator
				+ "images"
				+ File.separator
				+ "new"
				+ File.separator + "centroid.png"));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridheight = 2;
		gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 5);
		jPanType.add(jLabType, gridBagConstraints);

		jComboType.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				"1", "2", "3" }));
		jComboType.setMinimumSize(new java.awt.Dimension(26, 20));
		jComboType.setPreferredSize(new java.awt.Dimension(30, 20));
		jComboType.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evChangeType(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
		jPanType.add(jComboType, gridBagConstraints);

		jTextTypeInfo.setColumns(20);
		jTextTypeInfo.setFont(new java.awt.Font("Arial", 0, 10));
		jTextTypeInfo.setLineWrap(true);
		jTextTypeInfo.setRows(2);
		jTextTypeInfo.setEditable(false);
		jTextTypeInfo.setMinimumSize(new Dimension(160, 30));
		jTextTypeInfo.setPreferredSize(new Dimension(160, 40));
		jTextTypeInfo.setText(PluginServices.getText(null, "simcenstyle"));
		jScrollTypeInfo.setViewportView(jTextTypeInfo);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 3, 10);
		jPanType.add(jScrollTypeInfo, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 0.3;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		add(jPanType, gridBagConstraints);

		jPanDataSource.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Data source"));
		jPanDataSource.setLayout(new java.awt.GridBagLayout());

		jLabLayers.setText("Layers");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 5);
		jPanDataSource.add(jLabLayers, gridBagConstraints);

		jComboLayers.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "1", "2", "3" }));
		jComboLayers.setMinimumSize(new java.awt.Dimension(26, 20));
		jComboLayers.setPreferredSize(new java.awt.Dimension(30, 20));
		jComboLayers.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evChangeLayer(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
		jPanDataSource.add(jComboLayers, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		add(jPanDataSource, gridBagConstraints);

		jPanElements.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Elements"));
		jPanElements.setLayout(new java.awt.GridBagLayout());

		jTableComponents.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] {}, new String[] { "Elements", "Field" }) {
			boolean[] canEdit = new boolean[] { true, true };

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});

		jScrollElements.setViewportView(jTableComponents);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 0.9;
		jPanElements.add(jScrollElements, gridBagConstraints);

		jPanRange.setLayout(new java.awt.GridBagLayout());
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
		jPanElements.add(jPanRange, gridBagConstraints);

		jPanButsEle.setLayout(new java.awt.GridBagLayout());

		jButUp.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evUpComponent(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
		jPanButsEle.add(jButUp, gridBagConstraints);

		jButAdd.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evAddComponent(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		jPanButsEle.add(jButAdd, gridBagConstraints);

		jButDel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evDelComponent(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		jPanButsEle.add(jButDel, gridBagConstraints);

		jButDown.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evDownComponent(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		jPanButsEle.add(jButDown, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
		jPanElements.add(jPanButsEle, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		add(jPanElements, gridBagConstraints);

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
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		add(jPanSettings, gridBagConstraints);

		jPanButtons.setLayout(new java.awt.GridBagLayout());

		jButSave.setText("Save");
		jButSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evSave(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
		jPanButtons.add(jButSave, gridBagConstraints);

		jButCancel.setText("Cancel");
		jButCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evCancel(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
		jPanButtons.add(jButCancel, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		add(jPanButtons, gridBagConstraints);
	}

	/**
	 * Change layer selected in the combo
	 * 
	 * @param evt
	 */
	private void evChangeLayer(java.awt.event.ActionEvent evt) {
		ComboLayerName cLyr = (ComboLayerName) jComboLayers.getSelectedItem();
		if (cLyr != null) {

			// reload the descriptors of the new layer in the combos
			srange.setComboValues(getLayerDescriptors());
			drange.setComboValues(getLayerDescriptors());

			// clean the table model
			this.clearTableModel((DefaultTableModel) jTableComponents
					.getModel());
			// jTableComponents.setModel(model);
			jTableComponents.validate();

			// fill the combos of the table with descriptors of the new layer
			fillComponentsCombos(control.getListAddressComponents(),
					getLayerDescriptors());
		}

	}

	/**
	 * save the pattern
	 * 
	 * @param evt
	 */
	private void evSave(java.awt.event.ActionEvent evt) {
		/* PreSave */
		boolean ok = preSave();

		if (ok) {
			/* save the new pattern in the model */
			control.getGmodel().setPattern(pattern);

			/* save pattern */
			boolean apto = control.savePattern();

			/* close the panel */
			if (apto) {
				IWindow[] iws = PluginServices.getMDIManager().getAllWindows();
				for (int i = 0; i < iws.length; i++) {
					if (iws[i] instanceof NewPatternPanel) {
						PluginServices.getMDIManager().closeWindow(iws[i]);
					}
				}
				// update panel
				control.updateGeocoGUI();

				// enable components
				if (control.getPattern() != null) {
					control.getGPanel().enableComponents();
					// insert address panel
					control.getGPanel().insertAddressPanel();

				}
				// clean table results
				control.clearGeocodingPoint();
				// clean points on view
				control.getGPanel().cleanTableResults();
			}
		}
	}

	/**
	 * Cancel the process
	 * 
	 * @param evt
	 */
	private void evCancel(java.awt.event.ActionEvent evt) {
		/* Close the window */
		IWindow[] iws = PluginServices.getMDIManager().getAllWindows();
		for (int i = 0; i < iws.length; i++) {
			if (iws[i] instanceof NewPatternPanel) {
				PluginServices.getMDIManager().closeWindow(iws[i]);
			}
		}

	}

	/**
	 * Change geocoding type
	 * 
	 * @param evt
	 */
	private void evChangeType(java.awt.event.ActionEvent evt) {
		ComboShowName selected = (ComboShowName) jComboType.getSelectedItem();
		if (selected instanceof SimpleCentroidStyle) {
			jTextTypeInfo.setText(PluginServices.getText(null, "simcenstyle"));
			insertDynamicPanel(null);
			jLabType.setIcon(new ImageIcon(PluginServices.getPluginServices(
					this).getClassLoader().getBaseDir()
					+ File.separator
					+ "images"
					+ File.separator
					+ "new"
					+ File.separator + "centroid.png"));
			jLabType.validate();
		}
		if (selected instanceof SimpleRangeStyle) {
			jTextTypeInfo
					.setText(PluginServices.getText(null, "rangesimstyle"));
			insertDynamicPanel(srange);
			List<String> desc = getLayerDescriptors();
			srange.setComboValues(desc);
			jLabType.setIcon(new ImageIcon(PluginServices.getPluginServices(
					this).getClassLoader().getBaseDir()
					+ File.separator
					+ "images"
					+ File.separator
					+ "new"
					+ File.separator + "simplerange.png"));
			jLabType.validate();
		}
		if (selected instanceof DoubleRangeStyle) {
			jTextTypeInfo
					.setText(PluginServices.getText(null, "rangedoustyle"));
			insertDynamicPanel(drange);
			List<String> desc = getLayerDescriptors();
			drange.setComboValues(desc);
			jLabType.setIcon(new ImageIcon(PluginServices.getPluginServices(
					this).getClassLoader().getBaseDir()
					+ File.separator
					+ "images"
					+ File.separator
					+ "new"
					+ File.separator + "doublerange.png"));
			jLabType.validate();
		}
		if (selected instanceof ComposedStyle) {
			jTextTypeInfo.setText(PluginServices.getText(null, "compostyle"));
			insertDynamicPanel(null);
			jLabType.setIcon(new ImageIcon(PluginServices.getPluginServices(
					this).getClassLoader().getBaseDir()
					+ File.separator
					+ "images"
					+ File.separator
					+ "new"
					+ File.separator + "composed.png"));
			jLabType.validate();
		}

	}

	/**
	 * Up one position selected component in the table
	 * 
	 * @param evt
	 */
	private void evUpComponent(java.awt.event.ActionEvent evt) {
		DefaultTableModel model = (DefaultTableModel) jTableComponents
				.getModel();
		int sel = jTableComponents.getSelectedRow();
		if (model.getRowCount() > 0 && sel > 0) {

			model.moveRow(sel, sel, sel - 1);
			jTableComponents.setRowSelectionInterval(sel - 1, sel - 1);
			jTableComponents.validate();
		}
	}

	/**
	 * Add a new component to table
	 * 
	 * @param evt
	 */
	private void evAddComponent(java.awt.event.ActionEvent evt) {
		DefaultTableModel model = (DefaultTableModel) jTableComponents
				.getModel();
		String[] rowData = { "...", "..." };
		model.addRow(rowData);
		int n = model.getRowCount();
		jTableComponents.setRowSelectionInterval(n - 1, n - 1);
		jTableComponents.validate();
	}

	/**
	 * Remove selected component of the table
	 * 
	 * @param evt
	 */
	private void evDelComponent(java.awt.event.ActionEvent evt) {

		int sel = jTableComponents.getSelectedRow();
		if (sel >= 0) {
			DefaultTableModel model = (DefaultTableModel) jTableComponents
					.getModel();
			model.removeRow(sel);
			if (sel != 0) {
				jTableComponents.setRowSelectionInterval(sel - 1, sel - 1);
			}
			jTableComponents.validate();
		}

	}

	/**
	 * 
	 * @param evt
	 */
	private void evDownComponent(java.awt.event.ActionEvent evt) {
		DefaultTableModel model = (DefaultTableModel) jTableComponents
				.getModel();
		int sel = jTableComponents.getSelectedRow();
		if (model.getRowCount() > 0 && sel < model.getRowCount() - 1) {
			model.moveRow(sel, sel, sel + 1);
			jTableComponents.setRowSelectionInterval(sel + 1, sel + 1);
			jTableComponents.validate();
		}
	}

	/**
	 * Fills the combo of geocoding types
	 */
	private void fillTypesCombo() {

		SimpleCentroidStyle simple = new SimpleCentroidStyle();
		SimpleRangeStyle simplerange = new SimpleRangeStyle();
		DoubleRangeStyle doublerange = new DoubleRangeStyle();
		ComposedStyle composed = new ComposedStyle();
		Object[] item = { simple, simplerange, doublerange, composed };
		DefaultComboBoxModel model = new DefaultComboBoxModel(item);
		jComboType.setModel(model);
	}

	/**
	 * Fills the combo of geocoding types
	 */
	private void fillLayersCombo() {

		List<FLyrVect> lyrs = control.getListgvSIGVectLayers();
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		if (lyrs.size() > 0) {
			for (FLyrVect lyr : lyrs) {
				ComboLayerName cLyr = new ComboLayerName(lyr);
				model.addElement(cLyr);
			}
			jComboLayers.setModel(model);
		} else {
			jComboLayers.setModel(model);
		}
	}

	/**
	 * This method updates all labels of the panel from properties file
	 */
	private void setMessages() {

		PluginServices ps = PluginServices.getPluginServices(this);

		jPanDataSource.setBorder(GeocodingUtils.getTitledBorder(ps
				.getText("datasource")));
		jPanType.setBorder(GeocodingUtils.getTitledBorder(ps.getText("gtype")));
		jLabLayers.setText(ps.getText("selectedlayer"));
		jPanElements.setBorder(GeocodingUtils.getTitledBorder(ps
				.getText("components")));
		jPanSettings.setBorder(GeocodingUtils.getTitledBorder(ps
				.getText("settings")));
		jButCancel.setText(ps.getText("cancel"));
		jButSave.setText(ps.getText("save"));

		String[] volumnNames = { ps.getText("xelement"), ps.getText("xfield") };
		((DefaultTableModel) jTableComponents.getModel())
				.setColumnIdentifiers(volumnNames);

	}

	/**
	 * This method updates all images of the panel
	 */
	private void setImages() {

		String baseDir = PluginServices.getPluginServices(this)
				.getClassLoader().getBaseDir();

		jButSave.setIcon(new ImageIcon(baseDir + File.separator + "images"
				+ File.separator + "icons16" + File.separator + "save.png"));
		jButCancel.setIcon(new ImageIcon(baseDir + File.separator + "images"
				+ File.separator + "icons16" + File.separator + "stop.png"));

		jButUp.setIcon(new ImageIcon(baseDir + File.separator + "images"
				+ File.separator + "icons16" + File.separator + "up.png"));
		jButAdd.setIcon(new ImageIcon(baseDir + File.separator + "images"
				+ File.separator + "icons16" + File.separator + "add.png"));
		jButDel.setIcon(new ImageIcon(baseDir + File.separator + "images"
				+ File.separator + "icons16" + File.separator + "remove.png"));
		jButDown.setIcon(new ImageIcon(baseDir + File.separator + "images"
				+ File.separator + "icons16" + File.separator + "down.png"));
	}

	/**
	 * This method saves same pattern's attributes
	 */
	private boolean preSave() {

		boolean ok = true;

		// save the feature store in the pattern
		ComboLayerName cLyr = (ComboLayerName) jComboLayers.getSelectedItem();
		if (cLyr != null) {
			FLyrVect lyr = cLyr.getLayer();
			FeatureStore store = null;
			try {
				store = lyr.getFeatureStore();
				DataStoreParameters params = store.getParameters();
				String proj = lyr.getProjection().getAbrev();
				params.setDynValue("srs", proj);
				// params.setDynValue("DefaultSRS", proj);
				this.pattern.getSource().setLayerSource(store);

			} catch (ReadException e) {
				log.error(
						"Error getting the FeatureStore of the selected layer",
						e);
			}
		} else {
			ok = false;
			String message = PluginServices.getText(null, "nopatternlayer");
			String title = PluginServices.getText(null, "geocoding");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
		}
		if (cLyr != null) {
			// save style of geocoding pattern
			ComboShowName cType = (ComboShowName) jComboType.getSelectedItem();
			AbstractGeocodingStyle style = null;
			if (cType instanceof SimpleCentroidStyle) {
				style = new SimpleCentroid();
				Literal literal = getLiteralTableComponents();
				if (literal.size() > 0) {
					style.setRelationsLiteral(literal);
					pattern.getSource().setStyle(style);
				} else {
					ok = false;
					showMessageLiteralEmpty();
				}

			}
			if (cType instanceof SimpleRangeStyle) {
				style = new SimpleRange();
				Literal literal = getLiteralTableComponents();
				if (literal.size() > 0) {
					style.setRelationsLiteral(literal);
					RelationsComponent[] comps = getSimpleRangeComponents();
					((SimpleRange) style).setFirstNumber(comps[0]);
					((SimpleRange) style).setLastNumber(comps[1]);
					pattern.getSource().setStyle(style);
				} else {
					ok = false;
					showMessageLiteralEmpty();
				}
			}
			if (cType instanceof DoubleRangeStyle) {
				style = new DoubleRange();
				Literal literal = getLiteralTableComponents();
				if (literal.size() > 0) {
					style.setRelationsLiteral(literal);
					RelationsComponent[] comps = getDoubleRangeComponents();
					((DoubleRange) style).setFirstRightNumber(comps[0]);
					((DoubleRange) style).setLastRightNumber(comps[1]);
					((DoubleRange) style).setFirstLeftNumber(comps[2]);
					((DoubleRange) style).setLastLeftNumber(comps[3]);
					pattern.getSource().setStyle(style);
				} else {
					ok = false;
					showMessageLiteralEmpty();
				}
			}
			if (cType instanceof ComposedStyle) {
				style = new Composed();
				Literal literal = getLiteralTableComponents();
				if (literal.size() > 0) {
					style.setRelationsLiteral(literal);
					pattern.getSource().setStyle(style);
				} else {
					ok = false;
					showMessageLiteralEmpty();
				}
			}
		}

		// Get parameters
		GeocodingSettings sett = pattern.getSettings();
		sett.setResultsNumber(settingsPanel.getMaxResults());
		sett.setScore(settingsPanel.getScore());
		pattern.setSettings(sett);

		return ok;
	}

	/**
	 * insert ranges panel
	 * 
	 * @param pan
	 */
	private void insertDynamicPanel(JPanel pan) {

		if (pan != null) {
			jPanRange.removeAll();

			GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;

			jPanRange.add(pan, gridBagConstraints);

			this.setSize(this.getWidth() + 1, this.getHeight());
			jPanRange.validate();
			pan.validate();
			this.validate();
		} else {
			jPanRange.removeAll();
			this.validate();
			jPanRange.validate();
		}
	}

	/**
	 * Get layer descriptors
	 * 
	 * @return
	 */
	private List<String> getLayerDescriptors() {
		try {
			ComboLayerName cLyr = (ComboLayerName) jComboLayers
					.getSelectedItem();
			if (cLyr != null) {
				List<String> descs = new ArrayList<String>();
				FeatureType fType = cLyr.getLayer().getFeatureStore()
						.getDefaultFeatureType();
				for (int i = 0; i < fType.size(); i++) {
					descs.add(fType.getAttributeDescriptor(i).getName());
				}
				return descs;
			}
		} catch (Exception e) {
			return null;
		}
		return null;

	}

	/**
	 * 
	 * @param elements
	 * @param descs
	 */
	private void fillComponentsCombos(List<String> elements, List<String> descs) {

		if (elements != null) {

			// first column (elements)
			TableColumn column0 = jTableComponents.getColumnModel()
					.getColumn(0);
			DefaultTableCellRenderer render = new DefaultTableCellRenderer();
			render.setBackground(new Color(255, 253, 224));
			column0.setCellRenderer(render);
			JComboBox combo0 = new JComboBox();
			DefaultComboBoxModel model0 = new DefaultComboBoxModel();
			for (String ele : elements) {
				model0.addElement(ele);
			}
			combo0.setModel(model0);
			DefaultCellEditor dce = new DefaultCellEditor(combo0);
			dce.setClickCountToStart(2);
			column0.setCellEditor(dce);
		}

		if (descs != null) {
			// second column (fields)
			TableColumn column1 = jTableComponents.getColumnModel()
					.getColumn(1);
			JComboBox combo1 = new JComboBox();
			DefaultComboBoxModel model1 = new DefaultComboBoxModel();
			for (String desc : descs) {
				model1.addElement(desc);
			}
			combo1.setModel(model1);
			DefaultCellEditor dce = new DefaultCellEditor(combo1);
			dce.setClickCountToStart(2);
			column1.setCellEditor(dce);
		}
	}

	/**
	 * get literal from table of components
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Literal getLiteralTableComponents() {
		Literal literal = new DefaultLiteral();
		DefaultTableModel model = (DefaultTableModel) jTableComponents
				.getModel();
		Vector data = model.getDataVector();
		for (int i = 0; i < data.size(); i++) {
			String desc = null;
			String element = "";
			try {
				Vector vec = (Vector) data.elementAt(i);
				element = (String) vec.elementAt(0);

				desc = (String) vec.elementAt(1);
			} catch (Exception e) {
				desc = null;
				element = "...";
			}

			if (element.compareTo("...") != 0 && desc != null) {
				RelationsComponent rComp = new DefaultRelationsComponent(
						element, desc);
				literal.add(rComp);
			}
		}
		return literal;
	}

	/**
	 * set literal to table of components
	 * 
	 * @param lit
	 */

	private void setLiteralTableComponents(Literal lit) {
		// clean the table model
		this.clearTableModel((DefaultTableModel) jTableComponents.getModel());
		DefaultTableModel model = (DefaultTableModel) jTableComponents
				.getModel();
		for (Object obj : lit) {
			RelationsComponent comp = (RelationsComponent) obj;
			Object[] obje = { comp.getKeyElement(), comp.getValue() };
			model.addRow(obje);
		}
		jTableComponents.setModel(model);

	}

	/**
	 * Get components of the simple rang
	 * 
	 * @return
	 */
	private RelationsComponent[] getSimpleRangeComponents() {
		RelationsComponent[] comps = new RelationsComponent[2];
		// FromNum
		String fdesc = srange.getDescriptorFronMun();
		if (fdesc != null) {
			RelationsComponent comp = new DefaultRelationsComponent("fromNum",
					fdesc);
			comps[0] = comp;
		}
		// ToNum
		String tdesc = srange.getDescriptorToMun();
		if (tdesc != null) {
			RelationsComponent comp = new DefaultRelationsComponent("toNum",
					tdesc);
			comps[1] = comp;
		}

		return comps;
	}

	/**
	 * Get components of the double range
	 * 
	 * @return
	 */
	private RelationsComponent[] getDoubleRangeComponents() {
		RelationsComponent[] comps = new RelationsComponent[4];

		// RightFromNum
		String rfdesc = drange.getDescriptorRightFronMun();
		if (rfdesc != null) {
			RelationsComponent comp = new DefaultRelationsComponent(
					"rightFromNum", rfdesc);
			comps[0] = comp;
		}
		// RightToNum
		String rtdesc = drange.getDescriptorRightToMun();
		if (rtdesc != null) {
			RelationsComponent comp = new DefaultRelationsComponent(
					"rightToNum", rtdesc);
			comps[1] = comp;
		}

		// LeftFromNum
		String lfdesc = drange.getDescriptorLeftFronMun();
		if (lfdesc != null) {
			RelationsComponent comp = new DefaultRelationsComponent(
					"leftFromNum", lfdesc);
			comps[2] = comp;
		}
		// LeftToNum
		String ltdesc = drange.getDescriptorLeftToMun();
		if (ltdesc != null) {
			RelationsComponent comp = new DefaultRelationsComponent(
					"leftToNum", ltdesc);
			comps[3] = comp;
		}

		return comps;
	}

	/**
	 * Show a error message when the literal has not components
	 */
	private void showMessageLiteralEmpty() {
		String title = PluginServices.getText(null, "geocoding");
		String message = PluginServices.getText(null, "literalempty");
		JOptionPane.showMessageDialog(null, message, title,
				JOptionPane.ERROR_MESSAGE);

	}

	/**
	 * Clear all rows of the TableModel
	 * 
	 * @param model
	 */
	private void clearTableModel(DefaultTableModel model) {
		int n = model.getRowCount();
		for (int i = n - 1; i > -1; i--) {
			model.removeRow(i);
		}
	}

}
