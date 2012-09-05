/**
 * 
 */
package org.gvsig.i18n;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * <p>This class offers a class loader which is able to load files from the specified
 * directories.</p>
 * 
 * @author César Martínez Izquierdo (cesar.martinez@iver.es)
 *
 */
public class MessagesClassLoader extends URLClassLoader {
	
	/**
	 * <p>Creates a new class loader, which is able to load files from the directories
	 * specified as parameter.</p>
	 * 
	 * @param urls The list of directories which will be used to load files.
	 */
	public MessagesClassLoader(URL[] urls) {
		super(urls);
	}
	
	/**
	 * Loads a resource using the specified file name.
	 * 
	 * @param res The name of the resource to be loaded.
	 */
	public URL getResource(String res) {
		try {
			ArrayList resource = new ArrayList();
			StringTokenizer st = new StringTokenizer(res, "\\/");
			
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				resource.add(token);
			}
			URL ret = null;
			int currentUrl;
			URL[] urls = getURLs();
		
			for (currentUrl=0; currentUrl< urls.length; currentUrl++) {
				URL url = urls[currentUrl];
				File file = new File(url.getFile());
				if (url.getFile().endsWith("/"))
					ret = getResource(file, resource);
			}
			
			if (ret != null) {
				return ret;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return super.getResource(res);
	}
	
	/**
	 * Busca recursivamente el recurso res en el directorio base. res es una
	 * lista de String's con los directorios del path y base es el directorio
	 * a partir del cual se busca dicho recurso. En cada ejecución del método
	 * se toma el primer elemento de res y se busca dicho directorio en el
	 * directorio base. Si se encuentra, será el directorio base para una
	 * nueva llamada.
	 *
	 * @param base Directorio desde donde parte la búsqueda del recurso.
	 * @param res Lista de strings con el path del recurso que se quiere
	 *        encontrar
	 *
	 * @return URL con el recurso
	 */
	private URL getResource(File base, List res) {
		File[] files = base.listFiles();
		
		String parte = (String) res.get(0);
		
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().compareTo(parte) == 0) {
				if (res.size() == 1) {
					try {
						return new URL("file:" + files[i].toString());
					} catch (MalformedURLException e) {
						return null;
					}
				} else {
					return getResource(files[i], res.subList(1, res.size()));
				}
			}
		}
		
		return null;
	}
}


