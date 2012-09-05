package com.iver.cit.gvsig.project.documents.layout.tools.listener;

import com.iver.cit.gvsig.project.documents.layout.geometryadapters.GeometryAdapter;

/**
 * Iterfaz de los listeners con gr�ficos.
 *
 * @author Vicente Caballero Navarro
 */
public interface ILayoutGraphicListener {
	public void endGraphic();

	public GeometryAdapter createGeometryAdapter();
}
