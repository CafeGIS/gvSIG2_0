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
* $Id: TimeDimension.java 11218 2007-04-17 07:02:54Z jaume $
* $Log$
* Revision 1.9  2007-04-17 07:02:54  jaume
* *** empty log message ***
*
* Revision 1.8  2007/03/08 12:04:32  jaume
* fixed error parsing YYYY date formats
*
* Revision 1.7  2006/05/12 07:47:39  jaume
* removed unnecessary imports
*
* Revision 1.6  2006/04/21 11:53:04  jaume
* *** empty log message ***
*
* Revision 1.5  2006/02/28 15:25:14  jaume
* *** empty log message ***
*
* Revision 1.3.2.6  2006/02/28 12:54:12  jaume
* *** empty log message ***
*
* Revision 1.3.2.5  2006/02/23 10:36:30  jaume
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
* Revision 1.3  2006/01/24 18:01:17  jaume
* *** empty log message ***
*
* Revision 1.2  2006/01/24 14:36:33  jaume
* This is the new version
*
* Revision 1.1.2.11  2006/01/20 15:59:13  jaume
* *** empty log message ***
*
* Revision 1.1.2.10  2006/01/20 15:22:46  jaume
* *** empty log message ***
*
* Revision 1.1.2.9  2006/01/20 08:50:52  jaume
* handles time dimension for the NASA Jet Propulsion Laboratory WMS
*
* Revision 1.1.2.8  2006/01/19 16:09:30  jaume
* *** empty log message ***
*
* Revision 1.1.2.7  2006/01/11 12:20:30  jaume
* *** empty log message ***
*
* Revision 1.1.2.6  2006/01/10 11:33:31  jaume
* Time dimension working against Jet Propulsion Laboratory's WMS server
*
* Revision 1.1.2.5  2006/01/09 18:10:38  jaume
* casi con el time dimension
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Class for WMS TIME dimension from a WMS. It allows you to handle the correct
 * values for this kind of dimension.
 * <br>
 * <p>
 * At the moment this class was written the WMS TIME dimension is defined as the
 * ISO8601 standard for expressing times.
 * </p>
 * <br>
 * <p>
 * As far as this class implements IFMapWMSDimension it uses the same interface
 * and documentation.
 * </p>
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class TimeDimension implements IFMapWMSDimension {
	static private final byte YEAR_FORMAT          = 1;
	static private final byte YEAR_TO_MONTH_FORMAT = 2;
	static private final byte YEAR_TO_DAY_FORMAT   = 3;
	static private final byte FULL_FORMAT          = 4;

    static private final long millisXsec    = 1000;
    static private final long millisXminute = 60 * millisXsec;
    static private final long millisXhour   = 60 * millisXminute;
    static private final long millisXday    = 24 * millisXhour;
    static private final long millisXmonth  = 30 * millisXday;
    // according on the Wikipedia (1 year = 365 days 6 hours 9 minutes 9,7 seconds)
    static private final long millisXyear   = (365*millisXday) + (6*millisXhour) + (9*millisXminute) + 9700;

    static private final String digit = "[0-9]";
    static private final String nonZeroDigit = "[1-9]";
    static private final String seconds = "([0-5]"+digit+"((\\.|,)"+digit+digit+")?)";
    static private final String minutes = "([0-5]"+digit+")";
    static private final String hours = "(0"+digit+"|1"+digit+"|2[0-3])";
    static private final String time = hours+":"+minutes+"(:"+seconds+")?";
    static private final String days = "(0?"+nonZeroDigit+"|1"+digit+"|2"+digit+"|30|31)";
    static private final String months = "(0?"+nonZeroDigit+"|10|11|12)";
    static private final String year = "("+digit+digit+")";
    static private final String century = "("+digit+digit+")";
    static private final String floatingPointNumber = "("+digit+"+(\\."+digit+"+)?)";

    private String unit;
	private String unitSymbol;
	private ArrayList valueList;
	private boolean compiled;
	private boolean isGeologic;
	private String expression;
	private int type;
	private byte format = 0;

    static private final String regexDateExtendedForBCE1 = "B?"+century+year;
    static private final String regexDateExtendedForBCE2 = "B?"+century+year+"-"+months;
    static private final String regexDateExtendedForBCE3 = "B?"+century+year+"-"+months+"-"+days;
    static private final String regexDateExtendedForBCE4 = "B?"+century+year+"-"+months+"-"+days+"(T| )"+time+"Z";
    // Note: in WMS 1.1.0 the suffix Z is optional

    static private final String regexDateExtendedForBCE =
        "(" +  regexDateExtendedForBCE1  + "|"
            +  regexDateExtendedForBCE2  + "|"
            +  regexDateExtendedForBCE3  + "|"
            +  regexDateExtendedForBCE4  +      ")";

    static private final String periodMagnitude = "(Y|M|D)";
    static private final String p1 = "(("+digit+")+"+periodMagnitude+")";

    static private final String timeMagnitude = "(H|M|S)";
    static private final String p2 = "("+floatingPointNumber+timeMagnitude+")";
    static private final String regexPeriod = "P(("+p1+"+"+"(T"+p2+")*)|("+p1+"*"+"(T"+p2+")+))";

    static private final String regexIntervalTimeDimension =
        "("+regexDateExtendedForBCE+")/("+regexDateExtendedForBCE+")/("+regexPeriod+")";

    static private final String geologicDatasets = "(K|M|G)";
    static private final String regexDateForGeologicDatasets = geologicDatasets+floatingPointNumber;

    /**
     * Creates a new instance of TimeDimension.
     * @param units
     * @param unitSymbol
     * @param expression
     */
    public TimeDimension(String _units, String _unitSymbol, String _dimensionExpression) {
        this.unit = _units;
        this.unitSymbol = _unitSymbol;
        setExpression(_dimensionExpression);
    }

	public String getName() {
        return "TIME";
    }

    public String getUnit() {
        return unit;
    }

    public String getUnitSymbol() {
        return unitSymbol;
    }

	public String getLowLimit() {
		return (String) valueList.get(0);
	}

	public String getHighLimit() {
		return (String) valueList.get(valueList.size()-1);
	}


	public String getResolution() {
		return null;
	}

	public boolean isValidValue(String value) {
		return (value.matches(regexDateForGeologicDatasets) || value.matches(regexDateExtendedForBCE));
	}

	public Object valueOf(String value) throws IllegalArgumentException {
		if (compiled) {
    		// TODO Missing geological dates
    		String myValue = value.toUpperCase();
    		if (isValidValue(myValue)) {
    			Object val = null;
    			if (!isGeologic){
    				// This is a normal date
    				int myYear;
    				int myMonth;
    				int myDay;
    				int myHour;
    				int myMinute;
    				float mySecond;
    				String[] s = myValue.split("-");
    				myYear = (s[0].charAt(0)=='B') ? -Integer.parseInt(s[0].substring(1, 5)) : Integer.parseInt(s[0].substring(0, 4));
    				myMonth = (s.length>1) ? Integer.parseInt(s[1])-1 : 0;
    				if (myValue.matches(regexDateExtendedForBCE4)){
    					if (s[2].endsWith("Z"))
    						s[2] = s[2].substring(0,s[2].length()-1);
    					s = (s[2].indexOf('T')!=-1) ? s[2].split("T") : s[2].split(" ");
    					myDay = Integer.parseInt(s[0]);

    					// Go with the time
    					s = s[1].split(":");
    					myHour = Integer.parseInt(s[0]);
    					myMinute = (s.length>1) ? Integer.parseInt(s[1]) : 0;
    					mySecond = (s.length>2) ? Float.parseFloat(s[2]) : 0;
    				} else {
    					myDay = (s.length>2) ? Integer.parseInt(s[2]) : 1;

    					myHour = 0;
    					myMinute = 0;
    					mySecond = 0;
    				}
    				GregorianCalendar cal = new GregorianCalendar(myYear, myMonth, myDay, myHour, myMinute, (int)mySecond);
    				val = cal;
    			} else{
    				// this is a geological date >:-(
    			}
    			return val;
    		} else throw new IllegalArgumentException(myValue);
    	}
    	return null;
	}

	public String valueAt(int pos) throws ArrayIndexOutOfBoundsException {
		return toString((GregorianCalendar) valueList.get(pos));
	}

	public int valueCount() {
		return valueList.size();
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expr) {
		this.expression = expr;
	}

	public int getType() {
		return type;
	}

	public void compile() throws IllegalArgumentException {
		compiled = true;
		valueList = new ArrayList();
		String[] items = expression.split(",");
		for (int i = 0; i < items.length; i++) {
			// Each iteration is a value of a comma-separated list
			// which can be a single date or even an expression defining
			// an interval.
			if (items[i].matches(regexDateExtendedForBCE1)) {
				upgradeFormat(YEAR_FORMAT);
				valueList.add(valueOf(items[i]));
			} else if (items[i].matches(regexDateExtendedForBCE2)) {
				upgradeFormat(YEAR_TO_MONTH_FORMAT);
				valueList.add(valueOf(items[i]));
			} else if (items[i].matches(regexDateExtendedForBCE3)) {
				upgradeFormat(YEAR_TO_DAY_FORMAT);
				valueList.add(valueOf(items[i]));
			} else if (items[i].matches(regexDateExtendedForBCE4)) {
				upgradeFormat(FULL_FORMAT);
				valueList.add(valueOf(items[i]));
			} else if (items[i].matches(regexIntervalTimeDimension)) {
				// Analyze and transform this interval expression to a list
				// of values.

				// min value
				String[] s = items[i].split("/");
				if (s[0].matches(regexDateExtendedForBCE1)) {
					upgradeFormat(YEAR_FORMAT);
				} else if (s[0].matches(regexDateExtendedForBCE2)) {
					upgradeFormat(YEAR_TO_MONTH_FORMAT);
				} else if (s[0].matches(regexDateExtendedForBCE3)) {
					upgradeFormat(YEAR_TO_DAY_FORMAT);
				} else if (s[0].matches(regexDateExtendedForBCE4)) {
					upgradeFormat(FULL_FORMAT);
				}
				Object minValue = valueOf(s[0]);

				// max value
				if (s[0].matches(regexDateExtendedForBCE1)) {
					upgradeFormat(YEAR_FORMAT);
				} else if (s[1].matches(regexDateExtendedForBCE2)) {
					upgradeFormat(YEAR_TO_MONTH_FORMAT);
				} else if (s[1].matches(regexDateExtendedForBCE3)) {
					upgradeFormat(YEAR_TO_DAY_FORMAT);
				} else if (s[1].matches(regexDateExtendedForBCE4)) {
					upgradeFormat(FULL_FORMAT);
				}
				Object maxValue = valueOf(s[1]);

				String period = s[2];

				if (period == null) {
					valueList.add(minValue);
					valueList.add(maxValue);
					continue;
				}

				long x1 = ((GregorianCalendar) maxValue).getTimeInMillis();
				long x0 = ((GregorianCalendar) minValue).getTimeInMillis();
				long distance = x1-x0;
				long step = 0;

				boolean isTimeField = false;
				String val = "";

				for (int j = 0; j < period.length(); j++) {
					if (period.charAt(j) == 'P')
						continue;
					if (period.charAt(j) == 'T') {
						isTimeField = true;
						continue;
					}
					switch (period.charAt(j)) {
					case 'Y':
						step += Integer.parseInt(val) * millisXyear;
						val = "";
						break;
					case 'M':
						if (isTimeField)
							step += Integer.parseInt(val) * millisXminute;
						else
							step += Integer.parseInt(val) * millisXmonth;
						val = "";
						break;
					case 'D':
						step += Integer.parseInt(val) * millisXday;
						val = "";
						break;
					case 'H':
						step += Integer.parseInt(val) * millisXhour;
						val = "";
						break;
					case 'S':
						step += Integer.parseInt(val) * 1000;
						val = "";
						break;
					default:
						val += period.charAt(j);
						break;
					}
				}
				// Now that we know the earliest and the latest date and the period
				// between two dates, I'm going to populate the list of values
				// considering these values.
				int valueCount = (int)(distance/step) + 1; // + 1 for the initial point
				valueList.add(minValue);
				for (int pos = 1; pos < valueCount; pos++) {
					long newTime = ((GregorianCalendar) minValue).getTimeInMillis();
	    			newTime += (step*pos);
	    			GregorianCalendar cal = new GregorianCalendar();
	    			cal.setTimeInMillis(newTime);
	    			if (cal.after(maxValue) || cal.before(minValue))
	    				continue;

	    			valueList.add(cal);
				}
				valueList.add(maxValue);
			} else {
				compiled = false;
				throw new IllegalArgumentException();
			}
		}
    }

	/**
	 * Prints a GregorianCalendar value in WMS1.1.1 format.
	 * @param cal
	 * @return
	 */
	private String toString(GregorianCalendar cal) {
		int iYear   = cal.get(Calendar.YEAR);
		int iMonth  = cal.get(Calendar.MONTH) + 1;
		int iDay    = cal.get(Calendar.DAY_OF_MONTH);
		int iHour   = cal.get(Calendar.HOUR_OF_DAY);
		int iMinute = cal.get(Calendar.MINUTE);
		int iSecond = cal.get(Calendar.SECOND);
		String myYear;
		if (iYear<10)
			myYear = "200"+iYear;
		else if (iYear<100)
			myYear = "20"+iYear;
		else if (iYear<1000)
			myYear = "2"+iYear;
		else
			myYear = ""+iYear;
		String myMonth       = (iMonth<10) ? "0"+iMonth  : ""+iMonth;
		String myDay         = (iDay<10)   ? "0"+iDay    : ""+iDay;
		String myHour        = (iHour<10)  ? "0"+iHour   : ""+iHour;
		String myMinute      = (iMinute<10)? "0"+iMinute : ""+iMinute;
		String mySecond      = (iSecond<10)? "0"+iSecond : ""+iSecond;
		int myMilliSecond = cal.get(Calendar.MILLISECOND);


		String s = null;
		if (format == YEAR_FORMAT)
			s = myYear+"";
		else if (format == YEAR_TO_MONTH_FORMAT)
			s = myYear+"-"+myMonth;
		else if (format == YEAR_TO_DAY_FORMAT)
			s = myYear+"-"+myMonth+"-"+myDay;
		else if (format == FULL_FORMAT)
			s = myYear+"-"+myMonth+"-"+myDay+"T"+myHour+":"+myMinute+":"+mySecond+"."+(myMilliSecond/10)+"Z";
		if (iYear<0)
			s = "B"+s;
		return s;
	}

	private void upgradeFormat(byte sFormat) {
		switch (sFormat) {
		case YEAR_FORMAT:
			if (format < YEAR_FORMAT)
				format = YEAR_FORMAT;
			break;
		case YEAR_TO_MONTH_FORMAT:
			if (format < YEAR_TO_MONTH_FORMAT)
				format = YEAR_TO_MONTH_FORMAT;
			break;
		case YEAR_TO_DAY_FORMAT:
			if (format < YEAR_TO_DAY_FORMAT)
				format = YEAR_TO_DAY_FORMAT;
			break;
		case FULL_FORMAT:
			if (format < FULL_FORMAT)
				format = FULL_FORMAT;
			break;
		}
	}
}


/*

 // an old (and good one) version. However, the new version is a bit better.

 public class TimeDimension implements IFMapWMSDimension {
    static private final long millisXsec    = 1000;
    static private final long millisXminute = 60 * millisXsec;
    static private final long millisXhour   = 60 * millisXminute;
    static private final long millisXday    = 24 * millisXhour;
    static private final long millisXmonth  = 30 * millisXday;
    // according on the Wikipedia (1 year = 365 days 6 hours 9 minutes 9,7 seconds)
    static private final long millisXyear   = (365*millisXday) + (6*millisXhour) + (9*millisXminute) + 9700;

    static private final String digit = "[0-9]";
    static private final String nonZeroDigit = "[1-9]";
    static private final String letter = "[a-zA-Z]";
    static private final String seconds = "([0-5]"+digit+"((\\.|,)"+digit+digit+")?)";
    static private final String minutes = "([0-5]"+digit+")";
    static private final String hours = "(0"+digit+"|1"+digit+"|2[0-3])";
    static private final String time = hours+":"+minutes+"(:"+seconds+")?";
    static private final String days = "(0?"+nonZeroDigit+"|1"+digit+"|2"+digit+"|30|31)";
    static private final String months = "(0?"+nonZeroDigit+"|10|11|12)";
    static private final String year = "("+digit+digit+")";
    static private final String century = "("+digit+digit+")";

    static private final String geologicDatasets = "(K|M|G)";
    static private final String floatingPointNumber = "("+digit+"+(\\."+digit+"+)?)";


    static private final String regexDateExtendedForBCE1 = "B?"+century+year+"-";
    static private final String regexDateExtendedForBCE2 = "B?"+century+year+"-"+months;
    static private final String regexDateExtendedForBCE3 = "B?"+century+year+"-"+months+"-"+days;
    static private final String regexDateExtendedForBCE4 = "B?"+century+year+"-"+months+"-"+days+"(T| )"+time+"Z";
    // Note: in WMS 1.1.0 the suffix Z is optional
    // TODO truncated values not yet allowed

    static private final String regexDateExtendedForBCE =
        "(" +  regexDateExtendedForBCE1  + "|"
            +  regexDateExtendedForBCE2  + "|"
            +  regexDateExtendedForBCE3  + "|"
            +  regexDateExtendedForBCE4  +      ")";


    static private final String regexDateForGeologicDatasets = geologicDatasets+floatingPointNumber;

    static private final String periodMagnitude = "(Y|M|D)";
    static private final String p1 = "(("+digit+")+"+periodMagnitude+")";

    static private final String timeMagnitude = "(H|M|S)";
    static private final String p2 = "("+floatingPointNumber+timeMagnitude+")";
    static private final String regexPeriod = "P(("+p1+"+"+"(T"+p2+")*)|("+p1+"*"+"(T"+p2+")+))";

    static private final String regexTimeDimension =
        "("+regexDateExtendedForBCE+"(,"+regexDateExtendedForBCE+")*|("+regexDateExtendedForBCE+")/("+regexDateExtendedForBCE+")/("+regexPeriod+"))";

    private String name = "TIME";
    private String unit;
    private String unitSymbol;
    private String expression;
    private Object minValue;
    private Object maxValue;
    private boolean isGeologic = false;
    private Integer valueCount;
    private String period;
    private long step; // Distance between two points in milliseconds.
    private int type;
    private boolean compiled = false;

    /**
     * Creates a new instance of TimeDimension.
     * @param units
     * @param unitSymbol
     * @param expression
     * /
    public TimeDimension(String _units, String _unitSymbol, String _dimensionExpression) {
        this.unit = _units;
        this.unitSymbol = _unitSymbol;
        setExpression(_dimensionExpression);
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public String getUnitSymbol() {
        return unitSymbol;
    }


    public String getLowLimit() {
    	String separator = (type == INTERVAL) ? "/" : ",";
        return expression.split(separator)[0];
    }

    public String getHighLimit() {
    	if (type == INTERVAL) {
    		String[] s = expression.split("/");
    		return (s.length > 1) ? s[1] : s[0];
    	} else if (type == MULTIPLE_VALUE) {
    		String[] s = expression.split(",");
    		return s[s.length-1];
    	} else {
    		return expression;
    	}

    }

    public String getResolution() {
    	if (type == INTERVAL) {
    		String[] s = expression.split("/");
    		return (s.length == 1) ? s[3] : null;
    	} else return null;
    }

    public boolean isValidValue(String value) {
        return (value.matches(regexDateForGeologicDatasets) || value.matches(regexDateExtendedForBCE));
    }

    public Object valueOf(String value) throws IllegalArgumentException {
    	if (compiled) {
    		// TODO Missing geological dates
    		String myValue = value.toUpperCase();
    		if (isValidValue(myValue)) {
    			Object val = null;
    			if (!isGeologic){
    				// This is a normal date
    				int myYear;
    				int myMonth;
    				int myDay;
    				int myHour;
    				int myMinute;
    				float mySecond;
    				String[] s = myValue.split("-");
    				myYear = (s[0].charAt(0)=='B')? -Integer.parseInt(s[0].substring(1, 5)) : Integer.parseInt(s[0].substring(0, 4));
    				myMonth = (s.length>1) ? Integer.parseInt(s[1])-1 : 0;
    				if (myValue.matches(regexDateExtendedForBCE4)){
    					if (s[2].endsWith("Z"))
    						s[2] = s[2].substring(0,s[2].length()-1);
    					s = (s[2].indexOf('T')!=-1) ? s[2].split("T") : s[2].split(" ");
    					myDay = Integer.parseInt(s[0]);

    					// Go with the time
    					s = s[1].split(":");
    					myHour = Integer.parseInt(s[0]);
    					myMinute = (s.length>1) ? Integer.parseInt(s[1]) : 0;
    					mySecond = (s.length>2) ? Float.parseFloat(s[2]) : 0;
    				} else {
    					myDay = (s.length>2) ? Integer.parseInt(s[2]) : 1;

    					myHour = 0;
    					myMinute = 0;
    					mySecond = 0;
    				}
    				GregorianCalendar cal = new GregorianCalendar(myYear, myMonth, myDay, myHour, myMinute, (int)mySecond);
    				val = cal;
    			} else{
    				// this is a geological date >:-(
    			}
    			return val;
    		} else throw new IllegalArgumentException(myValue);
    	}
    	return null;
    }

    public String valueAt(int pos) throws ArrayIndexOutOfBoundsException {
    	if (compiled) {
    		if (pos<0 || pos>valueCount())
    			throw new ArrayIndexOutOfBoundsException(pos+"(must be >= 0 and <="+valueCount()+")");

    		if (type == SINGLE_VALUE)
    			return expression;

    		if (type == MULTIPLE_VALUE)
    			return expression.split(",")[pos];

    		if (!isGeologic){
    			long newTime = ((GregorianCalendar) minValue).getTimeInMillis();
    			newTime += (step*pos);
    			GregorianCalendar cal = new GregorianCalendar();
    			cal.setTimeInMillis(newTime);
    			if (cal.after(maxValue))
    				return toString((GregorianCalendar) maxValue);
    			else if (cal.before(minValue))
    				return toString((GregorianCalendar) minValue);
    			else
    				return toString(cal);
    		}
    	}
        return null;
    }

    /**
     * Prints a GregorianCalendar value in WMS1.1.1 format.
     * @param cal
     * @return
     * /
    private String toString(GregorianCalendar cal) {
        int iYear   = cal.get(cal.YEAR);
        int iMonth  = cal.get(cal.MONTH) + 1;
        int iDay    = cal.get(cal.DAY_OF_MONTH);
        int iHour   = cal.get(cal.HOUR_OF_DAY);
        int iMinute = cal.get(cal.MINUTE);
        int iSecond = cal.get(cal.SECOND);
        String myYear;
        if (iYear<10)
            myYear = "200"+iYear;
        else if (iYear<100)
            myYear = "20"+iYear;
        else if (iYear<1000)
            myYear = "2"+iYear;
        else
            myYear = ""+iYear;
        String myMonth       = (iMonth<10) ? "0"+iMonth  : ""+iMonth;
        String myDay         = (iDay<10)   ? "0"+iDay    : ""+iDay;
        String myHour        = (iHour<10)  ? "0"+iHour   : ""+iHour;
        String myMinute      = (iMinute<10)? "0"+iMinute : ""+iMinute;
        String mySecond      = (iSecond<10)? "0"+iSecond : ""+iSecond;
        int myMilliSecond = cal.get(cal.MILLISECOND);


        String s = myYear+"-"+myMonth+"-"+myDay+"T"+myHour+":"+myMinute+":"+mySecond+"."+(myMilliSecond/10)+"Z";
        if (iYear<0)
            s = "B"+s;
        return s;
    }

    public int valueCount() {
    	if (compiled) {
    		if (valueCount==null){
    			if (type == MULTIPLE_VALUE) {
    				return expression.split(",").length;
    			} else if (type == INTERVAL) {
    				if (period == null) {
    					valueCount = new Integer(0);
    					return valueCount.intValue();
    				}

    				if (!isGeologic){

    					long x1 = ((GregorianCalendar) maxValue).getTimeInMillis();
    					long x0 = ((GregorianCalendar) minValue).getTimeInMillis();
    					long distance = x1-x0;
    					step = 0;

    					boolean isTimeField = false;
    					String val = "";

    					for (int i = 0; i < period.length(); i++) {
    						if (period.charAt(i) == 'P')
    							continue;
    						if (period.charAt(i) == 'T'){
    							isTimeField = true;
    							continue;
    						}
    						switch (period.charAt(i)){
    						case 'Y':
    							step += Integer.parseInt(val) * millisXyear;
    							val = "";
    							break;
    						case 'M':
    							if (isTimeField)
    								step += Integer.parseInt(val) * millisXminute;
    							else
    								step += Integer.parseInt(val) * millisXmonth;
    							val = "";
    							break;
    						case 'D':
    							step += Integer.parseInt(val) * millisXday;
    							val = "";
    							break;
    						case 'H':
    							step += Integer.parseInt(val) * millisXhour;
    							val = "";
    							break;
    						case 'S':
    							step += Integer.parseInt(val) * 1000;
    							val = "";
    							break;
    						default:
    							val += period.charAt(i);
    						break;
    						}
    					}
    					valueCount = new Integer((int)(distance/step) + 1); // + 1 for the initial point
    				}
    			} else {
    				// this is a single value expression
    				valueCount = new Integer(1);
    				return valueCount.intValue();
    			}
    		}

    		return valueCount.intValue();
    	}
    	return -1;
    }

    public void setExpression(String expr) {
        expression = expr.toUpperCase();
    }

	public String getExpression() {
		return expression;
	}

	public int getType() {
		return type;
	}

	public void compile() {
		if (expression.matches(regexTimeDimension)){
            isGeologic = false;
        } else if (expression.matches(regexDateExtendedForBCE)) {
            isGeologic = false;
        } else if (expression.matches(regexDateForGeologicDatasets)){
            isGeologic = true;
        } else  {
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
        	period = (s.length>2 && s[2].matches(regexPeriod)) ? s[2] : null;
        } else if (type == MULTIPLE_VALUE) {
        	maxValue = valueOf(s[s.length-1]);
        } else {
        	maxValue = valueOf(s[0]);
        }
	}
}*/


