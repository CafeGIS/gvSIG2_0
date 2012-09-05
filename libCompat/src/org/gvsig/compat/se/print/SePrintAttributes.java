package org.gvsig.compat.se.print;

import org.gvsig.compat.print.PrintAttributes;

public class SePrintAttributes implements PrintAttributes{

	int pq = 0;
	
	public int getPrintQuality() {
		return pq;
	}

	public void setPrintQuality(int pq) {
		this.pq=pq;
		
	}

}
