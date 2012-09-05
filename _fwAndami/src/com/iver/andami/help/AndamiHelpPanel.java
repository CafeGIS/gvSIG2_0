package com.iver.andami.help;

import javax.help.HelpSet;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.MDIManager;
import com.iver.andami.ui.mdiManager.WindowInfo;


public class AndamiHelpPanel extends HelpPanel implements IWindow {

	private static final long serialVersionUID = 2683827167020046672L;

	private WindowInfo info = null ;

	public AndamiHelpPanel(HelpSet hs){
		super(hs);
	}

	public AndamiHelpPanel(HelpSet hs, String id){
		super(hs,id);
	}

	public void showWindow() {
		MDIManager mdim = PluginServices.getMDIManager();
		mdim.addWindow((IWindow) this);
	}

	public WindowInfo getWindowInfo() {
		if( info == null ) {
			info = new WindowInfo( WindowInfo.RESIZABLE |
	                WindowInfo.MAXIMIZABLE | WindowInfo.MODELESSDIALOG);
			info.setHeight(HEIGHT);
			info.setWidth(WIDTH);
			info.setTitle(getTitle());
		}
		return info;
	}

	public Object getWindowProfile() {
		return WindowInfo.TOOL_PROFILE;
	}

}
