package org.gvsig.remoteClient.Tests;

import java.awt.geom.Rectangle2D;

import junit.framework.TestCase;

import org.gvsig.remoteClient.wms.WMSClient;
import org.gvsig.remoteClient.wms.WMSStatus;

public class Test extends TestCase
{

	public static void main(String[] args)
	{
		try
		{
			WMSClient wmsclient = new WMSClient("http://orto.cth.gva.es:80/wmsconnector/com.esri.wms.Esrimap/wms_urbanismo_tematicos?");
			//http://orto.cth.gva.es/wmsconnector/com.esri.wms.Esrimap/wms_urbanismo_tematicos?REQUEST=GetMap&SERVICE=WMS&VERSION=1.1.1&LAYERS=3&STYLES=&SRS=EPSG:4326&BBOX=-1.5597948439,37.8178641941,0.5286772976,40.8151000365&WIDTH=500&HEIGHT=500&FORMAT=image/jpeg&EXCEPTIONS=XML

			wmsclient.connect(null);
			//System.out.println();
			//System.out.println(Utilities.Vector2CS(layerNames));

			WMSStatus status = new WMSStatus();
			status.addLayerName("3");
//			status.addLayerName("europa_rivieren");
//			status.addStyleName("default");
//			status.addStyleName("default");
			status.setSrs("EPSG:4326");

			Rectangle2D extent = new Rectangle2D.Double(-1.5597948439,37.8178641941,-1.5597948439-0.5286772976,37.8178641941-40.8151000365);

			status.setExtent(extent);
			status.setFormat("image/jpeg");
			status.setHeight(500);
			status.setWidth(500);

			//wmsclient.getMap(status);

			//wmsclient.getFeatureInfo(status,300,300);

			System.out.println();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

}
