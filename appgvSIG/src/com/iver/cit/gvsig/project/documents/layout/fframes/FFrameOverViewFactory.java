package com.iver.cit.gvsig.project.documents.layout.fframes;

import com.iver.andami.PluginServices;


/**
 * Factory of FFrameOverView.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameOverViewFactory extends FrameFactory {
    public static String registerName = "FFrameOverView";


    /**
     * Create a new IFFrame.
     *
     * @return IFFrame.
     */
    public IFFrame createFrame() {
        FFrameOverView view = new FFrameOverView();
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
        return PluginServices.getText(this, "FFrameOverView");
    }

    /**
     * Registers in the points of extension the Factory with alias.
     *
     */
    public static void register() {
        register(registerName, new FFrameOverViewFactory(),
            "com.iver.cit.gvsig.gui.layout.fframes.FFrameOverView");

    }

}
