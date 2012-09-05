package com.iver.cit.gvsig.project.documents;


/**
 * Interface que deben de cumplir una acci�n aplicable dede un
 * Menu contextual o popup.
 * <br>
 * Las acciones se deben registrar en el punto de extensi�n adecuado * 
 * <br><br>
 * Por lo general extender de la clase AbstractDocumentAction
 * 
 *  
 * 
 * @author Jose Manuel Viv� (Chema)
 */

public interface IContextMenuAction {
	
	/**
	 * Dice si la acci�n es visible 
	 * segun los documentos seleccionados
	 * <br>
	 * @param item elemento sobre el que se ha pulsado
	 * @param selectedItems elementos seleccionados en el momento de pulsar
	 * 
	 */
	public boolean isVisible(Object item, Object[] selectedItems);
	
	/**
	 * Dice si la acci�n esta habilitada 
	 * segun los documentos seleccionados
	 * <br>
	 * @param item elemento sobre el que se ha pulsado
	 * @param selectedItems elementos seleccionados en el momento de pulsar
	 * 
	 */
	public boolean isEnabled(Object item, Object[] selectedItems);
	
	/**
	 * Ejecuta la acci�n sobre los documentos seleccionados
	 * <br>
	 * @param item elemento sobre el que se ha pulsado
	 * @param selectedItems elementos seleccionados en el momento de pulsar
	 * 
	 */
	public void execute(Object item, Object[] selectedItems);
	
	/**
	 * Nombre del grupo al que pertenece la accion
	 */
	public String getGroup();
	
	/**
	 * Orden del grupo al que pertenece la acci�n
	 */
	public int getGroupOrder();
	
	/**
	 * Orden del elemento dentro del grupo
	 */
	public int getOrder();
	
	/**
	 * Texto del elemento
	 */
	public String getText();
	
	/**
	 * Descripci�n mas detallada de la acci�n
	 * (se utilizar� como Tooltip)
	 */
	public String getDescription();	
	
}
