package com.iver.cit.gvsig.project.documents.layout.fframes;

import com.iver.andami.PluginServices;


/**
 * Factory of FFrameGrid.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameGridFactory extends FrameFactory {
    public static String registerName = "FFrameGrid";


    /**
     * Create a new IFFrame.
     *
     * @return IFFrame.
     */
    public IFFrame createFrame() {
        FFrameGrid grid = new FFrameGrid();
        grid.setFrameLayoutFactory(this);
        return grid;
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
        return PluginServices.getText(this, "FFrameGrid");
    }

    /**
     * Registers in the points of extension the Factory with alias.
     *
     */
    public static void register() {
        register(registerName, new FFrameGridFactory(),
            "com.iver.cit.gvsig.gui.layout.fframes.FFrameGrid");

    }

}
