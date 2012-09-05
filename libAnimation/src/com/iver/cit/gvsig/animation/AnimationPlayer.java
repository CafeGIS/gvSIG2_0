
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

import com.iver.cit.gvsig.animation.timer.AnimationTimer;
import com.iver.cit.gvsig.animation.timer.IUpdateCallBack;
import com.iver.utiles.IPersistence;
import com.iver.utiles.XMLEntity;

/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */

public class AnimationPlayer implements IUpdateCallBack,
		IPersistence {

	public static final int ANIMATION_PLAY = 0;

	public static final int ANIMATION_STOP = 1;

	public static final int ANIMATION_PAUSE = 2;

	public static final int ANIMATION_RECORD = 3;
	
	public static final int ANIMATION_INITIAL = 4;//Default initial state.
							   //our animation modes.
	public static final int LOOP = 0;//begin..end - begin..end - begin..end...

	public static final int PING_PONG_LOOP = 1;//begin..end - end..begin - begin..end...

	public static final int PLAY_ONCE = 2;//begin..end

	public static final int PLAY_ONCE_BACKWARDS = 3;//end..begin
	
	private AnimationContainer animationContainer;

	private int frameRate = 30;

	private int animationPlayerState; //initial, play, pause, stop.
	
	private int animationMode = 0;//loop, ping pong, play once, play once backwards.

	private double animationFrequency = 1.0 / frameRate * 1000;// ms.

	private double preTime = 0.0;

	private double globalTime = 10.0;

	private double time = 0.0;

	private AnimationTimer AT;

	private boolean pauseFlag = false;
	
	private boolean playFlag = false;
	
	private boolean occupied = false;

	private int direcction = 1;//1: 255-0, 0: 0-255.

	public AnimationPlayer() {
		AT = new AnimationTimer();
	}

	/**
	 * Logic actions when the play button is pressed.
	 */
	public void play() {
		if (animationPlayerState != AnimationPlayer.ANIMATION_PLAY){
				animationPlayerState = AnimationPlayer.ANIMATION_PLAY;
				
				if (AT.isAlive() == false){
					AT = new AnimationTimer();
					AT.addCallBackObject(this);
					AT.start(animationFrequency);
				}

			//Initial time control, backward animation: initial time = end time, forward animation: initial time = 0.
			if ((this.getAnimationMode() == PLAY_ONCE_BACKWARDS)&& (!pauseFlag)) {
				time = this.getGlobalTime(); 
			}
		//	System.err.println("time dentro del play: "+ time);
			playFlag = true;// flag of play running.
	  //}IF DEL playFlag.
		}
	}

	
	/**
	 * Logic actions when the stop button is pressed.
	 */
	public void stop() {
		if ((animationPlayerState != AnimationPlayer.ANIMATION_STOP) && (playFlag == true || pauseFlag == true)) {//If the state is not stop.
			animationPlayerState = AnimationPlayer.ANIMATION_STOP;
			if(playFlag == true)//flag to control that the actual state is play. 
				playFlag = false;
			if(pauseFlag == true)//flag to control that the actual state is pause.
				pauseFlag = false;
			occupied = false;
			time = 0.0;
			AT.end();
		}
	}

	
	/**
	 * Logic actions when the pause button is pressed.
	 */
	public void pause() {
		if (animationPlayerState != AnimationPlayer.ANIMATION_PAUSE && playFlag == true) {//If the state is not pause.
			animationPlayerState = AnimationPlayer.ANIMATION_PAUSE;
			pauseFlag = true;//flag to control that is not first play pressing. 
			playFlag = false;
			occupied = false;
			AT.end();
		}
	}

	
	/**
	 * Logic actions when the record button is pressed.
	 */
	public void record() {
		if (animationPlayerState != AnimationPlayer.ANIMATION_RECORD) {
			animationPlayerState = AnimationPlayer.ANIMATION_RECORD;
			this.stop();//Provisional operation of record.
		}
	}

	/**
	 * Set of our player frequency.
	 * 
	 * @param animationFrequency
	 */
	
	public void setFrameRate(int frameRate) {
		this.frameRate = frameRate;
	}

	public void setTime() {
		// TODO generate method to set time
	}

	public AnimationContainer getAnimationContainer() {
		return animationContainer;
	}

	public void setAnimationContainer(AnimationContainer animationContainer) {
		this.animationContainer = animationContainer;
	}

	public AnimationPlayer(AnimationContainer animationContainer) {
		this.animationContainer = animationContainer;
	}

	private void apply(double Tini, double Tend) {
		getAnimationContainer().apply(Tini, Tend);
	}
	
	/**
	 * Getting the total duration of the animation. 
	 * @return total time in seconds
	 */
	public double getGlobalTime() {
		return (globalTime * 1000);
	}

	
	/**
	 * Setting the total time of the animation.
	 * @param globalTime
	 */
	public void setGlobalTime(double globalTime) {
		this.globalTime = globalTime;
	}

	/**
	 * Getting the actual state running.
	 * @return animationPlayerState: INITIAL, PLAY, STOP, PAUSE, RECORD.
	 */
	public int getAnimationPlayerState() {
		return animationPlayerState;
	}

	
	/**
	 * Setting the state selected in the interface.
	 * @param animationPlayerState
	 */
	public void setAnimationPlayerState(int animationPlayerState) {
		this.animationPlayerState = animationPlayerState;
	}
	
	
	/**
	 * Updating the timer according to our state and mode.
	 */
	public void update() {
		double currentTime=0, delay=0;
		if (!occupied) {
			occupied = true;
			//System.err.println("time dentro del update: "+ time);
			//System.err.println("MODO: " + this.getAnimationMode());			
			if (this.animationPlayerState == AnimationPlayer.ANIMATION_PLAY) {
					if (pauseFlag == true) {//pause->play.
						currentTime = System.currentTimeMillis();
						preTime = currentTime;
						//System.err.println("RETARDO PAUSE TRUE: " + delay);
						pauseFlag = false;
					}else{//normal play.
						 //Calculate the delay
						currentTime = System.currentTimeMillis();
						delay = currentTime - preTime;
						preTime = currentTime;
						//System.err.println("RETARDO PAUSE FALSE: " + delay);
				}
				//System.err.println("RETARDO: " + delay);

				// if the delay could be hightest than the frequency, we have
				// had increased the time with this delay
				
				//Play once or loop animation selected.
				if ((this.getAnimationMode() == PLAY_ONCE)||(this.getAnimationMode() == LOOP)) {
					if ((delay > animationFrequency) && (!pauseFlag))
					{
						if(time != 0)
							time += delay;
						else 
							time += animationFrequency;
					}
					else{
						time += animationFrequency;
						//Play once animation.
					}
					if (this.getAnimationMode() == PLAY_ONCE) {
						if (time >= this.getGlobalTime()){
							occupied = false;
							this.stop();
						}
					} else
						time = (time > this.getGlobalTime()) ? 0 : time;
				}//if loop.
				//Backward animation.
				if (this.getAnimationMode() == PLAY_ONCE_BACKWARDS) {
					if ((delay > animationFrequency) && (!pauseFlag)){
						if((time < this.getGlobalTime()))
							time -= delay;
						else
							time -= animationFrequency;
					}
					else
						time -= animationFrequency;
					if(time <= 0){
						occupied = false;
						pauseFlag = false;
						this.stop();
					}
				}//if backwards.
				//Ping pong animation.
				if(this.getAnimationMode() == PING_PONG_LOOP){
						if(direcction == 1){// 0..globalTime
							if ((delay > animationFrequency) && (!pauseFlag)){
								if((time < this.getGlobalTime())){
									time += delay;
								}
								else
									time += animationFrequency;
							}
							else
								time += animationFrequency;
						}
						else{
							if ((delay > animationFrequency) && (!pauseFlag)){
								if((time < this.getGlobalTime())){
									time -= delay;
								}
								else
									time -= animationFrequency;
							}
							else
								time -= animationFrequency;
						}
						if(time > this.getGlobalTime()){
							time = this.getGlobalTime();
							direcction = 0;
						}
						else if(time < 0.0){
							time = 0.0;
							direcction = 1;
						}
				}//if ping pong.
				// Apply and repaint all tracks.
				apply(time / this.getGlobalTime(), 0);

			} else if (this.animationPlayerState == AnimationPlayer.ANIMATION_STOP) {
				
			} else if (this.animationPlayerState == AnimationPlayer.ANIMATION_PAUSE) {
						
			} else if (this.animationPlayerState == AnimationPlayer.ANIMATION_RECORD) {

			}
			occupied = false;
		}//ocupied.
		//System.out.println("ocupado : " + occupied);
	}
	
	/**
	 * Return the actual time running in the player.
	 * @return time.
	 */
	public double getCurrentTime() {
		return this.time;
	}

	
	/**
	 * Return the name of this class.
	 * @return name.
	 */
	public String getClassName() {
		return this.getClass().getName();
	}
	
	
	/**
	 * This function returns the actual state of the player.
	 * 
	 * @return animationMode. loop, ping pong, play once, play once backwards.
	 */
	public int getAnimationMode() {
		return animationMode;
	}

	
	/**
	 * It Depends the selected option the mode change.
	 * 
	 * @param animationMode
	 */
	
	public void setAnimationMode(int animationMode) {
		this.animationMode = animationMode;
	}

	
	/*
	 * IPersistence get method.
	 * @see com.iver.utiles.IPersistence#getXMLEntity()
	 */
	public XMLEntity getXMLEntity() {
		XMLEntity xml = new XMLEntity();
		//xml.putProperty("animationContainer", "true");
		xml.putProperty("className", this.getClassName());
		xml.putProperty("animationMode", this.animationMode);
		//xml.putProperty("animationPlayerState", animationPlayerState);
		xml.putProperty("length",this.globalTime);
	//	xml.putProperty("animationFrequency",this.animationFrequency);

		return xml;
	}
	
	
	/*
	 * IPersistence set method.
	 * @see com.iver.utiles.IPersistence#setXMLEntity(com.iver.utiles.XMLEntity)
	 */
	public void setXMLEntity(XMLEntity xml) {
		
		if (xml.contains("animationMode"))
			this.animationMode = xml.getIntProperty("animationMode");
		if (xml.contains("length")){
			this.globalTime = xml.getDoubleProperty("length");
		}
	}
}
