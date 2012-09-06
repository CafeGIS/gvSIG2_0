package es.idr.crs.crsGT;

import org.cresques.cts.IProjection;
import org.gvsig.crs.CrsException;
import org.gvsig.crs.CrsGT;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.Proj4;
import org.gvsig.crs.repository.EpsgRepositoryGT;
import org.gvsig.crs.repository.EsriRepositoryGT;
import org.gvsig.crs.repository.Iau2000RepositoryGT;
import org.gvsig.crs.repository.UsrRepositoryGT;

public class CrsGTTest {

	public CrsGTTest() {
		epsgTest();
		esriTest();
		iau2000Test();
		usrTest();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CrsGTTest g = new CrsGTTest();
	}
	
	public void epsgTest() {
		EpsgRepositoryGT repo = new EpsgRepositoryGT();
		IProjection crs=null;
		crs = repo.getCrs("23030");
		if (crs!=null){
			System.out.println(crs.getAbrev());
			System.out.println(((ICrs)crs).getCode());
			System.out.println(((ICrs)crs).getWKT());
			System.out.println(((ICrs)crs).getDatum().getEIFlattening());
			System.out.println(((ICrs)crs).getDatum().getESemiMajorAxis());
			System.out.println(((ICrs)crs).isProjected()+"\n\n");
			
			System.out.println("----------- CrsWkt:--------------");
			System.out.println(((ICrs)crs).getCrsWkt().getAuthority()[0]+":"+((ICrs)crs).getCrsWkt().getAuthority()[1]);
			System.out.println(((ICrs)crs).getCrsWkt().getName());
			System.out.println(((ICrs)crs).getCrsWkt().getDatumName());
			System.out.println(((ICrs)crs).getCrsWkt().getGeogcs());
			System.out.println(((ICrs)crs).getCrsWkt().getProjcs());
			System.out.println(((ICrs)crs).getCrsWkt().getProjection());
			System.out.println(((ICrs)crs).getCrsWkt().getPrimen()[0]+" -- "+ ((ICrs)crs).getCrsWkt().getPrimen()[1]+"\n\n");
			
			
			
			try {
				Proj4 proj4 = new Proj4();
				System.out.println(proj4.exportToProj4(((CrsGT)crs).getCrsGT()));
			} catch (CrsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void esriTest() {
		EsriRepositoryGT repo = new EsriRepositoryGT();
		IProjection crs=null;
		crs = repo.getCrs("23030");
		if (crs!=null){
			System.out.println(crs.getAbrev());
			System.out.println(((ICrs)crs).getCode());
			System.out.println(((ICrs)crs).getWKT());
			System.out.println(((ICrs)crs).getDatum().getEIFlattening());
			System.out.println(((ICrs)crs).getDatum().getESemiMajorAxis());
			System.out.println(((ICrs)crs).isProjected()+"\n\n");
			
			System.out.println("----------- CrsWkt:--------------");
			System.out.println(((ICrs)crs).getCrsWkt().getAuthority()[0]+":"+((ICrs)crs).getCrsWkt().getAuthority()[1]);
			System.out.println(((ICrs)crs).getCrsWkt().getName());
			System.out.println(((ICrs)crs).getCrsWkt().getDatumName());
			System.out.println(((ICrs)crs).getCrsWkt().getGeogcs());
			System.out.println(((ICrs)crs).getCrsWkt().getProjcs());
			System.out.println(((ICrs)crs).getCrsWkt().getProjection());
			System.out.println(((ICrs)crs).getCrsWkt().getPrimen()[0]+" -- "+ ((ICrs)crs).getCrsWkt().getPrimen()[1]+"\n\n");
			
			
			
			try {
				Proj4 proj4 = new Proj4();
				System.out.println(proj4.exportToProj4(((CrsGT)crs).getCrsGT()));
			} catch (CrsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void iau2000Test() {
		Iau2000RepositoryGT repo = new Iau2000RepositoryGT();
		IProjection crs=null;
		crs = repo.getCrs("19900");
		if (crs!=null){
			System.out.println(crs.getAbrev());
			System.out.println(((ICrs)crs).getCode());
			System.out.println(((ICrs)crs).getWKT());
			System.out.println(((ICrs)crs).getDatum().getEIFlattening());
			System.out.println(((ICrs)crs).getDatum().getESemiMajorAxis());
			System.out.println(((ICrs)crs).isProjected()+"\n\n");
			
			System.out.println("----------- CrsWkt:--------------");
			System.out.println(((ICrs)crs).getCrsWkt().getAuthority()[0]+":"+((ICrs)crs).getCrsWkt().getAuthority()[1]);
			System.out.println(((ICrs)crs).getCrsWkt().getName());
			System.out.println(((ICrs)crs).getCrsWkt().getDatumName());
			System.out.println(((ICrs)crs).getCrsWkt().getGeogcs());
			System.out.println(((ICrs)crs).getCrsWkt().getProjcs());
			System.out.println(((ICrs)crs).getCrsWkt().getProjection());
			System.out.println(((ICrs)crs).getCrsWkt().getPrimen()[0]+" -- "+ ((ICrs)crs).getCrsWkt().getPrimen()[1]+"\n\n");
			
			
			
			try {
				Proj4 proj4 = new Proj4();
				System.out.println(proj4.exportToProj4(((CrsGT)crs).getCrsGT()));
			} catch (CrsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void usrTest() {
		UsrRepositoryGT repo = new UsrRepositoryGT();
		IProjection crs=null;
		crs = repo.getCrs("2000");
		if (crs!=null){
			System.out.println(crs.getAbrev());
			System.out.println(((ICrs)crs).getCode());
			System.out.println(((ICrs)crs).getWKT());
			System.out.println(((ICrs)crs).getDatum().getEIFlattening());
			System.out.println(((ICrs)crs).getDatum().getESemiMajorAxis());
			System.out.println(((ICrs)crs).isProjected()+"\n\n");
			
			System.out.println("----------- CrsWkt:--------------");
			System.out.println(((ICrs)crs).getCrsWkt().getAuthority()[0]+":"+((ICrs)crs).getCrsWkt().getAuthority()[1]);
			System.out.println(((ICrs)crs).getCrsWkt().getName());
			System.out.println(((ICrs)crs).getCrsWkt().getDatumName());
			System.out.println(((ICrs)crs).getCrsWkt().getGeogcs());
			System.out.println(((ICrs)crs).getCrsWkt().getProjcs());
			System.out.println(((ICrs)crs).getCrsWkt().getProjection());
			System.out.println(((ICrs)crs).getCrsWkt().getPrimen()[0]+" -- "+ ((ICrs)crs).getCrsWkt().getPrimen()[1]+"\n\n");
			
			
			
			try {
				Proj4 proj4 = new Proj4();
				System.out.println(proj4.exportToProj4(((CrsGT)crs).getCrsGT()));
			} catch (CrsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
