package com.iver.cit.gvsig.project.documents.table.operators;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.ExpressionFieldExtension;
import com.iver.cit.gvsig.project.documents.table.AbstractOperator;
import com.iver.cit.gvsig.project.documents.table.IOperator;

/**
 * @author fdiaz
 */
public class DateToString extends AbstractOperator{

	public String addText(String s) {
		return toString()+"("+s+")";
	}
	public String toString() {
		return "dateToString";
	}
	public void eval(BSFManager interpreter) throws BSFException {
//		interpreter.eval(ExpressionFieldExtension.BEANSHELL,null,-1,-1,"String toString(java.lang.Object value){" +
//				"if (value instanceof java.util.Date)" +
//					"return ((java.util.Date)value).toString();" +
//				"return String.valueOf(value);};");
		interpreter.exec(ExpressionFieldExtension.JYTHON,null,-1,-1,
				"import java.util.Date as Date\n"+
				"import java.text.DateFormat as DateFormat\n"+
				"import java.text.SimpleDateFormat as SimpleDateFormat\n" +
				"import java.text.NumberFormat as NumberFormat\n"+
				"dateFormat = DateFormat.getInstance()\n"+
				"myDateFormat = SimpleDateFormat()\n" +
				"def dateToString(value, format=None):\n" +
				"  if value == None:\n"+
				"    return None\n"+
				"  if value == '':\n"+
				"    return ''\n"+
				"  if isinstance(value,Date):\n"+
				"    if format != None:\n"+
				"      myDateFormat.applyPattern(format)\n"+
				"      return myDateFormat.format(value)\n"+
				"    else:\n"+
				"      return dateFormat.format(value)\n"+
				"  else:\n"+
				"    raise InputError\n"+
				"  return str(value)");
	}
	public boolean isEnable() {
		return (getType()==IOperator.DATE);
	}

	public String getTooltip(){
		return PluginServices.getText(this,"operator")+":  "+toString()+"("+PluginServices.getText(this,"parameter")+"[,"+PluginServices.getText(this,"format")+"])"+"\n"+getDescription();
	}

	public String getDescription() {
	    return PluginServices.getText(this, "parameter") + ": " +
	    PluginServices.getText(this, "date_value") + "\n" +
		PluginServices.getText(this, "format") + " ("+PluginServices.getText(this, "optional")+"): " +
	    PluginServices.getText(this, "string_value") + "\n" +
	    PluginServices.getText(this, "returns") + ": " +
	    PluginServices.getText(this, "string_value") + "\n" +
	    PluginServices.getText(this, "description") + ": " +
	    "Returns the string representation of the Object date parameter\n" +
	    "formatted according to the parameter format, if it is supplied.\n\n"+
	    "The format should follow the specifications of\n" +
	    "'http://java.sun.com/j2se/1.4.2/docs/api/java/text/SimpleDateFormat.html'.\n\n"+
	    "Examples: (for the date of December 23, 2008)\n"+
	    "  'dd/MM/yy'     23/12/08\n"+
	    "  'dd/MM/yyyy'   23/12/2008\n"+
	    "  'dd/MMM/yyyy'  23/dec/2008\n"+
	    "  'dd/MMMM/yyyy' 23/december/2008\n";
	}
}
