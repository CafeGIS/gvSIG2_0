package org.gvsig.gvsig3d.simbology3D.symbol3D.point;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

import org.gvsig.gvsig3d.simbology3D.symbol3D.Abstract3DSymbol;
import org.gvsig.osgvp.core.osg.AutoTransform;
import org.gvsig.osgvp.core.osg.Group;
import org.gvsig.osgvp.core.osg.Node;
import org.gvsig.osgvp.core.osg.PositionAttitudeTransform;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.core.osgdb.osgDB;
import org.gvsig.osgvp.exceptions.node.NodeException;

import com.iver.ai2.gvsig3d.legend.symbols.Object3DMarkerSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;

public class ObjectPoint3DSymbol extends Abstract3DSymbol {

	private Object3DMarkerSymbol object3DMarketSymbol;
	private static Node object3D = null;
	private static String oldPath = "";

	public ObjectPoint3DSymbol(ISymbol symbol) {
		super(symbol);
		object3DMarketSymbol = (Object3DMarkerSymbol) symbol;
	}

	@Override
	public Node generateSymbol(List<Vec3> position) {

		Group group = new Group();
		PositionAttitudeTransform posAttTrasn = null;
		Vec3 scale = object3DMarketSymbol.getScale();
		Vec3 rotation = object3DMarketSymbol.getRotationObject();
		try {
			// Node object3D = osgDB.readNodeFile("D:/modelos3d/cow.ive");
			if (!(oldPath.equals(object3DMarketSymbol.getObject3DPath()))){
				oldPath = object3DMarketSymbol.getObject3DPath();
				object3D = osgDB.readNodeFile(oldPath);
			}
			for (Iterator iterator = position.iterator(); iterator.hasNext();) {
				Vec3 pos = (Vec3) iterator.next();
				System.err.println("position " + pos.x() + " " + pos.y() + " "
						+ pos.z());
				posAttTrasn = new PositionAttitudeTransform();
				posAttTrasn.setPosition(pos);
				posAttTrasn.setScale(scale);

				if (object3DMarketSymbol.isAutoRotate()) {
					AutoTransform autoRotateTrasn = null;
					autoRotateTrasn = new AutoTransform();
					autoRotateTrasn.setPosition(pos);
					autoRotateTrasn.addChild(posAttTrasn);
					autoRotateTrasn
							.setAutoRotateMode(AutoTransform.AutoRotateMode.ROTATE_TO_CAMERA);
					// autoRotateTrasn.setAutoScaleToScreen(true);
					group.addChild(autoRotateTrasn);
				} else {
					group.addChild(posAttTrasn);
				}
				posAttTrasn.addChild(object3D);

			}
		} catch (NodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return group;
	}

}
