package com.iver.ai2.gvsig3d.legend.symbols;

import org.gvsig.fmap.mapcontext.rendering.symbols.SimpleLineSymbol;

public class ExtrusionLineSymbol extends SimpleLineSymbol implements IExtrusionSymbol {

	
	private double extrusion = 0.0;
	
	public ExtrusionLineSymbol() {
		// TODO Auto-generated constructor stub
	}
	
	public double getExtrusion() {
		return this.extrusion;
	}

	public void setExtrusion(double extrusion) {
		this.extrusion = extrusion;
	}


}
