package com.iver.ai2.gvsig3d.legend.symbols;

import org.gvsig.fmap.mapcontext.rendering.symbols.SimpleMarkerSymbol;

public class ExtrusionPointSymbol extends SimpleMarkerSymbol implements IExtrusionSymbol{

	private double extrusion = 0.0;
	
	public ExtrusionPointSymbol() {
		// TODO Auto-generated constructor stub
	}
	
	public double getExtrusion() {
		return this.extrusion;
	}

	public void setExtrusion(double extrusion) {
		this.extrusion = extrusion;
	}

}
