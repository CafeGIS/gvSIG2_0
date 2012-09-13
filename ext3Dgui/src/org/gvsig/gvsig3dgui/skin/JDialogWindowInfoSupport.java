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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;
import java.util.Hashtable;

import com.iver.andami.plugins.PluginClassLoader;
import com.iver.andami.ui.mdiFrame.MainFrame;
import com.iver.andami.ui.mdiFrame.NoSuchMenuException;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.SingletonWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.core.mdiManager.FrameWindowSupport;
import com.iver.core.mdiManager.SingletonWindowSupport;
import com.iver.core.mdiManager.WindowInfoSupport;


/**
 *
 */
public class JDialogWindowInfoSupport extends WindowInfoSupport {
	private static int serialId = 0;
	
	/**
	 * Support class which associates Frames and Windows
	 */
	private JDialogFrameWindowSupport fws;

	// Correspondencias entre las ventanas y su informacion
	/**
	 * key: IWindow, value: WindowInfo
	 */
	private Hashtable viewInfo = new Hashtable();
	/**
	 * key: WindowInfo, value: IWindow
	 */
	private Hashtable infoView = new Hashtable();
	private WindowPropertyChangeListener windowInfoListener = new WindowPropertyChangeListener();
	private JDialogSingletonWindowSupport svs;
	private MainFrame mdiFrame;

	/**
	 * Creates a new ViewInfoSupport object.
	 *
	 * @param frame DOCUMENT ME!
	 * @param fvs DOCUMENT ME!
	 * @param svs
	 */
	public JDialogWindowInfoSupport(MainFrame frame, FrameWindowSupport fvs,
		SingletonWindowSupport svs) {
		super(frame,fvs,svs);
		this.fws = (JDialogFrameWindowSupport) fvs;
		this.svs = (JDialogSingletonWindowSupport) svs;
		this.mdiFrame = frame;
	}

	/**
	 * Devuelve la vista cuyo identificador es el parametro
	 *
	 * @param id Identificador de la vista que se quiere obtener
	 *
	 * @return La vista o null si no hay ninguna vista con ese identificador
	 */
	public IWindow getWindowById(int id) {
		Enumeration en = infoView.keys();

		while (en.hasMoreElements()) {
			WindowInfo vi = (WindowInfo) en.nextElement();

			if (vi.getId() == id) {
				return (IWindow) infoView.get(vi);
			}
		}

		return null;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param w DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public synchronized WindowInfo getWindowInfo(IWindow w) {
		WindowInfo wi = (WindowInfo) viewInfo.get(w);

		if (wi != null) {
			fws.updateWindowInfo(w, wi);
		}
		else {
			wi = w.getWindowInfo();

			//Para el título
			if (wi.getHeight() != -1) {
				wi.setHeight(wi.getHeight() + 40);
			}

			wi.addPropertyChangeListener(windowInfoListener);
			viewInfo.put(w, wi);
			infoView.put(wi, w);
			wi.setId(serialId++);
		}

		return wi;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param p DOCUMENT ME!
	 */
	public void deleteWindowInfo(IWindow p) {
		WindowInfo vi = (WindowInfo) viewInfo.remove(p);
		infoView.remove(vi);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @author $author$
	 * @version $Revision: 1.1 $
	 */
	public class WindowPropertyChangeListener implements PropertyChangeListener {
		/**
		 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
		 */
		public void propertyChange(PropertyChangeEvent evt) {
			WindowInfo v = (WindowInfo) evt.getSource();
			IWindow view = (IWindow) infoView.get(v);

			if (view instanceof SingletonWindow) {
				SingletonWindow sv = (SingletonWindow) view;

				if (evt.getPropertyName().equals("x")) {
					svs.setX(sv, ((Integer) evt.getNewValue()).intValue());
				} else if (evt.getPropertyName().equals("y")) {
					svs.setY(sv, ((Integer) evt.getNewValue()).intValue());
				} else if (evt.getPropertyName().equals("height")) {
					svs.setHeight(sv, ((Integer) evt.getNewValue()).intValue());
				} else if (evt.getPropertyName().equals("width")) {
					svs.setWidth(sv, ((Integer) evt.getNewValue()).intValue());
				} else if (evt.getPropertyName().equals("maximized")) {
					svs.setMaximized(sv, ((Boolean) evt.getNewValue()).booleanValue());
				} else if (evt.getPropertyName().equals("normalBounds")) {
					// we don't do anything at the moment here
				//	svs.setNormalBounds(sv, (Rectangle) evt.getNewValue());
				} else if (evt.getPropertyName().equals("title")) {
					svs.setTitle(sv, (String) evt.getNewValue());

					try {
						mdiFrame.changeMenuName(new String[] {
								"ventana", (String) evt.getOldValue()
							}, (String) evt.getNewValue(),
							(PluginClassLoader) getClass().getClassLoader());
					} catch (NoSuchMenuException e) {
						/*
						 * No se hace nada porque puede modificarse el título de
						 * una ventana antes de ser añadida a Andami
						 */
					}
				}
			} else {
				if (evt.getPropertyName().equals("x")) {
					fws.setX(view, ((Integer) evt.getNewValue()).intValue());
				} else if (evt.getPropertyName().equals("y")) {
					fws.setY(view, ((Integer) evt.getNewValue()).intValue());
				} else if (evt.getPropertyName().equals("height")) {
					fws.setHeight(view, ((Integer) evt.getNewValue()).intValue());
				} else if (evt.getPropertyName().equals("width")) {
					fws.setWidth(view, ((Integer) evt.getNewValue()).intValue());
				} else if (evt.getPropertyName().equals("title")) {
					fws.setTitle(view, (String) evt.getNewValue());
					try{
						mdiFrame.changeMenuName(new String[] {
								"ventana", (String) evt.getOldValue()
							}, (String) evt.getNewValue(),
							(PluginClassLoader) getClass().getClassLoader());
					} catch (NoSuchMenuException e) {
						/*
						 * No se hace nada porque puede modificarse el título de
						 * una ventana antes de ser añadida a Andami
						 */
					}
				}
			}
		}
	}
}
