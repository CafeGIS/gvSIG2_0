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

/* CVS MESSAGES:
*
* $Id: DefaultDimension.java 18675 2008-02-11 10:16:01Z jdominguez $
* $Log$
* Revision 1.5  2006-02-28 15:25:14  jaume
* *** empty log message ***
*
* Revision 1.3.2.4  2006/02/10 13:22:35  jaume
* now analyzes dimensions on demand
*
* Revision 1.3.2.3  2006/01/31 16:25:24  jaume
* correcciones de bugs
*
* Revision 1.4  2006/01/26 16:07:14  jaume
* *** empty log message ***
*
* Revision 1.3.2.1  2006/01/26 12:59:32  jaume
* 0.5
*
* Revision 1.3  2006/01/25 09:08:53  jaume
* test save and reload project
*
* Revision 1.2  2006/01/24 14:36:33  jaume
* This is the new version
*
* Revision 1.1.2.1  2006/01/20 15:22:46  jaume
* *** empty log message ***
*
*
*/
package com.iver.cit.gvsig.fmap.layers;
/**
 * This class instances a regular WMS dimension. It handles single, multiple and
 * interval values and uses them as they were a point, a list or a regularly
 * split line, respectivelly.<br>
 * <p>
 * As far as it implements IFMapWMSDimension it uses the same interface and
 * documentation.
 * </p>
 * @author jaume dominguez faus (jaume.dominguez@iver.es)
 *
 */
public class DefaultDimension implements IFMapWMSDimension {
	static private final String digit = "[0-9]";
    static private final String nonZeroDigit = "[1-9]";
    static private final String letter = "[_$%a-zA-Z]";
    static private final String word = letter+"("+letter+"|"+digit+")+"; // TODO Should be * instead of + ??
    static private final String floatingPointNumber = "("+digit+"+(\\."+digit+"+)?)";
    static private final String integerNumber = nonZeroDigit+digit+"+";
    static private final String dimensionItem = "("+floatingPointNumber+"|"+word+")";
    /**
     * regular expression for matching dimensions.
     */
    static private final String regexpDefaultDimensionExpression =
    	"("+floatingPointNumber+"/"+floatingPointNumber+"/"+floatingPointNumber+"|"+
    	    dimensionItem+"(,"+dimensionItem+")*)";
    
	private String name;
    private String unit;
    private String unitSymbol;
    private String expression;
    private String period;
    private Object minValue;
    private Object maxValue;
	private int type;
	private boolean compiled = false;
    /**
     * Creates a new instance of DefaultDimension.
     * @param _name
     * @param _units
     * @param _unitSymbol
     * @param _dimensionExpression
     */
    public DefaultDimension(String _name, String _units, String _unitSymbol, String _dimensionExpression) {
    	this.name = _name;
    	this.unit = _units;
    	this.unitSymbol = _unitSymbol;
    	setExpression(_dimensionExpression);
    }
    
    /*
     *  (non-Javadoc)
     * @see com.iver.cit.gvsig.fmap.layers.IFMapWMSDimension#getName()
     */
    public String getName() {
		return name;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.IFMapWMSDimension#getUnit()
	 */
	public String getUnit() {
		return unit;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.IFMapWMSDimension#getUnitSymbol()
	 */
	public String getUnitSymbol() {
		return unitSymbol;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.IFMapWMSDimension#getLowLimit()
	 */
	public String getLowLimit() {
		return (String) minValue;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.IFMapWMSDimension#getHighLimit()
	 */
	public String getHighLimit() {
		return (String) maxValue;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.IFMapWMSDimension#getResolution()
	 */
	public String getResolution() {
		if (type == INTERVAL) {
    		String[] s = expression.split("/");
    		return (s.length == 1) ? s[3] : null;
    	} else return null;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.IFMapWMSDimension#isValidValue(java.lang.String)
	 */
	public boolean isValidValue(String value) {
		return value.matches(word) || value.matches(floatingPointNumber);
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.IFMapWMSDimension#valueOf(java.lang.String)
	 */
	public Object valueOf(String value) throws IllegalArgumentException {
		if (compiled) {
			if (value.matches(word)) {
				return (String) value;
			} else if (value.matches(integerNumber)){
				return new Integer(value);
			}
			else if (value.matches(floatingPointNumber)) {
				return new Float(value);
			}
		}
		return null;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.IFMapWMSDimension#valueAt(int)
	 */
	public String valueAt(int pos) throws ArrayIndexOutOfBoundsException {
		if (compiled) {
			if (pos<0 || pos>valueCount())
				throw new ArrayIndexOutOfBoundsException(pos+"(must be >= 0 and <="+valueCount()+")");
			
			if (type == SINGLE_VALUE)
				return expression;
			
			if (type == MULTIPLE_VALUE)
				return expression.split(",")[pos];
			
			if (type == INTERVAL) {
				double minPos = Double.parseDouble((String) minValue);
				double maxPos = Double.parseDouble((String) maxValue);
				double step = Double.parseDouble(period);
				double newPos = minPos + (step*pos);
				if (newPos < minPos)
					return minPos+"";
				
				if (newPos > maxPos)
					return maxPos+"";
				return newPos+"";
			}
		}
        return null;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.IFMapWMSDimension#valueCount()
	 */
	public int valueCount() {
		if (compiled) {
			if (type == MULTIPLE_VALUE) {
				return expression.split(",").length;
			} else if (type == INTERVAL) {
				int count;
				double min = Double.parseDouble((String) minValue);
				double max = Double.parseDouble((String) maxValue);
				double step = Double.parseDouble(period);
				double distance = max - min;
				count = (int) (distance/step);
				return count;
			} else {
				return 1;
			}
		}
		return -1;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.IFMapWMSDimension#getExpression()
	 */
	public String getExpression() {
		return expression;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.IFMapWMSDimension#setExpression(java.lang.String)
	 */
	public void setExpression(String expr) {
		expression = expr.toUpperCase();
        
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.IFMapWMSDimension#getType()
	 */
	public int getType() {
		return type;
	}

	public void compile() throws IllegalArgumentException {
		if (expression.matches(regexpDefaultDimensionExpression)){

        } else {
            //System.err.println("Invalid dimension expression: "+expr+" (for "+this.getName()+")");
            throw new IllegalArgumentException();
        }
		

        String separator;
        if (expression.indexOf("/")!=-1) {
        	separator = "/";
        	type = INTERVAL;
        } else if (expression.indexOf(",")!=-1) {
        	separator = ",";
        	type = MULTIPLE_VALUE;
        } else {
        	separator = ",";
        	type = SINGLE_VALUE;
        }
        compiled = true;
        String[] s = expression.split(separator);
        minValue = valueOf(s[0]);
        if (type == INTERVAL) {
        	maxValue = (s.length>1) ? valueOf(s[1]) : valueOf(s[0]);
        	period = (s.length>2) ? s[2] : null;
        } else if (type == MULTIPLE_VALUE) {
        	maxValue = valueOf(s[s.length-1]);
        } else {
        	maxValue = valueOf(s[0]);
        }
        
	}

}
