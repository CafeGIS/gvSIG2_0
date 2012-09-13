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
import java.util.HashMap;

import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.bands.ColorTableFilter;
import org.gvsig.raster.util.RasterUtilities;

/**
 * Aplica estrategias de dibujado para las capas raster. Hace que las 
 * capas que están ocultas por otras no sean dibujadas. Esta estrategia tiene que ser
 * aplicada a capas raster de FMap por falta de eficiencia en el dibujado. Esta clase
 * es totalmente dependiente de FMap y puede ser eliminada en caso de que este no exita.
 * 28/11/2007
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class RasterDrawStrategy {

	/**
	 * Cada mapContext existente lleva asociadad una estrategia de dibujado
	 */
	public static HashMap        mapContextStrategy    = new HashMap();
	
	private MapContext           mapContext            = null;
	private FLyrRasterSE         lyrRaster             = null;

	/**
	 * Estructura de datos para almacenar la asociación entre el extent de una capa
	 * y su información de transparencia. Esta es necesaria para la comprobación de si la 
	 * capa con la que se está intersectando tiene transparencia o no la tiene. Con esto 
	 * se puede decidir si dibujar la capa actual o no.
	 * 16/06/2008
	 * @author Nacho Brodin nachobrodin@gmail.com
	 */
	public class LayerIntersection {
		public Extent  extent          = null;
		public boolean hasTransparency = false;
		/**
		 * Constructor. Asigna el Extent y la información de transparencia
		 * @param ext Extent
		 * @param transp Transparencia
		 */
		public LayerIntersection(Extent ext, boolean transp) {
			this.extent = ext;
			this.hasTransparency = transp;
		} 
	}
	/**
	 * Al constructor le pasamos el contexto de dibujado para la toma de decisión.
	 * @param mapContext Context
	 * @throws ExpansionFileReadException
	 * @throws ReadDriverException
	 */
	public RasterDrawStrategy(MapContext mapContext, FLyrRasterSE lyrRaster) {
		this.mapContext = mapContext;
		this.lyrRaster = lyrRaster;
	}
	
	/**
	 * <P>
	 * Estrategia de dibujado para las capas raster que hace que si
	 * una capa está oculta completamente por otra que no tiene transparencias entonces
	 * esta no será dibujada.
	 * </P>
	 * <P>
	 * La estrategia la calcula solo la primera capa raster que aparezca. El resto
	 * de las capas la preguntaran a esta.
	 * </P>
	 * <P>
	 * <code>Estrategia:</code>
	 * Analizamos la lista de capas desde la primera que se dibuja en el TOC hasta la última
	 * <UL>
	 * <LI>Si la capa tiene rotación se dibuja siempre.</LI>
	 * <LI>Si la capa no está activa en el TOC no se dibuja nunca</LI>
	 * <LI>Si la capa no es transparente y no hay extents en la lista se dibuja y se añade el extent a la lista</LI>
	 * <LI>Si el extent de la capa intersecta con uno de la lista</LI>
	 * </UL>
	 * Comprobamos si está detrás de un extent de la lista. Si es así no se dibuja
	 * Si no está está detrás de un extent comprobamos si tiene transparencias
	 * Si no tiene transparencias añadimos el extent a la lista
	 * </P>
	 * @param lyrs
	 * @throws ReadDriverException 
	 * @throws ExpansionFileReadException 
	 */
	public void stackStrategy() {
		if(mapContext == null || lyrRaster == null)
			return;
		
		ArrayList listLayers = new ArrayList();
		FLayers lyrs = mapContext.getLayers();
		listLayers = getLayerList(lyrs, listLayers);
		
		ArrayList extentList = new ArrayList();
				
		//Solo la primera capa calcula la estrategia. Las demás se la preguntan a esta.
		
		//Calculamos cual es la primera capa raster de FLayers y contamos cuantas capas raster hay
		int posFirstRasterLayer = 0;
		int nRasterLayers = 0;
		boolean firstTime = true;
		for (int i = 0; i < listLayers.size(); i++) {
			FLayer lyr = (FLayer) listLayers.get(i);
			if(firstTime && lyr instanceof FLyrRasterSE && lyr.isVisible()) {
				posFirstRasterLayer = i;
				firstTime = false;
			}
			if(lyr instanceof FLyrRasterSE)
				nRasterLayers ++;
		}
		
		//Si hay solo una capa raster no se calcula ninguna estrategia
		if(nRasterLayers == 1) {
			mapContextStrategy.put(mapContext, null);
			return;
		}
		
		//Si no es la primera capa se obtiene la estrategia que calculó la primera capa
		if(lyrRaster != listLayers.get(posFirstRasterLayer)) {
			if(!(listLayers.get(posFirstRasterLayer) instanceof FLyrRasterSE))
				return;
			mapContextStrategy.put(mapContext, ((FLyrRasterSE)listLayers.get(posFirstRasterLayer)).getRasterStrategy());
			return;
		}
		
		HashMap layersToPaint = new HashMap();
		
		//Calculo de estrategia. Solo llega aquí la primera capa.
		//La lista de capas en FLayers está en orden inverso a lo que aparece en el TOC, es decir, la última capa
		//del TOC es la primera de la lista de FLayers y la primera del TOC es la última de la lista.
		for (int i = (listLayers.size() - 1); i >= 0; i--) {
			FLayer lyr = (FLayer) listLayers.get(i);
			
			if (!(lyr instanceof FLyrRasterSE))
				continue;
			
			FLyrRasterSE rLyr = ((FLyrRasterSE)lyr);
			
			if(rLyr.getRenderFilterList() == null || rLyr.getFullEnvelope() == null)
				continue;
			
			//Si no está activa y/o visible no se dibuja nunca 
			if(!rLyr.isVisible()) {
				layersToPaint.put(rLyr, new Boolean(false));
				continue;
			}
			
			//Si tiene rotación se dibuja siempre
			if(rLyr.getAffineTransform() != null) {
				if(	rLyr.getAffineTransform().getShearX() != 0 || 
						rLyr.getAffineTransform().getShearY() != 0) {
					layersToPaint.put(rLyr, new Boolean(true));
					continue;
				}
			}

			//Si es la primera metemos el extent en la lista y se dibuja
			//Si no es la primera y no intersecta con ningún extent. Metemos el extent y dibujamos			
			LayerIntersection li = areIntersecting(extentList, rLyr);
			if((i == (listLayers.size() - 1)) || li == null) {
				boolean colorTableAlpha = false;
				if(rLyr.getRenderFilterList() != null) { 
					RasterFilter rf = rLyr.getRenderFilterList().get("colortable");
					if(rf != null && rf instanceof ColorTableFilter) 
						colorTableAlpha = ((ColorTableFilter)rf).getColorTable().hasAlpha();
				}
				extentList.add(new LayerIntersection(rLyr.getFullRasterExtent(), (rLyr.existsAlphaBand() || rLyr.isTransparent() || colorTableAlpha)));
				layersToPaint.put(rLyr, new Boolean(true));
				continue;
			}
			
			//Si con la que intersecta no tiene transparencia no se dibuja
			if(li != null && !li.hasTransparency) {
				layersToPaint.put(rLyr, new Boolean(false));
				continue;
			}
			
			if(!rLyr.isTransparent())
				li.hasTransparency = false;
			layersToPaint.put(rLyr, new Boolean(true));			
		}
		
		mapContextStrategy.put(mapContext, layersToPaint);
	}
	
		
	/**
	 * Obtiene la lista de capas del TOC. Si hay capas agrupadas lo tiene en cuenta y mira
	 * dentro de la agrupación.
	 * @param srcLyrs
	 * @param destLyrs
	 * @return
	 */
	public static ArrayList getLayerList(FLayers srcLyrs, ArrayList destLyrs) {
		for (int i = 0; i < srcLyrs.getLayersCount(); i++) {
			if(srcLyrs.getLayer(i) instanceof FLyrRasterSE)
				destLyrs.add(srcLyrs.getLayer(i));
			if(srcLyrs.getLayer(i) instanceof FLayers)
				destLyrs = getLayerList((FLayers)srcLyrs.getLayer(i), destLyrs);
		}
		return destLyrs;
	}
	
	/**
	 * Comprueba si la capa que se pasa por parámetro está oculta por alguno
	 * de los extent de la lista
	 * @param extentList Lista de extensiones
	 * @param lyr Capa raster
	 * @return 
	 */
	private LayerIntersection areIntersecting(ArrayList extentList, FLyrRasterSE lyr) {
		for (int i = 0; i < extentList.size(); i++) {
			Extent ex = lyr.getFullRasterExtent();
			Extent ex1 = ((LayerIntersection)extentList.get(i)).extent;
			if(RasterUtilities.isInside(ex, ex1))
				return ((LayerIntersection)extentList.get(i));
		}
		return null;
	}
		
	/**
	 * Obtiene un TreeMap con la lista de capas que se dibujan. Si se al TreeMap se
	 * le pasa como parámetro una capa raster y devuelve null es que no se dibuja.
	 *  
	 * @return TreeMap con la lista de capas a dibujar.
	 */
	public HashMap getStrategy() {
		return (HashMap)mapContextStrategy.get(mapContext);
	}
}
