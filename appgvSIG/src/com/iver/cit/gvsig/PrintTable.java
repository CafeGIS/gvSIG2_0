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
package com.iver.cit.gvsig;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JTable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.project.document.table.gui.FeatureTableDocumentPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;


/**
 * Extensión para imprimir una tabla.
 *
 * @author Vicente Caballero Navarro
 */
public class PrintTable extends Extension implements Printable {
    private static final Logger logger = LoggerFactory
            .getLogger(PrintTable.class);
    
	private JTable table = null;

	/**
     * DOCUMENT ME!
     */
    public void initialize() {
    	registerIcons();
    }

    private void registerIcons(){
    	PluginServices.getIconTheme().registerDefault(
				"document-print",
				this.getClass().getClassLoader().getResource("images/print.png")
			);
    }
    /**
     * DOCUMENT ME!
     *
     * @param actionCommand DOCUMENT ME!
     */
    public void execute(String s) {
    	if (s.compareTo("PRINTTABLE") == 0){
    		IWindow f = PluginServices.getMDIManager().getActiveWindow();
    		try {
				table=((FeatureTableDocumentPanel)f).getTablePanel().getTable();
			} catch (DataException e) {
				NotificationManager.showMessageError(PluginServices.getText(this, "print_table_error"), e);
			}
    		PluginServices.backgroundExecution(new Runnable() {
				public void run() {
					PrinterJob pj=PrinterJob.getPrinterJob();
			        pj.setPrintable(PrintTable.this);
			        if (pj.printDialog()){
			        try{
			        	pj.print();
			        }catch (Exception PrintException) {}
			        }
				}
			});
    	}
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEnabled() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isVisible() {
    	IWindow f = PluginServices.getMDIManager().getActiveWindow();
		if (f == null) {
			return false;
		}
		if (f instanceof FeatureTableDocumentPanel) {
			return true;
		} else {
			return false;
		}
    }

    public int print(Graphics g, PageFormat pageFormat,
            int pageIndex) throws PrinterException {
         	Graphics2D  g2 = (Graphics2D) g;
         	g2.setColor(Color.black);
         	int fontHeight=g2.getFontMetrics().getHeight();
         	int fontDesent=g2.getFontMetrics().getDescent();

         	//leave room for page number
         	double pageHeight =
         	  pageFormat.getImageableHeight()-fontHeight;
         	double pageWidth =
         	  pageFormat.getImageableWidth();
         	double tableWidth = (double)
              table.getColumnModel(
              ).getTotalColumnWidth();
         	double scale = 1;
         	if (tableWidth >= pageWidth) {
    		scale =  pageWidth / tableWidth;
    	}

         	double headerHeightOnPage=
                     table.getTableHeader(
                     ).getHeight()*scale;
         	double tableWidthOnPage=tableWidth*scale;

         	double oneRowHeight=(table.getRowHeight()+
                          table.getRowMargin())*scale;
         	int numRowsOnAPage=
                  (int)((pageHeight-headerHeightOnPage)/
                                      oneRowHeight);
         	double pageHeightForTable=oneRowHeight*
         	                            numRowsOnAPage;
         	int totalNumPages=
         	      (int)Math.ceil((
                    (double)table.getRowCount())/
                                        numRowsOnAPage);
         	if(pageIndex>=totalNumPages) {
                          return NO_SUCH_PAGE;
         	}

         	g2.translate(pageFormat.getImageableX(),
                           pageFormat.getImageableY());
//    bottom center
         	g2.drawString("Page: "+(pageIndex+1),
         	    (int)pageWidth/2-35, (int)(pageHeight
         	    +fontHeight-fontDesent));

         	g2.translate(0f,headerHeightOnPage);
         	g2.translate(0f,-pageIndex*pageHeightForTable);

         	//If this piece of the table is smaller
         	//than the size available,
         	//clip to the appropriate bounds.
         	if (pageIndex + 1 == totalNumPages) {
               int lastRowPrinted =
                     numRowsOnAPage * pageIndex;
               int numRowsLeft =
                     table.getRowCount()
                     - lastRowPrinted;
               g2.setClip(0,
                 (int)(pageHeightForTable * pageIndex),
                 (int) Math.ceil(tableWidthOnPage),
                 (int) Math.ceil(oneRowHeight *
                                   numRowsLeft));
         	}
         	//else clip to the entire area available.
         	else{
                 g2.setClip(0,
                 (int)(pageHeightForTable*pageIndex),
                 (int) Math.ceil(tableWidthOnPage),
                 (int) Math.ceil(pageHeightForTable));
         	}

         	g2.scale(scale,scale);
         	table.paint(g2);
         	g2.scale(1/scale,1/scale);
         	g2.translate(0f,pageIndex*pageHeightForTable);
         	g2.translate(0f, -headerHeightOnPage);
         	g2.setClip(0, 0,
         	  (int) Math.ceil(tableWidthOnPage),
              (int)Math.ceil(headerHeightOnPage));
         	g2.scale(scale,scale);
         	table.getTableHeader().paint(g2);
         	//paint header at top

         	return Printable.PAGE_EXISTS;
       }


}
