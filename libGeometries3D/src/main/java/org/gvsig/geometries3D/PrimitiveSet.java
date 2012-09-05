package org.gvsig.geometries3D;


import java.util.Vector;

public class PrimitiveSet {
	public static class Mode {
		public final static int POINTS = 0;
		public final static int LINES = 1;
		public final static int LINE_STRIP = 2;
		public final static int LINE_LOOP = 3;
		public final static int TRIANGLES = 4;
		public final static int TRIANGLE_STRIP = 5;
		public final static int TRIANGLE_FAN = 6;
		public final static int QUADS = 7;
		public final static int QUAD_STRIP = 8;
		public final static int POLYGON = 9;
	}

	public static class Type {
		public final static int PrimitiveType = 0;
		public final static int DrawArrays= 1;
		public final static int DrawArrayLengths= 2;
		public final static int DrawElementsUInt = 5;
	}
	
	private Vector<Integer> indices = null;
	
	protected int mode =  Mode.TRIANGLES;
	
	protected int type = Type.DrawArrays;
	
	public PrimitiveSet() {
		
	}
	
	public PrimitiveSet(int mode, int type) {
		this.mode = mode;
		this.type = type;
		indices = new Vector<Integer>();
	}

	public Vector<Integer> getIndexArray() {
		return indices;
	}

	public void setIndexArray(Vector<Integer> indices) {
		this.indices = indices;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Vector<Integer> getIndices() {
		return indices;
	}

	public void setIndices(Vector<Integer> indices) {
		this.indices = indices;
	}
}
