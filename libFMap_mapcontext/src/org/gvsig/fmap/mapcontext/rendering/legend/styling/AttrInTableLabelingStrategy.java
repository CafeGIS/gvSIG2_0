package org.gvsig.fmap.mapcontext.rendering.legend.styling;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.operation.CreateLabels;
import org.gvsig.fmap.geom.operation.CreateLabelsOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.utils.FLabel;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.ContainsEnvelopeEvaluator;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontext.rendering.legend.NullValue;
import org.gvsig.fmap.mapcontext.rendering.symbols.CartographicSupport;
import org.gvsig.fmap.mapcontext.rendering.symbols.CartographicSupportToolkit;
import org.gvsig.fmap.mapcontext.rendering.symbols.SimpleTextSymbol;
import org.gvsig.tools.exception.BaseException;
import org.gvsig.tools.task.Cancellable;
import org.slf4j.LoggerFactory;

import com.iver.utiles.StringUtilities;
import com.iver.utiles.XMLEntity;


/**
 * LabelingStrategy used when the user wants to use label sizes, rotations, etc. from
 * the values included in fields of the datasource's table
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
public class AttrInTableLabelingStrategy implements ILabelingStrategy, CartographicSupport {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GeometryManager.class);
	public static final double MIN_TEXT_SIZE = 3;
	private ILabelingMethod method = new DefaultLabelingMethod();
	private IZoomConstraints zoom;
	private FLyrVect layer;
//	private double unitFactor = 1D;
	private double fixedSize=10;
	private Color fixedColor;
	private int unit = -1; //(pixel)
	private boolean useFixedSize;
	private boolean useFixedColor;
	private int referenceSystem;
	private boolean isPrinting;
	private double  printDPI;
	private String[] usedFields = null;
	private Font font;
	private Color colorFont;
	private String textFieldName;
	private String rotationFieldName;
	private String heightFieldName;
	private String colorFieldName;
	private PrintAttributes properties;

	public ILabelingMethod getLabelingMethod() {
		return this.method;
	}

	public void setLabelingMethod(ILabelingMethod method) {
		this.method = method;
	}

	public IPlacementConstraints getPlacementConstraints() {
		return null; // (automatically handled by the driver)
	}

	public void setPlacementConstraints(IPlacementConstraints constraints) {
		// nothing
	}

	public IZoomConstraints getZoomConstraints() {
		return zoom;
	}

	public void setZoomConstraints(IZoomConstraints constraints) {
		this.zoom = constraints;
	}

	public void draw(BufferedImage image, Graphics2D g, ViewPort viewPort, Cancellable cancel, double dpi)
	throws ReadException {
		double scale = viewPort.getScale();
//		double fontScaleFactor = FConstant.FONT_HEIGHT_SCALE_FACTOR;

		SimpleTextSymbol sym = new SimpleTextSymbol();

		sym.setFont(getFont());

		sym.setUnit(unit);
		sym.setReferenceSystem(referenceSystem);
		if (zoom==null ||
			( zoom.isUserDefined() && (scale >= zoom.getMaxScale())
			&& (scale <= zoom.getMinScale()) ) ) {
			FeatureSet set = null;
			DisposableIterator iterator = null;
			try {
				// limit the labeling to the visible extent
				ArrayList fields = new ArrayList();
				int heightPos =-1;
				int rotationPos =-1;
				int textPos = -1;
				int colorPos = -1;
				int geomPos = -1;

				if (!this.usesFixedSize()) {
					if (getHeightField() != null) {
						heightPos = fields.size();
						fields.add(getHeightField());
					}
				}
				if (getRotationField() != null) {
					rotationPos = fields.size();
					fields.add(getRotationField());
				}
				if (getTextField() != null) {
					textPos = fields.size();
					fields.add(getTextField());
				}

				if (!this.usesFixedColor() && getColorField() != null) {
					colorPos = fields.size();
					fields.add(getColorField());
				}

				FeatureStore featureStore=layer.getFeatureStore();

				geomPos = fields.size();
				String geomName=featureStore.getDefaultFeatureType().getDefaultGeometryAttributeName();
				fields.add(geomName);

				FeatureQuery featureQuery=featureStore.createFeatureQuery();

				featureQuery.setAttributeNames((String[]) fields.toArray(new String[fields.size()]));
				// TODO no set filter y layer is contained totaly in viewPort
				ContainsEnvelopeEvaluator iee=new ContainsEnvelopeEvaluator(viewPort.getAdjustedExtent(),viewPort.getProjection(),featureStore.getDefaultFeatureType(),geomName);
				featureQuery.setFilter(iee);



				set = featureStore
						.getFeatureSet(featureQuery);

//				ReadableVectorial source = layer.getSource();
//				SelectableDataSource recordSet = source.getRecordset();
				iterator = set.fastIterator();
				CreateLabelsOperationContext cloc=new CreateLabelsOperationContext();
				cloc.setDublicates(true);
				cloc.setPosition(0);
				while(iterator.hasNext()){
					if (cancel.isCanceled()){
						return;
					}
					Feature feature=(Feature)iterator.next();
//				for(int i=bs.nextSetBit(0); i>=0 && !cancel.isCanceled(); i=bs.nextSetBit(i+1)) {
//					Value[] vv = recordSet.getRow(i);
					double size;
					Color color = null;
					if (useFixedSize){
						// uses fixed size
						size = fixedSize;// * fontScaleFactor;
//					} else if (idHeightField != -1) {
					} else if (heightFieldName != null) {
						// text size is defined in the table
						try {
//							Object obj=feature.get(idHeightField);
							Object obj=feature.get(heightPos);
							if (obj!=null) {
								size = ((Number) obj).doubleValue();// * fontScaleFactor;
							} else {
								size=0;
							}
						} catch (ClassCastException ccEx) {
//							if (!NullValue.class.equals(feature.get(idHeightField).getClass())) {
							if (!NullValue.class.equals(feature.get(heightPos).getClass())) {

								throw new ReadException("Unknown", ccEx);
							}
							// a null value
//							Logger.getAnonymousLogger().
//								warning("Null text height value for text '"+feature.get(idTextField).toString()+"'");
							Logger.getAnonymousLogger().
								warning("Null text height value for text '"+feature.get(textPos).toString()+"'");

							continue;
						}
					} else {
						// otherwise will use the size in the symbol
						size = sym.getFont().getSize();
					}

					size = CartographicSupportToolkit.
								getCartographicLength(this,
													  size,
													  viewPort,
													  MapContext.getScreenDPI());
//													  dpi);
//								toScreenUnitYAxis(this,
//												  size,
//												  viewPort
//												 );

					if (size <= MIN_TEXT_SIZE) {
						// label is too small to be readable, will be skipped
						// this speeds up the rendering in wider zooms
						continue;
					}

					sym.setFontSize(size);

					if (useFixedColor){
						color = fixedColor;
//					} else if (idColorField != -1) {
					} else if (colorFieldName != null) {
						// text size is defined in the table
						try {
//							color = new Color(feature.getInt(idColorField));
							color = new Color(feature.getInt(colorPos));
						} catch (ClassCastException ccEx) {
//							if (feature.get(idColorField) != null) {
							if (feature.get(colorPos) != null) {
								throw new ReadException("Unknown", ccEx);
							}
							// a null value
//							Logger.getAnonymousLogger().
//								warning(
//									"Null color value for text '"
//											+ feature.getString(idTextField)
//											+ "'");
							Logger.getAnonymousLogger().warning(
									"Null color value for text '"
											+ feature.getString(textFieldName)
											+ "'");

							continue;
						}
					} else {
						color = sym.getTextColor();
					}

					sym.setTextColor(color);

					double rotation = 0D;
//					if (idRotationField!= -1) {
					if (rotationFieldName != null) {
						// text rotation is defined in the table
//						rotation = -Math.toRadians(((Number) feature.get(idRotationField)).doubleValue());
						rotation = -Math.toRadians(((Number) feature.get(rotationPos)).doubleValue());
					}

					Geometry geom = feature.getDefaultGeometry();
//					Object obj=feature.get(idTextField);
					Object obj = feature.get(textPos);
					if (obj!=null) {
						sym.setText(obj.toString());
					}
					sym.setRotation(rotation);

					FLabel[] aux =(FLabel[])geom.invokeOperation(CreateLabels.CODE,cloc);
//					FLabel[] aux = geom.createLabels(0, true);
					for (int j = 0; j < aux.length; j++) {
						Point p = geomManager.createPoint(aux[j].getOrig().getX(), aux[j].getOrig().getY(), SUBTYPES.GEOM2D);
						p.transform(viewPort.getAffineTransform());
						if (properties==null) {
							sym.draw(g, null, p, cancel);
						} else {
							sym.print(g, null, p, properties);
						}
					}
				}

			} catch (GeometryOperationNotSupportedException e) {
				throw new ReadException(
						"Could not draw annotation in the layer.", e);
			} catch (GeometryOperationException e) {
				throw new ReadException(
						"Could not draw annotation in the layer.", e);
			} catch (BaseException e) {
				throw new ReadException(
						"Could not draw annotation in the layer.", e);
			} finally {
				if (iterator != null) {
					iterator.dispose();
				}
				if (set != null) {
					set.dispose();
				}

			}

		}
	}

	public String getClassName() {
		return getClass().getName();
	}

	public XMLEntity getXMLEntity() {
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className", getClassName());
		xml.putProperty("labelingStrategy", "labelingStrategy");

		try {
			if(getHeightField() != null) {
				xml.putProperty("HeightField", getHeightField());
			}
		} catch (DataException e) {
			Logger.getAnonymousLogger().log(Level.SEVERE, "Acessing TextHeight field.\n"+e.getMessage());
		}

		try {
			if(getColorField() != null) {
				xml.putProperty("ColorField", getColorField());
			}
		} catch (DataException e) {
			Logger.getAnonymousLogger().log(Level.SEVERE, "Acessing ColorField field.\n"+e.getMessage());
		}

		try {
			if(getTextField() != null) {
				xml.putProperty("TextField", getTextField());
			}
		} catch (DataException e) {
			Logger.getAnonymousLogger().log(Level.SEVERE, "Acessing TextField field.\n"+e.getMessage());
		}

		try {
			if (getRotationField() != null) {
				xml.putProperty("RotationField", getRotationField());
			}
		} catch (DataException e) {
			Logger.getAnonymousLogger().log(Level.SEVERE, "Acessing RotationField field.\n"+e.getMessage());
		}

		if(getFont() != null){
			xml.putProperty("fontSize", getFont().getSize());
			xml.putProperty("fontName", getFont().getName());
			xml.putProperty("fontStyle", getFont().getStyle());
		}
		if(getColorFont() != null) {
			xml.putProperty("Color", StringUtilities.color2String(getColorFont()));
		}
		xml.putProperty("useFixedSize", useFixedSize);
		xml.putProperty("useFixedColor", useFixedColor);
		xml.putProperty("fixedColor", StringUtilities.color2String(fixedColor));
		xml.putProperty("fixedSize", fixedSize);
		xml.putProperty("Unit", unit);
		xml.putProperty("referenceSystem",referenceSystem);
		return xml;

	}

	public String getRotationField() throws DataException {
		return this.rotationFieldName;
	}

	public String getTextField() throws DataException {
		return this.textFieldName;
	}

	public String getHeightField() throws DataException {
		return this.heightFieldName;
	}

	public String getColorField() throws DataException {
		return this.colorFieldName;
	}

	public void setXMLEntity(XMLEntity xml) {
		if (xml.contains("TextField" )) {
			setTextField(xml.getStringProperty("TextField"));
		}

		if (xml.contains("HeightField")) {
			setHeightField(xml.getStringProperty("HeightField"));
		}

		if (xml.contains("ColorField")) {
			setColorField(xml.getStringProperty("ColorField"));
		}

		if (xml.contains("RotationField")) {
			setRotationField(xml.getStringProperty("RotationField"));
		}

		if (xml.contains("Unit")) {
			setUnit(xml.getIntProperty("Unit"));
		}

		if (xml.contains("fontName")){
			Font font=new Font(xml.getStringProperty("fontName"),xml.getIntProperty("fontStyle"),xml.getIntProperty("fontSize"));
			setFont(font);
		}
		if (xml.contains("useFixedSize")){
			useFixedSize=xml.getBooleanProperty("useFixedSize");
			fixedSize=xml.getDoubleProperty("fixedSize");
		}
		if (xml.contains("useFixedColor")){
			useFixedColor=xml.getBooleanProperty("useFixedColor");
			fixedColor=StringUtilities.string2Color(xml.getStringProperty("fixedColor"));
		}
		if (xml.contains("referenceSystem")) {
			referenceSystem=xml.getIntProperty("referenceSystem");
		}

	}
	public void setTextField(String textFieldName) {
		this.textFieldName=textFieldName;
		this.usedFields = null;
	}

	public void setRotationField(String rotationFieldName) {
		this.rotationFieldName = rotationFieldName;
		this.usedFields = null;
	}

	/**
	 * Sets the field that contains the size of the text. The size is computed
	 * in meters. To use any other unit, call setUnit(int) with the scale factor from meters
	 * (for centimeters, call <b>setUnitFactor(0.01))</b>.
	 * @param heightFieldName
	 */
	public void setHeightField(String heightFieldName) {
		this.heightFieldName = heightFieldName;
		this.usedFields = null;
	}


	public void setColorField(String colorFieldName) {
		this.colorFieldName = colorFieldName;
	}
	public void print(Graphics2D g, ViewPort viewPort, Cancellable cancel, PrintAttributes props) throws ReadException {
		this.properties=props;
		draw(null, g, viewPort, cancel, printDPI);
		this.properties=null;
	}

	public void setUsesFixedSize(boolean b) {
		useFixedSize = b;
		this.usedFields = null;
	}

	public boolean usesFixedSize() {
		return useFixedSize;
	}

	public double getFixedSize() {
		return fixedSize;
	}

	public void setFixedSize(double fixedSize) {
		this.fixedSize = fixedSize;
		this.usedFields = null;
	}

	public void setUsesFixedColor(boolean b) {
		useFixedColor = b;
		this.usedFields = null;
	}

	public boolean usesFixedColor() {
		return useFixedColor;
	}

	public Color getFixedColor() {
		return fixedColor;
	}

	public void setFixedColor(Color fixedColor) {
		this.fixedColor = fixedColor;
	}


	public void setUnit(int unitIndex) {
		unit = unitIndex;

	}

	public int getUnit() {
		return unit;
	}

	public String[] getUsedFields() {
		if (this.usedFields == null) {
			List v = new ArrayList(4);
			try {
				if (!this.usesFixedSize()) {
					if (getHeightField() != null) {
						v.add(getHeightField());
					}
				}
				if (getRotationField() != null) {
					v.add(getRotationField());
				}
				if (getTextField() != null) {
					v.add(getTextField());
				}

				if (!this.usesFixedColor() && getColorField() != null) {
					v.add(getColorField());
				}
			} catch (DataException e) {
				Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage());
			}
			this.usedFields = (String[]) v.toArray(new String[v.size()]);
		}
		return this.usedFields;
	}

	public int getReferenceSystem() {
		return referenceSystem;
	}

	public void setReferenceSystem(int referenceSystem) {
		this.referenceSystem = referenceSystem;
	}

	public double toCartographicSize(ViewPort viewPort, double dpi, Geometry geom) {
		// not required here
		throw new Error("Undefined in this context");
	}

	public void setCartographicSize(double cartographicSize, Geometry geom) {
		// not required here
		throw new Error("Undefined in this context");
	}

	public double getCartographicSize(ViewPort viewPort, double dpi, Geometry geom) {
		// not required here
		throw new Error("Undefined in this context");

	}

	public void setLayer(FLayer layer) throws ReadException {
		this.layer = (FLyrVect) layer;
		FeatureType type;
		try {
			type = this.layer.getFeatureStore().getDefaultFeatureType();
		} catch (DataException e) {
			// FIXME Throw DataException
			throw new ReadException(this.getClass().getName(), e);
		}

		if (textFieldName != null) {
			if (type.getIndex(textFieldName) < 0) {
				// FIXME exception ??
			}
		}
		if (rotationFieldName != null) {
			if (type.getIndex(rotationFieldName) < 0) {
				// FIXME exception ??
			}
		}
		if (heightFieldName != null) {
			if (type.getIndex(heightFieldName) < 0) {
				// FIXME exception ??
			}
		}
		if (colorFieldName != null) {
			if (type.getIndex(colorFieldName) < 0) {
				// FIXME exception ??
			}
		}
	}

	public boolean shouldDrawLabels(double scale) {
		return layer.isWithinScale(scale);
	}
	public Color getColorFont() {
		return colorFont;
	}

	public void setColorFont(Color colorFont) {
		this.colorFont = colorFont;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font selFont) {
		this.font = selFont;
	}
}
