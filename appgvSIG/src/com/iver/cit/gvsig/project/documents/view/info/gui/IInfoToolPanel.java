package com.iver.cit.gvsig.project.documents.view.info.gui;

import org.gvsig.fmap.mapcontext.layers.operations.XMLItem;

/**
 * Interface that must implement the panels provided from a layer
 * to display its Feature Information
 * 
 * @author laura
 *
 */
public interface IInfoToolPanel{

	public void show(String s);
	public void refreshSize();
	public void show(XMLItem item);
}
