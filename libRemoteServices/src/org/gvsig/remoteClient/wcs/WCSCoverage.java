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
/**
 *
 */
package org.gvsig.remoteClient.wcs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.gvsig.remoteClient.ILayer;
import org.gvsig.remoteClient.utils.BoundaryBox;
import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * Describes a coverage in a WCS server.
 *
 * @author jaume dominguez faus - jaume.domingue@iver.es
 *
 */
public abstract class WCSCoverage implements ILayer{
	public static final String SEPARATOR = " +"; // separator used to split list of values, usually is just a space
    private	String			name;
    private String			title;
    private String			_abstract;
    private Hashtable		bBoxes = new Hashtable();
	private BoundaryBox		lonLatBbox;
	public  RectifiedGrid	rg;
	private ArrayList 		timePositions = new ArrayList();
	public  String 			timePeriod;
	private String 			rangeSetName;
	private String 			rangeSetLabel;
	public  Hashtable 		axisPool = new Hashtable();
	private String 			nativeSRS;
	private ArrayList 		supportedSRSs = new ArrayList();
	private ArrayList		interpolationMethods;
	private String 			nativeFormat;
	private ArrayList 		formats = new ArrayList();

	public ArrayList getTimePositions() {
		return timePositions;
	}

	public void addTimePosition(String timeExpr) {
		if (timePositions == null)
			timePositions = new ArrayList();
		timePositions.add(timeExpr);
	}

	public ArrayList getFormats() {
		return formats;
	}

	public void addFormat(String format) {
		formats.add(format);
	}

    public void setNativeFormat(String nativeFormat) {
    	this.nativeFormat = nativeFormat;
    }

    public String getNativeFormat() {
    	return nativeFormat;
    }

	public String getNativeSRS() {
		return nativeSRS;
	}

	public void setNativeSRS(String nativeSRS) {
		this.nativeSRS = nativeSRS;
	}

	public ArrayList getSRSs() {
		return supportedSRSs;
	}

	public void addSRSs(String srs) {
		supportedSRSs.add(srs);
	}

	public String getRangeSetLabel() {
		return rangeSetLabel;
	}

	public void setRangeSetLabel(String rangeSetLabel) {
		this.rangeSetLabel = rangeSetLabel;
	}

	public String getRangeSetName() {
		return rangeSetName;
	}

	public void setRangeSetName(String rangeSetName) {
		this.rangeSetName = rangeSetName;
	}

	/* (non-Javadoc)
     * @see org.gvsig.remoteClient.ILayer#getName()
     */
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see org.gvsig.remoteClient.ILayer#setName(java.lang.String)
     */
    public void setName(String _name) {
        name = _name;
    }

    /* (non-Javadoc)
     * @see org.gvsig.remoteClient.ILayer#setTitle(java.lang.String)
     */
    public void setTitle(String _title) {
        title = _title;
    }

    /* (non-Javadoc)
     * @see org.gvsig.remoteClient.ILayer#getTitle()
     */
    public String getTitle() {
        return title;
    }

    /* (non-Javadoc)
     * @see org.gvsig.remoteClient.ILayer#getAbstract()
     */
    public String getAbstract() {
        return _abstract;
    }

    /* (non-Javadoc)
     * @see org.gvsig.remoteClient.ILayer#setAbstract(java.lang.String)
     */
    public void setAbstract(String _abstract) {
     this._abstract = _abstract;
    }

    public void addBBox(BoundaryBox bBox) {
    	bBoxes.put(bBox.getSrs(), bBox);
    }

    public ArrayList getAllSrs() {
    	ArrayList mySrs = new ArrayList();
    	mySrs.addAll(supportedSRSs);
    	if (!mySrs.contains(nativeSRS))
    		mySrs.add(nativeSRS);
    	return mySrs;
    }
    /**
     * <p>returns the bbox with that id in the Bboxes vector</p>
     * @param id
     */
    public BoundaryBox getBbox(String id) {
    	if ((id.compareToIgnoreCase( CapabilitiesTags.EPSG_4326 )==0)
    		||(id.compareToIgnoreCase( CapabilitiesTags.CRS_84)==0))
    	{
    		if (lonLatBbox != null)
    		return lonLatBbox;
    	}

        return (BoundaryBox)bBoxes.get(id);
    }

    public BoundaryBox getLatLonBox() {
        return lonLatBbox;
    }

    public void setLonLatBox(BoundaryBox box) {
        lonLatBbox = box;
    }

    public void addInterpolationMethod(String method) {
    	if (interpolationMethods==null) interpolationMethods = new ArrayList();
    	interpolationMethods.add(method);
    }

    public ArrayList getInterpolationMethods() {
    	return interpolationMethods;
    }
    /**
     * Parses the fragment of the XML document that describes this layer (or coverage)
     * @param parser
     * @throws IOException
     * @throws XmlPullParserException
     */
    public abstract void parse(KXmlParser parser) throws XmlPullParserException, IOException;

    /**
     * Inner class describing the Rectified Grid of this coverage.
     *
     * @author jaume dominguez faus - jaume.dominguez@iver.es
     */
    public class RectifiedGrid {
    	private int	       dimensions;
    	private String[]   axisNames;
    	private int[][]    lowGridEnvelopLimits;
    	private int[][]    highGridEnvelopLimits;
    	private double[]   origins;
    	private double[][] offsetVector;

		public RectifiedGrid(int dimensions) {
			this.dimensions = dimensions;
		}

		public void addAxisName(String axisName) {
			if (axisNames == null) {
				axisNames = new String[1];
				axisNames[0] = axisName;
			} else {
				String[] aux = axisNames;
				axisNames = new String[axisNames.length+1];
				for (int i = 0; i < aux.length; i++) {
					axisNames[i] = aux[i];
				}
				axisNames[axisNames.length-1] = axisName;
			}
		}

		public String[] getAxisNames() {
			return axisNames;
		}

		public void addLowGridEnvelopLimit(int[] lowLimit) {
			if (lowGridEnvelopLimits == null) {
				lowGridEnvelopLimits = new int[1][1];
				lowGridEnvelopLimits[0] = lowLimit;
			} else {
				int[][] aux = lowGridEnvelopLimits;
				lowGridEnvelopLimits = new int[lowGridEnvelopLimits.length+1][1];
				for (int i = 0; i < aux.length; i++) {
					lowGridEnvelopLimits[i] = aux[i];
				}
				lowGridEnvelopLimits[lowGridEnvelopLimits.length-1] = lowLimit;
			}
		}

		public int[][] getLowGridEnvelopLimits() {
			return lowGridEnvelopLimits;
		}

		public void addHighGridEnvelopLimit(int[] highLimit) {
			if (highGridEnvelopLimits == null) {
				highGridEnvelopLimits = new int[1][1];
				highGridEnvelopLimits[0] = highLimit;
			} else {
				int[][] aux = highGridEnvelopLimits;
				highGridEnvelopLimits = new int[highGridEnvelopLimits.length+1][1];
				for (int i = 0; i < aux.length; i++) {
					highGridEnvelopLimits[i] = aux[i];
				}
				highGridEnvelopLimits[highGridEnvelopLimits.length-1] = highLimit;
			}
		}

		public int[][] getHighGridEnvelopLimits() {
			return highGridEnvelopLimits;
		}

		public void setOrigin(double[] _origins) {
			origins = _origins;
		}

		public double[] getOrigins() {
			return origins;
		}

		public void addToOffsetVector(double[] _offsetVector) {
			if (offsetVector == null) {
				offsetVector = new double[1][1];
				offsetVector[0] = _offsetVector;
			} else {
				double[][] aux = offsetVector;
				offsetVector = new double[offsetVector.length+1][1];
				for (int i = 0; i < aux.length; i++) {
					offsetVector[i] = aux[i];
				}
				offsetVector[offsetVector.length-1] = _offsetVector;
			}
		}

		public double[][] getOffsetVector(){
			return offsetVector;
		}

		public int getDimensionCount() {
			return dimensions;
		}
    }

    /**
     * Inner class describing the Axis Description of this coverage.
     *
     * @author jaume dominguez faus - jaume.dominguez@iver.es
     */
    public class AxisDescription {
    	private String _name;
    	private String label;
    	private ArrayList singleValues = new ArrayList();
    	private String interval;
    	private String defaultValue;

		public String getDefaultValue() {
			return defaultValue;
		}

		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}

		public String getInterval() {
			return interval;
		}

		public void setInterval(String interval) {
			this.interval = interval;
		}

		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}

		public String getName() {
			return _name;
		}

		public void setName(String name) {
			this._name = name;
		}

		public ArrayList getSingleValues() {
			return singleValues;
		}

		public void addSingleValues(String singleValue) {
			this.singleValues.add(singleValue);
		}
    }

	public double getResX() {
		if (rg.offsetVector== null)
			return -1;
		return Math.abs(rg.offsetVector[0][0]);
	}

	public double getResY() {
		if (rg.offsetVector== null)
			return -1;

		return Math.abs(rg.offsetVector[1][1]);
	}

	public Hashtable getBBoxes() {
		if (bBoxes == null) return new Hashtable();
		else return bBoxes;
	}

}

