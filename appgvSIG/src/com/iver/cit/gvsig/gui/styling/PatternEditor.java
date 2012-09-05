package com.iver.cit.gvsig.gui.styling;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PatternEditor extends JPanel {
	private static final int NULL_STATE = 0;
	private static final int EMPTY_STATE = 1;
	private static final int FILL_STATE = 2;
	private static final int END_MARK_STATE = 3;
	private int dividers = 9;
	private int subDividersPerDivider = 5;
	private int separations = dividers * subDividersPerDivider;
	private int vGap = 5; // pixels
	private int hGap = 5;
	private float dash[];
	private int states[] = new int[separations];
	private MyMouseListener mouseBehavior = new MyMouseListener();
	private Rectangle bounds;
	private int width,height,halfHeight,separation;
	private ArrayList<ActionListener> listeners=new ArrayList<ActionListener>();

	public PatternEditor() {
		super();
		addMouseListener(mouseBehavior);
		addMouseMotionListener(mouseBehavior);
		for (int i = 0; i < states.length; i++) {
			states[i] = NULL_STATE;
		}
		states[0] = END_MARK_STATE;
	}

	public float[] getDash() {
		return dash;
	}

	public void setDash(float[] dash) {
		this.dash=dash;
		obtain_states(dash);
		repaint();
	}

	private void obtain_states(float[] dash) {
		int pos=0;
		if(dash == null) return;

		//Para el resto de casos
		for (int i = 0; i < dash.length; i++) {
			if(i%2 == 0) {
				for (int j = 0; j < dash[i]; j++) {
					states[pos]=FILL_STATE;
					pos++;
				}
			}
			else {
				for (int j = 0; j < dash[i]; j++) {
					states[pos]=EMPTY_STATE;
					pos++;
				}
			}
		}
		states[pos]=END_MARK_STATE;


	}

	private void create_Dash() {
		if (states[0] == NULL_STATE) dash=null;
		else {
			int divisions = 1;
			boolean firstfilled = true;
			
			//To create the divisions of the dash
			if (states[0] == EMPTY_STATE) {
				divisions ++;
				firstfilled = false;
			}
			int k=1;
			if(states[0] != END_MARK_STATE ) {
				while (states[k] != END_MARK_STATE) {
					if(states[k] != states[k-1])divisions++;
					k++;
				}
			}
			
			
			//To fill the dash array
			int pos=0;
			if(divisions ==1)dash=null;
			else {
				dash = new float[divisions];
				if(!firstfilled) {
					dash[0]=0;
					pos++;
					firstfilled = true;
				}
				dash[pos]=1;

				int i=1;
				while(states[i]!= END_MARK_STATE) {
					if(states[i] == states[i-1])dash[pos]++;
					else {
						pos++;
						dash[pos]=1;
					}
					i++;
				}
			}
			for (int i = 0; dash!=null &&  i < dash.length; i++) {
				System.out.print(dash[i]+" ");
			}
			System.out.print("\n");
			
		}	
		for (int i=0;i<listeners.size();i++) {
			listeners.get(i).actionPerformed(new ActionEvent(this,ActionEvent.ACTION_FIRST,"Released"));	
		}
	}

	public void clear_Dash() {
		dash = null;
		for (int i = 0; i < states.length; i++) {
			states[i] = NULL_STATE;
		}
		states[0] = END_MARK_STATE;
		repaint();
		create_Dash();

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		bounds = getBounds();
		width = bounds.width;	
		height = bounds.height-2*vGap;
		halfHeight = height / 2;
		separation = width / separations;
		for (int i = 0; i <= separations; i++) {
			if (i%subDividersPerDivider == 0) {
				g.drawLine(hGap+separation*i, vGap, hGap+separation*i, vGap+halfHeight-3);
			} else {
				g.drawLine(hGap+separation*i, vGap+(halfHeight/2), hGap+separation*i, vGap+halfHeight-3);
			}

		}

		for(int i = 0; i < states.length ; i++) {
			switch (states[i]) {
			case END_MARK_STATE:
				g.setColor(Color.GRAY);
				break;
			case NULL_STATE:
				continue;
			case FILL_STATE:
				g.setColor(Color.BLACK);
				break;
			case EMPTY_STATE:
				g.setColor(Color.WHITE);
				break;
			}
			bounds.setLocation(0, 0);
			g.fillRect(hGap+separation*i, (vGap)+height/2, separation, halfHeight);
			
		}
		bounds.setLocation(0, 0);
		g.drawRect(hGap,(vGap)+height/2 ,separation*separations,halfHeight);

	}
	public static void main(String[] args) {
		JFrame f = new JFrame();
		final PatternEditor pe1 = new PatternEditor(); 
		final PatternEditor pe2 = new PatternEditor();
		JPanel content = new JPanel(new GridLayout(2, 1));
		pe1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pe2.setDash(pe1.getDash());
			}
		});
		
		content.add(pe1);
		content.add(pe2);
		f.setContentPane(content);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setVisible(true);

	}

	private class MyMouseListener implements MouseListener, MouseMotionListener {
		private Point initialPoint = null;
		private Point endPoint = null;
		
	
		public void mouseEntered(MouseEvent e) { /* nothing */ }
		public void mouseExited(MouseEvent e){ /* nothing */ }
		public void mouseReleased(MouseEvent e) { /* nothing */ }
		public void mouseMoved(MouseEvent e) { /* nothing */ }

		public void mouseClicked(MouseEvent e){ 
			initialPoint = getPoint(e);
		}
		
		public void mousePressed(MouseEvent e) {
			initialPoint = getPoint(e);
			if(isValidPoint(initialPoint)) {
				if (states[getRect(initialPoint)]== EMPTY_STATE)
					states[getRect(initialPoint)] = FILL_STATE;
				else if (states[getRect(initialPoint)]== FILL_STATE)
					states[getRect(initialPoint)] = EMPTY_STATE;
				
				repaint();
				create_Dash();
			}
		}
		private Point getPoint(MouseEvent e) {
			Point p = new Point();//getLocation();
			Point p2 = e.getPoint();
			return new Point(p2.x - p.x, p2.y - p.y);
		}

		private synchronized void doIt() {
			if(!isValidPoint(endPoint) || !isValidPoint(initialPoint))return;
			
			if(isValidPoint(endPoint) && isValidPoint(initialPoint)) {
				int stateIndex = getRect(initialPoint);
				if (stateIndex>=separations) return;
				switch (states[stateIndex]) {
				case END_MARK_STATE:
				case NULL_STATE:
					enlarge();
					break;
				case FILL_STATE:
					swap();
					break;
				}
				repaint();
				create_Dash();
			}
		}
		
		private void swap() {
			if(!isValidPoint(endPoint) || !isValidPoint(initialPoint))return;
			
			if(states[getRect(endPoint)] == EMPTY_STATE)
				states[getRect(endPoint)]= FILL_STATE;

			else return;
		}
		
		private synchronized void enlarge() {
			
			if(!isValidPoint(endPoint) || !isValidPoint(initialPoint))
				return;
			int index = getRect(endPoint);
			if (index >= separations)
				return;
			if(isValidPoint(endPoint) && isValidPoint(initialPoint)) {
				for (int i = index-1; i >= 0; i--) {
					if (states[i] != FILL_STATE)
						states[i] = EMPTY_STATE;
				}
				for (int i = index+1; i < states.length; i++) {
					states[i] = NULL_STATE;
				}
				
				
				states[index] = END_MARK_STATE;
			}
		}

		private boolean isValidPoint(Point p) {
			if (getRect(p)!= -1)
				return true;
			return false;
		}

		private int getRect(Point p) {
			if (( vGap+halfHeight <= p.y ) && ( p.y <= vGap+2*halfHeight ))
				if((hGap <= p.x) && (p.x <= hGap+(separation*separations)))
					return (p.x - hGap) / separation;
			return -1;
		}

		public void mouseDragged(MouseEvent e) {
			endPoint = getPoint(e);
			if(isValidPoint(endPoint)) {
				doIt();
			}
		}

	}

	public void addActionListener(ActionListener patternChange) {
		listeners.add(patternChange);
		
	}

}