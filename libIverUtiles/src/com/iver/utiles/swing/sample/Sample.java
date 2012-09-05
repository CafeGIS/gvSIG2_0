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
package com.iver.utiles.swing.sample;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;


public class Sample {
	
    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        /* JFrame f = new JFrame("AutoCompleteComboBox");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComboBox box = new JComboBox(new Object[] {"", "bos días","ata logo","deica logo"});
        box.setEditable(true);
        f.getContentPane().add(box);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true); */
        /* JPasswordDlg dlg = new JPasswordDlg();
        dlg.setMessage("Hola");
        dlg.show(); */
        Locale ukLocale = new Locale("en", "UK"); // English, UK version
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, ukLocale);
        /* Calendar c = Calendar.getInstance();
        c.clear();
        c.set(Integer.parseInt(year), Integer.parseInt(month),
            Integer.parseInt(day));
        c.set(Calendar.MILLISECOND, 0); */
        String strAux = "12/06/1996";
        Date dat = null;
        try {
            dat = df.parse(strAux);
        } catch (ParseException e) {
            e.printStackTrace();            
        } 
        System.out.println(dat.getYear() + " " + dat.getMonth() + " " + dat.getDate());
        
        String aux = "Jaén";
        StringBuffer strBuf = new StringBuffer(aux);
        ByteArrayOutputStream out = new ByteArrayOutputStream(strBuf.length());
        PrintStream printStream = new PrintStream(out);
        printStream.print(aux);
        try {
            String aux2 = out.toString("UTF-8");
            System.out.println(aux + " " + aux2);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }                
    }}
