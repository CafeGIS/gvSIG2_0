/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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
package com.iver.cit.gvsig.project.documents.view.toolListeners;

import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.operations.InfoByPoint;
import org.gvsig.fmap.mapcontext.layers.operations.VectorialXMLItem;
import org.gvsig.fmap.mapcontext.layers.operations.XMLItem;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Events.PointEvent;
import org.gvsig.fmap.mapcontrol.tools.Listeners.PointListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.project.documents.view.info.gui.FInfoDialog;
import com.iver.cit.gvsig.project.documents.view.info.gui.FInfoDialogXML;
import com.iver.utiles.xmlViewer.XMLContent;

/**
 * <p>Listener that looks for alphanumeric information at the point selected by one click of any mouse's button,
 *   in the active layers of the associated <code>MapControl</code>, and displays that alphanumeric data on a
 *   {@link FInfoDialog FInfoDialog} dialog.</p>
 *
 * @author Vicente Caballero Navarro
 */
public class InfoListener implements PointListener {
	/**
	 * Object used to log messages for this listener.
	 */
	private static final Logger logger = LoggerFactory
            .getLogger(InfoListener.class);

	/**
	 * The image to display when the cursor is active.
	 */
	private final Image img = PluginServices.getIconTheme().get("cursor-query-information").getImage();

	/**
	 * The cursor used to work with this tool listener.
	 *
	 * @see #getCursor()
	 */
//	private Cursor cur = Toolkit.getDefaultToolkit().createCustomCursor(img,
//			new Point(16, 16), "");

	/**
	 * Reference to the <code>MapControl</code> object that uses.
	 */
	private MapControl mapCtrl;

	/**
	 * Radius as tolerance around the selected point, the area will be used to look for information.
	 */
	private static int TOL=7;

	/**
	 * <p>Creates a new <code>InfoListener</code> object.</p>
	 *
	 * @param mc the <code>MapControl</code> where will be applied the changes
	 */
	public InfoListener(MapControl mc) {
		this.mapCtrl = mc;
	}

	/**
	 * When user clicks on the associated <code>MapControl</code>'s view, the point is caught and handled by this method,
	 *  which will look for alphanumeric information in features at that position in the active layers.
	 *
	 * @param event mouse event with the coordinates of the point pressed
	 *
	 * @throws BehaviorException will be thrown when fails this process
	 * @deprecated
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.PointListener#point(org.gvsig.fmap.mapcontrol.tools.Events.PointEvent)
	 */
	public void point_(PointEvent event) throws BehaviorException {

		Point2D pReal = mapCtrl.getMapContext().getViewPort().toMapPoint(
				event.getPoint());
		Point imagePoint = new Point((int) event.getPoint().getX(), (int) event
				.getPoint().getY());

		FInfoDialogXML dlgXML = new FInfoDialogXML();
		int numLayersInfoable = 0;
		double tol = mapCtrl.getViewPort().toMapDistance(3);

		FLayer[] sel = mapCtrl.getMapContext().getLayers().getActives();
		final XMLItem[] items = new XMLItem[sel.length];

		for (int i = 0; i < sel.length; i++) {
			FLayer laCapa = sel[i];

            if (laCapa instanceof FLyrVect)
            {
                FLyrVect lyrVect = (FLyrVect) laCapa;
                FeatureSet newSelection = null;
//                try {
                    try {
						newSelection = lyrVect.queryByPoint(pReal, tol,lyrVect.getFeatureStore().getDefaultFeatureType());
					} catch (DataException e) {
						throw new BehaviorException(laCapa.getName(),e);
					}
                    items[i] = new VectorialXMLItem(newSelection, laCapa);
                    numLayersInfoable++;
//                } catch (ReadException e) {
//                    e.printStackTrace();
//                    throw new BehaviorException("Fallo al consultar " + lyrVect.getName());
//                }

			}
			// TODO: PROVISIONAL PARA LA CAPA WMS
/*
            else if (laCapa instanceof RasterOperations) {
				RasterOperations layer = (RasterOperations) laCapa;
				String text;
				try {

					ArrayList attr = ((RasterOperations) laCapa)
							.getAttributes();
					int anchoRaster = 0;
					int altoRaster = 0;

					for (int j = 0; j < attr.size(); j++) {
						Object[] a = (Object[]) attr.get(j);
						if (a[0].toString().equals("Width"))
							anchoRaster = Integer.parseInt(a[1].toString());
						if (a[0].toString().equals("Height"))
							altoRaster = Integer.parseInt(a[1].toString());
					}

					double xwc = ((RasterOperations) laCapa).getMaxX()
							- ((RasterOperations) laCapa).getMinX();//((FLyrDefault)laCapa).getFullExtent().getMaxX()-((FLyrRaster)laCapa).getFullExtent().getMinX();
					double ywc = ((RasterOperations) laCapa).getMaxY()
							- ((RasterOperations) laCapa).getMinY();//((FLyrDefault)laCapa).getFullExtent().getMaxY()-((FLyrRaster)laCapa).getFullExtent().getMinY();
					double ancho = ((RasterOperations) laCapa).getWidth();//((FLyrDefault)laCapa).getFullExtent().getWidth();
					double alto = ((RasterOperations) laCapa).getHeight();//((FLyrDefault)laCapa).getFullExtent().getHeight();

					//ptoX y ptoY son el pixel de la imagen donde se ha
					// pinchado a escala 1:1
					int ptoX = (int) (((pReal.getX() - ((RasterOperations) laCapa)
							.getMinX()) * anchoRaster) / xwc);//(int)(((pReal.getX()-((FLyrDefault)laCapa).getFullExtent().getMinX())*anchoRaster)/xwc);
					int ptoY = (int) (((pReal.getY() - ((RasterOperations) laCapa)
							.getMinY()) * altoRaster) / ywc);//(int)(((pReal.getY()-((FLyrDefault)laCapa).getFullExtent().getMinY())*altoRaster)/ywc);
					((RasterOperations) laCapa).setPos(ptoX, ptoY);
					ViewPort v = mapCtrl.getMapContext().getViewPort();

					int[] px = ((RasterOperations) laCapa).getPixel(pReal
							.getX(), pReal.getY());

					if (px != null)
						((RasterOperations) laCapa).setRGB(px[1], px[2], px[3]);
					((RasterOperations) laCapa).setPosWC(pReal.getX(), pReal
							.getY());

//					text = layer.getInfo(imagePoint, tol);
//					items[i] = new StringXMLItem(text);
					items[i] =  layer.getInfo(imagePoint, tol, null)[0];
					numLayersInfoable++;

				} catch (ReadDriverException e) {
					throw new BehaviorException("No se pudo procesar la capa",
							e);
				} catch (VisitorException e) {
					throw new BehaviorException("No se pudo procesar la capa",
							e);
				} catch (LoadLayerException e) {
					throw new BehaviorException("No se pudo procesar la capa",
							e);
				}
			}
*/
			else if (laCapa instanceof InfoByPoint) {
				// TODO Hecho para el WMS. No deberia hacer falta
				String text;
				try {
					InfoByPoint layer = (InfoByPoint) laCapa;
//					text = layer.getInfo(imagePoint, tol);
//					items[i] = new StringXMLItem(text);
					items[i] = layer.getInfo(imagePoint, tol, null)[0];
					numLayersInfoable++;
				} catch (DataException e) {
					throw new BehaviorException("No se pudo procesar la capa",
							e);
				} catch (LoadLayerException e) {
					throw new BehaviorException("No se pudo procesar la capa",
							e);
				}
			}
		}

		if (numLayersInfoable > 0) {
			try {
				if (PluginServices.getMainFrame() == null) {
					JDialog dialog = new JDialog();
					dlgXML.setPreferredSize(dlgXML.getSize());
					dialog.getContentPane().add(dlgXML);
					dialog.setModal(false);
					dialog.pack();
					dialog.show();

				} else {
					dlgXML = (FInfoDialogXML) PluginServices.getMDIManager()
							.addWindow(dlgXML);
				}

				dlgXML.setModel(new XMLContent() {
					private ContentHandler handler;

					public void setContentHandler(ContentHandler arg0) {
						handler = arg0;
					}

					public void parse() throws SAXException {
						handler.startDocument();

						for (int i = 0; i < items.length; i++) {
							items[i].parse(handler);
						}

						handler.endDocument();
					}
				});
				dlgXML.getXmlTree().setRootVisible(false);
				DefaultTreeModel treeModel = (DefaultTreeModel) dlgXML
						.getXmlTree().getModel();
				DefaultMutableTreeNode n;
				DefaultMutableTreeNode root = (DefaultMutableTreeNode) dlgXML
						.getXmlTree().getModel().getRoot();
				n = root.getFirstLeaf();
				TreePath path = new TreePath(treeModel.getPathToRoot(n));
				dlgXML.getXmlTree().expandPath(path);

				dlgXML.getXmlTree().setSelectionPath(path);

			} catch (SAXException e) {
				NotificationManager.addError(
						"Error formateando los resultados", e);
			}
		}
	}

	/**
	 * When user clicks on the associated <code>MapControl</code>'s view, the point is caught and handled by this method, which will look
	 * for alphanumeric information in features at that position in the active layers.
	 *
	 * @param event mouse event with the coordinates of the point pressed
	 *
	 * @throws BehaviorException will be thrown when fails this process
	 * @deprecated
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.PointListener#point(org.gvsig.fmap.mapcontrol.tools.Events.PointEvent)
	 */
	public void point2(PointEvent event) throws BehaviorException {

		Point imagePoint = new Point((int) event.getPoint().getX(), (int) event
				.getPoint().getY());

		FInfoDialogXML dlgXML = new FInfoDialogXML();
		int numLayersInfoable = 0;
		double tol = mapCtrl.getViewPort().toMapDistance(3);

		FLayer[] sel = mapCtrl.getMapContext().getLayers().getActives();
		Vector itemsVector = new Vector();
		XMLItem[] aux;

		for (int i = 0; i < sel.length; i++) {
			FLayer laCapa = sel[i];
			if (laCapa instanceof InfoByPoint) {
				try {
					InfoByPoint layer = (InfoByPoint) laCapa;
					aux = layer.getInfo(imagePoint, tol, null);
					for(int j = 0; j < aux.length; j++){
						itemsVector.add(aux[j]);
						numLayersInfoable++;
					}
				} catch (DataException e) {
					throw new BehaviorException("Processing layer",e);
				} catch (LoadLayerException e) {
					throw new BehaviorException("No se pudo procesar la capa",
							e);
				}
			}
		}
		final XMLItem[] items = (XMLItem[])itemsVector.toArray(new XMLItem[0]);

		if (numLayersInfoable > 0) {
			try {
				if (PluginServices.getMainFrame() == null) {
					JDialog dialog = new JDialog();
					dlgXML.setPreferredSize(dlgXML.getSize());
					dialog.getContentPane().add(dlgXML);
					dialog.setModal(false);
					dialog.pack();
					dialog.show();

				} else {
					dlgXML = (FInfoDialogXML) PluginServices.getMDIManager()
							.addWindow(dlgXML);
				}

				dlgXML.setModel(new XMLContent() {
					private ContentHandler handler;

					public void setContentHandler(ContentHandler arg0) {
						handler = arg0;
					}

					public void parse() throws SAXException {
						handler.startDocument();

						for (int i = 0; i < items.length; i++) {
							items[i].parse(handler);
						}

						handler.endDocument();
					}
				});
				dlgXML.getXmlTree().setRootVisible(false);
				DefaultTreeModel treeModel = (DefaultTreeModel) dlgXML
						.getXmlTree().getModel();
				DefaultMutableTreeNode n;
				DefaultMutableTreeNode root = (DefaultMutableTreeNode) dlgXML
						.getXmlTree().getModel().getRoot();
				n = root.getFirstLeaf();
				TreePath path = new TreePath(treeModel.getPathToRoot(n));
				dlgXML.getXmlTree().expandPath(path);

				dlgXML.getXmlTree().setSelectionPath(path);

			} catch (SAXException e) {
				NotificationManager.addError(
						"Error formateando los resultados", e);
			}
		}
	}

	/*
	 * (To use the old info tool, use again the point2 method!)
	 *
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.PointListener#point(com.iver.cit.gvsig.fmap.tools.Events.PointEvent)
	 */
	public void point(PointEvent event) throws BehaviorException {

		Point imagePoint = new Point((int) event.getPoint().getX(), (int) event
				.getPoint().getY());

		int numLayersInfoable = 0;
		double tol = mapCtrl.getViewPort().toMapDistance(TOL);

		FLayer[] sel = mapCtrl.getMapContext().getLayers().getActives();
		Vector itemsVector = new Vector();
		XMLItem[] aux;

		for (int i = 0; i < sel.length; i++) {
			FLayer laCapa = sel[i];
			if (laCapa instanceof InfoByPoint) {
				try {
					InfoByPoint layer = (InfoByPoint) laCapa;
					if (!(laCapa.getParentLayer().isActive())){
						aux = layer.getInfo(imagePoint, tol, null);
						for(int j = 0; j < aux.length; j++){
							itemsVector.add(aux[j]);
							numLayersInfoable++;
						}
					}
				} catch (DataException e) {
					throw new BehaviorException("Processing layer",e);
				} catch (LoadLayerException e) {
					throw new BehaviorException("No se pudo procesar la capa",
							e);
				}
			}
		}
		final XMLItem[] items = (XMLItem[])itemsVector.toArray(new XMLItem[0]);
		FInfoDialog dlgXML = new FInfoDialog();

		if (numLayersInfoable > 0) {
			try {
				if (PluginServices.getMainFrame() == null) {
					JDialog dialog = new JDialog();
					dlgXML.setPreferredSize(dlgXML.getSize());
					dialog.getContentPane().add(dlgXML);
					dialog.setModal(false);
					dialog.pack();
					dialog.show();

				} else {
					dlgXML = (FInfoDialog) PluginServices.getMDIManager()
							.addWindow(dlgXML);
				}

			} catch (Exception e) {
				NotificationManager.addError("FeatureInfo", e);
				e.printStackTrace();
			}
			dlgXML.setLayers(items);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#getImageCursor()
	 */
	public Image getImageCursor() {
		return img;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#cancelDrawing()
	 */
	public boolean cancelDrawing() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.PointListener#pointDoubleClick(com.iver.cit.gvsig.fmap.tools.Events.PointEvent)
	 */
	public void pointDoubleClick(PointEvent event) throws BehaviorException {
		// TODO Auto-generated method stub

	}
}