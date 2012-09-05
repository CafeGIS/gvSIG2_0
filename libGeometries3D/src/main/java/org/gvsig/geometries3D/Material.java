package org.gvsig.geometries3D;

import java.awt.Color;

public class Material {

	public static class Face {
		public final static int FRONT = 0;
		public final static int BACK = 1;
		public final static int FRONT_AND_BACK = 2;

	}

	public static class ColorMode {
		public final static int AMBIENT = 0;
		public final static int DIFFUSE = 1;
		public final static int SPECULAR = 2;
		public final static int EMISSION = 3;
		public final static int AMBIENT_AND_DIFFUSE = 4;
		public final static int OFF = 5;

	}

	protected int face = Face.FRONT;

	protected int mode = ColorMode.OFF;

	private Color ambient;
	private Color diffuse;
	private Color specular;
	private Color emission;
	private float shininess;

	public Material() {

	}

	public Material(int face, int mode) {
		this.setMode(mode);
		this.setFace(face);

	}

	public int getFace() {
		return face;
	}

	public void setFace(int face) {
		this.face = face;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public Color getAmbient() {
		return ambient;
	}

	public void setAmbient(Color ambient) {
		this.ambient = ambient;
	}

	public Color getDiffuse() {
		return diffuse;
	}

	public void setDiffuse(Color diffuse) {
		this.diffuse = diffuse;
	}

	public Color getSpecular() {
		return specular;
	}

	public void setSpecular(Color specular) {
		this.specular = specular;
	}

	public Color getEmission() {
		return emission;
	}

	public void setEmission(Color emission) {
		this.emission = emission;
	}

	public float getShininess() {
		return shininess;
	}

	public void setShininess(float shininess) {
		this.shininess = shininess;
	}

}
