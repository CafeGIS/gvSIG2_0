package org.gvsig.project.document.table.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.gvsig.gui.beans.swing.JButton;
import org.gvsig.project.document.table.ExportStatisticsFile;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
/**
 * @author Fernando González Cortés
 */
public class Statistics extends JPanel implements IWindow {

	private JScrollPane jScrollPane = null;
	private JTextArea txtStatistics = null;
	private JButton jButton = null;
	private JPanel jPanel = null;
private JButton jButtonExport;

//	private Hashtable<String, Number> valores = new Hashtable<String, Number>();
	//private TreeMap<String, Number> valores = new TreeMap<String, Number>();
	private List<MyObjectStatistics> valores = new ArrayList<MyObjectStatistics>();
//	private HashSet<String, Number> valores = new HashSet<String, Number>();
	/**
	 * This is the default constructor
	 */
	public Statistics() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private  void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(300,200);
		this.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
		this.add(getJPanel(), java.awt.BorderLayout.SOUTH);
	}
	/**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getTxtStatistics());
		}
		return jScrollPane;
	}
	/**
	 * This method initializes jTextArea
	 *
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getTxtStatistics() {
		if (txtStatistics == null) {
			txtStatistics = new JTextArea();
		}
		return txtStatistics;
	}
	/**
	 * This method initializes jButton
	 *
	 * @return JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setPreferredSize(new java.awt.Dimension(100,18));
			jButton.setText(PluginServices.getText(this, "cerrar"));
			jButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    PluginServices.getMDIManager().closeWindow(Statistics.this);
                }
            });
		}
		return jButton;
	}
	/**
	 * This method initializes jButton
	 *
	 * @return JButton
	 * 				- New JButton to Export the statistics
	 */
	private JButton getJButtonExport() {
		if (jButtonExport == null) {
			jButtonExport = new JButton();
			jButtonExport.setPreferredSize(new java.awt.Dimension(100,18));
			jButtonExport.setText(PluginServices.getText(this, "exportar"));
			jButtonExport.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new ExportStatisticsFile(valores); // Class to export statistics to dbf or csv format
				}
            });//listener
		}
		return jButtonExport;
	}
	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			FlowLayout layout = new FlowLayout();
			layout.setAlignment(FlowLayout.RIGHT);
			jPanel.setLayout(layout);
			jPanel.add(getJButtonExport(),null);
			jPanel.add(getJButton(), null);
		}
		return jPanel;
	}
    /**
     * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
     */
    public WindowInfo getWindowInfo() {
        WindowInfo vi = new WindowInfo(WindowInfo.MODALDIALOG);
        vi.setTitle(PluginServices.getText(this, "estadisticas"));
        return vi;
    }
    /**
     * @param i
     * @param j
     * @param k
     * @param l
     * @param m
     * @param n
     * @param o
     * @param p
     */
    public void setStatistics(double media, double maximo, double minimo, double varianza, double desviacion, int numero, double ambito, double suma) {
        getTxtStatistics().setText(PluginServices.getText(this, "suma") + ": " + suma + "\n"+
                PluginServices.getText(this, "recuento") + ": " + numero + "\n"+
                PluginServices.getText(this, "media") + ": " + media + "\n"+
                PluginServices.getText(this, "maximo") + ": " + maximo + "\n"+
                PluginServices.getText(this, "minimo") + ": " + minimo + "\n"+
                PluginServices.getText(this, "ambito") + ": " + ambito + "\n"+
                PluginServices.getText(this, "varianza") + ": " + varianza + "\n"+
                PluginServices.getText(this, "desviacion_tipica") + ": " + desviacion);

        // Saving the statistics table values necessary in ExportStatisticsFile.java
		valores.add(new MyObjectStatistics(PluginServices.getText(this, "suma"),suma));
		valores.add(new MyObjectStatistics(PluginServices.getText(this, "recuento"),numero));
		valores.add(new MyObjectStatistics(PluginServices.getText(this, "media"),media));
		valores.add(new MyObjectStatistics(PluginServices.getText(this, "maximo"),maximo));
		valores.add(new MyObjectStatistics(PluginServices.getText(this, "minimo"),minimo));
		valores.add(new MyObjectStatistics(PluginServices.getText(this, "ambito"),ambito));
		valores.add(new MyObjectStatistics(PluginServices.getText(this, "varianza"),varianza));
		valores.add(new MyObjectStatistics(PluginServices.getText(this, "desviacion_tipica"),desviacion));


    }
    /**
	 * Class to create an object with key and value.
	 *
	 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
	 *
	 */

	public class MyObjectStatistics{
		private String key;
		private double value;
		public MyObjectStatistics(String key, double value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return this.key;
		}
		public double getValue() {
			return this.value;
		}

	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

    }
