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

/* CVS MESSAGES:
*
* $Id: AbstractIntervalLegend.java 21144 2008-06-03 15:47:31Z vcaballero $
* $Log$
* Revision 1.12  2007-09-19 16:25:39  jaume
* ReadExpansionFileException removed from this context and removed unnecessary imports
*
* Revision 1.11  2007/09/10 15:47:11  jaume
* *** empty log message ***
*
* Revision 1.10  2007/08/03 09:22:08  jaume
* refactored class names
*
* Revision 1.9  2007/08/02 10:19:03  jvidal
* implements IPersistance
*
* Revision 1.8  2007/08/01 11:45:59  jaume
* passing general tests (drawing test yet missing)
*
* Revision 1.7  2007/05/28 15:36:42  jaume
* *** empty log message ***
*
* Revision 1.6  2007/05/17 09:32:06  jaume
* *** empty log message ***
*
* Revision 1.5  2007/05/10 14:13:36  jaume
* *** empty log message ***
*
* Revision 1.4  2007/05/10 09:44:08  jaume
* Refactored legend interface names
*
* Revision 1.3  2007/03/27 09:28:40  jaume
* *** empty log message ***
*
* Revision 1.2  2007/03/09 11:20:56  jaume
* Advanced symbology (start committing)
*
* Revision 1.1.2.2  2007/02/15 16:23:44  jaume
* *** empty log message ***
*
* Revision 1.1.2.1  2007/02/13 16:19:19  jaume
* graduated symbol legends (start commiting)
*
*
*/
package org.gvsig.fmap.mapcontext.rendering.legend;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeMap;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontext.Messages;
import org.gvsig.fmap.mapcontext.exceptions.LegendLayerException;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractIntervalLegend extends AbstractClassifiedVectorLegend implements IVectorialIntervalLegend{
	protected int shapeType;

	public static final int EQUAL_INTERVALS = 0;
	public static final int NATURAL_INTERVALS = 1;
	public static final int QUANTILE_INTERVALS = 2;
	protected TreeMap symbols = new TreeMap //<Object, ISymbol>
			(new Comparator() { //<Object>
		public int compare(Object o1, Object o2) {
			if ((o1 != null) && (o2 != null)) {
				if (o1 instanceof NullIntervalValue &&
						o2 instanceof NullIntervalValue) {
					return 0;
				}

				if (o2 instanceof NullIntervalValue) {
					return 1;
				}

				if (o1 instanceof NullIntervalValue) {
					return -1;
				}

				FInterval i2 = (FInterval) o2;
				FInterval i1 = (FInterval) o1;

				if (i1.getMin() > i2.getMin()) {
					return 1;
				}

				if (i1.getMin() < i2.getMin()) {
					return -1;
				}
				if (i1.getMax() < i2.getMax()) {
					return -1;
				}
				if (i1.getMax() > i2.getMax()) {
					return 1;
				}
			}

			return 0;
		}
	});
	protected ArrayList keys = new ArrayList(); //<Object>
	protected int index = 0;
	protected String[] fieldNames;
//	protected int fieldId;
	protected ISymbol defaultSymbol;
	protected FeatureStore featureStore;
	protected int intervalType = NATURAL_INTERVALS;
	protected boolean useDefaultSymbol = false;

	final static private Logger logger = LoggerFactory.getLogger(AbstractIntervalLegend.class);
	public void addSymbol(Object key, ISymbol symbol) {
		symbols.put(key, symbol);
		keys.add(key);
		fireClassifiedSymbolChangeEvent(new SymbolLegendEvent(null, symbol));
	}

	/*
	 * @see com.iver.cit.gvsig.fmap.rendering.IVectorialLegend#getSymbol(int)
	 */
	public ISymbol getSymbol(FeatureReference featureID) throws DataException {
		Object val = featureStore.getFeatureByReference(featureID).get(getClassifyingFieldNames()[0]);
		IInterval interval = getInterval(val);
		ISymbol theSymbol = getSymbolByInterval(interval);


		if (theSymbol != null) {
			return theSymbol;
		} else if (useDefaultSymbol) {
			return getDefaultSymbol();
		}

		return null;
	}

	public ISymbol getSymbolByFeature(Feature feat) {
		Object val = feat.get(getClassifyingFieldNames()[0]);
		IInterval interval = getInterval(val);
		ISymbol theSymbol = getSymbolByInterval(interval);

		return theSymbol;
	}


	public FInterval[] calculateIntervals(FeatureStore featureStore, String fieldName, int numIntervalos, int shapeType)
	throws LegendLayerException, DataException {
		logger.debug("elRs.start()");
//		recordSet.start();

//		int idField = -1;
		FeatureSet set = null;
		DisposableIterator iterator = null;
		try {

		setClassifyingFieldNames(new String[] {fieldName});

		String[] fieldNames = getClassifyingFieldNames();
		FeatureQuery featureQuery=featureStore.createFeatureQuery();
		featureQuery.setAttributeNames(fieldNames);
		set = featureStore.getFeatureSet(featureQuery);
			iterator = set.fastIterator();


//		for (int i = 0; i < recordSet.getFieldCount(); i++) {
//			String nomAux = recordSet.getFieldName(i).trim();
//
//			if (fieldNames[0].compareToIgnoreCase(nomAux) == 0) {
//				idField = i;
//
//				break;
//			}
//		}

//		if (idField == -1) {
//			logger.error("Campo no reconocido " + fieldNames);
//
//			return null;
//		}

		double minValue = Double.MAX_VALUE;
		double maxValue = Double.NEGATIVE_INFINITY;

		VectorialIntervalLegend auxLegend = LegendFactory.
		    createVectorialIntervalLegend(shapeType);

		Object clave;

		while (iterator.hasNext()) {
			Feature feature = (Feature) iterator.next();

//		for (int j = 0; j < recordSet.getRowCount(); j++) {
			clave = feature.get(fieldName);

			IInterval interval = auxLegend.getInterval(clave);

			////Comprobar que no esta repetido y no hace falta introducir en el hashtable el campo junto con el simbolo.
			if (auxLegend.getSymbolByInterval(interval) == null) {
				//si no esta creado el simbolo se crea
				double valor = 0;


				if (clave instanceof Number) {
					valor=((Number)clave).doubleValue();
				}else if (clave instanceof Date) {
					//TODO POR IMPLEMENTAR
					///valorDate = elRs.getFieldValueAsDate(idField);
					///if (valorDate.before(minValueDate)) minValueDate = valorDate;
					///if (valorDate.after(maxValueDate)) maxValueDate = valorDate;
				} else if (clave instanceof NullValue) {
					continue;
				}

				if (valor < minValue) {
					minValue = valor;
				}

				if (valor > maxValue) {
					maxValue = valor;
				}
			}
		}

		FInterval[] intervalArray = null;
		switch (getIntervalType()) {
		case VectorialIntervalLegend.EQUAL_INTERVALS:
			intervalArray = calculateEqualIntervals(numIntervalos,
					minValue, maxValue, fieldName);

			break;

		case VectorialIntervalLegend.NATURAL_INTERVALS:
			intervalArray = calculateNaturalIntervals(featureStore, numIntervalos,
					minValue, maxValue, fieldName);

			break;

		case VectorialIntervalLegend.QUANTILE_INTERVALS:
			intervalArray = calculateQuantileIntervals(featureStore, numIntervalos,
					minValue, maxValue, fieldName);

			break;
		}
//		recordSet.stop();
		return intervalArray;
		} finally {
			if (iterator != null) {
				iterator.dispose();
			}
			if (set != null) {
				set.dispose();
			}
		}
	}


    /**
     * EQUAL INTERVAL Devuelve un Array con el número de intervalos que se
     * quieren crear. Los intervalos se crean con un tamaño igual entre ellos.
     * @param numIntervals número de intervalos
     * @param minValue Valor mínimo.
     * @param maxValue Valor máximo.
     * @param fieldName Nombre del campo
     *
     * @return Array con los intervalos.
     */
    private FInterval[] calculateEqualIntervals(int numIntervals, double minValue,
        double maxValue, String fieldName) {
        FInterval[] theIntervalArray = new FInterval[numIntervals];
        double step = (maxValue - minValue) / numIntervals;

        if (numIntervals > 1) {
            theIntervalArray[0] = new FInterval(minValue, minValue + step);

            for (int i = 1; i < (numIntervals - 1); i++) {
                theIntervalArray[i] = new FInterval(minValue + (i * step) +
                        0.01, minValue + ((i + 1) * step));
            }

            theIntervalArray[numIntervals - 1] = new FInterval(minValue +
                    ((numIntervals - 1) * step) + 0.01, maxValue);
        } else {
            theIntervalArray[0] = new FInterval(minValue, maxValue);
        }

        return theIntervalArray;
    }

    /**
     * NATURAL INTERVAL Devuelve un Array con el número de intervalos que se
     * quieren crear. Los intervalos se distribuyen de forma natural.
     *
     * @param numIntervals número de intervalos
     * @param minValue Valor mínimo.
     * @param maxValue Valor máximo.
     * @param fieldName Nombre del campo
     *
     * @return Array con los intervalos.
     * @throws LegendLayerException
     */
    private FInterval[] calculateNaturalIntervals(FeatureStore featureStore, int numIntervals, double minValue,
        double maxValue, String fieldName) throws LegendLayerException {
        NaturalIntervalGenerator intervalGenerator = new NaturalIntervalGenerator(
        		featureStore, fieldName, numIntervals);

        try {
            intervalGenerator.generarIntervalos();
        } catch (DataException e) {
        	throw new LegendLayerException(Messages.getString("failed_calculating_intervals"), e);
        }

        int numIntervalsGen = intervalGenerator.getNumIntervals() - 1;

        if (numIntervalsGen == -1) {
            //TODO cuando no puede calcular los intervalos.
            numIntervalsGen = 1;
        }

        FInterval[] theIntervalArray = new FInterval[numIntervalsGen];

        if (numIntervalsGen > 1) {
            theIntervalArray[0] = new FInterval(minValue,
                    intervalGenerator.getValorRuptura(0));

            for (int i = 1; i < (numIntervalsGen - 1); i++) {
                theIntervalArray[i] = new FInterval(intervalGenerator.getValInit(i -
                            1), intervalGenerator.getValorRuptura(i));
            }

            theIntervalArray[numIntervalsGen - 1] = new FInterval(intervalGenerator.getValInit(numIntervalsGen -
                        2), maxValue);
        } else {
            theIntervalArray[numIntervalsGen - 1] = new FInterval(minValue,
                    maxValue);
        }

        return theIntervalArray;
    }

    /**
     * QUANTILE INTERVAL Devuelve un Array con el número de intervalos que se
     * quieren crear. Los intervalos se distribuyen de forma quantile.
     * @param recordSet
     *
     * @param numIntervals número de intervalos
     * @param minValue Valor mínimo.
     * @param maxValue Valor máximo.
     * @param fieldName Nombre del campo
     *
     * @return Array con los intervalos.
     * @throws LegendLayerException
     */
    private FInterval[] calculateQuantileIntervals(FeatureStore featureStore, int numIntervals, double minValue,
        double maxValue, String fieldName) throws LegendLayerException {
        QuantileIntervalGenerator intervalGenerator = new QuantileIntervalGenerator(
        		featureStore, fieldName, numIntervals);

        try {
            intervalGenerator.generarIntervalos();
        } catch (DataException e) {
        	throw new LegendLayerException(Messages.getString("failed_calculating_intervals"), e);
        }

        int numIntervalsGen = intervalGenerator.getNumIntervalGen();
        FInterval[] theIntervalArray = new FInterval[numIntervalsGen];

        if (intervalGenerator.getNumIntervalGen() > 1) {
            theIntervalArray[0] = new FInterval(minValue,
                    intervalGenerator.getValRuptura(0));

            for (int i = 1; i < (numIntervalsGen - 1); i++) {
                theIntervalArray[i] = new FInterval(intervalGenerator.getValInit(i -
                            1), intervalGenerator.getValRuptura(i));
            }

            theIntervalArray[numIntervalsGen - 1] = new FInterval(intervalGenerator.getValInit(numIntervalsGen -
                        2), maxValue);
        } else {
            theIntervalArray[numIntervalsGen - 1] = new FInterval(minValue,
                    maxValue);
        }

        return theIntervalArray;
    }

    public ISymbol getSymbolByInterval(IInterval key) {

		if (key == null){
			if (isUseDefaultSymbol()) {
				return defaultSymbol;
			}
			return null;
		}
		if (symbols.containsKey(key)) {
			return (ISymbol) symbols.get(key);
		}

		if (isUseDefaultSymbol()) {
			return defaultSymbol;
		}

		return null;
	}


	public String[] getDescriptions() {
		String[] descriptions = new String[symbols.size()];
		ISymbol[] auxSym = getSymbols();

		for (int i = 0; i < descriptions.length; i++) {
			descriptions[i] = auxSym[i].getDescription();
		}

		return descriptions;
	}


	public Object[] getValues() {
		return symbols.keySet().toArray();
	}

	public void clear() {
		index = 0;
		keys.clear();
		symbols.clear();
	}

	public ISymbol[] getSymbols() {
		return (ISymbol[]) symbols.values().toArray(new ISymbol[0]);
	}

	public String[] getClassifyingFieldNames() {
		return fieldNames;
	}

	public void setDefaultSymbol(ISymbol s) {
	    ISymbol old = defaultSymbol;
		if (s == null) {
			throw new NullPointerException("Default symbol cannot be null");
		}
		defaultSymbol = s;
		fireDefaultSymbolChangedEvent(new SymbolLegendEvent(old, defaultSymbol));
	}

	public void setClassifyingFieldNames(String[] fieldNames) {
		this.fieldNames = fieldNames;
	}

	public ISymbol getDefaultSymbol() {
		NullIntervalValue niv=new NullIntervalValue();
		if (symbols.containsKey(niv)) {
			return (ISymbol)symbols.get(niv);
		}

		if(defaultSymbol==null) {
			defaultSymbol=SymbologyFactory.createDefaultSymbolByShapeType(shapeType);
		}
		return defaultSymbol;
	}



    public void setFeatureStore(FeatureStore featureStore)
        throws DataException {
    		/*
    		 * when we move definitely to feature iterators this
    		 * method
    		 */
    		this.featureStore = featureStore;
//    		fieldId = ((FeatureType)featureStore.getFeatureTypes().get(0)).getIndex(fieldNames[0]);
    }


	public IInterval getInterval(Object v) {
		for (int i = 0; i < keys.size(); i++) {
			if (((IInterval) keys.get(i)).isInInterval(v)) {
				return (IInterval) keys.get(i);
			}
		}

		return null;
	}

	public void setIntervalType(int intervalType) {
		this.intervalType = intervalType;
	}


	public int getIntervalType() {
		return intervalType;
	}

	public void useDefaultSymbol(boolean b) {
		useDefaultSymbol = b;
	}


	public boolean isUseDefaultSymbol() {
		return useDefaultSymbol;
	}


	public void delSymbol(Object obj) {
		keys.remove(obj);
		symbols.remove(obj);
		fireClassifiedSymbolChangeEvent(
				new SymbolLegendEvent(
						(ISymbol)symbols.remove(obj),
						null));
	}


	public void replace(ISymbol oldSymbol, ISymbol newSymbol) {
		if (symbols.containsValue(oldSymbol)) {
			Iterator it = symbols.keySet().iterator();
			while (it.hasNext()) {
				Object key = it.next();
				if (symbols.get(key).equals(oldSymbol)) {
					symbols.remove(key);
					symbols.put(key, newSymbol);
					fireClassifiedSymbolChangeEvent(
							new SymbolLegendEvent(oldSymbol, newSymbol));
				}
			}
		}
	}
}
