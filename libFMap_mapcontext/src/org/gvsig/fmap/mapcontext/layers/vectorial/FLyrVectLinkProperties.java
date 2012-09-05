package org.gvsig.fmap.mapcontext.layers.vectorial;

import java.awt.geom.Point2D;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontext.layers.AbstractLinkProperties;
import org.gvsig.fmap.mapcontext.layers.FLayer;


/**
 * This class extends AbstractLinkProperties and implements the method to get an array of URI
 * using a point2D and a tolerance. This class extends AstractLinkProperties to add HyperLink
 * to Vectorial Layer(FLyrVect)
 */

public class FLyrVectLinkProperties extends AbstractLinkProperties{

    /**
     * Default Constructor. Costructs a LinkProperties with the necessary information
     *
     */
    public FLyrVectLinkProperties(){
        setType(0);
        setField(null);
        setExt(null);
    }

    /**
     * Constructor. Constructs a LinkProperties with the information that receives
     * @param tipo
     * @param fieldName
     * @param extension
     */
    public FLyrVectLinkProperties(int tipo, String fieldName, String extension){
        setType(tipo);
        setField(fieldName);
        setExt(extension);
    }

    /**
     * Creates an array of URI. With the point and the tolerance makes a query to the layer
     * and gets the geometries that contains the point with a certain error (tolerance).
     * For each one of this geometries creates one URI and adds it to the array
     * @param layer
     * @param point
     * @param tolerance
     * @return Array of URI
     * @throws ReadException
     */
    public URI[] getLink(FLayer layer, Point2D point, double tolerance) throws ReadException{
    	//Sacado de la clase LinkListener
		FLyrVect lyrVect = (FLyrVect) layer;
		FeatureStore fStore = null;
		FeatureSet fSet=null;

    	try {
    		fStore = lyrVect.getFeatureStore();
    		URI uri[]=null;

    		//Construimos el BitSet (Véctor con componentes BOOLEAN) con la consulta que
    		try {
    			fSet = lyrVect.queryByPoint(point, tolerance, fStore.getDefaultFeatureType());//fStore.getFeatureSet(featureQuery);
    		} catch (ReadException e) {
    			return null;
    		} catch (DataException e) {
    			return null;
			}

    		DisposableIterator iter = null;
    		//Si el bitset creado no está vacío creamos el vector de URLS correspondientes
    		//a la consulta que hemos hecho.

    		ArrayList tmpUris=new ArrayList();
    		String auxext="";
    		int idField = fStore.getDefaultFeatureType().getIndex(this.getField());




    		//Consigo el identificador del campo pasandole como parámetro el
    		//nombre del campo del énlace
    		if (idField == -1){
        		throw new ReadException(lyrVect.getName()+": '"+this.getField()+"' not found",new Exception());
    		}
    		//Recorremos el BitSet siguiendo el ejmplo de la clase que se
    		//proporciona en la API
    		iter = fSet.iterator();
    		while (iter.hasNext()){
    			//TODO
    			//Sacado de la clase LinkPanel, decidir como pintar la URL le
    			//corresponde a LinkPanel, aquí solo creamos el vector de URL´s
    			if (!super.getExt().equals("")){
    				auxext="."+this.getExt();
    			}
//  			ds.start();
    			//Creamos el fichero con el nombre del campo y la extensión.
    			Feature feature = (Feature) iter.next();
    			String auxField=feature.getString(idField);
    			URI tmpUri = null;
    			if(auxField.startsWith("http:/")){
    				tmpUri= new URI(auxField);
    			}else{
    				File file =new File(feature.getString(idField)+auxext);
    				tmpUri = file.toURI();
    			}
    			tmpUris.add(tmpUri);
    			System.out.println(tmpUri.toString());
    			//System.out.println(uri[j]);


    		}
    		iter.dispose();

    		if (tmpUris.size()==0){
    			return null;
    		}

    		// Creo el vector de URL´s con la misma longitud que el bitset

    		return (URI[]) tmpUris.toArray(new URI[0]);

    	} catch (ReadException e) {
    		throw new ReadException(lyrVect.getName(),e);
    	} catch (URISyntaxException e) {
    		throw new ReadException(lyrVect.getName(),e);
		} catch (DataException e) {
			throw new ReadException(lyrVect.getName(),e);
		}finally{
			fSet.dispose();
		}

    }

	public String getClassName() {
		// TODO Auto-generated method stub
		return null;
	}


}
