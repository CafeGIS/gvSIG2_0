package org.gvsig.gui.beans.openfile;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.swing.JFileChooser;

public class FileTextField extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField tf_fileName = null;
	private JButton bt_chooseFile = null;
	private File selectedFile = null;
	private ArrayList enabledListeners = new ArrayList();
	private JFileChooser jfc;
	/**
	 * Used to create file choosers with 'memory'
	 */
	private String JFC_ID = null;

	public static final int ACTION_EVENT_FIELD_DISABLED = 0;
	public static final int ACTION_EVENT_FIELD_ENABLED = 1;
	public static final int ACTION_EVENT_VALUE_CHANGED = 1;

	public FileTextField() {
		super();
		JFC_ID = this.getClass().getName();
		initializeUI();
	}

	public FileTextField(String id) {
		super();
		JFC_ID = id;
		initializeUI();
	}

	private void initializeUI() {
		 jfc = new JFileChooser(JFC_ID, (String) null);
		setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.insets = new Insets(0,0,0,4);
		this.add(getNameField(), constraints);

		getChooseFileButton().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (e.getSource()==getChooseFileButton()) {
					int action = jfc.showDialog(FileTextField.this, Messages.getText("Open"));
					if (action == JFileChooser.APPROVE_OPTION) {
						setSelectedFile(jfc.getSelectedFile());
						setEnabled(true);	
					}
				}
			}
		});
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.weightx = 0.0;
		constraints.weighty = 1.0;
		constraints.insets = new Insets(0,0,0,0);
		this.add(getChooseFileButton(), constraints);
	}

	private JTextField getNameField() {
		if (tf_fileName==null) {
			tf_fileName = new JTextField();
			tf_fileName.addFocusListener(new FocusListener() {
				public void focusGained(FocusEvent e) {}

				public void focusLost(FocusEvent e) {
					updateSelectedFile();
				}
			});
		}
		return tf_fileName;
	}
	
	private JButton getChooseFileButton() {
		if (bt_chooseFile==null) {
			bt_chooseFile = new JButton("...");
		}
		return bt_chooseFile;
	}


	public void setSelectedFile(File file) {
		File oldFile = selectedFile;
		selectedFile = normalizeExtension(file);
		getNameField().setText(selectedFile.toString());
		if (file.isDirectory()) {
			jfc.setLastPath(file);
		}
		else {
			jfc.setLastPath(file.getParentFile());
		}
		fireValueChanged(oldFile, file);
	}

	public File getSelectedFile() {
		return updateSelectedFile();
	}
	
	private File normalizeExtension(File file) {
		javax.swing.filechooser.FileFilter filter = (javax.swing.filechooser.FileFilter) jfc.getFileFilter();
		if (!filter.accept(file)) {
			String path = file.getPath();
			if (filter instanceof FileFilter)  {
				FileFilter ourFilter = (FileFilter) filter;
			if (path.endsWith(".")) {
				path = path+ourFilter.getDefaultExtension();
			}
			else {
				path = path+"."+ourFilter.getDefaultExtension();
			}
			file = new File(path);
			}
		}
		return file;	
	}
	
	private File updateSelectedFile() {
		File oldFile = selectedFile;
		String text = getNameField().getText();
		if ( (oldFile!=null && !oldFile.getPath().equals(text))
				|| ((oldFile==null) && !text.equals(""))){
			File newFile = normalizeExtension(new File(getNameField().getText()));
			selectedFile = newFile;
			fireValueChanged(oldFile, newFile);
		}
		return selectedFile;
	}

	protected void fireValueChanged(File oldValue, File newValue) {
		firePropertyChange("selectedFileChanged", oldValue, newValue);
	}

	protected void fireEnabledChanged(boolean oldValue, boolean newValue) {
		firePropertyChange("enabled", oldValue, newValue);
	}

	public void setEnabled(boolean enabled) {
		boolean oldValue = isEnabled();
		super.setEnabled(enabled);
		getNameField().setEnabled(enabled);
		fireEnabledChanged(oldValue, enabled);
	}

	public javax.swing.filechooser.FileFilter getFileFilter() {
		return jfc.getFileFilter();
	}

	public int getFileSelectionMode() {
		return jfc.getFileSelectionMode();
	}

	public boolean removeChoosableFileFilter(FileFilter f) {
		return jfc.removeChoosableFileFilter(f);
	}

	public void setFileFilter(FileFilter filter) {

		jfc.setFileFilter(filter);
		
	}

	public void addChoosableFileFilter(FileFilter filter) {
		jfc.addChoosableFileFilter(filter);
	}

	public FileFilter getAcceptAllFileFilter() {
		return (FileFilter) jfc.getAcceptAllFileFilter();
	}

	public boolean isAcceptAllFileFilterUsed() {
		return jfc.isAcceptAllFileFilterUsed();
	}

	public void resetChoosableFileFilters() {
		jfc.resetChoosableFileFilters();
	}

	public void setAcceptAllFileFilterUsed(boolean b) {
		jfc.setAcceptAllFileFilterUsed(b);
	}
	
}
