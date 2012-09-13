package com.iver.cit.gvsig;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.plaf.OptionPaneUI;

import org.apache.bsf.BSFManager;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.project.document.table.gui.FeatureTableDocumentPanel;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.gui.FieldExpressionPage;
import com.iver.cit.gvsig.project.documents.table.IOperator;
import com.iver.cit.gvsig.project.documents.table.gui.EvalExpressionDialog;
import com.iver.cit.gvsig.project.documents.table.operators.Abs;
import com.iver.cit.gvsig.project.documents.table.operators.Acos;
import com.iver.cit.gvsig.project.documents.table.operators.After;
import com.iver.cit.gvsig.project.documents.table.operators.Area;
import com.iver.cit.gvsig.project.documents.table.operators.Asin;
import com.iver.cit.gvsig.project.documents.table.operators.Atan;
import com.iver.cit.gvsig.project.documents.table.operators.Before;
import com.iver.cit.gvsig.project.documents.table.operators.Ceil;
import com.iver.cit.gvsig.project.documents.table.operators.Cos;
import com.iver.cit.gvsig.project.documents.table.operators.DateToString;
import com.iver.cit.gvsig.project.documents.table.operators.Distinct;
import com.iver.cit.gvsig.project.documents.table.operators.Division;
import com.iver.cit.gvsig.project.documents.table.operators.E;
import com.iver.cit.gvsig.project.documents.table.operators.EndsWith;
import com.iver.cit.gvsig.project.documents.table.operators.Equal;
import com.iver.cit.gvsig.project.documents.table.operators.Equals;
import com.iver.cit.gvsig.project.documents.table.operators.Exp;
import com.iver.cit.gvsig.project.documents.table.operators.Floor;
import com.iver.cit.gvsig.project.documents.table.operators.Geometry;
import com.iver.cit.gvsig.project.documents.table.operators.GetTimeDate;
import com.iver.cit.gvsig.project.documents.table.operators.IndexOf;
import com.iver.cit.gvsig.project.documents.table.operators.IsNumber;
import com.iver.cit.gvsig.project.documents.table.operators.LastIndexOf;
import com.iver.cit.gvsig.project.documents.table.operators.Length;
import com.iver.cit.gvsig.project.documents.table.operators.LessEquals;
import com.iver.cit.gvsig.project.documents.table.operators.LessThan;
import com.iver.cit.gvsig.project.documents.table.operators.Log;
import com.iver.cit.gvsig.project.documents.table.operators.Max;
import com.iver.cit.gvsig.project.documents.table.operators.Min;
import com.iver.cit.gvsig.project.documents.table.operators.Minus;
import com.iver.cit.gvsig.project.documents.table.operators.MoreEquals;
import com.iver.cit.gvsig.project.documents.table.operators.MoreThan;
import com.iver.cit.gvsig.project.documents.table.operators.Perimeter;
import com.iver.cit.gvsig.project.documents.table.operators.Pi;
import com.iver.cit.gvsig.project.documents.table.operators.Plus;
import com.iver.cit.gvsig.project.documents.table.operators.PointX;
import com.iver.cit.gvsig.project.documents.table.operators.PointY;
import com.iver.cit.gvsig.project.documents.table.operators.Pow;
import com.iver.cit.gvsig.project.documents.table.operators.Random;
import com.iver.cit.gvsig.project.documents.table.operators.Replace;
import com.iver.cit.gvsig.project.documents.table.operators.Round;
import com.iver.cit.gvsig.project.documents.table.operators.SetTimeDate;
import com.iver.cit.gvsig.project.documents.table.operators.Sin;
import com.iver.cit.gvsig.project.documents.table.operators.Sqrt;
import com.iver.cit.gvsig.project.documents.table.operators.StartsWith;
import com.iver.cit.gvsig.project.documents.table.operators.SubString;
import com.iver.cit.gvsig.project.documents.table.operators.Tan;
import com.iver.cit.gvsig.project.documents.table.operators.Times;
import com.iver.cit.gvsig.project.documents.table.operators.ToDate;
import com.iver.cit.gvsig.project.documents.table.operators.ToDegrees;
import com.iver.cit.gvsig.project.documents.table.operators.ToLowerCase;
import com.iver.cit.gvsig.project.documents.table.operators.ToNumber;
import com.iver.cit.gvsig.project.documents.table.operators.ToRadians;
import com.iver.cit.gvsig.project.documents.table.operators.ToString;
import com.iver.cit.gvsig.project.documents.table.operators.ToUpperCase;
import com.iver.cit.gvsig.project.documents.table.operators.Trim;
import com.iver.utiles.swing.threads.AbstractMonitorableTask;
/**
 * @author Vicente Caballero Navarro
 */
public class ExpressionFieldExtension extends Extension{
	public static final String JYTHON="jython";
	private static BSFManager interpreter=new BSFManager();
	private FeatureTableDocumentPanel table=null;
	private static ArrayList<IOperator> operators=new ArrayList<IOperator>();
	public void initialize() {
		registerOperations();
		registerIcons();
	}

	public void execute(String actionCommand) {
		com.iver.andami.ui.mdiManager.IWindow window = PluginServices.getMDIManager().getActiveWindow();
		table=(FeatureTableDocumentPanel)window;
		if (!table.getModel().getStore().isEditing()){
			int option=JOptionPane.showConfirmDialog((Component)PluginServices.getMainFrame(), PluginServices.getText(this,"is_not_in_editing_are_you_sure"));
			if (option != JOptionPane.OK_OPTION){
				return;
			}
		}
		if (operators.isEmpty()) {
			PluginServices.cancelableBackgroundExecution(new EvalOperatorsTask());
        }else{
        	 EvalExpressionDialog eed=new EvalExpressionDialog(table,interpreter,operators);
		     PluginServices.getMDIManager().addWindow(eed);
        }
	}
	public void postInitialize() {

	}

	public boolean isEnabled() {
		com.iver.andami.ui.mdiManager.IWindow window = PluginServices.getMDIManager().getActiveWindow();
		if (window instanceof FeatureTableDocumentPanel) {
			FeatureTableDocumentPanel table=(FeatureTableDocumentPanel)window;
			int columnSelected = 0;
			try {
				columnSelected = table.getTablePanel().getTable().getSelectedColumnCount();
			} catch (DataException e) {
				e.printStackTrace();
			}
		    if (columnSelected > 0 && table.getModel().getStore().isEditing()) {
		    	return true;
		    }
		}
		return false;
	}

	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow window = PluginServices.getMDIManager().getActiveWindow();
		if (window instanceof FeatureTableDocumentPanel) {
			return true;
		}
		return false;
	}
	private void registerOperations() {
		ExtensionPoint extensionPoints = ToolsLocator.getExtensionPointManager().add(
				"cad_editing_properties_pages", "");
		extensionPoints.append("fieldExpression", "",new FieldExpressionPage());

		extensionPoints = ToolsLocator.getExtensionPointManager().add(
				"ColumnOperators", "");
		extensionPoints.append(Abs.class.toString(),"",Abs.class);
		extensionPoints.append(Acos.class.toString(),"",Acos.class);
		extensionPoints.append(After.class.toString(),"",After.class);
		extensionPoints.append(Area.class.toString(),"",Area.class);
		extensionPoints.append(Asin.class.toString(),"",Asin.class);
		extensionPoints.append(Atan.class.toString(),"",Atan.class);
		extensionPoints.append(Acos.class.toString(),"",Acos.class);
		extensionPoints.append(Before.class.toString(),"",Before.class);
		extensionPoints.append(Ceil.class.toString(),"",Ceil.class);
		extensionPoints.append(Cos.class.toString(),"",Cos.class);
		extensionPoints.append(DateToString.class.toString(),"",DateToString.class);
		extensionPoints.append(Distinct.class.toString(),"",Distinct.class);
		extensionPoints.append(Division.class.toString(),"",Division.class);
		extensionPoints.append(E.class.toString(),"",E.class);
		extensionPoints.append(EndsWith.class.toString(),"",EndsWith.class);
		extensionPoints.append(Equal.class.toString(),"",Equal.class);
		extensionPoints.append(Equals.class.toString(),"",Equals.class);
		extensionPoints.append(Exp.class.toString(),"",Exp.class);
		extensionPoints.append(Floor.class.toString(),"",Floor.class);
		extensionPoints.append(Geometry.class.toString(),"",Geometry.class);
		extensionPoints.append(GetTimeDate.class.toString(),"",GetTimeDate.class);
		extensionPoints.append(IndexOf.class.toString(),"",IndexOf.class);
		extensionPoints.append(IsNumber.class.toString(),"",IsNumber.class);
		extensionPoints.append(LastIndexOf.class.toString(),"",LastIndexOf.class);
		extensionPoints.append(Length.class.toString(),"",Length.class);
		extensionPoints.append(LessEquals.class.toString(),"",LessEquals.class);
		extensionPoints.append(LessThan.class.toString(),"",LessThan.class);
		extensionPoints.append(Log.class.toString(),"",Log.class);
		extensionPoints.append(Max.class.toString(),"",Max.class);
		extensionPoints.append(Min.class.toString(),"",Min.class);
		extensionPoints.append(Minus.class.toString(),"",Minus.class);
		extensionPoints.append(MoreEquals.class.toString(),"",MoreEquals.class);
		extensionPoints.append(MoreThan.class.toString(),"",MoreThan.class);
		extensionPoints.append(Perimeter.class.toString(),"",Perimeter.class);
		extensionPoints.append(Pi.class.toString(),"",Pi.class);
		extensionPoints.append(Plus.class.toString(),"",Plus.class);
		extensionPoints.append(PointX.class.toString(),"",PointX.class);
		extensionPoints.append(PointY.class.toString(),"",PointY.class);
		extensionPoints.append(Pow.class.toString(),"",Pow.class);
		extensionPoints.append(Random.class.toString(),"",Random.class);
		extensionPoints.append(Replace.class.toString(),"",Replace.class);
		extensionPoints.append(Round.class.toString(),"",Round.class);
		extensionPoints.append(SetTimeDate.class.toString(),"",SetTimeDate.class);
		extensionPoints.append(Sin.class.toString(),"",Sin.class);
		extensionPoints.append(Sqrt.class.toString(),"",Sqrt.class);
		extensionPoints.append(StartsWith.class.toString(),"",StartsWith.class);
		extensionPoints.append(SubString.class.toString(),"",SubString.class);
		extensionPoints.append(Tan.class.toString(),"",Tan.class);
		extensionPoints.append(Times.class.toString(),"",Times.class);
		extensionPoints.append(ToDate.class.toString(),"",ToDate.class);
		extensionPoints.append(ToDegrees.class.toString(),"",ToDegrees.class);
		extensionPoints.append(ToLowerCase.class.toString(),"",ToLowerCase.class);
		extensionPoints.append(ToNumber.class.toString(),"",ToNumber.class);
		extensionPoints.append(ToRadians.class.toString(),"",ToRadians.class);
		extensionPoints.append(ToString.class.toString(),"",ToString.class);
		extensionPoints.append(ToUpperCase.class.toString(),"",ToUpperCase.class);
		extensionPoints.append(Trim.class.toString(),"",Trim.class);
	}

	 private void registerIcons(){
		 PluginServices.getIconTheme().registerDefault(
					"ext-kcalc",
					this.getClass().getClassLoader().getResource("images/kcalc.png")
				);

		 PluginServices.getIconTheme().registerDefault(
					"field-expression-kcalc",
					this.getClass().getClassLoader().getResource("images/FieldExpression.png")
				);
	 }

	 private class EvalOperatorsTask extends AbstractMonitorableTask{
	    	private ExtensionPoint extensionPoint;
	    	public EvalOperatorsTask(){
				setInitialStep(0);
				setDeterminatedProcess(true);
				setStatusMessage(PluginServices.getText(this, "charging_operators")+"...");
				this.extensionPoint = ToolsLocator.getExtensionPointManager().get(
					"ColumnOperators");
				setFinalStep(extensionPoint.getCount()+1);
	    	}
			public void run() throws Exception {
				NotificationManager.addInfo(PluginServices.getText(this,"charging_operators"));
				long t1=System.currentTimeMillis();
			        Iterator iterator = extensionPoint.iterator();
			        while (iterator.hasNext()) {
			            try {
			            	if (isCanceled())
								return;
			            	org.gvsig.tools.extensionpoint.ExtensionPoint.Extension obj= (org.gvsig.tools.extensionpoint.ExtensionPoint.Extension)iterator.next();
			                IOperator operator = (IOperator)extensionPoint.create(obj.getName());

			                operator.eval(interpreter);
			                operators.add(operator);
			                reportStep();
			                setNote(operator.getClass().getName());
			            } catch (InstantiationException e) {
			                e.printStackTrace();
			            } catch (IllegalAccessException e) {
			                e.printStackTrace();
			            } catch (ClassCastException e) {
			                e.printStackTrace();
			            }
			        }
			        long t2=System.currentTimeMillis();
			        System.out.println("Tiempo en evaluar y crear del extension point = "+(t2-t1) );
			        long t3=System.currentTimeMillis();
			        System.out.println("Tiempo en añadir los operadores correctos = "+(t3-t2) );
			        reportStep();
			}
			public void finished() {
				if (isCanceled())
					return;
				NotificationManager.addInfo(PluginServices.getText(this,"charged_operators"));
				EvalExpressionDialog eed=new EvalExpressionDialog(table,interpreter,operators);
		        PluginServices.getMDIManager().addWindow(eed);
			}

	 }
}
