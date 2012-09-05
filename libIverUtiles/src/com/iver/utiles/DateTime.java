
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
package com.iver.utiles;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * This class contains static methods to manage Dates. It was principally 
 * created because of some problems doing the "String do DateTime" 
 * and the "DateTime to String" conversions.  
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class DateTime {

/**
 * returns the current date
 * 
 * 
 * @return java.util.Date
 */
    public static Date getCurrentDate() {        
        Calendar cal = new GregorianCalendar();
 
        return cal.getTime();
    } 

/**
 * It trnasforms one date in one String
 * 
 * 
 * @return A String
 * @param dtK Date
 * @param sFormat Date format. Example: "Y-m-d H:i:s.Z";
 */
    public static String dateToString(Date dtK, String sFormat) {        
        String sDate;
        int nYear;
        int nMonth;
        int nDay;
        int nHour;
        int nMinute;
        int nSecond;
        int nMS;
        Calendar clnK;
        String sf;
        int jc;
        clnK = Calendar.getInstance(Locale.US);
        clnK.setTime(dtK);
        nYear = clnK.get(Calendar.YEAR);
        nMonth = 1 + clnK.get(Calendar.MONTH);
        nDay = clnK.get(Calendar.DAY_OF_MONTH);
        nHour = clnK.get(Calendar.HOUR_OF_DAY);
        nMinute = clnK.get(Calendar.MINUTE);
        nSecond = clnK.get(Calendar.SECOND);
        nMS = clnK.get(Calendar.MILLISECOND);
        sDate = "";
        for (jc = 0; jc < sFormat.length(); jc++) {
            switch (sFormat.charAt(jc)) {
                case 'Y':
                    sDate += nYear;
                    break;
                case 'm':
                    sf = "" + nMonth;
                    if (nMonth < 10) {
                        sf = "0" + sf;
                    }
                    sDate += sf;
                    break;
                case 'd':
                    sf = "" + nDay;
                    if (nDay < 10) {
                        sf = "0" + sf;
                    }
                    sDate += sf;
                    break;
                case 'H':
                    sf = "" + nHour;
                    if (nHour < 10) {
                        sf = "0" + sf;
                    }
                    sDate += sf;
                    break;
                case 'i':
                    sf = "" + nMinute;
                    if (nMinute < 10) {
                        sf = "0" + sf;
                    }
                    sDate += sf;
                    break;
                case 's':
                    sf = "" + nSecond;
                    if (nSecond < 10) {
                        sf = "0" + sf;
                    }
                    sDate += sf;
                    break;
                case 'Z':
                    sf = "" + nMS;
                    if (nMS < 10) {
                        sf = "0" + sf;
                    }
                    sDate += sf;
                    break;
                default:
                    sDate += sFormat.substring(jc, jc + 1);
            }
        }
        return sDate;
    } 
   
/**
 * It transfoms one String in one Date
 * 
 * 
 * @return Date
 * @param sDate String
 */
    public static Date stringToDate(String sDate) {        
        Date dtRes;
        Calendar clnK;
        int nYear;
        int nMonth;
        int nDay;
        int nHour;
        int nMinute;
        int nSecond;
        int nMS;
        String sf;
        for (; sDate.length() < 23;)
            sDate += "0";
        sf = sDate.substring(0, 4);
        nYear = Integer.parseInt(sf);
        sf = sDate.substring(5, 7);
        nMonth = Integer.parseInt(sf) - 1;
        sf = sDate.substring(8, 10);
        nDay = Integer.parseInt(sf);
        sf = sDate.substring(11, 13);
        nHour = Integer.parseInt(sf);
        sf = sDate.substring(14, 16);
        nMinute = Integer.parseInt(sf);
        sf = sDate.substring(17, 19);
        nSecond = Integer.parseInt(sf);
        sf = sDate.substring(20, 23);
        nMS = Integer.parseInt(sf);
        clnK = Calendar.getInstance(Locale.US);
        clnK.set(nYear, nMonth, nDay, nHour, nMinute, nSecond);
        clnK.set(Calendar.MILLISECOND, nMS);
        dtRes = new Date();
        dtRes = clnK.getTime();
        //	    sf=dateToString(dtRes,"Y-m-d H:i:s.Z");
        return dtRes;
    } 
 }
