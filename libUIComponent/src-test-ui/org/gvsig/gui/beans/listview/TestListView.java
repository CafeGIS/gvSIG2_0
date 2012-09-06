/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
 */
package org.gvsig.gui.beans.listview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
/**
 * @version 28/06/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class TestListView implements ActionListener {
	private JFrame jFrame = null;
	private JScrollPane jScrollPane = null;
	private ListViewComponent listViewComponent = null;
	JButton botonAdd = null;
	JButton botonDel = null;
	JCheckBox multiselect = null;
	JCheckBox enabled = null;

	public TestListView() {
		initialize();
	}

	private void initialize() {
		jFrame = new JFrame();
		jScrollPane = new JScrollPane();

		listViewComponent = new ListViewComponent();
		listViewComponent.setEditable(true);
		jScrollPane.setViewportView(listViewComponent);

		for (int i=0; i<1000; i++) {
			ListViewItem item = new ListViewItem(new RampPainter(), "Prueba-" + i);
			listViewComponent.addItem(item, true);
		}

		JPanel jpane = new JPanel();
		jpane.setLayout(new BorderLayout());
		jpane.add(jScrollPane, BorderLayout.CENTER);

		JPanel jPanel1 = new JPanel();
		jPanel1.setLayout(new java.awt.GridBagLayout());

		jpane.add(jPanel1, BorderLayout.SOUTH);

		botonAdd = new JButton("Add");
		botonAdd.addActionListener(this);
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 2, 2);
		jPanel1.add(botonAdd, gridBagConstraints);

		botonDel = new JButton("Del");
		botonDel.addActionListener(this);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(5, 2, 2, 5);
		jPanel1.add(botonDel, gridBagConstraints);

		multiselect = new JCheckBox("MultiSelect");
		multiselect.addActionListener(this);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new java.awt.Insets(2, 5, 5, 5);
		jPanel1.add(multiselect, gridBagConstraints);

		enabled = new JCheckBox("Activo");
		enabled.addActionListener(this);
		enabled.setSelected(true);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new java.awt.Insets(2, 5, 5, 5);
		jPanel1.add(enabled, gridBagConstraints);

		jFrame.setSize(new Dimension(192, 300));
		jFrame.setContentPane(jpane);

		jFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		jFrame.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestListView();
	}

	int id = 0;
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == botonAdd) {
			ListViewItem item = new ListViewItem(new RampPainter(), "Prueba");
			listViewComponent.addItem(item);
			return;
		}

		if (e.getSource() == botonDel) {
			listViewComponent.removeSelecteds();
			return;
		}

		if (e.getSource() == multiselect) {
			listViewComponent.setMultiSelect(multiselect.isSelected());
			return;
		}

		if (e.getSource() == enabled) {
			listViewComponent.setEnabled(enabled.isSelected());
			jScrollPane.setEnabled(enabled.isSelected());
			return;
		}
	}
}