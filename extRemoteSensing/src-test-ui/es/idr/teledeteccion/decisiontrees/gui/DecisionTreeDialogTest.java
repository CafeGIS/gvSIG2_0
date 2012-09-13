package es.idr.teledeteccion.decisiontrees.gui;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.gvsig.remotesensing.decisiontrees.gui.DecisionTreeDialog;

public class DecisionTreeDialogTest {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
		} catch (Exception e) {
			System.err.println("No se puede cambiar al LookAndFeel");
		}
		
		JFrame frame = new JFrame();
		DecisionTreeDialog dialog = new DecisionTreeDialog(600, 600, null);
		frame.setSize(610,610);
		frame.getContentPane().add(dialog);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	}

}
