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

package com.iver.cit.gvsig.animation.timer;

import java.util.ArrayList;
import java.util.List;



// this class is only an example. it isn´t used in librarys
public class AnimationTimer  implements Runnable {
	double milis;
	private boolean finish;
	private List<IUpdateCallBack> callBackList;
	private Thread thread;
	
	
    /**
     * Lanza un timer cada segundo.
     */
    public AnimationTimer()
    {
    	
    }

	public void run() {
		if (isAlive()) {
			while (true) {
				try {
					Thread.sleep((long) milis);
					synchronized (this) {
						if (finish) {
							break;
						}
					}
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				// Updating
				Update();
			}
		}		
	}
	
	public synchronized void end() {
		finish = true;
		//thread.stop();
		thread.interrupt();
	}
	
	
	private void Update() {
		// TODO Auto-generated method stub
		for (int i = 0; i < callBackList.size(); i++) {
			IUpdateCallBack element = (IUpdateCallBack) callBackList.get(i);
			element.update();		
		}
		
	}

	public void start(double milis) {
		this.milis = milis;

		// Create the thread supplying it with the runnable object
		thread = new Thread(this);

		// Start the thread
		thread.start();
	}
	
	public void addCallBackObject(IUpdateCallBack object) {
		if (callBackList == null)
			callBackList = new ArrayList<IUpdateCallBack>();
		if (!callBackList.contains(object)) {
			callBackList.add(object);
		}
	}

	public boolean isAlive() {
		if (thread != null)
			return thread.isAlive();
		else
			return false;
	}
	
  
}
