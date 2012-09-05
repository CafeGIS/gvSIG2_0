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
*/

package com.iver.cit.gvsig.animation.traks;

import java.util.List;

import com.iver.cit.gvsig.animation.IAnimationType;
import com.iver.cit.gvsig.animation.interval.IAnimationInterval;
import com.iver.utiles.IPersistence;

public interface IAnimationTrack extends IPersistence{

	// class information
	public String getName();

	public String getDescription();

	public boolean isEnable();

	public void setEnable(boolean enable);

	public IAnimationType getAnimationType();

	// Interval methods
	public List getIntervalList();

	public void setIntervalList(List intervalList);

	public void removeAllIntervals();

	public void removeInterval(IAnimationInterval animationInterval);
	
	public void apply(double Tini,double Tend);

	// Animated object method
	public Object getAnimatedObject();

	public void setAnimatedObject(Object animatedObject);

	
}