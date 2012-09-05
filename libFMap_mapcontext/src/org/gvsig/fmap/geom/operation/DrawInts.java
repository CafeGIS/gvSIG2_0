package org.gvsig.fmap.geom.operation;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.rendering.symbols.CartographicSupport;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.tools.task.Cancellable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrawInts extends GeometryOperation {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	final static private Logger logger = LoggerFactory.getLogger(DrawInts.class);

	public static final int CODE = GeometryLocator.getGeometryManager()
	.registerGeometryOperation("drawInts", new DrawInts());

	public Object invoke(Geometry geom, GeometryOperationContext ctx) throws GeometryOperationException{
		DrawOperationContext doc=(DrawOperationContext)ctx;
		ViewPort viewPort = doc.getViewPort();
		ISymbol symbol = doc.getSymbol();
		Graphics2D g=doc.getGraphics();
		Cancellable cancel=doc.getCancellable();
		//		 make the symbol to resize itself with the current rendering context
		try {
			if (doc.hasDPI()){
				double previousSize = ((CartographicSupport)symbol).
				toCartographicSize(viewPort, doc.getDPI(), geom);
				// draw it as normally
				Geometry decimatedShape = transformToInts(geom, viewPort.getAffineTransform());
				symbol.draw(g, viewPort.getAffineTransform(), decimatedShape,cancel);
				// restore previous size
				((CartographicSupport)symbol).setCartographicSize(previousSize, geom);
			}else{
				Geometry decimatedShape = transformToInts(geom, viewPort.getAffineTransform());
				symbol.draw(g, viewPort.getAffineTransform(), decimatedShape,cancel);
			}
		} catch (CreateGeometryException e) {
			e.printStackTrace();
			throw new GeometryOperationException(e);
		}

		return null;
	}

	public int getOperationIndex() {
		return CODE;
	}

	private Geometry transformToInts(Geometry gp, AffineTransform at) throws CreateGeometryException {
		GeneralPathX newGp = new GeneralPathX();
		double[] theData = new double[6];
		double[] aux = new double[6];

		// newGp.reset();
		PathIterator theIterator;
		int theType;
		int numParts = 0;

		java.awt.geom.Point2D ptDst = new java.awt.geom.Point2D.Double();
		java.awt.geom.Point2D ptSrc = new java.awt.geom.Point2D.Double();
		boolean bFirst = true;
		int xInt, yInt, antX = -1, antY = -1;


		theIterator = gp.getPathIterator(null); //, flatness);
		int numSegmentsAdded = 0;
		while (!theIterator.isDone()) {
			theType = theIterator.currentSegment(theData);

			switch (theType) {
			case PathIterator.SEG_MOVETO:
				numParts++;
				ptSrc.setLocation(theData[0], theData[1]);
				at.transform(ptSrc, ptDst);
				antX = (int) ptDst.getX();
				antY = (int) ptDst.getY();
				newGp.moveTo(antX, antY);
				numSegmentsAdded++;
				bFirst = true;
				break;

			case PathIterator.SEG_LINETO:
				ptSrc.setLocation(theData[0], theData[1]);
				at.transform(ptSrc, ptDst);
				xInt = (int) ptDst.getX();
				yInt = (int) ptDst.getY();
				if ((bFirst) || ((xInt != antX) || (yInt != antY)))
				{
					newGp.lineTo(xInt, yInt);
					antX = xInt;
					antY = yInt;
					bFirst = false;
					numSegmentsAdded++;
				}
				break;

			case PathIterator.SEG_QUADTO:
				at.transform(theData,0,aux,0,2);
				newGp.quadTo(aux[0], aux[1], aux[2], aux[3]);
				numSegmentsAdded++;
				break;

			case PathIterator.SEG_CUBICTO:
				at.transform(theData,0,aux,0,3);
				newGp.curveTo(aux[0], aux[1], aux[2], aux[3], aux[4], aux[5]);
				numSegmentsAdded++;
				break;

			case PathIterator.SEG_CLOSE:
				if (numSegmentsAdded < 3) {
					newGp.lineTo(antX, antY);
				}
				newGp.closePath();

				break;
			} //end switch

			theIterator.next();
		} //end while loop

		Geometry geom = null;
		switch (gp.getType()) {
		case Geometry.TYPES.POINT:
			geom = geomManager.createPoint(ptDst.getX(), ptDst.getY(), SUBTYPES.GEOM2D);
			break;
		case Geometry.TYPES.CURVE:
		case Geometry.TYPES.ARC:
			geom = geomManager.createCurve(newGp, SUBTYPES.GEOM2D);
			break;

		case Geometry.TYPES.SURFACE:
		case Geometry.TYPES.CIRCLE:
		case Geometry.TYPES.ELLIPSE:

			geom = geomManager.createSurface(newGp, SUBTYPES.GEOM2D);
			break;
		}
		return geom;
	}

	public static void register() {
		// Nothing to do

	}


}
