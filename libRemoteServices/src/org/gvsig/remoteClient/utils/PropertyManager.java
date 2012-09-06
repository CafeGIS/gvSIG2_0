package org.gvsig.remoteClient.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

/**
 * Description: Loads the configuration files
 *
 * @author  Laura Diaz
 * @version 1.0 
 */
public class PropertyManager
{
 public final static String LOGGER_PROPERTIES = "org/gvsig/remoteClient/conf/logger.properties";
 
 //The property file names to load
 private final static String[] s_propertyFileNames = new String[]{LOGGER_PROPERTIES};
 																	

 //Hashtable containing all properties objects that are loaded
 private static Hashtable s_propertyFiles = null;

 /**
 * Gets a properties object
 * If the the property files are not yet loaded, then loads them first
 *
 * @param propertyFileName, String
 * @return Properties
 * @throws java.io.IOException
 */
 public static Properties getProperties(String propertyFileName) throws IOException
 {
   if (s_propertyFiles == null)
   {
     loadProperties();
   }

   return (Properties)s_propertyFiles.get(propertyFileName);
 }

 //Loads the property files
 private static synchronized void loadProperties() throws IOException
 {
   s_propertyFiles = new Hashtable(s_propertyFileNames.length);
   ClassLoader loader = PropertyManager.class.getClassLoader();

   for (int i = 0; i < s_propertyFileNames.length; i++)
   {
     try
     {
       InputStream input = loader.getResourceAsStream(s_propertyFileNames[i]);
       Properties props = new Properties();
       props.load(input);
       s_propertyFiles.put(s_propertyFileNames[i], props);
     }
     catch(Exception e)
     {
     	System.err.println("\n[PropertyManager] ERROR - Failed to read properties file \""
                          + s_propertyFileNames[i] + "\": "
                          + e.getMessage());       
     }
   }
 }
 
 //Loads the property files
 /*
 public static synchronized void saveProperties(String propsName) throws IOException
 {  
     try
     {	 
	  //ClassLoader loader = PropertyManager.class.getClassLoader();    	  
	  //InputStream input = loader.getResourceAsStream(propsName);    	      	
	   FileOutputStream output = new FileOutputStream(propsName);
	   Properties props = new Properties();
	   props.store(output, propsName);
	   output.close();            
     }
     catch(Exception e)
     {
     	System.err.println("\n[PropertyManager] ERROR - Failed to save properties file \""
                          + propsName + "\": "
                          + e.getMessage());       
     }
 }
*/
 
 
}