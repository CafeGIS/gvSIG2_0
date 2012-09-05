package org.gvsig.fmap.mapcontext.layers;

import com.iver.utiles.XMLEntity;

public class MappingAnnotation {
private int columnText=-1;
private int columnRotate=-1;
private int columnColor=-1;
private int columnHeight=-1;
private int columnTypeFont=-1;
private int columnStyleFont=-1;
public int getColumnColor() {
	return columnColor;
}
public void setColumnColor(int columnColor) {
	this.columnColor = columnColor;
}
public int getColumnHeight() {
	return columnHeight;
}
public void setColumnHeight(int columnHeight) {
	this.columnHeight = columnHeight;
}
public int getColumnRotate() {
	return columnRotate;
}
public void setColumnRotate(int columnRotate) {
	this.columnRotate = columnRotate;
}
public int getColumnStyleFont() {
	return columnStyleFont;
}
public void setColumnStyleFont(int columnStyleFont) {
	this.columnStyleFont = columnStyleFont;
}
public int getColumnText() {
	return columnText;
}
public void setColumnText(int columnText) {
	this.columnText = columnText;
}
public int getColumnTypeFont() {
	return columnTypeFont;
}
public void setColumnTypeFont(int columnTypeFont) {
	this.columnTypeFont = columnTypeFont;
}
public XMLEntity getXMLEntity() {
	XMLEntity xml=new XMLEntity();
	xml.putProperty("className",this.getClass().getName());
	xml.putProperty("columnText",columnText);
	xml.putProperty("columnRotate",columnRotate);
	xml.putProperty("columnColor",columnColor);
	xml.putProperty("columnHeight",columnHeight);
	xml.putProperty("columnTypeFont",columnTypeFont);
	xml.putProperty("columnStyleFont",columnStyleFont);
	return xml;
}
public static MappingAnnotation createFromXML(XMLEntity xml) {
	MappingAnnotation m=new MappingAnnotation();
	m.setColumnText(xml.getIntProperty("columnText"));
	m.setColumnRotate(xml.getIntProperty("columnRotate"));
	m.setColumnColor(xml.getIntProperty("columnColor"));
	m.setColumnHeight(xml.getIntProperty("columnHeight"));
	m.setColumnTypeFont(xml.getIntProperty("columnTypeFont"));
	m.setColumnStyleFont(xml.getIntProperty("columnStyleFont"));
	return m;
}
}
