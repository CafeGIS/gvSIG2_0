/*
 * Created on 17-feb-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.layout.Attributes;
import com.iver.cit.gvsig.project.documents.layout.FLayoutUtilities;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;


/**
 * Extensión desde la que se imprime.
 *
 * @author Vicente Caballero Navarro
 */
public class Print extends Extension implements Printable {
	public static PrinterJob printerJob = PrinterJob.getPrinterJob();
	private static Layout l = null;
	//private Paper paper;
	Rectangle2D.Double aux = null;
	private int veces = 0;
	private PrintService[] m_cachePrintServices = null;
	private PrintService m_cachePrintService = null;

	public void execute(String s) {
		doPrint((Layout) PluginServices.getMDIManager().getActiveWindow());
	}

	public void doPrint(Layout layout) {
		l = layout;
		try {
			PluginServices.backgroundExecution(new Runnable() {
					public void run() {
						if (l.getLayoutContext().getAttributes().getType() == Attributes.CUSTOM) {
							l.showPrintDialog(printerJob);
						} else {
							l.showPrintDialog(null);
						}
					}
				});

		} catch (Exception e) {
			System.out.println("Excepción al abrir el diálogo de impresión: " +
				e);
		}
	}

	public void setLayout(Layout l){
		this.l=l;
	}

	/**
	 * Se dibuja sobre el graphics el Layout.
	 *
	 * @param g2 graphics sobre el que se dibuja.
	 */
	public void drawShapes(Graphics2D g2) {
		l.drawLayoutPrint(g2);
	}

	public boolean isVisible() {
		IWindow f = PluginServices.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}

		return (f instanceof Layout);
	}

	public boolean isEnabled() {
		Layout f = (Layout) PluginServices.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}

		return true;
	}

	public int print(Graphics g, PageFormat format, int pi)
		throws PrinterException {
		if (pi >= 1) {
			return Printable.NO_SUCH_PAGE;
		}

		System.err.println("Clip 0 = " + g.getClip());

		Graphics2D g2d = (Graphics2D) g;

		double x = format.getImageableX();
		double y = format.getImageableY();
		double w = format.getImageableWidth();
		double h = format.getImageableHeight();

		//System.err.println("Orientación en Print: " + format.getOrientation());
		System.out.println("print:(" + x + "," + y + "," + w + "," + h + ")");
		System.err.println("Clip 1 = " + g2d.getClip());

		AffineTransform at = g2d.getTransform();
		g2d.translate(0, 0);
		l.obtainRect(true);

		//LWSAffineTransform at = g2d.getTransform();
		g2d.scale((double) 72 / (double) (Attributes.DPI),
			(double) 72 / (double) (Attributes.DPI));
		System.err.println("Clip 2 =" + g2d.getClip());

		if (l.getLayoutContext().getAttributes().isMargin()) {
			g2d.setClip((int) (l.getLayoutControl().getRect().getMinX() +
				FLayoutUtilities.fromSheetDistance(l.getLayoutContext().getAttributes().m_area[2],
					l.getLayoutControl().getAT())),
				(int) (l.getLayoutControl().getRect().getMinY() +
				FLayoutUtilities.fromSheetDistance(l.getLayoutContext().getAttributes().m_area[0],
					l.getLayoutControl().getAT())),
				(int) (l.getLayoutControl().getRect().getWidth() -
				FLayoutUtilities.fromSheetDistance(l.getLayoutContext().getAttributes().m_area[2] +
					l.getLayoutContext().getAttributes().m_area[3], l.getLayoutControl().getAT())),
				(int) (l.getLayoutControl().getRect().getHeight() -
				FLayoutUtilities.fromSheetDistance(l.getLayoutContext().getAttributes().m_area[0] +
					l.getLayoutContext().getAttributes().m_area[1], l.getLayoutControl().getAT())));
		}

		veces++;
		System.out.println("veces = " + veces);

		drawShapes(g2d);
		g2d.setTransform(at);

		return Printable.PAGE_EXISTS;
	}

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
	 * Abre un diálogo para imprimir.
	 *
	 * @param layout Layout a imprimir.
	 */
	public void OpenDialogToPrint(Layout layout) {
		l = layout;

		try {
			if (layout.getLayoutContext().getAttributes().getType() == Attributes.CUSTOM) {
				layout.showPrintDialog(printerJob);
			} else {
				layout.showPrintDialog(null);
			}
		} catch (Exception e) {
			System.out.println("Excepción al abrir el diálogo de impresión: " +
				e);
			e.printStackTrace();
		}
	}

	/**
	 * Imprime el Layout que se pasa como parámetro.
	 *
	 * @param layout Layout a imprimir.
	 */
	public void printLayout(Layout layout) {
		l = layout;

		try {
			printerJob.setPrintable((Printable) PluginServices.getExtension(
					com.iver.cit.gvsig.Print.class));

			//Actualizar attributes
			PrintRequestAttributeSet att = layout.getLayoutContext().getAttributes()
												 .toPrintRequestAttributeSet();
			DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;

			if (m_cachePrintServices == null) {
				m_cachePrintServices = PrintServiceLookup.lookupPrintServices(flavor,
						null);
			}

			PrintService defaultService = null;

			if (m_cachePrintService == null) {
				defaultService = PrintServiceLookup.lookupDefaultPrintService();
			}

			if (m_cachePrintService == null) {
				m_cachePrintService = ServiceUI.printDialog(null, 200, 200,
						m_cachePrintServices, defaultService, flavor, att);
			}

			if (m_cachePrintService != null) {
				DocPrintJob jobNuevo = m_cachePrintService.createPrintJob();
				PrintJobListener pjlistener = new PrintJobAdapter() {
						public void printDataTransferCompleted(PrintJobEvent e) {
							System.out.println("Fin de impresión");
						}
					};

				jobNuevo.addPrintJobListener(pjlistener);

				Doc doc = new SimpleDoc(PluginServices.getExtension(
							com.iver.cit.gvsig.Print.class), flavor, null);
				jobNuevo.print(doc, att);
			}
		} catch (PrintException pe) {
			pe.printStackTrace();
		}
	}
}
