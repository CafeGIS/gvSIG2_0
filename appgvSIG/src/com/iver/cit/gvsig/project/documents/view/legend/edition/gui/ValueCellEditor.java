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
package com.iver.cit.gvsig.project.documents.view.legend.edition.gui;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

/**
 * Cell Editor sobre los valores únicos.
 *
 * @author Vicente Caballero Navarro
 */
public class ValueCellEditor extends JTextField implements TableCellEditor {
	private ArrayList listeners = new ArrayList();
	//private Value initialValue;

	/**
	 * Crea un nuevo FLabelCellEditor.
	 */
	public ValueCellEditor() {
		addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						stopCellEditing();
					} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						cancelCellEditing();
					}
				}
			});
	}

	//Implement the one CellEditor method that AbstractCellEditor doesn't.
	public Object getCellEditorValue() {
		return getValue(getText());
	}

	//Implement the one method defined by TableCellEditor.
	public Component getTableCellEditorComponent(JTable table, Object value,
		boolean isSelected, int row, int column) {
		//initialValue=(Value)value;
		if (value == null) {
			setText("");
		} else {
			setText(value.toString());
		}

		return this;
	}

	/**
	 * @see javax.swing.CellEditor#cancelCellEditing()
	 */
	public void cancelCellEditing() {
		//if (initialValue != null) {
		//	setText(initialValue.toString());
		//}

		for (int i = 0; i < listeners.size(); i++) {
			CellEditorListener l = (CellEditorListener) listeners.get(i);
			ChangeEvent evt = new ChangeEvent(this);
			l.editingCanceled(evt);
		}
	}

	/**
	 * @see javax.swing.CellEditor#stopCellEditing()
	 */
	public boolean stopCellEditing() {
		for (int i = 0; i < listeners.size(); i++) {
			CellEditorListener l = (CellEditorListener) listeners.get(i);
			ChangeEvent evt = new ChangeEvent(this);
			l.editingStopped(evt);
		}

		return true;
	}

	/**
	 * @see javax.swing.CellEditor#isCellEditable(java.util.EventObject)
	 */
	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}

	/**
	 * @see javax.swing.CellEditor#shouldSelectCell(java.util.EventObject)
	 */
	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}

	/**
	 * @see javax.swing.CellEditor#addCellEditorListener(javax.swing.event.CellEditorListener)
	 */
	public void addCellEditorListener(CellEditorListener l) {
		listeners.add(l);
	}

	/**
	 * @see javax.swing.CellEditor#removeCellEditorListener(javax.swing.event.CellEditorListener)
	 */
	public void removeCellEditorListener(CellEditorListener l) {
		listeners.remove(l);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param s DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	private Object getValue(String s) {
		Object val = null;
		//Value cero = null;

//		try {
			try {
				val = new Integer(s);//ValueFactory.createValueByType(s, Types.INTEGER);

				return val;
			} catch (NumberFormatException e) {
			}

			try {
				val = new Long(s);//ValueFactory.createValueByType(s, Types.BIGINT);

				return val;
			} catch (NumberFormatException e) {
			}

			try {
				val = new Float(s);//ValueFactory.createValueByType(s, Types.FLOAT);

				return val;
			} catch (NumberFormatException e) {
			}

			try {
				val = new Double(s);//ValueFactory.createValueByType(s, Types.DOUBLE);

				return val;
			} catch (NumberFormatException e) {
			}

			val = s;//ValueFactory.createValueByType(s, Types.LONGVARCHAR);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}

		/*try {
		   if (v instanceof DoubleValue) {
		           val = ValueFactory.createValue(Double.parseDouble(s));
		           cero = ValueFactory.createValue((double) 0);
		   } else if (v instanceof StringValue) {
		           val = ValueFactory.createValue(s);
		           cero = ValueFactory.createValue((String) "");
		   } else if (v instanceof LongValue) {
		           val = ValueFactory.createValue(Long.parseLong(s));
		           cero = ValueFactory.createValue((long) 0);
		   } else if (v instanceof IntValue) {
		           val = ValueFactory.createValue(Integer.parseInt(s));
		           cero = ValueFactory.createValue((int) 0);
		   } else if (v instanceof FloatValue) {
		           val = ValueFactory.createValue(Float.parseFloat(s));
		           cero = ValueFactory.createValue((float) 0);
		   } else if (v instanceof ShortValue) {
		           val = ValueFactory.createValue(Short.parseShort(s));
		           cero = ValueFactory.createValue((short) 0);
		   } else if (v instanceof BooleanValue) {
		           val = ValueFactory.createValue(Boolean.getBoolean(s));
		           cero = ValueFactory.createValue((boolean) false);
		   } else if (v instanceof DateValue) {
		           val = ValueFactory.createValue(Date.parse(s));
		           cero = ValueFactory.createValue((Date) new Date());
		   }
		   } catch (NumberFormatException e) {
		           ///JOptionPane.showMessageDialog(null, PluginServices...getText(this,"Formato de número erroneo")+".");
		           return cero;
		   }
		 */
		return val;
	}
}
