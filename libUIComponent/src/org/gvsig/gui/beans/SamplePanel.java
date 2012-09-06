/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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

/* CVS MESSAGES:
 *
 * $Id: SamplePanel.java 13655 2007-09-12 16:28:55Z bsanchez $
 * $Log$
 * Revision 1.3  2007-09-12 16:28:23  bsanchez
 * *** empty log message ***
 *
 * Revision 1.2  2007/08/21 08:39:05  bsanchez
 * - Sustituir "deprecated" show por setVisible
 *
 * Revision 1.1  2007/08/20 08:34:45  evercher
 * He fusionado LibUI con LibUIComponents
 *
 * Revision 1.1  2006/03/22 11:18:29  jaume
 * *** empty log message ***
 *
 * Revision 1.6  2006/02/28 15:25:14  jaume
 * *** empty log message ***
 *
 * Revision 1.4.2.4  2006/02/16 10:36:41  jaume
 * *** empty log message ***
 *
 * Revision 1.4.2.3  2006/01/31 16:25:24  jaume
 * correcciones de bugs
 *
 * Revision 1.5  2006/01/26 16:07:14  jaume
 * *** empty log message ***
 *
 * Revision 1.4.2.1  2006/01/26 12:59:32  jaume
 * 0.5
 *
 * Revision 1.4  2006/01/26 12:50:20  jaume
 * *** empty log message ***
 *
 * Revision 1.3  2006/01/25 15:14:02  jaume
 * *** empty log message ***
 *
 * Revision 1.2  2006/01/24 14:36:33  jaume
 * This is the new version
 *
 * Revision 1.1.2.5  2006/01/10 13:11:38  jaume
 * *** empty log message ***
 *
 * Revision 1.1.2.4  2006/01/10 11:33:31  jaume
 * Time dimension working against Jet Propulsion Laboratory's WMS server
 *
 * Revision 1.1.2.3  2006/01/09 18:10:38  jaume
 * casi con el time dimension
 *
 * Revision 1.1.2.2  2006/01/02 18:08:01  jaume
 * Tree de estilos
 *
 * Revision 1.1.2.1  2005/12/30 08:56:19  jaume
 * *** empty log message ***
 *
 *
 */
/**
 * 
 */
package org.gvsig.gui.beans;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.gvsig.gui.beans.listeners.BeanListener;
/**
 * Sample class for see how to use BeanListeners.
 * <p>
 * Have fun! ;-)
 * </p>
 * @author jaume
 */
public class SamplePanel extends JPanel {
  private static final long serialVersionUID = 3663247043945556938L;
		private Pager ep;
    /**
     * This is the default constructor
     */
    public SamplePanel() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(300, 200);
        this.setLayout(null);
        
        this.add(getEditionPanel(), null);
        
    }

    private Pager getEditionPanel(){
        if (ep == null){
            ep = new Pager(0, 20, Pager.HORIZONTAL);
            ep.addListener(new BeanListener(){

                public void beanValueChanged(Object value) {
                    System.out.println("("+((Integer) value).intValue()+")");
                }
                
            });
        }
        return ep;
    }
    
    public static void main(String[] args){
        //dim = new TimeDimension("units", "unitSymbol", "2004-12-24/2005-12-18/P1D");
        
        JFrame frame = new JFrame();
        frame.getContentPane().add(new SamplePanel());
        frame.setBounds(0, 0, 279, 63);
        frame.pack();
        frame.setVisible(true);
    }
}
