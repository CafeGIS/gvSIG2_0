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

package org.gvsig.remoteClient.wfs.filters;

import java.awt.geom.PathIterator;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Surface;
import org.gvsig.remoteClient.wfs.WFSGeometryOperation;
import org.gvsig.tools.evaluator.AbstractEvaluator;
import org.gvsig.tools.evaluator.EvaluatorData;
import org.gvsig.tools.evaluator.EvaluatorException;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class FEIntersectsQuery {
	private Geometry geometry = null;
	private String geometryName = null;
	private String srsName = null;

	/**
	 * @param geometry
	 * @param geometryName
	 * @param srsName
	 */
	public FEIntersectsQuery(Geometry geometry, String geometryName,
			String srsName) {
		super();
		this.geometry = geometry;
		this.geometryName = geometryName;
		this.srsName = srsName;		
	}
	
	public FEIntersectsQuery(WFSGeometryOperation geometryOperation) {
		this(geometryOperation.getArea(),
				geometryOperation.getAttributeName(),
				geometryOperation.getSrs());
	}
	
	public String getFilterEncoding(){
		StringBuffer request = new StringBuffer();
		if (geometry.getType() == TYPES.SURFACE){
			Surface surface = (Surface)geometry;
			request.append("<ogc:Intersects>");
			request.append("<ogc:PropertyName>" + geometryName + "</ogc:PropertyName>");
			request.append("<gml:MultiSurface srsName=\"" + srsName + "\">");
			request.append("<gml:surfaceMember>");
			request.append("<gml:Polygon>");
			request.append("<gml:exterior>");
			request.append("<gml:LinearRing>");
			request.append("<gml:coordinates cs=\",\" decimal=\".\" ts=\" \">");
			GeneralPathX generalPath = surface.getGeneralPath();
			PathIterator it = generalPath.getPathIterator(null);
			int type;
			double[] coordinates = new double[6];
			while (!it.isDone()){
				type = it.currentSegment(coordinates);
				switch (type) {
					case PathIterator.SEG_MOVETO:
					case PathIterator.SEG_LINETO:
						request.append(coordinates[0]);
						request.append(",");
						request.append(coordinates[1]);	
						request.append(" ");
						break;
				}				
				it.next();
			}			
			request.append("</gml:coordinates>");
			request.append("</gml:LinearRing>");
			request.append("</gml:exterior>");
			request.append("</gml:Polygon>");
			request.append("</gml:surfaceMember>");
			request.append("</gml:MultiSurface>");
			request.append("</ogc:Intersects>");
		}
		return request.toString();
	}


}

