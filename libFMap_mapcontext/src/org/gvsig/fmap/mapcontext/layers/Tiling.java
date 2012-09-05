/*
 * Created on 16-feb-2005
 */
package org.gvsig.fmap.mapcontext.layers;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;

import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Cálculo de Partes (Tiles) en las que se divide un raster grande.
 * Se usa para imprimir rasters y capas raste remotas (WMS).
 *
 * Para no pedir imagenes demasiado grandes, vamos
 * a hacer lo mismo que hace EcwFile: chunkear.
 * Llamamos a drawView con cuadraditos más pequeños
 * del BufferedImage ni caso, cuando se imprime viene con null
 * código original de Fran Peñarrubia
 * @author Luis W. Sevilla (sevilla_lui@gva.es)
 */

public class Tiling {	
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(Tiling.class);
	private static final int		MIN_SIZE = 50; //Tamaño mínimo en pixeles del tile
	private boolean 				debug = true;
	private int 					tileMaxWidth, tileMaxHeight;
	private int 					numRows, numCols;
	private double[][] 				srcPts;
	private Rectangle2D[] 			tile;
	private double 					width = 500, height = 500;
	private AffineTransform 		mat;
	private ViewPort 				vp;

	public Tiling(){}

	public Tiling(int tileW, int tileH, Rectangle2D r) {
		int[] size = this.calcMaxTileSize(tileW, tileH, r);
		tileMaxWidth = size[0];
		tileMaxHeight = size[1];

        int stepX, stepY;
        int xProv, yProv;
        int altoAux, anchoAux;

        //Vamos a hacerlo en trozos de AxH
        numCols = 1+(int) (r.getWidth()) / tileMaxWidth;
        numRows = 1+(int) (r.getHeight()) / tileMaxHeight;

        srcPts = new double[numCols*numRows][8];
        tile = new Rectangle2D[numCols*numRows];

    	yProv = (int) r.getY();
        for (stepY=0; stepY < numRows; stepY++) {
    		if ((yProv + tileMaxHeight) > r.getMaxY())
    			altoAux = (int) r.getMaxY() - yProv;
    		else
    			altoAux = tileMaxHeight;

    		xProv = (int) r.getX();
        	for (stepX=0; stepX < numCols; stepX++) {
	    		if ((xProv + tileMaxWidth) > r.getMaxX())
	    			anchoAux = (int) r.getMaxX() - xProv;
	    		else
	    			anchoAux = tileMaxWidth;

        		//Rectangle newRect = new Rectangle(xProv, yProv, anchoAux, altoAux);
        		int tileCnt = stepY*numCols+stepX;
        		// Parte que dibuja
        		srcPts[tileCnt][0] = xProv;
        		srcPts[tileCnt][1] = yProv;
        		srcPts[tileCnt][2] = xProv + anchoAux+1;
        		srcPts[tileCnt][3] = yProv;
        		srcPts[tileCnt][4] = xProv + anchoAux+1;
        		srcPts[tileCnt][5] = yProv + altoAux+1;
        		srcPts[tileCnt][6] = xProv;
        		srcPts[tileCnt][7] = yProv + altoAux+1;

        		tile[tileCnt] = new Rectangle2D.Float(xProv, yProv, anchoAux+1, altoAux+1);

				xProv += tileMaxWidth;
        	}
        	yProv += tileMaxHeight;
        }
	}

	/**
	 * Calcula el tamaño máximo de tile controlando que ningún tile tenga menos de MIN_SIZE
	 * pixeles
	 * @param tileW Ancho del tile
	 * @param tileH	Alto del tile
	 * @param r Rectangulo que define el area de la imagen
	 */
	public int[] calcMaxTileSize(int tileW, int tileH, Rectangle2D r){
		
		if(r.getWidth() < tileW || r.getHeight() < tileH){
			int[] sizeTiles = {tileW, tileH};
			return sizeTiles;
		}

        int wLastCol = 0;
        tileW += MIN_SIZE;
		do{
			tileW -= MIN_SIZE;
	        int numCols = (int) (r.getWidth() / tileW);
	        int w = 0;
	        for(int i = 0; i < numCols; i++)
	        	w += tileW;
	        wLastCol = ((int) r.getWidth()) - w;
		}while(wLastCol < MIN_SIZE && tileW > (MIN_SIZE * 2));

		int hLastRow = 0;
        tileH += MIN_SIZE;
		do{
			tileH -= MIN_SIZE;
	        int numRows = (int) (r.getHeight() / tileH);
	        int h = 0;
	        for(int i = 0; i < numRows; i++)
	        	h += tileH;
	        hLastRow = ((int) r.getHeight()) - h;
		}while(hLastRow < MIN_SIZE && tileH > (MIN_SIZE * 2));

		tileMaxWidth = tileW;
		tileMaxHeight = tileH;
		int[] sizeTiles = {tileMaxWidth, tileMaxHeight};
		return sizeTiles;
	}

	public double [] getTilePts(int colNr, int rowNr) {
		return srcPts[rowNr*numCols+colNr];
	}

	public double [] getTilePts(int num) {
		return srcPts[num];
	}

	public Rectangle2D getTileSz(int colNr, int rowNr) {
		return tile[rowNr*numCols+colNr];
	}

	public Rectangle2D getTile(int num) {
		return tile[num];
	}

	/**
	 * @return Returns the numCols.
	 */
	public int getNumCols() {
		return numCols;
	}
	/**
	 * @return Returns the numRows.
	 */
	public int getNumRows() {
		return numRows;
	}

	public int getNumTiles() { return numRows*numCols; }
	/**
	 * @return Returns the tileHeight.
	 */
	public int getMaxTileHeight() {
		return tileMaxHeight;
	}
	/**
	 * @return Returns the tileWidth.
	 */
	public int getMaxTileWidth() {
		return tileMaxWidth;
	}

	ViewPort[] viewPortList = null;
	private void calcViewPort(ViewPort viewPort)throws NoninvertibleTransformException{
		viewPortList = new ViewPort[numCols*numRows];

		/*if(viewPort.getImageWidth() < width && viewPort.getImageHeight() < height){
			viewPortList[0] = viewPort;
			return;
		}*/

	    int vpCnt = 0;

	    double imgPxX = viewPort.getImageWidth();
	    double dWcX = viewPort.getAdjustedExtent().getLength(0);
	    double tileWcW = (getTile(vpCnt).getWidth() * dWcX) / imgPxX;

	    double imgPxY = viewPort.getImageHeight();
	    double dWcY = viewPort.getAdjustedExtent().getLength(1);
	    double tileWcH = (getTile(vpCnt).getHeight() * dWcY) / imgPxY;

	    viewPortList[0] = viewPort.cloneViewPort();
	    
	    int auxw = (int) getTile(vpCnt).getWidth();
	    int auxh = (int) getTile(vpCnt).getHeight();
	    Dimension auxdim = new Dimension(auxw, auxh);
	    
	    viewPortList[0].setImageSize(auxdim);
	    Envelope r=viewPort.getAdjustedExtent();
	    try {
			viewPortList[0].setEnvelope(geomManager.createEnvelope(r.getMinimum(0), r.getMaximum(1) - tileWcH,r.getMinimum(0)+tileWcW, r.getMaximum(1), SUBTYPES.GEOM2D));
		} catch (CreateEnvelopeException e) {
			logger.error("Error setting the envelope", e);
			e.printStackTrace();
		}
	    viewPortList[0].setAffineTransform(mat);

	    double wt = tileWcW;
	    double ht = tileWcH;
	    double xt = viewPort.getAdjustedExtent().getMinimum(0);
	    double yt = viewPort.getAdjustedExtent().getMaximum(1) - tileWcH;

	    for (int stepY=0; stepY < numRows; stepY++) {
	    	wt = tileWcW;
	    	xt = viewPort.getAdjustedExtent().getMinimum(0);
	    	for (int stepX=0; stepX < numCols; stepX++) {
	    		vpCnt = stepY*numCols+stepX;
	    		if(vpCnt > 0){
	    			if(stepX > 0)
	    				xt += wt;
	    			if((xt + wt) > viewPort.getAdjustedExtent().getMaximum(0))
	    				wt = Math.abs(viewPort.getAdjustedExtent().getMaximum(0) - xt);

	    			viewPortList[vpCnt] = viewPort.cloneViewPort();
	    			
	    		    auxw = (int) getTile(vpCnt).getWidth();
	    		    auxh = (int) getTile(vpCnt).getHeight();
	    		    auxdim = new Dimension(auxw, auxh);

	    			viewPortList[vpCnt].setImageSize(auxdim);
	    			
	    			try {
						viewPortList[vpCnt].setEnvelope(geomManager.createEnvelope(xt, yt,xt + wt, yt + ht, SUBTYPES.GEOM2D));
					} catch (CreateEnvelopeException e) {
						logger.error("Error setting the envelope", e);
						e.printStackTrace();
					}
	    			viewPortList[vpCnt].setAffineTransform(mat);

	    		}
	    		//System.out.println("ViewPort: "+vpCnt+" "+viewPortList[vpCnt].getAdjustedExtent()+" "+getTile(vpCnt).getSize());
	    	}
	    	if((yt - ht) < viewPort.getAdjustedExtent().getMinimum(1)){
	    		ht = Math.abs(yt - viewPort.getAdjustedExtent().getMinimum(1));
	    		yt = viewPort.getAdjustedExtent().getMinimum(1);
	    	}else
	    		yt -= ht;
	    }
	}

	public ViewPort getTileViewPort(ViewPort viewPort, int tileNr) throws NoninvertibleTransformException {
		/*if(viewPortList == null)
			this.calcViewPort(viewPort);
		return viewPortList[tileNr];*/

		if(tile.length == 1)
			return viewPort;

		double [] dstPts = new double[8];
		double [] srcPts = getTilePts(tileNr);
		Rectangle2D tile = getTile(tileNr);
		//Rectangle newRect = new Rectangle((int)srcPts[0], (int)srcPts[1], tileSz[0], tileSz[1]);

		mat.inverseTransform(srcPts, 0, dstPts, 0, 4);
		double x = dstPts[0], w = dstPts[2] - dstPts[0];
		double y = dstPts[1], h = dstPts[5] - dstPts[3];
		if (w < 0) { x = dstPts[2]; w = dstPts[0] - dstPts[2]; }
		if (h < 0) { y = dstPts[5]; h = dstPts[3] - dstPts[5]; }
		Envelope rectCuadricula = null;
		try {
			rectCuadricula = geomManager.createEnvelope(x, y,x+ w,y+ h, SUBTYPES.GEOM2D);
		} catch (CreateEnvelopeException e) {
			logger.error("Error setting the envelope", e);
			e.printStackTrace();
		}
		//Extent extent = new Extent(rectCuadricula);

		ViewPort vp = viewPort.cloneViewPort();
		
		
	    int auxw = (int) tile.getWidth();
	    int auxh = (int) tile.getHeight();
	    Dimension auxdim = new Dimension(auxw, auxh);

		vp.setImageSize(auxdim);
		//vp.setOffset(tile.getLocation());
		vp.setEnvelope(rectCuadricula);
		vp.setAffineTransform(mat);

		if (debug)
    		System.out.println("Tiling.print(): tile "+tileNr+" de "
    		        + getNumTiles() +
    		        "\n, Extent = "+vp.getAdjustedExtent() + " tile: "
    		        + tile);

		return vp;
	}
	/**
	 * @return Returns the mat.
	 */
	public AffineTransform getAffineTransform() {
		return mat;
	}
	/**
	 * @param mat The mat to set.
	 */
	public void setAffineTransform(AffineTransform mat) {
		this.mat = mat;
	}
	/**
	 * @return Returns the debug.
	 */
	public boolean isDebug() {
		return debug;
	}
	/**
	 * @param debug The debug to set.
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}


