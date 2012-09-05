package com.iver.cit.gvsig.project.documents.view.info.gui;

import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.html.HTMLEditorKit;

import org.gvsig.fmap.mapcontext.layers.operations.XMLItem;

import com.iver.utiles.BrowserControl;

/**
 * JPanel to show the feature information return in HTML code
 * @author laura
 *
 */
public class HTMLInfoToolPanel extends JPanel implements IInfoToolPanel{
		
	private boolean initialized = false;
	private JEditorPane editor = null;
	private JScrollPane scrollPane = null;

	public HTMLInfoToolPanel() throws HeadlessException {
		super();
	}
	
	private void init() {
		if (this.initialized ) return;
				
		this.setAutoscrolls(true);
		this.setLocation(0,0);

		scrollPane = new JScrollPane();		
		scrollPane.setAutoscrolls(true);
				
		editor = new JEditorPane();
		editor.setContentType("text/html");
		editor.setAutoscrolls(true);
		editor.setEditable(false);
		editor.addHyperlinkListener(new javax.swing.event.HyperlinkListener() { 
	          public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent e) {
	           if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
	           {
	        	   BrowserControl.displayURL(e.getURL().toString());
	           }
	          }
		});
		
//azabala		this.add(editor);
		
		
		this.setSize(new Dimension(640, 400));
		editor.setSize(new Dimension(640, 400));
		
		
		editor.setEditorKit(new HTMLEditorKit());
        scrollPane.setViewportView(editor);
//azabalA		
		this.add(scrollPane);
		
//azabala		scrollPane.setLocation(0,0);
	}
	
	public void show(String text) 
	{
		this.init();
		this.setVisible(true);	
		editor.setText(text.replaceFirst("Content-Type","Content-Typex"));		
	}

	public void refreshSize() {
		// TODO Auto-generated method stub
		
	}

	public void show(XMLItem item) {
		// TODO Auto-generated method stub
		
	}
}
