package com.iver.cit.gvsig.project.documents.layout.fframes;

import com.iver.andami.PluginServices;


/**
 * Factory of FFrameBasic.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameBasicFactory extends FrameFactory {
    public static String registerName = "FFrameBasic";


    /**
     * Create a new IFFrame.
     *
     * @return IFFrame.
     */
    public IFFrame createFrame() {
        FFrameBasic basic = new FFrameBasic();
        basic.setFrameLayoutFactory(this);
        return basic;
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
        return PluginServices.getText(this, "FFrameBasic");
    }

    /**
     * Registers in the points of extension the Factory with alias.
     *
     */
    public static void register() {
        register(registerName, new FFrameBasicFactory(),
            "com.iver.cit.gvsig.gui.layout.fframes.FFrameBasic");

    }

}
