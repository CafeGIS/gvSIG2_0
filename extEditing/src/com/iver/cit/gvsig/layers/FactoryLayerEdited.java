package com.iver.cit.gvsig.layers;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;

public class FactoryLayerEdited {

	public static ILayerEdited createLayerEdited(FLayer lyr) {
		if (lyr instanceof FLyrVect)
			return new VectorialLayerEdited(lyr);
		return null;
	}

}
