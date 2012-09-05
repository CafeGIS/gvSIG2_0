package org.gvsig.gpe.parser;

import java.io.IOException;

/**
 * An iterator over the current geometry being parsed intended to be given to the consumer
 * {@link IGPEContentHandler}.
 * <p>
 * This provides an abstraction for specific format parsers to optimize as needed without imposing a
 * coordinate structure to the consumer application which may be inefficient for its geometry model.
 * </p>
 * <p>
 * Basically, using this interface allows for the following optimizations:
 * <ul>
 * <li>Avoid unneeded coordinate parsing where not necessary
 * <li>Allows to serve geometry sequences of arbitrary dimensions
 * <li>Avoids unnecessary copy of large coordinate buffers from different arrays of doubles,
 * letting the consumer iterate over the coordinates and build its geometries more efficiently
 * </ul>
 * </p>
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id: ICoordinateIterator.java 19589 2008-03-12 14:14:54Z groldan $
 */
public interface ICoordinateIterator {

    /**
     * Returns whether there are more coordinates in this sequence
     * 
     * @return
     * @throws IOException
     */
    public boolean hasNext() throws IOException;

    /**
     * Returns the dimension of the coordinate tuple being served. Consumer code should use it to
     * pass a buffer with the correct lenght to {@link #next(double[])}
     * 
     * @return
     */
    public int getDimension();

    /**
     * @param buffer where to store the parsed coordinates. The buffer lenght shall be >=
     *            {@link #getDimension()}
     * @throws IOException
     */
    public void next(double[] buffer) throws IOException;
}
