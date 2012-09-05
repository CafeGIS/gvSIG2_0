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
 * 2008 Prodevelop S.L  vsanjaime   programador
 */

package org.gvsig.geocoding.distance.impl;

import org.gvsig.geocoding.distance.StringsDistance;

/**
 * This class calculate the levenshtein distance between two strings
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class LevenshteinDistance implements StringsDistance{

	/**
	 * calculate levenshtein distance between two strings
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static int computeLevenshteinDistance(String str1, String str2) {
		return computeLevenshteinDistance(str1.toCharArray(), str2
				.toCharArray());
	}

	/**
	 * get levenshtein distance between two strings
	 * 
	 * @param s
	 * @param t
	 * @return
	 */
	public static int getLevenshteinDistance(String s, String t) {
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

	/**
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	private static int minimum(int a, int b, int c) {
		if (a <= b && a <= c)
			return a;
		if (b <= a && b <= c)
			return b;
		return c;
	}

	/**
	 * calculate levenshtein distance between two chars
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	private static int computeLevenshteinDistance(char[] str1, char[] str2) {
		int[][] distance = new int[str1.length + 1][];

		for (int i = 0; i <= str1.length; i++) {
			distance[i] = new int[str2.length + 1];
			distance[i][0] = i;
		}
		for (int j = 0; j < str2.length + 1; j++)
			distance[0][j] = j;

		for (int i = 1; i <= str1.length; i++)
			for (int j = 1; j <= str2.length; j++)
				distance[i][j] = minimum(distance[i - 1][j] + 1,
						distance[i][j - 1] + 1, distance[i - 1][j - 1]
								+ ((str1[i - 1] == str2[j - 1]) ? 0 : 1));

		return distance[str1.length][str2.length];
	}

	/**
	 * get levenshtein distance between two strings
	 * @param s first string
	 * @param t second string
	 */
	public int getStringsDistance(String s, String t) {
		
		return computeLevenshteinDistance(s, t);
	}
}
