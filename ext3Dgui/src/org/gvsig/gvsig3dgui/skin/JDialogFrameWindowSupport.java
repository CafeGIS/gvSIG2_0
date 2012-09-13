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

import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Window;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.iver.andami.ui.mdiFrame.MDIFrame;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.core.mdiManager.FrameWindowSupport;
import com.iver.core.mdiManager.WindowInfoSupport;

/**
 * 
 */
public class JDialogFrameWindowSupport extends FrameWindowSupport {

	private Hashtable frameView = new Hashtable();
	private Hashtable viewFrame = new Hashtable();
	private Image icon;
	private JDialogWindowInfoSupport vis;
	private JFrame mainFrame;

	public JDialogFrameWindowSupport(MDIFrame mainFrame) {
		super(mainFrame);
		this.mainFrame = mainFrame;
		icon = mainFrame.getIconImage();
	}

	 public Iterator getWindowIterator(){
	    	return viewFrame.keySet().iterator();
	    }
	    
	    public boolean contains(IWindow v){
	    	return viewFrame.containsKey(v);
	    }

		/**
		 * @param wnd
		 * @return
		 */
		public boolean contains(JDialog wnd) {
			return frameView.contains(wnd);
		}
	    
	    /**
	     * DOCUMENT ME!
	     *
	     * @param p DOCUMENT ME!
	     *
	     * @return DOCUMENT ME!
	     */
	    public JDialog getJDialog(IWindow p) {
	        JDialog dlg = (JDialog) viewFrame.get(p);

	        if (dlg == null) {
	            WindowInfo vi = vis.getWindowInfo(p);
	            JDialog nuevo = new JDialog(mainFrame);

	            nuevo.getContentPane().add((JPanel) p);
	            nuevo.setSize(getWidth(p, vi), getHeight(p, vi) + 30);
	            nuevo.setTitle(vi.getTitle());
	            nuevo.setResizable(vi.isResizable());

	            viewFrame.put(p, nuevo);
	            frameView.put(nuevo, p);

	            nuevo.setModal(vi.isModal());
	            return nuevo;
	        } else {
	            return dlg;
	        }
	    }
	    
	    /**
	     * DOCUMENT ME!
	     *
	     * @param p DOCUMENT ME!
	     *
	     * @return DOCUMENT ME!
	     */
	    public JDialog getFrame(IWindow p) {
	    	JDialog frame = (JDialog) viewFrame.get(p);

	        if (frame == null) {
	        	JDialog nuevo = createFrame(p);
	        	viewFrame.put(p, nuevo);
	        	frameView.put(nuevo, p);
	        	vis.getWindowInfo(p); // to update the windowInfo object
	        	return nuevo;
	        } else {
	            return frame;
	        }
	    }
	    
	    public JDialog createFrame(IWindow p)
	    {
	        WindowInfo windowInfo = vis.getWindowInfo(p);
	        JDialog frame = new JDialog(mainFrame, false);
	        frame.getContentPane().add((JPanel) p);
	        frame.setTitle(windowInfo.getTitle());
	        frame.setBounds(calculateBounds(p, windowInfo));
	        frame.setVisible(windowInfo.isVisible());
	        frame.setResizable(windowInfo.isResizable());
	        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	        return frame;
	    }
	    
	    public IWindow getWindow(Component dlg){
	    	return (IWindow) frameView.get(dlg);
	    }
	    
	    public Component getComponent(IWindow window) {
	    	return (Component) viewFrame.get(window);
	    }
	    
	    public void closeWindow(IWindow v){
	    	Object c = viewFrame.remove(v);
	    	frameView.remove(c);
	    }
	    
	    /**
	     * DOCUMENT ME!
	     *
	     * @param v DOCUMENT ME!
	     * @param x DOCUMENT ME!
	     */
	    public void setX(IWindow w, int x) {
	    	Window frame = (Window) viewFrame.get(w);
	    	frame.setLocation(x, frame.getY());
	    }

	    /**
	     * DOCUMENT ME!
	     *
	     * @param v DOCUMENT ME!
	     * @param y DOCUMENT ME!
	     */
	    public void setY(IWindow w, int y) {
	    	Window frame = (Window) viewFrame.get(w);
	    	frame.setLocation(frame.getX(), y);
	    }

	    /**
	     * DOCUMENT ME!
	     *
	     * @param v DOCUMENT ME!
	     * @param height DOCUMENT ME!
	     */
	    public void setHeight(IWindow w, int height) {
	    	Window frame = (Window) viewFrame.get(w);
	    	frame.setSize(frame.getWidth(), height);
	    }

	    /**
	     * DOCUMENT ME!
	     *
	     * @param v DOCUMENT ME!
	     * @param width DOCUMENT ME!
	     */
	    public void setWidth(IWindow w, int width) {
	    	Window frame = (Window) viewFrame.get(w);
	    	frame.setSize(width, frame.getHeight());
	    }

	    /**
	     * DOCUMENT ME!
	     *
	     * @param v DOCUMENT ME!
	     * @param title DOCUMENT ME!
	     */
	    public void setTitle(IWindow w, String title) {
	    	Window frame = (Window) viewFrame.get(w);
	    	if (frame instanceof JFrame) {
	    		((JFrame)frame).setTitle(title);
	    	}
	    	else if (frame instanceof JDialog) {
	    		((JDialog)frame).setTitle(title);
	    	}
	    }

	    /**
	     * DOCUMENT ME!
	     *
	     * @param vis The vis to set.
	     */
	    public void setVis(JDialogWindowInfoSupport vis) {
	        this.vis = vis;
	    }
	    
	    /**
	     * DOCUMENT ME!
	     *
	     * @param v DOCUMENT ME!
	     *
	     * @return DOCUMENT ME!
	     */
	    private int getWidth(IWindow v, WindowInfo wi) {
	        if (wi.getWidth() == -1) {
	            JPanel p = (JPanel) v;

	            return p.getSize().width;
	        } else {
	            return wi.getWidth();
	        }
	    }

	    /**
	     * DOCUMENT ME!
	     *
	     * @param v DOCUMENT ME!
	     *
	     * @return DOCUMENT ME!
	     */
	    private int getHeight(IWindow v, WindowInfo wi) {
	        if (wi.getHeight() == -1) {
	            JPanel p = (JPanel) v;

	            return p.getSize().height;
	        } else {
	            return wi.getHeight();
	        }
	    }

	    private Rectangle calculateBounds(IWindow win, WindowInfo wi) {
	    	Rectangle bounds = new Rectangle();
	        if (wi.getWidth() == -1) {
	            JPanel p = (JPanel) win;

	            bounds.width = p.getSize().width;
	        } else {
	            bounds.width = wi.getWidth();
	        }
	        
	        if (wi.getHeight() == -1) {
	            JPanel p = (JPanel) win;

	            bounds.height = p.getSize().height;
	        } else {
	            bounds.height = wi.getHeight();
	        }
	    	
	    	Rectangle frame = mainFrame.getBounds();
	        if (wi.getX() == -1) {
	            bounds.x = frame.x + (int)(Math.random()*(frame.width-bounds.width));
	            if (bounds.x<0)
	            	bounds.x = 0;
	        } else {
	        	bounds.x = wi.getX();
	        }
	        if (wi.getY() == -1) {
	        	// 135: because we don't want it to be placed at the upmost part of the window
	        	bounds.y = frame.y + 135 + (int)(Math.random()*(frame.height-bounds.height-135));
	        	if (bounds.y<0)
	        		bounds.y = 135;
	        } else {
	            bounds.y = wi.getY();
	        }
	        return bounds;
	    }
	    
	    private void updateWindowInfo(Container container, WindowInfo windowInfo) {
			windowInfo.setWidth(container.getWidth());
			windowInfo.setHeight(container.getHeight());
			windowInfo.setX(container.getX());
			windowInfo.setY(container.getY());
			windowInfo.setClosed(!container.isShowing());
			if (container instanceof JFrame) {
				JFrame frame = (JFrame) container;
				if ((frame.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
					windowInfo.setMaximized(true);
				}
				else {
					windowInfo.setMaximized(false);
					windowInfo.setNormalBounds(frame.getBounds());
				}
			}
	    }
	   
	    /**
	     * Updates the windowInfo object, so that it correctly reflects the
	     * current frame properties.
	     * 
	     * @param win
	     * @param windowInfo The WindowInfo object to update
	     */
	    public void updateWindowInfo(IWindow win, WindowInfo windowInfo) {
	    	Object o = viewFrame.get(win);
	    	if (windowInfo!=null && o!=null) {
	    		if (o instanceof Container) {
	    			updateWindowInfo((Container) o, windowInfo);
	    		}
	    	}
	    }
}
