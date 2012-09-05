package org.gvsig.fmap.dal.feature.impl.expansionadapter;

import java.util.HashMap;
import java.util.Iterator;


/**
 * Maneja el fichero de extensión en el que se almacenan las modificacionesy adiciónes
 * durante la edición. Los índices que devuelve esta clase en sus métodos
 * addFeature y modifyFeature son invariables, es decir, que si se invoca un
 * método addFeature que retorna un 8, independientemente de las operaciones
 * que se realicen posteriormente, una llamada a getFeature(8) retornará
 * dicha fila. Si esta geometría es eliminada posteriormente, se retornará un
 * null. Esto último se cumple mientras no se invoque el método compact, mediante
 * el que se reorganizan las geometrías dejando en el fichero sólo las que tienen
 * validez en el momento de realizar la invocación.
 *
 * @author Vicente Caballero Navarro
 *
 */
public interface ExpansionAdapter {
	/**
	 * Añade una geometria al final del fichero y retorna el índice que ocupa
	 * esta geometria en el mismo
	 *
	 * @param feature
	 * @param status TODO
	 * @param indexInternalFields fields that where valid when this feature was added.
	 *
	 * @return calculated index of the new feature.
	 */
	int addObject(Object obj);

	/**
	 * Modifica la index-ésima geometría del fichero devolviendo la posición en
	 * la que se pone la geometria modificada.
	 *
	 * @param calculated index of feature to be modified
	 * @param feature newFeature that replaces the old feature.
	 *
	 * @return new calculated index of the modified feature.
	 */
//	int updateObject(Object obj);

	/**
	 * Obtiene la geometria que hay en el índice 'index' o null si la geometría
	 * ha sido invalidada.
	 *
	 * @param index caculatedIndex of the feature to be read.
	 * @return feature
	 */
	Object getObject(int index);

	/**
	 * Realiza una compactación del fichero que maneja esta clase
	 *
	 * @param relations DOCUMENT ME!
	 */
	void compact(HashMap relations);

	/**
     * Mueve el puntero de escritura de manera que las siguientes escrituras
     * machacarán la última fila
     */
    void deleteLastObject();

    /**
     * Abre el fichero de expansión para comenzar la edición
     */
    void open();

    /**
     * Cierra el fichero de expansión al terminar la edición
     */
    void close();

    int getSize();

    Iterator iterator();
}
