package com.iver.cit.gvsig.project.documents.layout.fframes;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontext.MapContext;

public interface IFFrameUseFMap {
	public static final int AUTOMATICO = 0;
    public static final int CONSTANTE = 1;
    public static final int MANUAL = 2;

	public AffineTransform getATMap();
	public void setATMap(AffineTransform at);
	public MapContext getMapContext();
	public void refresh();
	public void setNewEnvelope(Envelope r);
	public BufferedImage getBufferedImage();
	public void fullExtent() throws ReadException;
	public void setPointsToZoom(Point2D px1, Point2D px2);
	public void movePoints(Point2D px1, Point2D px2);
	public int getTypeScale();
}
