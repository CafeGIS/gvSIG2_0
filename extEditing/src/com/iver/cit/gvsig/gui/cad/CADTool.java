/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ib��ez, 50
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
package com.iver.cit.gvsig.gui.cad;
import java.awt.Graphics;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import org.gvsig.fmap.dal.exception.DataException;

import com.iver.cit.gvsig.gui.cad.exception.CommandException;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 */
public interface CADTool {


	public static int TOPGEOMETRY = 2000;

	public void init();
	public void end();
	public void transition(double x, double y, InputEvent event);
	public void transition(double d);
	public void transition(String s) throws CommandException;
	public void addPoint(double x,double y,InputEvent event);
	public void addValue(double d);
	public void addOption(String s);
	public void setQuestion(String s);

	/**
	 * Recibe un graphics en el que se encuentra dibujada la
	 * EditableFeatureSource que se pasa como par�metro. En este m�todo, la
	 * herramienta ha de implementar el dibujado de la operaci�n que se est�
	 * realizando dependiendo del estado. Por ejemplo al dibujar un c�rculo
	 * mediante 3 puntos, cuando la herramienta se encuentre en el estado en
	 * el que s�lo falta un punto, se dibujar� el c�rculo teniendo en cuenta
	 * como tercer punto el puntero del rat�n (pasado en los par�metros x e
	 * y). Este m�todo es invocado tras cada transici�n y cada vez que se
	 * mueve el rat�n.
	 *
	 * @param g DOCUMENT ME!
	 * @param efs DOCUMENT ME!
	 * @param selectedGeometries DOCUMENT ME!
	 * @param x DOCUMENT ME!
	 * @param y DOCUMENT ME!
	 */
	void drawOperation(Graphics g,double x, double y);

	/**
	 * Obtiene la pregunta que saldr� en la consola relativa al estado en el
	 * que se encuentra la herramienta
	 *
	 * @return DOCUMENT ME!
	 */
	String getQuestion();

	/**
	 * DOCUMENT ME!
	 *
	 * @param cta DOCUMENT ME!
	 */
	public void setCadToolAdapter(CADToolAdapter cta);

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public CADToolAdapter getCadToolAdapter();
	public String[] getDescriptions();
	public void setDescription(String[] descriptions);
	public String getName();
	public VectorialLayerEdited getVLE();
	void clearSelection() throws DataException;
	public boolean isApplicable(int shapeType);
	public void setPreviosTool(DefaultCADTool tool);
	public void restorePreviousTool();
	public void endTransition(double x, double y, MouseEvent e);
}
