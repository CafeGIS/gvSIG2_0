/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2007 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
*
* For more information, contact:
*
*  Generalitat Valenciana
*   Conselleria d'Infraestructures i Transport
*   Av. Blasco Ibáñez, 50
*   46010 VALENCIA
*   SPAIN
*
*      +34 963862235
*   gvsig@gva.es
*      www.gvsig.gva.es
*
*    or
*
*   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
*   Campus Universitario s/n
*   02071 Alabacete
*   Spain
*
*   +34 967 599 200
*/

package org.gvsig.remotesensing.scatterplot.chart;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.iver.andami.PluginServices;

/**
 * Clase que representa la lista de ROICharts asociadas a un grafico.
 * 
 * @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)  
 * @version 11/12/2007
 */

public class ROIChartList {

	private HashMap listRois = null;
	private static Color[]   colors  = new Color[] {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.CYAN,
		Color.ORANGE, Color.PINK, Color.WHITE, Color.BLACK};
	
	private int nextColor=-1;
	private int defaultName=-1;
	
	
	/**
	 * Constructor de una lista de rois graficas para el diagrama
	 * */
	public ROIChartList() {
		listRois= new HashMap();
	}

	/**
	 *  @return hashmap con la lista de rois 
	 * */
	public HashMap getListRois(){
		return listRois;	
	}
	
	
	/**
	 * Añade una nueva roiChart al la lista
	 * */
	public void add(ROIChart roiChart){
		if(roiChart!=null)
			listRois.put(roiChart.getName(),roiChart);
		
	}
	
	/**
	 * Borra una roiChart d e la lista
	 * */
	public void deleteROI(Object nameRoi){
		listRois.remove(nameRoi);
		
	}
	
	
	/**
	 *  @return arraylist con la coleccion de roiChart asociadas al grafico
	 * */
	public ArrayList getList(){
		
		ArrayList list= new ArrayList();
		Collection ob= listRois.values();
		Iterator iterator =ob.iterator();
		 while(iterator.hasNext()){
				 list.add((ROIChart)iterator.next());
			 }
		return list;
		
	}
	
	
	/**
	 *  @ return color siguiente en  la secuencia de definición de rois
	 * */
	public Color getNexColor(){
		nextColor++;
		if(nextColor<9)
			return colors[nextColor];
		else
			return new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
	}
	

	/**
	 * Nombre por defecto para la siguiente roichart de la lista
	 * @return nombre correspondiente a la roi
	 * */
	
	public String getdefaultName(){
		defaultName++;
		return PluginServices.getText(this,"roi_chart")+defaultName;
	}

}
