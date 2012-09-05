/*
 * Created on 25-ene-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.readers.v15;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgLwPolyline;


/**
 * @author alzabord
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DwgLwPolylineReader15 extends AbstractDwg15Reader {

	/*
	 *TODO El metodo original de J.Morell daba excepciones (que eran
	 *ocultadas con un catch(Exception){}...BUF!
	 *
	 * Este metodo trata de copiar literalmente el de Python, para ver
	 * si somos capaces de resolver el bug.
	 * 
	 * */
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		
			if (!(dwgObj instanceof DwgLwPolyline))
				throw new RuntimeException(
						"ArcReader 15 solo puede leer DwgLwPolyLine");
			DwgLwPolyline line = (DwgLwPolyline) dwgObj;
			List val = null;
			int bitPos = offset;
			bitPos = headTailReader.readObjectHeader(data, bitPos, dwgObj);
			val = DwgUtil.getBitShort(data, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			int flag = ((Integer) val.get(1)).intValue();
			line.setFlag(flag);
			
			/*
			 * Flag son 4 bits que indican si la lwpline tiene ancho,
			 * (3er bit), elevacion (4o bit), grosor (2º bit) y vector
			 * de extrusion (1er bit).
			 * 5º bit -> nº de bulges
			 * 60 bit -> nº de widths
			 * 
			 * ahora se verifican y se van leyendo
			 * */
			
			
			double constWidth = 0d;
		    if( (flag & 0x4) > 0){
		    	val = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				constWidth = ((Double) val.get(1)).doubleValue();
		    }//if
		    line.setConstWidth(constWidth);
		    
		    double elev = 0d;
		    if ( (flag & 0x8) > 0){
		    	val = DwgUtil.getBitDouble(data, bitPos);
		    	bitPos = ((Integer) val.get(0)).intValue();
		    	elev = ((Double) val.get(1)).doubleValue();
		    }
		    line.setElevation(elev);
		    
		    double thickness = 0d;
		    if ( (flag & 0x2) > 0){
		    	val = DwgUtil.getBitDouble(data, bitPos);
		    	bitPos = ((Integer) val.get(0)).intValue();
		    	thickness = ((Double) val.get(1)).doubleValue();
		    }
		    line.setThickness(thickness);
		    
		    double nx = 0d; 
		    double ny = 0d;
		    double nz = 0d;
		    if( (flag & 0x1) > 0){
		    	val = DwgUtil.getBitDouble(data, bitPos);
		    	bitPos = ((Integer) val.get(0)).intValue();
		    	nx = ((Double) val.get(1)).doubleValue();
		    	
		    	val = DwgUtil.getBitDouble(data, bitPos);
		    	bitPos = ((Integer) val.get(0)).intValue();
		    	ny = ((Double) val.get(1)).doubleValue();
		    	
		    	val = DwgUtil.getBitDouble(data, bitPos);
		    	bitPos = ((Integer) val.get(0)).intValue();
		    	nz = ((Double) val.get(1)).doubleValue();
		    }
		    line.setNormal(new double[]{nx, ny, nz});
		  
		    val = DwgUtil.getBitLong(data, bitPos);
		    bitPos = ((Integer) val.get(0)).intValue();
		    int numberOfPoints = ((Integer) val.get(1)).intValue();
		    
		    int numberOfBulges = 0;
		    if ((flag & 0x10) > 0){
		    	val = DwgUtil.getBitLong(data, bitPos);
		 	    bitPos = ((Integer) val.get(0)).intValue();
		 	    numberOfBulges = ((Integer) val.get(1)).intValue();
		    }
		    
		    int numberOfWidths = 0;
		    if ((flag & 0x20) > 0){
		    	val = DwgUtil.getBitLong(data, bitPos);
		 	    bitPos = ((Integer) val.get(0)).intValue();
		 	    numberOfWidths = ((Integer) val.get(1)).intValue();
		    }
		    
		    if(numberOfPoints > 0){
		    	//Esto es una chapuza, pero las LwPolylines no se están
		    	//leyendo bien y en ocasiones nos llegan entidades
		    	//con MILLONES de puntos (OutOfMemoryException)
		    	//Plantear en la lista de PythonCAD
		    	if(numberOfPoints > 10000)
		    		throw new CorruptedDwgEntityException("LwPolyline corrupta");
		    	List vertices = new ArrayList();
		  	    val = DwgUtil.getRawDouble(data, bitPos);
		  	    bitPos = ((Integer) val.get(0)).intValue();
		  		double x0 = ((Double) val.get(1)).doubleValue();
		  		
		  		val = DwgUtil.getRawDouble(data, bitPos);
		  		bitPos = ((Integer) val.get(0)).intValue();
		  		double y0 = ((Double) val.get(1)).doubleValue();
		  		
		  		vertices.add(new double[]{x0, y0});
		  		
		  		
		  		/*
		  		 * TODO azabala 
		  		 * Algunos metodos de DwgUtil lanzan excepciones inexperadas
		  		 * cuando trabajan para rellenar clases de LwPolyline:
		  		 * ->getDefaultDouble
		  		 * ->getBits
		  		 * etc
		  		 * 
		  		 * Estas excepciones son del tipo OutOfBounds (se intenta leer un bit
		  		 * cuyo orden excede de la capacidad del array)
		  		 * La especificación 15 de DWG es seguida al pie de la letra por Pythoncad,
		  		 * (y por nosotros) así que puede ser que el problema esté en los metodos
		  		 * de DwgUtil.
		  		 * 
		  		 * Intentar capturar la excepcion para que no se pierdan las entidades que las
		  		 * provocan, y luego solucionar el error
		  		 * 
		  		 * 
		  		 * */
		  		for (int i = 1; i < numberOfPoints; i++) {
		  			val = DwgUtil.getDefaultDouble(data, bitPos, x0);
		  			bitPos = ((Integer) val.get(0)).intValue();
		  			double x = ((Double) val.get(1)).doubleValue();
		  			
		  			val = DwgUtil.getDefaultDouble(data, bitPos, y0);
		  			bitPos = ((Integer) val.get(0)).intValue();
		  			double y = ((Double) val.get(1)).doubleValue();
		  			
		  			vertices.add(new double[]{x, y});
		  			
		  			x0 = x;//se proporcionan como valores por defecto las coordenadas del pto anterior
		  			y0 = y;
		  		}
		  		line.setVertices(vertices);
		    }else{
		    	System.out.println("vertices == 0: lanzamos una excepcion??");
		    }
		    
		  
			
			if(numberOfBulges > 0){
				if (numberOfBulges != numberOfPoints){
			    	System.out.println("bulges != vertices: lanzamos una excepcion??");
			    }
				double[] bulges = new double[numberOfBulges];
				for(int i = 0; i < numberOfBulges; i++){
					//TODO OJOOOOOO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					//En PythonCAD se hacia getRawDouble, pero en la especificacion
					//(pagina 136) dice que es BitDouble (BD) 
					val = DwgUtil.getBitDouble(data, bitPos);
					bitPos = ((Integer) val.get(0)).intValue();
					bulges[i] = ((Double) val.get(1)).doubleValue();
				}
				line.setBulges(bulges);
			}else{
				double[] bulges = new double[numberOfPoints];
				for(int i = 0; i < numberOfPoints; i++)
					bulges[i] = 0d;
				line.setBulges(bulges);
			}
			
			if(numberOfWidths > 0){
			    if (numberOfWidths != numberOfPoints){
			    	System.out.println("widths != vertices: lanzamos una excepcion??");
			    }
				double[][] widths = new double[numberOfWidths][2];
				for (int i = 0; i < numberOfWidths; i++) {
					val = DwgUtil.getBitDouble(data, bitPos);
					bitPos = ((Integer) val.get(0)).intValue();
					double sw = ((Double) val.get(1)).doubleValue();
					
					val = DwgUtil.getBitDouble(data, bitPos);
					bitPos = ((Integer) val.get(0)).intValue();
					double ew = ((Double) val.get(1)).doubleValue();
					widths[i][0] = sw;
					widths[i][1] = ew;
				}
				line.setWidths(widths);
			}else{
				line.setWidths(new double[0][0]);
			}
			
			
			
			/*
			 * azabala: de siempre se producen excepciones al tratar de leer
			 * LwPolyline (si no se veian es pq se capturaban y no se sacaba
			 * traza)
			 * 
			 * Se producen siempre que numVertices == 0 ????
			 * Probar a saltarmelos.
			 * 
			 * 
			 * 
			 * TODO Ver por qué aparecen (y lo peor, pq una vez se da una se propagan 
			 * todas)
			 * 
			 * */
				bitPos = headTailReader.readObjectTailer(data, bitPos, line);
	}
}
