/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
* MA  02110-1301, USA.
*
*/

/*
* AUTHORS (In addition to CIT):
* 2008 {{Company}}   {{Task}}
*/


package org.gvsig.fmap.dal.feature.spi.index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.gvsig.fmap.dal.exception.InitializeException;


public abstract class AbstractFeatureIndexProvider implements FeatureIndexProvider {

	private FeatureIndexProviderServices services = null;

	public void initialize() throws InitializeException {
	}

	public void setFeatureIndexProviderServices(
			FeatureIndexProviderServices services) {
		this.services = services;
	}

	public FeatureIndexProviderServices getFeatureIndexProviderServices() {
		return services;
	}

	protected class LongList implements java.util.List{
		private ArrayList list;
		public LongList(java.util.List list) {
			this.list=(ArrayList)list;
		}
		public void add(int arg0, Object arg1) {
			list.add(arg0, arg1);
		}

		public boolean add(Object arg0) {
			return list.add(arg0);
		}

		public Object set(int arg0, Object arg1) {
			return list.set(arg0, arg1);
		}
		public boolean addAll(Collection arg0) {
			return list.addAll(arg0);
		}
		public boolean addAll(int arg0, Collection arg1) {
			return list.addAll(arg0, arg1);
		}
		public void clear() {
			list.clear();
		}
		public boolean contains(Object o) {
			return list.contains(o);
		}
		public boolean containsAll(Collection arg0) {
			return list.containsAll(arg0);
		}
		public Object get(int index) {
			Object obj=list.get(index);
			if (obj instanceof Integer){
				return new Long(((Integer)obj).longValue());
			}
			return obj;
		}
		public int indexOf(Object o) {
			return list.indexOf(o);
		}
		public boolean isEmpty() {
			return list.isEmpty();
		}
		public Iterator iterator() {
			return new LongIterator(list.iterator());
		}
		public int lastIndexOf(Object o) {
			return list.lastIndexOf(o);
		}
		public ListIterator listIterator() {
			return list.listIterator();
		}
		public ListIterator listIterator(int index) {
			return list.listIterator(index);
		}
		public boolean remove(Object o) {
			return list.remove(o);
		}
		public Object remove(int index) {
			return list.remove(index);
		}
		public boolean removeAll(Collection arg0) {
			return list.removeAll(arg0);
		}
		public boolean retainAll(Collection arg0) {
			return list.retainAll(arg0);
		}
		public int size() {
			return list.size();
		}
		public List subList(int fromIndex, int toIndex) {
			return list.subList(fromIndex, toIndex);
		}
		public Object[] toArray() {
			return list.toArray();
		}
		public Object[] toArray(Object[] arg0) {
			return list.toArray(arg0);
		}
		class LongIterator implements Iterator{
			private Iterator iterator;
			public LongIterator(Iterator it) {
				iterator=it;
			}
			public boolean hasNext() {
				return iterator.hasNext();
			}

			public Object next() {
				Object obj=iterator.next();
				if (obj instanceof Integer){
					return new Long(((Integer)obj).longValue());
				}
				return obj;
			}

			public void remove() {
				iterator.remove();
			}
		}
	}
}

