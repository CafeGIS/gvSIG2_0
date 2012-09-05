package com.iver.cit.gvsig.project.documents.layout.fframes;

import com.iver.andami.PluginServices;


/**
 * Factory of FFrameView.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameViewFactory extends FrameFactory {
    public static String registerName = "FFrameView";


    /**
     * Create a new IFFrame.
     *
     * @return IFFrame.
     */
    public IFFrame createFrame() {
        FFrameView view = new FFrameView();
        view.setFrameLayoutFactory(this);
        return view;
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
        return PluginServices.getText(this, "FFrameView");
    }

    /**
     * Registers in the points of extension the Factory with alias.
     *
     */
    public static void register() {
        register(registerName, new FFrameViewFactory(),
            "com.iver.cit.gvsig.gui.layout.fframes.FFrameView");

    }

}
