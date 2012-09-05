package org.gvsig.fmap.dal.store.shp.utils;

import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;

import org.gvsig.fmap.geom.Geometry;

public class SHPNull implements SHPShape {
	public int getShapeType() {
		return Geometry.TYPES.NULL;
	}

	public SHPNull(int type)  {
	}
	public SHPNull() {
	}
	public Geometry read(MappedByteBuffer buffer, int type) {
		return null;
	}

	public void write(ByteBuffer buffer, Geometry geometry) {
	}

	public int getLength(Geometry fgeometry) {
		return 4;
	}

	public void obtainsPoints(Geometry g) {
	}

}
