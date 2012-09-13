/*
 * Created on 08-nov-2005
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
package com.iver.gvsig.centerviewtopoint.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.prefs.Preferences;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.GraphicLayer;
import org.gvsig.fmap.mapcontext.rendering.legend.FGraphic;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.Events.PointEvent;
import org.gvsig.gui.beans.AcceptCancelPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.gui.panels.ColorChooserPanel;
import com.iver.cit.gvsig.project.documents.view.toolListeners.InfoListener;
import com.iver.gvsig.centerviewpoint.CenterViewToPointExtension;

/**
 * The InputCoordinatesPanel class creates a JPanel where the
 * user can input the coordinates of the point of reference
 * for center the View.
 *
 * @author jmorell
 */
public class InputCoordinatesPanel extends JPanel implements IWindow {
    private static final long serialVersionUID = 1L;
    private JLabel labelX = null;
    private JTextField textX = null;
    private JLabel labelY = null;
    private JTextField textY = null;
    private MapControl mapControl;
    private WindowInfo viewInfo = null;
    private String firstCoordinate;
    private String secondCoordinate;
    private Point2D center;
    private GraphicLayer lyr;
    private ColorChooserPanel colorPanel;
	private AcceptCancelPanel okCancelPanel = null;
	protected static Logger logger = Logger.getLogger(InputCoordinatesPanel.class
			.getName());
	/**
     * This is the default constructor
     */
    public InputCoordinatesPanel(MapContext mapContext) {
        super();
        this.mapControl = new MapControl();
        mapControl.setMapContext(mapContext);
        lyr=mapControl.getMapContext().getGraphicsLayer();
        initializeCoordinates();
        initialize();
    }

    /**
     * Sets the proper text for the first and second coordinate labels,
     * depending on the kind of selected projection.
     *
     */
    private void initializeCoordinates() {
        IProjection proj = mapControl.getProjection();
        if (proj.isProjected()) {
            firstCoordinate = "X";
            secondCoordinate = "Y";
        } else {
            firstCoordinate = "Lon";
            secondCoordinate = "Lat";
        }
    }

    /**
     * Move the view's extent so that the specified point gets
     * centered.
     *
     * @throws Exception
     */
    private void zoomToCoordinates() throws Exception {
       try{
    	Envelope oldExtent = mapControl.getViewPort().getAdjustedExtent();
        double oldCenterX = oldExtent.getCenter(0);
        double oldCenterY = oldExtent.getCenter(1);
        double centerX = (new Double((String)textX.getText())).doubleValue();
        double centerY = (new Double((String)textY.getText())).doubleValue();
        center=new Point2D.Double(centerX,centerY);
        double movX = centerX-oldCenterX;
        double movY = centerY-oldCenterY;
        double upperLeftCornerX = oldExtent.getMinimum(0)+movX;
        double upperLeftCornerY = oldExtent.getMinimum(1)+movY;
        double maxX = oldExtent.getMaximum(0);
        double maxY = oldExtent.getMaximum(1);
        Envelope extent = GeometryLocator.getGeometryManager().createEnvelope(upperLeftCornerX, upperLeftCornerY, maxX, maxY, SUBTYPES.GEOM2D);
        mapControl.getViewPort().setEnvelope(extent);
       }catch (NumberFormatException e) {
    	   throw new Exception();
       }

    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        labelY = new JLabel();
        labelY.setBounds(10, 35, 28, 20);
        labelY.setText(secondCoordinate + ":");
        labelX = new JLabel();
        labelX.setBounds(10, 10, 28, 20);
        labelX.setText(firstCoordinate + ":");
        this.setLayout(null);
        this.setSize(307, 100);
        this.add(labelX, null);
        this.add(getTextX(), null);
        this.add(labelY, null);
        this.add(getTextY(), null);
        this.add(getColorPanel());
        this.add(getOkCancelPanel(), null);
    }

    /**
     * This method initializes textX
     *
     * @return javax.swing.JTextField
     */
    private JTextField getTextX() {
    	if (textX == null) {
    		textX = new JTextField();
    		textX.setBounds(40, 10, 260, 20);
    		textX.addActionListener(new java.awt.event.ActionListener() {
    			public void actionPerformed(java.awt.event.ActionEvent e) {
    				textX.transferFocus();
    			}
    		});
    	}
    	return textX;
    }

    /**
     * This method initializes textY
     *
     * @return javax.swing.JTextField
     */
    private JTextField getTextY() {
    	if (textY == null) {
    		textY = new JTextField();
    		textY.setBounds(40, 35, 260, 20);
    		textY.addActionListener(new java.awt.event.ActionListener() {
    			public void actionPerformed(java.awt.event.ActionEvent e) {
    				textY.transferFocus();
    			}
    		});
    	}
    	return textY;
    }

    /* (non-Javadoc)
     * @see com.iver.andami.ui.mdiManager.View#getViewInfo()
     */
    public WindowInfo getWindowInfo() {
        // TODO Auto-generated method stub
        if (viewInfo == null) {
            viewInfo=new WindowInfo(WindowInfo.MODALDIALOG);
            viewInfo.setTitle(PluginServices.getText(this,"Centrar_la_Vista_sobre_un_punto"));
            viewInfo.setWidth(this.getWidth()+8);
            viewInfo.setHeight(this.getHeight());
        }
        return viewInfo;
    }
    /**
     * Opens the infoByPoint dialog for the selected point.
     *
     */
    private void openInfo(){
    	InfoListener infoListener=new InfoListener(mapControl);
    	MouseEvent e=new MouseEvent((Component)(((CenterViewToPointExtension)PluginServices.getExtension(CenterViewToPointExtension.class)).getView()),MouseEvent.BUTTON1,MouseEvent.ACTION_EVENT_MASK,MouseEvent.MOUSE_CLICKED,500,400,1,true);
    	Point2D centerPixels=mapControl.getViewPort().fromMapPoint(center.getX(),center.getY());
    	PointEvent pe=new PointEvent(centerPixels,e);
    	try {
			infoListener.point(pe);
		} catch (org.gvsig.fmap.mapcontrol.tools.BehaviorException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (mapControl.getMapContext().getLayers().getActives().length==0){
			JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),PluginServices.getText(this,"no_hay_ninguna_capa_seleccionada")+" \n"+
					PluginServices.getText(this,"debe_seleccionar_las_capas_de_las_que_quiera_obtener_informacion"));
		}
    }

    /**
     * Draws the selected point on the view.
     *
     * @param color
     */
    private void drawPoint(Color color){
    	CenterViewToPointExtension.COLOR=color;
    	lyr.clearAllGraphics();
    	org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol theSymbol = SymbologyFactory.createDefaultSymbolByShapeType(Geometry.TYPES.POINT,color);
        int idSymbol = lyr.addSymbol(theSymbol);
        GeometryManager geomManager = GeometryLocator.getGeometryManager();
//        org.gvsig.fmap.geom.Geometry geom = geomManager.create(center.getX(),center.getY());
        Point geom = null;
		try {
			geom = (Point)geomManager.create(TYPES.POINT, SUBTYPES.GEOM2D);
			geom.setX(center.getX());
			geom.setY(center.getY());
		} catch (org.gvsig.fmap.geom.exception.CreateGeometryException e) {
			logger.error(e);
		}
        FGraphic theGraphic = new FGraphic(geom, idSymbol);
        lyr.addGraphic(theGraphic);
        mapControl.drawGraphics();

    }


	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getColorPanel() {
		if (colorPanel==null){
		 	colorPanel=new ColorChooserPanel();
		 	colorPanel.setAlpha(250);
		 	colorPanel.setColor(CenterViewToPointExtension.COLOR);
		 	colorPanel.setBounds(new java.awt.Rectangle(40,59,123,24));
		}
		 	return colorPanel;
	}

	/**
	 * This method initializes okCancelPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private AcceptCancelPanel getOkCancelPanel() {
		if (okCancelPanel == null) {
			ActionListener okAction, cancelAction;
			okAction = new java.awt.event.ActionListener() {
    			public void actionPerformed(java.awt.event.ActionEvent e) {
    				try{
    				zoomToCoordinates();
    				}catch (Exception e1) {
    					JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),PluginServices.getText(this,"formato_de_numero_incorrecto"));
    					return;
    				}
    				// y sale.
                    if (PluginServices.getMainFrame() == null)
                        ((JDialog) (getParent().getParent().getParent().getParent())).dispose();
                    else
                        PluginServices.getMDIManager().closeWindow(InputCoordinatesPanel.this);
                    Preferences prefs = Preferences.userRoot().node( "gvsig.centerViewToPoint" );
                    if( prefs.get("showInfo", "True").equalsIgnoreCase("True")){
                    	openInfo();
                    }
                    drawPoint(((ColorChooserPanel)getColorPanel()).getColor());
    			}
    		};
    		cancelAction = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					closeThis();
				}
    		};
			okCancelPanel = new AcceptCancelPanel(okAction, cancelAction);
			okCancelPanel.setBounds(new java.awt.Rectangle(40, 88, 260, 30));
		}
		return okCancelPanel;
	}

	/**
	 * Close the window.
	 *
	 */
	private void closeThis() {
		PluginServices.getMDIManager().closeWindow(this);

	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}


}  //  @jve:decl-index=0:visual-constraint="103,18"
