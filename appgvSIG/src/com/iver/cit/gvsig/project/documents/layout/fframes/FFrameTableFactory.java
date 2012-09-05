package com.iver.cit.gvsig.project.documents.layout.fframes;

import com.iver.andami.PluginServices;


/**
 * Factory of FFrameTable.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameTableFactory extends FrameFactory {
    public static String registerName = "FFrameTable";


    /**
     * Create a new IFFrame.
     *
     * @return IFFrame.
     */
    public IFFrame createFrame() {
        FFrameTable table = new FFrameTable();
        table.setFrameLayoutFactory(this);
        return table;
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
        return PluginServices.getText(this, "FFrameTable");
    }

    /**
     * Registers in the points of extension the Factory with alias.
     *
     */
    public static void register() {
        register(registerName, new FFrameTableFactory(),
            "com.iver.cit.gvsig.gui.layout.fframes.FFrameTable");

    }

}
