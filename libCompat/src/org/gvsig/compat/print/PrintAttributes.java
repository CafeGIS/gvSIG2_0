package org.gvsig.compat.print;

public interface PrintAttributes {
	
	public static final int PRINT_QUALITY_DRAFT = 0;
	public static final int PRINT_QUALITY_NORMAL = 1;
	public static final int PRINT_QUALITY_HIGH = 2;
	
	public int getPrintQuality();
	public void setPrintQuality(int pq);

}
