package org.gvsig.cacheservice;

import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;

import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.PNGEncodeParam;
import com.sun.media.jai.codec.TIFFEncodeParam;

//import javax.imageio.ImageIO;

public abstract class RasterCacheService extends CacheService {

	private String _ext = ".jpg";

	public RasterCacheService(String planet, String name) {
		super(planet, name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Obtains the full path of the file associated to the given tilenum and the
	 * current rastercacheservice configuration.
	 * 
	 * @param tileNum
	 * @return Full path to the filename.
	 */
	protected String getTileFileName(TileNum tileNum) {
		String tileId = tileNum.getTileId(getTileIdType());

		String fileName = getCacheDir();
		// Adds HasdDir level to cache image file path.
		if (isWithHashDirs()) {
			Point num = tileNum.getNum();
			fileName += getHashDir(num) + "/";
		}

		return fileName + tileId + getFileExtension();
	}

	/**
	 * Process the tile an returns the image.
	 * 
	 * @param tileNum
	 * @return The image of the.
	 * @throws CacheServiceException
	 */
	abstract public Image getTileAsImage(TileNum tileNum, Rectangle2D extent)
			throws CacheServiceException;

	public Image getTileAsImage(int level, Point num, Rectangle2D extent)
			throws CacheServiceException {
		return getTileAsImage(new TileNum(level, num), extent);
	}

	/**
	 * devuelve el nombre del fichero en el que se salv� el tile.
	 * 
	 * @param tileNum
	 * @return path completo del fichero de tile.
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	abstract public String getTileAsFName(TileNum tileNum, Rectangle2D extent)
			throws CacheServiceException;

	/**
	 * devuelve el nombre del fichero en el que se salv� el tile.
	 * 
	 * @param level
	 *            nivel de zoom
	 * @param num
	 *            posici�n del tile
	 * @return path completo del fichero de tile.
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public String getTileAsFName(int level, Point num, Rectangle2D extent)
			throws CacheServiceException {
		return getTileAsFName(new TileNum(level, num), extent);
	}

	public Image getImageFromCachedFile(String fName) throws IOException {
		File cacheFile = new File(fName);
		return ImageIO.read(cacheFile);
	}

	public void saveCachedFile(Image img, String format, String fName)
			throws IOException {
		// ImageIO.write((RenderedImage) img, format, new File(fName));
		File file = new File(fName);
		FileOutputStream out = new FileOutputStream(file);

		if (format.equals("png")) {
			PNGEncodeParam params = new PNGEncodeParam.RGB();
			// params.setsetCompression(TIFFEncodeParam.COMPRESSION_NONE);
			ImageEncoder encoder = ImageCodec.createImageEncoder("PNG", out,
					params);
			if (encoder == null) {
				System.out.println("imageEncoder is null");
				System.exit(0);
			}
			encoder.encode((RenderedImage) img);
		} else if (format.equals("tif")) {
			TIFFEncodeParam params = new TIFFEncodeParam();
			params.setCompression(TIFFEncodeParam.COMPRESSION_NONE);
			ImageEncoder encoder = ImageCodec.createImageEncoder("TIFF", out,
					params);
			if (encoder == null) {
				System.out.println("imageEncoder is null");
				System.exit(0);
			}
			encoder.encode((RenderedImage) img);
		}

	}

	public void saveCachedFile(Image img, String format, File f)
			throws IOException {
//		ImageIO.write((RenderedImage) img, format, f);
//		File file = new File(fName);
		FileOutputStream out = new FileOutputStream(f);

		if (format.equals("png")) {
			PNGEncodeParam params = new PNGEncodeParam.RGB();
			// params.setsetCompression(TIFFEncodeParam.COMPRESSION_NONE);
			ImageEncoder encoder = ImageCodec.createImageEncoder("PNG", out,
					params);
			if (encoder == null) {
				System.out.println("imageEncoder is null");
				System.exit(0);
			}
			encoder.encode((RenderedImage) img);
		} else if (format.equals("tif")) {
			TIFFEncodeParam params = new TIFFEncodeParam();
			params.setCompression(TIFFEncodeParam.COMPRESSION_NONE);
			ImageEncoder encoder = ImageCodec.createImageEncoder("TIFF", out,
					params);
			if (encoder == null) {
				System.out.println("imageEncoder is null");
				System.exit(0);
			}
			encoder.encode((RenderedImage) img);
		}

	}

	/**
	 * @return Returns the ext.
	 */
	public String getFileExtension() {
		// TODO Auto-generated method stub
		return _ext;
	}

	/**
	 * @param ext
	 *            The ext to set.
	 */
	public void setFileExtension(String ext) {
		_ext = ext;
	}

}
