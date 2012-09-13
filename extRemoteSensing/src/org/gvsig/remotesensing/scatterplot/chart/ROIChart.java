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
import java.awt.Shape;
import java.util.ArrayList;

import org.jfree.data.Range;

/**
 * Clase que representa una Region de interes definida sobre el grafico.
 * 
 * @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)  
 * @version 11/12/2007
 */

public class ROIChart {

	private ArrayList shapeList= null;
	private ArrayList rangos = null;
	private Color roiColor=null;
	private String name= null;
	private int bandaX = 0;
	private int bandaY=0;
	
	/**
	 * Constructor
	 * */
	
	ROIChart(Color color, String name, int[] bands){
		roiColor= color;
		shapeList= new ArrayList();
		rangos = new ArrayList();
		// bandas para las que se definio la roi
		bandaX=bands[0];
		bandaY= bands[1];
		this.name = name;
	}
	
	
	/**
	 *  Añade un shape a la lista de shapes de la roi. 
	 *  @shape shape que se añade
	 *  @range intervalo  abarcado por el shape  en los ejes x e y
	 * */
	void add(Shape shape, Range[] range){
		shapeList.add(shape);
		rangos.add((Range[])range);
	}
	
	
	/**
	 *  @return color de la roi
	 * */
	public Color getColor(){
		return roiColor;
	}
	
	
	/**
	 * Asigna el color a la roi
	 * @param color asignado
	 * */
	public void setColor(Color color){
		roiColor= color;
		
	}
	
	
	
	/**
	 * Asigna el nombre de la roichart
	 * */
	
	public void setName(String roiName){
		name= roiName;
		
	}
	
	/**
	 *  @return lista con los rangos
	 * */
	public ArrayList getRanges(){
		return rangos;
		
	}
	
	
	/**
	 *  @return lista con los shapes
	 * */
	public ArrayList getShapesList(){
		return shapeList;
	}
	
	
	/**
	 * @return nombre de la roiChart
	 * */
	public String getName(){
		return name;
		
	}


	public int getBandaX() {
		return bandaX;
	}


	public int getBandaY() {
		return bandaY;
	}
	
	
	public void setBandX(int bandX){
		this.bandaX= bandX;	
	}
	
	public void setBandY(int bandY){
		this.bandaY= bandY;	
	}
}
