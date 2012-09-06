package org.gvsig.tools.undo.command;

/**
 * This interface represents a notification used by 
 * the commands record to indicate an action over a command.
 *
 */
public interface CommandNotification {

	/** The command has been added to the command record */
	public static final String ADD = "add_Command";
	/** The command has been undone */
	public static final String UNDO = "undo_Command";
	/** The command has been redone */ 
	public static final String REDO = "redo_Command";

	/**
	 * Returns the Command over which the action is executed
	 * @return Command over which the action is executed
	 */
	public Command getCommand();

	/**
	 * Returns this notification's type. One of the constants defined in this interface.
	 *
	 * @return a String containing this notification's type.
	 */
	public String getType();

}