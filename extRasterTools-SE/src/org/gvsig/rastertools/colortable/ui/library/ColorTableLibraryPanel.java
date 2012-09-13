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
package org.gvsig.rastertools.colortable.ui.library;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.gvsig.gui.beans.listview.ListViewComponent;
import org.gvsig.gui.beans.listview.ListViewItem;
import org.gvsig.gui.beans.listview.ListViewListener;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.datastruct.io.RasterLegendIO;
import org.gvsig.raster.datastruct.io.exceptions.RasterLegendIONotFound;
import org.gvsig.raster.datastruct.persistence.ColorTableLibraryPersistence;
import org.gvsig.raster.util.BasePanel;
import org.gvsig.raster.util.ExtendedFileFilter;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.raster.util.RasterUtilities;

import com.iver.andami.PluginServices;
/**
 * Panel que aparece en la parte inferior derecha que contiene la lista
 * de librerias disponibles para las tablas de color, con sus botones correspondientes
 * para importar, exportar, borrar y añadir librerias.
 * 
 * 19/02/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ColorTableLibraryPanel extends BasePanel implements ActionListener, ListViewListener {
	private static final long serialVersionUID       = 1L;
	private JPanel            panelButtons           = null;
	private JButton           buttonAdd              = null;
	private JButton           buttonDel              = null;
	private JButton           buttonImport           = null;
	private JButton           buttonExport           = null;
	private ListViewComponent listViewComponent      = null;

	private boolean           hasChanged             = false;

	private ArrayList         actionCommandListeners = new ArrayList();

	private String palettesPath = System.getProperty("user.home") +
	File.separator +
	"gvSIG" + // PluginServices.getArguments()[0] +
	File.separator + "colortable";
		
	/**
	 *Inicializa componentes gráficos y traduce
	 */
	public ColorTableLibraryPanel() {
		init();
		translate();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#init()
	 */
	protected void init() {
		setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
		
		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.setViewportView(getListViewComponent());
		jScrollPane.setAutoscrolls(true);
		
		panel.add(jScrollPane, BorderLayout.CENTER);
		add(panel, BorderLayout.CENTER);
		add(getPanelButtons(), BorderLayout.SOUTH);
		setPreferredSize(new Dimension(0, 192));

		loadDiskLibrary();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#translate()
	 */
	protected void translate() {
	}
	
	private void loadDiskLibrary() {
		ArrayList fileList = ColorTableLibraryPersistence.getPaletteFileList(palettesPath);
		for (int i = 0; i < fileList.size(); i++) {
			ArrayList paletteItems = new ArrayList();
			String paletteName = ColorTableLibraryPersistence.loadPalette(palettesPath, (String) fileList.get(i), paletteItems);

			if (paletteItems.size() <= 0)
				continue;

			ColorTable colorTable = new ColorTable();
			colorTable.setName(paletteName);
			colorTable.createPaletteFromColorItems(paletteItems, true);
			colorTable.setInterpolated(true);

			ListViewItem item = new ListViewItem(new ColorTableIconPainter(colorTable), paletteName);
			item.setTag(fileList.get(i));
			getListViewComponent().addItem(item);
		}
		getListViewComponent().setSelectedIndex(0);
		callColorTableChanged();		
	}
	
	/**
	 * Devuelve el jButtonAdd
	 * @return
	 */
	private JButton getButtonAdd() {
		if (buttonAdd == null) {
			ImageIcon icon = null;
			try {
				icon = PluginServices.getIconTheme().get("addlayer-icon");
			} catch (NullPointerException n) {
				// No ha encontrado el icono, no lo muestra.
			}

			buttonAdd = new JButton(icon);
			buttonAdd.setPreferredSize(new Dimension(22, 19));
			buttonAdd.addActionListener(this);
			buttonAdd.setToolTipText(getText(this, "nueva_libreria_title"));
		}
		return buttonAdd;
	}

	/**
	 * Devuelve el jButtonDel
	 * @return
	 */
	private JButton getButtonDel() {
		if (buttonDel == null) {
			ImageIcon icon = null;
			try {
				icon = PluginServices.getIconTheme().get("delall-icon");
			} catch (NullPointerException n) {
				// No ha encontrado el icono, no lo muestra.
			}
			buttonDel = new JButton(icon);
			buttonDel.setPreferredSize(new Dimension(22, 19));
			buttonDel.addActionListener(this);
			buttonDel.setToolTipText(getText(this, "borrar_libreria"));
		}
		return buttonDel;
	}

	/**
	 * Devuelve el jButtonDel
	 * @return
	 */
	private JButton getButtonImport() {
		if (buttonImport == null) {
			ImageIcon icon = null;
			try {
				icon = PluginServices.getIconTheme().get("import-icon");
			} catch (NullPointerException n) {
				// No ha encontrado el icono, no lo muestra.
			}
			buttonImport = new JButton(icon);
			buttonImport.setPreferredSize(new Dimension(22, 19));
			buttonImport.addActionListener(this);
			buttonImport.setToolTipText(getText(this, "import_libreria"));
		}
		return buttonImport;
	}

	/**
	 * Devuelve el jButtonDel
	 * @return
	 */
	private JButton getButtonExport() {
		if (buttonExport == null) {
			ImageIcon icon = null;
			try {
				icon = PluginServices.getIconTheme().get("export-icon");
			} catch (NullPointerException n) {
				// No ha encontrado el icono, no lo muestra.
			}
			buttonExport = new JButton(icon);
			buttonExport.setPreferredSize(new Dimension(22, 19));
			buttonExport.addActionListener(this);
			buttonExport.setToolTipText(getText(this, "export_libreria"));
		}
		return buttonExport;
	}
	
	/**
	 * Devuelve el panel de botones de la libreria de añadir y borrar
	 * @return
	 */
	private JPanel getPanelButtons() {
		if (panelButtons == null) {
			panelButtons = new JPanel();
			panelButtons.setPreferredSize(new Dimension(0, 21));

			FlowLayout flowLayout5 = new FlowLayout();
			flowLayout5.setHgap(5);
			flowLayout5.setVgap(0);
			flowLayout5.setAlignment(java.awt.FlowLayout.CENTER);
			panelButtons.setLayout(flowLayout5);

			panelButtons.add(getButtonAdd(), null);
			panelButtons.add(getButtonExport(), null);
			panelButtons.add(getButtonImport(), null);
			panelButtons.add(getButtonDel(), null);
		}
		return panelButtons;
	}

	private ListViewComponent getListViewComponent() {
		if (listViewComponent == null) {
			listViewComponent = new ListViewComponent();
			listViewComponent.addListSelectionListener(this);
			listViewComponent.setEditable(true);
		}
		return listViewComponent;
	}

	private void callColorTableChanged() {
		Iterator acIterator = actionCommandListeners.iterator();
		while (acIterator.hasNext()) {
			ColorTableLibraryListener listener = (ColorTableLibraryListener) acIterator.next();
			listener.actionColorTableChanged(new ColorTableLibraryEvent(this));
		}
	}
	
	/**
	 * Accion que se ejecuta cuando se disparan los siguientes eventos:
	 *  - Checkbox de interpolacion.
	 *  - Checkbox de habilitacion de colortables.
	 *  - Boton de añadir un colortable nuevo.
	 *  - Boton de borrar un colortable seleccionado.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getButtonAdd()) {
			AddLibraryWindow addLibrary = new AddLibraryWindow();
			if (addLibrary.showConfirm(this) == JOptionPane.OK_OPTION) {
				ColorTable colorTable = addLibrary.getColorTable();
				if (colorTable != null) {
					ListViewItem item = new ListViewItem(new ColorTableIconPainter(colorTable), colorTable.getName());
					item.setTag(item.getName() + ".xml");
					getListViewComponent().addItem(item);
					getListViewComponent().setSelectedIndex(getListViewComponent().getItems().size() - 1);
					((ColorTableIconPainter) item.getIcon()).getColorTable().setName(item.getName());
					colorTable.setName(((ColorTableIconPainter) item.getIcon()).getColorTable().getName());
					ColorTableLibraryPersistence.save_to_1_1(palettesPath, colorTable);
					callColorTableChanged();
				}
			}
		}
		
		if (e.getSource() == getButtonExport()) {
			JFileChooser chooser = new JFileChooser();
			chooser.setAcceptAllFileFilterUsed(false);

			String[] formats = RasterLegendIO.getFormats();
			ExtendedFileFilter fileFilter = null;
			ExtendedFileFilter firstFileFilter = null;
			for (int i = 0; i < formats.length; i++) {
				fileFilter = new ExtendedFileFilter();
				fileFilter.addExtension(formats[i]);

				try {
					String desc = RasterLegendIO.getRasterLegendIO(RasterUtilities.getExtensionFromFileName(formats[i])).getDescription();
					if (desc != null)
						fileFilter.setDescription(desc);
				} catch (RasterLegendIONotFound e1) {
					// Si no puedo pillar la descripcion no pasa nada
				}
				if (firstFileFilter == null)
					firstFileFilter = fileFilter;

				chooser.addChoosableFileFilter(fileFilter);
			}
			if (firstFileFilter != null)
				chooser.setFileFilter(firstFileFilter);

			int returnVal = chooser.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					ExtendedFileFilter filter = (ExtendedFileFilter) chooser.getFileFilter();
					String file = filter.getNormalizedFilename(chooser.getSelectedFile());

					RasterLegendIO.getRasterLegendIO(RasterUtilities.getExtensionFromFileName(file)).write(getColorTableSelected(), new File(file));
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (RasterLegendIONotFound e1) {
					e1.printStackTrace();
				}
			}
		}

		if (e.getSource() == getButtonImport()) {
			JFileChooser chooser = new JFileChooser();
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setMultiSelectionEnabled(true);

			String[] formats = RasterLegendIO.getFormats();
			ExtendedFileFilter allFilters = new ExtendedFileFilter();
			ExtendedFileFilter fileFilter = null;
			for (int i = 0; i < formats.length; i++) {
				fileFilter = new ExtendedFileFilter();
				fileFilter.addExtension(formats[i]);
				allFilters.addExtension(formats[i]);

				try {
					String desc = RasterLegendIO.getRasterLegendIO(RasterUtilities.getExtensionFromFileName(formats[i])).getDescription();
					if (desc != null)
						fileFilter.setDescription(desc);
				} catch (RasterLegendIONotFound e1) {
					// Si no puedo pillar la descripcion no pasa nada
				}

				chooser.addChoosableFileFilter(fileFilter);
			}
			allFilters.setDescription(getText(this, "todos_soportados"));
			chooser.addChoosableFileFilter(allFilters);
			chooser.setFileFilter(allFilters);

			int returnVal = chooser.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					for (int file = 0; file<chooser.getSelectedFiles().length; file++) {
						fileFilter = null;
						for (int i = 0; i < formats.length; i++) {
							fileFilter = new ExtendedFileFilter();
							fileFilter.addExtension(formats[i]);
							if (fileFilter.accept(chooser.getSelectedFiles()[file])) {
								ColorTable colorTable = RasterLegendIO.getRasterLegendIO(formats[i]).read(chooser.getSelectedFiles()[file]);

								colorTable.setInterpolated(true);

								ListViewItem item = new ListViewItem(new ColorTableIconPainter(colorTable), colorTable.getName());
								item.setTag(item.getName() + ".xml");
								getListViewComponent().addItem(item);
								getListViewComponent().setSelectedIndex(getListViewComponent().getItems().size() - 1);
								((ColorTableIconPainter) item.getIcon()).getColorTable().setName(item.getName());
								colorTable.setName(((ColorTableIconPainter) item.getIcon()).getColorTable().getName());
								ColorTableLibraryPersistence.save_to_1_1(palettesPath, colorTable);
								callColorTableChanged();
								break;
							}
						}
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (RasterLegendIONotFound e1) {
					e1.printStackTrace();
				}
			}
		}

		if (e.getSource() == getButtonDel()) {
			if (getListViewComponent().getSelectedIndices().length > 0) {
				if (RasterToolsUtil.messageBoxYesOrNot("desea_borrar_librerias", this)) {
					File oldFile = new File(palettesPath + File.separator + getListViewComponent().getSelectedValue().getTag());
					oldFile.delete();
					int pos = getListViewComponent().getSelectedIndices()[0];
					getListViewComponent().removeSelecteds();
					
					if (getListViewComponent().getItems().size() == 0) {
						loadDiskLibrary();
					} else {
						getListViewComponent().setSelectedIndex(pos - 1);
						callColorTableChanged();
					}
				}
			}
			return;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.listview.ListViewListener#actionItemNameChanged(java.util.EventObject, java.lang.String, org.gvsig.gui.beans.listview.ListViewItem)
	 */
	public void actionItemNameChanged(EventObject e, String oldName, ListViewItem item) {
		File oldFile = new File(palettesPath + File.separator + oldName + ".xml");
		oldFile.delete();

		getColorTableSelected().setName(item.getName());
		item.setTag(item.getName() + ".xml");

		ColorTableLibraryPersistence.save_to_1_1(palettesPath, getColorTableSelected());
		
		callColorTableChanged();
	}

	/**
	 * Comprueba si el usuario ha cambiado los valores de la tabla de colores,
	 * si es así, le pregunta al usuario si quiere guardar los datos, y según la
	 * selección, restaura los datos antiguos o los machaca.
	 */
	private void testLibraryChanged() {
		if (hasChanged) {
			if (RasterToolsUtil.messageBoxYesOrNot("desea_guardar_cambios", this)) {
				ColorTableLibraryPersistence.save_to_1_1(palettesPath, getColorTableSelected());
				getListViewComponent().repaint();
			}
			hasChanged = false;
		}
	}
	
	/**
	 * Selecciona la tabla de color por defecto.
	 */
	public void selectDefault() {
		int selected = 0;
		for (int i = 0; i < getListViewComponent().getItems().size(); i++) {
			if (((ListViewItem) getListViewComponent().getItems().get(i)).getName().equals("Default")) {
				selected = i;
				break;
			}
		}
		getListViewComponent().setSelectedIndex(selected);
		callColorTableChanged();
	}
	
	/**
	 * Inserta una tabla de color en la posicion especificada y selecciona.
	 */
	public void addColorTable(int pos, ColorTable colorTable) {
		ListViewItem item = new ListViewItem(new ColorTableIconPainter(colorTable), getText(this, "tabla_actual"));
		getListViewComponent().addItem(pos, item);
		getListViewComponent().setSelectedIndex(pos);
		callColorTableChanged();
	}
	
	/**
	 * Accion que se ejecuta cuando cambia la seleccion de un item del
	 * ListViewComponent
	 */
	public void actionValueChanged(EventObject e) {
		testLibraryChanged();
		callColorTableChanged();
	}
	
	/**
	 * Define si se visualizara el componente con interpolacion o no.
	 * @param enabled
	 */
	public void setInterpolated(boolean enabled) {
		for (int i = 0; i < getListViewComponent().getItems().size(); i++)
			((ColorTableIconPainter) ((ListViewItem) getListViewComponent().getItems().get(i)).getIcon()).setInterpolated(enabled);
		getListViewComponent().repaint();
	}
	
	/**
	 * Añadir un listener a la lista de eventos
	 * @param listener
	 */
	public void addColorTableLibraryListener(ColorTableLibraryListener listener) {
		if (!actionCommandListeners.contains(listener))
			actionCommandListeners.add(listener);
	}

	/**
	 * Borrar un listener de la lista de eventos
	 * @param listener
	 */
	public void removeColorTableLibraryListener(ColorTableLibraryListener listener) {
		actionCommandListeners.remove(listener);
	}

	/**
	 * Devuelve la tabla de color que hay seleccionada en ese momento
	 * @return
	 */
	public ColorTable getColorTableSelected() {
		return ((ColorTableIconPainter) getListViewComponent().getSelectedValue().getIcon()).getColorTable();
	}
	
	/**
	 * Reescribe la tabla de color en la posicion seleccionada
	 * @param colorTable
	 */
	public void setColorTableSelected(ColorTable colorTable) {
		hasChanged = true;
		((ColorTableIconPainter) getListViewComponent().getSelectedValue().getIcon()).getColorTable().createPaletteFromColorItems(colorTable.getColorItems(), false);
		getListViewComponent().repaint();
	}

}