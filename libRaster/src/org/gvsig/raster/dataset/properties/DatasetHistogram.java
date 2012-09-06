/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.raster.dataset.properties;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.FileNotOpenException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.dataset.serializer.RmfSerializerException;
import org.gvsig.raster.datastruct.Histogram;
import org.gvsig.raster.process.RasterTask;
import org.gvsig.raster.process.RasterTaskQueue;

/**
 * Clase para la gestión de histogramas de un raster. Es la encargada del calculo de un histograma
 * total o parcial de un raster a partir de los datos de disco. Además también es la encargada de gestionar
 * salvar este histograma a .rmf. En caso de que solicite un histograma del raster completo este irá
 * a buscarlo al fichero rmf asociado antes de calcularlo por si ya existise. Al realizar el calculo
 * del histograma de la imagen completa este será salvado en el fichero .rmf asociado al raster.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class DatasetHistogram {
	/**
	 * Histograma de la imagen completa
	 */
	private Histogram			histogram = null;
	/**
	 * Dataset del cual se calcula el histograma
	 */
	private RasterDataset		dataset = null;
	private int 				percent = 0;
	
	/**
	 * Constructor
	 * @param dataset
	 */
	public DatasetHistogram(RasterDataset dataset){
		this.dataset = dataset;
	}

	/**
	 * Obtiene el minimo valor de las estadisticas de un histograma.
	 * @return double
	 */
	public double getMinimum() {
		return dataset.getStatistics().getMinimun();
	}

	/**
	 * Obtiene el maximo valor de las estadisticas de un histograma.
	 * @return double
	 */
	public double getMaximum() {
		return dataset.getStatistics().getMaximun();
	}
	/**
	 * Obtiene el histograma. Si puede conseguirlo del fichero rmf irá allí a 
	 * buscarlo sino lo calculará.
	 * @return histograma 
	 */
	public Histogram getHistogram() throws FileNotOpenException, RasterDriverException, InterruptedException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());
		try {
			if (dataset != null) {

				dataset.getStatistics().calcFullStatistics();

				try {
					histogram = (Histogram) dataset.loadObjectFromRmf(Histogram.class, histogram);
					if (histogram != null)
						return histogram;
				} catch (RmfSerializerException e) {
					//No carga desde rmf. No afecta a la funcionalidad
				}
				
				if(task.getEvent() != null)
					task.manageEvent(task.getEvent());

				histogram = new Histogram(dataset.getBandCount(), dataset.getStatistics().getMin(), dataset.getStatistics().getMax(), dataset.getDataType()[0]);
									
				histogram = getHistogramByDataType();
				
				try {
					dataset.saveObjectToRmf(Histogram.class, histogram);
				} catch (RmfSerializerException e) {
					//No salva a rmf. No afecta a la funcionalidad
				}
				return histogram;
				
			}
		} catch (InvalidSetViewException e) {
			//La vista se selecciona automáticamente no debería darse esta excepción
		}
		return null;
	}

	/**
	 * Obtiene el histograma teniendo en cuenta la lista de clases
	 * @return Histograma correspondiente a la lista de clases
	 */
	private Histogram getHistogramByDataType() 
		throws InvalidSetViewException, FileNotOpenException, RasterDriverException, InterruptedException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());
		percent = 0;
		int type = dataset.getDataType()[0];
		int h = RasterLibrary.blockHeight;

		histogram.setNoDataValue(dataset.getNoDataValue());
		for (int block = 0; block < dataset.getHeight(); block += h) {
			Object buf = null;
			try {
				buf = dataset.readBlock(block, h);
			} catch (InvalidSetViewException e) {
				// La vista se asigna automáticamente
			}

			int hB = h;
			if ((block + hB) > dataset.getHeight())
				hB = Math.abs(dataset.getHeight() - block);

			switch (type) {
				case IBuffer.TYPE_BYTE:
					byte[][][] bBlock = (byte[][][]) buf;
					for (int iBand = 0; iBand < dataset.getBandCount(); iBand++)
						for (int col = 0; col < dataset.getWidth(); col++)
							for (int row = 0; row < hB; row++)
								histogram.incrementPxValue(iBand, (double) bBlock[iBand][row][col]);
					break;
				case IBuffer.TYPE_SHORT:
					short[][][] sBlock = (short[][][]) buf;
					for (int iBand = 0; iBand < dataset.getBandCount(); iBand++)
						for (int col = 0; col < dataset.getWidth(); col++)
							for (int row = 0; row < hB; row++)
								histogram.incrementPxValue(iBand, (double) sBlock[iBand][row][col]);
					break;
				case IBuffer.TYPE_INT:
					int[][][] iBlock = (int[][][]) buf;
					for (int iBand = 0; iBand < dataset.getBandCount(); iBand++)
						for (int col = 0; col < dataset.getWidth(); col++)
							for (int row = 0; row < hB; row++)
								histogram.incrementPxValue(iBand, (double) iBlock[iBand][row][col]);
					break;
				case IBuffer.TYPE_FLOAT:
					float[][][] fBlock = (float[][][]) buf;
					for (int iBand = 0; iBand < dataset.getBandCount(); iBand++)
						for (int col = 0; col < dataset.getWidth(); col++)
							for (int row = 0; row < hB; row++)
								histogram.incrementPxValue(iBand, (double) (fBlock[iBand][row][col]));
					break;
				case IBuffer.TYPE_DOUBLE:
					double[][][] dBlock = (double[][][]) buf;
					for (int iBand = 0; iBand < dataset.getBandCount(); iBand++)
						for (int col = 0; col < dataset.getWidth(); col++)
							for (int row = 0; row < hB; row++)
								histogram.incrementPxValue(iBand, (double) (dBlock[iBand][row][col]));
					break;
			}
			if (task.getEvent() != null)
				task.manageEvent(task.getEvent());
			percent += ((h * 100) / dataset.getHeight());
		}
		percent = 100;
		return histogram;
	}

	/**
	 * Pone a cero el porcentaje de progreso del proceso de calculo de histograma
	 */
	public void resetPercent() {
		percent = 0;
	}

	/**
	 * Obtiene el porcentaje de progreso del proceso de calculo de histograma
	 * @return porcentaje de progreso
	 */
	public int getPercent() {
		return percent;
	}
}
