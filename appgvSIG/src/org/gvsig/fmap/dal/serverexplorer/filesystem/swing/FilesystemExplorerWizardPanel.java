/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
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
 * 2008 IVER T.I. S.A.   {{Task}}
 */

/**
 *
 */
package org.gvsig.fmap.dal.serverexplorer.filesystem.swing;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import org.gvsig.AppGvSigLocator;
import org.gvsig.AppGvSigManager;
import org.gvsig.PrepareContext;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.FileNotFoundException;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemFileFilter;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorerParameters;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemStoreParameters;
import org.gvsig.gui.beans.swing.JButton;
import org.gvsig.gui.beans.swing.JFileChooser;
import org.gvsig.tools.dynobject.DynObject;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.gui.WizardPanel;

/**
 * @author jmvivo
 *
 */
public abstract class FilesystemExplorerWizardPanel extends WizardPanel
		implements
		ActionListener, ListSelectionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = -3371957786521876903L;

	private static final String OPEN_LAYER_FILE_CHOOSER_ID = "OPEN_LAYER_FILE_CHOOSER_ID";

	protected static final String ADD_COMMAND = "ADD";
	protected static final String EDIT_COMMAND = "EDIT";
	protected static final String REMOVE_COMMAND = "REMOVE";
	protected static final String UP_COMMAND = "UP";
	protected static final String DOWN_COMMAND = "DOWN";

	private static String lastPath = null;
	private static MyFileFilter lastFilter = null;

	private JList fileList;
	private JScrollPane fileListScroll;
	private JPanel buttonsPanel;
	private JButton addButton;
	private JButton editButton;
	private JButton removeButton;
	private JButton upButton;
	private JButton downButton;
	private JScrollPane paramsListScroll;
	private JPanel paramsList;
	private GridBagConstraints paramsListLabelConstraint;
	private GridBagConstraints paramsListValueConstraint;

	protected FilesystemServerExplorer explorer;
	private ArrayList<MyFileFilter> filters;

	private GridBagConstraints paramsListFillConstraint;

	public void setTabName(String name) {
		super.setTabName(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.gui.WizardPanel#getParameters()
	 */
	@Override
	public DataStoreParameters[] getParameters() {
		return ((FilesystemStoreListModel) getFileList().getModel())
				.getParameters();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.gui.WizardPanel#initWizard()
	 */
	@Override
	public void initWizard() {
		setTabName(PluginServices.getText(this, "Fichero"));
		if (lastPath == null) {
			Preferences prefs = Preferences.userRoot().node("gvsig.foldering");
			lastPath = prefs.get("DataFolder", null);
		}

		DataManager dm = DALLocator.getDataManager();
		FilesystemServerExplorerParameters param;
		try {
			param = (FilesystemServerExplorerParameters) dm
					.createServerExplorerParameters(FilesystemServerExplorer.NAME);
			param.setInitialpath(lastPath);
			explorer = (FilesystemServerExplorer) dm
					.createServerExplorer(param);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		this.filters = new ArrayList<MyFileFilter>();
		Iterator<FilesystemFileFilter> iter = explorer.getFilters();
		while (iter.hasNext()) {
			this.filters.add(new MyFileFilter(iter.next()));
		}
		this.filters.add(new MyFileFilter(explorer.getGenericFilter()));
		initUI();
	}

	private void initUI() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints constr = new GridBagConstraints();

		constr.gridwidth = GridBagConstraints.RELATIVE;
		constr.gridheight = GridBagConstraints.RELATIVE;
		constr.fill = GridBagConstraints.BOTH;
		constr.anchor = GridBagConstraints.FIRST_LINE_START;
		constr.weightx = 1;
		constr.weighty = 1;
		constr.ipadx = 3;
		constr.ipady = 3;

		this.add(getFileListScroll(), constr);

		constr.gridwidth = GridBagConstraints.REMAINDER;
		constr.gridheight = GridBagConstraints.RELATIVE;
		constr.fill = GridBagConstraints.NONE;
		constr.anchor = GridBagConstraints.FIRST_LINE_END;
		constr.weightx = 0;
		constr.weighty = 0;
		this.add(getButtonsPanel(), constr);

		constr.gridwidth = GridBagConstraints.REMAINDER;
		constr.gridheight = GridBagConstraints.REMAINDER;
		constr.fill = GridBagConstraints.BOTH;
		constr.anchor = GridBagConstraints.FIRST_LINE_START;
		constr.weightx = 1;
		constr.weighty = 1;
		this.add(getParamsListScroll(), constr);

		this.updateButtons();

	}

	protected class FilesystemStoreListModel extends AbstractListModel {
		/**
		 *
		 */
		private static final long serialVersionUID = -726119349962990665L;
		private ArrayList<FilesystemStoreParameters> theList;

		public FilesystemStoreListModel() {
			theList = new ArrayList<FilesystemStoreParameters>();
		}

		public DataStoreParameters[] getParameters() {
			return theList.toArray(new DataStoreParameters[0]);
		}

		public Object getElementAt(int index) {
			return theList.get(index).getFile().getName();
		}

		public FilesystemStoreParameters getStoreParameterAt(int index) {
			return theList.get(index);
		}

		public int getSize() {
			return theList.size();
		}

		public DynObject getDynObjectAt(int index) {
			return (DynObject) theList.get(index);
		}

		public void add(DynObject dynObject) {
			this.theList.add((FilesystemStoreParameters) dynObject);
			this.fireIntervalAdded(this, this.theList.size() - 1, this.theList
					.size() - 1);
		}

		public void addAll(List<FilesystemStoreParameters> toAdd) {
			int index0 = this.getSize() - 1;
			if (index0 < 0) {
				index0 = 0;
			}
			this.theList.addAll(toAdd);
			this.fireIntervalAdded(this, index0, this.theList.size() - 1);
		}

		public void remove(int i) {
			this.theList.remove(i);
			this.fireIntervalRemoved(this, i, i);

		}

		public void up(FilesystemStoreParameters item) {
			int curIndex = this.theList.indexOf(item);
			if (curIndex < 1){
				return;
			}
			this.theList.remove(item);
			this.theList.add(curIndex - 1, item);
			this.fireContentsChanged(this, curIndex, curIndex - 1);
		}

		public void down(FilesystemStoreParameters item) {
			int curIndex = this.theList.indexOf(item);
			if (curIndex < 0) {
				return;
			} else if (curIndex == this.theList.size() - 1) {
				return;
			}
			this.theList.remove(item);
			this.theList.add(curIndex + 1, item);
			this.fireContentsChanged(this, curIndex, curIndex + 1);
		}

	}

	protected JList getFileList() {
		if (fileList == null) {
			fileList = new JList(new FilesystemStoreListModel());

			fileList.addListSelectionListener(this);
		}
		return fileList;
	}

	private JScrollPane getFileListScroll() {
		if (fileListScroll == null) {
			fileListScroll = new JScrollPane();
			fileListScroll.setViewportView(getFileList());
			fileListScroll
					.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			fileListScroll
					.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		}
		return fileListScroll;
	}

	private Component getButtonsPanel() {
		if (buttonsPanel == null) {
			buttonsPanel = new JPanel();

			buttonsPanel.setLayout(new GridBagLayout());

			GridBagConstraints constr = new GridBagConstraints();

			constr.anchor = GridBagConstraints.CENTER;
			constr.fill = GridBagConstraints.NONE;
			constr.ipadx = 3;
			constr.ipady = 3;
			constr.insets = new Insets(3, 3, 3, 3);
			constr.gridwidth = GridBagConstraints.REMAINDER;

			buttonsPanel.add(getAddButton(), constr);
			buttonsPanel.add(getEditButton(), constr);
			buttonsPanel.add(getRemoveButton(), constr);
			buttonsPanel.add(getUpButton(), constr);
			buttonsPanel.add(getDownButton(), constr);
			//			buttonsPanel.add(new JLabel(), constrLbl);
		}
		return buttonsPanel;
	}

	private JButton getAddButton() {
		if (addButton == null) {
			addButton = new JButton();
			addButton.setText(getLocalizedText("add"));
			addButton.setActionCommand(ADD_COMMAND);
			addButton.addActionListener(this);
		}
		return addButton;
	}

	private JButton getEditButton() {
		if (editButton == null) {
			editButton = new JButton();
			editButton.setText(getLocalizedText("edit"));
			editButton.setActionCommand(EDIT_COMMAND);
			editButton.addActionListener(this);
		}
		return editButton;
	}

	private JButton getRemoveButton() {
		if (removeButton == null) {
			removeButton = new JButton();
			removeButton.setText(getLocalizedText("remove"));
			removeButton.setActionCommand(REMOVE_COMMAND);
			removeButton.addActionListener(this);
		}
		return removeButton;
	}

	private JButton getUpButton() {
		if (upButton == null) {
			upButton = new JButton();
			upButton.setText(getLocalizedText("up"));
			upButton.setActionCommand(UP_COMMAND);
			upButton.addActionListener(this);
		}
		return upButton;
	}

	private JButton getDownButton() {
		if (downButton == null) {
			downButton = new JButton();
			downButton.setText(getLocalizedText("down"));
			downButton.setActionCommand(DOWN_COMMAND);
			downButton.addActionListener(this);
		}
		return downButton;
	}

	private String getLocalizedText(String txt) {
		try {
			return PluginServices.getText(this, txt);
		} catch (Exception e) {
			return txt;
		}
	}

	private Component getParamsListScroll() {
		if (paramsListScroll == null) {
			paramsListScroll = new JScrollPane();
			paramsListScroll.setBorder(null);
			paramsListScroll.setViewportView(getParamsList());
			paramsListScroll
					.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			paramsListScroll
					.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		}
		return paramsListScroll;
	}

	private JPanel getParamsList() {
		if (paramsList == null) {
			paramsList = new DynObjectViewer();
		}
		return paramsList;

	}

	protected void loadParamsList(DynObject dynObj) {
		DynObjectViewer panel = (DynObjectViewer) getParamsList();

		panel.load(dynObj);
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		FilesystemStoreListModel model = (FilesystemStoreListModel) getFileList()
				.getModel();


		if (command == ADD_COMMAND) {
			List<FilesystemStoreParameters> toAdd = this.addFiles();
			if (toAdd.isEmpty()){
				return;
			}
			model.addAll(toAdd);

			getFileList().setModel(model);


		} else if (command == EDIT_COMMAND) {
			DynObject dynObject = model.getDynObjectAt(getFileList()
					.getSelectedIndex());

			DynObjectEditor editor = new DynObjectEditor(dynObject);
			editor.editObject(true);
			this.loadParamsList(dynObject);

		} else if (command == REMOVE_COMMAND) {
			int[] selecteds = getFileList().getSelectedIndices();
			for (int i = selecteds.length - 1; i > -1; i--) {
				model.remove(selecteds[i]);
			}
			getFileList().setSelectedIndex(-1);
			this.loadParamsList(null);


		} else if (command == UP_COMMAND) {
			List<FilesystemStoreParameters> items = new ArrayList<FilesystemStoreParameters>();
			int[] selecteds = getFileList().getSelectedIndices();
			if (selecteds.length == 0 || selecteds[0] == 0) {
				return;
			}
			for (int i = 0; i < selecteds.length; i++) {
				items.add(model.getStoreParameterAt(selecteds[i]));
				selecteds[i]--;
			}
			Iterator<FilesystemStoreParameters> iter = items.iterator();
			while (iter.hasNext()) {
				model.up(iter.next());
			}
			getFileList().setSelectedIndices(selecteds);

		} else if (command == DOWN_COMMAND) {
			List<FilesystemStoreParameters> items = new ArrayList<FilesystemStoreParameters>();
			int[] selecteds = getFileList().getSelectedIndices();
			if (selecteds.length == 0
					|| selecteds[selecteds.length - 1] == model.getSize() - 1) {
				return;
			}
			for (int i = selecteds.length - 1; i > -1; i--) {
				items.add(model.getStoreParameterAt(selecteds[i]));
				selecteds[i]++;
			}
			Iterator<FilesystemStoreParameters> iter = items.iterator();
			while (iter.hasNext()) {
				model.down(iter.next());
			}
			getFileList().setSelectedIndices(selecteds);

		} else {
			throw new IllegalArgumentException(command);
		}

	}

	public List<DynObject> getSelecteds() {
		ArrayList<DynObject> list = new ArrayList<DynObject>();
		JList fList = this.getFileList();
		int[] selecteds = fList.getSelectedIndices();
		FilesystemStoreListModel model = (FilesystemStoreListModel) fList
				.getModel();

		for (int index : selecteds) {
			list.add((DynObject) model.getStoreParameterAt(index));
		}
		return list;
	}

	public void valueChanged(ListSelectionEvent e) {
		int index = getFileList().getSelectedIndex();
		if (index < 0) {
			loadParamsList(null);
		} else {
			FilesystemStoreListModel model = ((FilesystemStoreListModel) getFileList()
					.getModel());
			this.loadParamsList((DynObject) model.getStoreParameterAt(index));
		}
		this.updateButtons();
	}

	public class MyFileFilter extends FileFilter {
		public FilesystemFileFilter filter = null;

		public MyFileFilter(FilesystemFileFilter params) {
			this.filter = params;
		}

		/**
		 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
		 */
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			}
			return filter.accept(f);

		}

		/**
		 * @see javax.swing.filechooser.FileFilter#getDescription()
		 */
		public String getDescription() {
			return filter.getDescription();
		}
	}

	private List<FilesystemStoreParameters> addFiles() {
		this.callStateChanged(true);
		JFileChooser fileChooser = new JFileChooser(OPEN_LAYER_FILE_CHOOSER_ID,
				explorer.getCurrentPath());
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setAcceptAllFileFilterUsed(false);

		Iterator<MyFileFilter> iter = this.filters.iterator();
		while (iter.hasNext()) {
			fileChooser.addChoosableFileFilter(iter.next());
		}

		if (lastFilter != null && filters.contains(lastFilter)) {
			fileChooser.setFileFilter(lastFilter);
		}

		int result = fileChooser.showOpenDialog(this);

		List<FilesystemStoreParameters> toAdd = new ArrayList<FilesystemStoreParameters>();

		if (result == JFileChooser.APPROVE_OPTION) {
			lastPath = fileChooser.getCurrentDirectory().getAbsolutePath();
			try {
				explorer.setCurrentPath(fileChooser.getCurrentDirectory());
			} catch (FileNotFoundException e) {
				NotificationManager.addError(e);
			}
			lastFilter = (MyFileFilter) fileChooser.getFileFilter();
			DataStoreParameters param, toAddParam;
			AppGvSigManager appGvSIGMan = AppGvSigLocator
					.getAppGvSigManager();
			PrepareContext context = this.getPrepareDataStoreContext();
			for (File aFile : fileChooser.getSelectedFiles()) {
				try {
					param = explorer
							.getParametersFor(aFile);
					if (param == null) {
						// TODO show warning
						continue;
					}

					try {
						toAddParam = appGvSIGMan
								.prepareOpenDataStoreParameters(param, context);
					} catch (Exception e) {
						NotificationManager.addError(e);
						continue;
					}

					toAdd.add((FilesystemStoreParameters) toAddParam);
				} catch (DataException e) {
					NotificationManager.addError(e);
					return null;
				}
			}

			//			IFileOpen lastFileOpen = null;
			//			for (int i = 0; i < listFileOpen.size(); i++) {
			//				IFileOpen fileOpen = listFileOpen.get(i);
			//				List<FileFilter> aux = fileOpen.getFileFilter();
			//				for (int j = 0; j < aux.size(); j++) {
			//					if (fileChooser.getFileFilter() == aux.get(j)) {
			//						for (int iFile = 0; iFile < newFiles.length; iFile++) {
			//							newFiles[iFile] = fileOpen.post(newFiles[iFile]);
			//						}
			//						lastFileOpen = fileOpen;
			//						break;
			//					}
			//				}
			//			}
			//
			//			for (int ind = 0; ind < newFiles.length; ind++) {
			//				if (newFiles[ind] == null) {
			//					break;
			//				}
			//				toAdd.add(new MyFile(newFiles[ind], (fileChooser
			//						.getFileFilter()), lastFileOpen));
			//			}
			//
			//			return toAdd.toArray();
		}
		return toAdd;
	}

	protected abstract PrepareContext getPrepareDataStoreContext();

	private void updateButtons() {
		int selectedIndex =this.getFileList().getSelectedIndex();
		int size =this.getFileList().getModel().getSize();
		int[] selecteds = this.getFileList().getSelectedIndices();
		if ( size < 1) {
			this.getEditButton().setEnabled(false);
			this.getRemoveButton().setEnabled(false);
			this.getUpButton().setEnabled(false);
			this.getDownButton().setEnabled(false);
			return;
		} else if (size == 1) {
			this.getUpButton().setEnabled(false);
			this.getDownButton().setEnabled(false);
		} else {
			this.getUpButton().setEnabled(selectedIndex > 0);

			this.getDownButton().setEnabled(
					selectedIndex > -1
							&& selecteds[selecteds.length - 1] < size - 1);
		}
		this.getEditButton().setEnabled(
				selectedIndex > -1
						&& this.getFileList().getSelectedIndices().length == 1);
		this.getRemoveButton().setEnabled(selectedIndex > -1);
	}

}