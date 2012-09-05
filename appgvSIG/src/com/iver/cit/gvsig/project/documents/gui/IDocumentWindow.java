/**
 * 
 */
package com.iver.cit.gvsig.project.documents.gui;

/**
 * @author Miguel �ngel Querol Carratal� <querol_mig@gva.es>
 *
 * Interfaz que tienen que implementar las clases de interfaz de 
 * usuario de los documentos que se quieran insertar en gvSIG. Estas
 * clases se usar�n para cargar y guardar las propiedades graficas de la ventana
 * del documento como tama�os y posiciones.
 */
public interface IDocumentWindow {
	
	/**
	 * M�todo para obtener un windowData con las propiedades de la
	 * ventana del documento como pueden ser tama�os, posiciones y
	 * estados de sliders, divisores etc. Este m�todo ser� llamado
	 * para guardar los datos a disco. Lo llamar� la clase project para 
	 * obtener los datos y asi guardarlos.
	 * @return un windowData con los datos de la ventana.
	 */
	public WindowData getWindowData() ;
	
	/**
	 * M�todo para cargar los datos de la ventana de proyecto. El widowData guardado
	 * se le pasa a la clase de interfaz de usuario correspondiente a la ventana
	 * del documento.
	 * @param winData
	 */
	public void setWindowData(WindowData winData);
	
}
