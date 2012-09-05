/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Gobernment (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 */

/*
 * AUTHORS (In addition to CIT):
 * 2008 {DiSiD Technologies}  {TableDocument implementation based on the gvSIG DAL API}
 */
package org.gvsig.project.document.table.gui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.data.feature.swing.FeatureTypesTablePanel;
import org.gvsig.fmap.data.feature.swing.table.notification.ColumnHeaderSelectionChangeNotification;
import org.gvsig.project.document.table.FeatureTableDocument;
import org.gvsig.tools.observer.Observable;
import org.gvsig.tools.observer.Observer;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindowTransform;
import com.iver.andami.ui.mdiManager.SingletonWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * Feature table visualization panel.
 *
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class FeatureTableDocumentPanel extends FeatureTypesTablePanel implements
        SingletonWindow, IWindowTransform, Observer, ListSelectionListener,
		TableModelListener {
    private boolean showTypes=false;
    private static final long serialVersionUID = -1003263265311764630L;

    private static final int DEFAULT_HEIGHT = 200;

    private static final int DEFAULT_WIDTH = 300;

    private boolean isPalette=false;

    private WindowInfo windowInfo = null;

    private FeatureTableDocument model = null;

	private JPanel jPanel;  //  @jve:decl-index=0:

	private JLabel jLabel;

	private JButton bShowFeatureTypes = null;

	public FeatureTableDocumentPanel(FeatureTableDocument document)
			throws DataException {
		super(document.getStore());
		this.model = document;
		initialize();
	}

    // IWindow interface

	public WindowInfo getWindowInfo() {
		if (windowInfo == null) {
			windowInfo = new WindowInfo(WindowInfo.ICONIFIABLE
					| WindowInfo.MAXIMIZABLE | WindowInfo.RESIZABLE);

			// TODO: RETURN MORE USEFUL INFO ABOUT DATA VIEWED AND ADD I18N FOR
			// TEXTS
			if (this.model == null) {
				windowInfo.setTitle("Tabla");
			} else {
				windowInfo.setTitle(this.model.getName());
			}

			windowInfo.setWidth(DEFAULT_WIDTH);
			windowInfo.setHeight(DEFAULT_HEIGHT);
		}
		return windowInfo;
	}

    // SingletonWindow interface

    public Object getWindowModel() {
        return this.model.getName();
    }

    public void toPalette() {
		isPalette=true;
		windowInfo.toPalette(true);
		windowInfo.setClosed(false);
		PluginServices.getMDIManager().changeWindowInfo(this,getWindowInfo());
	}

	public void restore() {
		isPalette=false;
		windowInfo.toPalette(false);
		windowInfo.setClosed(false);
		PluginServices.getMDIManager().changeWindowInfo(this,getWindowInfo());
	}

	public boolean isPalette() {
		return isPalette;
	}

	public FeatureTableDocument getModel() {
		return model;
	}

	public void update(Observable arg0, Object arg1) {

		if (arg1 instanceof ColumnHeaderSelectionChangeNotification) {
			PluginServices.getMainFrame().enableControls();

		}

	}

	public void updateSelection() {
		try {
			//			ListSelectionModel sm = getTablePanel().getTable()
			//					.getSelectionModel();
			//			sm.clearSelection();
			//TODO falta que implentar como transformar la selección del store en la selección de la tabla.
			FeatureSelection selection = (FeatureSelection) getModel()
					.getStore().getSelection();
			jLabel.setText(selection.getSize()
					+ " / "
					+ getTableModel().getHelper().getTotalSize()
					+ " "
					+ PluginServices.getText(this,
							"registros_seleccionados_total") + ".");
		} catch (DataException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {

			jLabel = new JLabel();
			jLabel.setText(PluginServices.getText(this,"seleccionados")+ ":   ");
			try {
				FeatureStore store=getModel().getStore();
				FeatureSelection selection = (FeatureSelection)store.getSelection();
				jLabel.setText(selection.getSize() + " / " + getTableModel().getHelper().getTotalSize() + " " +
	                PluginServices.getText(this, "registros_seleccionados_total") +
	                ".");
				jPanel = new JPanel();
				BorderLayout borderLayout=new BorderLayout();
//				jPanel.setLayout(borderLayout);
				jPanel.add(jLabel, BorderLayout.EAST);
				jPanel.add(getBShowFeatureTypes(), BorderLayout.WEST);
			} catch (DataException e) {
				e.printStackTrace();
			}
		}
		return jPanel;
	}
	private void initialize() throws DataException {
		add(getJPanel(),BorderLayout.SOUTH);
		getTablePanel().getTable().addObserver(this);
		getTablePanel().getTable().getSelectionModel()
				.addListSelectionListener(this);
		getTableModel().addTableModelListener(this);
	}

	/**
	 * This method initializes bShowFeatureTypes
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBShowFeatureTypes() {
		if (bShowFeatureTypes == null) {
			bShowFeatureTypes = new JButton();
			bShowFeatureTypes.setText(PluginServices.getText(this,"show_types"));
			bShowFeatureTypes.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					showTypes=!showTypes;
					if (showTypes){
						bShowFeatureTypes.setText(PluginServices.getText(this,"hide_types"));
						showTypes();
					}else{
						bShowFeatureTypes.setText(PluginServices.getText(this,"show_types"));
						hideTypes();
					}
				}
			});
		}
		return bShowFeatureTypes;
	}

	public Object getWindowProfile() {
		return WindowInfo.EDITOR_PROFILE;
	}

	public void valueChanged(ListSelectionEvent e) {
		this.updateSelection();
		this.updateUI();

	}

	public void tableChanged(TableModelEvent e) {
		this.updateSelection();
		this.updateUI();
	}
}