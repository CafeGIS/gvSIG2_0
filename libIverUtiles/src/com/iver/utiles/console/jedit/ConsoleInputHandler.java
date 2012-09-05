package com.iver.utiles.console.jedit;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.iver.utiles.console.JConsole;

public class ConsoleInputHandler extends DefaultInputHandler {

	private ArrayList listeners=new ArrayList();
	public void keyPressed(KeyEvent evt) {
		super.keyPressed(evt);
		callConsolePressed(evt);
	}

	public void keyTyped(KeyEvent evt) {
		super.keyTyped(evt);
	}

	public void keyReleased(KeyEvent arg0) {
		callConsoleReleased(arg0);
	}

	public void addConsoleListener(JConsole console) {
		listeners.add(console);
	}
	private void callConsolePressed(KeyEvent e){
		for (int i=0;i<listeners.size();i++){
			((JConsole)listeners.get(i)).keyPressed(e);
		}
	}
	private void callConsoleReleased(KeyEvent e){
		for (int i=0;i<listeners.size();i++){
			((JConsole)listeners.get(i)).keyReleased(e);
		}
	}
}
