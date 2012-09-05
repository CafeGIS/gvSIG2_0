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
 * 2008 Prodevelop S.L. main development
 */

package org.gvsig.normalization.algorithm.impl;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.gvsig.normalization.algorithm.NormalizationAlgorithm;
import org.gvsig.normalization.pattern.Datevalue;
import org.gvsig.normalization.pattern.Decimalvalue;
import org.gvsig.normalization.pattern.Element;
import org.gvsig.normalization.pattern.Fieldtype;
import org.gvsig.normalization.pattern.Integervalue;
import org.gvsig.normalization.pattern.Patternnormalization;
import org.gvsig.normalization.pattern.Stringvalue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class tokens strings
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 * 
 */

public class DefaultNormalizationAlgorithm implements NormalizationAlgorithm {

	private static final Logger log = LoggerFactory
			.getLogger(DefaultNormalizationAlgorithm.class);

	private Patternnormalization pat = null;
	private int numFields;
	private Element[] elements = null;

	/**
	 * Constructor
	 * 
	 * @param _pat
	 *            Normalization pattern
	 */
	public DefaultNormalizationAlgorithm(Patternnormalization _pat) {
		this.pat = _pat;
		this.elements = pat.getArrayElements();
	}

	/**
	 * This method cuts a chain in several parts and they are returned in a List
	 * 
	 * @param _chain
	 *            strings
	 * @return list with the strings tokenized
	 */
	public List<String> splitChain(String _chain) {
		String preChain = _chain;
		String postChain = "";
		List<String> subStrings = new ArrayList<String>();

		// EXCEPTIONAL CASES
		if (_chain == null) {
			return subStrings;
		}

		else if (_chain.compareToIgnoreCase("") == 0) {
			subStrings.add(_chain);
			return subStrings;
		}

		// NORMAL CASE
		else {
			int fw = 0;
			int init = 0;
			String subChain = "";

			for (int i = 0; i < numFields; i++) {

				if (preChain.length() == 0) {
					return subStrings;
				} else {

					fw = elements[i].getFieldwidth();
					// Cut chain with fixed width
					if (fw > 0 && preChain.length() > fw) {
						subChain = preChain.substring(init, fw);
						subStrings.add(subChain);
						postChain = preChain.substring(fw);
						preChain = postChain;

					} else if (fw > 0 && preChain.length() <= fw) {
						subStrings.add(preChain);
						return subStrings;
					}
					// Cut chain with separators
					else {
						// Load specific separators
						List<String> separators = loadSpecificSeparators(elements[i]);
						boolean join = withJoinSeparators(elements[i]);
						// Search the first delimiter in the chain
						int posi = calculatePosition(separators, preChain);
						int tamSep = 0;
						if (!join) {
							tamSep = calculateSizeSep(separators, preChain);
						} else {
							tamSep = calculateSizeJoinSep(separators, preChain,
									posi);
						}
						// Firsts elements
						if (i < numFields - 1) {

							if (join) {
								while (posi == 0) {
									preChain = deleteFirst(preChain);
									posi = calculatePosition(separators,
											preChain);
									if (preChain.length() == 0) {
										break;
									}
								}
								subChain = preChain.substring(0, posi);
								try {
									postChain = preChain.substring(posi
											+ tamSep);
								} catch (Exception e) {
									postChain = "";
								}
								subStrings.add(subChain);
								preChain = postChain;
							} else {
								subChain = preChain.substring(0, posi);
								postChain = preChain.substring(posi + tamSep);
								subStrings.add(subChain);
								preChain = postChain;
							}

						}
						// Last element
						else {
							subStrings.add(preChain);
						}
					}
				}
			}
		}
		return subStrings;
	}

	/**
	 * This method cuts a chain in several parts from separators
	 * 
	 * @param chain
	 *            string
	 * @param fields
	 *            fields number
	 * @param separators
	 *            array of characters
	 * @param joinDelimiters
	 *            with or without joinDelimiters
	 * @return list with the strings tokenized
	 */
	public static List<String> splitChainBySeparators(String chain, int fields,
			String[] separators, boolean joinDelimiters) {

		List<String> subStrings = new ArrayList<String>();
		int posTemp = -1;
		String separator;
		String chain2 = chain;
		int campos = fields;

		// EXCEPTIONAL CASES
		if (chain.compareToIgnoreCase("") == 0 || campos == 0
				|| separators.length < 1) {
			subStrings.add(chain);
			return subStrings;
		}

		// NORMAL CASE
		else {

			// Only (parts-1) loops
			for (int i = 0; i < (campos - 1); i++) {
				int posi = Integer.MAX_VALUE;
				String firstChain;
				for (int j = 0; j < separators.length; j++) {
					separator = separators[j];
					posTemp = chain2.indexOf(separator);
					if (posTemp != -1 && posTemp < posi) {
						posi = posTemp;
					}
					posTemp = -1;
				}
				if (posi == 0 && joinDelimiters) {
					campos++;
					chain2 = chain2.substring(posi + 1);
				} else {
					firstChain = chain2.substring(0, posi);
					chain2 = chain2.substring(posi + 1);
					subStrings.add(firstChain);
					// In the last loop add the first chain and the rest of
					// chain
					if (i == (campos - 2)) {
						subStrings.add(chain2);
					}
				}
			}
			return subStrings;
		}
	}

	/**
	 * This method cuts a chain in several parts from fixed width
	 * 
	 * @param chain
	 *            string
	 * @param fieldWidth
	 *            array with fields widths
	 * @return list with the strings tokenized
	 */
	public static List<String> splitChainByFixedWidth(String chain, int[] fieldWidth) {

		List<String> subStrings = new ArrayList<String>();
		int elements = fieldWidth.length;
		String subChain;
		int inicio = 0;
		int fin = 0;

		// EXCEPTIONAL CASES
		if (chain.compareToIgnoreCase("") == 0 || fieldWidth.length < 1) {
			subStrings.add(chain);
			return subStrings;
		}

		// NORMAL CASE
		else {
			for (int i = 0; i < elements; i++) {
				fin = fin + fieldWidth[i];
				subChain = chain.substring(inicio, fin);
				subStrings.add(subChain);
				inicio = fin;
			}
			return subStrings;
		}
	}

	/**
	 * This method filters the split chains with the in-separators
	 * 
	 * @param chains
	 *            strings of the one row
	 * @return strings filtered by type
	 */
	public List<String> filterSplitChains(List<String> chains) {

		DecimalFormat numForm = (DecimalFormat) DecimalFormat
				.getInstance(Locale.getDefault());
		DecimalFormatSymbols simb = numForm.getDecimalFormatSymbols();

		Fieldtype nft = null;

		String decsep;
		char cdecsep;
		String thosep;
		char cthosep;
		String txsep;

		List<String> postChain = new ArrayList<String>();
		String aux = "";

		for (int i = 0; i < numFields; i++) {

			nft = elements[i].getFieldtype();

			decsep = elements[i].getInfieldseparators().getDecimalseparator()
					.trim();
			cdecsep = decsep.compareTo("") == 0 ? (char) 0x20 : decsep
					.charAt(0);
			thosep = elements[i].getInfieldseparators().getThousandseparator()
					.trim();
			cthosep = thosep.compareTo("") == 0 ? (char) 0x20 : thosep
					.charAt(0);

			txsep = elements[i].getInfieldseparators().getTextseparator()
					.trim();

			if (txsep.compareToIgnoreCase("\"") == 0) {
				txsep = "\"";
			}

			simb.setDecimalSeparator(cdecsep);
			try {
				simb.setGroupingSeparator(cthosep);
			} catch (RuntimeException e1) {
				log.error("Error setting the group separator", e1);
			}
			// Fill fields empties
			if (chains.size() < numFields) {

				for (int j = chains.size(); j < numFields; j++) {
					chains.add(j, "");
				}
			}

			if (((Integervalue) nft.getIntegervalue()) != null
					|| ((Decimalvalue) nft.getDecimalvalue()) != null) {
				numForm.setDecimalFormatSymbols(simb);
				try {
					String cadena = ((String) chains.get(i)).trim();
					// aux = (numForm.parse(cadena)).toString().trim();

					int num = cadena.length();
					boolean comproba = test(cadena, cdecsep, cthosep);
					if (num > 0 && comproba) {
						aux = (numForm.parse(cadena)).toString().trim();
					} else {
						aux = "";
					}

				} catch (Exception e) {
					aux = "";
				}
			}

			// Field type (VARCHAR)
			if (((Stringvalue) nft.getStringvalue()) != null) {
				String cadena = (String) chains.get(i);
				if (cadena != null) {
					aux = cadena.replace(txsep, "");
				} else {
					aux = "";
				}
			}

			// Field type (DATE)
			if (((Datevalue) nft.getDatevalue()) != null) {
				String cadena = (String) chains.get(i);
				if (cadena.length() > 0 || cadena != null) {
					aux = cadena.replace(txsep, "");
				} else {
					aux = "";
				}
			}
			postChain.add(aux);
		}
		return postChain;

	}

	/**
	 * This method loads the join separators attribute of one Element
	 * 
	 * @param ad
	 * @return with or without joinSeparators
	 */
	private boolean withJoinSeparators(Element ad) {
		return ad.getFieldseparator().getJoinsep();
	}

	/**
	 * This method deletes the first element of one substring
	 * 
	 * @param chain
	 *            initial string
	 * @return string
	 */
	private String deleteFirst(String chain) {
		String del = chain.substring(1);
		return del;
	}

	/**
	 * This method gets the first position in the string of the separators
	 * group.
	 * 
	 * @param separators
	 *            separators characters list
	 * @param preChain
	 *            initial string
	 * @return
	 */
	private int calculatePosition(List<String> separators, String preChain) {

		String separator;
		int posTemp = -1;
		int posi = Integer.MAX_VALUE;
		for (int j = 0; j < separators.size(); j++) {
			separator = (String) separators.get(j);
			posTemp = preChain.indexOf(separator);
			if (posTemp != -1 && posTemp < posi) {
				posi = posTemp;
			}
			posTemp = -1;
		}
		if (posi > preChain.length()) {
			posi = preChain.length();
		}

		return posi;
	}

	/**
	 * This method calculates the number of elements of separators
	 * 
	 * @param separators
	 * @param preChain
	 * @return number of elements
	 */

	private int calculateSizeSep(List<String> separators, String preChain) {

		String separator;
		int posTemp = -1;
		int posi = Integer.MAX_VALUE;
		String sep = "";
		for (int j = 0; j < separators.size(); j++) {
			separator = (String) separators.get(j);
			posTemp = preChain.indexOf(separator);
			if (posTemp != -1 && posTemp < posi) {
				posi = posTemp;
				sep = separator;

			}
			posTemp = -1;
		}

		return sep.length();
	}

	/**
	 * This method calculates the number of elements of joins separators
	 * 
	 * @param separators
	 * @param preChain
	 * @return number of elements
	 */

	private int calculateSizeJoinSep(List<String> separators, String preChain,
			int position) {

		String chain = preChain.substring(position);
		int tam = 0;
		for (int i = 0; i < chain.length(); i++) {
			boolean exist = false;
			String cha = chain.substring(i, i + 1);
			for (int j = 0; j < separators.size(); j++) {
				String sep = (String) separators.get(j);
				if (cha.compareTo(sep) == 0) {
					tam = i;
					exist = true;
					break;
				}
			}
			if (!exist) {
				break;
			}
		}
		return tam + 1;
	}

	/**
	 * This method loads the specifics separators of one Element
	 * 
	 * @param adrElem
	 * @return separators list
	 */
	private List<String> loadSpecificSeparators(Element adrElem) {
		List<String> separators = new ArrayList<String>();
		if (adrElem.getFieldseparator().getColonsep()) {
			separators.add(",");
		}
		if (adrElem.getFieldseparator().getSemicolonsep()) {
			separators.add(";");
		}
		if (adrElem.getFieldseparator().getTabsep()) {
			separators.add("\t");
		}
		if (adrElem.getFieldseparator().getSpacesep()) {
			separators.add(" ");
		}
		if (adrElem.getFieldseparator().getOthersep() != null
				&& adrElem.getFieldseparator().getOthersep()
						.compareToIgnoreCase("") != 0) {
			String sepOth = (String) adrElem.getFieldseparator().getOthersep();
			separators.add(sepOth);
		}

		return separators;
	}

	/**
	 * This method tests the numbers format
	 * 
	 * @param str
	 * @param dec
	 * @param sep
	 * @return true if there aren't other characters
	 */
	private boolean test(String str, char dec, char sep) {
		String str2 = str.replaceAll("[0-9]", "");
		str2 = str2.replaceAll("-", "");
		str2 = str2.replace("E", "");

		String str3 = str2;
		if (str2.indexOf(dec) >= 0) {
			int ind = str2.indexOf(String.valueOf(dec));
			str3 = str2.substring(0, ind)
					+ str2.substring(ind + 1, str2.length());
		}
		String str4 = str3;
		if (str3.indexOf(sep) >= 0) {
			int ind = str3.indexOf(String.valueOf(sep));
			str4 = str3.substring(0, ind)
					+ str3.substring(ind + 1, str3.length());
		}

		return str4.length() == 0;
	}

}
