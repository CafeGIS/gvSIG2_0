/*
 * Created on 26-ene-2005
 */
package org.gvsig.crs.gui.dialog;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import org.cresques.cts.IProjection;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.gui.CRSSelectionTrDialog;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.gui.panels.crs.ISelectCrsPanel;

/**
 * @author Luis W. Sevilla (sevilla_lui@gva.es)
 */
public class TRSelectionDialog
	extends CRSSelectionTrDialog implements IWindow, ISelectCrsPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean targetNad = false;
	
	/**
	 * 
	 */
	public TRSelectionDialog(ICrs curproj) {
		super(curproj);
		this.init();		
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void init() {		
		this.setBounds(0, 0, 600, 500);
		this.setLayout(new GridLayout(0,1));
		this.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		
				
	}
	
	public void setTargetNad(boolean tarNad){
		targetNad = tarNad;
	}
	
	public boolean getTargetNad(){
		return targetNad;
	}
	
	public String getProjectionName(){
		return (String) getProjPanel().getProjection().getCrsWkt().getName();				
	}
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo=new WindowInfo(WindowInfo.MODALDIALOG);
   		m_viewinfo.setTitle(PluginServices.getText(this, "crsAndTransformation"));
   		//definir anchura y altura
   		m_viewinfo.setWidth(650);
   		m_viewinfo.setHeight(350);
		return m_viewinfo;
	}
	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}
}