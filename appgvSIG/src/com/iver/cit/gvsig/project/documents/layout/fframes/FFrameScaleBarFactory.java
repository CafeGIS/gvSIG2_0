package com.iver.cit.gvsig.project.documents.layout.fframes;

import com.iver.andami.PluginServices;


/**
 * Factory of FFrameScaleBar.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameScaleBarFactory extends FrameFactory {
    public static String registerName = "FFrameScaleBar";


    /**
     * Create a new IFFrame.
     *
     * @return IFFrame.
     */
    public IFFrame createFrame() {
        FFrameScaleBar scale = new FFrameScaleBar();
        scale.setFrameLayoutFactory(this);
        return scale;
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
        return PluginServices.getText(this, "FFrameScaleBar");
    }

    /**
     * Registers in the points of extension the Factory with alias.
     *
     */
    public static void register() {
        register(registerName, new FFrameScaleBarFactory(),
            "com.iver.cit.gvsig.gui.layout.fframes.FFrameScaleBar");

    }

}
