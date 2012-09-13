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
package org.gvsig.georeferencing.ui.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.gvsig.georeferencing.main.ApplicationControlsListener;
import org.gvsig.georeferencing.main.Georeferencing;
import org.gvsig.georeferencing.process.geotransform.GeoTransformDataResult;
import org.gvsig.georeferencing.process.geotransform.GeoTransformProcess;
import org.gvsig.gui.beans.datainput.DataInputContainer;
import org.gvsig.gui.beans.table.TableContainer;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.gui.beans.table.models.GCPModel;
import org.gvsig.raster.datastruct.GeoPoint;
import org.gvsig.raster.datastruct.GeoPointList;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * Panel que contiene la tabla de puntos de control
 * 
 * 22/12/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class GCPTablePanel extends JPanel implements IWindow, ComponentListener {
	private static final long             serialVersionUID    = 1L;
	private String[]                      columnNames         = {"-", "Nº", "X", "Y", "X'", "Y'", "Error X", "Error Y", "RMS", "-"};
	private int[]                         columnWidths        = {25, 25, 90, 90, 90, 90, 50, 50, 50, 0};
	private TableContainer                table               = null;
	
	private JPanel                        buttonsPanel        = null;
	private ApplicationControlsListener   buttonsListener     = null;
	private JButton                       bSaveToXml           = null;
	private JButton                       bLoadFromXml         = null;
	private JButton                       bSaveToAscii         = null;
	private JButton                       bLoadFromAscii       = null;
	private JButton                       bOptions             = null;
	private JButton                       bTest                = null;
	private JButton                       bEndTest             = null;
	private JButton                       bEndGeoref           = null;
	private JButton                       bCenterView          = null;
	private JToggleButton                 bAddPoint            = null;
	private DataInputContainer            error                = null;
	
	private int                           w                   = 640;
	private int                           h                   = 100;
	private int                           posX                = 0;
	private int                           posY                = 0;
	private GeoTransformProcess           process             = null;
	private Georeferencing                appMain             = null;
	
	/**
	 * Constructor.
	 * Crea la composición de controles de zoom.
	 */
	public GCPTablePanel(int posX, int posY, int w, int h, Georeferencing appMain) {
		setPosition(posX, posY);
		setWindowsSize(w, h);
		this.appMain = appMain;
	}
	
	/**
	 * Obtiene el objeto gestor de la georreferenciación.
	 * @return
	 */
	public Georeferencing getAppMain() {
		return appMain;
	}
		
	/**
	 * Asigna el listener para los botones
	 * @param listener
	 */
	public void initialize(ApplicationControlsListener listener) {
		this.buttonsListener = listener;
		process = new GeoTransformProcess();
		this.addComponentListener(this);
		try {
			init();
		} catch (NotInitializeException e) {
			RasterToolsUtil.messageBoxError(RasterToolsUtil.getText(this, "table_not_initialize"), this);
		}
	}
	
	/**
	 * Asigna la posición de la ventana
	 * @param posX Posición en X
	 * @param posY Posición en Y
	 */
	public void setPosition(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}
	
	/**
	 * Asigna la posición de la ventana
	 * @param posX Posición en X
	 * @param posY Posición en Y
	 */
	public void setWindowsSize(int w, int h) {
		this.w = w;
		this.h = h;
	}
	
	/**
	 * Inicialización de los componentes gráficos
	 */
	private void init() throws NotInitializeException  {
		setLayout(new BorderLayout());
		setSize(new java.awt.Dimension(w, h)); 
		
		GridBagConstraints gb = new GridBagConstraints();
		gb.insets = new java.awt.Insets(0, 0, 0, 0);
		gb.gridy = 0;
		gb.gridx = 0;
		gb.weightx = 1D; //El espacio sobrante se distribuye horizontalmente
		gb.weighty = 1D; //El espacio sobrante se distribuye verticalmente
		gb.fill = GridBagConstraints.BOTH; //El componente se hace tan ancho como espacio disponible tiene
		gb.anchor = GridBagConstraints.NORTH; //Alineamos las cajas arriba
		add(getTable(), BorderLayout.CENTER);
		
		gb.gridx = 1;
		add(getButtonsPanel(), BorderLayout.EAST);
	}
	
	/**
	 * Obtiene la tabla de puntos de control
	 * @return TableContainer
	 */
	public TableContainer getTable() throws NotInitializeException {
		if(table == null) {
			ArrayList listeners = new ArrayList();
			listeners.add(buttonsListener);
			table = new TableContainer(columnNames, columnWidths, listeners);
			table.setModel("GCPModel");
			table.initialize();
			table.setControlVisible(true);
			table.setMoveRowsButtonsVisible(true);
			table.setEditable(true);
			table.getTable().getJTable().getColumnModel().getColumn(columnNames.length - 1).setMinWidth(0);
			table.getTable().getJTable().getColumnModel().getColumn(columnNames.length - 1).setMaxWidth(0);
			table.getTable().getJTable().getColumnModel().getColumn(0).setMinWidth(26);
			table.getTable().getJTable().getColumnModel().getColumn(0).setMaxWidth(26);
			table.getTable().getJTable().getColumnModel().getColumn(1).setMinWidth(26);
			table.getTable().getJTable().getColumnModel().getColumn(1).setMaxWidth(26);
			table.getModel().addTableModelListener(buttonsListener);
		}
		return table;
	}
	
	/**
	 * Obtiene el panel de botones
	 * @return JPanel
	 */
	public JPanel getButtonsPanel() {
		if(buttonsPanel == null) {
			buttonsPanel = new JPanel();
			buttonsPanel.setLayout(new GridBagLayout());
			
			GridBagConstraints gb = new GridBagConstraints();
			gb.insets = new java.awt.Insets(0, 0, 1, 1);
			gb.gridy = 0;
			gb.gridx = 0;
			buttonsPanel.add(getSaveToXMLButton(), gb);
			
			gb.gridy = 0;
			gb.gridx = 1;
			buttonsPanel.add(getLoadFromXMLButton(), gb);
			
			gb.gridy = 0;
			gb.gridx = 2;
			buttonsPanel.add(getExporToCSVButton(), gb);
			
			gb.gridy = 0;
			gb.gridx = 3;
			buttonsPanel.add(getLoadFromCSVButton(), gb);
			
			gb.gridy = 1;
			gb.gridx = 0;
			buttonsPanel.add(getOptionsButton(), gb);
			
			gb.gridy = 1;
			gb.gridx = 1;
			buttonsPanel.add(getCenterButton(), gb);
			
			gb.gridy = 1;
			gb.gridx = 2;
			buttonsPanel.add(getToolSelectPointButton(), gb);
			
			gb.gridy = 1;
			gb.gridx = 3;
			buttonsPanel.add(getEndGeorefButton(), gb);
			
			gb.gridy = 2;
			gb.gridx = 0;
			buttonsPanel.add(getTestButton(), gb);
			
			gb.gridy = 2;
			gb.gridx = 1;
			buttonsPanel.add(getEndTestButton(), gb);
			
			gb.gridy = 3;
			gb.gridx = 0;
			gb.gridwidth = 4;
			buttonsPanel.add(getError(), gb);
		}
		return buttonsPanel;
	}
	
	/**
	 * Obtiene el objeto con el error total
	 * @return
	 */
	public DataInputContainer getError() {
		if(error == null) {
			error = new DataInputContainer();
			error.setLabelText("RMS");
			error.setPreferredSize(new Dimension(90, 35));
		}
		return error;
	}
	
	/**
	 * Obtiene el botón de finalizar georreferenciación. Si no existe se crea.
	 * @return JButton
	 */
	public JButton getEndGeorefButton() {
		if(bEndGeoref == null) {
			bEndGeoref = new JButton();
			bEndGeoref.setPreferredSize(new Dimension(22, 19));
			bEndGeoref.setIcon(RasterToolsUtil.getIcon("exit-icon"));
			bEndGeoref.setToolTipText(RasterToolsUtil.getText(this, "end_georef"));
			bEndGeoref.addActionListener(buttonsListener);
		}
		return bEndGeoref;
	}
	
	/**
	 * Obtiene el botón de procesar georreferenciación. Si no existe se crea.
	 * @return JButton
	 */
	public JButton getTestButton() {
		if(bTest == null) {
			bTest = new JButton();
			bTest.setPreferredSize(new Dimension(22, 19));
			bTest.setToolTipText(RasterToolsUtil.getText(this, "test_georef"));
			bTest.addActionListener(buttonsListener);
			bTest.setIcon(RasterToolsUtil.getIcon("process-icon"));
		}
		return bTest;
	}
	
	/**
	 * Obtiene el botón de terminar el test de georreferenciación. Si no existe se crea.
	 * @return JButton
	 */
	public JButton getEndTestButton() {
		if(bEndTest == null) {
			bEndTest = new JButton();
			bEndTest.setPreferredSize(new Dimension(22, 19));
			bEndTest.setToolTipText(RasterToolsUtil.getText(this, "end_test_georef"));
			bEndTest.addActionListener(buttonsListener);
			bEndTest.setIcon(RasterToolsUtil.getIcon("endprocess-icon"));
		}
		return bEndTest;
	}
		
	/**
	 * Obtiene el botón de procesar georreferenciación. Si no existe se crea.
	 * @return JButton
	 */
	public JButton getLoadFromCSVButton() {
		if(bLoadFromAscii == null) {
			bLoadFromAscii = new JButton();
			bLoadFromAscii.setPreferredSize(new Dimension(22, 19));
			bLoadFromAscii.setToolTipText(RasterToolsUtil.getText(this, "load_from_ascii"));
			bLoadFromAscii.addActionListener(buttonsListener);
			bLoadFromAscii.setIcon(RasterToolsUtil.getIcon("importfromcsv-icon"));
		}
		return bLoadFromAscii;
	}
	
	/**
	 * Obtiene el botón de exportar a Ascii. Si no existe se crea.
	 * @return JButton
	 */
	public JButton getExporToCSVButton() {
		if(bSaveToAscii == null) {
			bSaveToAscii = new JButton();
			bSaveToAscii.setPreferredSize(new Dimension(22, 19));
			bSaveToAscii.setToolTipText(RasterToolsUtil.getText(this, "save_to_ascii"));
			bSaveToAscii.addActionListener(buttonsListener);
			bSaveToAscii.setIcon(RasterToolsUtil.getIcon("exporttocsv-icon"));
		}
		return bSaveToAscii;
	}
	
	/**
	 * Obtiene el botón de cargar desde XML. Si no existe se crea.
	 * @return JButton
	 */
	public JButton getLoadFromXMLButton() {
		if(bLoadFromXml == null) {
			bLoadFromXml = new JButton();
			bLoadFromXml.setPreferredSize(new Dimension(22, 19));
			bLoadFromXml.setToolTipText(RasterToolsUtil.getText(this, "load_from_xml"));
			bLoadFromXml.addActionListener(buttonsListener);
			bLoadFromXml.setIcon(RasterToolsUtil.getIcon("tfwload-icon"));
		}
		return bLoadFromXml;
	}
	
	/**
	 * Obtiene el botón de salvar a XML. Si no existe se crea.
	 * @return JButton
	 */
	public JButton getSaveToXMLButton() {
		if(bSaveToXml == null) {
			bSaveToXml = new JButton();
			bSaveToXml.setPreferredSize(new Dimension(22, 19));
			bSaveToXml.setToolTipText(RasterToolsUtil.getText(this, "save_gcp_to_xml"));
			bSaveToXml.addActionListener(buttonsListener);
			bSaveToXml.setIcon(RasterToolsUtil.getIcon("save-icon"));
		}
		return bSaveToXml;
	}
	
	/**
	 * Obtiene el botón de procesar georreferenciación. Si no existe se crea.
	 * @return JButton
	 */
	public JButton getOptionsButton() {
		if(bOptions == null) {
			bOptions = new JButton();
			bOptions.setPreferredSize(new Dimension(22, 19));
			bOptions.setToolTipText(RasterToolsUtil.getText(this, "options"));
			bOptions.addActionListener(buttonsListener);
			bOptions.setIcon(RasterToolsUtil.getIcon("options-icon"));
		}
		return bOptions;
	}
	
	/**
	 * Obtiene el botón de asignar el punto de control seleccionado en la tabla sobre 
	 * una posición de la vista. Si no existe se crea.
	 * @return JButton
	 */
	public JToggleButton getToolSelectPointButton() {
		if(bAddPoint == null) {
			bAddPoint = new JToggleButton();
			bAddPoint.setPreferredSize(new Dimension(22, 19));
			bAddPoint.setToolTipText(RasterToolsUtil.getText(this, "move-point"));
			bAddPoint.addActionListener(buttonsListener);
			bAddPoint.setIcon(RasterToolsUtil.getIcon("add-icon"));
		}
		return bAddPoint;
	}
	
	/**
	 * Obtiene el botón de procesar georreferenciación. Si no existe se crea.
	 * @return JButton
	 */
	public JButton getCenterButton() {
		if(bCenterView == null) {
			bCenterView = new JButton();
			bCenterView.setPreferredSize(new Dimension(22, 19));
			bCenterView.setToolTipText(RasterToolsUtil.getText(this, "center_view"));
			bCenterView.addActionListener(buttonsListener);
			bCenterView.setIcon(RasterToolsUtil.getIcon("centerpoint-icon"));
		}
		return bCenterView;
	}
	
	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}
	
	/**
	 * Cuando la tabla tiene un tamaño mayor de 800 pixeles de ancho se muestran las 
	 * columnas de error en X e Y que cuando está minimizada no aparecen.
	 */
	public void componentResized(ComponentEvent e) {
		try {
			Dimension dim = ((GCPTablePanel)e.getSource()).getSize();
			if(dim.getWidth() <= 800) {
				TableColumn col = getTable().getTable().getJTable().getColumnModel().getColumn(6);
				col.setMinWidth(0);
				col.setMaxWidth(0);
				col = getTable().getTable().getJTable().getColumnModel().getColumn(7);
				col.setMinWidth(0);
				col.setMaxWidth(0);
			} else {
				TableColumn col = getTable().getTable().getJTable().getColumnModel().getColumn(6);
				col.setMaxWidth(110);
				col.setMinWidth(110);
				col = getTable().getTable().getJTable().getColumnModel().getColumn(7);
				col.setMaxWidth(110);
				col.setMinWidth(110);
			}
			getTable().getTable().invalidate();
		} catch (NotInitializeException ex) {
			return;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.RESIZABLE | WindowInfo.MAXIMIZABLE);
		//if (getClippingPanel().getFLayer() != null)
			//m_viewinfo.setAdditionalInfo(getClippingPanel().getFLayer().getName());
		m_viewinfo.setTitle(PluginServices.getText(this, "points_panel"));
		m_viewinfo.setX(posX);
		m_viewinfo.setY(posY);
		m_viewinfo.setHeight(h);
		m_viewinfo.setWidth(w);
		return m_viewinfo;
	}
	
	public Object getWindowProfile(){
		return WindowInfo.TOOL_PROFILE;
	}
	
	//****************************************************
	//Acceso a los datos de la tabla
	
	/**
	 * Obtiene las coordenadas de una fila
	 * @param row Fila de la que se quieren obtener las coordenadas
	 * @return Array con 4 valores coordenadas real X, coordenada real Y, coordenadas raster X y coordenada raste Y
	 */
	public double[] getCoordinates(int row) {
		double xMap = 0, yMap = 0, xRaster = 0, yRaster = 0;
		boolean numberFormatException = false;
		try {
			Object value = getTable().getModel().getValueAt(row, 2);
			try {
				if(value instanceof Double) 
					xMap = ((Double)value).doubleValue();
				else if(value instanceof String) 
					xMap = Double.valueOf(((String)value)).doubleValue();
			} catch (NumberFormatException ex1) {
				numberFormatException = true;
			}

			value = getTable().getModel().getValueAt(row, 3);
			try {
				if(value instanceof Double) 
					yMap = ((Double)value).doubleValue();
				else if(value instanceof String) 
					yMap = Double.valueOf(((String)value)).doubleValue();
			} catch (NumberFormatException ex1) {
				numberFormatException = true;
			}

			try {
				value = getTable().getModel().getValueAt(row, 4);
				if(value instanceof Double) 
					xRaster = ((Double)value).doubleValue();
				else if(value instanceof String) 
					xRaster = Double.valueOf(((String)value)).doubleValue();
			} catch (NumberFormatException ex1) {
				numberFormatException = true;
			}

			try {
				value = getTable().getModel().getValueAt(row, 5);
				if(value instanceof Double) 
					yRaster = ((Double)value).doubleValue();
				else if(value instanceof String) 
					yRaster = Double.valueOf(((String)value)).doubleValue();
			} catch (NumberFormatException ex1) {
				numberFormatException = true;
			}
			
			//Esto es necesario porque aunque se produzca una excepción en la lectura
			//de un valor deben leerse los demás campos antes de terminar.
			if(numberFormatException) {
				RasterToolsUtil.messageBoxError("value_not_valid", table);
				try {
					getTable().getModel().setValueAt(new String(xMap + ""), row, 2);
					getTable().getModel().setValueAt(new String(yMap + ""), row, 3);
					getTable().getModel().setValueAt(new String(xRaster + ""), row, 4);
					getTable().getModel().setValueAt(new String(yRaster + ""), row, 5);
				} catch (NotInitializeException e) {
					//Si está inicializada sino habría entrado en la excepción de nivel mayor
				}
			}
			
			//return new double[]{xMap, yMap, xRaster, yRaster};
		} catch (NotInitializeException ex) {
			RasterToolsUtil.messageBoxError("table_not_initialize", table);
		}
		return new double[]{xMap, yMap, xRaster, yRaster};
	}

	/**
	 * 
	 * @param row
	 * @return
	 */
	public boolean getChecked(int row) {
		try {
			Object value = getTable().getModel().getValueAt(row, 0);
			if(value != null && value instanceof Boolean) 
				return ((Boolean)value).booleanValue();
		}catch (NotInitializeException ex) {
			RasterToolsUtil.messageBoxYesOrNot("table_not_initialize", table);
		}
		return false;
	}
	
	/**
	 * Añade una fila de datos al final de la tabla
	 * @param checked
	 * @param mX
	 * @param mY
	 * @param pX
	 * @param pY
	 * @param z
	 * @param eX
	 * @param eY
	 * @param rms
	 */
	public void addRow(boolean checked, Point2D map, Point2D pixel, double z, double eX, double eY, double rms, long id) {
		try {
			int rowCount = getTable().getRowCount();
			DefaultTableModel model = getTable().getModel();
			if(model instanceof GCPModel) {
				Object[] rowData = ((GCPModel)model).getNewLine();
				rowData[0] = new Boolean(checked);
				rowData[1] = rowCount + "";
				rowData[2] = map.getX() + "";
				rowData[3] = map.getY() + "";
				rowData[4] = pixel.getX() + "";
				rowData[5] = pixel.getY() + "";
				rowData[rowData.length - 1] = new Long(id);
				getTable().addRow(rowData);
			}
		} catch (NotInitializeException e) {
			RasterToolsUtil.messageBoxYesOrNot("table_not_initialize", table);
		}
	}
	
	/**
	 * Elimina todos los puntos de la tabla
	 */
	public void removeAllPoints() {
		try {
			getTable().removeAllRows();
		} catch (NotInitializeException e) {
			RasterToolsUtil.messageBoxYesOrNot("table_not_initialize", table);
		}
	}
	
	/**
	 * Asigna una valor a una celda de la tabla
	 * @param value Valor a asignar
	 * @param row Fila
	 * @param col Columna
	 */
	public void setValueAt(Object value, int row, int col) {
		try {
			getTable().setValueAt(value, row, col);
		} catch (NotInitializeException e) {
			RasterToolsUtil.messageBoxError("table_not_initialize", this, e);
		}
	}
	
	/**
	 * Asigna un valor al punto en coordenadas raster de la tabla. Es recomendable gastar este y no 
	 * setValueAt para evitar que se recalculen los errores en cada inserción de cada celda. De esta
	 * forma al actualiza ambas coordenadas a la vez solo se calculará el error una vez.
	 * @param valueX Valor en X
	 * @param valueY Valor en Y
	 * @param row Fila a actualizar
	 * @param col Columna a actualizar
	 */
	public void updateRasterPoint(Object valueX, Object valueY, int row) {
		try {
			getTable().setValueAt(valueX, row, 4);
			getTable().setValueAt(valueY, row, 5);
			updateErrors();
		} catch (NotInitializeException e) {
			RasterToolsUtil.messageBoxError("table_not_initialize", this, e);
		}
	}
	
	/**
	 * Asigna un valor al punto en coordenadas del mundo de la tabla. Es recomendable gastar este y no 
	 * setValueAt para evitar que se recalculen los errores en cada inserción de cada celda. De esta
	 * forma al actualiza ambas coordenadas a la vez solo se calculará el error una vez.
	 * @param valueX Valor en X
	 * @param valueY Valor en Y
	 * @param row Fila a actualizar
	 * @param col Columna a actualizar
	 */
	public void updateMapPoint(Object valueX, Object valueY, int row) {
		try {
			getTable().setValueAt(valueX, row, 2);
			getTable().setValueAt(valueY, row, 3);
			updateErrors();
		} catch (NotInitializeException e) {
			RasterToolsUtil.messageBoxError("table_not_initialize", this, e);
		}
	}
	
	/**
	 * Asigna un valor al punto de la tabla. Es recomendable gastar este y no 
	 * setValueAt para evitar que se recalculen los errores en cada inserción de cada celda. De esta
	 * forma al actualiza ambas coordenadas a la vez solo se calculará el error una vez.
	 * @param mapX Valor en X en coordenadas del mundo
	 * @param mapY Valor en Y en coordenadas del mundo
	 * @param pixelX Valor en X en coordenadas del raster
	 * @param pixelY Valor en Y en coordenadas del raster
	 * @param row Fila a actualizar
	 * @param col Columna a actualizar
	 */
	public void updatePoint(Object mapX, Object mapY, Object pixelX, Object pixelY, int row) {
		try {
			getTable().setValueAt(mapX, row, 2);
			getTable().setValueAt(mapY, row, 3);
			getTable().setValueAt(pixelX, row, 4);
			getTable().setValueAt(pixelY, row, 5);
			updateErrors();
		} catch (NotInitializeException e) {
			RasterToolsUtil.messageBoxError("table_not_initialize", this, e);
		}
	}
	
	/**
	 * Actualiza los errores de toda la tabla. Esta función debe ser llamada cuando ha
	 * habido algún cambio en los valores de los puntos.
	 * @throws NotInitializeException
	 */
	public void updateErrors() {
		GeoPointList gpList = appMain.getLayerPointManager().getGeoPoints();			
		process.addParam("gpcs", gpList);
		int degree = 1;
		if(appMain.getOptions().getAlgorithm() == Georeferencing.POLYNOMIAL) 
			degree = appMain.getOptions().getDegree();
		process.addParam("orden", new Integer(degree));
		try {
			process.execute();
		} catch (InterruptedException e) {
			return;
		} catch(RuntimeException e) {
			//Matriz singular
			return;
		}
		GeoTransformDataResult result = (GeoTransformDataResult)process.getResult();
		if(result == null)
			return;
		
		//Actualizamos los errores de toda la tabla
		try {
			ColorColumnRenderer cr = new ColorColumnRenderer();
			for (int i = 0; i < gpList.size(); i++) {
				GeoPoint point = gpList.get(i);
				getTable().setValueAt(new Double(point.getErrorX()), point.number, 6);
				getTable().setValueAt(new Double(point.getErrorY()), point.number, 7);
				getTable().setValueAt(new Double(point.getRms()), point.number, 8);
				if (point.getRms() > appMain.getOptions().getThreshold())
					cr.addRowToColor1(point.number);
				else
					cr.addRowToColor2(point.number);
			}
			getTable().getTable().getJTable().getColumnModel().getColumn(8).setCellRenderer(cr);
			getError().setValue(result.getRmsTotal() + "");
		} catch (NotInitializeException e) {
			RasterToolsUtil.messageBoxError("table_not_initialize", this, e);
		}
	}
	
	/**
	 * Obtiene el resultado de aplicar el algoritmo de transformación
	 * @return
	 */
	public GeoTransformDataResult getGeoTransformDataResult() {
		if(process != null)
			return (GeoTransformDataResult)process.getResult();
		return null;
	}
	
	/**
	 * Obtiene una valor de una celda de la tabla
	 * @param row Fila
	 * @param col Columna
	 * @return Valor obtenido
	 */
	public Object getValueAt(int row, int col) {
		try {
			return getTable().getModel().getValueAt(row, col);
		} catch (NotInitializeException e) {
			RasterToolsUtil.messageBoxError("table_not_initialize", this, e);
		}
		return null;
	}
	
	/**
	 * Obtiene el número de filas de la tabla
	 * @return Entero que representa el número de filas de la tabla
	 */
	public int getRowCount() {
		try {
			return getTable().getRowCount();
		} catch (NotInitializeException e) {
			RasterToolsUtil.messageBoxError("table_not_initialize", this, e);
		}
		return 0;
	}
	
	/**
	 * Obtiene el número de columnas de la tabla
	 * @return Entero que representa el número de columnas de la tabla
	 */
	public int getColumnCount() {
		try {
			return getTable().getModel().getColumnCount();
		} catch (NotInitializeException e) {
			RasterToolsUtil.messageBoxError("table_not_initialize", this, e);
		}
		return 0;
	}
	
	/**
	 * Inicializa una fila de la tabla con los valores a 0, el número de punto
	 * y el identificador. Esto es util para la inserción de un nuevo punto antes
	 * de introducir sus coordenadas.
	 * @param row Fila a inicializar
	 * @param id Identificador
	 */
	public void initializeRow(int row, long id) {
		try {
			getTable().setValueAt(new Integer(row), row, 1);
			for (int i = 2; i <= 9; i++) 
				getTable().setValueAt(new Double(0), row, i);
			getTable().setValueAt(new Long(id), row, getColumnCount() - 1);
		} catch (NotInitializeException e) {
			RasterToolsUtil.messageBoxError("table_not_initialize", this, e);
		}
	}
	

	/**
	 * Renderer para cambiar el color a la columna de la tabla
	 * 06/02/2008
	 * @author Nacho Brodin nachobrodin@gmail.com
	 */
	public class ColorColumnRenderer extends DefaultTableCellRenderer {
		private static final long   serialVersionUID = 1L;
		private Color back = Color.red;
		private Color fore = Color.white;
		private Color back2 = Color.white;
		private Color fore2 = Color.black;
		private ArrayList color1 = null;
		private ArrayList color2 = null;
	 	
		 public ColorColumnRenderer() {
				super(); 
				color1 = new ArrayList();
				color2 = new ArrayList();
		 }
		 
		 /**
			* Añade una fila al tipo de color 1
			* @param row
			*/
		 public void addRowToColor1(int row) {
			 color1.add(new Integer(row));
		 }
		 
		 /**
			* Añade una fila al tipo de color 2
			* @param row
			*/
		 public void addRowToColor2(int row) {
			 color2.add(new Integer(row));
		 }
			
		 public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected,
															 boolean hasFocus, int row, int column) {
				Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				for (int i = 0; i < color1.size(); i++) {
					if(((Integer)color1.get(i)).intValue() == row) {
						cell.setBackground(back);
						cell.setForeground(fore);
					}
				}
			 for (int i = 0; i < color2.size(); i++) {
				 if(((Integer)color2.get(i)).intValue() == row) {
					 cell.setBackground(back2);
					 cell.setForeground(fore2);
				 }
			 }
				return cell;
		 }
	}
	
}

