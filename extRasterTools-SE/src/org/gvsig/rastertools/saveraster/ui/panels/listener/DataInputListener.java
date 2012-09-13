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
package org.gvsig.rastertools.saveraster.ui.panels.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.EventObject;

import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.datainput.DataInputContainer;
import org.gvsig.gui.beans.datainput.DataInputContainerListener;
import org.gvsig.gui.beans.swing.JFileChooser;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.util.ExtendedFileFilter;
import org.gvsig.raster.util.MathUtils;
import org.gvsig.raster.util.RasterUtilities;
import org.gvsig.rastertools.saveraster.ui.SaveRasterDialog;
import org.gvsig.rastertools.saveraster.ui.SaveRasterPanel;

import com.iver.andami.PluginServices;
/**
 * Panel encargado de manejar los eventos del los controles de Salvar a Raster
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class DataInputListener implements ActionListener, MouseListener, FocusListener, KeyListener, DataInputContainerListener {
	final private static long        serialVersionUID         = -3370601314380922368L;
	private SaveRasterPanel          controlPanel             = null;
	private SaveRasterDialog         dialog                   = null;
	private String                   fName                    = null;
	private double                   widthInPixels            = 0;
	private double                   heightInPixels           = 0;
	private double                   mtsPerPixel              = 0D;
	private Object                   obj                      = null;
	private double                   widthMts                 = 0D, heightMts = 0D;
	private boolean                  enableEventValueChanged  = true;

	/**
	 * This method initializes
	 *
	 */
	public DataInputListener(SaveRasterPanel controlPanel) {
		super();
		this.controlPanel = controlPanel;
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	void initialize() {

		//Añadimos gestión de eventos
		controlPanel.getTScale().addValueChangedListener(this);
		controlPanel.getTMtsPixel().addValueChangedListener(this);
		controlPanel.getTWidth().addValueChangedListener(this);
		controlPanel.getTHeight().addValueChangedListener(this);

		controlPanel.getCbMeasureType().addActionListener(this);

		controlPanel.getTInfDerX().addValueChangedListener(this);
		controlPanel.getTInfDerY().addValueChangedListener(this);
		controlPanel.getTSupIzqX().addValueChangedListener(this);
		controlPanel.getTSupIzqY().addValueChangedListener(this);

		controlPanel.getBSelect().addActionListener(this);
		controlPanel.getBProperties().addActionListener(this);

		controlPanel.getCbResolution().addActionListener(this);
		controlPanel.getCbResolution().addKeyListener(this);
	}

	/**
	 * Asigna un valor al panel padre
	 * @param dialogPanel
	 */
	public void setDialogPanel(SaveRasterDialog dialog) {
		this.dialog = dialog;
	}

	/**
	 * Obtiene el Panel de Controles interior
	 * @return
	 */
	public SaveRasterPanel getSaveParameters() {
		if (controlPanel == null) {
			controlPanel = new SaveRasterPanel();
		}
		return controlPanel;
	}

	/**
	 * A partir del valor de tamaño de imagen de salida, comprueba el tipo de
	 * dato seleccionado por el usuario (Pixels, mms, cms, mts, pulgadas) y devuelve
	 * el valor en pixeles.
	 * @param value Cadena introducida por el usuario
	 */
	private int getCorrectMeasure(String value){
		if(controlPanel.getCbMeasureType().getSelectedItem().equals("Pixels"))
			return (int)Double.parseDouble(value);

		int ppp = Integer.parseInt((String)controlPanel.getCbResolution().getSelectedItem());
		if(controlPanel.getCbMeasureType().getSelectedItem().equals("Mts"))
			return MathUtils.convertMtsToPixels(Double.parseDouble(value), ppp);
		if(controlPanel.getCbMeasureType().getSelectedItem().equals("Cms"))
			return MathUtils.convertCmsToPixels(Double.parseDouble(value), ppp);
		if(controlPanel.getCbMeasureType().getSelectedItem().equals("Mms"))
			return MathUtils.convertMmsToPixels(Double.parseDouble(value), ppp);
		if(controlPanel.getCbMeasureType().getSelectedItem().equals("Inches"))
			return MathUtils.convertInchesToPixels(Double.parseDouble(value), ppp);

		return 0;
	}

	/**
	 * Asigna al JTextField pasado como parámetro el valor en pixels, cms, mms ,mtrs, pulgadas dependiendo
	 * de la selección del combo
	 * @param pixel	Valor en pixels
	 * @param field Campo donde se escribe el valor
	 */
	private void setCorrectMeasure(double pixel, DataInputContainer field){
		if(controlPanel.getCbMeasureType().getSelectedItem().equals("Pixels")) {
			field.setValue(String.valueOf(pixel));
			return;
		}

		enableEventValueChanged = false;
		int ppp = Integer.parseInt((String)controlPanel.getCbResolution().getSelectedItem());
		if(controlPanel.getCbMeasureType().getSelectedItem().equals("Mts"))
			field.setValue(String.valueOf(MathUtils.clipDecimals(MathUtils.convertPixelsToMts(pixel, ppp),5)));

		if(controlPanel.getCbMeasureType().getSelectedItem().equals("Cms"))
			field.setValue(String.valueOf(MathUtils.clipDecimals(MathUtils.convertPixelsToCms(pixel, ppp),5)));

		if(controlPanel.getCbMeasureType().getSelectedItem().equals("Mms"))
			field.setValue(String.valueOf(MathUtils.clipDecimals(MathUtils.convertPixelsToMms(pixel, ppp),5)));

		if(controlPanel.getCbMeasureType().getSelectedItem().equals("Inches"))
			field.setValue(String.valueOf(MathUtils.clipDecimals(MathUtils.convertPixelsToInches(pixel, ppp),5)));
		enableEventValueChanged = true;

	}

		/**
		 * Calculo del tamaño en pixels de la imagen a partir de las coordenadas del
		 * mundo real, la escala y los puntos por pulgada
		 */
		private void recalcParams() {
			validateFields();
			double[] size = null;
			try{
				size = calcSizeInMts();
			} catch (NumberFormatException e) {
				return;
			}
			widthMts = size[0];
			heightMts = size[1];

			int resolution = Integer.parseInt((String)controlPanel.getCbResolution().getSelectedItem());

			//Al variar la resolución independientemente del método seleccionado recalculamos el ancho y el alto
			if (obj.equals(controlPanel.getCbResolution())) {
				String escala = controlPanel.getTScale().getValue();
				try{
					if(controlPanel.getProjection().isProjected())
						calcSizeMtsPixel(Double.parseDouble(escala), resolution);
				}
				catch(NumberFormatException exc){
					calcSizeMtsPixel(0, resolution);
				}
				return;
			}

			//Método por escala seleccionado
			if(	controlPanel.getRbScale().isSelected() &&
				!controlPanel.getTScale().getValue().equals("")) {
				double scale = Double.parseDouble(controlPanel.getTScale().getValue());
				if(controlPanel.getProjection().isProjected())
					calcSizeMtsPixel(scale, resolution);
				else
					calcSizeMtsPixelGeodesicas(scale, resolution);
			}

			//Método por tamaño seleccionado
			if(controlPanel.getRbSize().isSelected()) {
				double rel = (widthMts / heightMts);

				if(	obj != null && obj.equals(controlPanel.getTWidth().getDataInputField()) &&
					!controlPanel.getTWidth().getValue().equals("")) {
					this.widthInPixels = this.getCorrectMeasure(controlPanel.getTWidth().getValue());
					this.heightInPixels = (int)Math.floor(this.widthInPixels / rel);
					if(controlPanel.getProjection().isProjected())
						calcScaleMtsPixel(this.widthInPixels, this.heightInPixels, resolution);
				}
				if(	obj != null && obj.equals(controlPanel.getTHeight().getDataInputField()) &&
							!controlPanel.getTHeight().getValue().equals("")) {
					this.heightInPixels = this.getCorrectMeasure(controlPanel.getTHeight().getValue());
					this.widthInPixels = (int)Math.ceil(this.heightInPixels * rel);
					if(controlPanel.getProjection().isProjected())
						calcScaleMtsPixel(this.widthInPixels, this.heightInPixels, resolution);
				}
				if(	obj != null &&
					obj.equals(controlPanel.getCbMeasureType())) {
						if(controlPanel.getProjection().isProjected())
							calcScaleMtsPixel(this.widthInPixels, this.heightInPixels, resolution);
				}
			}

			//Método metros por pixel seleccionado
			if(	controlPanel.getRbMtsPixel().isSelected() &&
				!controlPanel.getTMtsPixel().getValue().equals("")) {
				double mtsPixel = Double.parseDouble(controlPanel.getTMtsPixel().getValue());
				if(controlPanel.getProjection().isProjected())
					calcSizeScale(mtsPixel, resolution);
			}
		}

		/**
		 * Comprueba si un campo de texto tiene el tipo de dato entero o double y si no lo
		 * tiene lo borra ya que su contenido es invalido para operar con el
		 * @param field	Campo de texto a validar
		 * @param isInt true si el valor a validar es entero y false si es double
		 */
		private void validateTextField(DataInputContainer field){
			try {
					Double.parseDouble(field.getValue());
			} catch (NumberFormatException e) {
					 field.setValue("0");
			}
		}

		/**
		 * Valida los campos de texto
		 */
		private void validateFields(){

			//Validamos la escala si se ha introducido algo
			if(!controlPanel.getTScale().getValue().equals(""))
				validateTextField(controlPanel.getTScale());

			//Validamos los mts por pixel si se ha introducido algo
			if(!controlPanel.getTMtsPixel().getValue().equals(""))
				validateTextField(controlPanel.getTMtsPixel());

			//Validamos el ancho si se ha introducido algo
			if(!controlPanel.getTWidth().getValue().equals(""))
				validateTextField(controlPanel.getTWidth());

			//Validamos el alto si se ha introducido algo
			if(!controlPanel.getTHeight().getValue().equals(""))
				validateTextField(controlPanel.getTHeight());
		}

		/**
		 * Calcula el tamaño en mtrs a partir de las coordenadas.
		 * TODO: Esto solo es valido para UTM. El ancho y alto debe obtenerse desde la vista
		 * @return
		 * @throws NumberFormatException
		 */
		private double[] calcSizeInMts()throws NumberFormatException{
			double[] size = new double[2];

			double lrX = Double.parseDouble(controlPanel.getTInfDerX().getValue());
			double lrY = Double.parseDouble(controlPanel.getTInfDerY().getValue());
			double ulX = Double.parseDouble(controlPanel.getTSupIzqX().getValue());
			double ulY = Double.parseDouble(controlPanel.getTSupIzqY().getValue());

			if (ulX > lrX)
				size[0] = ulX - lrX;
			else
				size[0] = lrX - ulX;

			if (ulY > lrY)
				size[1] = ulY - lrY;
			else
				size[1] = lrY - ulY;

			return size;
		}

		/**
		 * A partir de la escala y la resolución calcula el tamaño en pixels y los metros por pixel
		 * si la imagen está en coordenadas geograficas.
		 * @param scale Escala
		 * @param resolution Resolución
		 */
		private void calcSizeMtsPixelGeodesicas(double scaleIntro, int resolution){
			//TODO: Calculos para imagenes en coordenadas geograficas

			/*double lrX = Double.parseDouble(controlPanel.getTInfDerX().getText());
			double ulX = Double.parseDouble(controlPanel.getTSupIzqX().getText());

			//Nos aseguramos de que la escala sea un entero. Si no lo es ponemos un 0
				try {
						Integer.parseInt(controlPanel.getTScale().getText());
				} catch (NumberFormatException e) {
					controlPanel.getTScale().setText("0");
				}

			double scale = controlPanel.getProjection().getScale(ulX, lrX,
					controlPanel.getWidthInPixelsGeodesicas(),
					Integer.parseInt((String)controlPanel.getCbResolution().getSelectedItem()));

			this.widthInPixels = (int)((scaleIntro * controlPanel.getWidthInPixelsGeodesicas()) / scale);
			this.heightInPixels = (int)((scaleIntro * controlPanel.getHeightInPixelsGeodesicas()) / scale);

				mtsPerPixel = MathUtils.tailDecimals(((double)(widthMts / this.widthInPixels)), 5);

				controlPanel.getTMtsPixel().setText(String.valueOf(mtsPerPixel));
				this.setCorrectMeasure(this.widthInPixels, controlPanel.getTWidth());
				this.setCorrectMeasure(this.heightInPixels, controlPanel.getTHeight());*/
		}

		/**
		 * A partir de la escala y la resolución calcula el tamaño en pixels y los metros por pixel
		 * @param scale Escala
		 * @param resolution Resolución
		 */
		private void calcSizeMtsPixel(double scale, int resolution){
			if ((widthMts <= 0) || (heightMts <= 0) || (scale == 0)){
				controlPanel.getTWidth().setValue("0");
				controlPanel.getTHeight().setValue("0");
				return;
			}

			//Calculo del tamaño de la imagen definitiva en pulgadas
			double widthInches = (widthMts / scale) * MathUtils.INCHESMTR;
			double heightInches = (heightMts / scale) * MathUtils.INCHESMTR;

			//Ancho en pixeles = ppp * widthpulgadas
			this.widthInPixels = (int) (resolution * widthInches);
			this.heightInPixels = (int) (resolution * heightInches);

			mtsPerPixel = (double)(widthMts / this.widthInPixels);
			//double mtsPixelH = wc_altomts/altoPixels;

			//recortamos a 5 decimales
			mtsPerPixel = MathUtils.clipDecimals(mtsPerPixel, 5);

			enableEventValueChanged = false;
			controlPanel.getTMtsPixel().setValue(String.valueOf(mtsPerPixel));
			enableEventValueChanged = true;
			setCorrectMeasure(this.widthInPixels, controlPanel.getTWidth());
			setCorrectMeasure(this.heightInPixels, controlPanel.getTHeight());

			//int anchopixels =(int) (Toolkit.getDefaultToolkit().getScreenResolution() * anchopulgadas);
			//int altopixels = (int) (Toolkit.getDefaultToolkit().getScreenResolution() * altopulgadas);
		}

		/**
		 * A partir de los metros por pixel y la resolución calcula el tamaño en pixels y la escala
		 * @param mtsPixel Metros por pixel
		 * @param resolution Resolución
		 */
		private void calcSizeScale(double mtsPixel, int resolution){
				//Número de pìxeles de ancho y alto
			this.widthInPixels = (int)(widthMts / mtsPixel);
			this.heightInPixels = (int)(heightMts / mtsPixel);

			//Obtenemos los mts/pixel reales ya que el número de pixeles es entero deben redondearse
			mtsPerPixel = (double)(widthMts / widthInPixels);

			//recortamos a 5 decimales
			mtsPerPixel = MathUtils.clipDecimals(mtsPerPixel, 5);

			//Obtenemos el ancho y alto en pulgadas
			double widthInches = (double)(widthInPixels / Integer.parseInt(controlPanel.getCbResolution().getSelectedItem().toString()));
//			double heightInches = (double)(heightInPixels / Integer.parseInt(controlPanel.getCbResolution().getSelectedItem().toString()));

			//Calculo de la escala
			int scale = (int)((widthMts * MathUtils.INCHESMTR) / widthInches);

			controlPanel.getTScale().setValue(String.valueOf(scale));
			controlPanel.getTMtsPixel().setValue(String.valueOf(mtsPerPixel));
			setCorrectMeasure(widthInPixels, controlPanel.getTWidth());
			setCorrectMeasure(heightInPixels, controlPanel.getTHeight());
		}

		/**
		 * A partir del tamaño en pixels de la imagen y la resolución calcula los metros por pixel y la escala
		 * @param widthPixels	Ancho de la imagen en pixels
		 * @param heightPixels	Alto de la imagen en pixels
		 * @param resolution Resolución
		 */
		private void calcScaleMtsPixel(double widthPixels, double heightPixels, int resolution){
			this.widthInPixels = widthPixels;
			this.heightInPixels = heightPixels;

			//Obtenemos los mts/pixel reales ya que el número de pixeles es entero deben redondearse
			mtsPerPixel = (double)(widthMts / widthPixels);

			//recortamos a 5 decimales
			mtsPerPixel = MathUtils.clipDecimals(mtsPerPixel, 5);

			//Obtenemos el ancho y alto en pulgadas
			double widthInches = (double)(widthPixels / Integer.parseInt(controlPanel.getCbResolution().getSelectedItem().toString()));
//			double heightInches = (double)(heightPixels / Integer.parseInt(controlPanel.getCbResolution().getSelectedItem().toString()));

			//Calculo de la escala
			int scale = (int)((widthMts * MathUtils.INCHESMTR) / widthInches);

			controlPanel.getTScale().setValue(String.valueOf(scale));
			controlPanel.getTMtsPixel().setValue(String.valueOf(mtsPerPixel));
			setCorrectMeasure(widthPixels, controlPanel.getTWidth());
			setCorrectMeasure(heightPixels, controlPanel.getTHeight());
		}

	/**
	 * Controla cuando se cumplen todos los requisitos en el formulario para
	 * poder activar el botón de aceptar.
	 */
	public void enableButtons() {
		try {
			if (Double.parseDouble(controlPanel.getTWidth().getValue()) == 0 ||
				Double.parseDouble(controlPanel.getTHeight().getValue()) == 0 ||
				controlPanel.getTWidth().getValue().equals("") ||
				controlPanel.getTHeight().getValue().equals("") ||
				this.fName == null || this.fName.equals("")) {
				if (dialog != null)
					dialog.getButtonsPanel().getButton(ButtonsPanel.BUTTON_APPLY).setEnabled(false);
				return;
			}
		} catch (NumberFormatException e) {
			dialog.getButtonsPanel().getButton(ButtonsPanel.BUTTON_APPLY).setEnabled(false);
			return;
		}

		if (dialog != null)
			dialog.getButtonsPanel().getButton(ButtonsPanel.BUTTON_APPLY).setEnabled(true);
	}

	/**
	 * Gestiona los eventos del cambio de escala de la imagen y lla apertura del
	 * dialogo de selección del nombre del fichero.
	 */
	public void actionPerformed(ActionEvent e) {
		obj = e.getSource();

		// Selector de Fichero sobre el cual se va a salvar a raster
		if (obj.equals(controlPanel.getBSelect())) {
			JFileChooser chooser = new JFileChooser("DATA_INPUT_LISTENER",JFileChooser.getLastPath("DATA_INPUT_LISTENER", null));
			chooser.setDialogTitle(PluginServices.getText(this, "seleccionar_fichero"));
			chooser.setAcceptAllFileFilterUsed(false);

			// Añadimos las extensiones que hayan sido registradas en el driver
			String[] extList = GeoRasterWriter.getDriversExtensions();
			ExtendedFileFilter selectedFilter = null;
			for (int i = 0; i < extList.length; i++) {
				ExtendedFileFilter fileFilter = new ExtendedFileFilter(extList[i]);
				chooser.addChoosableFileFilter(fileFilter);
				if (extList[i].toLowerCase().equals("tif"))
					selectedFilter = fileFilter;
			}
			if (selectedFilter != null)
				chooser.setFileFilter(selectedFilter);

			int returnVal = chooser.showOpenDialog(controlPanel);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				ExtendedFileFilter fileFilter = ((ExtendedFileFilter) chooser.getFileFilter());

				fName = chooser.getSelectedFile().toString();
				fName = fileFilter.getNormalizedFilename(chooser.getSelectedFile());
				String ext = RasterUtilities.getExtensionFromFileName(fName);

				controlPanel.getBProperties().setText(
						PluginServices.getText(this, "props") + " " + GeoRasterWriter.getDriverType(ext));

				controlPanel.getBProperties().setEnabled(true);
				controlPanel.getLFileName().setText(
						fName.substring(fName.lastIndexOf(File.separator) + 1, fName.length()));

				enableButtons();
				JFileChooser.setLastPath("DATA_INPUT_LISTENER", chooser.getSelectedFile());
			}
		}

		// Al variar las unidades recalculamos el ancho y el alto
		if (e.getSource().equals(controlPanel.getCbMeasureType())) {
			setCorrectMeasure(this.widthInPixels, controlPanel.getTWidth());
			setCorrectMeasure(this.heightInPixels, controlPanel.getTHeight());
		}

		if (!obj.equals(controlPanel.getBSelect()))
			this.recalcParams();

		this.enableButtons();

	}

	/**
	 * Devuelve el nombre del fichero seleccionado
	 *
	 * @return Nombre del fichero seleccionado
	 */
	public String getFileName() {
		return fName;
	}

	/**
	 * Pone a null el valor de la variable con el valor del fichero de texto.
	 */
	public void resetFileName() {
		fName = null;
		controlPanel.getLFileName().setText("");
	}

	public void mouseClicked(MouseEvent e) {
		if ((obj.equals(controlPanel.getTHeight()) && e.getSource().equals(
				controlPanel.getTWidth()))
				|| (obj.equals(controlPanel.getTWidth()) && e.getSource()
						.equals(controlPanel.getTHeight())))
			return;
		obj = e.getSource();
		this.recalcParams();
		enableButtons();
	}

	/**
	 * Recalcula el tamaño de la imagen con los datos existentes y activa o
	 * desactiva los botones.
	 */
	public void focusLost(FocusEvent e) {
		obj = e.getSource();
		this.recalcParams();
		enableButtons();
	}

	/**
	 * Activa o Desactiva los botones a través de la función que valida los
	 * campos
	 */
	public void keyTyped(KeyEvent e) {
		enableButtons();
	}

	/**
	 * Activa o Desactiva los botones a través de la función que valida los
	 * campos
	 */
	public void keyPressed(KeyEvent e) {
		enableButtons();
	}

	/**
	 * Activa o Desactiva los botones a través de la función que valida los
	 * campos
	 */
	public void keyReleased(KeyEvent e) {
		enableButtons();
	}

	/**
	 * Obtiene la altura en pixels de la imagen de salida
	 *
	 * @return entero con la altura en pixels
	 */
	public double getHeightInPixels() {
		return heightInPixels;
	}

	/**
	 * Obtiene la anchura en pixels de la imagen de salida
	 *
	 * @return entero con la anchura en pixels
	 */
	public double getWidthInPixels() {
		return widthInPixels;
	}

	/**
	 * Asigna el valor de ancho en pixels
	 * @param value double
	 */
	public void setWidthInPixels(double value) {
		this.widthInPixels = value;
	}

	/**
	 * Asigna el valor de alto en pixels
	 * @param value double
	 */
	public void setHeightInPixels(double value) {
		this.heightInPixels = value;
	}

	public void actionValueChanged(EventObject e) {
		if(enableEventValueChanged) {
			obj = e.getSource();
			this.recalcParams();
			enableButtons();
		}
	}

	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void focusGained(FocusEvent e) {}
}