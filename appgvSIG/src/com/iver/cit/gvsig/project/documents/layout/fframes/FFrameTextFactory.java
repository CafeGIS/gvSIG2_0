package com.iver.cit.gvsig.project.documents.layout.fframes;

import com.iver.andami.PluginServices;


/**
 * Factory of FFrameText.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameTextFactory extends FrameFactory {
    public static String registerName = "FFrameText";


    /**
     * Create a new IFFrame.
     *
     * @return IFFrame.
     */
    public IFFrame createFrame() {
        FFrameText text = new FFrameText();
        text.setFrameLayoutFactory(this);
        return text;
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
        return PluginServices.getText(this, "FFrameText");
    }

    /**
     * Registers in the points of extension the Factory with alias.
     *
     */
    public static void register() {
        register(registerName, new FFrameTextFactory(),
            "com.iver.cit.gvsig.gui.layout.fframes.FFrameText");

    }

}
