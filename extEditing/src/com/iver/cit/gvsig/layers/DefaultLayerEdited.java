package com.iver.cit.gvsig.layers;

import org.gvsig.fmap.mapcontext.layers.FLayer;


/**
 * @author fjp
 *
 * Clase padre de las capas en edici�n, por si alguna vez
 * necesitamos editar Raster
 */
public class DefaultLayerEdited implements ILayerEdited {
	private FLayer lyr;

	public DefaultLayerEdited(FLayer lyr)
	{
		this.lyr = lyr;
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.ILayerEdited#getLayer()
	 */
	public FLayer getLayer() {
		return lyr;
	}
}
