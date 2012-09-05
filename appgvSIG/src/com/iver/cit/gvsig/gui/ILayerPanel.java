package com.iver.cit.gvsig.gui;

import org.gvsig.fmap.mapcontext.layers.FLayer;

/**
 * @author fjp
 *
 * Interfaz que se usa por las nuevas capas que necesiten definir
 * una capa que todavía no existe en gvSIG.
 * Para usarlo, en una extensión aparter, se define un JPanel
 * que implemente este interfaz, y luego se añade al Wizard 
 * correspondiente.
 * Por ejemplo, cuando se crea una nueva capa, existe un Wizard
 * del tipo JWizardComponents en la clase NewTheme de extCAD
 * (o extEdition, como quiera que se llame al final)
 */
public interface ILayerPanel {
	/**
	 * Creates a FLyrWFS.
	 * 
	 * @return the layer
	 */
	public FLayer getLayer();
}