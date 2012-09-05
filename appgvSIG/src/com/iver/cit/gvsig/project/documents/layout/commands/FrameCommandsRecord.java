package com.iver.cit.gvsig.project.documents.layout.commands;

import org.gvsig.tools.undo.command.impl.DefaultUndoRedoCommandStack;

import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;

/**
 * Clase en memoria para registrar y gestionar los comandos que vamos
 * realizando. La forma en que ha sido implementada esta clase, en vez de una
 * única lista para albergar los comandos de deshacer(undos) y los de
 * rehacer(redos), se ha optado por dos pilas una para deshacer(undos) y otra
 * para rehacer(redos), de esta forma :  Cuando se añade un nuevo comando, se
 * inserta este a la pila de deshacer(undos) y se borra de la de
 * rehacer(redos). Si se realiza un deshacer se desapila este comando de la
 * pila deshacer(undos) y se apila en la de rehacer(redos). Y de la misma
 * forma cuando se realiza un rehacer se desapila este comando de la pila de
 * rehacer(redos) y pasa a la de deshacer(undos).
 *
 * @author Vicente Caballero Navarro
 */
public class FrameCommandsRecord extends DefaultUndoRedoCommandStack {
	private FrameManager expansionManager;
	public FrameCommandsRecord(FrameManager expansionManager){
		this.expansionManager=expansionManager;
	}
	public void insert(IFFrame frame) {
		InsertFrameCommand command=new InsertFrameCommand(expansionManager, frame,frame.getName());
        add(command);
        command.execute();
    }

	public void delete(IFFrame frame) {
		DeleteFrameCommand command=new DeleteFrameCommand(expansionManager, frame,frame.getName());
        add(command);
        command.execute();
    }

	public void update(IFFrame frame, IFFrame oldFrame) {
		UpdateFrameCommand command=new UpdateFrameCommand(expansionManager,frame, oldFrame,frame.getName());
		add(command);
		command.execute();
	}
	public void clear() {
		super.clear();
		expansionManager.clear();
	}
	public FrameManager getFrameManager() {
		return expansionManager;
	}

}
