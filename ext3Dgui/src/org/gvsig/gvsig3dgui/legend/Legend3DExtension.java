package org.gvsig.gvsig3dgui.legend;

import com.iver.ai2.gvsig3d.legend.extrusion.ExtrusionLegendPanel;
import com.iver.ai2.gvsig3d.legend.symbols.Object3DMarker;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.gui.styling.SymbolEditor;
import com.iver.cit.gvsig.project.documents.view.legend.gui.LegendManager;


public class Legend3DExtension extends Extension{
	
	
	public void execute(String actionCommand) {
		
//		com.iver.andami.ui.mdiManager.IWindow window = PluginServices.getMDIManager().getActiveWindow();
//		Table table = (Table)window;
//		if (operators.isEmpty()) {
//			PluginServices.cancelableBackgroundExecution(new EvalOperatorsTask());
//        }else{
//        	 EvalExpressionDialog eed=new EvalExpressionDialog(table,EvalOperatorsTask.interpreter,operators);
//		     PluginServices.getMDIManager().addWindow(eed);
//        }
		
	}

	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	

	@Override
	public void postInitialize() {
		// TODO Auto-generated method stub
		
		//LegendManager.addLegendPage(ExtrusionProve.class);
		LegendManager.addLegendPage(ExtrusionLegendPanel.class);
//		LegendManager.addLegendPage(SingleSymbolLegend3D.class);
		SymbolEditor.addSymbolEditorPanel(Object3DMarker.class,  FShape.POINT);
		
		// Register operation of expresion
//		Prove.registerOperations();
		
	}
	

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isVisible() {
		// TODO Auto-generated method stub
		return true;
	}

}
