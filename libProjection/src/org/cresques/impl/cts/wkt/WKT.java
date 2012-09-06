package org.cresques.impl.cts.wkt;

import java.util.ArrayList;

/**
 * Generates a WKT from CRS parameters.
 * as, for example in
 * "GEOGCS[\"ETRS89\","+
        "DATUM[\"European_Terrestrial_Reference_System_1989\","+
            "SPHEROID[\"GRS 1980\",6378137,298.257222101,"+
                "AUTHORITY[\"EPSG\",\"7019\"]],"+
            "AUTHORITY[\"EPSG\",\"6258\"]," +
            "TOWGS84[0,0,0,0,0,0,0]],"+
        "PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],"+
        "UNIT[\"degree\",0.01745329251994328, AUTHORITY[\"EPSG\",\"9122\"]],"+
        "AUTHORITY[\"EPSG\",\"4258\"]]";
    
 * @author Luis W. Sevilla <sevilla_lui@gva.es>
 *
 */
public abstract class WKT {

	static class Authority {
		String name;
		String code;
		public Authority(String n, String c) {
			name = n;
			code = c;
		}
		public String toWKT() {
			return "AUTHORITY[\""+name+"\",\""+code+"\"]";
		}
	}

	static class Parameter {
		protected String name;
		private String value;
		protected Authority authority; // optional
		
		public Parameter(String n) {
			name = n;
		}
		
		public Parameter(String n, String v) {
			name = n;
			value = v;
		}
		public Parameter(String n, Authority a) {
			name = n;
			authority = a;
		}
		
		public String toWKT() {
			String txt = "PARAMETER[\""+name+"\","+value;
			if (authority != null)
				txt += ","+authority.toWKT();
			return txt + "]";
		}
	}

	static class Spheroid {
		String name;
		double semiMajor;
		double inverseFlattening;
		Authority authority = null; // optional
		
		public Spheroid(String n, double s, double f) {
			name = n;
			semiMajor = s;
			inverseFlattening = f;
		}
		public Spheroid(String n, double s, double f, Authority a) {
			name = n;
			semiMajor = s;
			inverseFlattening = f;
			authority = a;
		}
		public String toWKT() {
			String txt = "SPHEROID[\""+name+"\","+
				semiMajor+","+inverseFlattening;
			if (authority != null)
				txt += ","+authority.toWKT();
			return txt + "]";
		}
	}

	static class ToWGS84 {
		double dx = 0;
		double dy = 0;
		double dz = 0;
		double ex = 0;
		double ey = 0;
		double ez = 0;
		double ppm = 0;
		public ToWGS84() {
		}
		public ToWGS84(double dx, double dy, double dz, double ex, double ey, double ez, double ppm) {
			this.dx = dx;
			this.dy = dy;
			this.dz = dz;
			this.ex = ex;
			this.ey = ey;
			this.ez = ez;
			this.ppm = ppm;
		}
		public String toWKT() {
			return "TOWGS84["+dx+","+dy+","+dz+","+ex+","+ey+","+ez+","+ppm+"]";
		}
	}

	static class Datum {
		String name;
		Spheroid spheroid;
		ToWGS84 towgs84 = new ToWGS84(); // optional
		Authority authority; // optional
		public Datum(String n, Spheroid s) {
			init (n, s, new ToWGS84(), null);
		}
		public Datum(String n, Spheroid s, ToWGS84 t) {
			init (n, s, t, null);
		}
		public Datum(String n, Spheroid s, Authority a) {
			init (n, s, new ToWGS84(), a);
		}
		public Datum(String n, Spheroid s, ToWGS84 t, Authority a) {
			init (n, s, t, a);
		}
		private void init(String n, Spheroid s,
			ToWGS84 t, Authority a) {
			name = n;
			spheroid = s;
			towgs84 = t;
			authority = a;
		}
		public String toWKT() {
			String txt = "DATUM[\""+name+"\","+spheroid.toWKT();
			if (towgs84 != null)
				txt += ","+towgs84.toWKT();
			if (authority != null)
				txt += ","+authority.toWKT();
			return txt+"]";
		}
	}

	static class PrimeM {
		String name;
		double longitude;
		Authority authority = null; // optional
		
		public PrimeM(String n, double l) {
			name = n;
			longitude = l;
		}
		
		public PrimeM(String n, double l, Authority a) {
			name = n;
			longitude = l;
			authority = a;
		}
		
		public String toWKT() {
			String txt = "PRIMEM[\""+name+"\","+longitude;
			if (authority != null)
				txt += ","+authority.toWKT();
			return txt + "]";
		}
	}

	static class Unit {
		String name;
		double conversionFactor;
		Authority authority; // optional
		
		public Unit(String n, double l) {
			name = n;
			conversionFactor = l;
		}
		
		public Unit(String n, double l, Authority a) {
			name = n;
			conversionFactor = l;
			authority = a;
		}
		
		public String toWKT() {
			String txt = "UNIT[\""+name+"\","+conversionFactor;
			if (authority != null)
				txt += ","+authority.toWKT();
			return txt + "]";
		}
	}
	
	static class Orientation {
		public final static int NORTH = 0;
		public final static int SOUTH = 1;
		public final static int EAST = 2;
		public final static int WEST = 3;
		public final static int UP = 4;
		public final static int DOWN = 5;
		public final static int OTHER = 6;
		protected final String [] txt = {
			"NORTH","SOUTH","EAST","WEST","UP","DOWN","OTHER"
		};
		protected int to = -1;
		
		public Orientation(int to) {
			this.to = to;
		}
	}
	
	static class Axis extends Orientation {
		String name;
		
		public Axis(String name, int to) {
			super(to);
			this.name = name;
		}
		
		public String toWKT() {
			return "AXIS[\""+name+"\","+txt[to]+ "]";
		}
	}
	
	static class TwinAxes {
		Axis axe1;
		Axis axe2;
		
		public TwinAxes(Axis a1, Axis a2) {
			axe1 = a1;
			axe2 = a2;
		}

		public String toWKT() {
			return axe1.toWKT()+","+axe2.toWKT();
		}

	}

	static class GeogCS extends WKT {
		String name;
		Datum datum;
		PrimeM primeMeridian;
		Unit unit;
		TwinAxes axes = null; // optional
		Authority authority = null; // optional
		
		public GeogCS(String n, Datum d, PrimeM p, Unit u) {
			init(n, d, p, u, null, null);
		}
		
		public GeogCS(String n, Datum d, PrimeM p, Unit u,
				Authority a) {
			init(n, d, p, u, null, a);
		}
		
		public GeogCS(String n, Datum d, PrimeM p, Unit u,
				TwinAxes t) {
			init(n, d, p, u, t, null);
		}
		
		public GeogCS(String n, Datum d, PrimeM p, Unit u,
				TwinAxes t, Authority a) {
			init(n, d, p, u, t, a);
		}
		
		private void init(String n, Datum d, PrimeM p, Unit u,
			TwinAxes t, Authority a) {
			name = n;
			datum = d;
			primeMeridian = p;
			unit = u;
			axes = t;
			authority = a;
		}
		
		public String toWKT() {
			String txt = "GEOGCS[\""+name+"\","+datum.toWKT()+
				","+primeMeridian.toWKT()+","+unit.toWKT();
			if (axes != null)
				txt += ","+axes.toWKT();
			if (authority != null)
				txt += ","+authority.toWKT();
			return txt+"]";
		}
	}

	static class Projection extends Parameter {
		public Projection(String n) {
			super(n);
		}
		
		public Projection(String n, Authority a) {
			super(n,a);
		}
		
		public String toWKT() {
			String txt = "PROJECTION[\""+name+"\"";
			if (authority != null)
				txt += ","+authority.toWKT();
			return txt + "]";
		}
	}

	static class ProjCS extends Parameter {
		GeogCS geogcs;
		Projection proj;
		ArrayList params = new ArrayList();
		Unit unit;
		TwinAxes axes = null; // optional

		public ProjCS(String n, GeogCS cs, Projection prj, Unit u ) {
			super(n);
			Parameter [] p = {};
			init(n, cs, prj, p, u, null);
		}
		
		public ProjCS(String n, GeogCS cs, Projection prj,
				Parameter [] p, Unit u ) {
			super(n);
			init(n, cs, prj, p, u, null);
		}
		
		public ProjCS(String n, GeogCS cs, Projection prj,
				Unit u, TwinAxes t, Authority a ) {
			super(n, a);
			Parameter [] p = {};
			init(n, cs, prj, p, u, t);
		}
		
		public ProjCS(String n, GeogCS cs, Projection prj,
				Parameter [] p, Unit u, TwinAxes t, Authority a ) {
			super(n, a);
			init(n, cs, prj, p, u, t);
		}
		
		private void init(String n, GeogCS cs, Projection prj,
			Parameter [] p, Unit u, TwinAxes t) {
			geogcs = cs;
			proj = prj;
			for (int i=0; i<p.length; i++)
				params.add(p[i]);
			unit = u;
			axes = t;
		}

		public void add(Parameter p) {
			params.add(p);
		}
		
		public String toWKT() {
			String txt = "PROJCS[\""+name+"\","+geogcs.toWKT()+
				","+proj.toWKT();
			for (int i=0; i<params.size();i++)
				txt += ","+((Parameter)params.get(i)).toWKT();
			txt += ","+unit.toWKT();
			if (axes != null)
				txt += ","+axes.toWKT();
			if (authority != null)
				txt += ","+authority.toWKT();
			return txt+"]";
		}
	}

	abstract public String toWKT();
}
