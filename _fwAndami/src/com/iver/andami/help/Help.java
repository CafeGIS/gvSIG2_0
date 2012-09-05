package com.iver.andami.help;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import javax.help.HelpSet;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Help  {

	private static Help help = null;

	private HelpSet mainHS = null;
	private ArrayList resources = new ArrayList();
	private ArrayList helps = new ArrayList();

	private HelpPanel window=null;

	public static Help getHelp() {
		if( help == null ) {
			help = new Help();
		}
		return help;
	}

	private Logger log() {
		return LoggerFactory.getLogger("org.gvsig");
	}

	public void show() {
		log().info("show()");
		show(null);
	}

	public void show(String id) {
		try {
			log().info("show(id) id="+id);
			initHelp();
			try {
				window.showWindow(id);
			}catch(NullPointerException e) {
				window = new AndamiHelpPanel(mainHS, id);
				window.showWindow();
			}
		} catch (Exception ex) {
			log().error("Se ha producido un error mostrando la ventana de ayuda.",ex);
		}
	}

	public void enableHelp(JComponent comp, String id)
	{
		try{
			initHelp();
			comp.registerKeyboardAction(new DisplayHelpFromFocus(id), KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		}
		catch(Exception except){
			log().error("ID: "+id+" erronea");
		}
	}

	public boolean addResource(String path) {
		URL url = null;
		try {
			url =  (new File(path)).toURL();
		} catch (MalformedURLException e) {
			return false;
		}
		resources.add(url);
		return true;
	}


	public boolean addHelp(String helpName) {
		helps.add(helpName);
		return true;
	}

	private void initHelp()
	{
		if ( mainHS != null ) {
			return;
		}

		//
		// Obtenemos un array de URLs de array list de URLs
		//
		URL urls[] = new URL[resources.size()];
		int i=0;
	    for( Iterator it=resources.iterator(); it.hasNext() ;) {
	    	urls[i++] = (URL) it.next();
	    }

	    //
	    // Creamos la lista de helpsets que han de componer
	    // la ayuda
	    //
	    ArrayList helpSets = new ArrayList();

	    for( Iterator it=helps.iterator(); it.hasNext() ;) {
	    	String name = (String) it.next();
	    	String lang = Locale.getDefault().getLanguage();
	    	HelpSet hs = createHelpSet(urls, name + "/" + lang + "/help.hs");
	    	if( hs == null ) {
	    		lang = "en";
	    		hs = createHelpSet(urls, name + "/" + lang + "/help.hs");
	    	}
	    	if( hs != null ) {
	    		helpSets.add(hs);
	    	}
	    }

	    //
	    // Fijamos el primer helpset de la lista como master y
	    // le añadimos (merge) a este los demas helpsets
	    //
	    Iterator it=helpSets.iterator();
	    if( ! it.hasNext() ) {
	    	return;
	    }
	    mainHS = (HelpSet) it.next();
	    while( it.hasNext() ) {
			try {
		    	mainHS.add( (HelpSet) it.next() );
			} catch (Exception ex) {
				log().error(ex.toString());
			}
	    }
	}

	private HelpSet createHelpSet(URL resources[], String hsName) {
		HelpSet hs = null;

		ClassLoader loader = ClassLoader.getSystemClassLoader();
	    loader = new URLClassLoader(resources, loader);
	    URL url = HelpSet.findHelpSet(loader, hsName);
		if (url == null) {
			log().error(
					MessageFormat.format(
							"No se ha encontrado el helpset ''{0}''.",
							new Object[] { hsName }
					)
			);
			return null;
		}
		log().info("createHelpSet:  url="+url);

		try {
			hs = new HelpSet(null, url);
		} catch (Exception ex) {
			log().error(
				MessageFormat.format(
					"No se ha podido cargar el helpset desde ''{0}''.",
					new Object[] { url }
				),
			    ex
			);
			return null;
		}
		return hs;
	}


    public class DisplayHelpFromFocus implements ActionListener {

	private String id = null;

        public DisplayHelpFromFocus(String id) {
        	log().info("DisplayHelpFromFocus(id) id="+id);
            this.id=id;
        }

		public void actionPerformed(ActionEvent e) {
			Help.getHelp().show(this.id);
	    }
    }
}

