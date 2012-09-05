package org.gvsig.gpe.parser;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.namespace.QName;

import org.gvsig.gpe.containers.Bbox;
import org.gvsig.gpe.containers.Curve;
import org.gvsig.gpe.containers.Element;
import org.gvsig.gpe.containers.Feature;
import org.gvsig.gpe.containers.Geometry;
import org.gvsig.gpe.containers.Layer;
import org.gvsig.gpe.containers.LineString;
import org.gvsig.gpe.containers.LinearRing;
import org.gvsig.gpe.containers.MetaData;
import org.gvsig.gpe.containers.MultiCurve;
import org.gvsig.gpe.containers.MultiGeometry;
import org.gvsig.gpe.containers.MultiLineString;
import org.gvsig.gpe.containers.MultiPoint;
import org.gvsig.gpe.containers.MultiPolygon;
import org.gvsig.gpe.containers.Point;
import org.gvsig.gpe.containers.Polygon;
import org.gvsig.gpe.containers.Time;

/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
/* CVS MESSAGES:
 *
 * $Id: GPEContentHandlerTest.java 202 2007-11-27 12:00:11Z jpiera $
 * $Log$
 * Revision 1.14  2007/06/07 14:52:28  jorpiell
 * Add the schema support
 *
 * Revision 1.13  2007/05/15 12:09:41  jorpiell
 * The bbox is linked to the feature
 *
 * Revision 1.12  2007/05/15 11:54:35  jorpiell
 * A wrong label fixed
 *
 * Revision 1.11  2007/05/15 07:28:34  jorpiell
 * Children Element printed
 *
 * Revision 1.10  2007/05/14 09:29:34  jorpiell
 * Add some comments when an element is added
 *
 * Revision 1.9  2007/05/09 10:25:45  jorpiell
 * Add the multiGeometries
 *
 * Revision 1.8  2007/05/09 08:35:58  jorpiell
 * fixed an exception
 *
 * Revision 1.7  2007/05/02 11:46:07  jorpiell
 * Writing tests updated
 *
 * Revision 1.6  2007/04/26 14:39:12  jorpiell
 * Add some tests
 *
 * Revision 1.5  2007/04/19 07:23:20  jorpiell
 * Add the add methods to teh contenhandler and change the register mode
 *
 * Revision 1.4  2007/04/17 07:53:55  jorpiell
 * Before to start a new parsing process, the initialize method of the content handlers is throwed
 *
 * Revision 1.3  2007/04/14 16:06:35  jorpiell
 * Add the container classes
 *
 * Revision 1.2  2007/04/13 13:14:55  jorpiell
 * Created the base tests and add some methods to the content handler
 *
 * Revision 1.1  2007/04/12 17:06:42  jorpiell
 * First GML writing tests
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class GPEContentHandlerTest extends GPEContentHandler{
	private String tab = "";
	private ArrayList layers = new ArrayList();

	/**
	 * @return the layers without parent layer
	 */
	public ArrayList getLayers() {
		ArrayList newLayers = new ArrayList();
		for (int i=0 ; i<layers.size() ; i++){
			if (((Layer)layers.get(i)).getParentLayer() == null){
				newLayers.add(layers.get(i));
			}
		}
		return newLayers;
	}

	public void addNameToFeature(Object feature, String name){
		System.out.print(tab + "Feature name changed: " + name + "\n");
		((Feature)feature).setName(name);
	}

	public void endFeature(Object feature) {
		System.out.print(tab + "End Feature\n");		
	}	

	public void initialize(){
		layers = new ArrayList();
	}

	public void addBboxToFeature(Object bbox, Object feature) {
		((Feature)feature).setBbox(bbox);

	}

	public void addBboxToLayer(Object bbox, Object layer) {
		Bbox box = (Bbox)bbox;
		System.out.print(tab + "Layer bbox Added:\n");
		tab = tab + "\t";
		System.out.print(tab + "MINX: " + box.getMinX() + "\n");
		System.out.print(tab + "MINY: " + box.getMinY() + "\n");
		System.out.print(tab + "MINZ: " + box.getMinZ() + "\n");
		System.out.print(tab + "MAXX: " + box.getMaxX() + "\n");
		System.out.print(tab + "MAXY: " + box.getMaxY() + "\n");
		System.out.print(tab + "MAXZ: " + box.getMaxZ() + "\n");
		System.out.print(tab + "SRS: " + box.getSrs() + "\n");
		tab = tab.substring(0, tab.length()-1);
		((Layer)layer).setBbox(bbox);
	}

	public void addDescriptionToLayer(String description, Object layer) {
		System.out.print(tab + "Layer description changed: " + description + "\n");
		((Layer)layer).setDescription(description);
	}

	public void addElementToFeature(Object element, Object feature) {
		Element elem = (Element)element;
		tab = tab + "\t";
		System.out.print(tab + "Add Element " + elem.getName() + "=" +
				elem.getValue() + " to Feature\n");
		printChildElements(elem);		
		tab = tab.substring(0, tab.length()-1);
		((Feature)feature).addElement(element);		
	}

	/**
	 * Print the element children 
	 * @param element to print
	 */
	private void printChildElements(Element element){
		tab = tab + "\t";
		for (int i=0 ; i< element.getElements().size() ; i++){
			Element child = element.getElementAt(i);
			System.out.print(tab + "- Element " + child.getName() + "=" +
					child.getValue() + "\n");
			printChildElements(child);
		}
		tab = tab.substring(0, tab.length()-1);
	}

	public void addFeatureToLayer(Object feature, Object layer) {
		((Layer)layer).addFeature(feature);

	}

	public void addGeometryToFeature(Object geometry, Object feature) {
		((Feature)feature).setGeometry(geometry);

	}

	public void addInnerPolygonToPolygon(Object innerPolygon, Object polygon) {
		((Polygon)polygon).addInnerBoundary(innerPolygon);

	}

	public void addNameToFeature(String name, Object feature) {
		((Feature)feature).setName(name);

	}

	public void addNameToLayer(String name, Object layer) {
		System.out.print(tab + "Layer name changed: " + name + "\n");
		((Layer)layer).setName(name);		
	}

	public void addParentElementToElement(Object parent, Object element) {
		((Element)element).setParentElement(parent);

	}

	public void addParentLayerToLayer(Object parent, Object layer) {
		((Layer)layer).setParentLayer(parent);

	}

	public void addSrsToLayer(String srs, Object layer) {
		((Layer)layer).setSrs(srs);

	}


	public Object startBbox(String id, ICoordinateIterator coords, String srs) {
		Bbox bbox = new Bbox(coords.getDimension());
		double[] min = new double[coords.getDimension()];
		double[] max = new double[coords.getDimension()];
		try {
			if (coords.hasNext()){
				coords.next(min);
				bbox.addCoordinate(min);
				if (coords.hasNext()){
					coords.next(max);
					bbox.addCoordinate(max);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		bbox.setId(id);
		bbox.setSrs(srs);
		return bbox;
	}

	public void endBbox(Object bbox) {
		// TODO Apéndice de método generado automáticamente
	}

	public Object startElement(String namespace, String name, Object value,  IAttributesIterator iterator, Object parentElement) {
		Element element = new Element();
		element.setParentElement(parentElement);
		element.setName(name);
		element.setValue(value);		
		return element;
	}

	public void endElement(Object element) {
		// TODO Apéndice de método generado automáticamente

	}

	public Object startFeature(String id, String namspace, String name, IAttributesIterator iterator, Object layer) {
		System.out.print(tab + "Start Feature, ID: " +  id + " NAME: " + name + "\n");
		Feature feature = new Feature();
		feature.setName(name);
		feature.setId(id);
		if (layer != null){
			addFeatureToLayer(feature, layer);
		}
		return feature;
	}

	public Object startInnerPolygon(String id, ICoordinateIterator coords, String srs) {
		tab = tab + "\t";
		System.out.print(tab + "Start InnerPolygon, SRS:" + srs + "\n");
		tab = tab + "\t";
		Polygon inner = new Polygon();		
		try {
			while(coords.hasNext()){
				double[] buffer = new double[coords.getDimension()];
				coords.next(buffer);
				inner.addCoordinate(buffer);
				System.out.print(tab);
				for (int i=0 ; i<buffer.length ; i++){
					System.out.print(buffer[i]);
					if (i<buffer.length-1){
						System.out.print(",");
					}				
				}
				System.out.print("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
		inner.setId(id);
		inner.setSrs(srs);
		tab = tab.substring(0, tab.length()-2);
		return inner;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEContentHandler#endInnerPolygon(java.lang.Object)
	 */
	public void endInnerPolygon(Object polygon){
		tab = tab + "\t";
		System.out.print(tab + "End InnerPolygon\n");
		tab = tab.substring(0, tab.length()-1);
	}


	public Object startLayer(String id, String namespace, String name, String description, String srs,  IAttributesIterator iterator, Object parentLayer, Object bBox) {
		System.out.print(tab + "Start Layer, ID: " +  id + " NAME: " + name + "\n");
		tab = tab + "\t";
		Layer layer = new Layer();
		layer.setId(id);
		layer.setName(name);
		layer.setDescription(description);
		layer.setSrs(srs);
		layer.setBbox(bBox);
		layer.setParentLayer(parentLayer);
		if (parentLayer != null){
			((Layer)parentLayer).addLayer(layer);
		}
		layers.add(layer);
		return layer;
	}	

	public void endLayer(Object layer) {
		tab = tab.substring(0, tab.length()-1);
		System.out.print(tab + "End Layer\n");		
	}

	public Object startLineString(String id, ICoordinateIterator coords, String srs) {
		tab = tab + "\t";
		System.out.print(tab + "Start LineString, SRS:" + srs + "\n");
		tab = tab + "\t";		
		LineString lineString = new LineString();		
		try {
			while(coords.hasNext()){
				double[] buffer = new double[coords.getDimension()];
				coords.next(buffer);
				lineString.addCoordinate(buffer);
				System.out.print(tab);
				for (int i=0 ; i<buffer.length ; i++){
					System.out.print(buffer[i]);
					if (i<buffer.length-1){
						System.out.print(",");
					}				
				}
				System.out.print("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
		lineString.setId(id);
		lineString.setSrs(srs);
		tab = tab.substring(0, tab.length()-2);
		return lineString;
	}

	public void endLineString(Object line) {
		System.out.print(tab + "\t"+ "End LineString:\n");
	}	

	public Object startPoint(String id, ICoordinateIterator coords, String srs) {
		tab = tab + "\t";
		System.out.print(tab + "Start Point, SRS:" + srs + "\n");
		tab = tab + "\t";		
		Point point = new Point();
		double[] buffer = new double[coords.getDimension()];
		try {
			if (coords.hasNext()){
				coords.next(buffer);
				point.setCoordinates(buffer);				
				System.out.print(tab);
				for (int i=0 ; i<buffer.length ; i++){
					System.out.print(buffer[i]);
					if (i<buffer.length-1){
						System.out.print(",");
					}				
				}
				System.out.print("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		point.setId(id);
		point.setSrs(srs);
		tab = tab.substring(0, tab.length()-2);
		return point;
	}	

	public void endPoint(Object point) {
		System.out.print(tab + "\t" + "End Point\n");
	}

	public Object startPolygon(String id, ICoordinateIterator coords, String srs) {
		tab = tab + "\t";
		System.out.print(tab + "Start Polygon, SRS:" + srs + "\n");
		tab = tab + "\t";
		Polygon polygon = new Polygon();		
		try {
			while(coords.hasNext()){
				double[] buffer = new double[coords.getDimension()];
				coords.next(buffer);
				polygon.addCoordinate(buffer);
				System.out.print(tab);
				for (int i=0 ; i<buffer.length ; i++){
					System.out.print(buffer[i]);
					if (i<buffer.length-1){
						System.out.print(",");
					}				
				}
				System.out.print("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
		polygon.setId(id);
		polygon.setSrs(srs);
		tab = tab.substring(0, tab.length()-2);
		return polygon;
	}


	public void endPolygon(Object polygon) {
		System.out.print(tab + "\t"+ "End Polygon\n");
	}


	public Object startLinearRing(String id, ICoordinateIterator coords, String srs) {
		System.out.print(tab + "Start LinearRing, SRS:" + srs + "\n");
		tab = tab + "\t";
		LinearRing linearRing = new LinearRing();		
		try {
			while(coords.hasNext()){
				double[] buffer = new double[coords.getDimension()];
				coords.next(buffer);
				linearRing.addCoordinate(buffer);
				System.out.print(tab);
				for (int i=0 ; i<buffer.length ; i++){
					System.out.print(buffer[i]);
					if (i<buffer.length-1){
						System.out.print(",");
					}				
				}
				System.out.print("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
		linearRing.setId(id);
		linearRing.setSrs(srs);
		tab = tab.substring(0, tab.length()-1);
		return linearRing;
	}


	public void endLinearRing(Object linearRing) {
		System.out.print(tab + "End LinearRing\n");
	}


	public Object startMultiPoint(String id, String srs) {
		System.out.print(tab + "Start MultiPoint, ID: " + id + ", SRS:" + srs + "\n");
		tab = tab + "\t";
		MultiPoint multiPoint = new MultiPoint();
		multiPoint.setId(id);
		multiPoint.setSrs(srs);
		return multiPoint;
	}	

	public void endMultiPoint(Object multiPoint) {
		tab = tab.substring(0, tab.length()-1);		
		System.out.print(tab + "End MultiPoint\n");		
	}

	public void addPointToMultiPoint(Object point, Object multiPoint) {
		System.out.print(tab + "Add Point to MultiPoint");	
		((MultiPoint)multiPoint).addPoint((Point)point);
	}

	public Object startMultiLineString(String id, String srs) {
		System.out.print(tab + "Start MultiLineString, ID: " + id + ", SRS:" + srs + "\n");
		tab = tab + "\t";
		MultiLineString multiLineString = new MultiLineString();
		multiLineString.setId(id);
		multiLineString.setSrs(srs);
		return multiLineString;		
	}

	public void endMultiLineString(Object multiLineString) {
		tab = tab.substring(0, tab.length()-1);		
		System.out.print(tab + "End MultiLineString\n");		
	}	

	public void addLineStringToMultiLineString(Object lineString, Object multiLineString) {
		System.out.print(tab + "Add LineString to MultiLineString");	
		((MultiLineString)multiLineString).addLineString((LineString)lineString);
	}

	public Object startMultiPolygon(String id, String srs) {
		System.out.print(tab + "Start MultiPolygon, ID: " + id + ", SRS:" + srs + "\n");
		tab = tab + "\t";
		MultiPolygon multiPolygon = new MultiPolygon();
		multiPolygon.setId(id);
		multiPolygon.setSrs(srs);
		return multiPolygon;	
	}

	public void endMultiPolygon(Object multiPolygon) {
		tab = tab.substring(0, tab.length()-1);		
		System.out.print(tab + "End MultiPolygon\n");		
	}

	public void addPolygonToMultiPolygon(Object polygon, Object multiPolygon) {
		System.out.print(tab + "Add Polygon to MultiPolygon");	
		((MultiPolygon)multiPolygon).addPolygon((Polygon)polygon);
	}

	public Object startMultiGeometry(String id, String srs) {
		System.out.print(tab + "Start MultiGeometry, ID: " + id + ", SRS:" + srs + "\n");
		tab = tab + "\t";
		MultiGeometry multiGeometry = new MultiGeometry();
		multiGeometry.setId(id);
		multiGeometry.setSrs(srs);
		return multiGeometry;	
	}

	public void endMultiGeometry(Object multiGeometry) {
		tab = tab.substring(0, tab.length()-1);		
		System.out.print(tab + "End MultiGeometry\n");		
	}

	public void addGeometryToMultiGeometry(Object geometry, Object multiGeometry) {
		if (geometry instanceof Point){
			System.out.print(tab + "Add Point to MultiGeometry");
		}else if (geometry instanceof LineString){
			System.out.print(tab + "Add LineString to MultiGeometry");
		}else if (geometry instanceof Polygon){
			System.out.print(tab + "Add Polygon to MultiGeometry");
		}else if (geometry instanceof Curve){
			System.out.print(tab + "Add LineString to Curve");
		}else {
			System.out.print(tab + "Add Geometry to MultiGeometry");
		}		
		((MultiGeometry)multiGeometry).addGeometry((Geometry)geometry);
	}


	public Object startCurve(String id, ICoordinateIterator coords, String srs){
		tab = tab + "\t";
		System.out.print(tab + "Start Curve, ID: " + id + ", SRS:" + srs + "\n");
		tab = tab + "\t";
		Curve curve = new Curve();		
		try {
			while(coords.hasNext()){
				double[] buffer = new double[coords.getDimension()];
				coords.next(buffer);
				curve.addCoordinate(buffer);
				System.out.print(tab);
				for (int i=0 ; i<buffer.length ; i++){
					System.out.print(buffer[i]);
					if (i<buffer.length-1){
						System.out.print(",");
					}				
				}
				System.out.print("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
		curve.setId(id);
		curve.setSrs(srs);
		tab = tab.substring(0, tab.length()-2);
		return curve;
	}	

	public void endCurve(Object curve) {
		System.out.print(tab + "\t"+ "End Curve:\n");				
	}

	public Object startMultiCurve(String id, String srs) {
		System.out.print(tab + "Start MultiCurve, ID: " + id + ", SRS:" + srs + "\n");
		tab = tab + "\t";
		MultiCurve multiCurve = new MultiCurve();
		multiCurve.setId(id);
		multiCurve.setSrs(srs);
		return multiCurve;
	}

	public void endMultiCurve(Object multiCurve) {
		tab = tab.substring(0, tab.length()-1);		
		System.out.print(tab + "End MultiCurve\n");		
	}

	public void addCurveToMultiCurve(Object curve, Object multiCurve) {
		System.out.print(tab + "Add Curve to MultiCurve");	
		((MultiCurve)multiCurve).addCurve((Curve)curve);
	}

	public Object startMetadata(String type, String data, IAttributesIterator attributes){
		tab = tab + "\t";
		MetaData meta=null;
		System.out.print(tab + "Start Metadata, META: " + type + ", DATA:" + data + "\n");
		try {
			
			for(int i = 0; i<attributes.getNumAttributes();i++)
			{
				//String[] buffer = new String[2];
				QName name = attributes.nextAttributeName();
				Object value = attributes.nextAttribute();
				System.out.print(tab + name + " : " + value + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		meta = new MetaData();
		meta.setTagType(type);
		meta.setTagData(data);
		return meta;
	}
	
	public void endMetadata(Object metadata) {
		tab = tab.substring(0, tab.length()-1);
		System.out.print(tab + "\t"+ "End Metadata:\n");
	}
	
	public void addMetadataToFeature(Object metadata, Object feature){
		System.out.print(tab+tab + "Add Metadata to Feature \n");	
		((Feature)feature).addMetadata((MetaData)metadata);
	} 
	
	public void addMetadataToMetadata(Object metadata, Object parent){
		System.out.print(tab + "Add Metadata to Complex Metadata\n");	
		((MetaData)parent).addChildData((MetaData)metadata);
	}

	public void addTimeToFeature(Object time, Object feature) {
		System.out.print(tab + "Add Time to feature\n");	
		((Feature)feature).addTime(time);
	}

	public void endTime(Object time) {
		tab = tab.substring(0, tab.length()-1);
		System.out.print(tab + "\t"+ "End Time:\n");
	}

	public Object startTime(String type, String time) {
		tab = tab + "\t";
		System.out.print(tab + "Start Time, TYPE: " + type + ", DATA:" + time + "\n");
		Time t = new Time();
		t.setType(type);
		t.setValue(time);		
		return t;
	}

	public Object startTime(String name, String description, String type,
			String time, Time previous, Time next) 
	{
		tab = tab + "\t";
		System.out.print(tab + "Start Time, NAME: " + name + ", DATA:" + time + "\n");
		Time t = new Time();
		t.setType(type);
		t.setValue(time);	
		t.setDescription(description);
		t.setName(name);
		t.setPrevious(previous);
		t.setNext(next);
		return t;
	} 
}

