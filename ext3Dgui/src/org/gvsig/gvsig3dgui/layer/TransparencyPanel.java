/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *   Av. Blasco Ib��ez, 50
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
package org.gvsig.gvsig3dgui.layer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gvsig.gui.beans.swing.GridBagLayoutPanel;
import org.gvsig.gui.beans.swing.JButton;
import org.gvsig.gvsig3dgui.ProjectView3D;

import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.SingletonWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrDefault;
import com.iver.cit.gvsig.project.documents.view.IProjectView;

/**
 * Dialogo donde se muestran las propiedades de una vista
 * 
 * @author Fernando Gonz�lez Cort�s
 */
public class TransparencyPanel extends GridBagLayoutPanel implements
		SingletonWindow, ChangeListener {

	private ProjectView3D view3D;

	private int width = 250;

	private int height = 100;

	private GridBagLayoutPanel okCancelPanel;

	private JButton okButton;

	private JSlider sliderTrans;

	private int order;

	private Layer3DProps layer3DProps;

	private float opacity;

	private SliderUpdate runnable;

	private FLayer lyr3D;

	/**
	 * This is the default constructor
	 * 
	 * @param f
	 *            Frame padre del dialogo
	 * @param v
	 *            Vista que se representa
	 */
	public TransparencyPanel(IProjectView v) {

		view3D = (ProjectView3D) v;

		// UpdateOrder();
		// cogemos la capa q esta seleccionada

		MapContext mapa = view3D.getMapContext();

		// Getting all selected Extent
		FLayer[] selectedExtent = mapa.getLayers().getActives();

		if (selectedExtent.length == 1 && selectedExtent[0].isAvailable()) {

			lyr3D = (FLayer) selectedExtent[0];
			layer3DProps = Layer3DProps.getLayer3DProps(lyr3D);

			order = layer3DProps.getPlanetOrder();
			opacity = layer3DProps.getOpacity();

		}
		initialize();

	}

	private void UpdateOrder() {
		// cogemos la capa q esta seleccionada

		MapContext mapa = view3D.getMapContext();

		// Getting all selected Extent
		FLayer[] selectedExtent = mapa.getLayers().getActives();
		FLayer lyr3DAux = null;

		if (selectedExtent.length == 1 && selectedExtent[0].isAvailable()) {

			lyr3DAux = (FLayer) selectedExtent[0];
//			if (!(lyr3DAux.getName().equals(lyr3D.getName()))) {
//				// sliderTrans.setValue((int) opacity * 255);
//
//			}
			lyr3D = lyr3DAux;
			layer3DProps = Layer3DProps.getLayer3DProps(lyr3D);
			
			order = layer3DProps.getPlanetOrder();
			// opacity = layer3DProps.getOpacity();
			opacity = (((FLyrDefault) lyr3D).getTransparency())
			/ (float) 255;
		}

	}

	/**
	 * This method initializes this
	 */
	private void initialize() {

		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints1.gridy = 2;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.gridx = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridheight = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new Insets(15, 0, 0, 0);
		gridBagConstraints.gridy = 3;

		this.add(getSliderTrans(), gridBagConstraints1);
		this.add(getOkCancelPanel(), gridBagConstraints);
		// jContentPane = new JPanel();
		// jContentPane.setLayout(new GridBagLayout());
		// jContentPane.add(getJButton(), gridBagConstraints);
		// jContentPane.add(getJSlider(), gridBagConstraints1);

		// Inicialize component
		setName(PluginServices.getText(this, "Transparency"));
		// Introducing the margin
		// setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		// Dimension of the panel
		// setSize(new Dimension(width, height));
		this.setPreferredSize(new Dimension(width, height));
		this.setSize(new Dimension(width, height));

		// ADDING COMPONENTS

		// this.addComponent(getSliderTrans(), new Insets(2, 5, 2, 5));
		//
		// // Accept buton
		// this.addComponent(getOkCancelPanel(), new Insets(2, 5, 2, 5));

		// Launching slider update
		// Create the object with the run() method
		runnable = new SliderUpdate();

		// Create the thread supplying it with the runnable object
		Thread thread = new Thread(runnable);

		// Start the thread
		thread.start();

	}

	private javax.swing.JSlider getSliderTrans() {
		if (sliderTrans == null) {
			sliderTrans = new JSlider(JSlider.HORIZONTAL, 0, 255,
					(int) opacity * 255);
			// Create the label table
			Dictionary labelTable = new Hashtable();
			labelTable.put(new Integer(50), new JLabel("Transparente"));
			labelTable.put(new Integer(215), new JLabel("Opaco"));
			// Adding table to slider
			sliderTrans.setLabelTable(labelTable);
			// Activate the lables
			sliderTrans.setPaintLabels(true);
			// Add Change linstener
			sliderTrans.addChangeListener(this);

		}
		int a = (int) (opacity * 255);
		sliderTrans.setValue(a);
		return sliderTrans;
	}

	public void stateChanged(ChangeEvent e) {
		// this.UpdateOrder();
		JSlider source = (JSlider) e.getSource();
		int trans = (int) source.getValue();
		// System.out.println("valor de la transparencia capturada " + trans);
		UpdateOrder();
		// System.out.println("Activado refresco automatico");
		// Alpha value
		opacity = (float) trans / 255f;
		// Setting opaciti in layer properties
		// layer3DProps.setOpacity(opacity);
		// Setting opacity in layer
		((FLyrDefault) lyr3D).setTransparency(trans);
		// Notify the opacity to planet
		view3D.getPlanet().setTextureOpacityLayer(order, opacity);
		// Repainting canvas
		view3D.getCanvas3d().repaint();

		this.repaint();
	}

	private GridBagLayoutPanel getOkCancelPanel() {
		if (okCancelPanel == null) {
			ActionListener okAction, cancelAction, applyAction;
			okAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					UpdateOrder();
					// Alpha value
					float a = opacity;
					// Setting opaciti in layer properties
					layer3DProps.setOpacity(a);
					// Notify the opacity to planet
					view3D.getPlanet().setTextureOpacityLayer(order, a);
					// Repainting canvas
					view3D.getCanvas3d().repaint();
					// Stop thread
					runnable.end();
					PluginServices.getMDIManager().closeWindow(
							TransparencyPanel.this);
				}
			};
			okCancelPanel = new GridBagLayoutPanel();
			okCancelPanel.setAlignmentX(GridBagLayoutPanel.RIGHT_ALIGNMENT);
			okButton = new JButton();
			okButton.setLabel(PluginServices.getText(this, "Close"));
			okButton.addActionListener(okAction);

			okCancelPanel.addComponent(okButton);
		}
		return okCancelPanel;
	}

	public Object getWindowModel() {
		// TODO Auto-generated method stub
		return view3D;
	}

	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODELESSDIALOG);
		m_viewinfo.setTitle(PluginServices.getText(this, "Transparency"));
		m_viewinfo.setHeight(height);
		m_viewinfo.setWidth(width);
		m_viewinfo.setX(100);
		m_viewinfo.setY(100);
		return m_viewinfo;
	}

	public class SliderUpdate implements Runnable {

		private boolean finish = false;

		ProjectView3D view;

		JSlider slider;

		public SliderUpdate() {
		}

		// This method is called when the thread runs
		public void run() {
			while (true) {
				try {
					Thread.sleep(250);
					synchronized (this) {
						if (finish) {
							break;
						}
					}
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				// aki codigo de comprobacion
				UpdateOrder();
				sliderTrans.setValue((int) (opacity * 255));
				sliderTrans.repaint();
			}
		}

		public synchronized void end() {
			finish = true;
		}

	}
	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}
}
