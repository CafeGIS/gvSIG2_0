package org.gvsig.gvsig3d.drivers;

import java.io.File;

import org.gvsig.driver.OSGDriver;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.primitive.AbstractPrimitive;
import org.gvsig.geometries3D.MultiGeometry;
import org.gvsig.geometries3D.MultiSolid;
import org.gvsig.gpe.osg.OSGParser;
import org.gvsig.operations3D.Draw3DMultiSolid;
import org.gvsig.operations3D.context.Draw3DContext;
import org.gvsig.osgvp.core.osg.Group;
import org.gvsig.osgvp.exceptions.node.NodeException;

import com.hardcode.gdbms.driver.exceptions.CloseDriverException;
import com.hardcode.gdbms.driver.exceptions.OpenDriverException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.driver.exceptions.WriteDriverException;
import com.hardcode.gdbms.engine.data.edition.DataWare;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.drivers.DriverAttributes;
import com.iver.cit.gvsig.fmap.drivers.MemoryDriver;
import com.iver.cit.gvsig.fmap.drivers.VectorialFileDriver;

public class GvsigDriverOSG extends MemoryDriver implements VectorialFileDriver {

	private File m_Fich;
	private DriverAttributes attr = new DriverAttributes();
	private OSGDriver _osgDriver;
	private Group osgGroupRoot;

	// tipo grupo set mas get, en el load hago un set de lo q devuelve el parser

	public String getName() {
		return "gvSIG OSG Driver";
	}

	public int getShapeType() {
		return FShape.MULTI;
	}

	public boolean accept(File f) {
		if ((f.getName().toUpperCase().endsWith("OSG"))
				|| (f.getName().toUpperCase().endsWith("IVE"))) {
			return true;
		}
		return false;
	}

	public void close() throws CloseDriverException {
		// TODO Auto-generated method stub

	}

	public File getFile() {
		return m_Fich;
	}

	public void initialize() throws ReadDriverException {
		this.parseStream();
		
	}

	public void parseStream() {

		_osgDriver = new OSGDriver();
		OSGParser parser = new OSGParser("OSG", "OSG File Formats Parser");

		parser.parse(_osgDriver, null, m_Fich.toURI());

		// Getting the geometry from the driver.
	}

	public void buildGeometry(AbstractPrimitive geom, Group group)
			throws NodeException {

		int i;

		if (geom instanceof MultiGeometry) {
			MultiGeometry multiGeometry = (MultiGeometry) geom;
			for (i = 0; i < multiGeometry.getGeometries().size(); i++) {

				Group child = new Group();
				group.addChild(child);
				buildGeometry(multiGeometry.getGeometries().get(i), child);
			}
		} else if (geom instanceof MultiSolid) {
			// Getting the geometry
			MultiSolid multiSolid = (MultiSolid) geom;
			// Creating the context and adding parameters
			Draw3DContext ctx3D = new Draw3DContext();
			ctx3D.setGroup(group);
			// Creating the drawing operation
			Draw3DMultiSolid d3DMultiSolid = new Draw3DMultiSolid();

			try {
				// Invoking the operation for the multisolid
				multiSolid.invokeOperation(d3DMultiSolid.getOperationIndex(),
						ctx3D);
			} catch (GeometryOperationNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GeometryOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void open(File f) throws OpenDriverException {
		m_Fich = f;

	}

	public DriverAttributes getDriverAttributes() {
		return attr;
	}

	public boolean isWritable() {
		return m_Fich.canWrite();
	}

	public int[] getPrimaryKeys() throws ReadDriverException {
		return null;
	}

	public void write(DataWare arg0) throws WriteDriverException,
			ReadDriverException {
		// TODO Auto-generated method stub

	}

	public Group getOsgGroupRoot() {
		return osgGroupRoot;
	}

	public void setOsgGroupRoot(Group osgGroupRoot) {
		this.osgGroupRoot = osgGroupRoot;
	}

	public MultiGeometry getOsgRootMultigometry() {
		return _osgDriver.getRootFeature();
	}

	public OSGDriver getOSGDriver() {
		return _osgDriver;
	}

	public void setOSGDriver(OSGDriver driver) {
		_osgDriver = driver;
	}

}
