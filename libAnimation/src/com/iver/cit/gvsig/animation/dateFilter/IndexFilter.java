/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
*/

package com.iver.cit.gvsig.animation.dateFilter;

//import com.hardcode.gdbms.engine.values.NullValue;
//import com.hardcode.gdbms.engine.values.NumericValue;
//import com.hardcode.gdbms.engine.values.Value;
//import com.iver.cit.gvsig.fmap.core.IFeature;
//import com.iver.utiles.IPersistence;
//import com.iver.utiles.XMLEntity;

//public class IndexFilter implements ICustomVectorialFilter, IPersistence {
	public class IndexFilter  {

//	private int filterFieldIndex=-1;
//	private int minIndex = -1;
//	private int maxIndex = -1;
//	
//	public void setFieldIndex(int index) {
//		filterFieldIndex = index;
//	}
//	
//	// if type is INTEGER, use these instead of setMinDate/setMaxDate
//	// internally, the int value is used as year
//	public void setMinIndex(int miniIndex) {
//		minIndex = miniIndex;
//	}
//	
//	public void setMaxIndex(int maxiIndex) {
//		maxIndex = maxiIndex;
//	}
//	
//	public boolean accepts(IFeature feature) {
//		
//		Value val = feature.getAttribute(filterFieldIndex);
//		if (val instanceof NullValue)
//			return false;
//		
//		if (!(val instanceof NumericValue))
//			return false;
//		
//		int intVal = ((NumericValue)val).intValue();
//	
//		if (intVal < minIndex)
//		    return false;
//		
//		if (intVal > maxIndex)
//		    return false;
//			
//		return true;
//	}
//
//	public String getClassName() {
//		return this.getClass().getName();
//	}
//
//	public XMLEntity getXMLEntity() {
//		XMLEntity xml = new XMLEntity();
//
//		xml.putProperty("fieldIndex", filterFieldIndex);
//		xml.putProperty("minIndex", minIndex);
//		xml.putProperty("maxIndex", maxIndex);
//
//		return xml;
//	}
//
//	public void setXMLEntity(XMLEntity xml) {
//
//		if (xml.contains("fieldIndex"))
//			filterFieldIndex = xml.getIntProperty("fieldIndex");
//		
//		if (xml.contains("minIndex"))
//			minIndex = xml.getIntProperty("minIndex");
//
//		if (xml.contains("maxIndex"))
//			maxIndex = xml.getIntProperty("maxIndex");
//	}
//
//	public boolean compareTo(ICustomVectorialFilter filter) {
//		IndexFilter filterAux = (IndexFilter) filter;
//
//		return ((this.minIndex == filterAux.minIndex) && (this.maxIndex == filterAux.maxIndex));
//	}

}