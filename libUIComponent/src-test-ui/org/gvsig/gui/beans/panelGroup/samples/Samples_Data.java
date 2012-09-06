/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 */

package org.gvsig.gui.beans.panelGroup.samples;

import org.gvsig.gui.beans.panelGroup.loaders.PanelGroupLoaderFromList;
import org.gvsig.gui.beans.panelGroup.panels.AbstractPanel;
import org.gvsig.gui.beans.panelGroup.tabbedPanel.TabbedPanel;
import org.gvsig.gui.beans.panelGroup.treePanel.TreePanel;

/**
 * <p>This class has information to create samples to test {@link PanelGroupManagerDePablo PanelGroupManagerDePablo},
 *  {@link TabbedPanel TabbedPanel}, {@link TreePanel TreePanel},
 *  {@link AbstractPanel AbstractPanel}, {@link PanelGroupLoaderUtilities PanelGroupLoaderUtilities},
 *  {@link PanelGroupLoaderFromList PanelGroupLoaderFromList}, {@link PanelGroupLoaderFromExtensionPoint PanelGroupLoaderFromExtensionPoint}.</p>
 * 
 * @version 16/10/2007
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
 */
public class Samples_Data {
	public final static short PANELS_DEFAULT_WIDTH = 500;
	public final static short PANELS_DEFAULT_HEIGHT = 400;
	
	public final static Class[] TEST1_CLASSES = {SampleInfoPanel.class, SampleBandSetupPanel.class, SampleTransparencyPanel.class, SampleEnhancedPanel.class, SamplePanSharpeningPanel.class, SampleScalePanel.class};
	public final static Class[] TEST2_CLASSES = {SampleInfoPanel.class, SampleTransparencyPanel.class, SampleScalePanel.class};
	public final static Class[] TEST3_CLASSES = {SamplePanelWithoutGroupLabel.class, SamplePanelGroupLabelRepeated.class};
	public final static Class[] TEST4_CLASSES = {SampleInitializingExcetionPanel.class, SampleInfoPanel.class}; // Test 'ListCouldntLoadPanelFromListException' with any 'Exception' 
	public final static Class[] TEST5_CLASSES = {SampleInfoPanel.class, SampleUndefinedPreferredSizeExceptionPanel.class}; // Test 'ListCouldntAddPanelException' with a 'PanelWithNoPreferredSizeDefinedException'
	public final static Class[] TEST6_CLASSES = {}; // Test 'ListCouldntAddPanelException' with a 'EmptyPanelGroupException'
	public final static Class[] TEST7_CLASSES = {SampleInvisiblePanel.class}; // Test 'ListCouldntAddPanelException' with a 'EmptyPanelGroupGUIException'
	public final static Class[] TEST8_CLASSES = {SampleInitializingExcetionPanel.class, SampleUndefinedPreferredSizeExceptionPanel.class}; // Test 'ListCouldntAddPanelException' with a 'PanelWithNoPreferredSizeDefinedException', a 'EmptyPanelGroupException' and a 'ListCouldntLoadPanelFromListException' with any 'Exception'
	public final static Class[] TEST9_CLASSES = {SampleInvisiblePanel.class, SampleInitializingExcetionPanel.class, SampleUndefinedPreferredSizeExceptionPanel.class}; // Test 'ListCouldntAddPanelException' with a 'PanelWithNoPreferredSizeDefinedException', a 'EmptyPanelGroupGUIException' and a 'ListCouldntLoadPanelFromListException' with any 'Exception'
	public final static Class[] TEST10_CLASSES = {SampleBandSetupPanel.class, SampleInvisiblePanel.class, SampleInitializingExcetionPanel.class, SampleUndefinedPreferredSizeExceptionPanel.class}; // Test 'ListCouldntAddPanelException' with a 'PanelWithNoPreferredSizeDefinedException', a EmptyPanelGroupGUIException and a 'ListCouldntLoadPanelFromListException' with any 'Exception'
	public final static Object REFERENCE1 = new String("Raster Layer");
	public final static Object REFERENCE2 = new String("Imaginary Layer");
	public final static Object REFERENCE3 = new String("Other tests");	
	public final static String REFERENCE1_NAME = "Raster Layer reference";
	public final static String REFERENCE2_NAME = "Imaginary Layer reference";
	public final static String REFERENCE3_NAME = "Other tests reference";
	public final static String PANELGROUP1_ID = "RPG1_ID";
	public final static String PANELGROUP2_ID = "RPG2_ID";
	public final static String PANELGROUP3_ID = "RPG3_ID";
	public final static String[] PANELS1_IDS = {"Information_ID", "Bands_ID", "Transparency_ID", "Enhanced_ID", "PanSharpening_ID", "Scale_ID"};
	public final static String[] PANELS2_IDS = {"Information_ID", "Transparency_ID", "Scale_ID"};
	public final static String[] PANELS3_IDS = {"WithoutGroup_ID", "GroupLabelRepeated_ID"};
	public final static String[] PANELS1_LABELS = {"Information_LABEL", "Bands_LABEL", "Transparency_LABEL", "Enhanced_LABEL", "PanSharpening_LABEL", "Scale_LABEL"};
	public final static String[] PANELS2_LABELS = {"Information_LABEL", "Transparency_LABEL", "Scale_LABEL"};
	public final static String[] PANELS3_LABELS = {"WithoutGroup_LABEL", "GroupLabelRepeated_LABEL"};
	public final static String[] PANELS1_LABELGROUPS = {"Information_LABELGROUP", "Bands_LABELGROUP", "Transparency_LABELGROUP", "Enhanced_LABELGROUP", "PanSharpening_LABELGROUP", "Scale_LABELGROUP"};
	public final static String[] PANELS2_LABELGROUPS = {"Information_LABELGROUP", "Transparency_LABELGROUP", "Scale_LABELGROUP"};
	public final static String[] PANELS3_LABELGROUPS = {"WithoutGroup_LABELGROUP", "GroupLabelRepeated_LABELGROUP"};
}
