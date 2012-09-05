/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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

/* CVS MESSAGES:
*
* $Id: TestXMLEntity.java 10602 2007-03-05 11:15:50Z jaume $
* $Log$
* Revision 1.7  2007-03-05 11:15:43  jaume
* *** empty log message ***
*
* Revision 1.6  2007/03/05 10:03:12  jaume
* *** empty log message ***
*
* Revision 1.5  2007/03/05 09:00:11  jaume
* *** empty log message ***
*
* Revision 1.4  2007/03/02 13:35:56  jaume
* *** empty log message ***
*
* Revision 1.3  2007/03/02 13:27:32  jaume
* *** empty log message ***
*
* Revision 1.2  2007/03/02 13:24:53  jaume
* *** empty log message ***
*
* Revision 1.1  2007/03/02 13:23:50  jaume
* *** empty log message ***
*
* Revision 1.1  2006/08/29 06:18:17  jaume
* *** empty log message ***
*
*
*/
package com.iver.utiles;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

import junit.framework.TestCase;

import org.gvsig.tools.IverUtilesLibrary;
import org.gvsig.tools.ToolsLibrary;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.locator.Library;
import org.gvsig.tools.locator.LocatorException;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistenceManager;
import org.gvsig.tools.persistence.Persistent;
import org.gvsig.tools.persistence.PersistentState;

public class TestXMLEntityPersistenceManager extends TestCase {
	private XMLEntity x1, x2, x3, x4;
	private XMLEntity gvp1;
	private XMLEntity gvp2;
	private ArrayList list;
	private String string1;
	private String string2;
	private String string3;
	private String string4;
	private String string5;
	private int integer1;
	private int integer2;
	private int integer3;
	private long long1;
	private long long2;
	private double double1;
	private double double2;
	private double double3;
	private double double4;
	private double double5;
	private boolean bool1;
	private boolean bool2;
	private MyPersistentObject myPersObj;
	private final String ENCODING = "UTF-8";
	private MyPersistentContainer container1, container2;
	

	public void setUp() throws Exception {
		super.setUp();
		Library toolsLib = new ToolsLibrary();
		toolsLib.initialize();
		
		Library utilsLib = new IverUtilesLibrary();
		utilsLib.initialize();
		
		toolsLib.postInitialize();
		utilsLib.postInitialize();

		string1 = "This is my first$$ String to check";
		string2 = "An very long long long asdasfsafffffffffasfasasdffffffsf"
			+ "long long long long long asdfaaaddddddddddddddddddddddddasdsd"
			+ "fbkdfjger gsdf glkwerqwsdvsiñomodsgt string with special"
			+ "characters and etc etc asdfasdfwqfwefwfasdfasfasfasfasfasfasfas"
			+ "asfasfasfasfasfasfasfasfdasdsdfsafsafasfasfsafasfasfasfasfasfas"
			+ "safasfasfasdfkb sdfd  ertge ertwgfwrt43 fbgdsf gw wfwf sdgas  "
			+ "asfwr324rf635r sdgsa swe3er 6546               sfasdfsafas asfa"
			+ "asdfasfasf435hdhge sfsd g dfsafasfsa as";
		string3 = "pero perico pero";
		string4 = "piripi pi";
		string5 = "lepe lepero";

		integer1 = 9323823;
		integer2 = -234234234;
		integer3 = 0;
		
		double1 = 23234.234;
		double2 = -674633.2423423;
		double3 = 0.0;
		double4 = 0.23432445634;
		double5 = -0.2342323;
		
		bool1 = true;
		bool2 = false;
		
		myPersObj = new MyPersistentObject();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		list = null;
		container1 = null;
		container2 = null;
	}

	public void testMarshall1() {
		marshall1();
		
		try {
			Reader test = getReader("/tmp/persistence-test.xml");
			Reader sample = getReader("testdata/persistence-test.xml");
			char[] array1 = new char[1024];
			char[] array2 = new char[1024];
			while (sample.read(array1)!=-1) {
				test.read(array2);
				for (int i=0; i<array1.length; i++) {
					assertEquals(array1[i], array2[i]);
				}
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	public Reader getReader(String fileName) throws UnsupportedEncodingException, FileNotFoundException {
		return new InputStreamReader(
				new BufferedInputStream(
						new FileInputStream(fileName)), ENCODING) {
		};
	}

	public Writer getWriter(String fileName) throws UnsupportedEncodingException, FileNotFoundException{
		return new OutputStreamWriter(
				new BufferedOutputStream(
						new FileOutputStream(fileName)), ENCODING) {
		};
	}
	
	public void testMarshall2() {
		marshall2();
		
		try {
			Reader test = getReader("/tmp/persistence-test2.xml");
			Reader sample = getReader("testdata/persistence-test2.xml");
			char[] array1 = new char[1024];
			char[] array2 = new char[1024];
			while (sample.read(array1)!=-1) {
				test.read(array2);
				for (int i=0; i<array1.length; i++) {
					assertEquals(array1[i], array2[i]);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	private void initList() {
		if (list==null) {
			list = new ArrayList();
			list.add(string1);
			list.add(string2);
			list.add(new Integer(integer1));
			list.add(new Integer(integer2));
			list.add(new Integer(integer3));
			list.add(new Boolean(bool1));
			list.add(new Boolean(bool2));
			list.add(myPersObj);
			initContainer2();
			list.add(container2);
		}
	}

	private void marshall1() {
		initList();
		
		MyPersistentList originalList = new MyPersistentList(list);
		
		try {
			PersistentState state = ToolsLocator.getPersistenceManager().getState(originalList);
			Writer writer = getWriter("/tmp/persistence-test.xml");
			state.save(writer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (LocatorException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (PersistenceException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	public void testUnmarshall1() {
		try {
			initList();
			Reader reader = getReader("testdata/persistence-test.xml");
			PersistentState state = ToolsLocator.getPersistenceManager().loadState(reader);
			Iterator it = state.getIterator("myList");
			int i = 0;
			while (it.hasNext()) {
				Object obj = it.next();
				if (i<list.size()) {
					assertEquals(obj, list.get(i++));
				}
				else {
					fail("Iterator size is shorter than original list");
				}
			}
			if (i!=list.size()) {
				fail("Iterator size is longer than original list");
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (LocatorException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (PersistenceException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	public void testUnmarshall2() {
		try {
			initContainer1();
			Reader reader = getReader("testdata/persistence-test2.xml");
			PersistenceManager manager = ToolsLocator.getPersistenceManager();
			PersistentState state = manager.loadState(reader);
			Object obj = manager.create(state);
			assertEquals(container1, obj);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (LocatorException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (PersistenceException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	private void initContainer2() {
		if (container2==null) {
			container2 = new MyPersistentContainer();
			container2.setValue1(myPersObj);
			container2.setValue2(string4);
			container2.setValue3(string5);
		}
	}

	private void initContainer1() {
		if (container1==null) {
			initList();
			container1 = new MyPersistentContainer();
			container1.setValue1(string3);
			container1.setValue2(new MyPersistentList(list));
			container1.setValue3(myPersObj);
		}
	}

	private void marshall2() {
		initContainer1();	
			try {
				PersistentState state = ToolsLocator.getPersistenceManager().getState(container1);
				Writer writer = getWriter("/tmp/persistence-test2.xml");
				state.save(writer);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				fail(e.getMessage());
			} catch (LocatorException e) {
				e.printStackTrace();
				fail(e.getMessage());
			} catch (PersistenceException e) {
				e.printStackTrace();
				fail(e.getMessage());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
	}

	public void testIterator() {
		list = new ArrayList();
		list.add(string1);
		list.add(string2);
		list.add(new Integer(integer1));
		list.add(new Integer(integer2));
		list.add(new Integer(integer3));
		list.add(new Boolean(bool1));
		list.add(new Boolean(bool2));
		list.add(myPersObj);
		
		MyPersistentList originalList = new MyPersistentList(list);
		try {
			PersistentState state = ToolsLocator.getPersistenceManager().getState(originalList);
			MyPersistentList resultList = new MyPersistentList();
			resultList.loadFromState(state);
			checkEquals(originalList, resultList);
 			
		} catch (LocatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

    public void checkEquals(MyPersistentList originalList, MyPersistentList resultList) {
    	if (originalList == null) {
    		if (resultList == null) {
    			return;
    		}
    		else {
    			fail("List1 is null");
    		}
    	}
    	else if (resultList == null) {
    		fail("List2 is null");
    	}

    	Iterator iterator1 = originalList.iterator();
    	Iterator iterator2 = resultList.iterator();
    	while(iterator1.hasNext() && iterator2.hasNext()) {
    	    Object o1 = iterator1.next();
    	    Object o2 = iterator2.next();
    	    assertEquals(o1, o2);
    	}
    	assertEquals(iterator1.hasNext(), iterator2.hasNext());
    }

	public static class MyPersistentList implements Persistent {
		public ArrayList list;
		public MyPersistentList() {
			
		}
		public MyPersistentList(ArrayList list) {
			this.list = list;
		}

		public void saveToState(PersistentState state)
				throws PersistenceException {
			state.set("myList", list.iterator());
			
		}

		public void loadFromState(PersistentState state) throws PersistenceException {
			Iterator it = state.getIterator("myList");
			list = new ArrayList();
			while (it.hasNext()) {
				list.add(it.next());
			}
		}
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof MyPersistentList) {
				return list.equals(((MyPersistentList)obj).list);
			}
			return false;
		}
		
		public Iterator iterator() {
			return list.iterator();
		}
	}
	
	public static class MyPersistentObject implements Persistent {
		private String value = "Sopa de cabrita";
		private int intValue = 20090625;

		public void saveToState(PersistentState state)
				throws PersistenceException {
			state.set("miValor", value);
			state.set("numero", intValue);
			
		}

		public String getValue() {
			return value;
		}

		public void setValue(String val) {
			value = val;
		}

		public void loadFromState(PersistentState state) throws PersistenceException {
			value = state.getString("miValor");
			intValue = state.getInt("numero");
		}

		public boolean equals(Object obj) {
			if (obj instanceof MyPersistentObject) {
				MyPersistentObject myPers = (MyPersistentObject) obj;
				return value.equals(myPers.getValue());
			}
			return false;
		}
	}

	public static class MyPersistentContainer implements Persistent {
		private Object value1;
		private Object value2;
		private Object value3;

		public void saveToState(PersistentState state)
				throws PersistenceException {
			state.set("miObjetito1", value1);
			state.set("miObjetito2", value2);
			state.set("miObjetito3", value3);
			
		}
		public Object getValue1() {
			return value1;
		}
		public void setValue1(Object val) {
			value1 = val;
		}
		public Object getValue2() {
			return value2;
		}
		public void setValue2(Object val) {
			value2 = val;
		}
		public Object getValue3() {
			return value3;
		}
		public void setValue3(Object val) {
			value3 = val;
		}

		public void loadFromState(PersistentState state) throws PersistenceException {
			value1 = state.get("miObjetito1");
			value2 = state.get("miObjetito2");
			value3 = state.get("miObjetito3");
		}

		public boolean equals(Object obj) {
			if (obj instanceof MyPersistentContainer) {
				MyPersistentContainer myPers = (MyPersistentContainer) obj;
				if (!value1.equals(myPers.getValue1())) {
					return false;
				}
				if (!value2.equals(myPers.getValue2())) {
					return false;
				}
				if (!value3.equals(myPers.getValue3())) {
					return false;
				}
				else {
					return true;
				}
			}
			return false;
		}
		
	}
}
