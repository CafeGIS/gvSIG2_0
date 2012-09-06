package org.gvsig.raster.util;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.datastruct.TransparencyRange;


/**
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TransparencyRangeTest extends TestCase{
		
	private TransparencyRange tr = new TransparencyRange();
	private TransparencyRange tr1 = new TransparencyRange();
	private TransparencyRange tr2 = new TransparencyRange();
	private TransparencyRange tr3 = new TransparencyRange();
	
	static{
		RasterLibrary.wakeUp();	
	}
	
	public void start(){
		this.setUp();
		this.testStack();
	}
	
	public void setUp() {
		System.err.println("TransparencyRangeTest running...");
		
		//Simplificaciones entre dos intervalos
		
		tr.setRed(new int[]{3 , 5});
		tr.setGreen(new int[]{4 , 7});
		tr.setBlue(new int[]{1 , 5});
		tr.setAnd(true);
		tr.loadStrEntryFromValues();
		
		tr1.setRed(new int[]{4 , 7});
		tr1.setGreen(new int[]{3 , 5});
		tr1.setBlue(new int[]{2 , 4});
		tr1.setAnd(true);
		tr1.loadStrEntryFromValues();
		
		tr2.setRed(new int[]{5 , 10});
		tr2.setGreen(new int[]{10 , 15});
		tr2.setBlue(new int[]{5 , 14});
		tr2.setAnd(true);
		tr2.loadStrEntryFromValues();
		
		tr3.setRed(new int[]{6 , 12});
		tr3.setGreen(new int[]{14 , 20});
		tr3.setBlue(new int[]{16 , 24});
		tr3.setAnd(true);
		tr3.loadStrEntryFromValues();
	}
	
	public void testStack(){
		int[] r = tr.union(tr1.getRed(), RasterDataset.RED_BAND);
		int[] g = tr.union(tr1.getGreen(), RasterDataset.GREEN_BAND);
		int[] b = tr.union(tr1.getBlue(), RasterDataset.BLUE_BAND);
		
		assertEquals(r[0], 3);
		assertEquals(r[1], 7);
		assertEquals(g[0], 3);
		assertEquals(g[1], 7);
		assertEquals(b[0], 1);
		assertEquals(b[1], 5);
		
		//Simplificaciones de una lista de intervalos
		ArrayList l = new ArrayList();
		l.add(tr);
		l.add(tr1);
		l.add(tr2);
		l.add(tr3);
		
		/*Transparency t = new Transparency();
		t.setTransparencyRangeList(l);
		t.simplify();
		
		for (int i = 0; i < l.size(); i++)
			System.out.println("Entrada: " + ((TransparencyRange)l.get(i)).getStrEntry());*/
	}
	
}
