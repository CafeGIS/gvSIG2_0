package org.gvsig.tools.undo.command.impl;

import org.gvsig.tools.undo.command.Command;
import org.gvsig.tools.undo.command.CommandNotification;

public class DefaultCommandNotification implements CommandNotification {
	private Command command;
	private String type;

	public DefaultCommandNotification(Command command, String type) {
		this.command=command;
		this.type=type;
	}

	public Command getCommand() {
		return this.command;
	}

	public String getType() {
		return this.type;
	}
}
