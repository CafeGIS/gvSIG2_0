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

import java.util.ArrayList;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;

/**
 * Calcula los intervalos en función del número de intervalos que se pidan.
 *
 * @author Vicente Caballero Navarro
 */
public class QuantileIntervalGenerator {
//	private DataSource sds;
	private FeatureStore featureStore;
	private String msFieldName;
	private int miNumIntervalosSolicitados;
	private double[] mdaValoresRuptura;
	private double[] mdaValInit;
	private int num = 0;

	/**
	 * Crea un nuevo QuantileIntervalGenerator.
	 *
	 * @param layer DOCUMENT ME!
	 * @param field DOCUMENT ME!
	 * @param numIntervals DOCUMENT ME!
	 */
	public QuantileIntervalGenerator(FeatureStore fs, String field,
		int numIntervals) {
		featureStore = fs;
		msFieldName = field;
		miNumIntervalosSolicitados = numIntervals;
	}

	/**
	 * Genera los intervalos.
	 * @throws DataException TODO
	 *
	 */
	public void generarIntervalos()
		throws DataException {
		ArrayList ordenadas = new ArrayList();
		ArrayList coincidencias = new ArrayList();
		int pos = ((FeatureType)featureStore.getFeatureTypes().get(0)).getIndex(msFieldName);
		mdaValoresRuptura = new double[miNumIntervalosSolicitados - 1];
		mdaValInit = new double[miNumIntervalosSolicitados - 1];
		FeatureQuery featureQuery=featureStore.createFeatureQuery();
		featureQuery.setAttributeNames(new String[]{msFieldName});
		FeatureSet set = null;
		DisposableIterator iterator = null;
		try {
			set = featureStore.getFeatureSet(featureQuery);
			iterator = set.fastIterator();
			long rowCount = 0;
			while (iterator.hasNext()) {
				Feature feature = (Feature) iterator.next();
				insertarEnVector(ordenadas, coincidencias, feature.get(0));// sds.getFieldValue(i,
																			// pos));
				rowCount++;
			}
			// int MARGEN = 5;
			// for (int i = 0; i < sds.getRowCount(); i++) {
			// insertarEnVector(ordenadas, coincidencias, sds.getFieldValue(i,
			// pos));
			// }

			int index = 0;
			int posj = 0;

			for (int i = 1; i < miNumIntervalosSolicitados; i++) {
				long x = ((i * rowCount) / miNumIntervalosSolicitados);

				for (int j = posj; j < ordenadas.size(); j++) {
					int auxcoin = ((Integer) coincidencias.get(j)).intValue();
					index = index + auxcoin;

					if (x <= index) {
						mdaValoresRuptura[i - 1] = getValue(ordenadas.get(j));

						/*
						 * index = (int) ((x + (auxcoin /
						 * miNumIntervalosSolicitados)) - 1);
						 */
						posj = j + 1;

						if (posj < ordenadas.size()) {
							mdaValInit[i - 1] = getValue(ordenadas.get(posj));
						} else {
							mdaValInit[i - 1] = getValue(ordenadas.get(j));
						}

						num++;

						break;
					}
				}

				// double value=getValue(sds.getFieldValue(x,pos));
			}

			// }
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
	 * Esta función busca en el vector de datos la posición que le corresponde
	 * al valor almacenado en vdValor y devuelve dicha posición en
	 * vdValorAInsertar. Para hallar la posición se realiza una búsqueda
	 * binaria. Si se trata de un elemento que ya está en el vector devolvemos
	 * el índice que le corresponde en rlIndiceCorrespondiente y false en
	 * rbNuevoElemento. Si se trata de un nuevo elemento que hay que
	 * insertar... devolvemos el índice en el que iría y True en
	 * rbNuevoElemento En caso de que ocurra algún error devuelve false
	 *
	 * @param rVectorDatos ArrayList con los datos.
	 * @param coincidencia índice.
	 * @param vdValorAInsertar Valor a insertar.
	 */
	private void insertarEnVector(ArrayList rVectorDatos,
		ArrayList coincidencia, Object vdValorAInsertar) {
		int llIndiceIzq;
		int llIndiceDer;
		int llMedio;
		int indice = -1;
		double ldValorComparacion;
		double valorAInsertar = getValue(vdValorAInsertar);

		if (rVectorDatos.size() == 0) {
			rVectorDatos.add(vdValorAInsertar);
			coincidencia.add(new Integer(1));

			return;
		}

		llIndiceIzq = 0;
		llIndiceDer = rVectorDatos.size() - 1;
		llMedio = (llIndiceIzq + llIndiceDer) / 2; //'División entera!

		while (llIndiceIzq <= llIndiceDer) {
			//'Coger el valor situado en la mitad de la zona de búsqueda como valor de comparación
			ldValorComparacion = getValue( rVectorDatos.get(llMedio));

			//'Si el valor a insertar es mayor que el valor de comparación...
			if (valorAInsertar > ldValorComparacion) {
				//      'La zona de búsqueda queda restringida a la parte de la derecha
				llIndiceIzq = llMedio + 1;
				llMedio = (llIndiceIzq + llIndiceDer) / 2;

				//    'Si el valor a insertar es menor que el valor de comparación...
			} else if (valorAInsertar < ldValorComparacion) {
				//          'La zona de búsqueda queda restringida a la parte de la derecha
				llIndiceDer = llMedio - 1;
				llMedio = (llIndiceIzq + llIndiceDer) / 2;

				//        'Si el valor de comparación coincide con el valor a insertar
			} else if (valorAInsertar == ldValorComparacion) {
				indice = llMedio;

				int index = rVectorDatos.indexOf(vdValorAInsertar);
				int coin = ((Integer) coincidencia.get(index)).intValue() + 1;
				coincidencia.remove(index);
				coincidencia.add(index, new Integer(coin));

				return;
			}
		}

		//  'Nota:
		//  'En este caso (cuando en rbNuevoElemento se devuelve True) lo que hay que hacer al salir de esta función
		//  'es añadir un nuevo elemento al vector y desplazar todos los valores correspondientes a partir de rlIndiceCorrespondiente
		//  '¿Dónde va el nuevo elemento?
		//  'El último sitio estudiado viene dado por el valor de llMedio.
		//  'Si el valor a insertar es menor que el valor almacenado en la posición llMedio, el nuevo valor deberá ir a su izquierda.
		//  'Si fuera mayor debería ir a su derecha.
		ldValorComparacion = getValue( rVectorDatos.get(llMedio));

		if (valorAInsertar > ldValorComparacion) {
			indice = llMedio + 1;
		} else {
			indice = llMedio;
		}

		rVectorDatos.add(indice, vdValorAInsertar);
		coincidencia.add(indice, new Integer(1));
	}

	/**
	 * Devuelve el valor en un double del Value que se pasa como parámetro.
	 *
	 * @param value Value.
	 *
	 * @return valor.
	 */
	private double getValue(Object value) {
		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}
		return 0;

	}

	/**
	 * Devuelve el valor del punto de ruptura según el índice que se pasa como
	 * parámetro.
	 *
	 * @param index índice del punto de ruptura.
	 *
	 * @return valor.
	 */
	public double getValRuptura(int index) {
		return mdaValoresRuptura[index];
	}

	/**
	 * Devuelve el valor inicial de cada intervalo.
	 *
	 * @param index índice del intervalo.
	 *
	 * @return valor del intervalo.
	 */
	public double getValInit(int index) {
		return mdaValInit[index];
	}

	/**
	 * Devuelve el número de intervalos que se han generado.
	 *
	 * @return Número de intervalos generados.
	 */
	public int getNumIntervalGen() {
		return num + 1;
	}
}
