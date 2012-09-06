package org.gvsig.tools.undo.command.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.gvsig.tools.observer.Observer;
import org.gvsig.tools.observer.impl.DelegateWeakReferencingObservable;
import org.gvsig.tools.undo.RedoException;
import org.gvsig.tools.undo.UndoException;
import org.gvsig.tools.undo.command.Command;
import org.gvsig.tools.undo.command.CommandNotification;
import org.gvsig.tools.undo.command.UndoRedoCommandStack;

/**
 * Clase en memoria para registrar y gestionar los comandos que vamos
 * realizando. La forma en que ha sido implementada esta clase, en vez de una
 * única lista para albergar los comandos de deshacer(undos) y los de
 * rehacer(redos), se ha optado por dos pilas una para deshacer(undos) y otra
 * para rehacer(redos), de esta forma : Cuando se añade un nuevo comando, se
 * inserta este a la pila de deshacer(undos) y se borra de la de rehacer(redos).
 * Si se realiza un deshacer se desapila este comando de la pila deshacer(undos)
 * y se apila en la de rehacer(redos). Y de la misma forma cuando se realiza un
 * rehacer se desapila este comando de la pila de rehacer(redos) y pasa a la de
 * deshacer(undos).
 *
 * @author Vicente Caballero Navarro
 */
public class DefaultUndoRedoCommandStack implements UndoRedoCommandStack {
	private Stack undos = new Stack();
    private Stack redos = new Stack();
    private DelegateWeakReferencingObservable delegateObservable = new DelegateWeakReferencingObservable(
            this);
    private boolean complex = false;
    private CompoundCommand collection = null;

	public void add(Command command) {
		if (complex) {
            collection.add(command);
        } else {
            undos.add(command);
            redos.clear();
            delegateObservable.notifyObservers(new DefaultCommandNotification(
                    command, CommandNotification.ADD));
        }
    }

	public void undo() throws UndoException {
		AbstractCommand command = (AbstractCommand) undos.pop();
        command.undo();
        redos.add(command);
        delegateObservable.notifyObservers(new DefaultCommandNotification(
                command, CommandNotification.UNDO));
    }

	public void redo() throws RedoException {
		AbstractCommand command = (AbstractCommand) redos.pop();
        command.redo();
        undos.add(command);
        delegateObservable.notifyObservers(new DefaultCommandNotification(
                command, CommandNotification.REDO));
    }

    public void redo(int commands) throws RedoException {
        for (int i = 0; i < commands; i++) {
            redo();
        }
    }

    public void undo(int commands) throws UndoException {
        for (int i = 0; i < commands; i++) {
            undo();
        }
    }

	public boolean canUndo() {
        return (!undos.isEmpty());
    }

	public boolean canRedo() {
        return (!redos.isEmpty());
    }

	public List getUndoInfos() {
        Stack clonedUndos = (Stack) undos.clone();

		ArrayList commands = new ArrayList();
        while (!clonedUndos.isEmpty()) {
            commands.add(clonedUndos.pop());
        }

		return commands;
    }

	public List getRedoInfos() {
        Stack clonedRedos = (Stack) redos.clone();

		ArrayList commands = new ArrayList();
        while (!clonedRedos.isEmpty()) {
            commands.add(clonedRedos.pop());
        }
        return commands;
    }

	public int size() {
        return undos.size() + redos.size();
    }

	public void clear() {
        redos.clear();
        undos.clear();
    }

	public Command getNextUndoCommand() {
        return (Command) undos.peek();
    }

	public Command getNextRedoCommand() {
        return (Command) redos.peek();
    }

	public void addObserver(Observer o) {
        delegateObservable.addObserver(o);

	}

	public void deleteObserver(Observer o) {
        delegateObservable.deleteObserver(o);
    }

	public void deleteObservers() {
        delegateObservable.deleteObservers();
    }

	public void endComplex() {
        if (collection.isEmpty()) {
            complex = false;
            return;
        }
		complex = false;
        undos.add(collection);
        redos.clear();
        delegateObservable.notifyObservers(new DefaultCommandNotification(
                collection, CommandNotification.ADD));

	}

	public void startComplex(String description) {
        collection = new CompoundCommand(description);
        complex = true;
    }

	public boolean inComplex() {
		return complex;
	}

}
