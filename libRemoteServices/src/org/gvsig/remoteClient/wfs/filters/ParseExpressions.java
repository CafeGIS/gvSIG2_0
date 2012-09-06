package org.gvsig.remoteClient.wfs.filters;

import java.util.ArrayList;

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
 * $Id: ParseExpressions.java 7897 2006-10-05 10:25:15Z jorpiell $
 * $Log$
 * Revision 1.1  2006-10-05 10:25:15  jorpiell
 * Implementado el filter encoding
 *
 *
 */
/**
 * It parses a SQL expression (just the where part) and seperates
 * it in "sub-expresion". A sub-expresion could be a separator ('(' or ')'),
 * a logical operator ('And', 'Not' or 'Or') or a expresion (A op B).
 *  
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class ParseExpressions {
		
	/**
	 * It parses a expression and return one arrayList with
	 * the separated words
	 * @param expression
	 * @return
	 */
	public ArrayList parseExpression(String expression){
		ArrayList expressions = new ArrayList();
		
		char[] chars = expression.toCharArray();
		int i=0;
		while (i<chars.length){
			if (chars[i] == '('){
				expressions.add("(");
				i++;
			}else if(chars[i] == ')'){
				expressions.add(")");
				i++;
			}else if(chars[i] == ' '){
				i++;
			}else{
				int desp = getShift(chars,i);
				expressions.add(expression.substring(i,i + desp));
				i = i + desp;
			}
		}		
		return expressions;
	}
	
	/**
	 * Gets the shift of the next expression.
	 * @param chars
	 * @param position
	 * @return
	 */
	private int getShift(char[] chars,int position){
		int shift = 0;
		shift = isAndOperator(chars,position);
		if (shift == 0){
			shift = isOrOperator(chars,position);
			if (shift == 0){
				shift = isNotOperator(chars,position);
				if (shift == 0){
					shift = isExpression(chars,position);
				}
			}
		}
		return shift;
	}
	
	private int isExpression(char[] chars,int position){
		int desp = 0;
		for (int i=position ;i<chars.length ; i++){
			if (chars[i] == ')'){
				break;			
			}			
			desp++;
		}
		return desp;
	}
	
	/**
	 * Return true if the next array is a AND operator
	 * @param chars
	 * @param position
	 * @return
	 */
	private int isAndOperator(char[] chars,int position){
		if (chars[position] == 'A' && 
				chars[position+1] == 'N' && 
				chars[position+2] == 'D' &&
				chars[position+3] == ' '){
			return 3;
		}
		return 0;
	}
	
	/**
	 * Return true if the next array is a OR operator
	 * @param chars
	 * @param position
	 * @return
	 */
	private int isOrOperator(char[] chars,int position){
		if (chars[position] == 'O' && 
				chars[position+1] == 'R' && 
				chars[position+2] == ' '){
			return 2;
		}
		return 0;
	}
	
	/**
	 * Return true if the next array is a NOT operator
	 * @param chars
	 * @param position
	 * @return
	 */
	private int isNotOperator(char[] chars,int position){
		if (chars[position] == 'N' && 
				chars[position+1] == 'O' && 
				chars[position+2] == 'T' &&
				chars[position+3] == ' '){
			return 3;
		}
		return 0;
	}

}
