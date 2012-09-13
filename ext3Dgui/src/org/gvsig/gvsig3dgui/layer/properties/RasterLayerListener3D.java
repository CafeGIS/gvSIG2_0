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


package org.gvsig.gvsig3dgui.layer.properties;

import javax.swing.JOptionPane;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gvsig3d.map3d.MapContext3D;

import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.cit.gvsig.fmap.layers.FLayer;

/**
* 
* @version 
* @author Ángel Fraile Griñán.(angel.fraile@iver.es)
*/

public class RasterLayerListener3D {

	private Layer3DProps 				props3D;
	private MapContext3D 				context;
	private RasterLayerPanel3D 			rasterLayerPanel3D;
	private FLayer layer;
	private boolean bCanBeElev;
	
	
	public RasterLayerListener3D() {
	}
	
	public RasterLayerListener3D(RasterLayerPanel3D panel) {
		this.rasterLayerPanel3D = panel;
	}
	/**
	 * Actions to execute when the accept option is pressed
	 */
	public void accept() {
		
		context = rasterLayerPanel3D.getMapContext();
		props3D = rasterLayerPanel3D.getLayer3DProps();
		
		if (rasterLayerPanel3D.isElevationOptionSelected()&&(props3D.getType()== Layer3DProps.layer3DImage)) {
			layer = rasterLayerPanel3D.getLayer();
			 if (layer instanceof FLyrRasterSE) {
					FLyrRasterSE rasterLayer = (FLyrRasterSE) layer;
					if (rasterLayer.getBandCount() == 1) {
				//		bCanBeElev = true;
						context.pepareLayerToRefresh(rasterLayerPanel3D.getLayer());
						props3D.setType(Layer3DProps.layer3DElevation);
						props3D.setCacheService(null);
						context.refreshLayer3DProps(rasterLayerPanel3D.getLayer());
						
						
//						int offset = 0,factor = 0;
//						try {
//							offset = (int)Double.parseDouble(rasterLayerPanel3D.getJTextFieldOffset());
//							factor = (int)Double.parseDouble(rasterLayerPanel3D.getJTextFieldFactor());
//						} catch (NumberFormatException e) {
//							// TODO: handle exception
//						}
//						if (offset > 0 && factor > 0) {
//							
//						}
						
					}
					else {
//						
						JOptionPane.showMessageDialog(null, "Capa sin propiedades de elevación",
								"Alertas de prueba", JOptionPane.WARNING_MESSAGE);
					}
					
			 }
			 else if(rasterLayerPanel3D.isTerrainOptionSelected()) {
				 
			 }else if(rasterLayerPanel3D.isDetailOptionSelected()) {
					
				}
				else
					return;
		}
		
	}

	/**
	 * Actions to execute when the apply option is pressed
	 */
	
	public void apply() {
		
		context = rasterLayerPanel3D.getMapContext();
		props3D = rasterLayerPanel3D.getLayer3DProps();
		
		if (rasterLayerPanel3D.isElevationOptionSelected()&&(props3D.getType()== Layer3DProps.layer3DImage)) {
			layer = rasterLayerPanel3D.getLayer();
			
				props3D.setLayer(layer);
		
		
				context.pepareLayerToRefresh(rasterLayerPanel3D.getLayer());
				props3D.setType(Layer3DProps.layer3DElevation);
			//	props3D.setCacheService(null);
				context.refreshLayer3DProps(rasterLayerPanel3D.getLayer());
		}
	}

	/**
	 * Actions to execute when the cancel option is pressed
	 */
	public void cancel() {
	}


}
