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
import org.gvsig.raster.buffer.cache.RasterReadOnlyBuffer;
import org.gvsig.raster.dataset.FileNotOpenException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.dataset.properties.DatasetListStatistics;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.enhancement.LinearStretchParams.Stretch;
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
public class LinearStretchEnhancementFilter extends RasterFilter {
	protected double[][]               scale             = null;
	protected double[][]               offset            = null;
	protected DatasetListStatistics	   stats             = null;
	protected double[]                 minBandValue	     = null;
	protected double[]                 maxBandValue	     = null;
	protected int[]                    renderBands       = null;
	public static String[]             names             = new String[] {"enhanced_stretch"};
	private boolean                    removeEnds        = false;

	protected LinearStretchParams      stretchs          = null;
	protected Stretch[]                scaleOffsetList   = null;

	/**
	 * Construye un LinearEnhancementFilter
	 */
	public LinearStretchEnhancementFilter() {
		setName(names[0]);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#pre()
	 */
	public void pre() {
		raster = (IBuffer) params.get("raster");
		stats = (DatasetListStatistics) params.get("stats");
		stretchs = (LinearStretchParams) params.get("stretchs");
		if (params.get("remove") != null)
			removeEnds = ((Boolean) params.get("remove")).booleanValue();

		if (stretchs == null)
			return;

		renderBands = (int[]) params.get("renderBands");
		if (renderBands == null)
			renderBands = new int[0];
		if (renderBands != null && renderBands.length < raster.getBandCount()) {
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

		if (raster.getDataType() != IBuffer.TYPE_BYTE)
			stretchs.rgb = false;

		stretchs.setMaxMin(stats, renderBands);

		if (removeEnds)
			stretchs.applyRemoveEndsToStretchs(stats, renderBands);

		stretchs.loadTailTrimValues(stats);

		if (stretchs.hasTailTrim())
			stretchs.applyTrimToStretchs();

		stretchs.calcLinearScaleAndOffset();

		loadStretchList();

		if(raster instanceof RasterReadOnlyBuffer)
			((RasterReadOnlyBuffer) raster).addDrawableBands(renderBands);

		rasterResult = RasterBuffer.getBuffer(IBuffer.TYPE_BYTE, raster.getWidth(), raster.getHeight(), raster.getBandCount(), true);
	}

	/**
	 * La lista de escalas y desplazamientos es un array de 3 elementos en el que
	 * cada posición es un objeto Stretch con la escala y desplazamiento de la
	 * banda que se dibuja en esa posición. El objetivo es aplicar a cada banda
	 * el máximo y mínimo que le corresponde. Por ejemplo, cuando tenemos una imagen de
	 * 3 bandas de tipo short y queremos visualizar en RGB solo la primera banda entonces
	 * escaleOffsetList tendrá stretchs.red en las 3 posiciones.
	 */
	private void loadStretchList() {
		scaleOffsetList = new Stretch[3];
		scaleOffsetList[0] = stretchs.red;
		scaleOffsetList[1] = stretchs.green;
		scaleOffsetList[2] = stretchs.blue;
	}

	/**
	 * Obtiene el porcentaje de recorte de colas aplicado o 0 si no tiene.
	 * @return
	 */
	public Double getTailTrim() {
		double[] tailTrimList;
		if (stretchs != null)
			tailTrimList = stretchs.getTailTrimList();
		else
			tailTrimList = new double[0];
		double median = 0;
		double nValues = tailTrimList.length;
		for (int i = 0; i < tailTrimList.length; i++)
			median += tailTrimList[i];
		return new Double(nValues > 0 ? median / nValues : median);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getOutRasterDataType()
	 */
	public int getOutRasterDataType() {
		return IBuffer.TYPE_BYTE;
	}

	/**
	 * Obtiene true si está activado el flag de eliminar extremos y false si no lo
	 * está
	 */
	public Boolean getRemoveEnds() {
		return new Boolean(removeEnds);
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
		if(stretchs == null)
			stretchs = (LinearStretchParams) this.params.get("stretchs");
		Params params = new Params();
		params.setParam("TailTrim",
				new Double(Math.round(getTailTrim().doubleValue() * 100.0)),
				Params.SLIDER,
				new String[]{ "0", "100", "0", "1", "25" }); //min, max, valor defecto, intervalo pequeño, intervalo grande;
		params.setParam("StretchInRed",
				stretchs.red.stretchIn,
				Params.CHOICE,
				null);
		params.setParam("StretchInGreen",
				stretchs.green.stretchIn,
				Params.CHOICE,
				null);
		params.setParam("StretchInBlue",
				stretchs.blue.stretchIn,
				Params.CHOICE,
				null);
		params.setParam("StretchOutRed",
				stretchs.red.stretchOut,
				Params.CHOICE,
				null);
		params.setParam("StretchOutGreen",
				stretchs.green.stretchOut,
				Params.CHOICE,
				null);
		params.setParam("StretchOutBlue",
				stretchs.blue.stretchOut,
				Params.CHOICE,
				null);
		params.setParam("TailTrimRedMin",
				new Double(stretchs.red.tailTrimMin),
				Params.CHOICE,
				null);
		params.setParam("TailTrimRedMax",
				new Double(stretchs.red.tailTrimMax),
				Params.CHOICE,
				null);
		params.setParam("TailTrimGreenMin",
				new Double(stretchs.green.tailTrimMin),
				Params.CHOICE,
				null);
		params.setParam("TailTrimGreenMax",
				new Double(stretchs.green.tailTrimMax),
				Params.CHOICE,
				null);
		params.setParam("TailTrimBlueMin",
				new Double(stretchs.blue.tailTrimMin),
				Params.CHOICE,
				null);
		params.setParam("TailTrimBlueMax",
				new Double(stretchs.blue.tailTrimMax),
				Params.CHOICE,
				null);
		params.setParam("Remove",
				new Boolean(removeEnds),
				Params.CHOICE,
				null);
		if(renderBands == null)
			renderBands = (int[]) this.params.get("renderBands");
		params.setParam("RenderBands",
				convertArrayToString(renderBands),
				Params.NONE,
				null);
		params.setParam("RGB",
				new Boolean(stretchs.rgb),
				Params.NONE,
				null);
		return params;
	}

	/**
	 * Convierte un array de dobles a una cadena
	 * @param values
	 * @return
	 */
	private String convertArrayToString(int[] values) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < values.length; i++) {
			buffer.append(values[i]);
			if (i < (values.length - 1))
				buffer.append(" ");
		}
		return buffer.toString();
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
		return false;
	}

	/**
	 * @return the stretchs
	 */
	public LinearStretchParams getStretchs() {
		return stretchs;
	}
}