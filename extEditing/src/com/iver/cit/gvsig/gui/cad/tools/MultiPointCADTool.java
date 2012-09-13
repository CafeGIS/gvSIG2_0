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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.InputEvent;
import java.util.ArrayList;

import org.gvsig.fmap.dal.feature.exception.CreateGeometryException;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.aggregate.MultiPoint;
import org.gvsig.fmap.geom.operation.Draw;
import org.gvsig.fmap.geom.operation.DrawOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.primitive.Point;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.cad.DefaultCADTool;
import com.iver.cit.gvsig.gui.cad.exception.CommandException;
import com.iver.cit.gvsig.gui.cad.tools.smc.MultiPointCADToolContext;
import com.iver.cit.gvsig.gui.cad.tools.smc.MultiPointCADToolContext.MultiPointCADToolState;


/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class MultiPointCADTool extends DefaultCADTool {
	protected MultiPointCADToolContext _fsm;
	protected ArrayList points=new ArrayList();

	/**
	 * Crea un nuevo PointCADTool.
	 */
	public MultiPointCADTool() {

	}

	/**
	 * M�todo de incio, para poner el c�digo de todo lo que se requiera de una
	 * carga previa a la utilizaci�n de la herramienta.
	 */
	public void init() {
		_fsm = new MultiPointCADToolContext(this);
	}

	/**
	 * DOCUMENT ME!
	 * @param x DOCUMENT ME!
	 * @param y DOCUMENT ME!
	 * @param sel DOCUMENT ME!
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
	 * Equivale al transition del prototipo pero sin pasarle como par� metro el
	 * editableFeatureSource que ya estar� creado.
	 *
	 * @param sel Bitset con las geometr�as que est�n seleccionadas.
	 * @param x par�metro x del punto que se pase en esta transici�n.
	 * @param y par�metro y del punto que se pase en esta transici�n.
	 */
	public void addPoint(double x, double y,InputEvent event) {
		MultiPointCADToolState actualState = (MultiPointCADToolState) _fsm.getPreviousState();
		String status = actualState.getName();

		if (status.equals("MultiPoint.InsertPoint")) {
			points.add(new double[] {x,y});
			//addGeometry(ShapeFactory.createPoint2D(x, y));
		}
	}

	/**
	 * M�todo para dibujar la lo necesario para el estado en el que nos
	 * encontremos.
	 *
	 * @param g Graphics sobre el que dibujar.
	 * @param selectedGeometries BitSet con las geometr�as seleccionadas.
	 * @param x par�metro x del punto que se pase para dibujar.
	 * @param y par�metro x del punto que se pase para dibujar.
	 */
	public void drawOperation(Graphics g, double x,
			double y) {
		try {
			int num=points.size();
			Point[] pointsAux = new Point[num];
			for (int i=0;i<num;i++) {
				double[] p=(double[])points.get(i);
				pointsAux[i] = (Point)geomManager.create(TYPES.POINT, SUBTYPES.GEOM2D);
				pointsAux[i].setX(p[0]);
				pointsAux[i].setY(p[1]);
			}

			DrawOperationContext doc=new DrawOperationContext();
			doc.setGraphics((Graphics2D)g);
			doc.setViewPort(getCadToolAdapter().getMapControl().getViewPort());
			doc.setSymbol(DefaultCADTool.geometrySelectSymbol);

			MultiPoint multiPoint = (MultiPoint)geomManager.create(TYPES.MULTIPOINT, SUBTYPES.GEOM2D);
			for (int i=0 ; i<pointsAux.length ; i++){
				multiPoint.addPoint(pointsAux[i]);
			}
			multiPoint.invokeOperation(Draw.CODE,doc);
			createPoint(x,y).invokeOperation(Draw.CODE,doc);
		} catch (GeometryOperationNotSupportedException e) {
			e.printStackTrace();
		} catch (GeometryOperationException e) {
			e.printStackTrace();
		} catch (org.gvsig.fmap.geom.exception.CreateGeometryException e) {
			logger.error(new CreateGeometryException(TYPES.MULTIPOINT, SUBTYPES.GEOM2D, e));
		}

		//		geomFactory.createMultipoint2D(xs,ys).draw((Graphics2D) g,
		//                getCadToolAdapter().getMapControl().getViewPort(),
		//                DefaultCADTool.geometrySelectSymbol);
		//		geomFactory.createPoint2D(x,y).draw((Graphics2D) g,
		//                getCadToolAdapter().getMapControl().getViewPort(),
		//                DefaultCADTool.geometrySelectSymbol);
	}

	/**
	 * Add a diferent option.
	 *
	 * @param sel DOCUMENT ME!
	 * @param s Diferent option.
	 */
	public void addOption(String s) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.gui.cad.CADTool#addvalue(double)
	 */
	public void addValue(double d) {
		// TODO Auto-generated method stub
	}

	public String getName() {
		return PluginServices.getText(this,"multipoint_");
	}

	public String toString() {
		return "_multipoint";
	}
	public boolean isApplicable(int shapeType) {
		switch (shapeType) {
		case Geometry.TYPES.MULTIPOINT:
			return true;
		}
		return false;
	}

	public void endGeometry(){
		try {
			int num=points.size();
			Point[] pointsAux = new Point[num];
			for (int i=0;i<num;i++) {
				double[] p=(double[])points.get(i);
				pointsAux[i] = (Point)geomManager.create(TYPES.POINT, SUBTYPES.GEOM2D);
				pointsAux[i].setX(p[0]);
				pointsAux[i].setY(p[1]);
			}

			MultiPoint multiPoint;

			multiPoint = (MultiPoint)geomManager.create(TYPES.MULTIPOINT, SUBTYPES.GEOM2D);
			for (int i=0 ; i<pointsAux.length ; i++){
				multiPoint.addPoint(pointsAux[i]);
			}
			insertAndSelectGeometry(multiPoint);
		} catch (org.gvsig.fmap.geom.exception.CreateGeometryException e) {
			logger.error(new CreateGeometryException(TYPES.MULTIPOINT, SUBTYPES.GEOM2D, e));
		}
		end();
	}
	public void end() {
		points.clear();
		super.end();
	}
}
