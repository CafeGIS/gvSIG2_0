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
package org.gvsig.fmap.raster.layers;

public interface ILayerState {

	public static final int UNDEFINED    = -1;
	public static final int OPEN         = 0;
	public static final int CLOSED       = 1;
	public static final int AWAKE        = 2;
	public static final int STOPPED      = 3;
		
	/**
	 * Consulta si una capa está lecantada o no. Una capa levantada
	 * significa que no está open pero tampoco está closed. Tiene asignadas los valores
	 * a las variables para abrir los ficheros o descargar las imagenes pero la acción no
	 * será efectiva hasta que no se haga un open. En ese momento deja de estar awake y pasa 
	 * a open. De la misma forma estándo awake podemos hacer un close.
	 * 
	 * @return true si está levantada y false si no lo está. Si está a false no sabemos nada
	 * del estado en que se encuentra.
	 */
	public boolean isAwake();
	
	/**
	 * Asigna el flag que dice si una capa está levantada. Una capa levantada
	 * significa que no está open pero tampoco está closed. Tiene asignadas los valores
	 * a las variables para abrir los ficheros o descargar las imagenes pero la acción no
	 * será efectiva hasta que no se haga un open. En ese momento deja de estar awake y pasa 
	 * a open. De la misma forma estándo awake podemos hacer un close.
	 *  
	 *  @throws NotAvailableStateException Excepción lanzada si no es posible alcanzar el estado
	 */
	public void enableAwake() throws NotAvailableStateException;
	
	/**
	 * Consulta si una capa está abierta o no. Una capa está abierta cuando los 
	 * archivos a los que referencia están abiertos o descargados y las estructuras
	 * de memoria de gvSIG cargadas con los datos de cabecera y propiedades, es decir lista
	 * para renderizar o leer información. Que una capa no esté open no significa que deba
	 * estar closed, puede estar awake.
	 * 
	 * @return true si está abierta o false si no lo está.
	 */
	public boolean isOpen();
	
	/**
	 * Asigna una capa como abierta. Una capa está abierta cuando los 
	 * archivos a los que referencia están abiertos o descargados y las estructuras
	 * de memoria de gvSIG cargadas con los datos de cabecera y propiedades, es decir lista
	 * para renderizar o leer información. Que una capa no esté open no significa que deba
	 * estar closed, puede estar awake.
	 * 
	 * @throws NotAvailableStateException Excepción lanzada si no es posible alcanzar el estado
	 */
	public void enableOpen() throws NotAvailableStateException;
		
	/**
	 * Consulta si una capa está cerrada o no. Una capa está cerrada cuando no se ha asignado
	 * todavia una fuente de datos a la misma por lo cual no será posible hacer ninguna operación
	 * de consulta. Que una capa no esté closed no significa que deba estar open, puede estar awake.
	 * 
	 * @return true si está abierta o false si no lo está.
	 */
	public boolean isClosed();
	
	/**
	 * Asigna una capa como cerrada. Una capa está cerrada cuando no se ha asignado
	 * todavia una fuente de datos a la misma por lo cual no será posible hacer ninguna operación
	 * de consulta. Que una capa no esté closed no significa que deba estar open, puede estar awake.
	 * 
	 * @throws NotAvailableStateException Excepción lanzada si no es posible alcanzar el estado
	 */
	public void enableClosed() throws NotAvailableStateException;
	
	/**
	 * Consulta el estado de detención. En este estado la capa no puede ser abierta.
	 * Existen motivos por los cuales una capa no debe ser accedida por ningún motivo 
	 * aunque el dataset esté abierto y las estructuras cargadas, en este caso se define un
	 * estado de detención. Cuando una capa es detenida recuerda el estado del que salía y lo
	 * recupera al ser reanudada.
	 * 
	 * @return true si está detenida y false si no lo está.
	 */
	public boolean isStopped();
	
	/**
	 * Activa la detención de la capa. En este estado la capa no puede ser abierta.
	 * Existen motivos por los cuales una capa no debe ser accedida por ningún motivo 
	 * aunque el dataset esté abierto y las estructuras cargadas, en este caso se define un
	 * estado de detención. Cuando una capa es detenida recuerda el estado del que salía y lo
	 * recupera al ser reanudada.
	 * 
	 * @param stopped
	 * @throws NotAvailableStateException
	 */
	public void enableStopped();
	
	/**
	 * Desactiva la detención de la capa. En este estado la capa no puede ser abierta.
	 * Existen motivos por los cuales una capa no debe ser accedida por ningún motivo 
	 * aunque el dataset esté abierto y las estructuras cargadas, en este caso se define un
	 * estado de detención. Cuando una capa es detenida recuerda el estado del que salía y lo
	 * recupera al ser reanudada.
	 * 
	 * @param stopped
	 */
	public void disableStopped();	
}
