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
package org.gvsig.raster.vectorization;

import java.util.ArrayList;

import org.gvsig.jpotrace.Potrace;
import org.gvsig.jpotrace.PotraceException;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
/**
 * La clase VectorizationBinding se usa para poder vectorizar una capa raster.<p>
 * 
 * La entrada es una capa raster, ya sea en un fichero, BufferFactory o un
 * RasterDataset.<p>
 * 
 * El uso de comunicacion con la libreria jpotrace es como funciona el comando
 * potrace, de hecho, se pueden añadir mas opciones si potrace los admite por
 * linea de comandos.<p>
 * 
 * Una vez creada una instancia a VectorizationBinding. Se definen todos los
 * parametros deseables o en caso de no definirlos se usaran los valores por
 * defecto y se invoca al metodo VectorizeBuffer()<p>
 * 
 * Este ultimo metodo, devuelve un array de doubles indicando si lo que se ha
 * devuelto son operaciones tipicas de shapes... MoveTo, LineTo, CurveTo, etc...<p>
 * 
 * En la primera posicion se devuelve el tamaño del array.<p>
 * 
 * 03/09/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class VectorizationBinding {
	public final static int POLICY_BLACK    = 0;
	public final static int POLICY_WHITE    = 1;
	public final static int POLICY_RIGHT    = 2;
	public final static int POLICY_LEFT     = 3;
	public final static int POLICY_MINORITY = 4;
	public final static int POLICY_MAJORITY = 5;
	public final static int POLICY_RANDOM   = 6;
	
	private RasterBuffer buffer                = null;
	private Double       cornerThreshold       = null;
	private Integer      despeckle             = null;
	private Integer      policy                = null;
	private Double       optimizationTolerance = null;
	private boolean      curveOptimization     = true;
	private Integer      outputQuantization    = null;

	/**
	 * Construye un VectorizationBinding a partir de la ruta de un Raster
	 * @param file
	 * @throws NotSupportedExtensionException
	 * @throws RasterDriverException
	 * @throws InterruptedException
	 */
	public VectorizationBinding(String file) throws NotSupportedExtensionException, RasterDriverException, InterruptedException {
		this.setFile(file);
	}

	/**
	 * Construye un VectorizationBinding a partir de un RasterDataset
	 * @param dataset
	 * @throws RasterDriverException 
	 * @throws InterruptedException 
	 * @throws InterruptedException
	 * @throws RasterDriverException
	 */
	public VectorizationBinding(RasterDataset dataset) throws InterruptedException, RasterDriverException {
		this.setRasterDataset(dataset);
	}

	/**
	 * Construye un VectorizationBinding a partir de un BufferFactory
	 * @param bufferFactory
	 * @throws RasterDriverException 
	 * @throws InterruptedException 
	 * @throws InterruptedException
	 * @throws RasterDriverException
	 */
	public VectorizationBinding(BufferFactory bufferFactory) throws InterruptedException, RasterDriverException {
		this.setBufferFactory(bufferFactory);
	}
	
	/**
	 * Se especifica el fichero del raster
	 * @param file
	 * @throws NotSupportedExtensionException
	 * @throws RasterDriverException
	 * @throws InterruptedException
	 */
	private void setFile(String file) throws NotSupportedExtensionException, RasterDriverException, InterruptedException {
		setRasterDataset(RasterDataset.open(null, file));
	}

	/**
	 * Se especifica el RasterDataset
	 * @param dataset
	 * @throws InterruptedException
	 * @throws RasterDriverException
	 */
	private void setRasterDataset(RasterDataset dataset) throws InterruptedException, RasterDriverException {
		setBufferFactory(new BufferFactory(dataset));
	}

	/**
	 * Se especifica el BufferFactory
	 * @param bufferFactory
	 * @throws InterruptedException
	 * @throws RasterDriverException
	 */
	private void setBufferFactory(BufferFactory bufferFactory) throws InterruptedException, RasterDriverException {
		bufferFactory.setAllDrawableBands();
		bufferFactory.setAreaOfInterest();
		setRasterBuffer((RasterBuffer) bufferFactory.getRasterBuf());
	}

	/**
	 * Se establece el RasterBuffer
	 * @param buffer
	 */
	private void setRasterBuffer(RasterBuffer buffer) {
		this.buffer = buffer;
	}

	/**
	 * Pone a 1 la posición del bit pasado por parametro en el value. Operación binaria OR
	 * @param value
	 * @param pos
	 * @return
	 */
	private int enableBit(int value, int pos) {
		value = (value | ((int)1 << pos));
		return value;
	}
	
	/**
	 * Set the corner threshold parameter (default 1)
	 * @param value
	 */
	public void setCornerThreshold(double value) {
		cornerThreshold = new Double(value);
	}

	/**
	 * Quantize output to 1/unit pixels (default 10)
	 * @param value
	 */
	public void setOutputQuantization(int value) {
		outputQuantization = new Integer(value);
	}

	/**
	 * Suppress speckles of up to this size (default 2)
	 * @param value
	 */
	public void setDespeckle(int value) {
		despeckle = new Integer(value);
	}

	/**
	 * Set how to resolve ambiguities in path decomposition<p>
	 * 
	 * Use:<br><b>
	 * 	 POLICY_BLACK<br>
	 *   POLICY_LEFT<br>
	 *   POLICY_MAJORITY<br>
	 *   POLICY_MINORITY<br>
	 *   POLICY_RANDOM<br>
	 *   POLICY_RIGHT<br>
	 *   POLICY_WHITE</b>
	 *  
	 * @param value
	 */
	public void setPolicy(int value) {
		policy = new Integer(value);
	}
	
	/**
	 * Set the curve optimization tolerance (default 0.2)
	 * @param value
	 */
	public void setOptimizationTolerance(double value) {
		optimizationTolerance = new Double(value);
	}

	/**
	 * Return if the curve optimization is enabled or disabled
	 * @param value
	 */
	public boolean isEnabledCurveOptimization() {
		return curveOptimization;
	}

	/**
	 * Enable/Disable the curve optimization (default enabled)
	 * @param value
	 */
	public void setEnabledCurveOptimization(boolean value) {
		curveOptimization = value;
	}
	
	/**
	 * Devuelve los parametros en formato de array (Similar a como los recibe el
	 * main de un fichero en c) indicando la llamada entera al comando potrace.
	 * @return
	 */
	private String[] getParams() {
		ArrayList params = new ArrayList();
		params.add("./potrace");

		if (cornerThreshold != null) {
			params.add("-a");
			params.add(cornerThreshold.toString());
		}

		if (despeckle != null) {
			params.add("-t");
			params.add(despeckle.toString());
		}
		
		if (outputQuantization != null) {
			params.add("-u");
			params.add(outputQuantization.toString());
		}
		
		if (policy != null) {
			String param = null;
			switch (policy.intValue()) {
				case POLICY_BLACK:
					param = "black";
					break;
				case POLICY_WHITE:
					param = "white";
					break;
				case POLICY_LEFT:
					param = "left";
					break;
				case POLICY_RIGHT:
					param = "right";
					break;
				case POLICY_MINORITY:
					param = "minority";
					break;
				case POLICY_MAJORITY:
					param = "majority";
					break;
				case POLICY_RANDOM:
					param = "random";
					break;
			}
			if (param != null) {
				params.add("-z");
				params.add(param);
			}
		}
		
		if (optimizationTolerance != null) {
			// Curve optimization tolerance
			params.add("-O");
			params.add(optimizationTolerance.toString());
		}
		
		if (!curveOptimization) {
			// turn off curve optimization
			params.add("-n"); 
		}

		String[] strings = new String[params.size()];
		for (int i = 0; i < strings.length; i++) {
			strings[i] = (String) params.get(i);
		}

		return strings;
	}

	/**
	 * Hace el proeso de Vectorizacion de un Raster
	 * @return
	 * @throws PotraceException
	 */
	public double[] VectorizeBuffer() {
		int dy = (buffer.getWidth() + 32 - 1) / 32;

		int[] bufferBits = new int[buffer.getHeight() * dy];

		for (int i = 0; i < bufferBits.length; i++)
			bufferBits[i] = 0;
		
		int bit = 0;
		int pos = 0;
		for (int i = 0; i < buffer.getHeight(); i++) {
			pos = (buffer.getHeight() - i - 1) * dy;
			bit = 0;
			for (int j = 0; j < buffer.getWidth(); j++) {
				byte data = buffer.getElemByte(i, j, 0);

				if (data == 0) {
					bufferBits[pos] = enableBit(bufferBits[pos], 31 - bit);
				}

				bit++;
				if (bit >= 32) {
					bit = 0;
					pos++;
				}
			}
		}

		try {
			return Potrace.vectorizeBufferRaster(bufferBits, buffer.getWidth(), buffer.getHeight(), getParams());
		} catch (PotraceException e) {
		}
		return null;
	}
}
