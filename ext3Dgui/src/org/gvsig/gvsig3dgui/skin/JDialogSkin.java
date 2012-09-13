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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.swing.DefaultDesktopManager;
import javax.swing.DesktopManager;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.KeyStroke;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiFrame.GlassPane;
import com.iver.andami.ui.mdiFrame.MDIFrame;
import com.iver.andami.ui.mdiFrame.NewStatusBar;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.MDIUtilities;
import com.iver.andami.ui.mdiManager.SingletonDialogAlreadyShownException;
import com.iver.andami.ui.mdiManager.SingletonWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.andami.ui.theme.Theme;
import com.iver.core.mdiManager.NewSkin;

/**
 *
 */
public class JDialogSkin extends NewSkin{
	private static final int DefaultXMargin = 20; // Added for the method 'centreJInternalFrame'
	private static final int DefaultYMargin = 20; // Added for the method 'centreJInternalFrame'
	private static final int MinimumXMargin = 130; // Added for the method 'centreJInternalFrame'
	private static final int MinimumYMargin = 60; // Added for the method 'centreJInternalFrame'


    /**
     * Variable privada <code>desktopManager</code> para usarlo cuando sale
     * una ventana que no queremos que nos restaure las que tenemos maximizadas.
     * Justo antes de usar el setMaximize(false), le pegamos el cambiazo.
     */
    private static DesktopManager desktopManager = new DefaultDesktopManager();

    /** log */
    private static Logger logger = Logger.getLogger(JDialogSkin.class.getName());

    /** Panel de la MDIFrame */
    private MyDesktopPane panel = new MyDesktopPane();

    /** MDIFrame */
    private MDIFrame mainFrame;

    private GlassPane glassPane = new GlassPane();

    private JDialogStackSupport dss;

    /**
     * Associates JInternalFrames with the IWindow they contain
     */
    private JDialogFrameWindowSupport fws;

    private JDialogWindowInfoSupport wis;

    private JDialogWindowStackSupport wss;

    private JDialogSingletonWindowSupport sws;

    private Cursor lastCursor = null;
	private ImageIcon image;
	private String typeDesktop;

	private IWindow lastActiveWindow = null;

    /**
     * @see com.iver.andami.ui.mdiManager.MDIManager#init(com.iver.andami.ui.mdiFrame.MDIFrame)
     */
    public void init(MDIFrame f) {
        // Inicializa el Frame y la consola
        mainFrame = f;
        mainFrame.setGlassPane(glassPane);
        panel.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

        mainFrame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setDesktopManager(desktopManager);

        fws = new JDialogFrameWindowSupport(mainFrame);
        dss = new JDialogStackSupport(mainFrame);
        sws = new JDialogSingletonWindowSupport(wis,fws);
        wis = new JDialogWindowInfoSupport(mainFrame, fws, sws);
        fws.setVis(wis);
        wss = new JDialogWindowStackSupport(wis);


        // TODO (jaume) esto no debería de estar aquí...
        // molaría más en un diálogo de preferencias
        // es sólo una prueba
        KeyStroke controlTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.CTRL_MASK);

        PluginServices.registerKeyStroke(controlTab, new KeyEventDispatcher() {

			public boolean dispatchKeyEvent(KeyEvent e) {
				IWindow[] views = getAllWindows();
				if (views.length<=0 || e.getID() == KeyEvent.KEY_PRESSED)
					return false;


				int current = 0;
				for (int i = 0; i < views.length; i++) {
					if (views[i].equals(getActiveWindow())) {
						current = i;
						break;
					}
				}
				addWindow(views[(current +1) % views.length]);
				return true;
			}

        });
    }

    /**
     * @see com.iver.andami.ui.mdiManager.MDIManager#addWindow(com.iver.andami.ui.mdiManager.IWindow)
     */
    public IWindow addWindow(IWindow p) throws SingletonDialogAlreadyShownException {
        // se obtiene la información de la vista
        WindowInfo wi = wis.getWindowInfo(p);

        // Se comprueban las incompatibilidades que pudieran haber en la vista
        MDIUtilities.checkWindowInfo(wi);
        if ((p instanceof SingletonWindow) && (wi.isModal())) {
            throw new RuntimeException("A modal view cannot be a SingletonView");
        }

        /*
         * Se obtiene la referencia a la vista anterior por si es una singleton
         * y está siendo mostrada. Se obtiene su información si ya fue mostrada
         */
        boolean singletonPreviouslyAdded = false;

        if (p instanceof SingletonWindow) {
            SingletonWindow sw = (SingletonWindow) p;
            if (sws.registerWindow(sw.getClass(), sw.getWindowModel(), wi)) {
                singletonPreviouslyAdded = true;
            }
        }

        if (singletonPreviouslyAdded) {
            // Si la vista no está actualmente abierta
            if (!sws.contains((SingletonWindow) p)) {
            	JDialog frame = (JDialog)fws.getFrame(p);
                sws.openSingletonWindow((SingletonWindow) p,frame);

                addFrame(frame, wi);
                wss.add(p, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        IWindow v = wis.getWindowById(Integer.parseInt(e
                                .getActionCommand()));
                        JDialog frame = (JDialog)fws.getFrame(v);
                        activateFrame(frame);
                    }
                });
                return p;
            } else {
                // La vista está actualmente abierta
                JDialog frame = (JDialog) sws.getFrame((SingletonWindow) p);
                activateFrame(frame);
                wss.setActive(p);
                return fws.getWindow(frame);
            }
        } else {
            if (wi.isModal()) {
                addJDialog(p);
            } else {
                // Se sitúa la vista en la pila de vistas
                wss.add(p, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        IWindow v = wis.getWindowById(Integer.parseInt(e
                                .getActionCommand()));
                        JDialog f = (JDialog) fws.getFrame(v);
                        activateFrame(f);
                    }
                });
                addFrame(p);
            }

            return p;
        }
    }

    /**
     * Situa una ventana en el centro de la pantalla
     *
     * @param d
     *            Diálogo que se quiere situar
     */
    private void centerWindow(IWindow win) {
    	JDialog dialog = (JDialog) fws.getFrame(win);

        int offSetX = dialog.getWidth() / 2;
        int offSetY = dialog.getHeight() / 2;

        dialog.setLocation((mainFrame.getWidth() / 2) - offSetX,
        		(mainFrame.getHeight() / 2) - offSetY);
    }

    /**
     * Similar method as 'addView' but in this case centres the new JInternalFrame of the View in the contentPane of the MainFrame
     *
     * @see com.iver.core.mdiManager.NewSkin#addWindow(com.iver.andami.ui.mdiManager.IWindow)
     *
     * @author Pablo Piqueras Bartolomé
     */
	public IWindow addCentredWindow(IWindow p) throws SingletonDialogAlreadyShownException {
		IWindow window = addWindow(p);
		centerWindow(window);
		return window;
	}

	/**
	 * Centres the JInternalFrame in the center of the contentPane of the MainFrame
	 * If it can't be showed completely the JInternalFrame, tries that the top-left corner of the JInternalFrame would be showed
	 *
	 * @author Pablo Piqueras Bartolomé
	 *
	 * @param jInternalFrame A reference to the frame to centring
	 */
	private static synchronized void centreJFrame(JFrame frame) {

		// The top-left square of frame reference
		Point newReferencePoint = new Point();

		// A reference to the panel where the JInternalFrame will be displayed
		Container contentPane = ((JFrame)PluginServices.getMainFrame()).getContentPane();

		// Get the NewStatusBar component
		NewStatusBar newStatusBar = ((NewStatusBar)contentPane.getComponent(1));
		JDesktopPane jDesktopPane = ((JDesktopPane)contentPane.getComponent(2));

		int visibleWidth = contentPane.getWidth() - contentPane.getX(); // The last substraction is for if there is any menu,... at left
		int visibleHeight = contentPane.getHeight() - newStatusBar.getHeight() - contentPane.getY() - Math.abs(jDesktopPane.getY() - contentPane.getY()); // The last substraction is for if there is any menu,... at top
		int freeWidth = visibleWidth - frame.getWidth();
		int freeHeight = visibleHeight - frame.getHeight();

		// Calculate the new point reference (Assure that the top-left corner is showed)
		if (freeWidth < 0)
		{
			if (visibleWidth > MinimumXMargin)
				newReferencePoint.x = DefaultXMargin;
			else
				newReferencePoint.x = 0;
		}
		else
			newReferencePoint.x = freeWidth / 2;

		if (freeHeight < 0)
		{
			if (visibleHeight > MinimumYMargin)
				newReferencePoint.y = DefaultYMargin;
			else
				newReferencePoint.y = 0;
		}
		else
			newReferencePoint.y = freeHeight / 2;

		// Set the new location for this JInternalFrame
		frame.setLocation(newReferencePoint);
	}

    /**
     * DOCUMENT ME!
     *
     * @param wnd
     *            DOCUMENT ME!
     * @param wi
     *            DOCUMENT ME!
     */
    private void addFrame(JDialog wnd, WindowInfo wi) {
    	wnd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        FrameListener listener = new FrameListener();
        wnd.addWindowListener(listener);
        wnd.addWindowStateListener(listener);
        wnd.addWindowFocusListener(listener);

        if (wi.isModeless() || wi.isPalette()) {
          //  panel.add(wnd, JDesktopPane.PALETTE_LAYER);
            if (wi.isPalette())
                wnd.setFocusable(false);
        } else {
        //    panel.add(wnd);
        }
        updateFrameProperties(wnd, wi);


        activateFrame(wnd);
        /*
    	if (wi.isMaximized()) {
    		wnd.setExtendedState(JFrame.MAXIMIZED_BOTH);
    	}
    	else {
        	int state = wnd.getExtendedState();
        	state = (state & (~JFrame.MAXIMIZED_BOTH));
        	wnd.setExtendedState(state);
    	}*/
    }


    /**
     * DOCUMENT ME!
     *
     * @param wnd
     *            DOCUMENT ME!
     * @param wi
     *            DOCUMENT ME!
     */
   /* private void addJInternalFrame(JInternalFrame wnd, WindowInfo wi) {
        wnd.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        wnd.add
        wnd.addInternalFrameListener(new FrameListener());

        if (wi.isModeless() || wi.isPalette()) {
            panel.add(wnd, JDesktopPane.PALETTE_LAYER);
            if (wi.isPalette())
                wnd.setFocusable(false);
        } else {
            panel.add(wnd);
        }
      //  updateFrameProperties(wnd, wi);

        activateJInternalFrame(wnd);
        try{
        	wnd.setMaximum(wi.isMaximized());
        }catch(Exception ex){
        	logger.warn("Error: ", ex);
        }
    }*/

    private void updateFrameProperties(JDialog frame, WindowInfo wi) {
    	int height, width;
    	if (wi.isMaximized()) {
    		if (wi.getNormalWidth()!=-1)
    			width = wi.getNormalWidth();
    		else
    			width = frame.getWidth();
    		if (wi.getNormalHeight()!=-1)
    			height = wi.getNormalHeight();
    		else
    			height = frame.getHeight();

    		frame.setSize(width, height);
    		frame.setLocation(wi.getNormalX(), wi.getNormalY());
    	}
    	else {
    		if (wi.getWidth()!=-1)
    			width = wi.getWidth();
    		else
    			width = frame.getWidth();
    		if (wi.getHeight()!=-1)
    			height = wi.getHeight();
    		else
    			height = frame.getHeight();
    		frame.setSize(width, height);
        	frame.setLocation(wi.getX(), wi.getY());
    	}
    	frame.setTitle(wi.getTitle());
    	frame.setVisible(wi.isVisible());
    	frame.setResizable(wi.isResizable());
    	/*if (wi.isMaximized()) {
    		frame.set
    		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    	}
    	else {
        	int state = frame.getExtendedState();
        	state = (state & (~JFrame.MAXIMIZED_BOTH));
        	frame.setExtendedState(state);
    	}*/
    }

    /**
     * DOCUMENT ME!
     *
     * @param p
     */
 /*   private void addJInternalFrame(IWindow p) {
        WindowInfo wi = wis.getWindowInfo(p);

        JInternalFrame wnd = fws.getJInternalFrame(p);

        if (p instanceof SingletonWindow) {
            SingletonWindow sv = (SingletonWindow) p;
            sws.openSingletonWindow(sv, wnd);
        }

        addJInternalFrame(wnd, wi);
    }*/


    /**
     * DOCUMENT ME!
     *
     * @param p
     */
 /*   private void addJFrame(IWindow p) {
        WindowInfo wi = wis.getWindowInfo(p);

        JFrame wnd = fws.getFrame(p);

        if (p instanceof SingletonWindow) {
            SingletonWindow sv = (SingletonWindow) p;
            sws.openSingletonWindow(sv, wnd);
        }

        addJFrame(wnd, wi);
    }*/

    /**
     * DOCUMENT ME!
     *
     * @param wnd
     */
    private void activateJInternalFrame(JInternalFrame wnd) {
        try {
            wnd.moveToFront();
//            logger.debug("Activando " + wnd.getTitle());
            wnd.setSelected(true);
            wnd.setIcon(false);
        } catch (PropertyVetoException e) {
//            logger.error(e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param wnd
     */
    private void activateFrame(JDialog wnd) {
    	wnd.setVisible(true);
    	wnd.toFront();

    	/*int state = wnd.getExtendedState();
    	state = (state & (~JFrame.ICONIFIED));
    	wnd.setExtendedState(state);*/
    }

    /**
     * Situa un diálogo modal en el centro de la pantalla
     *
     * @param d
     *            Diálogo que se quiere situar
     */
    private void centerDialog(JDialog d) {
        int offSetX = d.getWidth() / 2;
        int offSetY = d.getHeight() / 2;

        d.setLocation((mainFrame.getWidth() / 2) - offSetX, (mainFrame
                .getHeight() / 2)
                - offSetY);
    }

    /**
     * DOCUMENT ME!
     *
     * @param p
     */
    private void addJDialog(IWindow p) {
        JDialog dlg = fws.getJDialog(p);

        centerDialog(dlg);

        dlg.addWindowListener(new DialogWindowListener());
        dss.pushDialog(dlg);

        dlg.setVisible(wis.getWindowInfo(p).isVisible());
    }

    /**
     * DOCUMENT ME!
     *
     * @param p
     */
    private void addFrame(IWindow p) {
        WindowInfo wi = wis.getWindowInfo(p);

        JDialog wnd = (JDialog) fws.getFrame(p);
        if (wnd==null)
        	wnd=new JDialog();
        if (p instanceof SingletonWindow) {
            SingletonWindow sv = (SingletonWindow) p;
            sws.openSingletonWindow(sv, wnd);
        }

        addFrame(wnd, wi);
    }

    /**
     * @see com.iver.andami.ui.mdiManager.MDIManager#getActiveWindow()
     */
    public IWindow getActiveWindow() {
    	return wss.getActiveWindow();
        /*JFrame frame = panel.getSelectedFrame();

        if (frame != null) {
            IWindow theWindow = fws.getWindow(frame);
            if (theWindow == null)
                return null;
            if (theWindow.getWindowInfo().isPalette())
                return wss.getActiveWindow();
            else
                return fws.getWindow(frame);
        }
        // return vss.getActiveView();

        return null;*/
    }
    public IWindow getFocusWindow(){
    	return wss.getActiveWindow();
    	/*fws.getFocusWindow()
    	JFrame.get
    	JFrame frame = panel.getSelectedFrame();

         if (frame != null) {
             IWindow theView = fws.getWindow(frame);
             if (theView == null)
                 return null;
             return fws.getWindow(frame);
         }
         return null;*/
    }
    /**
     * @see com.iver.andami.ui.mdiManager.MDIManager#closeWindow(com.iver.andami.ui.mdiManager.IWindow)
     */
    public void closeWindow(IWindow p) {
		// Si es un diálogo modal
		if (p.getWindowInfo().isModal()) {
			closeJDialog();
		} else { // Si no es modal se cierra el JInternalFrame
			closeJFrame((JDialog) fws.getFrame(p));
		}
	}

    /**
	 * @see com.iver.andami.ui.mdiManager.MDIManager#closeAllWindows()
	 */
    public void closeAllWindows() {
        ArrayList eliminar = new ArrayList();
        Iterator i = fws.getWindowIterator();

        while (i.hasNext()) {
            eliminar.add((IWindow) i.next());
        }

        for (Iterator iter = eliminar.iterator(); iter.hasNext();) {
            IWindow vista = (IWindow) iter.next();
            closeWindow(vista);
        }
    }

    /**
     * @see com.iver.andami.ui.mdiManager.MDIManager#getWindowInfo(com.iver.andami.ui.mdiManager.IWindow)
     */
    public WindowInfo getWindowInfo(IWindow w) {
    	WindowInfo wi = wis.getWindowInfo(w);

    /*
     * This is done now in vis.getWindowInfo(w)
     *
     * JInternalFrame f = fws.getJInternalFrame(w);
    	wi.setX(f.getX());
    	wi.setY(f.getY());
    	wi.setHeight(f.getHeight());
    	wi.setWidth(f.getWidth());
    	// isClosed() doesn't work as (I) expected, why? Using isShowing instead
    	wi.setClosed(!f.isShowing());
    	wi.setNormalBounds(f.getNormalBounds());
    	wi.setMaximized(f.isMaximum());*/
    	return wi;
    }

    /**
     * DOCUMENT ME!
     *
     * @param dialog
     * @throws RuntimeException
     *             DOCUMENT ME!
     */
    private void closeJDialog() {
        JDialog dlg = dss.popDialog();

        dlg.setVisible(false);

        IWindow s = (IWindow) fws.getWindow(dlg);

        callWindowClosed(s);

        fws.closeWindow(s);

        // Si es singleton se desasocia el modelo con la vista
        if (s instanceof SingletonWindow) {
            sws.closeWindow((SingletonWindow) s);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param window
     *            DOCUMENT ME!
     */
    private void callWindowClosed(IWindow window) {
        if (window instanceof IWindowListener) {
            ((IWindowListener) window).windowClosed();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param window
     *            DOCUMENT ME!
     */
    private void callWindowActivated(IWindow window) {
//        logger.debug("View " + window.getWindowInfo().getTitle()
//                + " activated (callViewActivated)");
        if (window instanceof IWindowListener) {
            ((IWindowListener) window).windowActivated();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param frame
     */
    private void closeJInternalFrame(JInternalFrame frame) {
        try {
            IWindow s = (IWindow) fws.getWindow(frame);

            frame.setClosed(true);
            callWindowClosed(s);
        } catch (PropertyVetoException e) {
//            logger
//                    .error(
//                            "Not compatible with property veto's. Use ViewInfo instead.",
//                            e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param frame
     */
    private void closeJFrame(JDialog frame) {
    	WindowListener[] listeners = frame.getWindowListeners();
    	for (int i=0; i<listeners.length; i++) {
    		listeners[i].windowClosing(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING	));
    	}
    	IWindow s = (IWindow) fws.getWindow(frame);
    	frame.dispose();
    	callWindowClosed(s);
    }

    /**
     * @see com.iver.andami.plugins.IExtension#initialize()
     */
    public void initialize() {
    }

    /**
     * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
     */
    public void execute(String actionCommand) {
        if (actionCommand.equals("cascada")) {
        } else if (actionCommand.equals("mosaico")) {
        }
    }

    /**
     * @see com.iver.andami.plugins.IExtension#isEnabled()
     */
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see com.iver.andami.plugins.IExtension#isVisible()
     */
    public boolean isVisible() {
        // TODO Auto-generated method stub
        return true;
    }

    /**
     * @see com.iver.andami.ui.mdiManager.MDIManager#setWaitCursor()
     */
    public void setWaitCursor() {
        if (mainFrame != null) {
            glassPane.setVisible(true);
            lastCursor = mainFrame.getCursor();
            dss.setWaitCursor();
            glassPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }
    }

    /**
     * @see com.iver.andami.ui.mdiManager.MDIManager#restoreCursor()
     */
    public void restoreCursor() {
        if (mainFrame != null) {
            glassPane.setVisible(false);
            dss.restoreCursor();
            glassPane.setCursor(lastCursor);
        }
    }

    /**
     * Listener para los eventos de cerrado de los diálogos. Tiene su razón de
     * ser en que los diálogos han de devolverse al pool cuando se cierran
     *
     * @author Fernando González Cortés
     */
    public class DialogWindowListener extends WindowAdapter {
        /**
         * Captura el evento de cerrado de los diálogos con el fin de realizar
         * tareas de mantenimiento
         *
         * @param e
         *            evento
         */
        public void windowClosing(WindowEvent e) {
            closeJDialog();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public class FrameListener extends WindowAdapter {
        /**
         * @see java.awt.event.WindowAdapter#windowActivated(java.awt.event.WindowEvent)
         */
        public void windowActivated(WindowEvent e) {
        	JDialog frame = (JDialog) e.getSource();
            IWindow panel = fws.getWindow(frame);

            if (panel!=null) {

            	WindowInfo wi = wis.getWindowInfo(panel);
            	if (wi.isPalette())
            		return;

            	wss.setActive(panel);

//            	if (lastActiveWindow!=null && !lastActiveWindow.equals(panel)) {
//            		wis.getWindowInfo(lastActiveWindow).setSelectedTools(mainFrame.getSelectedTools());
//            		mainFrame.setSelectedTools(wi.getSelectedTools());
//            	}
            	if (wi.getSelectedTools()==null) {
                	// this is the first time this window is activated
                	wi.setSelectedTools(new HashMap(mainFrame.getInitialSelectedTools()));
                }
            	if (lastActiveWindow!=null && !lastActiveWindow.equals(panel)) {
            		wis.getWindowInfo(lastActiveWindow).setSelectedTools(mainFrame.getSelectedTools());
            		mainFrame.setSelectedTools(wi.getSelectedTools());
            	}
            	mainFrame.enableControls();
            	callWindowActivated(panel);
            }

        }

        /**
         * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
         */
        public void windowClosed(WindowEvent e) {
        }


        /**
         * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
         */
        public void windowClosing(WindowEvent e) {
            // Se elimina la memoria del JInternalFrame si no es ALWAYS_LIVE
            // logger.debug("internalFrameClosing " +
            // e.getInternalFrame().getTitle());

        	JDialog	 c = (JDialog) e.getSource();
        	IWindow win = (IWindow) fws.getWindow(c);
        	if (win!=null) {

	            WindowInfo wi = wis.getWindowInfo(win);

	            callWindowClosed(win);
	            boolean alwaysLive;
	            if (win instanceof SingletonWindow) {
	                sws.closeWindow((SingletonWindow) win);
	            }

	            fws.closeWindow(win);

	            panel.remove(c);

	            wss.remove(win);

	            if (!wi.isPalette())
	                mainFrame.enableControls();
	            panel.repaint();
        	}

            // Para activar el JInternalFrame desde la que hemos
            // abierto la ventana que estamos cerrando
            IWindow lastWindow = wss.getActiveWindow();
            // La activamos
            if (lastWindow != null) {
//            	logger.debug(PluginServices.getText(this, "Devuelvo_el_foco_a_")+lastWindow.getWindowInfo().getTitle());
                JDialog frame = (JDialog) fws.getFrame(lastWindow);
                frame.toFront();
            }
        }

        /**
         * @see java.awt.event.WindowAdapter#windowDeactivated(java.awt.event.WindowEvent)
         */
        public void windowDeactivated(WindowEvent e) {
        	JDialog frame = (JDialog) e.getSource();
            IWindow win = fws.getWindow(frame);
            if (win != null) {
                WindowInfo wi = wis.getWindowInfo(win);
                if (wi.isPalette())
                    return;
                wi.setSelectedTools(mainFrame.getSelectedTools());
                lastActiveWindow = win;
            }
        }

        /**
         * @see java.awt.event.WindowAdapter#windowDeiconified(java.awt.event.WindowEvent)
         */
        public void windowDeiconified(WindowEvent e) {
            mainFrame.enableControls();
        }

        /**
         * @see java.awt.event.WindowAdapter#windowGainedFocus(java.awt.event.WindowEvent)
         */
        public void windowGainedFocus(WindowEvent e) {

        }

        /**
         * @see java.awt.event.WindowAdapter#windowIconified(java.awt.event.WindowEvent)
         */
        public void windowIconified(WindowEvent e) {
            mainFrame.enableControls();
        }

        /**
         * @see java.awt.event.WindowAdapter#windowLostFocus(java.awt.event.WindowEvent)n
         */
        public void windowLostFocus(WindowEvent e) {

        }

        /**
         * @see java.awt.event.WindowAdapter#windowOpened(java.awt.event.WindowEvent)
         */
        public void windowOpened(WindowEvent e) {
        }

        /**
         * @see java.awt.event.WindowAdapter#windowStateChanged(java.awt.event.WindowEvent)
         */
        public void windowStateChanged(WindowEvent e) {

        }
    }

    /**
     * @see com.iver.andami.ui.mdiManager.MDIManager#closeSingletonWindow(java.lang.Class,
     *      java.lang.Object)
     */
    public boolean closeSingletonWindow(Class viewClass, Object model) {
        JDialog frame = (JDialog)sws.getFrame(viewClass, model);
        if (frame == null)
            return false;
        closeJFrame(frame);
        return true;
    }

    /**
     * @see com.iver.andami.ui.mdiManager.MDIManager#closeSingletonWindow(java.lang.Object)
     */
    public boolean closeSingletonWindow(Object model) {
        JDialog[] frames = (JDialog[])sws.getFrames(model);
        if (frames.length == 0)
            return false;
        for (int i = 0; i < frames.length; i++) {
            closeJFrame(frames[i]);
        }
        return true;
    }

    /**
     * @see com.iver.andami.ui.mdiManager.MDIManager#getAllWindows()
     */
    public IWindow[] getAllWindows() {
        ArrayList windows = new ArrayList();
        Iterator i = fws.getWindowIterator();

        while (i.hasNext()) {
            windows.add((IWindow) i.next());
        }
        return (IWindow[]) windows.toArray(new IWindow[0]);
    }

    /**
     * @see com.iver.andami.ui.mdiManager.MDIManager#getOrderedWindows()
     */
    public IWindow[] getOrderedWindows() {
        //ArrayList windows = new ArrayList();
        TreeMap windows = new TreeMap();
        Iterator winIterator = fws.getWindowIterator();

        Component frame;
        IWindow win;
        /**
         * The order of the window in the JDesktopPane. Smaller numbers
         * are closer to the foreground.
         */
        int zPosition = 0;
        while (winIterator.hasNext()) {
        	win = (IWindow) winIterator.next();
        	// unfortunately we can't use this now, and I don't know how to retrieve the zPosition for JDialogs
        	//frame = fws.getComponent(win);
    		//zPosition = panel.getPosition(frame);
    		windows.put(new Integer(zPosition++), win);
        }
        winIterator = windows.values().iterator();
        ArrayList winList = new ArrayList();
        while (winIterator.hasNext()) {
        	winList.add(winIterator.next());
        }

        return (IWindow[]) winList.toArray(new IWindow[0]);
    }

    public void setMaximum(IWindow v, boolean bMaximum) throws PropertyVetoException
    {
        /*JDialog frame = fws.getFrame(v);

    	if (bMaximum) {

	        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    	}
    	else {
    		int state = frame.getExtendedState();
    		state = (state & (~JFrame.MAXIMIZED_BOTH));
    		frame.setExtendedState(state);
    	}*/
    }

    public void changeWindowInfo(IWindow w, WindowInfo wi){
    	JDialog frame = fws.getJDialog(w);
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		int width;
		int height;
		if (wi.getWidth()!=-1)
			width = wi.getWidth();
		else
			width = frame.getWidth();
		if (wi.getHeight()!=-1)
			height = wi.getHeight();
		else
			height = frame.getHeight();
		frame.setSize(new Dimension(width, height));
		frame.setLocation(wi.getX(), wi.getY());
		if (wi.isPalette()) {
			//panel.add(frame, JDesktopPane.PALETTE_LAYER);
			frame.setFocusable(false);
		} else {
			//panel.add(frame, JDesktopPane.DEFAULT_LAYER);
			frame.setFocusable(true);
			if (wi.isClosed()) {
				closeWindow(w);
			}
		}

		if (wi.isMaximized()) {
			frame.setBounds(wi.getNormalBounds());
		//	frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		}
		activateFrame(frame);
   }

    public void refresh(IWindow win) {
    	Component frame = fws.getFrame(win);
    	if (frame!=null)
    		frame.setVisible(true);
    }

	public void setBackgroundImage(ImageIcon image, String typeDesktop) {
		this.image=image;
		this.typeDesktop=typeDesktop;

	}
	class MyDesktopPane extends JDesktopPane
    {
      public MyDesktopPane(){
      }
      public void paintComponent(Graphics g){
        super.paintComponent(g);
        int x=0;
        int y=0;
        int w=0;
        int h=0;
        if (image != null) {
			if (typeDesktop.equals(Theme.CENTERED)) {
				w = image.getIconWidth();
				h = image.getIconHeight();
				x = (getWidth() - w) / 2;
				y = (getHeight() - h) / 2;
				g.drawImage(image.getImage(), x, y, w, h, this);
			} else if (typeDesktop.equals(Theme.EXPAND)) {
				w = getWidth();
				h = getHeight();
				g.drawImage(image.getImage(), x, y, w, h, this);
			} else if (typeDesktop.equals(Theme.MOSAIC)) {
				int wAux = image.getIconWidth();
				int hAux = image.getIconHeight();
				int i = 0;
				int j = 0;
				w = wAux;
				h = hAux;
				while (x < getWidth()) {
					x = wAux * i;
					while (y < getHeight()) {
						y = hAux * j;
						j++;
						g.drawImage(image.getImage(), x, y, w, h, this);
					}
					y = 0;
					j = 0;
					i++;
				}
			}
		}
      }
    }


}
