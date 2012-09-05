package org.gvsig.fmap.geom;

import junit.framework.TestCase;

import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.impl.DefaultGeometryLibrary;
import org.gvsig.fmap.geom.operation.GeometryOperation;
import org.gvsig.fmap.geom.operation.GeometryOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.operation.ensureOrientation.EnsureOrientation;
import org.gvsig.fmap.geom.operation.flip.Flip;
import org.gvsig.fmap.geom.operation.impl.DefaultGeometryOperationLibrary;
import org.gvsig.fmap.geom.operation.isCCW.IsCCW;
import org.gvsig.fmap.geom.operation.tojts.ToJTS;
import org.gvsig.fmap.geom.operation.towkb.ToWKB;
import org.gvsig.fmap.geom.primitive.Arc;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.impl.Arc2D;
import org.gvsig.fmap.geom.type.GeometryType;
import org.gvsig.fmap.geom.type.GeometryTypeNotSupportedException;
import org.gvsig.fmap.geom.type.GeometryTypeNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeometryManagerTest extends TestCase {

	final static private Logger logger = LoggerFactory.getLogger("org.gvsig");

	static {
		//logger.addAppender(new ConsoleAppender(new SimpleLayout()));
	}

	private GeometryManager manager;

	protected void setUp() throws Exception {
		super.setUp();

		DefaultGeometryLibrary lib = new DefaultGeometryLibrary();
		lib.initialize();
		lib.postInitialize();

		DefaultGeometryOperationLibrary opLib = new DefaultGeometryOperationLibrary();
		opLib.initialize();
		opLib.postInitialize();

		manager = GeometryLocator.getGeometryManager();
	}

	public void testRegister() throws Exception {
		logger.debug("--------- testRegister() START ----------");

		Point p2d = (Point) manager.create(TYPES.POINT, SUBTYPES.GEOM2D);
		assertNotNull(p2d);
		p2d.setCoordinateAt(0, 0);
		p2d.setCoordinateAt(1, 0);

		java.awt.geom.Point2D pt1 = new java.awt.geom.Point2D.Float(0,1);
		java.awt.geom.Point2D pt2 = new java.awt.geom.Point2D.Float(1,1);
		java.awt.geom.Point2D pt3 = new java.awt.geom.Point2D.Float(1,0);
		Arc2D c2d = (Arc2D) manager.create(TYPES.ARC, SUBTYPES.GEOM2D);
		assertNotNull(c2d);
		
		GeometryType geomType1 = manager.getGeometryType(TYPES.POINT, SUBTYPES.GEOM2D);
		GeometryType geomType2 = manager.getGeometryType(TYPES.ARC, SUBTYPES.GEOM2D);

		assertEquals(p2d.getGeometryType(), geomType1);
		assertEquals(c2d.getGeometryType(), geomType2);

		int op1 = manager.registerGeometryOperation("toJTS", new ToJTS(), geomType1);
		logger.debug("op1=" + op1);

		int op2 = -1;
		try {
			op2 = manager.registerGeometryOperation("toJTS", new ToJTS(), geomType1);
			logger.debug("op2=" + op2);
		} catch (Exception e) {
			logger.error("Error registrando operación: ", e);
		}

		int op3 = manager.registerGeometryOperation("toWKB", new ToWKB(),geomType1);
		int op4 = manager.registerGeometryOperation("toWKB", new ToWKB(),geomType2);

		logger.debug("op3=" + op3);

		assertTrue("FALLO: op1 != op2", op1 == op2);
		assertTrue("FALLO: op1 == op3", op1 != op3);
		assertFalse("FALLO: op4 == op", op4 == op1);

		//Point p = (Point) factory.createPoint2D(1, 2);
		Point p = (Point) manager.create(TYPES.POINT, SUBTYPES.GEOM2D);

		GeometryOperationContext ctx = new GeometryOperationContext();
		ctx.setAttribute("dummy", "DummyValue");

		try {
			p.invokeOperation(ToWKB.CODE, ctx);
			p.invokeOperation(ToJTS.CODE, ctx);
			p.invokeOperation(Flip.CODE, ctx);
		} catch (Exception e) {
			logger.error("Error, ", e);
		}

		Geometry sol = manager.create(TYPES.SOLID, SUBTYPES.GEOM2DZ);
		assertNotNull(sol);
		//logger.debug(sol);

		logger.debug("--------- testRegister() END ----------");
	}

	public void testGeneralPathXOperations() throws InstantiationException, IllegalAccessException, CreateGeometryException{
		logger.debug("--------- Test GeneralPathX Operation START ----------");

		//Registering the operation Flip() to all the geometries...
		manager.registerGeometryOperation("flip", new Flip());

		//Registering the operation ensureOrientation() to all the geometries...
		manager.registerGeometryOperation("ensureOrientation", new EnsureOrientation());

		//Registering the operation isCCW() to all the geometries...
		manager.registerGeometryOperation("isCCW", new IsCCW());

		//Building the Points for a curve...
//		java.awt.geom.Point2D p1 = new java.awt.geom.Point2D.Double(1,2);
//		java.awt.geom.Point2D p2 = new java.awt.geom.Point2D.Double(2,1);
//		java.awt.geom.Point2D p3 = new java.awt.geom.Point2D.Double(3,3);

		Point p1 = (Point)manager.create(TYPES.POINT, SUBTYPES.GEOM2D);
		p1.setCoordinateAt(0, 1);
		p1.setCoordinateAt(1, 2);

		Point p2 = (Point)manager.create(TYPES.POINT, SUBTYPES.GEOM2D);
		p2.setCoordinateAt(0, 2);
		p2.setCoordinateAt(1, 1);

		Point p3 = (Point)manager.create(TYPES.POINT, SUBTYPES.GEOM2D);
		p3.setCoordinateAt(0, 3);
		p3.setCoordinateAt(1, 3);

		//Build a curve to get the operation invoked with the registered operation.
		//Geometry curve = (Curve) factory.createArc(p1, p2, p3);
		Arc arc = (Arc) manager.create(TYPES.ARC, SUBTYPES.GEOM2D);
        arc.setPoints(p1, p2, p3);

		try {
			arc.invokeOperation(Flip.CODE, null);
		} catch (GeometryOperationNotSupportedException e) {
			logger.error("Operation doesn't be registered for this geometry. \n", e);
		} catch (GeometryOperationException e) {
			logger.error("An error produced while the Operation was running. \n", e);
		}
		GeometryOperationContext ctx = new GeometryOperationContext();
		ctx.setAttribute("bCCW",new Boolean(true));
		Boolean aux1 = null;
		try {
			aux1 = (Boolean) arc.invokeOperation(EnsureOrientation.CODE, ctx);
		} catch (GeometryOperationNotSupportedException e) {
			logger.error("Operation doesn't be registered for this geometry. \n", e);
		} catch (GeometryOperationException e) {
			// TODO Auto-generated catch block
			logger.error("An error produced while the Operation was running. \n", e);
		}
		//True si es exterior, le ha dado la vuelta.
		assertTrue(aux1.booleanValue());

		Boolean aux2 = null;
		try {
			aux2 = (Boolean) arc.invokeOperation(IsCCW.CODE, null);
		} catch (GeometryOperationNotSupportedException e) {
			logger.error("Operation doesn't be registered for this geometry. \n", e);
		} catch (GeometryOperationException e) {
			// TODO Auto-generated catch block
			logger.error("An error produced while the Operation was running. \n", e);
		}
		//True si es CCW.
		assertTrue(aux2.booleanValue());

		logger.debug("--------- Test GeneralPathX Operation END ----------");
	}

	public void testInvoke() throws InstantiationException, IllegalAccessException, CreateGeometryException {
		int size = 100000;

		// Fill the operation context with required params
		GeometryOperationContext ctx = new GeometryOperationContext();
		GeometryOperationContext var = new GeometryOperationContext();
		var.setAttribute("bCCW",new Boolean(true));

		logger.debug("ToJTS.-");
		indirectInvoke(ToJTS.CODE, ctx, size);
		directInvoke(ToJTS.CODE, ctx, size);
		logger.debug("ToWKB.-");
		indirectInvoke(ToWKB.CODE, null, size);
		directInvoke(ToWKB.CODE, null, size);
		logger.debug("Flip.-");
		indirectInvoke(Flip.CODE, ctx, size);
		directInvoke(Flip.CODE, ctx, size);
		logger.debug("EnsureOrientation.-");
		//indirectInvoke(EnsureOrientation.CODE, var, size);
		directInvoke(EnsureOrientation.CODE, var, size);
		logger.debug("isCCW.-");
		//indirectInvoke(IsCCW.CODE, ctx, size);
		directInvoke(IsCCW.CODE, ctx, size);

	}

	private Geometry[] createGeometryArray(int size) throws InstantiationException, IllegalAccessException, CreateGeometryException {
		Geometry[] geom = new Geometry[size];
		for (int i = 0; i < size; i++) {
			//geom[i] = factory.createPoint2D(Math.log(1 - i), Math.log(i + 1));
			geom[i] = manager.create(TYPES.POINT, SUBTYPES.GEOM2D);
		}
		return geom;
	}

	private long directInvoke(int opCode, GeometryOperationContext ctx, int size) throws InstantiationException, IllegalAccessException, CreateGeometryException {
		Geometry[] geoms = createGeometryArray(size);

		long t0 = System.currentTimeMillis();

		GeometryOperation geomOp = null;
		try {
			geomOp = manager.getGeometryOperation(ToJTS.CODE, TYPES.POINT, SUBTYPES.GEOM2D);
		} catch (GeometryTypeNotSupportedException gtnse) {
			logger.error("Error:", gtnse);
		} catch (GeometryOperationNotSupportedException gonse) {
			logger.error("Error:", gonse);
		} catch (GeometryTypeNotValidException e) {
			logger.error("Error:", e);
		}

		// Here is the main loop where you call the operation
		try {
			for (int i = 0; i < geoms.length; i++) {
				Object result = geomOp.invoke(geoms[i], ctx);
				// if(i%100 == 99) logger.debug(result);
			}

		} catch (GeometryOperationException goe) {
			logger.error("Error:", goe);
		}
		long t1 = System.currentTimeMillis();
		long t = t1 - t0;
		logger.debug("Milliseconds (direct): " + t);
		return t;
	}

	private long indirectInvoke(int opCode, GeometryOperationContext ctx,
			int size) throws InstantiationException, IllegalAccessException, CreateGeometryException {
		Geometry[] geoms = createGeometryArray(size);

		long t0 = System.currentTimeMillis();

		// Here is the main loop where you call the operation
		try {
			for (int i = 0; i < geoms.length; i++) {
				Object result = (geoms[i]).invokeOperation(opCode,
						ctx);
				// if(i%100 == 99) logger.debug(result);
			}
		} catch (GeometryOperationNotSupportedException gonse) {
			logger.error("Error:", gonse);
		} catch (GeometryOperationException goe) {
			logger.error("Error:", goe);
		}
		long t1 = System.currentTimeMillis();
		long t = t1 - t0;
		logger.debug("Milliseconds (indirect): " + t);
		return t;
	}

//	public void testRegisterBasicGeometryType() {
//		GeometryType gt = GeometryLocator.getGeometryManager()
//		.registerGeometryType(DummyPoint2D.class, "DummyPoint2D", TYPES.ELLIPSE,  SUBTYPES.GEOM2DZ);
//
//		logger.debug(String.valueOf(gt.getType()));
//
//		GeometryLocator.getGeometryManager().unregisterGeometryType(DummyPoint2D.class);
//
//		gt = GeometryLocator.getGeometryManager()
//		.registerGeometryType(DummyPoint2D.class, "DummyPoint2D", TYPES.POINT, SUBTYPES.GEOM2D);
//
//		logger.debug(String.valueOf(gt.getType()));
//		GeometryLocator.getGeometryManager().unregisterGeometryType(
//				DummyPoint2D.class);
//		
//	}
}
