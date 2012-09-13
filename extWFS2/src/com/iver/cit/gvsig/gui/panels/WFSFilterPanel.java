package com.iver.cit.gvsig.gui.panels;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.geotools.filter.DefaultExpression;
import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.filterPanel.filterQueryPanel.FilterQueryJPanel;
import org.gvsig.gui.beans.panelGroup.IPanelGroup;
import org.gvsig.remoteClient.wfs.schema.XMLElement;
import org.gvsig.remoteClient.wfs.schema.type.IXMLType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.gui.filter.ExpressionDataSource;
import com.iver.cit.gvsig.gui.filter.FilterException;
import com.iver.cit.gvsig.gui.panels.fieldstree.FieldsTreeTableModel;
import com.iver.cit.gvsig.gui.panels.model.WFSSelectedFeature;
import com.iver.cit.gvsig.sqlQueryValidation.SQLQueryValidation;
import com.iver.utiles.stringNumberUtilities.StringNumberUtilities;


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
 * <p>Panel that provides tools to apply a filter to the values of a feature.</p>
 * <p>Improves the functionality of {@link FilterQueryJPanel FilterQueryJPanel}.</p>
 *
 * @see FilterQueryJPanel
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class WFSFilterPanel extends FilterQueryJPanel implements IWFSPanel {
	private static final long serialVersionUID = -6041218260822015810L;

	private static Logger logger = LoggerFactory.getLogger(WFSFilterPanel.class);
	private ExpressionDataSource model = null;
	private FieldsTreeTableModel fieldsTreeTableModel;
	private boolean panelAsATabForWFSLayersLoad;
	private TreePath currentPath;
	private String featureName;
	private Map<String, Map<String, String>> allFieldsAndValuesKnownOfCurrentLayer; // This will have all values (not repeated) known of all fields (not repeated)

	///// GUI METHODS ////

	/**
	 * This method initializes
	 *
	 * @param parent A reference to the parent container component of this component
	 */
	public WFSFilterPanel() {
		super();
		currentPath = null;
		featureName = null;
		allFieldsAndValuesKnownOfCurrentLayer = new HashMap<String, Map<String, String>>(); // Initial capacity = 0

		// At beginning, the JList is disabled (and its set a particular color for user could knew it)
		super.getValuesJList().setEnabled(false);
		getValuesJList().setBackground(new Color(220, 220, 220));
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.gui.beans.filterPanel.AbstractFilterQueryJPanel#initialize()
	 */
	protected void initialize() {
		setLabel(PluginServices.getText(this, "filter"));
		setLabelGroup(PluginServices.getText(this, "wfs"));
		super.initialize();
		this.resizeHeight(380);

		defaultTreeModel = (DefaultTreeModel)fieldsJTree.getModel();

		this.addNewListeners();
		panelAsATabForWFSLayersLoad = true;
		getValidateFilterExpressionJCheckBox().setSelected(true);

		getValuesJLabel().setToolTipText(Messages.getText("values_of_the_selected_field_explanation"));
		getFieldsJLabel().setToolTipText(Messages.getText("fields_of_the_selected_feature_explanation"));
	}

	/**
	 * Adds some more listener to the components of the panel
	 */
	private void addNewListeners() {
		// Enable "Apply" button when user changes the filter query
		txtExpression.getDocument().addDocumentListener(new DocumentListener() {
			/*
			 *  (non-Javadoc)
			 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
			 */
			public void changedUpdate(DocumentEvent e) {
			}

			/*
			 *  (non-Javadoc)
			 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
			 */
			public void insertUpdate(DocumentEvent e) {
				if (!panelAsATabForWFSLayersLoad) {
					IPanelGroup panelGroup = getPanelGroup();
					
					if (panelGroup == null)
						return;
					
					((WFSParamsPanel)panelGroup).setApplicable(true);
				}
			}

			/*
			 *  (non-Javadoc)
			 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
			 */
			public void removeUpdate(DocumentEvent e) {
				if (!panelAsATabForWFSLayersLoad) {
					IPanelGroup panelGroup = getPanelGroup();
					
					if (panelGroup == null)
						return;
					
					((WFSParamsPanel)panelGroup).setApplicable(true);
				}
			}
		});

		// Listener for "fieldsJTree"
		getFieldsJTree().addMouseListener(new MouseAdapter() {
			/*
			 *  (non-Javadoc)
			 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
			 */
			public void mouseClicked(MouseEvent e) {
				int row = fieldsJTree.getRowForLocation(e.getX(), e.getY());
				TreePath treePath = fieldsJTree.getPathForLocation(e.getX(), e.getY());

				if (row > -1) {
					switch (e.getClickCount()) {
						case 2:
							putSymbolOfSelectedByMouseBranch(treePath);
							break;
					}
				}
			}
		});

		// Listener for "valuesJList"
		getValuesJList().addMouseListener(new MouseAdapter() {
			/*
			 *  (non-Javadoc)
			 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
			 */
			public void mouseClicked(MouseEvent e) {
				int index = getValuesJList().getSelectedIndex();

				// Avoids exception when no value is in the list
				if (index == -1)
					return;

				if (e.getClickCount() == 2){
					String valor = ((JLabel) valuesListModel.getElementAt(index)).getText();

					// If value is an string -> set it between apostrophes
					if (getNodeOfCurrentPath().getEntityType().getName().compareTo("xs:string") == 0) {
						putSymbol("'" + valor + "'");
					}
					else {
						putSymbol(valor);
					}
				}
			}
		});

		// Listener for a branch of the tree selection
		getFieldsJTree().addTreeSelectionListener(new TreeSelectionListener() {
			/*
			 *  (non-Javadoc)
			 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
			 */
			public void valueChanged(TreeSelectionEvent e) {
				if (!panelAsATabForWFSLayersLoad) {
					DefaultExpression data = DataLoadingFromActiveView.getDefaultExpressionDataSource();

					if (data != null) {
						currentPath = e.getPath();
						fillValuesByPath(currentPath);
					}
				}
			}
		});

		// Listener: when a user writes something on the textarea -> set it's foreground color to black
		getTxtExpression().getDocument().addDocumentListener(new DocumentListener() {
			/*
			 *  (non-Javadoc)
			 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
			 */
			public void changedUpdate(DocumentEvent e) {
			}

			/*
			 *  (non-Javadoc)
			 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
			 */
			public void insertUpdate(DocumentEvent e) {
				getTxtExpression().setForeground(Color.BLACK);
			}

			/*
			 *  (non-Javadoc)
			 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
			 */
			public void removeUpdate(DocumentEvent e) {
				getTxtExpression().setForeground(Color.BLACK);
			}
		});
	}

	/**
	 * Gets the element that the 'currentPath' field aims
	 *
	 * @return An XMLElement
	 */
	private XMLElement getNodeOfCurrentPath() {

		if (currentPath != null) {
			Object node = currentPath.getLastPathComponent();

			if ((node != null) && (node instanceof XMLElement)) {
				return (XMLElement) node;
			}
		}

		return null;
	}

	/**
	 * Puts the symbol of selected branch
	 *
	 * @param mouseEvent A MouseEvent with information  of the selected branch
	 */
	public void putSymbolOfSelectedByMouseBranch(TreePath treePath) {
		// Sets the node selected
		if (treePath != null) {
			putSymbol("\"" + this.getPathOfLeafWithoutRoot(treePath.getLastPathComponent()) + "\"");
		}
	}

	/**
	 * This method returns the path without the root, of a node of a tree
	 * Each node is separated from its parent with the symbol "/"
	 *
	 * @param node A leaf node
	 * @return An string with the path
	 */
	private String getPathOfLeafWithoutRoot(Object node) {
		String path = "";

		if ((node != null) && (node instanceof XMLElement)) {
			XMLElement element = (XMLElement) node;
			XMLElement parent = element.getParentElement();
			path = element.getName();

			while (parent.getParentElement() != null){
				path = parent.getName() + "/" + path;
				parent = parent.getParentElement();
			}
		}

		return path;
	}

	/**
	 * Gets the value of the inner attribute: 'panelAsATabForWFSLayersLoad'
	 *
	 * @return A boolean value
	 */
	public boolean getWFSFilterPanelIsAsTabForWFSLayersLoad() {
		return this.panelAsATabForWFSLayersLoad;
	}

	/**
	 * Sets the value of the inner attribute: 'panelAsATabForWFSLayersLoad'
	 *
	 * @param b A boolean value
	 */
	public void setWFSFilterPanelIsAsTabForWFSLayersLoad (boolean b) {
//		this.panelAsATabForWFSLayersLoad = b;
//
//		if (this.panelAsATabForWFSLayersLoad == true) {
//			// At beginning, the JList is disabled (and its set a particular color for user could knew it)
//			super.getValuesJList().setEnabled(false);
//			super.getValuesJList().setBackground(new Color(220, 220, 220)); // a grey color
//
//			this.allFieldsAndValuesKnownOfCurrentLayer.clear();
//			getValidateFilterExpressionJCheckBox().setSelected(true);
//		}
//		else {
//			// Unselect the selected path in the tree (if there was any selected)
//			if (this.currentPath != null) {
//				this.currentPath = null;
//				this.getFieldsJTree().removeSelectionPath(this.getFieldsJTree().getSelectionPath());
//			}
//
//			// Resets data loaded
//			super.getValuesJList().setEnabled(true);
//			super.getValuesJList().setBackground(Color.WHITE);
//
//			this.getFieldsJTree().removeAll();
//			this.getValuesJList().removeAll();
//
//			// Updates data associated to view with the new layer data
//			this.setNewDataToTable();
//
//			// Reads that new data
//			DefaultExpression data = DataLoadingFromActiveView.getDefaultExpressionDataSource();
//
//			if (data != null) {
//				setModel(data);
//			}
//
//			// Loads values known of fields
//			this.setValuesKnownOfFields();
//		}
	}

	/**
	 * Refresh all information about fields
	 *
	 * @param feature a feature with fields
	 */
	public void refresh(WFSSelectedFeature feature) {
		featureName = feature.getTitle();
		setFields(feature);	
		setFilterExpressionIntoInterface(feature.getFilter());
	}

	///// END GUI METHODS /////

	///// METHODS FOR THE FILTER QUERY /////

	/**
	 * Gets the query that will be send to the server
	 * @return SQL query (just the where part)
	 */
	public String getQuery(){
		String writtenQuery = txtExpression.getText().trim();

		// Validate expression
		if (!this.validateExpression(writtenQuery)) {
			getTxtExpression().setForeground(Color.red);
			return null;
		}
		else {
			logger.debug("Codified WFS filter query: " + writtenQuery);

			// Codify expression (only if the validation has been successful)
			return this.codifyExpression(writtenQuery); // Ignores the spaces at beginning and end of the chain of characters
		}
	}

	/**
	 * Gets the filter expression from the user interface
	 */
	public String getFilterExpressionFromInterface() {
		return getTxtExpression().getText();
	}

	/**
	 * Writes the filter expression into the user interface
	 * @param filterExpression An string
	 */
	public void setFilterExpressionIntoInterface(String filterExpression){
		getTxtExpression().setText(filterExpression);
	}

	/**
	 * Removes text in the JTextArea that has the filter subconsultation
	 */
	public void removeFilterExpression() {
		getTxtExpression().setText("");
	}

	/**
	 * Codifies the expression to ISO ISO-8859_1 and a format that the SQL parser could validate
	 *
	 * @param expression The expression to be codified
	 *
	 * @return The expression codified
	 */
	private String codifyExpression(String expression) {
		String result = new String("");

		// Encode each string of the query
		int index = 0;
		int lastIndex = 0;
		boolean endInnerLoop;

		// Encodes all inner strings to the equivalent codification in ISO-8859_1 with each ' symbol converted to ''
		while (index != -1) {
			index = expression.indexOf("'", index);

			// Add the parts of the chain of characters that not are string
			if (index == -1) {
				result += expression.substring(lastIndex, expression.length());
			}
			else {
				result += expression.substring(lastIndex, index).replaceAll(" [ ]+", " ");
			}

			lastIndex = index;
			endInnerLoop = false;

			// Tries to find each first apostrophe of each string of the query
			if ((index > 0) && (expression.charAt(index - 1) == ' ')) {
				index++;

				// Ignore all inner apostrophes and try to find the last of the string
				while (!endInnerLoop)  {
					index = expression.indexOf("'", index);
					index++;

					// If we haven't arrived to the finish of the string
					if (index != expression.length()) {
						if ((index == -1) || (expression.charAt(index) == ' ')) {
							result += translateString(expression.substring(lastIndex, index));
							endInnerLoop = true;
						}
					}
					else {
						result += translateString(expression.substring(lastIndex, index));
						endInnerLoop = true;
						index = -1; // Force to finish the external loop
					}
				}
				lastIndex = index;
			}
		}

		return result;
	}

	/**
	 * Checks the filter expression if it's correct
	 *
	 * @param query The query expression to analyze
	 * @return True if it's valid or false if not
	 */
	private boolean validateExpression(String query) {
		try {
			// If it's needed to validate the query
			if (getValidateFilterExpressionJCheckBox().isSelected()) {
				// If it's an empty query -> ok
				if (query.trim().length() == 0)
					return true;
	
				// Replace all Date(dd-mmm-yyyy) format to ddd-mmm-yyyy (characters will replaced to spaces)
				int index = 0;
				String query_copy = new String(query);
				while ((index = query_copy.indexOf("Date(", index)) != -1) {
					if (index > 0) {
						if ((query_copy.charAt(index-1) != ' ') && (query_copy.charAt(index-1) != '('))
							break;
					}
	
					if (((index + 16) < query_copy.length()) && (query_copy.charAt(index + 16) == ')')) { // +17 is the length of Date(dd-mmm-yyyy)
						if ((index + 17) < query_copy.length()) {
							query_copy = query_copy.substring(0, index) + "     " + query_copy.substring(index+6, index+16) + " " + query_copy.substring(index+17);
						}
						else {
							query_copy = query_copy.substring(0, index) + "     " + query_copy.substring(index+6, index+16);
						}
					}
				}
	
				SQLQueryValidation sQLQueryValidation = new SQLQueryValidation(formatSQLQuery(query_copy), true);
	
				// Tries to validate the query, and if fails shows a message
				if (!sQLQueryValidation.validateQuery()) {
					JOptionPane.showMessageDialog(null, PluginServices.getText(null, "filter_with_an_incorrect_format") + ": " + PluginServices.getText(null, "finded") + " " + sQLQueryValidation.getTokenThatProducedTheSyntacticError() + " " + PluginServices.getText(null, "in")  + " " + sQLQueryValidation.getErrorPositionAsMessage() + ".", PluginServices.getText(null, "error_validating_filter_query"), JOptionPane.ERROR_MESSAGE);
					return false;
				}
				else {
					// Analyzes tokens in query
					StringTokenizer tokens = new StringTokenizer(query, " ");
					String token, token_aux;
					boolean finish = false;
	
					// If there is a field or a value with spaces, (and then it's on differents tokens) -> unify them
					while (tokens.hasMoreTokens()) {
						token = tokens.nextToken().trim();
	
						if (token.charAt(0) == '\'') {
							if (token.charAt(token.length() -1) != '\'') {
								while (!finish) {
									if (!tokens.hasMoreTokens()) {
										JOptionPane.showMessageDialog(null, PluginServices.getText(null, "filter_with_an_incorrect_format") + ": " + PluginServices.getText(null, "the_token") + " " + token + " " + PluginServices.getText(null, "has_bad_format"), PluginServices.getText(null, "error_validating_filter_query"), JOptionPane.ERROR_MESSAGE);
										return false;
									}
									else {
										token_aux = tokens.nextToken().trim();
										token += " " + token_aux;
	
										if (token_aux.charAt(token_aux.length() -1) == '\'')
											finish = true;
									}
								}
	
								finish = false;
							}
						}
	
						if (token.charAt(0) == '\"') {
							if (token.charAt(token.length() -1) != '\"') {
								while (!finish) {
									if (!tokens.hasMoreTokens()) {
										JOptionPane.showMessageDialog(null, PluginServices.getText(null, "filter_with_an_incorrect_format") + ": " + PluginServices.getText(null, "the_token") + " " + token + " " + PluginServices.getText(null, "has_bad_format"), PluginServices.getText(null, "error_validating_filter_query"), JOptionPane.ERROR_MESSAGE);
										return false;
									}
									else {
										token_aux = tokens.nextToken().trim();
										token += " " + token_aux;
	
										if (token_aux.charAt(token_aux.length() -1) == '\"')
											finish = true;
									}
								}
	
								finish = false;
							}
						}
	
						// Tries to find an invalid token
						if (token.length() > 0) {
							// Validates if a supposed field exists
							if ( (token.length() > 2) && (token.charAt(0) == '\"') && (token.charAt(token.length()-1) == '\"') ) {
								if (! this.isAField(token.substring(1, token.length()-1))) {
									JOptionPane.showMessageDialog(null, PluginServices.getText(null, "filter_with_an_incorrect_format") + ": " + PluginServices.getText(null, "the_token") + " " + token + " " + PluginServices.getText(null, "isnt_a_field_of_layer"), PluginServices.getText(null, "error_validating_filter_query"), JOptionPane.ERROR_MESSAGE);
									return false;
								}
							}
							else {
								// If it's an string -> ignore
								if (! ((token.charAt(0) == token.charAt(token.length() - 1)) && (token.charAt(0) == '\''))) {
	
									// If it's a date -> ignore
									int returnValue = validateDate(token);
	
									if (returnValue == 1) {
										JOptionPane.showMessageDialog(null, PluginServices.getText(null, "filter_with_an_incorrect_format") + ": " + PluginServices.getText(null, "incorrect_format_on_date") + " " + token.substring(5, 16) + " .", PluginServices.getText(null, "error_validating_filter_query"), JOptionPane.ERROR_MESSAGE);
										return false;
									}
	
									if (returnValue == 2) {
										// Else -> Checks if the current token is a valid number or symbol
										if ((! StringNumberUtilities.isRealNumberWithIntegerExponent(token)) && (! this.isAnOperatorNameOrSymbol(token, getAllOperatorSymbols()))) {
											JOptionPane.showMessageDialog(null, PluginServices.getText(null, "filter_with_an_incorrect_format") + ": " + PluginServices.getText(null, "not_valid_token") + ": " + token, PluginServices.getText(null, "error_validating_filter_query"), JOptionPane.ERROR_MESSAGE);
											return false;
										}
									}
								}
							}
						}
					}
	
					// If has validate all tokens -> query validated
					return true;
				}
			}
			else
				return true; // No validation done because user selected that option
		}
		catch (Exception e) {
			NotificationManager.showMessageError(PluginServices.getText(null, "filter_with_an_incorrect_format") + ".", e);
			return true; // No validation done because user selected that option
		}
	}

	/**
	 * <p>Formats a query removing non English symbols that can produce the SQL validator could invalidate it.</p>
	 * 
	 * @param an SQL query
	 * @return the SQL query without non English symbols
	 */
	private String formatSQLQuery(String token) {
		return token = token.replace("ñ", "n").replace("ç", "c").replace("á", "a").
		replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u").replace("à", "a").
		replace("è", "e").replace("ì", "i").replace("ò", "o").replace("ù", "u").replace("ä", "a").
		replace("ë", "e").replace("ï", "i").replace("ö", "o").replace("ü", "u").replace("â", "a").
		replace("ê", "e").replace("î", "i").replace("ô", "o").replace("û", "u");
	}

	/**
	 * Returns true if there is a field with the same name as 'text'
	 *
	 * @param text An string
	 * @return A boolean value
	 */
	private boolean isAField(String text) {
		return this.allFieldsAndValuesKnownOfCurrentLayer.containsKey(text);
	}

	/**
	 * Validates if a text has a correct date format as Date(dd-mmm-yyyy)  (Ex. Date(03-feb-2004) )
	 *
	 * @param text date to validate
	 * @return 0 -> if has a date format; 1 -> if it's a date that has a but format; 2 -> if it isn't a date
	 */
	private int validateDate(String text) {
		// If it's a date -> check if format is correct (Ex.  Date(01-feb-2004) )
		if ( ((text.length() == 17) && (text.startsWith("Date(")) && (text.endsWith(")"))) && (text.charAt(7) == '-') && (text.charAt(11) == '-') ) {
			if ( (StringNumberUtilities.isNaturalNumber(text.substring(5, 7))) && (StringNumberUtilities.isNaturalNumber(text.substring(12, 16))) ) {
				try {
					// If can parse the date -> date with a correct format
					DateFormat.getDateInstance().parse(text.substring(5, 16));
					return 0;
				} catch (ParseException e) {
					// If can't parse the date -> date with an incorrect format
					NotificationManager.addError(e);
					return 1;
				}
			}
			else {
				return 1;
			}
		}

		return 2;
	}

	/**
	 * Returns true if there is the 'text' is a symbol or a operator name
	 *
	 * @param text An string
	 * @return A boolean value
	 */
	private boolean isAnOperatorNameOrSymbol(String text, Set<String> operatorNamesAndSymbols) {
		return operatorNamesAndSymbols.contains(text);
	}

	///// END METHODS FOR THE FILTER QUERY /////

	///// METHODS FOR TRANSLATE DATA IN FILTER SENTENCES /////

	/**
	 * Encodes an string to ISO 8859_1 with each ' symbol converted to ''
	 *
	 * @param text An string started and finished with simple apostrophes
	 *
	 * @return An string started and finished with simple apostrophes
	 */
	private String translateString(String text) {
		// Encode to the string to ISO 8859_1 (the URL codification)
		try {

			// Ignore the first and last apostrophes
			if (text.length() > 2) {
				text = text.substring(1, text.length() -1);

				// Convert the string to ISO 8859_1 codification
				text = URLEncoder.encode(text, "8859_1");

				// Change ' (%27 code) to '' for the SQL parser (before send the query)
				text = text.replaceAll("\\%27", "\\'\\'");
			}

		} catch (UnsupportedEncodingException e1) {
			NotificationManager.addError(e1);
		}

		return "'" + text + "'";
	}

	///// END METHODS FOR TRANSLATE DATA IN FILTER SENTENCES /////

	///// METHODS FOR MANIPULATE 'fields' and 'values' /////

	/**
	 * Sets the model with the fields and values of the feature selected.
	 *
	 * @param eds data source with the model of the feature selected
	 */
	public void setModel(ExpressionDataSource eds) {
//		try {
			model = eds;
//            model.start();
//        } catch (ReadDriverException e1) {
//        	NotificationManager.addError(e1);
//        }

        try {
        	int numberOfFields = model.getFieldCount();

        	if (numberOfFields > 0) {
        		Vector<Object> fields = new Vector<Object>(0, 1);
        		int j = 0;

				for (int i = 0; i < numberOfFields; i++) {
					 Object field = model.getFieldName(i);

					if (field != null) {
						fields.add(field);

						String completeFieldPath = this.getPathOfLeafWithoutRoot(field);

						if (! allFieldsAndValuesKnownOfCurrentLayer.containsKey(completeFieldPath) ) {
							allFieldsAndValuesKnownOfCurrentLayer.put(completeFieldPath, new HashMap<String, String>());
						}

						j++;
					}
				}

				fieldsTreeTableModel = new FieldsTreeTableModel(fields.toArray());
        	}
		} catch (FilterException e) {
			NotificationManager.addError(e);
		}
	}

	/**
	 * If there is a field selected, show its new values
	 */
	public void updateFieldValues() {
//		if (currentPath != null) {
//			DefaultExpression data = DataLoadingFromActiveView.getDefaultExpressionDataSource();
//
//			if (data != null) {
//				setModel(data);
//				fillValuesByPath(currentPath);
//
//				// Updates all tables that their data is about the changed view
//				this.updateTablesThatHasNewData();
//
//				// Adjust JScrollPanes to the the beginning
//				getFieldsJScrollPane().getHorizontalScrollBar().setValue(-1);
//				getFieldsJScrollPane().getVerticalScrollBar().setValue(-1);
//
//				// Adjust JScrollPanes to the the beginning
//				getValuesJScrollPane().getHorizontalScrollBar().setValue(-1);
//				getValuesJScrollPane().getVerticalScrollBar().setValue(-1);
//
//				// Adjusts valuesJList to the default position
//				getValuesJList().setSelectedIndex(-1); // No item selected
//			}
//		}
	}

	/**
	 * Adds the fields of the feature.
	 *
	 * @param feature A Layer node with fields information
	 */
	private boolean setFields(WFSSelectedFeature feature) {
		Vector<Object> fields = feature.getFields();

		this.resetFieldsAndValuesData();

		int numberOfFields = fields.size();

		if (numberOfFields > 0) {
			Vector<XMLElement> fieldBranches = new Vector<XMLElement>(0, 1);

			for (Object o_field : fields) {
				XMLElement field = (XMLElement) o_field;

				IXMLType type = field.getEntityType();

				if (type != null) {

					switch (type.getType()) {
						case IXMLType.GML_GEOMETRY: // Don't add branch / field
							break;
						case IXMLType.COMPLEX: case IXMLType.SIMPLE: // Add branch / field
							fieldBranches.add(field);
							break;
					}
				}
			}

			if (fieldBranches.size() > 0) {
				Object obj = fieldBranches.get(0);

				fieldsTreeTableModel = new FieldsTreeTableModel(obj);
				fieldsJTree.setModel(new FieldsTreeTableModel(obj, false));

				// Stores the name of all leafs (fields) of treeTableModel
				Object root = fieldsTreeTableModel.getRoot();

				if (root != null) {
					Vector<Object> fieldsNames = fieldsTreeTableModel.getLeafsFromNodeBranch(root);

					for (Object field : fieldsNames) {
						// Avoid errors
						if ( (! (field instanceof XMLElement)) || (field == null) )	continue;

						// Don't load a geometry field
						if (((XMLElement)field).getEntityType() != null){
							if ( ((XMLElement)field).getEntityType().getType() == IXMLType.GML_GEOMETRY ){
								continue;
							}
						}

						String completeFieldPath = this.getPathOfLeafWithoutRoot(field);

						if (! allFieldsAndValuesKnownOfCurrentLayer.containsKey(completeFieldPath) ) {
							allFieldsAndValuesKnownOfCurrentLayer.put(completeFieldPath, new HashMap<String, String>());
						}
					}
				}
			}
		}

		return true;
	}

	/**
	 * This method load all values known of all fields known.
	 * (It's used when a new layer is loaded).
	 */
	private void setValuesKnownOfFields() {
		// Desde el modelo se debería acceder a los campos y sus valores cargados
//		try {
//			for (int i = 0; i < model.getFieldCount(); i++) {
//				String fieldName = model.getFieldName(i);
//				HashMap<String, String> fieldValues = (HashMap<String, String>) allFieldsAndValuesKnownOfCurrentLayer.get(fieldName);
//
//				if (fieldValues != null) {
//					for (int j = 0; j < model.getRowCount(); j++) {
//						Value value = model.getFieldValue(j, i);
//
//						if (value instanceof NullValue)
//						    continue;
//
//						Object obj = (Object)value;
//
//						if (obj == null)
//							continue;
//
//						fieldValues.put(obj.toString(), obj.toString());
//
//					}
//				}
//			}
//		}
//		catch (Exception e) {
//			NotificationManager.addError(e);
//		}
	}

	/**
	 * Resets the data of fields and their values of the current layer feature, and removes the branches of <code>fieldsJTree</code>.
	 */
	private void resetFieldsAndValuesData() {
		fieldsJTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode()));
		txtExpression.setText("");
		((DefaultListModel)valuesJList.getModel()).removeAllElements();
	}

	/**
	 * Fills list with the values of selected field. 
	 *
	 * @param treePath A path in the tree
	 */
	private void fillValuesByPath(TreePath treePath) {
//		// Duplicates are removed
//		TreeSet<Object> conjunto = new TreeSet<Object>(new Comparator<Object> () {
//			public int compare(Object o1, Object o2) {
//				Value v1 = (Value)o1;
//				Value v2 = (Value)o2;
//				
//				if ((v1 != null) && (v2 != null)) {
//					BooleanValue boolVal;
//
//					try {
//						boolVal = (BooleanValue) (v1.greater(v2));
//
//						if (boolVal.getValue()) {
//							return 1;
//						}
//
//						boolVal = (BooleanValue) (v1.less(v2));
//
//						if (boolVal.getValue()) {
//							return -1;
//						}
//					} catch (IncompatibleTypesException e) {
//						throw new RuntimeException(e);
//					}
//				}
//
//				return 0;
//			}
//		}); // For ordernation
//
//		// Remove the previous items
//		valuesListModel.clear();
//
//		try {
//			//Object root = treePath.getPath()[0];
//			XMLElement element = ((XMLElement)treePath.getLastPathComponent());
//
//			if (element.getEntityType() == null)
//				return;
//			
//			// Gets the values associated to the selected branch
//			switch (element.getEntityType().getType()) {
//				case IXMLType.SIMPLE:
//
//					if(element.getParentElement().getParentElement() == null) {
//						// Find the selected field and try to obtein values related
//						for (int i = 0; i < model.getFieldCount(); i++) {
//							String name = model.getFieldName(i);
//
//							// If we find the field (this means that are loaded its values and we can obtein them)
//							if (name.equals(element.getName())) {
//								for (int j = 0; j < model.getRowCount(); j++) {
//									Value value = model.getFieldValue(j, i);
//
//									if (value instanceof NullValue)
//									    continue;
//
//									if (!conjunto.contains(value)) {
//										conjunto.add(value);
//									}
//								}
//
//								break;
//							}
//						}
//					} else {
//						//create a vector with the parent names from the leaf until the root
//						XMLElement parent = element.getParentElement();
//						Vector<String> parentNames = new Vector<String>();
//						parentNames.add(element.getName());
//						while (parent != null){
//							parentNames.add(parent.getName());
//							parent = parent.getParentElement();
//						}
//
//						//The field name (in the gvSIG table) is the second field name
//						String fieldName = parentNames.get(parentNames.size()-2);
//
//						for (int i = 0; i < model.getFieldCount(); i++) {
//							String name = model.getFieldName(i);
//
//							// If we find the field (this means that are loaded its values and we can obtein them)
//							if (name.equals(fieldName)) {
//								for (int j = 0; j < model.getRowCount(); j++) {
//									Value value = model.getFieldValue(j, i);
//
//									if (value instanceof NullValue)
//									    continue;
//
//									if (value instanceof ComplexValue){
//										for (int k=parentNames.size()-3 ; k >= 0 ; k--){
//											ComplexValue complex = (ComplexValue)value;
//											Value childValue = (Value)complex.get(parentNames.get(k));
//											if (k == 0) {
//												if (!conjunto.contains(childValue)) {
//													conjunto.add(childValue);
//												}
//											} else {
//												value = childValue;
//											}
//										}
//									}
//								}
//
//								break;
//							}
//						}
//					}
//					break;
//				case IXMLType.COMPLEX:
//					break;
//				default:
//					// Do Nothing
//			}
//
//			// Add the values to the model of the graphic list
//			Iterator<Object> it = conjunto.iterator();
//			Object[] objects = currentPath.getPath();
//
//			if (objects.length == 0)
//				return;
//
//			String selectedField = ((XMLElement)objects[objects.length-1]).getName(); // Gets the selected field
//
//			if (selectedField != null) { // If there is a selected field
//				Map<String, String> fieldValues = (HashMap<String, String>)allFieldsAndValuesKnownOfCurrentLayer.get(selectedField); // Gets values stored associated to this field
//				JLabel currentValueLabel = null;
//
//				// If the field doesn't exits -> create a new Map with its values
//				if (fieldValues == null) {
//					fieldValues = new HashMap<String, String>();
//					allFieldsAndValuesKnownOfCurrentLayer.put(selectedField, fieldValues);
//
//					while (it.hasNext()) {
//						// A label with text with yellow background color for values that are loaded in the layer
//						currentValueLabel = new JLabelAsCellValueLoaded();
//
//						currentValueLabel.setText(it.next().toString());
//
//						if (currentValueLabel.getText().compareTo("") != 0) {
//							fieldValues.put(currentValueLabel.getText(), currentValueLabel.getText());
//
//							// All values loaded in this loop must be at beginning of the list (and in a differenciated color)
//							if ( ! valuesListModel.contains(currentValueLabel) )
//								valuesListModel.addElement(currentValueLabel);
//						}
//					}
//				}
//				else { // Else -> Adds the new ones, and changes element labels that have changed  (before were loaded and now not, or before weren't loaded but now yes)
//
//					// Changes element labels that have changed  (before were loaded and now not, or before weren't loaded but now yes)
//					((DefaultListModelForJLabelAsCell)valuesListModel).setAllElementsToNotLoaded();
//
//					// For each current value associated to de current selected field -> if its loaded -> put it at beginning of the list and change to 'JLabelLoadedValue'
//					while (it.hasNext()) {
//						String text = it.next().toString();
//						int elementPosition = ((DefaultListModelForJLabelAsCell)valuesListModel).getIndexOfJLabelText(text);
//
//						if (elementPosition == -1) // If it must be added
//							valuesListModel.addElement(new JLabelAsCellValueLoaded(text));
//						else
//							((DefaultListModelForJLabelAsCell)valuesListModel).changeElementThatHasTextToJLabelLoadedValue(text); // Change to 'JLabelLoadedValue'
//					}
//				}
//
//				// Load the rest of the values associated to the current selected field
//				if (fieldValues != null) {
//					// A label with text with yellow background color for values that are loaded in the layer
//					currentValueLabel = new JLabelAsCellValueNotLoaded();
//
//					Set<String> values = fieldValues.keySet();
//
//					for (String name : values) {
//						if ( ! ((DefaultListModelForJLabelAsCell)valuesListModel).containsJLabelText(name) )
//							valuesListModel.addElement(new JLabelAsCellValueNotLoaded(name));
//					}
//				}
//			}
//
//		} catch (Exception e) {
//			NotificationManager.addError(e);
//		}
	}

	///// END METHODS FOR MANIPULATE 'fields' and 'values' /////

	///// METHOS FOR 'allFieldsAndValuesKnownOfCurrentLayer' /////

	/**
	 * Sets all fields and values known about the current layer
	 *
	 * @param _allFieldsAndValuesKnownOfCurrentLayer A Map object
	 */
	public void setAllFieldsAndValuesKnownOfCurrentLayer(Map<String, Map<String, String>> _allFieldsAndValuesKnownOfCurrentLayer) {
		if (_allFieldsAndValuesKnownOfCurrentLayer == null)
			this.allFieldsAndValuesKnownOfCurrentLayer = new HashMap<String, Map<String, String>>();
		else
			this.allFieldsAndValuesKnownOfCurrentLayer = _allFieldsAndValuesKnownOfCurrentLayer;
	}

	/**
	 * Gets all fields and values known about the current layer
	 *
	 * @return _allFieldsAndValuesKnownOfCurrentLayer A Map object
	 */
	public Map<String, Map<String, String>> getAllFieldsAndValuesKnownOfCurrentLayer() {
		return allFieldsAndValuesKnownOfCurrentLayer;
	}

	///// METHOS FOR 'allFieldsAndValuesKnownOfCurrentLayer' /////

	///// UPDATE TABLES DATA /////

	/**
	 * Updates all tables that their data is about the changed view
	 */
	private void updateTablesThatHasNewData() {
//		boolean oneTimeNewDataToTableAdded = false;
//
//		IWindow[] activeNoModalWindows = PluginServices.getMDIManager().getAllWindows();
//
//		for (int i = 0; i < activeNoModalWindows.length; i++) {
//			IWindow window = activeNoModalWindows[i];
//			if (window instanceof Table) {
//				Table table = (Table) window;
//
//				int pos1 = featureName.indexOf(':');
//
//				if ((pos1 >= 0) && (pos1 < featureName.length()))
//					featureName = featureName.substring(pos1 +1, featureName.length());
//
//					String featureOfTable = table.getModel().getName();
//					int pos2 = featureOfTable.indexOf(':');
//
//					if ((pos2 >= 0) && (pos2 < featureName.length()))
//						featureOfTable = featureOfTable.substring(pos2 +1, featureOfTable.length());
//
//				if (featureName.trim().compareTo(featureOfTable.trim()) == 0) {
//					// Only add the new data associated to the table one time
//					if (oneTimeNewDataToTableAdded == false) {
//						setNewDataToTable();
//						oneTimeNewDataToTableAdded = true;
//					}
//
//					// Refresh the table with the new data
//					table.invalidate();
//					table.refresh();
//				}
//			}
//		}
	}
	
	/**
	 * This method is a modification of the "execute" method from the "ShowTable" class
	 *
	 * @see com.iver.cit.gvsig.ShowTable#execute(String)
	 */
	private void setNewDataToTable() {
//		try {
//			if (getPanelGroup().getReference() == null)
//				return;
//
//			FLayer activeLayer = (FLayer) getPanelGroup().getReference();
//			AlphanumericData co = (AlphanumericData) activeLayer;
//
//			ProjectExtension ext = (ProjectExtension) PluginServices.getExtension(ProjectExtension.class);
//			ProjectTable projectTable = ext.getProject().getTable(co);
//
//			EditableAdapter ea = null;
//			ReadableVectorial rv = ((FLyrVect)activeLayer).getSource();
//
//			if (rv instanceof VectorialEditableAdapter) {
//				ea = (EditableAdapter)((FLyrVect)activeLayer).getSource();
//			} else {
//				ea = new EditableAdapter();
//				SelectableDataSource sds = ((FLyrVect)activeLayer).getRecordset();
//				ea.setOriginalDataSource(sds);
//			}
//
//			if (projectTable == null) {
//				projectTable = ProjectFactory.createTable(PluginServices.getText(this, "Tabla_de_Atributos") + ": " + activeLayer.getName(), ea);
//				projectTable.setProjectDocumentFactory(new ProjectTableFactory());
//				projectTable.setAssociatedTable(co);
//				ext.getProject().addDocument(projectTable);
//			}
//
//			projectTable.setModel(ea);
//		} catch (ReadDriverException e) {
//			NotificationManager.addError(PluginServices.getText(this,"No_se_pudo_obtener_la_tabla_de_la_capa"), e);
//        }
	}

//	/** OLD VERSION
//	 * This method is a modification of the "execute" method from the "ShowTable" class
//	 *
//	 * @see com.iver.cit.gvsig.ShowTable#execute(String)
//	 */
//	private void setNewDataToTable() {
//		BaseView vista = (BaseView) PluginServices.getMDIManager().getActiveWindow();
//		FLayer[] actives = vista.getModel().getMapContext().getLayers().getActives();
//
//		try {
//			for (int i = 0; i < actives.length; i++) {
//				if (actives[i] instanceof AlphanumericData) {
//					AlphanumericData co = (AlphanumericData) actives[i];
//
//					ProjectExtension ext = (ProjectExtension) PluginServices.getExtension(ProjectExtension.class);
//
//					ProjectTable projectTable = ext.getProject().getTable(co);
//					EditableAdapter ea=null;
//					ReadableVectorial rv=((FLyrVect)actives[i]).getSource();
//					if (rv instanceof VectorialEditableAdapter){
//						ea = (EditableAdapter)((FLyrVect)actives[i]).getSource();
//					} else {
//						ea = new EditableAdapter();
//						SelectableDataSource sds=((FLyrVect)actives[i]).getRecordset();
//						ea.setOriginalDataSource(sds);
//					}
//
//					if (projectTable == null) {
//						projectTable = ProjectFactory.createTable(PluginServices.getText(this, "Tabla_de_Atributos") + ": " + actives[i].getName(),	ea);
//						projectTable.setProjectDocumentFactory(new ProjectTableFactory());
//						projectTable.setAssociatedTable(co);
//						ext.getProject().addDocument(projectTable);
//					}
//
//					projectTable.setModel(ea);
//				}
//			}
//		} catch (ReadDriverException e) {
//			NotificationManager.addError(PluginServices.getText(this,"No_se_pudo_obtener_la_tabla_de_la_capa"), e);
//        }
//	}

	///// END UPDATE TABLES DATA /////
}