package com.iver.cit.gvsig.project.documents.layout.fframes;

import com.iver.andami.PluginServices;


/**
 * Factory of FFrameSymbol.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameSymbolFactory extends FrameFactory {
    public static String registerName = "FFrameSymbol";


    /**
     * Create a new IFFrame.
     *
     * @return IFFrame.
     */
    public IFFrame createFrame() {
        FFrameSymbol symbol = new FFrameSymbol();
        symbol.setFrameLayoutFactory(this);
        return symbol;
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
        return PluginServices.getText(this, "FFrameSymbol");
    }

    /**
     * Registers in the points of extension the Factory with alias.
     *
     */
    public static void register() {
        register(registerName, new FFrameSymbolFactory(),
            "com.iver.cit.gvsig.gui.layout.fframes.FFrameSymbol");

    }

}
