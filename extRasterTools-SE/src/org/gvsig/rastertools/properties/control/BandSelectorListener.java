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
package org.gvsig.rastertools.properties.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.File;

import javax.swing.JOptionPane;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.swing.JFileChooser;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
import org.gvsig.raster.dataset.serializer.RmfSerializerException;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.datastruct.Transparency;
import org.gvsig.raster.grid.GridTransparency;
import org.gvsig.raster.gui.wizards.DriverFileFilter;
import org.gvsig.raster.hierarchy.IRasterDataset;
import org.gvsig.raster.hierarchy.IRasterProperties;
import org.gvsig.raster.hierarchy.IRasterRendering;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.RasterModule;
import org.gvsig.rastertools.properties.panels.BandSelectorPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
/**
 * Clase que maneja los eventos del panel BandSetupPanel. Gestiona el añadir o
 * eliminar ficheros de la lista y contiene las acciones a realizar cuando en
 * panel registrable se pulsa aceptar, aplicar o cancelar.
 *
 * @author Nacho Brodin (brodin_ign@gva.es)
 */
public class BandSelectorListener implements ActionListener {
	private BandSelectorPanel     bandSetupPanel = null;
	private JFileChooser          fileChooser    = null;
	private FLayer                fLayer         = null;
	private IRasterDataset        dataset        = null;
	private IRasterProperties     prop           = null;
	private IRasterRendering      render         = null;
	private boolean               enabled        = true;

	/**
	 * Número de bandas.
	 */
	protected int                 nbands         = 0;

	/**
	 * Lista de geoRasterDataset correspondiente a los ficheros de bandas
	 */
	protected RasterDataset[]     grd            = null;

	/**
	 * Constructor
	 * @param bs Panel del selector de bandas
	 * @param lyr Capa raster
	 */
	public BandSelectorListener(BandSelectorPanel bs) {
		this.bandSetupPanel = bs;
		bs.getFileList().getJButtonAdd().addActionListener(this);
		bs.getFileList().getJButtonRemove().addActionListener(this);
		bs.getNumBandSelectorCombo().addActionListener(this);
	}

	/**
	 * Comprobar si la asignacion de color es correcta para las 4 bandas. No puede
	 * existir una banda con distintas interpretaciones de color. Se comprueban dos
	 * casos, asignaciones en escala de grises o en RGB.
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 * @return
	 */
	private boolean isCorrectAssignedBand(int r, int g, int b, int a) {
		// Si es gris es correcta la asignacion
		if ((r == g) && (r == b) && (r >= 0)) {
			// Si el alpha esta asignado a la misma banda es incorrecto
			if (r == a)
				return false;
			// En caso contrario es correcto
			return true;
		}

		// Si dos bandas coinciden, se dice que no es correcta la asignacion
		int list[] = { r, g, b, a };
		for (int i = 0; i <= 3; i++)
			for (int j = 0; j <= 3; j++)
				if ((i != j) && (list[i] == list[j]) && (list[i] > -1))
					return false;

		return true;
	}

	/**
	 * Constructor
	 * @param bs Panel del selector de bandas
	 * @param lyr Capa raster
	 */
	public void init(IRasterDataset dset, IRasterProperties prop, FLayer lyr) {
		//TODO: FUNCIONALIDAD: Cancelación para la selección de bandas
		this.dataset = dset;
		this.prop = prop;
		fLayer = lyr;
		if(fLayer instanceof IRasterRendering)
			render = (IRasterRendering)fLayer;
	}

	/**
	 * Listener para la gestión de los botones de añadir, eliminar fichero y
	 * el combo de selección de bandas.
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(bandSetupPanel.getFileList().getJButtonAdd()))
			addFileBand();

		if (e.getSource().equals(bandSetupPanel.getFileList().getJButtonRemove()))
			delFileBand();

		if (e.getSource().equals(bandSetupPanel.getNumBandSelectorCombo())) {
			String vBands = (String) bandSetupPanel.getNumBandSelectorCombo().getSelectedItem();
			if (vBands != null) {
				if (vBands.compareTo("3") == 0)
					bandSetupPanel.resetMode(3);

				if (vBands.compareTo("2") == 0)
					bandSetupPanel.resetMode(2);

				if (vBands.compareTo("1") == 0)
					bandSetupPanel.resetMode(1);
			}
		}

		if (e.getSource().equals(bandSetupPanel.getSaveButton())) {
			int rBand = bandSetupPanel.getAssignedBand(RasterDataset.RED_BAND);
			int gBand = bandSetupPanel.getAssignedBand(RasterDataset.GREEN_BAND);
			int bBand = bandSetupPanel.getAssignedBand(RasterDataset.BLUE_BAND);
			int aBand = bandSetupPanel.getAssignedBand(RasterDataset.ALPHA_BAND);

			if (!isCorrectAssignedBand(rBand, gBand, bBand, aBand)) {
				RasterToolsUtil.messageBoxError("combinacion_no_asignable", bandSetupPanel);
				return;
			}

			RasterToolsUtil.messageBoxYesOrNot("color_interpretation_continue", this);
			IRasterDataSource dataSource = ((FLyrRasterSE)fLayer).getDataSource();
			if(dataSource == null) {
				RasterToolsUtil.messageBoxError("error_carga_capa", bandSetupPanel);
				return;
			}

			DatasetColorInterpretation ci = dataSource.getColorInterpretation();
			try {
				// Combinación GRAY
				if ((rBand == gBand) && (rBand == bBand) && (rBand >= 0)) {
					for (int iBand = 0; iBand < bandSetupPanel.getARGBTable().getRowCount(); iBand++) {
						ci.setColorInterpValue(iBand, DatasetColorInterpretation.UNDEF_BAND);
					}
					ci.setColorInterpValue(rBand, DatasetColorInterpretation.GRAY_BAND);
					ci.setColorInterpValue(aBand, DatasetColorInterpretation.ALPHA_BAND);
				} else {
					// Combinación RGB
					for (int iBand = 0; iBand < bandSetupPanel.getARGBTable().getRowCount(); iBand++)
						ci.setColorInterpValue(iBand, bandSetupPanel.getColorInterpretationByBand(iBand));
				}
				((FLyrRasterSE) fLayer).getDataSource().getDataset(0)[0].saveObjectToRmf(DatasetColorInterpretation.class, ci);
			} catch (RmfSerializerException exc) {
				RasterToolsUtil.messageBoxError("error_salvando_rmf", bandSetupPanel, exc);
			} catch (NotInitializeException exc) {
				RasterToolsUtil.messageBoxError("table_not_initialize", bandSetupPanel, exc);
			}
		}

		if (!RasterModule.autoRefreshView)
			return;

		bandSetupPanel.onlyApply();
	}

	/**
	 * Añade una banda al raster
	 * @param e
	 */
	private void addFileBand() {
		// String[] driverNames = null;

		// Creación del dialogo para selección de ficheros

		fileChooser = new JFileChooser("BAND_SELECTOR_LISTENER",JFileChooser.getLastPath("BAND_SELECTOR_LISTENER",null));//,FileOpenWizard.getLastPath());
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setAcceptAllFileFilterUsed(false);

		fileChooser.addChoosableFileFilter(new DriverFileFilter());

		int result = fileChooser.showOpenDialog(bandSetupPanel);

		if (result == JFileChooser.APPROVE_OPTION) {
			IRasterDataSource ds = dataset.getDataSource();
			File[] files = fileChooser.getSelectedFiles();

			JFileChooser.setLastPath("BAND_SELECTOR_LISTENER",files[0]);

			// Lo añadimos a la capa si no esta

			for (int i = 0; i < files.length; i++) {

				// Comprobamos que el fichero no está
				boolean exist = false;
				for (int j = 0; j < ds.getDatasetCount(); j++) {
					if (dataset.getDataSource().getDataset(j)[0].getFName().endsWith(files[i].getName()))
						exist = true;
				}
				if (!exist) {
					try {
						Rectangle2D extentOrigin = prop.getFullRasterExtent().toRectangle2D();

						RasterDataset geoRasterDataset = RasterDataset.open(prop.getProjection(), files[i].getAbsolutePath());
						Extent extentNewFile = geoRasterDataset.getExtent();
						nbands += geoRasterDataset.getBandCount();

						// Comprobamos que el extent y tamaño del fichero añadido sea igual al
						// fichero original. Si no es así no abrimos la capa y mostramos un aviso

						double widthNewFile = (extentNewFile.getMax().getX() - extentNewFile.getMin().getX());
						double heightNewFile = (extentNewFile.getMax().getY() - extentNewFile.getMin().getY());

						if ((widthNewFile - extentOrigin.getWidth()) > 1.0 || (widthNewFile - extentOrigin.getWidth()) < -1.0 || (heightNewFile - extentOrigin.getHeight()) > 1.0
								|| (heightNewFile - extentOrigin.getHeight()) < -1.0) {
							JOptionPane.showMessageDialog(null, PluginServices.getText(this, "extents_no_coincidentes"), "", JOptionPane.ERROR_MESSAGE);
							continue;
						}

						if ((extentNewFile.getMax().getX() - extentNewFile.getMin().getX()) != extentOrigin.getWidth()
								|| (extentNewFile.getMax().getY() - extentNewFile.getMin().getY()) != extentOrigin.getHeight()) {
							JOptionPane.showMessageDialog(null, PluginServices.getText(this, "extents_no_coincidentes"), "", JOptionPane.ERROR_MESSAGE);
							continue;
						}

						geoRasterDataset.close();

					} catch (Exception exc) {
						RasterToolsUtil.messageBoxError("addband_error", bandSetupPanel, exc);
					}

					// Lo añadimos a la capa
					try {
						dataset.addFile(files[i].getAbsolutePath());
					} catch (NotSupportedExtensionException e) {
						RasterToolsUtil.messageBoxError("addband_error", bandSetupPanel, e);
					} catch (RasterDriverException e) {
						RasterToolsUtil.messageBoxError("addband_error", bandSetupPanel, e);
					}
				} else {
					RasterToolsUtil.messageBoxError("fichero_existe" + " " + files[i].getAbsolutePath(), bandSetupPanel);
					return;
				}
			}

			// Añadimos los georasterfile a la tabla del Panel
			try {
				bandSetupPanel.addFiles(ds);
			} catch (NotInitializeException e) {
				RasterToolsUtil.messageBoxError("table_not_initialize", this, e);
			}
		}
	}

	/**
	 * Elimina una banda del raster. Si queda solo un fichero o no se ha
	 * seleccionado ninguna banda no hace nada.
	 *
	 * @param e
	 */
	private void delFileBand() {
		Object[] objects = bandSetupPanel.getFileList().getJList().getSelectedValues();

		for (int i = objects.length - 1; i >=0; i--) {
			if (bandSetupPanel.getFileList().getNFiles() > 1) {
				String pathName = objects[i].toString();
				RasterDataset geoRasterFile = null;
				try {
					geoRasterFile = RasterDataset.open(prop.getProjection(), pathName);
				} catch (NotSupportedExtensionException e) {
					NotificationManager.addError(e.getMessage(), e);
				} catch (RasterDriverException e1) {
					NotificationManager.addError(e1.getMessage(), e1);
				}
				nbands -= geoRasterFile.getBandCount();
				dataset.delFile(pathName);
				String file = pathName.substring(pathName.lastIndexOf("/") + 1);
				file = file.substring(file.lastIndexOf("\\") + 1);
				bandSetupPanel.removeFile(file);
			}
		}
	}

	/**
	 * Acciones a ejecutar cuando se aplica
	 */
	public void apply() {
		if (enabled)
			setNewBandsPositionInRendering();
	}

	/**
	 * Asigna la posición de las bandas en el rederizado basandose en la selección
	 * hecho en la tabla de bandas.
	 */
	public void setNewBandsPositionInRendering() {
		if (prop != null && prop.getRender() != null) {
			if(render != null) {
				render.setRenderBands(new int[]{bandSetupPanel.getAssignedBand(RasterDataset.RED_BAND),
												bandSetupPanel.getAssignedBand(RasterDataset.GREEN_BAND),
												bandSetupPanel.getAssignedBand(RasterDataset.BLUE_BAND)});
				int alphaBand = bandSetupPanel.getAssignedBand(RasterDataset.ALPHA_BAND);
				// Ultima transparencia aplicada en el renderizador
				GridTransparency gt = render.getRenderTransparency();
				if(gt != null)
					gt.setTransparencyBand(alphaBand);

				// Transparencia del dataset
				Transparency t = ((FLyrRasterSE) fLayer).getDataSource().getTransparencyFilesStatus();
				if(t != null)
					t.setTransparencyBand(alphaBand);
			}
			fLayer.getMapContext().invalidate();
		}
	}

	/**
	 * Activa o desactiva la acción del panel
	 * @param enabled true para activa y false para desactivar.
	 */
	public void setEnabledPanelAction(boolean enabled) {
		this.enabled = enabled;
	}
}