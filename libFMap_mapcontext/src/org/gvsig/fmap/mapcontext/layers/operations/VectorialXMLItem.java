package org.gvsig.fmap.mapcontext.layers.operations;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;


public class VectorialXMLItem implements XMLItem {

	private FeatureSet selection;
	private FLayer layer;

	public VectorialXMLItem(FeatureSet selection, FLayer layer) {
		this.selection = selection;
		this.layer = layer;
	}

	public FLayer getLayer(){
		return layer;
	}
	/**
	 * @see com.iver.cit.gvsig.gui.toolListeners.InfoListener.XMLItem#parse(org.xml.sax.ContentHandler)
	 */
	public void parse(ContentHandler handler) throws SAXException {
		AttributesImpl aii = new AttributesImpl();
		handler.startElement("", "", (layer).getName(), aii);
		try {

//			FeatureStore ds = ((FLyrVect) layer).getFeatureStore();
			DisposableIterator iterator = selection.iterator();
			int j=0;
			FeatureAttributeDescriptor attr;
			Object value;
			String strValue;
			while (iterator.hasNext()) {
				Feature feature = (Feature) iterator.next();
				AttributesImpl ai = new AttributesImpl();
				FeatureType featureType=((FLyrVect) layer).getFeatureStore().getDefaultFeatureType();
				for (int k = 0; k < featureType.size(); k++) {
					attr = (FeatureAttributeDescriptor) featureType
							.get(k);
					value = feature.get(k);
					if (value == null) {
						strValue = "{null}";
					} else {
						strValue = value.toString();
					}
					ai.addAttribute("", attr.getName(), "",
					"xs:string",
							strValue);
				}
				handler.startElement("", "", String.valueOf(j), ai);
				handler.endElement("", "", String.valueOf(j));
				j++;
			}
			iterator.dispose();

			//TODO
//			ds.start();
//
//			for (int j = bitset.nextSetBit(0); j >= 0; j = bitset
//					.nextSetBit(j + 1)) {
//				AttributesImpl ai = new AttributesImpl();
//
//				for (int k = 0; k < ds.getFieldCount(); k++) {
//					ai.addAttribute("", ds.getFieldName(k), "",
//							"xs:string", ds.getFieldValue(j, k).toString());
//				}
//				handler.startElement("", "", String.valueOf(j), ai);
//				handler.endElement("", "", String.valueOf(j));
//			}
//
//			ds.stop();

		} catch (ReadException e) {
			throw new SAXException(e);
		} catch (DataException e) {
			throw new SAXException(e);
		}
		handler.endElement("", "", (layer).getName());
	}
}

