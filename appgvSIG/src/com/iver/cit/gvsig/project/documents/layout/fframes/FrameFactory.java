package com.iver.cit.gvsig.project.documents.layout.fframes;

import java.util.Iterator;
import java.util.Map;

import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionBuilder;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPoint.Extension;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;

/**
 * Factory of FFrame.
 *
 * @author Vicente Caballero Navarro
 */
public abstract class FrameFactory implements ExtensionBuilder {

	public static IFFrame createFrameFromName(String s) {

		Iterator<Extension> iterator = ToolsLocator.getExtensionPointManager()
				.get(
				"FFrames").iterator();
		while (iterator.hasNext()) {
			try {
				FrameFactory frameFactory = (FrameFactory) iterator.next()
						.create();
				if (frameFactory.getRegisterName().equals(s)) {
					IFFrame frame=frameFactory.createFrame();
					if (frame == null) {
						return null;
					}
					frame.setFrameLayoutFactory(frameFactory);
					return frame;
				}
			} catch (Exception e) {
				NotificationManager.addError(e);
			}
		}
		return null;
	}


    /**
     * Returns the name of FFrame.
     *
     * @return Name of fframe.
     */
    public String getNameType() {
        return PluginServices.getText(this, "frame");
    }

    /**
     * Create a new IFFrame.
     *
     * @return IFFrame.
     */
    public abstract IFFrame createFrame();

    /**
     * Returns the name of registration in the point of extension.
     *
     * @return Name of registration
     */
    public abstract String getRegisterName();

    /**
     * Create a FrameLayoutFactory.
     *
     * @return FrameLayoutFactory.
     */
    public Object create() {
        return this;
    }

    /**
     * Create a FrameLayoutFactory.
     *
     * @param args
     *
     * @return FrameLayoutFactory.
     */
    public Object create(Object[] args) {
        return this;
    }

    /**
     * Create a FrameLayoutFactory.
     *
     * @param args
     *
     * @return FrameLayoutFactory.
     */
    public Object create(Map args) {
        return this;
    }

	/**
	 * Registers in the points of extension the Factory with alias.
	 * 
	 * @param registerName
	 *            Register name.
	 * @param obj
	 *            FrameFactory to register.
	 * @param alias
	 *            Alias.
	 */
    public static void register(String registerName, FrameFactory obj,
			String alias) {
    	ExtensionPoint ep = ToolsLocator.getExtensionPointManager().add(
				"FFrames");
        ep.append(registerName, "", obj);
        if (alias != null){
        	ep.addAlias(registerName, alias);
        }
    }

	/**
	 * Registers in the points of extension the Factory
	 * 
	 * @param registerName
	 *            Register name.
	 * @param obj
	 *            FrameFactory to register.
	 */
    public static void register(String registerName, FrameFactory obj) {
    	register(registerName, obj, null);
    }
}
