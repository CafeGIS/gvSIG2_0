/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
package org.gvsig.fmap.mapcontext.rendering.legend;

import org.gvsig.compat.CompatLocator;


/**
 * Clase intervalo.
 *
 * @author Vicente Caballero Navarro
 */
public class FInterval implements IInterval {
	private double from;
	private double to;
	public FInterval(){
	}
	/**
	 * Crea un nuevo FInterval.
	 *
	 * @param from Origen.
	 * @param to Destino.
	 */
	public FInterval(double from, double to) {
		this.from = from;
		this.to = to;
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.rendering.IInterval#isInInterval(com.hardcode.gdbms.engine.values.Value)
	 */
	public boolean isInInterval(Object v) {
		double valor=0;
		if (v == null){
			return false;
		}else if (v instanceof Number) {
			valor = ((Number) v).doubleValue();
		}
		return ((valor >= from) && (valor <= to));
	}

	/**
	 * Devuelve el n�mero de origen.
	 *
	 * @return N�mero de inicio.
	 */
	public double getMin() {
		return from;
	}

	/**
	 * Devuelve el n�mero final.
	 *
	 * @return N�mero final.
	 */
	public double getMax() {
		return to;
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.rendering.IInterval#toString()
	 */
	public String toString() {
		return from + "-" + to;
	}

	/**
	 * Crea un FInterval nuevo a partir de un String con el n�mero inicial un
	 * gui�n y el n�mero final.
	 *
	 * @param s String.
	 *
	 * @return FInterval nuevo.
	 */
	public static IInterval create(String s) {
		//String[] str = s.split("-");
        String[] str = CompatLocator.getStringUtils().split(s,"-");
		if (s.startsWith("-")) {
			str[0]="-"+str[1];
			CompatLocator.getStringUtils().replaceFirst(s,"-","");
			str[1]=str[2];
		}
		if (s.contentEquals(new StringBuffer("--"))) {
			str[1]=s.substring(s.indexOf('-')+1,s.length()-1);
		}

		IInterval inter=null;
		try{
		inter = new FInterval(Double.parseDouble(str[0]),
				Double.parseDouble(str[1]));
		}catch (NumberFormatException e) {
			return new NullIntervalValue();
		}
		return inter;
	}
}
