<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gml="http://www.opengis.net/gml" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:gmd="http://www.isotc211.org/2005/gmd" >

	<xsl:template name="convertDateTime">
		<xsl:param name="param"/>
		<xsl:choose>
			<xsl:when test="contains($param, ' ')">
				<xsl:value-of select="concat(substring-after($param,' '), 'T', substring-before($param,' '))"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$param"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="convertDateTime115">
		<xsl:param name="param"/>
		<xsl:choose>
			<xsl:when test="contains($param, 'T')">
				<xsl:value-of select="concat(substring-after($param,'T'), ' ', substring-before($param,'T'))"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$param"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="stringToReal">
		<xsl:param name="param"/>
		<xsl:variable name="intValue" select="number($param)"/>
		<xsl:choose>
			<xsl:when test="contains($intValue, 'NaN')">0.0</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$intValue"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="stringToInteger">
		<xsl:param name="param"/>
		<xsl:variable name="intValue" select="round($param)"/>
		<xsl:choose>
			<xsl:when test="contains($intValue, 'NaN')">0</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$intValue"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="codeListElement19139">
		<xsl:param name="param"/>
		<xsl:param name="listName"/>
		<xsl:param name="namespace"/>
		<xsl:element name="{concat($namespace, ':', $listName)}" >
			<xsl:variable name="codeListValue" select="document('lists.xml')/*/list[@name=$listName]/element[@code=$param]" />
			<xsl:attribute name="codeList">
				<xsl:value-of select="concat('./resources/codeList.xml#', $listName)"/>
			</xsl:attribute>
			<xsl:attribute name="codeListValue">
				<xsl:value-of select="$codeListValue" />
			</xsl:attribute>
			<xsl:value-of select="$codeListValue" />
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="putTopicCategory">
		<xsl:param name="param"/>
		<xsl:param name="listName"/>
		<xsl:param name="namespace"/>
		<xsl:element name="{concat($namespace, ':', $listName)}" >
			<xsl:variable name="codeListValue" select="document('lists.xml')/*/list[@name=$listName]/element[@code=$param]" />
			<xsl:value-of select="$codeListValue" />
		</xsl:element>
	</xsl:template>
	
	<xsl:template name="codeListElement19115">
		<xsl:param name="param"/>
		<xsl:value-of select="*" />
	</xsl:template>
	
	<xsl:template name="putValue">
		<xsl:param name="param"/>
		<xsl:param name="value"/>
		<xsl:value-of select="$value" />
	</xsl:template>
	
	<xsl:template name="generateIdVerticalCS">verticalCS<xsl:value-of select="generate-id()" /></xsl:template>
	
	<xsl:template name="generateIdCoordinateSystemAxis">coordinateSystemAxis<xsl:value-of select="generate-id()" /></xsl:template>
	
	<xsl:template name="generateIdVerticalDatum">verticalDatum<xsl:value-of select="generate-id()" /></xsl:template>
	
	<xsl:template name="generateIdVerticalCRS">verticalCRS<xsl:value-of select="generate-id()" /></xsl:template>
	
</xsl:stylesheet>

