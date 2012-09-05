
package com.iver.cit.gvsig.project.documents.layout.commands;

import java.io.IOException;

import org.gvsig.tools.undo.command.Command;
import org.gvsig.tools.undo.command.impl.AbstractCommand;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;


/**
 * Añade una geometría nueva al EditableFeatureSource
 */
public class InsertFrameCommand extends AbstractCommand {
	private IFFrame frame;
	private int index;
	private FrameManager fm;
	//private DefaultEditableFeatureSource efs;
	public InsertFrameCommand(FrameManager fm,IFFrame f,String description){
		super(description);
		this.fm=fm;
		frame=f;
	}
	/**
	 * @throws IOException
	 * @throws DriverIOException
	 * @throws IOException
	 * @throws DriverIOException
	 * @see com.iver.cit.gvsig.fmap.edition.Command#undo()
	 */
	public void undo(){
		fm.undoAddFFrame(index);
	}
	/**
	 * @throws DriverIOException
	 * @throws IOException
	 * @see com.iver.cit.gvsig.fmap.edition.Command#redo()
	 */
	public void redo(){
		fm.doAddFFrame(frame,index);

	}
	public int getType() {
		return Command.INSERT;
	}
	public void execute() {
		index=fm.doAddFFrame(frame);

	}

}
