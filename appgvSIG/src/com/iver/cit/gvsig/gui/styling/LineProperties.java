package com.iver.cit.gvsig.gui.styling;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.gvsig.gui.beans.DefaultBean;
import org.gvsig.gui.beans.swing.JBlank;

import com.iver.andami.PluginServices;

public class LineProperties extends DefaultBean implements ActionListener  {

	private JRadioButton joinBevel;
	private JRadioButton joinMiter;
	private JRadioButton joinRound;
	private JRadioButton capBut;
	private JRadioButton capRound;
	private JRadioButton capSquare;
	private PatternEditor pe;
	private JButton clearButton;
	private float width;


	public LineProperties(float lineWidth) {
		super();
		this.width=lineWidth;
		initialize();
	}

	private void initialize() {
		setName(PluginServices.getText(this, "line_properties"));
		setLayout(new BorderLayout(10, 10));

		JPanel aux2 = new JPanel();
		JPanel pnlJoin = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));


		pnlJoin.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), PluginServices.getText(this, "join_style")+":"));

		//////////
		PluginServices.getIconTheme().registerDefault(
				"join-bevel",
				this.getClass().getClassLoader().getResource("images/join_bevel.png"));
		PluginServices.getIconTheme().registerDefault(
				"join-bevel-selected",
				this.getClass().getClassLoader().getResource("images/join_bevel-selected.png"));
		ImageIcon img= PluginServices.getIconTheme().get("join-bevel");

		JPanel d = new JPanel();
		JPanel toCenter;
		d.setLayout(new BoxLayout(d, BoxLayout.Y_AXIS));
		joinBevel = new JRadioButton(img);
		joinBevel.setSelectedIcon(PluginServices.getIconTheme().get("join-bevel-selected"));
		FlowLayout flowCenteredLayout = new FlowLayout(FlowLayout.CENTER, 0, 0);
		toCenter = new JPanel(flowCenteredLayout);
		toCenter.add(joinBevel);
		d.add(toCenter);

		toCenter = new JPanel(flowCenteredLayout);
		toCenter.add(new JLabel(PluginServices.getText(this, "join_bevel")));
		d.add(toCenter);

		pnlJoin.add(d);

		PluginServices.getIconTheme().registerDefault(
				"join-miter",
				this.getClass().getClassLoader().getResource("images/join_miter.png"));
		PluginServices.getIconTheme().registerDefault(
				"join-miter-selected",
				this.getClass().getClassLoader().getResource("images/join_miter-selected.png"));

		img= PluginServices.getIconTheme().get("join-miter");

		d = new JPanel();
		d.setLayout(new BoxLayout(d, BoxLayout.Y_AXIS));
		joinMiter = new JRadioButton(img);
		joinMiter.setSelectedIcon(PluginServices.getIconTheme().get("join-miter-selected"));
		toCenter = new JPanel(flowCenteredLayout);
		toCenter.add(joinMiter);
		d.add(toCenter);

		toCenter = new JPanel(flowCenteredLayout);
		toCenter.add(new JLabel(PluginServices.getText(this, "join_miter")));
		d.add(toCenter);
		pnlJoin.add(d);

		PluginServices.getIconTheme().registerDefault(
				"join-round",
				this.getClass().getClassLoader().getResource("images/join_round.png"));
		PluginServices.getIconTheme().registerDefault(
				"join-round-selected",
				this.getClass().getClassLoader().getResource("images/join_round-selected.png"));
		img= PluginServices.getIconTheme().get("join-round");

		d = new JPanel();
		d.setLayout(new BoxLayout(d, BoxLayout.Y_AXIS));
		joinRound = new JRadioButton(img);
		joinRound.setSelectedIcon(PluginServices.getIconTheme().get("join-round-selected"));
		toCenter = new JPanel(flowCenteredLayout);
		toCenter.add(joinRound);
		d.add(toCenter);
		toCenter = new JPanel(flowCenteredLayout);
		toCenter.add(new JLabel(PluginServices.getText(this, "join_round")));
		d.add(toCenter);
		pnlJoin.add(d);


		ButtonGroup groupJoin = new ButtonGroup();
		groupJoin.add(joinBevel);
		groupJoin.add(joinMiter);
		groupJoin.add(joinRound);
		aux2.add(pnlJoin);

		JPanel aux3 = new JPanel();

		JPanel pnlCap = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
		pnlCap.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), PluginServices.getText(this, "end_style")+":"));

		PluginServices.getIconTheme().registerDefault(
				"cap-butt",
				this.getClass().getClassLoader().getResource("images/cap_butt.png"));
		PluginServices.getIconTheme().registerDefault(
				"cap-butt-selected",
				this.getClass().getClassLoader().getResource("images/cap_butt-selected.png"));
		img= PluginServices.getIconTheme().get("cap-butt");

		JPanel c = new JPanel();
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		capBut = new JRadioButton(img);
		capBut.setSelectedIcon(PluginServices.getIconTheme().get("cap-butt-selected"));
		toCenter = new JPanel(flowCenteredLayout);
		toCenter.add(capBut);
		c.add(toCenter);
		toCenter = new JPanel(flowCenteredLayout);
		toCenter.add(new JLabel(PluginServices.getText(this, "cap_butt")));
		c.add(toCenter);
		pnlCap.add(c);

		PluginServices.getIconTheme().registerDefault(
				"cap-round",
				this.getClass().getClassLoader().getResource("images/cap_round.png"));
		PluginServices.getIconTheme().registerDefault(
				"cap-round-selected",
				this.getClass().getClassLoader().getResource("images/cap_round-selected.png"));
		img= PluginServices.getIconTheme().get("cap-round");


		JPanel a = new JPanel();
		a.setLayout(new BoxLayout(a, BoxLayout.Y_AXIS));
		capRound = new JRadioButton(img);
		capRound.setSelectedIcon(PluginServices.getIconTheme().get("cap-round-selected"));
		toCenter = new JPanel(flowCenteredLayout);
		toCenter.add(capRound);
		a.add(toCenter);
		toCenter = new JPanel(flowCenteredLayout);
		toCenter.add(new JLabel(PluginServices.getText(this, "cap_round")));
		a.add(toCenter);
		pnlCap.add(a);

		PluginServices.getIconTheme().registerDefault(
				"cap-square",
				this.getClass().getClassLoader().getResource("images/cap_square.png"));
		PluginServices.getIconTheme().registerDefault(
				"cap-square-selected",
				this.getClass().getClassLoader().getResource("images/cap_square-selected.png"));
		img= PluginServices.getIconTheme().get("cap-square");

		JPanel b = new JPanel();
		b.setLayout(new BoxLayout(b, BoxLayout.Y_AXIS));
		capSquare = new JRadioButton(img);
		capSquare.setSelectedIcon(PluginServices.getIconTheme().get("cap-square-selected"));
		toCenter = new JPanel(flowCenteredLayout);
		toCenter.add(capSquare);

		b.add(toCenter);
		toCenter = new JPanel(flowCenteredLayout);
		toCenter.add(new JLabel(PluginServices.getText(this, "cap_square")));
		b.add(toCenter);
		pnlCap.add(b);

		ButtonGroup groupCap = new ButtonGroup();
		groupCap.add(capBut);
		groupCap.add(capRound);
		groupCap.add(capSquare);
		aux3.add(pnlCap);

		JPanel aux4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		aux4.add(new JBlank(5,40));
		pe = new PatternEditor();
		pe.setPreferredSize(new Dimension(440,40));
		aux4.add(pe);
		aux4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), PluginServices.getText(this, "fill_pattern")+":"));

		JPanel aux5 = new JPanel(new FlowLayout(FlowLayout.LEFT,15, 0));
		clearButton = new JButton(PluginServices.getText(this, "clear"));
		aux5.add(clearButton);

		joinBevel.addActionListener(this);
		joinMiter.addActionListener(this);
		joinRound.addActionListener(this);
		capBut.addActionListener(this);
		capRound.addActionListener(this);
		capSquare.addActionListener(this);
		clearButton.addActionListener(clear);
		pe.addActionListener(patternChange);

		JPanel aux = new JPanel(new FlowLayout(FlowLayout.LEFT));
		aux.add(aux2, BorderLayout.CENTER);
		aux.add(aux3, BorderLayout.CENTER);
		aux.add(aux4, BorderLayout.CENTER);
		aux.add(aux5, BorderLayout.WEST);
		add(aux, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e) {
		callValueChanged(getLinePropertiesStyle());
	}

	private ActionListener patternChange = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			callValueChanged(getLinePropertiesStyle());
		}
	};

	private ActionListener clear = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			pe.clear_Dash();
			callValueChanged(getLinePropertiesStyle());
		}
	};



	public void setLinePropertiesStyle(BasicStroke str) {

		if (str == null) {
			str = new BasicStroke();
		}

		switch(str.getLineJoin()) {
		case 0:
			joinMiter.setSelected(true);
			break;
		case 1:
			joinRound.setSelected(true);
			break;
		case 2:
			joinBevel.setSelected(true);
			break;
		}

		switch(str.getEndCap()) {
		case 0:
			capBut.setSelected(true);
			break;
		case 1:
			capRound.setSelected(true);
			break;
		case 2:
			capSquare.setSelected(true);
			break;
		}

		pe.setDash(str.getDashArray());
		pe.repaint();
	}

	public BasicStroke getLinePropertiesStyle() {
		int capType=0, joinType=0;


		if (capBut.isSelected())			capType = 0;
		else if (capRound.isSelected())		capType = 1;
		else if (capSquare.isSelected())	capType = 2;

		if (joinMiter.isSelected())			joinType = 0;
		else if (joinRound.isSelected())	joinType = 1;
		else if (joinBevel.isSelected())	joinType = 2;

		float [] dash = pe.getDash();

		BasicStroke str = new BasicStroke(width, capType, joinType, 10, pe.getDash(), 0);
		pe.setDash(dash);
		return str;
	}
}
