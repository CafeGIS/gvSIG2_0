
/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.Extent;

/**
 * <P>
 * Esta clase representa a la cache raster. Consta de una ser�e de p�ginas (CachePages)
 * que son buffers con los bloques de datos cacheados en un instante dado. Esta cache tiene 
 * una estructura en forma de array donde cada elemento es una p�gina de cach�. Las p�ginas 
 * se agrupan en bloques de N p�ginas denominados grupos. La variable nelemsGroup contiene 
 * el n�mero de p�ginas de cada grupo.
 * </P>
 * <P>
 * La politica de reemplazo es que una p�gina siempre va a un conjunto determinado por el calculo
 * cto = pag % nelemsGroup. Si hay hueco vac�o en el grupo se a�adir� en el siguiente hueco pero
 * si hay que reeplazar se reeplazar� la p�gina que m�s tiempo haga su �ltimo acceso. 
 * </P>
 * <P>
 * Esta cach� lleva el control de que p�ginas est�n cargadas a trav�s de un array de de booleanos
 * donde cada elemento representa a una p�gina de datos del raster. Si el elemento en la posici�n
 * N de dicho array es true significa que la p�gina est� cacheada. Si es false no lo estar�. 
 * </P>
 * <P>
 * La p�gina de datos actualmente accedida debe estar cacheada por lo que est� clase debe llevar
 * el control de que p�gina se est� accediendo o a sido accedida por �ltima vez. La variable que
 * nos dice que p�gina est� siendo accedida es loadPage y pageBuffer ser� la variable que apunta
 * al buffer de esta p�gina. Cuando se cambia de p�gina en un acceso estas dos variables deben
 * cambiar a sus nuevos valores.
 * </P>
 * <P>
 * Otro par�metro que controla esta clase es que p�gina de cada grupo ha sido accedido con mayor 
 * frecuencia. Esto es �til porque siempre se reemplaza la p�gina de un grupo que haga m�s tiempo
 * que haya sido accedida bajo la premisa de que "las p�ginas con accesos recientes tienen mayor 
 * probabilidad de volver a ser usadas". Esto es controlado por la variable lastAccess que es
 * usada para implementar el algoritmo LRU. Cada vez que una p�gina de un grupo es accedida su 
 * contador de lastAccess se pone a 0. El resto de contadores de las p�ginas del grupo se incrementa
 * en uno. Siempre se sustituye la p�gina del grupo con valor m�s grande en el contador.
 * </P>
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class Cache{
	/**
	 * Cada elemento es una p�gina del raster y dice si est� cacheada o no.
	 */
	private boolean[] cacheada = null;
	/**
	 * N�mero de p�gina cargada en accessPage.
	 */
	private int numberInAccessPage = -1;
	/**
	 * Buffer de datos de la p�gina actualmente accedida.
	 */
	private PageBuffer accessPage = null;
	/**
	 * P�ginas de cach�
	 */
	private PageBuffer[] page = null;
	/**
	 * Cada elemento del array es una p�gina de cach� y contiene el n�mero de p�gina cargada en 
	 * esa posici�n.
	 */
	private int[]	pageNumberInCache = null;
	/**
	 * P�ginas de la cach�.
	 */
	private CacheStruct cacheStruct = null;
	/**
	 * �ltimo acceso a las p�ginas de cada grupo.
	 */
	private int[][] lastAccess = null;
	/**
	 * Cada elemento representa una p�gina de cach� y dice si esta ha sido modificada desde que
	 * fu� cargada en cach� o no.
	 */
	private boolean[] modified = null;
	/**
	 * P�ginas de disco
	 */
	private HddPage[] hddPage = null;
	
	/**
	 * Inicializamos la variables. Para ello creamos el objeto Cache que contendr� todos los 
	 * par�metros necesarios para crear el array que controla los accesos m�s recientes y el
	 * array que dice si una p�gina est� en memoria o no.
	 * @param nBands N�mero de bandas
	 * @param dataType Tipo de dato de la p�gina
	 * @param dataSourceWidth ancho de la fuente de datos
	 * @param driverParam par�metro para el driver
	 */
	public Cache(int nBands, int dataType, int dataSourceWidth, int dataSourceHeight, Object driverParam)  throws FileNotFoundException, NotSupportedExtensionException, RasterDriverException {
		if(driverParam instanceof String) {
			File file = new File(((String)driverParam));
			if(!file.exists())
				throw new FileNotFoundException("I can't make read only cache structure.");
		}
		
		init(nBands, dataType, dataSourceWidth, dataSourceHeight);
		
		//Creamos las estructuras de las p�ginas de disco
		hddPage = new HddPage[cacheStruct.getNTotalPags()];
				
		RasterDataset dataset = RasterDataset.open(null, driverParam);
		Extent[] extentList = calcExtentPages(dataset, hddPage.length);
		
		for (int iPage = 0; iPage < hddPage.length; iPage++)
			hddPage[iPage] = new HddPage(iPage, dataset, extentList[iPage]);
		
		//Creamos las p�ginas de memoria
		for(int i = 0; i < cacheStruct.getNPags(); i++) {
		  page[i] = new PageBuffer(dataType, dataSourceWidth, cacheStruct.getHPag(), nBands, true, i);
		  page[i].setHddPages(hddPage);
		}
	}
	
	/**
	 * Calcula la extensi�n de las p�ginas en coordenadas del mundo real
	 * @param dataset Dataset
	 * @param nPages N�mero de p�ginas en que se divide el raster
	 * @return Extensi�n de cada p�gina
	 */
	private Extent[] calcExtentPages(RasterDataset dataset, int nPages) {
		Extent datasetExtent = dataset.getExtent();
		double h = cacheStruct.getHPag() * (dataset.getExtent().height() / dataset.getHeight());
		Extent[] ext = new Extent[nPages];
		
		double minX = datasetExtent.getMin().getX();
		double maxX = datasetExtent.getMax().getX();
		double maxY = datasetExtent.getMax().getY();
		double minY = maxY - h;
		for (int i = 0; i < ext.length; i++) {
			ext[0] = new Extent(minX, maxY, maxX, minY);
			maxY = minY;
			minY -= h;
		}
		return ext;
	}
	
	/**
	 * Inicializamos la variables. Para ello creamos el objeto Cache que contendr� todos los 
	 * par�metros necesarios para crear el array que controla los accesos m�s recientes y el
	 * array que dice si una p�gina est� en memoria o no.
	 * @param nBands N�mero de bandas
	 * @param dataType Tipo de dato de la p�gina
	 * @param dataSourceWidth ancho de la fuente de datos
	 */
	public Cache(int nBands, int dataType, int dataSourceWidth, int dataSourceHeight) {
		init(nBands, dataType, dataSourceWidth, dataSourceHeight);
		
		//Creamos las estructuras de las p�ginas de disco
		hddPage = new HddPage[cacheStruct.getNTotalPags()];
		for (int iPage = 0; iPage < hddPage.length; iPage++)
			hddPage[iPage] = new HddPage(iPage, getNBands());
		
		//Creamos las p�ginas de memoria
		for(int i = 0; i < cacheStruct.getNPags(); i++) {
		  page[i] = new PageBuffer(dataType, dataSourceWidth, cacheStruct.getHPag(), nBands, true, i);
		  page[i].setHddPages(hddPage);
		}
	}
	
	/**
	 * Contructor
	 * @param cacheStruct
	 */
	public Cache(CacheStruct cacheStruct, int dataSourceWidth) {
		this.cacheStruct = cacheStruct;
		
		//Inicializamos la antig�edad de acceso  
		lastAccess = new int[cacheStruct.getNGroups()][cacheStruct.getPagsPerGroup()];
		
		//Creamos el buffer de la cach�
		page = new PageBuffer[cacheStruct.getNPags()];
		pageNumberInCache = new int[cacheStruct.getNPags()];
		modified = new boolean[cacheStruct.getNPags()];
		cacheada = new boolean[cacheStruct.getNTotalPags()];
		
		//Creamos las estructuras de las p�ginas de disco
		hddPage = new HddPage[cacheStruct.getNTotalPags()];
		for (int iPage = 0; iPage < hddPage.length; iPage++)
			hddPage[iPage] = new HddPage(iPage, getNBands());
		
		//Creamos las p�ginas de memoria
		for(int i = 0; i < cacheStruct.getNPags(); i++) {
		  page[i] = new PageBuffer(cacheStruct.getDataType(), dataSourceWidth, cacheStruct.getHPag(), cacheStruct.getNBands(), true, i);
		  page[i].setHddPages(hddPage);
		}
		
		initStructs();
	}
	
	/**
	 * 
	 * @param nBands
	 * @param dataType
	 * @param dataSourceWidth
	 * @param dataSourceHeight
	 */
	private void init(int nBands, int dataType, int dataSourceWidth, int dataSourceHeight) {
		//Creamos la estructura de la cach�
		cacheStruct = new CacheStruct(nBands, dataType, dataSourceWidth, dataSourceHeight);
		
		//Inicializamos la antig�edad de acceso  
		lastAccess = new int[cacheStruct.getNGroups()][cacheStruct.getPagsPerGroup()];
		
		//Creamos el buffer de la cach�
		page = new PageBuffer[cacheStruct.getNPags()];
		pageNumberInCache = new int[cacheStruct.getNPags()];
		modified = new boolean[cacheStruct.getNPags()];
		cacheada = new boolean[cacheStruct.getNTotalPags()];
		
		this.initStructs();
	}
		
	/**
	 * Inicializa las estructuras de mantenimiento de cach�. Estas son array de paginas
	 * accedidas por �ltima vez, numeros de p�gina cargadas en cada posici�n de cach�, 
	 * array con la informaci�n de p�gina modificada y array que dice si una p�gina de disco
	 * est� cacheada o no.
	 */
	private void initStructs() {
		for(int i = 0; i < cacheStruct.getNGroups(); i ++)
			for(int j = 0; j < cacheStruct.getPagsPerGroup(); j ++)
				lastAccess[i][j] = -1;
		
		for(int i = 0; i < cacheada.length; i++)
			cacheada[i] = false;
		
		for(int i = 0; i < cacheStruct.getNPags(); i++){
			pageNumberInCache[i] = -1;
			modified[i] = false;
		}
	}
	
	/**
	 * Limpia los trozos de cach� en disco. Despu�s del llamar a este 
	 * m�todo no puede volver a usarse esta cach�.
	 * @throws IOException 
	 */
	public void clearCache(int nBands) throws IOException {
		if(hddPage == null)
			return;
		for (int i = 0; i < hddPage.length; i++) 
			hddPage[i].deletePage(nBands);
	}
	
	/**
	 * Obtiene el n�mero de bits por p�gina para poder calcular el desplazamiento binario
	 * de la direcci�n de acceso de la petici�n. Es decir si se solicita un dato en la l�nea
	 * 36 (en decimal) del raster 100100(en binario) y el desplazamiento es 4 bits el n�mero de 
	 * p�gina resultante ser� 10(en binario) 2 (en decimal)
	 * @return
	 */
	public int getBitsPag() {
		return cacheStruct.getBitsPag();
	}

	/**
	 * Obtiene la altura de la p�gina de cache en l�neas. 
	 * @return N�mero de l�neas de altura de p�gina.
	 */
	public int getHPag() {
		return cacheStruct.getHPag();
	}
	
	/**
	 * Array de booleanos donde cada elemento es una p�gina del raster y dice si la p�gina 
	 * est� cacheada o no.
	 * @return true si la p�gina est� en cach� y false si no lo est�
	 */
	public boolean isInCache(int nPag) {
		if(nPag < 0 || nPag >= cacheada.length)
			return false;
		return cacheada[nPag];
	}
	
	/**
	 * Marca una p�gina como cargada en cach�.
	 * @param nPag N�mero de p�gina a marcar.
	 */
	public void setPageAsLoadInCache(int nPag) {
		if(nPag < 0 || nPag >= cacheada.length)
			return;
		cacheada[nPag] = true;
	}
	
	/**
	 * Desmarca una p�gina como cargada en cach�.
	 * @param nPag N�mero de p�gina a desmarcar.
	 */
	public void setPageAsNotLoadInCache(int nPag) {
		if(nPag < 0 || nPag >= cacheada.length)
			return;
		cacheada[nPag] = false;
	}
	
	/**
	 * Obtiene el n�mero de p�gina del raster cargada en una p�gina de cach� especificada
	 * en el par�metro. 
	 * @param nCachePage N�mero de p�gina de cach� de la que se quiere saber que p�gina del 
	 * raster hay cargada.
	 * @return N�mero de p�gina del raster
	 */
	public int getRasterPageNumberInPosition(int nCachePage) {
		return pageNumberInCache[nCachePage];
	}
	
	/**
	 * Obtiene el n�mero de p�gina del raster cargada en una p�gina de cach� especificada
	 * en el par�metro. 
	 * @param group Grupo en el que se encuentra la p�gina
	 * @param posInGroup Posici�n dentro del grupo en el que est� la p�gina
	 * @return N�mero de p�gina del raster
	 */
	public int getRasterPageNumberInPosition(int group, int posInGroup) {
		return pageNumberInCache[group * getPagsPerGroup() + posInGroup];
	}
	
	/**
	 * Asigna el n�mero de p�gina del raster cargada en una p�gina de cach� especificada.
	 * @param nCachePage N�mero de p�gina de cach� 
	 * @param nRasterPage N�mero de p�gina de raster a asignar
	 */
	public void setRasterPageNumberInPosition(int nCachePage, int nRasterPage) {
		pageNumberInCache[nCachePage] = nRasterPage;
	}
	
	/**
	 * Asigna el n�mero de p�gina del raster cargada en una p�gina de cach� especificada.
	 * @param nCachePage N�mero de p�gina de cach� 
	 * @param nRasterPage N�mero de p�gina de raster a asignar
	 */
	public void setRasterPageNumberInPosition(int group, int posInGroup, int nRasterPage) {
		pageNumberInCache[group * getPagsPerGroup() + posInGroup] = nRasterPage;
	}
	
	/**
	 * Obtiene el n�mero de p�gina de cach� donde est� cargada la p�gina del raster
	 * que se ha pasado por par�metro.
	 * @param pag P�gina del raster
	 * @return N�mero de p�gina del raster
	 */
	public int getNumberCachePageFromNumberRasterPage(int pag) {
		int group = pag % getNGroups(); 
		for(int i = 0; i < getPagsPerGroup(); i++)
			if(pageNumberInCache[group + i] == pag)
				return (group * getPagsPerGroup() + i);
		return -1;
	}
	
	/**
	 * Obtiene el n�mero de p�gina de cach� (en formato grupo/p�gina dentro del grupo) 
	 * donde est� cargada la p�gina del raster que se ha pasado por par�metro.
	 * @param pag P�gina del raster
	 * @return N�mero de p�gina del raster
	 */
	public int[] getNumberGroupFromNumberRasterPage(int pag) {
		int group = pag % getNGroups();
		for(int i = 0; i < getPagsPerGroup(); i++)
			if(pageNumberInCache[(group * getPagsPerGroup()) + i] == pag)
				return new int[]{group, i};
		return null;
	}
	
	/**
	 * Obtiene el array que contiene los valores de la antig�edad del acceso dentro del
	 * grupo. La primera dimensi�n del array corresponde al n�mero de grupo y la segunda
	 * a los elementos del grupo. El elemento del grupo con un acceso m�s reciente tendr� 
	 * un n�mero menor y el de mayor valor ser� el candidato para la sustituci�n en el pr�ximo
	 * remplazamiento.
	 * @return Array bidimensional con los valores de antig�edad.
	 */
	public int[][] getLastAccess() {
		return lastAccess;
	}

	/**
	 * Asigna un cero en la posici�n del array que contiene la antig�edad de acceso dentro 
	 * del grupo. Esta operaci�n se realiza cada vez que se accede a un dato del la p�gina 
	 * cacheada y significa que es la p�gina m�s recientemente accedida.
	 * @param group N�mero de grupo
	 * @param posInGroup Posici�n de la p�gina dentro del grupo.
	 */
	public void setZeroInLastAccess(int group, int posInGroup) {
		lastAccess[group][posInGroup] = 0;
	}
	
	/**
	 * Obtiene el n�mero de p�gina cargada en el buffer
	 * @return Entero con el n�mero de p�gina
	 */
	public int getNumberInAccessPage() {
		return numberInAccessPage;
	}

	/**
	 * Obtiene el n�mero de p�ginas que tiene cada grupo
	 * @return Entero con el n�mero de p�ginas por grupo 
	 */
	public int getPagsPerGroup() {
		return cacheStruct.getPagsPerGroup();
	}

	/**
	 * Obtiene la p�gina de datos de la posici�n pag
	 * @param pag N�mero de p�gina de cach� a recuperar
	 * @return PageBuffer correspondiente a la p�gina recuperada
	 */
	public PageBuffer getPageBufferFromNumberCachePage(int pag) {
		return page[pag];
	}
	
	/**
	 * Obtiene la p�gina de datos a partir del n�mero de p�gina de raster
	 * @param pag N�mero de p�gina raster a recuperar
	 * @return PageBuffer correspondiente a la p�gina recuperada o null si no est� en cach�
	 */
	public PageBuffer getPageBufferFromNumberRasterPage(int pag) {
		int group = pag % getNGroups();
		for(int i = 0; i < getPagsPerGroup(); i++)
			if(pageNumberInCache[group * getPagsPerGroup() + i] == pag)
				return page[group * getPagsPerGroup() + i];
		return null;
	}
	
	/**
	 * Obtiene la p�gina de datos del grupo definido en el par�metro group y de la 
	 * posici�n pag dentro de ese grupo.
	 * @param group Grupo de la p�gina requerida
	 * @param pag N�mero de p�gina dentro del grupo
	 * @return PageBuffer correspondiente a la p�gina recuperada
	 */
	public PageBuffer getPageBuffer(int group, int posInGroup) {
		return page[group * getPagsPerGroup() + posInGroup];
	}
	
	/**
	 * Obtiene la p�gina de datos actualmente accedida
	 * @return PageBuffer correspondiente a la p�gina que se est� accediendo
	 */
	public PageBuffer getAccessPage() {
		return accessPage;
	}
	
	/**
	 * Asigna el buffer de la p�gina accedida por referencia
	 * @param pb
	 */
	public void setAccessPage(PageBuffer pb, int pagNumber) {
		accessPage = pb;
		numberInAccessPage = pagNumber;
	}
	
	/**
	 * Consulta si una p�gina de cach� ha sido modificada desde que se carg� en cach� o no.
	 * @param nCachePag N�mero de p�gina de cach� (posici�n de esta)
	 * @return true si ha sido modificada y false si no lo ha sido.
	 */
	public boolean isModified(int nCachePag) {
		return modified[nCachePag];
	}

	/**
	 * Consulta si una p�gina de cach� ha sido modificada desde que se carg� en cach� o no.
	 * @param group Grupo en el que se encuentra la p�gina
	 * @param posInGroup Posici�n dentro del grupo en el que est� la p�gina
	 * @return true si ha sido modificada y false si no lo ha sido.
	 */
	public boolean isModified(int group, int posInGroup) {
		return modified[group * getPagsPerGroup() + posInGroup];
	}

	/**
	 * Pone como modificada una p�gina de cach�
	 * @param nCachePag N�mero de p�gina de cach� (posici�n de esta)
	 */
	public void setModify(int nCachePag) {
		modified[nCachePag] = true;	
	}
	
	/**
	 * Pone como modificada una p�gina de cach�
	 * @param group Grupo en el que se encuentra la p�gina
	 * @param posInGroup Posici�n dentro del grupo en el que est� la p�gina
	 */
	public void setModify(int group, int posInGroup) {
		modified[group * getPagsPerGroup() + posInGroup] = true;	
	}
	
	/**
	 * Pone como no modificada una p�gina de cach�
	 * @param nCachePag N�mero de p�gina de cach� (posici�n de esta)
	 */
	public void unsetModify(int nCachePag) {
		modified[nCachePag] = false;	
	}
	
	/**
	 * Pone como no modificada una p�gina de cach�
	 * @param group Grupo en el que se encuentra la p�gina
	 * @param posInGroup Posici�n dentro del grupo en el que est� la p�gina
	 */
	public void unsetModify(int group, int posInGroup) {
		modified[group * getPagsPerGroup() + posInGroup] = false;	
	}
	
	/**
	 * Obtiene el n�mero de p�ginas de la cach�
	 * @return N�mero total de p�ginas de la cach�
	 */
	public int getNPags() {
		return cacheStruct.getNPags();
	}
	
	/**
	 * Obtiene el n�mero de bandas
	 * @return N�mero de bandas
	 */
	public int getNBands() {
		return cacheStruct.getNBands();
	}
	
	/**
	 * Obtiene la estructura de ca cach�
	 * @return CacheStruct
	 */
	public CacheStruct getCacheStruct() {
		return cacheStruct;
	}
	
	/**
	 * Asigna la estructura de ca cach�
	 * @param CacheStruct
	 */
	public void setCacheStruct(CacheStruct cacheStruct) {
		this.cacheStruct = cacheStruct;
	}
	
	/**
	 * Obtiene el n�mero de grupos de cach�
	 * @return N�mero de grupos
	 */
	public int getNGroups() {
		return cacheStruct.getNGroups();
	}
	
	/**
	 * Obtiene el n�mero total de p�ginas del raster
	 * @return N�mero total de p�ginas
	 */
	public int getNTotalPags() {
		return cacheStruct.getNTotalPags();
	}
	
	/**
	 * Obtiene el array con los n�meros de p�gina que hay cargados
	 * en cada bloque de cache
	 * @return array con los n�mero de p�gina
	 */
	public int[] getPageNumberInCache() {
		return this.pageNumberInCache;
	}
	
	/**
	 * Para extraer el desplazamiento de una direcci�n (l�nea de raster) hay que hacer una operaci�n And con 
	 * con la altura de la p�gina -1. Por ejemplo, una p�gina de 16 l�neas de altura el desplazamiento ser�
	 * 16 - 1 = 15 porque 15 en binario es 1111.
	 * 
	 * Si queremos acceder a la linea del raster n�mero 83 (1010011) realizando la operaci�n And con el valor del
	 * desplazamiento obtenemos (0001111 & 1010011 = 0000011), es decir el valor 3 en decimal. Esto quiere decir
	 * que la l�nea 83 del raster es la 3 de su p�gina. 
	 * @return valor del desplazamiento
	 */
	public int getOffset() {
		return cacheStruct.getOffset();
	}
	
	/**
	 * Convierte una p�gina dentro de un grupo de la cach� en un n�mero de 
	 * p�gina de cach�. Por ejemplo, en una cach� de 10 grupos y 5 p�ginas por grupo
	 * la p�gina 2 del grupo 4 devolver� el 23 (5 * 4 + 3 = 23) teniendo en cuenta que
	 * la p�gina y el grupo cero tambi�n cuentan.  
	 * @param group
	 * @param pageInGroup
	 * @return
	 */
	public int convertPageInGroupToPageInCache(int group, int pageInGroup) {
		return (group * cacheStruct.getPagsPerGroup() + pageInGroup);
	}

	/**
	 * Actualiza los �ltimos accesos del grupo pasado por par�metro. Se incrementar� 
	 * uno en todas las p�ginas del conjunto a excepci�n de las que valen -1 ya que 
	 * esto significa que no hay p�gina cargada en dicha posici�n.
	 * @param group N�mero de grupo a actualizar sus accesos
	 * @return La posici�n del elemento del grupo con valor m�ximo. Este elemento 
	 * es el candidato para el reemplazo.
	 */
	public int updateLastAccess(int group) {
		int max = Integer.MIN_VALUE;
		int posMax = 0;
		for(int i = 0; i < getPagsPerGroup(); i++) {
			if(getLastAccess()[group][i] >= 0)
				getLastAccess()[group][i] ++;
			
			if(getLastAccess()[group][i] > max) {
				max = getLastAccess()[group][i];
				posMax = i;
			}
		}
		return posMax;
	}
	
	/**
	 * Obtiene la posici�n del grupo de la p�gina a reemplazar, es decir la de m�ximo valor
	 * en el vector lastAccess.
	 * @param group N�mero de grupo a obtener la p�gina de reemplazo
	 * @return La posici�n del elemento del grupo con valor m�ximo. Este elemento 
	 * es el candidato para el reemplazo.
	 */
	public int posInGroupPagToReplace(int group) {
		int max = Integer.MIN_VALUE;
		int posMax = 0;
		for(int i = 0; i < getPagsPerGroup(); i++) {
			if(getLastAccess()[group][i] > max) {
				max = getLastAccess()[group][i];
				posMax = i;
			}
		}
		return posMax;
	} 
	
	/**
	 * Carga una p�gina especificada en el par�metro nPag con los datos necesarios. La
	 * petici�n que ha de hacerse se calcula previamente con la estructura de la cach�.
	 *   
	 * @param group Grupo sobre el que se carga la p�gina nPag
	 * @param posInGroupPageToReplace Posici�n dentro del grupo sobre el que se carga la p�gina
	 * @param nPag P�gina a cargar
	 */
	public void loadPage(int group, int posInGroupPageToReplace, int nPag) {
		page[group * getPagsPerGroup() + posInGroupPageToReplace].loadPage(nPag);
	}
	
	/**
	 * Salva una p�gina especificada en el par�metro nPag a disco. La
	 * petici�n que ha de hacerse se calcula previamente con la estructura de la cach�.
	 *   
	 * @param group Grupo del que se salva la p�gina nPag
	 * @param posInGroupPageToReplace Posici�n dentro del grupo del que se salva la p�gina
	 * @param nPag P�gina a salvar
	 * @throws IOException
	 */
	public void savePage(int group, int posInGroupPageToReplace, int nPag) throws IOException {
		page[group * getPagsPerGroup() + posInGroupPageToReplace].savePage(nPag);
	}

	/**
	 * Salva a disco todas las p�ginas en memoria cach� e inicializa estructuras de datos.
	 * 
	 * @throws IOException
	 */
	public void resetCache() throws IOException {
		for (int iCachePage = 0; iCachePage < page.length; iCachePage++) { 
			if(modified[iCachePage])
				page[iCachePage].savePage(this.getRasterPageNumberInPosition(iCachePage));
		}
		initStructs();
	}
	
	/**
	 * Elimina una banda de la cach�. Antes salva a disco todos los trozos cacheados e inicializa
	 * las estructuras de control de cach�. 
	 * @param nBand N�mero de banda a eliminar
	 * @throws IOException 
	 */
	public void deleteBand(int nBand) throws IOException {
		resetCache();
		for (int iPage = 0; iPage < hddPage.length; iPage++)
			hddPage[iPage].deleteBand(nBand);
	}
	
	/**
	 * Asigna una banda de disco a todas las p�ginas.
	 * @param cacheDataSource Fuente de las p�ginas. Es la referencia a disco de ellas.
	 * @throws IOException 
	 */
	public void assignBand(int nBand, ICacheDataSource[] cacheDataSource) throws IOException {
		resetCache();
		for (int iPage = 0; iPage < hddPage.length; iPage++)
			hddPage[iPage].assignBand(nBand, cacheDataSource[iPage]);
	}
	
	/**
	 * Obtiene la fuente de datos de una banda de una p�gina de disco.
	 * @param nPage P�gina de la que se quiere la banda
	 * @param nBand Banda de la que se quiere la fuente de datos
	 * @return HddPage
	 */
	public ICacheDataSource getHddPage(int nPage, int nBand) {
		return hddPage[nPage].getBandDataSource(nBand);
	}
	
	/**
	 * Consulta si alguna p�gina de memoria tiene modificaciones para volcar a disco
	 * @return true si hay alguna p�gina con modificaciones y false si no la hay
	 */
	public boolean anyPageModified() {
		for (int iPage = 0; iPage < modified.length; iPage++) {
			if(modified[iPage])
				return true;
		}
		return false;
	}
	
	/**
	 * Imprime la informaci�n de estructura de cach�
	 */
	public void show() {
		cacheStruct.show();
		System.out.println("Cacheada:");
		for(int i = 0; i < cacheada.length; i++)
			System.out.print(i + "=" + cacheada[i]+" ");
		System.out.println();
		System.out.println("LastAccess:");
		for(int i = 0; i < cacheStruct.getNGroups(); i++) {
			System.out.println("Grupo " + i +":");
			for(int j = 0; j < cacheStruct.getPagsPerGroup(); j++)
				System.out.println("elem " + j + " : " + "Page " + pageNumberInCache[i * cacheStruct.getPagsPerGroup() + j] + " : " + lastAccess[i][j]);
		
		}
	}
	
}
