/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 */

/*
 * AUTHORS (In addition to CIT):
 * 2009 Prodevelop S.L  vsanjaime   programador
 */

package org.gvsig.geocoding.utils;

import java.io.File;
import java.util.List;

import org.gvsig.fmap.dal.feature.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class generates a Layer from feature store 
 * 
 * @author Jorge Gaspar Sanz Salinas (jsanz@prodevelop.es)
 * @author Vicente Sanjaime Calvet (vsanjaime@prodevelop.es)
 * 
 */

public class LayerUpThread extends Thread {

	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(LayerUpThread.class);
	private File file = null;
	private boolean apto = true;

	/**
	 * Constructor
	 */
	public LayerUpThread() {
		super();
	}

	/**
	 * Constructor with file
	 * 
	 * @param file
	 */
	public LayerUpThread(File file) {
		this();
		this.file = file;
	}

	/**
	 * Run
	 */
	public void run() {
//		//name
//		String name = this.file.getName();
//		//layerdefinition
//		LayerDefinition def = new LayerDefinition();
//		def.setShapeType(FShape.POINT);
//		def.setProjection(GeocoUtils.getCurrentView().getProjection());
//		//Features
//		List<IFeature> features = createFeatures();
//		////Driver
//		VectorialDriver driver = new FeatureCollectionMemoryDriver(name,
//				features, def);
//		FLyrVect lyr = new FLyrVect();

		
		
	}

	/**
	 * @return the apto
	 */
	public boolean isApto() {
		return apto;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * 
	 * @return
	 */
	private List<Feature> createFeatures() {

		return null;
	}

}
