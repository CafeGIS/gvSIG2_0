package org.gvsig.gvsig3d.simbology3D.symbol3D.point;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.gvsig.gvsig3d.gui.FeatureFactory;
import org.gvsig.gvsig3d.simbology3D.symbol3D.Abstract3DSymbol;
import org.gvsig.osgvp.core.osg.Node;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.core.osg.Vec4;
import org.gvsig.symbology.fmap.symbols.CharacterMarkerSymbol;

import com.iver.cit.gvsig.fmap.core.SymbologyFactory;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;
import com.iver.cit.gvsig.fmap.core.symbols.SymbolDrawingException;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.PNGEncodeParam;
import com.sun.media.jai.codec.TIFFEncodeParam;

public class CharacterPoint3DSymbol extends Abstract3DSymbol{

	private CharacterMarkerSymbol characterMarketSymbol;
	private String imagePath = null;
	

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public CharacterPoint3DSymbol(ISymbol symbol) {
		super(symbol);
		characterMarketSymbol =  (CharacterMarkerSymbol) symbol;
		this.setImagePath(rasterizeSymbol(characterMarketSymbol));
		
	}

	@Override
	public Node generateSymbol(List<Vec3> position) {
		return FeatureFactory.generateQuadPoligon(position.get(0),new Vec4(1.0,1.0,1.0,1.0),this.getImagePath(),(float)characterMarketSymbol.getSize()*10);
	}
	
	
	private String rasterizeSymbol(CharacterMarkerSymbol sym) {

		BufferedImage image;
		// Setting the size symbol
		double size = sym.getSize();
		// Generating the image
		image = new BufferedImage((int) size, (int) size,
				BufferedImage.TYPE_INT_ARGB);
		// Getting graphics
		Graphics2D graphics = image.createGraphics();
		// Transparent background
		Color backgColor = new Color(0, 0,0, 0);
		graphics.setColor(backgColor);
		// Setting the fill rectangle
		graphics.fillRect(0, 0, (int) size, (int) size);
		// Drawing

		AffineTransform at =new AffineTransform();
		at.scale(10, 10);
		
		try {
			sym.drawInsideRectangle(graphics,at ,new Rectangle((int)size,(int)size));
		} catch (SymbolDrawingException e1) {
			try {
				SymbologyFactory.getWarningSymbol(
						SymbolDrawingException.STR_UNSUPPORTED_SET_OF_SETTINGS,
						"desc",
						SymbolDrawingException.SHAPETYPE_MISMATCH).
						drawInsideRectangle(graphics,at ,new Rectangle((int)size,(int)size));
			} catch (SymbolDrawingException e) { /* impossible */ }
		}
		
		String sip = "SymbolTmp.png";
		File file = new File(sip);
		String format = "png";
		try {
			saveCachedFile(image, format, file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sip;
	}
	
	public void saveCachedFile(Image img, String format, File f)
			throws IOException {
		// ImageIO.write((RenderedImage) img, format, f);
		// File file = new File(fName);
		FileOutputStream out = new FileOutputStream(f);
		if (f.exists()){
			f.delete();
			f.createNewFile();
		}
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
		out.close();
	}
}
