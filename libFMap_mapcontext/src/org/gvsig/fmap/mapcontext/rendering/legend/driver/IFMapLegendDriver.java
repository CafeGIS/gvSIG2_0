


package org.gvsig.fmap.mapcontext.rendering.legend.driver;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.rendering.legend.ILegend;

public interface IFMapLegendDriver {

	boolean accept(File f);

	String getDescription();

	String getFileExtension();

	ArrayList getSupportedVersions();

	void write(FLayers layers, FLayer layer, ILegend legend, File file, String version) throws LegendDriverException;

	Hashtable read(FLayers layers,FLayer layer,File file) throws LegendDriverException; //<FLayer, ILegend>
}
