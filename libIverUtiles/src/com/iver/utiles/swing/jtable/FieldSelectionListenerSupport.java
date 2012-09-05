package com.iver.utiles.swing.jtable;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author Fernando González Cortés
 */
public class FieldSelectionListenerSupport {
    private ArrayList listeners = new ArrayList();

    /**
     * DOCUMENT ME!
     *
     * @param listener DOCUMENT ME!
     */
    public void addFieldSelectionListener(FieldSelectionListener listener) {
        listeners.add(listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @param listener DOCUMENT ME!
     */
    public void removeFieldSelectionListener(FieldSelectionListener listener) {
        listeners.remove(listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     */
    public void callFieldSelected(FieldSelectionEvent arg0) {
        Iterator i = listeners.iterator();

        while (i.hasNext()) {
            ((FieldSelectionListener) i.next()).fieldSelected(arg0);
        }
    }
}
