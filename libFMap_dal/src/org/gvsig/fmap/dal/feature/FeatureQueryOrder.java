package org.gvsig.fmap.dal.feature;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.gvsig.fmap.dal.feature.impl.DefaultFeatureComparator;
import org.gvsig.tools.evaluator.Evaluator;

public class FeatureQueryOrder {

	private List members = new ArrayList();

	public class FeatureQueryOrderMember {
		String attributeName = null;
		Evaluator evaluator = null;
		boolean ascending;

		FeatureQueryOrderMember(String attributeName, boolean ascending) {
			this.attributeName = attributeName;
			this.ascending = ascending;
		}
		FeatureQueryOrderMember(Evaluator evaluator, boolean ascending) {
			this.evaluator = evaluator;
            this.ascending = ascending;
		}
		public boolean hasEvaluator() {
			return this.evaluator != null;
		}
		public Evaluator getEvaluator() {
			return this.evaluator;
		}
		public boolean getAscending() {
			return this.ascending;
		}
		public String getAttributeName() {
			return this.attributeName;
		}
	}

	public Object add(String attributeName, boolean ascending) {
        FeatureQueryOrderMember member = new FeatureQueryOrderMember(
                attributeName, ascending);
		if( members.add(member) ) {
			return member;
		}
		return null;
	}

	public Object add(Evaluator evaluator, boolean ascending) {
		FeatureQueryOrderMember member = new FeatureQueryOrderMember(
				evaluator,
				ascending);
		if (members.add(member)) {
			return member;
		}
		return null;
	}

	public Iterator iterator() {
		return members.iterator();
	}

	public boolean remove(FeatureQueryOrderMember member) {
		return members.remove(member);
	}

	public void remove(int index) {
		members.remove(index);
	}

	public void clear() {
		members.clear();
	}

	public int size() {
		return this.members.size();
	}

	public Comparator getFeatureComparator() {
		return new DefaultFeatureComparator(this);
	}

	public FeatureQueryOrder getCopy() {
		FeatureQueryOrder aCopy = new FeatureQueryOrder();
		Iterator iter = this.members.iterator();
		FeatureQueryOrderMember member;
		while (iter.hasNext()) {
			member = (FeatureQueryOrderMember) iter.next();
			if (member.hasEvaluator()) {
				aCopy.add(member.getEvaluator(), member.getAscending());
			} else {
				aCopy.add(member.getAttributeName(), member.getAscending());
			}
		}
		return aCopy;
	}
}
