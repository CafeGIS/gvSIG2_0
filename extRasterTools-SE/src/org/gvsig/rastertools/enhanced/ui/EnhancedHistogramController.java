/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.rastertools.enhanced.ui;

import org.gvsig.raster.beans.canvas.layers.functions.StraightLine;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.grid.filter.enhancement.LinearStretchEnhancementFilter;
import org.gvsig.rastertools.enhanced.graphics.HistogramGraphicBase;
import org.gvsig.rastertools.enhanced.graphics.InputHistogram;
import org.gvsig.rastertools.enhanced.graphics.OutputHistogram;
import org.gvsig.rastertools.enhanced.graphics.HistogramGraphicBase.HistogramStatus;
/**
 * Manager para actualizar la vista previa y el histograma de salida del cuadro
 * de realce
 * 
 * @version 04/03/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class EnhancedHistogramController {
	private InputHistogram  inputHistogram  = null;
	private OutputHistogram outputHistogram = null;
	private EnhancedDialog  enhancedDialog  = null;

	public EnhancedHistogramController(InputHistogram inputHistogram, OutputHistogram outputHistogram, EnhancedDialog enhancedDialog) {
		this.inputHistogram = inputHistogram;
		this.outputHistogram = outputHistogram;
		this.enhancedDialog = enhancedDialog;
	}
	
	public void updatePreview() {
		Params params = new Params();

//		Double min = Double.valueOf(0.0D);
		boolean rgb = enhancedDialog.getGraphicsPanel().getRGB().isSelected();
		HistogramStatus histogram = inputHistogram.getHistogramStatus(HistogramGraphicBase.GRAY);
		if (enhancedDialog.getLayer().isRenderingAsGray()) {
			if (histogram.getBaseFunction() instanceof StraightLine) {
				StraightLine line = (StraightLine) histogram.getBaseFunction();
				double[] valuesIn;
				if (rgb)
					valuesIn = line.getInValues(0, 255);
				else
					valuesIn = line.getInValues(histogram.getMin(), histogram.getMax());
				int[] valuesOut = line.getOutValues();
				params.setParam("StretchInRed", valuesIn, -1, null);
				params.setParam("StretchOutRed", valuesOut, -1, null);
				params.setParam("StretchRedFunctionType", Integer.valueOf(line.getFunctionType()), -1, null);
				params.setParam("StretchRedValueFunction", Double.valueOf(line.getValueFunction()), -1, null);
				params.setParam("StretchInGreen", valuesIn, -1, null);
				params.setParam("StretchOutGreen", valuesOut, -1, null);
				params.setParam("StretchGreenFunctionType", Integer.valueOf(line.getFunctionType()), -1, null);
				params.setParam("StretchGreenValueFunction", Double.valueOf(line.getValueFunction()), -1, null);
				params.setParam("StretchInBlue", valuesIn, -1, null);
				params.setParam("StretchOutBlue", valuesOut, -1, null);
				params.setParam("StretchBlueFunctionType", Integer.valueOf(line.getFunctionType()), -1, null);
				params.setParam("StretchBlueValueFunction", Double.valueOf(line.getValueFunction()), -1, null);
			}
		} else {
			histogram = inputHistogram.getHistogramStatus(HistogramGraphicBase.RED);
			if (histogram != null) {
				if (histogram.getBaseFunction() instanceof StraightLine) {
					StraightLine line = (StraightLine) histogram.getBaseFunction();
					double[] valuesIn;
					if (rgb)
						valuesIn = line.getInValues(0, 255);
					else
						valuesIn = line.getInValues(histogram.getMin(), histogram.getMax());
					int[] valuesOut = line.getOutValues();

					params.setParam("StretchInRed", valuesIn, -1, null);
					params.setParam("StretchOutRed", valuesOut, -1, null);
					params.setParam("StretchRedFunctionType", Integer.valueOf(line.getFunctionType()), -1, null);
					params.setParam("StretchRedValueFunction", Double.valueOf(line.getValueFunction()), -1, null);
				}
			}
			histogram = inputHistogram.getHistogramStatus(HistogramGraphicBase.GREEN);
			if (histogram != null) {
				if (histogram.getBaseFunction() instanceof StraightLine) {
					StraightLine line = (StraightLine) histogram.getBaseFunction();
					double[] valuesIn;
					if (rgb)
						valuesIn = line.getInValues(0, 255);
					else
						valuesIn = line.getInValues(histogram.getMin(), histogram.getMax());
					int[] valuesOut = line.getOutValues();

					params.setParam("StretchInGreen", valuesIn, -1, null);
					params.setParam("StretchOutGreen", valuesOut, -1, null);
					params.setParam("StretchGreenFunctionType", Integer.valueOf(line.getFunctionType()), -1, null);
					params.setParam("StretchGreenValueFunction", Double.valueOf(line.getValueFunction()), -1, null);
				}
			}
			histogram = inputHistogram.getHistogramStatus(HistogramGraphicBase.BLUE);
			if (histogram != null) {
				if (histogram.getBaseFunction() instanceof StraightLine) {
					StraightLine line = (StraightLine) histogram.getBaseFunction();
					double[] valuesIn;
					if (rgb)
						valuesIn = line.getInValues(0, 255);
					else
						valuesIn = line.getInValues(histogram.getMin(), histogram.getMax());
					int[] valuesOut = line.getOutValues();

					params.setParam("StretchInBlue", valuesIn, -1, null);
					params.setParam("StretchOutBlue", valuesOut, -1, null);
					params.setParam("StretchBlueFunctionType", Integer.valueOf(line.getFunctionType()), -1, null);
					params.setParam("StretchBlueValueFunction", Double.valueOf(line.getValueFunction()), -1, null);
				}
			}
		}
		
		params.setParam("TailTrimRedMin",   Double.valueOf(0.0D), -1, null);
		params.setParam("TailTrimRedMax",   Double.valueOf(0.0D), -1, null);
		params.setParam("TailTrimGreenMin", Double.valueOf(0.0D), -1, null);
		params.setParam("TailTrimGreenMax", Double.valueOf(0.0D), -1, null);
		params.setParam("TailTrimBlueMin",  Double.valueOf(0.0D), -1, null);
		params.setParam("TailTrimBlueMax",  Double.valueOf(0.0D), -1, null);

		params.setParam("RGB", new Boolean(rgb), -1, null);
		String render = "";
		for (int i = 0; i < enhancedDialog.getLayer().getRenderBands().length; i++) {
			if (render != "")
				render += " ";
			render = render + "" + enhancedDialog.getLayer().getRenderBands()[i];
		}
		params.setParam("RenderBands", render, -1, null);

		enhancedDialog.getFilteredPreview().getParamsList().clear();
		enhancedDialog.getFilteredPreview().addNewParam("enhanced_stretch", params, LinearStretchEnhancementFilter.class);
		
		enhancedDialog.getPreviewBasePanel().refreshPreview();
	}

	/**
	 * Actualiza el histograma de salida del cuadro de realce
	 */
	public void updateHistogramOut() {
		HistogramStatus histogram = inputHistogram.getHistogramStatus(HistogramGraphicBase.DRAWED);
		if (histogram != null) {
			if (histogram.getBaseFunction() instanceof StraightLine) {
				StraightLine line = (StraightLine) histogram.getBaseFunction();
				double[] valuesIn = line.getPercentInValues();
				double[] valuesOut = line.getPercentOutValues();

				double origenHistogram[] = inputHistogram.getHistogramStatus(HistogramGraphicBase.DRAWED).getHistogram();
				double newHistogram[] = new double[origenHistogram.length];

				for (int i = 0; i < newHistogram.length; i++)
					newHistogram[i] = 0.0D;

				int pos;
				double p;
				for (int i = 0; i < origenHistogram.length; i++) {
					p = (((double) i) / (origenHistogram.length - 1.0D));
					
					for (int j = 0; j < (valuesIn.length - 1); j++) {
						if (valuesIn[j] == valuesIn[j + 1]) continue;
						if (p >= valuesIn[j] && p <= valuesIn[j + 1]) {
							p = valuesOut[j] + ((valuesOut[j + 1] - valuesOut[j]) * ((p - valuesIn[j]) / (valuesIn[j + 1] - valuesIn[j])));
							break;
						}
					}

					pos = (int) Math.round(p * (origenHistogram.length - 1.0D));
					if (pos < 0)
						pos = 0;
					if (pos > (origenHistogram.length - 1))
						pos = (origenHistogram.length - 1);

					newHistogram[pos] += origenHistogram[i];
				}

				HistogramStatus histogramOut = outputHistogram.getHistogramStatus(HistogramGraphicBase.DRAWED);
				histogramOut.setHistogram(newHistogram);
				histogramOut.setLimits(0.0D, 255.0D);
				outputHistogram.repaint();
			}
		}
	}
}