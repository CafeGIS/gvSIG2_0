/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package com.iver.ai2.gvsig3d.legend.symbols;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbolDrawingException;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.gvsig.osgvp.core.osg.Node;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.core.osgdb.osgDB;
import org.gvsig.osgvp.exceptions.node.LoadNodeException;
import org.gvsig.osgvp.viewer.IViewerContainer;
import org.gvsig.osgvp.viewer.ViewerFactory;

import com.iver.ai2.gvsig3d.resources.MyFileFilter3D;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.gui.styling.AbstractTypeSymbolEditor;
import com.iver.cit.gvsig.gui.styling.EditorTool;
import com.iver.cit.gvsig.gui.styling.Mask;
import com.iver.cit.gvsig.gui.styling.SymbolEditor;

/**
 * PictureMarker allows the user to store and modify the properties that define
 * a <b>picture marker symbol</b>.
 * <p>
 * <p>
 * This functionality is carried out thanks to a tab (simple marker)which is
 * included in the panel to edit the properities of a symbol (SymbolEditor)how
 * is explained in AbstractTypeSymbolEditor.
 * <p>
 * First of all, in the above mentioned tab the user will have options to change
 * the files from where the pictures for the symbol are taken (one for the
 * symbol when it is not selected in the map and the other when it is done).
 * <p>
 * <p>
 * Secondly, the user will have options to modify the pictures which had been
 * selected before (width and offset) .
 * 
 *@see AbstractTypeSymbolEditor
 *@author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class Object3DMarker extends AbstractTypeSymbolEditor implements
		ActionListener {
	protected ArrayList<JPanel> tabs = new ArrayList<JPanel>();
	protected Mask mask;
	protected JLabel lblFileName;
	protected JLabel lblSelFileName;
	private JButton btn;
	private File fileOSG;
	private String filePath = "";
	
	private static String lastPath = "";

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	private ActionListener chooseAction = new ActionListener() {


		public void actionPerformed(ActionEvent e) {

			JFileChooser jfc = new JFileChooser(lastPath);
			jfc.removeChoosableFileFilter(jfc.getAcceptAllFileFilter());
			
			
			List filterListFile =  new ArrayList<MyFileFilter3D>();
			
			filterListFile.add(new MyFileFilter3D("ive",
					PluginServices.getText(this, "Ficheros *.ive"), "ive"));
			filterListFile.add(new MyFileFilter3D("3ds",
					PluginServices.getText(this, "Ficheros *.3ds"), "3ds"));
			filterListFile.add(new MyFileFilter3D("obj",
					PluginServices.getText(this, "Ficheros *.obj"), "obj"));
			filterListFile.add(new MyFileFilter3D("osg",
					PluginServices.getText(this, "Ficheros *.osg"), "osg"));

			Iterator iter = filterListFile.iterator();
			while (iter.hasNext()) {
				jfc.addChoosableFileFilter((FileFilter) iter.next());
			}

			if (jfc.showOpenDialog((Component) PluginServices.getMainFrame()) == JFileChooser.APPROVE_OPTION) {

				fileOSG = jfc.getSelectedFile();
				if (fileOSG.exists()) {// file don't exists in the directory.

					MyFileFilter3D filter = (MyFileFilter3D) jfc
							.getFileFilter();
					fileOSG = filter.normalizeExtension(fileOSG);
					filePath = fileOSG.getAbsolutePath();
					filePathTextField.setText(filePath);
					updatePreview();
					lastPath = fileOSG.getPath();
				} else {
					JOptionPane.showMessageDialog(null,
							"El fichero no existe.", "Fichero no encontrado",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
			}// If aprove option.

		}

	};
	private IViewerContainer _canvas3d;
	private JPanel topPanel;
	private JPanel leftBottomPanel;
	private JPanel rigthBottomPanel;
	private JTextField rotationTextFieldX;
	private JTextField scaleTextFieldX;
	private JTextField rotationTextFieldZ;
	private JTextField rotationTextFieldY;
	private JTextField scaleTextFieldY;
	private JTextField scaleTextFieldZ;
	private JTextField filePathTextField;
	private JCheckBox autorotate;
	private String tempScreenshotimage;

	public Object3DMarker(SymbolEditor owner) {
		super(owner);
		initialize();
		
	
	}

	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		// this part is for test
		ViewerFactory.getInstance().stopAnimator();
		_canvas3d.dispose();
		///////////////////////////
	}

	/**
	 * Initializes the parameters that define a picturmarker.To do it, a tab is
	 * created inside the SymbolEditor panel with default values for the
	 * different attributes of the picture marker.
	 */

	private void initialize() {
		JPanel myTab = new JPanel(new GridBagLayout());
		myTab.setName("3D Object");

		// Top panel
		GridBagConstraints topPanelConstrain = new GridBagConstraints();
		topPanelConstrain.gridx = 0;
		topPanelConstrain.gridy = 0;
		topPanelConstrain.gridwidth = 2;
		topPanelConstrain.fill = GridBagConstraints.BOTH;
		topPanelConstrain.weightx = 1.0;
		topPanelConstrain.weighty = 1.0;
		myTab.add(getTopPanel(), topPanelConstrain);

		// bottom left panel
		GridBagConstraints leftBottomPanelConstrain = new GridBagConstraints();
		leftBottomPanelConstrain.gridx = 0;
		leftBottomPanelConstrain.gridy = 1;
		leftBottomPanelConstrain.fill = GridBagConstraints.BOTH;
		leftBottomPanelConstrain.weightx = 0.5;
		leftBottomPanelConstrain.weighty = 1.0;
		myTab.add(getLeftBottomPanel(), leftBottomPanelConstrain);

		// Bottom right panel
		GridBagConstraints rigthBottomPanelConstrain = new GridBagConstraints();
		rigthBottomPanelConstrain.gridx = 1;
		rigthBottomPanelConstrain.gridy = 1;
		rigthBottomPanelConstrain.fill = GridBagConstraints.BOTH;
		rigthBottomPanelConstrain.weightx = 0.5;
		rigthBottomPanelConstrain.weighty = 1.0;
		myTab.add(getRigthBottomPanel(), rigthBottomPanelConstrain);
		
	
		tabs.add(myTab);
		
		
		PluginServices core = PluginServices.getPluginServices("com.iver.core");
		if (core!= null){
			if (core.getPersistentXML().contains("DataFolder")){
				
				lastPath = core.getPersistentXML().getStringProperty("DataFolder");
			}
		}
	}

	private JPanel getTopPanel() {
		if (topPanel == null) {
			topPanel = new JPanel(new GridBagLayout());
			// topPanel.setBorder(new LineBorder(Color.gray));
			GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
			// picture file label
			lblFileName = new JLabel("Seleccione fihero :");
//			lblFileName.setFont(lblFileName.getFont().deriveFont(Font.BOLD));

			topPanel.add(lblFileName, gridBagConstraints);

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
			gridBagConstraints.weightx = 0.75;

			filePathTextField = new JTextField();
			topPanel.add(filePathTextField, gridBagConstraints);

			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 0.25;
			gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);

			// button browse
			btn = new JButton(PluginServices.getText(this, "browse"));
			btn.addActionListener(chooseAction);
			
			topPanel.add(btn, gridBagConstraints);
			
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 2;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
//			gridBagConstraints.weightx = 0.25;
			
			autorotate = new JCheckBox();
			autorotate.setSelected(false);
			autorotate.setText("Autorotate");
			
			
			topPanel.add(autorotate, gridBagConstraints);

		}
		return topPanel;
	}

	public JCheckBox getAutorotate() {
		return autorotate;
	}

	public void setAutorotate(JCheckBox autorotate) {
		this.autorotate = autorotate;
	}

	private JPanel getLeftBottomPanel() {
		if (leftBottomPanel == null) {
			leftBottomPanel = new JPanel(new GridBagLayout());
			// leftBottomPanel.setBorder(new LineBorder(Color.gray));

			Component transLabel = new JLabel("Transformaciones :");
			transLabel.setName("jLabel1"); // NOI18N
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			leftBottomPanel.add(transLabel, gridBagConstraints);

			// Scale X
			Component scaleXLabel = new JLabel("Escalado X");
			scaleXLabel.setName("jLabel2"); // NOI18N
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
			leftBottomPanel.add(scaleXLabel, gridBagConstraints);

			scaleTextFieldX = new JTextField();
			// scaleTextFieldX.setName(); // NOI18N
			scaleTextFieldX.setText("1");
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
			leftBottomPanel.add(scaleTextFieldX, gridBagConstraints);

			// Rotation Y
			Component scaleYLabel = new JLabel("Escalado Y");
			scaleYLabel.setName("jLabel5"); // NOI18N
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 2;
			gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
			leftBottomPanel.add(scaleYLabel, gridBagConstraints);

			scaleTextFieldY = new JTextField();
//			scaleTextFieldY.setName("0"); // NOI18N
			scaleTextFieldY.setText("1");
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 2;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
			leftBottomPanel.add(scaleTextFieldY, gridBagConstraints);

			// Rotation Z
			Component scaleZLabel = new JLabel("Escalado Z");
			scaleZLabel.setName("jLabel5"); // NOI18N
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 3;
			gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
			leftBottomPanel.add(scaleZLabel, gridBagConstraints);

			scaleTextFieldZ = new JTextField();
//			scaleTextFieldZ.setName("0"); // NOI18N
			scaleTextFieldZ.setText("1");
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 3;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
			leftBottomPanel.add(scaleTextFieldZ, gridBagConstraints);

			// Rotation X
			Component rotationXLabel = new JLabel("Rotacion X");
			rotationXLabel.setName("jLabel5"); // NOI18N
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 4;
			gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
			leftBottomPanel.add(rotationXLabel, gridBagConstraints);

			rotationTextFieldX = new JTextField();
//			rotationTextFieldX.setName("0"); // NOI18N
			rotationTextFieldX.setText("0");
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 4;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
			leftBottomPanel.add(rotationTextFieldX, gridBagConstraints);

			// Rotation Y
			Component rotationYLabel = new JLabel("Rotacion Y");
			rotationYLabel.setName("jLabel3"); // NOI18N
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 5;
			gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
			leftBottomPanel.add(rotationYLabel, gridBagConstraints);

			rotationTextFieldY = new JTextField();
			rotationTextFieldY.setName("0"); // NOI18N
			rotationTextFieldY.setText("0");
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 5;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
			leftBottomPanel.add(rotationTextFieldY, gridBagConstraints);

			// Rotation Z
			Component rotationZlabel = new JLabel("Rotacion Z");
			rotationZlabel.setName("jLabel4"); // NOI18N
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 6;
			gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
			leftBottomPanel.add(rotationZlabel, gridBagConstraints);

			rotationTextFieldZ = new JTextField();
//			rotationTextFieldZ.setName("0"); // NOI18N
			rotationTextFieldZ.setText("0");
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 6;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
			leftBottomPanel.add(rotationTextFieldZ, gridBagConstraints);
		}
		return leftBottomPanel;
	}

	private JPanel getRigthBottomPanel() {
		if (rigthBottomPanel == null) {
			rigthBottomPanel = new JPanel(new GridBagLayout());
			// rigthBottomPanel.setBorder(new LineBorder(Color.gray));
			GridBagConstraints constrain = new GridBagConstraints();
			constrain.gridx = 0;
			constrain.gridy = 0;
			constrain.anchor = GridBagConstraints.WEST;
			// selection picture file
			lblSelFileName = new JLabel("Preview:");
			rigthBottomPanel.add(lblSelFileName, constrain);

			int size = 150;
			JPanel aux3 = new JPanel();
			aux3.setPreferredSize(new Dimension(size, size));
			_canvas3d = ViewerFactory.getInstance().createViewer(
					ViewerFactory.VIEWER_TYPE.CANVAS_VIEWER, null);
			ViewerFactory.getInstance().startAnimator();

			_canvas3d.setSize(size, size);
			
			updatePreview();

			constrain.gridx = 0;
			constrain.gridy = 1;
			aux3.add((Component) _canvas3d);
			rigthBottomPanel.add(aux3, constrain);
			
			
		}
		return rigthBottomPanel;
	}
	
	
	private void updatePreview(){
		try {
			Node node = null;
			if (this.getFilePath() == "") {
				node = osgDB.readNodeFile("D:/modelos3d/cow.ive");
			} else {
				node = osgDB.readNodeFile(filePath);
			}
			_canvas3d.getOSGViewer().setSceneData(node);
			tempScreenshotimage = System.getenv("TMP")+"/tempScreenshotimage.png";
			System.err.println("screenshot del simbolo " + tempScreenshotimage);
			_canvas3d.getOSGViewer().takeScreenshot(tempScreenshotimage);
		} catch (LoadNodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ISymbol getLayer() {
		// this method builds the new symbol and returns it to the legend
		// manager
		try {
			Object3DMarkerSymbol symbol = null;

			if (this.getFilePath().equals(""))
				symbol = null;
			else {
				symbol = new Object3DMarkerSymbol(this.getFilePath());
				symbol.setScale(new Vec3(Double.parseDouble(scaleTextFieldX
						.getText()), Double.parseDouble(scaleTextFieldY
						.getText()), Double.parseDouble(scaleTextFieldZ
						.getText())));
				symbol.setRotation(new Vec3(Double
						.parseDouble(rotationTextFieldX.getText()), Double
						.parseDouble(rotationTextFieldY.getText()), Double
						.parseDouble(rotationTextFieldZ.getText())));
				
				symbol.setAutoRotate(autorotate.isSelected());
				symbol.setSnapshot(tempScreenshotimage);
			}

			
			return symbol;
		} catch (IOException e) {
			return SymbologyFactory.getWarningSymbol(PluginServices.getText(
					this, "failed_acessing_files"), null,
					SymbolDrawingException.UNSUPPORTED_SET_OF_SETTINGS);
		}

	}

	public void refreshControls(ISymbol layer) {

		// this method gets the simbol from the legend manager

		Object3DMarkerSymbol sym;
		try {
			String fileName = null;
			if (layer == null) {
				// initialize defaults
				System.err.println(getClass().getName()
						+ ":: should be unreachable code");
				fileName = "-";
			} else {
				sym = (Object3DMarkerSymbol) layer;
				fileName = sym.getObject3DPath();
			}

			this.setFilePath(fileName);

		} catch (IndexOutOfBoundsException ioEx) {
			NotificationManager.addWarning("Symbol layer index out of bounds",
					ioEx);
		} catch (ClassCastException ccEx) {
			NotificationManager.addWarning("Illegal casting from "
					+ layer.getClassName() + " to "
					+ getSymbolClass().getName() + ".", ccEx);
		}
	}

	public String getName() {
		return "Object 3D symbol";

	}

	public JPanel[] getTabs() {
		return tabs.toArray(new JPanel[tabs.size()]);
	}

	public Class getSymbolClass() {
		return Object3DMarkerSymbol.class;
	}

	public EditorTool[] getEditorTools() {
		return null;

	}

	public void actionPerformed(ActionEvent e) {
		fireSymbolChangedEvent();
	}

}