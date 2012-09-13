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
package org.gvsig.rastertools.vectorizacion.stretch.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.gvsig.raster.beans.canvas.DrawableElement;
import org.gvsig.raster.beans.canvas.GCanvas;
import org.gvsig.raster.beans.canvas.layers.Border;
import org.gvsig.raster.beans.canvas.layers.GraphicHistogram;
import org.gvsig.raster.beans.canvas.layers.InfoLayer;
import org.gvsig.raster.beans.canvas.layers.StretchLayer;
import org.gvsig.raster.beans.previewbase.IUserPanelInterface;
import org.gvsig.raster.beans.stretchselector.StretchSelectorPanel;
import org.gvsig.raster.util.BasePanel;
import org.gvsig.raster.util.GenericBasePanel;
import org.gvsig.rastertools.vectorizacion.stretch.StretchData;

/**
 * Panel con los controles de creación de tramos con distancia variable.
 * 
 * 09/06/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class StretchPanel extends BasePanel implements IUserPanelInterface, Observer {
	private static final long             serialVersionUID       = 1L;
	private StretchSelectorPanel          stretchSelectorPanel   = null;
	private GCanvas                       canvas                 = null;
	private JTable                        table                  = null;
	private JScrollPane                   panelList              = null;
	private IntervalTableModel            tableModel             = null;
	private BasePanel                     valuesStretchPanel     = null;
	private JButton                       loadButton             = null;
	
	/**
	 * Inicializa componentes gráficos y traduce
	 */
	public StretchPanel() {
		init();
		translate();
	}
		
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#translate()
	 */
	protected void translate() {}
	
	/**
	 * Inicialización de los componentes gráficos
	 */
	protected void init() {
		BorderLayout bl = new BorderLayout();
		bl.setHgap(5);
		bl.setVgap(5);
		setLayout(bl);
		
		BasePanel upPanel = BasePanel.getInstance();
		upPanel.setLayout(new BorderLayout());
		upPanel.add(getStretchSelectorPanel(), BorderLayout.WEST);
		BasePanel p = BasePanel.getInstance();
		p.add(getLoadButton());
		upPanel.add(p, BorderLayout.CENTER);
		
		add(upPanel, BorderLayout.NORTH);
		add(getStretchValuesPanel(), BorderLayout.CENTER);
	}
	
	/**
	 * Obtiene el botón de carga de tramos 
	 * @return JButton
	 */
	public JButton getLoadButton() {
		if(loadButton == null) {
			loadButton = new JButton(getText("loadstretch"));
			loadButton.setPreferredSize(new Dimension(180, 28));
		}
		return loadButton;
	}

	/**
	 * Obtiene el panel con el gráfico y la lista de intervalos. 
	 * @return JPanel
	 */
	private BasePanel getStretchValuesPanel() {
		if(valuesStretchPanel == null) {
			valuesStretchPanel = new GenericBasePanel();
			valuesStretchPanel.setBorder(BorderFactory.createTitledBorder(getText("tramos")));
			BorderLayout bl = new BorderLayout();
			bl.setHgap(5);
			bl.setVgap(5);
			valuesStretchPanel.setLayout(bl);
			valuesStretchPanel.add(getCanvas(), BorderLayout.CENTER);
			valuesStretchPanel.add(getTablePanel(), BorderLayout.EAST);
		}
		return valuesStretchPanel;
	}
	
	/** 
	 * Construye el gráfico con las capas para mover las
	 * líneas.
	 * @return
	 */
	public GCanvas getCanvas() {
		if(canvas == null) {
			canvas = new GCanvas(Color.BLACK);
			DrawableElement border = new Border(Color.WHITE);
			canvas.addDrawableElement(border);

			canvas.addDrawableElement(getStretchLayer());
			canvas.addDrawableElement(new InfoLayer(Color.WHITE));
		}
		return canvas;
	}
	
	/**
	 * Obtiene el componente con la lista de tramos.
	 * @return JList
	 */
	public JScrollPane getTablePanel() {
		if(panelList == null) {
			panelList = new JScrollPane(getTable());
			//Asignamos el ancho máximo para la columna de los intervalos númericos
			panelList.setPreferredSize(new Dimension(80, 0));
		}
		return panelList;
	}
	
	/**
	 * Obtiene el panel con el selector de tramos.
	 * @return
	 */
	public StretchSelectorPanel getStretchSelectorPanel() {
		if(stretchSelectorPanel == null) 
			stretchSelectorPanel = new StretchSelectorPanel();
		return stretchSelectorPanel;
	}
		
	public void actionPerformed(ActionEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.previewbase.IUserPanelInterface#getTitle()
	 */
	public String getTitle() {
		return getText(this, "stretch");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.previewbase.IUserPanelInterface#getPanel()
	 */
	public JPanel getPanel() {
		return this;
	}
	
	/**
	 * Obtiene el modelo de la tabla
	 */
	public IntervalTableModel getModel() {
		if(tableModel == null)
			tableModel = new IntervalTableModel(getStretchLayer().getStretchDataModel());
		return tableModel;
	}
	
	/**
	 * Obtiene la capa gráfica que representa los tramos
	 * @return StretchLayer 
	 */
	public StretchLayer getStretchLayer() {
		ArrayList listObjs = getCanvas().getDrawableElements(StretchLayer.class);
		if(listObjs.size() <= 0) {
			StretchLayer stretchLayer = new StretchLayer(Color.WHITE, 1);
			stretchLayer.fixExtreme(true);
			return stretchLayer;
		}
		return (StretchLayer)getCanvas().getDrawableElements(StretchLayer.class).get(0);
	}
	
	/**
	 * Obtiene la tabla
	 */
	public JTable getTable() {
		if(table == null) {
			table = new JTable(getModel());
			table.setTableHeader(null);
		}
		return table;
	}
	
	/**
	 * Obtiene la lista de valores
	 * @return
	 */
	public ArrayList getValueList() {
		return getModel().getValueList();
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg) {
		if(!(o instanceof StretchData))
			return;
		StretchData data = (StretchData)o;

		setEnableValueChangedEvent(false);
		
		//Asigna el modelo de datos del selector
		getStretchSelectorPanel().setData(data.getStretchSelectorData());
		getStretchSelectorPanel().getData().updateObservers();
		
		//Asignamos el modelo de datos de la capa gráfica al modelo de datos global si todavia no ha sido asignado
		if(data.getStretchDataModel() == null) {
			StretchLayer stretch = (StretchLayer)getCanvas().getDrawableElements(StretchLayer.class).get(0);
			data.setStretchLayerDataModel(stretch.getStretchDataModel());
		}
		
		//Asigna máximo y mínimo a la capa de información
		InfoLayer info = (InfoLayer)getCanvas().getDrawableElements(InfoLayer.class).get(0);
		info.setLimits(data.getMin(), data.getMax());
		
		//Asigna el histograma
		GraphicHistogram gHistR = new GraphicHistogram(data.getHistogram(), Color.RED);
		gHistR.setType(GraphicHistogram.TYPE_LINE);
		getCanvas().removeDrawableElement(GraphicHistogram.class);
		getCanvas().addDrawableElement(gHistR);
		getCanvas().repaint();

		setEnableValueChangedEvent(true);
	}
	
}
