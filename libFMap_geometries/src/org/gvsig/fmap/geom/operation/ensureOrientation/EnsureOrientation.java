/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
* MA  02110-1301, USA.
* 
*/

/*
* AUTHORS (In addition to CIT):
* 2008 PRODEVELOP S.L. Main Development
*/
 
package org.gvsig.fmap.geom.operation.ensureOrientation;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.operation.GeometryOperation;
import org.gvsig.fmap.geom.operation.GeometryOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;

import com.vividsolutions.jts.algorithm.CGAlgorithms;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;
import com.vividsolutions.jts.geom.CoordinateSequences;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

/**
 * Use this function class to ensure you get real polygons or holes
 * En JTS, con bCCW = false obtienes un poligono exterior.
 * Nota: Solo se le da la vuelta (si es que lo necesita) al
 * poligono exterior. El resto, por ahora, no se tocan.
 * Si se necesita tenerlos en cuenta, habría que mirar
 * si están dentro del otro, y entonces revisar que tiene
 * un CCW contrario al exterior.
 * @param bCCW true if you want the GeneralPath in CCW order
 * @return true si se le ha dado la vuelta. (true if flipped)
 * TODO: TERMINAR ESTO!! NO ESTÁ COMPLETO!! NO sirve para multipoligonos
 */
/**
 * @author Carlos Sánchez Periñán <a href = "mailto:csanchez@prodevelop.es"> e-mail </a>
 */
public class EnsureOrientation extends GeometryOperation{

	private GeneralPathX generalPathX = null;
	
	private Boolean bCCW;
	
	public static final int CODE = GeometryLocator.getGeometryManager().registerGeometryOperation("ensureOrientation", new EnsureOrientation());
	
	public int getOperationIndex() {
		return CODE;
	}

	public Object invoke(Geometry geom, GeometryOperationContext ctx)throws GeometryOperationException {
		bCCW = (Boolean) ctx.getAttribute("bCCW");
		if (bCCW==null)
			throw new GeometryOperationException(new Exception("The function Parameter hasn't been passed or is null."));
		
		generalPathX = geom.getGeneralPath();
		if (generalPathX==null){
			//if there isn't path the operation hasn't sense.
			return null;
		}
		
        byte[] pointTypesAux = new byte[generalPathX.getNumTypes()+1];
        double[] pointCoordsAux = new double[generalPathX.getNumCoords()+2];
        int i;
        int pointIdx = 0;

        Coordinate c1, c2, c3;
        CoordinateList coordList = new CoordinateList();
        CoordinateList firstList = new CoordinateList();
        boolean bFirstList = true;
        Coordinate cInicio = null;

        for (i=0; i< generalPathX.getNumTypes(); i++)
        {
        	int type = generalPathX.getPointTypes()[i];

        	switch (type)
        	{
        	case GeneralPathX.SEG_MOVETO:
        		c1= new Coordinate(generalPathX.getPointCoords()[pointIdx], generalPathX.getPointCoords()[pointIdx+1]);
        		cInicio = c1;
        		coordList.add(c1, true);
        		if (i>0) bFirstList = false;
        		if (bFirstList)
        		{
        			firstList.add(c1,true);
        		}
        		break;
        	case GeneralPathX.SEG_LINETO:
        		c1= new Coordinate(generalPathX.getPointCoords()[pointIdx], generalPathX.getPointCoords()[pointIdx+1]);
        		coordList.add(c1, true);
        		if (bFirstList)
        		{
        			firstList.add(c1,true);
        		}
        		break;
        	case GeneralPathX.SEG_QUADTO:
        		c1= new Coordinate(generalPathX.getPointCoords()[pointIdx], generalPathX.getPointCoords()[pointIdx+1]);
        		coordList.add(c1, true);
        		c2= new Coordinate(generalPathX.getPointCoords()[pointIdx+2],generalPathX.getPointCoords()[pointIdx+3]);
        		coordList.add(c2, true);
        		if (bFirstList)
        		{
        			firstList.add(c1,true);
        			firstList.add(c2,true);
        		}

        		break;
        	case GeneralPathX.SEG_CUBICTO:
        		c1= new Coordinate(generalPathX.getPointCoords()[pointIdx], generalPathX.getPointCoords()[pointIdx+1]);
        		coordList.add(c1, true);
        		c2= new Coordinate(generalPathX.getPointCoords()[pointIdx+2],generalPathX.getPointCoords()[pointIdx+3]);
        		coordList.add(c2, true);
        		c3= new Coordinate(generalPathX.getPointCoords()[pointIdx+4],generalPathX.getPointCoords()[pointIdx+5]);
        		coordList.add(c3, true);
        		if (bFirstList)
        		{
        			firstList.add(c1,true);
        			firstList.add(c2,true);
        			firstList.add(c3,true);
        		}

        		break;
        	case GeneralPathX.SEG_CLOSE:
        		coordList.add(cInicio, true);
        		if (bFirstList)
        		{
        			firstList.add(cInicio,true);
        		}
        		break;

        	}
        	pointIdx += GeneralPathX.curvesize[type];
        }
		// Guardamos el path dandole la vuelta
		Coordinate[] coords = coordList.toCoordinateArray();
		boolean bFlipped = false;
		if (CGAlgorithms.isCCW(coords) != bCCW.booleanValue()) // Le damos la vuelta
		{
			CoordinateArraySequence seq = new CoordinateArraySequence(coords);
			CoordinateSequences.reverse(seq);
			coords = seq.toCoordinateArray();


			// En el primer punto metemos un moveto
			pointCoordsAux[0] = coords[0].x;
			pointCoordsAux[1] = coords[0].y;
			pointTypesAux[0] = GeneralPathX.SEG_MOVETO;
			int idx = 2;
			i=0;
			int j=1;
			for (int k=0; k < coords.length; k++)
			{
				pointCoordsAux[idx++] = coords[k].x;
				pointCoordsAux[idx++] = coords[k].y;
	        	int type = generalPathX.getPointTypes()[i++];
	        	pointIdx += GeneralPathX.curvesize[type];
	        	switch (type)
	        	{
	        	case GeneralPathX.SEG_MOVETO:
	        		pointTypesAux[j] = GeneralPathX.SEG_LINETO;
	        		break;
	        	case GeneralPathX.SEG_LINETO:
	        		pointTypesAux[j] = GeneralPathX.SEG_LINETO;
	        		break;
	        	case GeneralPathX.SEG_QUADTO:
	        		pointTypesAux[j] = GeneralPathX.SEG_QUADTO;
	        		break;
	        	case GeneralPathX.SEG_CUBICTO:
	        		pointTypesAux[j] = GeneralPathX.SEG_CUBICTO;
	        		break;
	        	case GeneralPathX.SEG_CLOSE:
	        		// TODO: IMPLEMENTAR ESTO!!!
	        		break;

	        	}
	        	j++;

			}
			generalPathX.setPointTypes(pointTypesAux);
			generalPathX.setPointCoords(pointCoordsAux);
	        generalPathX.setNumCoords(generalPathX.getNumCoords()+2);
	        generalPathX.setNumTypes(generalPathX.getNumTypes()+1);
	        bFlipped  = true;
		}
		return new Boolean((boolean) bFlipped);
	}

}
