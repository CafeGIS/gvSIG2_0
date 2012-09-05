/*
 * Created on 27-sep-2004
 *
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
package com.iver.cit.gvsig.project.documents.layout;


import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.SimpleDoc;
import javax.print.StreamPrintService;
import javax.print.StreamPrintServiceFactory;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JOptionPane;

import org.gvsig.fmap.dal.exception.ReadException;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.Print;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;


/**
 * Clase que implementa los métodos del Layout que dibujan sobre el Graphics.
 *
 * @author Vicente Caballero Navarro
 */
public class FLayoutDraw {
    private Layout layout;


    /**
     * Crea un nuevo FLayoutDraw.
     *
     * @param l Referencia al Layout.
     */
    public FLayoutDraw(Layout l) {
        layout = l;
    }

    /**
     * Método para dibujar el Layout y modificar la matriz de transformación  a
     * partir del tamaño en pixels que tenga rect y el formato de papel
     * seleccionado.
     *
     * @param g2
     * @param imgBase Si es null, estás imprimiendo. Si no, la usas para el
     *        código de  optimización.
     *
     * @throws ReadDriverException
     */
    public void drawLayout(Graphics2D g2, BufferedImage imgBase)
        throws ReadException {
    	LayoutControl layoutControl=layout.getLayoutControl();
    	LayoutContext layoutContext= layout.getLayoutContext();
    	Attributes attributes=layoutContext.getAttributes();
    	Rectangle2D rLayout=layoutControl.getRect();
    	AffineTransform at=layoutContext.getAT();

    	layoutControl.setCancelDrawing(false);
        double scale = 0;
        scale = rLayout.getHeight() / attributes.m_sizePaper.getAlto() * 1;
        AffineTransform escalado = new AffineTransform();
        AffineTransform translacion = new AffineTransform();
        translacion.setToTranslation(rLayout.getMinX(),
            rLayout.getMinY());
        escalado.setToScale(scale, scale);
        at.setToIdentity();
        at.concatenate(translacion);
        at.concatenate(escalado);
        attributes.setDistanceUnitX(rLayout);
        attributes.setDistanceUnitY(rLayout);
        IFFrame[] fframes=layoutContext.getFFrames();
        for (int i = 0; i < fframes.length; i++) {
        	IFFrame f = fframes[i];
        	//original, lento (NO MAC)
    		f.draw(g2, at, layoutControl.getVisibleRect(),imgBase);
        	// ESTILO MAC
        	/* if (f instanceof FFrameView)
        	{
        		FFrameView fframe = (FFrameView) f;
        		BufferedImage img = new BufferedImage((int) layoutControl.getWidth(),
    				(int) layoutControl.getHeight(), BufferedImage.TYPE_INT_ARGB);


        		fframe.draw(img.createGraphics(), at,	layoutControl.getVisibleRect(), imgBase);
        		g2.drawImage(img, 0, 0, layoutControl);
        		fframe.setBufferedImage(img);
        	}
        	else
        	{
        		f.draw(g2, at, layoutControl.getVisibleRect(),imgBase);
        	} */

            //          Dibuja el símbolo de que contiene un tag.
            if ((f.getTag() != null) && (layout.isShowIconTag())) {
                f.drawSymbolTag(g2);
            }
        }

        if (!(fframes.length==0)) {
            layoutControl.setStatus(LayoutControl.ACTUALIZADO);
        } else {
            layoutControl.setStatus(LayoutControl.DESACTUALIZADO);
        }
    }

    /**
     * Dibuja sobre un Graphics2D el rectángulo que representa al folio.
     *
     * @param g Graphics2D
     */
    public void drawRectangle(Graphics2D g) {
    	Attributes attributes=layout.getLayoutContext().getAttributes();
    	Rectangle2D rLayout=layout.getLayoutControl().getRect();
    	AffineTransform at=layout.getLayoutContext().getAT();

    	double unidadesX = attributes.getNumUnitsX();
        double unidadesY = attributes.getNumUnitsY();

        if ((unidadesX == 0) && (unidadesY == 0)) {
            return;
        }

        g.setColor(Color.darkGray);

        Rectangle2D.Double rectBig = new Rectangle2D.Double(rLayout.getX(),
                rLayout.getY(), rLayout.getWidth(),
                rLayout.getHeight());

        g.fillRect((int) rectBig.x + 7, (int) rectBig.y + 7,
            (int) rectBig.width, (int) rectBig.height);

        Rectangle2D.Double r = new Rectangle2D.Double();

        if (attributes.isMargin()) {

            r = new Rectangle2D.Double((rLayout.getX() +
                    FLayoutUtilities.fromSheetDistance(
                        attributes.m_area[2], at)),
                    (rLayout.getY() +
                    FLayoutUtilities.fromSheetDistance(
                        attributes.m_area[0], at)),
                    (rLayout.getWidth() -
                    FLayoutUtilities.fromSheetDistance(
                    	attributes.m_area[2] +
                        attributes.m_area[3], at)),
                    (rLayout.getHeight() -
                    FLayoutUtilities.fromSheetDistance(
                    	attributes.m_area[0] +
                        attributes.m_area[1], at)));
        } else {
            r.setRect(rLayout);
        }

        g.setColor(Color.white);
        g.fill(rLayout);

        g.setColor(Color.darkGray);
        g.drawRect((int) rLayout.getMinX(),
            (int) rLayout.getMinY(),
            (int) rLayout.getWidth(),
            (int) rLayout.getHeight());

        if (attributes.isMargin()) {
            g.setColor(Color.black);

            g.drawRect((int) r.x, (int) r.y, (int) r.width, (int) r.height);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void drawGrid(Graphics2D g) {
        int unidadesMin = 6;
        LayoutContext layoutContext= layout.getLayoutContext();
        Attributes attributes=layoutContext.getAttributes();
    	Rectangle2D rLayout=layout.getLayoutControl().getRect();
    	AffineTransform at=layoutContext.getAT();


    	double unidadesX = attributes.getUnitInPixelsX();
        double unidadesY = attributes.getUnitInPixelsY();

        Rectangle2D.Double r = new Rectangle2D.Double();

        if (attributes.isMargin()) {
            r = new Rectangle2D.Double((rLayout.getX() +
                    FLayoutUtilities.fromSheetDistance(
                        attributes.m_area[2], at)),
                    (rLayout.getY() +
                    FLayoutUtilities.fromSheetDistance(
                        attributes.m_area[0], at)),
                    (rLayout.getWidth() -
                    FLayoutUtilities.fromSheetDistance(attributes.m_area[2] +
                        attributes.m_area[3], at)),
                    (rLayout.getHeight() -
                    FLayoutUtilities.fromSheetDistance(attributes.m_area[0] +
                        attributes.m_area[1], at)));
        } else {
            r.setRect(rLayout);
        }
        if ((unidadesX == 0) && (unidadesY == 0)) {
            return;
        }
        g.setColor(Color.darkGray);

        if (((unidadesX > unidadesMin) || (unidadesY > unidadesMin)) &&
                layoutContext.isGridVisible()) {
            double ax = r.getMinX();
            double ay;

            while (ax < (r.getMaxX())) {
                ay = (r.getMinY());

                while (ay < (r.getMaxY())) {
                    g.drawLine((int) ax, (int) ay, (int) ax, (int) ay);
                    ay = ay + unidadesY;
                }

                ax = ax + unidadesX;
            }
        }
    }

    /**
     * Dibuja sobre el graphics2d las reglas.
     *
     * @param g graphics2d sobre el que se dibuja.
     * @param color Color de la regla.
     */
    public void drawRuler(Graphics2D g, Color color) {
    	LayoutControl layoutControl=layout.getLayoutControl();
    	LayoutContext layoutContext= layout.getLayoutContext();
    	Attributes attributes=layoutContext.getAttributes();
    	Rectangle2D rLayout=layoutControl.getRect();
    	AffineTransform at=layoutContext.getAT();

        if (layoutContext.getRuler()) {
            int ini = 10;
            int w = 30;
            int wi = 16;

            g.setColor(new Color(250, 255, 250, 180));
            g.fillRect(ini, w, wi, layoutControl.getHeight() - w);
            g.fillRect(w, ini, layoutControl.getWidth() - w, wi);

            g.setColor(new Color(100, 155, 150, 180));
            g.fillRect(w, ini, (int) rLayout.getX() - w, wi);
            g.fillRect((int) rLayout.getMaxX(), ini,
                layoutControl.getWidth() - (int) rLayout.getMaxX(), wi);

            g.fillRect(ini, w, wi, (int) rLayout.getY() - w);
            g.fillRect(ini, (int) rLayout.getMaxY(), wi,
                layoutControl.getHeight() - (int) rLayout.getMaxY());

            if (attributes.isMargin()) {
                g.setColor(new Color(50, 55, 50, 180));
                g.fillRect((int) rLayout.getX(), ini,
                    (int) FLayoutUtilities.fromSheetDistance(
                        attributes.m_area[2], at), wi);
                g.fillRect((int) rLayout.getMaxX() -
                    (int) FLayoutUtilities.fromSheetDistance(
                        attributes.m_area[3], at), ini,
                    (int) FLayoutUtilities.fromSheetDistance(
                        attributes.m_area[3], at), wi);

                g.fillRect(ini, (int) rLayout.getY(), wi,
                    (int) FLayoutUtilities.fromSheetDistance(
                        attributes.m_area[0], at));
                g.fillRect(ini,
                    (int) rLayout.getMaxY() -
                    (int) FLayoutUtilities.fromSheetDistance(
                        attributes.m_area[1], at), wi,
                    (int) FLayoutUtilities.fromSheetDistance(
                        attributes.m_area[1], at));
            }

            g.setColor(color);
            g.drawLine(w, wi + ini, layoutControl.getWidth(), wi + ini);
            g.drawLine(w, ini, layoutControl.getWidth(), ini);
            g.drawLine(ini, w, ini, layoutControl.getHeight());
            g.drawLine(wi + ini, w, wi + ini, layoutControl.getHeight());

            drawLineY(g, 5, 12, 22, rLayout.getY(), 0);
            drawLineX(g, 5, 12, 22, rLayout.getX(), 0);

            if (FLayoutUtilities.fromSheetDistance(1, at) > 15) {
                drawLineY(g, 1, 12, 22, rLayout.getY(), 0);
                drawLineX(g, 1, 12, 22, rLayout.getX(), 0);

                if (FLayoutUtilities.fromSheetDistance(1, at) > 25) {
                    drawLineY(g, 1, 18, 22, rLayout.getY(), 0.5);
                    drawLineY(g, 0.1, 21, 22, rLayout.getY(), 0);
                    drawLineX(g, 1, 18, 22, rLayout.getX(), 0.5);
                    drawLineX(g, 0.1, 21, 22, rLayout.getX(), 0);
                }
            }
        }
    }

    /**
     * Dibuja sobre el graphics2d las líneas verticales que representan a las
     * unidades de medida.
     *
     * @param g Graphics2d sobre el que se dibuja.
     * @param dist distancia en centímetros de una línea a otra.
     * @param init inicio de la línea.
     * @param end fin de la línea.
     * @param y rectángulo, dentro del cual se dibujan las líneas.
     * @param desp Desplazamiento.
     */
    private void drawLineY(Graphics2D g, double dist, int init, int end,
        double y, double desp) {
    	AffineTransform at=layout.getLayoutContext().getAT();
    	LayoutControl layoutControl=layout.getLayoutControl();

    	double distY = FLayoutUtilities.fromSheetDistance(dist, at);
        double rota = Math.toRadians(90);

        if (distY > 4) {
            double despY = FLayoutUtilities.fromSheetDistance(desp,
                    at);

            double posUnitY = y + despY;
            double posUnitYNeg = posUnitY;
            int num = 0;
            double iniY = 40;
            Point2D.Double pfin = FLayoutUtilities.fromSheetPoint(new Point2D.Double(
                        layoutControl.getWidth(), layoutControl.getHeight()), at);

            while (posUnitY < (pfin.y - 5)) {
                posUnitYNeg -= distY;

                if (distY > 16) {
                    if (init == 12) {
                        if (posUnitY > iniY) {
                            g.rotate(-rota, 20, posUnitY - 12);
                            g.drawString(String.valueOf(num), 10,
                                (int) posUnitY - 12);
                            g.rotate(rota, 20, posUnitY - 12);
                        }

                        if (dist == 5) {
                            num = num + 5;
                        } else {
                            num++;
                        }

                        if (posUnitYNeg > iniY) {
                            g.rotate(-rota, 20, posUnitYNeg - 12);
                            g.drawString(String.valueOf(-num), 10,
                                (int) posUnitYNeg - 12);
                            g.rotate(rota, 20, posUnitYNeg - 12);
                        }
                    }
                }

                if (posUnitY > iniY) {
                    g.drawLine( 2 + init, (int) posUnitY, 2 + end,
                        (int) posUnitY);
                }

                if (posUnitYNeg > iniY) {
                    g.drawLine(2 + init, (int) posUnitYNeg,
                        2 + end, (int) posUnitYNeg);
                }

                posUnitY += distY;
            }
        }
    }

    /**
     * Dibuja sobre el graphics2d las líneas horizontales que representan a las
     * unidades de medida.
     *
     * @param g Graphics2d sobre el que se dibuja.
     * @param dist distancia en centímetros de una línea a otra.
     * @param init inicio de la línea.
     * @param end fin de la línea.
     * @param x rectángulo, dentro del cual se dibujan las líneas.
     * @param desp Desplazamiento.
     */
    private void drawLineX(Graphics2D g, double dist, int init, int end,
        double x, double desp) {
    	AffineTransform at=layout.getLayoutContext().getAT();
    	LayoutControl layoutControl=layout.getLayoutControl();

    	double distX = FLayoutUtilities.fromSheetDistance(dist, at);

        if (distX > 4) {
            double despX = FLayoutUtilities.fromSheetDistance(desp,
                    at);
            double posUnitX = x + despX;
            double posUnitXNeg = posUnitX;
            int num = 0;
            double iniX = 40;
            Point2D.Double pfin = FLayoutUtilities.fromSheetPoint(new Point2D.Double(
                        layoutControl.getWidth(), layoutControl.getHeight()), at);

            while (posUnitX < (pfin.x - 5)) {
                posUnitXNeg -= distX;

                if (init == 12) {
                    if (distX > 16) {
                        if (posUnitX > iniX) {
                            g.drawString(String.valueOf(num),
                                (int) posUnitX + 3, 20);
                        }

                        if (dist == 5) {
                            num = num + 5;
                        } else {
                            num++;
                        }

                        if (posUnitXNeg > iniX) {
                            g.drawString(String.valueOf(-num),
                                (int) posUnitXNeg + 3, 20);
                        }
                    }
                }

                if (posUnitX > iniX) {
                    g.drawLine((int) posUnitX, 2 + init, (int) posUnitX,
                        2 + end);
                }

                if (posUnitXNeg > iniX) {
                    g.drawLine((int) posUnitXNeg, 2 + init,
                        (int) posUnitXNeg, 2 + end);
                }

                posUnitX += distX;
            }
        }
    }

    /**
     * Dibuja los handlers sobre los fframes que esten seleccionados.
     *
     * @param g Graphics sobre el que se dibuja.
     * @param color Color de los Handlers.
     */
    public void drawHandlers(Graphics2D g, Color color) {
    	LayoutContext layoutContext= layout.getLayoutContext();

        g.setColor(color);
        IFFrame[] fframes=layoutContext.getFFrames();
        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i];

            if (fframe.getSelected() != IFFrame.NOSELECT) {
                fframe.drawHandlers(g);
            }
        }
    }

    /**
     * A partir de un fichero que se pasa como parámetro se crea un pdf con el
     * contenido del Layout.
     *
     * @param pdf
     */
 /*   public void toPS(File ps) {

             try {
                 // Open the image file
                // InputStream is = new BufferedInputStream(
                //     new FileInputStream("filename.gif"));

                 // Prepare the output file to receive the postscript
                 OutputStream fos = new BufferedOutputStream(
                     new FileOutputStream("filename.ps"));

                 // Find a factory that can do the conversion
                 //DocFlavor flavor = DocFlavor.INPUT_STREAM.GIF;
                 DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
                 StreamPrintServiceFactory[] factories =
                     StreamPrintServiceFactory.lookupStreamPrintServiceFactories(
                         flavor,
                         DocFlavor.BYTE_ARRAY.POSTSCRIPT.getMimeType());

                 if (factories.length > 0) {
                     StreamPrintService service = factories[0].getPrintService(fos);

                     // Create the print job
                     DocPrintJob job = service.createPrintJob();
                     Print print= (Print)PluginServices.getExtension(
                               com.iver.cit.gvsig.Print.class);
                     print.setLayout(layout);
                     Doc doc = new SimpleDoc((Printable)print, flavor, null);
                     //Doc doc = new SimpleDoc(is, flavor, null);
                     // Monitor print job events; for the implementation of PrintJobWatcher,
                     // see e702 Determining When a Print Job Has Finished
                     //PrintJobWatcher pjDone = new PrintJobWatcher(job);
//                   Actualizar attributes
                      PrintRequestAttributeSet att = layout.getAtributes()
                                                           .toPrintAttributes();
                     // Print it
                     job.print(doc, att);

                     // Wait for the print job to be done
                     //pjDone.waitForDone();
                     // It is now safe to close the streams
                 }

                // is.close();
                 fos.close();
             } catch (PrintException e) {
             } catch (IOException e) {
             }
             }
             */

/*
             public void printLayout(Layout layout) {
                 l = layout;

                 try {
                     printerJob.setPrintable((Printable) PluginServices.getExtension(
                             com.iver.cit.gvsig.Print.class));

                     //Actualizar attributes
                     PrintRequestAttributeSet att = layout.getAtributes()
                                                          .toPrintAttributes();
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

                         Doc doc = new SimpleDoc((Printable) PluginServices.getExtension(
                                     com.iver.cit.gvsig.Print.class), flavor, null);
                         jobNuevo.print(doc, att);
                     }
                 } catch (PrintException pe) {
                     pe.printStackTrace();
                 }
             }

        document.close();

        layout.fullRect();
    }
*/



    /**
     * A partir de un fichero que se pasa como parámetro se crea un ps con el
     * contenido del Layout.
     *
     * @param ps
     */
    public void toPS(File ps) {
    	Attributes attributes=layout.getLayoutContext().getAttributes();
    	LayoutControl layoutControl=layout.getLayoutControl();

    	try {
            // Prepare the output file to receive the postscript
            OutputStream fos = new BufferedOutputStream(
                new FileOutputStream(ps));

            // Find a factory that can do the conversion
            //DocFlavor flavor = DocFlavor.INPUT_STREAM.GIF;
            DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
            StreamPrintServiceFactory [] factories =
                 StreamPrintServiceFactory.lookupStreamPrintServiceFactories(flavor,
                       "application/postscript");

            if (factories.length > 0) {
                StreamPrintService service = factories[0].getPrintService(fos);

                // Create the print job
                DocPrintJob job = service.createPrintJob();
                Print print= new Print();//(Print)PluginServices.getExtension(
//                            com.iver.cit.gvsig.Print.class);
                print.setLayout(layout);
                Doc doc = new SimpleDoc(print, flavor, null);
                //Doc doc = new SimpleDoc(is, flavor, null);
                // Monitor print job events; for the implementation of PrintJobWatcher,
                // see e702 Determining When a Print Job Has Finished
                //PrintJobWatcher pjDone = new PrintJobWatcher(job);
//              Actualizar attributes
                 PrintRequestAttributeSet att = attributes
                                                      .toPrintRequestAttributeSet();
                // Print it
                job.print(doc, att);

                // Wait for the print job to be done
                //pjDone.waitForDone();
                // It is now safe to close the streams
            }

           // is.close();
            fos.close();
        } catch (PrintException e) {
        } catch (IOException e) {
        }
    /*    PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();
        DocPrintJob printerJob = defaultPrintService.createPrintJob();

        try {
        	DocFlavor flavor=DocFlavor.URL.POSTSCRIPT;
        	if (!defaultPrintService.isDocFlavorSupported(flavor)) {
        		   System.err.println("The printer does not support the appropriate DocFlavor");
        	}else {

        	SimpleDoc simpleDoc;
            simpleDoc = new SimpleDoc(ps.toURL(),flavor, null);
            printerJob.print(simpleDoc, null);
        	}
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (PrintException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
         layoutControl.fullRect();
    }


    /**
     * A partir de un fichero que se pasa como parámetro se crea un pdf con el
     * contenido del Layout.
     *
     * @param pdf
     */
    public void toPDF(File pdf) {
    	Attributes attributes=layout.getLayoutContext().getAttributes();
    	LayoutControl layoutControl=layout.getLayoutControl();

    	double w = 0;
        double h = 0;
        Document document = new Document();

        if (attributes.isLandSpace()) {
            w = ((attributes.m_sizePaper.getAlto() * Attributes.DPISCREEN) / Attributes.PULGADA);
            h = ((attributes.m_sizePaper.getAncho() * Attributes.DPISCREEN) / Attributes.PULGADA);
        } else {
            w = ((attributes.m_sizePaper.getAncho() * Attributes.DPISCREEN) / Attributes.PULGADA);
            h = ((attributes.m_sizePaper.getAlto() * Attributes.DPISCREEN) / Attributes.PULGADA);
        }

        document.setPageSize(new com.lowagie.text.Rectangle((float) w, (float) h));

        try {
        	FileOutputStream fos=new FileOutputStream(pdf);
            PdfWriter writer = PdfWriter.getInstance(document,fos);
            document.open();

            Print print = new Print();
            print.setLayout(layout);

            PdfContentByte cb = writer.getDirectContent();
            Graphics2D g2 = cb.createGraphicsShapes((float) w, (float) h);

            try {
                if (attributes.isLandSpace()) {
                    g2.rotate(Math.toRadians(-90), 0 + (w / (h / w)),
                        0 + (h / 2));
                    print.print(g2, new PageFormat(), 0);
                    g2.rotate(Math.toRadians(90), 0 + (w / (h / w)), 0 +
                        (h / 2));
                } else {
                    print.print(g2, new PageFormat(), 0);
                }
            } catch (PrinterException e) {
                e.printStackTrace();
            }

            g2.dispose();

        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog((Component) PluginServices.getMainFrame(),
                ioe.getMessage());
        }

        document.close();

        layoutControl.fullRect();
      }
}
