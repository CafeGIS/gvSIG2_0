package com.iver.cit.gvsig.gui.command;

import javax.swing.table.AbstractTableModel;

import org.gvsig.tools.undo.RedoException;
import org.gvsig.tools.undo.UndoException;
import org.gvsig.tools.undo.UndoRedoStack;
import org.gvsig.tools.undo.command.Command;


public class MyModel extends AbstractTableModel{
private UndoRedoStack cr;
public MyModel(UndoRedoStack cr) {
	this.cr=cr;
	}
	public int getPos() {
		return cr.getUndoInfos().size()-1;
	}
	public int getColumnCount() {
		return 1;
	}
	public int getRowCount() {
		return cr.getRedoInfos().size()+cr.getUndoInfos().size();
	}
	public Object getValueAt(int i, int j) {
		Command[] undos=(Command[])cr.getUndoInfos().toArray(new Command[0]);
		Command[] redos=(Command[])cr.getRedoInfos().toArray(new Command[0]);
		if (i<undos.length){
			//System.out.println("undo i=" + i + " index=" + (undos.length-1-i));
			return undos[undos.length-1-i];
		}else{
			//System.out.println("redo i=" + i + " index=" + (i-undos.length));
			return redos[i-undos.length];
		}
	}
	public void setPos(int newpos) {
		try {
			if (newpos>getPos()){
				cr.redo(newpos-getPos());

			}else{
				cr.undo(getPos()-newpos);
			}
		} catch (RedoException e) {
			e.printStackTrace();
		} catch (UndoException e) {
			e.printStackTrace();
		}
	}
}
