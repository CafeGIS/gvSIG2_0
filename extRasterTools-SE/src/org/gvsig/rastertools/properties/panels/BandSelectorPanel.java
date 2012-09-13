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
package org.gvsig.rastertools.properties.panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.image.DataBuffer;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.fmap.raster.layers.IRasterLayerActions;
import org.gvsig.gui.beans.panelGroup.panels.AbstractPanel;
import org.gvsig.gui.beans.table.TableContainer;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
import org.gvsig.raster.grid.GridTransparency;
import org.gvsig.raster.hierarchy.IRasterDataset;
import org.gvsig.raster.hierarchy.IRasterProperties;
import org.gvsig.raster.hierarchy.IRasterRendering;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.properties.RasterPropertiesTocMenuEntry;
import org.gvsig.rastertools.properties.control.BandSelectorListener;

import com.iver.andami.PluginServices;
/**
 * Selecciona las bandas visibles en un raster. Contiene una tabla con una fila
 * por cada banda de la imagen. Por medio de checkbox se selecciona para cada
 * RGB que banda de la imagen será visualizada.
 *

 * @author Nacho Brodin (brodin_ign@gva.es)
 */
public class BandSelectorPanel extends AbstractPanel implements TableModelListener {
	final private static long       serialVersionUID = -3370601314380922368L;

	private final String[]          columnNames       = { "A", "R", "G", "B", "Band" };
	private final int[]             columnWidths      = { 22, 22, 22, 22, 334 };

	FLayer                          fLayer = null;

	private BandSelectorFileList    fileList          = null;

	// Ultima y penultima columnas activadas del jtable para cuando hay 2 bandas seleccionadas en el combo
	private int[]                   col               = { 0, 1 };
	private BandSelectorListener    panelListener     = null;
	private IRasterProperties       prop              = null;
	private IRasterDataset          dataset           = null;
	private IRasterRendering        render            = null;
	private TableContainer          table             = null;
	private JButton                 saveButton        = null;

	private JPanel                  buttonsPanel      = null;
	private JComboBox		        jComboBox = null;

	/**
	 * This method initializes
	 */
	public BandSelectorPanel() {
		super();
		panelListener = new BandSelectorListener(this);
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	protected void initialize() {
		GridBagConstraints gbc = new GridBagConstraints();
		setLayout(new GridBagLayout());
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		add(getFileList(), gbc);
		
		gbc.gridy = 1;
		add(getARGBTable(), gbc);
		
		gbc.weighty = 0;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.EAST;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(0, 0, 0, 8);
		add(getButtonsPanel(), gbc);
		
		this.setPreferredSize(new Dimension(100, 80)); 
		super.setLabel(PluginServices.getText(this, "bands_panel"));
	}
	
	/**
	 * Obtiene el panel que contiene la lista de ficheros por banda.
	 * @return Panel FileList
	 */
	public BandSelectorFileList getFileList() {
		if (fileList == null) 
			fileList = new BandSelectorFileList();
		return fileList;
	}
	
	/**
	 * Obtiene la Tabla
	 * @return Tabla de bandas de la imagen
	 */
	public TableContainer getARGBTable() {
		if (table == null) {
			ArrayList listeners = new ArrayList();
			listeners.add(panelListener);
			table = new TableContainer(columnNames, columnWidths, listeners);
			table.setModel("ARGBBandSelectorModel");
			table.setControlVisible(false);
			table.getModel().addTableModelListener(this);
			table.initialize();
		}
		return table;
	}
	
	/**
	 * Obtiene el Panel con botón de salvado y selector de bandas. 
	 * @return JPanel
	 */
	public JPanel getButtonsPanel() {
		if (buttonsPanel == null) {
			buttonsPanel = new JPanel();
			buttonsPanel.setLayout(new GridBagLayout());
			JLabel lbandasVisibles = new JLabel();
			lbandasVisibles.setText("Bandas");

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(0, 8, 0, 0);
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1.0;
			buttonsPanel.add(lbandasVisibles, gbc);

			gbc = new GridBagConstraints();
			gbc.insets = new Insets(0, 8, 0, 0);
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridx = 1;
			getNumBandSelectorCombo().setMinimumSize(new Dimension(50, getNumBandSelectorCombo().getMinimumSize().height));
			getNumBandSelectorCombo().setPreferredSize(new Dimension(50, getNumBandSelectorCombo().getMinimumSize().height));
			buttonsPanel.add(getNumBandSelectorCombo(), gbc);

			gbc = new GridBagConstraints();
			gbc.insets = new Insets(0, 8, 0, 0);
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1.0;
			gbc.gridx = 2;
			buttonsPanel.add(getSaveButton(), gbc);
		}
		return buttonsPanel;
	}
	
	/**
	 * Obtiene el combo del selector del número de bandas
	 * @return JComboBox
	 */
	public JComboBox getNumBandSelectorCombo() {
		if (jComboBox == null) {
			String[] list = { "1", "2", "3" };
			jComboBox = new JComboBox(list);
			jComboBox.setSelectedIndex(2);
			jComboBox.setPreferredSize(new java.awt.Dimension(36, 25));
		}

		return jComboBox;
	}
	
	/**
	 * Botón de salvar la interpretación de color seleccionada como predeterminada en 
	 * la capa
	 * @return JButton
	 */
	public JButton getSaveButton() {
		if(saveButton == null) {
			saveButton = new JButton(RasterToolsUtil.getText(this, "save"));
			saveButton.setToolTipText(RasterToolsUtil.getText(this, "save_color_interpretation"));
			saveButton.addActionListener(panelListener);
		}
		return saveButton;
	}
	
	/**
	 * Añade la lista de georasterfiles a la tabla
	 *
	 * @param files
	 * @throws NotInitializeException 
	 */
	public void addFiles(IRasterDataSource mDataset) throws NotInitializeException {
		getFileList().clear();
		clear();
		for (int i = 0; i < mDataset.getDatasetCount(); i++) {
			String fName = mDataset.getDataset(i)[0].getFName();
			getFileList().addFName(fName);

			String bandName = new File(fName).getName();
			String bandType = "";

			switch (mDataset.getDataset(i)[0].getDataType()[0]) {
			case DataBuffer.TYPE_BYTE:
				bandType = "8U";
				break;
			case DataBuffer.TYPE_INT:
				bandType = "32";
				break;
			case DataBuffer.TYPE_DOUBLE:
				bandType = "64";
				break;
			case DataBuffer.TYPE_FLOAT:
				bandType = "32";
				break;
			case DataBuffer.TYPE_SHORT:
				bandType = "16";
				break;
			case DataBuffer.TYPE_USHORT:
				bandType = "16U";
				break;
			case DataBuffer.TYPE_UNDEFINED:
				bandType = "??";
				break;
			}

			for (int b = 0; b < mDataset.getDataset(i)[0].getBandCount(); b++)
				addBand((b + 1) + " [" + bandType + "] " + bandName);
		}
		readDrawedBands();
		saveStatus();
		evaluateControlsEnabled();
	}

	/**
	 * Elimina un fichero de la lista
	 * @param file Nombre del fichero a eliminar
	 */
	public void removeFile(String file) {
		getFileList().removeFName(file);

		for (int i = 0; i < ((DefaultTableModel) getARGBTable().getModel()).getRowCount(); i++) {
			// Si el fichero borrado estaba seleccionado como banda visible. Pasaremos
			// esta visibilidad a la banda inmediata superior y si esta acción produce
			// una excepción (porque no hay) se pasa al inmediato inferior.
			if (((String) ((DefaultTableModel) getARGBTable().getModel()).getValueAt(i, 4)).endsWith(file)) {
				try {
					if (((Boolean) ((DefaultTableModel) getARGBTable().getModel()).getValueAt(i, 0)).booleanValue()) {
						((DefaultTableModel) getARGBTable().getModel()).setValueAt(new Boolean(true), i - 1, 1);
					}
				} catch (ArrayIndexOutOfBoundsException exc) {
					((DefaultTableModel) getARGBTable().getModel()).setValueAt(new Boolean(true), i + 1, 1);
				}

				try {
					if (((Boolean) ((DefaultTableModel) getARGBTable().getModel()).getValueAt(i, 1)).booleanValue()) {
						((DefaultTableModel) getARGBTable().getModel()).setValueAt(new Boolean(true), i - 1, 2);
					}
				} catch (ArrayIndexOutOfBoundsException exc) {
					((DefaultTableModel) getARGBTable().getModel()).setValueAt(new Boolean(true), i + 1, 2);
				}

				try {
					if (((Boolean) ((DefaultTableModel) getARGBTable().getModel()).getValueAt(i, 2)).booleanValue()) {
						((DefaultTableModel) getARGBTable().getModel()).setValueAt(new Boolean(true), i - 1, 3);
					}
				} catch (ArrayIndexOutOfBoundsException exc) {
					((DefaultTableModel) getARGBTable().getModel()).setValueAt(new Boolean(true), i + 1, 3);
				}

				((DefaultTableModel) getARGBTable().getModel()).removeRow(i);
				i--; // Ojo! que hemos eliminado una fila
			}
		}
		panelListener.setNewBandsPositionInRendering();
		evaluateControlsEnabled();
	}
	
	/**
	 * Evalua la habilitación o desabilitación de controles
	 */
	public void evaluateControlsEnabled() {
		if(getFileList().getNFiles() > 1)
			getSaveButton().setEnabled(false);
		else
			getSaveButton().setEnabled(true);
	}

	/**
	 * Cuando cambiamos el combo de seleccion de numero de bandas a visualizar
	 * debemos resetear la tabla de checkbox para que no haya activados más de los
	 * permitidos
	 * @param mode
	 */
	public void resetMode(int mode) {
		DefaultTableModel model = getARGBTable().getModel();
		// Reseteamos los checkbox
		for (int i = 0; i < (model.getColumnCount() - 1); i++)
			for (int j = 0; j < model.getRowCount(); j++)
				model.setValueAt(Boolean.FALSE, j, i);

		// Asignamos los valores
		if (getNBands() >= 1) {
			switch (mode) {
				case 3:
					int b = 2;
					if (getNBands() < 3)
						b = getNBands() - 1;
					model.setValueAt(Boolean.TRUE, b, 3);
				case 2:
					int g = 1;
					if (getNBands() == 1)
						g = 0;
					model.setValueAt(Boolean.TRUE, g, 2);
				case 1:
					model.setValueAt(Boolean.TRUE, 0, 1);
			}
		}

		col[0] = 0;
		col[1] = 1;
	}

	/**
	 * Añade una banda a la tabla bandas de la imagen asignandole un nombre y
	 * valor a los checkbox
	 * @param bandName Nombre de la banda
	 * @throws NotInitializeException 
	 */
	private void addBand(String bandName) throws NotInitializeException {
		Object[] row = {	new Boolean(false), 
							new Boolean(false), 
							new Boolean(false), 
							new Boolean(false), 
							bandName };
		getARGBTable().addRow(row);
	}

	/**
	 * Elimina todas las entradas de la tabla de bandas.
	 */
	private void clear() {
		int rows = ((DefaultTableModel) getARGBTable().getModel()).getRowCount();
		if (rows > 0) {
			for (int i = 0; i < rows; i++)
				((DefaultTableModel) getARGBTable().getModel()).removeRow(0);
		}
	}

	/**
	 * Obtiene el número de bandas de la lista
	 *
	 * @return
	 */
	public int getNBands() {
		return ((DefaultTableModel) getARGBTable().getModel()).getRowCount();
	}

	/**
	 * Obtiene el nombre de la banda de la posición i de la tabla
	 *
	 * @param i
	 * @return
	 */
	public String getBandName(int i) {
		String s = (String) ((DefaultTableModel) getARGBTable().getModel()).getValueAt(i, 3);
		return s.substring(s.lastIndexOf("[8U]") + 5, s.length());
	}

	/**
	 * Mantiene la asignación entre R, G o B y la banda de la imagen que le
	 * corresponde
	 *
	 * @param nBand Banda de la imagen que corresponde
	 * @param flag R, G o B se selecciona por medio de un flag que los identifica
	 */
	public void assignBand(int nBand, int flag) {
		Boolean t = new Boolean(true);
		try {
			if ((flag & RasterDataset.ALPHA_BAND) == RasterDataset.ALPHA_BAND)
				((DefaultTableModel) getARGBTable().getModel()).setValueAt(t, nBand, 0);
			
			if ((flag & RasterDataset.RED_BAND) == RasterDataset.RED_BAND)
				((DefaultTableModel) getARGBTable().getModel()).setValueAt(t, nBand, 1);

			if ((flag & RasterDataset.GREEN_BAND) == RasterDataset.GREEN_BAND)
				((DefaultTableModel) getARGBTable().getModel()).setValueAt(t, nBand, 2);

			if ((flag & RasterDataset.BLUE_BAND) == RasterDataset.BLUE_BAND)
				((DefaultTableModel) getARGBTable().getModel()).setValueAt(t, nBand, 3);
		} catch (ArrayIndexOutOfBoundsException e) {

		}
	}

	/**
	 * Obtiene la correspondencia entre el R, G o B y la banda asignada
	 *
	 * @param flag R, G o B se selecciona por medio de un flag que los identifica
	 * @return Banda de la imagen asignada al flag pasado por parámetro
	 */
	public int getAssignedBand(int flag) {
		DefaultTableModel model = ((DefaultTableModel) getARGBTable().getModel());
		if ((flag & RasterDataset.ALPHA_BAND) == RasterDataset.ALPHA_BAND) {
			for (int nBand = 0; nBand < getARGBTable().getModel().getRowCount(); nBand++)
				if (((Boolean) model.getValueAt(nBand, 0)).booleanValue())
					return nBand;
		}
		
		if ((flag & RasterDataset.RED_BAND) == RasterDataset.RED_BAND) {
			for (int nBand = 0; nBand < getARGBTable().getModel().getRowCount(); nBand++)
				if (((Boolean) model.getValueAt(nBand, 1)).booleanValue())
					return nBand;
		}

		if ((flag & RasterDataset.GREEN_BAND) == RasterDataset.GREEN_BAND) {
			for (int nBand = 0; nBand < getARGBTable().getModel().getRowCount(); nBand++)
				if (((Boolean) model.getValueAt(nBand, 2)).booleanValue())
					return nBand;
		}

		if ((flag & RasterDataset.BLUE_BAND) == RasterDataset.BLUE_BAND) {
			for (int nBand = 0; nBand < getARGBTable().getModel().getRowCount(); nBand++)
				if (((Boolean) model.getValueAt(nBand, 3)).booleanValue())
					return nBand;
		}

		return -1;
	}

	/**
	 * Obtiene la interpretación de color por número de banda
	 * @param nBand Número de banda
	 * @return Interpretación de color. Constante definida en DatasetColorInterpretation
	 */
	public String getColorInterpretationByBand(int nBand) {
		DefaultTableModel model = ((DefaultTableModel) getARGBTable().getModel());
		try {
			for (int iBand = 0; iBand < getARGBTable().getRowCount(); iBand++) {
				for (int col = 0; col < 4; col++) {
					if(((Boolean) model.getValueAt(nBand, col)).booleanValue()) {
						switch (col) {
						case 0: return DatasetColorInterpretation.ALPHA_BAND; 
						case 1: return DatasetColorInterpretation.RED_BAND; 
						case 2: return DatasetColorInterpretation.GREEN_BAND;
						case 3: return DatasetColorInterpretation.BLUE_BAND; 
						}
					}
				}	
			}
		} catch (NotInitializeException e) {
			RasterToolsUtil.messageBoxError("table_not_initialize", this, e);
		}
		return DatasetColorInterpretation.UNDEF_BAND;
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	public void tableChanged(TableModelEvent e) {
		getARGBTable().revalidate();
		revalidate();
	}


	/**
	 * Activa o desactiva la funcionalidad
	 */
	private void actionEnabled() {
		boolean enabled = true;

		FLyrRasterSE fLyrRasterSE = ((FLyrRasterSE) fLayer);

		if (!fLyrRasterSE.isActionEnabled(IRasterLayerActions.BANDS_FILE_LIST))
			enabled = false;
		getFileList().setEnabled(enabled);

		enabled = true;
		if (!fLyrRasterSE.isActionEnabled(IRasterLayerActions.BANDS_RGB))
			enabled = false;
		getARGBTable().setEnabled(enabled);

		// TODO: Mirar el setVisible...
		if (!fLyrRasterSE.isActionEnabled(IRasterLayerActions.BANDS_FILE_LIST) &&
				!fLyrRasterSE.isActionEnabled(IRasterLayerActions.BANDS_RGB))
			setVisible(false);
		else
			setVisible(true);
		
		if (!fLyrRasterSE.isActionEnabled(IRasterLayerActions.SAVE_COLORINTERP))
			getSaveButton().setVisible(false);
	}

	/**
	 * Lee desde el renderizador las bandas que se han dibujado y en que posición se ha hecho.
	 */
	public void readDrawedBands() {
		if (prop.getRender() != null) {
			int[] renderBands = render.getRenderBands();
			GridTransparency transp = render.getRenderTransparency();
			if(transp != null && transp.isTransparencyActive() && transp.getAlphaBandNumber() != -1)
				this.assignBand(transp.getAlphaBandNumber(), RasterDataset.ALPHA_BAND);
			for (int i = 0; i < renderBands.length; i++) {
				if (renderBands[i] >= 0) {
					switch (i) {
					case 0:
						this.assignBand(renderBands[i], RasterDataset.RED_BAND);
						break;
					case 1:
						this.assignBand(renderBands[i], RasterDataset.GREEN_BAND);
						break;
					case 2:
						this.assignBand(renderBands[i], RasterDataset.BLUE_BAND);
						break;
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#accept()
	 */
	public void accept() {
		if (!getPanelGroup().isPanelInGUI(this))
			return;

		onlyApply();
	}

	/**
	 * Aplica y guarda los cambios del panel
	 */
	public void apply() {
		if (!getPanelGroup().isPanelInGUI(this))
			return;

		onlyApply();
		saveStatus();
	}

	/**
	 * Aplicar los cambios sin guardar su estado
	 */
	public void onlyApply() {
		if (RasterPropertiesTocMenuEntry.enableEvents)
			panelListener.apply();
	}

	/**
	 * Guarda el estado actual del panel
	 */
	private void saveStatus() {
		ArrayList aux = new ArrayList();
		int[] renderBands = render.getRenderBands();
		for (int i = 0; i < renderBands.length; i++) {
			aux.add(new Integer(renderBands[i]));
		}
		int alphaBand = render.getRenderTransparency().getAlphaBandNumber();
		getPanelGroup().getProperties().put("alphaBand", new Integer(alphaBand));
		getPanelGroup().getProperties().put("renderBands", aux);
	}


	/**
	 * Deja la capa en el último estado guardado y la refresca
	 */
	public void restoreStatus() {
		ArrayList aux = (ArrayList) getPanelGroup().getProperties().get("renderBands");
		Integer alphaBand = (Integer) getPanelGroup().getProperties().get("alphaBand");
		
		int[] renderBands = new int[aux.size()];
		for (int i = 0; i < aux.size(); i++)
			renderBands[i] = ((Integer) aux.get(i)).intValue();

		render.setRenderBands(renderBands);
		if(alphaBand != null) {
			// Ultima transparencia aplicada en el renderizador
			GridTransparency gt = render.getRenderTransparency();
			if(gt != null) 
				gt.setTransparencyBand(alphaBand.intValue());

			// Transparencia del dataset
//			Transparency t = ((FLyrRasterSE) fLayer).getDataSource().getTransparencyFilesStatus();
//			if (t != null)
//				t.setTransparencyBand(alphaBand.intValue());
		}

		if (fLayer != null)
			fLayer.getMapContext().invalidate();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#cancel()
	 */
	public void cancel() {
		if (!getPanelGroup().isPanelInGUI(this))
			return;

		restoreStatus();
	}

	/**
	 * Activa y desactiva el control
	 * @param enabled true para activar y false para desactivar
	 */
	public void setEnabled(boolean enabled) {
		if (panelListener != null)
			panelListener.setEnabledPanelAction(enabled);
		getARGBTable().setEnabled(enabled);
		getFileList().setEnabled(enabled);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.AbstractPanel#setReference(java.lang.Object)
	 */
	public void setReference(Object ref) {
		super.setReference(ref);

		if (!(ref instanceof FLayer))
			return;

		fLayer = (FLayer) ref;

		actionEnabled();

		clear();
		getFileList().clear();

		if (fLayer instanceof IRasterProperties)
			prop = (IRasterProperties) fLayer;

		//Si tiene tabla de color inicializamos solamente
		if (fLayer instanceof IRasterRendering) {
			render = (IRasterRendering)fLayer;
			if (((IRasterRendering) fLayer).existColorTable()) {
				panelListener.init(null, null, fLayer);
				return;
			}
		}

		//Si no tiene tabla de color se añaden los ficheros e inicializamos
		if (fLayer instanceof IRasterDataset) {
			dataset = (IRasterDataset) fLayer;
			try {
				addFiles(dataset.getDataSource());
			} catch (NotInitializeException e) {
				RasterToolsUtil.messageBoxError("table_not_initialize", this);
			}
		}

		panelListener.init(dataset, prop, fLayer);
	}

	public void componentHidden(ComponentEvent e) {}
	public void componentShown(ComponentEvent e) {}
	public void componentMoved(ComponentEvent e) {}
	public void selected() {}
}