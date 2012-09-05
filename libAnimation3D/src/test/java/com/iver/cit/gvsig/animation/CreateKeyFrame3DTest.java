package com.iver.cit.gvsig.animation;

import com.iver.cit.gvsig.animation.keyFrame.AnimationKeyFrame3DFlatFactory;
import com.iver.cit.gvsig.animation.keyframe.IKeyFrame;
import com.iver.utiles.extensionPoints.ExtensionPoint;
import com.iver.utiles.extensionPoints.ExtensionPoints;
import com.iver.utiles.extensionPoints.ExtensionPointsSingleton;

import junit.framework.TestCase;

public class CreateKeyFrame3DTest extends TestCase {

	
	public void test1() {
		
		AnimationKeyFrame3DFlatFactory.register();
		
		ExtensionPoints extensionPoints = ExtensionPointsSingleton.getInstance();
		System.out.println("Getting extension point...");
		ExtensionPoint extPoint = ((ExtensionPoint) extensionPoints.get("Animation"));
		
		//System.out.println("Creating ...");
		try {
			System.out.println("Creating ...");
			IKeyFrame keyframe = (IKeyFrame) extPoint.create("KeyFrame3DFlat");
			keyframe.getName();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done ");
	}
}
