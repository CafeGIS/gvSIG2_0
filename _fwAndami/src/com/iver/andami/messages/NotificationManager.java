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
package com.iver.andami.messages;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;


/**
 * Clase que recibe los mensajes de error, warning e información y dispara los
 * eventos en los listeners que escuchan dichos eventos
 *
 * @version $Revision: 29118 $
 */
public class NotificationManager {
	private static int SIZE_MESSAGE=4;
    /** DOCUMENT ME! */
    private static Logger logger = LoggerFactory.getLogger(NotificationManager.class.getName());

    /** Timer de espera de nuevos mensajes */
    private static Timer timer;

    /** Indica si se han añadido mensajes desde la última vez que se comprobó */
    private static boolean addedMessage = false;

    /** DOCUMENT ME! */
    private static ArrayList info = new ArrayList();
    private static ArrayList infoExceptions = new ArrayList();

    /** DOCUMENT ME! */
    private static ArrayList warnings = new ArrayList();
    private static ArrayList warningsExceptions = new ArrayList();

    /** DOCUMENT ME! */
    private static ArrayList errors = new ArrayList();
    private static ArrayList errorsExceptions = new ArrayList();

    /** DOCUMENT ME! */
    private static Vector listeners = new Vector();
    private static boolean isFirst = true;

    /**
     * Añade un objeto que escucha los mensajes de error, warning e información
     *
     * @param nl objeto que recibirá los eventos
     */
    public static synchronized void addNotificationListener(NotificationListener nl) {
        logger.info("Se añade un listener al manager de notificaciones");
        listeners.add(nl);
    }

    /**
     * @see com.iver.mdiApp.Notification#addError(java.lang.String)
     */
    public static synchronized void addError(final String err, Throwable e) {
    	logger.error(err, e);
        errors.add(err);
        errorsExceptions.add(e);

        if (isFirst) {
            AddError((String[]) errors.toArray(new String[0]),
                (Throwable[]) errorsExceptions.toArray(new Throwable[0]));
            errors.clear();
            errorsExceptions.clear();
            isFirst = false;
        }

        dispatchMessages();
    }

    /**
     * @see com.iver.mdiApp.Notification#addWarning(java.lang.String)
     */
    public static synchronized void addWarning(final String warn, final Throwable e) {
		logger.warn(warn, e);
        warnings.add(warn);
        warningsExceptions.add(e);

        if (isFirst) {
            AddWarning((String[]) warnings.toArray(new String[0]),
                (Throwable[]) warningsExceptions.toArray(new Throwable[0]));
            warnings.clear();
            warningsExceptions.clear();
            isFirst = false;
        }

        dispatchMessages();
    }
    /*
     * @see com.iver.mdiApp.Notification#addWarning(java.lang.String)
     */
    public static synchronized void addWarning(final String warn) {
		addWarning(warn,null);
    }

    /*
     * @see com.iver.mdiApp.Consola#addInfo(java.lang.String)
     */
    public static synchronized void addInfo(final String inf, final Throwable e) {
		logger.info(inf, e);
        info.add(inf);
        infoExceptions.add(e);

        if (isFirst) {
            AddInfo((String[]) info.toArray(new String[0]),
                (Throwable[]) infoExceptions.toArray(new Throwable[0]));
            info.clear();
            infoExceptions.clear();
            isFirst = false;
        }

        dispatchMessages();
    }
    /*
     * @see com.iver.mdiApp.Consola#addInfo(java.lang.String)
     */
    public static synchronized void addInfo(final String inf) {
		addInfo(inf,null);
    }
    /**
     * Método que es ejecutado en el thread de la interfaz y que se encarga de
     * avisar del mensaje de error a todos los listeners registrados
     *
     * @param error Mensaje de error
     * @param e s que van a recibir las notificaciones
     */
    private static void AddError(String[] error, Throwable[] e) {
        for (int i = 0; i < listeners.size(); i++) {
            ((NotificationListener) listeners.get(i)).errorEvent(new MessageEvent(
                    error, e));
        }
    }

    /**
     * Método que es ejecutado en el thread de la interfaz y que se encarga de
     * avisar del mensaje de error a todos los listeners registrados
     *
     * @param warn Mensaje de warning
     * @param e objetos que van a recibir las notificaciones
     */
    private static void AddWarning(String[] warn, Throwable[] e) {
        for (int i = 0; i < listeners.size(); i++) {
            ((NotificationListener) listeners.get(i)).warningEvent(new MessageEvent(
                    warn, e));
        }
    }

    /**
     * Método que es ejecutado en el thread de la interfaz y que se encarga de
     * avisar del mensaje de información a todos los listeners registrados
     *
     * @param info Mensaje de información
     * @param e objetos que van a recibir las notificaciones
     */
    private static void AddInfo(String[] info, Throwable[] e) {
        for (int i = 0; i < listeners.size(); i++) {
            ((NotificationListener) listeners.get(i)).infoEvent(new MessageEvent(
                    info, e));
        }
    }

    /**
     * DOCUMENT ME!
     */
    private static void dispatchMessages() {
        addedMessage = true;

        if (timer == null) {

            timer = new Timer(1000, new ActionListener() {
				public void actionPerformed(ActionEvent e) {

			        if (errors.size() > 0) {
			            AddError((String[]) errors.toArray(new String[0]),
			                (Throwable[]) errorsExceptions.toArray(new Throwable[0]));
			            errors.clear();
			            errorsExceptions.clear();
			        }

			        if (warnings.size() > 0) {
			            AddWarning((String[]) warnings.toArray(new String[0]),
			                (Throwable[]) warningsExceptions.toArray(new Throwable[0]));
			            warnings.clear();
			            warningsExceptions.clear();
			        }

			        if (info.size() > 0) {
			            AddInfo((String[]) info.toArray(new String[0]),
			                (Throwable[]) infoExceptions.toArray(new Throwable[0]));
			            info.clear();
			            infoExceptions.clear();
			        }

			        if (!addedMessage) {
			            if (timer != null) {
			                timer.stop();
			            }

			            timer = null;
			        }

			        addedMessage = false;
			        isFirst = true;
				}
			});
            timer.start();
        }
    }

	public static void addError(Throwable e1) {
		addError(e1.toString(), e1);

	}

	public static void showMessageError(String message,Exception e) {
		message=splitMessage(message);
		JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),message);
		NotificationManager.addWarning(message,e);
	}
	public static void showMessageWarning(String message,Exception e) {
		message=splitMessage(message);
		JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),message);
		NotificationManager.addWarning(message,e);
	}
	public static void showMessageInfo(String message,Exception e) {
		message=splitMessage(message);
		JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),message);
		NotificationManager.addInfo(message,e);
	}
	private static String splitMessage(String message) {
		String[] messages=message.split("\n");
		String resultMessage="";
		for (int i=0;i<messages.length && i<=SIZE_MESSAGE;i++){
			resultMessage+=(messages[i]);
			resultMessage+=("\n");
		}
		return resultMessage;
	}
}
