package com.iver.cit.gvsig.project.documents.table;

import java.awt.geom.PathIterator;
import java.util.ArrayList;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.util.Converter;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;

/**
 * @author Vicente Caballero Navarro
 */
public abstract class GraphicOperator extends AbstractOperator{
	private FLyrVect lv=null;
	public void setLayer(FLyrVect lv) {
		this.lv=lv;
	}
	public FLyrVect getLayer() {
		return lv;
	}
	public abstract double process(org.gvsig.fmap.dal.feature.Feature feature);
	protected ArrayList getXY(Geometry geometry) {
        ArrayList xs = new ArrayList();
        ArrayList ys = new ArrayList();
        ArrayList parts=new ArrayList();
        double[] theData = new double[6];

        //double[] aux = new double[6];
        PathIterator theIterator;
        int theType;
        int numParts = 0;
        theIterator = geometry.getPathIterator(null,Converter.FLATNESS); //, flatness);
        boolean isClosed = false;
        while (!theIterator.isDone()) {
            theType = theIterator.currentSegment(theData);

            switch (theType) {
            case PathIterator.SEG_MOVETO:
            		if (numParts==0){
            			xs.add(new Double(theData[0]));
            			ys.add(new Double(theData[1]));
            		}else{
            			if (!isClosed){
            				Double[] x = (Double[]) xs.toArray(new Double[0]);
            				Double[] y = (Double[]) ys.toArray(new Double[0]);
            				parts.add(new Double[][] { x, y });
            				xs.clear();
            				ys.clear();
            			}
            			xs.add(new Double(theData[0]));
            			ys.add(new Double(theData[1]));
            		}
                numParts++;
                isClosed = false;
                break;
            case PathIterator.SEG_LINETO:
            	isClosed=false;
                xs.add(new Double(theData[0]));
                ys.add(new Double(theData[1]));
                break;
            case PathIterator.SEG_CLOSE:
            	isClosed=true;
                xs.add(new Double(theData[0]));
                ys.add(new Double(theData[1]));
                Double[] x = (Double[]) xs.toArray(new Double[0]);
                Double[] y = (Double[]) ys.toArray(new Double[0]);
                parts.add(new Double[][] { x, y });
                xs.clear();
                ys.clear();
                break;
            } //end switch

            theIterator.next();
        } //end while loop

        if (!isClosed){
        	isClosed=true;
        	xs.add(new Double(theData[0]));
            ys.add(new Double(theData[1]));
            Double[] x = (Double[]) xs.toArray(new Double[0]);
            Double[] y = (Double[]) ys.toArray(new Double[0]);
            parts.add(new Double[][] { x, y });
            xs.clear();
            ys.clear();
        }
        return parts;

    }

}
