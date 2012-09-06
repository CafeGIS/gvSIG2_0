/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
 *   Campus Universitario s/n
 *   02071 Alabacete
 *   Spain
 *
 *   +34 967 599 200
 */

package org.gvsig.crs;

import java.util.TreeMap;

import org.cresques.cts.ICRSFactory;
import org.cresques.cts.IProjection;
import org.gvsig.crs.repository.EpsgRepository;
import org.gvsig.crs.repository.EpsgRepositoryGT;
import org.gvsig.crs.repository.EsriRepository;
import org.gvsig.crs.repository.EsriRepositoryGT;
import org.gvsig.crs.repository.ICrsRepository;
import org.gvsig.crs.repository.Iau2000Repository;
import org.gvsig.crs.repository.Iau2000RepositoryGT;
import org.gvsig.crs.repository.UsrRepository;
import org.gvsig.crs.repository.UsrRepositoryGT;

/**
 * Clase que consigue el CRS a través del código de la EPSG o de
 * la cadena WKT
 *
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */

public class CrsFactory implements ICRSFactory {
	static TreeMap data = new TreeMap();

	public CrsFactory() {
	}
	/**
	 * Obtiene un CRS a partir de su código (p.e. EPSG:23030).
	 * @param code
	 * @return
	 * @throws CrsException
	 */
	public ICrs getCRS(String code) throws CrsException {

		/*if (data.containsKey(code))
			return (ICrs) data.get(code);*/

		String repoId = "";
		String crsCode = "";
		ICrs crs = null;

		if(code.indexOf(":", code.indexOf(":")+1)<0){
			repoId = code.substring(0, code.indexOf(":"));
			crsCode = code.substring(code.indexOf(":")+1);

			ICrsRepository repo = null;

			if(repoId.equals("EPSG")){
				repo = new EpsgRepositoryGT();
				crs = repo.getCrs(crsCode);
				if (crs==null) {
					repo = new EpsgRepository();
					crs = repo.getCrs(crsCode);
				}
			}else if (repoId.equals("IAU2000")){
				repo = new Iau2000RepositoryGT();
				crs = repo.getCrs(crsCode);
				if (crs==null) {
					repo = new Iau2000Repository();
					crs = repo.getCrs(crsCode);
				}
			}else if (repoId.equals("ESRI")){
				repo = new EsriRepositoryGT();
				crs = repo.getCrs(crsCode);
				if (crs==null) {
					repo = new EsriRepository();
					crs = repo.getCrs(crsCode);
				}
			}else if (repoId.equals("USR")){
				repo = new UsrRepositoryGT();
				crs = repo.getCrs(crsCode);
				if (crs==null) {
					repo = new UsrRepository();
					crs = repo.getCrs(crsCode);
				}
			}
		}
		else{
			String sourceParams = null;
			String targetParams = null;

			crsCode = code.substring(0,code.indexOf(":",code.indexOf(":")+1));
			if (code.indexOf("@")==-1){
				crsCode=crsCode.substring(0, crsCode.indexOf(","));
			}else{
				sourceParams = code.substring(code.indexOf("@")+1,code.lastIndexOf("@"));
				targetParams = code.substring(code.lastIndexOf("@")+1);

				if (sourceParams.equals(""))
					sourceParams = null;
				else if (targetParams.equals("1")){ // Compativilidad con versiones de libJCrs sin soporte para transf. compuestas.
					targetParams = sourceParams;
					sourceParams = "";
				}
				if (targetParams.equals("")||targetParams.equals("0")) // Compativilidad con versiones de libJCrs sin soporte para transf. compuestas.
					targetParams = null;
			}
			crs = getCRS(crsCode);
			crs.setTransformationParams(sourceParams,targetParams);
		}

		/*code = crs.getAbrev();

		data.put(code, crs);*/

		return crs;

		/*if (data.containsKey(code))
			return (Crs) data.get(code);

		Crs crs = new Crs(code);

		// LWS Esta línea sobra, cuando el cuadro de diálogo esté
		// mejor hecho.
		code = crs.getAbrev();

		data.put(code, crs);

		return crs;*/
	}

	/**
	 *
	 * @param epsg_code
	 * @param code
	 * @return
	 * @throws CrsException
	 */
	public ICrs getCRS(int epsg_code, String code) throws CrsException {
		/*if (data.containsKey(code))
			return (Crs) data.get(code);*/

		Crs crs = new Crs(epsg_code, code);

		// LWS Esta línea sobra, cuando el cuadro de diálogo esté
		// mejor hecho.
		/*code = crs.getAbrev();

		data.put(code, crs);*/

		return crs;
	}

	/**
	 *
	 * @param epsg_code
	 * @param code
	 * @param params
	 * @return
	 * @throws CrsException
	 */
	public ICrs getCRS(int epsg_code, String code, String params) throws CrsException {
		/*if (data.containsKey(code))
			return (Crs) data.get(code);*/

		Crs crs = new Crs(epsg_code, code,params);

		// LWS Esta línea sobra, cuando el cuadro de diálogo esté
		// mejor hecho.
		/*code = crs.getAbrev();

		data.put(code, crs);*/

		return crs;
	}

	/**
	 *
	 */
	public IProjection get(String name) {
		// TODO Auto-generated method stub
		try {
			return getCRS(name);
		} catch (CrsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *
	 */
	public boolean doesRigurousTransformations() {
		return true;
	}
}
