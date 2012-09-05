package org.gvsig.fmap.mapcontext.layers;

import java.awt.geom.Point2D;
import java.net.URI;

import org.gvsig.fmap.dal.exception.ReadException;


import com.iver.utiles.IPersistence;
import com.iver.utiles.XMLEntity;

/**
 * This is an abstract class to keep the properties of the HyperLink. One layer may have
 * one or zero LinkProperties. The properties of a Link are:
 * - fieldName, the field큦 name of the selected column of the table associated to the
 * 	layer
 * - extName, the name of the extension that the usser indicates
 * - typeLink, the type of the Link
 * The class implements  'IPersistance' interface to provide persistance to the properties
 * of HyperLink
 */
public abstract class AbstractLinkProperties implements IPersistence{
	private String fieldName;
	private String extName;
	private int typeLink;


	/**
	 * Default constructor
	 */
	public AbstractLinkProperties(){
		fieldName=null;
		extName=null;
		typeLink=-1;
	}

	/**
	 * Provides an array with URIs. Returns one URI by geometry that includes the point
	 * in its own geometry limits with a allowed tolerance.
	 * @param layer, the layer
	 * @param point, the point to check that is contained or not in the geometries in the layer
	 * @param tolerance, the tolerance allowed. Allowed margin of error to detect if the  point
	 * 		is contained in some geometries of the layer
	 * @return
	 * @throws ReadException
	 * @throws BehaviorException
	 */
	public abstract URI[] getLink(FLayer layer, Point2D point, double tolerance) throws ReadException;

	/**
	 * Gets the field큦 name of the table that the user has selected
	 * @return this name of the field
	 */
	public String getField(){
		return this.fieldName;
	}

	/**
	 * Sets the field큦 name
	 * @param campo, the field큦 name
	 */
	public void setField(String campo){
		this.fieldName=campo;
	}

	/**
	 * Gets the type of the HyperLink
	 * @return the type
	 */
	public int getType(){
		return this.typeLink;
	}

	/**
	 * Sets the type of the HyperLink
	 * @param tipo
	 */
	public void setType(int tipo){
		this.typeLink=tipo;
	}

	/**
	 * Gets the extension of the files that the user has indicated like properties of the
	 * HyperLink, this extension is added to the content of the field selected in the table
	 *
	 * @return the extension
	 */
	public String getExt(){
		return this.extName;
	}

	/**
	 * Sets the extension
	 * @param extension, the extension of the files
	 */
	public void setExt(String extension){
		this.extName=extension;
	}

	/**
	 * Provides persistance to the properties of the HyperLink.
	 * @return XMLEntity with the information of the HyperLink
	 */
	public XMLEntity getXMLEntity() {
		XMLEntity xml=new XMLEntity();
		xml.putProperty("typeChild","linkProperties");
		xml.putProperty("extName",extName);
		xml.putProperty("fieldName",fieldName);
		xml.putProperty("typeLink",typeLink);
		return xml;
	}

	/**
	 * Sets the properties of the HyperLink using a XMLEntity that contains the information
	 * @param XMLEntity
	 */
	public void setXMLEntity(XMLEntity xml) {
		extName=xml.getStringProperty("extName");
		fieldName=xml.getStringProperty("fieldName");
		typeLink=xml.getIntProperty("typeLink");

	}
}
