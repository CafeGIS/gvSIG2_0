
/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
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

/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
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
package org.gvsig.fmap.dal.feature.impl.undo.command;

import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.impl.FeatureManager;
import org.gvsig.fmap.dal.feature.impl.SpatialManager;
import org.gvsig.tools.undo.RedoException;
import org.gvsig.tools.undo.UndoException;


/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class FeatureUpdateCommand extends AbstractFeatureCommand {
    private Feature oldFeature;
    private int position=-1;

    public FeatureUpdateCommand(FeatureManager expansionManager,
        SpatialManager spatialManager, Feature feature, Feature oldFeature, String description) {
        super(expansionManager, spatialManager, feature, description);
        this.oldFeature = oldFeature;
    }

    /* (non-Javadoc)
    * @see org.gvsig.fmap.dal.commands.Command#undo()
    */
    public void undo() throws UndoException {
//        if (expansionManager.contains(oldFeature.getReference())) {
//            expansionManager.deleteLastFeature(feature.getReference());
//        } else {
//            expansionManager.delete(feature.getReference());
//        }

        expansionManager.restore(oldFeature.getReference(),position);
        spatialManager.updateFeature(oldFeature, feature);
    }

    /* (non-Javadoc)
     * @see org.gvsig.fmap.dal.commands.Command#redo()
     */
    public void redo() throws RedoException {
        execute();
    }

    public int getType() {
        return UPDATE;
    }

    /* (non-Javadoc)
     * @see org.gvsig.fmap.dal.commands.Command#execute()
     */
    public void execute() {
        //		 Si la geometría no ha sido modificada
//        if (!expansionManager.contains(oldFeature.getReference())) {
//        if (expansionManager.delete(oldFeature.getReference())!=null){
//            expansionManager.add(feature);
//        } else {
    	position = expansionManager.update(feature, oldFeature);
//        }

        spatialManager.updateFeature(feature, oldFeature); //setFullExtentDirty(true);
    }

	public Feature getOldFeature() {
		return oldFeature;
	}
}
