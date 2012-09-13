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
package org.gvsig.rastertools.geolocation.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EventObject;

import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.MouseMovementBehavior;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.datainput.DataInputContainerListener;
import org.gvsig.gui.beans.swing.JFileChooser;
import org.gvsig.raster.dataset.serializer.RmfSerializerException;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.util.ExtendedFileFilter;
import org.gvsig.raster.util.Historical;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.geolocation.behavior.GeoRasterBehavior;
import org.gvsig.rastertools.geolocation.ui.GeoLocationDialog;
import org.gvsig.rastertools.geolocation.ui.GeoLocationOpeningRasterPanel;
import org.gvsig.rastertools.geolocation.ui.GeoLocationPanel;
import org.gvsig.rastertools.geolocation.ui.GeolocationBaseClassPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.toolListeners.StatusBarListener;

/**
 * Listener para el panel de geolocalización.
 * @version 31/07/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class GeoLocationPanelListener implements ActionListener, DataInputContainerListener, KeyListener {
	private static final GeometryManager 	geomManager 			= GeometryLocator.getGeometryManager();
	private static final Logger 			logger        			= LoggerFactory.getLogger(GeoLocationPanelListener.class);
	private GeolocationBaseClassPanel		panel 					= null;
	private boolean                     	enableValueChangeEvent 	= false;
	private GeoLocationDialog               dialog                  = null;

	/**
	 * Crea un nuevo <code>GeoLocationPanelListener</code>
	 * @param panel
	 */
	public GeoLocationPanelListener(GeolocationBaseClassPanel panel, GeoLocationDialog dialog) {
		this.dialog = dialog;
		this.panel = panel;
	}

	/**
	 * Método que se invoca cuando se disparan los eventos de los botones
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == panel.getSaveButton()) {
			if(RasterToolsUtil.messageBoxYesOrNot(PluginServices.getText(this,"aviso_write_transform"), panel)) {
				try {
					panel.getLayer().saveGeoToRmf();
					panel.activeButtons();
				} catch (RmfSerializerException e1) {
					RasterToolsUtil.messageBoxError(PluginServices.getText(this,"error_salvando_rmf"), panel, e1);
				}
				panel.setModify(false);
			}
			return;
		}

		//Asignamos la georreferenciación que hay en ese momento en el dialogo
		if(e.getSource() == panel.getApplyButton())
			try {
				double ulx = Double.parseDouble(panel.getUlx().getValue());
				double uly = Double.parseDouble(panel.getUly().getValue());
				double psx = Double.parseDouble(panel.getPsx().getValue());
				double psy = Double.parseDouble(panel.getPsy().getValue());
				double rotx = Double.parseDouble(panel.getRotx().getValue());
				double roty = Double.parseDouble(panel.getRoty().getValue());
				if(psx == 0 || psy == 0)
					return;
				AffineTransform at = new AffineTransform(psx, roty, rotx, psy, ulx, uly);
				panel.getLayer().setAffineTransform(at);
				panel.setModify(true);
				if(panel.getMapCtrl() != null)
					panel.getMapCtrl().getMapContext().invalidate();
				else if(panel instanceof GeoLocationOpeningRasterPanel)
					PluginServices.getMDIManager().closeWindow(((GeoLocationOpeningRasterPanel)panel).getDialog());

				return;
			} catch(NumberFormatException ex) {
				RasterToolsUtil.messageBoxError(PluginServices.getText(this,"error_transformacion"), panel);
				return;
			}

		if(e.getSource() == panel.getCancelButton())
			PluginServices.getMDIManager().closeWindow(((GeoLocationOpeningRasterPanel)panel).getDialog());

		Historical hist = panel.getHistorical();
		if(hist == null)
			return;

		AffineTransform at = null;

		//Cargamos la primera transformación
		if(e.getSource() == panel.getFirstButton())
			at = (AffineTransform)hist.getFirst();

		//Cargamos la transformación anterior
		if(e.getSource() == panel.getBackButton())
			at = (AffineTransform)hist.getBack();

		////Cargamos la transformación siguiente
		if(e.getSource() == panel.getNextButton())
			at = (AffineTransform)hist.getNext();

		//Cargamos la georreferenciación del raster
		if(e.getSource() == panel.getResetButton())
			at = panel.getLayer().getDataSource().getOwnAffineTransform();

		//Cargar la georreferenciación desde tfw
		if(e.getSource() == panel.getTfwLoad()) {
			JFileChooser chooser = new JFileChooser("GEO_LOCATION_PANEL_LISTENER", JFileChooser.getLastPath("GEO_LOCATION_PANEL_LISTENER", null));
			chooser.setDialogTitle(PluginServices.getText(this, "seleccionar_fichero"));
			chooser.addChoosableFileFilter(new ExtendedFileFilter("tfw"));
			chooser.addChoosableFileFilter(new ExtendedFileFilter("wld"));

			ExtendedFileFilter allFilters = new ExtendedFileFilter();
			allFilters.addExtension("tfw");
			allFilters.addExtension("wld");
			allFilters.setDescription(PluginServices.getText(this, "todos_soportados"));
			chooser.addChoosableFileFilter(allFilters);
			chooser.setFileFilter(allFilters);

			int returnVal = chooser.showOpenDialog(panel);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				String fName = chooser.getSelectedFile().toString();
				at = readTfw(fName);
			}
			JFileChooser.setLastPath("GEO_LOCATION_PANEL_LISTENER", chooser.getSelectedFile());
		}

		//Centrar el raster en la vista
		if(e.getSource() == panel.getCenterToView()) {
			Extent extentView = calcCenterExtent(panel.getViewPort());
			double x = extentView.minX();
			double y = extentView.maxY();
			double psX = extentView.width() / (panel.getLayer()).getPxWidth();
			double psY = -(extentView.height() / (panel.getLayer()).getPxHeight());
			at = new AffineTransform(psX, 0, 0, psY, x, y);
			panel.setModify(true);
		}
		
		//Recuperar el foco
		if(e.getSource() == panel.getFocus()) {
			if(dialog != null) {
				BaseView theView = dialog.getAssociateView();
				MapControl mapCtrl = theView.getMapControl();
				mapCtrl.setTool("geoPan");
			}
		}

		//Entrará en el caso de que se haya seleccionado alguna transformación
		if(at != null) {
			panel.getLayer().setAT(at);
			panel.loadTransform(at);
			panel.activeButtons();
			if(panel.getMapCtrl() != null)
				panel.getMapCtrl().getMapContext().invalidate();
		}
	}
	
	/**
	 * Control del cambio de valor dentro de las cajas de texto. Cuando esto ocurre tiene
	 * el mismo efecto que el botón "Aplicar"
	 * @param e EventObject
	 */
	public void actionValueChanged(EventObject e) {
		if(isEnableValueChangeEvent() && panel instanceof GeoLocationPanel) {
			ActionEvent ev = new ActionEvent(panel.getApplyButton(), 0, null);
			actionPerformed(ev);
		}
	}

	/**
	 * Obtiene el valor de la variable que informa sobre la activación y desactivación del evento de cambio de valor
	 * dentro de un campo de texto
	 * @return enableValueChangeEvent
	 */
	public boolean isEnableValueChangeEvent() {
		return enableValueChangeEvent;
	}

	/**
	 * Asigna el valor para la activación y desactivación del evento de cambio de valor
	 * dentro de un campo de texto
	 * @param enableValueChangeEvent
	 */
	public void setEnableValueChangeEvent(boolean enableValueChangeEvent) {
		this.enableValueChangeEvent = enableValueChangeEvent;
	}

	/**
	 * Centra el raster asociado a la capa en al extent del viewport pasado
	 * por parámetro.
	 * @param vp ViewPort
	 * @return	Extent para la imagen
	 */
	private Extent calcCenterExtent(ViewPort vp) {
		Extent tempExtent = null;
		double widthPxImg, heightPxImg;

		widthPxImg = (panel.getLayer()).getPxWidth();
		heightPxImg = (panel.getLayer()).getPxHeight();

		if(vp == null || vp.getAdjustedExtent() == null) {
			vp = new ViewPort(null);
			Envelope r2d;
			try {
				r2d = geomManager.createEnvelope(0, 0, widthPxImg,
						heightPxImg, SUBTYPES.GEOM2D);
				vp.setEnvelope(r2d);
			} catch (CreateEnvelopeException e) {
				logger.error("Error creating the envelope", e);
			}			
			tempExtent = new Extent(0, 0, widthPxImg, heightPxImg);
		} else{
			Envelope env=vp.getAdjustedExtent();
			tempExtent = new Extent(new Rectangle2D.Double(env.getMinimum(0),env.getMinimum(1),env.getLength(0),env.getLength(1)));
		}

		double ulX = 0D, ulY = 0D, lrX = 0D, lrY = 0D;
		if(widthPxImg > heightPxImg) {
			double widthView = tempExtent.maxX() - tempExtent.minX();
			ulX = tempExtent.minX() + (widthView / 4);
			lrX = ulX + (widthView / 2);
			double newAlto = ((heightPxImg * (widthView / 2)) / widthPxImg);
			double centroY = tempExtent.minY()+((tempExtent.maxY() - tempExtent.minY())/2);
			ulY = centroY - (newAlto / 2);
			lrY = centroY + (newAlto / 2);
		} else {
			double heightView = tempExtent.maxY() - tempExtent.minY();
			ulY = tempExtent.minY() + (heightView / 4);
			lrY = ulY + (heightView / 2);
			double newAncho = ((widthPxImg * (heightView / 2)) / heightPxImg);
			double centroX = tempExtent.minX()+((tempExtent.maxX() - tempExtent.minX())/2);
			ulX = centroX - (newAncho / 2);
			lrX = centroX + (newAncho / 2);
		}
		return new Extent(ulX, ulY, lrX, lrY);
	}

	/**
	 * Lee las coordenadas de un fichero de tfw con una transformación y
	 * devuelve la clase AffineTransform con dicha transformación. Esta llamada gestiona los
	 * errores producidos actuando en consecuencia. Muestra los mensajes al usuario y retorna
	 * null en caso de tener problemas.
	 * @param fName Nombre del fichero tfw
	 * @return AffineTransform
	 */
	private AffineTransform readTfw(String fName) {
		BufferedReader inGrf = null;
		double[] result = new double[6];
		try {
			inGrf = new BufferedReader(new InputStreamReader(new FileInputStream(fName)));
			String str = inGrf.readLine();
			int count = 0;
			while(str != null && count < 6) {
				try {
					Double value =  new Double(str);
					result[count] = value.doubleValue();
				} catch (NumberFormatException ex) {
					RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_file_not_valid"), panel, ex);
					return null;
				}
				str = inGrf.readLine();
				count ++;
			}
		} catch (FileNotFoundException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_file_not_found"), panel, e);
			return null;
		} catch (IOException ex) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_lectura"), panel, ex);
			return null;
		}
		return new AffineTransform(result[0], result[1], result[2], result[3], result[4], result[5]);
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {

	}

	/**
	 * Capturamos el evento de pulsar la tecla Enter y cancel. Cuando pulsamos enter
	 * ejecuta el evento del botón de aplicar y cuando pulsamos cancel cerramos el cuadro
	 * y no hacemos nada.
	 */
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			ActionEvent ev = new ActionEvent(panel.getApplyButton(), 0, null);
			actionPerformed(ev);
		}

		if(e.getKeyCode() == KeyEvent.VK_ESCAPE && panel.getCancelButton() != null) {
			ActionEvent ev = new ActionEvent(panel.getCancelButton(), 0, null);
			actionPerformed(ev);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent e) {
	}
}