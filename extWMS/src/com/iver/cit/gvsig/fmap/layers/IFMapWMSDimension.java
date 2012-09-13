/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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
* $Id: IFMapWMSDimension.java 4222 2006-02-28 15:26:59Z jaume $
* $Log$
* Revision 1.4  2006-02-28 15:25:14  jaume
* *** empty log message ***
*
* Revision 1.2.2.5  2006/02/23 10:36:30  jaume
* *** empty log message ***
*
* Revision 1.2.2.4  2006/02/10 13:22:35  jaume
* now analyzes dimensions on demand
*
* Revision 1.2.2.3  2006/01/31 16:25:24  jaume
* correcciones de bugs
*
* Revision 1.3  2006/01/26 16:07:14  jaume
* *** empty log message ***
*
* Revision 1.2.2.1  2006/01/26 12:59:32  jaume
* 0.5
*
* Revision 1.2  2006/01/24 14:36:33  jaume
* This is the new version
*
* Revision 1.1.2.8  2006/01/23 12:54:45  jaume
* *** empty log message ***
*
* Revision 1.1.2.7  2006/01/20 15:22:46  jaume
* *** empty log message ***
*
* Revision 1.1.2.6  2006/01/19 16:09:30  jaume
* *** empty log message ***
*
* Revision 1.1.2.5  2006/01/10 11:33:31  jaume
* Time dimension working against Jet Propulsion Laboratory's WMS server
*
* Revision 1.1.2.4  2006/01/05 23:15:53  jaume
* *** empty log message ***
*
* Revision 1.1.2.3  2006/01/04 18:09:02  jaume
* Time dimension
*
* Revision 1.1.2.2  2006/01/04 16:49:44  jaume
* Time dimensios
*
* Revision 1.1.2.1  2006/01/03 18:08:40  jaume
* *** empty log message ***
*
*
*/
/**
 * 
 */
package com.iver.cit.gvsig.fmap.layers;

/**
 * 
 * @author jaume
 *
 */
public interface IFMapWMSDimension {
	public static int SINGLE_VALUE = 0;
	public static int MULTIPLE_VALUE = 1;
	public static int INTERVAL = 2;
    /**
     * Return the dimension's name. This value is the value that will be used in
     * a GetMap request.
     * 
     * @return String containing the name of this dimension.
     */
    public String getName();
    /**
     * Return the unit used by this dimension.
     * @return
     */
    public String getUnit();
    /**
     * Returns the unit symbol (i.e. 'm', 's', or 'l' for meters, seconds, or liters respectively) 
     * @return
     */
    public String getUnitSymbol();
    
    
    /**
     * This method returns the <b>lowest</b> value of this dimension if this dimension is
     * specified as an interval or as a set of values, or the value specified if it
     * was a single value. 
     * @return String containing the coded value.
     */
    public String getLowLimit();

    /**
     * This method returns the <b>highest</b> value of this dimension if this dimension is
     * specified as an interval or as a set of values, or the value specified if it
     * was a single value. 
     * @return String containing the coded value.
     */
    public String getHighLimit();
    
    /**
     * This method returns the resolution supported by this dimension. This
     * means the step lenght between two consecutive points along the
     * dimension's axis. 
     * @return String containing the coded value, or null if no value for resolution.
     * @deprecated
     */
    public String getResolution();
    
    /**
     * Checks if the value represented as string is a valid value by checking
     * if the dimensions supports it. It should be true if one of the following is
     * true:
     * <p>
     * <ol> 
     * <li>
     *  The dimension <b>supports nearest values</b> and <b>the value is greather
     *  or  equal than the low limit</b> and <b>less or equal than the high limit</b>.  
     *  </li>
     *  <li>
     *  The value matches in one of the points defined by the low and high limits, and
     *  the resolution value.
     *  </li>
     * </ol>
     * </p>
     * @param value
     * @return
     */
    public boolean isValidValue(String value);
    
    /**
     * Return the value of the String passed in the dimension's unit-natural type.
     * @param value
     * @return
     */
    public Object valueOf(String value) throws IllegalArgumentException;
    
    /**
     * Returns the value that would be at the position passed as argument.
     * @param pos
     * @return
     * @throws ArrayIndexOutOfBoundsException
     */
    public String valueAt(int pos) throws ArrayIndexOutOfBoundsException;
    
    /**
     * The amount of positions that this dimension contains. 
     * @return -1 if the dimension is not recognized, the amount otherwise
     */
    public int valueCount();
    
    /**
     * Returns the expression describing this WMS Dimension
     */
    public String getExpression();
    
    /**
     * Sets the expression describing this WMS Dimension
     * @throws IllegalArgumentException
     */
    public void setExpression(String expr);
    
    /**
	 * Returns the type of the dimension expression.<br>
	 * Possible values are:
	 * <ol>
	 * 	<li>
	 * 		<b>IFMapWMSDimension.SINGLE_VALUE</b>
	 * 		<b>IFMapWMSDimension.MULTIPLE_VALUE</b>
	 * 		<b>IFMapWMSDimension.INTERVAL</b>
	 * 	</li>
	 * </ol>
	 * @return int
	 */
    public int getType();
    
    /**
     * Analyzes and establishes the starting values for this dimension. No operation of this
     * dimension can be called before the dimension has been compiled.
     * 
     * @throws IllegalArgumentException
     */
    public void compile() throws IllegalArgumentException;
}
