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
package com.iver.ai2.animationgui.gui;

import java.awt.Checkbox;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.gvsig.gui.beans.swing.GridBagLayoutPanel;
import org.gvsig.gvsig3dgui.view.View3D;
import org.gvsig.osgvp.planets.PlanetViewer;

import com.iver.ai2.gvsig3d.resources.ResourcesFactory;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.animation.AnimationPlayer;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 * Class to create and to process the event's panel animation.
 */

public class AnimationContol3D extends GridBagLayoutPanel implements
		ActionListener, KeyListener, ItemListener, FocusListener {

	/**
	 * default static final serial version ID
	 */
	private static final long serialVersionUID = 1L;

	private int operation; // Used in choice's functions

	private JLabel label = null;

	private JLabel label2 = null;
	
	private JLabel label3 = null;

	private JTextField textosegs = null;

	private JComboBox choice = null;

	private JButton buttonIcon = null;

	private JButton buttonIcon1 = null;

	private JButton buttonIcon2 = null;

	private JButton buttonIcon3 = null;

	private ImageIcon image = null;

	private ImageIcon image1 = null;

	private ImageIcon image2 = null;

	private ImageIcon image3 = null;

	private Checkbox checkbox = null;

	private String buttonPath;

	private double globalTime;

	private AnimationPlayer animationPlayer;

	private int animationMode;

	private boolean playFlag = false;

	private boolean pauseFlag = false;

	private StateUpdate runnable;

	private ArrayList<WindowClosedListener> actionCommandListeners = new ArrayList<WindowClosedListener>();

	private List<BaseView> viewListAdded =  new ArrayList<BaseView>();
	

	/**
	 * Default constructor.
	 * 
	 * @param animationPlayer: player associate
	 */
	public AnimationContol3D(AnimationPlayer animationPlayer) {

		this.animationPlayer = animationPlayer;
		this.animationPlayer.setAnimationPlayerState(AnimationPlayer.ANIMATION_INITIAL);

		// Setting up the button path
		String oldPath = ResourcesFactory.getExtPath();// Save the path.
		ResourcesFactory
				.setExtPath("/gvSIG/extensiones/com.iver.ai2.animationgui.gui/images/reproductoranim/");
		ResourcesFactory.setExtPath("/images/reproductoranim/");// my new path
		buttonPath = ResourcesFactory.getResourcesPath();
		URL path;
		path = this.getClass().getClassLoader().getResource("images/reproductoranim/");
		buttonPath = path.getPath(); 
		ResourcesFactory.setExtPath(oldPath);// Restore the old path.
		System.out.println(oldPath);
		System.out.println(buttonPath);

		initialize();
	}

	/*
	 * Panel creation.
	 * 
	 */
	private void initialize() {
		// Bag where have declared the characteristic of a Component in the
		// panel.
		GridBagConstraints c;

		/*
		 * gridx: column position in grid gridy: row position in grid anchor:
		 * bottom of space
		 */

		// Creation of four buttons: play, stop, pause y record.
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(2, 5, 2, 2);

		this.add(getPlayButton(), c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(2, 2, 2, 2);
		this.add(getPauseButton(), c);

		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 0;
		c.insets = new Insets(2, 2, 2, 2);
		this.add(getStopButton(), c);

		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(2, 2, 2, 5);
		this.add(getRecButton(), c);

		// Space between rows.
		c = new GridBagConstraints();
		c.insets = new Insets(2, 5, 2, 2);
		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.WEST;
		this.add(getLabelDuration(), c);

		// textField to introduce the duration of the movie in seconds or
		// frames.
		c = new GridBagConstraints();
		c.insets = new Insets(2, 2, 2, 5);
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		this.add(getSeconds(), c);

		c = new GridBagConstraints();
		c.insets = new Insets(2, 2, 2, 5);
		c.gridx = 3;
		c.gridy = 1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		this.add(getLabelSeconds(), c);
		
		// checkbox for the provisional selection of video in seconds or frames.
		// c.gridx = 3;
		// c.gridy = 1;
		// c.gridwidth = 2;
		// c.anchor = GridBagConstraints.EAST;
		// this.add(getJCheckBox(), c);

		c = new GridBagConstraints();
		c.insets = new Insets(2, 5, 2, 2);
		c.gridx = 1;
		c.gridy = 3;
		c.anchor = GridBagConstraints.WEST;
		this.add(getLabelMode(), c);
		
		
		// choice for mode player selection.
		c = new GridBagConstraints();
		c.insets = new Insets(2, 2, 5, 5);
		c.gridx = 2;
		c.gridy = 3;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 3;
		this.add(getChoiceValue(), c);
		choice.setSelectedIndex(this.animationPlayer.getAnimationMode());
		
		c = new GridBagConstraints();
		c.insets = new Insets(0, 0, 0, 0);
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 5;
		c.weightx = 1.0;
		c.weighty = 1.0;
		this.add(new JPanel(), c);

		setGlobalTime(this.animationPlayer.getGlobalTime() / 1000);

	}

	/**
	 * 
	 * @return JLabel.
	 */
	private JLabel getLabelMode() {
		label2 = new JLabel(PluginServices.getText(this, "Label_Mode"));
		label2.setToolTipText(PluginServices.getText(this, "Modo_de_reproduccion"));
		return label2;
	}

	/**
	 * 
	 * @return JLabel.
	 */
	private JLabel getLabelDuration() {
		label = new JLabel(PluginServices.getText(this, "Label_Duration"));
		label.setToolTipText(PluginServices.getText(this, "Duracion_de_la_animacion"));
		return label;
	}

	private JLabel getLabelSeconds() {
		label3 = new JLabel(PluginServices.getText(this, "seconds"));
		return label3;
	}
	
	/**
	 * @return Choice. Return the choice with the different modes of display.
	 */
	private JComboBox getChoiceValue() {
		if (choice == null) {
			choice = new JComboBox();
			choice.addItem(PluginServices.getText(this, "Loop_mode"));
			choice.addItem(PluginServices.getText(this,"Ping_pong_loop"));
			choice.addItem(PluginServices.getText(this,"Play_once"));
			choice.addItem(PluginServices.getText(this,"Loop_once_backwards"));
			choice.addItemListener(this);
			choice.setToolTipText(PluginServices.getText(this, "Modo_de_reproduccion"));
		}
		return choice;
	}

	/**
	 * @return TextField. Return textfield where the user put the seconds of the
	 *         movie.
	 */
	private JTextField getSeconds() {
		if (textosegs == null) {
			textosegs = new JTextField(PluginServices
					.getText(this, "Text_Secs"), 4);
			textosegs.setName("SECONDS");
			textosegs.setHorizontalAlignment(JTextField.RIGHT);
			textosegs.addKeyListener(this);
			textosegs.addFocusListener(this);
			textosegs.setToolTipText(PluginServices.getText(this, "Duracion_de_la_animacion"));
		}
		return textosegs;
	}

	/**
	 * @return JButton. Return record button.
	 */
	private JButton getRecButton() {
		if (buttonIcon3 == null) {
			buttonIcon3 = new JButton();
			// path where are the image to load in the button.
			image3 = new ImageIcon(buttonPath + "/record.png");
			buttonIcon3.setIcon(image3);
			buttonIcon3.setActionCommand("REC");
			buttonIcon3.addActionListener(this);
			buttonIcon3.setToolTipText(PluginServices.getText(this, "Grabar_animacion"));
		}
		return buttonIcon3;
	}

	/**
	 * @return JButton. Return stop button.
	 */
	private JButton getStopButton() {
		if (buttonIcon2 == null) {
			buttonIcon2 = new JButton();
			image2 = new ImageIcon(buttonPath + "/stop.png");
			buttonIcon2.setIcon(image2);
			buttonIcon2.setActionCommand("STOP");
			buttonIcon2.addActionListener(this);
			buttonIcon2.setToolTipText(PluginServices.getText(this, "Stop"));
		}
		return buttonIcon2;
	}

	/**
	 * @return JButton. Return pause button.
	 */
	private JButton getPauseButton() {
		if (buttonIcon1 == null) {
			buttonIcon1 = new JButton();
			image1 = new ImageIcon(buttonPath + "/pause.png");
			buttonIcon1.setIcon(image1);
			buttonIcon1.setActionCommand("PAUSE");
			buttonIcon1.addActionListener(this);
			buttonIcon1.setToolTipText(PluginServices.getText(this, "Pause"));
		}
		return buttonIcon1;
	}

	/**
	 * @return JButton. Return play button.
	 */
	public JButton getPlayButton() {
		if (buttonIcon == null) {
			buttonIcon = new JButton();
			image = new ImageIcon(buttonPath + "/play.png");
			buttonIcon.setIcon(image);
			buttonIcon.setActionCommand("PLAY");
			buttonIcon.addActionListener(this);
			buttonIcon.setIcon(image);
			buttonIcon.setToolTipText(PluginServices.getText(this, "Play"));
		
		}
		return buttonIcon;
	}
	
	/**
	 * 
	 * @param viewListAdded: open views list
	 */
	
	public void setViewListAdded(List<BaseView> viewListAdded) {
		this.viewListAdded = viewListAdded;
	}


	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 *      logic events of pressed buttons.
	 */
	public void actionPerformed(ActionEvent arg0) {
		String actionCommand = arg0.getActionCommand();
		
		if (actionCommand.equals("PLAY")) { // play pressed
			
			if(this.animationPlayer.getAnimationContainer().isEmpty())
			{
				JOptionPane.showMessageDialog(null,PluginServices.getText(this,"Animacion_sin_vistas_incluidas"),
						"Animación vacia", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			if (runnable == null) {
				runnable = new StateUpdate();
				// Create the thread supplying it with the runnable object
				Thread thread = new Thread(runnable);
				// Start the thread
				thread.start();
			}
			
			Iterator<BaseView> iter = viewListAdded.iterator();
			while (iter.hasNext()) {
				Object object = iter.next();
				if (object instanceof View3D){
					((View3D) object).getCanvas3d().getOSGViewer().setCameraManipulator(null);
				}
			}
			
			this.animationPlayer.play();
			playFlag = true;
			if (this.animationPlayer.getAnimationPlayerState() == AnimationPlayer.ANIMATION_PLAY) {
				image = new ImageIcon(buttonPath + "/playon.png");
				buttonIcon.setIcon(image);
			
				if (pauseFlag) {
					image1 = new ImageIcon(buttonPath + "/pause.png");
					buttonIcon1.setIcon(image1);
				}

			}

		} else if (actionCommand.equals("PAUSE")) { // pause pressed

			this.animationPlayer.pause();
			if (this.animationPlayer.getAnimationPlayerState() == AnimationPlayer.ANIMATION_PAUSE
					&& playFlag) {
				playFlag = false;
				pauseFlag = true;
				image = new ImageIcon(buttonPath + "/play.png");
				buttonIcon.setIcon(image);
				image1 = new ImageIcon(buttonPath + "/pauseon.png");
				buttonIcon1.setIcon(image1);
			}
		} else if (actionCommand.equals("STOP")) {

			Iterator<BaseView> iter = viewListAdded.iterator();
			while (iter.hasNext()) {
				Object object = iter.next();
				if (object instanceof View3D){
					((PlanetViewer) ((View3D) object).getCanvas3d().getOSGViewer()).restoreCustomTerrainManipulator();
				}
			}
			
			animationPlayer.stop();
			if (runnable != null)
				runnable.end();
			if (this.animationPlayer.getAnimationPlayerState() == AnimationPlayer.ANIMATION_STOP
					&& (playFlag || pauseFlag)) {

				if (playFlag) {
					image = new ImageIcon(buttonPath + "/play.png");
					buttonIcon.setIcon(image);
					playFlag = false;
				} else {
					image1 = new ImageIcon(buttonPath + "/pause.png");
					buttonIcon1.setIcon(image1);
					pauseFlag = false;
				}
			}
		
		} else if (actionCommand.equals("REC")) {
			animationPlayer.record();
			JOptionPane.showMessageDialog(null, "Opción no disponible",
					"Alertas de prueba", JOptionPane.WARNING_MESSAGE);

		}// if
	}// public void

	/*
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 * 
	 */
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 *      Events when you put the filmï¿½s seconds in the animation panel.
	 */
	public void keyReleased(KeyEvent arg0) {
//		System.out.println("valor del campo segundos: "
//				+ this.textosegs.getText());
		try {
			globalTime = new Double(this.textosegs.getText()).doubleValue();
		}
		catch (Exception e) {
		}
		this.animationPlayer.setGlobalTime(globalTime);
	}

	/*
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 *      ItemListener methods.
	 */
	public void itemStateChanged(ItemEvent event) {
		Object obj = event.getSource();
		if (obj == choice) {
			operation = choice.getSelectedIndex(); // get select item.
			setMode(operation);
		} else if (obj == checkbox) {
			System.out.println("opcion seleccionada checkbox");
		}
	}

	/*
	 * Player options modes. 
	 */
	private void setMode(int option) {

		if (option == 2) {
			animationMode = AnimationPlayer.PLAY_ONCE;
		} else if (option == 0) {
			animationMode = AnimationPlayer.LOOP;
		} else if (option == 3) {
			animationMode = AnimationPlayer.PLAY_ONCE_BACKWARDS;
		} else if (option == 1)
			animationMode = AnimationPlayer.PING_PONG_LOOP;
		
		this.animationPlayer.setAnimationMode(animationMode);

		//System.out.println("opcion: " + mode);
	}

	public void setGlobalTime(double time) {
		textosegs.setText(Double.toString(time));
	}

	/*
	 * Class to control the automatically stop state of the actual animation.
	 */

	public class StateUpdate implements Runnable {

		private boolean finish = false;

		public void run() {

			while (true) {
				try {
					Thread.sleep(800);
					synchronized (this) {
						if (finish) {
							Iterator<BaseView> iter = viewListAdded.iterator();
							while (iter.hasNext()) {
								Object object = iter.next();
								if (object instanceof View3D){
									((PlanetViewer) ((View3D) object).getCanvas3d().getOSGViewer()).restoreCustomTerrainManipulator();
								}
							}
							break;
						}
					}
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				
				if (animationPlayer.getAnimationPlayerState() == AnimationPlayer.ANIMATION_STOP
						&& (playFlag || pauseFlag)) {

					if (playFlag) {
						image = new ImageIcon(buttonPath + "/play.png");
						buttonIcon.setIcon(image);
						playFlag = false;
					} else {
						image1 = new ImageIcon(buttonPath + "/pause.png");
						buttonIcon1.setIcon(image1);
						pauseFlag = false;
					}
					runnable.end();
				}// long if
			}
		}

		/*
		 * function to stop the thread.
		 */

		public synchronized void end() {
			finish = true;
			runnable = null;
		}
	}// end class StateUpdate.

	/**
	 * Add a listener in a event list
	 * @param listener
	 */
	public void addWindowClosedListener(WindowClosedListener listener) {
		if (!actionCommandListeners.contains(listener))
			actionCommandListeners.add(listener);
	}
	
	/**
	 * Clear a listener in a event list
	 * @param listener
	 */
	public void removeWindowClosedListener(WindowClosedListener listener) {
		actionCommandListeners.remove(listener);
	}

	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
	}

	public void focusLost(FocusEvent e) {
		textosegs.setText(Double.toString(globalTime));
	}
}