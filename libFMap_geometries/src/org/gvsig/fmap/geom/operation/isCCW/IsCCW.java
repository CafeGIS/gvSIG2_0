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
 
/**
 * 
 */
package org.gvsig.fmap.geom.operation.isCCW;

import java.awt.geom.PathIterator;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.operation.GeometryOperation;
import org.gvsig.fmap.geom.operation.GeometryOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.util.Converter;

import com.vividsolutions.jts.algorithm.CGAlgorithms;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;

/**
 * This class checks if the first part from the General Path of a complex geometry is CCW.
 * @return Boolean <code>true<code> if is CCW
 * @author Carlos Sánchez Periñán <a href = "mailto:csanchez@prodevelop.es"> e-mail </a>
 */
public class IsCCW extends GeometryOperation{

	private GeneralPathX generalPathX = null;
	
	public static final int CODE = GeometryLocator.getGeometryManager().registerGeometryOperation("isCCW", new IsCCW());
	
	public int getOperationIndex() {
		return CODE;
	}

	public Object invoke(Geometry geom, GeometryOperationContext ctx) throws GeometryOperationException {
		generalPathX = geom.getGeneralPath();
		if(generalPathX == null){
			//if there isn't path the operation hasn't sense.
			return null;
    	}
		PathIterator theIterator = generalPathX.getPathIterator(null, Converter.FLATNESS); //polyLine.getPathIterator(null, flatness);
		double[] theData = new double[6];
        Coordinate first = null;
        CoordinateList coordList = new CoordinateList();
        Coordinate c1;
        boolean bFirst = true;
		while (!theIterator.isDone()) {
			//while not done
			int type = theIterator.currentSegment(theData);
        	switch (type)
        	{
        	case GeneralPathX.SEG_MOVETO:
        		c1= new Coordinate(theData[0], theData[1]);
        		if (bFirst == false) // Ya tenemos la primera parte.
        			break;
        		if (bFirst)
        		{
        			bFirst=false;
        			first = c1;
        		}
        		coordList.add(c1, true);
        		break;
        	case GeneralPathX.SEG_LINETO:
        		c1= new Coordinate(theData[0], theData[1]);
        		coordList.add(c1, true);
        		break;

        	}
        	theIterator.next();
		}
		coordList.add(first, true);
		return new Boolean(CGAlgorithms.isCCW(coordList.toCoordinateArray()));
	}

}
