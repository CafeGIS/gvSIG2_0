package com.iver.ai2.gvsig3d.legend.symbols;

import org.gvsig.fmap.mapcontext.rendering.symbols.SimpleFillSymbol;

import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

public class BaseExtrusionSymbol extends SimpleFillSymbol implements
		IExtrusionSymbol {

	private double extrusion = 0.0;

	public double getExtrusion() {
		return this.extrusion;
	}

	public void setExtrusion(double extrusion) {
		this.extrusion = extrusion;
	}

	public BaseExtrusionSymbol(double extrusion) {
		super();
		this.extrusion = extrusion;
	}
	

	public String getClassName() {
		return getClass().getName();
	}

	public XMLEntity getXMLEntity() {
		XMLEntity xml=null;
		try {
			xml = super.getXMLEntity();
		} catch (XMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xml.putProperty("extrusion", this.extrusion);
		return xml;
	}

	public void setXMLEntity(XMLEntity xml) {
		super.setXMLEntity(xml);
		if (xml.contains("extrusion"))
			extrusion = xml.getDoubleProperty("extrusion");
	}

	
}
