
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.Extent;

/**
 * <P>
 * Esta clase representa a la cache raster. Consta de una seríe de páginas (CachePages)
 * que son buffers con los bloques de datos cacheados en un instante dado. Esta cache tiene 
 * una estructura en forma de array donde cada elemento es una página de caché. Las páginas 
 * se agrupan en bloques de N páginas denominados grupos. La variable nelemsGroup contiene 
 * el número de páginas de cada grupo.
 * </P>
 * <P>
 * La politica de reemplazo es que una página siempre va a un conjunto determinado por el calculo
 * cto = pag % nelemsGroup. Si hay hueco vacío en el grupo se añadirá en el siguiente hueco pero
 * si hay que reeplazar se reeplazará la página que más tiempo haga su último acceso. 
 * </P>
 * <P>
 * Esta caché lleva el control de que páginas están cargadas a través de un array de de booleanos
 * donde cada elemento representa a una página de datos del raster. Si el elemento en la posición
 * N de dicho array es true significa que la página está cacheada. Si es false no lo estará. 
 * </P>
 * <P>
 * La página de datos actualmente accedida debe estar cacheada por lo que está clase debe llevar
 * el control de que página se está accediendo o a sido accedida por última vez. La variable que
 * nos dice que página está siendo accedida es loadPage y pageBuffer será la variable que apunta
 * al buffer de esta página. Cuando se cambia de página en un acceso estas dos variables deben
 * cambiar a sus nuevos valores.
 * </P>
 * <P>
 * Otro parámetro que controla esta clase es que página de cada grupo ha sido accedido con mayor 
 * frecuencia. Esto es útil porque siempre se reemplaza la página de un grupo que haga más tiempo
 * que haya sido accedida bajo la premisa de que "las páginas con accesos recientes tienen mayor 
 * probabilidad de volver a ser usadas". Esto es controlado por la variable lastAccess que es
 * usada para implementar el algoritmo LRU. Cada vez que una página de un grupo es accedida su 
 * contador de lastAccess se pone a 0. El resto de contadores de las páginas del grupo se incrementa
 * en uno. Siempre se sustituye la página del grupo con valor más grande en el contador.
 * </P>
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class Cache{
	/**
	 * Cada elemento es una página del raster y dice si está cacheada o no.
	 */
	private boolean[] cacheada = null;
	/**
	 * Número de página cargada en accessPage.
	 */
	private int numberInAccessPage = -1;
	/**
	 * Buffer de datos de la página actualmente accedida.
	 */
	private PageBuffer accessPage = null;
	/**
	 * Páginas de caché
	 */
	private PageBuffer[] page = null;
	/**
	 * Cada elemento del array es una página de caché y contiene el número de página cargada en 
	 * esa posición.
	 */
	private int[]	pageNumberInCache = null;
	/**
	 * Páginas de la caché.
	 */
	private CacheStruct cacheStruct = null;
	/**
	 * Último acceso a las páginas de cada grupo.
	 */
	private int[][] lastAccess = null;
	/**
	 * Cada elemento representa una página de caché y dice si esta ha sido modificada desde que
	 * fué cargada en caché o no.
	 */
	private boolean[] modified = null;
	/**
	 * Páginas de disco
	 */
	private HddPage[] hddPage = null;
	
	/**
	 * Inicializamos la variables. Para ello creamos el objeto Cache que contendrá todos los 
	 * parámetros necesarios para crear el array que controla los accesos más recientes y el
	 * array que dice si una página está en memoria o no.
	 * @param nBands Número de bandas
	 * @param dataType Tipo de dato de la página
	 * @param dataSourceWidth ancho de la fuente de datos
	 * @param driverParam parámetro para el driver
	 */
	public Cache(int nBands, int dataType, int dataSourceWidth, int dataSourceHeight, Object driverParam)  throws FileNotFoundException, NotSupportedExtensionException, RasterDriverException {
		if(driverParam instanceof String) {
			File file = new File(((String)driverParam));
			if(!file.exists())
				throw new FileNotFoundException("I can't make read only cache structure.");
		}
		
		init(nBands, dataType, dataSourceWidth, dataSourceHeight);
		
		//Creamos las estructuras de las páginas de disco
		hddPage = new HddPage[cacheStruct.getNTotalPags()];
				
		RasterDataset dataset = RasterDataset.open(null, driverParam);
		Extent[] extentList = calcExtentPages(dataset, hddPage.length);
		
		for (int iPage = 0; iPage < hddPage.length; iPage++)
			hddPage[iPage] = new HddPage(iPage, dataset, extentList[iPage]);
		
		//Creamos las páginas de memoria
		for(int i = 0; i < cacheStruct.getNPags(); i++) {
		  page[i] = new PageBuffer(dataType, dataSourceWidth, cacheStruct.getHPag(), nBands, true, i);
		  page[i].setHddPages(hddPage);
		}
	}
	
	/**
	 * Calcula la extensión de las páginas en coordenadas del mundo real
	 * @param dataset Dataset
	 * @param nPages Número de páginas en que se divide el raster
	 * @return Extensión de cada página
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
	 * Inicializamos la variables. Para ello creamos el objeto Cache que contendrá todos los 
	 * parámetros necesarios para crear el array que controla los accesos más recientes y el
	 * array que dice si una página está en memoria o no.
	 * @param nBands Número de bandas
	 * @param dataType Tipo de dato de la página
	 * @param dataSourceWidth ancho de la fuente de datos
	 */
	public Cache(int nBands, int dataType, int dataSourceWidth, int dataSourceHeight) {
		init(nBands, dataType, dataSourceWidth, dataSourceHeight);
		
		//Creamos las estructuras de las páginas de disco
		hddPage = new HddPage[cacheStruct.getNTotalPags()];
		for (int iPage = 0; iPage < hddPage.length; iPage++)
			hddPage[iPage] = new HddPage(iPage, getNBands());
		
		//Creamos las páginas de memoria
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
		
		//Inicializamos la antigüedad de acceso  
		lastAccess = new int[cacheStruct.getNGroups()][cacheStruct.getPagsPerGroup()];
		
		//Creamos el buffer de la caché
		page = new PageBuffer[cacheStruct.getNPags()];
		pageNumberInCache = new int[cacheStruct.getNPags()];
		modified = new boolean[cacheStruct.getNPags()];
		cacheada = new boolean[cacheStruct.getNTotalPags()];
		
		//Creamos las estructuras de las páginas de disco
		hddPage = new HddPage[cacheStruct.getNTotalPags()];
		for (int iPage = 0; iPage < hddPage.length; iPage++)
			hddPage[iPage] = new HddPage(iPage, getNBands());
		
		//Creamos las páginas de memoria
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
		//Creamos la estructura de la caché
		cacheStruct = new CacheStruct(nBands, dataType, dataSourceWidth, dataSourceHeight);
		
		//Inicializamos la antigüedad de acceso  
		lastAccess = new int[cacheStruct.getNGroups()][cacheStruct.getPagsPerGroup()];
		
		//Creamos el buffer de la caché
		page = new PageBuffer[cacheStruct.getNPags()];
		pageNumberInCache = new int[cacheStruct.getNPags()];
		modified = new boolean[cacheStruct.getNPags()];
		cacheada = new boolean[cacheStruct.getNTotalPags()];
		
		this.initStructs();
	}
		
	/**
	 * Inicializa las estructuras de mantenimiento de caché. Estas son array de paginas
	 * accedidas por última vez, numeros de página cargadas en cada posición de caché, 
	 * array con la información de página modificada y array que dice si una página de disco
	 * está cacheada o no.
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
	 * Limpia los trozos de caché en disco. Después del llamar a este 
	 * método no puede volver a usarse esta caché.
	 * @throws IOException 
	 */
	public void clearCache(int nBands) throws IOException {
		if(hddPage == null)
			return;
		for (int i = 0; i < hddPage.length; i++) 
			hddPage[i].deletePage(nBands);
	}
	
	/**
	 * Obtiene el número de bits por página para poder calcular el desplazamiento binario
	 * de la dirección de acceso de la petición. Es decir si se solicita un dato en la línea
	 * 36 (en decimal) del raster 100100(en binario) y el desplazamiento es 4 bits el número de 
	 * página resultante será 10(en binario) 2 (en decimal)
	 * @return
	 */
	public int getBitsPag() {
		return cacheStruct.getBitsPag();
	}

	/**
	 * Obtiene la altura de la página de cache en líneas. 
	 * @return Número de líneas de altura de página.
	 */
	public int getHPag() {
		return cacheStruct.getHPag();
	}
	
	/**
	 * Array de booleanos donde cada elemento es una página del raster y dice si la página 
	 * está cacheada o no.
	 * @return true si la página está en caché y false si no lo está
	 */
	public boolean isInCache(int nPag) {
		if(nPag < 0 || nPag >= cacheada.length)
			return false;
		return cacheada[nPag];
	}
	
	/**
	 * Marca una página como cargada en caché.
	 * @param nPag Número de página a marcar.
	 */
	public void setPageAsLoadInCache(int nPag) {
		if(nPag < 0 || nPag >= cacheada.length)
			return;
		cacheada[nPag] = true;
	}
	
	/**
	 * Desmarca una página como cargada en caché.
	 * @param nPag Número de página a desmarcar.
	 */
	public void setPageAsNotLoadInCache(int nPag) {
		if(nPag < 0 || nPag >= cacheada.length)
			return;
		cacheada[nPag] = false;
	}
	
	/**
	 * Obtiene el número de página del raster cargada en una página de caché especificada
	 * en el parámetro. 
	 * @param nCachePage Número de página de caché de la que se quiere saber que página del 
	 * raster hay cargada.
	 * @return Número de página del raster
	 */
	public int getRasterPageNumberInPosition(int nCachePage) {
		return pageNumberInCache[nCachePage];
	}
	
	/**
	 * Obtiene el número de página del raster cargada en una página de caché especificada
	 * en el parámetro. 
	 * @param group Grupo en el que se encuentra la página
	 * @param posInGroup Posición dentro del grupo en el que está la página
	 * @return Número de página del raster
	 */
	public int getRasterPageNumberInPosition(int group, int posInGroup) {
		return pageNumberInCache[group * getPagsPerGroup() + posInGroup];
	}
	
	/**
	 * Asigna el número de página del raster cargada en una página de caché especificada.
	 * @param nCachePage Número de página de caché 
	 * @param nRasterPage Número de página de raster a asignar
	 */
	public void setRasterPageNumberInPosition(int nCachePage, int nRasterPage) {
		pageNumberInCache[nCachePage] = nRasterPage;
	}
	
	/**
	 * Asigna el número de página del raster cargada en una página de caché especificada.
	 * @param nCachePage Número de página de caché 
	 * @param nRasterPage Número de página de raster a asignar
	 */
	public void setRasterPageNumberInPosition(int group, int posInGroup, int nRasterPage) {
		pageNumberInCache[group * getPagsPerGroup() + posInGroup] = nRasterPage;
	}
	
	/**
	 * Obtiene el número de página de caché donde está cargada la página del raster
	 * que se ha pasado por parámetro.
	 * @param pag Página del raster
	 * @return Número de página del raster
	 */
	public int getNumberCachePageFromNumberRasterPage(int pag) {
		int group = pag % getNGroups(); 
		for(int i = 0; i < getPagsPerGroup(); i++)
			if(pageNumberInCache[group + i] == pag)
				return (group * getPagsPerGroup() + i);
		return -1;
	}
	
	/**
	 * Obtiene el número de página de caché (en formato grupo/página dentro del grupo) 
	 * donde está cargada la página del raster que se ha pasado por parámetro.
	 * @param pag Página del raster
	 * @return Número de página del raster
	 */
	public int[] getNumberGroupFromNumberRasterPage(int pag) {
		int group = pag % getNGroups();
		for(int i = 0; i < getPagsPerGroup(); i++)
			if(pageNumberInCache[(group * getPagsPerGroup()) + i] == pag)
				return new int[]{group, i};
		return null;
	}
	
	/**
	 * Obtiene el array que contiene los valores de la antigüedad del acceso dentro del
	 * grupo. La primera dimensión del array corresponde al número de grupo y la segunda
	 * a los elementos del grupo. El elemento del grupo con un acceso más reciente tendrá 
	 * un número menor y el de mayor valor será el candidato para la sustitución en el próximo
	 * remplazamiento.
	 * @return Array bidimensional con los valores de antigüedad.
	 */
	public int[][] getLastAccess() {
		return lastAccess;
	}

	/**
	 * Asigna un cero en la posición del array que contiene la antigüedad de acceso dentro 
	 * del grupo. Esta operación se realiza cada vez que se accede a un dato del la página 
	 * cacheada y significa que es la página más recientemente accedida.
	 * @param group Número de grupo
	 * @param posInGroup Posición de la página dentro del grupo.
	 */
	public void setZeroInLastAccess(int group, int posInGroup) {
		lastAccess[group][posInGroup] = 0;
	}
	
	/**
	 * Obtiene el número de página cargada en el buffer
	 * @return Entero con el número de página
	 */
	public int getNumberInAccessPage() {
		return numberInAccessPage;
	}

	/**
	 * Obtiene el número de páginas que tiene cada grupo
	 * @return Entero con el número de páginas por grupo 
	 */
	public int getPagsPerGroup() {
		return cacheStruct.getPagsPerGroup();
	}

	/**
	 * Obtiene la página de datos de la posición pag
	 * @param pag Número de página de caché a recuperar
	 * @return PageBuffer correspondiente a la página recuperada
	 */
	public PageBuffer getPageBufferFromNumberCachePage(int pag) {
		return page[pag];
	}
	
	/**
	 * Obtiene la página de datos a partir del número de página de raster
	 * @param pag Número de página raster a recuperar
	 * @return PageBuffer correspondiente a la página recuperada o null si no está en caché
	 */
	public PageBuffer getPageBufferFromNumberRasterPage(int pag) {
		int group = pag % getNGroups();
		for(int i = 0; i < getPagsPerGroup(); i++)
			if(pageNumberInCache[group * getPagsPerGroup() + i] == pag)
				return page[group * getPagsPerGroup() + i];
		return null;
	}
	
	/**
	 * Obtiene la página de datos del grupo definido en el parámetro group y de la 
	 * posición pag dentro de ese grupo.
	 * @param group Grupo de la página requerida
	 * @param pag Número de página dentro del grupo
	 * @return PageBuffer correspondiente a la página recuperada
	 */
	public PageBuffer getPageBuffer(int group, int posInGroup) {
		return page[group * getPagsPerGroup() + posInGroup];
	}
	
	/**
	 * Obtiene la página de datos actualmente accedida
	 * @return PageBuffer correspondiente a la página que se está accediendo
	 */
	public PageBuffer getAccessPage() {
		return accessPage;
	}
	
	/**
	 * Asigna el buffer de la página accedida por referencia
	 * @param pb
	 */
	public void setAccessPage(PageBuffer pb, int pagNumber) {
		accessPage = pb;
		numberInAccessPage = pagNumber;
	}
	
	/**
	 * Consulta si una página de caché ha sido modificada desde que se cargó en caché o no.
	 * @param nCachePag Número de página de caché (posición de esta)
	 * @return true si ha sido modificada y false si no lo ha sido.
	 */
	public boolean isModified(int nCachePag) {
		return modified[nCachePag];
	}

	/**
	 * Consulta si una página de caché ha sido modificada desde que se cargó en caché o no.
	 * @param group Grupo en el que se encuentra la página
	 * @param posInGroup Posición dentro del grupo en el que está la página
	 * @return true si ha sido modificada y false si no lo ha sido.
	 */
	public boolean isModified(int group, int posInGroup) {
		return modified[group * getPagsPerGroup() + posInGroup];
	}

	/**
	 * Pone como modificada una página de caché
	 * @param nCachePag Número de página de caché (posición de esta)
	 */
	public void setModify(int nCachePag) {
		modified[nCachePag] = true;	
	}
	
	/**
	 * Pone como modificada una página de caché
	 * @param group Grupo en el que se encuentra la página
	 * @param posInGroup Posición dentro del grupo en el que está la página
	 */
	public void setModify(int group, int posInGroup) {
		modified[group * getPagsPerGroup() + posInGroup] = true;	
	}
	
	/**
	 * Pone como no modificada una página de caché
	 * @param nCachePag Número de página de caché (posición de esta)
	 */
	public void unsetModify(int nCachePag) {
		modified[nCachePag] = false;	
	}
	
	/**
	 * Pone como no modificada una página de caché
	 * @param group Grupo en el que se encuentra la página
	 * @param posInGroup Posición dentro del grupo en el que está la página
	 */
	public void unsetModify(int group, int posInGroup) {
		modified[group * getPagsPerGroup() + posInGroup] = false;	
	}
	
	/**
	 * Obtiene el número de páginas de la caché
	 * @return Número total de páginas de la caché
	 */
	public int getNPags() {
		return cacheStruct.getNPags();
	}
	
	/**
	 * Obtiene el número de bandas
	 * @return Número de bandas
	 */
	public int getNBands() {
		return cacheStruct.getNBands();
	}
	
	/**
	 * Obtiene la estructura de ca caché
	 * @return CacheStruct
	 */
	public CacheStruct getCacheStruct() {
		return cacheStruct;
	}
	
	/**
	 * Asigna la estructura de ca caché
	 * @param CacheStruct
	 */
	public void setCacheStruct(CacheStruct cacheStruct) {
		this.cacheStruct = cacheStruct;
	}
	
	/**
	 * Obtiene el número de grupos de caché
	 * @return Número de grupos
	 */
	public int getNGroups() {
		return cacheStruct.getNGroups();
	}
	
	/**
	 * Obtiene el número total de páginas del raster
	 * @return Número total de páginas
	 */
	public int getNTotalPags() {
		return cacheStruct.getNTotalPags();
	}
	
	/**
	 * Obtiene el array con los números de página que hay cargados
	 * en cada bloque de cache
	 * @return array con los número de página
	 */
	public int[] getPageNumberInCache() {
		return this.pageNumberInCache;
	}
	
	/**
	 * Para extraer el desplazamiento de una dirección (línea de raster) hay que hacer una operación And con 
	 * con la altura de la página -1. Por ejemplo, una página de 16 líneas de altura el desplazamiento será
	 * 16 - 1 = 15 porque 15 en binario es 1111.
	 * 
	 * Si queremos acceder a la linea del raster número 83 (1010011) realizando la operación And con el valor del
	 * desplazamiento obtenemos (0001111 & 1010011 = 0000011), es decir el valor 3 en decimal. Esto quiere decir
	 * que la línea 83 del raster es la 3 de su página. 
	 * @return valor del desplazamiento
	 */
	public int getOffset() {
		return cacheStruct.getOffset();
	}
	
	/**
	 * Convierte una página dentro de un grupo de la caché en un número de 
	 * página de caché. Por ejemplo, en una caché de 10 grupos y 5 páginas por grupo
	 * la página 2 del grupo 4 devolverá el 23 (5 * 4 + 3 = 23) teniendo en cuenta que
	 * la página y el grupo cero también cuentan.  
	 * @param group
	 * @param pageInGroup
	 * @return
	 */
	public int convertPageInGroupToPageInCache(int group, int pageInGroup) {
		return (group * cacheStruct.getPagsPerGroup() + pageInGroup);
	}

	/**
	 * Actualiza los últimos accesos del grupo pasado por parámetro. Se incrementará 
	 * uno en todas las páginas del conjunto a excepción de las que valen -1 ya que 
	 * esto significa que no hay página cargada en dicha posición.
	 * @param group Número de grupo a actualizar sus accesos
	 * @return La posición del elemento del grupo con valor máximo. Este elemento 
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
	 * Obtiene la posición del grupo de la página a reemplazar, es decir la de máximo valor
	 * en el vector lastAccess.
	 * @param group Número de grupo a obtener la página de reemplazo
	 * @return La posición del elemento del grupo con valor máximo. Este elemento 
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
	 * Carga una página especificada en el parámetro nPag con los datos necesarios. La
	 * petición que ha de hacerse se calcula previamente con la estructura de la caché.
	 *   
	 * @param group Grupo sobre el que se carga la página nPag
	 * @param posInGroupPageToReplace Posición dentro del grupo sobre el que se carga la página
	 * @param nPag Página a cargar
	 */
	public void loadPage(int group, int posInGroupPageToReplace, int nPag) {
		page[group * getPagsPerGroup() + posInGroupPageToReplace].loadPage(nPag);
	}
	
	/**
	 * Salva una página especificada en el parámetro nPag a disco. La
	 * petición que ha de hacerse se calcula previamente con la estructura de la caché.
	 *   
	 * @param group Grupo del que se salva la página nPag
	 * @param posInGroupPageToReplace Posición dentro del grupo del que se salva la página
	 * @param nPag Página a salvar
	 * @throws IOException
	 */
	public void savePage(int group, int posInGroupPageToReplace, int nPag) throws IOException {
		page[group * getPagsPerGroup() + posInGroupPageToReplace].savePage(nPag);
	}

	/**
	 * Salva a disco todas las páginas en memoria caché e inicializa estructuras de datos.
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
	 * Elimina una banda de la caché. Antes salva a disco todos los trozos cacheados e inicializa
	 * las estructuras de control de caché. 
	 * @param nBand Número de banda a eliminar
	 * @throws IOException 
	 */
	public void deleteBand(int nBand) throws IOException {
		resetCache();
		for (int iPage = 0; iPage < hddPage.length; iPage++)
			hddPage[iPage].deleteBand(nBand);
	}
	
	/**
	 * Asigna una banda de disco a todas las páginas.
	 * @param cacheDataSource Fuente de las páginas. Es la referencia a disco de ellas.
	 * @throws IOException 
	 */
	public void assignBand(int nBand, ICacheDataSource[] cacheDataSource) throws IOException {
		resetCache();
		for (int iPage = 0; iPage < hddPage.length; iPage++)
			hddPage[iPage].assignBand(nBand, cacheDataSource[iPage]);
	}
	
	/**
	 * Obtiene la fuente de datos de una banda de una página de disco.
	 * @param nPage Página de la que se quiere la banda
	 * @param nBand Banda de la que se quiere la fuente de datos
	 * @return HddPage
	 */
	public ICacheDataSource getHddPage(int nPage, int nBand) {
		return hddPage[nPage].getBandDataSource(nBand);
	}
	
	/**
	 * Consulta si alguna página de memoria tiene modificaciones para volcar a disco
	 * @return true si hay alguna página con modificaciones y false si no la hay
	 */
	public boolean anyPageModified() {
		for (int iPage = 0; iPage < modified.length; iPage++) {
			if(modified[iPage])
				return true;
		}
		return false;
	}
	
	/**
	 * Imprime la información de estructura de caché
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
