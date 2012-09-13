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
 * 2008 Prodevelop S.L. main development
 */

package org.gvsig.normalization.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import org.gvsig.app.daltransform.gui.impl.AbstractDataTransformWizardPanel;
import org.gvsig.normalization.pattern.Datevalue;
import org.gvsig.normalization.pattern.Decimalvalue;
import org.gvsig.normalization.pattern.Element;
import org.gvsig.normalization.pattern.Fieldseparator;
import org.gvsig.normalization.pattern.Fieldtype;
import org.gvsig.normalization.pattern.Infieldseparators;
import org.gvsig.normalization.pattern.Integervalue;
import org.gvsig.normalization.pattern.Patternnormalization;
import org.gvsig.normalization.pattern.Stringvalue;
import org.gvsig.normalization.pattern.impl.DefaultDatevalue;
import org.gvsig.normalization.pattern.impl.DefaultDecimalvalue;
import org.gvsig.normalization.pattern.impl.DefaultFieldseparator;
import org.gvsig.normalization.pattern.impl.DefaultFieldtype;
import org.gvsig.normalization.pattern.impl.DefaultInfieldseparators;
import org.gvsig.normalization.pattern.impl.DefaultIntegervalue;
import org.gvsig.normalization.pattern.impl.DefaultStringvalue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * Normalization main panel
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class NormalizationPanel extends AbstractDataTransformWizardPanel
		implements ActionListener, ChangeListener {

	private static final long serialVersionUID = -2882235643001676017L;

	private static final Logger log = LoggerFactory
			.getLogger(NormalizationPanel.class);

	private NormalizationPanelController controller;
	private PluginServices ps;
	private boolean dirty = false;
	private int selectedField = 0;

	/* GUI */
	private ButtonGroup butGroupDelimiter;
	private ButtonGroup butGroupFieldType;
	private JButton jButCancel;
	private JButton jButAddField;
	private JButton jButDownField;
	private JButton jButRemoveField;
	private JButton jButUpField;
	private JButton jButLoad;
	private JButton jButRun;
	private JButton jButSave;
	private JButton jButTest;
	private JCheckBox jCheckFirstRow;
	private JCheckBox jChkColon;
	private JCheckBox jChkDontImport;
	private JCheckBox jChkJoin;
	private JCheckBox jChkOther;
	private JCheckBox jChkSemicolon;
	private JCheckBox jChkSpace;
	private JCheckBox jChkTab;
	private JComboBox jComboDate;
	private JLabel jLabDecimal;
	private JLabel jLabFieldName;
	private JLabel jLabTextDel;
	private JLabel jLabThousand;
	private JLabel jLabelSelFields;
	private JList jListFieldList;
	private JList jListOriginalFields;
	private JPanel jPanApply;
	private JPanel jPanConsole;
	private JPanel jPanFieldList;
	private JPanel jPanFieldName;
	private JPanel jPanFieldSeparators;
	private JPanel jPanFieldSettings;
	private JPanel jPanFieldType;
	private JPanel jPanFields;
	private JPanel jPanFirstRow;
	private JPanel jPanInField;
	private JPanel jPanOutputOpt;
	private JPanel jPanSampleOutput;
	private JPanel jPanSeparators;
	private JPanel jPanSource;
	private JPanel jPanel1;
	private JRadioButton jRadioAlterTable;
	private JRadioButton jRadioCharacter;
	private JRadioButton jRadioDate;
	private JRadioButton jRadioDecimal;
	private JRadioButton jRadioFixed;
	private JRadioButton jRadioInteger;
	private JRadioButton jRadioNewTable;
	private JRadioButton jRadioString;
	private JScrollPane jScrollPane2;
	private JScrollPane jScrollPaneFieldList;
	private JScrollPane jScrollPaneSource;
	private JTable jTableResult;
	private JTable jTableSource;
	private JTextArea jTextAreaConsole;
	private JTextField jTextDecimal;
	private JTextField jTextNumberRows;
	private JLabel jLabelNumberRows;
	private JTextField jTextFieldName;
	private JTextField jTextFieldWidth;
	private JTextField jTextOther;
	private JTextField jTextTextDelimiter;
	private JTextField jTextThousand;

	/**
	 * Creates new form NormalizationPanel to normalize field of datastore
	 * 
	 * @param control
	 * @return panel
	 */
	public NormalizationPanel(NormalizationPanelController control) {
		this.controller = control;
		init();
	}

	/**
	 * This method sets the PluginServices and update the string and icons
	 * 
	 * @param ps
	 *            PluginServices
	 */
	public void setPs(PluginServices ps) {
		this.ps = ps;
		if (ps != null) {
			setMessages();
			setImages();
		}
	}

	/**
	 * This method inserts the sample split strings into the Sample Table
	 * 
	 * @param chains
	 *            array with strings
	 */
	public void insertChainsInRowsTableSample(String[] chains) {

		DefaultTableModel dtm = new DefaultTableModel();
		int nfil = dtm.getRowCount();
		for (int i = 0; i < nfil; i++) {
			dtm.setValueAt(chains[0], i, 0);
		}
		jTableSource.setModel(dtm);
	}

	/**
	 * Insert in the console the message
	 * 
	 * @param e
	 */
	public void stateChanged(ChangeEvent e) {
		String info = parseInfo(e.getSource().toString());
		this.jTextAreaConsole.append(info);
	}

	/**
	 * This method inserts the model in the list with the names of the fields of
	 * the original table
	 * 
	 * @param fieldsNames
	 */
	public void insertFieldsCurrentTable(String[] fieldsNames) {
		DefaultListModel dcb = new DefaultListModel();
		int cont = fieldsNames.length;
		for (int i = 0; i < cont; i++) {
			dcb.addElement(fieldsNames[i]);
		}
		this.jListOriginalFields.setModel(dcb);
		this.jListOriginalFields.setSelectedIndex(0);
	}

	/**
	 * getWindowInfo
	 * 
	 * @return windows properties
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo info = new WindowInfo(WindowInfo.MODALDIALOG
				| WindowInfo.RESIZABLE);
		info.setWidth(780);
		info.setHeight(620);
		info.setTitle(PluginServices.getText(this, "normalize"));
		return info;
	}

	/**
	 * Initialize components and other custom code
	 */
	private void init() {

		/* Initialize components GUI */
		initComponents();

		/* Final initialize methods to set up the GUI */
		updateGUI();

		jListOriginalFields.setEnabled(false);
	}

	/**
	 * This method creates the components of the GUI
	 */
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		butGroupDelimiter = new javax.swing.ButtonGroup();
		butGroupFieldType = new javax.swing.ButtonGroup();
		jPanSampleOutput = new javax.swing.JPanel();
		jScrollPane2 = new javax.swing.JScrollPane();
		jTableResult = new javax.swing.JTable();
		jPanSource = new javax.swing.JPanel();
		jScrollPaneSource = new javax.swing.JScrollPane();
		jTableSource = new javax.swing.JTable();
		jPanFirstRow = new javax.swing.JPanel();
		jCheckFirstRow = new javax.swing.JCheckBox();
		jTextNumberRows = new javax.swing.JTextField();
		jLabelNumberRows = new javax.swing.JLabel();
		jPanFields = new javax.swing.JPanel();
		jPanFieldList = new javax.swing.JPanel();
		jScrollPaneFieldList = new javax.swing.JScrollPane();
		jListFieldList = new javax.swing.JList();
		jButAddField = new javax.swing.JButton();
		jButRemoveField = new javax.swing.JButton();
		jButUpField = new javax.swing.JButton();
		jButDownField = new javax.swing.JButton();
		jPanFieldSettings = new javax.swing.JPanel();
		jPanFieldType = new javax.swing.JPanel();
		jRadioString = new javax.swing.JRadioButton();
		jRadioInteger = new javax.swing.JRadioButton();
		jRadioDecimal = new javax.swing.JRadioButton();
		jRadioDate = new javax.swing.JRadioButton();
		jComboDate = new javax.swing.JComboBox();
		jPanSeparators = new javax.swing.JPanel();
		jPanFieldSeparators = new javax.swing.JPanel();
		jChkTab = new javax.swing.JCheckBox();
		jChkSpace = new javax.swing.JCheckBox();
		jChkColon = new javax.swing.JCheckBox();
		jChkSemicolon = new javax.swing.JCheckBox();
		jChkOther = new javax.swing.JCheckBox();
		jTextOther = new javax.swing.JTextField();
		jChkJoin = new javax.swing.JCheckBox();
		jRadioFixed = new javax.swing.JRadioButton();
		jRadioCharacter = new javax.swing.JRadioButton();
		jTextFieldWidth = new javax.swing.JTextField();
		jPanFieldName = new javax.swing.JPanel();
		jChkDontImport = new javax.swing.JCheckBox();
		jLabFieldName = new javax.swing.JLabel();
		jTextFieldName = new javax.swing.JTextField();
		jPanel1 = new javax.swing.JPanel();
		jPanApply = new javax.swing.JPanel();
		jButTest = new javax.swing.JButton();
		jPanInField = new javax.swing.JPanel();
		jLabDecimal = new javax.swing.JLabel();
		jTextDecimal = new javax.swing.JTextField();
		jLabThousand = new javax.swing.JLabel();
		jTextThousand = new javax.swing.JTextField();
		jLabTextDel = new javax.swing.JLabel();
		jTextTextDelimiter = new javax.swing.JTextField();
		jPanOutputOpt = new javax.swing.JPanel();
		jRadioAlterTable = new javax.swing.JRadioButton();
		jRadioNewTable = new javax.swing.JRadioButton();
		jLabelSelFields = new javax.swing.JLabel();
		jListOriginalFields = new javax.swing.JList();
		jPanConsole = new javax.swing.JPanel();
		jTextAreaConsole = new javax.swing.JTextArea();
		jButLoad = new javax.swing.JButton();
		jButRun = new javax.swing.JButton();
		jButCancel = new javax.swing.JButton();
		jButSave = new javax.swing.JButton();

		setMinimumSize(new java.awt.Dimension(600, 620));
		setPreferredSize(new java.awt.Dimension(600, 620));
		addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				mainPanelGetFocus(evt);
			}
		});
		setLayout(new java.awt.GridBagLayout());

		jPanSampleOutput.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Sample output"));
		jPanSampleOutput.setLayout(new java.awt.GridBagLayout());

		jScrollPane2
				.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		jScrollPane2.setAutoscrolls(true);
		jScrollPane2.setMinimumSize(new java.awt.Dimension(100, 90));
		jScrollPane2.setPreferredSize(new java.awt.Dimension(100, 90));

		jTableResult.setFont(new java.awt.Font("Courier New", 0, 11));
		jTableResult.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] { {}, {}, {} }, new String[] {

				}));
		jTableResult.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTableResult.setEnabled(false);
		jScrollPane2.setViewportView(jTableResult);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		jPanSampleOutput.add(jScrollPane2, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		add(jPanSampleOutput, gridBagConstraints);

		jPanSource.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Source"));
		jPanSource.setMinimumSize(new java.awt.Dimension(36, 125));
		jPanSource.setLayout(new java.awt.GridBagLayout());

		jScrollPaneSource.setMinimumSize(new java.awt.Dimension(100, 90));
		jScrollPaneSource.setPreferredSize(new java.awt.Dimension(100, 90));

		jTableSource.setFont(new java.awt.Font("Courier New", 0, 11));
		jTableSource.setModel(new DefaultTableModel());
		jTableSource.getTableHeader().setReorderingAllowed(false);
		jScrollPaneSource.setViewportView(jTableSource);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		jPanSource.add(jScrollPaneSource, gridBagConstraints);

		jPanFirstRow.setLayout(new java.awt.GridBagLayout());

		jCheckFirstRow.setText("Don't normalize the first row");
		jCheckFirstRow.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		jCheckFirstRow.setSelected(false);
		jCheckFirstRow.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				EvCheckFirstRow(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(3, 10, 0, 0);
		jPanFirstRow.add(jCheckFirstRow, gridBagConstraints);

		jTextNumberRows.setText("0");
		jTextNumberRows.setEditable(false);
		jTextNumberRows.setInputVerifier(new IntVerifier());
		jTextNumberRows.setMaximumSize(new java.awt.Dimension(30, 18));
		jTextNumberRows.setMinimumSize(new java.awt.Dimension(30, 18));
		jTextNumberRows.setPreferredSize(new java.awt.Dimension(30, 18));

		jTextNumberRows.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				firstRowsvalueChange();
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(2, 3, 0, 5);
		jPanFirstRow.add(jTextNumberRows, gridBagConstraints);

		jLabelNumberRows.setText("rows");
		jLabelNumberRows.setEnabled(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
		jPanFirstRow.add(jLabelNumberRows, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		jPanSource.add(jPanFirstRow, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		add(jPanSource, gridBagConstraints);

		jPanFields.setLayout(new java.awt.GridBagLayout());

		jPanFieldList.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Fields"));
		jPanFieldList.setLayout(new java.awt.GridBagLayout());

		jScrollPaneFieldList.setPreferredSize(new java.awt.Dimension(100, 150));

		jListFieldList
				.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jListFieldList.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
				selectedField = jListFieldList.getSelectedIndex();
			}

			public void mouseReleased(MouseEvent e) {
			}
		});
		jListFieldList
				.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
					public void valueChanged(
							javax.swing.event.ListSelectionEvent evt) {
						jListFieldListValueChanged(evt);
					}
				});
		jScrollPaneFieldList.setViewportView(jListFieldList);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridheight = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		jPanFieldList.add(jScrollPaneFieldList, gridBagConstraints);

		jButAddField.setBorderPainted(false);
		jButAddField.setMargin(new java.awt.Insets(5, 5, 5, 5));
		jButAddField.setMaximumSize(new java.awt.Dimension(30, 30));
		jButAddField.setMinimumSize(new java.awt.Dimension(30, 30));
		jButAddField.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ActionButtonAddField(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		jPanFieldList.add(jButAddField, gridBagConstraints);

		jButRemoveField.setBorderPainted(false);
		jButRemoveField.setMargin(new java.awt.Insets(5, 5, 5, 5));
		jButRemoveField.setMaximumSize(new java.awt.Dimension(30, 30));
		jButRemoveField.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ActionButtonRemove(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		jPanFieldList.add(jButRemoveField, gridBagConstraints);

		jButUpField.setForeground(java.awt.SystemColor.activeCaptionBorder);

		jButUpField.setBorderPainted(false);
		jButUpField.setMargin(new java.awt.Insets(5, 5, 5, 5));
		jButUpField.setMaximumSize(new java.awt.Dimension(30, 30));
		jButUpField.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ActionButtonUp(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
		jPanFieldList.add(jButUpField, gridBagConstraints);

		jButDownField.setBorderPainted(false);
		jButDownField.setMargin(new java.awt.Insets(5, 5, 5, 5));
		jButDownField.setMaximumSize(new java.awt.Dimension(30, 30));
		jButDownField.setMinimumSize(new java.awt.Dimension(30, 30));
		jButDownField.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ActionButtonDown(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
		jPanFieldList.add(jButDownField, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		jPanFields.add(jPanFieldList, gridBagConstraints);

		jPanFieldSettings.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Field settings"));
		jPanFieldSettings.setLayout(new java.awt.GridBagLayout());

		jPanFieldType.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Select field type"));
		jPanFieldType.setLayout(new java.awt.GridBagLayout());

		butGroupFieldType.add(jRadioString);
		jRadioString.setSelected(true);
		jRadioString.setText("String");
		jRadioString.setAlignmentY(1.0F);
		jRadioString.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				updateStringType(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		jPanFieldType.add(jRadioString, gridBagConstraints);

		butGroupFieldType.add(jRadioInteger);
		jRadioInteger.setText("Integer");
		jRadioInteger.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				updateIntegerType(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		jPanFieldType.add(jRadioInteger, gridBagConstraints);

		butGroupFieldType.add(jRadioDecimal);
		jRadioDecimal.setText("Decimal");
		jRadioDecimal.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				updateDecimalType(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		jPanFieldType.add(jRadioDecimal, gridBagConstraints);

		butGroupFieldType.add(jRadioDate);
		jRadioDate.setText("Date");
		jRadioDate.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				dateStateChanged(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		jPanFieldType.add(jRadioDate, gridBagConstraints);

		jComboDate.setEditable(true);
		jComboDate.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				"dd/MM/yy", "dd/MM/yyyy", "MM/dd/yyyy", "yyyy/MM/dd",
				"yy/MM/dd" }));
		jComboDate.setEnabled(false);
		jComboDate.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ActionListDateFormat(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		jPanFieldType.add(jComboDate, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridheight = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		jPanFieldSettings.add(jPanFieldType, gridBagConstraints);

		jPanSeparators.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Set how field delimites with next one"));
		jPanSeparators.setRequestFocusEnabled(false);
		jPanSeparators.setLayout(new java.awt.GridBagLayout());

		jPanFieldSeparators.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Select field separators"));
		jPanFieldSeparators.setEnabled(false);
		jPanFieldSeparators.setLayout(new java.awt.GridBagLayout());

		jChkTab.setText("Tab");
		jChkTab.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ActionTab(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanFieldSeparators.add(jChkTab, gridBagConstraints);

		jChkSpace.setText("Space");
		jChkSpace.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ActionSpace(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanFieldSeparators.add(jChkSpace, gridBagConstraints);

		jChkColon.setText("Colon");
		jChkColon.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ActionColon(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanFieldSeparators.add(jChkColon, gridBagConstraints);

		jChkSemicolon.setText("Semicolon");
		jChkSemicolon.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ActionSemicolon(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanFieldSeparators.add(jChkSemicolon, gridBagConstraints);

		jChkOther.setText("Other");
		jChkOther.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				enableOtherText(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanFieldSeparators.add(jChkOther, gridBagConstraints);

		jTextOther.setEditable(false);
		jTextOther.setMinimumSize(new java.awt.Dimension(30, 20));
		jTextOther.setPreferredSize(new java.awt.Dimension(30, 20));
		jTextOther.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ActionTextOther(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanFieldSeparators.add(jTextOther, gridBagConstraints);

		jChkJoin.setText("Join consecutive delimiters ");
		jChkJoin.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ActionJoinSep(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
		jPanFieldSeparators.add(jChkJoin, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weighty = 1.0;
		jPanSeparators.add(jPanFieldSeparators, gridBagConstraints);

		butGroupDelimiter.add(jRadioFixed);
		jRadioFixed.setText("Fixed width");
		jRadioFixed.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				fixedWidthEnable(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		jPanSeparators.add(jRadioFixed, gridBagConstraints);

		butGroupDelimiter.add(jRadioCharacter);
		jRadioCharacter.setSelected(true);
		jRadioCharacter.setText("Character");
		jRadioCharacter.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				charDelimitedEnable(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		jPanSeparators.add(jRadioCharacter, gridBagConstraints);

		jTextFieldWidth.setEditable(false);
		jTextFieldWidth.setInputVerifier(new IntVerifier());
		jTextFieldWidth.setMinimumSize(new java.awt.Dimension(40, 20));
		jTextFieldWidth.setPreferredSize(new java.awt.Dimension(40, 20));
		jTextFieldWidth.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent evt) {
			}

			public void keyTyped(KeyEvent evt) {
			}

			public void keyReleased(KeyEvent e) {
				ActionTextFixedWidth(e);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
		jPanSeparators.add(jTextFieldWidth, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridheight = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
		jPanFieldSettings.add(jPanSeparators, gridBagConstraints);

		jPanFieldName.setLayout(new java.awt.GridBagLayout());

		jChkDontImport.setText("Don't import");
		jChkDontImport.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ActionDontImport(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.insets = new java.awt.Insets(5, 15, 5, 5);
		jPanFieldName.add(jChkDontImport, gridBagConstraints);

		jLabFieldName.setText("Field name:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		jPanFieldName.add(jLabFieldName, gridBagConstraints);

		jTextFieldName.setText("Field1");
		jTextFieldName.setMinimumSize(new java.awt.Dimension(100, 20));
		jTextFieldName.setPreferredSize(new java.awt.Dimension(100, 20));
		jTextFieldName.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				KeyChangesFieldName(e);
			}

			public void keyTyped(KeyEvent e) {
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 15, 5, 5);
		jPanFieldName.add(jTextFieldName, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		jPanFieldSettings.add(jPanFieldName, gridBagConstraints);

		jPanel1.setLayout(new java.awt.GridBagLayout());

		jPanApply.setLayout(new java.awt.GridBagLayout());

		jButSave.setText("Save");
		jButSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButSaveActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		jPanApply.add(jButSave, gridBagConstraints);

		jButLoad.setText("Load");
		jButLoad.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButLoadActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		jPanApply.add(jButLoad, gridBagConstraints);

		jButTest.setText("Test");
		jButTest.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				testNormalizer(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		jPanApply.add(jButTest, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		jPanel1.add(jPanApply, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		jPanFieldSettings.add(jPanel1, gridBagConstraints);

		jPanInField.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Set separators"));
		jPanInField.setLayout(new java.awt.GridBagLayout());

		jLabDecimal.setText("Decimal");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 15);
		jPanInField.add(jLabDecimal, gridBagConstraints);

		jTextDecimal.setText(".");
		jTextDecimal.setInputVerifier(new CharVerifier());
		jTextDecimal.setMinimumSize(new java.awt.Dimension(30, 20));
		jTextDecimal.setPreferredSize(new java.awt.Dimension(30, 20));
		jTextDecimal.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent evt) {
				ActionDecimal(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
		jPanInField.add(jTextDecimal, gridBagConstraints);

		jLabThousand.setText("Thousand");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 15);
		jPanInField.add(jLabThousand, gridBagConstraints);

		jTextThousand.setText(null);
		jTextThousand.setPreferredSize(new java.awt.Dimension(30, 20));
		jTextThousand.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent evt) {
				ActionThousand(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
		jPanInField.add(jTextThousand, gridBagConstraints);

		jLabTextDel.setText("Text:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 15);
		jPanInField.add(jLabTextDel, gridBagConstraints);

		jTextTextDelimiter.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		jTextTextDelimiter.setText("\"");
		jTextTextDelimiter.setPreferredSize(new java.awt.Dimension(30, 20));
		jTextTextDelimiter.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent evt) {
				ActionTextText(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
		jPanInField.add(jTextTextDelimiter, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		jPanFieldSettings.add(jPanInField, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		jPanFields.add(jPanFieldSettings, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		add(jPanFields, gridBagConstraints);

	}

	// *****************************************
	// PANEL EVENTS

	/**
	 * This method runs when check FirstRow is selected or deselected
	 * 
	 * @param evt
	 */
	private void EvCheckFirstRow(java.awt.event.ActionEvent evt) {
		/* DON'T Normalize the first rows */
		if (jCheckFirstRow.isSelected()) {
			jTextNumberRows.setText("1");
			jTextNumberRows.setEditable(true);
			controller
					.setFirstRows(Integer.parseInt(jTextNumberRows.getText()));
			this.jTextNumberRows.requestFocus();
			DefaultTableModel model = controller.getSamplesDataStore(controller.getFirstRows());
			jTableSource.setModel(model);
		}
		/* Normalize the first rows */
		else {
			jTextNumberRows.setText("0");
			jTextNumberRows.setEditable(false);
			controller.setFirstRows(0);
			DefaultTableModel model = controller.getSamplesDataStore(controller.getFirstRows());
			jTableSource.setModel(model);
		}
	}

	/**
	 * This method runs when textField is modify
	 * 
	 */
	private void firstRowsvalueChange() {
		try {
			controller
					.setFirstRows(Integer.parseInt(jTextNumberRows.getText()));
			controller.getSamplesFromFile(controller.getFirstRows());
			jTableSource.setModel(getSourceData());
			jTableSource.validate();
		} catch (Exception e) {
			jTextNumberRows.requestFocus();
		}
	}

	/**
	 * This method runs when check Don't Import is selected or deselected
	 * 
	 * @param evt
	 */
	private void ActionDontImport(java.awt.event.ActionEvent evt) {
		setDirty(true);
	}

	/**
	 * This method runs when combo Date Format has changed
	 * 
	 * @param evt
	 */
	private void ActionListDateFormat(java.awt.event.ActionEvent evt) {
		setDirty(true);
	}

	/**
	 * This method runs when text fixed width has changed
	 * 
	 * @param evt
	 */
	private void ActionTextFixedWidth(java.awt.event.KeyEvent evt) {

		boolean vali = new IntVerifier().verify(jTextFieldWidth);
		if (vali) {
			setDirty(true);
		} else {
			jTextFieldWidth.setText("1");
			setDirty(true);
		}
	}

	/**
	 * This method runs when check TAB is selected or deselected
	 * 
	 * @param evt
	 */
	private void ActionTab(java.awt.event.ActionEvent evt) {
		setDirty(true);
	}

	/**
	 * This method runs when check SPACE is selected or deselected
	 * 
	 * @param evt
	 */
	private void ActionSpace(java.awt.event.ActionEvent evt) {
		setDirty(true);
	}

	/**
	 * This method runs when check COLON is selected or deselected
	 * 
	 * @param evt
	 */
	private void ActionColon(java.awt.event.ActionEvent evt) {
		setDirty(true);
	}

	/**
	 * This method runs when check SEMICOLON is selected or deselected
	 * 
	 * @param evt
	 */
	private void ActionSemicolon(java.awt.event.ActionEvent evt) {
		setDirty(true);
	}

	/**
	 * This method runs when text Other has changed
	 * 
	 * @param evt
	 */
	private void ActionTextOther(java.awt.event.ActionEvent evt) {
		setDirty(true);
	}

	/**
	 * This method runs when text Decimal has changed
	 * 
	 * @param evt
	 */
	private void ActionDecimal(java.awt.event.FocusEvent evt) {
		setDirty(true);
	}

	/**
	 * This method runs when text Thousand has changed
	 * 
	 * @param evt
	 */
	private void ActionThousand(java.awt.event.FocusEvent evt) {
		setDirty(true);
	}

	/**
	 * This method runs when text Text has changed
	 * 
	 * @param evt
	 */
	private void ActionTextText(java.awt.event.FocusEvent evt) {
		setDirty(true);
	}

	/**
	 * This method runs when Field Name has changed
	 * 
	 * @param evt
	 */
	private void KeyChangesFieldName(KeyEvent evt) {
		// log.debug("#Key Pressed Field name# :" + this.selectedField);
		int pos = this.selectedField;
		updateFieldToModel(pos);
		updateGUI();
		setDirty(false);
	}

	/**
	 * 
	 * @param evt
	 */
	private void mainPanelGetFocus(java.awt.event.FocusEvent evt) {
		jTableSource.setModel(getSourceData());
	}

	/**
	 * This method runs when check Join Separators is selected or deselected
	 * 
	 * @param evt
	 */
	private void ActionJoinSep(java.awt.event.ActionEvent evt) {
		setDirty(true);
	}

	/**
	 * This method runs when check fixed width is selected or deselected
	 * 
	 * @param evt
	 */
	private void fixedWidthEnable(java.awt.event.ActionEvent evt) {
		this.jTextFieldWidth.setEditable(true);
		Component[] comps = this.jPanFieldSeparators.getComponents();
		for (int i = 0; i < comps.length; i++) {
			comps[i].setEnabled(false);
		}
		this.jTextFieldWidth.requestFocus();
		setDirty(true);
	}

	/**
	 * This method runs when radio character delimiters is selected or
	 * deselected
	 * 
	 * @param evt
	 */
	private void charDelimitedEnable(java.awt.event.ActionEvent evt) {
		this.jTextFieldWidth.setEditable(false);
		Component[] comps = this.jPanFieldSeparators.getComponents();
		for (int i = 0; i < comps.length; i++) {
			comps[i].setEnabled(true);
		}
		setDirty(true);
	}

	/**
	 * This method runs when radio Field Date is selected or deselected
	 * 
	 * @param evt
	 */
	private void dateStateChanged(java.awt.event.ItemEvent evt) {
		updateSelectors();
		boolean enable = this.jRadioDate.isSelected();
		this.jComboDate.setEnabled(enable);
		if (enable) {
			this.jComboDate.requestFocus();
		}
		setDirty(true);
	}

	/**
	 * This method runs when Testing button is pressed
	 * 
	 * @param evt
	 */
	private void testNormalizer(java.awt.event.ActionEvent evt) {
		/* Save elements */
		if (isDirty()) {
			updateFieldToModel(jListFieldList.getSelectedIndex());
		}
		/* Normalize */
		Object[][] data = this.controller.normalizeSamples();
		String[] nam = this.controller.getNewFieldNames();
		jTableResult.setModel(new DefaultTableModel(data, nam));
	}

	/**
	 * This method runs when check OTHER TEXT is selected or deselected
	 * 
	 * @param evt
	 */
	private void enableOtherText(java.awt.event.ActionEvent evt) {
		this.jTextOther.setEditable(this.jChkOther.isSelected());
		if (this.jChkOther.isSelected()) {
			jTextOther.requestFocus();
		}
		setDirty(true);
	}

	/**
	 * This method runs when Normalization button is pressed
	 * 
	 * @param evt
	 */
	private void runNormalizer(java.awt.event.ActionEvent evt) {

		/* Save the pattern */
		if (isDirty()) {
			updateFieldToModel(this.selectedField);
		}
		/* Get the relate fields of the list */
		if (!isFile) {
			String[] nam = null;
			Object[] names = jListOriginalFields.getSelectedValues();

			/* select all fields */
			if (jListOriginalFields.getSelectedIndex() == 1) {

				nam = this.controller.getFieldNamesMainTable();
			}
			/* fields selected */
			if (jListOriginalFields.getSelectedIndex() != 0
					&& jListOriginalFields.getSelectedIndex() != 1) {

				nam = new String[names.length];
				for (int i = 0; i < names.length; i++) {
					nam[i] = (String) names[i];
				}
			}
			this.controller.setNameRelateFields(nam);
		}
		/* RUN */
		this.controller.runModel(this);

	}

	/**
	 * Update the type
	 * 
	 * @param evt
	 */
	private void updateStringType(java.awt.event.ActionEvent evt) {
		updateSelectors();
		setDirty(true);
	}

	/**
	 * Update the type
	 * 
	 * @param evt
	 */
	private void updateIntegerType(java.awt.event.ActionEvent evt) {
		updateSelectors();
		setDirty(true);
	}

	/**
	 * Update the type
	 * 
	 * @param evt
	 */
	private void updateDecimalType(java.awt.event.ActionEvent evt) {
		updateSelectors();
		setDirty(true);
	}

	/**
	 * This method runs when UP button is pressed
	 * 
	 * @param evt
	 */
	private void ActionButtonUp(java.awt.event.ActionEvent evt) {
		// log.debug("#Up# Old selected field: " + this.selectedField);
		int pos = this.selectedField;
		updateFieldToModel(pos);
		setDirty(false);

		this.controller.fieldUp(pos);
		DefaultListModel dlm = this.updateListModel();
		jListFieldList.setModel(dlm);
		if (pos > 0 && dlm.getSize() > 1) {
			this.selectedField = pos - 1;
			jListFieldList.setSelectedIndex(pos - 1);
		} else {
			this.selectedField = pos;
			jListFieldList.setSelectedIndex(pos);
		}
	}

	/**
	 * This method runs when ADD button is pressed
	 * 
	 * @param evt
	 */
	private void ActionButtonAddField(java.awt.event.ActionEvent evt) {
		// log.debug("#Add# Old selected field: " + this.selectedField);
		int pos = this.selectedField;
		updateFieldToModel(pos);
		setDirty(false);

		controller.addField();
		DefaultListModel dlm = this.updateListModel();
		int siz = dlm.size();
		this.selectedField = siz - 1;
		jListFieldList.setModel(dlm);
		jListFieldList.setSelectedIndex(siz - 1);
		jTextOther.setEditable(false);
	}

	/**
	 * This method runs when REMOVE button is pressed
	 * 
	 * @param evt
	 */
	private void ActionButtonRemove(java.awt.event.ActionEvent evt) {
		int siz = jListFieldList.getModel().getSize();
		// log.debug("#Remove# Old selected field: " + this.selectedField);
		int pos = this.selectedField;
		if (siz > 0) {
			updateFieldToModel(pos);
			setDirty(false);
			controller.deleteField(pos);
			DefaultListModel dlm = this.updateListModel();
			if (pos > 0) {
				this.selectedField = pos - 1;
				jListFieldList.setModel(dlm);
				jListFieldList.setSelectedIndex(pos - 1);
			} else {
				this.selectedField = pos;
				jListFieldList.setModel(dlm);
				jListFieldList.setSelectedIndex(pos);
			}
		}
	}

	/**
	 * This method runs when DOWN button is pressed
	 * 
	 * @param evt
	 */
	private void ActionButtonDown(java.awt.event.ActionEvent evt) {
		// log.debug("#Down# Ols selected field: " + this.selectedField);
		int pos = this.selectedField;
		updateFieldToModel(pos);
		setDirty(false);

		controller.fieldDown(pos);
		DefaultListModel dlm = this.updateListModel();
		jListFieldList.removeAll();
		jListFieldList.setModel(dlm);
		int can = dlm.getSize();
		if (pos < can - 1 && can > 1) {
			this.selectedField = pos + 1;
			jListFieldList.setSelectedIndex(pos + 1);
		} else {
			jListFieldList.setSelectedIndex(pos);
		}
	}

	/**
	 * When press the Load pattern button
	 * 
	 * @param evt
	 */
	private void jButLoadActionPerformed(java.awt.event.ActionEvent evt) {
		// log.debug("#Load# Selected field: " + this.selectedField);
		/* update the model from the view */
		updateFieldToModel(this.selectedField);
		setDirty(false);

		/* load the model from xml file */
		Patternnormalization pat = controller.loadPatternXML();

		/* update the panel */
		if (pat != null) {
			/* save pattern in the model */
			controller.setPattern(pat);

			DefaultListModel dlm = this.updateListModel();
			jListFieldList.setModel(dlm);
			jListFieldList.setSelectedIndex(0);
			jCheckFirstRow.setSelected(controller.getFirstRows() != 0);
			if (jCheckFirstRow.isSelected()) {
				jTextNumberRows.setEditable(true);
				jTextNumberRows.setText(controller.getFirstRows() + "");
				controller.getSamplesFromFile(controller.getFirstRows());
				jTableSource.setModel(getSourceData());
			}
			setDirty(false);
		}

	}

	/**
	 * When change the selected element
	 * 
	 * @param evt
	 */
	private void jListFieldListValueChanged(
			javax.swing.event.ListSelectionEvent evt) {
		// log.debug("1. Fila seleccionada: " + this.selectedField);
		/* Save the parameters in the model */
		boolean dirty = isDirty();
		if (dirty) {
			updateFieldToModel(this.selectedField);
		}
		if (!evt.getValueIsAdjusting()) {
			int pos = this.selectedField;
			updateFieldToGUI(pos);
			jListFieldList.setSelectedIndex(pos);
		}
		setDirty(false);
	}

	/**
	 * When press the Save pattern button
	 * 
	 * @param evt
	 */
	private void jButSaveActionPerformed(java.awt.event.ActionEvent evt) {
		controller.setFirstRows(Integer.parseInt(jTextNumberRows.getText()));
		int pos = this.selectedField;
		updateFieldToModel(pos);
		setDirty(false);
		/* save the pattern */
		controller.savePatternXML();
	}

	/**
	 * Method to update GUI values with the selected column
	 * 
	 * @param id
	 *            column number
	 */
	private void updateFieldToGUI(int id) {

		Element elem = this.controller.getElement(id);

		/* name */
		this.jTextFieldName.setText(elem.getFieldname());

		/* don't import */
		this.jChkDontImport.setSelected(!elem.getImportfield());

		/* Update type */
		Fieldtype type = elem.getFieldtype();

		if (type == null) {
			type = new DefaultFieldtype();
			type.setStringvalue(new DefaultStringvalue());
			elem.setFieldtype(type);
		}

		boolean flag = false;
		if (type.getDatevalue() != null) {
			jRadioDate.setSelected(true);
			String formato = type.getDatevalue().getDatevalueformat();
			DefaultComboBoxModel model = (DefaultComboBoxModel) jComboDate
					.getModel();
			for (int i = 0; i < model.getSize(); i++) {
				String nameformato = (String) model.getElementAt(i);
				if (formato.compareToIgnoreCase(nameformato) == 0) {
					jComboDate.setSelectedIndex(i);
					flag = true;
				}
			}
			if (!flag) {
				model.addElement(formato);
				jComboDate.setModel(model);
				jComboDate.setSelectedItem(formato);
			}
		} else if (type.getStringvalue() != null) {
			jRadioString.setSelected(true);
		} else if (type.getIntegervalue() != null) {
			jRadioInteger.setSelected(true);
		} else if (type.getDecimalvalue() != null) {
			jRadioDecimal.setSelected(true);
		}

		/* Update field separator */
		Fieldseparator seps = elem.getFieldseparator();
		if (seps == null) {
			controller.getDefaultFieldseparators();
		}

		this.jTextFieldWidth.setText(Integer.toString(elem.getFieldwidth()));
		boolean isFixedWidth = elem.getFieldwidth() > 0;

		this.jRadioFixed.setSelected(isFixedWidth);
		this.jRadioCharacter.setSelected(!isFixedWidth);

		if (isFixedWidth) {
			this.fixedWidthEnable(null);
		} else {
			this.charDelimitedEnable(null);
		}

		this.jChkJoin.setSelected(seps.getJoinsep());

		this.jChkTab.setSelected(seps.getTabsep());

		this.jChkSpace.setSelected(seps.getSpacesep());

		this.jChkColon.setSelected(seps.getColonsep());

		this.jChkSemicolon.setSelected(seps.getSemicolonsep());

		boolean other = seps.getOthersep() != null;
		this.jChkOther.setSelected(other);

		if (other) {
			this.jTextOther.setText(seps.getOthersep());
		} else {
			this.jTextOther.setEditable(false);
			this.jTextOther.setText(null);
		}

		/* Update field separator */

		Infieldseparators insep = elem.getInfieldseparators();
		this.jTextDecimal.setText(insep.getDecimalseparator().trim());
		this.jTextThousand.setText(insep.getThousandseparator().trim());
		this.jTextTextDelimiter.setText(insep.getTextseparator().trim());

		updateSelectors();

		setDirty(false);
	}

	/**
	 * Update the field values in the model
	 */
	private void updateFieldToModel(int pos) {

		Element elem = this.controller.getElement(pos);

		/* name */
		elem.setFieldname(this.jTextFieldName.getText());

		/* don't import */
		elem.setImportfield(!this.jChkDontImport.isSelected());

		/* Update type */
		Fieldtype type = new DefaultFieldtype();

		if (jRadioString.isSelected()) {
			Stringvalue strval = new DefaultStringvalue();
			strval.setStringvaluewidth(50);
			type.setStringvalue(strval);
		} else if (jRadioInteger.isSelected()) {
			Integervalue intval = new DefaultIntegervalue();
			intval.setIntegervaluewidth(25);
			type.setIntegervalue(intval);
		} else if (jRadioDecimal.isSelected()) {
			Decimalvalue dec = new DefaultDecimalvalue();
			/* DECIMALES = 10 */
			dec.setDecimalvaluedec(10);
			dec.setDecimalvalueint(25);
			type.setDecimalvalue(dec);
		} else {
			Datevalue date = new DefaultDatevalue();
			String form = jComboDate.getSelectedItem().toString();
			date.setDatevalueformat(form);
			type.setDatevalue(date);
		}
		elem.setFieldtype(type);

		/* Update field separator */
		Fieldseparator seps = elem.getFieldseparator();

		if (elem.getFieldseparator() == null) {
			seps = new DefaultFieldseparator();
		}

		boolean isFixedWidth = this.jRadioFixed.isSelected();
		int val;
		if (isFixedWidth) {
			try {
				val = Integer.parseInt(jTextFieldWidth.getText());
				elem.setFieldwidth(val);
			} catch (Exception e) {
				jTextFieldWidth.requestFocus();
			}

		} else {
			elem.setFieldwidth(0);
		}

		seps.setJoinsep(this.jChkJoin.isSelected());
		seps.setSpacesep(this.jChkSpace.isSelected());
		seps.setColonsep(this.jChkColon.isSelected());
		seps.setSemicolonsep(this.jChkSemicolon.isSelected());
		seps.setTabsep(this.jChkTab.isSelected());

		if (jChkOther.isSelected() && jTextOther.getText().length() == 1) {
			seps.setOthersep(jTextOther.getText().trim());
		} else {
			seps.setOthersep(null);
		}

		/* Update In-field separator */
		Infieldseparators inseps = elem.getInfieldseparators();

		if (elem.getInfieldseparators() == null) {
			inseps = new DefaultInfieldseparators();
		}

		inseps.setThousandseparator(this.jTextThousand.getText().trim());
		inseps.setDecimalseparator(this.jTextDecimal.getText().trim());
		inseps.setTextseparator(this.jTextTextDelimiter.getText().trim());

	}

	

	/**
	 * Update GUI elements with model info
	 */
	private void updateGUI() {

		/* Source Samples values */
		jTableSource.setModel(getSourceData());

		/* Fields List */
		jListFieldList.setModel(this.updateListModel());
		jListFieldList.setSelectedIndex(0);

		/* Fields List in Original Table */
		if (!isFile) {
			DefaultListModel dlmot = controller.getAllOriginalFields();
			jListOriginalFields.setModel(dlmot);
			jListOriginalFields.setSelectedIndex(0);
		}
		/* Disable any controls */
		if (isFile) {
			jTableSource.setName(controller.getFileName());
			jCheckFirstRow.setEnabled(true);
			jLabelNumberRows.setEnabled(true);
			jCheckFirstRow.setSelected(controller.getFirstRows() != 0);
			jTextNumberRows.setText(controller.getFirstRows() + "");
			jRadioAlterTable.setEnabled(false);
			jListOriginalFields.removeAll();
			jRadioNewTable.setSelected(true);
			controller.setInNewTable(true);
			jListOriginalFields.setEnabled(false);
		}
	}

	/**
	 * 
	 * @author jsanz
	 * 
	 */
	class IntVerifier extends InputVerifier {

		public boolean verify(JComponent input) {
			try {
				Integer.parseInt(((JTextField) input).getText());
				return true;
			} catch (NumberFormatException e) {
				log.error("Parsing the value");
				return false;
			}
		}
	}

	/**
	 * 
	 * @author jsanz
	 * 
	 */
	class CharVerifier extends InputVerifier {

		public boolean verify(JComponent input) {
			String text = ((JTextField) input).getText();
			if (text != null && text.length() == 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * Update the in separators
	 * 
	 */
	private void updateSelectors() {
		boolean isDecimal = false;
		boolean isThousand = false;
		boolean isText = false;

		if (this.jRadioString.isSelected()) {
			isText = true;
		} else if (this.jRadioInteger.isSelected()) {
			isThousand = true;
		} else if (this.jRadioDecimal.isSelected()) {
			isThousand = true;
			isDecimal = true;
		} else if (this.jRadioDate.isSelected()) {

		}

		this.jTextDecimal.setEditable(isDecimal);
		this.jTextThousand.setEditable(isThousand);
		this.jTextTextDelimiter.setEditable(isText);
	}

	/**
	 * This method updates the labels
	 */
	private void setMessages() {

		if (this.ps == null)
			return;

		this.setName(ps.getText("normtitle"));
		jPanSource.setBorder(BorderFactory.createTitledBorder(ps
				.getText("normsource")));
		jCheckFirstRow.setText(ps.getText("normfirstrows"));
		jLabelNumberRows.setText(ps.getText("normfirstnumberrows"));

		jPanFieldList.setBorder(BorderFactory.createTitledBorder(ps
				.getText("normfields")));

		jPanFieldSettings.setBorder(BorderFactory.createTitledBorder(ps
				.getText("normfieldsettings")));
		jLabFieldName.setText(ps.getText("normfieldname"));
		jChkDontImport.setText(ps.getText("normdontimport"));

		jPanFieldType.setBorder(BorderFactory.createTitledBorder(ps
				.getText("normfieldtype")));
		jRadioString.setText(ps.getText("normfieldstring"));
		jRadioInteger.setText(ps.getText("normfieldinteger"));
		jRadioDecimal.setText(ps.getText("normfielddecimal"));
		jRadioDate.setText(ps.getText("normfielddate"));

		jPanSeparators.setBorder(BorderFactory.createTitledBorder(ps
				.getText("normdelimetersnext")));
		jRadioCharacter.setText(ps.getText("normcharacter"));
		jRadioFixed.setText(ps.getText("normfixedwidth"));

		jPanFieldSeparators.setBorder(BorderFactory.createTitledBorder(ps
				.getText("normfieldseparators")));
		jChkTab.setText(ps.getText("normtab"));
		jChkSpace.setText(ps.getText("normspace"));
		jChkColon.setText(ps.getText("normcolon"));
		jChkSemicolon.setText(ps.getText("normsemicolon"));
		jChkOther.setText(ps.getText("normother"));
		jChkJoin.setText(ps.getText("normjoin"));

		jPanInField.setBorder(BorderFactory.createTitledBorder(ps
				.getText("norminfieldseparators")));
		jLabDecimal.setText(ps.getText("normdecimal"));
		jLabThousand.setText(ps.getText("normthousand"));
		jLabTextDel.setText(ps.getText("normtextdelimiter"));

		jButTest.setText(ps.getText("normtest"));

		jPanSampleOutput.setBorder(BorderFactory.createTitledBorder(ps
				.getText("normsampleout")));

		jPanOutputOpt.setBorder(BorderFactory.createTitledBorder(ps
				.getText("normoutputopt")));
		jRadioAlterTable.setText(ps.getText("normaltertable"));
		jRadioNewTable.setText(ps.getText("normnewtable"));
		jLabelSelFields.setText(ps.getText("normjoinfield"));

		jPanConsole.setBorder(BorderFactory.createTitledBorder(ps
				.getText("normconsole")));

		jButLoad.setText(ps.getText("normload"));
		jButSave.setText(ps.getText("normsave"));
		jButRun.setText(ps.getText("normrun"));
		jButCancel.setText(ps.getText("normcancel"));
	}

	/**
	 * this method says if the panel has changed any parameter
	 * 
	 * @return the dirty
	 */
	private boolean isDirty() {
		return dirty;
	}

	/**
	 * 
	 * When the panel has changed any parameter (true)
	 * 
	 * @param dirty
	 *            flag
	 */
	private void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	/**
	 * This method updates the images
	 */
	private void setImages() {

		if (this.ps == null)
			return;

		String bDir = ps.getClassLoader().getBaseDir();

		jButUpField.setIcon(new ImageIcon(bDir + File.separator + "images"
				+ File.separator + "icons16" + File.separator + "go-up.png"));
		jButDownField.setIcon(new ImageIcon(bDir + File.separator + "images"
				+ File.separator + "icons16" + File.separator + "go-down.png"));
		jButAddField
				.setIcon(new ImageIcon(bDir + File.separator + "images"
						+ File.separator + "icons16" + File.separator
						+ "list-add.png"));
		jButRemoveField.setIcon(new ImageIcon(bDir + File.separator + "images"
				+ File.separator + "icons16" + File.separator
				+ "list-remove.png"));
	}

	/**
	 * This method parses the info
	 * 
	 * @param info
	 * @return
	 */
	private String parseInfo(String info) {
		String value = "";
		String temp[] = info.split("\\.");

		if (temp[0].compareToIgnoreCase("INFO") == 0) {
			value = "INFO: " + PluginServices.getText(null, temp[1]);
		} else {
			value = "ERROR: " + PluginServices.getText(null, temp[1]);
		}

		if (temp.length == 3) {
			value = value + " " + temp[2];
		}

		return value + "\n";
	}

	/**
	 * getWindowprofile
	 * 
	 * @return
	 */
	public Object getWindowProfile() {
		return null;
	}

	/**
	 * This method updates the model of the names FieldList
	 * 
	 * @return list model
	 */
	private DefaultListModel updateListModel() {

		DefaultListModel dlmodel = new DefaultListModel();
		Element[] adr = controller.getPattern().getArrayElements();
		String name = "";
		for (int i = 0; i < adr.length; i++) {
			name = adr[i].getFieldname();
			dlmodel.add(i, name);
		}
		return dlmodel;
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
