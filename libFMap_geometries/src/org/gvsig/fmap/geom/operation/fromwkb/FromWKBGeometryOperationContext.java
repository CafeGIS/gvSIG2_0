package org.gvsig.fmap.geom.operation.fromwkb;

import org.gvsig.fmap.geom.operation.GeometryOperationContext;

public class FromWKBGeometryOperationContext extends GeometryOperationContext{
	private byte[] data=null;

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}
