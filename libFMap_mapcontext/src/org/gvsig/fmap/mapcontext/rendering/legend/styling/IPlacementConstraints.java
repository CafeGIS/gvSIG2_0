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

/* CVS MESSAGES:
 *
 * $Id: IPlacementConstraints.java 13913 2007-09-20 09:36:02Z jaume $
 * $Log$
 * Revision 1.12  2007-09-20 09:33:15  jaume
 * Refactored: fixed name of IPersistAnce to IPersistence
 *
 * Revision 1.11  2007/09/10 15:47:11  jaume
 * *** empty log message ***
 *
 * Revision 1.10  2007/07/18 06:54:34  jaume
 * continuing with cartographic support
 *
 * Revision 1.9  2007/04/18 15:35:11  jaume
 * *** empty log message ***
 *
 * Revision 1.8  2007/04/13 12:42:32  jaume
 * *** empty log message ***
 *
 * Revision 1.7  2007/04/13 11:59:30  jaume
 * *** empty log message ***
 *
 * Revision 1.6  2007/04/12 16:01:11  jaume
 * *** empty log message ***
 *
 * Revision 1.5  2007/04/12 14:28:43  jaume
 * basic labeling support for lines
 *
 * Revision 1.4  2007/04/11 16:01:08  jaume
 * maybe a label placer refactor
 *
 * Revision 1.3  2007/04/02 16:34:56  jaume
 * Styled labeling (start commiting)
 *
 * Revision 1.2  2007/03/09 08:33:43  jaume
 * *** empty log message ***
 *
 * Revision 1.1.2.2  2007/02/15 16:23:44  jaume
 * *** empty log message ***
 *
 * Revision 1.1.2.1  2007/02/09 07:47:05  jaume
 * Isymbol moved
 *
 *
 */

package org.gvsig.fmap.mapcontext.rendering.legend.styling;

import com.iver.utiles.IPersistence;

/**
 * @author   jaume dominguez faus - jaume.dominguez@iver.es
 */
public interface IPlacementConstraints extends IPersistence, Cloneable {
	public static int DefaultDuplicateLabelsMode = IPlacementConstraints.ONE_LABEL_PER_FEATURE_PART;

	// constants regarding label duplication
	public static final int REMOVE_DUPLICATE_LABELS             =       2;
	public static final int ONE_LABEL_PER_FEATURE               =       3;
	public static final int ONE_LABEL_PER_FEATURE_PART          =       4;

	// constants regarding point settings
	public static final int OFFSET_HORIZONTALY_AROUND_THE_POINT =       5;
	public static final int ON_TOP_OF_THE_POINT                 =       6;
	public static final int AT_SPECIFIED_ANGLE                  =       7;
	public static final int AT_ANGLE_SPECIFIED_BY_A_FIELD       =       8;

	// constants regarding polygon settings (also apply for lines)
	public static final int HORIZONTAL                          =       9;
	public static final int PARALLEL                            =      10;

	// constants regarding line settings
	public static final int FOLLOWING_LINE                      =      11;
	public static final int PERPENDICULAR                 	    =      12;

	// constants regarding the location along the line
	public static final int AT_THE_END_OF_THE_LINE              =      13;
	public static final int AT_THE_MIDDLE_OF_THE_LINE           =      14;
	public static final int AT_THE_BEGINING_OF_THE_LINE         =      15;
	public static final int AT_BEST_OF_LINE 					=	   16;

	public abstract void setPlacementMode(int mode);

	// regarding label position along the line
	public abstract boolean isBelowTheLine();
	public abstract void setBelowTheLine(boolean b);
	public abstract boolean isAboveTheLine();
	public abstract void setAboveTheLine(boolean b);
	public abstract boolean isOnTheLine();
	public abstract void setOnTheLine(boolean b);
	public abstract void setLocationAlongTheLine(int location);


	// regarding the orientation system
	public abstract boolean isPageOriented();
	public abstract void setPageOriented(boolean b);

	// regarding the label duplication
	public abstract void setDuplicateLabelsMode(int mode);
	public abstract int getDuplicateLabelsMode();


	public abstract boolean isParallel();

	public abstract boolean isFollowingLine();

	public abstract boolean isPerpendicular();

	public abstract boolean isHorizontal();

	public abstract boolean isAtTheBeginingOfLine();

	public abstract boolean isInTheMiddleOfLine();

	public abstract boolean isAtTheEndOfLine();

	public boolean isAtBestOfLine();

	public abstract boolean isOnTopOfThePoint();

	public abstract boolean isAroundThePoint();

	public boolean isFitInsidePolygon();

	public void setFitInsidePolygon(boolean b);


}