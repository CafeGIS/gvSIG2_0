package org.gvsig.crs.repository;

import org.gvsig.crs.Crs;


public class RepositoryTest {
	public static void main(String[] args){
		EpsgRepository epsgRep= new EpsgRepository();
		Crs crs = (Crs)epsgRep.getCrs("23030");
		if (crs != null){
			String abrev = crs.getAbrev();
			System.out.println(abrev);
		}
		else
			System.out.println("el código no existe en el repositorio.");
	}
}
