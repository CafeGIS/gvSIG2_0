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
package org.gvsig.georeferencing.ui.zoom;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.georeferencing.ui.zoom.tools.BaseViewTool;
import org.gvsig.georeferencing.ui.zoom.tools.PanTool;
import org.gvsig.georeferencing.ui.zoom.tools.ToolEvent;
import org.gvsig.georeferencing.ui.zoom.tools.ToolListener;
import org.gvsig.georeferencing.ui.zoom.tools.ZoomRectangleTool;
import org.gvsig.raster.util.RasterToolsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <P>
 * Control de zoom. Muestra una imagen contenida en un buffer con controles de aumento
 * y disminución del zoom de la misma.
 * </P><P>
 * El visualizador mostrará el buffer de datos pasado en <code>setDrawParams</code>.
 * En esta llamada asignamos también el Extent del área de datos, tamaño de pixel y punto
 * del buffer que coincidirá con el centro del control de zoom. De esta forma la imagen
 * visualizada se centra sobre este punto. En caso de no tener información geográfica del
 * buffer de datos a visualizar el Extent coincidirá con su tamaño en píxeles y el tamaño
 * de pixel será de 1. El punto donde queramos centrar en este caso se dará también en
 * coordenadas pixel.
 * </P><P>
 * Hay dos usos posibles en este control al accionar el botón de zoom: el primero de ellos, en
 * caso de que no haya ningún listener del control de zoom registrado. En este caso se
 * aplicará un escalado sobre los datos del buffer pasado por parámetro. En el caso de que
 * haya un listener registrado (IExtensionBuffer) no se aplicará un escalado sobre el buffer
 * sino que se aplicará una escala de 1.0 y se ejecutará el método request del interfaz
 * registrado para que el cliente reciba notificación del nuevo extent.
 * </P>
 * <P>
 * En la inicialización podemos solicitar que los controles de zoom estén a la izquierda,
 * a la derecha o no esten.
 * </p>
 *
 * 21/12/2007
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class ViewControl extends JPanel  implements ActionListener, ToolListener {
	private static final long        serialVersionUID    = 1L;
	//Lista de controles
	public static final int          ZOOM_INCREASE       = 0;
	public static final int          ZOOM_DECREASE       = 1;
	public static final int          SELECT_ZOOM_AREA    = 2;
	public static final int          FULL_VIEW           = 3;
	public static final int          PREV_ZOOM           = 4;
	public static final int          PAN                 = 5;

	public static final int          RIGHT_CONTROL       = 1;
	public static final int          LEFT_CONTROL        = 2;
	public static final int          NOCONTROL           = 0;

	private final double             SCALE               = 0.5;

	private int                      control;
	private CanvasZone               canvas              = null;
	private JPanel                   buttons             = null;
	private JButton 				 bZoomMas            = null;
	private JButton 				 bZoomMenos          = null;
	private JToggleButton            bSelectZoomArea     = null;
	private JToggleButton            bMove               = null;
	private JButton 				 bFullView           = null;
	private JButton 				 bPrevZoom           = null;
	private double                   stepScale           = 2;
	private IExtensionRequest        extReq              = null;
	private ViewRecord               zoomRecord          = null;
	private ArrayList                toolList            = null;
    private ViewListener             viewListener        = null;
    private ViewEvent                viewEvent           = null;

	/**
	 * Constructor. Crea el panel y asigna el lado donde se crearán los botones
	 * @param right true para los botones a derecha y false para la izquierda
	 */
	public ViewControl(int control) {
		this.control = control;
		toolList = new ArrayList();

		init();

		//Tools predefinidas
		addTool(new ZoomRectangleTool(canvas, this));
		addTool(new PanTool(canvas, this));
	}

	/**
	 * Asigna el listener de eventos de la vista
	 * @param listener
	 */
	public void setViewListener(ViewListener listener) {
		this.viewListener = listener;
		viewEvent = new ViewEvent(this);
		getCanvas().setViewListener(viewListener);
	}

	/**
	 * Añade una tool a la vista de zoom
	 * @param tool
	 */
	public void addTool(BaseViewTool tool) {
		if(!toolList.contains(tool)) {
			toolList.add(tool);
		}
	}

	/**
	 * Añade una tool a la vista de zoom
	 * @param tool
	 */
	public void replaceTool(BaseViewTool tool) {
		for (int i = 0; i < toolList.size(); i++) {
			if(toolList.get(i).getClass().isInstance(tool)) {
				toolList.remove(i);
				break;
			}
			toolList.add(tool);
		}
	}

	/**
	 * Obtiene la tool seleccionada
	 * @return
	 */
	public BaseViewTool getToolSelected() {
		return canvas.getSelectedTool();
	}

	/**
	 * Selecciona una herramienta a partir de su clase. Si el argumento es null
	 * elimina cualquier herramienta seleccionada.
	 * @param tool
	 */
	public void selectTool(Class tool, boolean select) {
		//Si tool es null se desactivan todas y ponemos la seleccionada en el canvas a null
		if(tool == null) {
			for (int i = 0; i < toolList.size(); i++) {
				((BaseViewTool)toolList.get(i)).setActive(false);
			}
			canvas.setSelectedTool(null);
			return;
		}

		//Si seleccionamos una esta se activa, se asigna al canvas y las otras se desactivan
		if(select) {
			for (int i = 0; i < toolList.size(); i++) {
				if(tool.isInstance(toolList.get(i))) {
					((BaseViewTool)toolList.get(i)).setActive(true);
					canvas.setSelectedTool((BaseViewTool)toolList.get(i));
					if(viewListener != null) {
						viewListener.addingTool(viewEvent);
					}
				} else {
					((BaseViewTool)toolList.get(i)).setActive(false);
				}
			}
			return;
		}
		//Si quitamos una esta se desactiva y si es la del canvas se quita también
		for (int i = 0; i < toolList.size(); i++) {
			if(tool.isInstance(toolList.get(i))) {
				((BaseViewTool)toolList.get(i)).setActive(false);
				if(tool.isInstance(canvas.getSelectedTool())) {
					canvas.setSelectedTool(null);
				}
			}
		}
	}

	/**
	 * Selecciona una herramienta a partir de su clase deseleccionando cualquier
	 * otra que estuviera activa. Si el argumento es null
	 * elimina cualquier herramienta seleccionada.
	 * @param tool
	 */
	public void selectUniqueTool(Class tool) {
		if(tool == null) {
			for (int i = 0; i < toolList.size(); i++) {
				((BaseViewTool)toolList.get(i)).setActive(false);
			}
			canvas.setSelectedTool(null);
			return;
		}
		for (int i = 0; i < toolList.size(); i++) {
			if(tool.isInstance(toolList.get(i))) {
				((BaseViewTool)toolList.get(i)).setActive(true);
				canvas.setSelectedTool((BaseViewTool)toolList.get(i));
				if(viewListener != null) {
					viewListener.addingTool(viewEvent);
				}
			} else {
				((BaseViewTool)toolList.get(i)).setActive(false);
			}
		}
	}

	/**
	 * Registra un listener a la tool especificada por el parámetro de la llamada.
	 * @param tool Clase de la tool a la que queremos registrar el listener
	 * @return true si se ha registrado con éxito y false si no se ha hecho
	 */
	public boolean registerToolListener(Class tool, ToolListener listener) {
		if(tool == null) {
			return false;
		}
		for (int i = 0; i < toolList.size(); i++) {
			if(tool.isInstance(toolList.get(i))) {
				((BaseViewTool)toolList.get(i)).addToolListener(listener);
				return true;
			}
		}
		return false;
	}

	/**
	 * Desactiva las herramientas temporalmente. Guarda el estado en el que estaban
	 * para restaurarlo cuando se invoque a awake
	 */
	public void sleepTools() {
		for (int i = 0; i < toolList.size(); i++) {
			((BaseViewTool)toolList.get(i)).sleep();
		}
	}

	/**
	 * Recupera el estado de activación que tenía antes de la última invocación
	 * de sleep
	 */
	public void awakeTools() {
		for (int i = 0; i < toolList.size(); i++) {
			((BaseViewTool)toolList.get(i)).awake();
		}
	}

	/**
	 * Inicialización de los componetes
	 */
	private void init() {
		BorderLayout layout = new BorderLayout();
		setLayout(layout);

		if(control == LEFT_CONTROL) {
			add(getButtonsPanel(), BorderLayout.WEST);
		}
		if(control == RIGHT_CONTROL) {
			add(getButtonsPanel(), BorderLayout.EAST);
		}

		add(getCanvas(), BorderLayout.CENTER);
	}

	/**
	 * Oculta el botón indicado en el parámetro
	 * @param b Constante definida en ZoomControl
	 */
	public void hideButton(int b) {
		switch (b) {
		case ZOOM_INCREASE: this.getBZoomMas().setVisible(false);
			break;
		case ZOOM_DECREASE: this.getBZoomMenos().setVisible(false);
			break;
		case SELECT_ZOOM_AREA: this.getBSelectZoomArea().setVisible(false);
			break;
		case FULL_VIEW: this.getBFullView().setVisible(false);
			break;
		case PREV_ZOOM: this.getBPrevZoom().setVisible(false);
			break;
		case PAN: this.getBMove().setVisible(false);
			break;
		default:
			break;
		}
	}

	/**
	 * Muestra el botón indicado en el parámetro
	 * @param b Constante definida en ZoomControl
	 */
	public void showButton(int b) {
		switch (b) {
		case ZOOM_INCREASE: this.getBZoomMas().setVisible(true);
			break;
		case ZOOM_DECREASE: this.getBZoomMenos().setVisible(true);
			break;
		case SELECT_ZOOM_AREA: this.getBSelectZoomArea().setVisible(true);
			break;
		case FULL_VIEW: this.getBFullView().setVisible(true);
			break;
		case PREV_ZOOM: this.getBPrevZoom().setVisible(true);
			break;
		case PAN: this.getBMove().setVisible(true);
			break;
		default:
			break;
		}
	}

	/**
	 * Asigna una capa gráfica
	 * @param layer IGraphicLayer
	 */
	public void setGraphicLayer(IGraphicLayer layer) {
		canvas.setGraphicLayer(layer);
	}

	/**
	 * Obtiene el panel de dibujado del zoom
	 * @return Canvas Zone
	 */
	public CanvasZone getCanvas() {
		if(canvas == null) {
			canvas = new CanvasZone();
			canvas.setViewListener(viewListener);
			canvas.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
		}
		return canvas;
	}

	/**
	 * Obtiene el panel con los botones de zoom
	 * @return
	 */
	public JPanel getButtonsPanel() {
		if(buttons == null) {
			buttons = new JPanel();
			GridBagLayout l = new GridBagLayout();
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 0, 3, 3);
			buttons.setLayout(l);

			buttons.add(getBZoomMas(), gbc);
			gbc.gridy = 1;
			buttons.add(getBZoomMenos(), gbc);
			gbc.gridy = 2;
			buttons.add(getBSelectZoomArea(), gbc);
			gbc.gridy = 3;
			buttons.add(getBFullView(), gbc);
			gbc.gridy = 4;
			buttons.add(getBPrevZoom(), gbc);
			gbc.gridy = 5;
			buttons.add(getBMove(), gbc);
		}
		return buttons;
	}

	/**
	 * Crea el botón de zoom más
	 * @return JButton
	 */
	public JButton getBZoomMas() {
		if (bZoomMas == null) {
			bZoomMas = new JButton();
			bZoomMas.setToolTipText(RasterToolsUtil.getText(this, "zoom_mas"));
			bZoomMas.setPreferredSize(new java.awt.Dimension(25,25));
			try{
				bZoomMas.setIcon(RasterToolsUtil.getIcon("increase-icon"));
			}catch(NullPointerException e){
				//Sin icono
			}
			bZoomMas.addActionListener(this);
		}
		return bZoomMas;
	}

	/**
	 * Crea el botón de zoom menos
	 * @return JButton
	 */
	public JButton getBZoomMenos() {
		if (bZoomMenos == null) {
			bZoomMenos = new JButton();
			bZoomMenos.setToolTipText(RasterToolsUtil.getText(this, "zoom_menos"));
			bZoomMenos.setPreferredSize(new java.awt.Dimension(25,25));
			try{
				bZoomMenos.setIcon(RasterToolsUtil.getIcon("decrease-icon"));
			}catch(NullPointerException e){
				//Sin icono
			}
			bZoomMenos.addActionListener(this);
		}
		return bZoomMenos;
	}

	/**
	 * Crea el botón de selección de zoom por área
	 * @return JButton
	 */
	public JToggleButton getBSelectZoomArea() {
		if (bSelectZoomArea == null) {
			bSelectZoomArea = new JToggleButton();
			bSelectZoomArea.setToolTipText(RasterToolsUtil.getText(this, "select_zoom_area"));
			bSelectZoomArea.setPreferredSize(new java.awt.Dimension(25,25));
			try{
				bSelectZoomArea.setIcon(RasterToolsUtil.getIcon("selectzoomarea-icon"));
			}catch(NullPointerException e){
				//Sin icono
			}
			bSelectZoomArea.addActionListener(this);
		}
		return bSelectZoomArea;
	}

	/**
	 * Crea el botón de desplazamiento de imagen
	 * @return JToggleButton
	 */
	public JToggleButton getBMove() {
		if (bMove == null) {
			bMove = new JToggleButton();
			bMove.setToolTipText(RasterToolsUtil.getText(this, "move_image"));
			bMove.setPreferredSize(new java.awt.Dimension(25,25));
			try{
				bMove.setIcon(RasterToolsUtil.getIcon("hand-icon"));
			}catch(NullPointerException e){
				//Sin icono
			}
			bMove.addActionListener(this);
		}
		return bMove;
	}

	/**
	 * Crea el botón de zoom más
	 * @return
	 */
	public JButton getBPrevZoom() {
		if (bPrevZoom == null) {
			bPrevZoom = new JButton();
			bPrevZoom.setToolTipText(RasterToolsUtil.getText(this, "prev_zoom"));
			bPrevZoom.setPreferredSize(new java.awt.Dimension(25,25));
			try{
				bPrevZoom.setIcon(RasterToolsUtil.getIcon("prevzoom-icon"));
			}catch(NullPointerException e){
				//Sin icono
			}
			bPrevZoom.addActionListener(this);
		}
		return bPrevZoom;
	}

	/**
	 * Crea el botón de zoom completo. El zoom completo inicializa al primer zoom
	 * que recibió en caso de que no se esté usando un IExtensionRequest, ya que no tendría conocimiento
	 * del tamaño total de capa, sino solo del buffer que tiene en ese momento. En caso de usar un
	 * IExtensionRequest entonces se hará la petición correspondiente al interfaz.
	 * @return JButton
	 */
	public JButton getBFullView() {
		if (bFullView == null) {
			bFullView = new JButton();
			bFullView.setToolTipText(RasterToolsUtil.getText(this, "full_view"));
			bFullView.setPreferredSize(new java.awt.Dimension(25,25));
			try{
				bFullView.setIcon(RasterToolsUtil.getIcon("fullview-icon"));
			}catch(NullPointerException e){
				//Sin icono
			}
			bFullView.addActionListener(this);
		}
		return bFullView;
	}

	/**
	 * Asigna los parámetros de dibujado
	 * @param img Buffer con un área de datos
	 * @param ext Rectangle2D del área de datos dada
	 * @param pixelSize Tamaño de pixel
	 * @param center Punto del área de datos donde se quiere centrar el dibujado del buffer
	 */
	public void setDrawParams(BufferedImage img, Rectangle2D ext, double pixelSize, Point2D center) {
		getZoomRecord().setRequest(ext);
		canvas.setDrawParams(img, ext, pixelSize, center) ;
	}

	/**
	 * Asigna un nuevo centro de visualización. Vuelve a realizar la petición
	 * @param center
	 */
	public void setCenter(Point2D center) {
		Rectangle2D ext = canvas.getEnvelope();
		double diffX = center.getX() - ext.getCenterX();
		double diffY = center.getY() - ext.getCenterY();
		try {
			ext.setRect(ext.getX() + diffX, ext.getY() + diffY, ext.getWidth(), ext.getHeight());
			canvas.setForceRequest(true);
			extReq.request(ext);
		} catch (InvalidRequestException e) {
			RasterToolsUtil.messageBoxError("error_setview_preview", this);
		}
	}

	/**
	 * Registra un objeto IExtensionRequest para que no se aplique un escalado sobre
	 * el buffer pasado por parámetro. Alternativamente a la aplicación de este escalado
	 * se ejecutará el método request del interfaz para que el cliente pueda pasar un
	 * nuevo buffer con escala 1:1 y con la extensión correspondiente al zoom, Es decir, se
	 * deja en manos del cliente la aplicación del zoom.
	 * @param er
	 */
	public void setExtensionRequest(IExtensionRequest er) {
		this.extReq = er;
	}

	/**
	 * Obtiene el objeto IExtensionRequest
	 * @param er
	 */
	public IExtensionRequest getExtensionRequest() {
		return extReq;
	}

	/**
	 * Obtiene el historico de zooms
	 * @return ZoomRecord
	 */
	public ViewRecord getZoomRecord() {
		if(zoomRecord == null) {
			zoomRecord = new ViewRecord();
			//zoomRecord.setRequest(new Integer(FULL_VIEW));
		}
		return zoomRecord;
	}

	/**
	 * Eventos de los botones zoom más y zoom menos.
	 */
	public void actionPerformed(ActionEvent e) {
		if(extReq == null) {
			if(e.getSource() == bZoomMas) {
				canvas.setZoom(canvas.getZoom() * stepScale);
			}

			if(e.getSource() == bZoomMenos) {
				canvas.setZoom(canvas.getZoom() / stepScale);
			}

			if(e.getSource() == bFullView) {
				canvas.setZoom(1);
			}

		} else {
			double width = 0, height = 0;
			canvas.setZoom(1);

			//Zoom Todo
			if(e.getSource() == bFullView) {
				try {
					extReq.fullExtent();
				} catch (InvalidRequestException e1) {
					RasterToolsUtil.messageBoxError("error_setview_preview", this);
				}
				return;
			}

			//Zoom Anterior
			if(e.getSource() == bPrevZoom) {
				Object request = getZoomRecord().getRequest();
				if(request != null) {
					if(request instanceof Rectangle2D) {
						try {
							extReq.request((Rectangle2D)request);
						} catch (InvalidRequestException e1) {
							RasterToolsUtil.messageBoxError("error_setview_preview", this);
						}
					}
					if(request instanceof Integer && ((Integer)request).intValue() == FULL_VIEW) {
						try {
							extReq.fullExtent();
						} catch (InvalidRequestException e1) {
							RasterToolsUtil.messageBoxError("error_setview_preview", this);
						}
					}
					getZoomRecord().getRequest(); //El que hemos metido al hacer zoom previo no sirve por lo que hay que sacarlo
				}
				return;
			}

			width = canvas.getCanvasEnvelope().getWidth();
			height = canvas.getCanvasEnvelope().getHeight();

			//Desplazamiento
			if(e.getSource() == bMove) {
				if(getBMove().isSelected()) {
					selectTool(PanTool.class, true);
				} else {
					selectTool(PanTool.class, false);
				}
			}

			//Zoom por selección de área
			if(e.getSource() == bSelectZoomArea) {
				if(getBSelectZoomArea().isSelected()) {
					selectTool(ZoomRectangleTool.class, true);
				} else {
					selectTool(ZoomRectangleTool.class, false);
				}
			}

			//Zoom más
			if(e.getSource() == bZoomMas) {
				width = canvas.getCanvasEnvelope().getWidth() * SCALE;
				height = canvas.getCanvasEnvelope().getHeight() * SCALE;
			}
			
			//Zoom menos
			if(e.getSource() == bZoomMenos) {
				width = canvas.getCanvasEnvelope().getWidth() / SCALE;
				height = canvas.getCanvasEnvelope().getHeight() / SCALE;
			}
			//Código común a bZoomMas y bZoomMenos
			double x = canvas.getCenter().getX() - (width / 2);
			double y = canvas.getCenter().getY() - (height / 2);
			try {
				Rectangle2D request = new Rectangle2D.Double(x, y, width, height);
				extReq.request(request);
			} catch (InvalidRequestException e1) {
				RasterToolsUtil.messageBoxError("error_setview_preview", this);
			}
		}
	}
	
	/**
	 * Recarga la vista con los parámetros de tamaño y
	 * extensión actuales
	 */
	public void reload() {
		double width = canvas.getCanvasEnvelope().getWidth();
		double height = canvas.getCanvasEnvelope().getHeight();
		double x = canvas.getCenter().getX() - (width / 2);
		double y = canvas.getCenter().getY() - (height / 2);
		Rectangle2D request = new Rectangle2D.Double(x, y, width, height);
		try {
			canvas.setForceRequest(true);
			extReq.request(request);
		} catch (InvalidRequestException e1) {
			RasterToolsUtil.messageBoxError("error_setview_preview", this);
		}
	}

	/**
	 * Obtiene el ancho del canvas
	 * @return
	 */
	public int getCanvasWith() {
		return canvas.getVisibleRect().width;
	}

	/**
	 * Obtiene el alto del canvas
	 * @return
	 */
	public int getCanvasHeight() {
		return canvas.getVisibleRect().height;
	}

	/**
	 * Evento de finalización de la tool que maneja el viewControl y de la capa con el
	 * cursor gráfico que controla el área de la miniimagen asociada
	 */
	public void endAction(ToolEvent ev) {
		if(ev.getSource() instanceof ZoomRectangleTool || ev.getSource() instanceof PanTool) {
			Rectangle2D extent = (Rectangle2D)canvas.getSelectedTool().getResult();
			try {
				extReq.request(extent);
			} catch (InvalidRequestException e1) {
				RasterToolsUtil.messageBoxError("error_setview_preview", this);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.georeferencing.ui.zoom.tools.ToolListener#activeTool(org.gvsig.rastertools.georeferencing.ui.zoom.tools.ToolEvent)
	 */
	public void onTool(ToolEvent ev) {}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.georeferencing.ui.zoom.tools.ToolListener#offTool(org.gvsig.rastertools.georeferencing.ui.zoom.tools.ToolEvent)
	 */
	public void offTool(ToolEvent ev) {}

}
