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
package org.gvsig.fmap.mapcontext.rendering.legend;

import java.util.Hashtable;

import org.gvsig.fmap.mapcontext.rendering.legend.events.LabelLegendEvent;
import org.gvsig.fmap.mapcontext.rendering.legend.events.LegendChangedEvent;
import org.gvsig.fmap.mapcontext.rendering.legend.events.LegendClearEvent;
import org.gvsig.fmap.mapcontext.rendering.legend.events.LegendContentsChangedListener;
import org.gvsig.fmap.mapcontext.rendering.symbols.IMultiLayerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;

import com.iver.utiles.IPersistence;
import com.iver.utiles.XMLEntity;
import org.gvsig.compat.CompatLocator;

/**
 * Class ZSort is used in order to store information about the symbols
 * which are placed in the Z axis (because a level of symbols has been
 * used).
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class ZSort implements IPersistence, LegendContentsChangedListener {
	private static final String COLUMN_SEPARATOR = ",";
	private static final String ROW_SEPARATOR = ";";
	private int[][] matrix;
	private boolean usingZSort;
	private Hashtable symbols = new Hashtable();  //<ISymbol, Integer>


	public ZSort(ILegend legend) {
		initialize(legend);
	}

	private void initialize(ILegend legend) {
		ISymbol[] symbols;
		if (legend instanceof IClassifiedLegend) {
			symbols = ((IClassifiedLegend) legend).getSymbols();

		} else {
			symbols = new ISymbol[] {legend.getDefaultSymbol()};
		}
		matrix = new int[symbols.length][];

		for (int i = 0; i < symbols.length; i++) {
			this.symbols.put(symbols[i], new Integer(i));
			int rowLength =  symbols[i] instanceof IMultiLayerSymbol ?
                  	 ((IMultiLayerSymbol) symbols[i]).getLayerCount() :
	                 1;
			matrix[i] = new int[rowLength];
		}
		legend.addLegendListener(this);
	}

	private void addSymbol(ISymbol sym){
		if(!symbols.contains(sym)){
			int rowLength =  sym instanceof IMultiLayerSymbol ?
					((IMultiLayerSymbol) sym).getLayerCount() : 1;
			int[][] auxMatrix = new int[matrix.length+1][];
			int newIndex = matrix.length;
			for (int i=0; i<newIndex; i++){
				auxMatrix[i] = matrix[i];
			}
			int[] row = new int[rowLength];
			for (int i=0; i<rowLength; i++){
				row[i] = 0;
			}
			auxMatrix[newIndex] = row;
			matrix = auxMatrix;
			this.symbols.put(sym, new Integer(newIndex));
		}
	}

	public void legendChanged(LegendChangedEvent e) {
		symbols.clear();
		usingZSort = false;
		initialize(e.getNewLegend());

	}

	public String getClassName() {
		return getClass().getName();
	}

	public XMLEntity getXMLEntity() {
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className", getClassName());

		/*
		 * ADVICE:
		 * don't try to persist symbols!!
		 * they are already persisted by the legend!!!
		 */

		xml.putProperty("usingZSort", isUsingZSort());


		String strMatrix = "";
		for (int i = 0; i < matrix.length; i++) {
			int[] row = matrix[i];
			String strRow = "";
			for (int j = 0; j < row.length; j++) {
				strRow += matrix[i][j] + COLUMN_SEPARATOR;
			}
			strMatrix += strRow + ROW_SEPARATOR;
		}

		xml.putProperty("matrix", strMatrix);
		return xml;
	}

	public void setXMLEntity(XMLEntity xml) {
		setUsingZSort(xml.getBooleanProperty("usingZSort"));

		/*
		 * ADVICE:
		 * don't try to initialize the symbols!!
		 * they must be initialized by the legend!!!
		 */

		String strMatrix = xml.getStringProperty("matrix");
                String[] strRows = CompatLocator.getStringUtils().split(strMatrix,ROW_SEPARATOR);

		matrix = new int[strRows.length][];
		for (int i = 0; i < strRows.length; i++) {
			String[] strColumns = CompatLocator.getStringUtils().split(strRows[i],COLUMN_SEPARATOR);
			matrix[i] = new int[strColumns.length];
			for (int j = 0; j < strColumns.length; j++) {
				matrix[i][j] = Integer.parseInt(strColumns[j]);
			}
		}
	}


	public int getLevelCount() {
		int count = -1;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				count = Math.max(count, matrix[i][j]);
			}
		}
		return count + 1;
	}


	public void setUsingZSort(boolean usingZSort) {
		this.usingZSort = usingZSort;
	}

	public void setLevels(ISymbol sym, int[] values) {
		setLevels(((Integer)symbols.get(sym)).intValue(), values);
	}


	public void setLevels(int row, int[] values) {
		matrix[row] = values;
	}

	public int[] getLevels(ISymbol sym) {
		Integer row = (Integer) symbols.get(sym);
		if (row != null) {
			return getLevels(row.intValue());
		} else {
			ISymbol[] theSymbols = getSymbols();
			for (int i = 0; i < theSymbols.length; i++) {
				ISymbol auxSymbol = theSymbols[i];
				if (auxSymbol.equals(sym)){
					return getLevels(((Integer)symbols.get(auxSymbol)).intValue());
				}
			}
		}
		return null;
	}


	public int[] getLevels(int row) {
		return matrix[row];
	}


	public boolean isUsingZSort() {
		return usingZSort;
	}


	public ISymbol[] getSymbols() {
		return (ISymbol[])symbols.keySet().toArray(new ISymbol[0]);
	}


	public String[] getDescriptions() {
		ISymbol[] symbols = getSymbols();
		String[] descs = new String[symbols.length];
		for (int i = 0; i < descs.length; i++) {
			descs[i] = symbols[i].getDescription();
		}
		return descs;
	}

	/*
	 * [PACO] Comentarizado porque no hace lo que se esperaría de él.
	 * Si tenemos una leyenda con un simbolo Multilayer compuesto
	 * por, entre otros, un simbolo simple que coincide con otro de
	 * los simbolos de la leyenda, este metodo devuelve el nivel
	 * del símbolo que encuentra primero.
	 *
	 * Se ha eliminado cualquier referencia a este metodo en el resto
	 * del workspace, sustituyendola convenientemente por
	 * referencias a getLevels.
	 */
//	public int getSymbolLevel(ISymbol layer) {
//		ISymbol[] theSymbols = getSymbols();
//
//		for (int i = 0; i < theSymbols.length; i++) {
//			ISymbol mySymbol = theSymbols[i];
//
//			if (mySymbol instanceof IMultiLayerSymbol) {
//				IMultiLayerSymbol multiSym = (IMultiLayerSymbol) mySymbol;
//				for (int j = 0; j < multiSym.getLayerCount(); j++) {
//					ISymbol myInnerSymbol = multiSym.getLayer(j);
//					if (myInnerSymbol.equals(layer)) {
//						int row = ((Integer)symbols.get(multiSym)).intValue();
//						return matrix[row][j];
//					}
//				}
//			} else {
//				if (mySymbol.equals(layer)) {
//					return matrix[((Integer)symbols.get(layer)).intValue()][0];
//				}
//			}
//		}
//
////		return 0;
//		return -1;
//	}

	public int getTopLevelIndexAllowed() {
		ISymbol[] symbols = getSymbols();
		int count=0;
		for (int i = 0; i < symbols.length; i++) {
			if (symbols[i] instanceof IMultiLayerSymbol) {
				IMultiLayerSymbol mSymbol = (IMultiLayerSymbol) symbols[i];
				count = Math.max(count, mSymbol.getLayerCount());
			} else
				count = Math.max(count, 1);
		}
		return count;
	}

	public String toString() {
		String out = "Symbols:\n---------\n\n";
		ISymbol[] syms = getSymbols();
		for (int i = 0; i < syms.length; i++) {
			out += syms.getClass()+":\t"+syms[i].getDescription();
		}
		out += "\nMatrix:\n--------\n\n";
		out += "    \t";
		for (int i = 0; i < getTopLevelIndexAllowed(); i++) {
			out += "column"+i+"\t\t";
		}
		out += "\n";
		for (int i = 0; i < matrix.length; i++) {
			out += "row "+i+":\t";
			for (int j = 0; j < matrix[i].length; j++) {
				out += matrix[i][j]+"\t\t";
			}
			out += "\n";
		}
		return out;
	}

	private void replaceSymbol(ISymbol oldSymbol, ISymbol newSymbol) {
		if (oldSymbol == newSymbol) return;

		if(oldSymbol != null){

			Integer value = (Integer)symbols.get(oldSymbol);
			if (value != null){

				int intValue=value.intValue();
				if (newSymbol == null) {
					// emptying
					symbols.remove(oldSymbol);
					matrix[intValue] = new int[1];
				} else {
					symbols.remove(oldSymbol);

					symbols.put(newSymbol, value);

					// update matrix values if need
					int newArrayLength = newSymbol instanceof IMultiLayerSymbol ?
							((IMultiLayerSymbol) newSymbol).getLayerCount() : 1;

							int[] newRow = new int[newArrayLength];
							if (matrix[intValue].length == newArrayLength) {
								/*
								 * the new row is exactly the same long than the
								 * old one. Will use the old values
								 */
								newRow = matrix[intValue];
							} else	if (matrix[intValue].length < newArrayLength) {
								/*
								 * the new row is larger than the old one,
								 * let's copy all the first values and fill
								 * the rest with the last copied value
								 */
								int val=0;
								for (int i = 0; i < newRow.length; i++) {
									if (i<matrix[intValue].length) {
										val = matrix[intValue][i];
									}

									newRow[i] = val;
								}
							} else if (matrix[intValue].length > newArrayLength) {
								/*
								 * the new row is smaller than the old one,
								 * let's copy the first values
								 */
								for (int i = 0; i < newRow.length; i++) {
									newRow[i] = matrix[intValue][i];
								}
							}
							matrix[intValue] = newRow;
				}
			}
		} else {
			addSymbol(newSymbol);
		}
	}

	public boolean symbolChanged(SymbolLegendEvent e) {
		replaceSymbol(e.getOldSymbol(), e.getNewSymbol());
		return true;
	}


	public boolean classifiedSymbolChange(SymbolLegendEvent e) {
		replaceSymbol(e.getOldSymbol(), e.getNewSymbol());
		return true;
	}

	public boolean intervalChange(IntervalLegendEvent e) {
		return false;
	}

	public boolean valueChange(ValueLegendEvent e) {
		System.out.println("log ValueLegendEvent:"+e.getOldValue()+"->"+e.getNewValue());
		return false;
	}

	// TODO should not exist here
	public boolean labelFieldChange(LabelLegendEvent e) {
		return false;
	}


	public void legendCleared(LegendClearEvent event) {
//		this.usingZSort = false;
//		symbols.clear();
//		matrix = null;
	}
}

