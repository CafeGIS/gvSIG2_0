package org.gvsig.gvsig3d.simbology3D;

import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;


public abstract class AbstractFeature3D implements I3DFeature {
	
	private ISymbol symbol;
	private IGeometry geometry;
	
	public AbstractFeature3D(ISymbol symbol, IGeometry geometry) {
		super();
		this.symbol = symbol;
		this.geometry = geometry;
	}

	public ISymbol getSymbol() {
		return symbol;
	}

	public void setSymbol(ISymbol symbol) {
		this.symbol = symbol;
	}

	public IGeometry getGeometry() {
		return geometry;
	}

	public void setGeometry(IGeometry geometry) {
		this.geometry = geometry;
	}

	

}
