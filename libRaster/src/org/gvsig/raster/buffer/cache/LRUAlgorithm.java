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

import java.io.IOException;

/*
 * TODO: OPTIMIZACION: Trabajo de optimización en la velocidad e acceso a caché.
 * TODO: FUNCIONALIDAD: Acabar de implementar los métodos de acceso a datos, intercambio de bandas, etc...
 */
/**
 * Esta clase contiene el algoritmo de reemplazo de trozos de caché LRU
 *   
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class LRUAlgorithm {

	private Cache cache = null;
	
	/**
	 * Constructor. Asigna la caché para poder aplicar el algoritmo.
	 * @param cache
	 */
	public LRUAlgorithm(Cache cache){
		this.cache = cache;
	}
	
	/**
	 * Asigna el objeto caché.
	 * @param cache 
	 */
	public void setCache(Cache cache) {
		this.cache = cache;
	}
	
	/**
	 * Función que controla para cada línea si la página a la que accede está cacheada o no
	 * . Es la encargada de realizar los reemplazos o cargar la página en el buffer de "página
	 * actualmente accedida" (accessPage de la clase Cache)
	 * @param line Línea del raster a la que se está accediendo
	 * @param readOnly será true si el acceso que se está realizando es de lectura y false si se
	 * está escribiendo algún dato
	 */
	public void cacheAccess(int line, boolean readOnly)throws InvalidPageNumberException{
		int pag = line >> cache.getBitsPag();			
		if(cache.isInCache(pag)) {
			if(cache.getNumberInAccessPage() != pag)
				loadPage(pag, readOnly);
		} else {
			replacePage(pag + 1, readOnly, false);
			replacePage(pag, readOnly, true);
		}
	}
	
	/**
	 * Carga la página desde caché al buffer actualmente en uso. Esta operación no lleva
	 * cambio de datos sino que solo es un cambio de punteros. La secuencia de operaciones es:
	 * <UL>
	 * <LI>Cargar la página como accedida</LI>
	 * <LI>Asignar número a la página accedida</LI>
	 * <LI>Actualizar la antigüedad de accesos.Se incrementará uno en todas las páginas del 
	 * conjunto y se pondrá a 0 la página accedida.</LI>
	 * <LI>Poner a 0 el valor de lastAccess de la página cargada. Esto hace que tenga mínima 
	 * prioridad en la politica de reemplazamiento.</LI>
	 * <LI>Poner a false el flag de página modificada si el parámetro readOnly es false.</LI>
	 * </UL>
	 * @param nPag Número de página del raster a la que se intenta acceder.
	 * @param readOnly será true si el acceso que se está realizando es de lectura y false si se
	 * está escribiendo algún dato. 
	 */
	private void loadPage(int nPag, boolean readOnly)throws InvalidPageNumberException{
		PageBuffer buf = cache.getPageBufferFromNumberRasterPage(nPag);
		int[] cacheGroupPage = cache.getNumberGroupFromNumberRasterPage(nPag);
		if(buf != null && cacheGroupPage != null){ //Comprueba que el número de página sea correcto para la caché.
			cache.setAccessPage(buf, nPag);
			cache.updateLastAccess(cacheGroupPage[0]);
			cache.setZeroInLastAccess(cacheGroupPage[0], cacheGroupPage[1]);
			if(!readOnly)
				cache.setModify(cacheGroupPage[0], cacheGroupPage[1]);
		}else
			throw new InvalidPageNumberException("");
		//System.out.println("LOAD: "+nPag);
	}
	
	/**
	 * <P>
	 * Cuando se accede a una página que no está cacheada necesitamos cargarla en caché antes de 
	 * acceder a ella. Si hay un hueco libre en su conjunto se meterá en este pero sino habrá 
	 * que reemplazar una ocupada. Para esto habrá que localizar el conjunto donde va 
	 * destinada y luego la posición del conjunto donde se cargará localizando cual es el 
	 * elemento del conjunto que hace más tiempo que se accedió.
	 * </P>
	 * <P>
	 * Si el elemento es insertado en un hueco la secuencia es la siguiente:
	 * </P>
	 * <UL>
	 * <LI>Obtenemos la siguiente página vacia.</LI>
	 * <LI>La cargamos de datos.</LI>
	 * <LI>Se asigna como página accedida</LI>
	 * <LI>Ponemos true en su posición en el vector cacheada</LI>
	 * <LI>Asignamos el número de página de raster que hemos cargado en esa posición de la caché.</LI>
	 * <LI>Incrementamos en 1 todos los valores de último acceso de las páginas del grupo.</LI>
	 * <LI>Ponemos a 0 su último acceso</LI>
	 * <LI>Si el acceso es para escritura ponemos el flag de página modificada a true.</LI>
	 * </UL>
	 * <P>
	 * Si se reemplaza una página la secuencia es la siguiente:
	 * </P>
	 * <UL>
	 *  <LI>Incrementamos en 1 todos los valores de último acceso de las páginas del grupo y 
	 *  obtenemos la posición de la página de reemplazo.</LI>
	 * <LI>Ponemos a false la página que va a sacarse de caché</LI>
	 * <LI>Si ha sido modificada la página que va a sacarse de caché se vuelca a disco</LI>
	 * <LI>Ponemos el flag de modificada para la página sacada a disco a false.</LI>
	 * <LI>Cargamos la página de caché de datos.</LI>
	 * <LI>Asignamos el número de página de raster que hemos cargado en esa posición de la caché.</LI>
	 * <LI>Se asigna como página accedida</LI>
	 * <LI>Ponemos true en su posición en el vector cacheada</LI>
	 * <LI>Ponemos a 0 su último acceso</LI>
	 * <LI>Si el acceso es para escritura ponemos el flag de página modificada a true.</LI>
	 * </UL>
	 * @param nPag Número de página que está siendo accedida
	 * @param readOnly será true si el acceso que se está realizando es de lectura y false si se
	 * está escribiendo algún dato. 
	 * @param n será true si el acceso se está haciendo a la página n y false si se está haciendo a
	 * la n+1. 
	 */
	private void replacePage(int nPag, boolean readOnly, boolean n){
		int group = nPag % cache.getNGroups();
		
		if(nPag >= cache.getNTotalPags())
			return;
		
		//Insertamos en un hueco
		
		if(insertInAHole(group, readOnly, nPag, n))
			return;
				
		//Reemplazamos una existente
		
		int posInGroupPageToReplace = cache.updateLastAccess(group); //Se actualizan los indices y se obtiene la página a reemplazar
		
		cache.setZeroInLastAccess(group, posInGroupPageToReplace);
		int rasterPageToReplace = cache.getRasterPageNumberInPosition(group, posInGroupPageToReplace);
				
		//Sacamos la antigua
		cache.setPageAsNotLoadInCache(rasterPageToReplace);
		if(cache.isModified(group, posInGroupPageToReplace)){
			try {
				cache.savePage(group, posInGroupPageToReplace, rasterPageToReplace); 	//Volcamos disco 
				cache.unsetModify(group, posInGroupPageToReplace);
			} catch (IOException e) {
				System.err.println("No se ha podido salvar la página de caché.");
				e.printStackTrace();
			}
		}
		
		//Insertamos la nueva
		cache.loadPage(group, posInGroupPageToReplace, nPag);	//Cargamos de nuevo el buffer
		cache.setRasterPageNumberInPosition(group, posInGroupPageToReplace, nPag);
		cache.setPageAsLoadInCache(nPag);		//Pone true en su posición en el vector cacheada
		PageBuffer pb = cache.getPageBuffer(group, posInGroupPageToReplace);
		
		if(n){
			if(!readOnly)
				cache.setModify(group, posInGroupPageToReplace);
			cache.setAccessPage(pb, nPag);  	//Asigna como accedida
		}
		//System.out.println("REPLACE: "+nPag);
	}
	
	/**
	 * Comprueba si hay algún hueco en el que insertar y si es así se inserta en el y devuelve
	 * true. 
	 * @param group conjunto donde hay que buscar para la inserción
	 * @param readOnly si es true la operación es de solo consulta y si es false la operación 
	 * está modificando algún valor de la página a insertar
	 * @param nPag Número de página a cargar
	 * @return true si se ha insertado en un hueco y false si no se ha insertado
	 */
	private boolean insertInAHole(int group, boolean readOnly, int nPag, boolean n) {
		PageBuffer pb = null;
		for(int i = 0; i < cache.getPagsPerGroup(); i++) {
			if(cache.getLastAccess()[group][i] == -1) {

				//Pone true en su posición en el vector cacheada
				pb = cache.getPageBuffer(group, i);
				cache.loadPage(group, i, nPag);					//Sube la página a caché
				cache.setRasterPageNumberInPosition(group, i, nPag);//Asigna el número de pág a una posición de caché
				cache.setPageAsLoadInCache(nPag);
				cache.updateLastAccess(group);
				cache.setZeroInLastAccess(group, i);
				if(n){ //La n+1 no se carga como accedida ni se pone a 0 su contador de última accedida	
					if(!readOnly)
						cache.setModify(group, i);
					cache.setAccessPage(pb, nPag);  //Asigna como accedida
				}				
				//System.out.println("INSERT: "+nPag);
				return true;
			}
		}
		return false;
	}

}
