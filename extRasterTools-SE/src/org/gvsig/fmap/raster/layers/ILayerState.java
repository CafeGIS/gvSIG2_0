/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
	 * Consulta si una capa est� lecantada o no. Una capa levantada
	 * significa que no est� open pero tampoco est� closed. Tiene asignadas los valores
	 * a las variables para abrir los ficheros o descargar las imagenes pero la acci�n no
	 * ser� efectiva hasta que no se haga un open. En ese momento deja de estar awake y pasa 
	 * a open. De la misma forma est�ndo awake podemos hacer un close.
	 * 
	 * @return true si est� levantada y false si no lo est�. Si est� a false no sabemos nada
	 * del estado en que se encuentra.
	 */
	public boolean isAwake();
	
	/**
	 * Asigna el flag que dice si una capa est� levantada. Una capa levantada
	 * significa que no est� open pero tampoco est� closed. Tiene asignadas los valores
	 * a las variables para abrir los ficheros o descargar las imagenes pero la acci�n no
	 * ser� efectiva hasta que no se haga un open. En ese momento deja de estar awake y pasa 
	 * a open. De la misma forma est�ndo awake podemos hacer un close.
	 *  
	 *  @throws NotAvailableStateException Excepci�n lanzada si no es posible alcanzar el estado
	 */
	public void enableAwake() throws NotAvailableStateException;
	
	/**
	 * Consulta si una capa est� abierta o no. Una capa est� abierta cuando los 
	 * archivos a los que referencia est�n abiertos o descargados y las estructuras
	 * de memoria de gvSIG cargadas con los datos de cabecera y propiedades, es decir lista
	 * para renderizar o leer informaci�n. Que una capa no est� open no significa que deba
	 * estar closed, puede estar awake.
	 * 
	 * @return true si est� abierta o false si no lo est�.
	 */
	public boolean isOpen();
	
	/**
	 * Asigna una capa como abierta. Una capa est� abierta cuando los 
	 * archivos a los que referencia est�n abiertos o descargados y las estructuras
	 * de memoria de gvSIG cargadas con los datos de cabecera y propiedades, es decir lista
	 * para renderizar o leer informaci�n. Que una capa no est� open no significa que deba
	 * estar closed, puede estar awake.
	 * 
	 * @throws NotAvailableStateException Excepci�n lanzada si no es posible alcanzar el estado
	 */
	public void enableOpen() throws NotAvailableStateException;
		
	/**
	 * Consulta si una capa est� cerrada o no. Una capa est� cerrada cuando no se ha asignado
	 * todavia una fuente de datos a la misma por lo cual no ser� posible hacer ninguna operaci�n
	 * de consulta. Que una capa no est� closed no significa que deba estar open, puede estar awake.
	 * 
	 * @return true si est� abierta o false si no lo est�.
	 */
	public boolean isClosed();
	
	/**
	 * Asigna una capa como cerrada. Una capa est� cerrada cuando no se ha asignado
	 * todavia una fuente de datos a la misma por lo cual no ser� posible hacer ninguna operaci�n
	 * de consulta. Que una capa no est� closed no significa que deba estar open, puede estar awake.
	 * 
	 * @throws NotAvailableStateException Excepci�n lanzada si no es posible alcanzar el estado
	 */
	public void enableClosed() throws NotAvailableStateException;
	
	/**
	 * Consulta el estado de detenci�n. En este estado la capa no puede ser abierta.
	 * Existen motivos por los cuales una capa no debe ser accedida por ning�n motivo 
	 * aunque el dataset est� abierto y las estructuras cargadas, en este caso se define un
	 * estado de detenci�n. Cuando una capa es detenida recuerda el estado del que sal�a y lo
	 * recupera al ser reanudada.
	 * 
	 * @return true si est� detenida y false si no lo est�.
	 */
	public boolean isStopped();
	
	/**
	 * Activa la detenci�n de la capa. En este estado la capa no puede ser abierta.
	 * Existen motivos por los cuales una capa no debe ser accedida por ning�n motivo 
	 * aunque el dataset est� abierto y las estructuras cargadas, en este caso se define un
	 * estado de detenci�n. Cuando una capa es detenida recuerda el estado del que sal�a y lo
	 * recupera al ser reanudada.
	 * 
	 * @param stopped
	 * @throws NotAvailableStateException
	 */
	public void enableStopped();
	
	/**
	 * Desactiva la detenci�n de la capa. En este estado la capa no puede ser abierta.
	 * Existen motivos por los cuales una capa no debe ser accedida por ning�n motivo 
	 * aunque el dataset est� abierto y las estructuras cargadas, en este caso se define un
	 * estado de detenci�n. Cuando una capa es detenida recuerda el estado del que sal�a y lo
	 * recupera al ser reanudada.
	 * 
	 * @param stopped
	 */
	public void disableStopped();	
}
