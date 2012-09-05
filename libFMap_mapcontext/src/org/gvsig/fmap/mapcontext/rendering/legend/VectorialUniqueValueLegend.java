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
package org.gvsig.fmap.mapcontext.rendering.legend;

import java.awt.Color;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.mapcontext.rendering.legend.events.LegendClearEvent;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.utiles.StringUtilities;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

/**
 * Vectorial legend for unique values
 *
 * @author   Vicente Caballero Navarro
 */
//public class VectorialUniqueValueLegend implements IVectorialUniqueValueLegend {
public class VectorialUniqueValueLegend extends AbstractClassifiedVectorLegend implements IVectorialUniqueValueLegend {
	final static private Logger logger = LoggerFactory.getLogger(VectorialUniqueValueLegend.class);
//	protected int fieldId;
//	protected DataSource dataSource;
	protected FeatureStore featureStore;
	private TreeMap symbols = new TreeMap( //<Object, ISymbol>
			new Comparator() { //<Object>
		public int compare(Object o1, Object o2) {
			if ((o1 != null) && (o2 != null)) {
				Object v2 = o2;
				Object v1 = o1;
				Boolean boolVal;

				// TODO estas dos comprobaciones son por evitar un bug en el gdbms, cuando se solucione se puede eliminar.
				if (v1 == null && v2 == null) {
					return 0;
				}

				if (v1 instanceof NullUniqueValue && v2 instanceof NullUniqueValue) {
					return 0;
				}

				if (v1 instanceof NullUniqueValue) {
					return -1;
				}

				if (v2 instanceof NullUniqueValue) {
					return 1;
				}

				if (v1 instanceof Number && v2 instanceof Number){
					return((Number)v1).intValue()-((Number)v2).intValue();
				}
				if (v1 instanceof String && v2 instanceof String){
					return ((String)v1).compareTo(((String)v2));
				}
			}

			return 0;
		}
	}); // Para poder ordenar
    private ArrayList keys = new ArrayList(); // En lugar de un HashSet, para tener acceso por índice
    private String labelFieldName;
    private String labelFieldHeight;
    private String labelFieldRotation;
    private ISymbol defaultSymbol;
    private int shapeType;
    private String valueType = NullValue.class.getName();
    private boolean useDefaultSymbol = false;
    private Color[] selectedColors=null;
	/**
     * Constructor method
     */
    public VectorialUniqueValueLegend() {
    }

    /**
     * Constructor method
     *
     * @param shapeType Type of the shape.
     */
    public VectorialUniqueValueLegend(int shapeType) {
        setShapeType(shapeType);
    }

     public void setShapeType(int shapeType) {
    	if (this.shapeType != shapeType) {
    		ISymbol old = defaultSymbol;
    		defaultSymbol = SymbologyFactory.createDefaultSymbolByShapeType(shapeType);
    		fireDefaultSymbolChangedEvent(new SymbolLegendEvent(old, defaultSymbol));
    		this.shapeType = shapeType;
    	}
    }

    public void setValueSymbolByID(int id, ISymbol symbol) {
        ISymbol old = (ISymbol)symbols.put(keys.get(id), symbol);
        fireClassifiedSymbolChangeEvent(new SymbolLegendEvent(old, symbol));
    }

//    /**
//     * Used in the table that shows the legend
//     *
//     * @deprecated use setValueSymbolByID(int id, ISymbol symbol);
//     * @param reference
//     * @param symbol
//     */
//    public void setValueSymbol(int id, ISymbol symbol) {
//    	ISymbol old = symbols.put(keys.get(id), symbol);
//        fireClassifiedSymbolChangeEvent(new SymbolLegendEvent(old, symbol));
//    }

    public Object[] getValues() {
        return symbols.keySet().toArray(new Object[0]);
    }

    public void addSymbol(Object key, ISymbol symbol) {
        ISymbol resul;
        resul = (ISymbol)symbols.put(key, symbol);

        if (resul != null) {
        	logger.error("Error: la clave " + key +
                " ya existía. Resul = " + resul);
            logger.warn("symbol nuevo:" + symbol.getDescription() +
                " Sviejo= " + resul.getDescription());
        } else {
            keys.add(key);

            if (!key.getClass().equals(NullValue.class)) {
                valueType = key.getClass().getName();
        }
        }

        fireClassifiedSymbolChangeEvent(new SymbolLegendEvent(resul, symbol));

    }

    public void clear() {
        keys.clear();
        ISymbol[] olds = (ISymbol[])symbols.values().toArray(new ISymbol[0]);
        symbols.clear();
        removeLegendListener(getZSort());
        setZSort(null);

        fireLegendClearEvent(new LegendClearEvent(olds));
    }

    public String[] getDescriptions() {
        String[] descriptions = new String[symbols.size()];
        ISymbol[] auxSym = getSymbols();

        for (int i = 0; i < descriptions.length; i++) {
			descriptions[i] = auxSym[i].getDescription();
		}

        return descriptions;
    }

     public ISymbol[] getSymbols() {
        return (ISymbol[])symbols.values().toArray(new ISymbol[0]);
    }

    public void setClassifyingFieldNames(String[] fNames) {
    	 super.setClassifyingFieldNames(fNames);
//    	 try {
//    		 fieldId = ((FeatureType)featureStore.getFeatureTypes().get(0)).getIndex(getClassifyingFieldNames()[0]);
//    	 } catch (NullPointerException e) {
//    		 logger.warn("data source not set");
//    	 } catch (DataException e) {
//    		 logger.warn("data source not set");
//		}
     }
    /*
     * @see com.iver.cit.gvsig.fmap.rendering.IVectorialLegend#getSymbol(int)
     */
//    public ISymbol getSymbol(int recordIndex) throws ReadException {
//		Object val = dataSource.getFieldValue(recordIndex, fieldId);
//		ISymbol theSymbol = getSymbolByValue(val);
//
//		return theSymbol;
//	}

    /**
	 * Devuelve un símbolo a partir de una IFeature. OJO!! Cuando usamos un
	 * feature iterator de base de datos el único campo que vendrá rellenado es
	 * el de fieldID. Los demás vendrán a nulos para ahorra tiempo de creación.
	 *
	 * @param feat
	 *            IFeature
	 *
	 * @return Símbolo.
	 */
    public ISymbol getSymbolByFeature(Feature feat) {

    	Object val = feat.get(getClassifyingFieldNames()[0]);
//        Object val = feat.get(fieldId);
        ISymbol theSymbol = getSymbolByValue(val);

        if (theSymbol != null) {
        	return theSymbol;

        }

        if (isUseDefaultSymbol()) {
			return defaultSymbol;
		}

        return null;
    }


    public ISymbol getDefaultSymbol() {

    	if(defaultSymbol==null) {
    		defaultSymbol = SymbologyFactory.createDefaultSymbolByShapeType(shapeType);
    		fireDefaultSymbolChangedEvent(new SymbolLegendEvent(null, defaultSymbol));
    	}
    	return defaultSymbol;
    }

    public XMLEntity getXMLEntity() throws XMLException {
    	XMLEntity xml = new XMLEntity();
        xml.putProperty("className", this.getClass().getName());
        xml.putProperty("fieldNames", getClassifyingFieldNames()[0]);

        if (getClassifyingFieldTypes()!=null)
        	xml.putProperty("fieldTypes", getClassifyingFieldTypes()[0]);

        if (selectedColors != null) {
			String[] strColors = new String[selectedColors.length];
			for (int i = 0; i < strColors.length; i++) {
				strColors[i] = StringUtilities.color2String(selectedColors[i]);
			}
			xml.putProperty("colorScheme", strColors);
		}

        xml.putProperty("labelfield", labelFieldName);
        xml.putProperty("labelFieldHeight", labelFieldHeight);
        xml.putProperty("labelFieldRotation", labelFieldRotation);

        xml.putProperty("useDefaultSymbol", useDefaultSymbol);
        xml.addChild(getDefaultSymbol().getXMLEntity());
        xml.putProperty("numKeys", keys.size());


		if (keys.size() > 0) {
			xml.putProperty("tipoValueKeys", valueType);

			String[] sk = new String[keys.size()];
			int[] stk = new int[keys.size()];
			Object[] values = getValues();
			String[] sv = new String[values.length];
			int[] stv = new int[values.length];

			for (int i = 0; i < keys.size(); i++) {
				Object key = keys.get(i);
				sk[i] = key.toString();
				stk[i] = getSQLType(key);
			}

			for (int i=0; i < values.length; i++){
				Object value = values[i];
				sv[i] = value.toString();
				stv[i] = getSQLType(value);
				xml.addChild(((ISymbol)symbols.get(value)).getXMLEntity());
			}

			xml.putProperty("keys", sk);
			xml.putProperty("values", sv);
			xml.putProperty("typeKeys", stk);
			xml.putProperty("typeValues", stv);
		}


        if (getZSort()!=null) {
        	XMLEntity xmlZSort = getZSort().getXMLEntity();
        	xmlZSort.putProperty("id", "zSort");
        	xml.addChild(xmlZSort);
        }
        return xml;
    }

    private int getSQLType(Object object) {
		if (object instanceof Integer){
			return Types.INTEGER;
		}else if (object instanceof Long){
			return Types.BIGINT;
		}else if (object instanceof Float){
			return Types.FLOAT;
		}else if (object instanceof Double){
			return Types.DOUBLE;
		}else if (object instanceof NullUniqueValue){
			return Types.OTHER;
		}
    	return Types.LONGVARCHAR;
	}

	public void setXMLEntity(XMLEntity xml) {
        clear();
        if (xml.contains("fieldName")) {
			setClassifyingFieldNames(new String[] {xml.getStringProperty("fieldName")});
		} else {
			setClassifyingFieldNames(xml.getStringArrayProperty("fieldNames"));
		}

        if (xml.contains("fieldTypes")) {
			setClassifyingFieldTypes(xml.getIntArrayProperty("fieldTypes"));
		}
        if (xml.contains("colorScheme")) {
			String[] strColors = xml.getStringArrayProperty("colorScheme");

        	Color[] cc = new Color[strColors.length];
			for (int i = 0; i < cc.length; i++) {
				cc[i] = StringUtilities.string2Color(strColors[i]);
			}
			setColorScheme(cc);
		}
        useDefaultSymbol = xml.getBooleanProperty("useDefaultSymbol");
        setDefaultSymbol(SymbologyFactory.createSymbolFromXML(xml.getChild(0), null));

        int numKeys = xml.getIntProperty("numKeys");

		if (numKeys > 0) {
			String className = xml.getStringProperty("tipoValueKeys");
			String[] sk = xml.getStringArrayProperty("keys");
			if(sk.length == 0){
				sk = new String[]{""};
			}
			String[] sv = xml.getStringArrayProperty("values");
			if(sv.length == 0){
				sv = new String[]{""};
			}
			Object auxValue = null;
			Object auxValue2 = null;
			ISymbol sym;
			int[] stk = null;
			if (xml.contains("typeKeys")) {
				stk = xml.getIntArrayProperty("typeKeys");
				int[] stv = xml.getIntArrayProperty("typeValues");
				for (int i = 0; i < numKeys; i++) {
					auxValue = getValue(sk[i], stk[i]);
					if ( auxValue instanceof NullValue ) {
						auxValue = new NullUniqueValue();
					}
					keys.add(auxValue);
				}

				boolean foundNullValue = false;
				for (int i = 0; i < sv.length; i++) {
					auxValue2 = getValue(sv[i], stv[i]);
					if ( auxValue2 instanceof NullValue ) {
						foundNullValue = true;
						auxValue2 = new NullUniqueValue();
						sym = getDefaultSymbol();
					} else {
						sym = SymbologyFactory.createSymbolFromXML(xml
								.getChild(i+1), null);
					}

					symbols.put(auxValue2, sym);
				}
				if (!foundNullValue && useDefaultSymbol){
					auxValue2 = new NullUniqueValue();
					sym = getDefaultSymbol();
					symbols.put(auxValue2, sym);
				}
			} else {


				for (int i = 0; i < numKeys; i++) {
					auxValue = getValue(sk[i]);
					if ( auxValue  == null ) { //Default
						auxValue = new NullUniqueValue();
					}
					keys.add(auxValue);
				}

				boolean foundNullValue = false;
				for (int i = 0; i < sv.length; i++) {
					auxValue2 = getValue(sv[i]);
					if ( auxValue2 == null ) { //Default
						foundNullValue = true;
						auxValue2 = new NullUniqueValue();
						sym = getDefaultSymbol();
					} else {
						sym = SymbologyFactory.createSymbolFromXML(xml
								.getChild(i+1), null);
					}

					symbols.put(auxValue2, sym);
				}
				if (!foundNullValue && useDefaultSymbol){
					auxValue2 = new NullUniqueValue();
					sym = getDefaultSymbol();
					symbols.put(auxValue2, sym);
				}
			}
		}



        XMLEntity zSortXML = xml.firstChild("id", "zSort");
		if (zSortXML != null) {
			ZSort zSort = new ZSort(this);
			zSort.setXMLEntity(zSortXML);
			addLegendListener(zSort);
			setZSort(zSort);
		}
    }

	public void setDefaultSymbol(ISymbol s) {
    	ISymbol mySymbol = defaultSymbol;

    	if (s == null) {
			throw new NullPointerException("Default symbol cannot be null");
		}

    	ISymbol old = mySymbol;
    	defaultSymbol = s;
    	fireDefaultSymbolChangedEvent(new SymbolLegendEvent(old, s));
    }

    /**
     * Returns the value using the its value in a string.
	 *
	 *
	 * @param s String with the value.
	 * @deprecated Method used until 1.0 alpha 855 You should use getValue(String s,int type);
	 * @return Value.
	 */
    private Object getValue(String s) {
        Object val = new NullUniqueValue();
        if (s.equals("Resto de Valores")) {
			return val;
		}
//        try {
            try {
                val = new Integer(s);//(s, Types.INTEGER);

                return val;
            } catch (NumberFormatException e) {
            }

            try {
                val = new Long(s);//ValueFactory.createValueByType(s, Types.BIGINT);

                return val;
            } catch (NumberFormatException e) {
            }

            try {
                val = new Float(s);//ValueFactory.createValueByType(s, Types.FLOAT);

                return val;
            } catch (NumberFormatException e) {
            }

            try {
                val = new Double(s);//ValueFactory.createValueByType(s, Types.DOUBLE);

                return val;
            } catch (NumberFormatException e) {
            }

            val = s;//ValueFactory.createValueByType(s, Types.LONGVARCHAR);

//        } catch (ParseException e) {
//           log.warn("parse exception", e);
//        }

        return val;
    }

    /**
     * Devuelve el valor a partir de su valor en un string.
     *
     * @param s String con el valor.
     *
     * @return Value.
     */
    private Object getValue(String s,int type) {
    	Object val = new NullUniqueValue();
    	if (type==Types.OTHER) {
			return val;
		}
    	switch (type) {
    	case Types.INTEGER:
    		val = new Integer(s);
    		break;
    	case Types.BIGINT:
    		val = new Long(s);
    		break;
    	case Types.FLOAT:
    		val = new Float(s);
    		break;
    	case Types.DOUBLE:
    		val = new Double(s);
    		break;
    	default:
    		val=s;
    	break;
    	}
    	return val;
    }

    public ILegend cloneLegend() throws XMLException {
        return LegendFactory.createFromXML(getXMLEntity());
    }


    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.fmap.rendering.VectorialLegend#setDataSource(com.hardcode.gdbms.engine.data.DataSource)
     */
    public void setFeatureStore(FeatureStore fs) throws	DataException {
		featureStore = fs;
//		if (getClassifyingFieldNames()!=null) {
//			fieldId = ((FeatureType)fs.getFeatureTypes().get(0)).getIndex(getClassifyingFieldNames()[0]);
//		}
	}

    /*
     * (non-Javadoc)
     *
     * @see com.iver.cit.gvsig.fmap.rendering.UniqueValueLegend#getSymbolByValue(com.hardcode.gdbms.engine.values.Value)
     */
    public ISymbol getSymbolByValue(Object key) {
    	ISymbol symbol = (ISymbol)symbols.get(key);
    	if (symbol!=null) {
    		return symbol;
    	} else if (useDefaultSymbol) {
    		return getDefaultSymbol();
    	}
    	return null;

    }

    public int getShapeType() {
        return shapeType;
    }

    public void useDefaultSymbol(boolean b) {
        useDefaultSymbol = b;
    }

    /**
	 * Devuelve si se utiliza o no el resto de valores para representarse.
	 * @return  True si se utiliza el resto de valores.
	 */
    public boolean isUseDefaultSymbol() {
        return useDefaultSymbol;
    }

    public void delSymbol(Object key) {
        keys.remove(key);

		ISymbol removedSymbol = (ISymbol) symbols.remove(key);
		if (removedSymbol != null){
			fireClassifiedSymbolChangeEvent(new SymbolLegendEvent(removedSymbol, null));
		}
    }

	public String getClassName() {
		return getClass().getName();
	}

	public void replace(ISymbol oldSymbol, ISymbol newSymbol) {
		if (symbols.containsValue(oldSymbol)) {
			Iterator it = symbols.keySet().iterator();
			while (it.hasNext()) {
				Object key = it.next();
				if (symbols.get(key).equals(oldSymbol)) {
					fireClassifiedSymbolChangeEvent(new SymbolLegendEvent(
							(ISymbol)symbols.put(key, newSymbol), newSymbol));
				}

			}
		}
	}
	public Color[] getColorScheme() {
		return selectedColors;
	}

	public void setColorScheme(Color[] cc) {
		 this.selectedColors = cc;
	}
}
