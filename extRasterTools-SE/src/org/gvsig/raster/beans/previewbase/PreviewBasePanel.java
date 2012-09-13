/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
 */
package org.gvsig.raster.beans.previewbase;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.defaultbuttonspanel.DefaultButtonsPanel;
import org.gvsig.gui.beans.imagenavigator.IClientImageNavigator;
import org.gvsig.gui.beans.imagenavigator.ImageNavigator;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.util.RasterToolsUtil;
/**
 * Panel base para componentes con previsualización.
 * 
 * 19/02/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class PreviewBasePanel extends DefaultButtonsPanel {
	private static final long serialVersionUID = 6028780107787443656L;

	private ImageNavigator        imageNavigator        = null;
	private FLyrRasterSE          fLayer                = null;

	/**
	 * Lista de paneles de usuario que son añadidos a los tabs
	 */
	private ArrayList             userPanel             = new ArrayList();
	/**
	 * Panel de la base del cuadro con opciones comunes para todos los tabs
	 */
	private JPanel                generalPanel          = null;
	/**
	 * Panel de debajo de la vista previa
	 */
	private JPanel                downPreviewPanel      = null;
	private IPreviewRenderProcess renderProcess         = null;

	/**
	 * Panel central para el caso de que sea más de uno (con pestañas)
	 */
	private JTabbedPane           tabbedPane            = null;
	/**
	 * Panel central para el caso de que sea solo uno (sin pestañas)
	 */
	private JPanel                mainPanel             = null;

	private JSplitPane            jPanelRight           = null;
	private JPanel                jPanelPreview         = null;

	private PreviewRequestManager previewRequestManager =  null;
	
	/**
	 * Construir un nuevo PreviewBasePanel
	 * @param userPanel. Lista de paneles de usuario para los tabs
	 * @param generalPanel. Panel inferior global para todos los tabs
	 * @param downPreviewPanel. Panel inferior al de la previsualización
	 * @param renderProcess. Clase para procesar el raster antes del dibujado
	 * @param lyr. Capa raster a mostrar en la previsualización
	 */
	public PreviewBasePanel(ArrayList userPanel, 
							JPanel generalPanel, 
							JPanel downPreviewPanel, 
							IPreviewRenderProcess renderProcess,
							FLyrRasterSE lyr) {
		this(ButtonsPanel.BUTTONS_ACCEPTCANCELAPPLY, userPanel, generalPanel, downPreviewPanel, renderProcess, lyr);
	}

	/**
	 * Construir un nuevo PreviewBasePanel
	 * @param buttons
	 * @param userPanel
	 * @param generalPanel
	 * @param downPreviewPanel
	 * @param renderProcess
	 * @param lyr
	 */
	public PreviewBasePanel(int buttons,
			ArrayList userPanel, 
			JPanel generalPanel, 
			JPanel downPreviewPanel, 
			IPreviewRenderProcess renderProcess,
			FLyrRasterSE lyr) {
		super(buttons);
		this.userPanel = userPanel;
		this.generalPanel = generalPanel;
		this.downPreviewPanel = downPreviewPanel;
		this.renderProcess = renderProcess;
		setLayer(lyr);
		initialize();
	}

	/**
	 * Inicializa los componentes gráficos
	 */
	private void initialize() {
		setLayout(new BorderLayout(8, 8));

		JPanel panelLeft = new JPanel();
		panelLeft.setPreferredSize(new Dimension(390, 0));
		panelLeft.setLayout(new BorderLayout(8, 8));
		if(userPanel != null) {
			if(userPanel.size() == 1)
				panelLeft.add(getPanelWithoutTabs(), BorderLayout.CENTER);
			else if(userPanel.size() > 1)
				panelLeft.add(getTabbedPane(), BorderLayout.CENTER);
		}
		if(generalPanel != null)
			panelLeft.add(getGeneralPanel(), BorderLayout.SOUTH);

		JSplitPane jSplitPane1 = new JSplitPane();
		jSplitPane1.setLeftComponent(panelLeft);
		jSplitPane1.setRightComponent(getPanelRight());
		jSplitPane1.setResizeWeight(1.0);
		jSplitPane1.setContinuousLayout(true);
		jSplitPane1.setUI(new BasicSplitPaneUI() {
			public BasicSplitPaneDivider createDefaultDivider() {
				return new BasicSplitPaneDivider(this) {
					private static final long serialVersionUID = 1L;

					public void setBorder(Border b) {
					}
				};
			}
		});
		jSplitPane1.setBorder(null);

		add(jSplitPane1, BorderLayout.CENTER);
	}
	
	/**
	 * Asigna el panel de la posición 0 del array. Este panel es el que se utiliza
	 * cuando no hay un JTabbedPane
	 * @param panel
	 */
	public void setUniquePanel(JPanel panel) {
		userPanel.clear();
		userPanel.add(panel);
		mainPanel = panel;
		((JSplitPane)getContent().getComponent(0)).setLeftComponent(mainPanel);
		getContent().updateUI();
	}

	/**
	 * Obtiene el componente con los tabs donde se añaden las pestañas del usuario.
	 * @return JTabbedPane
	 */
	public JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane();
			for (int i = 0; i < userPanel.size(); i++) 
				tabbedPane.addTab(((IUserPanelInterface)userPanel.get(i)).getTitle(), ((IUserPanelInterface)userPanel.get(i)).getPanel());
		}
		return tabbedPane;
	}
	
	/**
	 * Obtiene el panel central sin tabs. Esto ocurre cuando el usuario solo quiere un
	 * panel. En este caso no hacen falta pestañas por lo que se incorpora solo un JPanel
	 * @return JTabbedPane
	 */
	public JPanel getPanelWithoutTabs() {
		if (mainPanel == null) {
			if(((IUserPanelInterface)userPanel.get(0)) instanceof JPanel) {
				mainPanel = ((IUserPanelInterface)userPanel.get(0)).getPanel();
			} else
				mainPanel = new JPanel();
		}
		return mainPanel;
	}
	
	/**
	 * Obtiene el panel con opciones comunes a todas las pestañas.
	 * @return JPanel
	 */
	public JPanel getGeneralPanel() {
		return generalPanel;
	}
	
	/**
	 * Obtiene el panel de debajo de la vista previa
	 * @return JPanel
	 */
	public JPanel getDownPreviewPanel() {
		return downPreviewPanel;
	}

	/**
	 * Obtiene la lista de paneles de usuario
	 * @return ArrayList de IUserPanelInterface
	 */
	public ArrayList getUserPanels() {
		return userPanel;
	}
	
	/**
	 * Obtiene el objeto IClientImageNavigator
	 * @return
	 */
	public IClientImageNavigator getPreviewManager() {
		return previewRequestManager;
	}

	/**
	 * Devuelve el componente para la vista previa
	 * @return
	 */
	public ImageNavigator getImageNavigator() {
		if (imageNavigator == null) {
			imageNavigator = new ImageNavigator(getPreviewManager());
			imageNavigator.setFocusable(true);
			imageNavigator.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
		}
		return imageNavigator;
	}

	/**
	 * Obtiene el panel con el componente con la vista previa
	 * @return JPanel
	 */
	public JPanel getPanelPreview() {
		if (jPanelPreview == null) {
			jPanelPreview = new JPanel();
			JPanel jsubpanel2 = new JPanel();
			jPanelPreview.setBorder(BorderFactory.createTitledBorder(null, RasterToolsUtil.getText(this, "vista_previa"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
			jPanelPreview.setLayout(new BorderLayout());
			jsubpanel2.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
			jsubpanel2.setLayout(new BorderLayout());

			jsubpanel2.add(getImageNavigator(), BorderLayout.CENTER);
			jPanelPreview.add(jsubpanel2, BorderLayout.CENTER);

			jPanelPreview.setPreferredSize(new Dimension(237, 237));
			jPanelPreview.setMinimumSize(new Dimension(237, 237));
			jPanelPreview.setMaximumSize(new Dimension(237, 237));
		}
		return jPanelPreview;
	}

	/**
	 * Devuelve el panel donde estará el widget de preview
	 * @return
	 */
	private JSplitPane getPanelRight() {
		if (jPanelRight == null) {
			jPanelRight = new JSplitPane();
			jPanelRight.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jPanelRight.setContinuousLayout(true);
			jPanelRight.setResizeWeight(0.0);
			jPanelRight.setPreferredSize(new Dimension(220, 0));
			jPanelRight.setMinimumSize(new Dimension(220, 0));

			if(getPanelPreview() != null)
				jPanelRight.setTopComponent(getPanelPreview());
			else
				jPanelRight.setTopComponent(new JPanel());
			if(getDownPreviewPanel() != null)
				jPanelRight.setBottomComponent(getDownPreviewPanel());
			else
				jPanelRight.setBottomComponent(new JPanel());
			jPanelRight.setUI(new BasicSplitPaneUI() {
				public BasicSplitPaneDivider createDefaultDivider() {
					return new BasicSplitPaneDivider(this) {
						private static final long serialVersionUID = 1L;

						public void setBorder(Border b) {
						}
					};
				}
			});
			jPanelRight.setBorder(null);
		}
		return jPanelRight;
	}

	/**
	 * Especificar la capa para la previsualización
	 * @param layer
	 */
	public void setLayer(FLyrRasterSE fLayer) {
		this.fLayer = fLayer;
		previewRequestManager = new PreviewRequestManager(this, renderProcess, fLayer);

		// Util para ejecutar el test sin una capa de prueba
		if (fLayer == null)
			return;

		// Especificar las dimensiones de la capa en la vista previa
		Extent extent2 = ((FLyrRasterSE) fLayer).getFullRasterExtent();//.getBufferFactory().getDataSource().getExtent();
		getImageNavigator().setViewDimensions(extent2.getMin().getX(), extent2.getMax().getY(), extent2.getMax().getX(), extent2.getMin().getY());
		getImageNavigator().setZoom(1.0/((FLyrRasterSE) fLayer).getBufferFactory().getDataSource().getCellSize());
		getImageNavigator().setAutoAdjusted();
	}

	/**
	 * Especificar el layer para el recorte
	 * @param layer
	 */
	public FLayer getLayer() {
		return fLayer;
	}
	
	/**
	 * Especifica el tamaño de la vista previa
	 * @param dimension
	 */
	public void setPreviewSize(Dimension dimension) {
		getPanelPreview().setPreferredSize(dimension);
		getPanelPreview().setMinimumSize(dimension);
		getPanelPreview().setMaximumSize(dimension);
	}
	
	/**
	 * Actualizamos la vista previa
	 */
	public void refreshPreview() {
		if (fLayer == null)
			return;
		getImageNavigator().updateBuffer();
	}

}