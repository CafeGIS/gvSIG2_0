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

import java.io.IOException;

/*
 * TODO: OPTIMIZACION: Trabajo de optimizaci�n en la velocidad e acceso a cach�.
 * TODO: FUNCIONALIDAD: Acabar de implementar los m�todos de acceso a datos, intercambio de bandas, etc...
 */
/**
 * Esta clase contiene el algoritmo de reemplazo de trozos de cach� LRU
 *   
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class LRUAlgorithm {

	private Cache cache = null;
	
	/**
	 * Constructor. Asigna la cach� para poder aplicar el algoritmo.
	 * @param cache
	 */
	public LRUAlgorithm(Cache cache){
		this.cache = cache;
	}
	
	/**
	 * Asigna el objeto cach�.
	 * @param cache 
	 */
	public void setCache(Cache cache) {
		this.cache = cache;
	}
	
	/**
	 * Funci�n que controla para cada l�nea si la p�gina a la que accede est� cacheada o no
	 * . Es la encargada de realizar los reemplazos o cargar la p�gina en el buffer de "p�gina
	 * actualmente accedida" (accessPage de la clase Cache)
	 * @param line L�nea del raster a la que se est� accediendo
	 * @param readOnly ser� true si el acceso que se est� realizando es de lectura y false si se
	 * est� escribiendo alg�n dato
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
	 * Carga la p�gina desde cach� al buffer actualmente en uso. Esta operaci�n no lleva
	 * cambio de datos sino que solo es un cambio de punteros. La secuencia de operaciones es:
	 * <UL>
	 * <LI>Cargar la p�gina como accedida</LI>
	 * <LI>Asignar n�mero a la p�gina accedida</LI>
	 * <LI>Actualizar la antig�edad de accesos.Se incrementar� uno en todas las p�ginas del 
	 * conjunto y se pondr� a 0 la p�gina accedida.</LI>
	 * <LI>Poner a 0 el valor de lastAccess de la p�gina cargada. Esto hace que tenga m�nima 
	 * prioridad en la politica de reemplazamiento.</LI>
	 * <LI>Poner a false el flag de p�gina modificada si el par�metro readOnly es false.</LI>
	 * </UL>
	 * @param nPag N�mero de p�gina del raster a la que se intenta acceder.
	 * @param readOnly ser� true si el acceso que se est� realizando es de lectura y false si se
	 * est� escribiendo alg�n dato. 
	 */
	private void loadPage(int nPag, boolean readOnly)throws InvalidPageNumberException{
		PageBuffer buf = cache.getPageBufferFromNumberRasterPage(nPag);
		int[] cacheGroupPage = cache.getNumberGroupFromNumberRasterPage(nPag);
		if(buf != null && cacheGroupPage != null){ //Comprueba que el n�mero de p�gina sea correcto para la cach�.
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
	 * Cuando se accede a una p�gina que no est� cacheada necesitamos cargarla en cach� antes de 
	 * acceder a ella. Si hay un hueco libre en su conjunto se meter� en este pero sino habr� 
	 * que reemplazar una ocupada. Para esto habr� que localizar el conjunto donde va 
	 * destinada y luego la posici�n del conjunto donde se cargar� localizando cual es el 
	 * elemento del conjunto que hace m�s tiempo que se accedi�.
	 * </P>
	 * <P>
	 * Si el elemento es insertado en un hueco la secuencia es la siguiente:
	 * </P>
	 * <UL>
	 * <LI>Obtenemos la siguiente p�gina vacia.</LI>
	 * <LI>La cargamos de datos.</LI>
	 * <LI>Se asigna como p�gina accedida</LI>
	 * <LI>Ponemos true en su posici�n en el vector cacheada</LI>
	 * <LI>Asignamos el n�mero de p�gina de raster que hemos cargado en esa posici�n de la cach�.</LI>
	 * <LI>Incrementamos en 1 todos los valores de �ltimo acceso de las p�ginas del grupo.</LI>
	 * <LI>Ponemos a 0 su �ltimo acceso</LI>
	 * <LI>Si el acceso es para escritura ponemos el flag de p�gina modificada a true.</LI>
	 * </UL>
	 * <P>
	 * Si se reemplaza una p�gina la secuencia es la siguiente:
	 * </P>
	 * <UL>
	 *  <LI>Incrementamos en 1 todos los valores de �ltimo acceso de las p�ginas del grupo y 
	 *  obtenemos la posici�n de la p�gina de reemplazo.</LI>
	 * <LI>Ponemos a false la p�gina que va a sacarse de cach�</LI>
	 * <LI>Si ha sido modificada la p�gina que va a sacarse de cach� se vuelca a disco</LI>
	 * <LI>Ponemos el flag de modificada para la p�gina sacada a disco a false.</LI>
	 * <LI>Cargamos la p�gina de cach� de datos.</LI>
	 * <LI>Asignamos el n�mero de p�gina de raster que hemos cargado en esa posici�n de la cach�.</LI>
	 * <LI>Se asigna como p�gina accedida</LI>
	 * <LI>Ponemos true en su posici�n en el vector cacheada</LI>
	 * <LI>Ponemos a 0 su �ltimo acceso</LI>
	 * <LI>Si el acceso es para escritura ponemos el flag de p�gina modificada a true.</LI>
	 * </UL>
	 * @param nPag N�mero de p�gina que est� siendo accedida
	 * @param readOnly ser� true si el acceso que se est� realizando es de lectura y false si se
	 * est� escribiendo alg�n dato. 
	 * @param n ser� true si el acceso se est� haciendo a la p�gina n y false si se est� haciendo a
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
		
		int posInGroupPageToReplace = cache.updateLastAccess(group); //Se actualizan los indices y se obtiene la p�gina a reemplazar
		
		cache.setZeroInLastAccess(group, posInGroupPageToReplace);
		int rasterPageToReplace = cache.getRasterPageNumberInPosition(group, posInGroupPageToReplace);
				
		//Sacamos la antigua
		cache.setPageAsNotLoadInCache(rasterPageToReplace);
		if(cache.isModified(group, posInGroupPageToReplace)){
			try {
				cache.savePage(group, posInGroupPageToReplace, rasterPageToReplace); 	//Volcamos disco 
				cache.unsetModify(group, posInGroupPageToReplace);
			} catch (IOException e) {
				System.err.println("No se ha podido salvar la p�gina de cach�.");
				e.printStackTrace();
			}
		}
		
		//Insertamos la nueva
		cache.loadPage(group, posInGroupPageToReplace, nPag);	//Cargamos de nuevo el buffer
		cache.setRasterPageNumberInPosition(group, posInGroupPageToReplace, nPag);
		cache.setPageAsLoadInCache(nPag);		//Pone true en su posici�n en el vector cacheada
		PageBuffer pb = cache.getPageBuffer(group, posInGroupPageToReplace);
		
		if(n){
			if(!readOnly)
				cache.setModify(group, posInGroupPageToReplace);
			cache.setAccessPage(pb, nPag);  	//Asigna como accedida
		}
		//System.out.println("REPLACE: "+nPag);
	}
	
	/**
	 * Comprueba si hay alg�n hueco en el que insertar y si es as� se inserta en el y devuelve
	 * true. 
	 * @param group conjunto donde hay que buscar para la inserci�n
	 * @param readOnly si es true la operaci�n es de solo consulta y si es false la operaci�n 
	 * est� modificando alg�n valor de la p�gina a insertar
	 * @param nPag N�mero de p�gina a cargar
	 * @return true si se ha insertado en un hueco y false si no se ha insertado
	 */
	private boolean insertInAHole(int group, boolean readOnly, int nPag, boolean n) {
		PageBuffer pb = null;
		for(int i = 0; i < cache.getPagsPerGroup(); i++) {
			if(cache.getLastAccess()[group][i] == -1) {

				//Pone true en su posici�n en el vector cacheada
				pb = cache.getPageBuffer(group, i);
				cache.loadPage(group, i, nPag);					//Sube la p�gina a cach�
				cache.setRasterPageNumberInPosition(group, i, nPag);//Asigna el n�mero de p�g a una posici�n de cach�
				cache.setPageAsLoadInCache(nPag);
				cache.updateLastAccess(group);
				cache.setZeroInLastAccess(group, i);
				if(n){ //La n+1 no se carga como accedida ni se pone a 0 su contador de �ltima accedida	
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
