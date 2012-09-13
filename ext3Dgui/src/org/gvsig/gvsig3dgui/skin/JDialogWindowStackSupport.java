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

import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.config.generate.Menu;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.core.mdiManager.WindowStackSupport;

/**
 * 
 */
public class JDialogWindowStackSupport extends WindowStackSupport {
	private Vector vistas = new Vector();
	
	private JDialogWindowInfoSupport vis;
	
	private Hashtable viewMenu = new Hashtable();
	
	/**
	 * @param vis
	 */
	public JDialogWindowStackSupport(JDialogWindowInfoSupport vis) {
		super(vis);
		this.vis = vis;
	}

	public void add(IWindow v, final ActionListener listener) {
		vistas.add(v);
		WindowInfo vi = vis.getWindowInfo(v);
		int id = vi.getId();
		Menu m = new Menu();
		m.setActionCommand(""+id);
		m.setTooltip(PluginServices.getText(this, "activa_la_ventana"));
		m.setText("ventana/"+vi.getTitle());
		viewMenu.put(v, m);
		PluginServices.getMainFrame().addMenu(m, listener, PluginServices.getPluginServices(this).getClassLoader() );
	}
	
	public void remove(IWindow v){
		Menu m = (Menu) viewMenu.get(v);
		if (m == null) return;
		PluginServices.getMainFrame().removeMenu(m);
		viewMenu.remove(v);
		vistas.remove(v);
	}
	
	/**
	 * FJP: No se usa, y no sé para qué estaba pensado. 
	 */
	public void ctrltab(){
		IWindow v = (IWindow) vistas.remove(vistas.size() - 1);
		vistas.add(0, v);
	}
	
	public IWindow getActiveWindow(){
		if (vistas.size() == 0) return null;
        int index = vistas.size()-1;
        while (index >= 0)
        {
            IWindow aux = (IWindow) vistas.get(index);
            if (!aux.getWindowInfo().isPalette())
            {
//                System.err.println("getActiveView = " + aux.getWindowInfo().getTitle());
                return aux;
            }
            index--;
        }
        return null;
	}
    /**
     * Se utiliza cuando ya está abierta la vista para indicar
     * que la pasamos a activa. De esta forma evitamos que el
     * getActiveView devuelva algo que no es. 
     * En realidad lo que haces es mover la vista a la última
     * posición.
     * @param v
     */
    public void setActive(IWindow v)
    {
        IWindow copia = null;
        boolean bCopiar = false;
        // Si es de tipo palette, no se pone como activa.
        // De esta forma, nunca nos la devolverá.... Bueno, 
        // igual si cerramos la de encima. Voy a ponerle en
        // getActiveView que si es de tipo Palette, devuelva la
        // de abajo.
        if (v.getWindowInfo().isPalette()) return;
        
        for (int i=0; i < vistas.size(); i++)
        {
            IWindow aux = (IWindow) vistas.get(i);
            if (aux == v)
            {
                copia = aux;
                bCopiar = true;
            }
            if (bCopiar)
            {
                if (i < vistas.size()-1)
                {
                    IWindow siguiente = (IWindow) vistas.get(i+1);
                    vistas.set(i,siguiente);
                }
                else // La última
                    vistas.set(i,copia);
            }
        } // for
    }
}
