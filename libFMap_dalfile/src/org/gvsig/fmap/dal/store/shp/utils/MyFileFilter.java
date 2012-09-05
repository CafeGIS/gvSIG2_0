package org.gvsig.fmap.dal.store.shp.utils;

import java.io.File;
import java.io.FileFilter;

public class MyFileFilter implements FileFilter {
	private String shpPath;
	public MyFileFilter(String shpPath){
		this.shpPath=shpPath;
	}
	public boolean accept(File pathname) {
		String strend=pathname.getAbsolutePath();
		strend=strend.substring(0,strend.length()-3);
		String strshp=shpPath.substring(0, shpPath.length() - 3);
		if (strend.equals(strshp)) {
			return true;
		}
		return false;
	}

}