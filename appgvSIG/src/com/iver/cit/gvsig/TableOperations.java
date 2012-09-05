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
import java.awt.Dimension;

import javax.swing.JOptionPane;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.project.document.table.FeatureTableDocument;
import org.gvsig.project.document.table.FeatureTableDocumentFactory;
import org.gvsig.project.document.table.gui.FeatureTableDocumentPanel;
import org.opengis.feature.FeatureCollection;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.gui.filter.ExpressionListener;
import com.iver.cit.gvsig.project.documents.gui.AndamiWizard;
import com.iver.cit.gvsig.project.documents.gui.ObjectSelectionStep;
import com.iver.cit.gvsig.project.documents.table.FieldSelectionModel;
import com.iver.cit.gvsig.project.documents.table.TableSelectionModel;
import com.iver.utiles.swing.objectSelection.SelectionException;
import com.iver.utiles.swing.wizard.WizardControl;
import com.iver.utiles.swing.wizard.WizardEvent;
import com.iver.utiles.swing.wizard.WizardListener;


/**
 * Extensión que controla las operaciones realizadas sobre las tablas.
 *
 * @author Fernando González Cortés
 */
public class TableOperations extends Extension implements ExpressionListener {
	private FeatureStore featureStore = null;

	/**
	 * @see com.iver.mdiApp.plugins.IExtension#updateUI(java.lang.String)
	 */
	public void execute(String actionCommand) {
		ProjectExtension pe = (ProjectExtension) PluginServices.getExtension(ProjectExtension.class);
		com.iver.cit.gvsig.project.Project project=pe.getProject();
		FeatureTableDocument[] pts = project.getDocumentsByType(FeatureTableDocumentFactory.registerName)
		.toArray(new FeatureTableDocument[0]);
		if ("LINK".equals(actionCommand)) {
			try {
				final ObjectSelectionStep sourceTable = new ObjectSelectionStep();
				sourceTable.setModel(new TableSelectionModel(pts,
						PluginServices.getText(this, "seleccione_tabla_origen")));

				final ObjectSelectionStep targetTable = new ObjectSelectionStep();
				targetTable.setModel(new TableSelectionModel(pts,
						PluginServices.getText(this, "seleccione_tabla_a_enlazar")));

				final ObjectSelectionStep firstTableField = new ObjectSelectionStep();
				final ObjectSelectionStep secondTableField = new ObjectSelectionStep();
				final AndamiWizard wiz = new AndamiWizard(PluginServices.getText(this, "back"), PluginServices.getText(this, "next"), PluginServices.getText(this, "finish"), PluginServices.getText(this, "cancel"));
				wiz.setSize(new Dimension(450,200));
				wiz.addStep(sourceTable);
				wiz.addStep(firstTableField);
				wiz.addStep(targetTable);
				wiz.addStep(secondTableField);

				wiz.addWizardListener(new WizardListener() {
					public void cancel(WizardEvent w) {
						PluginServices.getMDIManager().closeWindow(wiz);
					}

					public void finished(WizardEvent w) {
						PluginServices.getMDIManager().closeWindow(wiz);

						FeatureTableDocument sourceProjectTable = (FeatureTableDocument) sourceTable.getSelected();
						FeatureStore sds1 = sourceProjectTable.getStore();

						FeatureTableDocument targetProjectTable = (FeatureTableDocument) targetTable.getSelected();
						FeatureStore sds2 = targetProjectTable.getStore();

						String field1 = (String) firstTableField.getSelected();
						String field2 = (String) secondTableField.getSelected();
						sourceProjectTable.setLinkTable(sds2.getName(),field1,field2);
						((ProjectExtension)PluginServices.getExtension(ProjectExtension.class)).getProject().setLinkTable();

					}

					public void next(WizardEvent w) {
						WizardControl wiz = w.wizard;
						wiz.enableBack(true);
						wiz.enableNext(((ObjectSelectionStep) wiz.getCurrentStep()).getSelectedItem() != null);

						if (w.currentStep == 1) {
							FeatureTableDocument pt = (FeatureTableDocument) sourceTable.getSelected();

							try {
								firstTableField.setModel(new FieldSelectionModel(
										pt.getStore(),
										PluginServices.getText(this, "seleccione_campo_enlace"),
										DataTypes.STRING));
							} catch (SelectionException e) {
								NotificationManager.addError("Error obteniendo los campos de la tabla",
										e);
							}
						} else if (w.currentStep == 3) {
							try {
								//tabla
								FeatureTableDocument pt = (FeatureTableDocument) sourceTable.getSelected();

								//índice del campo
								FeatureStore fs = pt.getStore();
								String fieldName = (String) firstTableField.getSelected();
								int type = ((FeatureAttributeDescriptor)fs.getDefaultFeatureType().get(fieldName)).getDataType();

								secondTableField.setModel(new FieldSelectionModel(
										((FeatureTableDocument) targetTable
												.getSelected())
												.getStore(),
												PluginServices.getText(this, "seleccione_campo_enlace"),
												type));
							} catch (SelectionException e) {
								NotificationManager.addError("Error obteniendo los campos de la tabla",
										e);
							} catch (DataException e) {
								NotificationManager.addError("Error obteniendo los campos de la tabla",
										e);
							}
						}
					}

					public void back(WizardEvent w) {
						WizardControl wiz = w.wizard;
						wiz.enableBack(true);
						wiz.enableNext(((ObjectSelectionStep) wiz.getCurrentStep()).getSelectedItem() != null);
					}
				});
				project.setModified(true);
				PluginServices.getMDIManager().addWindow(wiz);
			} catch (SelectionException e) {
				NotificationManager.addError("Error abriendo el asistente", e);
			}
		}
	}

	/**
	 * @see com.iver.cit.gvsig.gui.filter.ExpressionListener#newSet(java.lang.String)
	 */
	public void newSet(String expression) throws DataException {
		// By Pablo: if no filter expression -> no element selected
		if (! this.filterExpressionFromWhereIsEmpty(expression)) {
			try {
				FeatureSet set = doSet(expression);

				if (set == null) {
					throw new RuntimeException("Not a 'where' clause?");
				}
				FeatureSelection newSel =featureStore.createFeatureSelection();
				newSel.select(set);
				featureStore.setSelection(newSel);
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
	 * @see com.iver.cit.gvsig.gui.filter.ExpressionListener#newSet(java.lang.String)
	 */
	private FeatureSet doSet(String expression) throws DataException {
		FeatureQuery query = featureStore.createFeatureQuery();
		query
		.setFilter(DALLocator.getDataManager().createExpresion(
				expression));
		return featureStore.getFeatureSet(query);
	}
	/**
	 * @see com.iver.cit.gvsig.gui.filter.ExpressionListener#addToSet(java.lang.String)
	 */
	public void addToSet(String expression) throws DataException {
		// By Pablo: if no filter expression -> don't add more elements to set
		if (! this.filterExpressionFromWhereIsEmpty(expression)) {
			FeatureSet set = null;
			set = doSet(expression);

			featureStore.getFeatureSelection().select(set);
			set.dispose();
		}
	}

	/**
	 * @see com.iver.cit.gvsig.gui.filter.ExpressionListener#fromSet(java.lang.String)
	 */
	public void fromSet(String expression) throws DataException {
		// By Pablo: if no filter expression -> no element selected
		if (!this.filterExpressionFromWhereIsEmpty(expression)) {

			FeatureSet set = null;
			DisposableIterator iterator = null;
			try {
				set = doSet(expression);

				if (set == null) {
					throw new RuntimeException("Not a 'where' clause?");
				}
				FeatureCollection oldSelection = (FeatureCollection) featureStore
				.getSelection();

				FeatureSelection newSelection = featureStore
				.createFeatureSelection();
				iterator = set.iterator();
				while (iterator.hasNext()) {
					Feature feature = (Feature) iterator.next();
					if (oldSelection.contains(feature)) {
						newSelection.select(feature);
					}
				}
				featureStore.setSelection(newSelection);
			} finally {
				if (iterator != null){
					iterator.dispose();
				}
				if (set != null){
					set.dispose();
				}
			}
		} else {
			// By Pablo: if no expression -> no element selected
			featureStore.setSelection(featureStore.createSelection());
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

	/**
	 * @see com.iver.mdiApp.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		IWindow v = PluginServices.getMDIManager().getActiveWindow();

		if (v == null) {
			return false;
		}

		if (v instanceof FeatureTableDocumentPanel) {
			return true;
		} /*else {
			if (v instanceof com.iver.cit.gvsig.gui.View) {
				com.iver.cit.gvsig.gui.View view = (com.iver.cit.gvsig.gui.View) v;
				ProjectView pv = view.getModel();
				FLayer[] seleccionadas = pv.getMapContext().getLayers()
										   .getActives();

				if (seleccionadas.length == 1) {
					if (seleccionadas[0] instanceof AlphanumericData) {
						return true;
					}
				}
			}
		 */
		return false;
		//}
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
		registerIcons();
		//		FIXME
		//		org.gvsig.fmap.data.feature.joinstore.Register.selfRegister();


	}

	private void registerIcons(){
		PluginServices.getIconTheme().registerDefault(
				"table-join",
				this.getClass().getClassLoader().getResource("images/tablejoin.png")
		);

		PluginServices.getIconTheme().registerDefault(
				"table-link",
				this.getClass().getClassLoader().getResource("images/tablelink.png")
		);
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		return true;
	}

	/**
	 * Ensure that field name only has 'safe' characters
	 * (no spaces, special characters, etc).
	 */
	public String sanitizeFieldName(String fieldName) {
		return fieldName.replaceAll("\\W", "_"); // replace any non-word character by an underscore
	}


}
