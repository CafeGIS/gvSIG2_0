package org.gvsig.rastertools.properties.control;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;

import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.panelGroup.AbstractPanelGroup;
import org.gvsig.gui.beans.slidertext.listeners.SliderEvent;
import org.gvsig.gui.beans.slidertext.listeners.SliderListener;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.RasterFilterListManager;
import org.gvsig.raster.grid.filter.pansharp.PanSharpeningFilter;
import org.gvsig.raster.grid.filter.pansharp.PanSharpeningListManager;
import org.gvsig.raster.hierarchy.IRasterDataset;
import org.gvsig.raster.hierarchy.IRasterProperties;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.properties.panels.PanSharpeningPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.Utilities;
/**
 * Clase que hace de interfaz entre los objetos que contienen la información de
 * pansharp y el panel.
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */

public class PanSharpeningControl implements ActionListener, SliderListener, ChangeListener  {

	private PanSharpeningPanel 			pansharpPanel=null;
	protected RasterDataset[] 			grd = null;
	private FLayer		 				fLayer = null;
	private RasterFilterList            filterList = null;
	private IRasterDataset				dataset = null;
	private boolean 					aplicado=false;
	String fileNameOutput=null;
	private AbstractPanelGroup              panelGroup = null;
	
	public void actionValueChanged(SliderEvent e) {
		// TODO Auto-generated method stub	
	}

	public void actionValueDragged(SliderEvent e) {
		// TODO Auto-generated method stub
	}

	public PanSharpeningControl(AbstractPanelGroup panelGroup, PanSharpeningPanel pP,IRasterDataset dataset,IRasterProperties prop,FLayer lyr,RasterFilterList rfl) {
		this.panelGroup = panelGroup;
		this.pansharpPanel = pP;
		this.dataset = dataset;
		this.filterList = rfl;
		fLayer= lyr;
		pP.getCbActiveSharpening().addActionListener(this);
		pP.getJSlider().addChangeListener(this);
		pP.getJSlider1().addChangeListener(this);
	}
	
	
	public void init(IRasterDataset dset, IRasterProperties prop, FLayer lyr) {
		this.dataset = dset;
		fLayer = lyr;
	}
	
	public void stateChanged(ChangeEvent e) {
		//Ponemos el valor del texto del coeficiente 
		if (e.getSource().equals(pansharpPanel.getJSlider())) {
			pansharpPanel.getJTextField().setText(String.valueOf((pansharpPanel.getJSlider().getValue()/200.0)));
		}

		if (e.getSource().equals(pansharpPanel.getJSlider1())) {
			pansharpPanel.getJTextField1().setText(String.valueOf((pansharpPanel.getJSlider1().getValue()/2)));
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == pansharpPanel.getCbActiveSharpening()) {

			if (this.pansharpPanel.getCbActiveSharpening().isSelected()) {
				pansharpPanel.setTableEnabled(true);
			}else{
				pansharpPanel.setTableEnabled(false);
			}
		}
	}
				
		/* (non-Javadoc)
			 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
			 */
			public void tableChanged(TableModelEvent e) {
					pansharpPanel.getBandTable().revalidate();
					pansharpPanel.getRBandPane().revalidate();
			}

			// Control de si se ha aplicado el procedimiento de 
			private boolean isAplicado(){
				return aplicado;
			}
			
			// Aciones tras aceptar
			public void  apply(){
				try {
				setValuesFromPanelToFilter();
			} catch (FilterTypeException e) {
				RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_adding_filters"), this, e);
			}
				aplicado=true;
			}
			
			public void accept(){
				if(isAplicado()==false)
				try {
					setValuesFromPanelToFilter();
				} catch (FilterTypeException e) {
					RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_adding_filters"), this, e);
				}
				 pansharpPanel.getCbActiveSharpening().setSelected(false);
				// Se elimina el filtro de la lista de filtros, ya ha sido aplicado
				
			}
			
			public void cancel(){
				restoreStatus();
			}
		
			public void restoreStatus(){
				filterList.setStatus((ArrayList) panelGroup.getProperties().get("filterStatus"));

			if (fLayer != null)
				fLayer.getMapContext().invalidate();
			}
				 
			/**
		 * Carga los valores del filtro de Pansharpening desde el panel
			 * @throws FilterTypeException 
		 */
		private void setValuesFromPanelToFilter() throws FilterTypeException {
			
			RasterFilterListManager manager = new RasterFilterListManager(filterList);
			// PANSHARPENING FILTER
			PanSharpeningListManager bcManager = (PanSharpeningListManager) manager.getManagerByClass(PanSharpeningListManager.class);
		
			// Fichero de salida
			String fileName = RasterLibrary.usesOnlyLayerName();
			fileNameOutput = Utilities.createTempDirectory() + File.separator + fileName + ".tif";
			if (pansharpPanel.getCbActiveSharpening().isSelected()){		
				// Tomar los parametros de la interfas pharsharpPanel
				int posPancromatica= pansharpPanel.getBandTable().getSelectedRow();
				// Array con el orden de las bandas asignadas a RGB
				ArrayList orden = (ArrayList) panelGroup.getProperties().get("renderBands");
				String method = "brovey";
			 	if(pansharpPanel.getRbHSL().isSelected())
			 		method = "hsl";	
			 	double coef = 0D;
			 	if(pansharpPanel.getRbHSL().isSelected())
			 		coef = Double.parseDouble(pansharpPanel.getJTextField().getText());
			 	int coefBrovey = 0;
			 	if(pansharpPanel.getRbBrovey().isSelected())
			 		coefBrovey = Integer.parseInt(pansharpPanel.getJTextField1().getText());	
			 	//			 	Añadir el filtro 		
				if(posPancromatica!=-1)
					bcManager.addPanSharpeningFilter(dataset,posPancromatica, orden, 1, method, coef, coefBrovey, fileNameOutput);	
				else
				filterList.remove(PanSharpeningFilter.class);

				
				applyPansharpeningProcess();
			
			}

		}
			
		/**
		 * Lanza la ejecucion del filtro y carga el resultado en la vista
		 * @throws FilterTypeException  
		 */
		void applyPansharpeningProcess() throws FilterTypeException {
			
//			 Ejecucion del filtro de pansharpening
				try {
				filterList.getFilterByBaseClass(PanSharpeningFilter.class).execute();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Aplicado el filtro lo borramos de la lista
			filterList.remove(PanSharpeningFilter.class);
				
				String fileName=fileNameOutput;
			int endIndex = fileName.lastIndexOf(".");
			if (endIndex < 0)
				 endIndex = fileName.length();
			
			
			FLayer lyr = null;
			
			try {
			 
				 lyr = FLyrRasterSE.createLayer(fileNameOutput.substring(
						 fileNameOutput.lastIndexOf(File.separator) + 1, endIndex),new File(fileNameOutput), dataset.getDataSource().getDataset(0)[0].getProjection());
			} catch (LoadLayerException e) {
				 
				 JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),
				 PluginServices.getText(this, "error_cargar_capa"));
			 }
			fLayer.getMapContext().getLayers().addLayer(lyr);
				
			
		}
			
		
		private int hasFilter(ArrayList filter, String name) {
			for (int i = 0; i < filter.size(); i++) {
				if (((RasterFilter) filter.get(i)).getName().equals(name))
					return i;
			}
			return -1;
		}


}


