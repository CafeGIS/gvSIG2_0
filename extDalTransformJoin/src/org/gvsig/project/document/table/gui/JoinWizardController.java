/**
 *
 */
package org.gvsig.project.document.table.gui;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ImageIcon;

import jwizardcomponent.FinishAction;

import org.gvsig.app.join.JoinToolExtension;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.project.document.table.FeatureTableDocument;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.gui.simpleWizard.SimpleWizard;
import com.iver.cit.gvsig.project.documents.table.FieldSelectionModel;

public class JoinWizardController {
	private final JoinToolExtension tableOperations;

	/**
	 * @param tableOperations
	 */
	public JoinWizardController(JoinToolExtension tableOperations) {
		this.tableOperations = tableOperations;
	}


	public void runWizard(FeatureTableDocument[] pts) {
		// create wizard
		ImageIcon logo = PluginServices.getIconTheme().get("table-join");
		final SimpleWizard wizard = new SimpleWizard(logo);
		wizard.getWindowInfo().setTitle(PluginServices.getText(this, "Table_Join"));

		// create first step (source table)
		final TableWizardStep srcTableWzrd = new TableWizardStep(wizard.getWizardComponents(), "Title" );
		srcTableWzrd.getHeaderLbl().setText(PluginServices.getText(this,"Source_table_options"));
		srcTableWzrd.getTableNameLbl().setText(PluginServices.getText(this,"Source_table_"));
		srcTableWzrd.getFieldNameLbl().setText(PluginServices.getText(this,"Field_to_use_for_JOIN_"));
		srcTableWzrd.getFieldPrefixLbl().setText(PluginServices.getText(this,"Field_prefix_"));
		srcTableWzrd.getTableNameCmb().addItemListener(
				new ItemListener() {

					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange()==e.SELECTED) {
							FeatureTableDocument pt = (FeatureTableDocument) srcTableWzrd.getTableNameCmb().getSelectedItem();

							srcTableWzrd.setFieldModel(new FieldSelectionModel(
									pt.getStore(),
									PluginServices.getText(this, "seleccione_campo_enlace"),
									-1));
							srcTableWzrd.getFieldPrefixTxt().setText(tableOperations.sanitizeFieldName(pt.getName()));
						}

					}
				}
		);
		for (int i=0; i<pts.length; i++) {
			srcTableWzrd.getTableNameCmb().addItem(pts[i]);
		}

		// create second step (target table)
		final TableWizardStep targTableWzrd = new TableWizardStep(wizard.getWizardComponents(), "Title" );
		targTableWzrd.getHeaderLbl().setText(PluginServices.getText(this,"Target_table_options"));
		targTableWzrd.getTableNameLbl().setText(PluginServices.getText(this,"Target_table_"));
		targTableWzrd.getFieldNameLbl().setText(PluginServices.getText(this,"Field_to_use_for_JOIN_"));
		targTableWzrd.getFieldPrefixLbl().setText(PluginServices.getText(this,"Field_prefix_"));
		targTableWzrd.getTableNameCmb().addItemListener(
				new ItemListener() {

					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange()==e.SELECTED) {
							try {
								//tabla
								FeatureTableDocument sourcePt = (FeatureTableDocument) srcTableWzrd.getTableNameCmb().getSelectedItem();
								FeatureTableDocument targetPt = (FeatureTableDocument) targTableWzrd.getTableNameCmb().getSelectedItem();
								targTableWzrd.getFieldPrefixTxt().setText(tableOperations.sanitizeFieldName(targetPt.getName()));

								//índice del campo
								FeatureStore sds = sourcePt.getStore();
								String fieldName = (String) srcTableWzrd.getFieldNameCmb().getSelectedItem();
								FeatureAttributeDescriptor fad=sds.getDefaultFeatureType().getAttributeDescriptor(fieldName);
//								int fieldIndex = sds.getFieldIndexByName(fieldName);
//								if (fieldIndex!=-1) {
//									int type = sds.getFieldType(fieldIndex);
									targTableWzrd.setFieldModel(new FieldSelectionModel(
											targetPt.getStore(),
											PluginServices.getText(this, "seleccione_campo_enlace"),
											fad.getDataType()));
//								}
//								else {
//									NotificationManager.addError(PluginServices.getText(this, "Error_getting_table_fields")
//											, new Exception());
//								}
							} catch (ReadException e2) {
								NotificationManager.addError(PluginServices.getText(this, "Error_getting_table_fields"),
										e2);
							} catch (DataException e2) {
								NotificationManager.addError(PluginServices.getText(this, "Error_getting_table_fields"),
										e2);
							}
						}

					}
				}
		);
		for (int i=0; i<pts.length; i++) {
			targTableWzrd.getTableNameCmb().addItem(pts[i]);
		}

		// add steps and configure wizard
		wizard.getWizardComponents().addWizardPanel(srcTableWzrd);
		wizard.getWizardComponents().addWizardPanel(targTableWzrd);
		wizard.getWizardComponents().updateComponents();
		wizard.setSize(new Dimension(450, 230));
		wizard.getWizardComponents().setFinishAction(new FinishAction(wizard.getWizardComponents()) {
			public void performAction() {
				FeatureTableDocument sourceProjectTable = (FeatureTableDocument) srcTableWzrd.getTableNameCmb().getSelectedItem();
				String field1 = (String) srcTableWzrd.getFieldNameCmb().getSelectedItem();
				String prefix1 = srcTableWzrd.getFieldPrefixTxt().getText();
				if (sourceProjectTable==null || field1==null || prefix1 == null) {
					NotificationManager.showMessageError(
							PluginServices.getText(this, "Join_parameters_are_incomplete"), new InvalidParameterException());
					return;
				}
				FeatureTableDocument targetProjectTable = (FeatureTableDocument) targTableWzrd.getTableNameCmb().getSelectedItem();
				String field2 = (String) targTableWzrd.getFieldNameCmb().getSelectedItem();
				String prefix2 = targTableWzrd.getFieldPrefixTxt().getText();
				if (targetProjectTable==null || field2==null || prefix2 == null) {
					NotificationManager.showMessageError(
							PluginServices.getText(this, "Join_parameters_are_incomplete"), new InvalidParameterException());
					return;
				}
				tableOperations.execJoin(sourceProjectTable, field1, prefix1, targetProjectTable, field2, prefix2);

				PluginServices.getMDIManager().closeWindow(wizard);
			}
		}
		);

		// show the wizard
		PluginServices.getMDIManager().addWindow(wizard);
	}

	private class InvalidParameterException extends Exception {
	}
}