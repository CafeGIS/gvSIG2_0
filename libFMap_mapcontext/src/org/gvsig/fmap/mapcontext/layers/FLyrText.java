/*
 * Created on 28-dic-2004
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
package org.gvsig.fmap.mapcontext.layers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Set;

import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.layers.operations.ClassifiableVectorial;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontext.rendering.legend.ILegend;
import org.gvsig.fmap.mapcontext.rendering.legend.IVectorLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.events.listeners.LegendListener;
import org.gvsig.tools.task.Cancellable;

/**
 * Capa de texto.
 *
 * @author FJP
 * @deprecated
 */
public class FLyrText extends FLyrDefault implements ClassifiableVectorial {
    /**
     * <code>m_labels</code> es una arrayList de FLabel (string + punto de
     * inserción + rotación + altura de texto
     */
    private ArrayList m_labels = new ArrayList();

    private IVectorLegend legend;

    // private Rectangle2D fullExtent;
    private FLyrVect assocLyrVect = null;

    /**
     * Crea un nuevo FLyrText.
     *
     * @param arrayLabels
     *            DOCUMENT ME!
     */
    public FLyrText() {
    	super();
    }

//    /**
//     * Esto tiene el fallo de que obligas a una etiqueta por
//     * entidad, para poder evitar esto, una posible solución
//     * sería que un FLabel pudiera ser una colección de FLabel
//     * (Patrón Composite)
//     * @param lyrVect
    // * @throws ReadDriverException
//     */
//    public void createLabels(FLyrVect lyrVect) throws ReadDriverException {
//
//        assocLyrVect = lyrVect;
//        SelectableDataSource ds=null;
//			ds = lyrVect.getRecordset();
//
//        try {
//           	ReadableVectorial adapter = lyrVect.getSource();
//            adapter.start();
//            ds.start();
//            int sc;
//            int fieldId = ds.getFieldIndexByName(legend.getLabelField());
//            IVectorialLegend l = (IVectorialLegend) getLegend();
//            int idFieldHeightText = -1;
//            int idFieldRotationText = -1;
//
////            FSymbol defaultSym = (FSymbol) l.getDefaultSymbol();
////
////            if (l.getLabelHeightField() != null) {
////                idFieldHeightText = ds.getFieldIndexByName(l
////                        .getLabelHeightField());
////                defaultSym.setFontSizeInPixels(false);
////            }
////
////            if (l.getLabelRotationField() != null) {
////                idFieldRotationText = ds.getFieldIndexByName(l
////                        .getLabelRotationField());
////            }
//
//            sc = (int) ds.getRowCount();
//            m_labels = new ArrayList(sc);
//
//
//            DriverAttributes attr = adapter.getDriverAttributes();
//            boolean bMustClone = false;
//            if (attr != null) {
//                if (attr.isLoadedInMemory()) {
//                    bMustClone = attr.isLoadedInMemory();
//                }
//            }
//            ICoordTrans ct = getCoordTrans();
//
//
//            for (int i = 0; i < sc; i++) {
//                IGeometry geom = adapter.getShape(i);
//
//                if (geom == null) {
//                    m_labels.add(null);
//                    continue;
//                }
//                if (ct != null) {
//                    if (bMustClone)
//                        geom = geom.cloneGeometry();
//                    geom.reProject(ct);
//                }
//
//                // TODO: El método contenedor (createLabelLayer) debe recoger
//                // los parámetros de posicionamiento y de allowDuplicates
//                // if (i >= 328)
//                // System.out.println("i= " + i + " " + val.toString());
//                Value val = ds.getFieldValue(i, fieldId);
//
//                if ((val instanceof NullValue) || (val == null)) {
//                    continue;
//                }
//
//                FLabel[] lbls = geom.createLabels(0, true);
//				for (int j = 0; j < lbls.length; j++) {
//					if (lbls[j] != null) {
//						lbls[j].setString(val.toString());
//
//						if (idFieldHeightText != -1) {
//							NumericValue height = (NumericValue) ds.getFieldValue(i,
//									idFieldHeightText);
//							lbls[j].setHeight(height.floatValue());
//						} else {
//                            // El tamaño del texto va siempre en el simbolo por defecto
//                            // cuando no hay un campo de altura de texto
//                            // TODO: Todo esto cambiará con el nuevo sistema de leyendas...
//// jaume							if (l.getDefaultSymbol()!=null)
//// jaume					lbls[j].setHeight(defaultSym.getFontSize());
//						}
//
//						if (idFieldRotationText != -1) {
//							DoubleValue rotation = (DoubleValue) ds.getFieldValue(i,
//									idFieldRotationText);
//							lbls[j].setRotation(rotation.getValue());
//						}
//						m_labels.add(lbls[j]);
//					}
//
//
//				}
//
//                /* if (lbls[0] == null)
//                    m_labels.add(null);
//                else
//                    m_labels.add(lbls[0].getOrig()); */
//            }
//
//            ds.stop();
//            adapter.stop();
//        } catch (ExpansionFileReadException e) {
//        	 throw new ReadDriverException(getName(),e);
//		} catch (InitializeDriverException e) {
//			 throw new ReadDriverException(getName(),e);
//		}
//
//    }

    /**
     * Dibuja sobre el graphics los textos.
     *
     * @param image
     * @param g
     *            Graphics.
     * @param viewPort
     *            ViewPort.
     * @param cancel
     */
//    private void drawLabels(BufferedImage image, Graphics2D g,
//            ViewPort viewPort, Cancellable cancel) {
//        int numReg;
//        /*
//         * refactored... labels will be managed by the LabelingStrategy
//        Rectangle2D elExtent = viewPort.getAdjustedExtent();
//
//        // int anchoMapa;
//        // int altoMapa;
//        // double anchoReal;
//        // double altoReal;
//        // double escala;
//        ISymbol theSymbol = null;
//        System.out.println("Dibujando etiquetas...");
//
//        for (numReg = 0; numReg < m_labels.size(); numReg++) {
//            if (cancel.isCanceled()) {
//                break;
//            }
//
//            FLabel theLabel = (FLabel) m_labels.get(numReg);
//            if ((theLabel == null) || (theLabel.getOrig() == null))
//                continue;
//
//            if (elExtent.contains(theLabel.getOrig())) // TODO: Aqui hay que
//            // ponerle al FLabel un
//            // getExtent()
//            {
//                theSymbol = getLegend().getDefaultSymbol();
//
//                FShape shp = new FPoint2D(theLabel.getOrig().getX(), theLabel
//                        .getOrig().getY());
//
//                theLabel.draw(g, viewPort.getAffineTransform(), shp, theSymbol);
//                // FGraphicUtilities.DrawLabel(g, viewPort.getAffineTransform(),
//                //         shp, theSymbol, theLabel);
//            }
//        }
//        */
//    }

//    /**
//     * @deprecated use drawlabels (compatibility with postgis)
//     * @param image
//     * @param g
//     * @param viewPort
//     * @param cancel
    // * @throws ReadDriverException
//     */
//    private void drawLabels2(BufferedImage image, Graphics2D g,
//            ViewPort viewPort, Cancellable cancel) throws ReadDriverException {
//        int numReg;
//        Rectangle2D elExtent = viewPort.getAdjustedExtent();
//
//        // int anchoMapa;
//        // int altoMapa;
//        // double anchoReal;
//        // double altoReal;
//        // double escala;
//        FSymbol theSymbol = null;
//        System.out.println("Dibujando etiquetas...");
//        FLabel theLabel = new FLabel();
//
//        SelectableDataSource ds=null;
//			ds = assocLyrVect.getRecordset();
//	        int fieldId = ds.getFieldIndexByName(legend.getLabelField());
//            IVectorialLegend l = (IVectorialLegend) getLegend();
//            int idFieldHeightText = -1;
//            int idFieldRotationText = -1;
//            FSymbol defaultSym = (FSymbol) l.getDefaultSymbol();
//
//            if (l.getLabelHeightField() != null) {
//                // l.setBWithHeightText(true);
//                idFieldHeightText = ds.getFieldIndexByName(l
//                        .getLabelHeightField());
//                defaultSym.setFontSizeInPixels(false);
//            }
//            /*
//             * }else{ l.setBWithHeightText(false); }
//             */
//
//            // boolean bWithRotationText = false;
//            if (l.getLabelRotationField() != null) {
//                // bWithRotationText = true;
//                idFieldRotationText = ds.getFieldIndexByName(l
//                        .getLabelRotationField());
//            }
//
//            for (numReg = 0; numReg < m_labels.size(); numReg++) {
//                if (cancel.isCanceled()) {
//                    break;
//                }
//
//                Point2D thePoint = (Point2D) m_labels.get(numReg);
//
//
//
//                if (thePoint == null)
//                    continue;
//                FPoint2D theShape = new FPoint2D(thePoint);
//                if (getCoordTrans() != null)
//                    theShape.reProject(this.getCoordTrans());
//
//                if (elExtent.contains(theShape.getX(), theShape.getY())) // TODO: Aqui hay que
//                // ponerle al FLabel un
//                // getExtent()
//                {
//                    Value val = ds.getFieldValue(numReg, fieldId);
//
//                    if ((val instanceof NullValue) || (val == null)) {
//                        continue;
//                    }
//
//                    theSymbol = defaultSym;
//                    theLabel.setString(val.toString());
//                    theLabel.setOrig(thePoint);
//
//                    if (idFieldHeightText != -1) {
//                        NumericValue height = (NumericValue) ds
//                                .getFieldValue(numReg, idFieldHeightText);
//                        theLabel.setHeight(height.floatValue());
//                    } else {
//                        // El tamaño del texto va siempre en el simbolo por
//                        // defecto
//                        // cuando no hay un campo de altura de texto
//                        // TODO: Todo esto cambiará con el nuevo sistema de
//                        // leyendas...
//                        if (defaultSym != null)
//                            theLabel.setHeight(defaultSym.getFontSize());
//                    }
//
//                    if (idFieldRotationText != -1) {
//                        DoubleValue rotation = (DoubleValue) ds
//                                .getFieldValue(numReg, idFieldRotationText);
//                        theLabel.setRotation(rotation.getValue());
//                    }
//                    theLabel.draw(g, viewPort.getAffineTransform(), theShape, theSymbol);
//                }
//            }
//
//
//    }

    /*
     * (non-Javadoc)
     *
     * @see com.iver.cit.gvsig.fmap.layers.FLayer#getFullExtent()
     */
    public Envelope getFullEnvelope() throws ReadException {
        return assocLyrVect.getFullEnvelope();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.iver.cit.gvsig.fmap.layers.FLayer#draw(java.awt.image.BufferedImage,
     *      java.awt.Graphics2D, com.iver.cit.gvsig.fmap.ViewPort,
     *      com.iver.cit.gvsig.fmap.operations.Cancellable)
     */
    public void draw(BufferedImage image, Graphics2D g, ViewPort viewPort,
            Cancellable cancel, double scale) throws ReadException {
//        if (isVisible() && isWithinScale(scale)) {
//            drawLabels(image, g, viewPort, cancel);
//        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.iver.cit.gvsig.fmap.layers.FLayer#print(java.awt.Graphics2D,
     *      com.iver.cit.gvsig.fmap.ViewPort,
     *      com.iver.cit.gvsig.fmap.operations.Cancellable)
     */
    public void print(Graphics2D g, ViewPort viewPort, Cancellable cancel,
            double scale, PrintAttributes properties) throws ReadException {
//        if (isVisible() && isWithinScale(scale)) {
//            drawLabels(null, g, viewPort, cancel);
//        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.iver.cit.gvsig.fmap.layers.layerOperations.ClassifiableVectorial#setLegend(com.iver.cit.gvsig.fmap.rendering.VectorialLegend)
     */
    public void setLegend(IVectorLegend r) {
        legend = r;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.iver.cit.gvsig.fmap.layers.layerOperations.Classifiable#addLegendListener(com.iver.cit.gvsig.fmap.layers.LegendListener)
     */
    public void addLegendListener(LegendListener listener) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see com.iver.cit.gvsig.fmap.layers.layerOperations.Classifiable#removeLegendListener(com.iver.cit.gvsig.fmap.layers.LegendListener)
     */
    public void removeLegendListener(LegendListener listener) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     *
     * @see com.iver.cit.gvsig.fmap.layers.layerOperations.Classifiable#getLegend()
     */
    public ILegend getLegend() {
        return legend;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.iver.cit.gvsig.fmap.layers.layerOperations.Classifiable#getShapeType()
     */
    public int getShapeType() {
        return Geometry.TYPES.TEXT;
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gvsig.metadata.Metadata#getMetadataChildren()
	 */
	public Set getMetadataChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gvsig.metadata.Metadata#getMetadataID()
	 */
	public Object getMetadataID() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gvsig.metadata.Metadata#getMetadataName()
	 */
	public String getMetadataName() {
		// TODO Auto-generated method stub
		return null;
	}

    /**
     * Devuelve un ArrayList con los textos de la capa.
     *
     * @return Texto de la capa.
     */
    /* public ArrayList getLabels() {
        return m_labels;
    } */

    /**
     * Inserta los textos de la capa
     *
     * @param m_labels
     *            ArrayList con los textos de la capa.
     */
    /* public void setLabels(ArrayList m_labels) {
        this.m_labels = m_labels;
    } */
}
