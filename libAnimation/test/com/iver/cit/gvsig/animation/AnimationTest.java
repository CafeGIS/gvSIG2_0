package com.iver.cit.gvsig.animation;

import java.sql.Date;

import junit.framework.TestCase;

import com.iver.cit.gvsig.animation.interval.AnimationDatedInterval;
import com.iver.cit.gvsig.animation.keyframe.interpolator.FuntionFactory;
import com.iver.cit.gvsig.animation.keyframe.interpolator.IInterpolatorTimeFuntion;
import com.iver.cit.gvsig.animation.keyframe.interpolator.LinearFuntion;
import com.iver.cit.gvsig.animation.test.Animation3D;
import com.iver.cit.gvsig.animation.test.AnimationTypeFactory3D;
import com.iver.cit.gvsig.animation.traks.AnimationDatedTrack;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.extensionPoints.ExtensionPoint;
import com.iver.utiles.extensionPoints.ExtensionPoints;
import com.iver.utiles.extensionPoints.ExtensionPointsSingleton;

public class AnimationTest extends TestCase {

	private Animation3D a3D;

	private AnimationContainer AC;

	public void testRegisterType() throws Throwable {

		AnimationTypeFactory3D.register();
		ExtensionPoints extensionPoints = ExtensionPointsSingleton
				.getInstance();
		System.out.println("Getting extension point...");
		ExtensionPoint extPoint = ((ExtensionPoint) extensionPoints
				.get("Animation"));
		System.out.println("Creating animation type ...");
		a3D = (Animation3D) extPoint.create("Animation3D");
		System.out.println("Done " + a3D);
		// System.out.println("Resultados:\n " + a3D.getName() + "\n "
		// + a3D.getClassName() + "\n " + a3D.getDescription());

	}
	
	public void testRegisterType2D() throws Throwable {

//		AnimationLayer2D a3D;
//		
//		AnimationTypeFactoryLayer2D.register();
//		ExtensionPoints extensionPoints = ExtensionPointsSingleton
//				.getInstance();
//		System.out.println("Getting extension point...");
//		ExtensionPoint extPoint = ((ExtensionPoint) extensionPoints
//				.get("Animation"));
//		System.out.println("Creating animation type ...");
//		a3D = (AnimationLayer2D) extPoint.create("AnimationLayer2D");
//		System.out.println("Done " + a3D);
//		// System.out.println("Resultados:\n " + a3D.getName() + "\n "
//		// + a3D.getClassName() + "\n " + a3D.getDescription());

	}

	public void testCreateAnimationContainer() throws Throwable {

		// Inicialicing
		a3D = null; // Animation type
		AC = null; // Animation Container
		XMLEntity xml = new XMLEntity();

		// Creando contenerdor de tracks
		AC = new AnimationContainer();
		testRegisterType();
	
		String antesName = AC.getClassName();
		System.out.println("antes: " + antesName);
		xml = AC.getXMLEntity();
		//saveWindowProperties(xml);
		AC.setXMLEntity(xml);
		
		String despuesName = xml.getStringProperty("class_name");
		System.out.println("\ndespues: " + despuesName);
		assertEquals(antesName,despuesName);
		
		
		System.out.println(AC);
	}

	public void testCreateDateTrack() {
		
		// Inicialicing
		a3D = null; // Animation type
		AC = null; // Animation Container

		// Creando track del tipo de animacion
		AnimationDatedTrack ADTrack = (AnimationDatedTrack) AC
				.CreateDatedTrack(a3D);
		// Inicializando el track
		ADTrack.setName("Track 1");
		ADTrack.setDescription("track de prueba para series temporales");
		ADTrack.setBeginDate(new Date(0));
		ADTrack.setEndDate(new Date(0));
		// Creando intervalo
		AnimationDatedInterval ADInterval = (AnimationDatedInterval) ADTrack
				.createInterval();

		// Inicializando intervalo
		Date beginDateInterval = new Date(0);
		ADInterval.setBeginDateInterval(beginDateInterval);
		Date endDateInterval = new Date(0);
		ADInterval.setEndDateInterval(endDateInterval);

		AC.removeAllTrack();
		AC.removeTrack(ADTrack);
		ADTrack.removeAllIntervals();
		ADTrack.removeInterval(ADInterval);
		//		
		//		
		// // IAnimationTrack T = AC.findTrack("Track 2");
		// // if (T != null) {
		// // T.toString();
		// // }else {
		// // System.out.println("no encontrada");
		// // }
		//		
		// // List l = AC.getTackListOfType(a3D);
		// // System.out.println(l);

	}

	public void testAnimationTimer() throws Throwable {

		AnimationPlayer AP = new AnimationPlayer();
		AP.play();

	}
	public void testCreateTrack() throws Throwable {

		// AnimationTimer modelo = new AnimationTimer();
		// modelo.addObserver (new Observer()
		// {
		// public void update (Observable unObservable, Object dato)
		// {
		// System.out.println (dato);
		// }
		// });
		// System.out.println("estoy fuera");

	}
	
	
	public void testFuntionFactory() throws Throwable {
		LinearFuntion lf = new LinearFuntion();
		System.out.println("registrando " + lf.getClassName());
		
		FuntionFactory.register(lf);
		
		LinearFuntion lf2 = new LinearFuntion();
		System.out.println("registrando " + lf.getClassName());
		
		FuntionFactory.register(lf2);
		
		IInterpolatorTimeFuntion resutl = FuntionFactory.createObject("com.iver.cit.gvsig.animation.keyframe.interpolator.LinearFuntion");
		
		System.out.println(resutl);

	}


	
}
