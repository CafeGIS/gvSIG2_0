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
package org.gvsig.gvsig3dgui.skin;

import java.awt.Window;
import java.util.ArrayList;

import javax.swing.JDialog;

import com.iver.andami.ui.mdiManager.SingletonDialogAlreadyShownException;
import com.iver.andami.ui.mdiManager.SingletonWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.core.mdiManager.SingletonWindowSupport;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class JDialogSingletonWindowSupport extends SingletonWindowSupport {


	private static int singletonViewInfoID = 0;
	/** Hashtable que asocia contenido con vistas */
	private HashMap contentWindowInfo = new HashMap();
	private JDialogWindowInfoSupport vis;
	private JDialogFrameWindowSupport frameWindowSupport;
	private HashMap contentFrame = new HashMap();

	public JDialogSingletonWindowSupport(JDialogWindowInfoSupport wis,
			JDialogFrameWindowSupport fvs) {
		super(wis,fvs);
		this.vis = wis;
		this.frameWindowSupport = (JDialogFrameWindowSupport) fvs;
	}

	

	/**
	 * Devuelve una referencia a la vista si ya está mostrada o null si la
	 * vista no ha sido añadida o ya fue cerrada
	 *
	 * @param windowClass DOCUMENT ME!
	 * @param model DOCUMENT ME!
	 * @param wi DOCUMENT ME!
	 *
	 * @return true si la vista existe ya y false si la vista no existe
	 *
	 * @throws SingletonDialogAlreadyShownException DOCUMENT ME!
	 */
	public boolean registerWindow(Class windowClass, Object model, WindowInfo wi) {
		//Se comprueba si la ventana está siendo mostrada
		SingletonWindowInfo swi = new SingletonWindowInfo(windowClass, model);

		if (contentWindowInfo.containsKey(swi)) {
			if (wi.isModal()) {
				throw new SingletonDialogAlreadyShownException();
			}

			wi.setWindowInfo((WindowInfo)contentWindowInfo.get(swi));

			return true;
		} else {
			//La ventana singleton no estaba mostrada
			//Se asocia el modelo con la vista
			contentWindowInfo.put(swi, wi);
			return false;
		}
	}

	public void openSingletonWindow(SingletonWindow sw, Window frame){
		SingletonWindowInfo swi = new SingletonWindowInfo(sw.getClass(), sw.getWindowModel());
		contentFrame.put(swi, frame);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param sw
	 */
	public void closeWindow(SingletonWindow sw) {
		SingletonWindowInfo swi = new SingletonWindowInfo(sw.getClass(), sw.getWindowModel());
		WindowInfo windowInfo = (WindowInfo) contentWindowInfo.get(swi);
		if (windowInfo!=null) {
			JDialog frame = (JDialog) contentFrame.get(swi);
			windowInfo.setWidth(frame.getWidth());
			windowInfo.setHeight(frame.getHeight());
			windowInfo.setX(frame.getX());
			windowInfo.setY(frame.getY());
			windowInfo.setClosed(true);
		/*	if ((frame.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
				windowInfo.setMaximized(true);
			}
			else {
				windowInfo.setMaximized(false);
				windowInfo.setNormalBounds(frame.getBounds());
			}*/
		}
		contentFrame.remove(swi);
	}

	/**
	 * Representa una vista singleton manteniendo el modelo y la clase de la
	 * vista que lo muestra
	 *
	 * @author Fernando González Cortés
	 */
	public class SingletonWindowInfo {

		public int id;

		/** Clase de la vista */
		public Class clase;

		/** Modelo que representa la vista */
		public Object modelo;

		/**
		 * Creates a new SingletonView object.
		 *
		 * @param clase Clase de la vista
		 * @param modelo Modelo que representa la vista
		 */
		public SingletonWindowInfo(Class clase, Object modelo) {
			this.clase = clase;
			this.modelo = modelo;
			this.id = singletonViewInfoID;
			singletonViewInfoID++;
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object obj) {
			if (obj.getClass() != SingletonWindowInfo.class) {
				throw new IllegalArgumentException();
			}

			SingletonWindowInfo s = (SingletonWindowInfo) obj;

			if ((clase == s.clase) && (modelo == s.modelo)) {
				return true;
			} else {
				return false;
			}
		}
	}

	private JDialog getFrame(SingletonWindowInfo svi){
		WindowInfo vi = (WindowInfo) contentWindowInfo.get(svi);
		return (JDialog) contentFrame.get(svi);
	}

	public JDialog getFrame(Class viewClass, Object model){
		SingletonWindowInfo svi = new SingletonWindowInfo(viewClass, model);
		return getFrame(svi);
	}

	/**
	 * @param model
	 * @return
	 */
	public JDialog[] getFrames(Object model) {
		ArrayList ret = new ArrayList();

		ArrayList keys = contentFrame.getKeys();
		for (int i = 0; i < keys.size(); i++) {
			SingletonWindowInfo svi = (SingletonWindowInfo) keys.get(i);

			if (svi.modelo == model){
				ret.add(contentFrame.get(svi));
			}
		}

		return (JDialog[]) ret.toArray(new JDialog[0]);
	}
	
	/**
	 * @param sv
	 * @param i
	 */
	public void setX(SingletonWindow sw, int x) {
		JDialog frame = (JDialog) contentFrame.get(new SingletonWindowInfo(sw.getClass(), sw.getWindowModel()));

        if (frame == null) return;
        frame.setLocation(x, frame.getY());
	}
	
	
	/**
	 * @param sv
	 * @param i
	 */
	public void setY(SingletonWindow sw, int y) {
		JDialog frame = (JDialog) contentFrame.get(new SingletonWindowInfo(sw.getClass(), sw.getWindowModel()));

        if (frame == null) return;

        frame.setLocation(frame.getX(), y);
	}

	/**
	 * @param sv
	 * @param i
	 */
	public void setHeight(SingletonWindow sw, int height) {
		JDialog frame = (JDialog) contentFrame.get(new SingletonWindowInfo(sw.getClass(), sw.getWindowModel()));

        if (frame == null) return;

        frame.setSize(frame.getWidth(), height);
	}

	/**
	 * @param sv
	 * @param i
	 */
	public void setWidth(SingletonWindow sw, int width) {
		JDialog frame = (JDialog) contentFrame.get(new SingletonWindowInfo(sw.getClass(), sw.getWindowModel()));

        if (frame == null) return;
        frame.setSize(width, frame.getHeight());
	}

	/**
	 * @param sw
	 * @param maximized
	 */
	public void setMaximized(SingletonWindow sw, boolean maximized) {
		JDialog frame = (JDialog) contentFrame.get(new SingletonWindowInfo(sw.getClass(), sw.getWindowModel()));

     /*   if (frame == null) return;
     
        if (maximized) {
        	frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        else {
        	frame.setExtendedState(JFrame.NORMAL);
        } */
	}


	/**
	 * @param sv
	 * @param string
	 */
	public void setTitle(SingletonWindow sw, String title) {
		JDialog frame = (JDialog) contentFrame.get(new SingletonWindowInfo(sw.getClass(), sw.getWindowModel()));

        if (frame == null) return;
        frame.setTitle(title);
	}

	private class HashMap {
	    private ArrayList keys = new ArrayList();
	    private ArrayList values = new ArrayList();

	    public void put(SingletonWindowInfo key, Object value) {
	        int index = -1;
	        for (int i = 0; i < keys.size(); i++) {
	            if (keys.get(i).equals(key)){
	                index = i;
	                break;
	            }
            }

	        if (index != -1){
	            keys.add(index, key);
	            values.add(index, value);
	        }else{
	            keys.add(key);
	            values.add(value);
	        }
	    }

	    public boolean containsKey(SingletonWindowInfo key){
	        for (int i = 0; i < keys.size(); i++) {
	            if (keys.get(i).equals(key)){
	                return true;
	            }
	        }

	        return false;
	    }

	    public Object get(SingletonWindowInfo key){
	        for (int i = 0; i < keys.size(); i++) {
	            if (keys.get(i).equals(key)){
	                return values.get(i);
	            }
	        }

	        return null;
	    }

	    public void remove(SingletonWindowInfo key){
	        for (int i = 0; i < keys.size(); i++) {
	            if (keys.get(i).equals(key)){
	                keys.remove(i);
	                values.remove(i);
	            }
	        }
	    }

	    public ArrayList getKeys(){
	        return keys;
	    }
	}
}
