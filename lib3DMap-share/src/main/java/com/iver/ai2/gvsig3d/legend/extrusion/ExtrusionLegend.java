package com.iver.ai2.gvsig3d.legend.extrusion;

import java.awt.Color;

import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.mapcontext.rendering.symbols.IFillSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ILineSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.IMarkerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SimpleFillSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SimpleLineSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SimpleMarkerSymbol;

import com.iver.ai2.gvsig3d.legend.symbols.BaseExtrusionSymbol;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

public class ExtrusionLegend extends org.gvsig.fmap.mapcontext.rendering.legend.VectorialUniqueValueLegend {

	private BaseExtrusionSymbol defaultSimbolExtrusion;
	private double extrusionFactor;


	public ExtrusionLegend() {
	}
	public ExtrusionLegend(int shapeType) {
		super(shapeType);
	}
	public ISymbol getSymbolByFeature(Feature feat) {
		return super.getSymbolByFeature(feat);
	}

	public ISymbol getSymbolByValue(Object key) {
		ISymbol theSymbol = super.getSymbolByValue(key);

		if (theSymbol== null)
			return null;
		double extrusion = Double.parseDouble(theSymbol.getDescription());
		
		

		defaultSimbolExtrusion = new BaseExtrusionSymbol(extrusion);
		if (theSymbol.getClass().equals(SimpleFillSymbol.class)) {// Nuevo símbolo polígono
			Color color = ((IFillSymbol)theSymbol).getFillColor();
			defaultSimbolExtrusion.setFillColor(color);
		}
		else if (theSymbol.getClass().equals(SimpleLineSymbol.class)) {// Nuevo símbolo línea
			Color color = ((ILineSymbol)theSymbol).getColor();
			defaultSimbolExtrusion.setFillColor(color);
		}
		else if (theSymbol.getClass().equals(SimpleMarkerSymbol.class)) {// Nuevo símbolo punto
			Color color = ((IMarkerSymbol)theSymbol).getColor();
			defaultSimbolExtrusion.setFillColor(color);
		}

//		defaultSimbolExtrusion.setExtrusion(factorExtrusion);

		return defaultSimbolExtrusion;
	}
	
	public String getClassName() {
		return getClass().getName();
	}
	
	public double getExtrusionFactor() {
		return extrusionFactor;
	}
	
	public void setExtrusionFactor(double extrusionFactor) {
		this.extrusionFactor = extrusionFactor;
	}
	

	public XMLEntity getXMLEntity() {
		XMLEntity xml=null;
		try {
			xml = super.getXMLEntity();
		} catch (XMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xml.putProperty("extrusionFactor", this.extrusionFactor);
		return xml;

	}

	public void setXMLEntity(XMLEntity xml) {
		// TODO: It is necessary to insert here all the properties to load with the extrusion legend.
		super.setXMLEntity(xml);		
		if (xml.contains("extrusionFactor"))
			extrusionFactor = xml.getDoubleProperty("extrusionFactor");	
	}

}
