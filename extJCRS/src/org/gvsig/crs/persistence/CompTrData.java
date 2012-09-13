package org.gvsig.crs.persistence;

/**
 * Clase que contiene los datos de una Transformación compuestas que son 
 * utilizados por el mecanismo de persistencia para almacenar las 
 * Transformaciones recientemente usadas. 
 * 
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class CompTrData extends TrData {
	
	TrData 			firstTr 		= null;
	TrData 			secondTr 		= null;
	public CompTrData(String authority, int code, String name, String crsSource, String crsTarget, String details, TrData firstTr, TrData secondTr) {
		super(authority, code, name, crsSource, crsTarget, details);
		this.firstTr = firstTr;
		this.secondTr = secondTr;
	}
	
	public CompTrData(TrData firstTr, TrData secondTr) {
		super("COMP", 0, "----", firstTr.getCrsSource(), secondTr.getCrsTarget(),
				firstTr.getDetails()+" @ "+secondTr.getDetails());
		this.firstTr = firstTr;
		this.secondTr = secondTr;
	}

	public TrData getFirstTr() {
		return firstTr;
	}

	public void setFirstTr(TrData firstTr) {
		this.firstTr = firstTr;
	}

	public TrData getSecondTr() {
		return secondTr;
	}

	public void setSecondTr(TrData secondTr) {
		this.secondTr = secondTr;
	}
}
