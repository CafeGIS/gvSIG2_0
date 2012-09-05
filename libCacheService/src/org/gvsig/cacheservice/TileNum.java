/*
 * Created on 25-ago-2005
 */
package org.gvsig.cacheservice;

import java.awt.Point;
import java.util.TreeMap;

/**
 * @author Luis W. Sevilla (sevilla_lui@gva.es)
 * @author Rafael Gait�n <rgaitan@dsic.upv.es>
 * Identificador de Tile.
 */
public class TileNum {
	private int level;
	private Point num;
	
	/**
	 * 
	 */
	public TileNum(int level, Point num) {
		super();
		this.level = level;
		this.num = num;
	}

	/**
	 * Crea un TileNum desde un tileId de google.
	 * Solo es v�lido a partir de un nivel 2 (4x2 tiles).
	 * @param tileId
	 * @return
	 */
	
	public TileNum(String tileId) {
		TreeMap map = new TreeMap();
		map.put("q", new Integer(0));
		map.put("r", new Integer(1));
		map.put("t", new Integer(2));
		map.put("s", new Integer(3));
		int x=0, y=0;
		
		for (int i=1; i<tileId.length(); i++) {
			int index = ((Integer) map.get(tileId.substring(i,i+1))).intValue();
			int ord = (int) Math.pow(2,(tileId.length()-i-1));
			x += ord*(index & 0x1);
			y += ord*((index & 0x2) >> 0x1);
		}
		y -= Math.pow(2,(tileId.length()-3));
		
		num = new Point(x, y);
		level = tileId.length()-1;
	}
	/**
	 * @return Returns the level.
	 */
	public int getLevel() {
		return level;
	}
	/**
	 * @return Returns the num.
	 */
	public Point getNum() {
		return num;
	}
	/**
	 * @return
	 */
	public int getX() {
		return num.x;
	}
	/**
	 * @return
	 */
	public int getY() {
		return num.y;
	}
	
	public String toString() {
		return "TileNum["+level+",("+num.x+","+num.y+")]";
	}
	
	public String getTileId(int tileIdType) {
		if (tileIdType == CacheService.TILEID_TYPE_OSGPLANET)
			return numToOpTileId();
		else if (tileIdType == CacheService.TILEID_TYPE_VIRTUALEARTH)
			return numToVeTileId();
		else
			return numToGmTileId();
	}
	
	/**
	 * Genera un VeTileId (identificador de tile de MSN Virtual Earth) a partir del numero de tile y nivel.
	 * @param num numero de tile
	 * @param level nivel
	 * @return
	 */
	private String numToVeTileId() {
		String tileId = "";
		int y = num.y + (int)Math.pow(2,(level-2));
		for (int i=0; i<level; i++) {
			int ord = (int) Math.pow(2,(level-i-1));
			tileId += ""+(2*((y / ord) & 0x1)+((num.x / ord) & 0x1));	
		}
		return tileId;
	}

	private String [] quadKeys = {"q", "r", "t", "s"};

	/**
	 * Genera un GmTileId (identificador de tile de GoogleMaps) a partir del numero de tile y nivel.
	 * @param num numero de tile
	 * @param level nivel
	 * @return
	 */
	private String numToGmTileId() {
		String tileId = "t";
		int y = num.y + (int)Math.pow(2,(level-2));
		for (int i=0; i<level; i++) {
			int ord = (int) Math.pow(2,(level-i-1));
			tileId += quadKeys[2*((y / ord) & 0x1)+((num.x / ord) & 0x1)];	
		}
		return tileId;
	}
	

	/**
	 * Genera un OpTileId (identificador de tile de OsgPlanet) a partir del numero de tile y nivel.
	 * @param num numero de tile
	 * @param level nivel
	 * @return
	 */
	public String numToOpTileId() {
		return "L"+(level-1)+"_X"+num.x+"_Y"+num.y;
	}
	

}
