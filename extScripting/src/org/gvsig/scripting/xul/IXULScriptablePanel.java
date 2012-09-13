package org.gvsig.scripting.xul;

import org.apache.bsf.BSFException;

public interface IXULScriptablePanel extends IXULPanel {
	
	public void invokeMethod(Object component, String method);
	
	public XULScriptablePanel createPanel() throws BSFException ;
	
}
