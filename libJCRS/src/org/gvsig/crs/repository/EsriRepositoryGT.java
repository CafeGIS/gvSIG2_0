package org.gvsig.crs.repository;

import org.geotools.referencing.CRS;
import org.gvsig.crs.CrsGT;
import org.gvsig.crs.ICrs;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class EsriRepositoryGT implements ICrsRepository {
	
	public ICrs getCrs(String code) {
		CrsGT crsGT = null;
		try {
			CoordinateReferenceSystem crs = CRS.decode("ESRI:"+code);
			crsGT = new CrsGT(crs);
		} catch (NoSuchAuthorityCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;			
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return crsGT;
	}

}
