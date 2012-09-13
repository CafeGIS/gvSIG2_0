package org.gvsig.scripting.xul;

import java.io.File;

import org.apache.bsf.BSFException;
import org.gvsig.fmap.mapcontext.layers.operations.XMLItem;

import com.iver.cit.gvsig.project.documents.view.info.gui.IXULInfoToolSupport;

public class XULInfoToolSupport extends XULScriptableJPanel implements  IXULInfoToolSupport{

	private static final long serialVersionUID = 3943484157036349337L;

	public void show(String s) {
		
	}
	
	public void refreshSize() {
		
	}
	
	public void show(XMLItem item) {
		this.put("infoData",item);
		String path = "r'" + new File(getXULFile()).getParent() + "'";
	    try {
	    	getThinlet().getBSFManager().exec(
	    		XULScriptablePanel.JYTHON,
	    		"Java:JPanelThinlet.show(XMLItem)",
	    		1,
	    		1,
	    		"import sys\n" +
	    		"try:\n" +
	    		"  sys.path.remove("+path+")\n" +
	    		"except:\n" +
	    		"  pass\n" +
	    		"sys.path.insert(1,"+path+")\n"
	    	);
        } catch(BSFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
		Object root = getThinlet().getItem(getThinlet().getDesktop(),0);
		getThinlet().invokeMethod(root,"show");
	}
	

}
