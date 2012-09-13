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

package com.iver.ai2.animationgui.gui;

import com.iver.ai2.animationgui.gui.document.ProjectAnimationDocument;
import com.iver.ai2.animationgui.gui.util.AnimationUtils;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.plugins.IExtension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.animation.AnimationContainer;
import com.iver.cit.gvsig.animation.IAnimationType;
import com.iver.cit.gvsig.animation.animatedObject.AnimatedObjectBase;
import com.iver.cit.gvsig.animation.keyframe.interpolator.IInterpolator;
import com.iver.cit.gvsig.animation.keyframe.interpolator.InterpolatorFactory;
import com.iver.cit.gvsig.animation.traks.AnimationTimeTrack;
import com.iver.cit.gvsig.animation.traks.IAnimationTrack;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.utiles.extensionPoints.ExtensionPoint;
import com.iver.utiles.extensionPoints.ExtensionPoints;
import com.iver.utiles.extensionPoints.ExtensionPointsSingleton;

/**
 * @author Ángel
 * @since 1.1
 * 
 * Menu of animation transparency toolbar's options.
 */

public class MenuAnimationTransparency extends Extension {

	private Project project;

	// private AnimationContainer ac = null;
	private AnimationTimeTrack animationTimeTrack = null;
	private AnimatedObjectBase object = new AnimatedObjectBase();

	// It is the project Document create to create a fast animation.
	private ProjectAnimationDocument pad;

	public void execute(String actionCommand) {

		AnimationContainer ac;
		// if the button pressed is "this"
		if (actionCommand.equals("CREARANIMTRANSP")) {

			String prefix = "Default-animation-document";
			// if there is not create the default animation. we will create it.
			pad = AnimationUtils.createAnimationDocument(prefix, project);
			if (!AnimationUtils.exitsProject(prefix, project)){
				project.addDocument(pad);
			}

			// Getting the active windows
			IWindow window = PluginServices.getMDIManager().getActiveWindow();

			// Getting the animation container
			ac = pad.getAnimationContainer();
			// Generating the animation transparency
			AnimationUtils.createLayerTransparencyAnimation((BaseView) window,
					ac, prefix);

			// Menu Player.
			// AnimationPlayer ap = ac.getAnimationPlayer();
			// ap.setAnimationContainer(ac);
			// AnimationContol3D fp = new AnimationContol3D(ap);
			// PluginServices.getMDIManager().addWindow((IWindow) fp);
		}

		// System.out.println("pulsado boton transparencias !!!");
		//			
		// // Getting the active windows
		// IWindow window = PluginServices.getMDIManager().getActiveWindow();
		//			
		// // Getting the animation container
		// ac = this.getAnimationContainer();
		//			
		//			
		// // Getting the animation extension point
		// ExtensionPoints extensionPoints =
		// ExtensionPointsSingleton.getInstance();
		// ExtensionPoint extPoint = ((ExtensionPoint)
		// extensionPoints.get("Animation"));
		//			
		//			
		// //List of keyframes.
		// // If the window is instance of View
		// if (window instanceof BaseView) {
		//
		// BaseView view = (BaseView) window;
		//
		// // Generating the list of keyframes
		// //keyFrameList = new ArrayList();
		// IProjectView model = view.getModel();
		// MapContext mapContext = model.getMapContext();
		// FLayers layers = mapContext.getLayers();
		// FLayer[] actives = layers.getActives();
		//				
		// double fin = 1;//AKFinterval.getEndTime();
		// double ini = 0;//AKFinterval.getInitialTime();
		// double tiempo = 0.0;
		// int size = actives.length;
		// double incremento = ((fin - ini) / (size));
		// for(int i = size-1; i >= 0; i--){// order in TOC.
		//					
		// animationTimeTrack = null;
		// animationTimeTrack =
		// AnimationUtils.getAnimationTimeTrackTransparency("Track_layer_" + i,
		// ac);
		// animationTimeTrack.setEnabale(true);
		// object.addAnimatedObject("layer",actives[i]);
		//					
		//					
		// // Generating the interval.
		// AKFinterval = (AnimationKeyFrameInterval) animationTimeTrack
		// .createKeyFrameInterval();
		// AKFinterval.setInitialTime(0.0);
		// AKFinterval.setEndTime(1.0);
		//					
		// keyFrameList = new ArrayList<IKeyFrame>();
		// // KeyFrameTransparency kfTransAux = new KeyFrameTransparency();
		// IKeyFrame kfTransAux = null;
		// IKeyFrame kfTransIni = null;
		// IKeyFrame kfTransFin = null;
		// try {
		// kfTransAux = (IKeyFrame)extPoint.create("KeyFrameTransparency");
		// kfTransIni = (IKeyFrame)extPoint.create("KeyFrameTransparency");
		// kfTransFin = (IKeyFrame)extPoint.create("KeyFrameTransparency");
		// }
		// catch (InstantiationException e1) {
		// e1.printStackTrace();
		// } catch (IllegalAccessException e1) {
		// e1.printStackTrace();
		// }
		// //Calculating the time insertion.
		// if (i == size-1) // the keyframe is the first in the list.
		// tiempo = ini + 0.001;
		//					
		// //Three keyframes by track.
		// //Keyframe 1 of the layer i.
		// kfTransAux.setName("KF1_layer_" + i);//name of keyframe.
		// kfTransAux.setTime(ini); // initial time interval.
		// kfTransAux.setAnimatedObject(actives[i]);//set the object to
		// animate(a layer).
		// //kfTransAux.setLevelTransparency(255);
		// kfTransAux.CapturesProperties(); //capturing actual transparency.
		// keyFrameList.add(kfTransAux); // adding in the list.
		//					
		// //keyframe 2 of the layer i.
		// kfTransIni.setName("KF2_layer_" + i);//name of keyframe.
		// kfTransIni.setTime(tiempo); // initial time interval.
		// kfTransIni.setAnimatedObject(actives[i]);//set the same layer in the
		// second keyframe.
		// kfTransIni.CapturesProperties(); //capturing actual transparency.
		// keyFrameList.add(kfTransIni); // adding in the list.
		//					
		// //keyframe 3 of the layer i.
		// try {
		// if(i == 0) // the keyframe is the last in the list.
		// tiempo = fin;
		// else
		// tiempo += incremento;
		//						
		// kfTransFin.setName("KF3_layer_" + i);//name of keyframe.
		// kfTransFin.setTime(tiempo);// saving final time interval.
		// // kfTransFin.setLevelTransparency(0);// interpolation: layer
		// transparency in the moment to 0.
		// kfTransFin.setAnimatedObject(actives[i]); //save the same layer in
		// the third keyframe
		// keyFrameList.add(kfTransFin);//adding to the list.
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// AKFinterval.setKeyFrameList(keyFrameList);// Adding the list of
		// keyframes to the interval.
		//					
		// }
		// }
		// }

	}

	public void postInitialize() {
		// Getting the project using plugin services.
		IExtension extension = PluginServices
				.getExtension(com.iver.cit.gvsig.ProjectExtension.class);
		ProjectExtension pe = (ProjectExtension) extension;
		project = pe.getProject();

		PluginServices.getIconTheme().registerDefault(
				"transparency-icon",
				this.getClass().getClassLoader().getResource(
						"images/trans6.png"));
	}

	public void initialize() {
		// TODO Auto-generated method stub
	}

	
	/**
	 * 
	 * @param name :
	 *            Track name.
	 * @param ac :
	 *            animation container.
	 * @return at : Return a animation track based in time.
	 * 
	 * Creating a animation track based in time.
	 */
	private AnimationTimeTrack getAnimationTimeTrack_DELETED(String name,
			AnimationContainer ac) {

		AnimationTimeTrack at = this.animationTimeTrack;

		IAnimationTrack animationTrack = ac.findTrack(name);
		if (animationTrack == null) {

			ExtensionPoints extensionPoints = ExtensionPointsSingleton
					.getInstance();
			ExtensionPoint extPoint = ((ExtensionPoint) extensionPoints
					.get("Animation"));
			com.iver.andami.ui.mdiManager.IWindow f = PluginServices // window
																		// active.
					.getMDIManager().getActiveWindow();

			if (f instanceof BaseView) {
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
				BaseView view = (BaseView) f;

				object.addAnimatedObject("view", view);

				at.setAnimatedObject(object);
				animTransp.setAnimatedObject(object);
				// My interpolator based in time and transparency.
				IInterpolator interTransparency = InterpolatorFactory
						.createObject("InterpolatorTransparency");
				animTransp.setInterpolator(interTransparency);
				at.setAnimationType(animTransp);
			}
		}
		return at;
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isVisible() Options to see the
	 *      button in the tool bar. View: must be a base view. One or more
	 *      layers in the view.
	 */
	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices
				.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}

		// Only isVisible = true, where the view have layers.
		if (f instanceof BaseView) {
			BaseView vista = (BaseView) f;
			IProjectView model = vista.getModel();
			MapContext mapa = model.getMapContext();

			return mapa.getLayers().getLayersCount() > 0;
		}
		return false;
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled() Options when we can
	 *      use this animation type. View: must be a base view. One or more
	 *      ACTIVATED layers in the view.
	 */
	public boolean isEnabled() {
		// TODO Auto-generated method stub

		com.iver.andami.ui.mdiManager.IWindow f = PluginServices
				.getMDIManager().getActiveWindow();

		// if the view is not activated.
		if (f == null)
			return false;
		// if the view is a instance of a base view.
		if (f instanceof BaseView) {
			BaseView baseView = (BaseView) f;
			IProjectView model = baseView.getModel();
			MapContext mapContext = model.getMapContext();
			FLayers layers = mapContext.getLayers();
			FLayer[] actives = layers.getActives();
			// only is enabled with one or more layers activated.
			if (actives.length > 0) {
				return true;
			}
		}
		return false;
	}
}
