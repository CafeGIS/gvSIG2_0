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
package org.gvsig.rastertools.enhanced.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.beans.canvas.GCanvasEvent;
import org.gvsig.raster.beans.canvas.IGCanvasListener;
import org.gvsig.raster.beans.canvas.layers.GraphicHistogram;
import org.gvsig.raster.beans.canvas.layers.functions.BaseFunction;
import org.gvsig.raster.beans.canvas.layers.functions.DensitySlicingLine;
import org.gvsig.raster.beans.canvas.layers.functions.LogaritmicExponentialLine;
import org.gvsig.raster.beans.canvas.layers.functions.SquareRootPowLine;
import org.gvsig.raster.beans.canvas.layers.functions.StraightLine;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.enhancement.EqualizationFilter;
import org.gvsig.raster.grid.filter.enhancement.LinearStretchEnhancementFilter;
import org.gvsig.raster.grid.filter.enhancement.LinearStretchParams;
import org.gvsig.raster.grid.filter.enhancement.LinearStretchParams.Stretch;
import org.gvsig.raster.hierarchy.IRasterRendering;
import org.gvsig.raster.util.LayerVisualStatusList;
import org.gvsig.raster.util.RasterNotLoadException;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.raster.util.process.FilterProcess;
import org.gvsig.rastertools.enhanced.graphics.HistogramGraphicBase;
import org.gvsig.rastertools.enhanced.graphics.HistogramGraphicBase.HistogramStatus;

import com.iver.andami.PluginServices;
/**
 * Gestor de eventos de los paneles de gráficas y controles.
 * 
 * 21/02/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class EnhancedListener implements ActionListener, IGCanvasListener, ButtonsPanelListener, IProcessActions {
	private SelectorsPanel               selectorsPanel  = null;
	private GraphicsPanel                graphicsPanel   = null;
	private EnhancedDialog               enhancedDialog  = null;
	private PreviewFiltering             filteredPreview = null;
	private EnhancedHistogramController  enhancedManager = null;
	private LayerVisualStatusList        status          = new LayerVisualStatusList();
	
	/**
	 * Constructor
	 * @param selectorsPanel Panel con los selectores de opciones
	 * @param graphicsPanel Panel con los gráficos
	 * @param enhancedPanel Panel base con la previsualización
	 * @param enhancedDialog Dialogo general
	 * @param filteredPreview Preprocesado para la preview
	 */
	public EnhancedListener(SelectorsPanel selectorsPanel, 
							GraphicsPanel graphicsPanel, 
							EnhancedDialog enhancedDialog, 
							PreviewFiltering filteredPreview) {
		this.selectorsPanel = selectorsPanel;
		this.graphicsPanel = graphicsPanel;
		this.enhancedDialog = enhancedDialog;
		this.filteredPreview = filteredPreview;
		status.getVisualStatus(((FLyrRasterSE) enhancedDialog.getLayer()));
		
		
		enhancedManager = new EnhancedHistogramController(graphicsPanel.getInputHistogram(), graphicsPanel.getOutputHistogram(), enhancedDialog);
		
		selectorsPanel.getHistogramType().addActionListener(this);
		selectorsPanel.getDrawType().addActionListener(this);
		selectorsPanel.getBand(null).addActionListener(this);
		selectorsPanel.getEnhancedType().addActionListener(this);
		graphicsPanel.getLevels().addActionListener(this);
		graphicsPanel.getRGB().addActionListener(this);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		//Cambio del tipo de de dibujado del histograma
		if(e.getSource() == selectorsPanel.getDrawType()) {
			if(((String)selectorsPanel.getDrawType().getSelectedItem()).equals("Fill")) {
				graphicsPanel.getInputHistogram().setType(GraphicHistogram.TYPE_FILL);
				graphicsPanel.getOutputHistogram().setType(GraphicHistogram.TYPE_FILL);
			}
			
			if(((String)selectorsPanel.getDrawType().getSelectedItem()).equals("Line")) {
				graphicsPanel.getInputHistogram().setType(GraphicHistogram.TYPE_LINE);
				graphicsPanel.getOutputHistogram().setType(GraphicHistogram.TYPE_LINE);
			}
			graphicsPanel.getInputHistogram().getCanvas().repaint();
			graphicsPanel.getOutputHistogram().getCanvas().repaint();
		}

		// Cambio el tipo de visualizacion del histograma
		if (e.getSource() == selectorsPanel.getHistogramType()) {
			if (((String) selectorsPanel.getHistogramType().getSelectedItem()).equals("Standard")) {
				graphicsPanel.getOutputHistogram().setHistogramType(GraphicHistogram.VIEW_LINEAL);
			}
			
			if (((String) selectorsPanel.getHistogramType().getSelectedItem()).equals("Cumulative")) {
				graphicsPanel.getOutputHistogram().setHistogramType(GraphicHistogram.VIEW_ACUMMULATED);
			}
			
			if (((String) selectorsPanel.getHistogramType().getSelectedItem()).equals("Logarithmic")) {
				graphicsPanel.getOutputHistogram().setHistogramType(GraphicHistogram.VIEW_LOGARITHMIC);
			}
			
			if (((String) selectorsPanel.getHistogramType().getSelectedItem()).equals("Cumulative Logarithmic")) {
				graphicsPanel.getOutputHistogram().setHistogramType(GraphicHistogram.VIEW_ACUMMULATEDLOG);
			}
			graphicsPanel.getOutputHistogram().getCanvas().repaint();
		}

		// Seleccion de modo RGB o normal
		if (e.getSource() == graphicsPanel.getRGB()) {
			graphicsPanel.updateHistogram();
			updatePreview();
		}
		
		//Cambio de banda
		if(e.getSource() == selectorsPanel.getBand(null)) {
			if(((String)selectorsPanel.getBand(null).getSelectedItem()).equals("Red")) {
				graphicsPanel.getInputHistogram().setHistogramDrawed(HistogramGraphicBase.RED);
				graphicsPanel.getOutputHistogram().setHistogramDrawed(HistogramGraphicBase.RED);
			}
			
			if(((String)selectorsPanel.getBand(null).getSelectedItem()).equals("Green")) {
				graphicsPanel.getInputHistogram().setHistogramDrawed(HistogramGraphicBase.GREEN);
				graphicsPanel.getOutputHistogram().setHistogramDrawed(HistogramGraphicBase.GREEN);
			}
			
			if(((String)selectorsPanel.getBand(null).getSelectedItem()).equals("Blue")) {
				graphicsPanel.getInputHistogram().setHistogramDrawed(HistogramGraphicBase.BLUE);
				graphicsPanel.getOutputHistogram().setHistogramDrawed(HistogramGraphicBase.BLUE);
			}

			updateTypeSelected();
			
			HistogramStatus status = graphicsPanel.getOutputHistogram().getHistogramStatus(HistogramGraphicBase.DRAWED);

			switch (status.getGraphicHistogram().getType()) {
				case GraphicHistogram.TYPE_FILL:
					selectorsPanel.getDrawType().setSelectedItem("Fill");
					break;
				default:
					selectorsPanel.getDrawType().setSelectedItem("Line");
					break;
			}

			switch (status.getGraphicHistogram().getTypeViewed()) {
				case GraphicHistogram.VIEW_ACUMMULATED:
					selectorsPanel.getHistogramType().setSelectedItem("Cumulative");
					break;
				case GraphicHistogram.VIEW_LOGARITHMIC:
					selectorsPanel.getHistogramType().setSelectedItem("Logarithmic");
					break;
				case GraphicHistogram.VIEW_ACUMMULATEDLOG:
					selectorsPanel.getHistogramType().setSelectedItem("Cumulative Logarithmic");
					break;
				default:
					selectorsPanel.getHistogramType().setSelectedItem("Standard");
					break;
			}
		}
		
		//Cambio de operación
		if(e.getSource() == selectorsPanel.getEnhancedType()) {
			graphicsPanel.setLevelsEnabled(false);
			
			if(((String)selectorsPanel.getEnhancedType().getSelectedItem()).equals("Lineal")) {
				graphicsPanel.getInputHistogram().setFunction(GraphicHistogram.FUNCTION_LINEAL);
				updatePreview();
			}
			
			if(((String)selectorsPanel.getEnhancedType().getSelectedItem()).equals("Gaussian")) {

			}
			
			if(((String)selectorsPanel.getEnhancedType().getSelectedItem()).equals("Logaritmic")) {
				graphicsPanel.getInputHistogram().setFunction(GraphicHistogram.FUNCTION_LOGARIT);
				updatePreview();
			}
			
			if(((String)selectorsPanel.getEnhancedType().getSelectedItem()).equals("Exponential")) {
				graphicsPanel.getInputHistogram().setFunction(GraphicHistogram.FUNCTION_EXPONENT);
				updatePreview();
			}
			
			if(((String)selectorsPanel.getEnhancedType().getSelectedItem()).equals("Equalization")) {
				graphicsPanel.getInputHistogram().setFunction(GraphicHistogram.FUNCTION_NONE);
				int[] renderBands = enhancedDialog.getLayer().getRender().getRenderBands();
				String values = "";
				for (int i = 0; i < renderBands.length; i++) 
					values += renderBands[i] + " ";
				values = values.trim();
				Params params = new Params();
				params.setParam("Histogram", graphicsPanel.getHistogram(), -1, null);
				params.setParam("RenderBands", values, -1, null);
				params.setParam("EcualizedBands", new int[]{0, 1, 2}, -1, null);
								
				filteredPreview.addNewParam("equalization", params, EqualizationFilter.class);				
				updatePreview();
			}
			
			if(((String)selectorsPanel.getEnhancedType().getSelectedItem()).equals("Square-root")) {
				graphicsPanel.getInputHistogram().setFunction(GraphicHistogram.FUNCTION_SQUARE_ROOT);
				updatePreview();
			}
			
			if(((String)selectorsPanel.getEnhancedType().getSelectedItem()).equals("Level-slice")) {
				graphicsPanel.setLevelsEnabled(true);
				graphicsPanel.getInputHistogram().setFunction(GraphicHistogram.FUNCTION_DENSITY);

				// Establece el numero de niveles en el cuadro de texto
				HistogramStatus status = graphicsPanel.getInputHistogram().getHistogramStatus(HistogramGraphicBase.DRAWED);
				graphicsPanel.getLevels().setValue(new Long(((DensitySlicingLine) status.getBaseFunction()).getLevels()));
				updatePreview();
			}
		}
		
		//Cambio de tipo (estandar/acumulado)
		if(e.getSource() == selectorsPanel.getHistogramType()) {
			if(((String)selectorsPanel.getHistogramType().getSelectedItem()).equals("Standard")) {
				
			}
			
			if(((String)selectorsPanel.getHistogramType().getSelectedItem()).equals("Cumulative")) {
				
			}
		}
		
		//Cambio en el número de niveles
		if(e.getSource() == graphicsPanel.getLevels()) {
			Long lValue = (Long)graphicsPanel.getLevels().getValue();
			int value = lValue.intValue();
			if(value > 30 || value < 2) {
				RasterToolsUtil.messageBoxInfo(RasterToolsUtil.getText(this, "range_wrong") + " [2-30]", null);
				if(value > 30)
					value = 30;
				if(value < 2)
					value = 2;
			}
			graphicsPanel.getLevels().setValue(new Long(value));
			try {
				graphicsPanel.getInputHistogram().setLevel(value);
				updatePreview();
			} catch (NumberFormatException exc) {
				//No asignamos el nivel
			}
		}
	}
	
	/**
	 * Actualiza el combo de EnhancedType para que este seleccionado siempre el
	 * item que corresponde con la grafica mostrada en ese momento
	 */
	private void updateTypeSelected() {
		HistogramStatus status = graphicsPanel.getInputHistogram().getHistogramStatus(HistogramGraphicBase.DRAWED);

		if (status.getBaseFunction().getClass().equals(DensitySlicingLine.class))
			selectorsPanel.setSelectedEnhancedType("Level-slice");

		if (status.getBaseFunction().getClass().equals(StraightLine.class))
			selectorsPanel.setSelectedEnhancedType("Lineal");

		if (status.getBaseFunction().getClass().equals(EqualizationFilter.class))
			selectorsPanel.setSelectedEnhancedType("Equalization");

		if (status.getBaseFunction().getClass().equals(SquareRootPowLine.class))
			selectorsPanel.setSelectedEnhancedType("Square-root");

		if (status.getBaseFunction().getClass().equals(LogaritmicExponentialLine.class)) {
			if (((StraightLine) status.getBaseFunction()).getValueFunction() >= 0)
				selectorsPanel.setSelectedEnhancedType("Logaritmic");
			else
				selectorsPanel.setSelectedEnhancedType("Exponential");
		}
	}
	
	/**
	 * En la primera carga se define cada banda en los histogramas, para dejarlo en
	 * su estado logico.
	 * @param stretch
	 * @param band
	 */
	private void firstLoadBand(Stretch stretch, int band) {
		boolean firstBand = ((band == HistogramGraphicBase.GRAY) || (band == HistogramGraphicBase.RED));

		graphicsPanel.getInputHistogram().setHistogramDrawed(band);
		
		BaseFunction baseFunction = null;
		HistogramStatus status;
		status = graphicsPanel.getInputHistogram().getHistogramStatus(band);
		if (status == null)
			return;
		
		switch (stretch.functionType) {
			case 0:
				if (firstBand) {
					selectorsPanel.getEnhancedType().setSelectedItem("Lineal");
					graphicsPanel.setLevelsEnabled(false);
				}
				baseFunction = new StraightLine(status.getBaseFunction().getColor());
				((StraightLine) baseFunction).clearSquares();
				for (int i = 0; i < stretch.stretchIn.length; i++) {
					((StraightLine) baseFunction).addSquare(
						(stretch.stretchIn[i] - stretch.minValue) / (stretch.maxValue - stretch.minValue),
						stretch.stretchOut[i] / 255.0D);
				}
				break;
			case 1:
				if (firstBand) {
					if (stretch.valueFunction >= 0)
						selectorsPanel.getEnhancedType().setSelectedItem("Logaritmic");
					else
						selectorsPanel.getEnhancedType().setSelectedItem("Exponential");
					graphicsPanel.setLevelsEnabled(false);
				}
				baseFunction = new LogaritmicExponentialLine(status.getBaseFunction().getColor(), stretch.valueFunction);
				break;
			case 2:
				if (firstBand) {
					selectorsPanel.getEnhancedType().setSelectedItem("Square-root");
					graphicsPanel.setLevelsEnabled(false);
				}
				baseFunction = new SquareRootPowLine(status.getBaseFunction().getColor(), stretch.valueFunction);
				break;
			case 3:
				if (firstBand) {
					selectorsPanel.getEnhancedType().setSelectedItem("Level-slice");
					graphicsPanel.setLevelsEnabled(true);
				}
				baseFunction = new DensitySlicingLine(status.getBaseFunction().getColor(), (int) stretch.valueFunction);
				break;
		}
		if (baseFunction != null) {
			status.setBaseFunction(baseFunction);
			graphicsPanel.getInputHistogram().setHistogramDrawed(band);
		}
	}
	
	/**
	 * En la primera carga se han de establecer todos los histogramas de entrada
	 * a sus valores correspondientes segun el filtro.
	 */
	public void firstLoad() {
		RasterFilterList rasterFilterList = ((FLyrRasterSE) enhancedDialog.getLayer()).getRenderFilterList();

		LinearStretchEnhancementFilter filter = (LinearStretchEnhancementFilter) rasterFilterList.getByName(LinearStretchEnhancementFilter.names[0]);
		if (filter != null) {
			LinearStretchParams stretch = filter.getStretchs();
			
			firstLoadBand(stretch.blue, HistogramGraphicBase.BLUE);
			firstLoadBand(stretch.green, HistogramGraphicBase.GREEN);
			firstLoadBand(stretch.red, HistogramGraphicBase.RED);
			firstLoadBand(stretch.red, HistogramGraphicBase.GRAY);
			graphicsPanel.getRGB().setSelected(stretch.rgb);
		}
		graphicsPanel.getInputHistogram().repaint();
	}
	
	/**
	 * Coge los datos que hay en los histogramas y los aplica a la vista previa
	 */
	public void updatePreview() {
		enhancedManager.updatePreview();
		enhancedManager.updateHistogramOut();
	}

	/**
	 * Coge los datos que hay en los histogramas y los aplica en el histograma de salida
	 */
	private void updateHistogramOut() {
		updateTypeSelected();
		enhancedManager.updatePreview();
		enhancedManager.updateHistogramOut();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.IGCanvasListener#actionDataChanged(org.gvsig.raster.beans.canvas.GCanvasEvent)
	 */
	public void actionDataChanged(GCanvasEvent e) {
		if (e.getKey().equals("minmax")) {
			updatePreview();
			return;
		}
		if (e.getKey().equals("line")) {
			updatePreview();
			return;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.IGCanvasListener#actionDataDragged(org.gvsig.raster.beans.canvas.GCanvasEvent)
	 */
	public void actionDataDragged(GCanvasEvent e) {
		if (e.getKey().equals("minmax")) {
			updateHistogramOut();
			return;
		}
		if (e.getKey().equals("line")) {
			updateHistogramOut();
			return;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener#actionButtonPressed(org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent)
	 */
	public void actionButtonPressed(ButtonsPanelEvent e) {
		switch (e.getButton()) {
			case ButtonsPanel.BUTTON_ACCEPT:
				apply();
				enhancedDialog.close();
				break;
			case ButtonsPanel.BUTTON_CANCEL:
				cancel();
				enhancedDialog.close();
				break;
			case ButtonsPanel.BUTTON_APPLY:
				apply();
				break;
		}
	}
	
	/**
	 * Que acciones se ejecutaran al haber presionado el botón aceptar o aplicar
	 */
	public void apply() {
		IRasterDataSource raster = ((FLyrRasterSE) enhancedDialog.getLayer()).getDataSource();
		if (raster == null)
			return;

		String path = null;
		if (!enhancedDialog.getNewOrSaveLayerPanel().isOnlyViewSelected()) {
			path = enhancedDialog.getNewOrSaveLayerPanel().getFileSelected();
			if (path == null)
				return;
		}

		//Rendering rendering = ((FLyrRasterSE) getFilterPanel().getLayer()).getRender();
		IRasterRendering rendering = (IRasterRendering) enhancedDialog.getLayer();

		// Array para guardar los filtros que se van a usar en forma de ParamStruct
		ArrayList listFilterUsed = enhancedDialog.getFilteredPreview().applyFilters(rendering);

		if (enhancedDialog.getNewOrSaveLayerPanel().isOnlyViewSelected()) {
			try {
				FilterProcess.addSelectedFilters(rendering.getRenderFilterList(), listFilterUsed);
				((FLyrRasterSE) enhancedDialog.getLayer()).setRenderFilterList(rendering.getRenderFilterList());
				enhancedDialog.getLayer().getMapContext().invalidate();
			} catch (FilterTypeException e) {
				RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_adding_filters"), this, e);
			}
		} else {
			FilterProcess filterProcess = new FilterProcess();
			filterProcess.setActions(this);
			filterProcess.addParam("rendering", rendering);
			filterProcess.addParam("filename", path);
			filterProcess.addParam("rasterdatasource", raster);
			filterProcess.addParam("listfilterused", listFilterUsed);
			filterProcess.addParam("onlyrenderbands", Boolean.TRUE);
			filterProcess.start();
		}
	}
	
	/**
	 * Volvemos todo a la normalidad cuando se cancela
	 */
	public void cancel() {
		if (enhancedDialog.getLayer() != null) {
			status.restoreVisualStatus(enhancedDialog.getLayer());
			enhancedDialog.getLayer().getMapContext().invalidate();
		}
	}
	

	/**
	 * Acciones que se realizan al finalizar de crear los recortes de imagen.
	 * Este método es llamado por el thread TailRasterProcess al finalizar.
	 */
	public void loadLayerInToc(String fileName) {
		if (!enhancedDialog.getNewOrSaveLayerPanel().isNewLayerSelected())
			return;
		if(!new File(fileName).exists())
			return;
		try {
			RasterToolsUtil.loadLayer(enhancedDialog.getViewName(), fileName, null);
		} catch (RasterNotLoadException e) {
			RasterToolsUtil.messageBoxError("error_cargar_capa", this, e);
		}

		if(enhancedDialog != null)
			enhancedDialog.getNewOrSaveLayerPanel().updateNewLayerText();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.IProcessActions#end(java.lang.Object)
	 */
	public void end(Object param) {
		loadLayerInToc((String) param);
	}
	
	public void interrupted() {}
}