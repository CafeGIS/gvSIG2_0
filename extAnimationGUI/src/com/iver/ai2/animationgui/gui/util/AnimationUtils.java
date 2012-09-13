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


/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */

package com.iver.ai2.animationgui.gui.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gvsig.gvsig3dgui.view.View3D;
import org.gvsig.osgvp.planets.Planet;
import org.gvsig.osgvp.viewer.Camera;

import com.iver.ai2.animationgui.gui.document.ProjectAnimationDocument;
import com.iver.ai2.animationgui.gui.document.ProjectAnimationDocumentFactory;
import com.iver.ai2.gvsig3d.camera.ProjectCamera;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.IExtension;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.animation.AnimationContainer;
import com.iver.cit.gvsig.animation.IAnimationType;
import com.iver.cit.gvsig.animation.animatedObject.AnimatedObjectBase;
import com.iver.cit.gvsig.animation.animatedObject.AnimationObject2D;
import com.iver.cit.gvsig.animation.animatedObject.AnimationObject3DFlat;
import com.iver.cit.gvsig.animation.animatedObject.IAnimatedObject;
import com.iver.cit.gvsig.animation.interval.AnimationKeyFrameInterval;
import com.iver.cit.gvsig.animation.keyframe.IKeyFrame;
import com.iver.cit.gvsig.animation.keyframe.interpolator.FuntionFactory;
import com.iver.cit.gvsig.animation.keyframe.interpolator.IInterpolator;
import com.iver.cit.gvsig.animation.keyframe.interpolator.IInterpolatorTimeFuntion;
import com.iver.cit.gvsig.animation.keyframe.interpolator.InterpolatorFactory;
import com.iver.cit.gvsig.animation.traks.AnimationTimeTrack;
import com.iver.cit.gvsig.animation.traks.IAnimationTrack;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.ProjectExtent;
import com.iver.cit.gvsig.project.ProjectFactory;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.ProjectDocumentFactory;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.extensionPoints.ExtensionPoint;
import com.iver.utiles.extensionPoints.ExtensionPoints;
import com.iver.utiles.extensionPoints.ExtensionPointsSingleton;

public class AnimationUtils {

	private static String AnimationPrefix = "Animation-";
	private static int cont2D = 0;
	private static int cont3D = 0;
	private static Project project;

	static {
		IExtension extension = PluginServices
				.getExtension(com.iver.cit.gvsig.ProjectExtension.class);
		ProjectExtension pe = (ProjectExtension) extension;
		project = pe.getProject();
	}

	
	/**
	 * Default function in movement animations 2D and 3D
	 * 
	 * @param view: Actual view selected
	 * @param ac: Animation container
	 * @param prefix: Part of the tracks animation names 
	 */
	public static void captureEncuadrator(BaseView view, AnimationContainer ac,
			String prefix) {
		// snapshot pressed.
		System.out.println("pulsado boton de capturar encuadres!!!");

		// If the window instance is View, it will manage extends 2D
		if (view instanceof View) {
			AnimationUtils.captureEncuadrator2D((View) view, ac, prefix);
		}
		else
			if (view instanceof BaseView) {
				AnimationUtils.captureEncuadrator3D((View3D) view, ac, prefix);
			}
		

		System.out.println(ac);

	}
	
	/**
	 * Creating a animation 2D track based in time.
	 * 
	 * @param view: Actual view selected
	 * @param ac: Animation container
	 * @param prefix: Part of the tracks animation names 
	 */

	public static void captureEncuadrator2D(View view, AnimationContainer ac,
			String prefix) {

		AnimationTimeTrack animationTimeTrack = null;
		AnimationKeyFrameInterval AKFinterval = null;
		List<IKeyFrame> keyFrameList = null;

		String baseName = prefix + "-" + view.getWindowInfo().getTitle() + "-"
				+ AnimationPrefix;
		String name = baseName + cont2D++;

		// Getting the animation extension point.
		ExtensionPoints extensionPoints = ExtensionPointsSingleton
				.getInstance();
		ExtensionPoint extPoint = ((ExtensionPoint) extensionPoints
				.get("Animation"));

		// Creating new track
		animationTimeTrack = AnimationUtils.getAnimationTimeTrack(baseName
				+ "-encuadrator_track_2D", ac, view);
		// Creating the interval
		List intervalList = animationTimeTrack.getIntervalList();
		
		if (intervalList != null) {
			for (Iterator<AnimationKeyFrameInterval> iterator = intervalList.iterator(); iterator
					.hasNext();) {
				AnimationKeyFrameInterval animationKeyFrameInterval = iterator.next();
				if (animationKeyFrameInterval != null) {
					AKFinterval = animationKeyFrameInterval;
				}
			}
			if (intervalList.size() == 0) {
				AKFinterval = (AnimationKeyFrameInterval) animationTimeTrack
						.createKeyFrameInterval();
				// Setting up interval properties.
				AKFinterval.setInitialTime(0.0);
				AKFinterval.setEndTime(1.0);
			}
		}
		
		

		// Getting the current extend and adding to project extends.
		MapControl mapa = view.getMapControl();
		ProjectExtent extent = ProjectFactory.createExtent();
		extent.setDescription(name);
		extent.setExtent(mapa.getViewPort().getExtent());
		project.addExtent(extent);

		// Creating the keyframe list.
		ProjectExtent[] extentsList = project.getExtents();
		keyFrameList = new ArrayList<IKeyFrame>();
		for (int i = 0; i < extentsList.length; i++) {
			ProjectExtent projectExtent = extentsList[i];
			if (projectExtent.getDescription().contains(baseName)) {
				IKeyFrame kf2D = null;
				try {
					kf2D = (IKeyFrame) extPoint.create("KeyFrame2D");
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

				// Setting up the initial and end time interval.
				double fin = AKFinterval.getEndTime();
				double ini = AKFinterval.getInitialTime();
				double size = extentsList.length;
				// Calculating the insertion time.
				double tiempo = 0.0;
				if (i != 0)
					tiempo = i * ((fin - ini) / (size - 1.0));
				kf2D.setTime(tiempo);
				// hacer funcion de insercion o adapter
				kf2D.setAnimatedObject(projectExtent);
				kf2D.CapturesProperties();
				keyFrameList.add(kf2D);
			}
		}
		AKFinterval.setKeyFrameList(keyFrameList);
	}

	public static Project getProject() {
		return project;
	}
	
	
	public static String getAnimationPrefix() {
		return AnimationPrefix;
	}

	
	/**
	 * Creating a animation 3D track based in time.
	 * 
	 * @param view: Actual view selected
	 * @param ac: Animation container
	 * @param prefix: Part of the tracks animation names 
	 */
	public static void captureEncuadrator3D(View3D view, AnimationContainer ac,
			String prefix) {
		AnimationTimeTrack animationTimeTrack = null;
		AnimationKeyFrameInterval AKFinterval = null;
		List<IKeyFrame> keyFrameList = null;

		String baseName = prefix + "-" + view.getWindowInfo().getTitle() + "-"
				+ AnimationPrefix;
		String name = baseName + cont3D++;

		// Getting the animation extension point.
		ExtensionPoints extensionPoints = ExtensionPointsSingleton
				.getInstance();
		ExtensionPoint extPoint = ((ExtensionPoint) extensionPoints
				.get("Animation"));

		// Creating new track
		animationTimeTrack = AnimationUtils.getAnimationTimeTrack(baseName
				+ "-encuadrator_track_3D", ac, view);

		// Creating the interval
		List intervalList = animationTimeTrack
				.getIntervalList();
		if (intervalList != null) {
			for (Iterator iterator = intervalList.iterator(); iterator
					.hasNext();) {
				AnimationKeyFrameInterval animationKeyFrameInterval = (AnimationKeyFrameInterval) iterator
						.next();
				if (animationKeyFrameInterval != null) {
					AKFinterval = animationKeyFrameInterval;
				}
			}
			if (intervalList.size() == 0) {
				AKFinterval = (AnimationKeyFrameInterval) animationTimeTrack
						.createKeyFrameInterval();
				// Setting up interval properties.
				AKFinterval.setInitialTime(0.0);
				AKFinterval.setEndTime(1.0);
			}
		}
		// Getting the current camera position and insert it into project camera list.
		Camera ca = view.getCamera();
		ProjectCamera camera = new ProjectCamera();
		camera.setDescription(name);
		camera.setCamera(ca);
		project.addCamera(camera);

		List<ProjectCamera> extentsList = filterExtendsList(baseName, project);
		keyFrameList = new ArrayList<IKeyFrame>();
		for (int i = 0; i < extentsList.size(); i++) {
			ProjectCamera projectCamera = (ProjectCamera) extentsList.get(i);
			IKeyFrame kf3D = null;
			try {
				kf3D = (IKeyFrame) extPoint.create("KeyFrame3DFlat");
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			// Setting up the initial and end time interval.
			double fin = AKFinterval.getEndTime();
			double ini = AKFinterval.getInitialTime();
			double size = extentsList.size();
			// Calculating the correct time for this key frame.
			double tiempo = 0.0;
			if (i != 0)
				tiempo = i * ((fin - ini) / (size - 1.0));
			// Setting the time.
			kf3D.setTime(tiempo);
			// hacer funcion de insercion o adapter
			kf3D.setAnimatedObject(projectCamera);
			kf3D.CapturesProperties();
			keyFrameList.add(kf3D);
		}
		AKFinterval.setKeyFrameList(keyFrameList);

	}

	private static List<ProjectCamera> filterExtendsList(String baseName,
			Project project2) {
		List<ProjectCamera> extendsList = null;
		Object[] extentsList = project2.getCameras();
		for (int i = 0; i < extentsList.length; i++) {
			ProjectCamera projectCamera = (ProjectCamera) extentsList[i];
			if (projectCamera.getDescription().contains(baseName)) {
				if (extendsList == null)
					extendsList = new ArrayList<ProjectCamera>();
				extendsList.add(projectCamera);
			}
		}
		return extendsList;
	}

	private static AnimationTimeTrack getAnimationTimeTrack(String name,
			AnimationContainer ac, BaseView view) {

		AnimationTimeTrack att = null;
		if (view instanceof View) {
			att = AnimationUtils.getAnimationTimeTrackForView(name, ac,
					(View) view);
		}

		if (view instanceof View3D) {
			att = AnimationUtils.getAnimationTimeTrackForView3D(name, ac,
					(View3D) view);
		}
		return att;
	}

	private static AnimationTimeTrack getAnimationTimeTrackForView(String name,
			AnimationContainer ac, View view) {

		AnimationTimeTrack at = null;
		IAnimationTrack aa = ac.findTrack(name);
		if (aa == null) {

			// Getting the extension point.
			ExtensionPoints extensionPoints = ExtensionPointsSingleton
					.getInstance();
			ExtensionPoint extPoint = ((ExtensionPoint) extensionPoints
					.get("Animation"));

			IAnimationType animationLayer2D = null;
			try {
				animationLayer2D = (IAnimationType) extPoint
						.create("AnimationLayer2D");
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Setting up the track properties
			at = (AnimationTimeTrack) ac.CreateTimeTrack(animationLayer2D);
			at.setName(name);
			at.setEnable(true);
			// Setting up the animated object
			// Creating the object 2D
			AnimatedObjectBase object2D = new AnimationObject2D();

			// Getting the interpolator
			IInterpolator inter2D = InterpolatorFactory
					.createObject("Interpolator2D");
			((AnimationObject2D)object2D).setAnimatedView(view);
			
			// Getting the interpolator funcion
			IInterpolatorTimeFuntion funtion = FuntionFactory
					.createObject("com.iver.cit.gvsig.animation.keyframe.interpolator.LinearFuntion");
			inter2D.setFuntion(funtion);

			animationLayer2D.setInterpolator(inter2D);
			at.setAnimationType(animationLayer2D);
			animationLayer2D.setAnimatedObject(object2D);
			
			at.setAnimatedObject(object2D);
		} else {
			at = (AnimationTimeTrack) aa;
		}

		return at;

	}

	private static AnimationTimeTrack getAnimationTimeTrackForView3D(
			String name, AnimationContainer ac, View3D view) {

		AnimationTimeTrack at = null;
		IAnimationTrack aa = ac.findTrack(name);
		if (aa == null) {

			// Getting the extension point.
			ExtensionPoints extensionPoints = ExtensionPointsSingleton
					.getInstance();
			ExtensionPoint extPoint = ((ExtensionPoint) extensionPoints
					.get("Animation"));

			IAnimationType animationLayer3DFlat = null;
			try {
				animationLayer3DFlat = (IAnimationType) extPoint
						.create("AnimationLayer3DFlat");
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			at = (AnimationTimeTrack) ac.CreateTimeTrack(animationLayer3DFlat);

			at.setName(name);
			at.setEnable(true);

			AnimatedObjectBase object3D = new AnimationObject3DFlat();
			((AnimationObject3DFlat)object3D).setAnimatedCanvas3D(view.getCanvas3d());
			((AnimationObject3DFlat)object3D).setAnimatedView(view);
			
			at.setAnimatedObject(object3D);

			/** ******************************************************************* */
			IInterpolator interpolator3D = null;
			if (view.getPlanet().getCoordinateSystemType() == Planet.CoordinateSystemType.PROJECTED) {
				interpolator3D = InterpolatorFactory
						.createObject("Interpolator3DFlat");
			} else if (view.getPlanet().getCoordinateSystemType() == Planet.CoordinateSystemType.GEOCENTRIC) {
				interpolator3D = InterpolatorFactory
						.createObject("Interpolator3DSpherical");
			}

			IInterpolatorTimeFuntion funtion = FuntionFactory
					.createObject("com.iver.cit.gvsig.animation.keyframe.interpolator.LinearFuntion");
			interpolator3D.setFuntion(funtion);
			animationLayer3DFlat.setInterpolator(interpolator3D);
			at.setAnimationType(animationLayer3DFlat);
			
			animationLayer3DFlat.setAnimatedObject(object3D);

			/** ********************************************************************* */
			/*******************************************************************
			 * old code * IInterpolator inter3DFlat = InterpolatorFactory
			 * .createObject("Interpolator3DFlat"); IInterpolatorTimeFuntion
			 * funtion = FuntionFactory
			 * .createObject("com.iver.cit.gvsig.animation.keyframe.interpolator.LinearFuntion");
			 * inter3DFlat.setFuntion(funtion);
			 * animationLayer3DFlat.setInterpolator(inter3DFlat);
			 * at.setAnimationType(animationLayer3DFlat);
			 * animationLayer3DFlat.setAnimatedObject(object3D); /
			 ******************************************************************/
		} else {
			at = (AnimationTimeTrack) aa;
		}
		return at;

	}

	/**
	 * Return a new animation document or the document where the view viewName is included
	 * 
	 * @param viewName
	 * @param project
	 * @return a new or actual animation document
	 */
	public static ProjectAnimationDocument createAnimationDocument(
			String viewName, Project project) {
		ProjectAnimationDocument pv = null;
		ArrayList<ProjectDocument> projectList = project
				.getDocumentsByType(ProjectAnimationDocumentFactory.registerName);
		Iterator<ProjectDocument> it = projectList.iterator();
		while (it.hasNext()) {
			ProjectDocument projectDocument = (ProjectDocument) it.next();
			if (viewName.equals(projectDocument.getName())) {
				pv = (ProjectAnimationDocument) projectDocument;
			}

		}
		if (pv == null) {
			ExtensionPoints extensionPoints = ExtensionPointsSingleton
					.getInstance();
			ExtensionPoint extPoint = ((ExtensionPoint) extensionPoints
					.get("Documents"));
			ProjectDocumentFactory pdf = null;
			try {
				pdf = (ProjectDocumentFactory) extPoint
						.create(ProjectAnimationDocumentFactory.registerName);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			pv = (ProjectAnimationDocument) pdf.create((Project) null);
			pv.setProjectDocumentFactory(pdf);
			pv.setName(viewName);
		}
		return pv;
	}

	/**
	 * Return the document indicated with the parameter name
	 * 
	 * @param name
	 * @param project
	 * @return
	 */
	public static ProjectAnimationDocument getProjectDocument(String name,
			Project project) {
		ProjectAnimationDocument pv = null;
		ArrayList<ProjectDocument> projectList = project
				.getDocumentsByType(ProjectAnimationDocumentFactory.registerName);
		Iterator<ProjectDocument> it = projectList.iterator();
		while (it.hasNext()) {
			ProjectDocument projectDocument = (ProjectDocument) it.next();
			if (name.equals(projectDocument.getName())) {
				pv = (ProjectAnimationDocument) projectDocument;
			}

		}
		return pv;
	}

	public static boolean exitsProject(String name, Project project) {
		boolean pv = false;
		ArrayList<ProjectDocument> projectList = project
				.getDocumentsByType(ProjectAnimationDocumentFactory.registerName);
		Iterator<ProjectDocument> it = projectList.iterator();
		while (it.hasNext()) {
			ProjectDocument projectDocument = (ProjectDocument) it.next();
			if (name.equals(projectDocument.getName())) {
				pv = true;
			}

		}
		return pv;
	}

	/**
	 * 
	 * @param name :
	 *            Track name.
	 * @param ac: animation container.
	 * @param object: animated object
	 * @return at : Return an animation track based in time.
	 * 
	 * Creating a animation track based in time.
	 */
	public static AnimationTimeTrack getAnimationTimeTrackTransparency(
			String name, AnimationContainer ac, BaseView view,
			IAnimatedObject object) {

		AnimationTimeTrack at = null;// this.animationTimeTrack;

		IAnimationTrack animationTrack = ac.findTrack(name);
		if (animationTrack == null) {

			ExtensionPoints extensionPoints = ExtensionPointsSingleton
					.getInstance();
			ExtensionPoint extPoint = ((ExtensionPoint) extensionPoints
					.get("Animation"));

			IAnimationType animTransp = null;
			try {
				System.out.println(extPoint.getDescription());

				// Instancing my new animation based in transparency.
				animTransp = (IAnimationType) extPoint
						.create("AnimationTransparency");
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			at = (AnimationTimeTrack) ac.CreateTimeTrack(animTransp);
			at.setName(name);

			// Saving the view( in this case a BaseView).

			object.addAnimatedObject("view", view);

			at.setAnimatedObject(object);
			animTransp.setAnimatedObject(object);
			// My interpolator based in time and transparency.
			IInterpolator interTransparency = InterpolatorFactory
					.createObject("InterpolatorTransparency");
			animTransp.setInterpolator(interTransparency);
			at.setAnimationType(animTransp);
		} else {
			at = (AnimationTimeTrack) animationTrack;
		}
		return at;
	}
	
	/**
	 * Creating a transparency animation common 2D and 3D 
	 * @param window
	 * @param ac
	 * @param prefix
	 */
	
	public static void createLayerTransparencyAnimation(BaseView window,
			AnimationContainer ac, String prefix) {
		System.out.println("pulsado boton transparencias !!!");

		AnimationTimeTrack animationTimeTrack = null;
		AnimationKeyFrameInterval AKFinterval = null;
		List<IKeyFrame> keyFrameList = null;
		AnimatedObjectBase object = new AnimatedObjectBase();
		// Getting the animation extension point
		ExtensionPoints extensionPoints = ExtensionPointsSingleton
				.getInstance();
		ExtensionPoint extPoint = ((ExtensionPoint) extensionPoints
				.get("Animation"));

		// List of keyframes.
		// If the window is instance of View
		if (window instanceof BaseView) {

			BaseView view = (BaseView) window;

			// Generating the list of keyframes
			// keyFrameList = new ArrayList();
			IProjectView model = view.getModel();
			MapContext mapContext = model.getMapContext();
			FLayers layers = mapContext.getLayers();
			FLayer[] actives = layers.getActives();

			double fin = 1;// AKFinterval.getEndTime();
			double ini = 0;// AKFinterval.getInitialTime();
			double tiempo = 0.0;
			int size = actives.length;
			double incremento = ((fin - ini) / (size));
			for (int i = size - 1; i >= 0; i--) {// order in TOC.

				animationTimeTrack = null;
				animationTimeTrack = AnimationUtils
						.getAnimationTimeTrackTransparency("Track_layer_" + i,
								ac, view, object);
				animationTimeTrack.setEnable(true);
				object.addAnimatedObject("layer", actives[i]);

				// Generating the interval.
				AKFinterval = (AnimationKeyFrameInterval) animationTimeTrack
						.createKeyFrameInterval();
				AKFinterval.setInitialTime(0.0);
				AKFinterval.setEndTime(1.0);

				keyFrameList = new ArrayList<IKeyFrame>();
				IKeyFrame kfTransAux = null;
				IKeyFrame kfTransIni = null;
				IKeyFrame kfTransFin = null;
				try {
					kfTransAux = (IKeyFrame) extPoint
							.create("KeyFrameTransparency");
					kfTransIni = (IKeyFrame) extPoint
							.create("KeyFrameTransparency");
					kfTransFin = (IKeyFrame) extPoint
							.create("KeyFrameTransparency");
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
				// Calculating the time insertion.
				if (i == size - 1) // the keyframe is the first in the list.
					tiempo = ini + 0.001;

				// Three keyframes by track.
				// Keyframe 1 of the layer i.
				kfTransAux.setName("KF1_layer_" + i);// name of keyframe.
				kfTransAux.setTime(ini); // initial time interval.
				kfTransAux.setAnimatedObject(actives[i]);// set the object to
				// animate(a layer).
				kfTransAux.CapturesProperties(); // capturing actual
				// transparency.
				keyFrameList.add(kfTransAux); // adding in the list.

				// keyframe 2 of the layer i.
				kfTransIni.setName("KF2_layer_" + i);// name of keyframe.
				kfTransIni.setTime(tiempo); // initial time interval.
				kfTransIni.setAnimatedObject(actives[i]);// set the same
				// layer in the
				// second keyframe.
				kfTransIni.CapturesProperties(); // capturing actual
				// transparency.
				keyFrameList.add(kfTransIni); // adding in the list.

				// keyframe 3 of the layer i.
				try {
					if (i == 0) // the keyframe is the last in the list.
						tiempo = fin;
					else
						tiempo += incremento;

					kfTransFin.setName("KF3_layer_" + i);// name of keyframe.
					kfTransFin.setTime(tiempo);// saving final time interval.
					// kfTransFin.setLevelTransparency(0);// interpolation:
					// layer transparency in
					// the moment to 0.
					kfTransFin.setAnimatedObject(actives[i]); // save the same
					// layer in the
					// third
					// keyframe
					keyFrameList.add(kfTransFin);// adding to the list.
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				AKFinterval.setKeyFrameList(keyFrameList);// Adding the list
				// of keyframes to
				// the interval.

			}
		}
	}

}
