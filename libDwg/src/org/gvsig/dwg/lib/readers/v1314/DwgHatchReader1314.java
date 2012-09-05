/*
 * Created on 30-ene-2007
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
/* CVS MESSAGES:
*
* $Id: DwgHatchReader1314.java 28969 2009-05-25 13:23:12Z jmvivo $
* $Log$
* Revision 1.4.2.2  2007-03-21 19:49:16  azabala
* implementation of dwg 12, 13, 14.
*
* Revision 1.4  2007/02/02 12:00:00  azabala
* first implementation
*
* Revision 1.2  2007/01/31 20:28:44  azabala
* *** empty log message ***
*
* Revision 1.1  2007/01/30 19:40:23  azabala
* *** empty log message ***
*
*
*/
package org.gvsig.dwg.lib.readers.v1314;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgHatch;


public class DwgHatchReader1314 extends AbstractDwg1314Reader {

	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException{
		
		 if(! (dwgObj instanceof DwgHatch))
		    	throw new RuntimeException("ArcReader 14 solo puede leer DwgHatch");
		 DwgHatch h = (DwgHatch) dwgObj;
		
		int bitPos = offset;
		bitPos = headTailReader.readObjectHeader(data, offset, dwgObj);
		
		List val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double zCoord = ((Double)val.get(1)).doubleValue();
		h.setZ(zCoord);
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double xEx = ((Double)val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double yEx = ((Double)val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double zEx = ((Double)val.get(1)).doubleValue();
		
		h.setExtrusion(new double[]{xEx, yEx, zEx});
		
		val = DwgUtil.getTextString(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		String txt = (String)val.get(1);
		h.setName(txt);
		
		val = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		boolean solidFill = ((Boolean)val.get(1)).booleanValue();
		h.setSolidFill(solidFill);
		
		val = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		boolean associative = ((Boolean)val.get(1)).booleanValue();
		h.setAssociative(associative);
		
		val = DwgUtil.getBitLong(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		int numPaths =  ((Integer)val.get(1)).intValue();
		
//		TODO Esto lo pongo por analogia con las colecciones de Python
		//pero hay que quitarlo
		
		/*
		 * paths puede tener distintos tipos de paths:
		 * a) del tipo polyline
		 * b) del tipo stdpath
		 * 
		 * 
		 * paths.put("polyline", pathSegments);
		 * paths.put("stdpath", pathSegments);
		
		 * */
		Map paths = new HashMap();
		
		int pathFlag = 0;
		int pixel = 0;
		int allBounds = 0;
		for(int i = 0; i < numPaths; i++){
			val = DwgUtil.getBitLong(data, bitPos);
			bitPos = ((Integer)val.get(0)).intValue();
			pathFlag = ((Integer)val.get(1)).intValue();
			
			if( (pathFlag  & 0x4) > 0){
				pixel++;
				//TODO La especificacion no dice esto (lo hace PythonCAD)
				//la especificacion dice pixel = DwgUtil.getBitDouble(...etc)
			}
			
			if( (pathFlag & 0x2) > 0){
				
				//POLYLINE PATH
				val = DwgUtil.testBit(data, bitPos);
				bitPos = ((Integer)val.get(0)).intValue();
				boolean hasBulges = ((Boolean)val.get(1)).booleanValue();
				
				val = DwgUtil.testBit(data, bitPos);
				bitPos = ((Integer)val.get(0)).intValue();
				boolean isClosed = ((Boolean)val.get(1)).booleanValue();
				
				
				val = DwgUtil.getBitLong(data, bitPos);
				bitPos = ((Integer)val.get(0)).intValue();
				int nps = ((Integer)val.get(1)).intValue();
				
				List pathSegments = new ArrayList();
				for (int j = 0; j < nps; j++){
					val = DwgUtil.getRawDouble(data, bitPos);
					bitPos = ((Integer)val.get(0)).intValue();
					double x = ((Double)val.get(1)).doubleValue();
					
					val = DwgUtil.getRawDouble(data, bitPos);
					bitPos = ((Integer)val.get(0)).intValue();
					double y = ((Double)val.get(1)).doubleValue();
					double bulge = 0d;
					if(hasBulges){
						val = DwgUtil.getBitDouble(data, bitPos);
						bitPos = ((Integer)val.get(0)).intValue();
						bulge = ((Double)val.get(1)).doubleValue();
					}
					pathSegments.add(new double[]{x, y, bulge});
				}//for
				paths.put("polyline", pathSegments);
			}else{
				val = DwgUtil.getBitLong(data, bitPos);
				bitPos = ((Integer)val.get(0)).intValue();
				int nps = ((Integer)val.get(1)).intValue();
				Map pathSegments = new HashMap();
				for(int j = 0; j < nps; j++){
					val = DwgUtil.getRawChar(data, bitPos);
					bitPos = ((Integer)val.get(0)).intValue();
					int pts = ((Integer)val.get(1)).intValue();
					
					double x, y, r, sa, ea;
					ArrayList list;
					boolean isCcw;
					
					switch(pts){
					case 1://LINE
						val = DwgUtil.getRawDouble(data, bitPos);
						bitPos = ((Integer)val.get(0)).intValue();
						double x1 = ((Double)val.get(1)).doubleValue();
						
						val = DwgUtil.getRawDouble(data, bitPos);
						bitPos = ((Integer)val.get(0)).intValue();
						double y1 = ((Double)val.get(1)).doubleValue();
						
						val = DwgUtil.getRawDouble(data, bitPos);
						bitPos = ((Integer)val.get(0)).intValue();
						double x2 = ((Double)val.get(1)).doubleValue();
						
						val = DwgUtil.getRawDouble(data, bitPos);
						bitPos = ((Integer)val.get(0)).intValue();
						double y2 = ((Double)val.get(1)).doubleValue();
						
						pathSegments.put("line", new double[]{x1, y1, x2, y2});
					
					break;
					
					case 2://CIRCULAR ARC
						val = DwgUtil.getRawDouble(data, bitPos);
						bitPos = ((Integer)val.get(0)).intValue();
						x = ((Double)val.get(1)).doubleValue();
						
						val = DwgUtil.getRawDouble(data, bitPos);
						bitPos = ((Integer)val.get(0)).intValue();
						y = ((Double)val.get(1)).doubleValue();
						
						val = DwgUtil.getBitDouble(data, bitPos);
						bitPos = ((Integer)val.get(0)).intValue();
						r = ((Double)val.get(1)).doubleValue();
						
						val = DwgUtil.getBitDouble(data, bitPos);
						bitPos = ((Integer)val.get(0)).intValue();
						sa = ((Double)val.get(1)).doubleValue();
						
						val = DwgUtil.getBitDouble(data, bitPos);
						bitPos = ((Integer)val.get(0)).intValue();
						ea = ((Double)val.get(1)).doubleValue();
						
						val = DwgUtil.testBit(data, bitPos);
						bitPos = ((Integer)val.get(0)).intValue();
						isCcw = ((Boolean)val.get(1)).booleanValue();
						//TODO Esto es una kk.
						//CAMBIAR POR UNA JERARQUIA DE OBJETOS SEGÚN EL SEGMENTO
						list = new ArrayList();
						list.add(new Double(x));
						list.add(new Double(y));
						list.add(new Double(r));
						list.add(new Double(sa));
						list.add(new Double(ea));
						list.add(new Boolean(isCcw));
						pathSegments.put("arc", list);
						
					break;
					
					case 3://ELLIPTICAL ARC
						val = DwgUtil.getRawDouble(data, bitPos);
						bitPos = ((Integer)val.get(0)).intValue();
						x = ((Double)val.get(1)).doubleValue();
						
						val = DwgUtil.getRawDouble(data, bitPos);
						bitPos = ((Integer)val.get(0)).intValue();
						y = ((Double)val.get(1)).doubleValue();
						
						val = DwgUtil.getRawDouble(data, bitPos);
						bitPos = ((Integer)val.get(0)).intValue();
						double xe = ((Double)val.get(1)).doubleValue();
						
						val = DwgUtil.getRawDouble(data, bitPos);
						bitPos = ((Integer)val.get(0)).intValue();
						double ye = ((Double)val.get(1)).doubleValue();
						
						val = DwgUtil.getBitDouble(data, bitPos);
						bitPos = ((Integer)val.get(0)).intValue();
						r = ((Double)val.get(1)).doubleValue();
						
						val = DwgUtil.getBitDouble(data, bitPos);
						bitPos = ((Integer)val.get(0)).intValue();
						sa = ((Double)val.get(1)).doubleValue();
						
						val = DwgUtil.getBitDouble(data, bitPos);
						bitPos = ((Integer)val.get(0)).intValue();
						ea = ((Double)val.get(1)).doubleValue();
						
						val = DwgUtil.testBit(data, bitPos);
						bitPos = ((Integer)val.get(0)).intValue();
						isCcw = ((Boolean)val.get(1)).booleanValue();
						
						list = new ArrayList();
						list.add(new Double(x));
						list.add(new Double(y));
						list.add(new Double(xe));
						list.add(new Double(ye));
						list.add(new Double(r));
						list.add(new Double(sa));
						list.add(new Double(ea));
						list.add(new Boolean(isCcw));
						pathSegments.put("elliptical_arc", list);
					break;
					
					case 4://SPLINE ARC
						val = DwgUtil.getBitLong(data, bitPos);
						bitPos = ((Integer)val.get(0)).intValue();
						int deg = ((Integer)val.get(1)).intValue();
						
						val = DwgUtil.testBit(data, bitPos);
						bitPos = ((Integer)val.get(0)).intValue();
						boolean isRat = ((Boolean)val.get(1)).booleanValue();
						
						val = DwgUtil.testBit(data, bitPos);
						bitPos = ((Integer)val.get(0)).intValue();
						boolean isPer = ((Boolean)val.get(1)).booleanValue();
						
						val = DwgUtil.getBitLong(data, bitPos);
						bitPos = ((Integer)val.get(0)).intValue();
						int numKnokts = ((Integer)val.get(1)).intValue();
						
						val = DwgUtil.getBitLong(data, bitPos);
						bitPos = ((Integer)val.get(0)).intValue();
						int numCtrlPts = ((Integer)val.get(1)).intValue();
						 
						double[] knots = new double[numKnokts];
						for(int z = 0;  z < numKnokts; z++){
							val = DwgUtil.getBitDouble(data, bitPos);
							bitPos = ((Integer)val.get(0)).intValue();
							knots[z] = ((Double)val.get(1)).doubleValue();
						}
						
						double[][] controlPoints = new double[3][numCtrlPts];
						for(int z = 0; z < numCtrlPts; z++){
							//x
							val = DwgUtil.getRawDouble(data, bitPos);
							bitPos = ((Integer)val.get(0)).intValue();
							controlPoints[0][z] = ((Double)val.get(1)).doubleValue();
							//y
							val = DwgUtil.getRawDouble(data, bitPos);
							bitPos = ((Integer)val.get(0)).intValue();
							controlPoints[1][z] = ((Double)val.get(1)).doubleValue();
							
							//weight
							if(isRat){
								val = DwgUtil.getBitDouble(data, bitPos);
								bitPos = ((Integer)val.get(0)).intValue();
								controlPoints[2][z] = ((Double)val.get(1)).doubleValue();
							}else{
								controlPoints[2][z] = 0;
							}
							list = new ArrayList();
							list.add(new Boolean(isRat));
							list.add(new Boolean(isPer));
							list.add(knots);
							list.add(controlPoints);
							pathSegments.put("spline", list);
						}
					break;
					
					default:
						System.out.println("Error leyendo un segmento de hatch:codigo="+pts);
					break;
					}//switch
				}//for
				paths.put("stdpath", pathSegments);
			}//else
			
			val = DwgUtil.getBitLong(data, bitPos);
			bitPos = ((Integer)val.get(0)).intValue();
			int nbh = ((Integer)val.get(1)).intValue();
			allBounds += nbh;
		}//for
		
		h.setPaths(paths);
		
		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		int style = ((Integer)val.get(1)).intValue();
		h.setStyle(style);
		
		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		int patternType = ((Integer)val.get(1)).intValue();
		h.setPatterType(patternType);
		
		if(!solidFill){
			val = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)val.get(0)).intValue();
			double fillAngle = ((Double)val.get(1)).doubleValue();
			h.setFillAngle(fillAngle);
			
			val = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)val.get(0)).intValue();
			double fillScaleOrSpacion = 
					((Double)val.get(1)).doubleValue();
			h.setFillScale(fillScaleOrSpacion);
			
			val = DwgUtil.testBit(data, bitPos);
			bitPos = ((Integer)val.get(0)).intValue();
			boolean fileDoubleHath = 
				((Boolean)val.get(1)).booleanValue();
			h.setFileDoubleHath(fileDoubleHath);
			
			val = DwgUtil.getBitShort(data, bitPos);
			bitPos = ((Integer)val.get(0)).intValue();
			int ndf = ((Integer)val.get(1)).intValue();
			
			ArrayList[] lines = new ArrayList[ndf];
			for(int i = 0; i < ndf; i++){
				val = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer)val.get(0)).intValue();
				double x = ((Double)val.get(1)).doubleValue();
				
				val = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer)val.get(0)).intValue();
				double y = ((Double)val.get(1)).doubleValue();
				
				val = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer)val.get(0)).intValue();
				double x0 = ((Double)val.get(1)).doubleValue();
				
				val = DwgUtil.getBitDouble(data, bitPos);
				bitPos = ((Integer)val.get(0)).intValue();
				double y0 = ((Double)val.get(1)).doubleValue();
				
				val = DwgUtil.getBitShort(data, bitPos);
				bitPos = ((Integer)val.get(0)).intValue();
				int numDashes = ((Integer)val.get(1)).intValue();
				double[] dashes = new double[numDashes];
				for(int j = 0; j < numDashes; j++){
					val = DwgUtil.getBitDouble(data, bitPos);
					bitPos = ((Integer)val.get(0)).intValue();
					dashes[j] = ((Double)val.get(1)).doubleValue();
					/*TODO el original python decia 
					 *  for _j in range(_nds):
					 		_bitpos, _val = dwgutil.get_bit_double(data, _bitpos)
					 		ent.setEntityData('dashlength', _val)
					 	Aunque no le veo mucho sentido
					 */
				}//for j
				//TODO Esto es horrible. Sustituir por orientación a objetos
				lines[i] = new ArrayList();
				lines[i].add(new Double(x));
				lines[i].add(new Double(y));
				lines[i].add(new Double(x0));
				lines[i].add(new Double(y0));
				lines[i].add(dashes);
			}//for i
			h.setFillLines(lines);
		}//if solidFill
		
		if(pixel > 0){
			val = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)val.get(0)).intValue();
			double pixelSize = ((Double)val.get(1)).doubleValue();
			h.setPixelSize(pixelSize);
		}
		val = DwgUtil.getBitLong(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		int numSeddPoints = ((Integer)val.get(1)).intValue();
		double[][] seedPoints = new double[2][numSeddPoints];
		for(int j = 0; j < numSeddPoints; j++){
			val = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)val.get(0)).intValue();
			double x = ((Double)val.get(1)).doubleValue();
			
			val = DwgUtil.getBitDouble(data, bitPos);
			bitPos = ((Integer)val.get(0)).intValue();
			double y = ((Double)val.get(1)).doubleValue();
			
			seedPoints[0][j] = x;
			seedPoints[1][j] = y;
 		}
		h.setSeedPoints(seedPoints);
		
		DwgHandleReference handle = null;
		for(int i = 0; i < h.getNumReactors(); i++){
			handle = new DwgHandleReference();
			bitPos = handle.read(data, bitPos);
			//los DwgObject ahora mismo estan pasando de los reactors
		}
		handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		h.setXDicObjHandle(handle);
		
		handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		h.setLayerHandle(handle);
		
		if(! h.isLyrByLineType()){
			handle = new DwgHandleReference();
			bitPos = handle.read(data, bitPos);
			h.setLineTypeHandle(handle);
		}
		
		if (! h.isNoLinks()){
			handle = new DwgHandleReference();
			bitPos = handle.read(data, bitPos);
			h.setPreviousHandle(handle);
			
			handle = new DwgHandleReference();
			bitPos = handle.read(data, bitPos);
			h.setNextHandle(handle);
		}
		
		if(allBounds > 0){
			DwgHandleReference[] handles = new DwgHandleReference[allBounds];
			for(int i = 0; i < allBounds; i++){
				handle = new DwgHandleReference();
				bitPos = handle.read(data, bitPos);
				handles[i] = handle;
			}//for
			h.setBoundaryHandles(handles);
		}
	}
}

