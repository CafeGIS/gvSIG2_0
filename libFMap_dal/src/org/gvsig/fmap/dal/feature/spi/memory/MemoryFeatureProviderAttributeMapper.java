/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
* MA  02110-1301, USA.
*
*/

/*
* AUTHORS (In addition to CIT):
* 2009 IVER T.I   {{Task}}
*/

package org.gvsig.fmap.dal.feature.spi.memory;

import java.util.Iterator;

import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.spi.DefaultFeatureProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Envelope;

/**
 * Envuelve un FeatureProvider en memoria para que muestre un FeautureType
 * distinto del orginal sin modificarlo
 * 
 * 
 * Wrap a FeatureProvider stored in memory to display a FeautureType other than
 * the original one without change it
 * 
 * @author jmvivo
 *
 */
public class MemoryFeatureProviderAttributeMapper implements FeatureProvider {
	private DefaultFeatureProvider original;
	private FeatureType myFeatureType;

	public MemoryFeatureProviderAttributeMapper(DefaultFeatureProvider original,
			FeatureType featureType) {
		this.original = original;
		this.myFeatureType = featureType;

	}

	public Object get(int i) {
		return original.get(myFeatureType.getAttributeDescriptor(i).getName());
	}

	public Object get(String name) {
		return original.get(name);
	}

	public FeatureProvider getCopy() {
		DefaultFeatureProvider data = new DefaultFeatureProvider(myFeatureType);
		data.setOID(this.original.getOID());
		Iterator iter = myFeatureType.iterator();
		FeatureAttributeDescriptor attr;
		while (iter.hasNext()) {
			attr = (FeatureAttributeDescriptor) iter.next();
			data.set(attr.getIndex(), this.original.get(attr.getName()));
		}
		data.setNew(this.original.isNew());
		return data;
	}

	public Envelope getDefaultEnvelope() {
		return original.getDefaultEnvelope();
	}

	public Geometry getDefaultGeometry() {
		return original.getDefaultGeometry();
	}

	public Object getOID() {
		return original.getOID();
	}

	public FeatureType getType() {
		return myFeatureType;
	}

	public boolean isNew() {
		return original.isNew();
	}

	public boolean isNull(int i) {
		return original.isNull(myFeatureType.getAttributeDescriptor(i)
				.getName());
	}

	public boolean isNull(String name) {
		return original.isNull(name);
	}

	public void set(int i, Object value) {
		original.set(myFeatureType.getAttributeDescriptor(i).getName(),
				value);


	}

	public void set(String name, Object value) {
		original.set(name, value);
	}

	public void setDefaultEnvelope(Envelope extent) {
		original.setDefaultEnvelope(extent);
	}

	public void setDefaultGeometry(Geometry geom) {
		original.setDefaultGeometry(geom);

	}

	public void setNew(boolean isNew) {
		original.setNew(isNew);
	}

	public void setOID(Object oid) {
		original.setOID(oid);
	}

}
