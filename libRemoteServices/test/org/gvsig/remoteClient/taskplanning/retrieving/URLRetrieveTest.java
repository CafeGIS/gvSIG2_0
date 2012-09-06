/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */

/* CVS MESSAGES:
 *
 * $Id: URLRetrieveTest.java 5230 2006-05-16 17:10:27Z jaume $
 * $Log$
 * Revision 1.3  2006-05-16 17:10:27  jaume
 * *** empty log message ***
 *
 * Revision 1.2  2006/05/15 09:39:52  jaume
 * *** empty log message ***
 *
 * Revision 1.1  2006/05/12 07:45:49  jaume
 * some warnings removed
 *
 * Revision 1.1  2006/05/12 07:15:45  jaume
 * *** empty log message ***
 *
 *
 */
package org.gvsig.remoteClient.taskplanning.retrieving;

import java.io.File;

import junit.framework.TestCase;

import org.gvsig.remoteClient.taskplanning.IQueue;
import org.gvsig.remoteClient.taskplanning.IRunnableTask;
/**
 * 
 * @author jaume
 *
 */
public class URLRetrieveTest extends TestCase {
	private final static String tempDirectoryPath = System.getProperty("java.io.tmpdir")+"tmp-andami";
	IQueue queue;
	IRunnableTask task1, task2, task3, task4;
	boolean finished1, finished2, finished3, finished4;
	RetrieveListener listener1 = new RetrieveListener() {
		public void transferEventReceived(RetrieveEvent event) {
			int e = event.getType();
			printMessage("listener1", event.getType());
			if (e==RetrieveEvent.REQUEST_FINISHED || e == RetrieveEvent.REQUEST_FAILED || e == RetrieveEvent.REQUEST_CANCELLED )
				finished1 = true;
		}
	};
	
	String protocol = URLRequest.HTTP;
	String host1 = "192.168.0.223";
	String page1 = "cgi-bin/mapserv_46.exe?map=c:\\ms4w\\Apache\\cgi-bin\\demo.map&&REQUEST=GetMap&SERVICE=WMS&VERSION=1.1.1&LAYERS=comunidades&SRS=EPSG:23030&BBOX=507522.0,4154976.282477341,942309.0,4552983.717522658&WIDTH=332&HEIGHT=305&FORMAT=image/png&STYLES=&TRANSPARENT=TRUE";
	URLRequest request1;
	
	String host2 = "localhost";
	String page2 = "aegCapabilities1.3.xml";
	URLRequest request2;
	RetrieveListener listener2 = new RetrieveListener() {
		public void transferEventReceived(RetrieveEvent event) {
			int e = event.getType();
			printMessage("listener2", event.getType());
			if (e==RetrieveEvent.REQUEST_FINISHED || e == RetrieveEvent.REQUEST_FAILED || e==RetrieveEvent.REQUEST_CANCELLED )
				finished2 = true;

		}
	};
	
	String host3 = "localhost";
	String page3 = "avalencia.ecw";
	URLRequest request3;
	RetrieveListener listener3 = new RetrieveListener() {
		public void transferEventReceived(RetrieveEvent event) {
			int e = event.getType();
			printMessage("listener3", event.getType());
			if (e==RetrieveEvent.REQUEST_FINISHED || e == RetrieveEvent.REQUEST_FAILED  || e == RetrieveEvent.REQUEST_CANCELLED )
				finished3 = true;

		}
	};
	
	String host4 = "192.168.0.223";
	String page4 = "prueba.rar";
	URLRequest request4;
	RetrieveListener listener4 = new RetrieveListener() {
		public void transferEventReceived(RetrieveEvent event) {
			int e = event.getType();
			printMessage("listener4", event.getType());
			if (e==RetrieveEvent.REQUEST_FINISHED || e == RetrieveEvent.REQUEST_FAILED  || e == RetrieveEvent.REQUEST_CANCELLED )
				finished4 = true;

		}
	};
	{
		cleanFiles();
	}
	private static void cleanFiles(){
		try{
			File tempDirectory = new File(tempDirectoryPath);
			
			File[] files = tempDirectory.listFiles();
			if (files!=null) {
				for (int i = 0; i < files.length; i++) {
					// sólo por si en un futuro se necesitan crear directorios temporales
					files[i].delete();
				}
			}
			tempDirectory.delete();
		} catch (Exception e) {	}
	}
	
	
	public void setUp() {
		File tmpDir = new File(tempDirectoryPath);
		if (!tmpDir.exists())
			tmpDir.mkdir();
		finished1 = finished2 = finished3 = finished4 = false;
		System.out.println("\n\n\nSetting up test..");
		queue = new RetrieveQueue("http://192.168.0.223/cgi-bin/mapserv_46.exe?map=c:\\ms4w\\Apache\\cgi-bin\\demo.map");
		
		request1 = new URLRequest();
		// http://192.168.0.223/cgi-bin/mapserv_46.exe?map=c:\\ms4w\\Apache\\cgi-bin\\demo.map&&REQUEST=GetMap&SERVICE=WMS&VERSION=1.1.1&LAYERS=comunidades&SRS=EPSG:23030&BBOX=507522.0,4154976.282477341,942309.0,4552983.717522658&WIDTH=332&HEIGHT=305&FORMAT=image/png&STYLES=&TRANSPARENT=TRUE
		request1.setProtocol(protocol);
		request1.setHost(host1);
		request1.setFile(page1);
		request1.setFileName(tempDirectoryPath+File.separator+"task1");
		request1.setRequestType(URLRequest.GET);
		task1 = new URLRetrieveTask(request1, listener1); 
		
		request2 = new URLRequest();
		// http://localhost/aegCapabilities1.3.xml
		request2.setProtocol(protocol);
		request2.setHost(host2);
		request2.setFile(page2);
		request2.setFileName(tempDirectoryPath+File.separator+"task2");
		request2.setRequestType(URLRequest.GET);
		task2 = new URLRetrieveTask(request2, listener2);
		
		request3 = new URLRequest();
		// http://localhost/avalencia.ecw
		request3.setProtocol(protocol);
		request3.setHost(host3);
		request3.setFile(page3);
		request3.setFileName(tempDirectoryPath+File.separator+"task3");
		request3.setRequestType(URLRequest.GET);
		task3 = new URLRetrieveTask(request3, listener3);
		
		request4 = new URLRequest();
		// http://192.168.0.223/prueba.rar
		request4.setProtocol(protocol);
		request4.setHost(host4);
		request4.setFile(page4);
		request4.setFileName(tempDirectoryPath+File.separator+"task4");
		request4.setRequestType(URLRequest.GET);
		
		task4 = new URLRetrieveTask(request4, listener4);
		
	}
	
	public void testRetrieve() {
		queue.put(task1);
		queue.put(task2);
		
		while(!queue.isEmpty()) {		
		}
	}
	
	public void testCancelling() {
		long time = System.currentTimeMillis();
		
		queue.put(task1);
		queue.put(task2);
		boolean more = true;
		while (!queue.isEmpty()) {
			if (more && System.currentTimeMillis()-time > 1000) { // wait 1 seconds and cancel
				task2.cancel();
				more = false;
			}
		}
	}
	
	public void testRequestManager() {
		System.out.println("tests parallel downloading from different servers");
		request3.setFileName("task3");
		// http://localhost/avalencia.ecw
		
		request4.setFileName("task4");
		// http://192.168.0.223/prueba.rar
		RequestManager manager = RequestManager.getInstance();
		manager.addURLRequest(request3, listener3);
		manager.addURLRequest(request4, listener4);
		manager.addURLRequest(request4, listener1);
		while (!(finished1 && finished3 && finished4)) { 
			
		}
	}
	
	public void testCocurrentTransfers() {
		finished1 = finished2 = finished3 = finished4 = false;
		System.out.println("tests to merge two or more equivalent transfers into one");
		RequestManager manager = RequestManager.getInstance();
		//manager.removeURLRequest(request3);
		request3.setFileName("task3");
		
		manager.addURLRequest(request3, listener1);
		manager.addURLRequest(request3, listener2);
		manager.addURLRequest(request3, listener3);
		
		manager.addURLRequest(request3, listener4);
		while (!(finished1 && finished2 && finished3 && finished4)) { 
			
		}
	}
	
	public void printMessage(String who, int code) {
		switch (code) {
		case RetrieveEvent.CONNECTING:
			System.out.println(who+": connecting");
			break;
		case RetrieveEvent.TRANSFERRING:
			System.out.println(who+": transferring");
			break;
		case RetrieveEvent.REQUEST_CANCELLED:
			System.out.println(who+": cancelled");
			break;
		case RetrieveEvent.REQUEST_FINISHED:
			System.out.println(who+": finished");
			break;
		case RetrieveEvent.REQUEST_FAILED:
			System.err.println(who+": failed");
			break;
		case RetrieveEvent.NOT_STARTED:
			System.out.println(who+": not started");
			break;
		case RetrieveEvent.POSTPROCESSING:
			System.out.println(who+": postprocessing");
			break;
		}
	}
}
