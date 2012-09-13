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
package org.gvsig.rastertools.properties.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.DataBuffer;
import java.io.File;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.fmap.raster.layers.IRasterLayerActions;
import org.gvsig.gui.beans.panelGroup.panels.AbstractPanel;
import org.gvsig.gui.util.StatusComponent;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.hierarchy.IRasterDataset;
import org.gvsig.raster.hierarchy.IRasterProperties;
import org.gvsig.raster.hierarchy.IRasterRendering;
import org.gvsig.rastertools.properties.control.PanSharpeningControl;

import com.iver.andami.PluginServices;

/**
 * Selecciona las bandas visibles en un raster. Contiene una tabla con una fila por cada
 * banda de la imagen. Por medio de checkbox se selecciona para cada RGB que banda de la
 * imagen será visualizada.
 * @author Luis W. Sevilla (sevilla_lui@gva.es)
 * @author Nacho Brodin (brodin_ign@gva.es)
 */

public class PanSharpeningPanel extends AbstractPanel implements TableModelListener {
	final private static long serialVersionUID = 0;

	private String                nom                = "Pansharp";
	private JTable                rgbTable           = null;
	private JScrollPane           bandPane           = null;
	private BandTableModel        tableModel         = null;
	private int                   sizeX              = 445;
	private int                   sizeY              = 174;
	private JPanel                activationPanel    = null;
	private JCheckBox             cbActiveSharpening = null;
	private JPanel                pSharpNorth        = null;
	private JPanel                pSharpSouth        = null;
	private JRadioButton          rbBrovey           = null;
	private JRadioButton          rbHSL              = null;
	private JPanel                pHSL               = null;
	private JSlider               jSlider            = null;
	private JTextField            jTextField         = null;
	private JPanel                pBrovey            = null;
	private JSlider               slBrovey           = null;
	private JTextField            jTextField1        = null;
	private PanSharpeningControl  pansharpControl    = null;
	private IRasterProperties     prop               = null;
	private IRasterDataset        dataset            = null;
	FLayer                        fLayer             = null;
	private final static String[] columnNames        = { " ", "Band" };

	public PanSharpeningPanel()
	{
		super();
		setLabel(PluginServices.getText(this, "pansharp"));
		initialize();
	}


	protected void initialize() {
		this.setPreferredSize(new Dimension(sizeX, sizeY));
		this.setLayout(new FlowLayout());
		this.setLocation(0, 0);
		this.setSize(445, 239);
		this.add(getPSharpNorth(), null);
		this.setTableEnabled(false);
		this.setPreferredSize(new Dimension(100, 80));
	}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.gvsig.raster.gui.properties.dialog.IRegistrablePanel#initializeUI()
		 */
		public void initializeUI() {
		}

		/**
		 * Obtiene el nombre del panel
		 * @return Cadena con el nombre del panel
		 */
		public String getName(){
			return this.nom;
		}

		/**
		 * Activa o desactiva la tabla de selección de banda de refinado
		 * @param enable
		 */
		public void setTableEnabled(boolean enabled){
			this.getRBandPane().setEnabled(enabled);
				this.getBandTable().setEnabled(enabled);
				this.getJSlider().setEnabled(enabled);
				this.getJSlider1().setEnabled(enabled);
				this.getJTextField().setEnabled(enabled);
				this.getJTextField1().setEnabled(enabled);
				this.getRbBrovey().setEnabled(enabled);
				this.getRbHSL().setEnabled(enabled);
		}

		/**
		 * Determina el bandtype de de cada banda y añade la banda a la tabla
		 * */
		public void addFiles(IRasterDataSource mDataset) {
			for (int i = 0; i < mDataset.getDatasetCount(); i++) {
			String fName = mDataset.getDataset(i)[0].getFName();

			String bandName = new File(fName).getName();
			String bandType = "";


			switch (mDataset.getDataset(i)[0].getDataType()[0]) {
			case DataBuffer.TYPE_BYTE:
				bandType = "8U";
				break;
			case DataBuffer.TYPE_INT:
				bandType = "32";
				break;
			case DataBuffer.TYPE_DOUBLE:
				bandType = "64";
				break;
			case DataBuffer.TYPE_FLOAT:
				bandType = "32";
				break;
			case DataBuffer.TYPE_SHORT:
				bandType = "16";
				break;
			case DataBuffer.TYPE_USHORT:
				bandType = "16U";
				break;
			case DataBuffer.TYPE_UNDEFINED:
				bandType = "??";
				break;
		}

			for (int b = 0; b < mDataset.getDataset(i)[0].getBandCount(); b++)
				addBand((b + 1) + " [" + bandType + "] " + bandName);

				}
		}

		// Añade una fila a la tabla
		private void addBand(String bandName) {
		Vector v = new Vector();
		v.add(new Boolean(false));
		v.add(bandName);
		((DefaultTableModel) getBandTable().getModel()).addRow(v);
	}


		public JTable getBandTable() {
				if (rgbTable == null) {
						tableModel = new BandTableModel();
						tableModel.addTableModelListener(this);
						rgbTable = new JTable(tableModel);
						rgbTable.setPreferredScrollableViewportSize(new Dimension(300, 60));
				}
				return rgbTable;
		}

		/**
		 * Traducciones del panel
		 */
		public void setTranslation(){
			this.getCbActiveSharpening().setText(PluginServices.getText(this,"onSharpening"));
		this.getPSharpNorth().setBorder(javax.swing.BorderFactory.createTitledBorder(null, PluginServices.getText(this,"selectBandaRefinado"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		}

		/**
		 * This method initializes jTable
		 *
		 * @return javax.swing.JTable
		 */
		public JScrollPane getRBandPane() {
				if (bandPane == null) {
						bandPane = new JScrollPane(getBandTable());
						bandPane.setPreferredSize(new java.awt.Dimension(400,100));
						bandPane.setEnabled(true);
						TableColumn column = null;

						for (int i = 0; i < 1; i++) {
							 column = rgbTable.getColumnModel().getColumn(i);
							 column.setCellRenderer((TableCellRenderer) new RadioColumnRenderer());
							 column.setCellEditor((TableCellEditor) new RadioColumnEditor());
							 column.setMaxWidth(22);
							 column.setMinWidth(22);
					}
				}
				return bandPane;
		}

		/**
		 * Obtiene el número de bandas de la lista
		 * @return
		 */
		public int getNBands() {
				return ((DefaultTableModel) rgbTable.getModel()).getRowCount();
		}

		/**
		 * Obtiene el nombre de la banda de la posición i de la tabla
		 * @param i
		 * @return
		 */
		public String getBandName(int i) {
				String s = (String) ((DefaultTableModel) rgbTable.getModel()).getValueAt(i, 1);
				return s.substring(s.lastIndexOf("[8U]") + 5, s.length());
		}

		/**
		 * Selecciona una banda de la tabla.
		 * @param i
		 */
		public void selectRow(int i){
			((DefaultTableModel) rgbTable.getModel()).setValueAt(new Boolean(true),i,0);
		}

		/**
		 * Elimina todas las filas seleccionadas
		 */
		public void removeSelections(){
			for(int i=0;i<((DefaultTableModel) rgbTable.getModel()).getRowCount();i++){
				((DefaultTableModel) rgbTable.getModel()).setValueAt(new Boolean(false),i,0);
			}
		}


	public JPanel getPSharpNorth() {
		if (pSharpNorth == null) {
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.gridy = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			pSharpNorth = new JPanel();
			pSharpNorth.setLayout(new GridBagLayout());
			pSharpNorth.setPreferredSize(new java.awt.Dimension(440,259));
			pSharpNorth.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Selector de banda de refinado", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.gridwidth = 0;
			gridBagConstraints2.gridheight = 1;
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 2;
			gridBagConstraints3.weightx = 0.0D;
			gridBagConstraints3.weighty = 0.0D;
			gridBagConstraints3.fill = java.awt.GridBagConstraints.NONE;
			gridBagConstraints3.ipadx = 0;
			gridBagConstraints3.ipady = 0;
			gridBagConstraints3.insets = new java.awt.Insets(0,0,0,0);
			gridBagConstraints3.gridheight = 0;
			gridBagConstraints3.gridwidth = 0;
			pSharpNorth.add(getActivationPanel(), gridBagConstraints2);
			pSharpNorth.add(getRBandPane(), gridBagConstraints3);
			pSharpNorth.add(getPSharpSouth(), gridBagConstraints10);
		}
		return pSharpNorth;
	}

	private JPanel getActivationPanel() {
		if (activationPanel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.LEFT);
			flowLayout.setVgap(0);
			flowLayout.setHgap(0);
			activationPanel = new JPanel();
			activationPanel.setLayout(flowLayout);
			activationPanel.setPreferredSize(new java.awt.Dimension(340,30));
			activationPanel.add(getCbActiveSharpening(), null);
		}
		return activationPanel;
	}

	public JCheckBox getCbActiveSharpening() {
		if (cbActiveSharpening == null) {
			cbActiveSharpening = new JCheckBox();
			cbActiveSharpening.setText("Activar Pansharpening");
			cbActiveSharpening.setPreferredSize(new java.awt.Dimension(150,23));
			cbActiveSharpening.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		}
		return cbActiveSharpening;
	}


	private JPanel getPSharpSouth() {
		if (pSharpSouth == null) {
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			pSharpSouth = new JPanel();
			pSharpSouth.setLayout(new GridBagLayout());
			pSharpSouth.setPreferredSize(new java.awt.Dimension(410,62));
			gridBagConstraints9.gridy = 1;
			gridBagConstraints8.gridy = 0;
			pSharpSouth.add(getJPanel(), gridBagConstraints8);
			ButtonGroup group = new ButtonGroup();
			group.add(getRbBrovey());
			group.add(getRbHSL());
			gridBagConstraints9.ipadx = 0;
			pSharpSouth.add(getPKernel(), gridBagConstraints9);
		}
		return pSharpSouth;
	}
	/**
	 * This method initializes jRadioButton
	 *
	 * @return javax.swing.JRadioButton
	 */
	public JRadioButton getRbBrovey() {
		if (rbBrovey == null) {
			rbBrovey = new JRadioButton();
			rbBrovey.setText("Brovey");
			rbBrovey.setPreferredSize(new java.awt.Dimension(70,23));
			rbBrovey.setSelected(true);
		}
		return rbBrovey;
	}
	/**
	 * This method initializes jRadioButton
	 *
	 * @return javax.swing.JRadioButton
	 */
	public JRadioButton getRbHSL() {
		if (rbHSL == null) {
			rbHSL = new JRadioButton();
			rbHSL.setText("HSL");
			rbHSL.setPreferredSize(new java.awt.Dimension(70,23));
		}
		return rbHSL;
	}
	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPKernel() {
		if (pHSL == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			pHSL = new JPanel();
			pHSL.setLayout(new GridBagLayout());
			pHSL.setPreferredSize(new java.awt.Dimension(400,30));
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.insets = new java.awt.Insets(0,0,0,0);
			gridBagConstraints5.gridheight = 0;
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.gridwidth = 1;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.insets = new java.awt.Insets(0,0,0,0);
			gridBagConstraints6.gridheight = 0;
			gridBagConstraints7.weightx = 1.0D;
			gridBagConstraints7.weighty = 1.0D;
			gridBagConstraints7.gridwidth = 1;
			gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.gridheight = 1;
			gridBagConstraints7.gridy = 0;
			gridBagConstraints7.gridx = 2;
			pHSL.add(getJTextField(), gridBagConstraints7);
			pHSL.add(getRbHSL(), gridBagConstraints5);
			pHSL.add(getJSlider(), gridBagConstraints6);
			this.getJSlider().setValue(0);
		}
		return pHSL;
	}
	/**
	 * This method initializes jSlider
	 *
	 * @return javax.swing.JSlider
	 */
	public JSlider getJSlider() {
		if (jSlider == null) {
			jSlider = new JSlider();
			jSlider.setMinimum(0);
			jSlider.setPreferredSize(new java.awt.Dimension(180,16));
			jSlider.setMaximum(100);

		}
		return jSlider;
	}
	/**
	 * This method initializes jTextField
	 *
	 * @return javax.swing.JTextField
	 */
	public JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
			jTextField.setPreferredSize(new java.awt.Dimension(20,19));
			jTextField.setText("0.0");
		}
		return jTextField;
	}


	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (pBrovey == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridx = 2;
			gridBagConstraints11.gridy = 0;
			gridBagConstraints11.weighty = 1.0D;
			gridBagConstraints11.weightx = 1.0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridheight = 0;
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.gridheight = 0;
			gridBagConstraints.gridx = 0;
			pBrovey = new JPanel();
			pBrovey.setLayout(new GridBagLayout());
			pBrovey.setPreferredSize(new java.awt.Dimension(400,30));
			pBrovey.add(getRbBrovey(), gridBagConstraints);
			pBrovey.add(getJSlider1(), gridBagConstraints1);
			this.getJSlider1().setValue(0);
			pBrovey.add(getJTextField1(), gridBagConstraints11);
		}
		return pBrovey;
	}

	/**
	 * This method initializes jSlider1
	 *
	 * @return javax.swing.JSlider
	 */
	public JSlider getJSlider1() {
		if (slBrovey == null) {
			slBrovey = new JSlider();
			slBrovey.setMaximum(100);
			slBrovey.setPreferredSize(new java.awt.Dimension(180,16));

		}
		return slBrovey;
	}

	/**
	 * This method initializes jTextField1
	 *
	 * @return javax.swing.JTextField
	 */
	public JTextField getJTextField1() {
		if (jTextField1 == null) {
			jTextField1 = new JTextField();
			jTextField1.setPreferredSize(new java.awt.Dimension(20,19));
			jTextField1.setText("0");
		}
		return jTextField1;
	}



	/*
	 * Actualiza por si hay variaciones*/
	private void refresh() {
		setReference(fLayer);
	}

	public void accept() {
		pansharpControl.accept();
	}

	/**
	 * Aplicar los cambios sin guardar su estado
	 */
	public void apply() {
		pansharpControl.apply();
	}

	public void cancel() {
		pansharpControl.cancel();
	}

	public void setComponentSize(int w, int h) {

	}

	public void setReference(Object ref) {
		super.setReference(ref);

		if (!(ref instanceof FLayer))
			return;

		FLayer lyr = (FLayer) ref;

		fLayer = lyr;
		actionEnabled();
		clear();
		//getFileList().clear();

		RasterFilterList rfl = null;

		/*if (lyr instanceof IRasterDataset)
			dataset = (IRasterDataset) lyr;*/
		if (lyr instanceof IRasterProperties)
			rfl = (((IRasterProperties) lyr).getRenderFilterList());

		if (lyr instanceof IRasterProperties)
			prop = (IRasterProperties) lyr;

		if (lyr instanceof IRasterRendering) {
			if (((IRasterRendering) lyr).existColorTable() && pansharpControl != null) {
				pansharpControl.init(null, null, lyr);
				this.setEnabled(false);
				return;
			}
		}

		this.setEnabled(true);

		if (lyr instanceof IRasterDataset) {
			dataset = (IRasterDataset) lyr;
			addFiles(dataset.getDataSource());
		}

		pansharpControl= new PanSharpeningControl(getPanelGroup(), this, (IRasterDataset)dataset,prop, lyr,rfl);

	}

	private void actionEnabled() {
		FLyrRasterSE fLyrRasterSE = ((FLyrRasterSE) fLayer);

		if (!fLyrRasterSE.isActionEnabled(IRasterLayerActions.PANSHARPENING))
			StatusComponent.setDisabled(getPSharpNorth());

		if (!fLyrRasterSE.isActionEnabled(IRasterLayerActions.PANSHARPENING))
			setVisible(false);
		else
			setVisible(true);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.properties.dialog.IRegistrablePanel#getLayerClass()
	 */
	public Class getLayerClass() {
		return FLyrRasterSE.class;
	}

	/**
	 * Elimina todas las entradas de la tabla de bandas.
	 */
	private void clear() {
		int rows = ((DefaultTableModel) getBandTable().getModel()).getRowCount();
		if (rows > 0) {
			for (int i = 0; i < rows; i++)
				((DefaultTableModel) getBandTable().getModel()).removeRow(0);
		}
	}



	 class RadioColumnEditor extends AbstractCellEditor implements TableCellEditor {
			final private static long serialVersionUID = -3370601314380922368L;
			public JRadioButton       theRadioButton;

			public RadioColumnEditor() {
				super();
				theRadioButton = new JRadioButton();
				theRadioButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						fireEditingStopped();
						//setMode(mode);
						//onlyApply();
					}
				});
			}

			public Component getTableCellEditorComponent(JTable table, Object obj, boolean isSelected, int row, int col) {
				theRadioButton.setHorizontalAlignment(SwingUtilities.CENTER);

				Boolean lValueAsBoolean = (Boolean) obj;
				theRadioButton.setSelected(lValueAsBoolean.booleanValue());

				return theRadioButton;
			}

			public Object getCellEditorValue() {
				return new Boolean(theRadioButton.isSelected());
			}
		}

		class RadioColumnRenderer extends JRadioButton implements TableCellRenderer {
			final private static long serialVersionUID = -3370601314380922368L;

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				if (value == null) {
					this.setSelected(false);
				}

				Boolean ValueAsBoolean = (Boolean) value;
				this.setSelected(ValueAsBoolean.booleanValue());
				this.setHorizontalAlignment(SwingConstants.CENTER);

				return this;
			}
		}

			// Modelo de la tabla que contiene las bandas
			class BandTableModel extends DefaultTableModel {
			final private static long serialVersionUID = -3370601314380922368L;
			 // String de con las cabeceras de la tabla

			public BandTableModel() {
				super(new Object[0][2], columnNames);
			}

			public Class getColumnClass(int c) {
				if (c < 1) {
					return Boolean.class;
				}

				return String.class;
			}


			 public void addNew() {
								super.addRow(new Object[] { new Boolean(false), ""});
						}

			 public void addRow(){
				addRow(new Object[] { new Boolean(false), ""});

			 }
			public void setValueAt(Object value, int row, int col) {
				if ((col < 1) && ((Boolean) value).booleanValue()) {
					for (int i = 0; i < getRowCount(); i++) {
						if (i != row) {
							setValueAt(new Boolean(false), i, col);
						}
					}
				}

				super.setValueAt(value, row, col);
			}
		}

		public void tableChanged(TableModelEvent arg0) {

		}

		public void selected() {
			refresh();
		}
}