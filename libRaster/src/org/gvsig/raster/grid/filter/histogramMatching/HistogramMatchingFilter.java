/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
 *   Av. Blasco Ibañez, 50
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

package org.gvsig.raster.grid.filter.histogramMatching;

import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.datastruct.Histogram;
import org.gvsig.raster.datastruct.HistogramException;
import org.gvsig.raster.grid.filter.RasterFilter;

/**
 * Filtro para la aplicación de HistogramMatching a un raster.
 * 
 * @author aMuÑoz (alejandro.munoz@uclm.es)
 * @version 27-5-2008
 * 
 * */

public class HistogramMatchingFilter  extends RasterFilter {

	public static String[] 				names 					= new String[] {"HistogramMatch"};

	Histogram histogramReference = null;
	Histogram histogramSource= null;
	double [][] acumulateS= null;
	double [][]acumulateR = null;
	String fileNameOutput = null;
	byte tableAsign[][]= null;
	int numbands= 0;
	
	/** Constructor */
	public HistogramMatchingFilter() {
			super();
	}

	public String getGroup() {
		return "HistogramMatch";
	}

	public int getInRasterDataType() {
		return 0;
	}

	public String[] getNames() {
		return names;			
	}

	public int getOutRasterDataType() {
		return RasterBuffer.TYPE_BYTE;
	}


	public Params getUIParams(String nameFilter) {
		Params params = new Params();
		return params;
	}

	
	 public boolean isVisible() {
			return false;
	 }
	
	
	/**
	 * Acciones antes de la ejecución del filtro
	 * */
	public void pre() {	
		// Carga de los parametros del filtro y obtencion de histogramas
		loadParam();
		
		// Funcion que calcula la correspondencia entre los histogramas, El resultado es tableAsign completa
		// para cada clase en el histograma fuente asigan su clase en el histograma corregido.
		double inValue=0;
		tableAsign= new byte[numbands][256];
		
		// Cálculo de la tabla de asignaciones entre los histogramas
		for (int band=0; band <numbands; band++){
			for(int i= 0; i< tableAsign[0].length; i++){
				inValue = acumulateS[band][i];
				int value= (searchInHistogramReference(inValue,band));
				tableAsign[band][i]= (byte)value;
			}
		}
	}
	
	
	/**
	 * Acciones posteriores a la ejecución del filtro
	 * */
	public void post() {		
	}



	public Object getResult(String name) {
		return rasterResult;
	}
	
	
	/**
	 *  Se recogen los parametros necesarios para la aplicacion del filtro. 
	 * 	histogramReference histograma de referencia.
	 *	fileNameOutput 
	 *  
	 */
	protected void loadParam(){	
		// Raster
		raster= (RasterBuffer) params.get("raster"); 
		height= raster.getHeight();
		width= raster.getWidth();
		try {

			histogramSource= raster.getHistogram();
			histogramSource = Histogram.convertHistogramToRGB(histogramSource);
			
			// COMPROBACION HISTOGRAMA RGB
			if (!histogramSource.isInRangeRGB())
				return;
			
			// Histograma de referencia
			histogramReference= (Histogram)params.get("histogramReference");
			histogramReference = Histogram.convertHistogramToRGB(histogramReference);
			//numbands = (int)params.get("numbads");
			numbands=((Integer)params.get("numbands")).intValue();
			// Histogramas acumilados y normalizados 
			acumulateS = Histogram.convertTableToNormalizeAccumulate(histogramSource.getTable());
			acumulateR= Histogram.convertTableToNormalizeAccumulate(histogramReference.getTable());
			
		} catch (HistogramException e) {
		
		} catch (InterruptedException e) {
			
		} 
		rasterResult= RasterBuffer.getBuffer(RasterBuffer.TYPE_BYTE,raster.getWidth(),raster.getHeight(),numbands,true);
		fileNameOutput= (String)params.get("fileNameOutput");
	}


	/**
	 * Método que realiza la correspondencia entre las clases de los histogramas
	 * */
	public int searchInHistogramReference(double value,int band){
		int i=0;
		while(value>acumulateR[band][i]){
			i++;
			if(i==255) 
				return (int)255;
		}
		if(i==0)
			return 0;
		if((acumulateR[band][i]-value) < (acumulateR[band][i-1]-value))
			return (int)i;
		else 
			return (int)(i-1);
	}

	public void process(int x, int y) {
	}
	
}
