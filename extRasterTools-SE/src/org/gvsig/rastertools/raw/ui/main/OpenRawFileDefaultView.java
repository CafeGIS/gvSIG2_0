/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
*/
package org.gvsig.rastertools.raw.ui.main;

import java.io.File;

import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.raw.ui.listener.OpenRawFileDefaultViewListener;

import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
/**
 * This class implemens the View interface to add the open raw panel in gvSIG
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class OpenRawFileDefaultView extends OpenRawFileDefaultPanel implements IWindow{
  private static final long serialVersionUID = -686663312171728878L;
	private File imageFile = null;
	
	/**
	 * Constructor. It create and sets the listener for the window buttons.
	 * @param rawFile Raw file name
	 */
	public OpenRawFileDefaultView(String rawFileName){
		super(rawFileName);
		setActionListener(new OpenRawFileDefaultViewListener(this));
		RasterToolsUtil.addWindow(this);	
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG);
		m_viewinfo.setTitle(RasterToolsUtil.getText(this, "open_raw_file"));
		m_viewinfo.setHeight(270);
		m_viewinfo.setWidth(540);
		return m_viewinfo;
	}

	/**
	 * @return Returns the imageFile.
	 */
	public File getImageFile() {
		return imageFile;
	}

	/**
	 * @param imageFile The imageFile to set.
	 */
	public void setImageFile(File imageFile) {
		this.imageFile = imageFile;
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}
}