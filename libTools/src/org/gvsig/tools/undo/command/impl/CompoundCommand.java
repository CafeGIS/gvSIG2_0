package org.gvsig.tools.undo.command.impl;

import java.util.ArrayList;
import java.util.List;

import org.gvsig.tools.undo.RedoException;
import org.gvsig.tools.undo.UndoException;
import org.gvsig.tools.undo.command.Command;

public class CompoundCommand extends AbstractCommand {

    private List commands = new ArrayList();

    public CompoundCommand(String description) {
        super(description);
    }

    public boolean isEmpty() {
        return commands.size() == 0;
    }

    public void undo() throws UndoException {
        for (int i = commands.size() - 1; i >= 0; i--) {
            ((AbstractCommand) commands.get(i)).undo();
        }
    }

    public void redo() throws RedoException {
        for (int i = 0; i < commands.size(); i++) {
            ((AbstractCommand) commands.get(i)).redo();
        }
    }

    public void add(Command c) {
        commands.add(c);
    }
    
    public int getType() {
        if (isEmpty()) {
            return UPDATE;
        } else {
            return ((Command) commands.get(0)).getType();
        }
    }
}