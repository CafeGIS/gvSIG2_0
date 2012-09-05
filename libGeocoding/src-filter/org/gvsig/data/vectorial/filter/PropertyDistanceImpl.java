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
 * 2008 Prodevelop S.L  main development
 */

package org.gvsig.data.vectorial.filter;

import java.util.ArrayList;
import java.util.List;

import org.opengis.filter.Filter;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.expression.Expression;

/**
 * class
 * 
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 * 
 */

public class PropertyDistanceImpl implements PropertyDistance {

    private static final String REGEX_TOKENIZER = "\\s";
    private Expression attribute;
    private String pattern;
    private float threshold;

    public PropertyDistanceImpl(Expression arg0, Expression arg1,
	    float threshold) {
	this.attribute = arg0;
	this.pattern = arg1.toString();
	this.threshold = threshold;
    }

    public PropertyDistanceImpl(Expression arg0, String arg1, float threshold) {
	this.attribute = arg0;
	this.pattern = arg1;
	this.threshold = threshold;
    }

    public Object accept(FilterVisitor visitor, Object extraData) {
	return visitor.visitNullFilter(extraData);
    }

    /**
     * Evaluate the text distance between a feature attribute value and
     */
    @SuppressWarnings("unchecked")
    public boolean evaluate(Object feature) {

	if (attribute == null) {
	    return false;
	}

	Object value = attribute.evaluate(feature);

	if (null == value) {
	    return false;
	}

	String[] valueChains = value.toString().split(REGEX_TOKENIZER);
	String[] pattChains = this.pattern.split(REGEX_TOKENIZER);

	DistanceFilterFactory2Impl fact = new DistanceFilterFactory2Impl();

	/**
	 * Really evaluate the filter when we have just one string per input
	 */
	if (valueChains.length == 1 && pattChains.length == 1) {
	    int dist = getLevenshteinDistance(valueChains[0], pattChains[0]);
	    double distNorm = 1.0 - dist / (pattern.length() * 1.0);
	    return distNorm >= threshold;
	} else {
	    /**
	     * Otherwise we will iterate to assure all patters appear in one of
	     * the value chains
	     */
	    Filter filtAND;
	    List listAND = new ArrayList();
	    for (int i = 0; i < pattChains.length; i++) {
		String patt = pattChains[i];
		Filter filtOR;
		List listOR = new ArrayList();
		for (int j = 0; j < valueChains.length; j++) {
		    Expression exp2 = fact.literal(valueChains[j]);
		    PropertyDistance dist = fact.distance(exp2, patt,
			    this.threshold);
		    listOR.add(dist);
		}
		filtOR = fact.or(listOR);
		listAND.add(filtOR);
	    }
	    filtAND = fact.and(listAND);
	    return filtAND.evaluate(null);
	}
    }

    private int getLevenshteinDistance(String s, String t) {
	if (s == null || t == null) {
	    throw new IllegalArgumentException("Strings must not be null");
	}

	/*
	 * The difference between this impl. and the previous is that, rather
	 * than creating and retaining a matrix of size s.length()+1 by
	 * t.length()+1, we maintain two single-dimensional arrays of length
	 * s.length()+1. The first, d, is the 'current working' distance array
	 * that maintains the newest distance cost counts as we iterate through
	 * the characters of String s. Each time we increment the index of
	 * String t we are comparing, d is copied to p, the second int[]. Doing
	 * so allows us to retain the previous cost counts as required by the
	 * algorithm (taking the minimum of the cost count to the left, up one,
	 * and diagonally up and to the left of the current cost count being
	 * calculated). (Note that the arrays aren't really copied anymore, just
	 * switched...this is clearly much better than cloning an array or doing
	 * a System.arraycopy() each time through the outer loop.)
	 * 
	 * Effectively, the difference between the two implementations is this
	 * one does not cause an out of memory condition when calculating the LD
	 * over two very large strings.
	 */

	int n = s.length(); // length of s
	int m = t.length(); // length of t

	if (n == 0) {
	    return m;
	} else if (m == 0) {
	    return n;
	}

	int p[] = new int[n + 1]; // 'previous' cost array, horizontally
	int d[] = new int[n + 1]; // cost array, horizontally
	int _d[]; // placeholder to assist in swapping p and d

	// indexes into strings s and t
	int i; // iterates through s
	int j; // iterates through t

	char t_j; // jth character of t

	int cost; // cost

	for (i = 0; i <= n; i++) {
	    p[i] = i;
	}

	for (j = 1; j <= m; j++) {
	    t_j = t.charAt(j - 1);
	    d[0] = j;

	    for (i = 1; i <= n; i++) {
		cost = s.charAt(i - 1) == t_j ? 0 : 1;
		// minimum of cell to the left+1, to the top+1, diagonally left
		// and up +cost
		d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1]
			+ cost);
	    }

	    // copy current distance counts to 'previous row' distance counts
	    _d = p;
	    p = d;
	    d = _d;
	}

	// our last action in the above loop was to switch d and p, so p now
	// actually has the most recent cost counts
	return p[n];
    }

}
