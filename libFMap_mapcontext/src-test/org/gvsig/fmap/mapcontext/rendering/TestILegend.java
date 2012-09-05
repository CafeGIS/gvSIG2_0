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
package org.gvsig.fmap.mapcontext.rendering;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.gvsig.fmap.mapcontext.rendering.legend.IClassifiedVectorLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.ILegend;
import org.gvsig.fmap.mapcontext.rendering.legend.IVectorLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.IVectorialIntervalLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.IVectorialUniqueValueLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.SingleSymbolLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.VectorialIntervalLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.VectorialUniqueValueLegend;
import org.gvsig.fmap.mapcontext.rendering.symbols.SimpleFillSymbol;

import com.iver.utiles.XMLException;

import junit.framework.TestCase;

/**
 * Integration test to ensure that the legends follow the rules that follow the
 * managing of them by the application.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class TestILegend extends TestCase {
	 private static ArrayList classesToTest;
	 transient private ILegend[] legends;

	 private static ArrayList getClassesToTest() {
	        if (classesToTest == null) {
	            classesToTest = new ArrayList();

	          TestILegend.addLegendToTest(VectorialIntervalLegend.class);
	          TestILegend.addLegendToTest(VectorialUniqueValueLegend.class);
	          TestILegend.addLegendToTest(SingleSymbolLegend.class);

//	          TestILegend.addLegendToTest(QuantityByCategoryLegend.class);
//	          TestILegend.addLegendToTest(DotDensityLegend.class);
//	          TestILegend.addLegendToTest(ProportionalSymbolsLegend.class);
//	          TestILegend.addLegendToTest(GraduatedSymbolLegend.class);


	        }

	        return classesToTest;
	    }

	 /**
	  * This method fills the attributes of a legend.The purpose is to ensure that the new
	  * legend is 'full' and ready to be used for a test.
	  * @param le
	  * @return
	  */

	 private static ILegend fillthelegend(ILegend le) {

		 SimpleFillSymbol mfs = new SimpleFillSymbol();
		 mfs.setFillColor(Color.ORANGE.darker());
		 mfs.setDescription("hola");
		 mfs.setIsShapeVisible(true);
		 String[] cad = new String[] {"Pepe","Juan"};

		 if(le instanceof IVectorLegend) {
			 IVectorLegend ssl=(IVectorLegend)le;
			 ssl.setDefaultSymbol(mfs);
		 }

		 if(le instanceof IVectorialUniqueValueLegend) {
			 IVectorialUniqueValueLegend ivuvl= (IVectorialUniqueValueLegend)le;
			 ivuvl.setClassifyingFieldNames(cad);
			 }

		 if(le instanceof IVectorialIntervalLegend) {
			 IVectorialIntervalLegend ivil=(IVectorialIntervalLegend)le;
			 ivil.setClassifyingFieldNames(cad);
			 }

		 if(le instanceof IClassifiedVectorLegend) {
			 IClassifiedVectorLegend icvl=(IClassifiedVectorLegend)le;
			 icvl.setClassifyingFieldNames(cad);
		 }



		 return le;
	 }


	 public static void addLegendToTest(Class legendClass) {
	        try {
	            ILegend len = (ILegend) legendClass.newInstance();
	            fillthelegend(len);
	            len.getXMLEntity();

	        } catch (InstantiationException e) {
	            // TODO Auto-generated catch block
	            fail("Instantiating class, cannot test a non-instantiable symbol");
	        } catch (IllegalAccessException e) {
	            // TODO Auto-generated catch block
	            fail("Class not instantiable");
	        } catch (ClassCastException ccEx) {
	            fail("Cannot test a non symbol class");
	        } catch (XMLException e) {
	        	fail("Cannot test a non symbol class");
			}
	        getClassesToTest().add(legendClass);
	    }

	 /**
	  * The main purpose of this method is to create new legend instances to be used for
	  * other test methods.The new instances will be filled for the fillthelegend method.
	  * @return
	  * @throws FieldNotFoundException
	  */
	 public static ILegend[] getNewLegendInstances(){
	        ILegend[] legends = new ILegend[getClassesToTest().size()];
	        for (int i = 0; i < legends.length; i++) {

	            try {
	            	legends[i] = (ILegend) ((Class) getClassesToTest().get(i)).newInstance();
	            	fillthelegend(legends[i]);

	            } catch (InstantiationException e) {
	                fail("Instantiating class");
	            } catch (IllegalAccessException e) {
	                fail("Class not instantiable");
	            }

	        }
	        return legends;
	    }


	  protected void setUp() throws Exception {
	      legends = getNewLegendInstances();
	    }

	/**
	 * this test ensures that the legend is self-defining. Checks that
	 * the symbols contained by it can be replicated, and the rules for
	 * such symbols as well.
	 * @throws XMLException
	 */
	public void testILegendSelfDefinition() throws XMLException{
		for (int i = 0; i < legends.length; i++) {
			final ILegend theLegend = legends[i];
			final ILegend cloneLegend =theLegend.cloneLegend();
			assertTrue(theLegend.getClassName()+ " wrong class name declaration in getXMLEntity() ",
					cloneLegend.getClass().equals(theLegend.getClass()));
	        final Field[] theLegendFields = theLegend.getClass().getFields();
            for (int j = 0; j < theLegendFields.length; j++) {
                final Field fi = theLegendFields[j];
                final boolean wasAccessible = fi.isAccessible();
                fi.setAccessible(true);

                try {
                    assertTrue(theLegend.getClassName() + " fails or misses clonning the field " +fi.getName(),
                            fi.get(theLegend).equals(fi.get(cloneLegend)));
                } catch (IllegalArgumentException e) {
                    fail();
                } catch (IllegalAccessException e) {
                    fail();
                }
                fi.setAccessible(wasAccessible);
            }
			}
		}

	/**
	 * this test ensures that any legend always have a symbol ready to be used.
	 * an empty legend is incorrect.
	 *
	 */
	public void testSymbolAvailability() {
		for (int i = 0; i < legends.length; i++) {
			assertNotNull("Legend no. "+i+" '"+legends[i].getClassName()+" does not have a symbol ready to be used", legends[i].getDefaultSymbol());
		}

		for (int i = 0; i < legends.length; i++) {

			if (legends[i] instanceof IVectorLegend) {
				IVectorLegend vectLegend = (IVectorLegend) legends[i];
				try {
					vectLegend.setDefaultSymbol(null);
					fail("setDefaultSymbol(ISymbol) should not accept null values");
				} catch (NullPointerException e) {
					// correct
				}
			}

		}
	}


}
