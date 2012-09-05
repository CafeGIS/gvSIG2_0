package com.iver.cit.gvsig.util;


import java.util.Map;

import org.gvsig.fmap.mapcontrol.MapControl;

import com.vividsolutions.jump.util.Blackboard;


/**
 * 
 * Prototype of user's session.
 * 
 * This class will save information of the
 * user session, to recover later.
 * 
 * The key of the session will be the Andami's view
 * (to allows a user to save different user sessions
 * for different views)
 * */

public class GvSession {
	private final static GvSession session = new GvSession();
	
	Blackboard blackboard = new Blackboard();
	
	private GvSession(){}
	
	public static GvSession getInstance(){
		return session;
	}
	
	public void put(MapControl map, String key, Object sessionObject){
		String gKey = map.toString() + ":" + key;
		blackboard.put(gKey, sessionObject );
	}
	
	public Object get(MapControl map, String key){
		return blackboard.get(map.toString() + ":" + key);
	}
	
	public void delete(MapControl map, String key){
		Map props = blackboard.getProperties();
		String newKey = map.toString()+":"+key;
		if(props.containsKey(newKey))
		{
			props.remove(newKey);
		}
		
	}
	
	
}

