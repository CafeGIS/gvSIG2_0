package com.iver.ai2.gvsig3d.legend.extrusion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.gvsig.gui.beans.swing.JButton;

import com.iver.ai2.gvsig3d.legend.symbols.BaseExtrusionSymbol;
import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.operations.ClassifiableVectorial;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontext.rendering.legend.AbstractClassifiedVectorLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.ILegend;
import org.gvsig.fmap.mapcontext.rendering.legend.LegendFactory;
import org.gvsig.fmap.mapcontext.rendering.legend.NullUniqueValue;
import org.gvsig.fmap.mapcontext.rendering.legend.NullValue;
import org.gvsig.fmap.mapcontext.rendering.legend.VectorialUniqueValueLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.ZSort;
import org.gvsig.fmap.mapcontext.rendering.legend.events.LegendContentsChangedListener;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;

import com.iver.cit.gvsig.gui.styling.JComboBoxColorScheme;
import com.iver.cit.gvsig.gui.styling.SymbolLevelsWindow;
import com.iver.cit.gvsig.project.documents.view.legend.gui.ILegendPanel;
import com.iver.cit.gvsig.project.documents.view.legend.gui.SymbolTable;
import org.gvsig.fmap.mapcontext.layers.operations.ClassifiableVectorial;

/**
 * DOCUMENT ME!
 * 
 * @author fjp To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExtrusionLegendPanel extends JPanel implements ILegendPanel,
		ActionListener, KeyListener, ItemListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6470946030927461473L;

	// private static Logger logger =
	// Logger.getLogger(VectorialUniqueValue.class.getName());

	private ExtrusionLegend theLegend;
	private ClassifiableVectorial layer;
	private SymbolTableExtrusion symbolTable;
	private JComboBox cmbFields;
	private JButton btnRemoveAll;
	private JButton btnRemove;
	private JCheckBox chbUseDefault = null;
	private ExtrusionLegend auxLegend;
	private JPanel pnlCenter;
	private ZSort zSort;
	private JButton btnOpenSymbolLevelsEditor;
	private JTextField jtvariable;
	private JComboBoxColorScheme cmbColorScheme;
	private double extrusionValue = 1;
	private JTextField jtfija;
	private JCheckBox fixedOption;
	private double extrusionFixedValue = 1;

	private Object aux;

	/**
     *
     */
	public ExtrusionLegendPanel() {
		super();
		initialize();
	}

	/**
	 * DOCUMENT ME!
	 */
	protected void initialize() {
		/*
		 * JLabel label = new JLabel(); label.setIcon(new
		 * javax.swing.ImageIcon(Abrir
		 * .class.getClassLoader().getResource("images/ValoresUnicos.png")));
		 * limagen[1] = new JLabel(); limagen[1] = label;
		 */
		JPanel pnlButtons = new JPanel();

		JButton btnAddAll = new JButton(PluginServices.getText(this,
				"Anadir_todos"));
		btnAddAll.setActionCommand("ADD_ALL_VALUES");
		btnAddAll.addActionListener(this);
		pnlButtons.add(btnAddAll);

		JButton btnAdd = new JButton(PluginServices.getText(this, "Anadir"));
		btnAdd.setActionCommand("ADD_VALUE");
		btnAdd.addActionListener(this);
		pnlButtons.add(btnAdd);

		btnRemoveAll = new JButton(PluginServices.getText(this, "Quitar_todos"));
		btnRemoveAll.setActionCommand("REMOVE_ALL");
		btnRemoveAll.addActionListener(this);
		pnlButtons.add(btnRemoveAll);

		btnRemove = new JButton(PluginServices.getText(this, "Quitar"));
		btnRemove.setActionCommand("REMOVE");
		btnRemove.addActionListener(this);
		pnlButtons.add(btnRemove);

		// btnOpenSymbolLevelsEditor = new JButton(PluginServices.getText(this,
		// "symbol_levels"));
		// btnOpenSymbolLevelsEditor.addActionListener(this);
		// btnOpenSymbolLevelsEditor.setActionCommand("OPEN_SYMBOL_LEVEL_EDITOR")
		// ;
		// pnlButtons.add(btnOpenSymbolLevelsEditor);

		pnlCenter = new JPanel();
		pnlCenter.setLayout(new BorderLayout());

		cmbFields = new JComboBox();
		cmbFields.setActionCommand("FIELD_SELECTED");
		cmbFields.addActionListener(this);
		cmbFields.setVisible(true);

		JPanel pnlNorth = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
		JPanel pAux1 = new JPanel();

		JLabel lblFieldClassification = new JLabel(PluginServices.getText(this,
				"Campo_de_clasificacion"));
		pAux1.add(lblFieldClassification);
		pAux1.add(cmbFields);
		pAux1.add(getChbUseDefault(), null);
		pnlNorth.add(pAux1);

		pAux1 = new JPanel();
		pAux1.add(new JLabel(PluginServices.getText(this, "Nivel_de_extrusion")
				+ ":"));
		jtvariable = new JTextField(PluginServices.getText(this, ""
				+ this.extrusionValue), 3);
		jtvariable.setColumns(3);
		jtvariable.setHorizontalAlignment(JTextField.RIGHT);
		jtvariable.setToolTipText(PluginServices.getText(this, "Valor_inicial")
				+ " * " + PluginServices.getText(this, "Nivel_de_extrusion"));
		jtvariable.addKeyListener(this);
		pAux1.add(jtvariable);

		pnlNorth.add(pAux1);

		pAux1 = new JPanel();
		fixedOption = new JCheckBox(PluginServices.getText(this, "Altura_fija")
				+ ":", false);
		fixedOption.setHorizontalAlignment(JTextField.RIGHT);
		fixedOption.setEnabled(true);
		fixedOption.setSelected(false);
		pAux1.add(fixedOption);

		jtfija = new JTextField(PluginServices.getText(this, "1"), 3);
		jtfija.setColumns(10);
		jtfija.setHorizontalAlignment(JTextField.RIGHT);
		jtfija.addKeyListener(this);
		jtfija.setToolTipText(PluginServices.getText(this, "Valor_inicial")
				+ " = " + PluginServices.getText(this, "Altura_fija"));
		jtfija.setEnabled(false);
		pAux1.add(jtfija);

		pnlNorth.add(pAux1);

		pAux1 = new JPanel();
		pAux1
				.add(new JLabel(PluginServices.getText(this, "color_scheme")
						+ ":"));
		cmbColorScheme = new JComboBoxColorScheme(false);

		// pAux1.add(cmbColorScheme);
		// pnlNorth.add(pAux1);

		fixedOption.addItemListener(this);
		this.setLayout(new BorderLayout());
		this.add(pnlNorth, BorderLayout.NORTH);
		this.add(pnlCenter, BorderLayout.CENTER);
		this.add(pnlButtons, BorderLayout.SOUTH);

	}

	/**
	 * DOCUMENT ME!
	 */
	private void fillTableValues() {
		FeatureStore elRs;
		FeatureSet set = null;
		DisposableIterator iterator = null;
		ISymbol theSymbol = null;
		try {
			elRs = ((FLyrVect) layer).getFeatureStore();
			// logger.debug("elRs.start()");
			// elRs.start();
			//
			// int idField = -1;
			String fieldName = (String) cmbFields.getSelectedItem();
			if (fieldName == null) {
				JOptionPane.showMessageDialog((Component) PluginServices
						.getMainFrame(), PluginServices.getText(this,
						"no_hay_campo_seleccionado"));
				return;
			}

			// idField = elRs.getFieldIndexByName(fieldName);
			theLegend.setClassifyingFieldNames(new String[] { fieldName });

			// long numReg = elRs.getRowCount();
			// if (idField == -1) {
			// NotificationManager.addWarning(PluginServices.getText(this,
			// "unrecognized_field_name")
			// + " " + fieldName, null);
			//
			// return;
			// }

			symbolTable.removeAllItems();

			int numSymbols = 0;
			

			// auxLegend=(VectorialUniqueValueLegend)m_lyr.getLegend();
			// auxLegend =
			// LegendFactory.createVectorialUniqueValueLegend(layer.getShapeType
			// ());
			auxLegend = new ExtrusionLegend(layer.getShapeType());
			auxLegend.setExtrusionFactor(this.extrusionValue);
		
			// Object resul;
			if (chbUseDefault.isSelected()) {
				auxLegend.getDefaultSymbol().setDescription("Default");
				auxLegend.addSymbol(new NullUniqueValue(), auxLegend
						.getDefaultSymbol());
			}

			int r;
			int g;
			int b;
			int stepR;
			int stepG;
			int stepB;

			int interval = 0;
			HashMap<Object, Object> auxMap = new HashMap<Object, Object>();
			
			Object clave = null;

			set = elRs.getFeatureSet();

			int count = 0;

			iterator = set.iterator();
			while (iterator.hasNext()) {
				Feature feature = (Feature) iterator.next();
				clave = feature.get(fieldName);
				// for (int j = 0; j < elRs.getFeatureCount(); j++) {
				// Object clave = elRs.getFieldValue(j, idField);

				if (clave instanceof NullValue) {
					continue;
				}
				if (auxLegend.getSymbolByValue(clave) == null) {
					auxMap.put((Object) clave, null);
				}
			}
			interval = auxMap.size() - 1;
			Color startColor = Color.blue;

			Color endColor = Color.red;

			r = startColor.getRed();
			g = startColor.getGreen();
			b = startColor.getBlue();
			stepR = (endColor.getRed() - r) / interval;
			stepG = (endColor.getGreen() - g) / interval;
			stepB = (endColor.getBlue() - b) / interval;
			HashMap<Object, Color> colorPalete = new HashMap<Object, Color>();
			int cont = 0;

			set = elRs.getFeatureSet();

		

			iterator = set.iterator();
			while (iterator.hasNext()) {
				Feature feature = (Feature) iterator.next();
				clave = feature.get(fieldName);
				if (clave instanceof NullValue) {
					continue;
				}

				// Comprobar que no esta repetido y no hace falta introducir en
				// el hashtable el campo junto con el simbolo.
				if (auxLegend.getSymbolByValue(clave) == null) {
					// si no esta creado el simbolo se crea
					// jaume (moved to ISymbol); theSymbol = new
					// FSymbol(layer.getShapeType());
					// theSymbol = SymbologyFactory.
					// createDefaultSymbolByShapeType(layer.getShapeType(),
					// colorScheme[rand.nextInt(colorScheme.length)].getColor());

					Color co = null;
					if (!(colorPalete.containsKey(clave))) {
						int red, green, blue;
						red = r + (stepR * cont);
						green = g + (stepG * cont);
						blue = b + (stepB * cont);
						co = new Color(red, green, blue);
						colorPalete.put((Object) clave, co);
						cont++;
					} else {
						co = colorPalete.get(clave);
					}

					theSymbol = SymbologyFactory
							.createDefaultSymbolByShapeType(layer
									.getShapeType(), co);

//					aux = clave.producto(ValueFactory
//							.createValue(extrusionValue));
					theSymbol.setDescription(clave.toString());
					auxLegend.addSymbol(clave, theSymbol);

					numSymbols++;

					if (numSymbols == 100) {
						int resp = JOptionPane.showConfirmDialog(this,
								PluginServices.getText(this,
										"mas_de_100_simbolos"), PluginServices
										.getText(this, "quiere_continuar"),
								JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE);

						if ((resp == JOptionPane.NO_OPTION)
								|| (resp == JOptionPane.DEFAULT_OPTION))
							return;
						
					}
				}
			
			}
			// for
		} catch (ReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (iterator != null) {
				iterator.dispose();
			}
			if (set != null) {
				set.dispose();
			}
		}
		Object[] extrudedValue = auxLegend.getValues();

		double defaultValue = 1;

		for (int i = 0; i < extrudedValue.length; i++) {
			if (jtvariable.isEnabled()) {
			
					extrudedValue[i] = ((Object) auxLegend.getValues()[i]);
					theSymbol.setDescription(extrudedValue[i].toString());

				
			} else {}
//				extrudedValue[i] = ValueFactory
//						.createValue(extrusionFixedValue);

		
		// symbolTable.fillTableFromSymbolList(auxLegend.getSymbols(),
		// auxLegend.getValues(), auxLegend.getDescriptions());

		symbolTable.fillTableFromSymbolList(auxLegend.getSymbols(), auxLegend
				.getValues(), extrudedValue);
		// elRs.stop();

		btnRemoveAll.setEnabled(true);
		btnRemove.setEnabled(true);
		}
		// m_bCacheDirty = false;
	}

	/**
	 * A partir de los registros de la tabla, regenera el FRenderer. (No solo el
	 * symbolList, si no tambi�n el arrayKeys y el defaultRenderer
	 */
	private void fillSymbolListFromTable() {
		Object clave;
		ISymbol theSymbol;

		// Borramos las anteriores listas:

		boolean bRestoValores = false; // PONERLO EN UN CHECKBOX
		int hasta;
		String fieldName = (String) cmbFields.getSelectedItem();
		theLegend.setClassifyingFieldNames(new String[] { fieldName });
		// ///////////////////////////////////////PEPE
		FLyrVect m = (FLyrVect) layer;
	
			int fieldType = 0;
			try {
				fieldType = m.getFeatureStore().getDefaultFeatureType()
				.getAttributeDescriptor(cmbFields.getSelectedIndex())
				.getDataType();
			} catch (ReadException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (DataException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			theLegend.setClassifyingFieldTypes(new int[] { fieldType });
	
		// ///////////////////////////////////////PEPE
		if (bRestoValores) {
			hasta = symbolTable.getRowCount() - 1;
		} else {
			hasta = symbolTable.getRowCount();
		}

		for (int row = 0; row < symbolTable.getRowCount(); row++) {
			clave = (Object) symbolTable.getFieldValue(row, 0);
			Double extrusion = null;
			try {
				extrusion = (Double) symbolTable.getFieldValue(row, 1);
				theSymbol = new BaseExtrusionSymbol(extrusion.doubleValue());
				theSymbol.setDescription(extrusion.toString());
			} catch (Exception e) {
				theSymbol = new BaseExtrusionSymbol(1);
				theSymbol.setDescription("1");
			}

			theLegend.addSymbol(clave, theSymbol);
		}

		if (bRestoValores) {
			theSymbol = (ISymbol) symbolTable.getFieldValue(hasta, 0);
			theLegend.setDefaultSymbol(theSymbol);
		}
	}

	/**
	 * DOCUMENT ME!
	 */
	protected void fillFieldNames() {
		FeatureStore rs = null;
		ArrayList nomFields = null;

		
			  try {
				rs = ((FLyrVect) layer).getFeatureStore();
			} catch (ReadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//rs.start();

			nomFields = new ArrayList();

			int type;
			 Iterator iterator = null;
			try {
				iterator = rs.getDefaultFeatureType().iterator();
			} catch (DataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	            while (iterator.hasNext()) {
					FeatureAttributeDescriptor descriptor = (FeatureAttributeDescriptor) iterator.next();
					nomFields.add(descriptor.getName());
				}
			
//			for (int i = 0; i < rs.getFieldCount(); i++) {
//				type = rs.getFieldType(i);
//
//				if (type == Types.NULL) {
//					continue;
//				}
//
//				if ((type == Types.INTEGER) || (type == Types.DOUBLE)
//						|| (type == Types.FLOAT) || (type == Types.BIGINT)) {
//					nomFields.add(rs.getFieldName(i).trim());
//				}
//			}

		//	rs.stop();
	

		DefaultComboBoxModel cM = new DefaultComboBoxModel(nomFields.toArray());
		cmbFields.setModel(cM);

		symbolTable.removeAllItems();
	}

	public void setData(FLayer layer, ILegend legend) {
		this.layer = (ClassifiableVectorial) layer;
		int shapeType = 0;
		
			try {
				shapeType = this.layer.getShapeType();
			} catch (ReadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	

		if (symbolTable != null)
			pnlCenter.remove(symbolTable);
		symbolTable = new SymbolTableExtrusion(this, SymbolTable.VALUES_TYPE,
				shapeType);
		pnlCenter.add(symbolTable, BorderLayout.CENTER);

		fillFieldNames();

		symbolTable.removeAllItems();

		if (legend instanceof VectorialUniqueValueLegend) {
			theLegend = (ExtrusionLegend) legend;
			getChbUseDefault().setSelected(theLegend.isUseDefaultSymbol());
			cmbFields.getModel().setSelectedItem(
					theLegend.getClassifyingFieldNames()[0]);
			symbolTable.fillTableFromSymbolList(theLegend.getSymbols(),
					theLegend.getValues(), theLegend.getDescriptions());
			zSort = theLegend.getZSort();
		} else {
			theLegend = new ExtrusionLegend(shapeType);
		}
		if (theLegend != null) {
			this.extrusionValue = ((ExtrusionLegend) this.theLegend)
					.getExtrusionFactor();
			jtvariable.setText(Double.toString(this.extrusionValue));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.iver.cit.gvsig.gui.legendmanager.panels.ILegendPanel#getLegend()
	 */
	public ILegend getLegend() {
		fillSymbolListFromTable();
		fillTableValues();
		if (auxLegend != null) {
			// your settings that are not the set of symbols must be located
			// here
			auxLegend
					.setClassifyingFieldNames(new String[] { (String) cmbFields
							.getSelectedItem() });
			auxLegend.setClassifyingFieldTypes(theLegend
					.getClassifyingFieldTypes());

			LegendContentsChangedListener[] l = theLegend.getListeners();
			for (int i = 0; i < l.length; i++) {
				auxLegend.addLegendListener(l[i]);
			}
			;

			theLegend = auxLegend;
		}
		theLegend.setZSort(zSort);

		return theLegend;
	}

	private JCheckBox getChbUseDefault() {
		if (chbUseDefault == null) {
			chbUseDefault = new JCheckBox();
			chbUseDefault.setSelected(true);
			chbUseDefault
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							if (auxLegend == null)
								auxLegend = theLegend;
							if (chbUseDefault.isSelected()) {
								addDefault();
							} else {
								delDefault();
							}
						}
					});
			chbUseDefault
					.setText(PluginServices.getText(this, "resto_valores"));
		}

		return chbUseDefault;
	}

	/**
	 * A�ade el resto de valores.
	 */
	private void addDefault() {
		auxLegend.getDefaultSymbol().setDescription("Default");
		auxLegend
				.addSymbol(new NullUniqueValue(), auxLegend.getDefaultSymbol());
		symbolTable.addTableRecord(auxLegend.getDefaultSymbol(),
				new NullUniqueValue(), auxLegend.getDefaultSymbol()
						.getDescription());
		symbolTable.repaint();
	}

	/**
	 * Elimina el resto de valores que no estan representados por ning�n otro
	 * s�mbolo..
	 */
	private void delDefault() {
		auxLegend.delSymbol(new NullUniqueValue());
		symbolTable.removeRow(new NullUniqueValue());
		symbolTable.repaint();
	}

	public void actionPerformed(ActionEvent e) {

		// modificar el combobox de valor
		if (e.getActionCommand() == "FIELD_SELECTED") {
			JComboBox cb = (JComboBox) e.getSource();
			String fieldName = (String) cb.getSelectedItem();
			symbolTable.removeAllItems();

		}

		// add all elements by value
		if (e.getActionCommand() == "ADD_ALL_VALUES") {
			fillTableValues();
		}

		// add only one value
		if (e.getActionCommand() == "ADD_VALUE") {
			try {
				ISymbol symbol = SymbologyFactory.createDefaultSymbolByShapeType(layer.getShapeType());
				Double clave = new Double(0.0);
				symbolTable.addTableRecord(symbol,
						clave,"0 - 0");
				btnOpenSymbolLevelsEditor.setEnabled(true);
    		} catch (ReadException ex) {
    			NotificationManager.addError(PluginServices.getText(this, "getting_shape_type"), ex);
    		}
		}

		// Vacia la tabla
		if (e.getActionCommand() == "REMOVE_ALL") {
			symbolTable.removeAllItems();
			theLegend.setZSort(null);
			zSort = null;
		}

		// Quitar solo el elemento seleccionado
		if (e.getActionCommand() == "REMOVE") {
			symbolTable.removeSelectedRows();
		}

		if (e.getActionCommand() == "OPEN_SYMBOL_LEVEL_EDITOR") {

			if (theLegend != null) {
				ZSort myZSort = ((AbstractClassifiedVectorLegend) getLegend())
						.getZSort();
				if (myZSort == null) {
					myZSort = new ZSort(theLegend);
				}
				SymbolLevelsWindow sl = new SymbolLevelsWindow(myZSort);
				PluginServices.getMDIManager().addWindow(sl);
				zSort = sl.getZSort();
				if (auxLegend != null)
					auxLegend.setZSort(zSort);
			}

		}
	}

	public String getDescription() {
		return PluginServices.getText(this, PluginServices.getText(this,
				"info_extrusion"));
	}

	public ImageIcon getIcon() {
		return null;
	}

	public Class getParentClass() {
		return null;
	}

	public String getTitle() {
		return ("<html><b>" + PluginServices.getText(this, "Extrusion") + "</b></html>");
	}

	public JPanel getPanel() {
		return this;
	}

	public Class getLegendClass() {
		return ExtrusionLegend.class;
	}

	public boolean isSuitableFor(FLayer layer) {

		Layer3DProps props3D = Layer3DProps.getLayer3DProps(layer);
		return ((props3D != null) && (props3D.getType() == Layer3DProps.layer3DVector));
	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyReleased(KeyEvent e) {

		try {
			extrusionValue = new Double(this.jtvariable.getText())
					.doubleValue();
			extrusionFixedValue = new Double(this.jtfija.getText())
					.doubleValue();
		} catch (Exception e2) {
			// String s = " ";
			// if(!this.jtvariable.getText().equals(s)) {
			// JOptionPane.showMessageDialog(null, PluginServices.getText(this,
			// "Introduce un dato numerico"),
			// PluginServices.getText(this, "Dato incorrecto"),
			// JOptionPane.WARNING_MESSAGE);
			//	
			// jtvariable.setText(Double.toString(extrusionValue));
			// jtfija.setText(Double.toString(extrusionFixedValue));
			// }
		}
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void itemStateChanged(ItemEvent e) {
		jtfija.setEnabled(fixedOption.isSelected());
		jtvariable.setEnabled(!fixedOption.isSelected());

	}

}
