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
package org.gvsig.raster.projection;

import org.cresques.cts.ICRSFactory;
import org.cresques.cts.IProjection;
import org.cresques.impl.cts.ProjectionPool;
import org.gvsig.jogr.CrsGdalException;
import org.gvsig.jogr.OGRException;
import org.gvsig.jogr.OGRSpatialReference;
import org.slf4j.LoggerFactory;
/**
 * Esta clase se encarga de hacer la conversion entre Wkt e IProjection.
 *
 * El uso se hace mediante dos llamadas estaticas que son: convertIProjectionToWkt y
 * convertWktToIProjection.
 *
 * Antes de usarlos, hay que saber si tenemos acceso a gvSIG e intentar coger el factory desde allí.
 * Ya que su uso consume tiempo y espacio de memoria y es preferible reaprovechar ese objeto ya
 * creado. Esto se hace con setCRSFactory.
 *
 * En caso de no asignarse el ya existente, el creará uno interno y lo dejara en una variable estatica.
 *
 * @version 11/07/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class CRS {
	private static CRS singleton = null;
	private static ICRSFactory factory = null;

	/**
	 * Prohibida su instanciación sin usar un singleton
	 */
	private CRS() {}

	/**
	 * Coger una instancia de CRS.
	 * @return
	 */
	private static CRS getSingleton() {
		if (singleton == null)
			singleton = new CRS();
		return singleton;
	}

	/**
	 * Asigna la instancia interna a CRSFactory. Es bueno asignarlo si ya existe para evitar su creación.
	 * @param factory
	 */
	public static void setCRSFactory(ICRSFactory factory) {
		CRS.factory = factory;
	}

	/**
	 * Devuelve el CRSFactory asignado desde fuera o creado desde dentro, todo depende de como se haya
	 * usado.
	 *
	 * @param code
	 * @return
	 */
	private static IProjection getCRS(String code) {
		if (factory == null)
			factory = new ProjectionPool();
		return factory.get(code);
	}

	/**
	 * Convierte una cadena de texto Wkt a IProjection
	 * @param wkt
	 * @return
	 */
	public static IProjection convertWktToIProjection(String wkt) {
		if (wkt == null || wkt.equals(""))
			return null;

		String code = null;
		String name = null;

		OGRSpatialReference oSRSSource = new OGRSpatialReference();
		try {
			OGRSpatialReference.importFromWkt(oSRSSource, wkt);

			code = oSRSSource.getAuthorityCode("PROJCS");
			if (code == null)
				code = oSRSSource.getAuthorityCode("GEOGCS");
			name = oSRSSource.getAuthorityName("PROJCS");
			if (name == null)
				name = oSRSSource.getAuthorityName("GEOGCS");
			try {
				return CRS.getCRS(name + ":" + code);
			} catch (NumberFormatException ex) {
				return null;
			}
		} catch (OGRException e) {
			LoggerFactory.getLogger(CRS.getSingleton().getClass().getName()).debug("Problemas obteniendo el código EPSG", e);
		} catch (CrsGdalException e) {
			LoggerFactory.getLogger(CRS.getSingleton().getClass().getName()).debug("Problemas obteniendo el código EPSG", e);
		}

		return null;
	}

	/**
	 * Convierte un IProjection a una cadena de texto Wkt
	 * @param projection
	 * @return
	 */
	public static String convertIProjectionToWkt(IProjection projection) {
		OGRSpatialReference oSRSSource = new OGRSpatialReference();

		String code = projection.getAbrev();
		String name = code.substring(0, code.indexOf(":"));
		code = code.substring(code.indexOf(":") + 1);

		try {
			do
				if (name.equals("EPSG")) {
					OGRSpatialReference.importFromEPSG(oSRSSource, Integer.parseInt(code));
					break;
				}
			while (false);
			return OGRSpatialReference.exportToWkt(oSRSSource);
		} catch (CrsGdalException e) {
			LoggerFactory.getLogger(CRS.getSingleton().getClass().getName()).debug("Problemas obteniendo el código WKT", e);
		} catch (NumberFormatException e) {
			LoggerFactory.getLogger(CRS.getSingleton().getClass().getName()).debug("Problemas obteniendo el código WKT", e);
		} catch (OGRException e) {
			LoggerFactory.getLogger(CRS.getSingleton().getClass().getName()).debug("Problemas obteniendo el código WKT", e);
		}
		return null;
	}
}
