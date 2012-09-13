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
package com.iver.cit.gvsig.gui.cad.tools;

import java.awt.Graphics;
import java.awt.event.InputEvent;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.operation.tojts.ToJTS;
import org.gvsig.fmap.geom.util.Converter;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.gui.cad.DefaultCADTool;
import com.iver.cit.gvsig.gui.cad.exception.CommandException;
import com.iver.cit.gvsig.gui.cad.tools.smc.JoinCADToolContext;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;
import com.vividsolutions.jts.geom.Geometry;


/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class JoinCADTool extends DefaultCADTool {
    private JoinCADToolContext _fsm;

    /**
     * Crea un nuevo JoinCADTool.
     */
    public JoinCADTool() {
    }

    /**
     * Método de inicio, para poner el código de todo lo que se requiera de una
     * carga previa a la utilización de la herramienta.
     */
    public void init() {
        _fsm = new JoinCADToolContext(this);
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
    	FeatureSet selection = null;
		try {
			selection = (FeatureSet) getVLE().getFeatureStore().getSelection();

			if (selection.getSize() == 0
					&& !CADExtension
							.getCADTool()
							.getClass()
							.getName()
							.equals(
									"com.iver.cit.gvsig.gui.cad.tools.SelectionCADTool")) {
				CADExtension.setCADTool("_selection", false);
				((SelectionCADTool) CADExtension.getCADTool())
						.setNextTool("_join");
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
     * Equivale al transition del prototipo pero sin pasarle como parámetro el
     * editableFeatureSource que ya estará creado.
     *
     * @param x parámetro x del punto que se pase en esta transición.
     * @param y parámetro y del punto que se pase en esta transición.
     */
    public void addPoint(double x, double y,InputEvent event) {
    }

    /**
     * Método para dibujar la lo necesario para el estado en el que nos
     * encontremos.
     *
     * @param g Graphics sobre el que dibujar.
     * @param x parámetro x del punto que se pase para dibujar.
     * @param y parámetro x del punto que se pase para dibujar.
     */
    public void drawOperation(Graphics g, double x, double y) {
    }
    public void join() {
    	VectorialLayerEdited vle = getVLE();
		FeatureStore featureStore = null;
		try {
			featureStore = vle.getFeatureStore();
		} catch (ReadException e1) {
			NotificationManager.addError(e1.getMessage(),e1);
		}
		DisposableIterator features = null;
		try {
		FeatureSet selection = (FeatureSet) featureStore.getSelection();
		// getSelectedRows();
    		featureStore.beginEditingGroup(getName());
    		Geometry geomTotal=null;
    		features = selection.iterator();
    		boolean first=true;
    		Feature firstFeature=null;
    		while (features.hasNext()) {
				Feature feature = (Feature) features.next();
				if (first){
					firstFeature=feature;
					first=false;
				}

				org.gvsig.fmap.geom.Geometry geom = (feature
						.getDefaultGeometry()).cloneGeometry();
				featureStore.delete(feature);
				if (geomTotal==null){
    				geomTotal=(Geometry)geom.invokeOperation(ToJTS.CODE, null);
    			}else{
    				Geometry geomJTS=(Geometry)geom.invokeOperation(ToJTS.CODE, null);
    				geomTotal=geomTotal.union(geomJTS);
    			}
			}

    		EditableFeature eFeature = featureStore.createNewFeature(firstFeature
					.getType(), firstFeature);
			eFeature.setGeometry(featureStore.getDefaultFeatureType()
					.getDefaultGeometryAttributeName(), Converter.jtsToGeometry(geomTotal));
			featureStore.insert(eFeature);
			refresh();
		} catch (DataException e) {
			NotificationManager.addError(e.getMessage(),e);
		} catch (GeometryOperationNotSupportedException e) {
			NotificationManager.addError(e.getMessage(),e);
		} catch (GeometryOperationException e) {
			NotificationManager.addError(e.getMessage(),e);
		} catch (CreateGeometryException e) {
			NotificationManager.addError(e.getMessage(),e);
		} finally {
			if (features != null) {
				features.dispose();
			}
		}
    }
       /**
	 * Add a diferent option.
	 *
	 * @param s
	 *            Diferent option.
	 */
    public void addOption(String s) {
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.gui.cad.CADTool#addvalue(double)
	 */
    public void addValue(double d) {

    }

	public String getName() {
		return PluginServices.getText(this,"join_");
	}

	public String toString() {
		return "_join";
	}
	public boolean isApplicable(int shapeType) {
		switch (shapeType) {
		case org.gvsig.fmap.geom.Geometry.TYPES.POINT:
			return false;
	}
		return true;
	}
}
