package com.iver.cit.gvsig.project.documents.layout.fframes;

import com.iver.andami.PluginServices;


/**
 * Factory of FFrameGroup.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameGroupFactory extends FrameFactory {
    public static String registerName = "FFrameGroup";


    /**
     * Create a new IFFrame.
     *
     * @return IFFrame.
     */
    public IFFrame createFrame() {
        FFrameGroup group = new FFrameGroup();
        group.setFrameLayoutFactory(this);
        return group;
    }

    /**
     * Returns the name of registration in the point of extension.
     *
     * @return Name of registration
     */
    public String getRegisterName() {
        return registerName;
    }

    /**
     * Returns the name of IFFrame.
     *
     * @return Name of IFFrame.
     */
    public String getNameType() {
        return PluginServices.getText(this, "FFrameGroup");
    }

    /**
     * Registers in the points of extension the Factory with alias.
     *
     */
    public static void register() {
        register(registerName, new FFrameGroupFactory(),
            "com.iver.cit.gvsig.gui.layout.fframes.FFrameGroup");

    }

}
