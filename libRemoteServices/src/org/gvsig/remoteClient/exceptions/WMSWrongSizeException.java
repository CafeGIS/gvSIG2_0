/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package org.gvsig.remoteClient.exceptions;

/**
 * La petición al servidor era de un tamaño equivocado.
 * Reconoce execepciones de algunos tipos:<br>
 * <li>El server http://wms.jpl.nasa.gov/wms.cgi da estos:
 * <pre>
    <?xml version='1.0' encoding="UTF-8" standalone="no" ?>
	<!DOCTYPE ServiceExceptionReport SYSTEM "http://www.digitalearth.gov/wmt/xml/exception_1_1_0.dtd ">
	<ServiceExceptionReport version="1.1.0">
	  <ServiceException>
	    Requested image is too wide, max allowed width is 4096
	  </ServiceException>
	</ServiceExceptionReport>
	
	<?xml version='1.0' encoding="UTF-8" standalone="no" ?>
	<!DOCTYPE ServiceExceptionReport SYSTEM "http://www.digitalearth.gov/wmt/xml/exception_1_1_0.dtd ">
	<ServiceExceptionReport version="1.1.0">
	  <ServiceException>
	    Requested image is too tall, max allowed height is 4096
	  </ServiceException>
	</ServiceExceptionReport>
 * </pre>
 * <li>El server http://www.idee.es/wms/IDEE-Base/IDEE-Base da
 * <pre>
 	<ERROR Operation="GetMap Request" status="ERROR" source="Web Map Server" description="El servidor no ha podido realizar la operacion">
		<ERROR CODE="El tamano en pixels pedido no es valido."/>
		<ERROR CODE="Su valor debe ser mayor que 0 y menor que el limite de descarga: anchura = 1500, altura = 1500"/>
	</ERROR>
 * </pre>
 * <li>El server http://ovc.catastro.meh.es/Cartografia/WMS/ServidorWMS.aspx da
 * <pre>
 	<ServiceExceptionReport version="1.1.1">
-
	<ServiceException code="InvalidFormat">

Parámetros erroneos:
prefijo = 
mapa =  0
formato = IMAGE/JPEG
XMin =  1.1578804698593
YMin =  53.5852110737936
XMax =  10.3
YMax =  53.8000038968219
AnchoPixels =  64
AltoPixels =  5023
Transparente = TRUE
Descripción error:
AltoPixels > 2000
</ServiceException>
</ServiceExceptionReport>
 * </pre>
 * 
 * <pre>
 	<?xml version='1.0' encoding="ISO-8859-1" standalone="no" ?>
<!DOCTYPE ServiceExceptionReport SYSTEM "http://schemas.opengeospatial.net/wms/1.1.1/exception_1_1_1.dtd">
<ServiceExceptionReport version="1.1.1">
<ServiceException>
msWMSLoadGetMapParams(): WMS server error. Image size out of range, WIDTH and HEIGHT must be between 1 and 2048 pixels.
</ServiceException>
</ServiceExceptionReport>

 * </pre>
 * 
 */
public class WMSWrongSizeException extends WMSException 
{	
	private int height = -1;
	private int width = -1;
	
	/**
	 *
	 */
	public WMSWrongSizeException() {
		super();
	}

	/**
	 * Crea WMSException.
	 *
	 * @param message
	 */
	public WMSWrongSizeException(String message) {
		super(message);
	}

	/**
	 * Crea WMSException.
	 *
	 * @param message
	 * @param cause
	 */
	public WMSWrongSizeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	  * Crea WMSException.
	 *
	 * @param cause
	 */
	public WMSWrongSizeException(Throwable cause) {
		super(cause);
	}
	
	public int getWidth()
	{
		return width; 
	}
	public int getHeight()
	{
		return height;
	}
	public void setWidth(int w)
	{
		width =w;
	}
	public void setHeight(int h)
	{
		height =h;
	}
	
	/**
	 * Checks if the argument is a WrongSizeError message, in this
	 * case throws a WMSWrongSizeException
	 * @param errorMsg El mensaje de error que pasa el server.
	 * @throws WMSException
	 */
	public static void check(String errorMsg) throws WMSException 
	{
		//TODO:
		//check the errorMsg to see if it matches with one of the 
		// well known string error messages.
	}
}
