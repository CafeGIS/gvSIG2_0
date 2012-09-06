/*
 * Created on 01-oct-2005
 */
package org.gvsig.remoteClient.taskplanning.retrieving;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;

import org.gvsig.remoteClient.taskplanning.IRunnableTask;

/**
 * Clase para bajar ficheros en un thread independiente.
 * La idea (y parte del código) está tomada de EarthFlicks
 * @author Luis W. Sevilla (sevilla_lui@gva.es)
 * @see http://today.java.net/lpt/a/212
 */
public class URLRetrieveTask  implements IRunnableTask {
	
	private boolean running, cancelled;
	private URLRequest request;
	private Vector listeners = new Vector();
	private RetrieveEvent event = new RetrieveEvent();
	private InputStream is;
	
	/**
	 * 
	 */
	public URLRetrieveTask(URLRequest request, RetrieveListener listener) {
		this.request = request;
		addRetrieveListener(listener);
		running = cancelled = false;
	}
	

	public void execute() {
		event.setType(RetrieveEvent.NOT_STARTED);
		fireEvent();
		cancelled = false;
		running= true;
		
		long t = System.currentTimeMillis();
		File f = new File(request.getFileName()+System.currentTimeMillis());
		while (f.exists()) {
			t++;
			f = new File(request.getFileName()+t);
		}
		URL url;
		try {
			event.setType(RetrieveEvent.CONNECTING);
			fireEvent();
			url = request.getUrl();
			request.setFileName(f.getAbsolutePath());
			System.out.println("downloading '"+url+"' to: "+f.getAbsolutePath());
			
			DataOutputStream dos = new DataOutputStream( new BufferedOutputStream(new FileOutputStream(f)));
			byte[] buffer = new byte[1024*256];
			fireEvent();
			is = url.openStream();
			event.setType(RetrieveEvent.TRANSFERRING);
			fireEvent();
			if (!cancelled) {
				long readed = 0;
				for (int i = is.read(buffer); !cancelled && i>0; i = is.read(buffer)){
					dos.write(buffer, 0, i);
					readed += i;
				}
				
				dos.close();
			}
			
			if (cancelled) {
				// Bad incomplete file, delete it.
				System.out.println("download cancelled ("+url+")");
				f.delete();
			} else {
				// register job in the cache
				// TODO això deuria d'estar al principi per a poder capturar
				// treballs que s'estan fent però que encara no s'han acabat
				synchronized (this) {
					RequestManager.getInstance().addDownloadedURLRequest(request, f.getAbsolutePath());
				}
			}
			
			running = false;
			if (cancelled)
				event.setType(RetrieveEvent.REQUEST_CANCELLED);
			else
				event.setType(RetrieveEvent.REQUEST_FINISHED);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			event.setType(RetrieveEvent.REQUEST_FAILED);
		} catch (IOException e) {
			e.printStackTrace();
			event.setType(RetrieveEvent.REQUEST_FAILED);
		}
		fireEvent();
	}

	private void fireEvent() {
		Iterator it = listeners.iterator();
		while (it.hasNext()) {
			RetrieveListener listener = (RetrieveListener) it.next();
			listener.transferEventReceived(event);		
		}
	}

	public void addRetrieveListener(RetrieveListener l) {
		if (l!=null)
			listeners.add(l);
	}

	public void cancel() {
		cancelled = true;
		try {
			if (is != null) {
				is.close();
				is = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isRunning() {
		return running && !cancelled;
	}


	public URLRequest getRequest() {
		return request;
	}


	public Vector getListeners() {
		return listeners;
	}


	public long getTaskTimeout() {
		return 30*1000;
	}
}


//static private String makeSuffix(String contentType) {
//	contentType = contentType.toLowerCase();
//    if (contentType.indexOf("png") >= 0)
//        return ".png";
//
//    if (contentType.indexOf("xml") >= 0)
//        return ".xml";
//
//    if (contentType.indexOf("gif") >= 0)
//        return ".gif";
//
//    if (contentType.indexOf("tif") >= 0)
//        return ".tif";
//
//    if (contentType.indexOf("jpg") >= 0
//        || contentType.indexOf("jpeg") >= 0)
//        return ".jpg";
//
//    if (contentType.indexOf("html") >= 0)
//        return ".html";
//
//    return "";
//}
///**
// * 
// */
//private void end() {
//}
//
///**
// * 
// */
//private void inThreadEnd() {
//}
//
///**
// * 
// */
//private void update() {
//}
//
///**
// * 
// */
//private void begin() {
//}

//public void start() {
//// this.reset();
//this.thread = new Thread(this);
//this.thread.start();
//}

/* (non-Javadoc)
* @see java.lang.Runnable#run()
* /
public void run() {
try {
	URL url = request.getUrl();
	this.status = Status.CONNECTING;
	this.begin();
	
    {
        // Retrieve the contents.
        int numBytesRead = 0;
//        System.out.println("File being retrieved. " + this.getUrl().toString());
        java.net.URLConnection connection = url.openConnection();
        connection.setAllowUserInteraction(true);
        java.io.InputStream incoming = connection.getInputStream();
        this.contentLength = connection.getContentLength();
        this.contentType = connection.getContentType();

        java.io.File destFile = java.io.File.createTempFile("Cq_", makeSuffix(this.contentType));
        destFile.deleteOnExit(); // It's copied later if it's to be persisted.
        this.destination = destFile.getPath();
        java.io.FileOutputStream outgoing = new java.io.FileOutputStream(destFile);

        this.status = Status.RETRIEVING;

        byte[] buffer = new byte[4096];

        try {
            while (!Thread.currentThread().isInterrupted() && numBytesRead >= 0) {
                numBytesRead = incoming.read(buffer);
                if (numBytesRead > 0) {
                    this.bytesRead += numBytesRead;
                    outgoing.write(buffer, 0, numBytesRead);
                    this.update();
                }
            }
        } finally {
            if (incoming != null)
                try {incoming.close();} catch (java.io.IOException e) {}; // TODO: log it maybe
            if (outgoing != null)
                try {outgoing.close();} catch (java.io.IOException e) {}; // TODO: log it maybe
        }
        this.status = Status.POSTPROCESSING;
    }
} catch (Exception e) {
    this.status = Status.ERROR;
    this.error = e;
    e.printStackTrace();
} finally {
    if (Thread.currentThread().isInterrupted())
        this.status = Status.INTERRUPTED;
    this.update();
    this.inThreadEnd();
    if (this.status == Status.POSTPROCESSING)
        this.status = Status.DONE;
    this.end();
}
}*/

//File tempDirectory = new File(tempDirectoryPath);
//if (!tempDirectory.exists())
//tempDirectory.mkdir();
//
//f = new File(tempDirectoryPath+"/"+name+System.currentTimeMillis());
//if (cancelled)
//throw new TaskCancelledException(); 
//addDownloadedURL(url, f.getAbsolutePath());
//try {
//URL url = request.getUrl();
//System.out.println(url);
//this.status = Status.CONNECTING;
//this.begin();
//
//{
//// Retrieve the contents.
//int numBytesRead = 0;
////System.out.println("File being retrieved. " + this.getUrl().toString());
//java.net.URLConnection connection = url.openConnection();
//connection.setAllowUserInteraction(true);
//java.io.InputStream incoming = connection.getInputStream();
//this.contentLength = connection.getContentLength();
//this.contentType = connection.getContentType();
//
//java.io.File destFile = java.io.File.createTempFile("Cq_", makeSuffix(this.contentType));
//System.out.println(destFile.getAbsolutePath());
////destFile.deleteOnExit(); // It's copied later if it's to be persisted.
//this.destination = destFile.getPath();
//java.io.FileOutputStream outgoing = new java.io.FileOutputStream(destFile);
//
//this.status = Status.RETRIEVING;
//
//byte[] buffer = new byte[4096];
//
//try {
//while (!Thread.currentThread().isInterrupted() && numBytesRead >= 0) {
//numBytesRead = incoming.read(buffer);
//if (numBytesRead > 0) {
//this.bytesRead += numBytesRead;
//outgoing.write(buffer, 0, numBytesRead);
//this.update();
//}
//}
//} finally {
//if (incoming != null)
//try {incoming.close();} catch (java.io.IOException e) {}; // TODO: log it maybe
//if (outgoing != null)
//try {outgoing.close();} catch (java.io.IOException e) {}; // TODO: log it maybe
//}
//this.status = Status.POSTPROCESSING;
//}
//} catch (Exception e) {
//this.status = Status.ERROR;
//this.error = e;
//e.printStackTrace();
//} finally {
//if (Thread.currentThread().isInterrupted())
//this.status = Status.INTERRUPTED;
//this.update();
//this.inThreadEnd();
//if (this.status == Status.POSTPROCESSING)
//this.status = Status.DONE;
//this.end();
//}

