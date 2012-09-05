package com.iver.cit.gvsig.animation;

import java.security.KeyException;
import java.util.Map;

import com.iver.cit.gvsig.animation.traks.IAnimationTrack;
import com.iver.utiles.extensionPoints.ExtensionPoint;
import com.iver.utiles.extensionPoints.ExtensionPoints;
import com.iver.utiles.extensionPoints.ExtensionPointsSingleton;
import com.iver.utiles.extensionPoints.IExtensionBuilder;

/**
 * @author julio
 * 
 * Class factory to create animation scenes
 * 
 */
public abstract class AnimationFactory implements IExtensionBuilder {

	public static IAnimationTrack createDateTrack(IAnimationTrack animationTrack) {
		return null;
		// return new AnimationDatedTrack(animationTrack);
	}

	public static IAnimationTrack getAnimationContainer() {
		return null;
	}

	/**
	 * Registers in the points of extension the Factory with alias.
	 * 
	 * @param registerName
	 *            Register name.
	 * @param obj
	 *            Class of register.
	 * @param alias
	 *            Alias.
	 */
	public static void register(String registerName, Object obj, String alias) {
		ExtensionPoints extensionPoints = ExtensionPointsSingleton
				.getInstance();
		extensionPoints.add("Animation", registerName, obj);
		ExtensionPoint extPoint = ((ExtensionPoint) extensionPoints
				.get("Animation"));

		try {
			extPoint.addAlias(registerName, alias);
		} catch (KeyException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Registers in the points of extension the Factory
	 * 
	 * @param registerName
	 *            Register name.
	 * @param obj
	 *            Class of register.
	 */
	public static void register(String registerName, Object obj) {
		ExtensionPoints extensionPoints = ExtensionPointsSingleton
				.getInstance();
		extensionPoints.add("Animation", registerName, obj);
	}

	public Object create() {
		return this;
	}

	public Object create(Object[] args) {
		return this;
	}

	public Object create(Map args) {
		return this;
	}

	/**
	 * Returns the name of registration in the point of extension.
	 * 
	 * @return Name of registration
	 */
	public abstract String getRegisterName();

}