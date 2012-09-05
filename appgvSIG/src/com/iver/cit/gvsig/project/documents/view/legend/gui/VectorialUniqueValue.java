/* gvSIG. Sistema de Informaciï¿½n Geogrï¿½fica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibï¿½ï¿½ez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package com.iver.cit.gvsig.project.documents.view.legend.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.gvsig.gui.beans.swing.GridBagLayoutPanel;
import org.gvsig.gui.beans.swing.JBlank;
import org.gvsig.gui.beans.swing.JButton;
import org.gvsig.raster.datastruct.ColorItem;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.gui.styling.JComboBoxColorScheme;
import com.iver.cit.gvsig.gui.styling.SymbolLevelsWindow;
import com.iver.cit.gvsig.project.documents.view.toc.actions.ChangeSymbolTocMenuEntry;
import com.iver.utiles.XMLException;


/**
 * DOCUMENT ME!
 *
 * @author fjp To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 *         Comments
 */
public class VectorialUniqueValue extends JPanel implements ILegendPanel, ActionListener{
    private static final Logger logger = LoggerFactory
    .getLogger(VectorialUniqueValue.class);

    protected VectorialUniqueValueLegend theLegend;
    private ClassifiableVectorial layer;
    private SymbolTable symbolTable;
    protected JComboBox cmbFields;
    private JButton btnRemoveAll;
    private JButton btnRemove;
    private JCheckBox chbUseDefault = null;
    private JSymbolPreviewButton defaultSymbolPrev;
    private VectorialUniqueValueLegend auxLegend;
	private JPanel pnlCenter;
	private JButton btnOpenSymbolLevelsEditor;

	private JComboBoxColorScheme cmbColorScheme;
	private GridBagLayoutPanel defaultSymbolPanel = new GridBagLayoutPanel();
    /**
     *
     */
    public VectorialUniqueValue() {
        super();
        initComponents();
    }

    /**
     * DOCUMENT ME!
     */
    protected void initComponents() {
        /* JLabel label = new JLabel();
           label.setIcon(new javax.swing.ImageIcon(Abrir.class.getClassLoader()
                                                                                                              .getResource("images/ValoresUnicos.png")));
           limagen[1] = new JLabel();
           limagen[1] = label; */
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

        btnOpenSymbolLevelsEditor = new JButton(PluginServices.getText(this, "symbol_levels"));
        btnOpenSymbolLevelsEditor.addActionListener(this);
        btnOpenSymbolLevelsEditor.setActionCommand("OPEN_SYMBOL_LEVEL_EDITOR");
        pnlButtons.add(btnOpenSymbolLevelsEditor);
		btnOpenSymbolLevelsEditor.setEnabled(symbolTable != null && symbolTable.getRowCount()>0);

		pnlCenter = new JPanel();
        pnlCenter.setLayout(new BorderLayout());

        cmbFields = new JComboBox();
        cmbFields.setActionCommand("FIELD_SELECTED");
        cmbFields.addActionListener(this);
        cmbFields.setVisible(true);

		JPanel pnlNorth = new JPanel();
		pnlNorth.setLayout(new GridLayout(0,2));

		GridBagLayoutPanel auxPanel = new GridBagLayoutPanel();
		JLabel lblFieldClassification = new JLabel(PluginServices.getText(
				this, "Campo_de_clasificacion")+": ");
		auxPanel.add(lblFieldClassification);
		auxPanel.add(cmbFields);
		pnlNorth.add(auxPanel);

		auxPanel = new GridBagLayoutPanel();
		auxPanel.add(new JLabel(PluginServices.getText(this, "color_scheme")+": "));
		cmbColorScheme = new JComboBoxColorScheme(false);
		cmbColorScheme.addActionListener(this);
		auxPanel.add(cmbColorScheme);
		pnlNorth.add(auxPanel);


		defaultSymbolPanel.add(getChbUseDefault(), null);
		pnlNorth.add(defaultSymbolPanel);
		pnlNorth.add(new JBlank(0,30));

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

        try {
            elRs = ((FLyrVect) layer).getFeatureStore();
//            logger.debug("elRs.start()");
//            elRs.start();

//            int idField = -1;
            String fieldName = (String) cmbFields.getSelectedItem();
            if (fieldName==null) {
            	JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),PluginServices.getText(this,"no_hay_campo_seleccionado"));
            	return;
            }

//            idField = elRs.getFieldIndexByName(fieldName);
            auxLegend.setClassifyingFieldNames(new String[] {fieldName});

            //long numReg = elRs.getRowCount();
//            if (idField == -1) {
//                NotificationManager.addWarning(
//                		PluginServices.getText(this, "unrecognized_field_name")+" " + fieldName, null);
//
//                return;
//            }

            symbolTable.removeAllItems();

            int numSymbols = 0;
            ISymbol theSymbol = null;

            //auxLegend=(VectorialUniqueValueLegend)m_lyr.getLegend();
            auxLegend = LegendFactory.createVectorialUniqueValueLegend(layer.getShapeType());

            Object clave=null;

            //Object resul;
            if (chbUseDefault.isSelected()) {
                auxLegend.getDefaultSymbol().setDescription("Default");
                auxLegend.addSymbol(new NullUniqueValue(),
                    auxLegend.getDefaultSymbol());
            }

            ColorItem[] colorScheme = cmbColorScheme.getSelectedColors();

            Color[] colors = new Color[colorScheme.length];
			for (int i = 0; i < colorScheme.length; i++) {
				colors[i] = colorScheme[i].getColor();
			}
			auxLegend.setColorScheme(colors);

            Random rand = new Random(System.currentTimeMillis());

            FeatureSet set = null;
			DisposableIterator iterator = null;

			try {
				set = elRs.getFeatureSet();

				int count = 0;

				iterator = set.iterator();
				while (iterator.hasNext()) {
					Feature feature = (Feature) iterator.next();
					clave = feature.get(fieldName);
					// }
					// for (int j = 0; j < elRs.getRowCount(); j++) {
					// clave = elRs.getFieldValue(j, idField);

					if (clave instanceof NullValue) {
						continue;
					}

					// //Comprobar que no esta repetido y no hace falta
					// introducir en el hashtable el campo junto con el simbolo.
					if (auxLegend.getSymbolByValue(clave) == null) {

						if (count == 100) {
							int resp = JOptionPane.showConfirmDialog(this,
									PluginServices.getText(this,
											"mas_de_100_simbolos"),
									PluginServices.getText(this,
											"quiere_continuar"),
									JOptionPane.YES_NO_OPTION,
									JOptionPane.WARNING_MESSAGE);

							if ((resp == JOptionPane.NO_OPTION)
									|| (resp == JOptionPane.DEFAULT_OPTION)) {
								break;
							}
						}


						// si no esta creado el simbolo se crea
						// jaume (moved to ISymbol); theSymbol = new
						// FSymbol(layer.getShapeType());
						theSymbol = SymbologyFactory
								.createDefaultSymbolByShapeType(layer
										.getShapeType(), colorScheme[rand
										.nextInt(colorScheme.length)]
										.getColor());
						theSymbol.setDescription(clave.toString());
						auxLegend.addSymbol(clave, theSymbol);
						count++;
					}

				} // for
			}finally{
				if (iterator != null){
					iterator.dispose();
				}
				if (set != null) {
					set.dispose();
				}
			}

            symbolTable.fillTableFromSymbolList(auxLegend.getSymbols(),
                auxLegend.getValues(), auxLegend.getDescriptions());
//            elRs.stop();
//            set.dispose();
        } catch (DataException e) {
        	NotificationManager.addError(PluginServices.getText(this, "recovering_recordset"), e);
        }

        btnRemoveAll.setEnabled(true);
        btnRemove.setEnabled(true);

        //m_bCacheDirty = false;
    }

	private boolean compareClassifyingFieldNames(String[] a, String[] b){
		if (a==b) {
			return true;
		}
		if (a == null || b == null) {
			return false;
		}
		if (a.length != b.length) {
			return false;
		}
		for (int i=0; i<a.length; i++){
			if (!a[i].equals(b[i])) {
				return false;
			}
		}
		return true;
	}

	private boolean compareClassifyingFieldTypes(int[] a, int[] b){
		if (a==b) {
			return true;
		}
		if (a == null || b == null) {
			return false;
		}
		if (a.length != b.length) {
			return false;
		}
		for (int i=0; i<a.length; i++){
			if (a[i]!=b[i]) {
				return false;
			}
		}
		return true;
	}

    /**
     * A partir de los registros de la tabla, regenera el FRenderer. (No solo
     * el symbolList, si no tambiï¿½n el arrayKeys y el defaultRenderer
     */
    private void fillSymbolListFromTable() {
        Object clave=null;
        ISymbol theSymbol;
		ArrayList visitedKeys = new ArrayList();
		boolean changedLegend = false;

    	String fieldName = (String) cmbFields.getSelectedItem();
		String[] classifyingFieldNames = new String[] {fieldName};
		if(auxLegend!=null){
			if(!compareClassifyingFieldNames(classifyingFieldNames,auxLegend.getClassifyingFieldNames())){
				auxLegend.setClassifyingFieldNames(classifyingFieldNames);
				changedLegend = true;
			}
		} else {
			auxLegend.setClassifyingFieldNames(classifyingFieldNames);
			changedLegend = true;
		}

		FLyrVect m = (FLyrVect) layer;

        try {
        	int fieldType = m.getFeatureStore().getDefaultFeatureType()
					.getAttributeDescriptor(cmbFields.getSelectedIndex())
					.getDataType();
//        	int fieldType = m.getSource().getRecordset().getFieldType((int)cmbFields.getSelectedIndex());
			int[] classifyingFieldTypes = new int[] {fieldType};
			if(auxLegend!=null){
				if(!compareClassifyingFieldTypes(classifyingFieldTypes,auxLegend.getClassifyingFieldTypes())){
					auxLegend.setClassifyingFieldTypes(classifyingFieldTypes);
					changedLegend = true;
				}
			} else {
				auxLegend.setClassifyingFieldTypes(classifyingFieldTypes);
				changedLegend = true;
			}
        } catch (DataException e) {
        	NotificationManager.addError(PluginServices.getText(this, "could_not_setup_legend"), e);
        }

		if(changedLegend){
			auxLegend.clear();
		}

        for (int row = 0; row < symbolTable.getRowCount(); row++) {
            clave = symbolTable.getFieldValue(row, 1);
            theSymbol = (ISymbol) symbolTable.getFieldValue(row, 0);
			String description = (String) symbolTable.getFieldValue(row, 2);
			theSymbol.setDescription(description);
			ISymbol legendSymbol = null;
			if (auxLegend != null){
				legendSymbol = auxLegend.getSymbolByValue(clave);
			}
			if( legendSymbol == null || ( auxLegend.isUseDefaultSymbol() && legendSymbol == auxLegend.getDefaultSymbol())){
				if (auxLegend != null){
					auxLegend.addSymbol(clave, theSymbol);
				}
			} else {
				/* FIXME: Se optimizaría descomentarizando el if, pero el metodo equals del AbstractSymbol
				 * no tiene en cuenta determinadas propiedades del simbolo, como, por ejemplo, el tamaño.
				 * Descomentarizar al arreglar el metodo equals del AbstractSymbol.
				 */
//				if(!legendSymbol.equals(theSymbol)){
					auxLegend.replace(legendSymbol, theSymbol);
//				}
			}
			visitedKeys.add(clave);
		}
		if(auxLegend != null){
			Object[] keys = auxLegend.getValues();
			for(int i=0; i<keys.length; i++){
				Object key = keys[i];
				if(!visitedKeys.contains(key)){
					auxLegend.delSymbol(key);
				}
			}
		}
		clave = new NullUniqueValue();
		if(chbUseDefault.isSelected()){
			theSymbol = defaultSymbolPrev.getSymbol();
			if(theSymbol != null){
				String description = PluginServices.getText(this,"default");
				theSymbol.setDescription(description);
				ISymbol legendSymbol = null;
				if (auxLegend != null){
					legendSymbol = auxLegend.getSymbolByValue(clave);
				}
				if( legendSymbol == null){
					auxLegend.addSymbol(clave, theSymbol);
				} else {
//					if(!legendSymbol.equals(theSymbol)){
					if(legendSymbol!=theSymbol){
						auxLegend.replace(legendSymbol, theSymbol);
					}
				}
			}
		} else {
			if (auxLegend != null){
				ISymbol legendSymbol = auxLegend.getSymbolByValue(clave);
				if( legendSymbol != null){
					auxLegend.replace(legendSymbol, null);
				}
			}
		}
	}

    /**
     * DOCUMENT ME!
     */
    private void fillFieldNames() {
        FeatureStore rs;

        try {
            rs = ((FLyrVect) layer).getFeatureStore();
//            logger.debug("rs.start()");
//            rs.start();

            ArrayList names=new ArrayList();
            Iterator iterator=rs.getDefaultFeatureType().iterator();
            while (iterator.hasNext()) {
				FeatureAttributeDescriptor descriptor = (FeatureAttributeDescriptor) iterator.next();
				names.add(descriptor.getName());
			}
//            String[] nomFields = new String[rs.getFieldCount()];
//
//            for (int i = 0; i < rs.getFieldCount(); i++) {
//                nomFields[i] = rs.getFieldName(i).trim();
//            }
//
//            rs.stop();

            DefaultComboBoxModel cM = new DefaultComboBoxModel(names.toArray(new String[0]));
            cmbFields.setModel(cM);

            // fieldsListValor.setSelectedIndex(0);
        } catch (DataException e) {
        	NotificationManager.addError(PluginServices.getText(this, "recovering_recordset"), e);
        }
    }

    public void setData(FLayer layer, ILegend legend) {
    	this.layer = (ClassifiableVectorial) layer;
      	int shapeType = 0;
      	try {
      		shapeType = this.layer.getShapeType();
      	} catch (ReadException e) {
    		NotificationManager.addError(PluginServices.getText(this, "generating_intervals"), e);
		}

      	getDefaultSymbolPrev(shapeType);

      	if (symbolTable != null) {
			pnlCenter.remove(symbolTable);
		}
      	symbolTable = new SymbolTable(this, SymbolTable.VALUES_TYPE, shapeType);
      	pnlCenter.add(symbolTable, BorderLayout.CENTER);

        fillFieldNames();

        symbolTable.removeAllItems();

    	if (VectorialUniqueValueLegend.class.equals(legend.getClass())) {
			try {
				auxLegend = (VectorialUniqueValueLegend) legend.cloneLegend();
			} catch (XMLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			getChbUseDefault().setSelected(auxLegend.isUseDefaultSymbol());
			cmbFields.getModel().setSelectedItem(auxLegend.getClassifyingFieldNames()[0]);
			setColorScheme();
			symbolTable.fillTableFromSymbolList(auxLegend.getSymbols(),
					auxLegend.getValues(),auxLegend.getDescriptions());
			chbUseDefault.setSelected(auxLegend.isUseDefaultSymbol());
		} else {
			auxLegend = new VectorialUniqueValueLegend(shapeType);
		}
		defaultSymbolPrev.setSymbol(auxLegend.getDefaultSymbol());
		btnOpenSymbolLevelsEditor.setEnabled(symbolTable != null && symbolTable.getRowCount()>0);
    }


    private void setColorScheme(){

		if(auxLegend.getColorScheme() != null) {
			ColorItem[] colors = new ColorItem[auxLegend.getColorScheme().length];
			for (int i = 0; i < auxLegend.getColorScheme().length; i++) {
				colors[i] = new ColorItem();
				colors[i].setColor(auxLegend.getColorScheme()[i]);
			}
			cmbColorScheme.setSelectedColors(colors);
		}
	}

    private void getDefaultSymbolPrev(int shapeType) {
		if(defaultSymbolPrev == null){
			defaultSymbolPrev = new JSymbolPreviewButton(shapeType);
			defaultSymbolPrev.setPreferredSize(new Dimension(110,20));
			defaultSymbolPrev.addActionListener(this);
			defaultSymbolPanel.add(defaultSymbolPrev,null);
		}
	}

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.legendmanager.panels.ILegendPanel#getLegend()
     */
    public ILegend getLegend() {
    	fillSymbolListFromTable();

    	if (auxLegend != null) {
    		// your settings that are not the set of symbols must be located here
//  		auxLegend.setClassifyingFieldNames(
//  		new String[] {(String) cmbFields.getSelectedItem()});

    		ISymbol defaultSymbolLegend = auxLegend.getDefaultSymbol();
    		ISymbol symbol = defaultSymbolPrev.getSymbol();
    		if(symbol != null){
    			if(symbol!=defaultSymbolLegend){
    				auxLegend.setDefaultSymbol(symbol);
    			}
    		}

    		auxLegend.useDefaultSymbol(chbUseDefault.isSelected());

    		try {
    			theLegend = (VectorialUniqueValueLegend) auxLegend.cloneLegend();
    		} catch (XMLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		theLegend.setZSort(auxLegend.getZSort());
    	}

    	return theLegend;
    }

    private JCheckBox getChbUseDefault() {
    	if (chbUseDefault == null) {
			chbUseDefault = new JCheckBox();
			chbUseDefault.setSelected(false);
			chbUseDefault.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (chbUseDefault.isSelected()) {
						auxLegend.useDefaultSymbol(true);
					} else {
						auxLegend.useDefaultSymbol(false);
					}
				}
			});
			chbUseDefault.setText(PluginServices.getText(this, "resto_valores")+ ": ");
		}

		return chbUseDefault;
    }

    /**
     * Aï¿½ade el resto de valores.
     */
    private void addDefault() {
        auxLegend.getDefaultSymbol().setDescription("Default");
        auxLegend.addSymbol(new NullUniqueValue(), auxLegend.getDefaultSymbol());
        symbolTable.addTableRecord(auxLegend.getDefaultSymbol(),
            new NullUniqueValue(), auxLegend.getDefaultSymbol().getDescription());
        symbolTable.repaint();
    }

    /**
     * Elimina el resto de valores que no estan representados por ningï¿½n otro sï¿½mbolo..
     */
    private void delDefault() {
        auxLegend.delSymbol(new NullUniqueValue());
        symbolTable.removeRow(new NullUniqueValue());
        symbolTable.repaint();
    }


    public void actionPerformed(ActionEvent e) {

    	//modificar el combobox de valor
    	if (e.getActionCommand() == "FIELD_SELECTED") {
    		JComboBox cb = (JComboBox) e.getSource();
    		String fieldName = (String) cb.getSelectedItem();
    		symbolTable.removeAllItems();
			btnOpenSymbolLevelsEditor.setEnabled(false);


//    		if (theLegend.getClassifyingFieldNames()!=null && fieldName != theLegend.getClassifyingFieldNames()[0]) {
//    			//m_bCacheDirty = true;
//    			theLegend.setClassifyingFieldNames(new String[] {fieldName});
//    		}
//
//    		//////////////////////////////////////////PEPE
//    		FLyrVect m = (FLyrVect) layer;
//    		try {
//    			int fieldType = m.getSource().getRecordset().getFieldType((int)cb.getSelectedIndex());
//    			if (theLegend.getClassifyingFieldTypes()!=null && fieldType != theLegend.getClassifyingFieldTypes()[0]) {
//    				//m_bCacheDirty = true;
//    				theLegend.setClassifyingFieldTypes(new int[] {fieldType});
//    			}
//    		} catch (ReadDriverException e1) {
//    			NotificationManager.addError(PluginServices.getText(this, "could_not_setup_legend"), e1);
//    		}
//    		/////////////////////////////////////////PEPE
    	}

    	// add all elements by value
    	if (e.getActionCommand() == "ADD_ALL_VALUES") {
    		fillTableValues();
			btnOpenSymbolLevelsEditor.setEnabled(symbolTable != null && symbolTable.getRowCount()>0);
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

    	//Vacia la tabla
    	if (e.getActionCommand() == "REMOVE_ALL") {
    		symbolTable.removeAllItems();
    		auxLegend.setZSort(null);
			btnOpenSymbolLevelsEditor.setEnabled(false);

    	}

    	//Quitar solo el elemento seleccionado
    	if (e.getActionCommand() == "REMOVE") {
    		symbolTable.removeSelectedRows();
			btnOpenSymbolLevelsEditor.setEnabled(symbolTable.getRowCount()>0);
    	}

    	if (e.getActionCommand() == "OPEN_SYMBOL_LEVEL_EDITOR") {
			ZSort myZSort = null;
			if (auxLegend != null) {
				myZSort = ((AbstractClassifiedVectorLegend) getLegend()).getZSort();
				if(myZSort == null){
					myZSort = new ZSort(auxLegend);
				}
			}
			if (myZSort == null && theLegend != null) {
				myZSort = new ZSort(theLegend);
			}
			SymbolLevelsWindow sl = new SymbolLevelsWindow(myZSort);
			PluginServices.getMDIManager().addWindow(sl);
			auxLegend.setZSort(sl.getZSort());
		}
    }

	public String getDescription() {
		return PluginServices.getText(this,"Dado_un_campo_de_atributos") + "," + PluginServices.getText(this,"muestra_los_elementos_de_la_capa_usando_un_simbolo_por_cada_valor_unico") + ".";
	}

	public ImageIcon getIcon() {
		return new ImageIcon(this.getClass().getClassLoader().
				getResource("images/ValoresUnicos.png"));
	}

	public Class getParentClass() {
		return Categories.class;
	}

	public String getTitle() {
		return PluginServices.getText(this,"Valores_unicos");
	}

	public JPanel getPanel() {
		return this;
	}

	public Class getLegendClass() {
		return VectorialUniqueValueLegend.class;
	}


	public boolean isSuitableFor(FLayer layer) {
		return (layer instanceof FLyrVect);
	}
}
