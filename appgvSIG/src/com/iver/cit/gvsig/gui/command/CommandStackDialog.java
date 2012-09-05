package com.iver.cit.gvsig.gui.command;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.gvsig.fmap.dal.DataStoreNotification;
import org.gvsig.gui.beans.DefaultBean;
import org.gvsig.tools.observer.Observable;
import org.gvsig.tools.observer.Observer;
import org.gvsig.tools.undo.UndoRedoInfo;
import org.gvsig.tools.undo.UndoRedoStack;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.SingletonWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

public class CommandStackDialog extends DefaultBean implements SingletonWindow, IWindowListener,Observer{

	private JTable jTable = null;
	private JPanel jPanel = null;
	private UndoRedoStack undoRedoStack;
	private JSlider jSlider = null;
	//private int itemCount;
	private int lowLimit;
	private int currentValue=-1;
	private JPanel jPanel1 = null;
	protected boolean refreshing;
	private JPanel pCenter = null;
	private JScrollPane jScrollPane = null;
	private static final ImageIcon imodify=PluginServices.getIconTheme()
		.get("edition-modify-command");
	private static final ImageIcon iadd= PluginServices.getIconTheme()
		.get("edition-add-command");

	private static final ImageIcon idel = PluginServices.getIconTheme()
		.get("edition-del-command");

	private MyModel mymodel = null;
	/**
	 * This is the default constructor
	 */
	public CommandStackDialog() {
		super();
		initialize();
	}
	public void setModel(UndoRedoStack cr1){
        if (this.undoRedoStack != null) {
            if (this.undoRedoStack.equals(cr1)) {
                return;
            } else {
                this.undoRedoStack.deleteObserver(this);
            }
		}
		this.undoRedoStack=cr1;
		this.undoRedoStack.addObserver(this);
		initTable();
		initSlider();
		currentValue=mymodel.getPos();
    	refreshControls();
		refreshScroll();
	}
	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(328, 229);
		this.add(getJPanel(), java.awt.BorderLayout.NORTH);
		this.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
	}

	/**
	 * This method initializes jList
	 *
	 * @return javax.swing.JList
	 */
	private JTable getTable() {
		if (jTable == null) {
			jTable = new JTable();
		}
		return jTable;
	}
	private void initTable(){
		mymodel=new MyModel(undoRedoStack);
		jTable.setModel(mymodel);
		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTable.setSelectionBackground(Color.orange);
		jTable.setSelectionForeground(Color.black);
		jTable.setShowGrid(false);
		jTable.getTableHeader().setBackground(Color.white);
		TableColumn tc = jTable.getColumnModel().getColumn(0);
		tc.setCellRenderer(new DefaultTableCellRenderer() {
			   public Component getTableCellRendererComponent(JTable table,
			                                               Object value,
			                                               boolean isSelected,
			                                               boolean hasFocus,
			                                               int row,
			                                               int column)
			      {
			         JLabel label = (JLabel)
			            super.getTableCellRendererComponent
			               (table, value, isSelected, hasFocus, row, column);
			            UndoRedoInfo info = (UndoRedoInfo) value;
			            switch (info.getType()) {
                            case UndoRedoInfo.INSERT:
                                label.setIcon(iadd);
                                break;
                            case UndoRedoInfo.DELETE:
                                label.setIcon(idel);
                                break;
                            default:
                                label.setIcon(imodify);
                        }
			         if (mymodel.getPos()<row){
			        	 label.setBackground(Color.lightGray);
			         }else {
			       		 label.setBackground(Color.orange);
			         }
			            return label;
			      }
			});

		jTable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent e) {
				int newpos=jTable.getSelectedRow();
				mymodel.setPos(newpos);
				PluginServices.getMainFrame().enableControls();
			}
		});
	}
	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
		}
		return jPanel;
	}

	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.ICONIFIABLE |
				WindowInfo.MODELESSDIALOG | WindowInfo.RESIZABLE | WindowInfo.PALETTE);
		m_viewinfo.setTitle(PluginServices.getText(this,
				"pila_de_comandos"));
		return m_viewinfo;
	}

	public Object getWindowModel() {
		return mymodel.getClass().getName();
	}

	public void windowActivated() {
		this.validateTree();
	}

	public void windowClosed() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.edition.commands.CommandListener#executeCommand(com.iver.cit.gvsig.fmap.edition.commands.CommandEvent)
	 */
	public void commandRepaint() {
		setValue(mymodel.getPos()+1,true);
		refreshControls();
		refreshScroll();
	}
	private void refreshScroll(){
		Dimension size=new Dimension(jSlider.getPreferredSize().width,((mymodel.getRowCount())*getTable().getRowHeight()));
		JScrollBar verticalScrollBar=getJScrollPane().getVerticalScrollBar();//ove(size.width,size.height);
		verticalScrollBar.setValue(mymodel.getPos()*getTable().getRowHeight());
		jSlider.setPreferredSize(size);
		jSlider.setSize(size);
		validateTree();
	}
	/**
	 * This method initializes jSlider
	 *
	 * @return javax.swing.JSlider
	 */
	private JSlider getJSlider() {
		if (jSlider == null) {
			jSlider = new JSlider();
		}
		return jSlider;
	}
	private void initSlider(){
		jSlider.setOrientation(SwingConstants.VERTICAL);
		jSlider.setPreferredSize(new Dimension(jSlider.getPreferredSize().width,((getTable().getRowCount())*getTable().getRowHeight())));
		jSlider.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent e) {

            	}
            });
    	jSlider.addChangeListener(new javax.swing.event.ChangeListener() {


				public void stateChanged(javax.swing.event.ChangeEvent e) {
					int value = (int) (getJSlider().getValue() * mymodel.getRowCount() * 0.01);
                   	if (!refreshing) {
						mymodel.setPos(mymodel.getRowCount()-1-value);
						//System.out.println("setPos = "+(cr.getCommandCount()-1-value));
					}
			    }
    		});
    	setValue(mymodel.getRowCount()-1-mymodel.getPos(),true);
	}
    public void setValue(int number, boolean fireEvent) {
        int rowCount=mymodel.getRowCount();
        if (number < lowLimit) {
			number = lowLimit;
		}
        if (number > rowCount) {
			number = rowCount;
		}
        if (number != currentValue) {
        	currentValue = number;
        	refreshControls();
        	if (fireEvent) {
				callValueChanged(new Integer(currentValue));
			}
        }
        int selpos=rowCount-1-number;
        if (selpos>=0){
       	 	getTable().setRowSelectionInterval(selpos,selpos);
        } else {
			getTable().clearSelection();
		}
    }
    /**
     * Refreshes all the mutable controls in this component.
     */
    private void refreshControls() {
    	int normalizedValue = (int) (((mymodel.getRowCount()-currentValue) / (float) mymodel.getRowCount())*100);
		refreshSlider(normalizedValue);
		jTable.repaint();
	}
    /**
	 * Sets the slider to the correct (scaled) position.
     * @param normalizedValue
     */
    private void refreshSlider(int normalizedValue) {
    	refreshing = true;
        getJSlider().setValue(normalizedValue);
        refreshing = false;
    }

	/**
	 * This method initializes jPanel1
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.add(getJSlider());
		}
		return jPanel1;
	}



	/**
	 * This method initializes pCenter
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPCenter() {
		if (pCenter == null) {
			pCenter = new JPanel();
			pCenter.setLayout(new BorderLayout());
			pCenter.add(getTable(), java.awt.BorderLayout.CENTER);
			pCenter.add(getJPanel1(), java.awt.BorderLayout.WEST);
		}
		return pCenter;
	}

	/**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getPCenter());
		}
		return jScrollPane;
	}
	public void update(Observable observable, Object notification) {
//	    if (notification instanceof DataStoreNotification) {
//            DataStoreNotification storeNotification = (DataStoreNotification) notification;
//            if (DataStoreNotification.SELECTION_CHANGE.equals(storeNotification
//                    .getType())) {
//                commandRepaint();
//            }
//        }
	    commandRepaint();
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
