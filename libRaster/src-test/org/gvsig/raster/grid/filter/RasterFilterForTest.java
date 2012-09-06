package org.gvsig.raster.grid.filter;

import org.gvsig.raster.dataset.Params;

/**
 * Filtro para de prueba para el test TestRasterFilterList
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */

public class RasterFilterForTest extends RasterFilter {
	public static String[] names = new String[] {"test"};

	public RasterFilterForTest() {
		setName(names[0]);
	}

	public String getGroup() {
		return null;
	}

	public int getInRasterDataType() {
		return 0;
	}

	public String[] getNames() {
		return names;
	}

	public int getOutRasterDataType() {
		return 0;
	}

	public Object getResult(String name) {
		return null;
	}

	public Params getUIParams(String nameFilter) {
		Params params = new Params();
		return params;
	}

	public void post() {
	}

	public void pre() {
	}

	public void process(int x, int y) {
	}

	public boolean isVisible() {
		return false;
	}
}
