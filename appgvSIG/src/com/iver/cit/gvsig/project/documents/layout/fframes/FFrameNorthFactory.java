package com.iver.cit.gvsig.project.documents.layout.fframes;

import com.iver.andami.PluginServices;


/**
 * Factory of FFrameNorth.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameNorthFactory extends FrameFactory {
    public static String registerName = "FFrameNorth";


    /**
     * Create a new IFFrame.
     *
     * @return IFFrame.
     */
    public IFFrame createFrame() {
        FFrameNorth north = new FFrameNorth();
        north.setFrameLayoutFactory(this);
        return north;
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
        return PluginServices.getText(this, "FFrameNorth");
    }

    /**
     * Registers in the points of extension the Factory with alias.
     *
     */
    public static void register() {
        register(registerName, new FFrameNorthFactory(),
            "com.iver.cit.gvsig.gui.layout.fframes.FFrameNorth");

    }

}
