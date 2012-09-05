package org.gvsig.fmap.dal.feature.impl.expansionadapter;

import java.util.HashMap;
import java.util.Iterator;


/**
 * Maneja el fichero de extensi�n en el que se almacenan las modificacionesy adici�nes
 * durante la edici�n. Los �ndices que devuelve esta clase en sus m�todos
 * addFeature y modifyFeature son invariables, es decir, que si se invoca un
 * m�todo addFeature que retorna un 8, independientemente de las operaciones
 * que se realicen posteriormente, una llamada a getFeature(8) retornar�
 * dicha fila. Si esta geometr�a es eliminada posteriormente, se retornar� un
 * null. Esto �ltimo se cumple mientras no se invoque el m�todo compact, mediante
 * el que se reorganizan las geometr�as dejando en el fichero s�lo las que tienen
 * validez en el momento de realizar la invocaci�n.
 *
 * @author Vicente Caballero Navarro
 *
 */
public interface ExpansionAdapter {
	/**
	 * A�ade una geometria al final del fichero y retorna el �ndice que ocupa
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
	 * Modifica la index-�sima geometr�a del fichero devolviendo la posici�n en
	 * la que se pone la geometria modificada.
	 *
	 * @param calculated index of feature to be modified
	 * @param feature newFeature that replaces the old feature.
	 *
	 * @return new calculated index of the modified feature.
	 */
//	int updateObject(Object obj);

	/**
	 * Obtiene la geometria que hay en el �ndice 'index' o null si la geometr�a
	 * ha sido invalidada.
	 *
	 * @param index caculatedIndex of the feature to be read.
	 * @return feature
	 */
	Object getObject(int index);

	/**
	 * Realiza una compactaci�n del fichero que maneja esta clase
	 *
	 * @param relations DOCUMENT ME!
	 */
	void compact(HashMap relations);

	/**
     * Mueve el puntero de escritura de manera que las siguientes escrituras
     * machacar�n la �ltima fila
     */
    void deleteLastObject();

    /**
     * Abre el fichero de expansi�n para comenzar la edici�n
     */
    void open();

    /**
     * Cierra el fichero de expansi�n al terminar la edici�n
     */
    void close();

    int getSize();

    Iterator iterator();
}
