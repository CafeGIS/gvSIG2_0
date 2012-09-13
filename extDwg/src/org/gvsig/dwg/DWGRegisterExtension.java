/**
 *
 */
package org.gvsig.dwg;

import org.gvsig.dwg.fmap.dal.store.dwg.DWGLibrary;
import org.gvsig.dwg.fmap.dal.store.dwg.legend.DWGLegendLibrary;

import com.iver.andami.plugins.Extension;

/**
 * @author paco
 *
 */
public class DWGRegisterExtension extends Extension {
	private DWGLibrary dwg;
	private DWGLegendLibrary dwgLegend;

	/* (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String actionCommand) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
		dwg = new DWGLibrary();
		dwg.initialize();

		dwgLegend = new DWGLegendLibrary();
		dwgLegend.initialize();
	}


	public void postInitialize() {
		super.postInitialize();

		dwg.postInitialize();

		dwgLegend.postInitialize();
	}


	/* (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}


}
