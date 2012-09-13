package org.gvsig.crs.jgridshift;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import au.com.objectix.jgridshift.GridShift;
import au.com.objectix.jgridshift.GridShiftFile;
import au.com.objectix.jgridshift.SubGrid;

public class JGridShiftTest {
	
	public static void main(String[] args){
		//InputStream fis = null;
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile("/home/dguerrero/Desarrollo-gvSIG/crs/workspace/extJCRS/data/sped2et.gsb","r");
			//fis = new FileInputStream("/home/dguerrero/Desarrollo-gvSIG/crs/workspace/extJCRS/data/sped2et.gsb");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GridShiftFile gsf = new GridShiftFile();
		try {
			gsf.loadGridShiftFile(raf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SubGrid subGrid[] = gsf.getSubGridTree(); 
		System.out.println(subGrid.length);
		System.out.println(subGrid[0].getMaxLat());
		System.out.println(subGrid[0].getMinLat());
		System.out.println(subGrid[0].getMaxLon());
		System.out.println(subGrid[0].getMinLon());
		GridShift gridShift = new GridShift();
	}

}
