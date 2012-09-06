package org.gvsig.remoteClient.wms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Vector;

import org.gvsig.remoteClient.utils.BoundaryBox;
import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * <p>Abstract class that defines an WMSLayer.</p>
 *
 */
public abstract class WMSLayer implements org.gvsig.remoteClient.ILayer {

    protected ArrayList children;
    protected WMSLayer parent;

    /**
     * <p>Layer Abstract field in the capabilities document </p>
     */
    private String layerAbstract;

    /**
     * <p>Themes provided by the WMS for the layer</p>
     */
    public ArrayList styles = new ArrayList();

    /**
     * <p>Layer name</p>
     */
    private String name;

    /**
     * <p>Layer title</p>
     */
    private String title;

    private ArrayList keywordList = new ArrayList();
    /**
     * <p>Layer srs.</p>
     */
    protected Vector srs = new Vector();

    /**
     * <p>extents for each srs the layer can be reproyected to</p>
     */
    private Hashtable bBoxes  = new Hashtable();

    /**
     * <p>extents that defines the bbox for the LatLon projection
     * It can be included in the bBoxes vector as well, because it is the most used, we keep it separeted too, according
     *  with the OGC WMSCapabilities specifications...
     */
    private org.gvsig.remoteClient.utils.BoundaryBox latLonBbox;

    /**
     * <p>min scale for the layer to be visible</p>
     */
    private double scaleMin;

    /**
     * <p>max scale for the layer to be visible</p>
     */
    private double scaleMax;

    /**
     * <p>Dimensions defined for the layer in the capabilities doc</p>
     */
    protected java.util.ArrayList dimensions = new ArrayList();

    /**
     * Tells if this layer accepts getFeatureInfo requests.
     */
    private boolean queryable = false;

    /**
     * Tells if this layer is opaque.
     */
    private boolean opaque = false;
    /**
     * when set to true, noSubsets indicates that the server is not able to make a map
     * of a geographic area other than the layer's bounding box.
     */
    private boolean m_noSubSets = false;

    /**
     * when present and non-zero fixedWidth and fixedHeight indicate that the server is not
     * able to produce a map of the layer at a width and height different from the fixed sizes indicated.
     */
    private int fixedWidth = 0;
    private int fixedHeight = 0;

    /**
     * Tells if this layer can be served with transparency.
     */
    private boolean transparency;

    /**
     * <p>Parses the LAYER tag in the WMS capabilities, filling the WMSLayer object
     * loading the data in memory to be easily accesed</p>
     *
     */
    public abstract void parse(KXmlParser parser, TreeMap layerTreeMap)
    throws IOException, XmlPullParserException;

    //public abstract ArrayList getAllDimensions();

    /**
     * add a new keyword to the keywordList.
     * @param key
     */
    protected void addkeyword(String key)
    {
    	keywordList.add(key);
    }
    public ArrayList getKeywords()
    {
    	return keywordList;
    }
    /**
     * <p>Adds a style to the styles vector</p>
     * @param _style
     */
    public void addStyle(org.gvsig.remoteClient.wms.WMSStyle _style) {
        styles.add( _style );    }

   /**
     * <p>Gets the style vector</p>
     * @return
     */
    public ArrayList getStyles() {
    	ArrayList list = new ArrayList();
    	if (styles != null)
    		list.addAll(styles);
    	if (this.getParent()!= null)
    	{
    		//return getAllStyles(this);
    		if(this.getParent().getStyles() != null)
    			list.addAll(this.getParent().getStyles());
    	}
        return list;
    }

    public ArrayList getAllStyles(WMSLayer layer)
    {
    	if (layer.getParent()!= null)
    	{
    		ArrayList list = getAllStyles(layer.getParent());
    		for(int i = 0; i < this.styles.size(); i++)
    		{
    			list.add(styles.get(i));
    		}
    		return list;
    	}
    	else
    	{
    		return styles;
    	}
    }
    /**
     * <p>Adds a bbox to the Bboxes vector</p>
     * @param bbox
     */
    public void addBBox(BoundaryBox bbox) {
        bBoxes.put(bbox.getSrs(), bbox);
    }

    /**
     * <p>returns the bbox with that id in the Bboxes vector</p>
     * @param id
     */
    public BoundaryBox getBbox(String id) {
    	if ((id.compareToIgnoreCase( CapabilitiesTags.EPSG_4326 )==0)
    		||(id.compareToIgnoreCase( CapabilitiesTags.CRS_84)==0))
    	{
    		if (latLonBbox != null)
    		return (BoundaryBox)latLonBbox;
    	}
        BoundaryBox b = (BoundaryBox) bBoxes.get(id);
        if (b == null && parent!=null)
            return parent.getBbox(id);
        return (BoundaryBox)bBoxes.get(id);
    }

    /**
     * <p>Gets the bBoxes vector</p>
     * @return
     */
    public Hashtable getBboxes() {
        return bBoxes;
    }


    //Methods to manipulate the box that defines the layer extent in LatLon SRS.
    public BoundaryBox getLatLonBox()
    {
        return latLonBbox;
    }
    public void setLatLonBox(BoundaryBox box)
    {
        latLonBbox = box;
    }
    /**
     * <p>adds a new srs to the srs vector</p>
     */
    public void addSrs(String srs)
    {
    	if (!this.srs.contains(srs))
    		this.srs.add(srs);
    }

    public Vector getAllSrs()
    {
        Vector mySRSs = (Vector) this.srs.clone();
        if (parent!=null)
            mySRSs.addAll(parent.getAllSrs());
        return mySRSs;

//    	if (this.getParent()!= null)
//    	{
//    		Vector list = this.getParent().getAllSrs();
//    		for(int i = 0; i < this.srs.size(); i++)
//    		{
//    			list.add(srs.get(i));
//    		}
//    		return list;
//    	}
//    	else
//    	{
//    		return srs;
//    	}

    }
    /**
     * <p>gets the maximum scale for this layer</p>
     * @return
     */
    public double getScaleMax() {
        return scaleMax;
    }

    /**
     * <p>gets the minimum scale for this layer</p>
     * @return
     */
    public double getScaleMin() {
        return scaleMin;
    }

    /**
     * <p>sets the minimum scale for this layer to be visible.</p>
     *
     * @param scale
     */
    public void setScaleMin(double scale) {
        scaleMin = scale;
    }

    /**
     * <p>sets the maximum scale for this layer to be visible</p>
     * @param scale
     */
    public void setScaleMax(double scale) {
        scaleMax = scale;
    }

    /**
     * <p> gets the dimension vector defined in this layer</p>
     * @return
     */
    public abstract ArrayList getDimensions();
//    public ArrayList getDimensions() {
//        return dimensions;
//    }

    public WMSDimension getDimension(String name)
    {
    	for(int i = 0; i < dimensions.size(); i++ ){
    		if(((WMSDimension)dimensions.get(i)).getName().compareTo(name)==0)
    		{
    			return (WMSDimension)dimensions.get(i);
    		}
    	}
    	return null;
    }

//    /**
//     * <p>Sets the dimension vector defined for this layer</p>
//     * @param v
//     */
//    public void setDimensions(ArrayList v) {
//        dimensions = (ArrayList)v.clone();
//    }

    /**
     * <p>Adds a dimension to the dimension vector </p>
     * @param dimension
     */
    public void addDimension(org.gvsig.remoteClient.wms.WMSDimension dimension) {
        dimensions.add(dimension);
    }

    /**
     * <p>Gets layer name</p>
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * <p>Sets layer name</p>
     * @param _name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Gets layer title</p>
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * <p>Sets the layer title</p>
     * @param _title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * <p>Gets the layer abstract</p>
     * @return
     */
    public String getAbstract() {
        return layerAbstract;
    }

    /**
     * <p>Sets the layer abstract</p>
     * @param m_abstract
     */
    public void setAbstract(String _abstract) {
        layerAbstract = _abstract;
    }


    public ArrayList getChildren() {
        return children;
    }


    public void setChildren(ArrayList children) {
        this.children = children;
    }


    public WMSLayer getParent() {
        return parent;
    }


    public void setParent(WMSLayer parent) {
        this.parent = parent;
    }

    public String toString(){
        return this.getTitle();
    }


    /**
     * Tells if this layer accepts getFeatureInfo requests.
     */
    public boolean isQueryable() {
        return queryable;
    }


    /**
     * @param queryable The queryable to set.
     */
    public void setQueryable(boolean queryable) {
        this.queryable = queryable;
    }

    /**
     * Tells if this layer is opaque.
     */
    public boolean isOpaque() {
        return opaque;
    }
    /**
     * @param opaque.
     */
    public void setOpaque(boolean opaque) {
        this.opaque = opaque;
    }

    /**
     * Tells if this layer is subsettable
     */
    public boolean noSubSets() {
        return this.m_noSubSets;
    }
    /**
     * @param set layer nosubsets attribute.
     */
    public void setNoSubSets(boolean _noSubSets) {
        m_noSubSets = _noSubSets;
    }

    public void setfixedWidth(int w) {
        fixedWidth = w;
    }

    public int getfixedWidth() {
        return fixedWidth;
    }

    public void setfixedHeight(int h) {
        fixedHeight = h;
    }

    public int getfixedHeight() {
        return fixedHeight;
    }

    /**
     * @return <b>true</b> if this layer can be served with transparency, otherwise <b>false</b>
     */
    public boolean hasTransparency() {
        return transparency;
    }

    //Methods to parse tags that are common to several versions of WMS.
    //In case there is a version which has different implemantation of one of this tags
    // the subclass can overwrite this method

    /**
     * Parses the keywordlist from the capabilities and fills this list in the WMSLayer.
     * @param parser
     */
    protected void parseKeywordList(KXmlParser parser)  throws IOException, XmlPullParserException
    {
    	int currentTag;
    	boolean end = false;
    	String value;

    	parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.KEYWORDLIST);
    	currentTag = parser.nextTag();

        while (!end)
    	{
			 switch(currentTag)
			 {
				case KXmlParser.START_TAG:
					if (parser.getName().compareTo(CapabilitiesTags.KEYWORD)==0)
					{
						value = parser.nextText();
						if ((value != null) && (value.length() > 0 ))
							addkeyword(value);
					}
					break;
				case KXmlParser.END_TAG:
					if (parser.getName().compareTo(CapabilitiesTags.KEYWORDLIST) == 0)
						end = true;
					break;
				case KXmlParser.TEXT:
					break;
			 }
			 if (!end)
			 {
				 currentTag = parser.next();
			 }
    	}
    	parser.require(KXmlParser.END_TAG, null, CapabilitiesTags.KEYWORDLIST);
    }

    /**
     * Reads and parses the layer attributes
     * Maybe this method should be moved to the WMSLayer. Until now the attributes are teh same for all versions.
     * @param parser
     */
    protected void readLayerAttributes(KXmlParser parser)
    {
    	String value = new String();

        //First of all set whether the layer is Queryable reading the attribute.
        value = parser.getAttributeValue("", CapabilitiesTags.QUERYABLE);
        if (value != null)
        {
            if (value.compareTo("0")==0)
                setQueryable(false);
            else
                setQueryable(true);
        }
        value = parser.getAttributeValue("", CapabilitiesTags.OPAQUE);
        if (value != null)
        {
            if (value.compareTo("0")==0)
                setOpaque(false);
            else
                setOpaque(true);
        }
        value = parser.getAttributeValue("", CapabilitiesTags.NOSUBSETS);
        if (value != null)
        {
            if (value.compareTo("0")==0)
                setNoSubSets(false);
            else
            	setNoSubSets(true);
        }
        value = parser.getAttributeValue("", CapabilitiesTags.FIXEDWIDTH);
        if (value != null)
        {
        	setfixedWidth(Integer.parseInt(value));
        }
        value = parser.getAttributeValue("", CapabilitiesTags.FIXEDHEIGHT);
        if (value != null)
        {
        	setfixedHeight(Integer.parseInt(value));
        }
    }


    /**
     * <p>Inner class describing the MetadataURL tag in OGC specifications in WMS</p>
     *
     */
    protected class MetadataURL
    {
    	public MetadataURL()
    	{
    		type = new String();
    		format = new String();
    		onlineResource_xlink = new String();
    		onlineResource_type = new String();
    		onlineResource_href = new String();
    	}
        public String type;
        public String format;
        public String onlineResource_xlink;
        public String onlineResource_type;
        public String onlineResource_href;
     }

    /**
     * <p>Inner class describing the DataURL tag in OGC specifications in WMS</p>
     *
     */
    protected class DataURL
    {
    	public DataURL()
    	{
    		type = new String();
    		format = new String();
    		onlineResource_xlink = new String();
    		onlineResource_type = new String();
    		onlineResource_href = new String();
    	}
        public String type;
        public String format;
        public String onlineResource_xlink;
        public String onlineResource_type;
        public String onlineResource_href;
     }
}
