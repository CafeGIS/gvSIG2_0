package com.iver.cit.gvsig.gui.panels;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;
import javax.swing.FocusManager;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;


/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
//TODO comentado para que compile
public class TextFieldEdit extends JPopupMenu implements KeyListener {
    private JTextField txt = new JTextField();
//    private FLyrAnnotation lyr;
    /**
     * This is the default constructor
     */
//    public TextFieldEdit(FLyrAnnotation lyr) {
//        super();
//        this.lyr=lyr;
//        initialize();
//    }

    /**
     * This method initializes this
     */
    private void initialize() {
        this.setSize(100, 20);
        txt.setPreferredSize(new Dimension(100, 20));
//        txt.addActionListener(new java.awt.event.ActionListener() {
//        	public void actionPerformed(java.awt.event.ActionEvent e) {
//        		 try {
//        				VectorialEditableAdapter vea=(VectorialEditableAdapter)lyr.getSource();
//        				int index=lyr.getInEdition();
//        				IRowEdited row = vea.getRow(index); //.getAttribute(columnIndex);
//        		        Value[] values = row.getAttributes();
//        		        int columnText=lyr.getMapping().getColumnText();
//        		        int type=vea.getRecordset().getFieldType(columnText);
//        		        values[columnText] = ValueFactory.createValueByType(txt.getText(),type);
//
//        		        IRow newRow = null;
//
//        		        if (row.getLinkedRow() instanceof IFeature) {
//        		            IGeometry geometry = ((DefaultFeature) row.getLinkedRow()).getGeometry();
//        		            newRow = new DefaultFeature(geometry, values);
//        		        } else {
//        		            newRow = new DefaultRow(values);
//        		        }
//
//
//        					vea.modifyRow(index, newRow,"Editar valor de texto", EditionEvent.GRAPHIC);
//        				} catch (ReadDriverException e1) {
//        					e1.printStackTrace();
//        				}  catch (ParseException e1) {
//							e1.printStackTrace();
//						} catch (NumberFormatException e1) {
//							NotificationManager.addError("no_es_de_tipo_numerico",e1);
//						} catch (ValidateRowException e1) {
//							e1.printStackTrace();
//						}
//        				View view=(View)PluginServices.getMDIManager().getActiveWindow();
//        				TextFieldEdit.this.show(false);
//        				// view.getMapControl().drawMap(true);
//        				lyr.setDirty(true);
//        				view.getMapControl().rePaintDirtyLayers();
//
//        				PluginServices.getMainFrame().enableControls();
//        	}
//        });
        this.add(txt);
        this.setBorderPainted(false);
        this.setBorder(BorderFactory.createEmptyBorder());
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     * @param component DOCUMENT ME!
     */
    public void show(Point2D p, JComponent component) {

//    	VectorialEditableAdapter vea=(VectorialEditableAdapter)lyr.getSource();
//		int index=lyr.getInEdition();
//		Value value=null;
//		try {
//			int columnText=lyr.getMapping().getColumnText();
//			value = vea.getRecordset().getFieldValue(index,columnText);
//			txt.setText(value.toString());
//		} catch (ReadDriverException e) {
//			e.printStackTrace();
//		}

		this.show(component, (int)p.getX(), (int)p.getY()-20);
        FocusManager fm = FocusManager.getCurrentManager();
        fm.focusPreviousComponent(txt);
    }

	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void keyReleased(KeyEvent arg0) {
		 FocusManager fm = FocusManager.getCurrentManager();
	     fm.focusPreviousComponent(txt);
	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
