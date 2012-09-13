package org.gvsig.crs.test;


import java.util.Enumeration;
import java.util.Hashtable;

import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;


/*clase cliente para el servicio web CSCat*/

public class CSCatClient {
	private String endpoint = null;
                        
private int nivel;
/* dependiendo del nivel del sistema de referencia se accedera
                                     al catÃ¡logo con parametros diferentes*/             
                        
            public CSCatClient(int nivel){
                                    
             this.nivel=nivel;
                                    

            this.endpoint = "http://mapas.topografia.upm.es:9090/swccat/ccat.jws";

             /*Se podria definir un fichero properties para guardar la url con la ruta del servicio
             y utilizar el endpoint(url) para el mÃ©todo setTargetEndpointAddress de la clase Call
             en este caso se ha omitido la utilizaciÃ³n del fichero y directamente se utilizarÃ¡ la URL pero serÃ­a:
                                    
                        Properties prop=new Properties();
                        try{                                                                                                       
                             FileInputStream in=new FileInputStream("C:\\GimmConfig\\rutaCSCat.properties");
                                                prop.load(in);
                                                // guardamos en atributo de la clase la url guardada en el fichero properties
                                                this.endpoint=prop.getProperty("url");
                                     } catch (Exception e){
                                                 System.out.println("Error al acceder al fichero "+e.getMessage());
                                     }*/
                        }
                        
                        
                        //public String[] csCatAccess(String  proj, Hashtable<String, String> tPROJ4){
            			public String[] csCatAccess(String  proj, Hashtable tPROJ4){
                        /*mÃ©todo para acceder al catÃ¡logo*/       
                                    
                                    try{
                                                 Service service = new Service();
                                                Call call = (Call) service.createCall();
                                                 call.setTargetEndpointAddress( new java.net.URL("http://mapas.topografia.upm.es:9090/swccat/ccat.jws"));
                                        
                                         /*el nombre del metodo a invocar y sus correspondientes parametros se obtienen del archivo WSDL (ver respuesta del servicio WSDL)*/

                                         call.setOperationName("GetCRS");
                                        
                                         // Se establecem tanto los parÃ¡metros que necesita el mÃ©todo y sus correspondientes tipos  como el tipo que devuelve segun el WSDL//
                                      
                        call.addParameter( "InputFormatId", XMLType.XSD_STRING, ParameterMode.IN );
                       call.addParameter( "OutputFormatId", XMLType.XSD_STRING, ParameterMode.IN );
                        call.addParameter( "SourceCRS", XMLType.XSD_STRING, ParameterMode.IN );
                        call.addParameter( "SourceProjection", XMLType.XSD_STRING, ParameterMode.IN );
                        call.addParameter( "SourceDatum", XMLType.XSD_STRING, ParameterMode.IN );
                        call.addParameter( "SourceEllipsoid", XMLType.XSD_STRING, ParameterMode.IN );
                        call.addParameter( "SourceUnit", XMLType.XSD_STRING, ParameterMode.IN );
                                                  
                        call.setReturnType( XMLType.XSD_ANYTYPE);
                                          
                                           Object ret;

                        // En el ejemplo se estÃ¡ pidiendo transformar del espacio de nombres PROJ4 a EPSG lo que tenga el parÃ¡metro proj. Es importante destacar que la respuesta es siempre una matriz de String, aunque se espere una sola respuesta.

                                           if (nivel == 1){
                                        	   	  //ret = (Object) call.invoke ( new Object [] {"IAU2000","PROJ4",proj,"","","",""});
                                        	   	  //ret = (Object) call.invoke ( new Object [] {"EPSG","ESRI",proj,"","","",""});
                                                  ret = (Object) call.invoke ( new Object [] {"EPSG","PROJ4",proj,"","","",""});
                                                  //ret = (Object) call.invoke ( new Object [] {"PROJ4","EPSG",proj,"","","",""});
                                                  String[] resul=(String[])ret;
                                                              //System.out.println(resul[0]);  aunque se devuelva un unico valor se almacena en un array
                                                              return resul;
                                                             
                                           } else{  // nivel 2
                                                  String[] parameters = new String[4];
                                                  getParameters(tPROJ4, parameters); // metodo para obtener los valores de los parametros para la llamada a invoke segun la estructura de la aplicaciÃ³n
                                                 
                                                  /*for (int i = 0; i < parameters.length; i++) {
                                                              System.out.println(parameters[i]) ;
                                                              }*/
                                             
                                                              // en este caso (nivel 2)  la llamada al metodo se debe realizar dando como argumentos la proyeccion, el datum y ellipsoide
                                                  ret = (Object) call.invoke ( new Object [] {"PROJ4","EPSG","",parameters[0],parameters[1],parameters[2],parameters[3]});
                                                  String[] resul=(String[])ret;
                                                 
                                                  /*for (int i = 0; i < resul.length; i++) {
                                                              System.out.println(resul[i]);
                                                              } */
                                                 
                                                  return resul;
                                           }
                                        
                                                }catch (Exception e){
                                                            System.out.println("Fallo al consultar el catÃ¡logo CsCat");
                                                            System.out.println(e.getMessage());
                                                            return null;
                                                
                                                }
                        }


                        //private void getParameters(Hashtable<String, String> tproj4, String[] parameters) {
            			private void getParameters(Hashtable tproj4, String[] parameters) {
                        // tproj4: tabla Hash que almacena los valores del PROJ4 obtenido para el datum, la proyecciÃ³n, elipsoide y unidades          
                        
                                    for (int i = 0; i < parameters.length; i++) {
                                                parameters[i]="";
                                    }
                                    
                                    Enumeration keysProj = tproj4.keys();
                                    
                                    while (keysProj.hasMoreElements()) {
                                                String key = (String) keysProj.nextElement();
                                                if (key.indexOf("proj")>0)
                                                            parameters[0] = (String) tproj4.get(key);
                                                if (key.indexOf("datum")>0)
                                                            parameters[1] = (String) tproj4.get(key);
                                                if (key.indexOf("ellps")>0)
                                                            parameters[2] = (String) tproj4.get(key);
                                                if (key.indexOf("units")>0)
                                                            parameters[3] = (String) tproj4.get(key);
                                                
                                    }           
                                    
                        }

                        

}