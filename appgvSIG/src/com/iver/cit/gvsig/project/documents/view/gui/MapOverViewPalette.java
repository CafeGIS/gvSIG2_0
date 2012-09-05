package com.iver.cit.gvsig.project.documents.view.gui;

import javax.swing.JPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowTransform;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.MapOverview;

public class MapOverViewPalette extends JPanel implements IWindow, IWindowTransform{

	private MapOverview mov;
	private View view;
	public MapOverViewPalette(MapOverview mapOverView,IView view) {
		super();
		mov = mapOverView;
		this.view=(View)view;
		initialize();
		this.add(mov);
	}


	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setBackground(java.awt.SystemColor.control);
		this.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.gray,0));
	}

	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.ICONIFIABLE
				| WindowInfo.MODELESSDIALOG | WindowInfo.PALETTE);
		m_viewinfo.setTitle(PluginServices.getText(this, "localizador"));
		m_viewinfo.setWidth(mov.getWidth()+20);
		m_viewinfo.setHeight(mov.getHeight());

		return m_viewinfo;
	}


	public void toPalette() {
		view.toPalette();

	}


	public void restore() {
		view.restore();

	}


	public boolean isPalette() {
		return true;
	}

	public Object getWindowProfile() {
		return WindowInfo.TOOL_PROFILE;
	}

}
