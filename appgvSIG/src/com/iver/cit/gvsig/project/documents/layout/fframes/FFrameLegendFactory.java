package com.iver.cit.gvsig.project.documents.layout.fframes;

import com.iver.andami.PluginServices;


/**
 * Factory of FFrameLegend.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameLegendFactory extends FrameFactory {
    public static String registerName = "FFrameLegend";


    /**
     * Create a new IFFrame.
     *
     * @return IFFrame.
     */
    public IFFrame createFrame() {
        FFrameLegend legend = new FFrameLegend();
        legend.setFrameLayoutFactory(this);
        return legend;
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
        return PluginServices.getText(this, "FFrameLegend");
    }

    /**
     * Registers in the points of extension the Factory with alias.
     *
     */
    public static void register() {
        register(registerName, new FFrameLegendFactory(),
            "com.iver.cit.gvsig.gui.layout.fframes.FFrameLegend");

    }

}
