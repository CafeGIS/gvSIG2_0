package org.gvsig.gvsig3dgui.layout.fframe;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.layout.fframes.FrameFactory;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;

public class FFrameView3DFractory extends FrameFactory {
	public static String registerName = "FFrameView3D";

	@Override
	public IFFrame createFrame() {
		FFrameView3D ffView3D = new FFrameView3D();
		ffView3D.setFrameLayoutFactory(this);
		return ffView3D;
	}

	@Override
	public String getRegisterName() {
		// TODO Auto-generated method stub
		return registerName;
	}

	/**
	 * Returns the name of IFFrame.
	 * 
	 * @return Name of IFFrame.
	 */
	public String getNameType() {
		return PluginServices.getText(this, "FFrameView3D");
	}

	/**
	 * Registers in the points of extension the Factory with alias.
	 * 
	 */
	public static void register() {
		register(registerName, new FFrameView3DFractory(),
				"org.gvsig.gvsig3dgui.layout.FFrameView3D");

	}

}
