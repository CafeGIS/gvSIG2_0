package org.gvsig.raster.grid.render;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;


/**
 * Test para probar la funcionalidad de formateo de array de renderización
 * AUTO:OK
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class FormatArrayRenderTest extends TestCase{
	private Rendering r = null;
	
	static{
		RasterLibrary.wakeUp();	
	}
	
	public void start(){
		this.setUp();
		this.testStack();
	}
	
	public void setUp() {
		System.err.println("FormatArrayRenderTest running...");
		r = new Rendering();
	}
	
	public void testStack(){
		int[] out = r.formatArrayRenderBand(new int[]{0, 1, 2});
		validation(out, new int[]{0, 1, 2});
		//show(out);
		out = r.formatArrayRenderBand(new int[]{-1, 1, 2});
		validation(out, new int[]{1, 2});
		//show(out);
		out = r.formatArrayRenderBand(new int[]{-1, 0, -1});
		validation(out, new int[]{0});
		//show(out);
		out = r.formatArrayRenderBand(new int[]{-1, -1, 0, 3, -1, 1, -1});
		validation(out, new int[]{0, 3, 1});
		//show(out);
	}
	
	private void validation(int[] a, int[] b){
		if(a.length != b.length)
			fail();
		for(int i = 0; i < a.length; i++)
			assertEquals(a[i], b[i]);
	}
	
	/*private void show(int[] out){
		for(int i = 0; i < out.length; i++)
			System.out.print(out[i] + " ");
		System.out.println();
	}*/
}
