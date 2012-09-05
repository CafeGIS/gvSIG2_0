package com.iver.cit.gvsig.project.documents.layout.fframes;

import com.iver.andami.PluginServices;


/**
 * Factory of FFrameGraphics.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameGraphicsFactory extends FrameFactory {
    public static String registerName = "FFrameGraphics";


    /**
     * Create a new IFFrame.
     *
     * @return IFFrame.
     */
    public IFFrame createFrame() {
        FFrameGraphics graphics = new FFrameGraphics();
        graphics.setFrameLayoutFactory(this);
        return graphics;
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
        return PluginServices.getText(this, "FFrameGraphics");
    }

    /**
     * Registers in the points of extension the Factory with alias.
     *
     */
    public static void register() {
        register(registerName, new FFrameGraphicsFactory(),
            "com.iver.cit.gvsig.gui.layout.fframes.FFrameGraphics");

    }

}
