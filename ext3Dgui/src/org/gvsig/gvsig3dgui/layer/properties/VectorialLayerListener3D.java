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

import java.awt.geom.Rectangle2D;

import org.apache.log4j.Logger;
import org.gvsig.gvsig3d.map3d.MapContext3D;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.fmap.layers.FLayer;

/**
* 
* @version 
* @author Ángel Fraile Griñán.(angel.fraile@iver.es)
*/

public class VectorialLayerListener3D {

	private VectorialLayerPanel3D vectorialLayerPanel3D = null;
	private Layer3DProps props3D;
	private MapContext3D context;
	private FLayer flyr;
	private static Logger logger = Logger.getLogger(VectorialLayerListener3D.class
			.getName());
	
	public VectorialLayerListener3D(VectorialLayerPanel3D panel) {
		this.vectorialLayerPanel3D = panel;
	}
	
	/**
	 * Actions to execute when the accept option is pressed
	 */
	public void accept() {
		
		context = vectorialLayerPanel3D.getMapContext();
		props3D = vectorialLayerPanel3D.getLayer3DProps();
		flyr = vectorialLayerPanel3D.getLayer();
		
		//Rastering the layer
		if (vectorialLayerPanel3D.isRasterOptionSelected() && (props3D.getType()!=Layer3DProps.layer3DImage)) {
			context.pepareLayerToRefresh(flyr);
			props3D.setType(Layer3DProps.layer3DImage);
			props3D.setCacheService(null);
			context.refreshLayer3DProps(flyr);
		} else if ((!vectorialLayerPanel3D.isZValueSelected())&&(!vectorialLayerPanel3D.isRasterOptionSelected())) {
					int new_h = 0;
					int actual_h = 0;
					
					try {
						actual_h = (int)Double.parseDouble(vectorialLayerPanel3D.getMyHeight());
						new_h = (int)Double.parseDouble(vectorialLayerPanel3D.getJTextFieldHeight());
					} catch (NumberFormatException e) {
						logger.error("Command: "
								+ "Error getting maximun range layer elevation value.", e);
					}
					
					if ((new_h > 0 && new_h!= actual_h) || (actual_h == vectorialLayerPanel3D.getDefaultElevation()) ) //aplicando una altura distinta, si es la misma no se hace nada
					{
						context.pepareLayerToRefresh(flyr);
						props3D.setZEnable(false);
						props3D.setType(Layer3DProps.layer3DVector);
						props3D.setCacheService(null);
						props3D.setHeigth(new_h);
						context.refreshLayer3DProps(flyr);
					}//if
			//else if		
			} else if(vectorialLayerPanel3D.isZValueSelected() && props3D.getType()!= Layer3DProps.layer3DVector){//Value Z associated to the layer
				context.pepareLayerToRefresh(flyr);
				props3D.setType(Layer3DProps.layer3DVector);
				props3D.setCacheService(null);
				props3D.setZEnable(true);
				context.refreshLayer3DProps(flyr);
				
			}//else 
				
			if(vectorialLayerPanel3D.isDetailOptionSelected()) {
				Rectangle2D planetExtent = null;
				Rectangle2D extentLayer = null;
				
				try {
					planetExtent = context.getPlanet().getExtent();
					extentLayer = flyr.getFullExtent();
				} catch (ExpansionFileReadException e) {
					e.printStackTrace();
				} catch (ReadDriverException e) {
					e.printStackTrace();
				}
				double extPW = planetExtent.getWidth();	// planet width 
				double extPH = planetExtent.getHeight();// planet hight
				double extW = extentLayer.getWidth();	// layer width
				double extH = extentLayer.getHeight();	// layer hight
				int depth = 0;
				
				while(extPW > extW && extPH > extH) {// Calculating level of depth (div x /(2 pow n))
					
					extPW = extPW/2;
					extPH = extPH/2;
					
					depth++;//level of depth
				}//while
				
				int order = props3D.getTocOrder();
 				context.getPlanet().setMaxTextureResolution(order, depth);//setting new depth to the planet
			}//if
			else {
				int order = props3D.getTocOrder();
				context.getPlanet().setMaxTextureResolution(order, vectorialLayerPanel3D.getDefaultDepth());
			}	
			
			if(vectorialLayerPanel3D.isRangeOptionSelected()) {
				
				int new_max = 0;
				int new_min = 0;
				
				int order = props3D.getTocOrder();
				try {
					new_max = (int)Double.parseDouble(vectorialLayerPanel3D.getJTextFieldRangeMax());
					new_min = (int)Double.parseDouble(vectorialLayerPanel3D.getJTextFieldRangeMin());
				} catch (NumberFormatException e) {
					logger.error("Command: "
							+ "Number format must be a integer.", e);
				}
				
				if ((new_min > vectorialLayerPanel3D.getDefaultMinRange() || new_max < vectorialLayerPanel3D.getDefaultMaxRange())) //aplicando una altura distinta, si es la misma no se hace nada
				{
					order = props3D.getTocOrder();
					
					context.getPlanet().setMaxTextureRange(order, new_max);
					context.getPlanet().setMinTextureRange(order, new_min);
				}//if
				
			}//if
			else {
				int order = props3D.getTocOrder();
				context.getPlanet().setMaxTextureRange(order, vectorialLayerPanel3D.getDefaultMaxRange());
				context.getPlanet().setMinTextureRange(order, vectorialLayerPanel3D.getDefaultMinRange());
			}//else
				
	}

	/**
	 * Actions to execute when the apply option is pressed
	 */
	
	public void apply() {
		
		accept();
	}

	/**
	 * Actions to execute when the cancel option is pressed
	 */
	public void cancel() {
		
	}
	
}
