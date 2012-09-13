package org.gvsig.scripting;

import java.awt.Container;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JDialog;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;

import thinlet.script.ScriptableThinlet;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;

public class DefaultThinlet extends ScriptableThinlet {

	public DefaultThinlet() throws BSFException {
		super();
	}

	public DefaultThinlet(String scriptLanguage) throws BSFException {
		super(scriptLanguage);
	}
	
	public static void setBSFManager(BSFManager bsfManager) {
		fbsfManager = bsfManager;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8042912379044685040L;

    public Object parse(File file) throws IOException {
        return parse(file, this);
    }


    public Object parse(String path, Object handler) throws IOException {
        ffilename = path;
        InputStream inputstream = null;
        try {
            inputstream = getClass().getResourceAsStream(path);
            if (inputstream == null) {
            	
                try {
                    inputstream = new URL(path).openStream();
                } catch (MalformedURLException mfe) {
                	try {
	                	File file = new File(path);
	                	inputstream = new FileInputStream(file);
                	} catch (Exception e) {
                		/* thows nullpointerexception*/
                	}                	 
                }
                
            }
            fin = new LineNumberReader(new BufferedReader(new InputStreamReader(inputstream)));
            fin.setLineNumber(1);
            Object ret = parse(fin, true, false, handler);
            return ret;
        } finally{ 
            fin = null;
            if(inputstream != null) try{inputstream.close();}catch(IOException ignore){}
        }
    }

    public Object parse(File file, Object handler) throws IOException {
        ffilename = file.getAbsolutePath();
        InputStream inputstream = null;

        try {
	        try {	        	
	        	inputstream = new FileInputStream(file);
	        } catch (Exception e) {
	        	/* thows nullpointerexception*/
	        }                	 
	        fin = new LineNumberReader(new BufferedReader(new InputStreamReader(inputstream)));
	        fin.setLineNumber(1);
	        Object ret = parse(fin, true, false, handler);
	        return ret;
        } finally{ 
            fin = null;
            if(inputstream != null) try{inputstream.close();}catch(IOException ignore){}
        }
    }
	
	public void closeWindow() {
        if (PluginServices.getMainFrame() == null) {
        	Container dialog = this.getParent();
        	int i=0;
        	while (i < 150) {
        		if (dialog instanceof JDialog) {
        			((JDialog)dialog).dispose();
        			break;
        		}
				dialog = dialog.getParent();
				i++;
			}            
        } else {
            PluginServices.getMDIManager().closeWindow((IWindow)this.getParent());
        }
	}
		
	
}
