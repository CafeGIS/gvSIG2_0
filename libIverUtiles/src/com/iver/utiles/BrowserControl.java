/*
 * Created on 18-oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
package com.iver.utiles;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

 
/**
* <p>A simple, static class to display a URL in the system browser.<br></p>
*
* <p>Under <b>Unix</b> systems, it will open one of the browser set through the
* <b>setBrowserCommand(String)</b>. The argument of this method
* must be a fully qualified command or one of the commands returned
* by <b>getSupportedBrowsers()</b>.<br>By default, the browser is Firefox,
* which is included in the list of supported browsers.<br></p>
*
* <p>Under <b>Windows</b>, this will bring up the default browser under windows,
* usually either Netscape or Microsoft IE.  The default browser is
* determined by the OS so no config is necessary.<br>This has been tested under Windows 95/98/NT/XP.</p>
* <p>Notice that <b>you must include</b> the url type -- either "http://" or  "file://".</p>
*
* <p>Under <b>Mac</b>, the usability of this class is pending of test. So probably it does not work</p>
* Examples:
* BrowserControl.displayURL("http://www.javaworld.com")
* BrowserControl.displayURL("file://c:\\docs\\index.html")
* BrowserContorl.displayURL("file:///user/joe/index.html");
*
* @author jaume dominguez faus - jaume.dominguez@iver.es
*/
public class BrowserControl
{
	private static Logger logger = Logger.getLogger(BrowserControl.class.getName());
	public static final String OPERA = "Opera";
	public static final String NETSCAPE = "Netscape";
	public static final String MOZILLA = "Mozilla";
	public static final String GALEON = "Galeon";
	public static final String EPIPHANY = "Epiphany";
	public static final String FIREFOX = "Firefox";
	public static final String KONQUEROR = "Konqueror";
	private static String browserCommand = FIREFOX;
	private static ArrayList supportedBrowsers;

    /**
     * Display a file in the system browser.  If you want to display a
     * file, you must include the absolute path name.
     *
     * @param url the file's url (the url must start with either "http://" or  "file://").
     */
    public static void displayURL(String url)
    {
        boolean windows = isWindowsPlatform();
        String cmd = null;
        try
        {
        	if (windows)
        	{
        		// cmd = 'rundll32 url.dll,FileProtocolHandler http://...'
        		cmd = WIN_PATH + " " + WIN_FLAG + " " + url;
        		Process p = Runtime.getRuntime().exec(cmd);
        	}
        	else
        	{
        		//
        		if (browserCommand.equals(NETSCAPE) ||
        			browserCommand.equals(FIREFOX) ||
        			browserCommand.equals(OPERA) ||
        			browserCommand.equals(MOZILLA)) {
        			// Under Unix, Netscape has to be running for the "-remote"
        			// command to work.  So, we try sending the command and
        			// check for an exit value.  If the exit command is 0,
        			// it worked, otherwise we need to start the browser.
        			// cmd = 'netscape -remote openURL(http://www.javaworld.com)'
        			cmd = browserCommand.toLowerCase() + " -remote openURL(" + url + ")";
        			Process p = Runtime.getRuntime().exec(cmd);
        			try
        			{
        				// wait for exit code -- if it's 0, command worked,
        				// otherwise we need to start the browser up.
        				int exitCode = p.waitFor();
        				if (exitCode != 0)
        				{
        					// Command failed, start up the browser
        					// cmd = 'netscape http://www.javaworld.com'
        					cmd = browserCommand.toLowerCase() + " "  + url;
        					p = Runtime.getRuntime().exec(cmd);
        				}
        			}
        			catch(InterruptedException x)
        			{
        				System.err.println("Error bringing up browser, cmd='" +
        						cmd + "'");
        				System.err.println("Caught: " + x);
        			}
        		} else if (browserCommand.equals(KONQUEROR)) {
        			cmd = "konqueror "+ url;
        			Runtime.getRuntime().exec(cmd);
        		} else if (browserCommand.equals(EPIPHANY)) {
        			cmd = "epiphany "+ url;
        			Runtime.getRuntime().exec(cmd);
//        		} else if (browserCommand.equals(GALEON)) {
//        			TODO available if command is rigth, check it
//        			cmd = "galeon "+ url;
//        			Runtime.getRuntime().exec(cmd);
        		} else {
        			cmd = cmd.replaceAll("%url|%URL", url);
        			logger.log(Level.FINEST, "Executing system runtime command: " + cmd);

        			Runtime.getRuntime().exec(cmd);
        		}


        	}
        }
        catch(IOException x)
        {
            // couldn't exec browser
            System.err.println("Could not invoke browser, command=" + cmd);
            System.err.println("Caught: " + x);
        }

    }

    /**
     * Try to determine whether this application is running under Windows
     * or some other platform by examing the "os.name" property.
     *
     * @return true if this application is running under a Windows OS
     */
    public static boolean isWindowsPlatform()
    {
        String os = System.getProperty("os.name");
        if ( os != null && os.startsWith(WIN_ID))
            return true;
        else
            return false;
    }


    public static void setBrowserCommand(String browserCommand) {
    	BrowserControl.browserCommand = browserCommand;
    }
    /**
     * Returns a list of supported browsers.
     * @return
     */
    public static ArrayList getSupportedBrowsers() {
    	if (supportedBrowsers==null)
    	{
    		supportedBrowsers = new ArrayList();
    		supportedBrowsers.add(KONQUEROR);
    		supportedBrowsers.add(FIREFOX);
    		supportedBrowsers.add(EPIPHANY);
    		// supportedBrowsers.add(GALEON);
    		supportedBrowsers.add(MOZILLA);
    		supportedBrowsers.add(NETSCAPE);
    		supportedBrowsers.add(OPERA);
    	}
    	return supportedBrowsers;
    }
    // Used to identify the windows platform.
    private static final String WIN_ID = "Windows";
    // The default system browser under windows.
    private static final String WIN_PATH = "rundll32";
    // The flag to display a url.
    private static final String WIN_FLAG = "url.dll,FileProtocolHandler";
    // The default browser under unix.
//    private static final String UNIX_PATH = "netscape";
    // The flag to display a url.
//    private static final String UNIX_FLAG = "-remote openURL";
}
