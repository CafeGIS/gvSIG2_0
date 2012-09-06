package org.gvsig.gui.beans.comboboxconfigurablelookup.usertests;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.comboboxconfigurablelookup.DefaultComboBoxConfigurableLookUpModel;
import org.gvsig.gui.beans.comboboxconfigurablelookup.JComboBoxConfigurableLookUp;
import org.gvsig.gui.beans.comboboxconfigurablelookup.programmertests.SampleBasicComboBoxRenderer;


/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
*
* For more information, contact:
*
*  Generalitat Valenciana
*   Conselleria d'Infraestructures i Transport
*   Av. Blasco Ibáñez, 50
*   46010 VALENCIA
*   SPAIN
*
*      +34 963862235
*   gvsig@gva.es
*      www.gvsig.gva.es
*
*    or
*
*   IVER T.I. S.A
*   Salamanca 50
*   46005 Valencia
*   Spain
*
*   +34 963163400
*   dac@iver.es
*/

/** 
 * <p>Application for testing <code>JComboBoxLookUpConfigurable</code> objects.</p>
 * 
 * <p>This class is a JPanel and has other classes nested.</p>
 * 
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 * @version 07/02/2008
 */
public class JPanelUserTestOfJComboBoxConfigurableLookUp extends JPanel implements Serializable {
	private static final long serialVersionUID = -8900802197326194647L;

	private final int panelWidth = 735;
	private final int panelHeight = 580;
	private final int testWidth = 260;
	private ConfigurationJPanel configurationPanel = null;
	private OtherTestsJPanel otherTestsPanel = null;
	private JavaFileMethodsLoader javaFileMethodsLoader;
	private int testNumber;
	private String path;
	private boolean isConfiguring;
	
	private static final short DEFAULT_CELL_RENDERER = 0;
	private static final short SAMPLE_BG_COLOR_CELL_RENDERER = 1;
	
	/**
	 * <p>Default constructor.</p>
	 */
	public JPanelUserTestOfJComboBoxConfigurableLookUp() {
		super();
		this.initialize();
	}

	/**
	 * <p>Initializes the application pane.</p>
	 */
	private void initialize() {
		path = "src/org/gvsig/gui/beans/comboboxconfigurablelookup/JComboBoxConfigurableLookUp.java";
		isConfiguring = false;
		this.setPreferredSize(new Dimension(panelWidth, panelHeight));
		this.setSize(new Dimension(panelWidth, panelHeight));
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		this.setLayout(flowLayout);
		this.add(this.getConfigurationPanel());
		this.add(this.getOtherTestsPanel());	
		this.loadPublicMethods();
		this.redirectDebugAndErrorMessages();
		testNumber = 1;
	}	

	/**
	 * <p>Loads public methods of the <code>JComboBoxConfigurableLookUp</code> from its file class and adds the constructors to the
	 *   inner <code>ConfigurationPanel</code> and the other methods to the <code>OtherTestsPanel</code>.</p>
	 */
	private void loadPublicMethods() {
		String methodHead;
		String reservedWord = new String("public ");
		int reservedWordLength = reservedWord.length();
		
		for (int i = 0; i < javaFileMethodsLoader.size(); i++)
		{
			methodHead = javaFileMethodsLoader.getMethodHead(i);
			if (methodHead.startsWith(reservedWord))
			{
				if (methodHead.startsWith(reservedWord + javaFileMethodsLoader.getClassName()))
					getConfigurationPanel().addConstructorItem(methodHead.substring(reservedWordLength, methodHead.length()));
				else
					getOtherTestsPanel().addMethodItem(methodHead.substring(reservedWordLength, methodHead.length()));
			}
		}
	}
	
	/**
	 * <p>Redirects the default output and error stream to the inner log <code>JTextArea</code>.</p>
	 */
	private void redirectDebugAndErrorMessages() {
		// Now create a new TextAreaOutputStream to write to our JTextArea control and wrap a
		// PrintStream around it to support the println/printf methods.
		PrintStream out = new PrintStream( new TextAreaOutputStream( getOtherTestsPanel().getJTextAreaLog() ) );

		// Redirect standard output stream to the TextAreaOutputStream
		System.setOut( out );

		// Redirect standard error stream to the TextAreaOutputStream
		System.setErr( out );
	}
	
	/**
	 * <p>This method initializes <code>configurationPanel</code>.</p>
	 * 	
	 * @return javax.swing.JComboBoxItemsSeekerConfigurableTestingByTheUserConfigurationPanel	
	 */
	private ConfigurationJPanel getConfigurationPanel() {
		if (configurationPanel == null) {			
			configurationPanel = new ConfigurationJPanel();
			configurationPanel.getJButtonNew().addMouseListener(new MouseAdapter() {
				/*
				 * (non-Javadoc)
				 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
				 */
				public void mouseClicked(MouseEvent arg0) {
					addNewTestObject();
				}
			});
		}

		return configurationPanel;
	}
	
	/**
	 * <p>This method initializes <code>otherTestsPanel</code>.<p>
	 * 	
	 * @return javax.swing.JComboBoxItemsSeekerConfigurableTestingByTheUserOtherTestsPanel	
	 */
	private OtherTestsJPanel getOtherTestsPanel() {
		if (this.otherTestsPanel == null)
			this.otherTestsPanel = new OtherTestsJPanel(path);

		return this.otherTestsPanel;
	}
	
	/**
	 * <p>This method creates a JFrame with a new object for testing.</p>
	 * <p>This method also defines the closing methods and the name of the <code>JFrame</code>, and add the <code>JFrame</code> to the <code>OtherTestPanel</code>; that panel.
	 *    will use it for do methods.</p>
	 */
	private void addNewTestObject() {
		JComboBoxConfigurableLookUp object = this.createComponent();
		object.setPreferredSize(new Dimension(testWidth, object.getPreferredSize().height));
		String keyAndTitle = Messages.getText("test") + " " + String.valueOf(testNumber++);
		
		JFrame jFrame = new JFrame(keyAndTitle);
		jFrame.getContentPane().add(object);
		jFrame.pack();
		jFrame.setVisible(true);
		
		// Centers the JFrame in the middle of the screen
		jFrame.setLocationRelativeTo(null);
		
		// Defines the methods that will have to do the JFrame when it would have to be closed
		jFrame.addWindowListener(new WindowAdapter(){

			/*
			 * (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
			 */
			public void windowClosed(WindowEvent e) {
				// It's executen when the application closes the JFrame
				getOtherTestsPanel().removeTestObject(((JFrame)e.getSource()).getTitle());
				getOtherTestsPanel().getJTextAreaLog().append('(' + ((JFrame)e.getSource()).getTitle() + ", " + Messages.getText("elimination") + ")\n");
			}
			
			/*
			 * (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			public void windowClosing(WindowEvent e) {
				// It's executen when the user closes the JFrame
				getOtherTestsPanel().removeTestObject(((JFrame)e.getSource()).getTitle());
				getOtherTestsPanel().getJTextAreaLog().append('(' + ((JFrame)e.getSource()).getTitle() + ", " + Messages.getText("elimination") + ")\n");
			}
		});

		this.getOtherTestsPanel().addTestObject(keyAndTitle, jFrame);
	}
	

	/**
	 * <p>Obtains the configuration value for create the new object and creates it.</p>
	 * 
	 * @return JComboBoxConfigurableLookUp The new object created
	 */
	private JComboBoxConfigurableLookUp createComponent() {
		JComboBoxConfigurableLookUp comboBox = null;
		String argument = "";

		// ESTA PARTE CREA EL OBJETO, AVISA DE QUE EN CASO NECESARIO PONDRÁ UN PARÁMETRO PREESTABLECIDO, Y ALMACENA EL TEXTO ACERCA
		//  DEL PARÁMETRO QUE PONDRÁ LUEGO EN EL LOG
		switch(getConfigurationPanel().getSelectedComboBoxConstructor()) {
			case 0:
				comboBox = new JComboBoxConfigurableLookUp();
				break;
			case 1:
				JOptionPane.showMessageDialog(null, Messages.getText("will_use_a_default_model"), Messages.getText("warning"), JOptionPane.WARNING_MESSAGE);
				DefaultComboBoxConfigurableLookUpModel model = new DefaultComboBoxConfigurableLookUpModel(new Object[] {"Item1", "Item2", "Item3", "Item4", "Item5"});
				comboBox = new JComboBoxConfigurableLookUp(model);
				argument = "model; ";
				break;
			case 2:
				JOptionPane.showMessageDialog(null, Messages.getText("will_use_a_default_array_of_items"), Messages.getText("warning"), JOptionPane.WARNING_MESSAGE);
				comboBox = new JComboBoxConfigurableLookUp(new Object[] {"Item1", "Item2", "Item3", "Item4", "Item5"});
				argument = "array; ";
				break;
			case 3:
				JOptionPane.showMessageDialog(null, Messages.getText("will_use_a_default_vector_of_items"), Messages.getText("warning"), JOptionPane.WARNING_MESSAGE);
				Vector<Object> vector = new Vector<Object>();
				vector.add("Item1");
				vector.add("Item2");
				vector.add("Item3");
				vector.add("Item4");
				vector.add("Item5");
				comboBox = new JComboBoxConfigurableLookUp(vector);
				argument = "vector; ";
				break;
			default:
				comboBox = new JComboBoxConfigurableLookUp();
		}

		DefaultComboBoxConfigurableLookUpModel model = (DefaultComboBoxConfigurableLookUpModel)comboBox.getModel();
		String itemsOrderText = null;
		String languageRule = null;
		String cellRenderer = getConfigurationPanel().getSelectedComboBoxCellRenderer();

		
		if (isConfiguring) {
			comboBox.setOnlyOneColorOnText(getConfigurationPanel().isSelectedJCheckBoxOnlyOneColor());
			comboBox.setBeepEnabled(getConfigurationPanel().isSelectedJCheckBoxBeepEnabled());
			comboBox.setHidePopupIfThereAreNoItems(getConfigurationPanel().isSelectedJCheckBoxHidePopupIfThereAreNoItems());
			comboBox.setToForceSelectAnItem(getConfigurationPanel().isSelectedJCheckBoxToForceSelectAnItem());
			comboBox.setCompleteArrowKeySelection(getConfigurationPanel().isSelectedJCheckBoxCompleteArrowKeySelection());
			comboBox.setDisplayAllItemsWithArrowButton(getConfigurationPanel().isSelectedJCheckBoxDisplayAllItemsWithArrowButton());
			
			if (getConfigurationPanel().getSelectedComboBoxCellRendererIndex() == SAMPLE_BG_COLOR_CELL_RENDERER)
				comboBox.setRenderer(new SampleBasicComboBoxRenderer());

			// Configuration of the model
			model.setItemsOrder(getConfigurationPanel().getSelectedComboBoxItemsOrder());
			model.setShowAllItemsInListBox(getConfigurationPanel().isSelectedJCheckBoxShowAllItemsInListBox());
			model.setLocaleRules(getConfigurationPanel().getSelectedComboBoxLanguageRuleText());
			model.setCaseSensitive(getConfigurationPanel().isSelectedJCheckBoxCaseSensitive());
			
			itemsOrderText = getConfigurationPanel().getSelectedComboBoxItemsOrderText();
			languageRule = getConfigurationPanel().getSelectedComboBoxLanguageRuleText();
			
			// Write the log
			getOtherTestsPanel().getJTextAreaLog().append('(' + Messages.getText("test") + " " + String.valueOf(testNumber) + ", " +
					Messages.getText("creaction") + ": " + argument + Messages.getText("configuration") + ": " + getConfigurationPanel().isSelectedJCheckBoxOnlyOneColor() + ", " + 
					getConfigurationPanel().isSelectedJCheckBoxBeepEnabled() + ", " + getConfigurationPanel().isSelectedJCheckBoxHidePopupIfThereAreNoItems() + ", " +
					getConfigurationPanel().isSelectedJCheckBoxToForceSelectAnItem() + ", " +
					getConfigurationPanel().isSelectedJCheckBoxCompleteArrowKeySelection() + ", " +
					getConfigurationPanel().isSelectedJCheckBoxDisplayAllItemsWithArrowButton() + ", " +
					cellRenderer + ", " +
					itemsOrderText + ", " +
					getConfigurationPanel().isSelectedJCheckBoxShowAllItemsInListBox() + ", " + languageRule + ", " +
					getConfigurationPanel().isSelectedJCheckBoxCaseSensitive() + ")\n");
		}
		else {
			switch(model.getItemsOrder()) {
				case DefaultComboBoxConfigurableLookUpModel.MAINTAIN_POSITION:
					itemsOrderText = Messages.getText("maintain_position");
					break;
				case DefaultComboBoxConfigurableLookUpModel.ALPHABETICAL_ORDERED:
					itemsOrderText = Messages.getText("alphabetical_ordered");
					break;
				case DefaultComboBoxConfigurableLookUpModel.MAINTAIN_AGENT_POSITIONS:
					itemsOrderText = Messages.getText("agent_positions");
					break;
			}
			
			// Write the log
			getOtherTestsPanel().getJTextAreaLog().append('(' + Messages.getText("test") + " " + String.valueOf(testNumber) + ", " +
					Messages.getText("creaction") + ": " + argument + Messages.getText("configuration") + ": " + comboBox.isOnlyOneColorOnText() + ", " +
					 comboBox.isBeepEnabled() + ", " + comboBox.isHidePopupIfThereAreNoItems() + ", " + comboBox.isToForceSelectAnItem() + ", " +
					 comboBox.isCompleteArrowKeySelection() + ", " + comboBox.isDisplayAllItemsWithArrowButton() + ", " + cellRenderer + ", " +
					 itemsOrderText + ", " + model.isShowAllItemsInListBox() + ", " + model.getLocaleRules() + ", " + model.isCaseSensitive() + ")\n");
		}

		return comboBox;
	}

	/**
	 * <p>A JPanel for the configuration and create a new JComboBoxConfigurableLookUp for testing.</p>
	 *  
	 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
	 */
	private class ConfigurationJPanel extends JPanel implements Serializable {
		private static final long serialVersionUID = 6327342028864667372L;

		private final int configurationPanelWidth = 310;
		private final int configurationPanelHeight = 570;
		private final int subPanelWidth = 280;
		private final int subPanelHeight = 28;
		private final int buttonWidth = 90;
		private final int buttonHeight = 20;
		private final int jComboBoxWidth = 160;
		private final int jComboBoxHeight = 20;
		private final int jPaneConfigurationParametersWidth = 296;
		private final int jPaneConfigurationParametersHeight = 440;
		private final int jPaneConstructorsWidth = jPaneConfigurationParametersWidth;
		private final int jPaneConstructorsHeight = 55;
		private final int jComboBoxConstructorsWidth = jPaneConfigurationParametersWidth - 10;
		private final int jComboBoxConstructorsHeight = jComboBoxHeight;
		private JCheckBox jCheckBoxOnlyOneColorOnText = null;
		private JCheckBox jCheckBoxBeepEnabled = null;
		private JCheckBox jCheckBoxHidePopupIfThereAreNoItems = null;
		private JCheckBox jCheckBoxToForceSelectAnItem = null;
		private JCheckBox jCheckBoxShowAllItemsInListBox = null;
		private JCheckBox jCheckBoxCaseSensitive = null;
		private JCheckBox jCheckBoxCompleteArrowKeySelection = null;
		private JCheckBox jCheckBoxDisplayAllItemsWithArrowButton = null;
		private JLabel jLabelOnlyOneColorOnText = null;
		private JLabel jLabelBeepEnabled = null;
		private JLabel jLabelHidePopupIfThereAreNoItems = null;
		private JLabel jLabelToForceSelectAnItem = null;
		private JLabel jLabelItemsOrder = null;
		private JLabel jLabelShowAllItemsInListBox = null;
		private JLabel jLabelLanguageRules = null;
		private JLabel jLabelCaseSensitive = null;
		private JLabel jLabelCompleteArrowKeySelection = null;
		private JLabel jLabelDisplayAllItemsWithArrowButton = null;
		private JLabel jLabelCellRenderer = null;
		private JButton jButtonReset = null;
		private JPanel jPaneConstructor = null;
		private JPanel jPaneConfigurationParameters = null;
		private JPanel jPaneOnlyOneColorOnText = null;
		private JPanel jPaneBeepEnabled = null;
		private JPanel jPaneHidePopupIfThereAreNoItems = null;
		private JPanel jPaneToForceSelectAnItem = null;
		private JPanel jPaneItemsOrder = null;
		private JPanel jPaneShowAllItemsInListBox = null;
		private JPanel jPaneLanguageRules = null;
		private JPanel jPaneCaseSensitive = null;
		private JPanel jPaneCompleteArrowKeySelection = null;
		private JPanel jPaneDisplayAllItemsWithArrowButton = null;
		private JPanel jPaneCellRenderer = null;
		private JPanel jPaneButtons = null;
		private JComboBox jComboBoxConstructors = null;
		private JComboBox jComboBoxItemsOrder = null;
		private JComboBox jComboBoxLanguageRules = null;
		private JComboBox jComboBoxCellRenderer = null;
		private JButton jButtonNew = null;
		private JButton jButtonEnableParametersConfiguration = null;
		private Object lastSelectedConstructor = null;
		
		/**
		 * <p>Default constructor.</p>
		 */
		public ConfigurationJPanel() {
			super();
			this.initialize();
		}

		/**
		 * <p>Initializes this pane.</p>
		 */
		private void initialize() {
			this.setPreferredSize(new Dimension(configurationPanelWidth, configurationPanelHeight));
			this.setSize(new Dimension(configurationPanelWidth, configurationPanelHeight));
			this.setBorder(BorderFactory.createTitledBorder(null, Messages.getText("objectsCreation"), TitledBorder.LEFT, TitledBorder.TOP, new Font("Dialog", Font.BOLD, 12), Color.black));
			this.add(getJPaneConstructors(), null);
			this.add(getJPaneConfigurationParameters(), null);
			this.add(getJPaneButtons(), null);
			this.resetConfigurationPanel();
		}

		/**
		 * <p>This method initializes <code>jCheckBoxOnlyOneColor</code>.</p>
		 * 	
		 * @return javax.swing.JCheckBox	
		 */
		private JCheckBox getJCheckBoxOnlyOneColorOnText() {
			if (jCheckBoxOnlyOneColorOnText == null) {
				jCheckBoxOnlyOneColorOnText = new JCheckBox();
			}
			return jCheckBoxOnlyOneColorOnText;
		}

		/**
		 * <p>This method initializes <code>jCheckBoxBeepEnabled</code>.</p>
		 * 	
		 * @return javax.swing.JCheckBox	
		 */
		private JCheckBox getJCheckBoxBeepEnabled() {
			if (jCheckBoxBeepEnabled == null) {
				jCheckBoxBeepEnabled = new JCheckBox();
			}
			return jCheckBoxBeepEnabled;
		}

		/**
		 * <p>This method initializes <code>jCheckBoxHidePopupIfThereAreNoItems</code>.</p>
		 * 	
		 * @return javax.swing.JCheckBox	
		 */
		private JCheckBox getJCheckBoxHidePopupIfThereAreNoItems() {
			if (jCheckBoxHidePopupIfThereAreNoItems == null) {
				jCheckBoxHidePopupIfThereAreNoItems = new JCheckBox();
			}
			return jCheckBoxHidePopupIfThereAreNoItems;
		}

		/**
		 * <p>This method initializes <code>jCheckBoxToForceSelectAnItem</code>.</p>
		 * 	
		 * @return javax.swing.JCheckBox	
		 */
		private JCheckBox getJCheckBoxToForceSelectAnItem() {
			if (jCheckBoxToForceSelectAnItem == null) {
				jCheckBoxToForceSelectAnItem = new JCheckBox();
			}
			return jCheckBoxToForceSelectAnItem;
		}

		/**
		 * <p>This method initializes <code>jCheckBoxShowAllItemsInListBox</code>.</p>
		 * 	
		 * @return javax.swing.JCheckBox	
		 */
		private JCheckBox getJCheckBoxShowAllItemsInListBox() {
			if (jCheckBoxShowAllItemsInListBox == null) {
				jCheckBoxShowAllItemsInListBox = new JCheckBox();
			}
			return jCheckBoxShowAllItemsInListBox;
		}		

		/**
		 * <p>This method initializes <code>jCheckBoxCaseSensitive</code>.</p>
		 * 	
		 * @return javax.swing.JCheckBox	
		 */
		private JCheckBox getJCheckBoxCaseSensitive() {
			if (jCheckBoxCaseSensitive == null) {
				jCheckBoxCaseSensitive = new JCheckBox();
			}
			return jCheckBoxCaseSensitive;
		}

		/**
		 * <p>This method initializes <code>jCheckBoxCompleteArrowKeySelection</code>.</p>
		 * 	
		 * @return javax.swing.JCheckBox	
		 */
		private JCheckBox getJCheckBoxCompleteArrowKeySelection() {
			if (jCheckBoxCompleteArrowKeySelection == null) {
				jCheckBoxCompleteArrowKeySelection = new JCheckBox();
			}
			return jCheckBoxCompleteArrowKeySelection;
		}

		/**
		 * <p>This method initializes <code>jCheckBoxDisplayAllItemsWithArrowButton</code>.</p>
		 * 	
		 * @return javax.swing.JCheckBox	
		 */
		private JCheckBox getJCheckBoxDisplayAllItemsWithArrowButton() {
			if (jCheckBoxDisplayAllItemsWithArrowButton == null) {
				jCheckBoxDisplayAllItemsWithArrowButton = new JCheckBox();
			}
			return jCheckBoxDisplayAllItemsWithArrowButton;
		}
		
		/**
		 * @see JCheckBox#setSelected(boolean)
		 */
		public void setSelectedJCheckBoxOnlyOneColor(boolean b) {
			getJCheckBoxOnlyOneColorOnText().setSelected(b);
		}

		/**
		 * @see JCheckBox#setSelected(boolean)
		 */
		public void setSelectedJCheckBoxBeepEnabled(boolean b) {
			getJCheckBoxBeepEnabled().setSelected(b);
		}

		/**
		 * @see JCheckBox#setSelected(boolean)
		 */
		public void setSelectedJCheckBoxHidePopupIfThereAreNoItems(boolean b) {
			getJCheckBoxHidePopupIfThereAreNoItems().setSelected(b);
		}

		/**
		 * @see JCheckBox#setSelected(boolean)
		 */
		public void setSelectedJCheckBoxToForceSelectAnItem(boolean b) {
			getJCheckBoxToForceSelectAnItem().setSelected(b);
		}

		/**
		 * @see JCheckBox#setSelected(boolean)
		 */
		public void setSelectedJCheckShowAllItemsInListBox(boolean b) {
			getJCheckBoxShowAllItemsInListBox().setSelected(b);
		}

		/**
		 * @see JCheckBox#setSelected(boolean)
		 */
		public void setSelectedJCheckBoxCaseSensitive(boolean b) {
			jCheckBoxCaseSensitive.setSelected(b);
		}

		/**
		 * @see JCheckBox#setSelected(boolean)
		 */
		public void setSelectedJCheckBoxCompleteArrowKeySelection(boolean b) {
			jCheckBoxCompleteArrowKeySelection.setSelected(b);
		}

		/**
		 * @see JCheckBox#setSelected(boolean)
		 */
		public void setSelectedJCheckBoxDisplayAllItemsWithArrowButton(boolean b) {
			jCheckBoxDisplayAllItemsWithArrowButton.setSelected(b);
		}
		
		/**
		 * @see JCheckBox#isSelected()
		 */
		public boolean isSelectedJCheckBoxOnlyOneColor() {
			return getJCheckBoxOnlyOneColorOnText().isSelected();
		}

		/**
		 * @see JCheckBox#isSelected()
		 */
		public boolean isSelectedJCheckBoxBeepEnabled() {
			return getJCheckBoxBeepEnabled().isSelected();
		}	
		
		/**
		 * @see JCheckBox#isSelected()
		 */
		public boolean isSelectedJCheckBoxHidePopupIfThereAreNoItems() {
			return getJCheckBoxHidePopupIfThereAreNoItems().isSelected();
		}

		/**
		 * @see JCheckBox#isSelected()
		 */
		public boolean isSelectedJCheckBoxToForceSelectAnItem() {
			return getJCheckBoxToForceSelectAnItem().isSelected();
		}

		/**
		 * @see JCheckBox#isSelected()
		 */
		public boolean isSelectedJCheckBoxShowAllItemsInListBox() {
			return getJCheckBoxShowAllItemsInListBox().isSelected();
		}
		
		/**
		 * @see JCheckBox#isSelected()
		 */
		public boolean isSelectedJCheckBoxCaseSensitive() {
			return getJCheckBoxCaseSensitive().isSelected();
		}
		
		/**
		 * @see JCheckBox#isSelected()
		 */
		public boolean isSelectedJCheckBoxCompleteArrowKeySelection() {
			return getJCheckBoxCompleteArrowKeySelection().isSelected();
		}

		/**
		 * @see JCheckBox#isSelected()
		 */
		public boolean isSelectedJCheckBoxDisplayAllItemsWithArrowButton() {
			return getJCheckBoxDisplayAllItemsWithArrowButton().isSelected();
		}
		
		/**
		 * <p>This method initializes <code>jButtonReset</code>.<p>
		 * 	
		 * @return javax.swing.JButton	
		 */
		private JButton getJButtonReset() {
			if (jButtonReset == null) {
				jButtonReset = new JButton();
				jButtonReset.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
				jButtonReset.setText(Messages.getText("reset"));
				jButtonReset.addMouseListener(new MouseAdapter() {
					/*
					 * (non-Javadoc)
					 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
					 */
					public void mouseClicked(MouseEvent arg0) {
						resetConfigurationPanel();
					}
				});
			}
			return jButtonReset;
		}

		/**
		 * <p>This method initializes <code>jPaneConfigurationParameters</code>.</p>
		 * 	
		 * @return javax.swing.JPanel	
		 */
		private JPanel getJPaneConfigurationParameters() {
			if (jPaneConfigurationParameters == null) {				
				jPaneConfigurationParameters = new JPanel();
				jPaneConfigurationParameters.setPreferredSize(new Dimension(jPaneConfigurationParametersWidth, jPaneConfigurationParametersHeight));
				jPaneConfigurationParameters.setBorder(BorderFactory.createTitledBorder(null, Messages.getText("configurationParameters"), TitledBorder.LEFT, TitledBorder.TOP, new Font("Dialog", Font.BOLD, 12), Color.black));

				// Enable or disable the configuration of the parameters
				jPaneConfigurationParameters.add(getJButtonEnableParametersConfiguration());
				
				// Parameters of the JComboBoxLookUpConfigurable View and Control
				jPaneConfigurationParameters.add(getJPaneOnlyOneColorOnText(), null);
				jPaneConfigurationParameters.add(getJPaneBeepEnabled(), null);
				jPaneConfigurationParameters.add(getJPaneHidePopupIfThereAreNoItems(), null);
				jPaneConfigurationParameters.add(getJPaneToForceSelectAnItem(), null);
				jPaneConfigurationParameters.add(getJPaneCompleteArrowKeySelection(), null);
				jPaneConfigurationParameters.add(getJPaneDisplayAllItemsWithArrowButton(), null);
				jPaneConfigurationParameters.add(getJPaneCellRenderer(), null);
				
				// Parameters of the JComboBoxLookUpConfigurable Model
				jPaneConfigurationParameters.add(getJPaneItemsOrder(), null);
				jPaneConfigurationParameters.add(getJPaneShowAllItemsInListBox(), null);
				jPaneConfigurationParameters.add(getJPaneLanguageRules(), null);
				jPaneConfigurationParameters.add(getJPaneCaseSensitive(), null);

				// By default don't change the default configuration
				enableAllConfigurationComponents(false);
			}
			return jPaneConfigurationParameters;
		}

		/**
		 * <p>This method initializes <code>jLabelOnlyOneColorOnText</code>.</p>
		 * 	
		 * @return javax.swing.JLabel	
		 */
		private JLabel getJLabelOnlyOneColorOnText() {
			if (jLabelOnlyOneColorOnText == null) {
				jLabelOnlyOneColorOnText = new JLabel();
				jLabelOnlyOneColorOnText.setText(Messages.getText("onlyOneColorOnTextConfigurationLabel"));
				jLabelOnlyOneColorOnText.addMouseListener(new MouseAdapter(){
					/*
					 * (non-Javadoc)
					 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
					 */
					public void mouseClicked(MouseEvent arg0) {
						getJCheckBoxOnlyOneColorOnText().setSelected(!getJCheckBoxOnlyOneColorOnText().isSelected());
					}
				});
			}
			return jLabelOnlyOneColorOnText;
		}

		/**
		 * <p>This method initializes <code>jLabelBeepEnabled</code>.</p>
		 * 	
		 * @return javax.swing.JLabel	
		 */
		private JLabel getJLabelBeepEnabled() {
			if (jLabelBeepEnabled == null) {
				jLabelBeepEnabled = new JLabel();
				jLabelBeepEnabled.setText(Messages.getText("beepConfigurationLabel"));
				jLabelBeepEnabled.addMouseListener(new MouseAdapter(){
					/*
					 * (non-Javadoc)
					 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
					 */
					public void mouseClicked(MouseEvent arg0) {
						getJCheckBoxBeepEnabled().setSelected(!getJCheckBoxBeepEnabled().isSelected());
					}
				});
			}
			return jLabelBeepEnabled;
		}

		/**
		 * <p>This method initializes <code>jLabelHidePopupIfThereAreNoItems</code>.</p>
		 * 	
		 * @return javax.swing.JLabel	
		 */
		private JLabel getJLabelHidePopupIfThereAreNoItems() {
			if (jLabelHidePopupIfThereAreNoItems == null) {
				jLabelHidePopupIfThereAreNoItems = new JLabel();
				jLabelHidePopupIfThereAreNoItems.setText(Messages.getText("hidePopupIfThereAreNoItemsConfigurationLabel"));
				jLabelHidePopupIfThereAreNoItems.addMouseListener(new MouseAdapter(){
					/*
					 * (non-Javadoc)
					 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
					 */
					public void mouseClicked(MouseEvent arg0) {
						getJCheckBoxHidePopupIfThereAreNoItems().setSelected(!getJCheckBoxHidePopupIfThereAreNoItems().isSelected());
					}
				});
			}
			return jLabelHidePopupIfThereAreNoItems;
		}

		/**
		 * <p>This method initializes <code>jLabelToForceSelectAnItem</code>.</p>
		 * 	
		 * @return javax.swing.JLabel	
		 */
		private JLabel getJLabelToForceSelectAnItem() {
			if (jLabelToForceSelectAnItem == null) {
				jLabelToForceSelectAnItem = new JLabel();
				jLabelToForceSelectAnItem.setText(Messages.getText("toForceSelectAnItemConfigurationLabel"));
				jLabelToForceSelectAnItem.addMouseListener(new MouseAdapter(){
					/*
					 * (non-Javadoc)
					 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
					 */
					public void mouseClicked(MouseEvent arg0) {
						getJCheckBoxToForceSelectAnItem().setSelected(!getJCheckBoxToForceSelectAnItem().isSelected());
					}
				});
			}
			return jLabelToForceSelectAnItem;
		}

		/**
		 * <p>This method initializes <code>jLabelItemsOrder</code>.<p>
		 * 	
		 * @return javax.swing.JLabel	
		 */
		private JLabel getJLabelItemsOrder() {
			if (jLabelItemsOrder == null) {
				jLabelItemsOrder = new JLabel();
				jLabelItemsOrder.setText(Messages.getText("itemsOrderConfigurationLabel"));
			}
			return jLabelItemsOrder;
		}

		/**
		 * <p>This method initializes <code>jLabelShowAllItemsInListBox</code>.</p>
		 * 	
		 * @return javax.swing.JLabel	
		 */
		private JLabel getJLabelShowAllItemsInListBox() {
			if (jLabelShowAllItemsInListBox == null) {
				jLabelShowAllItemsInListBox = new JLabel();
				jLabelShowAllItemsInListBox.setText(Messages.getText("showAllItemsInListBoxConfigurationLabel"));
				jLabelShowAllItemsInListBox.addMouseListener(new MouseAdapter(){
					/*
					 * (non-Javadoc)
					 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
					 */
					public void mouseClicked(MouseEvent arg0) {
						getJCheckBoxShowAllItemsInListBox().setSelected(!getJCheckBoxShowAllItemsInListBox().isSelected());
					}
				});
			}
			return jLabelShowAllItemsInListBox;
		}

		/**
		 * <p>This method initializes <code>jLabelLanguageRules</code>.</p>
		 * 	
		 * @return javax.swing.JLabel	
		 */
		private JLabel getJLabelLanguageRules() {
			if (jLabelLanguageRules == null) {
				jLabelLanguageRules = new JLabel();
				jLabelLanguageRules.setText(Messages.getText("languageRulesConfigurationLabel"));
			}
			return jLabelLanguageRules;
		}

		/**
		 * <p>This method initializes <code>jLabelCaseSensitive1</code>.</p>
		 * 	
		 * @return javax.swing.JLabel	
		 */
		private JLabel getJLabelCaseSensitive() {
			if (jLabelCaseSensitive == null) {
				jLabelCaseSensitive = new JLabel();
				jLabelCaseSensitive.setText(Messages.getText("caseSensitiveConfigurationLabel"));
				jLabelCaseSensitive.addMouseListener(new MouseAdapter(){
					/*
					 * (non-Javadoc)
					 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
					 */
					public void mouseClicked(MouseEvent arg0) {
						getJCheckBoxCaseSensitive().setSelected(!getJCheckBoxCaseSensitive().isSelected());
					}
				});
			}
			return jLabelCaseSensitive;
		}
		

		/**
		 * <p>This method initializes <code>jLabelCompleteArrowKeySelection</code>.</p>
		 * 	
		 * @return javax.swing.JLabel	
		 */
		private JLabel getJLabelCompleteArrowKeySelection() {
			if (jLabelCompleteArrowKeySelection == null) {
				jLabelCompleteArrowKeySelection = new JLabel();
				jLabelCompleteArrowKeySelection.setText(Messages.getText("completeArrowKeySelectionConfigurationLabel"));
				jLabelCompleteArrowKeySelection.addMouseListener(new MouseAdapter(){
					/*
					 * (non-Javadoc)
					 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
					 */
					public void mouseClicked(MouseEvent arg0) {
						getJCheckBoxCompleteArrowKeySelection().setSelected(!getJCheckBoxCompleteArrowKeySelection().isSelected());
					}
				});
			}
			return jLabelCompleteArrowKeySelection;
		}

		/**
		 * <p>This method initializes <code>jLabelDisplayAllItemsWithArrowButton</code>.</p>
		 * 	
		 * @return javax.swing.JLabel	
		 */
		private JLabel getJLabelDisplayAllItemsWithArrowButton() {
			if (jLabelDisplayAllItemsWithArrowButton == null) {
				jLabelDisplayAllItemsWithArrowButton = new JLabel();
				jLabelDisplayAllItemsWithArrowButton.setText(Messages.getText("displayAllItemsWithArrowButtonConfigurationLabel"));
				jLabelDisplayAllItemsWithArrowButton.addMouseListener(new MouseAdapter(){
					/*
					 * (non-Javadoc)
					 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
					 */
					public void mouseClicked(MouseEvent arg0) {
						getJCheckBoxDisplayAllItemsWithArrowButton().setSelected(!getJCheckBoxDisplayAllItemsWithArrowButton().isSelected());
					}
				});
			}
			return jLabelDisplayAllItemsWithArrowButton;
		}

		/**
		 * <p>This method initializes <code>jLabelCellRenderer</code>.</p>
		 * 	
		 * @return javax.swing.JLabel	
		 */
		private JLabel getJLabelCellRenderer() {
			if (jLabelCellRenderer == null) {
				jLabelCellRenderer = new JLabel();
				jLabelCellRenderer.setText(Messages.getText("cellRendererConfigurationLabel"));
			}
			return jLabelCellRenderer;
		}

		/**
		 * <p>This method initializes <code>jPaneConstructor</code>.</p>
		 * 	
		 * @return javax.swing.JPanel	
		 */
		private JPanel getJPaneConstructors() {
			if (jPaneConstructor == null) {
				jPaneConstructor = new JPanel();
				jPaneConstructor.setPreferredSize(new Dimension(jPaneConstructorsWidth, jPaneConstructorsHeight));
				jPaneConstructor.setBorder(BorderFactory.createTitledBorder(null, Messages.getText("constructors"), TitledBorder.LEFT, TitledBorder.TOP, new Font("Dialog", Font.BOLD, 12), Color.black));
				jPaneConstructor.add(getJComboBoxConstructors(), null);
			}
			return jPaneConstructor;
		}

		/**
		 * <p>This method initializes <code>jPaneOnlyOneColor</code>.</p>	
		 * 	
		 * @return javax.swing.JPanel	
		 */
		private JPanel getJPaneOnlyOneColorOnText() {
			if (jPaneOnlyOneColorOnText == null) {				
				FlowLayout flowLayout = new FlowLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				jPaneOnlyOneColorOnText = new JPanel();
				jPaneOnlyOneColorOnText.setPreferredSize(new Dimension(subPanelWidth, subPanelHeight));
				jPaneOnlyOneColorOnText.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
				jPaneOnlyOneColorOnText.setLayout(flowLayout);
				jPaneOnlyOneColorOnText.add(getJCheckBoxOnlyOneColorOnText(), null);
				jPaneOnlyOneColorOnText.add(getJLabelOnlyOneColorOnText(), null);
			}
			return jPaneOnlyOneColorOnText;
		}

		/**
		 * <p>This method initializes <code>jPaneBeepEnabled</code>.</p>
		 * 	
		 * @return javax.swing.JPanel	
		 */
		private JPanel getJPaneBeepEnabled() {
			if (jPaneBeepEnabled == null) {
				FlowLayout flowLayout = new FlowLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				jPaneBeepEnabled = new JPanel();
				jPaneBeepEnabled.setPreferredSize(new Dimension(subPanelWidth, subPanelHeight));
				jPaneBeepEnabled.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
				jPaneBeepEnabled.setLayout(flowLayout);
				jPaneBeepEnabled.add(getJCheckBoxBeepEnabled(), null);
				jPaneBeepEnabled.add(getJLabelBeepEnabled(), null);
			}
			return jPaneBeepEnabled;
		}

		/**
		 * <p>This method initializes <code>jPaneHidePopupIfThereAreNoItems</code>.</p>
		 * 	
		 * @return javax.swing.JPanel	
		 */
		private JPanel getJPaneHidePopupIfThereAreNoItems() {
			if (jPaneHidePopupIfThereAreNoItems == null) {
				FlowLayout flowLayout = new FlowLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				jPaneHidePopupIfThereAreNoItems = new JPanel();
				jPaneHidePopupIfThereAreNoItems.setPreferredSize(new Dimension(subPanelWidth, subPanelHeight));
				jPaneHidePopupIfThereAreNoItems.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
				jPaneHidePopupIfThereAreNoItems.setLayout(flowLayout);
				jPaneHidePopupIfThereAreNoItems.add(getJCheckBoxHidePopupIfThereAreNoItems(), null);
				jPaneHidePopupIfThereAreNoItems.add(getJLabelHidePopupIfThereAreNoItems(), null);
			}
			return jPaneHidePopupIfThereAreNoItems;
		}

		/**
		 * <p>This method initializes <code>jPaneToForceSelectAnItem</code>.</p>
		 * 	
		 * @return javax.swing.JPanel	
		 */
		private JPanel getJPaneToForceSelectAnItem() {
			if (jPaneToForceSelectAnItem == null) {
				FlowLayout flowLayout = new FlowLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				jPaneToForceSelectAnItem = new JPanel();
				jPaneToForceSelectAnItem.setPreferredSize(new Dimension(subPanelWidth, subPanelHeight));
				jPaneToForceSelectAnItem.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
				jPaneToForceSelectAnItem.setLayout(flowLayout);
				jPaneToForceSelectAnItem.add(getJCheckBoxToForceSelectAnItem(), null);
				jPaneToForceSelectAnItem.add(getJLabelToForceSelectAnItem(), null);
			}
			return jPaneToForceSelectAnItem;
		}

		/**
		 * <p>This method initializes <code>jPaneItemsOrder</code>.</p>
		 * 	
		 * @return javax.swing.JPanel	
		 */
		private JPanel getJPaneItemsOrder() {
			if (jPaneItemsOrder == null) {
				FlowLayout flowLayout = new FlowLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				jPaneItemsOrder = new JPanel();
				jPaneItemsOrder.setPreferredSize(new Dimension(subPanelWidth, subPanelHeight));
				jPaneItemsOrder.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
				jPaneItemsOrder.setLayout(flowLayout);
				jPaneItemsOrder.add(getJComboBoxItemsOrder(), null);
				jPaneItemsOrder.add(getJLabelItemsOrder(), null);
			}
			return jPaneItemsOrder;
		}

		/**
		 * <p>This method initializes <code>jPaneShowAllItemsInListBox</code>.</p>
		 * 	
		 * @return javax.swing.JPanel	
		 */
		private JPanel getJPaneShowAllItemsInListBox() {
			if (jPaneShowAllItemsInListBox == null) {
				FlowLayout flowLayout = new FlowLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				jPaneShowAllItemsInListBox = new JPanel();
				jPaneShowAllItemsInListBox.setPreferredSize(new Dimension(subPanelWidth, subPanelHeight));
				jPaneShowAllItemsInListBox.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
				jPaneShowAllItemsInListBox.setLayout(flowLayout);
				jPaneShowAllItemsInListBox.add(getJCheckBoxShowAllItemsInListBox(), null);
				jPaneShowAllItemsInListBox.add(getJLabelShowAllItemsInListBox(), null);
			}
			return jPaneShowAllItemsInListBox;
		}

		/**
		 * <p>This method initializes <code>jPaneLanguageRules</code>.</p>
		 * 	
		 * @return javax.swing.JPanel	
		 */
		private JPanel getJPaneLanguageRules() {
			if (jPaneLanguageRules == null) {
				FlowLayout flowLayout = new FlowLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				jPaneLanguageRules = new JPanel();
				jPaneLanguageRules.setPreferredSize(new Dimension(subPanelWidth, subPanelHeight));
				jPaneLanguageRules.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
				jPaneLanguageRules.setLayout(flowLayout);
				jPaneLanguageRules.add(getJComboBoxLanguageRules(), null);
				jPaneLanguageRules.add(getJLabelLanguageRules(), null);
			}
			return jPaneLanguageRules;
		}

		/**
		 * <p>This method initializes <code>jPaneCaseSensitive</code>.</p>
		 * 	
		 * @return javax.swing.JPanel	
		 */
		private JPanel getJPaneCaseSensitive() {
			if (jPaneCaseSensitive == null) {
				FlowLayout flowLayout = new FlowLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				jPaneCaseSensitive = new JPanel();
				jPaneCaseSensitive.setPreferredSize(new Dimension(subPanelWidth, subPanelHeight));
				jPaneCaseSensitive.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
				jPaneCaseSensitive.setLayout(flowLayout);
				jPaneCaseSensitive.add(getJCheckBoxCaseSensitive(), null);
				jPaneCaseSensitive.add(getJLabelCaseSensitive(), null);
			}
			return jPaneCaseSensitive;
		}

		/**
		 * <p>This method initializes <code>jPaneCompleteArrowKeySelection</code>.</p>
		 * 	
		 * @return javax.swing.JPanel	
		 */
		private JPanel getJPaneCompleteArrowKeySelection() {
			if (jPaneCompleteArrowKeySelection == null) {
				FlowLayout flowLayout = new FlowLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				jPaneCompleteArrowKeySelection = new JPanel();
				jPaneCompleteArrowKeySelection.setPreferredSize(new Dimension(subPanelWidth, subPanelHeight));
				jPaneCompleteArrowKeySelection.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
				jPaneCompleteArrowKeySelection.setLayout(flowLayout);
				jPaneCompleteArrowKeySelection.add(getJCheckBoxCompleteArrowKeySelection(), null);
				jPaneCompleteArrowKeySelection.add(getJLabelCompleteArrowKeySelection(), null);
			}
			return jPaneCompleteArrowKeySelection;
		}

		/**
		 * <p>This method initializes <code>jPaneDisplayAllItemsWithArrowButton</code>.</p>
		 * 	
		 * @return javax.swing.JPanel	
		 */
		private JPanel getJPaneDisplayAllItemsWithArrowButton() {
			if (jPaneDisplayAllItemsWithArrowButton == null) {
				FlowLayout flowLayout = new FlowLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				jPaneDisplayAllItemsWithArrowButton = new JPanel();
				jPaneDisplayAllItemsWithArrowButton.setPreferredSize(new Dimension(subPanelWidth, subPanelHeight));
				jPaneDisplayAllItemsWithArrowButton.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
				jPaneDisplayAllItemsWithArrowButton.setLayout(flowLayout);
				jPaneDisplayAllItemsWithArrowButton.add(getJCheckBoxDisplayAllItemsWithArrowButton(), null);
				jPaneDisplayAllItemsWithArrowButton.add(getJLabelDisplayAllItemsWithArrowButton(), null);
			}
			return jPaneDisplayAllItemsWithArrowButton;
		}

		/**
		 * <p>This method initializes <code>jPaneCellRenderer</code>.</p>
		 * 	
		 * @return javax.swing.JPanel	
		 */
		private JPanel getJPaneCellRenderer() {
			if (jPaneCellRenderer == null) {
				FlowLayout flowLayout = new FlowLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				jPaneCellRenderer = new JPanel();
				jPaneCellRenderer.setPreferredSize(new Dimension(subPanelWidth, subPanelHeight));
				jPaneCellRenderer.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
				jPaneCellRenderer.setLayout(flowLayout);
				jPaneCellRenderer.add(getJComboBoxCellRenderer(), null);
				jPaneCellRenderer.add(getJLabelCellRenderer(), null);
			}
			return jPaneCellRenderer;
		}

		/**
		 * <p>This method initializes <code>jPaneButtons</code>.</p>
		 * 	
		 * @return javax.swing.JPanel	
		 */
		private JPanel getJPaneButtons() {
			if (jPaneButtons == null) {
				FlowLayout flowLayout = new FlowLayout();
				flowLayout.setAlignment(FlowLayout.RIGHT);
				jPaneButtons = new JPanel();
				jPaneButtons.setPreferredSize(new Dimension(subPanelWidth, subPanelHeight));
				jPaneButtons.setLayout(flowLayout);
				jPaneButtons.add(getJButtonReset(), null);
				jPaneButtons.add(getJButtonNew(), null);
			}
			return jPaneButtons;
		}		
		
		/**
		 * <p>This method initializes <code>jComboBoxConstructors</code>.</p>	
		 * 	
		 * @return javax.swing.JComboBox	
		 */
		private JComboBox getJComboBoxConstructors() {
			if (jComboBoxConstructors == null) {
				jComboBoxConstructors = new JComboBox();
				jComboBoxConstructors.setEditable(false);
				jComboBoxConstructors.setPreferredSize(new Dimension(jComboBoxConstructorsWidth, jComboBoxConstructorsHeight));

				jComboBoxConstructors.getModel().addListDataListener(new ListDataListener() {
					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.ListDataListener#contentsChanged(javax.swing.event.ListDataEvent)
					 */
					public void contentsChanged(ListDataEvent e) {
						Object item = jComboBoxConstructors.getSelectedItem();

						if (item != null)
							jComboBoxConstructors.setToolTipText(item.toString());
					}

					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.ListDataListener#intervalAdded(javax.swing.event.ListDataEvent)
					 */
					public void intervalAdded(ListDataEvent e) {
					}

					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.ListDataListener#intervalRemoved(javax.swing.event.ListDataEvent)
					 */
					public void intervalRemoved(ListDataEvent e) {
					}
				});

				jComboBoxConstructors.addPopupMenuListener(new PopupMenuListener() {
					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.PopupMenuListener#popupMenuCanceled(javax.swing.event.PopupMenuEvent)
					 */
					public void popupMenuCanceled(PopupMenuEvent e) {
					}

					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent)
					 */
					public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
					}

					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent)
					 */
					public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
						lastSelectedConstructor = jComboBoxConstructors.getSelectedItem();
					}				
				});
			}
			return jComboBoxConstructors;
		}
		
		/**
		 * <p>This method initializes <code>jComboBoxItemsOrder</code>.</p>
		 * 	
		 * @return javax.swing.JComboBox	
		 */
		private JComboBox getJComboBoxItemsOrder() {
			if (jComboBoxItemsOrder == null) {
				jComboBoxItemsOrder = new JComboBox();
				jComboBoxItemsOrder.setEditable(false);
				jComboBoxItemsOrder.setPreferredSize(new Dimension(jComboBoxWidth, jComboBoxHeight));

				jComboBoxItemsOrder.getModel().addListDataListener(new ListDataListener() {
					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.ListDataListener#contentsChanged(javax.swing.event.ListDataEvent)
					 */
					public void contentsChanged(ListDataEvent e) {
						Object item = jComboBoxItemsOrder.getSelectedItem();

						if (item != null)
							jComboBoxItemsOrder.setToolTipText(item.toString());
					}

					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.ListDataListener#intervalAdded(javax.swing.event.ListDataEvent)
					 */
					public void intervalAdded(ListDataEvent e) {
					}

					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.ListDataListener#intervalRemoved(javax.swing.event.ListDataEvent)
					 */
					public void intervalRemoved(ListDataEvent e) {
					}
				});

				jComboBoxItemsOrder.addItem(Messages.getText("maintain_position"));
				jComboBoxItemsOrder.addItem(Messages.getText("alphabetical_ordered"));
				jComboBoxItemsOrder.addItem(Messages.getText("disordered"));
			}

			return jComboBoxItemsOrder;
		}
		
		/**
		 * <p>This method initializes <code>jComboBoxLanguageRules</code>.</p>
		 * 	
		 * @return javax.swing.JComboBox	
		 */
		private JComboBox getJComboBoxLanguageRules() {
			if (jComboBoxLanguageRules == null) {
				jComboBoxLanguageRules = new JComboBox();
				jComboBoxLanguageRules.setEditable(false);
				jComboBoxLanguageRules.setPreferredSize(new Dimension(jComboBoxWidth, jComboBoxHeight));

				jComboBoxLanguageRules.getModel().addListDataListener(new ListDataListener() {
					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.ListDataListener#contentsChanged(javax.swing.event.ListDataEvent)
					 */
					public void contentsChanged(ListDataEvent e) {
						Object item = jComboBoxLanguageRules.getSelectedItem();

						if (item != null)
							jComboBoxLanguageRules.setToolTipText(item.toString());
					}

					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.ListDataListener#intervalAdded(javax.swing.event.ListDataEvent)
					 */
					public void intervalAdded(ListDataEvent e) {
					}

					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.ListDataListener#intervalRemoved(javax.swing.event.ListDataEvent)
					 */
					public void intervalRemoved(ListDataEvent e) {
					}
				});

				jComboBoxLanguageRules.addItem("es_ES");
				jComboBoxLanguageRules.addItem("en_US");
				jComboBoxLanguageRules.addItem("fr_FR");
			}

			return jComboBoxLanguageRules;
		}
		

		/**
		 * <p>This method initializes <code>jComboBoxCellRenderer</code>.</p>
		 * 	
		 * @return javax.swing.JCheckBox	
		 */
		private JComboBox getJComboBoxCellRenderer() {
			if (jComboBoxCellRenderer == null) {
				jComboBoxCellRenderer = new JComboBox();
				jComboBoxCellRenderer.setEditable(false);
				jComboBoxCellRenderer.setPreferredSize(new Dimension(jComboBoxWidth, jComboBoxHeight));

				jComboBoxCellRenderer.getModel().addListDataListener(new ListDataListener() {
					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.ListDataListener#contentsChanged(javax.swing.event.ListDataEvent)
					 */
					public void contentsChanged(ListDataEvent e) {
						Object item = jComboBoxCellRenderer.getSelectedItem();

						if (item != null)
							jComboBoxCellRenderer.setToolTipText(item.toString());
					}

					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.ListDataListener#intervalAdded(javax.swing.event.ListDataEvent)
					 */
					public void intervalAdded(ListDataEvent e) {
					}

					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.ListDataListener#intervalRemoved(javax.swing.event.ListDataEvent)
					 */
					public void intervalRemoved(ListDataEvent e) {
					}
				});

				jComboBoxCellRenderer.addItem("Default");
				jComboBoxCellRenderer.addItem("Sample_BG_Color");
			}

			return jComboBoxCellRenderer;
		}
		
		/**
		 * <p>Adds a constructor item to <code>jComboBoxConstructors</code>, and if it`s needed configures the configuration options of this JPanel.</p>
		 *  
		 * @param name Constructor name
		 */
		public void addConstructorItem(String name) {
			this.jComboBoxConstructors.addItem(name);
		
			// Displays description of the first item added by default
			if (this.lastSelectedConstructor == null)
			{
				this.lastSelectedConstructor = getJComboBoxConstructors().getSelectedItem();
			}
		}
	
		/**
		 * @see JComboBox#getSelectedIndex()
		 */
		public int getSelectedComboBoxItemsOrder() {
			return jComboBoxItemsOrder.getSelectedIndex();
		}

		/**
		 * @see JComboBox#setSelectedIndex(int)
		 */
		public void setSelectedComboBoxItemsOrder(int index) {
			jComboBoxItemsOrder.setSelectedIndex(index);
		}

		/**
		 * @see JComboBox#getSelectedIndex()
		 */
		public int getSelectedComboBoxLanguageRules() {
			return jComboBoxLanguageRules.getSelectedIndex();
		}
		
		/**
		 * @see JComboBox#setSelectedIndex(int)
		 */
		public void setSelectedComboBoxLanguageRules(int index) {
			jComboBoxLanguageRules.setSelectedIndex(index);
		}

		/**
		 * <p>Gets the selected cell renderer.</p>
		 * 
		 * @return the selected cell renderer index
		 */
		public int getSelectedComboBoxCellRendererIndex() {
			return jComboBoxCellRenderer.getSelectedIndex();
		}

		/**
		 * @see JComboBox#getSelectedIndex()
		 */
		public int getSelectedComboBoxConstructor() {
			return jComboBoxConstructors.getSelectedIndex();
		}

		/**
		 * <p>Gets an string that identifies the selected items order.</p>
		 * 
		 * @return text that identifies the selected items order
		 */
		public String getSelectedComboBoxItemsOrderText() {
			return jComboBoxItemsOrder.getSelectedItem().toString();
		}

		/**
		 * <p>Gets an string that identifies the selected language rule.</p>
		 * 
		 * @return text that identifies the selected language rule
		 */
		public String getSelectedComboBoxLanguageRuleText() {
			return jComboBoxLanguageRules.getSelectedItem().toString();
		}
		
		/**
		 * <p>Gets an string that identifies the selected cell renderer.</p>
		 * 
		 * @return text that identifies the selected cell renderer
		 */
		public String getSelectedComboBoxCellRenderer() {
			return jComboBoxCellRenderer.getSelectedItem().toString();
		}

		/**
		 * <p>This method initializes <code>jButtonNew</code>.</p>
		 * 	
		 * @return javax.swing.JButton	
		 */
		private JButton getJButtonNew() {
			if (jButtonNew == null) {
				jButtonNew = new JButton();
				jButtonNew.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
				jButtonNew.setText(Messages.getText("new"));
			}
			return jButtonNew;
		}
		
		/**
		 * <p>This method initializes <code>jButtonEnableParametersConfiguration</code>.</p>
		 * 	
		 * @return javax.swing.JButton	
		 */
		private JButton getJButtonEnableParametersConfiguration() {
			if (jButtonEnableParametersConfiguration == null) {
				jButtonEnableParametersConfiguration = new JButton();
				jButtonEnableParametersConfiguration.setPreferredSize(new Dimension(subPanelWidth, buttonHeight));
				jButtonEnableParametersConfiguration.setText(Messages.getText("configure"));
				
				jButtonEnableParametersConfiguration.addMouseListener(new MouseAdapter() {
					/*
					 * (non-Javadoc)
					 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
					 */
					public void mouseClicked(MouseEvent e) {
						if (getJComboBoxConstructors().getItemCount() == 0) {
							JOptionPane.showMessageDialog(null, Messages.getText("cant_configure_parameters"), Messages.getText("warning"), JOptionPane.WARNING_MESSAGE);
							return;
						}
						
						isConfiguring = !isConfiguring;

						enableAllConfigurationComponents(isConfiguring);
					}
				});
			}
			return jButtonEnableParametersConfiguration;
		}

		/**
		 * <p>Sets all configuration components enabled or disabled according the value of the parameter.</p>
		 * 
		 * @param b a boolean value
		 */
		private void enableAllConfigurationComponents(boolean b) {
			getJLabelOnlyOneColorOnText().setEnabled(b);
			getJCheckBoxOnlyOneColorOnText().setEnabled(b);

			getJLabelBeepEnabled().setEnabled(b);
			getJCheckBoxBeepEnabled().setEnabled(b);

			getJLabelHidePopupIfThereAreNoItems().setEnabled(b);
			getJCheckBoxHidePopupIfThereAreNoItems().setEnabled(b);

			getJLabelToForceSelectAnItem().setEnabled(b);
			getJCheckBoxToForceSelectAnItem().setEnabled(b);

			getJLabelCompleteArrowKeySelection().setEnabled(b);
			getJCheckBoxCompleteArrowKeySelection().setEnabled(b);

			getJLabelDisplayAllItemsWithArrowButton().setEnabled(b);
			getJCheckBoxDisplayAllItemsWithArrowButton().setEnabled(b);

			getJLabelCellRenderer().setEnabled(b);
			getJComboBoxCellRenderer().setEnabled(b);
			
			getJLabelItemsOrder().setEnabled(b);
			getJComboBoxItemsOrder().setEnabled(b);

			getJLabelShowAllItemsInListBox().setEnabled(b);
			getJCheckBoxShowAllItemsInListBox().setEnabled(b);

			getJLabelLanguageRules().setEnabled(b);
			getJComboBoxLanguageRules().setEnabled(b);

			getJLabelCaseSensitive().setEnabled(b);
			getJCheckBoxCaseSensitive().setEnabled(b);
		}

		/**
		 * <p>Reset the configuration options of this pane to their default value.</p>
		 */
		private void resetConfigurationPanel() {
			isConfiguring = false;
			
			// JComboBoxConstructors (The constructor selected)
			if (getJComboBoxConstructors().getItemCount() > 0)
				getJComboBoxConstructors().setSelectedIndex(0);
			else
				getJComboBoxConstructors().setSelectedIndex(-1);

			
			// Disable all options
			enableAllConfigurationComponents(false);
			
			// Only One Color
			getJCheckBoxOnlyOneColorOnText().setSelected(JComboBoxConfigurableLookUp.DEFAULT_ONLY_ONE_COLOR_ON_TEXT_CONFIGURATION);
			
			// Beep Enabled
			getJCheckBoxBeepEnabled().setSelected(JComboBoxConfigurableLookUp.DEFAULT_BEEP_ENABLED_CONFIGURATION);

			// Hide Popup If There Are No Items
			getJCheckBoxHidePopupIfThereAreNoItems().setSelected(JComboBoxConfigurableLookUp.DEFAULT_HIDE_POPUP_IF_THERE_ARE_NO_ITEMS_CONFIGURATION);
			
			// To Force Select An Item
			getJCheckBoxToForceSelectAnItem().setSelected(JComboBoxConfigurableLookUp.DEFAULT_TO_FORCE_SELECT_AN_ITEM_CONFIGURATION);
			
			// Complete Arrow Key Selection
			getJCheckBoxCompleteArrowKeySelection().setSelected(JComboBoxConfigurableLookUp.DEFAULT_COMPLETE_ARROW_KEY_SELECTION_CONFIGURATION);
			
			// Display All Items With Arrow Button
			getJCheckBoxDisplayAllItemsWithArrowButton().setSelected(JComboBoxConfigurableLookUp.DEFAULT_DISPLAY_ALL_ITEMS_WITH_ARROW_BUTTON_CONFIGURATION);
			
			// Cell Renderer -> the default (that's a the position 0)
			getJComboBoxCellRenderer().setSelectedIndex(DEFAULT_CELL_RENDERER);

			// Items order
			getJComboBoxItemsOrder().setSelectedIndex(DefaultComboBoxConfigurableLookUpModel.DEFAULT_ITEMS_ORDER_CONFIGURATION);
			
			// Show all items in list box
			getJCheckBoxShowAllItemsInListBox().setSelected(DefaultComboBoxConfigurableLookUpModel.DEFAULT_SHOW_ALL_ITEMS_IN_LIST_BOX_CONFIGURATION);
			
			// Language rules
			getJComboBoxLanguageRules().setSelectedIndex(0); // es_ES

			// Case Sensitive
			getJCheckBoxCaseSensitive().setSelected(DefaultComboBoxConfigurableLookUpModel.DEFAULT_CASE_SENSITIVE_CONFIGURATION);
		}	
	}

	
	/**
	 * <p>A <code>JPanel</code> for the configuration and create a new JComboBoxLookUpConfigurable for testing.</p>
	 *  
	 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
	 */
	private class OtherTestsJPanel extends JPanel implements Serializable {
		private static final long serialVersionUID = 402109040183526786L;

		private final int panelWidth = 410;
		private final int panelHeight = 570;
		private final int buttonWidth = 90;
		private final int buttonHeight = 20;
		private final int methodsPanelWidth = 390;
		private final int methodsPanelHeight = 60;
		private final int otherUtilitiesJTabbedPaneWidth = methodsPanelWidth;
		private final int otherUtilitiesJTabbedPaneHeight = 435;
		private final int columnsOfJTextAreaDescriptionInstructions = 34;
		private final int rowsOfJTextAreaDescriptionInstructions = 22;
		private final int jScrollPaneIncrementConstant = 10;
		private final int jLabelWidth = 70;
		private final int jLabelHeight = buttonHeight;
		private final int parametersTextFieldWidth = methodsPanelWidth - jLabelWidth - 25; // -25 for the margins
		private final int parametersTextFieldHeight = 20;
		private final int buttonsPanelHeight = 30;
		private final int buttonsPanelWidth = methodsPanelWidth;
		private final int jComboBoxWidth = 180;
		private final int jComboBoxHeigth = 20;
		private JComboBox jComboBoxMethods = null;
		private JTextField jTextField = null;
		private JButton jButtonTest = null;
		private JLabel jLabelMethod = null;
		private JPanel jPanelMethods = null;
		private JLabel jLabelComponents = null;
		private JLabel jLabelParameters = null;
		private JComboBox jComboBoxTestObjects = null;
		private JTextArea jTextAreaDescriptionInstructions = null;
		private JButton jButtonReset = null;
		private JPanel jPanelButtons = null;
		private Vector<Object> methods = null;
		private Object lastSelectedMethod = null;
		private Vector<Object> currentMethodTypes = null;
		private JScrollPane jScrollPaneDescriptionInstructions = null;
		String textHelp;
		private String globalHelp;
		private Map<Object, Object> testObjects;
		private JButton jButtonFill = null;
		private JButton jButtonOtherTestsHelp = null;
		private JTabbedPane jTabbedPaneOtherUtilities = null;
		private JScrollPane jScrollPaneLog = null;
		private JTextArea jTextAreaLog = null;

		/**
		 * <p>Default constructor without parameters.</p>
		 */
		public OtherTestsJPanel() {
			super();
		}
		
		/**
		 * <p>Default constructor with 1 parameter.</p>
		 */
		public OtherTestsJPanel(String path) {
			super();
			javaFileMethodsLoader = new JavaFileMethodsLoader(path);
			this.initialize();
		}
		
		/**
		 * <p>Initializes this pane.</p>
		 */
		private void initialize() {
			this.setPreferredSize(new Dimension(panelWidth, panelHeight));
			this.setBorder(BorderFactory.createTitledBorder(null, Messages.getText("proofs"), TitledBorder.LEFT, TitledBorder.TOP, new Font("Dialog", Font.BOLD, 12), Color.black));
			this.setSize(new Dimension(panelWidth, panelHeight));
			this.testObjects = new HashMap<Object, Object>();
			
			this.add(getJPanelMethods(), null);
			this.add(getJTabbedPaneOtherUtilities(), null);
			this.add(getJPaneButtons(), null);
			textHelp = new String("AYUDA CON LOS PARÁMETROS:\n\n" +									
										"Se permiten 5 tipos de parámetros:\n" +
										"   - Entero (int) (Ejemplo: 24)\n" +
										"   - Real corto (float) (Ejemplo: 2.5)\n" +
										"   - Real largo (double) (Ejemplo: 2.56869479346)\n" +
										"   - Booleano (boolean) (Ejemplo valor true: T) (Ejemplo valor false: F)\n" +
										"   - Cadena de caracteres (String) (Se deben usar comillas dobles) (Ejemplo: \"La casa.\")\n" +
										"   - Carácter (char) (Se deben usar comillas simples) (Ejemplo: 'a')\n\n" +
										"Los parámetros se deben escribir separados por coma:\n" +
										"   Ejemplo: 2, 3.5, true, \"El coche\", 'c'\n");

			globalHelp = new String("   AYUDA CON LOS PARÁMETROS:\n" +
									"   ==========================\n" +									
									"   Se permiten 5 tipos de parámetros:\n" +
									"      - Entero (int) (Ejemplo: 24)\n" +
									"      - Real corto (float) (Ejemplo: 2.5)\n" +
									"      - Real largo (double) (Ejemplo: 2.56869479346)\n" +
									"      - Booleano/Bandera (boolean) (Ejemplo valor cierto: true) (Ejemplo valor falso: false)\n" +
									"      - Cadena de caracteres (String) (Se deben usar comillas dobles) (Ejemplo: \"La casa.\")\n" +
									"      - Carácter (char) (Se deben usar comillas simples) (Ejemplo: 'a')\n" +
									"      - Objeto (Object) (Se considerará como un item a añadir o quitar a un JComboBox; se\n" +
									"           escribirá como si fuese una cadena de caracteres (String)) (Ejemplo: \"El reloj\"\n\n" +									
									"   Los parámetros se deben escribir separados por coma:\n" +
									"      Ejemplo: 2, 3.5, true, \"El coche\", 'c', \"El reloj\"\n\n\n" +
									"   AYUDA PANEL \"OTRAS PRUEBAS\" :\n" +
									"   =============================\n" +
									"   + JComboBox \"Operación\" :\n" +
									"      Puede seleccionar la operación que desee realizar.\n\n" +
									"   + JComboBox \"Objeto\" :\n" +
									"      Puede seleccionar el objeto sobre el que realizar\n" +
									"   la operación seleccionada.\n\n" +
									"   + Pestaña \"Descripción\" :\n" +
									"   - Área de texto \"Descripción\" :\n" +
									"      Muestra información de la operación seleccionada y\n" +
									"   puede mostrar esta información de ayuda acerca del\n" +
									"   manejo de esta aplicación\n\n" +
									"   - Campo de texto \"Parámetros\" :\n" +
									"      Permite introducir parámetros para probar la operación\n" +
									"   seleccionada sobre el objeto seleccionado. Se debe introducir\n" +
									"   parámetros del mismo tipo que los necesarios por la operación,\n" +
									"   y el mismo número. En caso de que la operación no necesite\n" +
									"   parámetros de entrada, este campo estará deshabilitado. Y en\n" +
									"   en caso que no el usuario no pueda introducir un tipo de\n" +
									"   parámetro (tipo distinto a los soportados), en caso de poderse\n" +
									"   realizar la operación, se usarán valores por defecto desde la\n" +
									"   aplicación (y no escribir el parámetro).\n\n" +	
									"   - Botón \"Rellenar\" :\n" +
									"      Añade un conjunto de items por defecto al objeto actual,\n" +
									"   permite así al usuario probar el objeto sin tener que introducir\n" +
									"   datos manualmente.\n\n" +
									"   - Botón \"Reset\" :\n" +
									"      Pulsándolo deja el panel \"Otras Pruebas\" tal y como\n" +
									"   estaba inicialmente.\n\n" +
									"   - Botón \"Test\" :\n" +
									"      Pulsándolo se intenta ejecutar un test de la operación\n" +
									"   seleccionada sobre el objeto seleccionado, y usando los\n" +
									"   parámetros introducidos en caso necesario.\n" +
									"      El test puede no ejecutarse si algo de lo anterior no es\n" +
									"   correcto.\n\n" +
									"   + Pestaña \"Log\" :\n" +
									"      Se va mostrando el resultado de las operaciones, así como\n" +
									"   texto de depuración que puedan ir generando los objetos\n" +
									"   objetos según se usen.");

			this.inializeMethods();
		}
		
		/**
		 * <p>Pass a JavaFileMethodsLoader object that has been used for load a the <code>JComboBoxLookUpConfigurable</code> class file.</p>
		 * 
		 * @param methodsLoader A JavaFileMethodsLoader object
		 */
		public void setJavaFileMethodsLoader(JavaFileMethodsLoader methodsLoader) {
			javaFileMethodsLoader = methodsLoader;
			this.initialize();
		}
		
		/**
		 * <p>Add a method to a private Vector with all the methods that this pane will use.</p>
		 */
		private void inializeMethods() {
			methods = new Vector<Object>(0, 1);
			
			/* Create object without parameters */
			MethodsDescription description = new MethodsDescription();
			methods.add(description);
		}

		/**
		 * <p>This method initializes <code>jComboBoxMethods</code>.</p>
		 * 	
		 * @return javax.swing.JComboBox	
		 */
		private JComboBox getJComboBoxMethods() {
			if (jComboBoxMethods == null) {
				jComboBoxMethods = new JComboBox();
				jComboBoxMethods.setPreferredSize(new Dimension(jComboBoxWidth, jComboBoxHeigth));

				jComboBoxMethods.getModel().addListDataListener(new ListDataListener() {
					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.ListDataListener#contentsChanged(javax.swing.event.ListDataEvent)
					 */
					public void contentsChanged(ListDataEvent e) {
						Object item = jComboBoxMethods.getSelectedItem();

						if (item != null)
							jComboBoxMethods.setToolTipText(item.toString());
					}

					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.ListDataListener#intervalAdded(javax.swing.event.ListDataEvent)
					 */
					public void intervalAdded(ListDataEvent e) {
					}

					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.ListDataListener#intervalRemoved(javax.swing.event.ListDataEvent)
					 */
					public void intervalRemoved(ListDataEvent e) {
					}
				});

				jComboBoxMethods.addPopupMenuListener(new PopupMenuListener() {

					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.PopupMenuListener#popupMenuCanceled(javax.swing.event.PopupMenuEvent)
					 */
					public void popupMenuCanceled(PopupMenuEvent e) {
					}

					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent)
					 */
					public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
						// If the user has selected another method -> show its description
						Object item = jComboBoxMethods.getSelectedItem();
						if (item != null)
						{
							if ((lastSelectedMethod == null) || (!lastSelectedMethod.equals(item)))
							{
								displayMethodDescription(item);

								currentMethodTypes = javaFileMethodsLoader.getMethodParametersType(item.toString());

								// Clears the previous parameters
								getJTextField().setText("");

								// Updates the editable status of the field with parameters
								if (currentMethodTypes.size() == 0)
									getJTextField().setEditable(false);
								else
									getJTextField().setEditable(true);
							}
						}
					}

					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent)
					 */
					public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
						lastSelectedMethod = jComboBoxMethods.getSelectedItem();
					}				
				});
			}

			return jComboBoxMethods;
		}
		
		/**
		 * <p>Displays in the <code>JTextAreaDescriptionInstructions</code> information about a method.</p>
		 * 
		 * @param method A method
		 */
		private void displayMethodDescription(Object method) {
			// Adds the description text
			String methodHead = new String("public " + method);
			this.getJTextAreaDescriptionInstructions().setText(javaFileMethodsLoader.getMethodDocumentation(methodHead) + "\n" + method);
			
			// Adjusts the view (jTextAreaDescriptionInstructions) to the jtreeRoot-left corner
			this.getJTextAreaDescriptionInstructions().getCaret().setDot(0);
		}	

		/**
		 * <p>Adds an method item to the <code>jComboBoxMethods</code>.</p>
		 * 
		 * @param name Name of the new method
		 */
		public void addMethodItem(String name) {
			this.jComboBoxMethods.addItem(name);		
			
			// Displays description of the first item added by default
			if (this.lastSelectedMethod == null)
			{
				this.lastSelectedMethod = getJComboBoxMethods().getSelectedItem();
				currentMethodTypes = javaFileMethodsLoader.getMethodParametersType((String)lastSelectedMethod);
				
				if (currentMethodTypes.size() == 0)
					getJTextField().setEditable(false);
				else
					getJTextField().setEditable(true);
				
				displayMethodDescription(name);
			}
		}
		
		/**
		 * <p>Removes a method item from the <code>jComboBoxMethods</code>.</p>
		 * 
		 * @param name Name of a method to remove
		 */
		public void removeMethodItem(String name) {
			this.jComboBoxMethods.removeItem(name);
		}
		
		/**
		 * <p>Removes all method items from the <code>jComboBoxMethods</code>.</p>
		 */
		public void removeAllMethodItems() {
			this.jComboBoxMethods.removeAll();
		}

		/**
		 * <p>This method initializes <code>jTextField</code>.</p>
		 * 	
		 * @return javax.swing.JTextField	
		 */
		private JTextField getJTextField() {
			if (jTextField == null) {
				jTextField = new JTextField();
				jTextField.setPreferredSize(new Dimension(parametersTextFieldWidth, parametersTextFieldHeight));
			}
			return jTextField;
		}

		/**
		 * <p>This method initializes <code>jLabelMethod</code>.</p>
		 * 	
		 * @return javax.swing.JLabel	
		 */
		private JLabel getJLabelMethod() {
			if (jLabelMethod == null) {
				jLabelMethod = new JLabel();
				jLabelMethod.setText(Messages.getText("method"));
			}
			return jLabelMethod;
		}

		/**
		 * <p>This method initializes <code>jLabelParameters</code>.</p>
		 * 	
		 * @return javax.swing.JLabel	
		 */
		private JLabel getJLabelParameters() {
			if (jLabelParameters == null) {
				jLabelParameters = new JLabel();
				jLabelParameters.setPreferredSize(new Dimension(this.jLabelWidth, this.jLabelHeight));
				jLabelParameters.setText(Messages.getText("parameters"));
			}
			return jLabelParameters;
		}
		
		/**
		 * <p>This method initializes <code>jPanelMethods</code>.</p>
		 * 	
		 * @return javax.swing.JPanel	
		 */
		private JPanel getJPanelMethods() {
			if (jPanelMethods == null) {
				jPanelMethods = new JPanel();		
				jPanelMethods.setPreferredSize(new Dimension(methodsPanelWidth, methodsPanelHeight));
				jPanelMethods.setSize(new Dimension(methodsPanelWidth, methodsPanelHeight));
				jPanelMethods.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
				
				GridLayout gridLayout = new GridLayout(2, 2);
				gridLayout.setHgap(4);
				gridLayout.setVgap(4);
				jPanelMethods.setLayout(gridLayout);
				
				jPanelMethods.add(getJLabelMethod());
				jPanelMethods.add(getJLabelComponents());
				jPanelMethods.add(getJComboBoxMethods());
				jPanelMethods.add(getJComboBoxTestObjects());
			}
			return jPanelMethods;
		}
		
		/**
		 * <p>This method initializes <code>jLabelComponents</code>.</p>
		 * 	
		 * @return javax.swing.JLabel	
		 */
		private JLabel getJLabelComponents() {
			if (jLabelComponents == null) {
				jLabelComponents = new JLabel();
				jLabelComponents.setText(Messages.getText("object"));
			}
			return jLabelComponents;
		}

		/**
		 * <p>This method initializes <code>jComboBoxTestObjects</code>.</p>
		 * 	
		 * @return javax.swing.JComboBox	
		 */
		private JComboBox getJComboBoxTestObjects() {
			if (jComboBoxTestObjects == null) {
				jComboBoxTestObjects = new JComboBox();
				jComboBoxTestObjects.setPreferredSize(new Dimension(jComboBoxWidth, jComboBoxHeigth));
				jComboBoxTestObjects.setSize(new Dimension(jComboBoxWidth, jComboBoxHeigth));
				jComboBoxTestObjects.setMaximumSize(new Dimension(jComboBoxWidth, jComboBoxHeigth));

				jComboBoxTestObjects.getModel().addListDataListener(new ListDataListener() {
					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.ListDataListener#contentsChanged(javax.swing.event.ListDataEvent)
					 */
					public void contentsChanged(ListDataEvent e) {
						Object item = jComboBoxTestObjects.getSelectedItem();

						if (item != null)
							jComboBoxTestObjects.setToolTipText(item.toString());
					}

					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.ListDataListener#intervalAdded(javax.swing.event.ListDataEvent)
					 */
					public void intervalAdded(ListDataEvent e) {
					}

					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.ListDataListener#intervalRemoved(javax.swing.event.ListDataEvent)
					 */
					public void intervalRemoved(ListDataEvent e) {
					}
				});
			}
			return jComboBoxTestObjects;
		}
		
		/**
		 * <p>Adds an object for testing.</p>
		 * 
		 * @param key A reference/name of the object
		 * @param object The object
		 */
		public void addTestObject(Object key, Object object) {
			this.testObjects.put(key, object);
			this.jComboBoxTestObjects.addItem(key);
		}
		
		/**
		 * <p>Removes a testing object.</p>
		 * 
		 * @param key A reference/name of the object
		 */
		public void removeTestObject(Object key) {
			this.testObjects.remove(key);
			this.jComboBoxTestObjects.removeItem(key);
		}
		
		/**
		 * <p>Removes all testing objects.</p>
		 */
		public void removeAllTestObjects() {
			this.testObjects.clear();
			this.jComboBoxTestObjects.removeAllItems();
		}
		
		/**
		 * <p>This method initializes <code>jScrollPaneDescriptionInstructions</code>.</p>
		 * 	
		 * @return javax.swing.JScrollPane	
		 */
		private JScrollPane getJScrollPaneDescriptionInstructions() {
			if (jScrollPaneDescriptionInstructions == null) {
				jScrollPaneDescriptionInstructions = new JScrollPane(getJTextAreaDescriptionInstructions());
				Dimension dimension = getJTextAreaDescriptionInstructions().getPreferredSize();
				dimension.height += jScrollPaneIncrementConstant;
				dimension.width += jScrollPaneIncrementConstant;
				jScrollPaneDescriptionInstructions.setPreferredSize(dimension);
			}
			return jScrollPaneDescriptionInstructions;
		}
		
		/**
		 * <p>This method initializes <code>jTextAreaDescriptionInstructions</code>.</p>
		 * 	
		 * @return javax.swing.JTextArea	
		 */
		private JTextArea getJTextAreaDescriptionInstructions() {
			if (jTextAreaDescriptionInstructions == null) {
				jTextAreaDescriptionInstructions = new JTextArea(rowsOfJTextAreaDescriptionInstructions, columnsOfJTextAreaDescriptionInstructions);
				jTextAreaDescriptionInstructions.setEditable(false);
			}
			return jTextAreaDescriptionInstructions;
		}
		
		/**
		 * <p>This method initializes <code>jButtonTest</code>.</p>
		 * 	
		 * @return javax.swing.JButton	
		 */
		private JButton getJButtonTest() {
			if (jButtonTest == null) {
				jButtonTest = new JButton();
				jButtonTest.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
				jButtonTest.setText(Messages.getText("test"));
				jButtonTest.addMouseListener(new MouseAdapter() {
					/*
					 * (non-Javadoc)
					 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
					 */
					public void mouseClicked(MouseEvent arg0) {
						try {
							String methodHead = (String)getJComboBoxMethods().getSelectedItem();
							if (methodHead == null)
							{
								JOptionPane.showMessageDialog(null, Messages.getText("selectAMethod"), Messages.getText("warning"), JOptionPane.WARNING_MESSAGE);
								return;
							}
							
							int selectedTestObjectIndex = getJComboBoxTestObjects().getSelectedIndex();
							if (selectedTestObjectIndex == -1)
							{
								JOptionPane.showMessageDialog(null, Messages.getText("selectAComponent"), Messages.getText("warning"), JOptionPane.WARNING_MESSAGE);
								return;
							}
							
							// If the method selected needs an special treatment
							if (javaFileMethodsLoader.hasTheMethodAnStrangeParameterType(methodHead) || javaFileMethodsLoader.hasTheMethodAnStrangeReturnParameterType(methodHead))
							{
								JOptionPane.showMessageDialog(null, Messages.getText("cantTestThisMethod"), Messages.getText("warning"), JOptionPane.WARNING_MESSAGE);
								return;
							}
							
							Vector<Object> parametersWritten = getParametersWritten();
							if (parametersWritten.size() != currentMethodTypes.size())
							{
								JOptionPane.showMessageDialog(null, Messages.getText("incorrectNumberOfParametersWritten"), Messages.getText("warning"), JOptionPane.WARNING_MESSAGE);
								return;
							}
							
							// Defines the type of parameters for the current selected method
							Class parameterTypes[] = new Class[currentMethodTypes.size()];

							// Create the 'parameters' array
							Object parameters[] = new Object[currentMethodTypes.size()];

							for (int i = 0; i < currentMethodTypes.size(); i++)
							{
								MethodParameter parameter = parametersTypeIsCorrect((String)parametersWritten.get(i), (Integer)currentMethodTypes.get(i));
	
								if (parameter == null)
								{
									JOptionPane.showMessageDialog(null, Messages.getText("incorrectParameter") + ": " + (String)parametersWritten.get(i), Messages.getText("warning"), JOptionPane.WARNING_MESSAGE);
									return;
								}
								
								parameters[i] = parameter.getParameter();
								parameterTypes[i] = parameter.getType();
							}
							
							// Select the "log" tab of the jTabbedPane
							getJTabbedPaneOtherUtilities().setSelectedIndex(1);							
							
							// Gets the frame of the object selected
							String objectName = getJComboBoxTestObjects().getSelectedItem().toString();
							JFrame jTestObjectFrame = ((JFrame)testObjects.get(objectName));
							
							// Sets the focus to the frame of the selected object
							((JFrame)jTestObjectFrame).getRootPane().requestDefaultFocus();
							
							// Gets the selected object for test
							Object object = jTestObjectFrame.getContentPane().getComponent(0);
							
							// Gets the class of the current selected object
							Class testObjectClass = object.getClass();
								
							// Gets the method that corresponds with the selected method
							String methodName = javaFileMethodsLoader.getMethodName(methodHead);
							Method method = testObjectClass.getMethod(methodName, parameterTypes);
							
							// Show information about the next method to do 
							getJTextAreaLog().append(Messages.getText("invoke") +"(" + objectName + ", " + methodName + "):\n");
							
							// Invoke the selected method for the selected object
							Object objectReturned = method.invoke(object, parameters);
							
							// Show the results of the method
							if (objectReturned != null)
								getJTextAreaLog().append(Messages.getText("resultOf") +"(" + objectName + ", " + methodName + "): " + objectReturned.toString() + "\n");
						}
						catch(Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
			return jButtonTest;
		}
		
		/**
		 * <p>This method initializes <code>jTabbedPaneOtherUtilities</code>.</p>	
		 * 	
		 * @return javax.swing.JTabbedPane	
		 */
		private JTabbedPane getJTabbedPaneOtherUtilities() {
			if (jTabbedPaneOtherUtilities == null) {
				jTabbedPaneOtherUtilities = new JTabbedPane();
				jTabbedPaneOtherUtilities.setPreferredSize(new Dimension(otherUtilitiesJTabbedPaneWidth, otherUtilitiesJTabbedPaneHeight));
				
				JPanel panel1 = new JPanel();
				panel1.setPreferredSize(new Dimension(otherUtilitiesJTabbedPaneWidth - 10, 30));
				panel1.add(getJLabelParameters());
				panel1.add(getJTextField());
				
				JPanel tab1Pane = new JPanel();
				tab1Pane.add(getJScrollPaneDescriptionInstructions());
				tab1Pane.add(panel1);
				jTabbedPaneOtherUtilities.addTab(Messages.getText("description"), tab1Pane);
				
				jTabbedPaneOtherUtilities.addTab(Messages.getText("log"), getJScrollPaneLog());
			}
			return jTabbedPaneOtherUtilities;
		}
		
		/**
		 * <p>Verifies if a parameter is of one particular type: if <code>true</code> -> returns a <code>MethodParameter</code> object with
		 * the type codification and the parameter as an object of its type; if <code>false</code> -> returns <code>null</code>.</p>
		 * 
		 * @param parameter A parameter as a name
		 * @param type An Integer object with the codification of the type
		 * @return <code>Null<code> or a <code>MethodParameter<code> object with an object of the same type as the parameter type, and
		 *    the codification of the type.
		 */
		private MethodParameter parametersTypeIsCorrect(String parameter, Integer type) {
			MethodParameter methodParameter = new MethodParameter();
			
			switch (type.intValue()) {
			case JavaFileMethodsLoader.BOOLEAN_TYPE:
				if (parameter.compareTo("true") == 0)
				{
					methodParameter.setParameter(new Boolean(true));
					methodParameter.setType(Boolean.TYPE);				
				}
				else
				{
					if (parameter.compareTo("false") == 0)
					{
						methodParameter.setParameter(new Boolean(false));
						methodParameter.setType(Boolean.TYPE);				
					}
					else
						return null;
				}
				return methodParameter;
			case JavaFileMethodsLoader.CHAR_TYPE:
				if ((parameter.length() == 3) && (parameter.charAt(0) == '\'') && (parameter.charAt(2) == '\''))
				{
					methodParameter.setParameter(new Character(parameter.charAt(1)));
					methodParameter.setType(Character.TYPE);				
				}
				else
					return null;
			case JavaFileMethodsLoader.FLOAT_TYPE:
				try	{
					methodParameter.setParameter(new Float(parameter));
					methodParameter.setType(Float.TYPE);
					return methodParameter;
				}
				catch(Exception e) {
					return null;
				}
			case JavaFileMethodsLoader.DOUBLE_TYPE:
				try	{
					methodParameter.setParameter(new Double(parameter));
					methodParameter.setType(Double.TYPE);
					return methodParameter;
				}
				catch(Exception e) {
					return null;
				}
			case JavaFileMethodsLoader.INT_TYPE:
				try	{
					methodParameter.setParameter(new Integer(parameter));
					methodParameter.setType(Integer.TYPE);
					return methodParameter;
				}
				catch(Exception e) {
					return null;
				}
			case JavaFileMethodsLoader.STRING_TYPE:
				// If the string is between "" simbols:
				if ((parameter.charAt(0) == '\"') && (parameter.charAt(parameter.length() - 1) == '\"'))
				{
					methodParameter.setParameter(new String(parameter.substring(1, parameter.length() -1)));
					methodParameter.setType(String.class);
					return methodParameter;
				}
				else
					return null;
			case JavaFileMethodsLoader.OBJECT_TYPE:
				// If the string written is between "" simbols:
				if ((parameter.charAt(0) == '\"') && (parameter.charAt(parameter.length() - 1) == '\"'))
				{
					methodParameter.setParameter(parameter.substring(1, parameter.length() -1));
					methodParameter.setType(Object.class);
					return methodParameter;
				}
				else
					return null;
			default: // JavaFileMethodsLoader.OTHER_TYPE			
				return null;
			}
		}
		
		/**
		 * <p>Return a Vector with the parameters written as <code>String</code>.</p>
		 * 
		 * @return A Vector with the parameters written
		 */
		private Vector<Object> getParametersWritten() {
			Vector<Object> parameters = new Vector<Object>(0, 1);
			
			String parametersWritten = getJTextField().getText().trim();
			
			// If there is only one parameter
			if (parametersWritten.indexOf(',') == -1)
			{
				if (parametersWritten.length() > 0)
					parameters.add(parametersWritten);
				else
					return parameters;
			}
			else
			{
			StringTokenizer tokens = new StringTokenizer(parametersWritten, ",");

				while(tokens.hasMoreTokens())
					parameters.add(tokens.nextToken().trim());
			}
			
			return parameters;
		}
		
		/**
		 * <p>This method initializes <code>jButtonReset</code>.</p>
		 * 	
		 * @return javax.swing.JButton	
		 */
		private JButton getJButtonReset() {
			if (jButtonReset == null) {
				jButtonReset = new JButton();
				jButtonReset.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
				jButtonReset.setText(Messages.getText("reset"));
				jButtonReset.addMouseListener(new MouseAdapter() {
					/*
					 * (non-Javadoc)
					 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
					 */
					public void mouseClicked(MouseEvent arg0) {
						resetOtherTestsPanel();
					}
				});
			}
			return jButtonReset;
		}

		/**
		 * <p>Resets this <code>JPanel</code>: all will be similar as at beginning except the log, that won't be reset.</p>
		 */
		private void resetOtherTestsPanel() {
			getJTextAreaDescriptionInstructions().setText("");
			
			getJTextField().setText("");
			
			if (getJComboBoxMethods().getItemCount() == 0)
			{
				getJComboBoxMethods().setSelectedIndex(-1);
				this.lastSelectedMethod = this.currentMethodTypes = null;
				getJTextField().setEditable(false);
			}
			else
			{
				this.getJComboBoxMethods().setSelectedIndex(0);
				this.lastSelectedMethod = getJComboBoxMethods().getSelectedItem();
				currentMethodTypes = javaFileMethodsLoader.getMethodParametersType((String)lastSelectedMethod);
				
				if (currentMethodTypes.size() == 0)
					getJTextField().setEditable(false);
				else
					getJTextField().setEditable(true);
				
				displayMethodDescription(this.lastSelectedMethod);
			}

			// Removes all test objects (closing the JFrames that cointain them)
			Object[] objects = testObjects.keySet().toArray();
			for (int i = 0; i < objects.length; i++)
			{
				((JFrame)testObjects.get(objects[i])).dispose();
				((JFrame)testObjects.get(objects[i])).setVisible(false); // For complete de JFrame close method (it's necessary)
			}

			// Set selected the firt tab
			getJTabbedPaneOtherUtilities().setSelectedIndex(0);
		}
		
		/**
		 * <p>This method initializes <code>jPaneButtons</code>.</p>
		 * 	
		 * @return javax.swing.JPanel	
		 */
		private JPanel getJPaneButtons() {
			if (jPanelButtons == null) {
				FlowLayout flowLayout = new FlowLayout();
				flowLayout.setAlignment(FlowLayout.RIGHT);
				jPanelButtons = new JPanel();
				jPanelButtons.setPreferredSize(new Dimension(buttonsPanelWidth, buttonsPanelHeight));
				jPanelButtons.setLayout(flowLayout);
				jPanelButtons.add(getJButtonOtherTestsHelp(), null);
				jPanelButtons.add(getJButtonFill(), null);
				jPanelButtons.add(getJButtonReset(), null);
				jPanelButtons.add(getJButtonTest(), null);
			}
			return jPanelButtons;
		}

		/**
		 * <p>Returns the number of testing objects that this panel has in this moments.</p>
		 * 
		 * @return Number of testing objects that this panel has in this moments
		 */
		public int getTestObjectsCount() {
			return this.getJComboBoxTestObjects().getItemCount();
		}

		/**
		 * <p>This method initializes <code>jButtonLog</code>.</p>
		 * 	
		 * @return javax.swing.JButton	
		 */
		private JButton getJButtonFill() {
			if (jButtonFill == null) {
				jButtonFill = new JButton();
				jButtonFill.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
				jButtonFill.setText(Messages.getText("fill"));
				jButtonFill.addMouseListener(new MouseAdapter() {
					/*
					 * (non-Javadoc)
					 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
					 */
					public void mouseClicked(MouseEvent arg0) {
						try
						{
							// It must be selected a component -> else: show a message dialog and don't fill with data the object
							int selectedTestObjectIndex = getJComboBoxTestObjects().getSelectedIndex();
							if (selectedTestObjectIndex == -1)
							{
								JOptionPane.showMessageDialog(null, Messages.getText("selectAComponent"), Messages.getText("warning"), JOptionPane.WARNING_MESSAGE);
								return;
							}							
							
							JFrame jTestObjectFrame = ((JFrame)testObjects.get(getJComboBoxTestObjects().getSelectedItem().toString()));
							
							// Sets the focus to the frame of the selected object
							((JFrame)jTestObjectFrame).getRootPane().requestDefaultFocus();
							
							// Gets the selected object for test
							Object object = jTestObjectFrame.getContentPane().getComponent(0);
							
							// Gets the class of the current selected object
							Class testObjectClass = object.getClass();

							// Defines the type of parameter for the 'addItem' method
							Class addItemParameterTypes[] = new Class[1];
							addItemParameterTypes[0] = Object.class;

							// Gets the 'addItem' method of the current selected object
							Method addItemMethod = testObjectClass.getMethod("addItem", addItemParameterTypes);
							
							// ADDS SAMPLE DATA:
							addSampleData(object, addItemMethod);						
						}
						catch(Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
			
			return jButtonFill;
		}
		
		/**
		 * <p>This method add sample data (items) to the current selected object invoking the "addItem" method for the object.</p>
		 * 
		 * @param object Object where the items will be added
		 * @param addItemMethod Method "addItem" to invoke
		 */
		private void addSampleData(Object object, Method addItemMethod) {
			try {
				// Create the 'parameters' array
				Object parameters[] = new Object[1];
				
				// Invokes the 'addItem' method of the object for adding sample items
				parameters[0] = "_fwAndami";		
				addItemMethod.invoke(object, parameters);
				
				parameters[0] = "appgvSIG";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "extAddEventTheme";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "extAddIDEELayers";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "extCAD";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "extCenterViewToPoint";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "extJDBC";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "extNomenclatorIGN";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "extScripting";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "extWCS";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "extWMS";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "libCorePlugin";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "libCq CMS for java";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "libDriverManager";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "libFMap";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "libGDBMS";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "libIverUtiles";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "libNomeclatorIGN";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "libNomenclatorIGN_GUI";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "libRemoteServices";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "libUI";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "Mayúscula1";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "MAYÚSCULA2";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "Mayuscula3";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "mayuscula4?";		
				addItemMethod.invoke(object, parameters);
				
				parameters[0] = "minúscula1";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "minuscula2";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "ñandú";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "ÑANDÚ";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "a";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "aa";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "aaa";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "aaaa";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "aaaaa";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "af¿?=)(/?/";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = ".";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "-a,malj'=)/";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "ZZZZZ";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "BLIUEÑ ";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "etye7yyy-er";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "_abcdefghijklmnñopqrstuvwxyz";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "_0123456789";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "...";		
				addItemMethod.invoke(object, parameters);

				parameters[0] = "gvSIG";		
				addItemMethod.invoke(object, parameters);
				
				parameters[0] = "ÚLTIMO ITEM EN AÑADIRSE POR DEFECTO";		
				addItemMethod.invoke(object, parameters);

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}		
		}

		/**
		 * <p>This method initializes <code>jButtonOtherTestsHelp</code>.</p>
		 * 	
		 * @return javax.swing.JButton	
		 */
		private JButton getJButtonOtherTestsHelp() {
			if (jButtonOtherTestsHelp == null) {
				jButtonOtherTestsHelp = new JButton();
				jButtonOtherTestsHelp.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
				jButtonOtherTestsHelp.setText(Messages.getText("help"));
				jButtonOtherTestsHelp.addMouseListener(new MouseAdapter() {
					/*
					 * (non-Javadoc)
					 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
					 */
					public void mouseClicked(MouseEvent arg0) {
						// Selects the 'description' tab pane
						getJTabbedPaneOtherUtilities().setSelectedIndex(0);
						
						// Sets the help text
						getJTextAreaDescriptionInstructions().setText(globalHelp);
						
						// Adjusts the view (jTextAreaDescriptionInstructions) to the jtreeRoot-left corner
						getJTextAreaDescriptionInstructions().getCaret().setDot(0);
						
						// Reset the current method selected
						lastSelectedMethod = null;
						getJTextField().setEditable(false);
						currentMethodTypes = null;
						getJComboBoxMethods().setSelectedIndex(-1);
					}					
				});

			}
			return jButtonOtherTestsHelp;
		}
		
		/**
		 * <p>This method initializes <code>jScrollPaneLog</code>.</p>
		 * 	
		 * @return javax.swing.JScrollPane	
		 */
		private JScrollPane getJScrollPaneLog() {
			if (jScrollPaneLog == null) {
				jScrollPaneLog = new JScrollPane(getJTextAreaLog());
			}
			return jScrollPaneLog;
		}

		/**
		 * <p>This method initializes <code>jTextAreaLog</code>.</p>
		 * 	
		 * @return javax.swing.JTextArea	
		 */
		private JTextArea getJTextAreaLog() {
			if (jTextAreaLog == null) {
				jTextAreaLog = new JTextArea();
				jTextAreaLog.setEditable(false);
			}
			return jTextAreaLog;
		}
		
		/**
		 * <p>This class has information about a parameter of a method of a class.</p>
		 * 
		 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
		 */
		private class MethodParameter implements Serializable {
			private static final long serialVersionUID = -6492607912912912179L;

			private Object parameter;
			private Class type;
			
			/**
			 * <p>Default constructor without parameters.</p>
			 */
			public MethodParameter() {
				super();
			}
			
			/**
			 * <p>Default constructor with 2 parameters.</p>
			 * 
			 * @param parameter The parameter object
			 * @param type The type of the parameter
			 */
			public MethodParameter(Object parameter, Class type) {
				super();
				this.parameter = parameter;
				this.type = type;
			}
		
			/**
			 * <p>Returns the parameter object.</p>
			 * 
			 * @return Parameter object
			 */
			public Object getParameter() {
				return parameter;
			}
			
			/**
			 * <p>Sets the parameter object.</p>
			 * 
			 * @param parameter Parameter object
			 */
			public void setParameter(Object parameter) {
				this.parameter = parameter;
			}
			
			/**
			 * <p>Returns the type of the parameter.</p>
			 * 
			 * @return Type of the parameter
			 */
			public Class getType() {
				return type;
			}
			
			/**
			 * <p>Sets the type of the parameter.</p>
			 * 
			 * @param type Type of the parameter
			 */
			public void setType(Class type) {
				this.type = type;
			}	
		}
	}
	
	
	/**
	 * <p>This class loads from a java file the methods of the class defined, and has some methods for get or manipulate them
	 * (It's supposed that the file to load has a determinated structure).</p>
	 *  
	 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
	 */
	private class JavaFileMethodsLoader implements Serializable {
		private static final long serialVersionUID = 4934916590524133748L;

		private String className;
		private Map<String, String> methods;
		private Vector<Object> methodHeads;
		private String filePath;
		private File file;
		
		public static final int BOOLEAN_TYPE = 0;
		public static final int STRING_TYPE = 1;
		public static final int INT_TYPE = 2;
		public static final int FLOAT_TYPE = 3;
		public static final int DOUBLE_TYPE = 4;
		public static final int CHAR_TYPE = 5;
		public static final int VOID_TYPE = 6;
		public static final int OBJECT_TYPE = 7;
		public static final int OTHER_TYPE = 8;
		
		/**
		 * <p>Default constructor without parameters.</p>
		 */
		public JavaFileMethodsLoader() {
			methods = new HashMap<String, String>();
			methodHeads = new Vector<Object>(0, 1); // Initial capacity = 0; Increment capacity = 1
		}
		
		/**
		 * <p>Default constructor with a parameter: the path of the file to load.</p>
		 * 
		 * <p>Loads that file.</p>
		 * 
		 * @param path The path for the file to load
		 */
		public JavaFileMethodsLoader(String path) {
			methods = new HashMap<String, String>();
			methodHeads = new Vector<Object>(0, 1); // Initial capacity = 0; Increment capacity = 1

			this.filePath = path;
			this.file = new File(this.filePath);
			this.className = this.obteinClassName();
			this.processFile();				
		}
		
		/**
		 * <p>Obtains the name of the class.</p>
		 * 
		 * @return Name of the main class of the file loaded
		 */
		private String obteinClassName() {
			return file.getName().substring(0, file.getName().lastIndexOf('.'));
		}
		
		/**
		 * <p>Returns the name of the main class of the file loaded.</p>
		 * 
		 * @return Name of the main class of the file loaded
		 */
		public String getClassName() {
			return this.className;
		}
		
		/**
		 * <p>Returns the name of the file loaded.</p>
		 * 
		 * @return Name of the file loaded
		 */
		public String getClassFileName() {
			return this.file.getName();
		}
		
		/**
		 * <p>Returns a method head in order as it was declared/defined in the file loaded.</p>
		 * 
		 * @param position Order of the method in the file, for return its head
		 * @return A method head
		 */
		public String getMethodHead(int position) {
			return (String)methodHeads.get(position);
		}

		/**
		 * <p>Returns the documentation of a method.</p>
		 * 
		 * @param methodHead A method head
		 * @return Documentation of a method
		 */
		public String getMethodDocumentation(String methodHead) {
			return (String)methods.get(methodHead);
		}
		
		/**
		 * <p>Returns only the name from a method head.</p>
		 * 
		 * @param methodHead A method head
		 * @return A name of a method
		 */
		public String getMethodName(String methodHead) {
			int endPosition = methodHead.indexOf('(');

			StringTokenizer tokens = new StringTokenizer(methodHead.substring(0, endPosition), " ");

			int numberOfTokens = tokens.countTokens();
			
			if (numberOfTokens == 0)
				return null;
			else
			{
				// Get the last token -> which is the name of the method
				for (int i = 0; i < (numberOfTokens - 1); i++)
					tokens.nextToken();
				
				return tokens.nextToken();
			}
		}
		
		/**
		 * <p>Returns a vector with the codes of the parameter types of a method from its method head.</p>
		 * 
		 * @param methodHead A method head
		 * @return A vector with the parameter types of a method. Each element in the vector is an Integer object
		 */
		public Vector<Object> getMethodParametersType(String methodHead) {
			Vector<Object> types = new Vector<Object>(0, 1);
			
			int startPosition = methodHead.indexOf('(') + 1;
			int endPosition = methodHead.lastIndexOf(')');
			
			StringTokenizer tokens = new StringTokenizer(methodHead.substring(startPosition, endPosition).trim(), ",");
			
			a: while(tokens.hasMoreTokens()) {
				String token = tokens.nextToken().trim();
				
				if (token.startsWith("boolean "))
				{
					types.add(new Integer(JavaFileMethodsLoader.BOOLEAN_TYPE));
					continue a;
				}
				
				if (token.startsWith("int "))
				{
					types.add(new Integer(JavaFileMethodsLoader.INT_TYPE));
					continue a;
				}
			
				if (token.startsWith("double "))
				{
					types.add(new Integer(JavaFileMethodsLoader.DOUBLE_TYPE));
					continue a;
				}
		
				if (token.startsWith("String "))
				{
					types.add(new Integer(JavaFileMethodsLoader.STRING_TYPE));
					continue a;
				}
	
				if (token.startsWith("char "))
				{
					types.add(new Integer(JavaFileMethodsLoader.CHAR_TYPE));
					continue a;
				}

				if (token.startsWith("float "))
				{
					types.add(new Integer(JavaFileMethodsLoader.FLOAT_TYPE));
					continue a;
				}
				
				if (token.startsWith("Object "))
				{
					types.add(new Integer(JavaFileMethodsLoader.OBJECT_TYPE));
					continue a;
				}
				
				// Other type of parameters
				types.add(new Integer(JavaFileMethodsLoader.OTHER_TYPE));
			}
			
			return types;
		}
		
		/**
		 * <p>Returns the return type name of a method.</p>
		 * 
		 * @param methodHead A method head
		 * @return A return type name
		 */
		public String getMethodReturnTypeName(String methodHead) {
			int endPosition = methodHead.indexOf('(');
			String aux = methodHead.substring(0, endPosition);
			endPosition = aux.lastIndexOf(' ');
			return methodHead.substring(0, endPosition);
		}
		
		/**
		 * <p>Returns the code of the return type of a method from its method head.</p>
		 * 
		 * @param methodHead A method head
		 * @return An Integer object with the code
		 */
		public Integer getMethodReturnType(String methodHead) {
			String returnType = getMethodReturnTypeName(methodHead);
			
			if (returnType.compareTo("boolean") == 0)
				return new Integer(JavaFileMethodsLoader.BOOLEAN_TYPE);
			
			if (returnType.compareTo("int") == 0)
				return new Integer(JavaFileMethodsLoader.INT_TYPE);
		
			if (returnType.compareTo("double") == 0)
				return new Integer(JavaFileMethodsLoader.DOUBLE_TYPE);
	
			if (returnType.compareTo("String") == 0)
				return new Integer(JavaFileMethodsLoader.STRING_TYPE);

			if (returnType.compareTo("char") == 0)
				return new Integer(JavaFileMethodsLoader.CHAR_TYPE);

			if (returnType.compareTo("float") == 0)
				return new Integer(JavaFileMethodsLoader.FLOAT_TYPE);
			
			if (returnType.compareTo("void") == 0)
				return new Integer(JavaFileMethodsLoader.VOID_TYPE);
			
			if (returnType.compareTo("Object") == 0)
				return new Integer(JavaFileMethodsLoader.OBJECT_TYPE);
			
			// Other type of parameters
			return new Integer(JavaFileMethodsLoader.OTHER_TYPE);
			
		}
		
		/**
		 * <p>Returns true if the method has almost one parameter that its type is unkwown <code>(JavaFileMethodsLoader.OTHER_TYPE)</code>.</p>
		 * 
		 * @param methodHead A method head
		 * @return true or false
		 */
		public boolean hasTheMethodAnStrangeParameterType(String methodHead) {
			Vector<Object> types = this.getMethodParametersType(methodHead);
			Integer otherType = new Integer(JavaFileMethodsLoader.OTHER_TYPE);
			
			for (int i = 0; i < types.size(); i++) {
				if (((Integer)types.get(i)).intValue() == otherType.intValue())
					return true;
			}
			
			return false;			
		}
		
		/**
		 * <p>Returns true if the method has a return parameter that its type is unkwown <code>(JavaFileMethodsLoader.OTHER_TYPE)</code>.</p>
		 * 
		 * @param methodHead A method head
		 * @return true or false
		 */
		public boolean hasTheMethodAnStrangeReturnParameterType(String methodHead) {
			Integer otherType = new Integer(JavaFileMethodsLoader.OTHER_TYPE);
			
			if (((Integer)getMethodReturnType(methodHead)).intValue() == otherType.intValue())
				return true;
			
			return false;
		}
		
		/**
		 * <p>Returns the number of methods loaded.</p>
		 * 
		 * @return Number of methods loaded
		 */
		public int size() {
			return methodHeads.size();
		}
		
		/**
		 * <p>Returns the path of the file loaded.</p>
		 * 
		 * @return The path of the file loaded
		 */
		public String getPath() {
			return filePath;
		}

		/**
		 * <p>Sets a path for a file to load, and loads it.</p>
		 * 
		 * @param aPath A path for a file to load
		 */
		public void setPath(String aPath) {
			this.filePath = aPath;
			file = new File(this.filePath);
			this.className = this.obteinClassName();
			this.processFile();
		}		

		/**
		 * <p>Verifies if can read the file and invokes a method for analyze it.</p>
		 */
		private void processFile() {
			try
			{
				// Verify if exists the file
				if (!file.exists())
				{
				     // File doesn't exists
					 JOptionPane.showMessageDialog(null, Messages.getText("jOPMessageFileDoesntExists"), Messages.getText("error"), JOptionPane.ERROR_MESSAGE);
					 return;
				}
				
				// Verify if it's a file
				if (!file.isFile())
				{
					 JOptionPane.showMessageDialog(null, Messages.getText("jOPMessageIncorrectFile"), Messages.getText("error"), JOptionPane.ERROR_MESSAGE);
					 return;
				}
				
				// Verify if it can be read
				if (!file.canRead())
				{
					 JOptionPane.showMessageDialog(null, Messages.getText("jOPMessageFileWithoutReadPermissions"), Messages.getText("error"), JOptionPane.ERROR_MESSAGE);
					 return;
				}
				
				analyzeFile();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		/**
		 * <p>Analyes/loads a file, obtaining its methods and other information.</p>
		 * 
		 * @throws IOException 
		 */
		private void analyzeFile() throws IOException {
			String line, commentary, methodHead;
			int pos;
			
			// Process:
			// Associates a buffered-reader to the file-reader of the file
			BufferedReader input = new BufferedReader(new FileReader(file));

			while(input.ready())
			{
				// Tries to read a commentary
				line = input.readLine();

				if (line.startsWith("\t/*")) // This include the "\t/**" and "\t/*" commentary starts
				{
					commentary = new String(line.trim());
					
					while((pos = (line = input.readLine()).indexOf("*/")) == -1)
						commentary += "\n " + line.trim();
					
					commentary += "\n " + line.substring(0, pos+2).trim();
					
					// Tries to read the head of a method
					line = input.readLine();

					if (line.startsWith("\tpublic ") || line.startsWith("\tprivate ") || line.startsWith("\tprotected "))
					{
						// Ignore inner/nested classes
						if (line.indexOf(" class ") == -1)
						{
							// Removes the '{' character if exists and does a trim:
							if ((pos = line.indexOf("{")) != -1)
								line = line.substring(0, pos);
							
							methodHead = new String(line.trim());
							commentary = commentary.trim();

							// Adds needed information of the new method
							this.methodHeads.add(methodHead);
							this.methods.put(methodHead, commentary);
						}
					}
				}
			}
			
			// Closes the buffered-reader
			input.close();
		}
	}
	
	/**
	 * <p>This class stores for a method, its previous commentary description.</p>
	 * 
	 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
	 */
	private class MethodsDescription implements Serializable {
		private static final long serialVersionUID = 5425689217735155592L;

		private String methodName;
		private String description;

		/**
		 * <p>Default Constructor without parameters.</p>
		 */
		public MethodsDescription() {
			super();
		}

		/**
		 * <p>Default Constructor with 2 parameters.</p>
		 * 
		 * @param methodName Name of a method
		 * @param description Description (commentary) of the method
		 */
		public MethodsDescription(String methodName, String description) {
			super();
			this.methodName = methodName;
			this.description = description;
		}

		/**
		 * <p>Returns the description of this method.</p>
		 * 
		 * @return Description of this method
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * <p>Sets a description for this method.</p>
		 * 
		 * @param description A description for this method
		 */
		public void setDescription(String description) {
			this.description = description;
		}

		/**
		 * <p>Returns the name of this method.</p>
		 * 
		 * @return The name of this method
		 */
		public String getMethodName() {
			return methodName;
		}

		/**
		 * <p>Sets a name of this method.</p>
		 * 
		 * @param methodName A name for this method
		 */
		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}
	}

	/**
	 * <p>An output stream that writes its output to a <code>javax.swing.JTextArea</code> control.</p>
	 * 
	 * @see javax.swing.JTextArea
	 *
	 * @author Ranganath Kini
	 */
	public class TextAreaOutputStream extends OutputStream {
	    private JTextArea textControl;
	    
	    /**
	     * <p>Creates a new instance of TextAreaOutputStream which writes
	     * to the specified instance of <code>javax.swing.JTextArea</code> control.</p>
	     *
	     * @param control   A reference to the javax.swing.JTextArea
	     *                  control to which the output must be redirected
	     *                  to.
	     */
	    public TextAreaOutputStream( JTextArea control ) {
	        textControl = control;
	    }

	    /*
	     * Writes the specified byte as a character to the
	     * javax.swing.JTextArea.
	     * 
	     * @see java.io.OutputStream#write(int)
	     *
	     * @param   b   The byte to be written as character to the
	     *              JTextArea.
	     */
		public void write(int arg0) throws IOException {
	        // append the data as characters to the JTextArea control
	        textControl.append( String.valueOf( ( char )arg0 ) );		
		}
	}
}
