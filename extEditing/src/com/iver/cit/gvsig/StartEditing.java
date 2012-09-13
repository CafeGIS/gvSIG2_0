package com.iver.cit.gvsig;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.mapcontext.exceptions.StartEditionLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontext.rendering.legend.ILegend;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.project.document.table.FeatureTableDocument;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.gui.cad.CADTool;
import com.iver.cit.gvsig.gui.tokenmarker.ConsoleToken;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.XMLException;
import com.iver.utiles.console.jedit.KeywordMap;
import com.iver.utiles.console.jedit.Token;

/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class StartEditing extends Extension {

//	private class MyAction extends AbstractAction
//	{
//
//		public void actionPerformed(ActionEvent e) {
//			System.err.println("F3");
//		}
//
//	}

	//View vista;
	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String actionCommand) {
		CADExtension.initFocus();
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
		.getActiveWindow();

		if (f instanceof View) {
			View vista = (View) f;

			MapControl mapControl = vista.getMapControl();
			mapControl.initializeSnapping();
			IProjectView model = vista.getModel();
			FLayer[] actives = model.getMapContext().getLayers().getActives();

			boolean bEditingStarted = false;
			if (actives.length == 1 &&	actives[0] instanceof FLyrVect) {

				vista.showConsole();
				EditionManager editionManager=CADExtension.getEditionManager();
				editionManager.setMapControl(mapControl);
				/*
				 * for (int j = 0; j < i; j++) {
				 * layers.getLayer(j).setVisible(false); }
				 */

				FLyrVect lv = (FLyrVect) actives[0];
				// lv.setVisible(true);
				lv.addLayerListener(editionManager);
				try {
					ILegend legendOriginal=lv.getLegend().cloneLegend();

					if (!lv.isWritable())
					{
						JOptionPane.showMessageDialog(
								(Component) PluginServices.getMDIManager().getActiveWindow(),
								PluginServices.getText(this, "this_layer_is_not_self_editable"),
								PluginServices.getText(this, "warning_title"),
								JOptionPane.WARNING_MESSAGE);
					}

					lv.setEditing(true);
//					FeatureType featureType=lv.getFeatureStore().getDefaultFeatureType();
//					featureType.getRules().clear();
//					if (lv.getShapeType()==Geometry.TYPES.SURFACE){
//						FeatureRule rulePol = new FeatureRulePolygon("PolygonRule", "Sentido correcto de un pol�gono");
//						featureType.addRule(rulePol);
//					}

//					if (!(lv.getSource().getDriver() instanceof IndexedShpDriver)){
//						VectorialLayerEdited vle=(VectorialLayerEdited)editionManager.getLayerEdited(lv);
//						vle.setLegend(legendOriginal);
//					}
					lv.getFeatureStore().addObserver(mapControl);
//					vea.getCommandRecord().addCommandListener(mapControl);
					// Si existe una tabla asociada a esta capa se cambia su
					// modelo por el VectorialEditableAdapter.
					ProjectExtension pe = (ProjectExtension) PluginServices
					.getExtension(ProjectExtension.class);
					FeatureTableDocument pt = pe.getProject().getTable(lv);
					if (pt != null){
						pt.setStore(lv.getFeatureStore());
//						changeModelTable(pt,vea);
					}

					startCommandsApplicable(vista,lv);
					vista.repaintMap();

				} catch (XMLException e) {
					NotificationManager.addError(e.getMessage(),e);
				} catch (StartEditionLayerException e) {
					NotificationManager.addError(e.getMessage(),e);
				} catch (ReadException e) {
					NotificationManager.addError(e.getMessage(),e);
				} catch (DataException e) {
					NotificationManager.addError(e.getMessage(),e);
				}

//				return;
			}
		}

		/*
		 * PluginServices.getMDIManager().setWaitCursor(); try { if
		 * (((FLyrVect) capa).getSource().getDriver().getClass() ==
		 * DXFCadDriver.class) { if (JOptionPane.showConfirmDialog(
		 * (Component) PluginServices.getMainFrame(), "Todas las geometr�as
		 * del formato DXF no se pueden editar, de momento podemos editar:
		 * Line, Point, Polyline, Arc, Circle y Ellipse. \n El resto de
		 * geometr�as se perder�n con la edici�n. \n �Desea continuar?") ==
		 * JOptionPane.YES_OPTION) { capa.startEdition();
		 * vista.getMapControl().setCadTool("selection"); } else { } } else {
		 * capa.startEdition();
		 * vista.getMapControl().setCadTool("selection"); } } catch
		 * (EditionException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * PluginServices.getMDIManager().restoreCursor();
		 */
		// vista.getMapControl().drawMap(false);
	}

//	 private void registerKeyStrokes() {
//		 JComponent theComponent = vista.getConsolePanel().getTxt();
//
//		 // The actions
//		 Action F3Action = new AbstractAction("REFENT") {
//			public void actionPerformed(ActionEvent evt) {
//				System.err.println("SOY F3");
//			}
//		};
//
//		 InputMap inputMap = theComponent.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//		 inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), F3Action.getValue(Action.NAME));
//
//		 ActionMap actionMap = theComponent.getActionMap();
//		 // actionMap.put("REFENT", new MyAction());
//		 actionMap.put(F3Action.getValue(Action.NAME), F3Action);
//
//	}

	public static void startCommandsApplicable(View vista,FLyrVect lv) {
		if (vista==null)
			vista=(View)PluginServices.getMDIManager().getActiveWindow();
		CADTool[] cadtools = CADExtension.getCADTools();
		KeywordMap keywordMap = new KeywordMap(true);
		for (int i = 0; i < cadtools.length; i++) {
			try {
				if (cadtools[i].isApplicable(lv.getShapeType())){
					keywordMap.add(cadtools[i].getName(), Token.KEYWORD2);
					keywordMap.add(cadtools[i].toString(), Token.KEYWORD3);
				}
			} catch (ReadException e) {
				NotificationManager.addError(e.getMessage(),e);
			}

		}
		ConsoleToken consoletoken = new ConsoleToken(keywordMap);
		vista.getConsolePanel().setTokenMarker(consoletoken);

	}

//	private void changeModelTable(ProjectTable pt, VectorialEditableAdapter vea){
//   	 com.iver.andami.ui.mdiManager.IWindow[] views = PluginServices.getMDIManager().getAllWindows();
//
//		for (int i=0 ; i<views.length ; i++){
//			if (views[i] instanceof Table){
//				Table table=(Table)views[i];
//				ProjectTable model =table.getModel();
//				if (model.equals(pt)){
//						table.setModel(pt);
//						vea.getCommandRecord().addCommandListener(table);
//				}
//			}
//		}
//   }
	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		View f = (View) PluginServices.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}

		FLayer[] selected = f.getModel().getMapContext().getLayers()
				.getActives();
		if (selected.length == 1 && selected[0].isAvailable() && selected[0] instanceof FLyrVect) {
			if (selected[0].isEditing())
				return false;
			return true;
		}
		return false;
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
				.getActiveWindow();

		if (f == null) {
			return false;
		}

		if (f instanceof View)
			return true;
		return false;
	}
}
