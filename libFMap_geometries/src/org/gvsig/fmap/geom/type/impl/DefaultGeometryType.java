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
* 2009 {Iver T.I.}   {Task}
*/
 
package org.gvsig.fmap.geom.type.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.operation.GeometryOperation;
import org.gvsig.fmap.geom.type.GeometryType;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class DefaultGeometryType implements GeometryType {
	/** 
	 * The geometry identifier
	 */
	private int id;
	
	/** 
	 * Geometry type name
	 */
	private String name;
		
	/** Class that implements this class type */
	private Class geometryClass;
	
	/**
	 * The type of the geometry. The type is an abstract representation
	 * of the object (Point, Curve...) but it is not a concrete 
	 * representation (Point2D, Point3D...). To do that comparation
	 * the id field is used.
	 */
	private int type;
	
	/**
	 * The subtype of the geometry. The subtype represents a set of 
	 * geometries with a dimensional relationship (2D, 3D, 2DM...)
	 */
	private int subType;
	
	
		
	/** Registered operations for a concrete geometry type */
	private List geometryOperations = new ArrayList();
	
	/**
	 * This constructor is used by the {@link GeometryManager} when it
	 * register a new GeometryType. It has not be used from other
	 * parts.
	 * @param geomClass
	 * Geometry class (e.g: Point2D.class)
	 * @param name
	 * Symbolic Geometry name that is used to persist the geometry type. In some
	 * cases, it is better to use this name because the id can change for different
	 * application executions.  	 		
	 * @param id
	 * Geometry id	
	 * @param typeName
	 * The geometry type name
	 * @param type
	 * The geometry abstract type			
	 */
	public DefaultGeometryType(Class geomClass, String name, int id, int type, int subType) {
		this.geometryClass = geomClass;
		if (name == null) {
			this.name = geomClass.getName();
		} else {
			this.name = name;
		}
		this.id = id;		
		this.type = type;	
		this.subType = subType;
	}
	
	/**
	 * This method creates a {@link Geometry} with the type specified 
	 * by this GeometryType. The geometry has to have a constructor
	 * without arguments.
	 * 
	 * @return A new geometry
	 * @throws CreateGeometryException 
	 */
	public Geometry create() throws CreateGeometryException{
		try {
			Class[] parameterTypes = {GeometryType.class};
			Object[] parameters = {this};
			return (Geometry)geometryClass.getConstructor(parameterTypes).newInstance(parameters);
		} catch (Exception e) {
			throw new CreateGeometryException(type, subType, e);
		} 
	}
	
	/**
	 * Guardamos una referencia a una instancia de la operación en el índice que se pasa como parámetro.
	 * Si el índice ya está ocupado se sobrescribe.
	 * 
	 * @param index
	 * @param geomOp
	 */
	public void setGeometryOperation(int index, GeometryOperation geomOp) {
		
		while (index > geometryOperations.size()) {
			geometryOperations.add(null);
		}
		
		if (index == geometryOperations.size()) {
			geometryOperations.add(geomOp);
		} else {				
			geometryOperations.set(index, geomOp);
		}
	}
	
	public GeometryOperation getGeometryOperation(int index) {
		return (GeometryOperation) geometryOperations.get(index);
	}
	
	public Class getGeometryClass() {
		return geometryClass;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer("[")
		.append(geometryClass.getName())
		.append(",[")
		.append(geometryOperations.toString())
		.append("]");
		
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.type.GeometryType#getId()
	 */
	public int getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.type.GeometryType#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.type.GeometryType#getType()
	 */
	public int getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.type.GeometryType#getSubType()
	 */
	public int getSubType() {
		return subType;
	}
}

