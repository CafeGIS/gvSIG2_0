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
package org.gvsig.raster.dataset;

import java.awt.geom.AffineTransform;
import java.util.Date;

import org.cresques.cts.IProjection;
import org.cresques.geo.Projected;
import org.gvsig.raster.datastruct.Extent;
/**
 * Ancestro de todos los formatos geográficos
 */
public abstract class GeoInfo implements Projected {
	protected IProjection     proj                   = null;
	protected long            fileSize               = 0;
	protected long            bytesReaded            = 0;
	protected long            lineCnt                = 0;
	protected String          name;
	/**
	 * Transformación creada a partir de la información de georreferencia de la
	 * propia imagen. Esta información está en la cabecera o en ficheros
	 * worldfile.
	 */
	protected AffineTransform ownTransformation      = null;
	/**
	 * Transformación asignada de forma externa, bien desde el fichero rmf o
	 * asignada directamente por el usuario.
	 */
	protected AffineTransform externalTransformation = null;

	public GeoInfo() {}

		public GeoInfo(IProjection p, Object param) {
		proj = p;
		if (param instanceof String)
			name = translateFileName((String) param);
		else
			if (param instanceof IRegistrableRasterFormat)
				name = ((IRegistrableRasterFormat) param).getFormatID();
			else
				name = String.valueOf(System.currentTimeMillis());
		ownTransformation = new AffineTransform();
		externalTransformation = new AffineTransform();
	}
		
	/**
	 * Traduce el nombre del fichero por un alias asignado por el propio driver.
	 * Cuando es traducido por un alias el driver intentará abrir el alias y no el
	 * fichero. Esto es util porque algunos formatos tienen la extensión en el
	 * fichero de cabecera pero lo que se abre realmente es el fichero de datos.
	 * @param fileName
	 * @return
	 */
	public String translateFileName(String fileName) {
		return fileName;
	}

	public String getFName() {
		return name;
	}

	public void setFName(String n) {
		name = n;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long sz) {
		fileSize = sz;
	}

	public IProjection getProjection() {
		return proj;
	}

	public void setProjection(IProjection p) {
		proj = p;
	}

	/**
	 * Extent completo del raster.
	 * @return Extent
	 */
	abstract public Extent getExtent();

	/**
	 * Este es el extent sobre el que se ajusta una petición para que esta no
	 * exceda el extent máximo del raster. Para un raster sin rotar será igual al
	 * extent pero para un raster rotado será igual al extent del raster como si
	 * no tuviera rotación. Esto ha de ser así ya que la rotación solo se hace
	 * sobre la vista y las peticiones han de hacerse en coordenadas de la imagen
	 * sin shearing aplicado.
	 * @return Extent
	 */
	abstract public Extent getExtentWithoutRot();

	abstract public GeoInfo load();

	abstract public void close();

	/**
	 * Filtra espacios en blanco. Deja solo uno por
	 */
	public static String filterWS(String buf) {
		boolean lastCharWhite = false;
		String str = "";
		buf = buf.trim();

		for (int i = 0; i < buf.length(); i++) {
			char c = buf.charAt(i);

			if (Character.isWhitespace(c)) {
				if (lastCharWhite) {
					continue;
				}
				lastCharWhite = true;
				c = ' ';
			} else {
				lastCharWhite = false;
			}
			str += c;
		}

		return str;
	}

	protected long getTime() {
		return (new Date()).getTime();
	}

	/**
	 * Obtiene la proyección asociada al dataset en formato de cadena de texto
	 * @return Proyección
	 */
	public String getWktProjection() {
		return null;
	}

	/**
	 * Asigna una transformación al raster para que se tenga en cuenta en la
	 * asignación del setView. Esta asignación recalcula el extent, el
	 * requestExtent y asigna el AffineTransform que se usará para la
	 * transformación. Esta transformación será considerada como si la imagen
	 * tuviera asociado un rmf.
	 * @param t Transformación afín a aplicar
	 */
	public void setAffineTransform(AffineTransform t) {
		externalTransformation = (AffineTransform) t.clone();
	}

	/**
	 * Obtiene la transformación afin aplicada en las peticiones con coordenadas
	 * reales. Esta corresponde al producto matricial entre la transformación de
	 * la propia georreferenciación del raster (ownTransformation) y la
	 * transformación que se le aplique de forma externa. Si esta última no existe
	 * será la matriz identidad.
	 * @return Matriz de la transformación afín.
	 */
	public AffineTransform getAffineTransform() {
		return externalTransformation;
	}

	/**
	 * Elimina la matriz de transformación asociada al raster y que se tiene en
	 * cuenta para el setView. Este reseteo tendrá en cuenta que si el raster
	 * tiene asociado un rmf esta transformación no será eliminada sino que se
	 * asignará la correspondiente al rmf existente.
	 * @return devuelve true si tiene fichero rmf asociado y false si no lo tiene.
	 */
	public void resetAffineTransform() {
		externalTransformation.setToIdentity();
	}

	/**
	 * Obtiene la matriz de transformación del propio raster. Esta matriz es la
	 * encargada de convertir las coordenadas de la petición en coordenadas a las
	 * que se pide a la libreria. En gdal, por ejemplo, se piden las coordenadas a
	 * la libreria en coordenadas pixel por lo que esta matriz tendrá la
	 * georreferenciación asociada en el worldfile o cabecera. Otras librerias
	 * como ermapper la petición a la libreria se hace en coordenadas geograficas
	 * que son las mismas en las que pide el usuario de gvSIG por lo que esta
	 * matriz en este caso se inicializa con la identidad.
	 * @return
	 */
	public AffineTransform getOwnTransformation() {
		return ownTransformation;
	}
}
