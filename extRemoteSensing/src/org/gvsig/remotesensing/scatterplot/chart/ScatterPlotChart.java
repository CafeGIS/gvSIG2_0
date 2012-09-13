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
import java.awt.geom.Point2D;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.FastScatterPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.data.Range;

/**
 * Clase que define el diagrama de puntos.  Personalización de un modelo concretro de
 * grafico extraido de jfreechart. Esta clase define sobreescribe de forma personalizada 
 * los metodos getRangeX() y getRangeY().
 * 
 * @see org.jfree.chart.JFreeChart FastScatterPlot
 * @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)  
 * @version 11/12/2007
 */
public class ScatterPlotChart extends FastScatterPlot
      
{

	private static final long serialVersionUID = 4140359543416276410L;


	/**
     * Crea un nuevo  FastScatterPlot
     * @param data  datos que se muestran en el diagrama (<code>null</code> permitted).
     * @param domainAxis  rango en eje x.
     * @param rangeAxis  rango en eje y.
     *   
     */
	public ScatterPlotChart(float[][] data, 
                           ValueAxis domainAxis, ValueAxis rangeAxis) {
		 
		super(data,domainAxis,rangeAxis);
		setPaint(Color.BLACK);
		
    }
	

    /**
     *  Metodo que devuelve el Rango de la seleccion en el eje x.
     * 
     **/
    public Range getRangeX(double lowerPercent, double upperPercent, 
            PlotRenderingInfo info, Point2D source) {
    		
    	   double start = super.getDomainAxis().getRange().getLowerBound();
           double length = super.getDomainAxis().getRange().getLength();
           Range adjusted = null;
           if (super.getDomainAxis().isInverted()) {
               adjusted = new Range(start + (length * (1 - upperPercent)), 
                                    start + (length * (1 - lowerPercent))); 
           }
           else {
               adjusted = new Range(start + length * lowerPercent, 
                       start + length * upperPercent);
           }
           return adjusted;
    }
 
    
    /**
     *  Metodo que devuelve el Rango de la seleccion en el eje y.
     * */
    public Range getRangeY(double lowerPercent, double upperPercent, 
            PlotRenderingInfo info, Point2D source) {
    	double start = super.getRangeAxis().getRange().getLowerBound();
        double length = super.getRangeAxis().getRange().getLength();
        Range adjusted = null;
        if (super.getRangeAxis().isInverted()) {
            adjusted = new Range(start + (length * (1 - upperPercent)), 
                                 start + (length * (1 - lowerPercent))); 
        }
        else {
            adjusted = new Range(start + length * lowerPercent, 
                    start + length * upperPercent);
        }
        return adjusted;
    }   
}
