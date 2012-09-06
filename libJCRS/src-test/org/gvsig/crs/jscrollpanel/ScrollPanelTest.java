package org.gvsig.crs.jscrollpanel;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class ScrollPanelTest {
	
	JTable jTable = null;
	JScrollPane jScrollPane = null;

	public ScrollPanelTest() {
		super();
		JFrame jFrame = new JFrame();
		jFrame.getContentPane().add(getJScrollPane());	
		jFrame.setSize(500, 150);
		jFrame.setVisible(true);
		// TODO Auto-generated constructor stub
	}
	
	public JTable getJTable() {
		if (jTable == null) {
			String[] columnNames= {"UNA COLUMNA", "DOS COLUMNAS"};
			Object[][]data = {};			
			DefaultTableModel dtm = new DefaultTableModel(data, columnNames);
			jTable = new JTable(dtm);
			jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			TableColumn column = null;
			   for (int i = 0; i < 2; i++) {
			       column = jTable.getColumnModel().getColumn(i);
			       if (i == 0) {
			           column.setPreferredWidth(600);
			       }
			   }
			//jTable.setPreferredSize(new Dimension(800, 100));			
						
		}
		
		return jTable;
		
	}
	
	
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane(getJTable(),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			jScrollPane.setPreferredSize(new Dimension(500,150));
			jScrollPane.setBorder(
				    BorderFactory.createCompoundBorder(
					BorderFactory.createCompoundBorder(
							BorderFactory.createTitledBorder("TABLA GENÉRICA DE 2 CAMPOS"),
							BorderFactory.createEmptyBorder(5,5,5,5)),
							jScrollPane.getBorder()));
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}

	
	public static void main(String[] args){
		ScrollPanelTest al = new ScrollPanelTest();
	}
		
		

}
