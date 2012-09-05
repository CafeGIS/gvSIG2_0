package com.iver.cit.gvsig.animation;

import java.sql.Date;
import junit.framework.TestCase;
import com.iver.cit.gvsig.animation.interval.AnimationDatedInterval;
import com.iver.cit.gvsig.animation.interval.AnimationFunctionInterval;
import com.iver.cit.gvsig.animation.interval.AnimationKeyFrameInterval;
import com.iver.cit.gvsig.animation.traks.AnimationDatedTrack;
import com.iver.cit.gvsig.animation.traks.AnimationTimeTrack;
import com.iver.utiles.DateTime;
import com.iver.utiles.XMLEntity;


/**
 * @author
 * 
 * Testing IPersistance methods.
 */


public class AnimationPlayerIPersistanceTest extends TestCase {
	
	/*
	 * Testing getXMLEntity, setXMLEntity in AnimationPlayer.
	 */
	
	public void testAnimationPlayer()
	{
		AnimationPlayer animationPlayer = new AnimationPlayer();
		int antesFrameRate = 1;
		double antesGlobalTime = 60;
		int antesMode = 0;
		XMLEntity xml = new XMLEntity();
		
		
		animationPlayer.setFrameRate(antesFrameRate);
		animationPlayer.setGlobalTime(antesGlobalTime);
		animationPlayer.setAnimationMode(antesMode);
						
		xml = animationPlayer.getXMLEntity();
		animationPlayer.setXMLEntity(xml);
		

		int despuesMode = xml.getIntProperty("mode");
		double despuesGlobalTime = xml.getDoubleProperty("length");
		int despuesFrameRate = xml.getIntProperty("framerate");
		assertEquals(antesMode,despuesMode);
		assertEquals(antesGlobalTime,despuesGlobalTime,0);
		assertEquals(antesFrameRate,despuesFrameRate);
		
//		System.out.println("Escribiendo depuesMode: " + despuesMode);
//		System.out.println("Escribiendo despuesGlobalTime: " + despuesGlobalTime);
//		System.out.println("Escribiendo despuesFrameRate: " + despuesFrameRate);
//		
	}

	/*
	 * Testing getXMLEntity, setXMLEntity in AnimationTimeTrack.
	 */
	
	public void testAnimationTimeTrack()
	{
		AnimationTimeTrack animationTimeTrack = new AnimationTimeTrack(null);		
		XMLEntity xml = new XMLEntity();
		
		String antesName = "nombreCualkiera";
		String antesDescription = "DescripcionCualkiera";
		double antesBeginTime = 0;
		double antesEndTime = 1;
		
		animationTimeTrack.setName(antesName);
		animationTimeTrack.setDescription(antesDescription);
		animationTimeTrack.setBeginTime(antesBeginTime);
		animationTimeTrack.setendTime(antesEndTime);
						
		xml = animationTimeTrack.getXMLEntity();
		animationTimeTrack.setXMLEntity(xml);
		
		String despuesName = xml.getStringProperty("name");
		String despuesDescription = xml.getStringProperty("description");
		double despuesBeginTime = xml.getDoubleProperty("begin_time");
		double despuesEndTime = xml.getDoubleProperty("end_time");
		
		assertEquals(antesName,despuesName);
		assertEquals(antesDescription,despuesDescription);
		assertEquals(antesBeginTime,despuesBeginTime,0);
		assertEquals(antesEndTime,despuesEndTime,0);

	}
	
	/*
	 * Testing getXMLEntity, setXMLEntity in AnimationFunctionInterval.
	 */
	
	public void testAnimationFunctionInterval()
	{
		AnimationFunctionInterval interval = new AnimationFunctionInterval();
		XMLEntity xml = new XMLEntity();
		double antesInitialTime = 0;
		double antesEndTime = 1;
		double antesIntervalTime = antesEndTime - antesInitialTime;
		
		interval.setInitialTime(antesInitialTime);
		interval.setEndTime(antesEndTime);
		interval.setIntervalTime(antesIntervalTime);
						
		xml = interval.getXMLEntity();
		interval.setXMLEntity(xml);
		
		double despuesInitialInterval = xml.getDoubleProperty("begin_time");
		double despuesEndTime = xml.getDoubleProperty("end_time");
		double despuesIntervalTime = xml.getDoubleProperty("interval_time");
		assertEquals(antesInitialTime,despuesInitialInterval,0);
		assertEquals(antesEndTime,despuesEndTime,0);
		assertEquals(antesIntervalTime,despuesIntervalTime,0);
		
	}
	
	/*
	 * Testing getXMLEntity, setXMLEntity in AnimationKeyFrameInterval.
	 */
	
	public void testAnimationKeyFrameInterval()
	{
		AnimationKeyFrameInterval interval = new AnimationKeyFrameInterval();
		XMLEntity xml = new XMLEntity();
		
		double antesInitialTime = 0;
		double antesEndTime = 1;
		double antesIntervalTime = antesEndTime - antesInitialTime;
		
		interval.setInitialTime(antesInitialTime);
		interval.setEndTime(antesEndTime);
		interval.setIntervalTime(antesIntervalTime);
						
		xml = interval.getXMLEntity();
		interval.setXMLEntity(xml);
		

		double despuesInitialInterval = xml.getDoubleProperty("begin_time");
		double despuesEndTime = xml.getDoubleProperty("end_time");
		double despuesIntervalTime = xml.getDoubleProperty("interval_time");
		assertEquals(antesInitialTime,despuesInitialInterval,0);
		assertEquals(antesEndTime,despuesEndTime,0);
		assertEquals(antesIntervalTime,despuesIntervalTime,0);
		
	}
	
	/*
	 * Testing getXMLEntity, setXMLEntity in AnimationDatedTrack.
	 */
	
	public void testAnimationDatedTrack()
	{
		AnimationDatedTrack animationDatedTrack = new AnimationDatedTrack(null);
		
		XMLEntity xml = new XMLEntity();
		
		String antesName = "nombreCualkiera";
		String antesDescription = "DescripcionCualkiera";
		Date antesBeginDate = new Date(107,3,21);
		Date antesEndDate = new Date(107,6,21);
		String sFormat = new String( "Y-m-d" );
		String prueba = "nada";
		
		animationDatedTrack.setName(antesName);
		animationDatedTrack.setDescription(antesDescription);
		animationDatedTrack.setBeginDate(antesBeginDate);
		animationDatedTrack.setEndDate(antesEndDate);
						
		xml = animationDatedTrack.getXMLEntity();
		animationDatedTrack.setXMLEntity(xml);
		
		String despuesName = xml.getStringProperty("name");
		String despuesDescription = xml.getStringProperty("description");
		String despuesBeginDate = xml.getStringProperty("begin_date");
		String despuesEndDate = xml.getStringProperty("end_date");
		
		String antesBeginDateString = DateTime.dateToString(antesBeginDate, sFormat);
		String antesEndDateString = DateTime.dateToString(antesEndDate, sFormat);
		
		
		assertEquals(antesName,despuesName);
		assertEquals(antesDescription,despuesDescription);
		assertEquals(prueba,antesBeginDateString,despuesBeginDate);
		assertEquals(prueba,antesEndDateString,despuesEndDate);

	}
	
	/*
	 * Testing getXMLEntity, setXMLEntity in AnimationDatedInterval.
	 */
	
	public void testAnimationDatedInterval()
	{
		AnimationDatedInterval animationDatedInterval = new AnimationDatedInterval();
		
		XMLEntity xml = new XMLEntity();
		
		Date antesBeginDate = new Date(107,3,21);
		Date antesEndDate = new Date(107,6,21);
		String sFormat = new String( "Y-m-d" );
		String prueba = "nada";
		
		animationDatedInterval.setBeginDateInterval(antesBeginDate);
		animationDatedInterval.setEndDateInterval(antesEndDate);
						
		xml = animationDatedInterval.getXMLEntity();
		animationDatedInterval.setXMLEntity(xml);
		
		String despuesBeginDate = xml.getStringProperty("begin_date");
		String despuesEndDate = xml.getStringProperty("end_date");
		String antesBeginDateString = DateTime.dateToString(antesBeginDate, sFormat);
		String antesEndDateString = DateTime.dateToString(antesEndDate, sFormat);
		
		assertEquals(prueba,antesBeginDateString,despuesBeginDate);
		assertEquals(prueba,antesEndDateString,despuesEndDate);

	}
	
}
