package org.gvsig.raster.grid.filter.pansharp;

import java.awt.Dimension;
import java.util.ArrayList;

import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.hierarchy.IRasterDataset;
import org.gvsig.raster.util.RasterUtilities;

public class PanSharpeningByteFilter extends  PanSharpeningFilter {
	
	/** Filtro de pansharpening aplicado a un buffer Multiespectral y Pancromatico de tipo byte
	 * El buffer resultante es de tipo byte*/	
	public PanSharpeningByteFilter (){
		super();	
	}

	/**
	 *  Se toman los buffers multiespectral y pancromatico para la aplicacion del filtro.
	 */
	public void init (){
	
		loadParam();
		checkInput();  
		//colorConversion= new ColorConversion();

		try {
			// Tomar el Buffer Multiespectral con las bandas activas
			Object[] bands= null;
			bands= bandOrder.toArray();
			int bandas[]= new int [bands.length];
			for (int i=0;i<bands.length;i++)
				bandas[i]= ((Integer) bands[i]).intValue();

			BufferFactory bfFactoryMultiespc = new BufferFactory(dataset.getDataSource());
			Extent adjustedExtent = RasterUtilities.calculateAdjustedView(dataset.getDataSource().getExtent(),dataset.getDataSource().getAffineTransform(0), new Dimension(widthMultiespec,heightMultiespec));
			bfFactoryMultiespc.setDrawableBands(bandas);
			try {
				bfFactoryMultiespc.setAreaOfInterest(adjustedExtent.getULX(),adjustedExtent.getULY(),adjustedExtent.getLRX(), adjustedExtent.getLRY(),widthMultiespec,heightMultiespec);
			} catch (RasterDriverException e) {
				e.printStackTrace();
			} catch (InvalidSetViewException e) {
				e.printStackTrace();
			}
			bufferMultiespectral =bfFactoryMultiespc.getRasterBuf() ;


			// Tomar buffer Pancromatica
			BufferFactory bfFactoryPancr = new BufferFactory(dataset.getDataSource());
			Extent adjustedExtentP = RasterUtilities.calculateAdjustedView(dataset.getDataSource().getDataset(pancrName).getExtent(), dataset.getDataSource().getDataset(pancrName).getAffineTransform(), new Dimension(widthPancr,heightPancr));
			bfFactoryPancr.setDrawableBands(new int[]{posPancromatica});
			try {
				bfFactoryPancr.setAreaOfInterest(adjustedExtentP.getULX(),adjustedExtentP.getULY(),adjustedExtentP.getLRX(), adjustedExtentP.getLRY(),widthPancr,heightPancr);
			} catch (RasterDriverException e) {
				e.printStackTrace();
			} catch (InvalidSetViewException e) {
				e.printStackTrace();
			}
			bufferPancr = bfFactoryPancr.getRasterBuf();


			// Buffer donde se pinta el resultado
			rasterResult= RasterBuffer.getBuffer(RasterBuffer.TYPE_BYTE, widthPancr, heightPancr, 3, true);
		} catch (InterruptedException e) {
			//Gestionar cancelación de la lectura
		}
	}		

	/**
	 *  Validamos que haya alguna otra banda además de la pancromática y que la pancromática 
	 *  sea de mayor resolución que las demás.
	 */
	private void checkInput(){
		
		exec = true;
		if(heightMultiespec > heightPancr || widthMultiespec > widthPancr || heightMultiespec == 0 || widthMultiespec == 0)
    	exec = false;
		String ficheros[]= dataset.getFileName();
		for(int i=0;i<ficheros.length;i++){
			if(ficheros[i] != pancrName){
				if(dataset.getDataSource().getDataset(ficheros[i]).getHeight() != heightMultiespec || dataset.getDataSource().getDataset(ficheros[i]).getWidth()!= widthMultiespec){ 
					exec = false;
					break;
				}   		
			}
		}
	}

	/**
	 *  Se recogen los parametros necesarios para la aplicacion del filtro. 
	 *  posPancromatica: posicion de la banda pancromatica 
	 *  dataset:  
	 *  bandOrden: 			array con las bandas que se visualizan
	 *  alpha: 				parametro alpha 
	 *  coef: 				parametro coef
	 *  coefBrovey: 		parametro  co
	 *  filenameOutput:		ruta con el nombre del ficheo de salida(fichero temporal)
	 *  heightPancr: 		height de la imagen Pancromatica
	 *  widthPancr :		width de la imagen Pancromatica
	 *  widthMultiespec: 	width de la imagen multiespectral
	 *  heightMultiespec:	heigth de la imagen multiespectral
	 *  
	 */
	private void loadParam(){
	
		posPancromatica= ((Integer) params.get("posPancromatica")).intValue();
		dataset=  (IRasterDataset)params.get("dataset");
		bandOrder = (ArrayList) params.get("order");
		alpha = ((Integer) params.get("alpha")).intValue();
		method = (String) params.get("method");
		coef = ((Double) params.get("coef")).doubleValue();
		coefBrovey = ((Integer) params.get("coefBrovey")).intValue();
		fileNameOutput= (String)params.get("fileNameOutput");
		
		// Name de pancromatica
		pancrName=dataset.getDataSource().getBands().getBand(posPancromatica).getFileName();
		params.put("pancName", pancrName);
		heightPancr = dataset.getDataSource().getDataset(pancrName).getHeight();
		widthPancr = dataset.getDataSource().getDataset(pancrName).getWidth();
		
		// Nombre de los ficheros que componen el dataser
		String[] ficherosNames= dataset.getFileName(); 
		for(int i=0;i<ficherosNames.length;i++){
			if(ficherosNames[i] != pancrName){
				heightMultiespec = dataset.getDataSource().getDataset(ficherosNames[i]).getHeight(); 
				widthMultiespec = dataset.getDataSource().getDataset(ficherosNames[i]).getWidth();
			}
		}

		if (widthMultiespec!=0 && heightMultiespec!=0){
			relX = (int)widthPancr/widthMultiespec;
			relY = (int)heightPancr/heightMultiespec;
		}
	}


	/**Ejecucion del filtro*/
	public void execute() {
	
		pre();
		init();
		if(exec){	
			byte [][] pRGBMultiespectral= null;
			//Para cada linea leemos los valores  del buffer multiespectral. Aplicamos el algoritmo
			// El resultado se almacena en rasterResult
			if(method.equals("hsl")){
				for(int iLine=0;iLine<heightMultiespec ;iLine++){
					pRGBMultiespectral = bufferMultiespectral.getLineByte(iLine);
					processIHS(pRGBMultiespectral, iLine);	    		
				}
			}else{
				// Para cada linea leemos los valores  del buffer multiespectral. Aplicamos el algoritmo
				// El resultado se almacena en rasterResult
				for(int iLine=0;iLine<heightMultiespec ;iLine++){
					pRGBMultiespectral = bufferMultiespectral.getLineByte(iLine);
					processBrovey(pRGBMultiespectral, iLine);	    		
				}
			}
		}	
		post();
	}
	
	/**
	 * @return  tipo de dato del buffer de entrada
	 * */

	public int getInRasterDataType() {
		return RasterBuffer.TYPE_BYTE;
	}


	/**
	 * @return tipo de dato del buffer de salida
	 * */
	public int getOutRasterDataType() {
		return RasterBuffer.TYPE_BYTE;
	}

	
	/**
	 * @return  buffer resultante tras aplicar el filtro
	 * */
	public Object getResult(String name) {
		if (name.equals("raster"))
			return (IBuffer)raster;
		return null;
	}



	/**
	 * Aplica la operación de refinamiento sobre el buffer Multiespectral que contiene 
	 * el RGB que es pasado por parámetro utilizando la pancromática.
	 * @param bufferInput	Buffer rgb 
	 * @param length		longitud del buffer de la pancromatica utilizado
	 * @param iLine			línea leida de la imagen multiespectral
	 * @return				
	 */
	private void processBrovey(byte[][] bufferInput, int iLine){    	
	
		int r=0,g=0,b=0;
		double r1=0, g1=0, b1=0;
		double scale=0;
		int valorPanc=0;
		int line=0, col=0;
		// Para cada elemento Punto de la multiespectral
		for(int iElem=0; iElem<bufferInput[0].length; iElem++){
			r = (int)(bufferInput[0][iElem]& 0xff);
			g = (int) (bufferInput[1][iElem]&0xff);
			b = (int)(bufferInput[2][iElem]& 0xff);
			for(int j=0;j<relX;j++){
				for(int k=0;k<relY;k++){	
					line=iLine*relX+j;
					col= iElem*relY+k;
					valorPanc = (bufferPancr.getElemByte(line,col,0)&0xff);
					scale = (3*(valorPanc+coefBrovey))/(r+g+b+1.0);		
					r1 = r*scale;
					g1 = g*scale; 
					b1 = b*scale;
					rasterResult.setElem(line, col, 0, (byte)r1);
					rasterResult.setElem(line, col, 1, (byte)g1);
					rasterResult.setElem(line, col, 2, (byte)b1);
				}
			}
		}
	
	}

	/**
	 * Aplica la operación de refinamiento sobre el buffer Multiespectral que contiene 
	 * el RGB que es pasado por parámetro utilizando la pancromática.
	 * @param bufferInput	Buffer rgb 
	 * @param length		longitud del buffer de la pancromatica utilizado
	 * @param iLine			línea leida de la imagen multiespectral
	 * @return				buffer con el resultado de la operación
	 */
	private void processIHS(byte[][] bufferInput, int iLine){    	
	
		int[] uvw , tmp = new int[3];
		int line=0,col=0;
	    double[] xyz;
		
	    for(int iElem=0; iElem<bufferInput[0].length; iElem++){
	    	xyz = colorConversion.RGBtoHSL((int)(bufferInput[0][iElem]& 0xff),(int)(bufferInput[1][iElem]& 0xff),(int)(bufferInput[2][iElem]& 0xff));
			for(int j=0;j<relX;j++){
				for(int k=0;k<relY;k++){
					line= iLine*relX+j;
					col= iElem*relY+k;
					xyz[2] = (((bufferPancr.getElemByte(line,col,0)& 0xff))/255.0) + coef;
					tmp[0] = (int)(255.0 * xyz[0] / 360.0 + 0.5);
					tmp[2] = (int) (xyz[2]*255. + 0.5);
					tmp[1] = (int) (xyz[1]*255. + 0.5);
					uvw = colorConversion.HSLtoRGB(tmp[0],
								   tmp[1],
								   tmp[2]);
					
					rasterResult.setElem(line, col, 0, (byte)uvw[0]);
					rasterResult.setElem(line, col, 1, (byte)uvw[1]);
					rasterResult.setElem(line, col, 2, (byte)uvw[2]);
				
				}
			}
		} // Fin del prodeso para la linea iLine
	}

} // Fin de la clase