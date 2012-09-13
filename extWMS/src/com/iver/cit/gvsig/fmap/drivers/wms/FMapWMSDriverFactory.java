package com.iver.cit.gvsig.fmap.drivers.wms;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.Hashtable;

public class FMapWMSDriverFactory {
	private static Hashtable drivers = new Hashtable();
	private FMapWMSDriverFactory() { }
	
	static public final FMapWMSDriver getFMapDriverForURL(URL url) throws ConnectException, IOException {
		FMapWMSDriver drv = (FMapWMSDriver) drivers.get(url);
		if (drv == null) {
			drv = new FMapWMSDriver(url);
			drivers.put(url, drv);
		}
		return drv;
	}

}
