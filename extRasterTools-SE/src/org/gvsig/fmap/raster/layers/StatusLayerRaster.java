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
package org.gvsig.fmap.raster.layers;

import java.util.ArrayList;

import org.gvsig.raster.dataset.FileNotOpenException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.datastruct.TransparencyRange;
import org.gvsig.raster.grid.GridTransparency;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.RasterFilterListManager;
import org.gvsig.raster.grid.filter.bands.ColorTableFilter;
import org.gvsig.raster.grid.filter.enhancement.EnhancementStretchListManager;
import org.gvsig.raster.grid.filter.enhancement.LinearEnhancementFilter;
import org.gvsig.raster.grid.filter.enhancement.LinearStretchParams;
import org.gvsig.raster.grid.filter.statistics.TailTrimFilter;
import org.gvsig.raster.hierarchy.IRasterProperties;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.gui.IView;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

/**
 * Esta clase almacena el estado de un raster en cuanto a las caracteristicas
 * de opacidad, bandas y filtros. Estas características pueden ser salvadas a 
 * un xml y recuperadas por la capa a través de las funciones setXMLEntity y 
 * getXMLEntity
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class StatusLayerRaster implements IStatusRaster {

	public static String 			defaultClass = "org.gvsig.fmap.raster.layers.StatusLayerRaster";
	
	//Valor de opacidad global de la imagen
	public int						transparency = 255;
	
	//Rangos de transparencia
	public ArrayList				ranges = new ArrayList();
		
	//(Selección de bandas)Número de banda  asignado al Rojo, verde y azul 	
	public int 						bandR = 0;
	public int 						bandG = 1;
	public int 						bandB = 2;
	
	//Ficheros cargados en un PxRaster 
	public ArrayList				files = new ArrayList();
	
	//Filtros para poder montar una nueva pila
	public ArrayList				filters = new ArrayList();
	private IRasterProperties       layer = null;
		
	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.StatusRasterInterface#setXMLEntity(com.iver.utiles.XMLEntity)
	 */
	public void setXMLEntity(XMLEntity xml, IRasterProperties layer)throws XMLException {
		this.layer = layer;
		//RECUPERAR PROPIEDADES
		
		//Recuperamos las propiedades de los filtros
		for(int i = 0; i < xml.getPropertyCount(); i++) {
			if(xml.getPropertyName(i).startsWith("filter."))	
				filters.add(xml.getPropertyName(i) + "=" + xml.getPropertyValue(i));
		}
		
		//Rangos de transparencia
		if (xml.contains("filter.transparency.active") && xml.getBooleanProperty("filter.transparency.active")) {
			int i = 0;
			String value = null;
			while(true) {
				if(xml.contains("filter.transparency.transparencyRange" + i)) {
					value = xml.getStringProperty("filter.transparency.transparencyRange" + i);
					int alpha = 0;
					if(value.indexOf("@") != 0) {
						try {
							alpha = Integer.parseInt(value.substring(value.indexOf("@") + 1, value.length()));
						} catch (NumberFormatException e) {
							alpha = 0;
						}
						if(value.indexOf("@") != -1)
							value = value.substring(0, value.indexOf("@"));
					}
					TransparencyRange range = new TransparencyRange(value);
					if(alpha != 0)
						range.setAlpha(alpha);
					ranges.add(range);
				} else 
					break;
				i ++;
			}
		}
		
		if (xml.contains("raster.opacityLevel")) {
			transparency = xml.getIntProperty("raster.opacityLevel");
			//Esto soluciona un problema de compatibilidad entre branch v10 y HEAD. Eliminar en futuras versiones
			if(nameClass != null && nameClass.compareTo("com.iver.cit.gvsig.fmap.layers.StatusLayerRaster") == 0)
				transparency = 255 - transparency;
		}
				
		if (xml.contains("raster.bandR")) 
			bandR = xml.getIntProperty("raster.bandR");
		
		if (xml.contains("raster.bandG")) 
			bandG = xml.getIntProperty("raster.bandG");
		
		if (xml.contains("raster.bandB")) 
			bandB = xml.getIntProperty("raster.bandB");
		
	
		int cont = 0;
		while(true && cont < 50) {
			if (xml.contains("raster.file" + cont)) {
				files.add(xml.getStringProperty("raster.file" + cont));
				cont++;
			}else 
				break;
		}
	}
	

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.StatusRasterInterface#getXMLEntity(com.iver.utiles.XMLEntity)
	 */
	public void getXMLEntity(XMLEntity xml, boolean loadClass, IRasterProperties layer) throws XMLException {
		this.layer = layer;
		//SALVAR PROPIEDADES
		
		if(loadClass)
			xml.putProperty("raster.class", StatusLayerRaster.defaultClass);
		
		//Opacidad
		GridTransparency transp = layer.getRenderTransparency();
		if(transp != null) {
			if(transp.getOpacity() != 255)
				xml.putProperty("raster.opacityLevel", "" + transp.getOpacity());
			
			//Rangos de transparencia
			if(transp.getTransparencyRange().size() > 0) {
				xml.putProperty("filter.transparency.active", "true");
				for (int i = 0; i < transp.getTransparencyRange().size(); i++) 
					xml.putProperty("filter.transparency.transparencyRange" + i, "" + ((TransparencyRange)transp.getTransparencyRange().get(i)).getStrEntry() + "@" + ((TransparencyRange)transp.getTransparencyRange().get(i)).getAlpha());
			}
		}
		
		//Posición de visualizado de bandas
		xml.putProperty("raster.bandR", "" + layer.getRenderBands()[0]);
		xml.putProperty("raster.bandG", "" + layer.getRenderBands()[1]);
		xml.putProperty("raster.bandB", "" + layer.getRenderBands()[2]);
		
		//Ficheros
		if(files != null && layer.getFileCount() != 0) {
			for(int i = 0; i < layer.getFileCount(); i++)
				xml.putProperty("raster.file" + i, "" + layer.getFileName()[i]);
		}
		
		//Salvamos la lista de filtros aplicada en la renderización.
		//Si la lista es null (esto puede ocurrir cuando se abre un 
		//proyecto que tiene WCS y no se abre la vista de este) entonces hay que leer los filtros
		//que van a salvarse a disco directamente de la variable filters que es la que se ha cargado
		//al hacer el setXMLEntity.
		
		
		//Filtros
		RasterFilterListManager filterListManager = null;
		ArrayList l = null;
		if(layer.getRenderFilterList() != null) {
			filterListManager = new RasterFilterListManager(layer.getRenderFilterList());
			l = filterListManager.getStringsFromFilterList();
			if(l == null /*|| l.size() == 0*/)
				l = filters;
		}else
			l = filters;
					 
		for(int i = 0; i < l.size(); i++)
			xml.putProperty(getElem((String)l.get(i)), getValue((String)l.get(i)));
	}
	
	/**
	 * Obtiene el valor de una cadena de la forma elemento=valor
	 * @param cadena 
	 * @return
	 */
	private String getValue(String cadena) {
		if(cadena!=null)
			return cadena.substring(cadena.indexOf("=") + 1, cadena.length());
		else 
			return null;
	}
	
	/**
	 * Obtiene el elemento de una cadena de la forma elemento=valor
	 * @param cadena 
	 * @return
	 */
	private String getElem(String cadena) {
		if(cadena != null)
			return cadena.substring(0, cadena.indexOf("="));
		else 
			return null;
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.StatusRasterInterface#getFilters()
	 */
	public ArrayList getFilters() {
		return this.filters;
	}
	
	
	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.StatusRasterInterface#applyStatus(com.iver.cit.gvsig.fmap.layers.RasterFileAdapter)
	 */
	public void applyStatus(FLyrRasterSE layer) throws NotSupportedExtensionException, RasterDriverException, FilterTypeException {
										
		//Eliminamos el fichero inicial y cargamos las bandas si hay para que se carguen 
		//en el orden correcto
		if(layer instanceof FLyrRasterSE) {
			if(files != null && files.size() != 0){
				//((FLyrRasterSE)layer).delFile((String)files.get(0));
				for (int i = 1; i < files.size(); i++) 
					((FLyrRasterSE)layer).addFile((String)files.get(i));
			}
		}
		
		//Asigna las bandas
		int[] renderBands = new int[]{bandR, bandG, bandB};
		if(layer.getRender() != null)
			layer.getRender().setRenderBands(renderBands);
				
		//Asigna la transparencia
		GridTransparency transp = layer.getRender().getLastTransparency(); 
		if(transparency != 255) { 
			transp.setOpacity(transparency);
			transp.activeTransparency();
		}
		
		//Rangos de transparencia
		if(transp != null && ranges != null) {
			transp.setTransparencyRangeList(ranges);
			transp.activeTransparency();
		}
		
		//Filtros
		if (layer.getRenderFilterList() != null) {
			RasterFilterListManager filterListManager = new RasterFilterListManager(layer.getRenderFilterList());
			filterListManager.createFilterListFromStrings(filters);
			enhancedCompV10(filters, layer, filterListManager);
			//sortFilters(layer.getRenderFilterList());
		}
		
		//Refrescamos todas las vistas
		IWindow[] w = PluginServices.getMDIManager().getAllWindows();
		for (int i = 0; i < w.length; i++) {
			if(w[i] != null && w[i] instanceof IView) 
				((IView)w[i]).getMapControl().getMapContext().invalidate();	
		}
	}
	
	/**
	 * Método para mantener la compatibilidad con la v10 del realce. El realce de la v10 no
	 * aporta suficiente información por lo que se añade un filtro de realce generico v1.9
	 * @param filterArguments
	 * @param layer
	 * @param filterListManager
	 * @throws FilterTypeException
	 */
	public static void enhancedCompV10(ArrayList<String> filterArguments, FLyrRasterSE layer, RasterFilterListManager filterListManager) throws FilterTypeException {
		boolean removed = false;
		RasterFilterList list = layer.getRenderFilterList();
		for (int i = 0; i < list.lenght(); i++) {
			RasterFilter f = list.get(i); 
			if(f instanceof TailTrimFilter || f instanceof LinearEnhancementFilter) { 
				list.remove(f.getName());
				removed = true;
			}
		}
		if(removed)
			list.controlTypes();
		
		//Para compatibilidad de realce con proyectos antiguos
		for (int i = 0; i < filterArguments.size(); i++) { 
			if(((String)filterArguments.get(i)).startsWith("filter.enhanced.active=true")) {
				EnhancementStretchListManager elm = new EnhancementStretchListManager(filterListManager);
				try {
					elm.addEnhancedStretchFilter(LinearStretchParams.createStandardParam(layer.getRenderBands(), 0.0, layer.getDataSource().getStatistics(), false), 
												layer.getDataSource().getStatistics(), 
												layer.getRender().getRenderBands(), 
												false);
				} catch (FileNotOpenException e) {
					//No podemos aplicar el filtro
				} catch (RasterDriverException e) {
					//No podemos aplicar el filtro
				}
			}
		}
	}
	
	/**
	 * Al cargar filtros desde un proyecto salvado con la V10 los filtros pueden estar desordenados.
	 * Colocamos los más importantes en su posición adecuada.
	 * @throws FilterTypeException 
	 *
	 */
	private void sortFilters(RasterFilterList list) throws FilterTypeException {
		for (int i = 0; i < list.lenght(); i++) {
			RasterFilter f = list.get(i); 
			if(f instanceof TailTrimFilter) {
				list.remove(f.getName());
				list.add(f, 0);
			}
		}
		
		for (int i = 0; i < list.lenght(); i++) {
			RasterFilter f = list.get(i);
			if(f instanceof LinearEnhancementFilter && list.get(0) instanceof TailTrimFilter) {
				list.remove(f.getName());
				list.add(f, 1);
			} else if(f instanceof LinearEnhancementFilter) {
				list.remove(f.getName());
				list.add(f, 0);
			}
		}
		list.controlTypes();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.layers.IStatusRaster#getRenderBands()
	 */
	public int[] getRenderBands() {
		return new int[]{bandR, bandG, bandB};
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.layers.IStatusRaster#getFilterList()
	 */
	public RasterFilterList getFilterList() throws FilterTypeException {
		try {
			RasterFilterListManager filterListManager = null;
			if(layer.getRenderFilterList() != null)
				filterListManager = new RasterFilterListManager(layer.getRenderFilterList());
			else {
				RasterFilterList fl = new RasterFilterList();
				fl.setInitDataType(IBuffer.TYPE_BYTE);
				filterListManager = new RasterFilterListManager(fl);
			}
			filterListManager.createFilterListFromStrings(filters);
			return filterListManager.getFilterList();
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.layers.IStatusRaster#getColorTable()
	 */
	public ColorTable getColorTable() throws FilterTypeException {
		RasterFilterList fl = getFilterList();
		for (int i = 0; i < fl.lenght(); i++) {
			if(fl.get(i) instanceof ColorTableFilter) 
				return (ColorTable) fl.get(i).getParam("colorTable");
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.layers.IStatusRaster#getTransparency()
	 */
	public GridTransparency getTransparency() {		
		//Asigna la transparencia
		GridTransparency transp = new GridTransparency();
		if(transparency != 255) { 
			transp.setOpacity(transparency);
			transp.activeTransparency();
		}
		
		//Rangos de transparencia
		if(ranges != null) {
			transp.setTransparencyRangeList(ranges);
			transp.activeTransparency();
		}
		return (transp.isTransparencyActive()) ? transp : null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.layers.IStatusRaster#getFilterArguments()
	 */
	public ArrayList getFilterArguments() {
		return filters;
	}
	
	String nameClass = null;
	/**
	 * Asigna el nombre de la clase que se ha leido desde el proyecto
	 * @param nameClass
	 */
	public void setNameClass(String nameClass) {
		this.nameClass = nameClass;		
	}
}