package org.gvsig.raster.grid.filter.convolution;

import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.IBuffer;

/**
 * Filtro de convolucion para Buffer de tipo Int
 * @author Alejandro Muñoz        <alejandro.munoz@uclm.es>
 * @author Diego Guerrero Sevilla  <diego.guerrero@uclm.es>
 * */

public class ConvolutionIntegerFilter extends ConvolutionFilter {

	public ConvolutionIntegerFilter(){
		super();
	}
	/**
	 * @param Kernel a aplicar. En caso de que no se trate de un kernel definido en ConvolutionFilter, se puede pasar como
	 * parametro el kernel se pretende aplicar.
	 * **/
	public ConvolutionIntegerFilter(Kernel k){
		super();
		super.kernel=k;
	}

	public void pre(){
		super.pre();
		rasterResult = RasterBuffer.getBuffer(IBuffer.TYPE_INT, raster.getWidth(), raster.getHeight(), raster.getBandCount(), true);
	}

	/** Aplicacion del filtro para el pixel de la posicion line, col */
	public void process(int col, int line) {
		int lado = kernel.getLado();
		int semiLado = (lado - 1) >> 1;
		double ventana[][] = new double[lado][lado];
		double resultConvolution = 0;
		for (int band = 0; band < raster.getBandCount(); band++) {
			if ((col - semiLado >= 0) && (line - semiLado >= 0) && (col + semiLado < width) && (line + semiLado < height)) {
				for (int j = -semiLado; j <= semiLado; j++)
					for (int i = -semiLado; i <= semiLado; i++)
						ventana[i + semiLado][j + semiLado] = raster.getElemInt(line + j, col + i, band) & 0xff;
				Kernel Kventana = new Kernel(ventana);
				resultConvolution = kernel.convolution(Kventana);
				if (resultConvolution > Integer.MAX_VALUE)
					resultConvolution = Integer.MAX_VALUE;
				else
					if (resultConvolution < 0)
						resultConvolution = 0;
				rasterResult.setElem(line, col, band, (int) resultConvolution);
			} else
				rasterResult.setElem(line, col, band, raster.getElemInt(line, col, band));
		}
	}

	/**
	 * @return  tipo de dato del buffer de entrada
	 * */
	public int getInRasterDataType() {
		return RasterBuffer.TYPE_INT;
	}

	/**
	 * @return  tipo de dato del buffer de salida
	 * */
	public int getOutRasterDataType() {
		return RasterBuffer.TYPE_INT;
	}


	/**
	 * @return  buffer resultante tras aplicar el filtro
	 * */
	public Object getResult(String name) {
		if (name.equals("raster"))
			return this.rasterResult;
		return null;
	}

}
