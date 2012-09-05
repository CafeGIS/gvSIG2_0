package org.gvsig.cacheservice;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.io.File;
/**
 *
 * @author Rafael Gait�n <rgaitan@dsic.upv.es>, modified code from cq RasterCache of Luis W. Sevilla.
 *
 */
public abstract class CacheService {

	/**
	 * Tipo planetario. Las coordenadas son Lat/Lon
	 */
	public static int SPHERIC	= 0x10;
	/**
	 * Tipo plano (UTM). Las coordenadas son m�tricas.
	 */
	public static int PLANE		= 0x20;
	/**
	 * el cach� se realiza en funci�n del tama�o del fichero,
	 * con los tiles relativos a su coordenada 0,0.
	 */
	public static int LOCAL		= 0x100;
	/**
	 * el cach� se realiza con parametros globales, de manera que
	 * el tileado es con respecto a una rejilla de referencia, y no
	 * calculado especialmente para ese raster.
	 */
	public static int GLOBAL	= 0x200;

	public static final Rectangle2D sphericGlobalBounds =
		new Rectangle2D.Double(-180,-90,360,180);
	public static final Rectangle2D planeGlobalBounds =
		new Rectangle2D.Double(-20000000,-10000000,40000000,20000000);

	public static int TILEID_TYPE_GOOGLE 		= 0x01;
	public static int TILEID_TYPE_OSGPLANET		= 0x02;
	public static int TILEID_TYPE_VIRTUALEARTH	= 0x03;

	private String _name = "";
	private String _planet = "";

	private int _cacheType = GLOBAL | SPHERIC;
	private int _tileIdType = TILEID_TYPE_OSGPLANET;
	private String _cacheRootDir = "/data/cache";
	private String _cacheDir = null;
	private int _tileSize = 256;


	private boolean _withHashDirs = false;
	private int _hashDirsNumber = 100;

	private int _minLevel = 0;
	private int _maxLevel = 20;

	private Rectangle2D _bounds;

	private Rectangle2D _fullExtent = sphericGlobalBounds;

	/**
	 * @param planet
	 * @param name
	 * @param server
	 */
	public CacheService(String planet, String name) {
		super();
		this._name = name;
		this._planet = planet;
		setBounds(sphericGlobalBounds);
	}


	/**
	 * Devuelve el tama�o de Tile como Dimension;
	 * @return
	 */
	public Dimension computeSz() {
		return new Dimension(getTileSize(),getTileSize());
	}

	public Dimension getFullSize(int level) {
		int w = getTileSize() * (int)Math.pow(2,level);
		return new Dimension(w, w/2);
	}

	/**
	 * Devuelve el numero de Tiles (ancho y alto) para ese nivel.
	 * @param level
	 * @return
	 */
	public Dimension getLevelSize(int level) {
		int w = (int)Math.pow(2,level);
		return new Dimension(w, w/2);
	}

	public boolean isFileInCache(String fName) {
		File cacheFile = new File(fName);
		return cacheFile.exists();
	}


	public void setCacheDir(String dir) {
		_cacheDir = dir;
	}
	public String getCacheDir() {
		File file;
		if (_cacheDir == null) {
			// Compruebo si existe el directorio del planeta
			_cacheDir = getCacheRootDir()+"/"+getPlanet()+"/";
			file = new File(_cacheDir);
			if (!file.exists()) file.mkdir();
			// Compruebo si existe el directorio del servidor
			_cacheDir += getName()+"/";
		}
		file = new File(_cacheDir);
		if (!file.exists()) file.mkdirs();
		return _cacheDir;
	}

	/**
	 * @return Returns the cacheRootDir.
	 */
	public String getCacheRootDir() {
		return _cacheRootDir;
	}
	/**
	 * @param cacheRootDir The cacheRootDir to set.
	 */
	public void setCacheRootDir(String cacheRootDir) {
		_cacheRootDir = cacheRootDir;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return _name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		_name = name;
	}
	/**
	 * @return Returns the planet.
	 */
	public String getPlanet() {
		return _planet;
	}
	/**
	 * @param planet The planet to set.
	 */
	public void setPlanet(String planet) {
		_planet = planet;
	}
	/**
	 * @return Returns the tileSize.
	 */
	public int getTileSize() {
		return _tileSize;
	}
	/**
	 * @param tileSize The tileSize to set.
	 */
	public void setTileSize(int tileSize) {
		_tileSize = tileSize;
	}
	/**
	 * @return Returns the tileIdType.
	 */
	public int getTileIdType() {
		return _tileIdType;
	}
	/**
	 * @param tileIdType The tileIdType to set.
	 */
	public void setTileIdType(int tileIdType) {
		_tileIdType = tileIdType;
	}
	/**
	 * @return Returns the maxLevel.
	 */
	public int getMaxLevel() {
		return _maxLevel;
	}
	/**
	 * @param maxLevel The maxLevel to set.
	 */
	public void setMaxLevel(int maxLevel) {
		_maxLevel = maxLevel;
	}
	/**
	 * @return Returns the minLevel.
	 */
	public int getMinLevel() {
		return _minLevel;
	}
	/**
	 * @param minLevel The minLevel to set.
	 */
	public void setMinLevel(int minLevel) {
		_minLevel = minLevel;
	}
	/**
	 * @return Returns the bounds.
	 */
	public Rectangle2D getBounds() {
		return _bounds;
	}

	public void setBounds(Rectangle2D r) {
		_bounds = r;
	}
	/**
	 * @return Returns the cacheType.
	 */
	public int getCacheType() {
		return _cacheType;
	}
	/**
	 * @param cacheType The cacheType to set.
	 */
	public void setCacheType(int cacheType) {
		_cacheType = cacheType;
		if ((cacheType & GLOBAL) == GLOBAL)
			if ((cacheType & PLANE) == PLANE)
				setFullExtent(planeGlobalBounds);
			else if ((cacheType & SPHERIC) == SPHERIC)
				setFullExtent(sphericGlobalBounds);
	}
	/**
	 * @return Returns the fullExtent.
	 */
	public Rectangle2D getFullExtent() {
		return _fullExtent;
	}
	/**
	 * @param fullExtent The fullExtent to set.
	 */
	public void setFullExtent(Rectangle2D fullExtent) {
		_fullExtent = fullExtent;
	}
	/**
	 * Gets Number of Hash Directories. 100 by default.
	 * @return Returns the hashDirsOrder.
	 */
	public int getHashDirsNumber() {
		return _hashDirsNumber;
	}
	/**
	 * Sets Number of Hash Directories.
	 * @param hashDirsOrder The hashDirsOrder to set.
	 */
	public void setHashDirsNumber(int hashDirsNumber) {
		_hashDirsNumber = hashDirsNumber;
	}
	/**
	 * returns if this cache has HashDirectories. False by difault.
	 * @return Returns the withHashDirs.
	 */
	public boolean isWithHashDirs() {
		return _withHashDirs;
	}
	/**
	 * sets wheter this cache has HashDirectories or no.
	 * @param withHashDirs The withHashDirs to set.
	 */
	public void setWithHashDirs(boolean withHashDirs) {
		_withHashDirs = withHashDirs;
	}
	/**
	 * Compute HashDirNumbre from tileNumber.
	 * @param num tileNumber (without level).
	 * @return
	 */
	public String getHashDir(Point num) {
		String hd = null;
		hd = ""+(num.x % _hashDirsNumber);
		return hd;
	}
}
