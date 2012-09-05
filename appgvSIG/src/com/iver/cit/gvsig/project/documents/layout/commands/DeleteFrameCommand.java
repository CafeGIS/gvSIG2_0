/*
 * Created on 10-feb-2005
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package com.iver.cit.gvsig.project.documents.layout.commands;


import java.io.IOException;

import org.gvsig.tools.undo.command.Command;
import org.gvsig.tools.undo.command.impl.AbstractCommand;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;



public class DeleteFrameCommand extends AbstractCommand {
	private FrameManager fm;
	private int index;
	public DeleteFrameCommand(FrameManager fm,IFFrame frame, String description) {
		super(description);
		this.fm=fm;
		for (int i = 0; i < fm.getAllFFrames().length; i++) {
            if (fm.getFFrame(i).equals(frame)) {
                index=i;
            }
        }
}

	/**
	 * @throws DriverIOException
	 * @throws IOException
	 * @see com.iver.cit.gvsig.fmap.edition.Command#undo()
	 */
	public void undo(){
		fm.undoRemoveFFrame(index);
	}
	/**
	 * @throws IOException
	 * @throws DriverIOException
	 * @see com.iver.cit.gvsig.fmap.edition.Command#redo()
	 */
	public void redo(){
		fm.doRemoveFFrame(index);
	}

	public int getType() {
		return Command.DELETE;
	}

	public void execute() {
		if (fm.invalidates().get(index)) {
            return;
        }
//        IFFrame frame=fm.getFFrame(index);
        fm.doRemoveFFrame(index);

	}

}
