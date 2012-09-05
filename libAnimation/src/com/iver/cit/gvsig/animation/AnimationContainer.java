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

package com.iver.cit.gvsig.animation;

import java.util.ArrayList;
import java.util.List;

import com.iver.cit.gvsig.animation.traks.AnimationDatedTrack;
import com.iver.cit.gvsig.animation.traks.AnimationTimeTrack;
import com.iver.cit.gvsig.animation.traks.IAnimationTrack;
import com.iver.utiles.IPersistence;
import com.iver.utiles.XMLEntity;

public class AnimationContainer implements IPersistence {

	// IAnimationTrakc List.
	public List<IAnimationTrack>  AnimationTrackList;

	private int trackListSize=0;

	private  AnimationPlayer animationPlayer = new AnimationPlayer();

	public AnimationContainer() {
		List<IAnimationTrack> aniTrackList = this.getAnimationTrackList();
		aniTrackList = new ArrayList<IAnimationTrack>();
		this.setAnimationTrackList(aniTrackList);
	}

	/**
	 * @param animationType
	 * @return
	 */
	public List<IAnimationTrack> getTackListOfType(IAnimationType animationType) {
		List<IAnimationTrack> typeList = new ArrayList<IAnimationTrack>();
		for (int i = 0; i < this.AnimationTrackList.size(); i++) {
			IAnimationTrack trackElement = (IAnimationTrack) this.AnimationTrackList
					.get(i);
			if (animationType.getClassName().equals(
					trackElement.getAnimationType().getClassName())) {
				typeList.add(trackElement);
			}

		}
		return typeList;
	}

	/**
	 * @param animationType
	 * @return
	 */
	public IAnimationTrack CreateDatedTrack(IAnimationType animationType) {

		List<IAnimationTrack> aniTrackList = this.getAnimationTrackList();
		if (aniTrackList == null) {
			aniTrackList = new ArrayList<IAnimationTrack>();
		}
		AnimationDatedTrack ADTrack = new AnimationDatedTrack(animationType);
		aniTrackList.add(ADTrack);
		this.setAnimationTrackList(aniTrackList);
		return ADTrack;
	}

	public IAnimationTrack CreateTimeTrack(IAnimationType animationType) {

		List<IAnimationTrack> aniTrackList = this.getAnimationTrackList();
		if (aniTrackList == null) {
			aniTrackList = new ArrayList<IAnimationTrack>();
		}
		AnimationTimeTrack ADTrack = new AnimationTimeTrack(animationType);
		aniTrackList.add(ADTrack);
		this.setAnimationTrackList(aniTrackList);
		return ADTrack;
	}

	/**
	 * @param animationTrack
	 */
	public void addTrack(IAnimationTrack animationTrack) {
		IAnimationTrack track = findTrack(animationTrack.getName());
		if (track == null) {
			this.AnimationTrackList.add(animationTrack);
		}
	}

	/**
	 * @param name
	 * @return
	 */
	public IAnimationTrack findTrack(String name) {

		IAnimationTrack IAT = null;
		for (int i = 0; i < this.AnimationTrackList.size(); i++) {
			IAnimationTrack trackElement = (IAnimationTrack) this.AnimationTrackList
					.get(i);
			if (trackElement.getName().equals(name)) {
				IAT = trackElement;
			}

		}
		return IAT;
	}

	/**
	 * @param animationTrack
	 */
	public void removeTrack(IAnimationTrack animationTrack) {
		animationTrack.removeAllIntervals();
		this.AnimationTrackList.remove(animationTrack);
	}

	/**
	 * 
	 */
	public void removeAllTrack() {
		for (int i=AnimationTrackList.size() - 1; i>=0; i--) {
			removeTrack(AnimationTrackList.get(i));
		}
		
//		for (Iterator<IAnimationTrack> iterator = AnimationTrackList.iterator(); iterator.hasNext();) {
//			((IAnimationTrack) iterator.next()).removeAllIntervals();
////			removeTrack(iterator.next());
//		}
//		AnimationTrackList.clear();
	}

	/**
	 * @return
	 */
	public List<IAnimationTrack> getAnimationTrackList() {
		return AnimationTrackList;
	}

	
	public boolean isEmpty() {
		
		if(this.AnimationTrackList.size() == 0)
			return true;
		else
			return false;
		
	}
	
	/**
	 * @param animationTrackList
	 */
	public void setAnimationTrackList(List<IAnimationTrack> animationTrackList) {
		AnimationTrackList = animationTrackList;
	}
	
	public String toString() {
		String result = "";
		List<IAnimationTrack> ATL = this.AnimationTrackList;
		result += "Mostrando lista de tracks:";
		if ((ATL == null) || ATL.isEmpty()) {
			result += "\nLista vacia";
		} else {
			for (int i = 0; i < ATL.size(); i++) {
				Object element = ATL.get(i);
				result += "\n" + element;
			}
		}
		return result;

	}

	public void apply(double Tini, double Tend) {
		List<IAnimationTrack> ATL = this.AnimationTrackList;
//		System.out.println("Tiempo de inicio: " + Tini + " tiempo final "
//				+ Tend);
		if ((ATL != null) && !ATL.isEmpty()) {
			for (int i = 0; i < ATL.size(); i++) {
				IAnimationTrack element = (IAnimationTrack) ATL.get(i);
//				System.out.println("track: " + element.getName() + "OBJETO" + element.getAnimatedObject());
				element.apply(Tini, Tend);
				if (element.isEnable()){}
			}
		}
	}
	
	
	/**
	 * Return the name of this class.
	 * @return name.
	 */
	
	public String getClassName() {
		return this.getClass().getName();
	}
	
	/**
	 * Return the player associated with the container.
	 * @return animationPlayer.
	 */
	
	public AnimationPlayer getAnimationPlayer(){
		return this.animationPlayer;
	}
	
	/*
	 * IPersistence methods.
	 */
	
	public XMLEntity getXMLEntity() {
		XMLEntity xml = new XMLEntity();
		
		xml.putProperty("className", this.getClassName());
		xml.putProperty("animationContainer", "true");
		xml.putProperty("trackListSize", this.AnimationTrackList.size());
		
		xml.addChild(((IPersistence)animationPlayer).getXMLEntity());
		
		for (int i = 0; i < this.AnimationTrackList.size(); i++) {			
			IAnimationTrack trackElement = (IAnimationTrack) this.AnimationTrackList.get(i);			
			xml.addChild(((IPersistence)trackElement).getXMLEntity());
		}
		return xml;
	}

	
	

	/**
	 * @param xml
	 */
	public void setXMLEntity(XMLEntity xml) {
		
		String class_name;
		
		if (xml.contains("trackListSize"))
			this.trackListSize = xml.getIntProperty("trackListSize");
		 
		// Node AnimationPlayer in the animation tree. Calling setXmlEntity() methods.
		// Reconstruyendo el framework. Paso: accediendo a la configuracion del player.
		
		this.animationPlayer.setXMLEntity(xml.getChild(0));
		
		
		// Node IAnimationTrack in the animation tree. Calling setXmlEntity() methods.
		// Reconstruyendo el framework. Paso: nuevo IAnimationTrack, AnimationDateTrack o AnimationTimeTrack.
		
		List<IAnimationTrack> trackList = new ArrayList<IAnimationTrack>();
		for (int i = 1; i <= this.trackListSize; i++) {//desde el hijo 1 que es el primer track hasta el último track.
			XMLEntity xmlTrack = xml.getChild(i);
			class_name = xmlTrack.getStringProperty("className");//Accedemos al tipo de track.
				try {
					Class<?> classTrack = Class.forName(class_name);
					Object obj = classTrack .newInstance();
					IPersistence objPersist = (IPersistence) obj;
					objPersist.setXMLEntity(xmlTrack);
					IAnimationTrack trackElement = (IAnimationTrack) obj;
					trackElement.setEnable(true);
					trackList.add(trackElement);
				} catch (Exception e) {
					e.printStackTrace();
			}
		}
		this.setAnimationTrackList(trackList);

	}

}