package com.iver.andami.help;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.help.BadIDException;
import javax.help.HelpSet;
import javax.help.JHelp;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelpPanel extends JPanel  {

	private static final long serialVersionUID = 4164482247505362337L;

	public static int WIDTH = 645;
	public static int HEIGHT = 495;
	private String title = null;
	private JHelp jh;

	private Logger log() {
		return LoggerFactory.getLogger("org.gvsig");
	}

	public HelpPanel(HelpSet hs){
		log().info("HelpPanel(hs)");
		init(hs,null);
	}

	public HelpPanel(HelpSet hs, String id){
		log().info("HelpPanel(hs,id) id="+id);
		init(hs,id);
	}

	private void init(HelpSet hs, String id){
		jh = new JHelp(hs);
		log().info("init() ID "+ id);
		if (id != null) {
			try {
				log().info("init() setCurrentID "+ id);
				jh.setCurrentID(id);
			} catch (BadIDException ex) {
				log().error(ex.toString());

			} catch (NullPointerException ex) {
				log().error(ex.toString());
			}
		}
		String hsTitle = hs.getTitle();
		if (hsTitle == null || hsTitle.equals("")) {
			hsTitle = "gvSIG Help";
		}
		title = hsTitle;
		setLayout(new BorderLayout());
		add(jh,BorderLayout.CENTER);
	}

	public void showWindow() {

		log().info("showWindow()");
		Frame frame = new Frame();
        frame.add(this);
        frame.setSize(WIDTH, HEIGHT + 30);
        frame.setTitle(getTitle());
        frame.setResizable(true);
		this.setVisible(true);
		frame.show();
	}

	public void showWindow(String id) {
		if (id != null) {
			try {
				log().info("showWindow(id) -> setCurrentID "+ id);
				jh.setCurrentID(id);
			} catch (BadIDException ex) {
				log().error(ex.toString());
			}
		}
		String hsTitle = jh.getHelpSetPresentation().getTitle();
		if (hsTitle == null || hsTitle.equals("")) {
			hsTitle = "gvSIG Help";
		}
		title = hsTitle;
		showWindow();
	}

	public String getTitle() {
		return this.title;
	}

}
