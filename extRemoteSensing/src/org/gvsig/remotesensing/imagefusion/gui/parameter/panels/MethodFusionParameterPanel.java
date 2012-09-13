package org.gvsig.remotesensing.imagefusion.gui.parameter.panels;

import java.util.HashMap;

import javax.swing.JPanel;


/**
 * Clase padre de todas las clases de paneles de parametros para los 
 * métodos de fusión.
 * 
 * @version 27/02/2008
 * @author Alejandro Muñoz Sánchez (alejandro.munoz@uclm.es)
 * 
 */
public abstract class MethodFusionParameterPanel {
	
	protected String idPanel = "";
	
	protected HashMap params=new HashMap();
	
	public abstract JPanel getParameterPanel();
	
	// Devolucion de los parametros del panel
	public  abstract HashMap getParams();
	
	// Asignacion de los parametros del panel
	public abstract void setParams();
	
	// Identificador del panel
	public abstract String getIDPanel();
	
}

