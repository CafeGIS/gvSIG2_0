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
package org.gvsig.rastertools.histogram.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.gui.beans.defaultbuttonspanel.DefaultButtonsPanel;
import org.gvsig.gui.beans.graphic.GraphicContainer;
import org.gvsig.gui.beans.table.TableContainer;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.hierarchy.IHistogramable;
import org.gvsig.raster.util.MathUtils;
import org.gvsig.rastertools.histogram.HistogramPanelListener;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
/**
 * <code>HistogramPanel</code>. Interfaz de usuario para la representación de
 * histogramas.
 *
 * @version 20/03/2007
 * @author Diego Guerrero (diego.guerrero@uclm.es)
 * @author BorSanZa - Borja Sanchez Zamorano (borja.sanchez@iver.es)
 */
public class HistogramPanel extends DefaultButtonsPanel implements ButtonsPanelListener {
	private static final long serialVersionUID = 2772897994667886753L;

	/**
	 * Objeto que controlara toda la carga pesada del panel
	 */
	private HistogramPanelListener histogramPanelListener = null;

	/**
	 * Array para la seleccion del origen
	 */
	private ArrayList              comboSource            = new ArrayList();

	/**
	 * Variable para controlar si se dispararan los eventos del panel, si es la
	 * primera vez no tiene porque disparar eventos.
	 */
	public boolean                 panelInizialited       = false;

	/**
	 * Componentes graficos
	 */
	private JComboBox              comboBoxSource         = null;
	private JComboBox              comboBoxType           = null;
	private JButton                bTable                 = null;
	private JPanel                 panelNorth             = null;
	private JPanel                 panelSouth             = null;
	private JButton                buttonShowAll          = null;
	private JButton                buttonClean            = null;
	private JTable                 jTableBands            = null;
	private JCheckBox              checkBoxDeleteEdges    = null;
	private JCheckBox              checkBoxRGB            = null;
	private TableContainer         tableContainer         = null;
	private GraphicContainer       graphicContainer       = null;

	/**
	 * Tipo de dato de la petición. Si la petición del histograma es de la vista
	 * el tipo de dato será byte aunque la imagen sea de 16 bits. Si la petición
	 * es de los datos reales de la imagen entonces el tipo de dato de la petición
	 * coincide con el tipo de datos de la imagen.
	 */
	private int                    dataType               = IBuffer.TYPE_BYTE;

	/**
	 * Crea un dialogo para los histogramas.
	 *
	 */
	public HistogramPanel() {
		super(ButtonsPanel.BUTTONS_NONE);
		this.getButtonsPanel().addHideDetails();
		this.getButtonsPanel().addSeeDetails();
		this.getButtonsPanel().getButton(ButtonsPanel.BUTTON_SEEDETAILS).setVisible(false);
		this.getButtonsPanel().getButton(ButtonsPanel.BUTTON_SEEDETAILS).setText(PluginServices.getText(this, "mostrar_estadisticas"));
		this.getButtonsPanel().getButton(ButtonsPanel.BUTTON_HIDEDETAILS).setText(PluginServices.getText(this, "ocultar_estadisticas"));
		this.getButtonsPanel().addClose();
		this.addButtonPressedListener(this);
		histogramPanelListener = getHistogramPanelListener();
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 */
	private void initialize() {
		this.setLayout(new BorderLayout(5, 5));
		this.add(getPanelNorth(), java.awt.BorderLayout.NORTH);
		this.add(getGraphicContainer(), java.awt.BorderLayout.CENTER);
		this.add(getPanelSouth(), java.awt.BorderLayout.SOUTH);
		this.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

		getHistogramPanelListener().setControlListeners();
	}

	public ArrayList getComboSource() {
		return comboSource;
	}

	/**
	 * Asigna una fuente de datos de un histograma. Hay dos tipos de fuentes de
	 * datos, la de los datos visualizados y la del dataset. Ambas deben ser
	 * objetos IHistogramable aunquela primera sea un buffer de datos y la segunda
	 * un Dataset.
	 * @param source Fuente de datos del histograma.
	 * @param name Etiqueta que aparece en el combo para el cambio de fuente de datos
	 */
	private void setHistogramableSource(IHistogramable source, String name) {
		getHistogramPanelListener().eventsEnabled = false;
		ArrayList aux = new ArrayList();
		aux.add(source);
		aux.add(name);
		getComboSource().add(aux);
		updateComboBoxSource();
		getHistogramPanelListener().eventsEnabled = panelInizialited;
	}

	private void updateComboBoxSource() {
		getHistogramPanelListener().eventsEnabled = false;
		getComboBoxSource().removeAllItems();
		for (int i = 0; i < getComboSource().size(); i++) {
			getComboBoxSource().addItem(((ArrayList) getComboSource().get(i)).get(1));
		}
		getHistogramPanelListener().eventsEnabled = panelInizialited;
	}

	/**
	 * Asigna la capa para obtener las fuentes de datos tanto del
	 * datasource como de la visualización.
	 * @param lyr Capa
	 */
	public void setLayer(FLyrRasterSE lyr) throws Exception {
		setHistogramableSource(((FLyrRasterSE) lyr).getRender().getLastRenderBuffer(), PluginServices.getText(this, "datos_visualizados"));
		setHistogramableSource(((FLyrRasterSE) lyr).getDataSource(), PluginServices.getText(this, "imagen_completa"));
		getHistogramPanelListener().setLayer(lyr);
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	public GraphicContainer getGraphicContainer() {
		if (graphicContainer == null) {
			graphicContainer = new GraphicContainer(true);
		}
		return graphicContainer;
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPanelSouth() {
		if (panelSouth == null) {
			panelSouth = new JPanel();
			panelSouth.setLayout(new BorderLayout(5, 5));
			panelSouth.add(getTableContainer(), BorderLayout.CENTER);

			JPanel jPanel2 = new JPanel();
			jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));
			jPanel2.add(getBCreateTable());
			jPanel2.add(getCheckBoxDeleteEdges());
			jPanel2.add(getCheckBoxRGB());
			jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

			panelSouth.add(jPanel2, BorderLayout.EAST);
		}
		return panelSouth;
	}

	public JCheckBox getCheckBoxDeleteEdges() {
		if (checkBoxDeleteEdges == null) {
			checkBoxDeleteEdges = new JCheckBox(PluginServices.getText(this, "eliminar_extremos"));
			checkBoxDeleteEdges.addActionListener(histogramPanelListener);
		}
		return checkBoxDeleteEdges;
	}

	public JCheckBox getCheckBoxRGB() {
		if (checkBoxRGB == null) {
			checkBoxRGB = new JCheckBox(PluginServices.getText(this, "RGB"));
			checkBoxRGB.addActionListener(histogramPanelListener);
		}
		return checkBoxRGB;
	}

	/**
	 * Obtiene la tabla de estadisticas
	 * @return
	 */
	private TableContainer getTableContainer(){
		if (tableContainer == null) {
			String[] columnNames = { PluginServices.getText(this, "banda"), PluginServices.getText(this, "minimo"), PluginServices.getText(this, "maximo"), PluginServices.getText(this, "media"), PluginServices.getText(this, "mediana"), PluginServices.getText(this, "npixeles") };
			int[] columnWidths = { 50, 65, 65, 65, 65, 115 };
			tableContainer = new TableContainer(columnNames, columnWidths);
			tableContainer.setPreferredSize(new Dimension(0, 90));
			tableContainer.setControlVisible(true);
			tableContainer.setModel("ListModel");
			tableContainer.initialize();
			tableContainer.setName("tableContainer");
			tableContainer.setControlVisible(false);

			try {
				tableContainer.setEditable(false);
			} catch (NotInitializeException ex) {
			}
		}
		return tableContainer;
	}

	/**
	 * Devuelve el boton de mostrar todas las bandas
	 * @return JButton
	 */
	public JButton getButtonShowAll() {
		if (buttonShowAll == null) {
			buttonShowAll = new JButton(PluginServices.getText(this, "boton_mostrar"));
			buttonShowAll.addActionListener(getHistogramPanelListener());
		}
		return buttonShowAll;
	}

	/**
	 * Devuelve el boton de limpiar las bandas
	 * @return JButton
	 */
	public JButton getButtonClean() {
		if (buttonClean == null) {
			buttonClean = new JButton(PluginServices.getText(this, "boton_limpiar"));
			buttonClean.addActionListener(getHistogramPanelListener());
		}
		return buttonClean;
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPanelNorth() {
		if (panelNorth == null) {
			panelNorth = new JPanel();
			JScrollPane jScrollPane1 = new JScrollPane();
			JPanel jPanel4 = new JPanel();
			java.awt.GridBagConstraints gridBagConstraints;

			jScrollPane1.setPreferredSize(new java.awt.Dimension(120,65));
			jScrollPane1.setViewportView(getJTableBands());

			panelNorth.setLayout(new BorderLayout());
			panelNorth.add(jPanel4, BorderLayout.CENTER);
			panelNorth.add(jScrollPane1, BorderLayout.EAST);

			jPanel4.setLayout(new java.awt.GridBagLayout());
			JLabel jLabel1 = new JLabel(PluginServices.getText(this, "origen")+":");
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
			jPanel4.add(jLabel1, gridBagConstraints);

			JLabel jLabel2 = new JLabel(PluginServices.getText(this, "tipo")+":");
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
			jPanel4.add(jLabel2, gridBagConstraints);

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
			jPanel4.add(getComboBoxSource(), gridBagConstraints);

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
			jPanel4.add(getComboBoxType(), gridBagConstraints);

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 2;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
			jPanel4.add(getButtonShowAll(), gridBagConstraints);

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 2;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
			jPanel4.add(getButtonClean(), gridBagConstraints);
		}
		return panelNorth;
	}

	/**
	 * Obtiene el combo con la selección de tipo de histograma, acumulado/no acumulado
	 * @return javax.swing.JComboBox
	 */
	public JComboBox getComboBoxType() {
		if (comboBoxType == null) {
			String lista [] = { "normal", "accumulated", "logaritmic" };
			for (int i = 0; i < lista.length; i++)
				lista[i] = PluginServices.getText(this, lista[i]);
			comboBoxType = new JComboBox(lista);
			comboBoxType.addActionListener(getHistogramPanelListener());
			comboBoxType.setPreferredSize(new java.awt.Dimension(150, 25));
		}
		return comboBoxType;
	}

	/**
	 * Obtiene el combo con la selección de la fuente de datos en el calculo del histograma,
	 * datos de la vista, datos reales con el extent de la vista e imagen completa.
	 * @return javax.swing.JComboBox
	 */
	public JComboBox getComboBoxSource() {
		if (comboBoxSource == null) {
			comboBoxSource = new JComboBox();
			comboBoxSource.addActionListener(getHistogramPanelListener());
			comboBoxSource.setPreferredSize(new java.awt.Dimension(150,25));
		}
		return comboBoxSource;
	}

	/**
	 * This method initializes jButton
	 *
	 * @return javax.swing.JButton
	 */
	public JButton getBCreateTable() {
		if (bTable == null) {
			bTable = new JButton();
			bTable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			bTable.setText(PluginServices.getText(this, "crear_tabla"));
			bTable.setName("bTable");
			bTable.addActionListener(getHistogramPanelListener());
			bTable.setPreferredSize(new java.awt.Dimension(150,25));
		}
		return bTable;
	}

	/**
	 * This method initializes jComboBox
	 *
	 * @return javax.swing.JComboBox
	 */
	public JTable getJTableBands() {
		getHistogramPanelListener().eventsEnabled = false;
		if (jTableBands == null) {
			jTableBands = new JTable();
			jTableBands.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { " ", PluginServices.getText(this, "bandas") }) {
				private static final long serialVersionUID = 0L;
				Class[]                   types            = new Class[] { java.lang.Boolean.class, java.lang.String.class };
				boolean[]                 canEdit          = new boolean[] { true, false };

				public Class getColumnClass(int columnIndex) {
					return types[columnIndex];
				}

				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return canEdit[columnIndex];
				}
			});
			jTableBands.addPropertyChangeListener(getHistogramPanelListener());
			jTableBands.getColumnModel().getColumn(0).setPreferredWidth(20);
			jTableBands.getColumnModel().getColumn(0).setResizable(false);
		}
		getHistogramPanelListener().eventsEnabled = panelInizialited;
		return jTableBands;
	}

	/**
	 * Asigna el número de bandas al combobox
	 *
	 * @param bands Número de bandas de la imagen
	 */
	private void setBands(int bands, boolean initValue) {
		getHistogramPanelListener().eventsEnabled = false;

		((DefaultTableModel) getJTableBands().getModel()).setRowCount(0);
		for (int i = 0; i < bands; i++) {
			((DefaultTableModel) getJTableBands().getModel()).addRow(new Object[] { new Boolean(initValue), PluginServices.getText(this, "band") + " " + String.valueOf(i) });
		}
		getHistogramPanelListener().eventsEnabled = panelInizialited;
	}

	/**
	 * Asigna la estadistica a la tabla
	 * @param stat
	 */
	public void setStatistic(double[][] stat){
		try {
			getTableContainer().removeAllRows();
			if (stat == null)
				return;
			for (int iBand = 0; iBand < stat[0].length; iBand++) {
				Object[] list = new Object[stat.length + 1];
				list[0] = new Integer(iBand);
				for (int iStat = 1; iStat <= stat.length; iStat++)
					list[iStat] = new Double(MathUtils.format(new Double(stat[iStat - 1][iBand]).doubleValue(), 3));

				getTableContainer().addRow(list);
				list = null;
			}
		} catch (NotInitializeException e) {
			NotificationManager.addError("Error al eliminar las filas de la tabla", e);
		}
	}

	/**
	 * Resetea el control de bandas del panel con los valores RGB para
	 * cuando se está haciendo el histograma de la visualización en
	 * vez del histograma con los datos
	 *
	 */
	public void setRGBInBandList(boolean initValue){
		getHistogramPanelListener().eventsEnabled = false;

		((DefaultTableModel) getJTableBands().getModel()).setRowCount(0);
		((DefaultTableModel) getJTableBands().getModel()).addRow(new Object[] { new Boolean(initValue), "R"});
		((DefaultTableModel) getJTableBands().getModel()).addRow(new Object[] { new Boolean(initValue), "G"});
		((DefaultTableModel) getJTableBands().getModel()).addRow(new Object[] { new Boolean(initValue), "B"});

		getHistogramPanelListener().eventsEnabled = panelInizialited;
	}

	/**
	 * Obtiene el valor del control izquierdo en el rango 0-100. Los controles dan
	 * un rango en tanto por cien 0-100.
	 * @return Valor del control de la izquierda
	 */
	public double getBoxValueX1() {
		return getGraphicContainer().getX1();
	}

	/**
	 * Obtiene el valor del control derecha en el rango 0-100. Los controles dan
	 * un rango en tanto por cien 0-100.
	 * @return Valor del control de la derecha
	 */
	public double getBoxValueX2() {
		return getGraphicContainer().getX2();
	}

	private HistogramPanelListener getHistogramPanelListener() {
		if (histogramPanelListener == null) {
			histogramPanelListener = new HistogramPanelListener(this);
		}
		return histogramPanelListener;
	}

	public void refreshBands(boolean initValue) {
		if (getHistogramPanelListener().getLastHistogram() == null) {
			setBands(0, initValue);
			return;
		}

		//En caso de que el histograma se monte a partir de los datos de la vista ponemos RGB en el combo
		int bandCount = getHistogramPanelListener().getLastHistogram().getNumBands();
		if ((getComboBoxSource().getSelectedIndex() == 0) && (bandCount == 3)) {
			setRGBInBandList(initValue);
		} else {
			setBands(bandCount, initValue);
		}
	}

	public void firstRun() {
		getHistogramPanelListener().showHistogram();
	}

	/**
	 * Asigna el tipo de dato de la capa
	 * @param dataType
	 */
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	/**
	 * Obtiene el tipo de dato de la capa
	 * @param dataType
	 */
	public int getDataType() {
		return dataType;
	}

	public void actionButtonPressed(ButtonsPanelEvent e) {
		switch (e.getButton()) {
			case ButtonsPanel.BUTTON_HIDEDETAILS:
				this.getButtonsPanel().getButton(ButtonsPanel.BUTTON_HIDEDETAILS).setVisible(false);
				this.getButtonsPanel().getButton(ButtonsPanel.BUTTON_SEEDETAILS).setVisible(true);
				panelSouth.setVisible(false);
				break;
			case ButtonsPanel.BUTTON_SEEDETAILS:
				this.getButtonsPanel().getButton(ButtonsPanel.BUTTON_HIDEDETAILS).setVisible(true);
				this.getButtonsPanel().getButton(ButtonsPanel.BUTTON_SEEDETAILS).setVisible(false);
				panelSouth.setVisible(true);
				break;
		}
	}
}