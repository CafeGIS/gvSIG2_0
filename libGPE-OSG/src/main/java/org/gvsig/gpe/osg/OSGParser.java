/* gvSIG. Geographic Information System of the Valencian Government
 *  osgVP. OSG Virtual Planets.
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
 * 2008 Instituto de Automática e Informática Industrial, UPV.
 */

package org.gvsig.gpe.osg;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.Vector;
import java.util.logging.Level;

import org.gvsig.gpe.IGPEContentHandler3D;
import org.gvsig.gpe.parser.GPEParser;
import org.gvsig.osgvp.core.osg.AutoTransform;
import org.gvsig.osgvp.core.osg.DrawArrayLengths;
import org.gvsig.osgvp.core.osg.Drawable;
import org.gvsig.osgvp.core.osg.Geode;
import org.gvsig.osgvp.core.osg.Geometry;
import org.gvsig.osgvp.core.osg.Group;
import org.gvsig.osgvp.core.osg.Material;
import org.gvsig.osgvp.core.osg.Matrix;
import org.gvsig.osgvp.core.osg.MatrixTransform;
import org.gvsig.osgvp.core.osg.Node;
import org.gvsig.osgvp.core.osg.PositionAttitudeTransform;
import org.gvsig.osgvp.core.osg.PrimitiveSet;
import org.gvsig.osgvp.core.osg.Quat;
import org.gvsig.osgvp.core.osg.Texture2D;
import org.gvsig.osgvp.core.osg.Vec2;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.core.osg.Vec4;
import org.gvsig.osgvp.core.osgdb.osgDB;
import org.gvsig.osgvp.exceptions.image.ImageConversionException;
import org.gvsig.osgvp.exceptions.image.ImageException;
import org.gvsig.osgvp.exceptions.node.ChildIndexOutOfBoundsExceptions;
import org.gvsig.osgvp.exceptions.node.LoadNodeException;
import org.gvsig.osgvp.exceptions.node.NodeException;
import org.gvsig.osgvp.exceptions.texture.TextureStageException;
import org.gvsig.osgvp.util.Util;

public class OSGParser extends GPEParser {

	Stack<Matrix> _transforms = new Stack<Matrix>();
	Vector<Stack<Texture2D>> _textures = new Vector<Stack<Texture2D>>();
	Stack<Material> _materials = new Stack<Material>();
	Stack<Boolean> _blendings = new Stack<Boolean>();
	private IGPEContentHandler3D _content;
	private String description;
	private String name;

	public OSGParser(String name, String description) {
		// super(name, description);
		this.name = name;
		this.description = description;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return this.description;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public boolean accept(URI uri) {
		if ((uri.getPath().toUpperCase().endsWith("OSG"))
				|| (uri.getPath().toUpperCase().endsWith("3DS"))
				|| (uri.getPath().toUpperCase().endsWith("IVE"))
				|| (uri.getPath().toUpperCase().endsWith("OBJ"))) {
			return true;
		}
		return false;
	}

	public String[] getFormats() {
		String[] formats = new String[4];
		formats[0] = "OSG";
		formats[1] = "3DS";
		formats[2] = "OBJ";
		formats[3] = "IVE";
		return formats;
	}

	public String[] getVersions() {
		String[] versions = new String[1];
		versions[0] = "All";
		return versions;
	}

	@Override
	protected void parseStream() {

	}

	@Override
	protected void parseURI() {

		// This is a very bad hack to avoid testing hangs in Mac OS Lepard
		Color color = new Color(0, 0, 0, 255);
		// TODO remove this hack

		Vector<Integer> addedTextures = new Vector<Integer>();
		boolean addedMaterial = false;

		_content = (IGPEContentHandler3D) getContentHandler();
//		Util.logger.log(Level.FINEST, getMainFile().getPath());
		Node root=null;

		try {
			String os = System.getProperty("os.name");
			String path;
			if (os.toLowerCase().startsWith("windows")) {

				path = getMainFile().getPath().substring(1,
						getMainFile().getPath().length());
			} else {
				path = getMainFile().getPath();
			}
			
			root = osgDB.readNodeFile(path);
//			root = osgDB.readNodeFile(getMainFile().getPath().substring(1, getMainFile().getPath().length()));
			
		} catch (LoadNodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		if (root == null)
//			Util.logger.log(Level.FINEST, "Cannot open"
//					+ getMainFile().getPath());
		System.out.println("error");
		else {

			Object multiGeometry;
			multiGeometry = _content.startMultiGeometry("", "");

			if (root.className().equals("Geode")) {

				Geode rootGeode = new Geode(root.getCPtr());
				Object multiSolid;

				multiSolid = _content.startMultiSolid("", "");

				_content.addGeometryToMultiGeometry(multiSolid, multiGeometry);

				try {
					addedTextures = pushTextures(rootGeode);
				} catch (NodeException e) {
				}
				try {
					addedMaterial = pushMaterial(rootGeode);
				} catch (NodeException e) {
				}
				
				_blendings.push(rootGeode.getOrCreateStateSet().getEnabledBlending());

				for (int i = 0; i < rootGeode.getNumDrawables(); i++) {

					try {
						parseDrawable(rootGeode.getDrawable(i), multiSolid);
					} catch (ChildIndexOutOfBoundsExceptions e) {
						e.printStackTrace();
						return;
					} catch (NodeException e) {
					}

				}

				_blendings.pop();
				_content.endMultiSolid(multiSolid);

			}

			else if (root.className().equals("Group")) {
				Group rootGroup;

				try {
					rootGroup = new Group(root.getCPtr());
				} catch (NodeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
				try {
					addedTextures = pushTextures(rootGroup);
				} catch (NodeException e) {
				}
				try {
					addedMaterial = pushMaterial(rootGroup);
				} catch (NodeException e) {
				}
				_blendings.push(rootGroup.getOrCreateStateSet().getEnabledBlending());

				//System.out.println("CHILDREN: " + rootGroup.getNumChildren());
				
				for (int i = 0; i < rootGroup.getNumChildren(); i++) {

					try {
						parseNode(rootGroup.getChild(i), multiGeometry);
					} catch (ChildIndexOutOfBoundsExceptions e) {
						e.printStackTrace();
						return;
					}
//					Util.logger.log(Level.FINEST, "Node parsed");
				}

				_blendings.pop();

			}

			else {

//				Util.logger.log(Level.FINEST,
//						"First parsed object must be Group or Geode");

			}

			popTextures(addedTextures);
			if (addedMaterial)
				_materials.pop();
			_content.endMultiGeometry(multiGeometry);

		}

	}

	protected void parseNode(Node node, Object parent) {
		// Util.logger.log(Level.FINEST, "node init");
		Node instance;
		boolean isTransform = false;
		boolean addedMaterial = false;
		Vector<Integer> addedTextures = new Vector<Integer>();

		try {

			Class<?> reflect = Class.forName("org.gvsig.osgvp."
					+ node.className());
			Constructor<?> builder = reflect
					.getConstructor(new Class[] { long.class });
			instance = (Node) builder
					.newInstance(new Object[] { node.getCPtr() });

		}

		catch (Exception e) {
			instance = new Node(node.getCPtr());
		}

		if (instance instanceof Group) {

			Object multiGeometry;
			multiGeometry = _content.startMultiGeometry("", "");
			_content.addGeometryToMultiGeometry(multiGeometry, parent);
			isTransform = pushTransform((Group) instance);
			try {
				addedTextures = pushTextures(instance);
			} catch (NodeException e) {
			}
			try {
				addedMaterial = pushMaterial(instance);
			} catch (NodeException e) {
			}
			if (_blendings.lastElement())
				_blendings.push(true);
			else
				_blendings.push(instance.getOrCreateStateSet()
						.getEnabledBlending());
			for (int i = 0; i < ((Group) instance).getNumChildren(); i++) {

				try {
					parseNode(((Group) instance).getChild(i), multiGeometry);
				} catch (ChildIndexOutOfBoundsExceptions e) {
					e.printStackTrace();
					return;
				}

			}
			if (isTransform)
				_transforms.pop();
			if (addedMaterial)
				_materials.pop();
			_blendings.pop();
			popTextures(addedTextures);
			_content.endMultiGeometry(multiGeometry);

		}

		else if (instance instanceof Geode) {

			Object multiSolid;
			multiSolid = _content.startMultiSolid("", "");
			_content.addGeometryToMultiGeometry(multiSolid, parent);
			try {
				addedTextures = pushTextures(instance);
			} catch (NodeException e) {
			}
			try {
				addedMaterial = pushMaterial(instance);
			} catch (NodeException e) {
			}
			if (_blendings.lastElement())
				_blendings.push(true);
			else
				_blendings.push(instance.getOrCreateStateSet()
						.getEnabledBlending());
			for (int i = 0; i < ((Geode) instance).getNumDrawables(); i++) {

				try {
					parseDrawable(((Geode) instance).getDrawable(i), multiSolid);
				} catch (ChildIndexOutOfBoundsExceptions e) {
					e.printStackTrace();
					return;
				} catch (NodeException e) {
				}

			}
			if (addedMaterial)
				_materials.pop();
			_blendings.pop();
			popTextures(addedTextures);
			_content.endMultiSolid(multiSolid);

		}
		// Util.logger.log(Level.FINEST, "Node parsed-0");
		// else Util.logger.log(Level.FINEST,instance.className() + " is not
		// instanceof
		// Group or Geode");

	}

	protected void parseDrawable(Drawable drawable, Object feature)
			throws NodeException {
		Util.logger.log(Level.FINEST, "drawable init");
		int t = 0;
		if (drawable instanceof Geometry) {

			int j;
			Object solid;
			solid = _content.startSolid("", "");
			_content.addSolidToMultiSolid(solid, feature);

			Matrix matrix = new Matrix();
			for (int i = 0; i < _transforms.size(); i++) {

				matrix.mult(_transforms.get(i), matrix);

			}

			// Util.logger.log(Level.FINEST, "transforms");

			Vector<Vec3> vertices = ((Geometry) drawable).getVertexArray();
			if (vertices == null)
				vertices = new Vector<Vec3>();
			Vec3 vertex;
			int i;
			// Util.logger.log(Level.FINEST, "vertex starts");
			_content.startSolidVertexArray(solid);
			for (i = 0; i < vertices.size(); i++) {

				vertex = vertices.get(i);

				// Util.logger.log(Level.FINEST,"Vertice sin
				// transformar"+vertex.x()+";"+vertex.y()+";"+vertex.z()+";");
				vertex = matrix.prod(vertex, matrix);

				// Util.logger.log(Level.FINEST,"Vertice
				// transformado"+vertex.x()+";"+vertex.y()+";"+vertex.z()+";");
				_content.addVertexToSolid(solid, vertex.x(), vertex.y(), vertex
						.z());

			}
			_content.endSolidVertexArray();

			Vector<Vec3> normals = ((Geometry) drawable).getNormalArray();
			// Util.logger.log(Level.FINEST, "talla normals" + normals.size());

			if (normals == null)
				normals = new Vector<Vec3>();

			Vec3 normal;
			_content.startSolidNormalArray(solid);

			for (i = 0; i < normals.size(); i++) {

				normal = normals.get(i);
				// Util.logger.log(Level.FINEST, " normal bucleee" + normal);
				normal = matrix.prod(normal, matrix);
				normal.normalize();
				_content.addNormalToSolid(solid, normal.x(), normal.y(), normal
						.z());

			}
			_content.endSolidNormalArray();

			_content.setNormalBindingToSolid(solid, ((Geometry) drawable)
					.getNormalBinding());

			try {
				Vector<Vec4> colors = ((Geometry) drawable).getColorArray();
				if (colors == null)
					colors = new Vector<Vec4>();
				Vec4 color;
				// Util.logger.log(Level.FINEST, "color starts" +
				// colors.size());
				_content.startSolidColorArray(solid);
				for (i = 0; i < colors.size(); i++) {
					// Util.logger.log(Level.FINEST, "bucleeeeee 0");
					color = colors.get(i);
					_content.addColorToSolid(solid, (float) color.x(),
							(float) color.y(), (float) color.z(), (float) color
									.w());
					// Util.logger.log(Level.FINEST, "bucleeeeee");
				}

				_content.endSolidColorArray();

			} catch (NullPointerException e) {

			}

			// Util.logger.log(Level.FINEST, "primitiveSets: "
			// + ((Geometry) drawable).getNumPrimitiveSets());

			for (int k = 0; k < ((Geometry) drawable).getNumPrimitiveSets(); k++) {
				int mode = ((Geometry) drawable).getPrimitiveSet(k).getMode();
				int type = ((Geometry) drawable).getPrimitiveSet(k).getType();

				if (type == PrimitiveSet.Type.DrawArrayLengthsPrimitiveType) {

					int offset = 0;
					int accum = 0;
					DrawArrayLengths lengths = new DrawArrayLengths(
							((PrimitiveSet) ((Geometry) drawable)
									.getPrimitiveSet(k)).getCPtr());
					Vector<Integer> arrayLengths = lengths
							.getElementLengthsVector();

					if (arrayLengths == null)
						arrayLengths = new Vector<Integer>();

					for (int h = 0; h < arrayLengths.size(); h++) {

						Object primitiveSet = _content.startPrimitiveSet(mode,
								PrimitiveSet.Type.DrawArraysPrimitiveType);
						_content.addPrimitiveSetToSolid(solid, primitiveSet);

						accum = arrayLengths.get(h);

						for (int l = 0; l < accum; l++) {

							_content.addIndexToPrimitiveSet(primitiveSet,
									((Geometry) drawable).getPrimitiveSet(k)
											.index(offset + l));

						}

						offset = offset + accum;
						_content.endPrimitiveSetIndexArray();
						_content.endPrimitiveSet(primitiveSet);

					}

				} else {
					Object primitiveSet = _content
							.startPrimitiveSet(mode, type);

					_content.addPrimitiveSetToSolid(solid, primitiveSet);
					_content.startPrimitiveSetIndexArray(primitiveSet,
							((Geometry) drawable).getPrimitiveSet(k)
									.getNumIndices());
					// Util.logger.log(Level.FINEST, "Num Indices: "
					// + String.valueOf(((Geometry) drawable)
					// .getPrimitiveSet(k).getNumIndices()));
					for (int ps = 0; ps < ((Geometry) drawable)
							.getPrimitiveSet(k).getNumIndices(); ps++) {

						// Util.logger.log(Level.FINEST, "adding index");
						_content.addIndexToPrimitiveSet(primitiveSet,
								((Geometry) drawable).getPrimitiveSet(k).index(
										ps));
						// Util.logger.log(Level.FINEST, String.valueOf(t));
						// t++;
					}
					_content.endPrimitiveSetIndexArray();
					_content.endPrimitiveSet(primitiveSet);
				}

			}

			Vector<Integer> addedTextures = new Vector<Integer>();
			boolean addedMaterial = false;

			addedTextures = pushTexturesDrawable(drawable);

			// Util.logger.log(Level.FINEST, "Added textures: " +
			// addedTextures);

			try {
				addTexturesToGeometry(solid);
			} catch (Exception e) {

				e.printStackTrace();
			}

			// Util.logger.log(Level.FINEST, "getNumTextures"
			// + drawable.getOrCreateStateSet().getNumTextureStages());
			for (j = 0; j < drawable.getOrCreateStateSet()
					.getNumTextureStages(); j++) {

				try {
					if (drawable.getOrCreateStateSet().getTextureAttribute(j) != null) {
						Vector<Vec2> texCoords = ((Geometry) drawable)
								.getTexCoordArray(j);
						if (texCoords == null)
							texCoords = new Vector<Vec2>();
						Vec2 texCoord;

						_content.startSolidTexCoordArray(solid, texCoords
								.size(), j);

						for (i = 0; i < texCoords.size(); i++) {

							texCoord = texCoords.get(i);
							_content.addTextureCoordinateToSolid(solid,
									texCoord.x(), texCoord.y(), j);

						}
						_content.endSolidTexCoordArray();
					}
				} catch (TextureStageException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			addedMaterial = pushMaterialDrawable(drawable);
			try {

				Material osgMaterial = _materials.lastElement();

				float x, y, z, w;

				Vec4 vectorAmb = new Vec4();
				Vec4 vectorDif = new Vec4();
				Vec4 vectorEmi = new Vec4();
				Vec4 vectorSpe = new Vec4();

				vectorAmb = osgMaterial.getAmbient(Material.Face.FRONT);
				vectorDif = osgMaterial.getDiffuse(Material.Face.FRONT);
				vectorEmi = osgMaterial.getEmission(Material.Face.FRONT);
				vectorSpe = osgMaterial.getSpecular(Material.Face.FRONT);

				Object material = _content.startMaterial();
				x = (float) vectorAmb.x();
				y = (float) vectorAmb.y();
				z = (float) vectorAmb.z();
				w = (float) vectorAmb.w();

				_content.addAmbientToMaterial(material, x, y, z, w);

				x = (float) vectorDif.x();
				y = (float) vectorDif.y();
				z = (float) vectorDif.z();
				w = (float) vectorDif.w();

				_content.addDiffuseToMaterial(material, x, y, z, w);

				x = (float) vectorSpe.x();
				y = (float) vectorSpe.y();
				z = (float) vectorSpe.z();
				w = (float) vectorSpe.w();

				_content.addSpecularToMaterial(material, x, y, z, w);

				x = (float) vectorEmi.x();
				y = (float) vectorEmi.y();
				z = (float) vectorEmi.z();
				w = (float) vectorEmi.w();

				_content.addEmissionToMaterial(material, x, y, z, w);

				_content.addShininessToMaterial(material, osgMaterial
						.getShininess(Material.Face.FRONT));

				_content.addMaterialToSolid(solid, material);

				_content.endMaterial(material);

			} catch (ArrayIndexOutOfBoundsException ex) {

			}

			if (_blendings.lastElement())
				_content.addBlendingToSolid(solid, true);
			else
				_content.addBlendingToSolid(solid, drawable
						.getOrCreateStateSet().getEnabledBlending());

			popTextures(addedTextures);

			_content.endSolid(solid);

		}
		// Util.logger.log(Level.FINEST, "drawable parsed");
	}

	protected boolean pushTransform(Group group) {

		if (group instanceof MatrixTransform) {

			// Util.logger.log(Level.FINEST,"Push a MatrixTransform");
			_transforms.push(((MatrixTransform) group).getMatrix());

			return true;

		}

		if (group instanceof PositionAttitudeTransform) {

			// Util.logger.log(Level.FINEST,"Push a PAT");
			Matrix scaleMatrix = Matrix
					.scale(((PositionAttitudeTransform) group).getScale());

			Quat quat = new Quat((((PositionAttitudeTransform) group)
					.getAttitudeAngle()), (((PositionAttitudeTransform) group)
					.getAttitudeAxis()));
			Matrix rotateMatrix = Matrix.rotate(quat);

			Matrix transMatrix = Matrix
					.translate(((PositionAttitudeTransform) group)
							.getPosition());

			// rotateMatrix.postMult(scaleMatrix);
			// transMatrix.postMult(rotateMatrix);
			rotateMatrix.preMult(scaleMatrix);
			transMatrix.preMult(rotateMatrix);

			_transforms.push(transMatrix);

			return true;

		}

		if (group instanceof AutoTransform) {

			// Util.logger.log(Level.FINEST,"Push a AutoTransform");
			Matrix scaleMatrix = Matrix.scale(((AutoTransform) group)
					.getScale());
			Quat quat = new Quat((((AutoTransform) group).getRotationAngle()),
					(((AutoTransform) group).getRotationAxis()));
			Matrix rotateMatrix = Matrix.rotate(quat);
			Matrix transMatrix = Matrix.translate(((AutoTransform) group)
					.getPosition());

			// rotateMatrix.postMult(scaleMatrix);
			// transMatrix.postMult(rotateMatrix);

			rotateMatrix.preMult(scaleMatrix);
			transMatrix.preMult(rotateMatrix);

			_transforms.push(transMatrix);

			return true;

		}

		return false;

	}

	protected Vector<Integer> pushTextures(Node node) throws NodeException {

		int numTextures;
		numTextures = node.getOrCreateStateSet().getNumTextureStages();
		Vector<Texture2D> vector = node.getOrCreateStateSet()
				.getTextureAttributeVector();
		Vector<Integer> stages = new Vector<Integer>();

		for (int i = 0; i < numTextures; i++) {

			_textures.get(i);

			_textures.add(i, new Stack<Texture2D>());

			_textures.get(i).push(vector.get(i));
			// Util.logger.log(Level.FINEST,"PUSH");
			stages.add(i);

		}

		return stages;
	}

	protected boolean pushMaterial(Node node) throws NodeException {

		boolean hasMaterial = false;

		Material material;
		material = node.getOrCreateStateSet().getMaterial();
		_materials.push(material);
		hasMaterial = true;

		return hasMaterial;

	}

	protected Vector<Integer> pushTexturesDrawable(Drawable drawable)
			throws NodeException {

		int numTextures;
		numTextures = drawable.getOrCreateStateSet().getNumTextureStages();
		Vector<Texture2D> vector = drawable.getOrCreateStateSet()
				.getTextureAttributeVector();
		Vector<Integer> stages = new Vector<Integer>();

		// Util.logger.log(Level.FINEST, "NumTextures: " + numTextures);
		for (int i = 0; i < numTextures; i++) {

			try {

				_textures.get(i);

			} catch (ArrayIndexOutOfBoundsException ex) {

				_textures.add(i, new Stack<Texture2D>());

			}

			try {

				_textures.get(i).push(vector.get(i));
				// Util.logger.log(Level.FINEST,"PUSH");
				stages.add(i);
				Util.logger.log(Level.FINEST, "Added textures: " + stages);
			} catch (ArrayIndexOutOfBoundsException ex) {

			}
		}

		return stages;
	}

	protected boolean pushMaterialDrawable(Drawable draw) throws NodeException {

		boolean hasMaterial = false;

		try {

			Material material;
			material = draw.getOrCreateStateSet().getMaterial();
			_materials.push(material);
			hasMaterial = true;

		} catch (ArrayIndexOutOfBoundsException ex) {

			hasMaterial = false;

		} catch (NullPointerException e) {

		}

		return hasMaterial;

	}

	protected void popTextures(Vector<Integer> stages) {

		for (int i = 0; i < stages.size(); i++) {

			// Util.logger.log(Level.FINEST,"POP");
			_textures.get(stages.get(i)).pop();

		}

	}

	protected void addTexturesToGeometry(Object solid) throws Exception {

		BufferedImage image;
		for (int i = 0; i < _textures.size(); i++) {

			try {
				// Util.logger.log(Level.FINEST, "Num textures in stack: "
				// + _textures.get(i).size());
				_textures.get(i).lastElement();
				try {
					// Util.logger.log(Level.FINEST, "TRYING STAGE: " + i);
					image = _textures.get(i).lastElement().getImage()
							.getBufferedImage();
					_content.addTextureToSolid(solid, i, image);

				} catch (ArrayIndexOutOfBoundsException ex) {

				} catch (ImageConversionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ImageException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			catch (NoSuchElementException ex) {

			}
		}

	}

	@Override
	public String getFormat() {
		// TODO Auto-generated method stub
		return null;
	}

}
