/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 */
package org.gvsig.raster.grid.filter.enhancement;

import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.FileNotOpenException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.dataset.properties.DatasetListStatistics;
import org.gvsig.raster.grid.filter.RasterFilter;
/**
 * Clase base para los filtros de realzado lineal. Lee el mínimo y máxmo de la
 * clase Statistic que serán calculados por PercentTailTrimFilter o
 * ComputeMinMaxFilter dependiendo de si está activado el recorte de colas o no.
 * En Statistic también están los segundos valores después del mínimo y máximo
 * que son los que se utilizan con la opción eliminar extremos activada. Estos
 * se usaran en vez del mínimo y máximo cuando la variable removeExtrema esté a
 * true.
 *
 * @version 31/05/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class LinearEnhancementFilter extends RasterFilter {
	protected double[]                 scale = new double[3];
	protected double[]                 offset = new double[3];
	protected DatasetListStatistics	   stats = null;
	protected double[]                 minBandValue	= null;
	protected double[]                 maxBandValue	= null;
	protected boolean                  removeEnds = false;
	protected double                   tailTrim	= 0D;
	protected int                      nbands = 3;
	protected int[]                    renderBands = null;
	public static String[]             names = new String[] {"enhanced"};

	/**
	 * Construye un LinearEnhancementFilter
	 */
	public LinearEnhancementFilter() {
		setName(names[0]);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#pre()
	 */
	public void pre() {
		raster = (IBuffer) params.get("raster");
		stats = (DatasetListStatistics) params.get("stats");
		removeEnds = ((Boolean) params.get("remove")).booleanValue();
		tailTrim = ((Double) params.get("tailTrim")).doubleValue();
		// En realidad esto no se deberia hacer, pero por logica, un valor del 50%
		// devolveria una capa en blanco, asi que evitamos un resultado supuestamente
		// no esperado
		if ((tailTrim >= 0.5) && (tailTrim < 0.51))
			tailTrim = 0.51;
		if ((tailTrim > 0.49) && (tailTrim < 0.50))
			tailTrim = 0.49;
		renderBands = (int[]) params.get("renderBands");
		if(renderBands != null && renderBands.length < raster.getBandCount()) {
			int[] newRenderBands = new int[raster.getBandCount()];
			for (int i = 0; i < renderBands.length; i++)
				newRenderBands[i] = renderBands[i];
			for (int i = renderBands.length; i < newRenderBands.length; i++)
				newRenderBands[i] = i;
			renderBands = newRenderBands;
		}
		height = raster.getHeight();
		width = raster.getWidth();

		try {
			stats.calcFullStatistics();
		} catch (FileNotOpenException e) {
			exec = false;
		} catch (RasterDriverException e) {
			exec = false;
		} catch (InterruptedException e) {
			exec = false;
		}
		double[][] tailTrimByBand = (double[][]) stats.getTailTrimValue(tailTrim);
		if ((tailTrim != 0) && (tailTrimByBand != null)) { // Max y Min con recorte de colas
			scale = new double[tailTrimByBand.length];
			offset = new double[tailTrimByBand.length];
			minBandValue = new double[tailTrimByBand.length];
			maxBandValue = new double[tailTrimByBand.length];
			for (int i = 0; i < tailTrimByBand.length; i++) {
				minBandValue[i] = tailTrimByBand[i][0];
				maxBandValue[i] = tailTrimByBand[i][1];
			}
		} else {
			scale = new double[stats.getMin().length];
			offset = new double[stats.getMin().length];
			if (removeEnds) { // Si está activado eliminar extremos gastamos el 2º máximo/mínimo
				if(raster.getDataType() == IBuffer.TYPE_BYTE) {
					minBandValue = stats.getSecondMinRGB();
					maxBandValue = stats.getSecondMaxRGB();
				} else {
					minBandValue = stats.getSecondMin();
					maxBandValue = stats.getSecondMax();
				}
			} else { // Si no está activado eliminar extremos
				if(raster.getDataType() == IBuffer.TYPE_BYTE) {
					minBandValue = stats.getMinRGB();
					maxBandValue = stats.getMaxRGB();
				} else {
					minBandValue = stats.getMin();
					maxBandValue = stats.getMax();
				}
			}
		}

		for (int i = 0; i < minBandValue.length; i++) {
			scale[i] = 255D / (maxBandValue[i] - minBandValue[i]);
			offset[i] = (255D * minBandValue[i]) / (minBandValue[i] - maxBandValue[i]);
		}

		nbands = stats.getBandCount();
		rasterResult = RasterBuffer.getBuffer(IBuffer.TYPE_BYTE, raster.getWidth(), raster.getHeight(), raster.getBandCount(), true);
	}

	/**
	 * Obtiene true si está activado el flag de eliminar extremos y false si no lo
	 * está
	 */
	public Boolean getRemoveEnds() {
		return new Boolean(removeEnds);
	}

	/**
	 * Obtiene el porcentaje de recorte de colas aplicado o 0 si no tiene.
	 * @return
	 */
	public Double getTailTrim(){
		return new Double(tailTrim);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getOutRasterDataType()
	 */
	public int getOutRasterDataType() {
		return IBuffer.TYPE_BYTE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getResult(java.lang.String)
	 */
	public Object getResult(String name) {
		if (name.equals("raster")) {
			if (!exec)
				return this.raster;
			return this.rasterResult;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getGroup()
	 */
	public String getGroup() {
		return "radiometricos";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getUIParams()
	 */
	public Params getUIParams(String nameFilter) {
		Params params = new Params();
		params.setParam("RemoveEnds",
				new Boolean(removeEnds),
				Params.CHECK,
				null);
		params.setParam("TailTrim",
				new Double(Math.round(tailTrim * 100.0)),
				Params.SLIDER,
				new String[]{ "0", "100", "0", "1", "25" }); //min, max, valor defecto, intervalo pequeño, intervalo grande;
		return params;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#post()
	 */
	public void post() {
		// En caso de que nadie apunte a raster, se liberará su memoria.
		raster = null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getInRasterDataType()
	 */
	public int getInRasterDataType() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#process(int, int)
	 */
	public void process(int x, int y) {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getNames()
	 */
	public String[] getNames() {
		return names;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#isVisible()
	 */
	public boolean isVisible() {
		return true;
	}
}