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
package org.gvsig.rastertools.clipping.ui.listener;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.JOptionPane;

import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.MouseMovementBehavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.RectangleBehavior;
import org.gvsig.fmap.mapcontrol.tools.Events.EnvelopeEvent;
import org.gvsig.fmap.mapcontrol.tools.Listeners.RectangleListener;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.gui.beans.coordinatespanel.CoordinatesEvent;
import org.gvsig.gui.beans.coordinatespanel.CoordinatesListener;
import org.gvsig.gui.beans.coordinatespanel.CoordinatesPanel;
import org.gvsig.gui.beans.datainput.DataInputContainerListener;
import org.gvsig.gui.beans.table.models.CheckBoxModel;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.grid.roi.ROI;
import org.gvsig.raster.hierarchy.IRasterGeoOperations;
import org.gvsig.raster.hierarchy.IRasterOperations;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.raster.util.process.ClippingProcess;
import org.gvsig.rastertools.clipping.ClippingData;
import org.gvsig.rastertools.clipping.panels.ClippingCoordinatesPanel;
import org.gvsig.rastertools.clipping.panels.ClippingOptionsPanel;
import org.gvsig.rastertools.clipping.panels.ClippingResolutionPanel;
import org.gvsig.rastertools.clipping.panels.ClippingSelectionPanel;
import org.gvsig.rastertools.clipping.ui.ClippingPanel;
import org.gvsig.rastertools.saveraster.ui.info.EndInfoDialog;

import com.iver.andami.PluginServices;
import com.iver.andami.Utilities;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.toolListeners.StatusBarListener;
/**
 * <code>ClippingPanelListener</code> es una clase donde se recogerán y
 * tratarán todos los eventos del panel de recorte
 *
 * @version 19/04/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ClippingPanelListener implements ActionListener, RectangleListener, ButtonsPanelListener, CoordinatesListener, DataInputContainerListener, IProcessActions {
	private Dimension             dim                     = new Dimension();
	private AffineTransform       at                      = null;
	private ClippingPanel         clippingPanel           = null;
	private ClippingData          data                    = null;
	private boolean               enableValueChangedEvent = true;

	private FLyrRasterSE          fLayer                  = null;
	private MapControl            mapControl              = null;

	/**
	 * Herramienta seleccionada en el momento de la apertura del dialogo
	 */
	private String                 lastTool                = null;

	private String                 viewName                = null;

	private static ClippingData    lastDataSaved           = null;

	/**
	 * Crea un nuevo <code>ClippingPanelListener</code> con el
	 * <code>ClippingPanelListener</code> asociado
	 * @param panel
	 */
	public ClippingPanelListener(ClippingPanel clippingPanel) {
		this.clippingPanel = clippingPanel;
	}

	/**
	 * Asigna el modelo de datos
	 * @param data
	 */
	public void setData(ClippingData data) {
		this.data = data;
	}

	/**
	 * Asigna la matriz de transformación entre puntos en coordenadas del raster y
	 * puntos en coordenadas reales.
	 * @param AffineTransform
	 */
	private void setAffineTransform(AffineTransform at) {
		this.at = at;
	}

	/**
	 * Asigna la dimensión del raster
	 * @param dim
	 */
	public void setDimension(Dimension dim) {
		this.dim = dim;
	}

	/**
	 * Método que se invoca cuando se disparan los eventos de los botones de
	 * extensión completa o de seleccion de extensión con el ratón
	 */
	public void actionPerformed(ActionEvent e) {
		// Botón de selección del extent completo
		// Modificamos las coordenadas reales y recalculamos las coordenadas pixel
		if (e.getSource() == getResolutionPanel().getButtonRestore()) {
			getClippingPanel().restoreStatus(data);
		}

		//Load parameters
		if (e.getSource() == getCoordinatesPanel().getButtonBarContainer().getButton(0)) {
			if(lastDataSaved != null) {
				data = (ClippingData)lastDataSaved.clone();
				data.addObserver(getClippingPanel());
				data.updateObservers();
				getClippingPanel().getButtonsPanel().getButton(ButtonsPanel.BUTTON_APPLY).setEnabled(true);
				getClippingPanel().getButtonsPanel().getButton(ButtonsPanel.BUTTON_ACCEPT).setEnabled(true);
			} else
				RasterToolsUtil.messageBoxError(RasterToolsUtil.getText(this, "no_data_saved"), null);
		}

		//Save parameters
		if (e.getSource() == getCoordinatesPanel().getButtonBarContainer().getButton(1)) {
			lastDataSaved = (ClippingData)data.clone();
		}

		//Botón de selección del área máxima asociada a los ROIs
		if (e.getSource() == getCoordinatesPanel().getButtonBarContainer().getButton(2)) {
			ArrayList<ROI> roiList = getFLayer().getRois();
			if(roiList != null && roiList.size() > 0) {
				Extent ext = ROI.getROIsMaximunExtent(roiList);
				assignROISExtent(ext, getFLayer());
			} else
				assignFullExtent();
			return;
		}

		// Botón de selección del extent completo
		// Modificamos las coordenadas reales y recalculamos las coordenadas pixel
		if (e.getSource() == getCoordinatesPanel().getButtonBarContainer().getButton(3)) {
			assignFullExtent();
			return;
		}

		// Botón de selección de la herramienta de seleccionar desde la vista
		if (e.getSource() == getCoordinatesPanel().getButtonBarContainer().getButton(4)) {
			selectToolButton();
			return;
		}

	}

	/**
	 * Asigna el extent completo a los cuadros de texto donde se introducen las coordenadas
	 * reales y píxel.
	 */
	private void assignROISExtent(Extent ext, FLyrRasterSE layer) {
		AffineTransform at = layer.getAffineTransform();
		Point2D ulWc = new Point2D.Double(ext.minX(), ext.maxY());
		Point2D lrWc = new Point2D.Double(ext.maxX(), ext.minY());
		Point2D llWc = new Point2D.Double(ext.minX(), ext.minY());
		Point2D urWc = new Point2D.Double(ext.maxX(), ext.maxY());

		ulWc = getFLayer().adjustWorldRequest(ulWc);
		lrWc = getFLayer().adjustWorldRequest(lrWc);
		llWc = getFLayer().adjustWorldRequest(llWc);
		urWc = getFLayer().adjustWorldRequest(urWc);

		Point2D ulPx = new Point2D.Double();
		Point2D lrPx = new Point2D.Double();
		Point2D llPx = new Point2D.Double();
		Point2D urPx = new Point2D.Double();

		try {
			at.inverseTransform(ulWc, ulPx);
			at.inverseTransform(lrWc, lrPx);
			at.inverseTransform(ulWc, llPx);
			at.inverseTransform(lrWc, urPx);
		} catch (NoninvertibleTransformException e) {
			JOptionPane.showMessageDialog((Component) PluginServices.getMainFrame(), PluginServices.getText(this, "coordenadas_erroneas"));
			return;
		}

		data.setCoorPixel(ulPx, lrPx, llPx, urPx);
		data.setCoorReal(ulWc, lrWc, llWc, urWc);
		data.setAffineTransform(at);
		data.initSize();
		getClippingPanel().saveStatus(data);

		getClippingPanel().getButtonsPanel().getButton(ButtonsPanel.BUTTON_APPLY).setEnabled(true);
		getClippingPanel().getButtonsPanel().getButton(ButtonsPanel.BUTTON_ACCEPT).setEnabled(true);
	}

	/**
	 * Asigna el extent completo a los cuadros de texto donde se introducen las coordenadas
	 * reales y píxel.
	 */
	private void assignFullExtent() {
		Point2D ulPx = new Point2D.Double(0, 0);
		Point2D lrPx = new Point2D.Double(dim.width, dim.height);
		Point2D urPx = new Point2D.Double(dim.width, 0);
		Point2D llPx = new Point2D.Double(0, dim.height);

		//Convertimos nuevamente a coordenadas reales
		Point2D ulWc = new Point2D.Double();
		Point2D lrWc = new Point2D.Double();
		Point2D urWc = new Point2D.Double();
		Point2D llWc = new Point2D.Double();
		at.transform(ulPx, ulWc);
		at.transform(lrPx, lrWc);
		at.transform(urPx, urWc);
		at.transform(llPx, llWc);

		ulPx = new Point2D.Double(0, 0);
		lrPx = new Point2D.Double(dim.width - 1, dim.height - 1);
		urPx = new Point2D.Double(dim.width - 1, 0);
		llPx = new Point2D.Double(0, dim.height - 1);

		data.setCoorPixel(ulPx, lrPx, llPx, urPx);
		data.setCoorReal(ulWc, lrWc, llWc, urWc);
		data.setAffineTransform(at);
		data.initSize();

		getClippingPanel().saveStatus(data);
		getClippingPanel().getButtonsPanel().getButton(ButtonsPanel.BUTTON_APPLY).setEnabled(true);
		getClippingPanel().getButtonsPanel().getButton(ButtonsPanel.BUTTON_ACCEPT).setEnabled(true);
	}

	/**
	 * Al producirse un evento de perdida de foco o pulsación de "enter" en un campo de texto de coordenadas
	 * hay que asignar el nuevo valor introducido.
	 * @param obj
	 */
	private void eventJTextField(CoordinatesEvent e) {
		try {
			if (e.getSource() == getCoordinatesPanel().getPixelCoordinates()) {
				if (e.getName().equals("11"))
					data.setUlxPx(Double.valueOf(getCoordinatesPanel().getPixelCoordinates().getValue11()).doubleValue());
				if (e.getName().equals("12"))
					data.setUlyPx(Double.valueOf(getCoordinatesPanel().getPixelCoordinates().getValue12()).doubleValue());
				if (e.getName().equals("21"))
					data.setLrxPx(Double.valueOf(getCoordinatesPanel().getPixelCoordinates().getValue21()).doubleValue());
				if (e.getName().equals("22"))
					data.setLryPx(Double.valueOf(getCoordinatesPanel().getPixelCoordinates().getValue22()).doubleValue());
				data.updateObservers();
			}

			if (e.getSource() == getCoordinatesPanel().getRealCoordinates()) {
				if (e.getName().equals("11"))
					data.setUlxWc(Double.valueOf(getCoordinatesPanel().getRealCoordinates().getValue11()).doubleValue());
				if (e.getName().equals("12"))
					data.setUlyWc(Double.valueOf(getCoordinatesPanel().getRealCoordinates().getValue12()).doubleValue());
				if (e.getName().equals("21"))
					data.setLrxWc(Double.valueOf(getCoordinatesPanel().getRealCoordinates().getValue21()).doubleValue());
				if (e.getName().equals("22"))
					data.setLryWc(Double.valueOf(getCoordinatesPanel().getRealCoordinates().getValue22()).doubleValue());
				data.updateObservers();
			}
		} catch (NumberFormatException ex1) {
			// No hay valores parseables a decimal en las cajas de texto. No hacemos nada
		}
	}



	/**
	 * Recalcula el valor de los campos de coordenadas reales y pixel. Cuando modificamos algún campo
	 * de las coordenadas reales se modifican los pixeles y viceversa.
	 * @param modifyPx true si se ha modificado algún campo de coordenadas pixel y false si se ha modificado
	 * algún campo de las coordenadas reales.
	 */
	private void recalcCoordFields(boolean modifyPx) {
		try {
			getClippingPanel().getButtonsPanel().getButton(ButtonsPanel.BUTTON_APPLY).setEnabled(false);
			getClippingPanel().getButtonsPanel().getButton(ButtonsPanel.BUTTON_ACCEPT).setEnabled(false);

			if (modifyPx) {
				Point2D ulPx = new Point2D.Double(data.getUlxPx(), data.getUlyPx());
				Point2D lrPx = new Point2D.Double(data.getLrxPx(), data.getLryPx());
				Point2D llPx = new Point2D.Double(data.getLlxPx(), data.getLlyPx());
				Point2D urPx = new Point2D.Double(data.getUrxPx(), data.getUryPx());

				//Comprobamos que las esquinas no esten cambiadas de sitio
				if(ulPx.getX() > lrPx.getX()) {
					double ulTmp = ulPx.getX();
					ulPx.setLocation(lrPx.getX(), ulPx.getY());
					lrPx.setLocation(ulTmp, lrPx.getY());
				}
				if(ulPx.getY() > lrPx.getY()) {
					double ulTmp = ulPx.getY();
					ulPx.setLocation(ulPx.getX(), lrPx.getY());
					lrPx.setLocation(lrPx.getX(), ulTmp);
				}

				//Ajustamos la selección al área
				ulPx = adjustPixelRequest(ulPx);
				lrPx = adjustPixelRequest(lrPx);

				Point2D ulWc = new Point2D.Double();
				Point2D lrWc = new Point2D.Double();
				Point2D llWc = new Point2D.Double();
				Point2D urWc = new Point2D.Double();
				at.transform(ulPx, ulWc);
				at.transform(new Point2D.Double(lrPx.getX() + 1, lrPx.getY() + 1), lrWc);
				at.transform(new Point2D.Double(llPx.getX(), llPx.getY() + 1), llWc);
				at.transform(new Point2D.Double(urPx.getX() + 1, urPx.getY()), urWc);

				data.setCoorPixel(ulPx, lrPx, llPx, urPx);
				data.setCoorReal(ulWc, lrWc, llWc, urWc);
				data.setAffineTransform(at);
				data.initSize();

				getClippingPanel().saveStatus(data);

				getClippingPanel().getButtonsPanel().getButton(ButtonsPanel.BUTTON_APPLY).setEnabled(true);
				getClippingPanel().getButtonsPanel().getButton(ButtonsPanel.BUTTON_ACCEPT).setEnabled(true);
			} else {
				Point2D ulWc = new Point2D.Double(data.getUlxWc(), data.getUlyWc());
				Point2D lrWc = new Point2D.Double(data.getLrxWc(), data.getLryWc());
				Point2D llWc = new Point2D.Double(data.getLlxWc(), data.getLlyWc());
				Point2D urWc = new Point2D.Double(data.getUrxWc(), data.getUryWc());

				//Ajustamos la selección al área
				ulWc = getFLayer().adjustWorldRequest(ulWc);
				lrWc = getFLayer().adjustWorldRequest(lrWc);
				llWc = getFLayer().adjustWorldRequest(llWc);
				urWc = getFLayer().adjustWorldRequest(urWc);

				Point2D ulPx = new Point2D.Double();
				Point2D lrPx = new Point2D.Double();
				Point2D llPx = new Point2D.Double();
				Point2D urPx = new Point2D.Double();

				try {
					at.inverseTransform(ulWc, ulPx);
					at.inverseTransform(lrWc, lrPx);
					at.inverseTransform(llWc, llPx);
					at.inverseTransform(urWc, urPx);
				} catch (NoninvertibleTransformException e) {
					JOptionPane.showMessageDialog((Component) PluginServices.getMainFrame(), PluginServices.getText(this, "coordenadas_erroneas"));
					return;
				}

				data.setCoorPixel(ulPx, lrPx, llPx, urPx);
				data.setCoorReal(ulWc, lrWc, llWc, urWc);
				data.setAffineTransform(at);
				data.initSize();
				getClippingPanel().saveStatus(data);

				getClippingPanel().getButtonsPanel().getButton(ButtonsPanel.BUTTON_APPLY).setEnabled(true);
				getClippingPanel().getButtonsPanel().getButton(ButtonsPanel.BUTTON_ACCEPT).setEnabled(true);
			}
		} catch (NumberFormatException ex) {
			return;
		}

	}

	/**
	 * Ajusta las coordenadas especificadas en los parámetros al área máxima
	 * del raster en píxeles
	 * @param req
	 */
	private Point2D adjustPixelRequest(Point2D req) {
		req.setLocation(Math.max(0, req.getX()), Math.max(0, req.getY()));
		req.setLocation(Math.min(dim.width - 1, req.getX()), Math.min(dim.height - 1, req.getY()));
		return req;
	}

	/**
	 * Invocación de los eventos de la ventana de <code>DefaultButtonsPanel</code>
	 */
	public void actionButtonPressed(ButtonsPanelEvent e) {
		// Botón de Aceptar
		if (e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
			accept();
			close();
		}

		// Botón de Aplicar
		if (e.getButton() == ButtonsPanel.BUTTON_APPLY)
			accept();

		// Botón de Cerrar
		if (e.getButton() == ButtonsPanel.BUTTON_CANCEL)
			close();

		getFLayer().getMapContext().invalidate();
	}

	/**
	 * Cerrar la ventana del recorte
	 */
	private void close() {
		try {
			if (getLastTool() != null)
				getMapControl().setTool(getLastTool());
			PluginServices.getMDIManager().closeWindow(getClippingPanel().getClippingDialog());
		} catch (ArrayIndexOutOfBoundsException ex) {
			// Si la ventana no se puede eliminar no hacemos nada
		}
	}

	/**
	 * Obtener el <code>ClippingPanel</code> asociado
	 * @return ClippingPanel
	 */
	private ClippingPanel getClippingPanel() {
		return clippingPanel;
	}

	private ClippingCoordinatesPanel getCoordinatesPanel() {
		return getClippingPanel().getCoordinatesPanel();
	}

	private ClippingResolutionPanel getResolutionPanel() {
		return getClippingPanel().getResolutionPanel();
	}

	private ClippingOptionsPanel getOptionsPanel() {
		return getClippingPanel().getOptionsPanel();
	}

	private ClippingSelectionPanel getSelectionPanel() {
		return getClippingPanel().getSelectionPanel();
	}

	/**
	 * Acciones realizadas cuando se acepta en el dialogo. Se obtendrán los datos
	 * de recorte desde el dialogo, crearemos el objeto ClippingProcess que
	 * gestiona el recortado, ajustamos el tamaño del grid de salida y procesamos.
	 */
	private void accept() {
		// Controlamos las coordenadas del recorte que no se salgan de la imagen.
		// De ser así mostramos un error
		CoordinatesPanel coordinatesReales = getCoordinatesPanel().getRealCoordinates();
		double ulx = 0;
		double lrx = 0;
		double lry = 0;
		double uly = 0;
		try {
			ulx = Double.parseDouble(coordinatesReales.getValue11());
			lry = Double.parseDouble(coordinatesReales.getValue22());
			lrx = Double.parseDouble(coordinatesReales.getValue21());
			uly = Double.parseDouble(coordinatesReales.getValue12());
			Envelope ext = getFLayer().getFullEnvelope();
			if (((int) ulx) > ((int) ext.getMaximum(0)) || ((int) lrx) < ((int) ext.getMinimum(0)) || ((int) uly) > ((int) ext.getMaximum(1)) || ((int) lry) < ((int) ext.getMinimum(1))) {
				RasterToolsUtil.messageBoxError(RasterToolsUtil.getText(this, "coordenadas_erroneas"), null);
				return;
			}
		} catch (NumberFormatException e) {
			RasterToolsUtil.messageBoxError(RasterToolsUtil.getText(this, "coordenadas_erroneas"), null);
			return;
		}

		double[] wcValues = data.getWcCoordinatesToClip();
		//double[] pxSize = data.getPxSizeToClip();

		// Seleccionamos las bandas que se usaran en el recorte a partir de la tabla
		int countBands = 0;
		int rowCount = ((CheckBoxModel) getSelectionPanel().getTableContainer().getModel()).getRowCount();
		for (int iRow = 0; iRow < rowCount; iRow++)
			if ((((Boolean) ((CheckBoxModel) getSelectionPanel().getTableContainer().getModel()).getValueAt(iRow, 0))).booleanValue())
				countBands++;

		int[] drawableBands = new int[countBands];
		int i = 0;
		for (int iRow = 0; iRow < rowCount; iRow++) {
			if ((((Boolean) ((CheckBoxModel) getSelectionPanel().getTableContainer().getModel()).getValueAt(iRow, 0))).booleanValue()) {
				int row = ((Integer) ((CheckBoxModel) getSelectionPanel().getTableContainer().getModel()).getValueAt(iRow, 2)).intValue();
				drawableBands[i++] = row;
			}
		}

		/**
		 * Donde se va a guardar el fichero
		 */
		String path;
		if (getOptionsPanel().getCbSaveFile().isSelected()) {
			path = getOptionsPanel().getDirectoryTextField().getText();
			File f = new File(path);
			if(!f.exists() || !f.canWrite()) {
				RasterToolsUtil.messageBoxError(RasterToolsUtil.getText(this, "path_not_valid"), null);
				return;
			}
		} else
			path = Utilities.createTempDirectory();

		String file = getOptionsPanel().getFilenameTextField().getText();
		if (file.compareTo(RasterLibrary.getOnlyLayerName()) == 0)
			RasterLibrary.usesOnlyLayerName();

		if (file == "")
			file = "cutlayer";

		String filename = path + File.separator + file;

		if (new File(filename + ".tif").exists())
			if (!RasterToolsUtil.messageBoxYesOrNot("raster_error_file_exists", getOptionsPanel()))
				return;

		/**
		 * Preparacion para la generacion del proceso del recorte
		 */
		if (getFLayer() == null)
			return;

		WriterBufferServer dataWriter1 = new WriterBufferServer();

		AffineTransform transf = calcAffineTransform(wcValues[0], wcValues[1], wcValues[2], wcValues[3],
													Math.round(data.getPxWidth()), Math.round(data.getPxHeight()), at);

		int interpMethod = getResolutionPanel().getSelectedInterpolationMethod();

		// Creamos la interpretación de color para el caso de que la salida tenga
		// más de una banda por fichero. Siempre creamos RED, GREEN y BLUE
		String[] ci = new String[drawableBands.length];
		for (int j = 0; j < ci.length; j++) {
			switch (j) {
				case 0:
					if (ci.length >= 3)
						ci[j] = DatasetColorInterpretation.RED_BAND;
					else
						ci[j] = DatasetColorInterpretation.GRAY_BAND;
					break;
				case 1:
					if (ci.length >= 3)
						ci[j] = DatasetColorInterpretation.GREEN_BAND;
					else
						ci[j] = DatasetColorInterpretation.UNDEF_BAND;
					break;
				case 2:
					ci[j] = DatasetColorInterpretation.BLUE_BAND;
					break;
				default:
					ci[j] = DatasetColorInterpretation.UNDEF_BAND;
					break;
			}
		}

		RasterProcess clippingProcess = new ClippingProcess();
		clippingProcess.setActions(this);
		clippingProcess.addParam("viewname", getViewName());
		//clippingProcess.addParam("pixelcoordinates", dValues);
		clippingProcess.addParam("realcoordinates", wcValues);
		clippingProcess.addParam("filename", filename);
		clippingProcess.addParam("datawriter", dataWriter1);
		clippingProcess.addParam("layer", getFLayer());
		clippingProcess.addParam("drawablebands", drawableBands);
		clippingProcess.addParam("onelayerperband", new Boolean(getOptionsPanel().getCbOneLyrPerBand().isSelected()));
		clippingProcess.addParam("interpolationmethod", new Integer(interpMethod));
		clippingProcess.addParam("affinetransform", transf);
		clippingProcess.addParam("colorInterpretation", new DatasetColorInterpretation(ci));
		clippingProcess.addParam("resolution", new int[]{(int) Math.round(data.getPxWidth()),
														 (int) Math.round(data.getPxHeight())});
		clippingProcess.start();
	}

	/**
	 * Calcula la matriz de transformación que se usará para el nuevo raster generado.
	 * @param ulx Coordenada X real de la esquina superior izquierda
	 * @param uly Coordenada Y real de la esquina superior izquierda
	 * @param lrx Coordenada X real de la esquina inferior derecha
	 * @param lry Coordenada Y real de la esquina inferior derecha
	 * @param width Ancho en píxeles del nuevo raster
	 * @param height Alto en píxeles del nuevo raster
	 * @param trans Matriz de transformación de la nueva capa
	 * @return Matriz de transformación para el nuevo raster
	 */
	private AffineTransform calcAffineTransform(double ulx, double uly, double lrx, double lry,
												double width, double height, AffineTransform trans) {
		Point2D ul = new Point2D.Double(ulx, uly);
		Point2D lr = new Point2D.Double(lrx, lry);
		try {
			trans.inverseTransform(ul, ul);
			trans.inverseTransform(lr, lr);
		} catch (NoninvertibleTransformException e) {
			JOptionPane.showMessageDialog(null, RasterToolsUtil.getText(this, "coordenadas_erroneas"));
			return new AffineTransform();
		}
		double w = Math.abs(lr.getX() - ul.getX());

		Point2D ur = new Point2D.Double(ul.getX() + w, ul.getY());
		Point2D ll = new Point2D.Double(lr.getX() - w, lr.getY() );

		//Obtenemos la georreferenciación de las cuatro esquinas del nuevo raster
		trans.transform(ul, ul);
		trans.transform(ur, ur);
		trans.transform(lr, lr);
		trans.transform(ll, ll);

		double pixelSizeX = (ur.getX() - ul.getX()) / width;
		double pixelSizeY = (ll.getY() - ul.getY()) / height;
		double rotX = trans.getShearX();
		double rotY = trans.getShearY();
		return new AffineTransform(pixelSizeX, rotY, rotX, pixelSizeY, ulx, uly);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#cancelDrawing()
	 */
	public boolean cancelDrawing() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#getCursor()
	 */
	public Cursor getCursor() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.coordinatespanel.CoordinatesListener#actionValueChanged(org.gvsig.gui.beans.coordinatespanel.CoordinatesEvent)
	 */
	public void actionValueChanged(CoordinatesEvent e) {
		if (e.getSource() == getCoordinatesPanel().getPixelCoordinates()) {
			eventJTextField(e);
			recalcCoordFields(true);
		}
		if (e.getSource() == getCoordinatesPanel().getRealCoordinates()) {
			eventJTextField(e);
			recalcCoordFields(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.datainput.DataInputContainerListener#actionValueChanged(java.util.EventObject)
	 */
	public void actionValueChanged(EventObject e) {
		if(!enableValueChangedEvent)
			return;

		enableValueChangedEvent = false; //Desactivamos el evento de cambio de valor de las cajas de texto para que no se bucle

		if (e.getSource() == getResolutionPanel().getCCellSize().getDataInputField()) {
			// Cambiamos PS ==> wPx=wWC/PS & hPx=wPx/rel
			double ps = 0;
			try {
				ps = Double.parseDouble(getResolutionPanel().getCCellSize().getValue());
			} catch (NumberFormatException ex) {
				return;
			}
			data.setPxWidth(data.getWcWidth() / ps);
			data.setPxHeight(data.getWcHeight() / ps);
			data.updateObservers();
		} else if (e.getSource() == getResolutionPanel().getCWidth().getDataInputField()) {
			// Cambiamos wPx ==> hPx=wPx/rel & PS=wWC/wPx
			double wPx = 0;
			try {
				wPx = Double.parseDouble(getResolutionPanel().getCWidth().getValue());
			} catch (NumberFormatException ex) {
				return;
			}
			data.setPxWidth(wPx);
			data.setPxHeight(Math.round(wPx / data.getRatio()));
			data.updateObservers();
		} else if (e.getSource() == getResolutionPanel().getCHeight().getDataInputField()) {
			// Cambiamos hPx ==> wPx=rel*wPx & PS=hWC/hPx
			double hPx = 0;
			try {
				hPx = Double.parseDouble(getResolutionPanel().getCHeight().getValue());
			} catch (NumberFormatException ex) {
				return;
			}
			data.setPxHeight(hPx);
			data.setPxWidth(Math.round(Math.round(hPx * data.getRatio())));
			data.updateObservers();
		}
		enableValueChangedEvent = true;
	}

	/**
	 * Asigna el valor para la activación y desactivación del evento de cambio de valor en
	 * las cajas de texto.
	 * @param enableValueChangedEvent
	 */
	public void setEnableValueChangedEvent(boolean enableValueChangedEvent) {
		this.enableValueChangedEvent = enableValueChangedEvent;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.IProcessActions#end(java.lang.Object)
	 */
	public void end(Object params) {
		if(	params instanceof Object[] &&
			((Object[])params).length == 2 &&
			((Object[])params)[0] instanceof String &&
			((Object[])params)[1] instanceof Long) {

			String fName = (String)((Object[])params)[0];
			long milis = ((Long)((Object[])params)[1]).longValue();

			EndInfoDialog.show(fName, milis);
		}
	}

	/**
	 * Obtener la capa de un raster.
	 * @return
	 */
	public FLyrRasterSE getFLayer() {
		return fLayer;
	}

	/**
	 * Obtiene la ultima herramienta seleccionada antes de cargar el recorte
	 * @return
	 */
	public String getLastTool() {
		return lastTool;
	}

	/**
	 * Obtiene el nombre de la vista
	 * @return
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * Establecer la capa para usarla en el recorte
	 * @param fLayer
	 */
	public void setLayer(FLyrRasterSE fLayer) {
		this.fLayer = fLayer;
		BaseView view = (BaseView) PluginServices.getMDIManager().getActiveWindow();
		viewName = PluginServices.getMDIManager().getWindowInfo(view).getTitle();
		mapControl = view.getMapControl();

		lastTool = mapControl.getCurrentTool();

		// Listener de eventos de movimiento que pone las coordenadas del ratón en
		// la barra de estado
		StatusBarListener sbl = new StatusBarListener(mapControl);

		// Cortar Raster
		ClippingMouseViewListener clippingMouseViewListener = new ClippingMouseViewListener(mapControl, getClippingPanel(), data, fLayer);
		mapControl.addMapTool("cutRaster", new Behavior[] {
				new RectangleBehavior(clippingMouseViewListener), new MouseMovementBehavior(sbl)
			}
		);

		getSelectionPanel().setLayer(fLayer);

		// Obtener la extension completa de la capa

		if(fLayer instanceof IRasterGeoOperations && fLayer instanceof IRasterOperations) {
			setAffineTransform(((IRasterGeoOperations)fLayer).getAffineTransform());
			setDimension(new Dimension((int)((IRasterOperations)fLayer).getPxWidth(), (int)((IRasterOperations)fLayer).getPxHeight()));
		} else {
			RasterToolsUtil.messageBoxError("Error obteniendo el extent.", this);
			return;
		}

	}

	/**
	 * Acciones que se realizan para seleccionar la tool CutRaster
	 */
	public void selectToolButton() {
		// seleccionamos la vista de gvSIG
		com.iver.cit.gvsig.project.documents.view.gui.View theView = null;
		try {
			IWindow[] allViews = PluginServices.getMDIManager().getAllWindows();
			for (int i = 0; i < allViews.length; i++) {
				if (allViews[i] instanceof com.iver.cit.gvsig.project.documents.view.gui.View
						&& PluginServices.getMDIManager().getWindowInfo((View) allViews[i])
								.getTitle().equals(viewName))
					theView = (com.iver.cit.gvsig.project.documents.view.gui.View) allViews[i];
			}
			if (theView == null)
				return;
		} catch (ClassCastException ex) {
			// RasterToolsUtil.messageBoxError("cant_get_view "), this, ex);
			return;
		}
		MapControl m_MapControl = theView.getMapControl();

		// Listener de eventos de movimiento que pone las coordenadas del ratón en
		// la barra de estado
		//StatusBarListener sbl = new StatusBarListener(m_MapControl);

		// Cortar Raster
		/*ClippingMouseViewListener clippingMouseViewListener = new ClippingMouseViewListener(m_MapControl, getClippingPanel(), data, getFLayer());
		m_MapControl.addMapTool("cutRaster", new Behavior[] {
				new RectangleBehavior(clippingMouseViewListener), new MouseMovementBehavior(sbl)
				}
		);*/

		m_MapControl.setTool("clipRaster");
	}

	/**
	 * Obtiene el <code>MapControl</code> de gvSIG
	 * @return <code>MapControl</code>
	 */
	public MapControl getMapControl() {
		return mapControl;
	}


	public void rectangle(EnvelopeEvent event) throws BehaviorException {}
	public void interrupted() {}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener#getImageCursor()
	 */
	public Image getImageCursor() {
		return null;
	}
}