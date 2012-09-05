package org.gvsig.fmap.geom.operation.flip;

import java.awt.geom.PathIterator;
import java.util.ArrayList;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.operation.GeometryOperation;
import org.gvsig.fmap.geom.operation.GeometryOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.util.Converter;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;
import com.vividsolutions.jts.geom.CoordinateSequences;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

/**
 * This class converts the path into points, then flip it down. 
 * 
 * @author Carlos Sánchez Periñán <a href = "mailto:csanchez@prodevelop.es"> e-mail </a>
 */
public class Flip extends GeometryOperation{
   
	private GeneralPathX generalPathX = null;
    
	public static final int CODE = GeometryLocator.getGeometryManager().registerGeometryOperation("flip", new Flip());
	
	public int getOperationIndex() {
		return CODE;
	}
	
    public Object invoke(Geometry geom, GeometryOperationContext ctx) throws GeometryOperationException {
    	generalPathX = geom.getGeneralPath();
		if(generalPathX == null){
			//if there isn't path the operation hasn't sense.
			return null;
    	}
		PathIterator theIterator = geom.getPathIterator(null, Converter.FLATNESS); //polyLine.getPathIterator(null, flatness);
    	double[] theData = new double[6];
    	//Coordinate first = null;
    	CoordinateList coordList = new CoordinateList();
    	Coordinate c1;
    	GeneralPathX newGp = new GeneralPathX();
    	ArrayList listOfParts = new ArrayList();
    	while (!theIterator.isDone()) {
    		//while not done
    		int type = theIterator.currentSegment(theData);
    		switch (type)
    		{
    		case (byte) PathIterator.SEG_MOVETO:
    			coordList = new CoordinateList();
    			listOfParts.add(coordList);
    			c1= new Coordinate(theData[0], theData[1]);
    			coordList.add(c1, true);
    			break;
    	    case (byte) PathIterator.SEG_LINETO:
    			c1= new Coordinate(theData[0], theData[1]);
    			coordList.add(c1, true);
    			break;
    		case (byte) PathIterator.SEG_CLOSE:
    			coordList.add(coordList.getCoordinate(0));
    			break;
    		}
    		theIterator.next();
    	}
    		
    	for (int i=listOfParts.size()-1; i>=0; i--)
    	{
    		coordList = (CoordinateList) listOfParts.get(i);
    		Coordinate[] coords = coordList.toCoordinateArray();
    		CoordinateArraySequence seq = new CoordinateArraySequence(coords);
    		CoordinateSequences.reverse(seq);
    		coords = seq.toCoordinateArray();
    		newGp.moveTo(coords[0].x, coords[0].y);
    		for (int j=1; j < coords.length; j++)
    		{
    			newGp.lineTo(coords[j].x, coords[j].y);
    		}
    	}
    	generalPathX.reset();
    	generalPathX.append(newGp, false);	
    	
		return null;		
	}
}