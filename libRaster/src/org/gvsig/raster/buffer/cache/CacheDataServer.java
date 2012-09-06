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
package org.gvsig.raster.buffer.cache;

import java.awt.geom.AffineTransform;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.Band;
import org.gvsig.raster.dataset.BandList;
import org.gvsig.raster.dataset.BandNotFoundInListException;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IDataWriter;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.dataset.Params.Param;

/**
 * Servidor de datos de caché. Esta clase es la encargada de volcar a disco las
 * páginas de caché y recuperar las cuando le son solicitadas. Por ello debe
 * implementar el interfaz ICacheDataSource con dos métodos loadPage y savePage.
 * 
 * Ambos a partir del número de página recuperaran o salvaran de/a disco una página.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class CacheDataServer implements ICacheDataSource {
	
	/**
	 * Directorio temporal para la caché. Si gastamos el mismo que andami este se ocupará de gestionar su
	 * destrucción al cerrar gvSIG.
	 */
	private String tempDirectoryPath;
	private String id = null;        // Identificador de fichero.

	 
	/**
	 * Constructor. 
	 * Crea el identificador para todos los trozos de caché que se guardarán en disco. 
	 * @param id Identificador de fichero. Si este es null se calcula uno automáticamente
	 * @param numBand Número de banda
	 * @param numPag Número de página
	 */
	public CacheDataServer(String id, int numBand, int numPag) {
		tempDirectoryPath = RasterLibrary.getTemporalPath();
		setName(id, numBand, numPag);
	}

	/**
	 * Crea el identificador para todos los trozos de caché que se guardarán en disco. 
	 * @param id Identificador de fichero. Si este es null se calcula uno automáticamente
	 * @param numBand Número de banda
	 * @param numPag Número de página
	 */
	public void setName(String id, int numBand, int numPag) {
		String oldFileName = tempDirectoryPath + File.separator + this.id;

		if (id == null)
			this.id = Long.toString(System.currentTimeMillis()) + "-" + numPag + "-" + numBand;
		else
			this.id = id + "-" + numPag + "-" + numBand;

		String newFileName = tempDirectoryPath + File.separator + this.id;
		File newFile = new File(newFileName);
		File oldFile = new File(oldFileName);
		if (oldFile.exists())
			oldFile.renameTo(newFile);
	}
	
	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dataaccess.cache.ICacheDataSource#loadPage(int, org.gvsig.fmap.dataaccess.cache.PageBuffer)
	 */
	public void loadPage(PageBandBuffer pageBuffer) {
		String inFileName = tempDirectoryPath + File.separator + id;

		File file = new File(inFileName);
		if (!file.exists())
			return;

		DataInputStream dis;
		try {
			dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
			read(dis, pageBuffer);
			dis.close();
		} catch (FileNotFoundException e) {
			return;
		} catch (IOException ex) {
			// TODO: EXCEPTION: Lanzar IO y FileNotFound
			ex.printStackTrace();
			return;
		} 
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dataaccess.cache.ICacheDataSource#savePage(int, org.gvsig.fmap.dataaccess.cache.PageBuffer)
	 */
	public void savePage(PageBandBuffer pageBuffer) throws IOException {
		tempDirectoryPath = RasterLibrary.getTemporalPath();
		String fileName = tempDirectoryPath + File.separator + id;

		File f = new File(fileName);
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
		save(dos, pageBuffer);
		dos.close();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataaccess.cache.ICacheDataSource#delete()
	 */
	public void delete() {
		String fileName = tempDirectoryPath + File.separator + id;
		File f = new File(fileName);
		if (f.exists()) {
			f.delete();
		}
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.raster.dataaccess.cache.ICacheDataSource#getPath()
	 */
	public String getPath() {
		return tempDirectoryPath + File.separator + id;
	}
	
	//TODO: TEST: Implementar y probar el cacheado para tipos de datos != de byte
	/**
	 * Tilea un raster en disco para que sea accesible por la caché.
	 * @param fileName Nombre del fichero a tilear
	 * @param pageLines Número de líneas de cada tile
	 * @throws IOException
	 * @throws RasterDriverException 
	 * @throws NotSupportedExtensionException 
	 */
	public void cachear(String fileName, int pageLines)throws IOException, NotSupportedExtensionException, RasterDriverException, InterruptedException{
		if (id == null)
			id = Long.toString(System.currentTimeMillis());

		RasterDataset grf = RasterDataset.open(null, fileName);

		// Creamos un BandList con todas las bandas del fichero
		BandList bandList = new BandList();
		for (int i = 0; i < grf.getBandCount(); i++) {
			try {
				Band band = new Band(grf.getFName(), i, grf.getDataType()[i]);
				bandList.addBand(band, i);
				bandList.addDrawableBand(i, i);
			} catch (BandNotFoundInListException ex) {
				// No añadimos la banda
			}
		}

		int pages = (int) Math.ceil(grf.getHeight() / pageLines);

		tempDirectoryPath = RasterLibrary.getTemporalPath();

		PageBandBuffer pageBuffer = new PageBandBuffer(grf.getDataType()[0], grf.getWidth(), pageLines, grf.getBandCount(), true, 0);
		int y = 0;
		for (int i = 0; i < pages; i++) {
			grf.getWindowRaster(0, y, grf.getWidth(), pageLines, bandList, pageBuffer);
			String outFileName = tempDirectoryPath + File.separator + id + "-" + i;

			File f = new File(outFileName);
			DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
			save(dos, pageBuffer);
			dos.close();
			y += pageLines;
		}
		
		//Solo para pruebas
		//convertFromByteFileToTif(grf, pageBuffer, pageLines);
	}
	
	/**
	 * Función para pruebas.
	 * Convierte los ficheros generados por la función cachear en ficheros tif para comprobar que están
	 * bien generados.
	 * @param grf
	 * @param pageBuffer
	 * @param pageLines
	 * @throws IOException
	 * @deprecated Este método no se usa pero es necesario para testeo
	 */
	public void convertFromByteFileToTif(RasterDataset grf, PageBandBuffer pageBuffer, int pageLines)throws IOException, InterruptedException {
		int w = pageBuffer.getWidth();
		int h = pageBuffer.getHeight();
		int pages = (int) Math.ceil(grf.getHeight() / pageLines);

		GeoRasterWriter grw = null;
		PageBandBuffer pageBuffer2 = new PageBandBuffer(grf.getDataType()[0], grf.getWidth(), pageLines, grf.getBandCount(), true, 0);
		for (int i = 0; i < pages; i++) {
			String inFileName = tempDirectoryPath + File.separator + id + "-" + i;
			File f = new File(inFileName);
			DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(f)));
			byte[] b;
			switch (pageBuffer.getDataType()) {
				case 0:
					b = new byte[pageBuffer.getWidth() * pageBuffer.getHeight() * pageBuffer.getBandCount()];
					dis.readFully(b);
					for (int iBand = 0; iBand < pageBuffer.getBandCount(); iBand++) {
						for (int line = 0; line < pageBuffer.getHeight(); line++) {
							byte[] linea = new byte[w];
							for (int d = 0; d < linea.length; d++)
								linea[d] = b[(iBand * w * h) + (line * w) + d];
							pageBuffer2.setLineInBandByte(linea, line, iBand);
						}
					}
					break;
			}
			dis.close();
			IDataWriter dataWriter1 = new WriterBufferCompleteServer(pageBuffer2);
			try {
				GeoRasterWriter writer = GeoRasterWriter.getWriter(inFileName + ".tif");
				Params params = writer.getParams();
				int newPosBlockSize = 7;
				Param p = params.getParamById("blocksize");
				for (int j = 0; j < p.list.length; j++) {
					if (p.list[i].equals(String.valueOf(pageLines)))
						newPosBlockSize = j;
				}
				params.changeParamValue("blocksize", new Integer(newPosBlockSize));
				grw = GeoRasterWriter.getWriter(dataWriter1, 
												inFileName + ".tif", 
												pageBuffer2.getBandCount(),
												new AffineTransform(),
												pageBuffer2.getWidth(), 
												pageBuffer2.getHeight(), 
												pageBuffer2.getDataType(), 
												params,
												null);
			} catch (NotSupportedExtensionException e) {
				e.printStackTrace();
			} catch (RasterDriverException e) {
				e.printStackTrace();
			}
			grw.dataWrite();
			grw.writeClose();
		}
	}
	
	/**
	 * Salva un PageBuffer sobre un stream DataOutpuStream dependiendo del tipo de dato del 
	 * buffer.
	 * @param dos DataOutputStream 
	 * @param pageBuffer PageBuffer
	 * @throws IOException
	 */
	private void save(DataOutputStream dos, PageBandBuffer pageBuffer) throws IOException {
		switch (pageBuffer.getDataType()) {
			case IBuffer.TYPE_BYTE:
				for (int iBand = 0; iBand < pageBuffer.getBandCount(); iBand++)
					for (int line = 0; line < pageBuffer.getHeight(); line++)
//						for(int col = 0; col < pageBuffer.getWidth(); col ++)
//							dos.writeByte(pageBuffer.getElemByte(line, col, iBand));
						dos.write(pageBuffer.getLineFromBandByte(line, iBand));
				break;
			case IBuffer.TYPE_SHORT:
				for (int iBand = 0; iBand < pageBuffer.getBandCount(); iBand++)
					for (int line = 0; line < pageBuffer.getHeight(); line++)
						for (int col = 0; col < pageBuffer.getWidth(); col++)
							dos.writeShort(pageBuffer.getElemShort(line, col, iBand));
				break;
			case IBuffer.TYPE_INT:
				for (int iBand = 0; iBand < pageBuffer.getBandCount(); iBand++)
					for (int line = 0; line < pageBuffer.getHeight(); line++)
						for (int col = 0; col < pageBuffer.getWidth(); col++)
							dos.writeInt(pageBuffer.getElemInt(line, col, iBand));
				break;
			case IBuffer.TYPE_FLOAT:
				for (int iBand = 0; iBand < pageBuffer.getBandCount(); iBand++)
					for (int line = 0; line < pageBuffer.getHeight(); line++)
						for (int col = 0; col < pageBuffer.getWidth(); col++)
							dos.writeFloat(pageBuffer.getElemFloat(line, col, iBand));
				break;
			case IBuffer.TYPE_DOUBLE:
				for (int iBand = 0; iBand < pageBuffer.getBandCount(); iBand++)
					for (int line = 0; line < pageBuffer.getHeight(); line++)
						for (int col = 0; col < pageBuffer.getWidth(); col++)
							dos.writeDouble(pageBuffer.getElemDouble(line, col, iBand));
				break;
		}
	}
	
	/**
	 * Carga un PageBuffer desde un stream DataInputStream dependiendo del tipo de dato del 
	 * buffer.
	 * @param dis DataInputStream
	 * @param pageBuffer PageBuffer
	 * @throws IOException
	 */
	private void read(DataInputStream dis, PageBandBuffer pageBuffer) throws IOException {
		int w = pageBuffer.getWidth();
		int h = pageBuffer.getHeight();
		int wxh = w * h;
		int j = 0;
		switch (pageBuffer.getDataType()) {
			case IBuffer.TYPE_BYTE:
				byte[] b = new byte[pageBuffer.getWidth() * pageBuffer.getHeight() * pageBuffer.getBandCount()];
				dis.readFully(b);
				// while(j < b.length){ b[j] = dis.readByte(); j ++; }
				for (int iBand = 0; iBand < pageBuffer.getBandCount(); iBand++) {
					for (int line = 0; line < pageBuffer.getHeight(); line++) {
						byte[] linea = new byte[w];
						for (int d = 0; d < linea.length; d++)
							linea[d] = b[(iBand * wxh) + (line * w) + d];
						pageBuffer.setLineInBandByte(linea, line, iBand);
					}
				}
				break;
			case IBuffer.TYPE_SHORT:
				short[] s = new short[pageBuffer.getWidth() * pageBuffer.getHeight() * pageBuffer.getBandCount()];
				while (j < s.length) {
					s[j] = dis.readShort();
					j++;
				}
				for (int iBand = 0; iBand < pageBuffer.getBandCount(); iBand++) {
					for (int line = 0; line < pageBuffer.getHeight(); line++) {
						short[] linea = new short[w];
						for (int x = 0; x < linea.length; x++)
							linea[x] = s[(iBand * wxh) + (line * w) + x];
						pageBuffer.setLineInBandShort(linea, line, iBand);
					}
				}
				break;
			case IBuffer.TYPE_INT:
				int[] i = new int[pageBuffer.getWidth() * pageBuffer.getHeight() * pageBuffer.getBandCount()];
				while (j < i.length) {
					i[j] = dis.readInt();
					j++;
				}
				for (int iBand = 0; iBand < pageBuffer.getBandCount(); iBand++) {
					for (int line = 0; line < pageBuffer.getHeight(); line++) {
						int[] linea = new int[w];
						for (int x = 0; x < linea.length; x++)
							linea[x] = i[(iBand * wxh) + (line * w) + x];
						pageBuffer.setLineInBandInt(linea, line, iBand);
					}
				}
				break;
			case IBuffer.TYPE_FLOAT:
				float[] f = new float[pageBuffer.getWidth() * pageBuffer.getHeight() * pageBuffer.getBandCount()];
				while (j < f.length) {
					f[j] = dis.readFloat();
					j++;
				}
				for (int iBand = 0; iBand < pageBuffer.getBandCount(); iBand++) {
					for (int line = 0; line < pageBuffer.getHeight(); line++) {
						float[] linea = new float[w];
						for (int x = 0; x < linea.length; x++)
							linea[x] = f[(iBand * wxh) + (line * w) + x];
						pageBuffer.setLineInBandFloat(linea, line, iBand);
					}
				}
				break;
			case IBuffer.TYPE_DOUBLE:
				double[] d = new double[pageBuffer.getWidth() * pageBuffer.getHeight() * pageBuffer.getBandCount()];
				while (j < d.length) {
					d[j] = dis.readDouble();
					j++;
				}
				for (int iBand = 0; iBand < pageBuffer.getBandCount(); iBand++) {
					for (int line = 0; line < pageBuffer.getHeight(); line++) {
						double[] linea = new double[w];
						for (int x = 0; x < linea.length; x++)
							linea[x] = d[(iBand * wxh) + (line * w) + x];
						pageBuffer.setLineInBandDouble(linea, line, iBand);
					}
				}
				break;
		}
	}
	
	//---------------------------------------------------------------
	//TILEADO A BASE DE TIFF: Desechado porque es muy lento
	
	/*public void loadPage(int nPag, PageBuffer pageBuffer) {
		String fileName =  tempDirectoryPath + File.separator + id + "-" + nPag + ".tif";
		File file = new File(id);
		if(!file.exists())
			return; //El trozo no ha sido volcado a disco por lo que no se carga.
		
		GeoRasterFile grf = GeoRasterFile.openFile(null, fileName);
		
		//Creamos un BandList con todas las bandas del fichero
		BandList bandList = new BandList();
		for(int i = 0; i < grf.getBandCount();i++){
			try{
				Band band = new Band(grf.getName(), i, grf.getDataType());
				bandList.addBand(band, i);
			}catch(BandFoundInListException ex){
				//No añadimos la banda
			}
		}
		grf.getWindowRaster(0, 0, grf.getWidth(), grf.getHeight(), bandList, pageBuffer);
	}*/

	
	/*public void savePage(int nPag, PageBuffer pageBuffer) throws IOException {
		createTempDirectory();
		String fileName =  tempDirectoryPath + File.separator + id + "-" + nPag + ".tif";
		IDataWriter dataWriter = new WriterBufferServer(pageBuffer);
		GeoRasterWriter grw = GeoRasterWriter.getWriter(dataWriter, 
														fileName, 
														pageBuffer.getHeight(), //Block size 
														pageBuffer.getBandCount(),
														new Extent(0, 0, pageBuffer.getWidth() - 1, pageBuffer.getHeight() - 1), 
														0, 
														pageBuffer.getWidth(), 
														pageBuffer.getHeight(), 
														pageBuffer.getDataType());
		grw.dataWrite();
		grw.writeClose();
		
	}*/
}
