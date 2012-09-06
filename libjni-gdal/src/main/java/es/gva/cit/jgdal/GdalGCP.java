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
package es.gva.cit.jgdal;
/**
 * Contiene las funcionalidades necesarias para el acceso a los
 * elementos de un dataset de gdal correspondiente a una imágen 
 * 
 * @author Nacho Brodin <brodin_ign@gva.es>.<BR> Equipo de desarrollo gvSIG.<BR> http://www.gvsig.gva.es
 * @link http://www.gvsig.gva.es
 */
public class GdalGCP extends JNIBase{

	private native long GdalGCPNat();
	private native long GdalGCPPointsNat(	double pixelXGCP, 
											double pixelYGCP,
											double mapXGCP,
											double mapYGCP,
											double mapZGCP);
	private native void FreeGdalGCPNat();
	
	private String id; 			//identificador 
	private String info;		//información
	private double pixelXGCP;	/** Pixel (x) location of GCP on raster */
	private double pixelYGCP;	/** Line (y) location of GCP on raster */
	private double mapXGCP;		/** X position of GCP in georeferenced space */
	private double mapYGCP; 	/** Y position of GCP in georeferenced space */
	private double mapZGCP;		/** Elevation of GCP, or zero if not known */
		
	/**
	 * Constructor
	 */
	public GdalGCP(){
		cPtr = this.GdalGCPNat();
	}
	
	/**
	 * Constructor
	 * @param pixelXGCP
	 * @param pixelYGCP
	 * @param mapXGCP
	 * @param mapYGCP
	 * @param mapZGCP
	 */
	public GdalGCP(	double pixelXGCP, 
					double pixelYGCP,
					double mapXGCP,
					double mapYGCP,
					double mapZGCP){
		this.pixelXGCP = pixelXGCP;  
		this.pixelYGCP = pixelYGCP;
		this.mapXGCP = mapXGCP; 
		this.mapYGCP = mapYGCP;
		this.mapZGCP = mapYGCP;
	}
	
	protected void finalize(){
		
	}
	
	/**
	 * @return Returns the mapXGCP.
	 */
	public double getMapXGCP() {
		return mapXGCP;
	}
	/**
	 * @param mapXGCP The mapXGCP to set.
	 */
	public void setMapXGCP(double mapXGCP) {
		this.mapXGCP = mapXGCP;
	}
	/**
	 * @return Returns the mapYGCP.
	 */
	public double getMapYGCP() {
		return mapYGCP;
	}
	/**
	 * @param mapYGCP The mapYGCP to set.
	 */
	public void setMapYGCP(double mapYGCP) {
		this.mapYGCP = mapYGCP;
	}
	/**
	 * @return Returns the mapZGCP.
	 */
	public double getMapZGCP() {
		return mapZGCP;
	}
	/**
	 * @param mapZGCP The mapZGCP to set.
	 */
	public void setMapZGCP(double mapZGCP) {
		this.mapZGCP = mapZGCP;
	}
	/**
	 * @return Returns the pixelXGCP.
	 */
	public double getPixelXGCP() {
		return pixelXGCP;
	}
	/**
	 * @param pixelXGCP The pixelXGCP to set.
	 */
	public void setPixelXGCP(double pixelXGCP) {
		this.pixelXGCP = pixelXGCP;
	}
	/**
	 * @return Returns the pixelYGCP.
	 */
	public double getPixelYGCP() {
		return pixelYGCP;
	}
	/**
	 * @param pixelYGCP The pixelYGCP to set.
	 */
	public void setPixelYGCP(double pixelYGCP) {
		this.pixelYGCP = pixelYGCP;
	}
}