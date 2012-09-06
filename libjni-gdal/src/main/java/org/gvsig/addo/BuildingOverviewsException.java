package org.gvsig.addo;

/**
 * Es generada cuando hay algun problema en la construccion de 
 * las overviews y la funcion nativa devuelve 0
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com).<BR> Equipo de desarrollo gvSIG.<BR> http://www.gvsig.gva.es
 * @version 0.0
 * @link http://www.gvsig.org
 */


public class BuildingOverviewsException extends Exception {

	public BuildingOverviewsException(String msg){
		super(msg);
	}
	
	
}