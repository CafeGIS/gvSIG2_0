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
package com.iver.cit.gvsig.gui.cad.tools;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.InputEvent;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.aggregate.impl.BaseMultiPrimitive;
import org.gvsig.fmap.geom.handler.Handler;
import org.gvsig.fmap.geom.operation.DrawInts;
import org.gvsig.fmap.geom.operation.DrawOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.util.Converter;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.rendering.symbols.FGraphicUtilities;
import org.gvsig.fmap.mapcontrol.MapControl;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.gui.cad.DefaultCADTool;
import com.iver.cit.gvsig.gui.cad.exception.CommandException;
import com.iver.cit.gvsig.gui.cad.tools.smc.EditVertexCADToolContext;
import com.iver.cit.gvsig.gui.cad.tools.smc.EditVertexCADToolContext.EditVertexCADToolState;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;


/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class EditVertexCADTool extends DefaultCADTool {
    protected EditVertexCADToolContext _fsm;
    protected int numSelect=0;
    protected int numHandlers;
    protected boolean addVertex=false;

    /**
     * Crea un nuevo PolylineCADTool.
     */
    public EditVertexCADTool() {
    }

    /**
     * M�todo de incio, para poner el c�digo de todo lo que se requiera de una
     * carga previa a la utilizaci�n de la herramienta.
     */
    public void init() {
        _fsm = new EditVertexCADToolContext(this);
    }

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.CADTool#transition(com.iver.cit.gvsig.fmap.layers.FBitSet, double, double)
     */
    public void transition(double x, double y, InputEvent event) {
        _fsm.addPoint(x, y, event);
    }

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.CADTool#transition(com.iver.cit.gvsig.fmap.layers.FBitSet, double)
     */
    public void transition(double d) {
    	_fsm.addValue(d);
    }

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.CADTool#transition(com.iver.cit.gvsig.fmap.layers.FBitSet, java.lang.String)
     */
    public void transition(String s) throws CommandException {
    	if (!super.changeCommand(s)){
    		_fsm.addOption(s);
    	}
    }

    /**
     * DOCUMENT ME!
     */
    public void selection() {
    	FeatureSet selection=null;
    	try {
    		selection = (FeatureSet)getVLE().getFeatureStore().getSelection();

    		if (selection.getSize() == 0 && !CADExtension.getCADTool().getClass().getName().equals("com.iver.cit.gvsig.gui.cad.tools.SelectionCADTool")) {
    			CADExtension.setCADTool("_selection",false);
    			((SelectionCADTool) CADExtension.getCADTool()).setNextTool(
    			"_editvertex");
    		}
    	} catch (ReadException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * Equivale al transition del prototipo pero sin pasarle como par�metro el
     * editableFeatureSource que ya estar� creado.
     *
     * @param x par�metro x del punto que se pase en esta transici�n.
     * @param y par�metro y del punto que se pase en esta transici�n.
     */
    public void addPoint(double x, double y,InputEvent event) {
    	selectHandler(x,y);
    	addVertex=false;
    }

    private Geometry getSelectedGeometry() {
    	FeatureSet selection=null;
    	try {
    		selection = (FeatureSet)getVLE().getFeatureStore().getSelection();

    		Feature feature=null;
    		Geometry ig=null;
    		if (selection.getSize()==1){
    			feature=(Feature)selection.iterator().next();
    			ig=(feature.getDefaultGeometry()).cloneGeometry();
    			return ig;
    		}
    	} catch (ReadException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	return null;
    }

	/**
     * M�todo para dibujar la lo necesario para el estado en el que nos
     * encontremos.
     *
     * @param g Graphics sobre el que dibujar.
     * @param x par�metro x del punto que se pase para dibujar.
     * @param y par�metro x del punto que se pase para dibujar.
     */
    public void drawOperation(Graphics g, double x, double y) {
        drawVertex(g,getCadToolAdapter().getMapControl().getViewPort());
    }

    /**
     * Add a diferent option.
     *
     * @param s Diferent option.
     */
    public void addOption(String s) {
    	EditVertexCADToolState actualState = (EditVertexCADToolState) _fsm.getPreviousState();
        String status = actualState.getName();
        VectorialLayerEdited vle=getVLE();
        FeatureStore featureStore=null;
		try {
			featureStore = vle.getFeatureStore();

        FeatureSet selection=(FeatureSet)featureStore.getSelection();
        Feature feature=null;
        Geometry ig=null;
        Handler[] handlers=null;
        if (selection.getSize()==1){
				feature =  (Feature)selection.iterator().next();
			ig=(feature.getDefaultGeometry()).cloneGeometry();
        	handlers=ig.getHandlers(Geometry.SELECTHANDLER);
        	numHandlers=handlers.length;
        	if (numHandlers ==0){
        		try {
        			featureStore.delete(feature);
				} catch (ReadException e) {
					NotificationManager.addError(e.getMessage(),e);
				} catch (DataException e) {
					NotificationManager.addError(e.getMessage(),e);
				}
        	}
        }else{
        	JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),PluginServices.getText(this,"hay_mas_de_una_geometria_seleccionada"));
        }

        int dif=1;//En el caso de ser pol�gono.
        if (ig instanceof BaseMultiPrimitive){
        	dif=2;
        }

        if (status.equals("EditVertex.SelectVertexOrDelete")){
        	if(s.equalsIgnoreCase(PluginServices.getText(this,"EditVertexCADTool.nextvertex")) || s.equals(PluginServices.getText(this,"next"))){
        		numSelect=numSelect-dif;
        		if (numSelect<0){
        			numSelect=numHandlers-1+(numSelect+1);
        		}
           }else if(s.equalsIgnoreCase(PluginServices.getText(this,"EditVertexCADTool.previousvertex")) || s.equals(PluginServices.getText(this,"previous"))){
        	   	numSelect=numSelect+dif;
       			if (numSelect>(numHandlers-1)){
       				numSelect=numSelect-(numHandlers);
       			}

        	}else if(s.equalsIgnoreCase(PluginServices.getText(this,"EditVertexCADTool.delvertex")) || s.equals(PluginServices.getText(this,"del"))){
        		if (handlers!=null){
        			Geometry newGeometry=null;
        			if (ig instanceof BaseMultiPrimitive) {
        				newGeometry=removeVertexGC((BaseMultiPrimitive)ig,handlers[numSelect]);
        			}else {
        				newGeometry=removeVertex(ig,handlers,numSelect);
        			}
        			try {
        				EditableFeature eFeature=feature.getEditable();
        				eFeature.setGeometry(featureStore.getDefaultFeatureType().getDefaultGeometryAttributeName(),newGeometry);
        				featureStore.update(eFeature);

        			} catch (ReadException e) {
        				NotificationManager.addError(e.getMessage(),e);
        			}
        			catch (DataException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
        		}
        	}else if(s.equalsIgnoreCase(PluginServices.getText(this,"EditVertexCADTool.addvertex")) || s.equals(PluginServices.getText(this,"add"))){
        		addVertex=true;
        	}
        }
		} catch (ReadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    private void drawVertex(Graphics g,ViewPort vp){
    	VectorialLayerEdited vle=getVLE();
    	DisposableIterator iterator = null;
    	try {
			iterator = ((FeatureSelection) vle.getFeatureStore().getSelection())
					.iterator();
			while (iterator.hasNext()) {
				Feature feature = (Feature) iterator.next();

    			Geometry ig = (feature.getDefaultGeometry()).cloneGeometry();
				if (ig == null) {
					continue;
				}
				DrawOperationContext doc = new DrawOperationContext();
				doc.setGraphics((Graphics2D) g);
				doc.setViewPort(vp);
				doc.setSymbol(DefaultCADTool.geometrySelectSymbol);
				try {
					ig.invokeOperation(DrawInts.CODE, doc);
				} catch (GeometryOperationNotSupportedException e) {
					e.printStackTrace();
				} catch (GeometryOperationException e) {
					e.printStackTrace();
				}
				Handler[] handlers = ig.getHandlers(Geometry.SELECTHANDLER);
				if (numSelect >= handlers.length) {
					numSelect = 0;
				}
				FGraphicUtilities.DrawVertex((Graphics2D) g, vp
						.getAffineTransform(), handlers[numSelect]);
			}
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (iterator != null) {
				iterator.dispose();
			}
		}
	}

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.CADTool#addvalue(double)
     */
    public void addValue(double d) {
    }
    private Geometry removeVertex(Geometry gp,Handler[] handlers,int numHandler) {
        GeneralPathX newGp = new GeneralPathX();
        double[] theData = new double[6];

        PathIterator theIterator;
        int theType;
        int numParts = 0;

        Point2D ptSrc = new Point2D.Double();
        boolean bFirst = false;

        theIterator = gp.getPathIterator(null, Converter.FLATNESS);
        int numSegmentsAdded = 0;
        while (!theIterator.isDone()) {
            theType = theIterator.currentSegment(theData);
            if (bFirst){
        		newGp.moveTo(theData[0], theData[1]);
        		numSegmentsAdded++;
        		bFirst=false;
        		theIterator.next();
        		continue;
        	}
            switch (theType) {

                case PathIterator.SEG_MOVETO:
                    numParts++;
                    ptSrc.setLocation(theData[0], theData[1]);
                    if (ptSrc.equals(handlers[numHandler].getPoint())){
                    	numParts--;
                    	bFirst=true;
                    	break;
                    }
                    newGp.moveTo(ptSrc.getX(), ptSrc.getY());
                    numSegmentsAdded++;
                    bFirst = false;
                    break;

                case PathIterator.SEG_LINETO:
                    ptSrc.setLocation(theData[0], theData[1]);
                    if (ptSrc.equals(handlers[numHandler].getPoint())){
                    	break;
                    }
                    newGp.lineTo(ptSrc.getX(), ptSrc.getY());
                    bFirst = false;
                    numSegmentsAdded++;
                    break;

                case PathIterator.SEG_QUADTO:
                    newGp.quadTo(theData[0], theData[1], theData[2], theData[3]);
                    numSegmentsAdded++;
                    break;

                case PathIterator.SEG_CUBICTO:
                    newGp.curveTo(theData[0], theData[1], theData[2], theData[3], theData[4], theData[5]);
                    numSegmentsAdded++;
                    break;

                case PathIterator.SEG_CLOSE:
                    if (numSegmentsAdded < 3) {
						newGp.lineTo(theData[0], theData[1]);
					}
                    newGp.closePath();

                    break;
            } //end switch

            theIterator.next();
        } //end while loop
        Geometry shp = null;
        switch (gp.getType())
        {
            case Geometry.TYPES.POINT: //Tipo punto
                shp = createPoint(ptSrc.getX(), ptSrc.getY());
                break;

            case Geometry.TYPES.CURVE:
                shp = createCurve(newGp);
                break;
            case Geometry.TYPES.SURFACE:
                shp = createSurface(newGp);
                break;
        }
        Geometry ig=shp;
        int dif=1;//En el caso de ser pol�gono.
       	numSelect=numSelect-dif;
		if (numSelect<0){
			numSelect=numHandlers-1+(numSelect+1);
		}
        return ig;
    }

    private Geometry removeVertexGC(BaseMultiPrimitive gc,Handler handler) {
        Geometry[] geoms=gc.getGeometries();
    	ArrayList geomsAux=new ArrayList();
        int pos=-1;
    	for (int i=0;i<geoms.length;i++) {
    		Handler[] handlers=geoms[i].getHandlers(Geometry.SELECTHANDLER);
    		for (int j=0;j<handlers.length;j++) {
    			if (handlers[j].equalsPoint(handler)) {
    				geomsAux.add(geoms[i]);
    				if (pos==-1) {
						pos=i;
					}
    			}
    		}
    	}
    	int numGeomsAux=geomsAux.size();
    	GeneralPathX gpx=new GeneralPathX();
        for (int i=0;i<numGeomsAux;i++) {
    		Handler[] handlers=((Geometry)geomsAux.get(i)).getHandlers(Geometry.SELECTHANDLER);
    		if (numGeomsAux == 2) {
				for (int j = 0; j < handlers.length; j++) {
					if (handlers[j].equalsPoint(handler)) {
						if (j == (handlers.length - 1)) {
							Point2D ph = handlers[0].getPoint();
							gpx.moveTo(ph.getX(), ph.getY());
						} else {
							Point2D ph = handlers[handlers.length - 1]
									.getPoint();
							gpx.lineTo(ph.getX(), ph.getY());
						}
					}
				}
			}

    	}
        ArrayList newGeoms=new ArrayList();
        for (int i=0;i<pos;i++) {
        	newGeoms.add(geoms[i]);
        }
        newGeoms.add(createCurve(gpx));
        for (int i=pos+numGeomsAux;i<geoms.length;i++) {
        	newGeoms.add(geoms[i]);
        }


    	return createMultiPrimitive((Geometry[])newGeoms.toArray(new Geometry[0]));
    }



    private Geometry addVertex(Geometry geome,Point2D p,Rectangle2D rect) {
    	Geometry geometryCloned=geome.cloneGeometry();
    	Geometry geom1=null;
    	GeneralPathX gpxAux;
    	boolean finish=false;
    	//FGeometry geom2=null;

    	//if (geometryCloned.getGeometryType() == FShape.POLYGON){
    		/////////////////

    		GeneralPathX newGp = new GeneralPathX();
            double[] theData = new double[6];

            PathIterator theIterator;
            int theType;
            int numParts = 0;
            Point2D pLast=new Point2D.Double();
            Point2D pAnt = new Point2D.Double();
            Point2D firstPoint=null;
            theIterator = geome.getPathIterator(null,Converter.FLATNESS); //, flatness);
            int numSegmentsAdded = 0;
            while (!theIterator.isDone()) {
                theType = theIterator.currentSegment(theData);
                switch (theType) {
                    case PathIterator.SEG_MOVETO:
                    	pLast.setLocation(theData[0], theData[1]);
                    	if (numParts==0) {
							firstPoint=(Point2D)pLast.clone();
						}
                    	numParts++;

                    	gpxAux=new GeneralPathX();
                    	gpxAux.moveTo(pAnt.getX(),pAnt.getY());
                    	gpxAux.lineTo(pLast.getX(),pLast.getY());
                    	geom1=createCurve(gpxAux);
                    	if (geom1.intersects(rect)){
                    		finish=true;
                    		newGp.moveTo(pLast.getX(), pLast.getY());
                    		//newGp.lineTo(pLast.getX(),pLast.getY());
                    	}else{
                    		newGp.moveTo(pLast.getX(), pLast.getY());
                    	}
                        pAnt.setLocation(pLast.getX(), pLast.getY());
                        numSegmentsAdded++;
                        break;

                    case PathIterator.SEG_LINETO:
                    	pLast.setLocation(theData[0], theData[1]);
                    	gpxAux=new GeneralPathX();
                    	gpxAux.moveTo(pAnt.getX(),pAnt.getY());
                    	gpxAux.lineTo(pLast.getX(),pLast.getY());
                    	geom1=createCurve(gpxAux);
                    	if (geom1.intersects(rect)){
                    		newGp.lineTo(p.getX(), p.getY());
                    		newGp.lineTo(pLast.getX(),pLast.getY());
                    	}else{
                    		newGp.lineTo(pLast.getX(), pLast.getY());
                    	}
                    	pAnt.setLocation(pLast.getX(), pLast.getY());
                        numSegmentsAdded++;
                        break;

                    case PathIterator.SEG_QUADTO:
                        newGp.quadTo(theData[0], theData[1], theData[2], theData[3]);
                        numSegmentsAdded++;
                        break;

                    case PathIterator.SEG_CUBICTO:
                        newGp.curveTo(theData[0], theData[1], theData[2], theData[3], theData[4], theData[5]);
                        numSegmentsAdded++;
                        break;

                    case PathIterator.SEG_CLOSE:
                        //if (numSegmentsAdded < 3){
                        	gpxAux=new GeneralPathX();
                        	gpxAux.moveTo(pAnt.getX(),pAnt.getY());
                        	gpxAux.lineTo(firstPoint.getX(),firstPoint.getY());
                        	geom1=createCurve(gpxAux);
                        	if (geom1.intersects(rect)|| finish){
                        		newGp.lineTo(p.getX(), p.getY());
                        		newGp.lineTo(pLast.getX(),pLast.getY());
                        	}else{
                        		newGp.lineTo(pLast.getX(), pLast.getY());
                        	}
                        //}
                        newGp.closePath();
                        break;
                } //end switch

                theIterator.next();
            } //end while loop
            Geometry shp = null;
            switch (geometryCloned.getType())
            {
                case Geometry.TYPES.POINT: //Tipo punto
                	shp = createPoint(pLast.getX(), pLast.getY());
                    break;

                case Geometry.TYPES.CURVE:
                    shp = createCurve(newGp);
                    break;
                case Geometry.TYPES.SURFACE:
                case Geometry.TYPES.CIRCLE:
                case Geometry.TYPES.ELLIPSE:
                    shp = createSurface(newGp);
                    break;
            }
            return shp;


    		/////////////////////
    	//}else if (geometryCloned.getGeometryType() == FShape.LINE){

    	//}




    /*	if (geometryCloned instanceof FGeometryCollection){
    		IGeometry[] geometries=((FGeometryCollection)geometryCloned).getGeometries();
    		boolean isSelected=false;
    		for (int i=0;i<geometries.length;i++){
    			if (geometries[i].intersects(rect) && !isSelected){
    				isSelected=true;
    				Handler[] handlers=geometries[i].getHandlers(IGeometry.SELECTHANDLER);

    				GeneralPathX gp1=new GeneralPathX();
    				Point2D pinit1=(Point2D)handlers[0].getPoint().clone();
    				gp1.moveTo(pinit1.getX(),pinit1.getY());
    				System.out.println("Handler inicial = "+pinit1);
    				gp1.lineTo(p.getX(),p.getY());
    				System.out.println("Handler medio = "+p);
    				FPolyline2D poly1=new FPolyline2D(gp1);
    				geom1=ShapeFactory.createGeometry(poly1);

    				GeneralPathX gp2=new GeneralPathX();
    				gp2.moveTo(p.getX(),p.getY());
    				System.out.println("Handler medio = "+p);
    				Point2D pEnd=(Point2D)handlers[1].getPoint().clone();
    				gp2.lineTo(pEnd.getX(),pEnd.getY());
    				System.out.println("Handler final = "+pEnd);
    				FPolyline2D poly2=new FPolyline2D(gp2);
    				geom2=ShapeFactory.createGeometry(poly2);

    				ArrayList geomsAux=new ArrayList();
    				geometries[i]=geom1;
    				for (int j=i;j<geometries.length;j++){
    					geomsAux.add(geometries[j]);
    				}

    				if (i<geometries.length-1){
    					geometries[i+1]=geom2;
    					Handler[] hands=((IGeometry)geom1).getHandlers(IGeometry.SELECTHANDLER);
    					for (int h=0;h<hands.length;h++)
    					System.out.println("Handlers New Geometry = "+hands[h].getPoint());
    					Handler[] hands2=((IGeometry)geom2).getHandlers(IGeometry.SELECTHANDLER);
    					for (int h=0;h<hands2.length;h++)
    					System.out.println("Handlers New Geometry = "+hands2[h].getPoint());
    				}else{
    					geometryCloned=new FGeometryCollection(geometries);
    					((FGeometryCollection)geometryCloned).addGeometry(geom2);
    				}
    				for (int j=i+1;j<geometries.length;j++){
    					if ((j-i)<geomsAux.size()-1){
        					geometries[j+1]=(IGeometry)geomsAux.get(j-i);
        				}else{
        					geometryCloned=new FGeometryCollection(geometries);
        					((FGeometryCollection)geometryCloned).addGeometry((IGeometry)geomsAux.get(j-i));

        				}
    				}
    			}

    		}
    	}
    	return geometryCloned;
*/
    }
    private Geometry addVertexGC(BaseMultiPrimitive gc,Point2D p,Rectangle2D rect) {
    	Geometry[] geoms=gc.getGeometries();
    	int pos=-1;
    	for (int i=0;i<geoms.length;i++) {
    		if (geoms[i].intersects(rect)) {
    			pos=i;
    		}
    	}
    	ArrayList newGeoms=new ArrayList();
    	for (int i=0;i<pos;i++) {
    		newGeoms.add(geoms[i]);
    	}
    	if (pos!=-1) {
    	GeneralPathX gpx1=new GeneralPathX();
    	GeneralPathX gpx2=new GeneralPathX();
    	Handler[] handlers=geoms[pos].getHandlers(Geometry.SELECTHANDLER);
    	Point2D p1=handlers[0].getPoint();
    	Point2D p2=p;
    	Point2D p3=handlers[handlers.length-1].getPoint();
    	gpx1.moveTo(p1.getX(),p1.getY());
    	gpx1.lineTo(p2.getX(),p2.getY());
    	gpx2.moveTo(p2.getX(),p2.getY());
    	gpx2.lineTo(p3.getX(),p3.getY());
    	newGeoms.add(createCurve(gpx1));
    	newGeoms.add(createCurve(gpx2));
    	for (int i=pos+1;i<geoms.length;i++) {
    		newGeoms.add(geoms[i]);
    	}
    	return createMultiPrimitive((Geometry[])newGeoms.toArray(new Geometry[0]));
    	}else {
    		return null;
    	}
    }
	public String getName() {
		return PluginServices.getText(this,"edit_vertex_");
	}
	private void selectHandler(double x, double y) {
		Point2D firstPoint = new Point2D.Double(x, y);
		VectorialLayerEdited vle = getVLE();
		FeatureStore featureStore=null;
		DisposableIterator iterator = null;
		try {
			featureStore = vle.getFeatureStore();

			iterator = ((FeatureSelection) featureStore.getSelection())
					.iterator();
			double tam = getCadToolAdapter().getMapControl().getViewPort()
					.toMapDistance(MapControl.tolerance);
			Rectangle2D rect = new Rectangle2D.Double(firstPoint.getX() - tam,
					firstPoint.getY() - tam, tam * 2, tam * 2);
			while (iterator.hasNext()) {
				Feature feature = (Feature) iterator.next();

				boolean isSelectedHandler = false;
				Geometry geometry = getSelectedGeometry();
				if (geometry != null) {
					Handler[] handlers = geometry
							.getHandlers(Geometry.SELECTHANDLER);
					for (int h = 0; h < handlers.length; h++) {
						if (handlers[h].getPoint().distance(firstPoint) < tam) {
							numSelect = h;
							isSelectedHandler = true;
						}
					}

					if (!isSelectedHandler) {
						boolean isSelectedGeometry = false;
						if (geometry.intersects(rect)) { // , 0.1)){
							isSelectedGeometry = true;
						}
						if (isSelectedGeometry && addVertex) {
							Feature feat = (Feature) ((FeatureSelection) featureStore
									.getSelection()).iterator().next();
							Point2D posVertex = new Point2D.Double(x, y);
							Geometry geom1 = (feat.getDefaultGeometry())
									.cloneGeometry();
							Geometry geom = null;
							if (geom1 instanceof BaseMultiPrimitive) {
								geom = addVertexGC((BaseMultiPrimitive) geom1,
										posVertex, rect);
							} else {
								geom = addVertex(geom1, posVertex, rect);
							}
							if (geom != null) {
								EditableFeature eFeature = feature
										.getEditable();
								eFeature.setGeometry(featureStore
										.getDefaultFeatureType()
										.getDefaultGeometryAttributeName(),
										geom);
								featureStore.update(eFeature);
								Handler[] newHandlers = geom
										.getHandlers(Geometry.SELECTHANDLER);
								for (int h = 0; h < newHandlers.length; h++) {
									if (newHandlers[h].getPoint().distance(
											posVertex) < tam) {
										numSelect = h;
										isSelectedHandler = true;
									}
								}

								// clearSelection();
							}
						}
					}
				}
			}

		} catch (DataException e) {
			if (iterator != null) {
				iterator.dispose();
			}
			e.printStackTrace();
		}

	}

	public String toString() {
		return "_editvertex";
	}

	public boolean isApplicable(int shapeType) {
		switch (shapeType) {
		case Geometry.TYPES.POINT:
		case Geometry.TYPES.MULTIPOINT:
			return false;
		}
		return true;
	}


}
