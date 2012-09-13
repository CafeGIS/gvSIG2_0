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
*/
package com.iver.ai2.animationgui.gui.document;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.gvsig.gui.beans.swing.GridBagLayoutPanel;
import org.gvsig.gui.beans.swing.JButton;
import org.gvsig.gvsig3dgui.view.View3D;
import org.gvsig.osgvp.planets.PlanetViewer;
import org.gvsig.osgvp.viewer.IViewerContainer;

import com.iver.ai2.animationgui.gui.AnimationContol3D;
import com.iver.ai2.animationgui.gui.util.AnimationUtils;
import com.iver.ai2.gvsig3d.camera.ProjectCamera;
import com.iver.ai2.gvsig3d.resources.ResourcesFactory;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.SingletonWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.animation.AnimationContainer;
import com.iver.cit.gvsig.animation.AnimationPlayer;
import com.iver.cit.gvsig.animation.animatedObject.IAnimatedObject;
import com.iver.cit.gvsig.animation.traks.IAnimationTrack;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */

public class Animation extends GridBagLayoutPanel implements SingletonWindow,
		ItemListener, ActionListener,IWindowListener {

	
	private static final long serialVersionUID = 1L;

	private int width = 300;

	private int height = 80;
	private int height2 = 135;

	private AnimationContainer animationContainer;

	private JButton captureKeyFrameButton;

	private JButton captureLayerStateButton;

	private JButton animationControl;

	private JButton clearButton;

	private JComboBox viewChoice = null;

	private JComboBox layerChoice = null;

	private BaseView viewSelected = null;

	private FLayer layerSelected = null;

	private List<IWindow> viewList;
	
	private List<BaseView> viewListAdded = new ArrayList<BaseView>();

	private String buttonPath;

	private ImageIcon imageCameraKeyFrame;

	private ImageIcon imageLayer;

	private ImageIcon imageAnimationControl;

	private ProjectAnimationDocument projectAnimationDocument;

	private AnimationContol3D contol3D = null;

	private StateUpdate runnable = null;

	private boolean openViews = false;

	private Thread thread;
	
	private static int contador = 0;
	
	
	/**
	 * This is the default constructor
	 * 
	 * @param ProjectAnimationDocument: animation project document selected
	 *           
	 */
	public Animation(ProjectAnimationDocument projectAnimationDocument) {
		
		this.projectAnimationDocument = projectAnimationDocument;
		
		animationContainer = projectAnimationDocument.animationContainer;
		String oldPath = ResourcesFactory.getExtPath();// Save the path.
		ResourcesFactory
				.setExtPath("/gvSIG/extensiones/org.gvsig.extAnimationGUI/images/");// my new path
		buttonPath = ResourcesFactory.getResourcesPath();
		ResourcesFactory.setExtPath(oldPath);// Restore the old path.
		initialize();
		
		IViewerContainer canvas = null;
		List<IAnimationTrack> atl = animationContainer.getAnimationTrackList();
		for (Iterator<IAnimationTrack> iterator = atl.iterator(); iterator.hasNext();) {
			IAnimationTrack track = (IAnimationTrack) iterator.next();
			IAnimatedObject animatedObject = (IAnimatedObject) track.getAnimatedObject();
			try {
				//if persistence is loaded, the canvas is null in this step
				canvas = (IViewerContainer) animatedObject.getAnimatedObject("canvas");
			}
			catch (Exception e) {
				//if canvas is null, do nothing
			}
			
			if (canvas == null) {
				try {
						//Getting the name of the view
						View3D view = (View3D) animatedObject.getAnimatedObject("view");
						//Getting the canvas and insert this canvas in the animated object
						animatedObject.addAnimatedObject("canvas", view.getCanvas3d());
						if(!viewListAdded.contains((BaseView)view)) {
							viewListAdded.add(((BaseView)view));
							this.contol3D.setViewListAdded(viewListAdded);
						}
						
						boolean find = projecViewControl(((BaseView)view));
						
						if (!find) {
							ExtensionPoint3D point = ExtensionPoint3D.getExtensionPoint(projectAnimationDocument.getName());
							point.register("vista" + contador++ , ((BaseView)view));
						}
						else
						{
							JOptionPane.showMessageDialog(null, PluginServices.getText(this, "Vista_animada_info"),
									viewChoice.getSelectedItem() + " " + PluginServices.getText(this, "Vista_animada_titulo"), JOptionPane.WARNING_MESSAGE);
							return;
						}
						
				}
				catch(Exception e) {
					//if canvas is null, do nothing. The view is 2D
				}
			}
		
		}
		
	}

	public BaseView getViewSelected() {
		return viewSelected;
	}

	public void setViewSelected(BaseView viewSelected) {
		this.viewSelected = viewSelected;
	}

	public FLayer getLayerSelected() {
		return layerSelected;
	}

	public void setLayerSelected(FLayer layerSelected) {
		this.layerSelected = layerSelected;
	}

	/**
	 * Composing the panel of an animation project
	 */
	private void initialize() {
		// Inicialize component
		setName("Animation properties");
		// Introducing the margin
		setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		// Dimension of the panel
		// setSize(new Dimension(width, height));
		//this.setPreferredSize(new Dimension(width, height));

		// ADDING COMPONENTS

		GridBagConstraints c = new GridBagConstraints();

		/*
		 * gridx: column position in grid gridy: row position in grid anchor:
		 * bottom of space
		 */

		// Creation of four buttons: play, stop, pause y record.
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 2, 2);
		this.add(getCaptureKeyFrameButton(), c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(5, 2, 2, 2);
		this.add(getCaptureLayerStateButton(), c);

		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 0;
		c.insets = new Insets(5, 2, 2, 2);
		this.add(getAnimationControl(), c);

		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 0;
		c.insets = new Insets(5, 2, 2, 5);
		c.fill = GridBagConstraints.BOTH;
		this.add(getClearButton(), c);

		// choice for mode player selection.
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(2, 2, 2, 5);
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.BOTH;
		this.add(getViewChoiceValue(), c);

		// choice.setSelectedIndex(this.animationPlayer.getAnimationMode());
		// Initializing values
		viewChoice.setSelectedIndex(0);
		if(viewChoice.getComponentCount()!=0){
			int index = viewChoice.getSelectedIndex(); // get select item.
			if (viewList != null && !viewList.isEmpty()) 
				setViewSelected((BaseView) viewList.get(index));
		}
		
//		c = new GridBagConstraints();
//		c.gridx = 1;
//		c.gridy = 2;
//		c.insets = new Insets(2, 2, 2, 5);
//		c.anchor = GridBagConstraints.WEST;
//		c.gridwidth = 3;
//		c.fill = GridBagConstraints.BOTH;
//		this.add(getLayerChoiceValue(), c);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(0, 0, 0, 0);
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.gridwidth = 4;
		getAnimationContol3D().setVisible(false);
		this.add(getAnimationContol3D(), c);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 4;
		c.insets = new Insets(0, 0, 0, 0);
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridwidth = 4;
		this.add(new JPanel(), c);
		
		startThread();
	}

	/**
	 * Method to create the button that control the capture for key Frames.
	 * The listener to lister this component is this class
	 * 
	 * @return JButton: component with all values inicilize 
	 */
	private JButton getCaptureKeyFrameButton() {
		if (captureKeyFrameButton == null) {
			captureKeyFrameButton = new JButton();
			// path where are the image to load in the button.
			imageCameraKeyFrame = new ImageIcon(buttonPath + "camera.png");
			captureKeyFrameButton.setIcon(imageCameraKeyFrame);
			captureKeyFrameButton.setActionCommand("CAMERA_KEY_FRAME");
			captureKeyFrameButton.addActionListener(this);
			captureKeyFrameButton.setToolTipText(PluginServices.getText(this,"Capturar_fotograma"));
		}
		return captureKeyFrameButton;
	}

	/**
	 * Method to create the button that control the capture for Layer properties
	 * The listener to lister this component is this class
	 * 
	 * @return JButton: component with all default values 
	 */
	private JButton getCaptureLayerStateButton() {
		if (captureLayerStateButton == null) {
			captureLayerStateButton = new JButton();
			// path where are the image to load in the button.
			imageLayer = new ImageIcon(buttonPath + "trans6.png");
			captureLayerStateButton.setIcon(imageLayer);
			captureLayerStateButton.setActionCommand("LAYER_PROPERTIES");
			captureLayerStateButton.addActionListener(this);
			captureLayerStateButton.setToolTipText(PluginServices.getText(this,"Animacion_de_transparencias"));
		}
		return captureLayerStateButton;
	}

	/**
	 * Method that generates the button to open the player menu
	 * The listener to lister this component is this class
	 * 
	 * @return JButton: Button to extend the player controls
	 */
	private JButton getAnimationControl() {
		if (animationControl == null) {
			animationControl = new JButton();
			animationControl.setToolTipText(PluginServices.getText(this,"Player"));
			// path where are the image to load in the button.
			imageAnimationControl = new ImageIcon(buttonPath
					+ "player.png");
			animationControl.setIcon(imageAnimationControl);
			animationControl.setActionCommand("ANIMATION_CONTROL");
			animationControl.addActionListener(this);
			
		}
		return animationControl;
	}

	/**
	 * Method that generates the button to open the advance configuration
 	 * The listener to lister this component is this class
 	 * 
	 * @return JButton: Button to clear the animation container
	 */
	private JButton getClearButton() {
		if (clearButton == null) {
			clearButton = new JButton();
			clearButton.setText(PluginServices.getText(this, "Borrar_animacion"));
			clearButton.setActionCommand("CLEAR_TRACK");
			clearButton.addActionListener(this);
			clearButton.setToolTipText(PluginServices.getText(this, "Borrar_animacion"));
		}
		return clearButton;
	}

	/**
	 * @return Choice. Return the choice with the diferents modes of display.
	 */
	private JComboBox getViewChoiceValue() {
		if (viewChoice == null) {
			viewChoice = new JComboBox();
			if (viewChoice.getItemCount() == 0)
				viewChoice.addItem(PluginServices.getText(this, "no_views"));
			
			viewChoice.setToolTipText(PluginServices.getText(this, "Vista_a_animar"));
			refreshViewSelector();
		}
		return viewChoice;
	}
	
	/*
	 * Refresh the choice with the ready views to animate
	 */
	private void refreshViewSelector() {
		
		IWindow[] openWindows = PluginServices.getMDIManager().getAllWindows();
		
		IWindow activeWindows = PluginServices.getMDIManager().getActiveWindow();
		if(activeWindows instanceof BaseView)
			viewChoice.setSelectedItem(activeWindows.getWindowInfo().getTitle());
		
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < openWindows.length; i++) {
			IWindow window = openWindows[i];
			if ((window.getClass().equals(View.class)) || (window.getClass().equals(View3D.class))) {
				list.add(window.getWindowInfo().getTitle());
				openViews = true;
			}
		}

		if (list.size() == 0) {//No hay vistas abiertas
			list.add(PluginServices.getText(this, "no_views"));
			openViews = false;
		}
			
		selectActualView();
		// Comprobamos si el ArrayList es diferente al combo actual
		if (compareArrayListWithComboBox(list, viewChoice))
			return;
		
		Object object = viewChoice.getSelectedItem();
		
		viewChoice.removeAllItems();
		viewList = new ArrayList<IWindow>();
		for (int i = 0; i < openWindows.length; i++) {
			IWindow window = openWindows[i];
			if ((window.getClass().equals(View.class))
					|| (window.getClass().equals(View3D.class))) {
				viewChoice.addItem(window.getWindowInfo().getTitle());
				viewList.add(window);
			}
		}
		
		if (viewChoice.getItemCount() == 0) {
			viewChoice.addItem(PluginServices.getText(this, "no_views"));
			viewList = null;
		}
		
		viewChoice.setSelectedItem(object);
		selectActualView();
		
		
		this.updateUI();
	}

	
	/*
	 * Setting the actual view in the choice
	 */
	private void selectActualView() {
		int index = viewChoice.getSelectedIndex(); // get select item.
		if (viewList != null) {
			setViewSelected((BaseView) viewList.get(index));
		}
	}

	/*
	 * Check if the ArrayList elements are equal to the JComboBox elements
	 * Return true when the elements are equal
	 * @param list
	 * @param comboBox
	 * @return boolean
	 */
	private boolean compareArrayListWithComboBox(ArrayList<String> list, JComboBox comboBox) {
		if (list.size() != comboBox.getItemCount())
			return false;

		for (int i=0; i<list.size(); i++)
			if (!list.get(i).equals(comboBox.getItemAt(i)))
				return false;
		
		return true;
	}
	
	
	private void refreshLayerSelector() {
		if (layerChoice == null)
			return;
		
		// Rellenamos el ArrayList con los elementos que tiene que tener el combo
		ArrayList<String> list = new ArrayList<String>();
		IWindow[] viewArray = PluginServices.getMDIManager().getAllWindows();
		for (int i = 0; i < viewArray.length; i++) {
			IWindow window = viewArray[i];
			if (viewChoice.getSelectedItem() != null) {
				if (window.getWindowInfo().getTitle().equals(viewChoice.getSelectedItem())) {
					BaseView view = (BaseView) window;// Working with base view
					MapControl model = view.getMapControl();
					FLayers flayers = model.getMapContext().getLayers();
					for (int j = 0; j < flayers.getLayersCount(); j++) {
						FLayer layer = flayers.getLayer(j);
						String name = layer.getName();
						list.add(name);
					}
					break;
				}

			}
		}
		if (list.size() == 0)
			list.add(PluginServices.getText(this, "no_layers"));
		
		// Comprobamos si el ArrayList es diferente al combo actual
		if (compareArrayListWithComboBox(list, layerChoice))
			return;
		
		// En caso de que sea diferente, vaciamos el combo y lo volvemos a rellenar
		layerChoice.removeAllItems();
		for (int i = 0; i < viewArray.length; i++) {
			IWindow window = viewArray[i];
			if (viewChoice.getSelectedItem() != null) {
				if (window.getWindowInfo().getTitle().equals(viewChoice.getSelectedItem())) {
					BaseView view = (BaseView) window;// Working with base view
					MapControl model = view.getMapControl();
					FLayers flayers = model.getMapContext().getLayers();
					for (int j = 0; j < flayers.getLayersCount(); j++) {
						FLayer layer = flayers.getLayer(j);
						String name = layer.getName();
						layerChoice.addItem(name);
					}
					break;
				}

			}
		}
		if (layerChoice.getItemCount() == 0)
			layerChoice.addItem(PluginServices.getText(this, "no_layers"));
		
		this.updateUI();
	}
	

//	private JComboBox getLayerChoiceValue() {
//		if (layerChoice == null) {
//			layerChoice = new JComboBox();
//			layerChoice.addItem(PluginServices.getText(this, "no_layers"));
//			layerChoice.setToolTipText("Selección de la vista a animar");
//		}
//		return layerChoice;
//	}

	/**
	 * Return full animation container 
	 * @return animationContainer: Container with all the animations created
	 */
	public Object getWindowModel() {
		return animationContainer;
	}
	
	/**
	 * 
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 * @return WindowInfo: window parameters
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo();
		m_viewinfo.setTitle(this.projectAnimationDocument.getName());
		m_viewinfo.setHeight(height);
		m_viewinfo.setWidth(width);
		return m_viewinfo;
	}
	
	/**
	 * Listener of view choice changes
	 */
	public void itemStateChanged(ItemEvent event) {
		Object obj = event.getSource();
		// If the view select has changed
		if (obj == viewChoice) {
			int index = viewChoice.getSelectedIndex(); // get select item.
			if (viewList != null) {
				try {
					setViewSelected((BaseView) viewList.get(index));
//					System.out.println("Vista seleccionada : "
//							+ getViewSelected().getWindowInfo().getTitle());
					
				}
				catch(Exception e){
					
				}
				
			}
		}
	}
	
	
	private void refreshWindowTittle() {
		PluginServices.getMDIManager().getWindowInfo(this).setTitle(projectAnimationDocument.getName());
	}
	
	/**
	 * If the controls exists do nothing, else new instance of it 
	 * @return AnimationContol3D: All the options to do animations
	 */
	private AnimationContol3D getAnimationContol3D() {
		if (contol3D == null) {
			AnimationPlayer ap = this.animationContainer.getAnimationPlayer();
			ap.setAnimationContainer(animationContainer);
			contol3D = new AnimationContol3D(ap);
		}
		return contol3D;
	}
	
	/**
	 * This method do the control of the actual view selected and the animated projects
	 * 
	 * @param bv: view selected
	 * @return 
	 *   - if the view is not in an animation: false
	 *   - if the view is in an animation: true
	 */
	public boolean projecViewControl(BaseView bv) {
		
		boolean find = false;
		
		Iterator<?> iterator = ExtensionPoint3D.getExtensionIterator();
		while (iterator.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
			if (entry != null) {
				String key = (String) entry.getKey();
				if (!key.equals(projectAnimationDocument.getName())) {
					ExtensionPoint3D point2 = ExtensionPoint3D.getExtensionPoint(key);
					Iterator<?> iterator2 = point2.getIterator();
					while (iterator2.hasNext()) {
						Entry<?, ?> entry2 = (Entry<?, ?>) iterator2.next();
						if (entry2 != null) {
							if (bv == entry2.getValue()) {
								find = true;
								
//								Iterator<ProjectDocument> it = projectList.iterator();
//								while (it.hasNext()) {
//									ProjectDocument projectDocument = (ProjectDocument) it.next();
//									if (!projectAnimationDocument.getName().equals(projectDocument.getName())) {
//											ProjectAnimationDocument pv = (ProjectAnimationDocument) projectDocument;
//											int state = pv.getAnimationContainer().getAnimationPlayer().getAnimationPlayerState();
//											if (state == 0 || state ==  2) {
//												find = true;
//											}
//									}
//								}
							}
						}
					}
				}
			}
		}
		return find;
	}
	
	/** 
	 * Listener of all options of animation
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * @param e: actual event
	 */
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		//view keyframe
		if (actionCommand.equals("CAMERA_KEY_FRAME")) {

			// if the view is not activated or if the view is a instance of a base view.
			if (!openViews ) {
				JOptionPane.showMessageDialog(null, PluginServices.getText(this, "Closed_views_titulo2") + " " + projectAnimationDocument.getName(),
						PluginServices.getText(this, "Closed_views_titulo") , JOptionPane.WARNING_MESSAGE);
				return;
				
			}
			
			int statePlayer = this.projectAnimationDocument.animationContainer.getAnimationPlayer().getAnimationPlayerState();
			if(statePlayer == 0 || statePlayer == 2) {
				JOptionPane.showMessageDialog(null, PluginServices.getText(this, "Animacion_en_curso_info"),
						PluginServices.getText(this, "Animacion_en_curso_info2") + " " + projectAnimationDocument.getName()
						+ " " + PluginServices.getText(this, "Animacion_en_curso_titulo"), JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			BaseView bv = (BaseView)this.getViewSelected();
			boolean find = projecViewControl(bv);
			
			if (!find) {
				ExtensionPoint3D point = ExtensionPoint3D.getExtensionPoint(projectAnimationDocument.getName());
				point.register("vista" + contador++ , bv);
			}
			else
			{
				JOptionPane.showMessageDialog(null, PluginServices.getText(this, "Vista_animada_info"),
						viewChoice.getSelectedItem() + " " + PluginServices.getText(this, "Vista_animada_titulo"), JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			if(!viewListAdded.contains(bv)) {
				viewListAdded.add(bv);
				this.contol3D.setViewListAdded(viewListAdded);
			}
			
			AnimationUtils.captureEncuadrator(
					(BaseView) this.getViewSelected(), this.animationContainer,
					this.projectAnimationDocument.getName());
		} else if (actionCommand.equals("LAYER_PROPERTIES")) {

			// Generating the animation transparency
			BaseView bv = (BaseView)this.getViewSelected();
			boolean find2 = projecViewControl(bv);
			if(bv == null) {
				JOptionPane.showMessageDialog(null, PluginServices.getText(this, "Closed_views_info"),
						PluginServices.getText(this, "Closed_views_titulo"), JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			if (!find2) {
				ExtensionPoint3D point = ExtensionPoint3D.getExtensionPoint(projectAnimationDocument.getName());
				point.register("vista" + contador++ , bv);
			}
			else
			{
				JOptionPane.showMessageDialog(null, PluginServices.getText(this, "Vista_animada_info"),
						viewChoice.getSelectedItem() + " " + PluginServices.getText(this, "Vista_animada_titulo"), JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			IProjectView model = bv.getModel();
			MapContext mapContext = model.getMapContext();
			FLayers layers = mapContext.getLayers();
			FLayer[] actives = layers.getActives();
			if( actives.length == 0) {
				JOptionPane.showMessageDialog(null, PluginServices.getText(this, "Mensaje_capas_info"),
						PluginServices.getText(this, "Mensaje_capas_titulo"), JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			if(!viewListAdded.contains(bv)) {
				viewListAdded.add(bv);
				this.contol3D.setViewListAdded(viewListAdded);
			}
			AnimationUtils.createLayerTransparencyAnimation((BaseView) this
					.getViewSelected(), this.animationContainer,
					this.projectAnimationDocument.getName());

		} else if (actionCommand.equals("ANIMATION_CONTROL")) {// player displayed
		
			// Resize the window
			if (getAnimationContol3D().isVisible()) {
				PluginServices.getMDIManager().getWindowInfo(this).setHeight(height + 30);
			} else {
				PluginServices.getMDIManager().getWindowInfo(this).setHeight(height2 + 30);
			}
			getAnimationContol3D().setVisible(!getAnimationContol3D().isVisible());
		}
		else if(actionCommand.equals("CLEAR_TRACK")) {// Option to clear the animation
			int statePlayer = this.animationContainer.getAnimationPlayer().getAnimationPlayerState();
			if(statePlayer == 0 || statePlayer == 2) {
				JOptionPane.showMessageDialog(null,PluginServices.getText(this, "Animacion_borrar_info"),
						PluginServices.getText(this,"Animacion_borrar_titulo"), JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			boolean emptyContainer = projectAnimationDocument.getAnimationContainer().isEmpty();
			if(emptyContainer) {
				JOptionPane.showMessageDialog(null, PluginServices.getText(this, "No_vistas_info") + " " +
						projectAnimationDocument.getName()+ " " + PluginServices.getText(this, "No_vistas_info2"),
						PluginServices.getText(this, "No_vistas_titulo"), JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			this.animationContainer.removeAllTrack();
			Iterator<BaseView> iter = viewListAdded.iterator();
			while (iter.hasNext()) {
				Object object = iter.next();
				if (object instanceof View3D){
					((PlanetViewer) ((View3D) object).getCanvas3d().getOSGViewer()).restoreCustomTerrainManipulator();
				}
			}
			Project project = AnimationUtils.getProject();
			removeCameras(project);
			try {
				ExtensionPoint3D.removeExtensionPoint(projectAnimationDocument.getName());
				viewListAdded.clear();
				this.contol3D.setViewListAdded(viewListAdded);
			} catch (NullPointerException point_Null) {
				point_Null.printStackTrace();
				System.err.println("El punto de extensión no existe.");
			}
		}
	}

	/**
	 * Remove the cameras associated an animation project
	 * @param project: Actual project to get the cameras
	 */
	void removeCameras(Project project) {
		// Remove all animation keyframe from the encuadrator.
		Object[] pcList = project.getCameras();
		String AnimationPrefix = AnimationUtils.getAnimationPrefix();
		int i = 0;
		while (i < pcList.length) {
			ProjectCamera projectCamera = (ProjectCamera) pcList[i];
			if (projectCamera.getDescription().contains(AnimationPrefix)) {
				project.removeCamera(i);
				pcList = project.getCameras();
			} else {
				i++;
			}
		}
	}

	
	public void windowActivated() {
	}

	/*
	 * Stop the running thread when the animated window is closed
	 * @see com.iver.andami.ui.mdiManager.IWindowListener#windowClosed()
	 */
	public void windowClosed() {
		if(runnable != null)
			runnable.end();
		
	}
	

	public class StateUpdate implements Runnable {

		private boolean finish = false;

		public void run() {
			while (true) {
				try {
					Thread.sleep(300);
					synchronized (this) {
						refreshWindowTittle();
						refreshViewSelector();
						refreshLayerSelector();
						if (finish) {
							break;
						}
					}
				} catch (InterruptedException e) {
 
					e.printStackTrace();
				}
			}
		}
		
		/*
		 * function to stop the thread.
		 */
		public synchronized void end() {
			finish = true;
//			runnable = null;
		}
	}// end class StateUpdate.

	/**
	 * Getting the window profile
	 * 
	 */
	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		
		this.thread = thread;
	}
	public void startThread() {
		if (runnable == null) {
			runnable = new StateUpdate();
			// Create the thread supplying it with the runnable object
			thread = new Thread(runnable);
			// Start the thread
			thread.start();
		}
	}
	
}