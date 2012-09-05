package com.iver.cit.gvsig.project.documents.layout.fframes;

import com.iver.andami.PluginServices;


/**
 * Factory of FFramePicture.
 *
 * @author Vicente Caballero Navarro
 */
public class FFramePictureFactory extends FrameFactory {
    public static String registerName = "FFramePicture";


    /**
     * Create a new IFFrame.
     *
     * @return IFFrame.
     */
    public IFFrame createFrame() {
        FFramePicture picture = new FFramePicture();
        picture.setFrameLayoutFactory(this);
        return picture;
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
        return PluginServices.getText(this, "FFramePicture");
    }

    /**
     * Registers in the points of extension the Factory with alias.
     *
     */
    public static void register() {
        register(registerName, new FFramePictureFactory(),
            "com.iver.cit.gvsig.gui.layout.fframes.FFramePicture");

    }

}
