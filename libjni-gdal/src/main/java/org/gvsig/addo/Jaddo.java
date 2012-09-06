package org.gvsig.addo;
import java.io.File;

import es.gva.cit.jgdal.JNIBase;

/**
 * Clase para la construccion de overviews de un raster.
 *
 * 15-nov-2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class Jaddo extends JNIBase implements IOverviewIncrement{
	public final static int NEAREST = 0;
	public final static int AVERAGE = 1;
	public final static int AVERAGE_MP = 2;
	public final static int AVERAGE_MAGPHASE = 3;
	public final static int MODE = 4;

	private IOverviewIncrement  incrementListener = null;
	private int value = 0;

	private native int buildOverviewsNative(int resamplingAlg, String file, int[] overviews);

	/**
	 * Construccion de overviews
	 */
	public void buildOverviews(int resamplingAlg, String file, int[] overviews)
		throws BuildingOverviewsException, WritingException {
		File f = new File(file);
		if(!f.exists() || !f.isFile())
			throw new BuildingOverviewsException("File does not exist.");
		if(!f.canWrite())
			throw new WritingException("File is not writeable");
		if(buildOverviewsNative(resamplingAlg, file, overviews) >= 0)
			throw new BuildingOverviewsException("Problems building overviews");
	}

	/**
	 * Devuelve el porcentaje del incremento.
	 * @return int
	 */
	public int getPercent() {
		return value;
	}

	/**
	 * Asigna el porcentaje de incremento de la construccion de overview.
	 * Esto se hace automaticamente desde el callback que asigna el porcentaje.
	 */
	public void setPercent(int value) {
		this.value = value;
		if(incrementListener != null)
			incrementListener.setPercent(value);
	}

	/**
	 * Asigna el listener para la asignacion del incremento
	 * @param incr IOverviewIncrement
	 */
	public void setIncrementListener(IOverviewIncrement incr) {
		this.incrementListener = incr;
	}
}
