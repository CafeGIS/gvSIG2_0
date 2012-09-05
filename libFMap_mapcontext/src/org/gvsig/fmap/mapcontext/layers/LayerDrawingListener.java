package org.gvsig.fmap.mapcontext.layers;


/**
 * <p><code>LayerDrawingListener</code> defines listeners
 *  to catch and handle drawing events from layers.</p>
 */
public interface LayerDrawingListener {
    /**
 	 * <p>Called before a layer of type {@link FLayer FLayer} or {@link FLayers FLayers} is drawn.</p>
 	 * 
     * @param e a layer event object
     * 
     * @throws CancelationException if cancels the operation, this exception will have the message
     *  that user will see.
     * 
     * @see LayerDrawEvent
     */
    void beforeLayerDraw(LayerDrawEvent e) throws CancelationException;
    
    /**
     * <p>Process to execute after a layer had been drawn.</p>
     * 
     * @param e a layer event object
     * 
     * @throws CancelationException if cancels the operation, this exception will have the message
     *  that user will see.
     * 
     * @see LayerDrawEvent
     */
    void afterLayerDraw(LayerDrawEvent e) throws CancelationException;
    
    /**
     * <p>Process to execute before a {@link GraphicLayer GraphicLayer} had been drawn.</p>
     *
     * @param e a layer event object
     * 
     * @throws CancelationException if cancels the operation, this exception will have the message
     *  that user will see.
     * 
     * @see LayerDrawEvent
     */
    void beforeGraphicLayerDraw(LayerDrawEvent e) throws CancelationException;

    /**
     * <p>Process to execute after a {@link GraphicLayer GraphicLayer} had been drawn.</p>
     * 
     * @param e a layer event object
     * 
     * @throws CancelationException if cancels the operation, this exception will have the message
     *  that user will see.
     * 
     * @see LayerDrawEvent
     */
    void afterLayerGraphicDraw(LayerDrawEvent e) throws CancelationException;
}
