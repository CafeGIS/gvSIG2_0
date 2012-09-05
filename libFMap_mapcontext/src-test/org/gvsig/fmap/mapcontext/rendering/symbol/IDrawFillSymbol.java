package org.gvsig.fmap.mapcontext.rendering.symbol;

import org.gvsig.fmap.mapcontext.rendering.symbols.IFillSymbol;

public interface IDrawFillSymbol {

	IFillSymbol makeSymbolTransparent(IFillSymbol newSymbol);

	boolean isSuitableFor(IFillSymbol newSymbol);

}
