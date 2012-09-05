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

import java.awt.Component;

import javax.swing.JOptionPane;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.project.document.table.gui.FeatureTableDocumentPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.gui.filter.ExpressionListener;
import com.iver.cit.gvsig.gui.filter.FilterDialog;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.utiles.exceptionHandling.ExceptionListener;


/**
 * Extensión que abre un diálogo para poder hacer un filtro de una capa o tabla.
 *
 * @author Vicente Caballero Navarro
 */
public class FiltroExtension extends Extension implements ExpressionListener {
	protected FeatureStore featureStore = null;
	protected FeatureTableDocumentPanel table;
	private String filterTitle;

	/**
	 * DOCUMENT ME!
	 */
	public void initialize() {
		registerIcons();
	}

	private void registerIcons(){
		PluginServices.getIconTheme().registerDefault(
				"table-filter",
				this.getClass().getClassLoader().getResource("images/Filtro.png")
		);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param actionCommand DOCUMENT ME!
	 */
	public void execute(String actionCommand) {
		if ("FILTRO".equals(actionCommand)) {
			try {
				IWindow v = PluginServices.getMDIManager().getActiveWindow();

				if (v instanceof FeatureTableDocumentPanel) {
					table = (FeatureTableDocumentPanel) v;

					featureStore = table.getModel().getStore();
					filterTitle = table.getModel().getName();
					table.getModel().setModified(true);
				} else if (v instanceof com.iver.cit.gvsig.project.documents.view.gui.View) {
					IProjectView pv = ((com.iver.cit.gvsig.project.documents.view.gui.View) v).getModel();
					filterTitle = ((com.iver.cit.gvsig.project.documents.view.gui.View) v).getModel().getName();
					FLayer layer = pv.getMapContext()
					.getLayers().getActives()[0];
					featureStore = ((FLyrVect)layer).getFeatureStore();//pv.getProject().getDataSourceByLayer(layer);
					((ProjectDocument)pv).setModified(true);
				}
			}  catch (ReadException e) {
				NotificationManager.addError("Error filtrando", e);
			}

			doExecute();
		}
	}

	/**
	 * "execute" method action.
	 *
	 */
	protected void doExecute(){
		//		DefaultExpressionDataSource ds = new DefaultExpressionDataSource();
		//		ds.setTable(featureStore);
		FilterDialog dlg = new FilterDialog(filterTitle);
		dlg.addExpressionListener(this);
		dlg.addExceptionListener(new ExceptionListener() {
			public void exceptionThrown(Throwable t) {
				NotificationManager.addError(t.getMessage(), t);
			}
		});
		dlg.setModel(featureStore);
		PluginServices.getMDIManager().addWindow(dlg);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean isEnabled() {
		IWindow v = PluginServices.getMDIManager().getActiveWindow();

		if (v == null) {
			return false;
		}

		if (v instanceof FeatureTableDocumentPanel) {
			return true;
		} else {
			if (v instanceof com.iver.cit.gvsig.project.documents.view.gui.View) {
				com.iver.cit.gvsig.project.documents.view.gui.View view = (com.iver.cit.gvsig.project.documents.view.gui.View) v;
				IProjectView pv = view.getModel();
				FLayer[] seleccionadas = pv.getMapContext().getLayers()
				.getActives();

				if (seleccionadas.length == 1) {
					if (seleccionadas[0].isAvailable() && seleccionadas[0] instanceof FLyrVect) {
						return true;
					}
				}
			}

			return false;
		}

	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean isVisible() {
		IWindow v = PluginServices.getMDIManager().getActiveWindow();

		if (v == null) {
			return false;
		}

		if (v instanceof FeatureTableDocumentPanel) {
			return true;
		} else {
			if (v instanceof com.iver.cit.gvsig.project.documents.view.gui.View) {
				com.iver.cit.gvsig.project.documents.view.gui.View view = (com.iver.cit.gvsig.project.documents.view.gui.View) v;
				IProjectView pv = view.getModel();
				FLayer[] seleccionadas = pv.getMapContext().getLayers()
				.getActives();

				if (seleccionadas.length == 1) {
					if (seleccionadas[0] instanceof FLyrVect) {
						return true;
					}
				}
			}

			return false;
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param expression DOCUMENT ME!
	 */
	public void newSet(String expression) throws DataException {
		// By Pablo: if no filter expression -> no element selected
		if (! this.filterExpressionFromWhereIsEmpty(expression)) {
			try {
				FeatureSet set = doSet(expression);

				if (set == null) {
					//throw new RuntimeException("Not a 'where' clause?");
					return;
				}
				featureStore.setSelection(set);

				set.dispose();
			}catch(Exception e){
				JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(), "Asegurate de que la consulta es correcta.");
			}
		}
		else {
			// By Pablo: if no expression -> no element selected
			featureStore.getFeatureSelection().deselectAll();
		}
	}

	/**
	 * @throws DataException
	 * @see com.iver.cit.gvsig.gui.filter.ExpressionListener#newSet(java.lang.String)
	 */
	private FeatureSet doSet(String expression) throws DataException {
		FeatureQuery query = featureStore.createFeatureQuery();
		DataManager manager = DALLocator.getDataManager();
		query.setFilter(manager.createExpresion(expression));
		return featureStore.getFeatureSet(query);
		//		try {
		//			DataSource ds = LayerFactory.getDataSourceFactory().executeSQL(expression,
		//					DataSourceFactory.MANUAL_OPENING);
		//
		//			return ds.getWhereFilter();
		//		} catch (DriverLoadException e) {
		//			JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),PluginServices.getText(this,"driver_error")+"\n"+e.getMessage());
		//		} catch (ReadDriverException e) {
		//			JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),PluginServices.getText(this,"driver_error")+"\n"+e.getMessage());
		//		} catch (ParseException e) {
		//			JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),PluginServices.getText(this,"parse_expresion_error")+"\n"+e.getMessage());
		//		} catch (SemanticException e) {
		//			JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),PluginServices.getText(this,"semantic_expresion_error")+"\n"+e.getMessage());
		//		} catch (IOException e) {
		//			JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),PluginServices.getText(this,"input_output_error")+"\n"+e.getMessage());
		//		} catch (EvaluationException e) {
		//			JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),PluginServices.getText(this,"parse_expresion_error")+"\n"+e.getMessage());
		//		} catch (com.hardcode.gdbms.parser.TokenMgrError e) {
		//			JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),PluginServices.getText(this,"expresion_error")+"\n"+e.getMessage());
		//		}
		//		return null;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param expression
	 *            DOCUMENT ME!
	 * @throws DataException
	 */
	public void addToSet(String expression) throws DataException {
		// By Pablo: if no filter expression -> don't add more elements to set
		if (! this.filterExpressionFromWhereIsEmpty(expression)) {
			FeatureSet set = null;
			set = doSet(expression);

			if (set == null) {
				//throw new RuntimeException("Not a 'where' clause?");
				return;
			}
			featureStore.getFeatureSelection().select(set);

			set.dispose();

			//			FBitSet selection = new FBitSet();
			//
			//			for (int i = 0; i < sel.length; i++) {
			//				selection.set((int) sel[i]);
			//			}
			//
			//			FBitSet fbs = featureStore.getSelection();
			//			fbs.or(selection);
			//			featureStore.setSelection(fbs);
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param expression DOCUMENT ME!
	 */
	public void fromSet(String expression) throws DataException {
		// By Pablo: if no filter expression -> no element selected
		try{
			if (! this.filterExpressionFromWhereIsEmpty(expression)) {
				NotificationManager.showMessageInfo("Falta por implementar",
						null);

				// FeatureSet set = null;
				// set = doSet(expression);
				//
				// if (set == null) {
				// throw new RuntimeException("Not a 'where' clause?");
				// }
				//
				// FeatureSelection oldSelection = featureStore
				// .getFeatureSelection();
				//
				// FeatureSelection newSelection = featureStore
				// .createFeatureSelection();
				// Iterator iterator = set.iterator();
				// while (iterator.hasNext()) {
				// Feature feature = (Feature) iterator.next();
				// if (oldSelection.isSelected(feature)) {
				// newSelection.select(feature);
				// }
				// }
				// featureStore.setSelection(newSelection);
				// set.dispose();

			} else {
				// By Pablo: if no expression -> no element selected
				featureStore.getFeatureSelection().deselectAll();
				;
			}
		} catch (DataException e) {
			NotificationManager.addError(e);
		}

	}

	/**
	 * Returns true if the WHERE subconsultation of the filterExpression is empty ("")
	 *
	 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
	 * @param expression An string
	 * @return A boolean value
	 */
	private boolean filterExpressionFromWhereIsEmpty(String expression) {
		String subExpression = expression.trim();
		int pos;

		// Remove last ';' if exists
		if (subExpression.charAt(subExpression.length() -1) == ';') {
			subExpression = subExpression.substring(0, subExpression.length() -1).trim();
		}

		// If there is no 'where' clause
		if ((pos = subExpression.indexOf("where")) == -1) {
			return false;
		}

		// If there is no subexpression in the WHERE clause -> true
		subExpression = subExpression.substring(pos + 5, subExpression.length()).trim(); // + 5 is the length of 'where'
		if ( subExpression.length() == 0 ) {
			return true;
		} else {
			return false;
		}
	}
}
