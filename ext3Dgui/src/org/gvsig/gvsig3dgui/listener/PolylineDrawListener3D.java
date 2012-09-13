package org.gvsig.gvsig3dgui.listener;

import java.awt.Cursor;

import org.gvsig.gvsig3d.map3d.MapControl3D;

import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.tools.BehaviorException;
import com.iver.cit.gvsig.fmap.tools.PolygonSelectionListener;
import com.iver.cit.gvsig.fmap.tools.Events.MeasureEvent;
import com.iver.cit.gvsig.fmap.tools.Listeners.PolylineListener;

public class PolylineDrawListener3D extends  PolygonSelectionListener{
	
	public PolylineDrawListener3D(MapControl mapCtrl) {
		super(mapCtrl);
	}
	public void pointFixed(MeasureEvent arg0) throws BehaviorException {
		// TODO Auto-generated method stub
		
	}

	public void points(MeasureEvent arg0) throws BehaviorException {
		// TODO Auto-generated method stub
		
	}

	public void polylineFinished(MeasureEvent arg0) throws BehaviorException {
		// TODO Auto-generated method stub
		
	}

	public boolean cancelDrawing() {
		// TODO Auto-generated method stub
		return false;
	}

	

}
