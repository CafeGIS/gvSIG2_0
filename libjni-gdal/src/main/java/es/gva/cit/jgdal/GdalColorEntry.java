package es.gva.cit.jgdal;

/**
 * Clase que representa una entrada de la tabla de color.
 * @author Nacho Brodin (brodin_ign@gva.es)
 *
 */
public class GdalColorEntry{
	 /*! gray, red, cyan or hue */
    public short      c1;      

    /*! green, magenta, or lightness */    
    public short      c2;      

    /*! blue, yellow, or saturation */
    public short      c3;      

    /*! alpha or blackband */
    public short      c4;      
}