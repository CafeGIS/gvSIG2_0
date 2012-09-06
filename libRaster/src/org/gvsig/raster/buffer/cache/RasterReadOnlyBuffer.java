package org.gvsig.raster.buffer.cache;

import java.awt.geom.Point2D;
import java.io.FileNotFoundException;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.IBand;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.BandList;
import org.gvsig.raster.dataset.FileNotExistsException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.Extent;

/**
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class RasterReadOnlyBuffer extends RasterBuffer {
	/**
	 * Pagina cargada
	 */
	private IBuffer            page = null;
	private IBuffer            secondPage = null;
	/**
	 * Número de página cargada en IBuffer
	 */
	private int                loadedPage = -1;
	private int                loadedSecondPage = -1;
	

	private int                heightLastPage = 0;
	
	private IRasterDataSource  dataset = null;
	
	private int                bitsPag = 0;
	/**
	 * Número de páginas
	 */
	private int                nPages = 0;
	/**
	 * Lista de bandas
	 */
	private BandList           bandList = null;
	private int[]              drawableBands = null;
	/**
	 * Extensión asociada al buffer
	 */
	private int                minX = 0, minY = 0, maxX = 0, maxY = 0;
	/**
	 * BufferFactory para las cargas de páginas. Lleva asociado un IRasterDatasource de un raster
	 * de disco. 
	 */
	private BufferFactory      bFactory = null;
	
	
	/**
	 * Para extraer el desplazamiento de una dirección (línea de raster) hay que hacer una operación And con 
	 * con la altura de la página -1. Por ejemplo, una página de 16 líneas de altura el desplazamiento será
	 * 16 - 1 = 15 porque 15 en binario es 1111.
	 * 
	 * Si queremos acceder a la linea del raster número 83 (1010011) realizando la operación And con el valor del
	 * desplazamiento obtenemos (0001111 & 1010011 = 0000011), es decir el valor 3 en decimal. Esto quiere decir
	 * que la línea 83 del raster es la 3 de su página. 
	 */
	private int            offset = 1;
	
	private int            bufWidth, bufHeight;
	 
		/**
		 * Constructor. Asigna las variables de inicialización y crea la estructura de 
		 * la caché con los datos pasados.
		 * @param dataType Tipo de dato
		 * @param width Ancho
		 * @param height Alto
		 * @param bandNr Banda
		 * @param orig
		 * @throws RasterDriverException 
		 * @throws NotSupportedExtensionException 
		 * @throws FileNotFoundException 
		 */
	public RasterReadOnlyBuffer(int dataType, int width, int height, int nBand) {
		this.dataType = dataType;
				this.width = width;
				this.height = height;
				this.nBands = nBand;	
	}
	
	/**
	 * Asigna los parametros relativos al buffer. Estos son el nombre del fichero asociado 
	 * y la extensión asignada
	 * @param fileName Nombre del fichero asociado
	 * @param ext Extensión
	 */
	public void setBufferParams(IRasterDataSource datasource, Extent ext, BandList bandList) 
		throws InvalidSetViewException, FileNotExistsException, NotSupportedExtensionException  {
		this.bandList = bandList;
		this.dataset = dataset.newDataset();
				
		if(	ext.minX() < dataset.getExtent().minX() || ext.minY() < dataset.getExtent().minY() ||
			ext.maxX() > dataset.getExtent().maxX() || ext.maxY() > dataset.getExtent().maxY())
			throw new InvalidSetViewException("");
		
		Point2D p1 = dataset.worldToRaster(new Point2D.Double(minX, maxY));
		Point2D p2 = dataset.worldToRaster(new Point2D.Double(maxX, minY));
		
		this.minX = (int)p1.getX();
		this.minY = (int)p1.getY();
		this.maxX = (int)p2.getX();
		this.maxY = (int)p2.getY();
		
		init();
	}
			
	/**
	 * Asigna los parametros relativos al buffer. Estos son el nombre del fichero asociado 
	 * y la extensión asignada
	 * @param dataset Dataset del fichero asociado
	 * @param minX Coordenada pixel X mínima
	 * @param minY Coordenada pixel Y mínima
	 * @param maxX Coordenada pixel X máxima
	 * @param maxY Coordenada pixel Y máxima
	 * @exception InvalidSetViewException
	 * @exception FileNotExistsException
	 * @exception NotSupportedExtensionException
	 */
	public void setBufferParams(IRasterDataSource dataset, int minX, int minY, int maxX, int maxY, BandList bandList) 
		throws InvalidSetViewException, FileNotExistsException, NotSupportedExtensionException {
		this.bandList = bandList;
		this.dataset = dataset.newDataset();
		
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
		
		if((maxX - minX) != width)
			bufWidth = width;
		if((maxX - minX) != width) 
			bufHeight = height;
					
		init();
	}
		
	/**
	 * Acciones de inicialización. 
	 * Crear el buffer de memoria y la lista de bandas, así comno los bits empleados
	 * por cada página de memoria y el desplazamiento que supone para el calculo
	 * de direcciones.
	 */
	private void init() {
		//Calculo de los bits por página
		int h = RasterLibrary.blockHeight; 
		while(h > 1) {
			h >>= 1;
			bitsPag ++;
		}
		
		//Calculo del desplazamiento
		offset = RasterLibrary.blockHeight - 1;    
		
		//Creamos el bufferFactory asociado a la carga de páginas. Cada página se fuerza
		//a que sea cargada en memoria.
		bFactory = new BufferFactory(dataset);
		bFactory.setMemoryBuffer(true);
		bFactory.setDrawableBands(bandList.getDrawableBands());
		
		nPages = (int)Math.ceil((double)height / (double)RasterLibrary.blockHeight);
		double aux = ((double)height / (double)RasterLibrary.blockHeight);
		heightLastPage = (int)((aux - (int)aux) * RasterLibrary.blockHeight);
		
		drawableBands = new int[dataset.getBandCount()];
			for (int i = 0; i < drawableBands.length; i++) 
			drawableBands[i] = i;
	}
	
	/**
	 * Asigna la lista de bandas
	 * @param bandList
	 */
	public void addDrawableBands(int[] db) {
		if(db == null || db.length > this.bandList.getBandCount())
			return;
		this.bandList.setDrawableBands(db);
		bFactory = new BufferFactory(dataset);
		bFactory.setMemoryBuffer(true);
		bFactory.setDrawableBands(bandList.getDrawableBands());
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.driver.IBuffer#getBandCount()
	 */
	public int getBandCount() {
		return this.bandList.getDrawableBandsCount();
	}
	
	
	/**
	 * Calcula la extensión de las páginas en coordenadas del mundo real
	 * @param dataset Dataset
	 * @param nPages Número de páginas en que se divide el raster
	 * @deprecated
	 * @return Extensión de cada página
	 */
	public Extent[] calcExtentPages(RasterDataset dataset, int nPages) {
		Extent datasetExtent = dataset.getExtent();
		double h = (RasterLibrary.blockHeight * dataset.getExtent().height()) / dataset.getHeight();
		Extent[] ext = new Extent[nPages];
		
		double minX = datasetExtent.getMin().getX();
		double maxX = datasetExtent.getMax().getX();
		double maxY = datasetExtent.getMax().getY();
		double minY = maxY - h;
		for (int i = 0; i < ext.length; i++) {
			ext[i] = new Extent(minX, maxY, maxX, minY);
			maxY = minY;
			minY -= h;
			if(minY < datasetExtent.minY())
				minY = datasetExtent.minY();
		}
		return ext;
	}
	
	public void malloc(int dataType, int width, int height, int bandNr) {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.driver.IBuffer#isBandSwitchable()
	 */
	public boolean isBandSwitchable() {
		return false;
	}

	/**
	 * Carga las páginas de memoria cuando hay un fallo en el acceso al dato
	 * @param pag Número de página a cargar
	 */
	private void loadPage(int pag) {
		try {
			if(page == null) {
				if(bufWidth == 0 && bufHeight == 0) {
					if(pag == nPages - 1)
						bFactory.setAreaOfInterest(minX, (RasterLibrary.blockHeight * pag) + minY, maxX - minX, heightLastPage);
					else 
						bFactory.setAreaOfInterest(minX, (RasterLibrary.blockHeight * pag) + minY, maxX - minX, RasterLibrary.blockHeight);
				} else {
					int hPage = Math.round((RasterLibrary.blockHeight * (maxY - minY)) / bufHeight);
					if(pag == nPages - 1) {
						int hLastPage = Math.round((heightLastPage * bufHeight) / (maxY - minY));
						bFactory.setAreaOfInterest(minX, (hPage * pag) + minY, maxX - minX, hLastPage, bufWidth, heightLastPage);
					} else 
						bFactory.setAreaOfInterest(minX, (hPage * pag) + minY, maxX - minX, hPage, bufWidth, RasterLibrary.blockHeight);
				}
				page = bFactory.getRasterBuf();
				loadedPage = pag;

				if(bufWidth == 0 && bufHeight == 0) {
					if((pag + 1) == nPages - 1)
						bFactory.setAreaOfInterest(minX, (RasterLibrary.blockHeight * (pag + 1)) + minY, maxX - minX, heightLastPage);
					else 
						bFactory.setAreaOfInterest(minX, (RasterLibrary.blockHeight * (pag + 1)) + minY, maxX - minX, RasterLibrary.blockHeight);
				} else {
					int hPage = Math.round((RasterLibrary.blockHeight * (maxY - minY)) / bufHeight);
					if(pag == nPages - 1) {
						int hLastPage = Math.round((heightLastPage * bufHeight) / (maxY - minY));
						bFactory.setAreaOfInterest(minX, (hPage * (pag + 1)) + minY, maxX - minX, hLastPage, bufWidth, heightLastPage);
					} else 
						bFactory.setAreaOfInterest(minX, (hPage * (pag + 1)) + minY, maxX - minX, hPage, bufWidth, RasterLibrary.blockHeight);
				}
				secondPage = bFactory.getRasterBuf();
				loadedSecondPage = pag + 1;
			} else {
				if(pag == loadedSecondPage)
					switchPage();
				else {
					switchPage();
					if(bufWidth == 0 && bufHeight == 0) {
						if(pag == nPages - 1)
							bFactory.setAreaOfInterest(minX, (RasterLibrary.blockHeight * pag) + minY, maxX - minX, heightLastPage);
						else 
							bFactory.setAreaOfInterest(minX, (RasterLibrary.blockHeight * pag) + minY, maxX - minX, RasterLibrary.blockHeight);
					} else {
						int hPage = Math.round((RasterLibrary.blockHeight * (maxY - minY)) / bufHeight);
						if(pag == nPages - 1) {
							int hLastPage = Math.round((heightLastPage * bufHeight) / (maxY - minY));
							bFactory.setAreaOfInterest(minX, (hPage * pag) + minY, maxX - minX, hLastPage, bufWidth, heightLastPage);
						} else 
							bFactory.setAreaOfInterest(minX, (hPage * pag) + minY, maxX - minX, hPage, bufWidth, RasterLibrary.blockHeight);
					}
					page = bFactory.getRasterBuf();
					loadedPage = pag;
				}
			}

		} catch (InterruptedException e) {

		} catch (InvalidSetViewException e) {

		} catch (RasterDriverException e) {

		} finally {
			//Al interrumpir la carga de datos de un buffer o producirse una excepción page quedará como null. 
			//Tenemos que asignarle algún valor para que las últimas consultas no den error.
			if(page ==  null) {
				page = RasterBuffer.getBuffer(dataType, width, height, nBands, true);
				loadedPage = pag;
			}
		}
	}


	/**
	 * Intercambia las dos páginas de memoria
	 */
	private void switchPage() {
		IBuffer aux = secondPage;
		secondPage = page;
		page = aux;
		int auxint = loadedSecondPage;
		loadedSecondPage = loadedPage;
		loadedPage = auxint;
	}
			 
	//*********************************************************
	
	public byte[][] getLineByte(int line) {
		int pag = line >> bitsPag;
		if(pag != loadedPage)
			loadPage(pag);
		return page.getLineByte(line & offset);
	}

	public short[][] getLineShort(int line) {
		int pag = line >> bitsPag;
		if(pag != loadedPage)
			loadPage(pag);
		return page.getLineShort(line & offset);
	}

	public int[][] getLineInt(int line) {
		int pag = line >> bitsPag;
		if(pag != loadedPage)
			loadPage(pag);
		return page.getLineInt(line & offset);
	}

	public float[][] getLineFloat(int line) {
		int pag = line >> bitsPag;
		if(pag != loadedPage)
			loadPage(pag);
		return page.getLineFloat(line & offset);
	}

	public double[][] getLineDouble(int line) {
		int pag = line >> bitsPag;
		if(pag != loadedPage)
			loadPage(pag);
		return page.getLineDouble(line & offset);
	}

	//*********************************************************
	
	public byte[] getLineFromBandByte(int line, int band) {
		int pag = line >> bitsPag;
		if(pag != loadedPage)
			loadPage(pag);
		return page.getLineFromBandByte(line & offset, band);
	}

	public short[] getLineFromBandShort(int line, int band) {
		int pag = line >> bitsPag;
		if(pag != loadedPage)
			loadPage(pag);
		return page.getLineFromBandShort(line & offset, band);
	}

	public int[] getLineFromBandInt(int line, int band) {
		int pag = line >> bitsPag;
		if(pag != loadedPage)
			loadPage(pag);
		return page.getLineFromBandInt(line & offset, band);
	}

	public float[] getLineFromBandFloat(int line, int band) {
		int pag = line >> bitsPag;
		if(pag != loadedPage)
			loadPage(pag);
		return page.getLineFromBandFloat(line & offset, band);
	}

	public double[] getLineFromBandDouble(int line, int band) {
		int pag = line >> bitsPag;
		if(pag != loadedPage)
			loadPage(pag);
		return page.getLineFromBandDouble(line & offset, band);
	}

	//*********************************************************
	
	public byte getElemByte(int line, int col, int band) {
		int pag = line >> bitsPag;
		if(pag != loadedPage)
			loadPage(pag);
		return page.getElemByte(line & offset, col, band);
	}

	public short getElemShort(int line, int col, int band) {
		int pag = line >> bitsPag;
		if(pag != loadedPage)
			loadPage(pag);
		return page.getElemShort(line & offset, col, band);
	}

	public int getElemInt(int line, int col, int band) {
		int pag = line >> bitsPag;
		if(pag != loadedPage)
			loadPage(pag);
		return page.getElemInt(line & offset, col, band);
	}

	public float getElemFloat(int line, int col, int band) {
		int pag = line >> bitsPag;
		if(pag != loadedPage)
			loadPage(pag);
		return page.getElemFloat(line & offset, col, band);
	}

	public double getElemDouble(int line, int col, int band) {
		int pag = line >> bitsPag;
		if(pag != loadedPage)
			loadPage(pag);
		return page.getElemDouble(line & offset, col, band);
	}
	
	//*********************************************************

	public void getElemByte(int line, int col, byte[] data) {
		int pag = line >> bitsPag;
		if(pag != loadedPage)
			loadPage(pag);
		page.getElemByte(line & offset, col, data);
	}

	public void getElemShort(int line, int col, short[] data) {
		int pag = line >> bitsPag;
		if(pag != loadedPage)
			loadPage(pag);
		page.getElemShort(line & offset, col, data);
	}

	public void getElemInt(int line, int col, int[] data) {
		int pag = line >> bitsPag;
		if(pag != loadedPage)
			loadPage(pag);
		page.getElemInt(line & offset, col, data);
	}

	public void getElemFloat(int line, int col, float[] data) {
		int pag = line >> bitsPag;
		if(pag != loadedPage)
			loadPage(pag);
		page.getElemFloat(line & offset, col, data);
	}

	public void getElemDouble(int line, int col, double[] data) {
		int pag = line >> bitsPag;
		if(pag != loadedPage)
			loadPage(pag);
		page.getElemDouble(line & offset, col, data);
	}
	
	//***********************************************
		//Obtiene una banda entera
		
		public IBand getBand(int band){
			return null;
		}

		/*
		 *  (non-Javadoc)
		 * @see org.gvsig.fmap.driver.IBuffer#getBandBuffer(int)
		 */
		public IBuffer getBandBuffer(int iBand){
			return null;
		}
		
	public void replicateBand(int orig, int dest) {
	}
		
	/*
		 *  (non-Javadoc)
		 * @see org.gvsig.fmap.dataaccess.buffer.RasterBuffer#switchBands(int[])
		 */
		public void switchBands(int[] bandPosition){
			
		}

	//*********************************************************
			
		/*
		 *  (non-Javadoc)
		 * @see org.gvsig.fmap.driver.IBuffer#cloneBuffer()
		 */
		public IBuffer cloneBuffer(){
			return null;
		}

		
		/*
		 *  (non-Javadoc)
		 * @see org.gvsig.fmap.driver.IBuffer#mallocOneBand(int, int, int, int)
		 */
		public void mallocOneBand(int dataType, int width, int height, int band) {
			
	}

		/*
		 *  (non-Javadoc)
		 * @see org.gvsig.fmap.driver.IBuffer#copyBand(int, org.gvsig.fmap.dataaccess.buffer.IBand)
		 */
	public void copyBand(int nBand, IBand band) {
				
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.driver.IBuffer#assignBand(int, org.gvsig.fmap.dataaccess.buffer.IBand)
	 */
	public void assignBand(int nBand, IBand band) {
				
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.driver.IBuffer#createBand(byte)
	 */
	public IBand createBand(byte defaultValue) {
		
		return null;
	}

	public void assignBandToNotValid(int iBand) {}
	public void assign(int band, byte value) {}
	public void assign(int band, short value) {}
	public void assign(int band, int value) {}
	public void assign(int band, float value) {}
	public void assign(int band, double value) {}
	public void interchangeBands(int band1, int band2) {}
	public void setElem(int line, int col, int band, byte data) {}
	public void setElem(int line, int col, int band, short data) {}
	public void setElem(int line, int col, int band, int data) {}
	public void setElem(int line, int col, int band, float data) {}
	public void setElem(int line, int col, int band, double data) {}
	public void setElemByte(int line, int col, byte[] data) {}
	public void setElemDouble(int line, int col, double[] data) {}
	public void setElemFloat(int line, int col, float[] data) {}
	public void setElemInt(int line, int col, int[] data) {}
	public void setElemShort(int line, int col, short[] data) {}
	public void setLineByte(byte[][] data, int line) {}
	public void setLineDouble(double[][] data, int line) {}
	public void setLineFloat(float[][] data, int line) {}
	public void setLineInBandByte(byte[] data, int line, int band) {}
	public void setLineInBandDouble(double[] data, int line, int band) {}
	public void setLineInBandFloat(float[] data, int line, int band) {}
	public void setLineInBandInt(int[] data, int line, int band) {}
	public void setLineInBandShort(short[] data, int line, int band) {}
	public void setLineInt(int[][] data, int line) {}
	public void setLineShort(short[][] data, int line) {}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IBuffer#free()
	 */
	public void free() {
		if(page != null)
			page.free();
		if(bFactory != null)
			bFactory.free();
		bFactory = null;
		dataset = null;
		bandList = null;
		System.gc();
	}

}