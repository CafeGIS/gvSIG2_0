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
package org.gvsig.rastertools.saveraster.operations;

import java.io.IOException;

import org.gvsig.fmap.dal.coverage.dataset.io.GdalWriter;
import org.gvsig.fmap.mapcontext.ViewPort;

import com.iver.andami.messages.NotificationManager;

import es.gva.cit.jgdal.Gdal;
import es.gva.cit.jgdal.GdalDriver;
import es.gva.cit.jgdal.GdalException;
/**
 * Esta clase copia un dataset desde disco en un formato soportado por Gdal a
 * otro especificado.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
class CopyDatasetThread extends Thread {
	private GdalDriver 				driver = null;
	private String 						fDstName = null;
	private String 						fSrcName = null;
	private ViewPort 					vp = null;
	private String[]					driverProps = null;
//	private String 						extension = null;
//	private double 						maxPercentBar = 0D;
//	private boolean 					runCopy = false;

	/**
	 * Constructor
	 * @param src	Nombre del fichero fuente
	 * @param dst	Nombre del fichero destino
	 * @param viewPort	Viewport
	 * @param dp	DriverProperties
	 * @param drvName	Nombre del driver de Gdal
	 * @param extens	Extensión del fichero
	 */
	public CopyDatasetThread(String src, String dst, ViewPort viewPort, String[] dp, String drvName, String extens) {
		fSrcName = src;
		fDstName = dst;
		vp = viewPort;
		this.driverProps = dp;
		// this.extension = extens;
		try {
			driver = Gdal.getDriverByName(drvName);
		} catch (GdalException exc) {
			System.err.println("No se ha podido obtener el driver.");
		}
		// runCopy = true;
	}

	/**
	 * Función que realiza la copia del dataset
	 */
	public void copy() {
		try {
			GdalWriter.createCopy(driver, fDstName, fSrcName, false, driverProps, vp.getProjection());
		} catch (IOException ev) {
			NotificationManager.addError("Error al hacer la copia", ev);
		} catch (GdalException ev) {
			NotificationManager.addError("Error al hacer la copia", ev);
		}
//		runCopy = false;
	}

	/**
	 * El thread maneja el incremento de la barra
	 */
	public void run() {

	}

	/**
	 * Asigna el porcentaje máximo a la barra
	 * @param max	Porcentaje máximo de la barra
	 */
/*
	public void setMaxPercentBar(double max){
		this.maxPercentBar = max;
	}
*/
}