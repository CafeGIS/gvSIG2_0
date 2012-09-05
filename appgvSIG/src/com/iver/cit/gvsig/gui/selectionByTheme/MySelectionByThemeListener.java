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
package com.iver.cit.gvsig.gui.selectionByTheme;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.ContainsGeometryEvaluator;
import org.gvsig.fmap.mapcontext.layers.vectorial.CrossesGeometryEvaluator;
import org.gvsig.fmap.mapcontext.layers.vectorial.DisjointGeometryEvaluator;
import org.gvsig.fmap.mapcontext.layers.vectorial.EqualsGeometryEvaluator;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontext.layers.vectorial.IntersectsGeometryEvaluator;
import org.gvsig.fmap.mapcontext.layers.vectorial.OverlapsGeometryEvaluator;
import org.gvsig.fmap.mapcontext.layers.vectorial.TouchesGeometryEvaluator;
import org.gvsig.fmap.mapcontext.layers.vectorial.WithinGeometryEvaluator;
import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.evaluator.EvaluatorData;
import org.gvsig.tools.evaluator.EvaluatorException;
import org.gvsig.tools.evaluator.EvaluatorFieldsInfo;

import com.iver.andami.messages.NotificationManager;

/**
 * DOCUMENT ME!
 *
 * @author Fernando González Cortés
 */
//TODO comentado para que compile
public class MySelectionByThemeListener implements SelectionByThemeListener {
	/**
	 * @see com.iver.cit.gvsig.gui.selectionByTheme.SelectionByThemeListener#newSet(int,
	 *      int, int)
	 */
	public void newSet(FLayer[] toSelect, FLayer selectionLayer, int action) {
		long t1 = System.currentTimeMillis();

		if (selectionLayer instanceof FLyrVect) {
			try {
				FeatureSelection selection=(FeatureSelection)((FLyrVect)selectionLayer).getFeatureStore().getSelection();
				SelectByTheme filter=new SelectByTheme(selection,selectionLayer.getMapContext().getProjection(),selection.getDefaultFeatureType().getDefaultGeometryAttributeName(), action);

				for (int i = 0; i < toSelect.length; i++) {
					if (toSelect[i] instanceof FLyrVect) {
//						if (!selectionLayer.getFullEnvelope().intersects(toSelect[i].getFullEnvelope())){
//							continue;
//						}
						FeatureStore storeToQuery = ((FLyrVect) toSelect[i]).getFeatureStore();
						FeatureQuery fQuery = storeToQuery
								.createFeatureQuery();
						filter.setData(storeToQuery.getDefaultFeatureType(),storeToQuery.getDefaultFeatureType().getDefaultGeometryAttributeName(),null);

						fQuery.setFilter(filter);
						FeatureSet featureSet = null;
						try {
							featureSet = storeToQuery.getFeatureSet(fQuery);
							((FeatureSelection) storeToQuery.getSelection())
									.deselectAll();
							storeToQuery.setSelection(featureSet);
						} finally {
							if (featureSet != null) {
								featureSet.dispose();
							}
						}
					}
				}
			} catch (ReadException e) {
				NotificationManager.addError("error_selection_by_theme", e);
			} catch (DataException e) {
				NotificationManager.addError("error_selection_by_theme", e);
			}

		}
		long t2 = System.currentTimeMillis();
		System.out
				.println("Tiempo de consulta: " + (t2 - t1) + " milisegundos");
		// doSelection(toSelect, selectionLayer, action, false);
	}

	/**
	 * @see com.iver.cit.gvsig.gui.selectionByTheme.SelectionByThemeListener#addToSet(int,
	 *      int, int)
	 */
	public void addToSet(FLayer[] toSelect, FLayer selectionLayer, int action) {
		long t1 = System.currentTimeMillis();

		if (selectionLayer instanceof FLyrVect) {
			try {
				FeatureSelection selection=(FeatureSelection)((FLyrVect)selectionLayer).getFeatureStore().getSelection();
				SelectByTheme filter=new SelectByTheme(selection,selectionLayer.getMapContext().getProjection(),selection.getDefaultFeatureType().getDefaultGeometryAttributeName(), action);

				for (int i = 0; i < toSelect.length; i++) {
					if (toSelect[i] instanceof FLyrVect) {
						if (!selectionLayer.getFullEnvelope().intersects(toSelect[i].getFullEnvelope())){
							continue;
						}
						FeatureStore storeToQuery = ((FLyrVect) toSelect[i]).getFeatureStore();
						FeatureQuery fQuery = storeToQuery
								.createFeatureQuery();
						filter.setData(storeToQuery.getDefaultFeatureType(),storeToQuery.getDefaultFeatureType().getDefaultGeometryAttributeName(),null);

						fQuery.setFilter(filter);
						FeatureSet featureSet = null;
						try {
							featureSet = storeToQuery.getFeatureSet(fQuery);
							storeToQuery.setSelection(featureSet);
						} finally {
							if (featureSet != null) {
								featureSet.dispose();
							}
						}
					}
				}
			} catch (ReadException e) {
				NotificationManager.addError("error_selection_by_theme", e);
			} catch (DataException e) {
				NotificationManager.addError("error_selection_by_theme", e);
			}

		}
		long t2 = System.currentTimeMillis();
		System.out
				.println("Tiempo de consulta: " + (t2 - t1) + " milisegundos");
	}

	/**
	 * @see com.iver.cit.gvsig.gui.selectionByTheme.SelectionByThemeListener#fromSet(int,
	 *      int, int)
	 */
	public void fromSet(FLayer[] toSelect, FLayer selectionLayer, int action) {
		long t1 = System.currentTimeMillis();
		if (selectionLayer instanceof FLyrVect) {
			try {
				FeatureSelection selection=(FeatureSelection)((FLyrVect)selectionLayer).getFeatureStore().getSelection();
				SelectByTheme filter=new SelectByTheme(selection,selectionLayer.getMapContext().getProjection(),selection.getDefaultFeatureType().getDefaultGeometryAttributeName(), action);

				for (int i = 0; i < toSelect.length; i++) {
					if (toSelect[i] instanceof FLyrVect) {
						if (!selectionLayer.getFullEnvelope().intersects(toSelect[i].getFullEnvelope())){
							continue;
						}
						FeatureStore storeToQuery = ((FLyrVect) toSelect[i]).getFeatureStore();
						FeatureQuery fQuery = storeToQuery
								.createFeatureQuery();
						filter.setData(storeToQuery.getDefaultFeatureType(),storeToQuery.getDefaultFeatureType().getDefaultGeometryAttributeName(),(FeatureSelection)storeToQuery.getSelection());

						fQuery.setFilter(filter);
						FeatureSet featureSet = null;
						try {
							featureSet = storeToQuery.getFeatureSet(fQuery);
							((FeatureSelection) storeToQuery.getSelection())
									.deselectAll();
							storeToQuery.setSelection(featureSet);
						} finally {
							if (featureSet != null) {
								featureSet.dispose();
							}

						}
					}
				}
			} catch (ReadException e) {
				NotificationManager.addError("error_selection_by_theme", e);
			} catch (DataException e) {
				NotificationManager.addError("error_selection_by_theme", e);
			}

		}
		long t2 = System.currentTimeMillis();
		System.out
				.println("Tiempo de consulta: " + (t2 - t1) + " milisegundos");
	}
	class SelectByTheme implements Evaluator{
		private FeatureSelection selection=null;
		private IProjection projection;
		private FeatureType featureType;
		private String geomName;
		private EvaluatorFieldsInfo info;
		private String nameSelectedGeom;
		private FeatureSelection featureSelection;
		private int action;
		private int index;

		public SelectByTheme(FeatureSelection selection, IProjection projection,String nameSelectedGeom, int action){
			this.selection=selection;

			this.projection=projection;
			this.nameSelectedGeom=nameSelectedGeom;
			this.action=action;
			this.index=selection.getDefaultFeatureType().getIndex(nameSelectedGeom);
		}
		public void setData(FeatureType featureType, String geomName, FeatureSelection featureSelection){
			this.featureType=featureType;
			this.geomName=geomName;
			this.featureSelection=featureSelection;
			this.info=new EvaluatorFieldsInfo();
			this.info.addFieldValue(geomName);
		}

		public Object evaluate(EvaluatorData data) throws EvaluatorException {
			if (featureSelection!=null && !featureSelection.isSelected(((Feature)data.getContextValue("feature")))){
				return new Boolean(false);
			}
			DisposableIterator features = null;
			try {
				features = selection.iterator();

				while (features.hasNext()) {
					Feature feature = (Feature) features.next();
					Geometry geometry = feature.getGeometry(index);
					Evaluator eval = null;
					switch (action) {
					case SelectionByTheme.INTERSECTS:
						eval = new IntersectsGeometryEvaluator(geometry,
								projection, featureType, geomName);
						break;
					case SelectionByTheme.CONTAINS:
						eval = new ContainsGeometryEvaluator(geometry,
								projection, featureType, geomName);
						break;
					case SelectionByTheme.CROSSES:
						eval = new CrossesGeometryEvaluator(geometry,
								projection, featureType, geomName);
						break;
					case SelectionByTheme.DISJOINT:
						eval = new DisjointGeometryEvaluator(geometry,
								projection, featureType, geomName);
						break;
					case SelectionByTheme.EQUALS:
						eval = new EqualsGeometryEvaluator(geometry,
								projection, featureType, geomName);
						break;
					case SelectionByTheme.OVERLAPS:
						eval = new OverlapsGeometryEvaluator(geometry,
								projection, featureType, geomName);
						break;
					case SelectionByTheme.TOUCHES:
						eval = new TouchesGeometryEvaluator(geometry,
								projection, featureType, geomName);
						break;
					case SelectionByTheme.WITHIN:
						eval = new WithinGeometryEvaluator(geometry,
								projection, featureType, geomName);
						break;

					default:
						eval = new IntersectsGeometryEvaluator(geometry,
								projection, featureType, geomName);
					break;
					}

					Boolean evaluate = (Boolean) eval.evaluate(data);
					if ((evaluate).booleanValue()) {
						return evaluate;
					}
				}
			} catch (DataException e) {
				NotificationManager.addError("error_selection_by_theme", e);
			} finally {
				if (features != null) {
					features.dispose();
				}
			}
			return new Boolean(false);
		}

		public String getName() {
			return "select by theme";
		}
		public String getCQL() {
			// TODO Auto-generated method stub
			return null;
		}
		public String getDescription() {
			return getName();
		}
		public EvaluatorFieldsInfo getFieldsInfo() {
			return info;
		}
	}
}
