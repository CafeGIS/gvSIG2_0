/*
 * Created on 01-oct-2005
 */
package org.gvsig.remoteClient.taskplanning.retrieving;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.TreeMap;

import org.gvsig.remoteClient.taskplanning.IQueue;

/**
 * pa administrar les tasques (la història aquella
 *  de que hi haja una cola per a cada servidor)
 * @author Luis W. Sevilla (sevilla_lui@gva.es)
 */
public class RequestManager {
	private boolean debug = true;
    
	private static RequestManager instance;
	private TreeMap serversTable = new TreeMap();
	private RequestManager() {} // Avoid public instantiation
	 
	public static RequestManager getInstance() {
		if (instance == null)
			instance = new RequestManager();
		return instance;
	}
	
	
	public URLRetrieveTask addURLRequest(URLRequest request, RetrieveListener listener) {
		try {
			
			// TODO canviar per a quetorne el Request antic, que la request guarde el
			// seu estat així com la llista de listeners
			File f = getPreviousDownloadedURLRequest(request);
			
			if (f!=null) {
				// The file was already requested and it is in the cache or
				// the download is in process
				
				// Overwrite the file name with the file in the cache's one.
				request.setFileName(f.getAbsolutePath());
				System.out.println(request.getUrl()+" is cached at '"+f.getAbsolutePath()+"'");
				

				// get this server's task queue
				RetrieveQueue serverQueue = (RetrieveQueue) getQueue(request.getHost());
				
				// Look up the previous cached jobs
				
				URLRetrieveTask workingTask = serverQueue.getURLPreviousRequest(request);
				if (workingTask == null) {
					// Task already done. Notify listener
					if (debug)
						System.err.println("done job found: "+request.getUrl());
					RetrieveEvent event = new RetrieveEvent();
					event.setType(RetrieveEvent.REQUEST_FINISHED);
					listener.transferEventReceived(event);
					
				} else {
					// The task is working yet, will register the listener
					
					// TODO no va bé... perquè la cola va buidant-se molt ràpidament
					// per a fer que vaja també hi hauria que registrar en cache
					// lo que s'està baixant al principi de l'execute();
					if (debug)
						System.err.println("working job found: "+request.getUrl());
					workingTask.addRetrieveListener(listener);
					
				}
			} else {
				// Pick an unrepeatable fileName
				String host = request.getHost();
				String fileName = request.getFileName();
				File tempDir = new File(tempDirectoryPath);
				if (!tempDir.exists())
					tempDir.mkdir();
				String fileNamePrefix = tempDirectoryPath + 
											File.separator + host + 
											"-" ;
				if (fileName.startsWith(fileNamePrefix))
					fileName = fileName.substring(fileNamePrefix.length(), fileName.length());
				
				// get this server's task queue
				RetrieveQueue serverQueue = (RetrieveQueue) getQueue(request.getHost());
				
				
				fileName = fileNamePrefix + fileName;
				
				request.setFileName(fileName);
				
				// TODO
				// jo ací comprovaria quin protocol, mètode, host, etc... i crearia un
				// objecte que tractara la descàrrega segons el mètode que li toca.
				// algo en plan Strategy's
				//
				// per exemple si fem URLRetrieveTask una abstracta i tenim
				// 
				// GET de tota la vida
				// serverQueue.put(new HTTPGetRetrieveTask(request, listener));
				//
				// POST
				// serverQueue.put(new HTTPPostRetrieveTask(request, listener));
				//
				// FTP
				// serverQueue.put(new FTPRetrieveTask(request, listener));
				//
				// ????Xarxa local?????
				// serverQueue.put(new SMBRetrieveTask(request, listener));
				
				// Enqueue the request and the listener will be notified when done.
				URLRetrieveTask task = new URLRetrieveTask(request, listener);
				return (URLRetrieveTask) serverQueue.put(task);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private IQueue getQueue(String hostName) {
		RetrieveQueue queue = null;
		if (serversTable.containsKey(hostName))
			queue = (RetrieveQueue) serversTable.get(hostName);
		else {
			// crea la cola
			queue = new RetrieveQueue(hostName);
			// pone la cola del server en marcha.
		}
		return queue;
	}


	private Hashtable downloadedFiles;
	private final String tempDirectoryPath = System.getProperty("java.io.tmpdir")+"tmp-andami";
	/**
     * Remove an URL from the system cache. The file will remain in the file
     * system for further eventual uses.
     * @param request
     */
	public void removeURLRequest(URLRequest request) {
		if (downloadedFiles != null && downloadedFiles.containsKey(request))
			downloadedFiles.remove(request);
	}
	/**
     * Adds an URL to the table of downloaded files for further uses. If the URL
     * already exists in the table its filePath value is updated to the new one and
     * the old file itself is removed from the file system.
     * 
     * @param url
     * @param filePath
     */
    protected void addDownloadedURLRequest(URLRequest request, String filePath){
        if (downloadedFiles==null)
            downloadedFiles = new Hashtable();
        String fileName = (String) downloadedFiles.put(request, filePath);
        if (fileName!=null){
            File f = new File(fileName);
            if (f.exists())
                f.delete();
        }
    }
    /**
     * Returns the content of this URL as a file from the file system.<br>
     * <p>
     * If the URL has been already downloaded in this session and notified 
     * to the system using the static <b>Utilities.addDownloadedURL(URL)</b>
     * method, it can be restored faster from the file system avoiding to
     * download it again.
     * </p>
     * @param url
     * @return File containing this URL's content or null if no file was found.
     */
    private File getPreviousDownloadedURLRequest(URLRequest request){
        File f = null;
        if (downloadedFiles!=null && downloadedFiles.containsKey(request)){
            String filePath = (String) downloadedFiles.get(request);
            f = new File(filePath);
        }
        return f;
    }
    
}
