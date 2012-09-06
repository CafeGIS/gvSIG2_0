package org.gvsig.raster.grid.filter.pansharp;

import java.io.IOException;
import java.util.ArrayList;

import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.hierarchy.IRasterDataset;
import org.gvsig.raster.util.ColorConversion;



/**
 * Filtros de  Pansharpening
 * */

public class PanSharpeningFilter  extends RasterFilter {

	public static String[] 				names 					= new String[] {"pansharp"};
	protected IRasterDataset			dataset 				= null;
	protected int 						heightPancr 			= 0;
	protected int 						widthPancr 				= 0;
	protected int 						heightMultiespec 		= 0;
	protected int 						widthMultiespec 		= 0;
	protected int						nBands 					= 0;
	protected int						relX 					= 0;
	protected int						relY 					= 0;
	protected int						posPancromatica 		= 0;	
	public ArrayList					bandOrder 				= null;
	public int							alpha 					= 0;
	public String						method 					= "";
	public String 						fileNameOutput			= "";
	public double						coef 					= 0.0;
	public int							coefBrovey 				= 0;
	protected String					pancrName				= null;
	protected IBuffer 					bufferMultiespectral	= null;
	protected IBuffer 					bufferPancr				= null;
	protected ColorConversion           colorConversion         = null;	 
		
		public PanSharpeningFilter() {
			super();
		}

	public String getGroup() {
		return "espectrales";
	}

	public int getInRasterDataType() {
		return 0;
	}

	public String[] getNames() {
		return names;			
	}

	public int getOutRasterDataType() {
		return 0;
	}


	public Params getUIParams(String nameFilter) {
		Params params = new Params();
		return params;
	}

	
	 public boolean isVisible() {
			return false;
		 }
	
	public void post() {	
		// Escritura del raster a disco en fichero temporal
		GeoRasterWriter grw = null;
		WriterBufferServer writerBufferServer = new WriterBufferServer(rasterResult);
	
		try {
			grw = GeoRasterWriter.getWriter(writerBufferServer, fileNameOutput, rasterResult.getBandCount(),dataset.getDataSource().getDataset(pancrName).getAffineTransform(), rasterResult.getWidth(), rasterResult.getHeight(), rasterResult.getDataType(), GeoRasterWriter.getWriter(fileNameOutput).getParams(), dataset.getDataSource().getDataset(pancrName).getProjection());
			grw.setWkt(dataset.getDataSource().getWktProjection());
			grw.dataWrite();
		
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			//TODO: Gestionar cancelación de escritura
		}
		
		grw.writeClose();
			rasterResult.free();
	}

	public void pre() {	
		if(colorConversion == null)
			colorConversion = new ColorConversion();
	}

	public void process(int x, int y) {	
	}

	public Object getResult(String name) {
		return null;
	}
	
}
