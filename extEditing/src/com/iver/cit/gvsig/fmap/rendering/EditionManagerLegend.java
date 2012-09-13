package com.iver.cit.gvsig.fmap.rendering;

import java.util.ArrayList;

import org.gvsig.fmap.mapcontext.rendering.legend.IVectorLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.IVectorialUniqueValueLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.VectorialIntervalLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.VectorialUniqueValueLegend;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;

import com.iver.andami.PluginServices;

public class EditionManagerLegend implements EditionLegend{
	private ArrayList rules=new ArrayList();
	private IVectorLegend vectorialLegend;
	private IVectorLegend originalVectorialLegend;
	private LegendControl lc=new LegendControl();

	public EditionManagerLegend(IVectorLegend vl) {
		originalVectorialLegend=vl;
		vectorialLegend=vl;//(VectorialLegend)vl.cloneLegend();
	}
	public Object getValue(int i) {
		Object value=null;
		if (vectorialLegend instanceof VectorialUniqueValueLegend) {
			value=((VectorialUniqueValueLegend)vectorialLegend).getValues()[i];
		}else if (vectorialLegend instanceof VectorialIntervalLegend) {
			value=((VectorialIntervalLegend)vectorialLegend).getValues()[i];
		}else {
			value=new String(PluginServices.getText(this,"todos_los_valores"));
		}
		return value;
	}
	public ISymbol getSymbol(int i) {
		ISymbol symbol=null;
			symbol=((IVectorialUniqueValueLegend)vectorialLegend).getSymbolByValue(getValue(i));
		return symbol;
	}
	public boolean isActived(int i) {
		return lc.isActivated(i);
	}
	public boolean isBlocked(int i) {
		return lc.isBlocked(i);
	}
	public boolean isDisable(int i) {
		return lc.isDisabled(i);
	}
	public boolean isFilled(int i) {
		return lc.isFilled(i);
	}
	public boolean isPresent(int i) {
		return (i==getPresent());
	}
	private int getPresent() {
		return lc.getPresent();
	}
	public void setActived(int i,boolean b) {
		lc.setActivated(i,b);
	}
	public void setBlocked(int i,boolean b) {
		lc.setBlocked(i,b);
	}
	public void setDisable(int i, boolean b) {
		lc.setDisabled(i,b);
	}
	public void setFilled(int i,boolean b) {
		lc.setFilled(i,b);
	}
	public void setPresent(int i) {
		lc.setPresent(i);
	}
	public int getRowCount() {
		if (vectorialLegend instanceof VectorialUniqueValueLegend || vectorialLegend instanceof VectorialIntervalLegend) {
    		VectorialUniqueValueLegend vuvl=(VectorialUniqueValueLegend)vectorialLegend;
    		return vuvl.getValues().length;
    	}
        return 1;
	}
	public void setValue(int i, Object value) {
		Object previousValue=getValue(i);
		ISymbol previousSymbol=getSymbol(i);
		Object clave;
	    ISymbol theSymbol=null;
	    int numRow=getRowCount();
	    // Borramos las anteriores listas:
	    //((UniqueValueLegend)vectorialLegend).clear();

	    boolean bRestoValores = false; // PONERLO EN UN CHECKBOX
	    int hasta;
	    hasta = getRowCount();
//	    for (int row = 0; row < numRow; row++) {
//	        clave = getValue(row);
//	    	if (row==i)
	    if (!value.equals(previousValue)) {
	    	((IVectorialUniqueValueLegend)vectorialLegend).delSymbol(previousValue);
	    	clave=value;
	        ((IVectorialUniqueValueLegend)vectorialLegend).addSymbol(value, previousSymbol);
	        System.out.println(value);
//	    }
	    }
	    if (bRestoValores) {
	      	theSymbol = getSymbol(hasta);
	       	vectorialLegend.setDefaultSymbol(theSymbol);
	    }
	}
	public void setSymbol(int row, Object value) {
	}
	public String getPresentSubLayer() {
		return getValue(getPresent()).toString();
	}
}
