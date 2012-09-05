package org.gvsig.gpe.gml.parser.profiles;

import org.gvsig.gpe.gml.parser.sfp0.coordinates.PosListTypeIterator;
import org.gvsig.gpe.gml.parser.sfp0.coordinates.PosTypeIterator;
import org.gvsig.gpe.gml.parser.sfp0.geometries.CurvePropertyTypeBinding;
import org.gvsig.gpe.gml.parser.sfp0.geometries.CurveTypeBinding;
import org.gvsig.gpe.gml.parser.sfp0.geometries.EnvelopeTypeBinding;
import org.gvsig.gpe.gml.parser.sfp0.geometries.ExteriorTypeBinding;
import org.gvsig.gpe.gml.parser.sfp0.geometries.InteriorTypeBinding;
import org.gvsig.gpe.gml.parser.sfp0.geometries.LinestringSegmentTypeBinding;
import org.gvsig.gpe.gml.parser.sfp0.geometries.LowerCornerTypeBinding;
import org.gvsig.gpe.gml.parser.sfp0.geometries.MultiCurveTypeBinding;
import org.gvsig.gpe.gml.parser.sfp0.geometries.SegmentsTypeBinding;
import org.gvsig.gpe.gml.parser.sfp0.geometries.UpperCornerTypeBinding;

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
 * $Id$
 * $Log$
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class DefaultBindingProfile implements IBindingProfile {

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getPosTypeBinding()
	 */
	public PosTypeIterator getPosTypeBinding() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getEnvelopeTypeBinding()
	 */
	public EnvelopeTypeBinding getEnvelopeTypeBinding() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getLowerCornerTypeBinding()
	 */
	public LowerCornerTypeBinding getLowerCornerTypeBinding() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getLowerCornerTypeBinding()
	 */
	public UpperCornerTypeBinding getUpperCornerTypeBinding() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getCurvePropertyTypeBinding()
	 */
	public CurvePropertyTypeBinding getCurvePropertyTypeBinding() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getPosListTypeBinding()
	 */
	public PosListTypeIterator getPosListTypeBinding() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getCurveTypeBinding()
	 */
	public CurveTypeBinding getCurveTypeBinding() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getSegmentsTypeBinding()
	 */
	public SegmentsTypeBinding getSegmentsTypeBinding() {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getLinestringSegmentTypeBinding()
	 */
	public LinestringSegmentTypeBinding getLinestringSegmentTypeBinding() {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getExteriorTypeBinding()
	 */
	public ExteriorTypeBinding getExteriorTypeBinding() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.gml.profiles.IProfile#getInteriorTypeBinding()
	 */
	public InteriorTypeBinding getInteriorTypeBinding() {
		return null;
	}	
	
	/* (non-Javadoc)
	 * @see org.gvsig.gpe.gml.parser.profiles.IBindingProfile#getMultiCurveTypeBinding()
	 */
	public MultiCurveTypeBinding getMultiCurveTypeBinding() {
		return null;
	}
}
