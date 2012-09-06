/*
 * Created on 07-sep-2005
 */
package org.gvsig.remoteClient.utils;
import java.awt.geom.Rectangle2D;
import java.util.TreeMap;

/**
 * @author Luis W. Sevilla (sevilla_lui@gva.es)
 */
public class DumbXMLParser {
	public Rectangle2D getBoundingBox(String l) {
		// System.out.println("l='"+l+"'");
		TreeMap pairs = getPairs(l);
		return getBoundingBox(pairs);
	}

	public Rectangle2D getBoundingBox(TreeMap pairs) {
		double minX = Double.parseDouble((String)pairs.get("minx"));
		double minY = Double.parseDouble((String)pairs.get("miny"));
		double maxX = Double.parseDouble((String)pairs.get("maxx"));
		double maxY = Double.parseDouble((String)pairs.get("maxy"));

		return new Rectangle2D.Double(minX, minY, maxX-minX, maxY-minY);
	}
	
	public TreeMap getPairs(String l) {
		TreeMap data = new TreeMap();
		int pos = l.indexOf("=");
		while (pos>0) {
			String cmd = l.substring(l.lastIndexOf(" ", pos)+1,
				l.indexOf("\"", pos+2)+1);
			//System.out.println(cmd);
			int comilla = cmd.indexOf("\"");
			if (comilla > 0) {
				String key = l.substring(l.lastIndexOf(" ", pos)+1, pos);
				String value = cmd.substring(comilla+1,cmd.length()-1);
				data.put(key.toLowerCase(), value);
			}
			pos = l.indexOf("=", pos+1);
		}
		return data;
	}
	
	/**
	 * saca un valor double de una linea del tipo 
	 * <westBoundLongitude>-180</westBoundLongitude>
	 * @param l
	 * @return
	 */
	public double getValueDouble(String l) {
		return Double.parseDouble(getValue(l));	
	}
	
	/**
	 * saca un valor double de una linea del tipo 
	 * <westBoundLongitude>-180</westBoundLongitude>
	 * @param l
	 * @return
	 */
	public String getValue(String l) {
		int pos = l.indexOf(">");
		return l.substring(pos+1, l.indexOf("<", pos));	
	}
	
	public String getToken(String l, int num) {
		int pos = l.indexOf("<");
		for (int i=0; i<num; i++)
			pos = l.indexOf("<", pos+1);
		return l.substring(pos+1, l.indexOf(">", pos));	
	}
	
	public int countTokens(String l) {
		int times = 0;
		int pos = l.indexOf("<");
		while (pos >= 0) {
			times ++; pos = l.indexOf("<", pos+1);
		}
		return times;
	}
}
