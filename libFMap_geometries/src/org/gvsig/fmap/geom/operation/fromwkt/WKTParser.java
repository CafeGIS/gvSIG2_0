package org.gvsig.fmap.geom.operation.fromwkt;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.aggregate.MultiPoint;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.Surface;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.io.ParseException;

/**
 *  Converts a Well-Known Text string to a <code>Geometry</code>.
 * <p>
 *  The <code>WKTReader</code> allows
 *  extracting <code>Geometry</code> objects from either input streams or
 *  internal strings. This allows it to function as a parser to read <code>Geometry</code>
 *  objects from text blocks embedded in other data formats (e.g. XML). <P>
 * <p>
 * The Well-known
 *  Text format is defined in the <A HREF="http://www.opengis.org/techno/specs.htm">
 *  OpenGIS Simple Features Specification for SQL</A> . <P>
 * <p>
 *  <B>Note: </B> There is an inconsistency in the SFS. The WKT grammar states
 *  that <code>MultiPoints</code> are represented by <code>MULTIPOINT ( ( x y), (x y) )</code>
 *  , but the examples show <code>MultiPoint</code>s as <code>MULTIPOINT ( x y, x y )</code>
 *  . Other implementations follow the latter syntax, so JTS will adopt it as
 *  well.
 *
 *  A <code>WKTReader</code> is parameterized by a <code>GeometryFactory</code>
 *  , to allow it to create <code>Geometry</code> objects of the appropriate
 *  implementation. In particular, the <code>GeometryFactory</code> will
 *  determine the <code>PrecisionModel</code> and <code>SRID</code> that is
 *  used. <P>
 *
 *  The <code>WKTReader</code> will convert the input numbers to the precise
 *  internal representation.
 *
 *  Reads non-standard "LINEARRING" tags.
 *
 *@version 1.5
 */
public class WKTParser {

	private GeometryManager manager = GeometryLocator.getGeometryManager();
	/**
	 * Creates a WKTReader that creates objects using a basic GeometryFactory.
	 */
	public WKTParser() {
	}



	/**
	 * Converts a Well-known Text representation to a <code>Geometry</code>.
	 * 
	 * @param wellKnownText
	 *            one or more <Geometry Tagged Text>strings (see the OpenGIS
	 *            Simple Features Specification) separated by whitespace
	 * @return a <code>Geometry</code> specified by <code>wellKnownText</code>
	 * @throws ParseException
	 *             if a parsing problem occurs
	 */
	public Geometry read(String wellKnownText) throws ParseException {
		StringReader reader = new StringReader(wellKnownText);
		try {
			return read(reader);
		}
		finally {
			reader.close();
		}
	}

	/**
	 *  Converts a Well-known Text representation to a <code>Geometry</code>.
	 *
	 *@param  reader           a Reader which will return a <Geometry Tagged Text>
	 *      string (see the OpenGIS Simple Features Specification)
	 *@return                  a <code>Geometry</code> read from <code>reader</code>
	 *@throws  ParseException  if a parsing problem occurs
	 */
	public Geometry read(Reader reader) throws ParseException {
		StreamTokenizer tokenizer = new StreamTokenizer(reader);
		try {
			return readGeometryTaggedText(tokenizer);
		}
		catch (IOException e) {
			throw new ParseException(e.toString());
		} catch (CreateGeometryException e) {
			throw new ParseException(e.toString());
		}
	}

	/**
	 *  Returns the next array of <code>Coordinate</code>s in the stream.
	 *
	 *@param  tokenizer        tokenizer over a stream of text in Well-known Text
	 *      format. The next element returned by the stream should be "(" (the
	 *      beginning of "(x1 y1, x2 y2, ..., xn yn)") or "EMPTY".
	 *@return                  the next array of <code>Coordinate</code>s in the
	 *      stream, or an empty array if "EMPTY" is the next element returned by
	 *      the stream.
	 *@throws  IOException     if an I/O error occurs
	 *@throws  ParseException  if an unexpected token was encountered
	 */
	private Coordinate[] getCoordinates(StreamTokenizer tokenizer)
	throws IOException, ParseException
	{
		String nextToken = getNextEmptyOrOpener(tokenizer);
		if (nextToken.equals("EMPTY")) {
			return new Coordinate[]{};
		}
		ArrayList coordinates = new ArrayList();
		coordinates.add(getPreciseCoordinate(tokenizer));
		nextToken = getNextCloserOrComma(tokenizer);
		while (nextToken.equals(",")) {
			coordinates.add(getPreciseCoordinate(tokenizer));
			nextToken = getNextCloserOrComma(tokenizer);
		}
		Coordinate[] array = new Coordinate[coordinates.size()];
		return (Coordinate[]) coordinates.toArray(array);
	}

	private Coordinate getPreciseCoordinate(StreamTokenizer tokenizer)
	throws IOException, ParseException
	{
		Coordinate coord = new Coordinate();
		coord.x = getNextNumber(tokenizer);
		coord.y = getNextNumber(tokenizer);
		if (isNumberNext(tokenizer)) {
			coord.z = getNextNumber(tokenizer);
		}
		return coord;
	}
	private boolean isNumberNext(StreamTokenizer tokenizer) throws IOException {
		try {
			return tokenizer.nextToken() == StreamTokenizer.TT_NUMBER;
		}
		finally {
			tokenizer.pushBack();
		}
	}
	/**
	 *  Returns the next number in the stream.
	 *
	 *@param  tokenizer        tokenizer over a stream of text in Well-known Text
	 *      format. The next token must be a number.
	 *@return                  the next number in the stream
	 *@throws  ParseException  if the next token is not a number
	 *@throws  IOException     if an I/O error occurs
	 */
	private double getNextNumber(StreamTokenizer tokenizer) throws IOException,
	ParseException {
		int type = tokenizer.nextToken();
		switch (type) {
		case StreamTokenizer.TT_EOF:
			throw new ParseException("Expected number but encountered end of stream");
		case StreamTokenizer.TT_EOL:
			throw new ParseException("Expected number but encountered end of line");
		case StreamTokenizer.TT_NUMBER:
			return tokenizer.nval;
		case StreamTokenizer.TT_WORD:
			throw new ParseException("Expected number but encountered word: " +
					tokenizer.sval);
		case '(':
			throw new ParseException("Expected number but encountered '('");
		case ')':
			throw new ParseException("Expected number but encountered ')'");
		case ',':
			throw new ParseException("Expected number but encountered ','");
		}
		return 0;
	}

	/**
	 *  Returns the next "EMPTY" or "(" in the stream as uppercase text.
	 *
	 *@param  tokenizer        tokenizer over a stream of text in Well-known Text
	 *      format. The next token must be "EMPTY" or "(".
	 *@return                  the next "EMPTY" or "(" in the stream as uppercase
	 *      text.
	 *@throws  ParseException  if the next token is not "EMPTY" or "("
	 *@throws  IOException     if an I/O error occurs
	 */
	private String getNextEmptyOrOpener(StreamTokenizer tokenizer) throws IOException, ParseException {
		String nextWord = getNextWord(tokenizer);
		if (nextWord.equals("EMPTY") || nextWord.equals("(")) {
			return nextWord;
		}
		throw new ParseException("Expected 'EMPTY' or '(' but encountered '" +
				nextWord + "'");
	}

	/**
	 *  Returns the next ")" or "," in the stream.
	 *
	 *@param  tokenizer        tokenizer over a stream of text in Well-known Text
	 *      format. The next token must be ")" or ",".
	 *@return                  the next ")" or "," in the stream
	 *@throws  ParseException  if the next token is not ")" or ","
	 *@throws  IOException     if an I/O error occurs
	 */
	private String getNextCloserOrComma(StreamTokenizer tokenizer) throws IOException, ParseException {
		String nextWord = getNextWord(tokenizer);
		if (nextWord.equals(",") || nextWord.equals(")")) {
			return nextWord;
		}
		throw new ParseException("Expected ')' or ',' but encountered '" + nextWord
				+ "'");
	}

	/**
	 *  Returns the next ")" in the stream.
	 *
	 *@param  tokenizer        tokenizer over a stream of text in Well-known Text
	 *      format. The next token must be ")".
	 *@return                  the next ")" in the stream
	 *@throws  ParseException  if the next token is not ")"
	 *@throws  IOException     if an I/O error occurs
	 */
	private String getNextCloser(StreamTokenizer tokenizer) throws IOException, ParseException {
		String nextWord = getNextWord(tokenizer);
		if (nextWord.equals(")")) {
			return nextWord;
		}
		throw new ParseException("Expected ')' but encountered '" + nextWord + "'");
	}

	/**
	 *  Returns the next word in the stream as uppercase text.
	 *
	 *@param  tokenizer        tokenizer over a stream of text in Well-known Text
	 *      format. The next token must be a word.
	 *@return                  the next word in the stream as uppercase text
	 *@throws  ParseException  if the next token is not a word
	 *@throws  IOException     if an I/O error occurs
	 */
	private String getNextWord(StreamTokenizer tokenizer) throws IOException, ParseException {
		int type = tokenizer.nextToken();
		switch (type) {
		case StreamTokenizer.TT_EOF:
			throw new ParseException("Expected word but encountered end of stream");
		case StreamTokenizer.TT_EOL:
			throw new ParseException("Expected word but encountered end of line");
		case StreamTokenizer.TT_NUMBER:
			throw new ParseException("Expected word but encountered number: " +
					tokenizer.nval);
		case StreamTokenizer.TT_WORD:
			return tokenizer.sval.toUpperCase();
		case '(':
			return "(";
		case ')':
			return ")";
		case ',':
			return ",";
		}
		// Assert.shouldNeverReachHere("Encountered unexpected StreamTokenizer type: " + type);
		return null;
	}

	/**
	 *  Creates a <code>Geometry</code> using the next token in the stream.
	 *
	 *@param  tokenizer        tokenizer over a stream of text in Well-known Text
	 *      format. The next tokens must form a &lt;Geometry Tagged Text&gt;.
	 *@return                  a <code>Geometry</code> specified by the next token
	 *      in the stream
	 *@throws  ParseException  if the coordinates used to create a <code>Polygon</code>
	 *      shell and holes do not form closed linestrings, or if an unexpected
	 *      token was encountered
	 *@throws  IOException     if an I/O error occurs
	 * @throws CreateGeometryException 
	 */
	private Geometry readGeometryTaggedText(StreamTokenizer tokenizer) throws IOException, ParseException, CreateGeometryException{
		String type = getNextWord(tokenizer);
		if (type.equals("POINT")) {
			return readPointText(tokenizer);
		}
		else if (type.equals("LINESTRING")) {
			return readLineStringText(tokenizer);
		}
		else if (type.equals("LINEARRING")) {
			return readLinearRingText(tokenizer);
		}
		else if (type.equals("POLYGON")) {
			return readPolygonText(tokenizer);
		}
		else if (type.equals("MULTIPOINT")) {
			return readMultiPointText(tokenizer);
		}
		else if (type.equals("MULTILINESTRING")) {
			return readMultiLineStringText(tokenizer);
		}
		else if (type.equals("MULTIPOLYGON")) {
			return readMultiPolygonText(tokenizer);
		}
		/* else if (type.equals("GEOMETRYCOLLECTION")) {
      return readGeometryCollectionText(tokenizer);
    } */
		System.err.println("Unknown type: " + type);
		throw new ParseException("Unknown type: " + type);
	}

	/**
	 *  Creates a <code>Point</code> using the next token in the stream.
	 *
	 *@param  tokenizer        tokenizer over a stream of text in Well-known Text
	 *      format. The next tokens must form a &lt;Point Text&gt;.
	 *@return                  a <code>Point</code> specified by the next token in
	 *      the stream
	 *@throws  IOException     if an I/O error occurs
	 *@throws  ParseException  if an unexpected token was encountered
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws CreateGeometryException 
	 */
	private Geometry readPointText(StreamTokenizer tokenizer) throws IOException, ParseException, CreateGeometryException {
		String nextToken = getNextEmptyOrOpener(tokenizer);
		if (nextToken.equals("EMPTY")) {
			return null;
		}
		Coordinate c = getPreciseCoordinate(tokenizer);
		Point point = (Point)manager.create(TYPES.POINT, SUBTYPES.GEOM2D);
		point.setX(c.x);
		point.setY(c.y);
		getNextCloser(tokenizer);

		return point;
	}

	/**
	 *  Creates a <code>LineString</code> using the next token in the stream.
	 *
	 *@param  tokenizer        tokenizer over a stream of text in Well-known Text
	 *      format. The next tokens must form a &lt;LineString Text&gt;.
	 *@return                  a <code>LineString</code> specified by the next
	 *      token in the stream
	 *@throws  IOException     if an I/O error occurs
	 *@throws  ParseException  if an unexpected token was encountered
	 * @throws CreateGeometryException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private Geometry readLineStringText(StreamTokenizer tokenizer) throws IOException, ParseException, CreateGeometryException {
		Coordinate[] arrayC = getCoordinates(tokenizer);
		GeneralPathX gp = new GeneralPathX();
		gp.moveTo(arrayC[0].x,arrayC[0].y);
		for (int i=1;i < arrayC.length; i++)
		{
			gp.lineTo(arrayC[i].x, arrayC[i].y);
		}
		Surface surface = (Surface)manager.create(TYPES.SURFACE, SUBTYPES.GEOM2D);
		surface.setGeneralPath(gp);
		return surface;
	}

	/**
	 *  Creates a <code>LinearRing</code> using the next token in the stream.
	 *
	 *@param  tokenizer        tokenizer over a stream of text in Well-known Text
	 *      format. The next tokens must form a &lt;LineString Text&gt;.
	 *@return                  a <code>LinearRing</code> specified by the next
	 *      token in the stream
	 *@throws  IOException     if an I/O error occurs
	 *@throws  ParseException  if the coordinates used to create the <code>LinearRing</code>
	 *      do not form a closed linestring, or if an unexpected token was
	 *      encountered
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws CreateGeometryException 
	 */
	private Geometry readLinearRingText(StreamTokenizer tokenizer)
	throws IOException, ParseException, CreateGeometryException
	{
		Coordinate[] arrayC = getCoordinates(tokenizer);
		GeneralPathX gp = new GeneralPathX();
		gp.moveTo(arrayC[0].x, arrayC[0].y);
		for (int i=1;i < arrayC.length; i++)
		{
			gp.lineTo(arrayC[i].x, arrayC[i].y);
		}
		Surface surface = (Surface)manager.create(TYPES.SURFACE, SUBTYPES.GEOM2D);
		surface.setGeneralPath(gp);
		return surface;
	}

	/**
	 *  Creates a <code>MultiPoint</code> using the next token in the stream.
	 *
	 *@param  tokenizer        tokenizer over a stream of text in Well-known Text
	 *      format. The next tokens must form a &lt;MultiPoint Text&gt;.
	 *@return                  a <code>MultiPoint</code> specified by the next
	 *      token in the stream
	 *@throws  IOException     if an I/O error occurs
	 *@throws  ParseException  if an unexpected token was encountered
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws CreateGeometryException 
	 */
	private Geometry readMultiPointText(StreamTokenizer tokenizer) throws IOException, ParseException, CreateGeometryException {
		Coordinate[] coords = getCoordinates(tokenizer);
		MultiPoint multiPoint = (MultiPoint)manager.create(TYPES.MULTIPOINT, SUBTYPES.GEOM2D);
		for (int i=0; i < coords.length; i++)
		{
			Point point = (Point)manager.create(TYPES.POINT, SUBTYPES.GEOM2D);
			point.setX(coords[i].x);
			point.setY(coords[i].y);
			multiPoint.addPoint(point);
		}   

		return multiPoint;
	}


	/**
	 *  Creates a <code>Polygon</code> using the next token in the stream.
	 *
	 *@param  tokenizer        tokenizer over a stream of text in Well-known Text
	 *      format. The next tokens must form a &lt;Polygon Text&gt;.
	 *@return                  a <code>Polygon</code> specified by the next token
	 *      in the stream
	 *@throws  ParseException  if the coordinates used to create the <code>Polygon</code>
	 *      shell and holes do not form closed linestrings, or if an unexpected
	 *      token was encountered.
	 *@throws  IOException     if an I/O error occurs
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws CreateGeometryException 
	 */
	private Geometry readPolygonText(StreamTokenizer tokenizer) throws IOException, ParseException, CreateGeometryException {
		String nextToken = getNextEmptyOrOpener(tokenizer);
		if (nextToken.equals("EMPTY")) {
			return null;
		}
		ArrayList holes = new ArrayList();
		Geometry shell = readLinearRingText(tokenizer);
		nextToken = getNextCloserOrComma(tokenizer);
		while (nextToken.equals(",")) {
			Geometry hole = readLinearRingText(tokenizer);
			holes.add(hole);
			nextToken = getNextCloserOrComma(tokenizer);
		}
		// LinearRing[] array = new LinearRing[holes.size()];
		return shell; //geometryFactory.createPolygon(shell, (LinearRing[]) holes.toArray(array));
	}

	/**
	 *  Creates a <code>MultiLineString</code> using the next token in the stream.
	 *
	 *@param  tokenizer        tokenizer over a stream of text in Well-known Text
	 *      format. The next tokens must form a &lt;MultiLineString Text&gt;.
	 *@return                  a <code>MultiLineString</code> specified by the
	 *      next token in the stream
	 *@throws  IOException     if an I/O error occurs
	 *@throws  ParseException  if an unexpected token was encountered
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws CreateGeometryException 
	 */
	private Geometry readMultiLineStringText(StreamTokenizer tokenizer) throws IOException, ParseException, CreateGeometryException {
		// TODO: HACER ESTO BIEN, CON UN GENERAL PATH
		String nextToken = getNextEmptyOrOpener(tokenizer);
		if (nextToken.equals("EMPTY")) {
			return null;
		}
		ArrayList lineStrings = new ArrayList();
		Geometry lineString = readLineStringText(tokenizer);
		lineStrings.add(lineString);
		nextToken = getNextCloserOrComma(tokenizer);
		while (nextToken.equals(",")) {
			lineString = readLineStringText(tokenizer);
			lineStrings.add(lineString);
			nextToken = getNextCloserOrComma(tokenizer);
		} 
		// LineString[] array = new LineString[lineStrings.size()];
		return lineString; // geometryFactory.createMultiLineString((LineString[]) lineStrings.toArray(array));
	}

	/**
	 *  Creates a <code>MultiPolygon</code> using the next token in the stream.
	 *
	 *@param  tokenizer        tokenizer over a stream of text in Well-known Text
	 *      format. The next tokens must form a &lt;MultiPolygon Text&gt;.
	 *@return                  a <code>MultiPolygon</code> specified by the next
	 *      token in the stream, or if if the coordinates used to create the
	 *      <code>Polygon</code> shells and holes do not form closed linestrings.
	 *@throws  IOException     if an I/O error occurs
	 *@throws  ParseException  if an unexpected token was encountered
	 * @throws CreateGeometryException 
	 */
	// TODO:
	private Geometry readMultiPolygonText(StreamTokenizer tokenizer) throws IOException, ParseException, CreateGeometryException {
		String nextToken = getNextEmptyOrOpener(tokenizer);
		if (nextToken.equals("EMPTY")) {
			return null;
		}
		ArrayList polygons = new ArrayList();
		Geometry polygon = readPolygonText(tokenizer);
		/* polygons.add(polygon);
    nextToken = getNextCloserOrComma(tokenizer);
    while (nextToken.equals(",")) {
      polygon = readPolygonText(tokenizer);
      polygons.add(polygon);
      nextToken = getNextCloserOrComma(tokenizer);
    } */
		// Polygon[] array = new Polygon[polygons.size()];
		return polygon; //geometryFactory.createMultiPolygon((Polygon[]) polygons.toArray(array));
	} 

	/**
	 *  Creates a <code>GeometryCollection</code> using the next token in the
	 *  stream.
	 *
	 *@param  tokenizer        tokenizer over a stream of text in Well-known Text
	 *      format. The next tokens must form a &lt;GeometryCollection Text&gt;.
	 *@return                  a <code>GeometryCollection</code> specified by the
	 *      next token in the stream
	 *@throws  ParseException  if the coordinates used to create a <code>Polygon</code>
	 *      shell and holes do not form closed linestrings, or if an unexpected
	 *      token was encountered
	 *@throws  IOException     if an I/O error occurs
	 */
	// TODO:
	/* private GeometryCollection readGeometryCollectionText(StreamTokenizer tokenizer) throws IOException, ParseException {
    String nextToken = getNextEmptyOrOpener(tokenizer);
    if (nextToken.equals("EMPTY")) {
      return geometryFactory.createGeometryCollection(new Geometry[]{});
    }
    ArrayList geometries = new ArrayList();
    Geometry geometry = readGeometryTaggedText(tokenizer);
    geometries.add(geometry);
    nextToken = getNextCloserOrComma(tokenizer);
    while (nextToken.equals(",")) {
      geometry = readGeometryTaggedText(tokenizer);
      geometries.add(geometry);
      nextToken = getNextCloserOrComma(tokenizer);
    }
    Geometry[] array = new Geometry[geometries.size()];
    return geometryFactory.createGeometryCollection((Geometry[]) geometries.toArray(array));
  } */
}

