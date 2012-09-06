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
package org.gvsig.raster.grid.render;

import java.awt.Image;
import java.awt.image.BufferedImage;

import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.grid.GridTransparency;
import org.gvsig.raster.process.RasterTask;
import org.gvsig.raster.process.RasterTaskQueue;
import org.slf4j.LoggerFactory;

/**
 * Objeto para la escritura de datos desde un buffer a un objeto Image. En este nivel de
 * renderizado no se gestiona extents, ni rotaciones ni coordenadas del mundo, solo la
 * escritura desde un buffer hasta otro de tamaño dado. Por medio de parámetros y de objetos
 * de estado varia el resultado de la escritura, selección de bandas a escribir desde el buffer
 * a RGB, transparencias aplicadas o paletas.
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class ImageDrawer {
	/**
	 * Fuente de datos para el renderizado
	 */
	private IBuffer   rasterBuf = null;
	private double[]  step      = null;
	/**
	 * Ancho y alto del objeto image
	 */
	private int       width     = 0;
	private int       height    = 0;
	private Rendering rendering = null;

	public ImageDrawer(Rendering rendering) {
		this.rendering = rendering;
	}

	/**
	 * Dibuja el buffer sobre un objeto Image de java.awt y devuelve el resultado.
	 *
	 * @param replicateBand Flag de comportamiento del renderizado. Al renderizar el buffer
	 * este obtiene la primera banda del buffer y la asigna al R, la segunda al G y la tercera
	 * al B. Este flag no es tomado en cuenta en caso de que existan 3 bandas en el buffer.
	 * Si no hay tres bandas, por ejemplo una y el flag es true esta será replicada
	 * en R, G y B, en caso de ser false la banda será dibujada en su posición (R, G o B)
	 * y en las otras bandas se rellenará con 0.
	 *
	 * @param transparentBand. Si es true la banda 4 es alpha y si es false no lo es.
	 *
	 * @return java.awt.Image con el buffer dibujado.
	 * @throws InterruptedException
	 */
	public Image drawBufferOverImageObject(boolean replicateBand, int[] renderBands) throws InterruptedException {
		if (rasterBuf == null || width == 0 || height == 0)
			return null;

		try { // Temporal para la traza de un error aleatorio
			BufferedImage image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_ARGB);

			// Dibujado de raster de 1 o 2 bandas.
			// adaptBufferToRender(replicateBand, renderBands);

			if (rasterBuf.getDataType() != IBuffer.TYPE_BYTE)
				rasterBuf = convertToByte(rasterBuf);

			// Asigna la banda de transparencia si existe esta.
			// assignTransparencyBand(renderBands);

			byte[] data = new byte[rasterBuf.getBandCount()];

			GridTransparency transparency = rendering.getLastTransparency();
			if (transparency != null && transparency.isTransparencyActive()) {
				if (transparency.existAlphaBand()
						&& transparency.getAlphaBand() != null
						&& (transparency.getAlphaBand().getDataType() != IBuffer.TYPE_BYTE))
					transparency.setAlphaBand(convertToByte(transparency
							.getAlphaBand()));
				drawWithTransparency(image, data, (step != null));
			} else
				drawByte(image, data, (step != null));

			step = null;
			return image;
		} catch (OutOfMemoryError error) { // Temporal para la traza de un error
											// aleatorio
			LoggerFactory.getLogger(this.getClass()).debug(
					"Buffer: " + width + " " + height + " RenderBands: "
							+ renderBands, error);
		}
		return null;
	}

	/**
	 * Calcula los vectores de desplazamiento en pixels en X e Y cuando se supersamplea.
	 * @param r Array de desplazamientos para las filas. Debe tener espacio reservado
	 * @param c Array de desplazamientos para las columnas. Debe tener espacio reservado
	 * cargados.
	 */
	private void calcSupersamplingStepsArrays(int[] r, int[] c) {
		double pos = step[1];
		for(int row = 0; row < r.length; row ++) {
			r[row] = (int)(pos / step[3]);
			pos ++;
		}
		pos = step[0];
		for(int col = 0; col < c.length; col ++) {
			c[col] = (int)(pos / step[2]);
			pos ++;
		}
	}

	/**
	 * Dibuja un raster sobre un BufferedImage
	 * @param image BufferedImage sobre el que se dibuja
	 * @param data buffer vacio. Se trata de un array de bytes donde cada elemento representa una banda.
	 * @param supersampling true si se necesita supersamplear y false si no se necesita
	 * @throws InterruptedException
	 */
	private void drawByte(BufferedImage image, byte[] data, boolean supersampling) throws InterruptedException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());

		if (supersampling) {
			int[] r = new int[height];
			int[] c = new int[width];
			calcSupersamplingStepsArrays(r, c);
			for (int row = 0; row < height; row++) {
				for (int col = 0; col < width; col++)
					try {
						rasterBuf.getElemByte(r[row], c[col], data);
						image.setRGB(col, row, (0xff000000 + ((data[0] & 0xff) << 16)
								+ ((data[1] & 0xff) << 8) + (data[2] & 0xff)));
					} catch (ArrayIndexOutOfBoundsException e) {
						System.err.println("== Size Image:" + image.getWidth() + " " + image.getHeight());
						System.err.println("== Position required:" + col + " " + row);
						break;
					}
				if (task.getEvent() != null)
					task.manageEvent(task.getEvent());
			}
		} else
			for (int row = 0; row < rasterBuf.getHeight(); row++) {
				for (int col = 0; col < rasterBuf.getWidth(); col++)
					try {
						rasterBuf.getElemByte(row, col, data);
						image.setRGB(col, row, (0xff000000 + ((data[0] & 0xff) << 16)
								+ ((data[1] & 0xff) << 8) + (data[2] & 0xff)));
					} catch (ArrayIndexOutOfBoundsException ex) {
						System.err.println("== Size Image:" + image.getWidth() + " " + image.getHeight());
						System.err.println("== Position required:" + col + " " + row);
						break;
					}
				if (task.getEvent() != null)
					task.manageEvent(task.getEvent());
			}
	}

	/**
	 * Dibuja un raster sobre un BufferedImage con las propiedades de paleta y transparencia
	 * @param image BufferedImage sobre el que se dibuja
	 * @param data buffer vacio. Se trata de un array de bytes donde cada elemento representa una banda.
	 * @param supersampling true si se necesita supersamplear y false si no se necesita
	 * @throws InterruptedException
	 */
	private void drawWithTransparency(BufferedImage image, byte[] data, boolean supersampling) throws InterruptedException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());
		int value = 0;
		GridTransparency transparency = rendering.getLastTransparency();
//		try {
		if (supersampling) {
			int[] r = new int[height];
			int[] c = new int[width];
			calcSupersamplingStepsArrays(r, c);
			for (int row = 0; row < height; row++) {
				for (int col = 0; col < width; col++)
					try {
						rasterBuf.getElemByte(r[row], c[col], data);
						value = transparency.processRGB(data[0] & 0xff, data[1] & 0xff, data[2] & 0xff, r[row], c[col]);
						image.setRGB(col, row, value);
					} catch (ArrayIndexOutOfBoundsException e) {
						System.err.println("== Size Image:" + image.getWidth() + " " + image.getHeight());
						System.err.println("== Position required:" + col + " " + row);
						break;
					}
				if (task.getEvent() != null)
					task.manageEvent(task.getEvent());
			}
		} else
			for (int row = 0; row < rasterBuf.getHeight(); row++) {
				for (int col = 0; col < rasterBuf.getWidth(); col++)
					try {
						rasterBuf.getElemByte(row, col, data);
						value = transparency.processRGB(data[0] & 0xff, data[1] & 0xff, data[2] & 0xff, row, col);
						image.setRGB(col, row, value);
					} catch (ArrayIndexOutOfBoundsException e) {
						System.err.println("== Size Image:" + image.getWidth() + " " + image.getHeight());
						System.err.println("== Position required:" + col + " " + row);
						break;
					}
				if (task.getEvent() != null)
					task.manageEvent(task.getEvent());
			}
	}
	/**
	 * Intercala bandas en el buffer dependiendo de si hay que replicar o meter
	 * bandas en negro. Esto tiene es valido para buffers con solo una banda ya que
	 * el dibujado sobre Graphics debe ser R, G, B.
	 *
	 * @param replicateBand false si no se replican bandas y las que no existen
	 * se ponen en negro y false si hay que dibujar la misma en R,G y B. Esto
	 * tiene sentido si el raster tiene solo 1 o 2 bandas.
	 * @param renderBands array con las posiciones de renderizado.
	 *  A la hora de renderizar hay que tener en cuenta que solo se renderizan las
	 * tres primeras bandas del buffer por lo que solo se tienen en cuenta los tres primeros
	 * elementos. Por ejemplo, el array {1, 0, 3} dibujará sobre el Graphics las bandas 1,0 y 3 de un
	 * raster de al menos 4 bandas.La notación con -1 en alguna posición del vector solo tiene sentido
	 * en la visualización pero no se puede asígnar una banda del buffer a null.
	 * Algunos ejemplos:
	 * <P>
	 * {-1, 0, -1} Dibuja la banda 0 del raster en la G de la visualización.
	 * Si replicateBand es true R = G = B sino R = B = 0
	 * {1, 0, 3} La R = banda 1 del raster, G = 0 y B = 3
	 * {0} La R = banda 0 del raster. Si replicateBand es true R = G = B sino G = B = 0
	 * </P>
	 */
	/*private void adaptBufferToRender(boolean replicateBand, int[] renderBands){
		byte[][] aux = null;
		if(rasterBuf.getBandCount() < 3){
			for(int i = 0; i < renderBands.length; i++){
				if( replicateBand && renderBands[i] == -1)
					rasterBuf.replicateBand(0, i);
				if( !replicateBand && renderBands[i] == -1){
					if(aux == null)
						aux = rasterBuf.createByteBand(rasterBuf.getWidth(), rasterBuf.getHeight(), (byte)0);
					rasterBuf.addBandByte(i, aux);
				}
			}
		}
	}*/

	/**
	 * Asigna al objeto GridTransparency la banda de transparencia si la tiene para
	 * tenerla en cuenta en el renderizado.
	 * @param renderBands Lista de bandas a renderizar
	 * @param ts Objeto con las propiedades de transparencia del Grid.
	 */
	/*private void assignTransparencyBand(int[] renderBands) {
		if(transparency != null){
			for(int i = 0; i < transparency.getTransparencyBandNumberList().size(); i ++) {
				for(int j = 0; j < renderBands.length; j ++) {
					if(renderBands[j] == ((Integer)transparency.getTransparencyBandNumberList().get(i)).intValue()){
						if(transparency.getBand() == null)
							transparency.setBand(rasterBuf.getBandBuffer(renderBands[j]));
						else {
							IBuffer outBuf = transparency.getBand().cloneBuffer();
							transparency.mergeTransparencyBands(new IBuffer[]{transparency.getBand(), rasterBuf.getBandBuffer(renderBands[j])}, outBuf);
						}
					}
				}
			}
		}
	}*/

	private IBuffer convertToByte(IBuffer buf) {
		IBuffer b = RasterBuffer.getBuffer(IBuffer.TYPE_BYTE, buf.getWidth(), buf.getHeight(), buf.getBandCount(), true);
		if(buf.getDataType() == IBuffer.TYPE_SHORT)
			for (int nBand = 0; nBand < buf.getBandCount(); nBand++)
				for (int row = 0; row < buf.getHeight(); row++)
					for (int col = 0; col < buf.getWidth(); col++)
						b.setElem(row, col, nBand, (byte)(buf.getElemShort(row, col, nBand) & 0xffff));
		if(buf.getDataType() == IBuffer.TYPE_INT)
			for (int nBand = 0; nBand < buf.getBandCount(); nBand++)
				for (int row = 0; row < buf.getHeight(); row++)
					for (int col = 0; col < buf.getWidth(); col++)
						b.setElem(row, col, nBand, (byte)(buf.getElemInt(row, col, nBand) & 0xffffffff));
		if(buf.getDataType() == IBuffer.TYPE_FLOAT)
			for (int nBand = 0; nBand < buf.getBandCount(); nBand++)
				for (int row = 0; row < buf.getHeight(); row++)
					for (int col = 0; col < buf.getWidth(); col++)
						b.setElem(row, col, nBand, (byte)(Math.round(buf.getElemFloat(row, col, nBand))));
		if(buf.getDataType() == IBuffer.TYPE_DOUBLE)
			for (int nBand = 0; nBand < buf.getBandCount(); nBand++)
				for (int row = 0; row < buf.getHeight(); row++)
					for (int col = 0; col < buf.getWidth(); col++)
						b.setElem(row, col, nBand, (byte)(Math.round(buf.getElemDouble(row, col, nBand))));
		return b;
	}

	/**
	 * Asigna el buffer a renderizar
	 * @param b Buffer a renderizar
	 */
	public void setBuffer(IBuffer b) {
		this.rasterBuf = b;
	}

	/**
	 * Asigna la paleta asociada al grid
	 * @param palette
	 */
	/*public void setPalette(GridPalette palette) {
		this.palette = palette;
	}*/

	/**
	 * Asigna el desplazamiento en pixeles desde la esquina superior izquierda. Si es null se considera que esta
	 * función la ha hecho el driver quedando desactivada en el renderizador. Si es así no debe variar el resultado
	 * en la visualizacion.
	 * Si Supersamplea el renderizador se cargará una matriz de datos 1:1 por lo que se podrá aplicar un filtro
	 * a este buffer de datos leidos independientemente del zoom que tengamos.
	 * @param step Desplazamiento
	 */
	public void setStep(double[] step) {
		this.step = step;
	}

	/**
	 * Asigna el ancho y el alto del BufferedImage. Esto es util para cuando hay supersampling
	 * que el tamaño del objeto Image no coincide con el buffer con los datos raster.
	 * @param w Ancho
	 * @param h Alto
	 */
	public void setBufferSize(int w, int h) {
		this.width = w;
		this.height = h;
	}

}
