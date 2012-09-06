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
package org.gvsig.raster.dataset;

import java.awt.geom.AffineTransform;
import java.util.Date;

import org.cresques.cts.IProjection;
import org.cresques.geo.Projected;
import org.gvsig.raster.datastruct.Extent;
/**
 * Ancestro de todos los formatos geogr�ficos
 */
public abstract class GeoInfo implements Projected {
	protected IProjection     proj                   = null;
	protected long            fileSize               = 0;
	protected long            bytesReaded            = 0;
	protected long            lineCnt                = 0;
	protected String          name;
	/**
	 * Transformaci�n creada a partir de la informaci�n de georreferencia de la
	 * propia imagen. Esta informaci�n est� en la cabecera o en ficheros
	 * worldfile.
	 */
	protected AffineTransform ownTransformation      = null;
	/**
	 * Transformaci�n asignada de forma externa, bien desde el fichero rmf o
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
	 * Cuando es traducido por un alias el driver intentar� abrir el alias y no el
	 * fichero. Esto es util porque algunos formatos tienen la extensi�n en el
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
	 * Este es el extent sobre el que se ajusta una petici�n para que esta no
	 * exceda el extent m�ximo del raster. Para un raster sin rotar ser� igual al
	 * extent pero para un raster rotado ser� igual al extent del raster como si
	 * no tuviera rotaci�n. Esto ha de ser as� ya que la rotaci�n solo se hace
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
	 * Obtiene la proyecci�n asociada al dataset en formato de cadena de texto
	 * @return Proyecci�n
	 */
	public String getWktProjection() {
		return null;
	}

	/**
	 * Asigna una transformaci�n al raster para que se tenga en cuenta en la
	 * asignaci�n del setView. Esta asignaci�n recalcula el extent, el
	 * requestExtent y asigna el AffineTransform que se usar� para la
	 * transformaci�n. Esta transformaci�n ser� considerada como si la imagen
	 * tuviera asociado un rmf.
	 * @param t Transformaci�n af�n a aplicar
	 */
	public void setAffineTransform(AffineTransform t) {
		externalTransformation = (AffineTransform) t.clone();
	}

	/**
	 * Obtiene la transformaci�n afin aplicada en las peticiones con coordenadas
	 * reales. Esta corresponde al producto matricial entre la transformaci�n de
	 * la propia georreferenciaci�n del raster (ownTransformation) y la
	 * transformaci�n que se le aplique de forma externa. Si esta �ltima no existe
	 * ser� la matriz identidad.
	 * @return Matriz de la transformaci�n af�n.
	 */
	public AffineTransform getAffineTransform() {
		return externalTransformation;
	}

	/**
	 * Elimina la matriz de transformaci�n asociada al raster y que se tiene en
	 * cuenta para el setView. Este reseteo tendr� en cuenta que si el raster
	 * tiene asociado un rmf esta transformaci�n no ser� eliminada sino que se
	 * asignar� la correspondiente al rmf existente.
	 * @return devuelve true si tiene fichero rmf asociado y false si no lo tiene.
	 */
	public void resetAffineTransform() {
		externalTransformation.setToIdentity();
	}

	/**
	 * Obtiene la matriz de transformaci�n del propio raster. Esta matriz es la
	 * encargada de convertir las coordenadas de la petici�n en coordenadas a las
	 * que se pide a la libreria. En gdal, por ejemplo, se piden las coordenadas a
	 * la libreria en coordenadas pixel por lo que esta matriz tendr� la
	 * georreferenciaci�n asociada en el worldfile o cabecera. Otras librerias
	 * como ermapper la petici�n a la libreria se hace en coordenadas geograficas
	 * que son las mismas en las que pide el usuario de gvSIG por lo que esta
	 * matriz en este caso se inicializa con la identidad.
	 * @return
	 */
	public AffineTransform getOwnTransformation() {
		return ownTransformation;
	}
}
