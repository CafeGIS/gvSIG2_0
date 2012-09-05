package com.iver.ai2.gvsig3d.legend.symbols;

import org.gvsig.fmap.mapcontext.rendering.symbols.SimpleFillSymbol;

public class ExtrusionPolygonSymbol extends SimpleFillSymbol implements
		IExtrusionSymbol {

	private double extrusion = 0.0;

	public ExtrusionPolygonSymbol() {
		// TODO Auto-generated constructor stub
	}
	
	public double getExtrusion() {
		return this.extrusion;
	}

	public void setExtrusion(double extrusion) {
		this.extrusion = extrusion;
	}

}
