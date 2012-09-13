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
/*
 * $Id: WCSLayer.java 4573 2006-03-24 07:56:07Z jaume $ 
 * $Log$
 * Revision 1.5  2006-03-24 07:55:53  jaume
 * *** empty log message ***
 *
 * Revision 1.4  2006/03/14 18:17:59  jaume
 * *** empty log message ***
 *
 * Revision 1.3  2006/03/14 17:36:44  jaume
 * *** empty log message ***
 *
 * Revision 1.2  2006/03/14 10:52:21  jaume
 * *** empty log message ***
 *
 * Revision 1.1  2006/03/10 10:25:03  jaume
 * *** empty log message ***
 * 
 */
package com.iver.cit.gvsig.fmap.layers;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Hashtable;

public class WCSLayer {
	
	private String name;
	private ArrayList srs;
	private String title;
	private String nativeSRS;
	private ArrayList formats;
	private Point2D maxRes;
	private ArrayList timePositions;
	private String description;
	private ArrayList interpolationMethods;
	private ArrayList pList;
	private Hashtable extents;

	public void setName(String name) {
		this.name = name;
	}

	public void addAllSrs(ArrayList srs) {
		if (this.srs == null)
			this.srs = new ArrayList();
		this.srs.addAll(srs);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setNativeSRS(String nativeSRS) {
		this.nativeSRS = nativeSRS;
	}

	public String getName() {
		return name;
	}

	public void setFormats(ArrayList formats) {
		this.formats = formats;
	}

	public ArrayList getFormats() {
		return formats;
	}

	public ArrayList getSRSs() {
		if (!srs.contains(nativeSRS)) {
			ArrayList l = new ArrayList(srs);
			l.add(nativeSRS);
			return l;
		}
		return srs;
	}

	public String getTitle() {
		return title;
	}

	public Rectangle2D getExtent(String srs) {
		if ( extents != null ) {
			return (Rectangle2D) extents.get(srs);
		}
		return null;
	}
	
	public void addExtent(String srs, Rectangle2D extent) {
		if ( extents == null ) extents = new Hashtable();
		extents.put(srs, extent);
	}

	public Point2D getMaxRes() {
		return maxRes;
	}
	

	public void setMaxRes(Point2D maxRes) {
		this.maxRes = maxRes;
	}
	
	public String toString(){
    	String str;
    	if (getName()==null)
    		str = getTitle();
    	else
    		str = "["+getName()+"] "+getTitle();
        return str;
    }

	public void setTimePositions(ArrayList timePositions) {
		this.timePositions = timePositions;
	}
    
	public ArrayList getTimePositions() {
		return this.timePositions;
	}

	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String descr) {
		this.description = descr;
	}

	public String getLonLatEnvelope() {
		return "yet unimplemented";
	}

	public void setInterpolationMethods(ArrayList interpolationMethods) {
		this.interpolationMethods = interpolationMethods;
	}
	
	public ArrayList getInterpolationMethods() {
		return interpolationMethods;
	}

	public void addParameter(FMapWCSParameter p) {
		if (pList == null) pList = new ArrayList();
		pList.add(p);
	}

	public ArrayList getParameterList() {
		return pList;
	}
}
