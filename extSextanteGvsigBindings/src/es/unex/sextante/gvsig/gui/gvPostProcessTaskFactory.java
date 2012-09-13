package es.unex.sextante.gvsig.gui;

import es.unex.sextante.core.GeoAlgorithm;
import es.unex.sextante.gui.core.IPostProcessTaskFactory;

public class gvPostProcessTaskFactory implements IPostProcessTaskFactory{

	public Runnable getPostProcessTask(GeoAlgorithm alg) {

		return new gvPostProcessTask(alg);

	}

}
