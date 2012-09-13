<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gml="http://www.opengis.net/gml" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:gmd="http://www.isotc211.org/2005/gmd" >
<xsl:import href="externalTemplates.xsl" />
<xsl:output method="xml" indent="yes" encoding="ISO-8859-1"/>
<xsl:template match="/" >
<xsl:apply-templates select="gmd:MD_Metadata" />
</xsl:template>
<xsl:template match="gco:Distance" >
<xsl:element name="distance" >
<xsl:for-each select="gco:distance" >
<xsl:element name="value" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:CI_DateTypeCode" >
<xsl:element name="CI_DateTypeCode" >
<xsl:for-each select="gmd:creation" >
<xsl:element name="creation" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:publication" >
<xsl:element name="publication" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:revision" >
<xsl:element name="revision" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:EX_TemporalElement" >
<xsl:element name="EX_TemporalElement" >
<xsl:choose>
<xsl:when test="gmd:extent" >
<xsl:for-each select="gmd:extent[1]" >
<xsl:element name="extent" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="extent" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:CI_OnLineFunctionCode" >
<xsl:element name="CI_OnLineFunctionCode" >
<xsl:for-each select="gmd:download" >
<xsl:element name="download" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:information" >
<xsl:element name="information" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:offlineAccess" >
<xsl:element name="offlineAccess" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:order" >
<xsl:element name="order" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:search" >
<xsl:element name="search" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:CI_PresentationFormCode" >
<xsl:element name="CI_PresentationFormCode" >
<xsl:for-each select="gmd:documentDigital" >
<xsl:element name="documentDigital" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:documentHardcopy" >
<xsl:element name="documentHardcopy" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:imageDigital" >
<xsl:element name="imageDigital" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:imageHardcopy" >
<xsl:element name="imageHardcopy" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:mapDigital" >
<xsl:element name="mapDigital" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:mapHardcopy" >
<xsl:element name="mapHardcopy" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:modelDigital" >
<xsl:element name="modelDigital" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:modelHardcopy" >
<xsl:element name="modelHardcopy" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:profileDigital" >
<xsl:element name="profileDigital" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:profileHardcopy" >
<xsl:element name="profileHardcopy" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:tableDigital" >
<xsl:element name="tableDigital" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:tableHardcopy" >
<xsl:element name="tableHardcopy" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:videoDigital" >
<xsl:element name="videoDigital" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:videoHardcopy" >
<xsl:element name="videoHardcopy" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:CI_RoleCode" >
<xsl:element name="CI_RoleCode" >
<xsl:for-each select="gmd:resourceProvider" >
<xsl:element name="resourceProvider" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:custodian" >
<xsl:element name="custodian" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:owner" >
<xsl:element name="owner" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:user" >
<xsl:element name="user" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:distributor" >
<xsl:element name="distributor" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:originator" >
<xsl:element name="originator" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:pointOfContact" >
<xsl:element name="pointOfContact" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:principalInvestigator" >
<xsl:element name="principalInvestigator" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:processor" >
<xsl:element name="processor" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:publisher" >
<xsl:element name="publisher" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:author" >
<xsl:element name="author" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:DQ_EvaluationMethodTypeCode" >
<xsl:element name="DQ_EvaluationMethodTypeCode" >
<xsl:for-each select="gmd:directInternal" >
<xsl:element name="directInternal" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:directExternal" >
<xsl:element name="directExternal" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:indirect" >
<xsl:element name="indirect" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:DS_AssociationTypeCode" >
<xsl:element name="DS_AssociationTypeCode" >
<xsl:for-each select="gmd:crossReference" >
<xsl:element name="crossReference" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:largerWorkCitation" >
<xsl:element name="largerWorkCitation" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:partOfSeamlessDatabase" >
<xsl:element name="partOfSeamlessDatabase" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:source" >
<xsl:element name="source" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:stereoMate" >
<xsl:element name="stereoMate" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:DS_InitiativeTypeCode" >
<xsl:element name="DS_InitiativeTypeCode" >
<xsl:for-each select="gmd:campaign" >
<xsl:element name="campaign" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:collection" >
<xsl:element name="collection" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:exercise" >
<xsl:element name="exercise" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:experiment" >
<xsl:element name="experiment" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:investigation" >
<xsl:element name="investigation" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:mission" >
<xsl:element name="mission" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:sensor" >
<xsl:element name="sensor" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:operation" >
<xsl:element name="operation" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:platform" >
<xsl:element name="platform" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:process" >
<xsl:element name="process" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:program" >
<xsl:element name="program" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:project" >
<xsl:element name="project" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:study" >
<xsl:element name="study" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:task" >
<xsl:element name="task" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:trial" >
<xsl:element name="trial" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_CellGeometryCode" >
<xsl:element name="MD_CellGeometryCode" >
<xsl:for-each select="gmd:point" >
<xsl:element name="point" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:area" >
<xsl:element name="area" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_CharacterSetCode" >
<xsl:element name="MD_CharacterSetCode" >
<xsl:for-each select="gmd:ucs2" >
<xsl:element name="ucs2" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:ucs4" >
<xsl:element name="ucs4" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:utf7" >
<xsl:element name="utf7" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:utf8" >
<xsl:element name="utf8" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:utf16" >
<xsl:element name="utf16" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:jis" >
<xsl:element name="jis" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:shiftJIS" >
<xsl:element name="shiftJIS" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:eucJP" >
<xsl:element name="eucJP" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:usAscii" >
<xsl:element name="usAscii" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:ebcdic" >
<xsl:element name="ebcdic" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:eucKR" >
<xsl:element name="eucKR" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:big5" >
<xsl:element name="big5" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:GB2312" >
<xsl:element name="GB2312" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_ClassificationCode" >
<xsl:element name="MD_ClassificationCode" >
<xsl:for-each select="gmd:unclassified" >
<xsl:element name="unclassified" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:restricted" >
<xsl:element name="restricted" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:confidential" >
<xsl:element name="confidential" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:secret" >
<xsl:element name="secret" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:topSecret" >
<xsl:element name="topSecret" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_CoverageContentTypeCode" >
<xsl:element name="MD_CoverageContentTypeCode" >
<xsl:for-each select="gmd:image" >
<xsl:element name="image" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:thematicClassification" >
<xsl:element name="thematicClassification" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:physicalMeasurement" >
<xsl:element name="physicalMeasurement" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_Metadata" >
<xsl:element name="MD_Metadata" >
<xsl:for-each select="gmd:fileIdentifier[1]" >
<xsl:element name="fileIdentifier" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:language[1]" >
<xsl:element name="language" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:characterSet[1]" >
<xsl:element name="characterSet" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:parentIdentifier[1]" >
<xsl:element name="parentIdentifier" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:hierarchyLevel" >
<xsl:element name="hierarchyLevel" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:hierarchyLevelName" >
<xsl:element name="hierarchyLevelName" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:contact" >
<xsl:for-each select="gmd:contact" >
<xsl:element name="contact" >
<xsl:apply-templates select="gmd:CI_ResponsibleParty" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="contact" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:dateStamp" >
<xsl:for-each select="gmd:dateStamp[1]" >
<xsl:element name="dateStamp" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="dateStamp" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:metadataStandardName[1]" >
<xsl:element name="metadataStandardName" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:metadataStandardVersion[1]" >
<xsl:element name="metadataStandardVersion" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:dataSetURI[1]" >
<xsl:element name="dataSet" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:spatialRepresentationInfo" >
<xsl:element name="spatialRepresentationInfo" >
<xsl:apply-templates select="gmd:MD_SpatialRepresentation" />
<xsl:apply-templates select="gmd:MD_Georectified" />
<xsl:apply-templates select="gmd:MD_Georeferenceable" />
<xsl:apply-templates select="gmd:MD_VectorSpatialRepresentation" />
<xsl:apply-templates select="gmd:MD_GridSpatialRepresentation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:referenceSystemInfo" >
<xsl:element name="referenceSystemInfo" >
<xsl:apply-templates select="gmd:MD_ReferenceSystem" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:metadataExtensionInfo" >
<xsl:element name="metadataExtensionInfo" >
<xsl:apply-templates select="gmd:MD_MetadataExtensionInformation" />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:identificationInfo" >
<xsl:for-each select="gmd:identificationInfo" >
<xsl:element name="identificationInfo" >
<xsl:apply-templates select="gmd:MD_Identification" />
<xsl:apply-templates select="gmd:MD_DataIdentification" />
<xsl:apply-templates select="gmd:MD_ServiceIdentification" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="identificationInfo" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:contentInfo" >
<xsl:element name="contentInfo" >
<xsl:apply-templates select="gmd:MD_ContentInformation" />
<xsl:apply-templates select="gmd:MD_FeatureCatalogueDescription" />
<xsl:apply-templates select="gmd:MD_CoverageDescription" />
<xsl:apply-templates select="gmd:MD_ImageDescription" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:distributionInfo[1]" >
<xsl:element name="distributionInfo" >
<xsl:apply-templates select="gmd:MD_Distribution" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:dataQualityInfo" >
<xsl:element name="dataQualityInfo" >
<xsl:apply-templates select="gmd:DQ_DataQuality" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:portrayalCatalogueInfo" >
<xsl:element name="portrayalCatalogueInfo" >
<xsl:apply-templates select="gmd:MD_PortrayalCatalogueReference" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:metadataConstraints" >
<xsl:element name="metadataConstraints" >
<xsl:apply-templates select="gmd:MD_Constraints" />
<xsl:apply-templates select="gmd:MD_LegalConstraints" />
<xsl:apply-templates select="gmd:MD_SecurityConstraints" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:applicationSchemaInfo" >
<xsl:element name="applicationSchemaInfo" >
<xsl:apply-templates select="gmd:MD_ApplicationSchemaInformation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:metadataMaintenance[1]" >
<xsl:element name="metadataMaintenance" >
<xsl:apply-templates select="gmd:MD_MaintenanceInformation" />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_DatatypeCode" >
<xsl:element name="MD_DatatypeCode" >
<xsl:for-each select="gmd:class" >
<xsl:element name="class" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:codelist" >
<xsl:element name="codelist" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:enumeration" >
<xsl:element name="enumeration" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:codelistElement" >
<xsl:element name="codelistElement" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:abstractClass" >
<xsl:element name="abstractClass" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:aggregateClass" >
<xsl:element name="aggregateClass" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:specifiedClass" >
<xsl:element name="specifiedClass" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:datatypeClass" >
<xsl:element name="datatypeClass" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:interfaceClass" >
<xsl:element name="interfaceClass" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:unionClass" >
<xsl:element name="unionClass" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:metaClass" >
<xsl:element name="metaClass" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:typeClass" >
<xsl:element name="typeClass" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:characterString" >
<xsl:element name="characterString" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:integer" >
<xsl:element name="integer" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:association" >
<xsl:element name="association" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_DimensionTypeCode" >
<xsl:element name="MD_DimensionTypeCode" >
<xsl:for-each select="gmd:row" >
<xsl:element name="row" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:column" >
<xsl:element name="column" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:vertical" >
<xsl:element name="vertical" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:track" >
<xsl:element name="track" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:crossTrack" >
<xsl:element name="crossTrack" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:line" >
<xsl:element name="line" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:sample" >
<xsl:element name="sample" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:time" >
<xsl:element name="time" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_GeometricObjectTypeCode" >
<xsl:element name="MD_GeometricObjectTypeCode" >
<xsl:for-each select="gmd:complex" >
<xsl:element name="complex" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:composite" >
<xsl:element name="composite" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:curve" >
<xsl:element name="curve" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:point" >
<xsl:element name="point" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:solid" >
<xsl:element name="solid" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:surface" >
<xsl:element name="surface" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_ImagingConditionCode" >
<xsl:element name="MD_ImagingConditionCode" >
<xsl:for-each select="gmd:blurredImage" >
<xsl:element name="blurredImage" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:cloud" >
<xsl:element name="cloud" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:degradingObliquity" >
<xsl:element name="degradingObliquity" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:fog" >
<xsl:element name="fog" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:heavySmokeOrDust" >
<xsl:element name="heavySmokeOrDust" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:night" >
<xsl:element name="night" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:rain" >
<xsl:element name="rain" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:seminDarkness" >
<xsl:element name="seminDarkness" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:shadow" >
<xsl:element name="shadow" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:snow" >
<xsl:element name="snow" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:terrainMasking" >
<xsl:element name="terrainMasking" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_KeywordTypeCode" >
<xsl:element name="MD_KeywordTypeCode" >
<xsl:for-each select="gmd:dicipline" >
<xsl:element name="dicipline" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:place" >
<xsl:element name="place" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:stratum" >
<xsl:element name="stratum" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:temporal" >
<xsl:element name="temporal" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:theme" >
<xsl:element name="theme" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_MaintenanceFrequencyCode" >
<xsl:element name="MD_MaintenanceFrequencyCode" >
<xsl:for-each select="gmd:continual" >
<xsl:element name="continual" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:daily" >
<xsl:element name="daily" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:weekly" >
<xsl:element name="weekly" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:fortnightly" >
<xsl:element name="fortnightly" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:monthly" >
<xsl:element name="monthly" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:quarterly" >
<xsl:element name="quarterly" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:biannually" >
<xsl:element name="biannually" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:annually" >
<xsl:element name="annually" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:asNeeded" >
<xsl:element name="asNeeded" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:irregular" >
<xsl:element name="irregular" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:notPlanned" >
<xsl:element name="notPlanned" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:unknown" >
<xsl:element name="unknown" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_MediumFormatCode" >
<xsl:element name="MD_MediumFormatCode" >
<xsl:for-each select="gmd:cpio" >
<xsl:element name="cpio" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:tar" >
<xsl:element name="tar" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:highSierra" >
<xsl:element name="highSierra" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:iso9660" >
<xsl:element name="iso9660" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:iso9660RockRidge" >
<xsl:element name="iso9660RockRidge" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:iso9660AppleHFS" >
<xsl:element name="iso9660AppleHFS" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_MediumNameCode" >
<xsl:element name="MD_MediumNameCode" >
<xsl:for-each select="gmd:cdRom" >
<xsl:element name="cdRom" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:dvd" >
<xsl:element name="dvd" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:dvdRom" >
<xsl:element name="dvdRom" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:digitalLinearTape" >
<xsl:element name="digitalLinearTape" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:onLine" >
<xsl:element name="onLine" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:satellite" >
<xsl:element name="satellite" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:telephoneLink" >
<xsl:element name="telephoneLink" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:hardcopy" >
<xsl:element name="hardcopy" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_ObligationCode" >
<xsl:element name="MD_ObligationCode" >
<xsl:for-each select="gmd:mandatory" >
<xsl:element name="mandatory" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:optional" >
<xsl:element name="optional" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:conditional" >
<xsl:element name="conditional" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_PixelOrientationCode" >
<xsl:element name="MD_PixelOrientationCode" >
<xsl:for-each select="gmd:center" >
<xsl:element name="center" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:lowerLeft" >
<xsl:element name="lowerLeft" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:lowerRight" >
<xsl:element name="lowerRight" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:upperRight" >
<xsl:element name="upperRight" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:upperLeft" >
<xsl:element name="upperLeft" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_ProgressCode" >
<xsl:element name="MD_ProgressCode" >
<xsl:for-each select="gmd:completed" >
<xsl:element name="completed" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:historicalArchive" >
<xsl:element name="historicalArchive" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:obsolete" >
<xsl:element name="obsolete" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:onGoing" >
<xsl:element name="onGoing" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:planned" >
<xsl:element name="planned" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:required" >
<xsl:element name="required" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:underDevelopment" >
<xsl:element name="underDevelopment" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_RestrictionCode" >
<xsl:element name="MD_RestrictionCode" >
<xsl:for-each select="gmd:copyright" >
<xsl:element name="copyright" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:patent" >
<xsl:element name="patent" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:patentPending" >
<xsl:element name="patentPending" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:trademark" >
<xsl:element name="trademark" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:license" >
<xsl:element name="license" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:intellectualPropertyRights" >
<xsl:element name="intellectualPropertyRights" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:restricted" >
<xsl:element name="restricted" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:otherRestrictions" >
<xsl:element name="otherRestrictions" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_ScopeCode" >
<xsl:element name="MD_ScopeCode" >
<xsl:for-each select="gmd:attribute" >
<xsl:element name="attribute" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:attributeType" >
<xsl:element name="attributeType" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:collectionHardware" >
<xsl:element name="collectionHardware" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:collectionSession" >
<xsl:element name="collectionSession" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:dataset" >
<xsl:element name="dataset" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:series" >
<xsl:element name="series" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:nonGeographicDataset" >
<xsl:element name="nonGeographicDataset" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:dimensionGroup" >
<xsl:element name="dimensionGroup" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:feature" >
<xsl:element name="feature" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:featureType" >
<xsl:element name="featureType" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:propertyType" >
<xsl:element name="propertyType" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:fieldSession" >
<xsl:element name="fieldSession" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:software" >
<xsl:element name="software" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:service" >
<xsl:element name="service" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:model" >
<xsl:element name="model" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:tile" >
<xsl:element name="tile" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_Identification" >
</xsl:template>
<xsl:template match="gmd:MD_DataIdentification" >
<xsl:element name="MD_DataIdentification" >
<xsl:choose>
<xsl:when test="gmd:citation" >
<xsl:for-each select="gmd:citation[1]" >
<xsl:element name="citation" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="citation" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:abstract" >
<xsl:for-each select="gmd:abstract[1]" >
<xsl:element name="abstract" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="abstract" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:purpose[1]" >
<xsl:element name="purpose" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:credit" >
<xsl:element name="credit" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:status" >
<xsl:element name="status" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:pointOfContact" >
<xsl:element name="pointOfContact" >
<xsl:apply-templates select="gmd:CI_ResponsibleParty" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:resourceMaintenance" >
<xsl:element name="resourceMaintenance" >
<xsl:apply-templates select="gmd:MD_MaintenanceInformation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:graphicOverview" >
<xsl:element name="graphicOverview" >
<xsl:apply-templates select="gmd:MD_BrowseGraphic" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:resourceFormat" >
<xsl:element name="resourceFormat" >
<xsl:apply-templates select="gmd:MD_Format" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:descriptiveKeywords" >
<xsl:element name="descriptiveKeywords" >
<xsl:apply-templates select="gmd:MD_Keywords" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:resourceSpecificUsage" >
<xsl:element name="resourceSpecificUsage" >
<xsl:apply-templates select="gmd:MD_Usage" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:resourceConstraints" >
<xsl:element name="resourceConstraints" >
<xsl:apply-templates select="gmd:MD_Constraints" />
<xsl:apply-templates select="gmd:MD_LegalConstraints" />
<xsl:apply-templates select="gmd:MD_SecurityConstraints" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:aggregationInfo" >
<xsl:element name="aggregationInfo" >
<xsl:apply-templates select="gmd:MD_AggregateInformation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:spatialRepresentationType" >
<xsl:element name="spatialRepresentationType" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:spatialResolution" >
<xsl:element name="spatialResolution" >
<xsl:apply-templates select="gmd:MD_Resolution" />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:language" >
<xsl:for-each select="gmd:language" >
<xsl:element name="language" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="language" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:characterSet" >
<xsl:element name="characterSet" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:topicCategory" >
<xsl:element name="topicCategory" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:environmentDescription[1]" >
<xsl:element name="environmentDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:extent" >
<xsl:element name="extent" >
<xsl:apply-templates select="gmd:EX_Extent" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:supplementalInformation[1]" >
<xsl:element name="supplementalInformation" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_ServiceIdentification" >
<xsl:element name="MD_ServiceIdentification" >
<xsl:choose>
<xsl:when test="gmd:citation" >
<xsl:for-each select="gmd:citation[1]" >
<xsl:element name="citation" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="citation" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:abstract" >
<xsl:for-each select="gmd:abstract[1]" >
<xsl:element name="abstract" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="abstract" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:purpose[1]" >
<xsl:element name="purpose" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:credit" >
<xsl:element name="credit" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:status" >
<xsl:element name="status" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:pointOfContact" >
<xsl:element name="pointOfContact" >
<xsl:apply-templates select="gmd:CI_ResponsibleParty" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:resourceMaintenance" >
<xsl:element name="resourceMaintenance" >
<xsl:apply-templates select="gmd:MD_MaintenanceInformation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:graphicOverview" >
<xsl:element name="graphicOverview" >
<xsl:apply-templates select="gmd:MD_BrowseGraphic" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:resourceFormat" >
<xsl:element name="resourceFormat" >
<xsl:apply-templates select="gmd:MD_Format" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:descriptiveKeywords" >
<xsl:element name="descriptiveKeywords" >
<xsl:apply-templates select="gmd:MD_Keywords" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:resourceSpecificUsage" >
<xsl:element name="resourceSpecificUsage" >
<xsl:apply-templates select="gmd:MD_Usage" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:resourceConstraints" >
<xsl:element name="resourceConstraints" >
<xsl:apply-templates select="gmd:MD_Constraints" />
<xsl:apply-templates select="gmd:MD_LegalConstraints" />
<xsl:apply-templates select="gmd:MD_SecurityConstraints" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:aggregationInfo" >
<xsl:element name="aggregationInfo" >
<xsl:apply-templates select="gmd:MD_AggregateInformation" />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_SpatialRepresentationTypeCode" >
<xsl:element name="MD_SpatialRepresentationTypeCode" >
<xsl:for-each select="gmd:vector" >
<xsl:element name="vector" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:grid" >
<xsl:element name="grid" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:textTable" >
<xsl:element name="textTable" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:tin" >
<xsl:element name="tin" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:stereoModel" >
<xsl:element name="stereoModel" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:video" >
<xsl:element name="video" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_TopicCategoryCode" >
<xsl:element name="MD_TopicCategoryCode" >
<xsl:for-each select="gmd:farming" >
<xsl:element name="farming" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:biota" >
<xsl:element name="biota" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:boundaries" >
<xsl:element name="boundaries" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:climatologyMeteorologyAtmosphere" >
<xsl:element name="climatologyMeteorologyAtmosphere" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:economy" >
<xsl:element name="economy" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:elevation" >
<xsl:element name="elevation" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:environment" >
<xsl:element name="environment" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:geoscientificInformation" >
<xsl:element name="geoscientificInformation" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:health" >
<xsl:element name="health" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:imageryBaseMapsEarthCover" >
<xsl:element name="imageryBaseMapsEarthCover" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:intelligenceMilitary" >
<xsl:element name="intelligenceMilitary" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:inlandWaters" >
<xsl:element name="inlandWaters" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:location" >
<xsl:element name="location" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:oceans" >
<xsl:element name="oceans" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:planningCadastre" >
<xsl:element name="planningCadastre" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:society" >
<xsl:element name="society" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:structure" >
<xsl:element name="structure" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:transportation" >
<xsl:element name="transportation" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:utilitiesCommunication" >
<xsl:element name="utilitiesCommunication" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_TopologyLevelCode" >
<xsl:element name="MD_TopologyLevelCode" >
<xsl:for-each select="gmd:geometryOnly" >
<xsl:element name="geometryOnly" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:topology1D" >
<xsl:element name="topology1D" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:planarGraph" >
<xsl:element name="planarGraph" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:fullPlanarGraph" >
<xsl:element name="fullPlanarGraph" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:surfaceGraph" >
<xsl:element name="surfaceGraph" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:fullSurfaceGraph" >
<xsl:element name="fullSurfaceGraph" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:topology3D" >
<xsl:element name="topology3D" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:fullTopology3D" >
<xsl:element name="fullTopology3D" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:abstract" >
<xsl:element name="abstract" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_BrowseGraphic" >
<xsl:element name="MD_BrowseGraphic" >
<xsl:choose>
<xsl:when test="gmd:fileName" >
<xsl:for-each select="gmd:fileName[1]" >
<xsl:element name="fileName" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="fileName" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:fileDescription[1]" >
<xsl:element name="fileDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:fileType[1]" >
<xsl:element name="fileType" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_Keywords" >
<xsl:element name="MD_Keywords" >
<xsl:choose>
<xsl:when test="gmd:keyword" >
<xsl:for-each select="gmd:keyword" >
<xsl:element name="keyword" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="keyword" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:type[1]" >
<xsl:element name="type" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:thesaurusName[1]" >
<xsl:element name="thesaurusName" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_RepresentativeFraction" >
<xsl:element name="MD_RepresentativeFraction" >
<xsl:choose>
<xsl:when test="gmd:denominator" >
<xsl:for-each select="gmd:denominator[1]" >
<xsl:element name="denominator" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="denominator" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_Usage" >
<xsl:element name="MD_usage" >
<xsl:choose>
<xsl:when test="gmd:specificUsage" >
<xsl:for-each select="gmd:specificUsage[1]" >
<xsl:element name="specificUsage" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="specificUsage" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:usageDateTime[1]" >
<xsl:element name="usageDateTime" >
<xsl:call-template name="convertDateTime115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:userDeterminedLimitations[1]" >
<xsl:element name="userDeterminedLimitations" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:userContactInfo" >
<xsl:for-each select="gmd:userContactInfo" >
<xsl:element name="userContactInfo" >
<xsl:apply-templates select="gmd:CI_ResponsibleParty" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="userContactInfo" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_AggregateInformation" >
<xsl:element name="MD_AggregateInformation" >
<xsl:for-each select="gmd:aggregateDataSetName[1]" >
<xsl:element name="aggregateDataSetName" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:aggregateDataSetIdentifier[1]" >
<xsl:element name="aggregateDataSetIdentifier" >
<xsl:apply-templates select="gmd:MD_Identifier" />
<xsl:apply-templates select="gmd:RS_Identifier" />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:associationType" >
<xsl:for-each select="gmd:associationType[1]" >
<xsl:element name="associationType" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="associationType" >

<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:initiativeType[1]" >
<xsl:element name="initiativeType" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_Constraints" >
<xsl:element name="MD_Constraints" >
<xsl:for-each select="gmd:useLimitation" >
<xsl:element name="useLimitation" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_LegalConstraints" >
<xsl:element name="MD_LegalConstraints" >
<xsl:for-each select="gmd:useLimitation" >
<xsl:element name="useLimitation" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:accessConstraints" >
<xsl:element name="accessConstraints" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:useConstraints" >
<xsl:element name="useConstraints" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:otherConstraints" >
<xsl:element name="otherConstraints" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_SecurityConstraints" >
<xsl:element name="MD_SecurityConstraints" >
<xsl:for-each select="gmd:useLimitation" >
<xsl:element name="useLimitation" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:classification" >
<xsl:for-each select="gmd:classification[1]" >
<xsl:element name="classification" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="classification" >

<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:userNote[1]" >
<xsl:element name="userNote" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:classificationSystem[1]" >
<xsl:element name="classificationSystem" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:handlingDescription[1]" >
<xsl:element name="handlingDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:DQ_DataQuality" >
<xsl:element name="DQ_DataQuality" >
<xsl:choose>
<xsl:when test="gmd:scope" >
<xsl:for-each select="gmd:scope[1]" >
<xsl:element name="scope" >
<xsl:apply-templates select="gmd:DQ_Scope" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="scope" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:report" >
<xsl:element name="report" >
<xsl:apply-templates select="gmd:DQ_Element" />
<xsl:apply-templates select="gmd:DQ_Completeness" />
<xsl:apply-templates select="gmd:DQ_CompletenessCommission" />
<xsl:apply-templates select="gmd:DQ_CompletenessOmission" />
<xsl:apply-templates select="gmd:DQ_LogicalConsistency" />
<xsl:apply-templates select="gmd:DQ_ConceptualConsistency" />
<xsl:apply-templates select="gmd:DQ_DomainConsistency" />
<xsl:apply-templates select="gmd:DQ_FormatConsistency" />
<xsl:apply-templates select="gmd:DQ_TopologicalConsistency" />
<xsl:apply-templates select="gmd:DQ_PositionalAccuracy" />
<xsl:apply-templates select="gmd:DQ_AbsoluteExternalPositionalAccuracy" />
<xsl:apply-templates select="gmd:DQ_GriddedDataPositionalAccuracy" />
<xsl:apply-templates select="gmd:DQ_RelativeInternalPositionalAccuracy" />
<xsl:apply-templates select="gmd:DQ_TemporalAccuracy" />
<xsl:apply-templates select="gmd:DQ_AccuracyOfATimeMeasurement" />
<xsl:apply-templates select="gmd:DQ_TemporalConsistency" />
<xsl:apply-templates select="gmd:DQ_TemporalValidity" />
<xsl:apply-templates select="gmd:DQ_ThematicAccuracy" />
<xsl:apply-templates select="gmd:DQ_ThematicClassificationCorrectness" />
<xsl:apply-templates select="gmd:DQ_NonQuantitativeAttributeAccuracy" />
<xsl:apply-templates select="gmd:DQ_QuantitativeAttributeAccuracy" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:lineage[1]" >
<xsl:element name="lineage" >
<xsl:apply-templates select="gmd:LI_Lineage" />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:LI_Lineage" >
<xsl:element name="LI_Lineage" >
<xsl:for-each select="gmd:statement[1]" >
<xsl:element name="statement" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:processStep" >
<xsl:element name="processStep" >
<xsl:apply-templates select="gmd:LI_ProcessStep" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:source" >
<xsl:element name="source" >
<xsl:apply-templates select="gmd:LI_Source" />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:LI_ProcessStep" >
<xsl:element name="LI_ProcessStep" >
<xsl:choose>
<xsl:when test="gmd:description" >
<xsl:for-each select="gmd:description[1]" >
<xsl:element name="description" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="description" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:rationale[1]" >
<xsl:element name="rationale" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:dateTime[1]" >
<xsl:element name="dateTime" >
<xsl:call-template name="convertDateTime115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:processor" >
<xsl:element name="processor" >
<xsl:apply-templates select="gmd:CI_ResponsibleParty" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:source" >
<xsl:element name="source" >
<xsl:apply-templates select="gmd:LI_Source" />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:LI_Source" >
<xsl:element name="LI_Source" >
<xsl:for-each select="gmd:description[1]" >
<xsl:element name="description" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:scaleDenominator[1]" >
<xsl:element name="scaleDenominator" >
<xsl:apply-templates select="gmd:MD_RepresentativeFraction" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:sourceReferenceSystem[1]" >
<xsl:element name="sourceReferenceSystem" >
<xsl:apply-templates select="gmd:MD_ReferenceSystem" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:sourceCitation[1]" >
<xsl:element name="sourceCitation" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:sourceExtent" >
<xsl:element name="sourceExtent" >
<xsl:apply-templates select="gmd:EX_Extent" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:sourceStep" >
<xsl:element name="sourceStep" >
<xsl:apply-templates select="gmd:LI_ProcessStep" />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:DQ_Completeness" >
</xsl:template>
<xsl:template match="gmd:DQ_CompletenessCommission" >
<xsl:element name="DQ_CompletenessCommission" >
<xsl:for-each select="gmd:nameOfMeasure" >
<xsl:element name="nameOfMeasure" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:measureDescription[1]" >
<xsl:element name="measureDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodType[1]" >
<xsl:element name="evaluationMethodType" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodDescription[1]" >
<xsl:element name="evaluationMethodDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationProcedure[1]" >
<xsl:element name="evaluationProcedure" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:dateTime" >
<xsl:element name="dateTime" >
<xsl:call-template name="convertDateTime115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:result" >
<xsl:for-each select="gmd:result" >
<xsl:element name="result" >
<xsl:apply-templates select="gmd:DQ_Result" />
<xsl:apply-templates select="gmd:DQ_ConformanceResult" />
<xsl:apply-templates select="gmd:DQ_QuantitativeResult" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="result" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:measureIdentification[1]" >
<xsl:element name="measureIdentification" >
<xsl:for-each select="gmd:MD_Identifier/gmd:authority[1]" >
<xsl:element name="authority" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:MD_Identifier/gmd:code[1]" >
<xsl:element name="code" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:DQ_CompletenessOmission" >
<xsl:element name="DQ_CompletenessOmmission" >
<xsl:for-each select="gmd:nameOfMeasure" >
<xsl:element name="nameOfMeasure" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:measureDescription[1]" >
<xsl:element name="measureDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodType[1]" >
<xsl:element name="evaluationMethodType" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodDescription[1]" >
<xsl:element name="evaluationMethodDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationProcedure[1]" >
<xsl:element name="evaluationProcedure" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:dateTime" >
<xsl:element name="dateTime" >
<xsl:call-template name="convertDateTime115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:result" >
<xsl:for-each select="gmd:result" >
<xsl:element name="result" >
<xsl:apply-templates select="gmd:DQ_Result" />
<xsl:apply-templates select="gmd:DQ_ConformanceResult" />
<xsl:apply-templates select="gmd:DQ_QuantitativeResult" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="result" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:measureIdentification[1]" >
<xsl:element name="measureIdentification" >
<xsl:for-each select="gmd:MD_Identifier/gmd:authority[1]" >
<xsl:element name="authority" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:MD_Identifier/gmd:code[1]" >
<xsl:element name="code" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:DQ_LogicalConsistency" >
</xsl:template>
<xsl:template match="gmd:DQ_ConceptualConsistency" >
<xsl:element name="DQ_ConceptualConsistency" >
<xsl:for-each select="gmd:nameOfMeasure" >
<xsl:element name="nameOfMeasure" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:measureDescription[1]" >
<xsl:element name="measureDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodType[1]" >
<xsl:element name="evaluationMethodType" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodDescription[1]" >
<xsl:element name="evaluationMethodDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationProcedure[1]" >
<xsl:element name="evaluationProcedure" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:dateTime" >
<xsl:element name="dateTime" >
<xsl:call-template name="convertDateTime115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:result" >
<xsl:for-each select="gmd:result" >
<xsl:element name="result" >
<xsl:apply-templates select="gmd:DQ_Result" />
<xsl:apply-templates select="gmd:DQ_ConformanceResult" />
<xsl:apply-templates select="gmd:DQ_QuantitativeResult" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="result" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:measureIdentification[1]" >
<xsl:element name="measureIdentification" >
<xsl:for-each select="gmd:MD_Identifier/gmd:authority[1]" >
<xsl:element name="authority" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:MD_Identifier/gmd:code[1]" >
<xsl:element name="code" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:DQ_DomainConsistency" >
<xsl:element name="DQ_DomainConsistency" >
<xsl:for-each select="gmd:nameOfMeasure" >
<xsl:element name="nameOfMeasure" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:measureDescription[1]" >
<xsl:element name="measureDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodType[1]" >
<xsl:element name="evaluationMethodType" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodDescription[1]" >
<xsl:element name="evaluationMethodDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationProcedure[1]" >
<xsl:element name="evaluationProcedure" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:dateTime" >
<xsl:element name="dateTime" >
<xsl:call-template name="convertDateTime115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:result" >
<xsl:for-each select="gmd:result" >
<xsl:element name="result" >
<xsl:apply-templates select="gmd:DQ_Result" />
<xsl:apply-templates select="gmd:DQ_ConformanceResult" />
<xsl:apply-templates select="gmd:DQ_QuantitativeResult" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="result" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:measureIdentification[1]" >
<xsl:element name="measureIdentification" >
<xsl:for-each select="gmd:MD_Identifier/gmd:authority[1]" >
<xsl:element name="authority" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:MD_Identifier/gmd:code[1]" >
<xsl:element name="code" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:DQ_FormatConsistency" >
<xsl:element name="DQ_FormatConsistency" >
<xsl:for-each select="gmd:nameOfMeasure" >
<xsl:element name="nameOfMeasure" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:measureDescription[1]" >
<xsl:element name="measureDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodType[1]" >
<xsl:element name="evaluationMethodType" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodDescription[1]" >
<xsl:element name="evaluationMethodDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationProcedure[1]" >
<xsl:element name="evaluationProcedure" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:dateTime" >
<xsl:element name="dateTime" >
<xsl:call-template name="convertDateTime115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:result" >
<xsl:for-each select="gmd:result" >
<xsl:element name="result" >
<xsl:apply-templates select="gmd:DQ_Result" />
<xsl:apply-templates select="gmd:DQ_ConformanceResult" />
<xsl:apply-templates select="gmd:DQ_QuantitativeResult" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="result" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:measureIdentification[1]" >
<xsl:element name="measureIdentification" >
<xsl:for-each select="gmd:MD_Identifier/gmd:authority[1]" >
<xsl:element name="authority" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:MD_Identifier/gmd:code[1]" >
<xsl:element name="code" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:DQ_TopologicalConsistency" >
<xsl:element name="DQ_TopologicalConsistency" >
<xsl:for-each select="gmd:nameOfMeasure" >
<xsl:element name="nameOfMeasure" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:measureDescription[1]" >
<xsl:element name="measureDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodType[1]" >
<xsl:element name="evaluationMethodType" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodDescription[1]" >
<xsl:element name="evaluationMethodDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationProcedure[1]" >
<xsl:element name="evaluationProcedure" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:dateTime" >
<xsl:element name="dateTime" >
<xsl:call-template name="convertDateTime115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:result" >
<xsl:for-each select="gmd:result" >
<xsl:element name="result" >
<xsl:apply-templates select="gmd:DQ_Result" />
<xsl:apply-templates select="gmd:DQ_ConformanceResult" />
<xsl:apply-templates select="gmd:DQ_QuantitativeResult" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="result" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:measureIdentification[1]" >
<xsl:element name="measureIdentification" >
<xsl:for-each select="gmd:MD_Identifier/gmd:authority[1]" >
<xsl:element name="authority" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:MD_Identifier/gmd:code[1]" >
<xsl:element name="code" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:DQ_PositionalAccuracy" >
</xsl:template>
<xsl:template match="gmd:DQ_AbsoluteExternalPositionalAccuracy" >
<xsl:element name="DQ_AbsoluteExternalPositionalAccuracy" >
<xsl:for-each select="gmd:nameOfMeasure" >
<xsl:element name="nameOfMeasure" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:measureDescription[1]" >
<xsl:element name="measureDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodType[1]" >
<xsl:element name="evaluationMethodType" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodDescription[1]" >
<xsl:element name="evaluationMethodDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationProcedure[1]" >
<xsl:element name="evaluationProcedure" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:dateTime" >
<xsl:element name="dateTime" >
<xsl:call-template name="convertDateTime115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:result" >
<xsl:for-each select="gmd:result" >
<xsl:element name="result" >
<xsl:apply-templates select="gmd:DQ_Result" />
<xsl:apply-templates select="gmd:DQ_ConformanceResult" />
<xsl:apply-templates select="gmd:DQ_QuantitativeResult" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="result" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:measureIdentification[1]" >
<xsl:element name="measureIdentification" >
<xsl:for-each select="gmd:MD_Identifier/gmd:authority[1]" >
<xsl:element name="authority" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:MD_Identifier/gmd:code[1]" >
<xsl:element name="code" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:DQ_GriddedDataPositionalAccuracy" >
<xsl:element name="DQ_GriddedDataPositionalAccuracy" >
<xsl:for-each select="gmd:nameOfMeasure" >
<xsl:element name="nameOfMeasure" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:measureDescription[1]" >
<xsl:element name="measureDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodType[1]" >
<xsl:element name="evaluationMethodType" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodDescription[1]" >
<xsl:element name="evaluationMethodDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationProcedure[1]" >
<xsl:element name="evaluationProcedure" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:dateTime" >
<xsl:element name="dateTime" >
<xsl:call-template name="convertDateTime115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:result" >
<xsl:for-each select="gmd:result" >
<xsl:element name="result" >
<xsl:apply-templates select="gmd:DQ_Result" />
<xsl:apply-templates select="gmd:DQ_ConformanceResult" />
<xsl:apply-templates select="gmd:DQ_QuantitativeResult" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="result" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:measureIdentification[1]" >
<xsl:element name="measureIdentification" >
<xsl:for-each select="gmd:MD_Identifier/gmd:authority[1]" >
<xsl:element name="authority" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:MD_Identifier/gmd:code[1]" >
<xsl:element name="code" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:DQ_RelativeInternalPositionalAccuracy" >
<xsl:element name="DQ_RelativeInternalPositionalAccuracy" >
<xsl:for-each select="gmd:nameOfMeasure" >
<xsl:element name="nameOfMeasure" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:measureDescription[1]" >
<xsl:element name="measureDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodType[1]" >
<xsl:element name="evaluationMethodType" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodDescription[1]" >
<xsl:element name="evaluationMethodDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationProcedure[1]" >
<xsl:element name="evaluationProcedure" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:dateTime" >
<xsl:element name="dateTime" >
<xsl:call-template name="convertDateTime115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:result" >
<xsl:for-each select="gmd:result" >
<xsl:element name="result" >
<xsl:apply-templates select="gmd:DQ_Result" />
<xsl:apply-templates select="gmd:DQ_ConformanceResult" />
<xsl:apply-templates select="gmd:DQ_QuantitativeResult" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="result" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:measureIdentification[1]" >
<xsl:element name="measureIdentification" >
<xsl:for-each select="gmd:MD_Identifier/gmd:authority[1]" >
<xsl:element name="authority" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:MD_Identifier/gmd:code[1]" >
<xsl:element name="code" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:DQ_TemporalAccuracy" >
</xsl:template>
<xsl:template match="gmd:DQ_AccuracyOfATimeMeasurement" >
<xsl:element name="DQ_AccuracyOfATimeMeasurement" >
<xsl:for-each select="gmd:nameOfMeasure" >
<xsl:element name="nameOfMeasure" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:measureDescription[1]" >
<xsl:element name="measureDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodType[1]" >
<xsl:element name="evaluationMethodType" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodDescription[1]" >
<xsl:element name="evaluationMethodDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationProcedure[1]" >
<xsl:element name="evaluationProcedure" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:dateTime" >
<xsl:element name="dateTime" >
<xsl:call-template name="convertDateTime115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:result" >
<xsl:for-each select="gmd:result" >
<xsl:element name="result" >
<xsl:apply-templates select="gmd:DQ_Result" />
<xsl:apply-templates select="gmd:DQ_ConformanceResult" />
<xsl:apply-templates select="gmd:DQ_QuantitativeResult" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="result" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:measureIdentification[1]" >
<xsl:element name="measureIdentification" >
<xsl:for-each select="gmd:MD_Identifier/gmd:authority[1]" >
<xsl:element name="authority" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:MD_Identifier/gmd:code[1]" >
<xsl:element name="code" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:DQ_TemporalConsistency" >
<xsl:element name="DQ_TemporalConsistency" >
<xsl:for-each select="gmd:nameOfMeasure" >
<xsl:element name="nameOfMeasure" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:measureDescription[1]" >
<xsl:element name="measureDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodType[1]" >
<xsl:element name="evaluationMethodType" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodDescription[1]" >
<xsl:element name="evaluationMethodDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationProcedure[1]" >
<xsl:element name="evaluationProcedure" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:dateTime" >
<xsl:element name="dateTime" >
<xsl:call-template name="convertDateTime115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:result" >
<xsl:for-each select="gmd:result" >
<xsl:element name="result" >
<xsl:apply-templates select="gmd:DQ_Result" />
<xsl:apply-templates select="gmd:DQ_ConformanceResult" />
<xsl:apply-templates select="gmd:DQ_QuantitativeResult" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="result" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:measureIdentification[1]" >
<xsl:element name="measureIdentification" >
<xsl:for-each select="gmd:MD_Identifier/gmd:authority[1]" >
<xsl:element name="authority" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:MD_Identifier/gmd:code[1]" >
<xsl:element name="code" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:DQ_TemporalValidity" >
<xsl:element name="DQ_TemporalValidity" >
<xsl:for-each select="gmd:nameOfMeasure" >
<xsl:element name="nameOfMeasure" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:measureDescription[1]" >
<xsl:element name="measureDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodType[1]" >
<xsl:element name="evaluationMethodType" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodDescription[1]" >
<xsl:element name="evaluationMethodDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationProcedure[1]" >
<xsl:element name="evaluationProcedure" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:dateTime" >
<xsl:element name="dateTime" >
<xsl:call-template name="convertDateTime115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:result" >
<xsl:for-each select="gmd:result" >
<xsl:element name="result" >
<xsl:apply-templates select="gmd:DQ_Result" />
<xsl:apply-templates select="gmd:DQ_ConformanceResult" />
<xsl:apply-templates select="gmd:DQ_QuantitativeResult" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="result" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:measureIdentification[1]" >
<xsl:element name="measureIdentification" >
<xsl:for-each select="gmd:MD_Identifier/gmd:authority[1]" >
<xsl:element name="authority" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:MD_Identifier/gmd:code[1]" >
<xsl:element name="code" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:DQ_ThematicAccuracy" >
</xsl:template>
<xsl:template match="gmd:DQ_ThematicClassificationCorrectness" >
<xsl:element name="DQ_ThematicClassificationCorrectness" >
<xsl:for-each select="gmd:nameOfMeasure" >
<xsl:element name="nameOfMeasure" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:measureDescription[1]" >
<xsl:element name="measureDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodType[1]" >
<xsl:element name="evaluationMethodType" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodDescription[1]" >
<xsl:element name="evaluationMethodDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationProcedure[1]" >
<xsl:element name="evaluationProcedure" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:dateTime" >
<xsl:element name="dateTime" >
<xsl:call-template name="convertDateTime115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:result" >
<xsl:for-each select="gmd:result" >
<xsl:element name="result" >
<xsl:apply-templates select="gmd:DQ_Result" />
<xsl:apply-templates select="gmd:DQ_ConformanceResult" />
<xsl:apply-templates select="gmd:DQ_QuantitativeResult" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="result" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:measureIdentification[1]" >
<xsl:element name="measureIdentification" >
<xsl:for-each select="gmd:MD_Identifier/gmd:authority[1]" >
<xsl:element name="authority" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:MD_Identifier/gmd:code[1]" >
<xsl:element name="code" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:DQ_NonQuantitativeAttributeAccuracy" >
<xsl:element name="DQ_NonQuantitativeAttributeAccuracy" >
<xsl:for-each select="gmd:nameOfMeasure" >
<xsl:element name="nameOfMeasure" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:measureDescription[1]" >
<xsl:element name="measureDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodType[1]" >
<xsl:element name="evaluationMethodType" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodDescription[1]" >
<xsl:element name="evaluationMethodDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationProcedure[1]" >
<xsl:element name="evaluationProcedure" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:dateTime" >
<xsl:element name="dateTime" >
<xsl:call-template name="convertDateTime115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:result" >
<xsl:for-each select="gmd:result" >
<xsl:element name="result" >
<xsl:apply-templates select="gmd:DQ_Result" />
<xsl:apply-templates select="gmd:DQ_ConformanceResult" />
<xsl:apply-templates select="gmd:DQ_QuantitativeResult" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="result" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:measureIdentification[1]" >
<xsl:element name="measureIdentification" >
<xsl:for-each select="gmd:MD_Identifier/gmd:authority[1]" >
<xsl:element name="authority" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:MD_Identifier/gmd:code[1]" >
<xsl:element name="code" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:DQ_QuantitativeAttributeAccuracy" >
<xsl:element name="DQ_QuantitativeAttributeAccuracy" >
<xsl:for-each select="gmd:nameOfMeasure" >
<xsl:element name="nameOfMeasure" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:measureDescription[1]" >
<xsl:element name="measureDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodType[1]" >
<xsl:element name="evaluationMethodType" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationMethodDescription[1]" >
<xsl:element name="evaluationMethodDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:evaluationProcedure[1]" >
<xsl:element name="evaluationProcedure" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:dateTime" >
<xsl:element name="dateTime" >
<xsl:call-template name="convertDateTime115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:result" >
<xsl:for-each select="gmd:result" >
<xsl:element name="result" >
<xsl:apply-templates select="gmd:DQ_Result" />
<xsl:apply-templates select="gmd:DQ_ConformanceResult" />
<xsl:apply-templates select="gmd:DQ_QuantitativeResult" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="result" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:measureIdentification[1]" >
<xsl:element name="measureIdentification" >
<xsl:for-each select="gmd:MD_Identifier/gmd:authority[1]" >
<xsl:element name="authority" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:MD_Identifier/gmd:code[1]" >
<xsl:element name="code" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:DQ_Result" >
</xsl:template>
<xsl:template match="gmd:DQ_ConformanceResult" >
<xsl:element name="DQ_ConformanceResult" >
<xsl:choose>
<xsl:when test="gmd:specification" >
<xsl:for-each select="gmd:specification[1]" >
<xsl:element name="specification" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="specification" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:explanation" >
<xsl:for-each select="gmd:explanation[1]" >
<xsl:element name="explanation" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="explanation" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:pass" >
<xsl:for-each select="gmd:pass[1]" >
<xsl:element name="pass" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="pass" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:DQ_Scope" >
<xsl:element name="DQ_Scope" >
<xsl:choose>
<xsl:when test="gmd:level" >
<xsl:for-each select="gmd:level[1]" >
<xsl:element name="level" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="level" >

<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:extent[1]" >
<xsl:element name="extent" >
<xsl:apply-templates select="gmd:EX_Extent" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:levelDescription" >
<xsl:element name="levelDescription" >
<xsl:apply-templates select="gmd:MD_ScopeDescription" />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_MaintenanceInformation" >
<xsl:element name="MD_MaintenanceInformation" >
<xsl:choose>
<xsl:when test="gmd:maintenanceAndUpdateFrequency" >
<xsl:for-each select="gmd:maintenanceAndUpdateFrequency[1]" >
<xsl:element name="maintenanceAndUpdateFrequency" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="maintenanceAndUpdateFrequency" >

<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:dateOfNextUpdate[1]" >
<xsl:element name="dateOfNextUpdate" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:userDefinedMaintenanceFrequency[1]" >
<xsl:element name="userDefinedMaintenanceFrequency" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:updateScope" >
<xsl:element name="updateScope" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:updateScopeDescription" >
<xsl:element name="updateScopeDescription" >
<xsl:apply-templates select="gmd:MD_ScopeDescription" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:maintenanceNote" >
<xsl:element name="maintenanceNote" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:contact" >
<xsl:element name="contact" >
<xsl:apply-templates select="gmd:CI_ResponsibleParty" />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_ScopeDescription" >
<xsl:element name="MD_ScopeDescription" >
<xsl:for-each select="gmd:attributes[1]" >
<xsl:element name="attributes" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:features[1]" >
<xsl:element name="features" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:featureInstances[1]" >
<xsl:element name="featureInstances" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:attributeInstances[1]" >
<xsl:element name="attributeInstances" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:dataset[1]" >
<xsl:element name="dataset" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:other[1]" >
<xsl:element name="other" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_SpatialRepresentation" >
</xsl:template>
<xsl:template match="gmd:MD_Georectified" >
<xsl:element name="MD_Georectified" >
<xsl:choose>
<xsl:when test="gmd:numberOfDimensions" >
<xsl:for-each select="gmd:numberOfDimensions[1]" >
<xsl:element name="numberOfDimensions" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="numberOfDimensions" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:cellGeometry" >
<xsl:for-each select="gmd:cellGeometry[1]" >
<xsl:element name="cellGeometry" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="cellGeometry" >

<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:transformationParameterAvailability" >
<xsl:for-each select="gmd:transformationParameterAvailability[1]" >
<xsl:element name="transformationParameterAvailability" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="transformationParameterAvailability" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:element name="axisDimensionProperties" >
<xsl:choose>
<xsl:when test="gmd:axisDimensionProperties" >
<xsl:for-each select="gmd:axisDimensionProperties" >
<xsl:element name="MD_Dimension" >
<xsl:for-each select="gmd:MD_Dimension/gmd:resolution/gco:Measure[1]" >
<xsl:element name="resolution" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:MD_Dimension/gmd:dimensionName" >
<xsl:for-each select="gmd:MD_Dimension/gmd:dimensionName[1]" >
<xsl:element name="dimensionName" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="dimensionName" >

<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:MD_Dimension/gmd:dimensionSize" >
<xsl:for-each select="gmd:MD_Dimension/gmd:dimensionSize[1]" >
<xsl:element name="dimensionSize" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="dimensionSize" >

<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="MD_Dimension" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
<xsl:choose>
<xsl:when test="gmd:checkPointAvailability" >
<xsl:for-each select="gmd:checkPointAvailability[1]" >
<xsl:element name="checkPointAvailability" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="checkPointAvailability" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:checkPointDescription[1]" >
<xsl:element name="checkPointDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:cornerPoints" >
<xsl:for-each select="gmd:cornerPoints[1]" >
<xsl:element name="cornerPoints" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="cornerPoints" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:centerPoint[1]" >
<xsl:element name="centerPoint" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:pointInPixel" >
<xsl:for-each select="gmd:pointInPixel[1]" >
<xsl:element name="pointInPixel" >
<xsl:apply-templates select="gmd:MD_PixelOrientationCode" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="pointInPixel" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:transformationDimensionDescription[1]" >
<xsl:element name="transformationDimensionDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:transformationDimensionMapping" >
<xsl:element name="transformationDimensionMapping" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_Georeferenceable" >
<xsl:element name="MD_Georeferenceable" >
<xsl:choose>
<xsl:when test="gmd:numberOfDimensions" >
<xsl:for-each select="gmd:numberOfDimensions[1]" >
<xsl:element name="numberOfDimensions" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="numberOfDimensions" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:cellGeometry" >
<xsl:for-each select="gmd:cellGeometry[1]" >
<xsl:element name="cellGeometry" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="cellGeometry" >

<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:transformationParameterAvailability" >
<xsl:for-each select="gmd:transformationParameterAvailability[1]" >
<xsl:element name="transformationParameterAvailability" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="transformationParameterAvailability" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:element name="axisDimensionProperties" >
<xsl:choose>
<xsl:when test="gmd:axisDimensionProperties" >
<xsl:for-each select="gmd:axisDimensionProperties" >
<xsl:element name="MD_Dimension" >
<xsl:for-each select="gmd:MD_Dimension/gmd:resolution/gco:Measure[1]" >
<xsl:element name="resolution" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:MD_Dimension/gmd:dimensionName" >
<xsl:for-each select="gmd:MD_Dimension/gmd:dimensionName[1]" >
<xsl:element name="dimensionName" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="dimensionName" >

<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:MD_Dimension/gmd:dimensionSize" >
<xsl:for-each select="gmd:MD_Dimension/gmd:dimensionSize[1]" >
<xsl:element name="dimensionSize" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="dimensionSize" >

<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="MD_Dimension" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
<xsl:choose>
<xsl:when test="gmd:controlPointAvailability" >
<xsl:for-each select="gmd:controlPointAvailability[1]" >
<xsl:element name="controlPointAvailability" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="controlPointAvailability" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:orientationParameterAvailability" >
<xsl:for-each select="gmd:orientationParameterAvailability[1]" >
<xsl:element name="orientationParameterAvailability" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="orientationParameterAvailability" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:orientationParameterDescription[1]" >
<xsl:element name="orientationParameterDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:georeferencedParameters" >
<xsl:for-each select="gmd:georeferencedParameters[1]" >
<xsl:element name="georeferencedParameters" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="georeferencedParameters" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:parameterCitation" >
<xsl:element name="parameterCitation" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_VectorSpatialRepresentation" >
<xsl:element name="MD_VectorSpatialRepresentation" >
<xsl:for-each select="gmd:topologyLevel[1]" >
<xsl:element name="topologyLevel" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:geometricObjects" >
<xsl:element name="geometricObjects" >
<xsl:apply-templates select="gmd:MD_GeometricObjects" />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_Dimension" >
<xsl:element name="MD_Dimension" >
<xsl:choose>
<xsl:when test="gmd:dimensionName" >
<xsl:for-each select="gmd:dimensionName[1]" >
<xsl:element name="dimensionName" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="dimensionName" >

<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:dimensionSize" >
<xsl:for-each select="gmd:dimensionSize[1]" >
<xsl:element name="dimensionSize" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="dimensionSize" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:resolution[1]" >
<xsl:element name="resolution" >
<xsl:element name="gco:Measure" >
<xsl:value-of select="." />
</xsl:element>
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_GeometricObjects" >
<xsl:element name="MD_GeometricObjects" >
<xsl:choose>
<xsl:when test="gmd:geometricObjectType" >
<xsl:for-each select="gmd:geometricObjectType[1]" >
<xsl:element name="geometricObjectType" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="geometricObjectType" >

<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:geometricObjectCount[1]" >
<xsl:element name="geometricObjectCount" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_ReferenceSystem" >
<xsl:element name="MD_CRS" >
<xsl:for-each select="gmd:referenceSystemIdentifier[1]" >
<xsl:element name="referenceSystemIdentifier" >
<xsl:apply-templates select="gmd:RS_Identifier" />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:CI_Address" >
<xsl:element name="CI_Address" >
<xsl:for-each select="gmd:deliveryPoint" >
<xsl:element name="deliveryPoint" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:city[1]" >
<xsl:element name="city" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:administrativeArea[1]" >
<xsl:element name="administrativeArea" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:postalCode[1]" >
<xsl:element name="postalCode" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:country[1]" >
<xsl:element name="country" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:electronicMailAddress" >
<xsl:element name="electronicMailAddress" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:CI_Contact" >
<xsl:element name="CI_Contact" >
<xsl:for-each select="gmd:phone[1]" >
<xsl:element name="phone" >
<xsl:apply-templates select="gmd:CI_Telephone" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:address[1]" >
<xsl:element name="address" >
<xsl:apply-templates select="gmd:CI_Address" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:onlineResource[1]" >
<xsl:element name="onlineResource" >
<xsl:apply-templates select="gmd:CI_OnlineResource" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:hoursOfService[1]" >
<xsl:element name="hoursOfService" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:contactInstructions[1]" >
<xsl:element name="contactInstructions" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:CI_Date" >
<xsl:element name="CI_Date" >
<xsl:choose>
<xsl:when test="gmd:date" >
<xsl:for-each select="gmd:date[1]" >
<xsl:element name="date" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="date" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:dateType" >
<xsl:for-each select="gmd:dateType[1]" >
<xsl:element name="dateType" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="dateType" >

<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:CI_OnlineResource" >
<xsl:element name="CI_OnlineResource" >
<xsl:choose>
<xsl:when test="gmd:linkage" >
<xsl:for-each select="gmd:linkage[1]" >
<xsl:element name="linkage" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="linkage" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:protocol[1]" >
<xsl:element name="protocol" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:applicationProfile[1]" >
<xsl:element name="applicationProfile" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:name[1]" >
<xsl:element name="name" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:description[1]" >
<xsl:element name="description" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:function[1]" >
<xsl:element name="function" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:RS_ReferenceSystem" >
</xsl:template>
<xsl:template match="gmd:MD_EllipsoidParameters" >
<xsl:element name="MD_EllipsoidParameters" >
<xsl:choose>
<xsl:when test="gmd:semiMajorAxis" >
<xsl:for-each select="gmd:semiMajorAxis[1]" >
<xsl:element name="semiMajorAxis" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="semiMajorAxis" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:axisUnits" >
<xsl:for-each select="gmd:axisUnits[1]" >
<xsl:element name="axisUnits" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="axisUnits" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:denominatorOfFlatteningRatio[1]" >
<xsl:element name="denominatorOfFlatteningRatio" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_Identifier" >
<xsl:element name="MD_Identifier" >
<xsl:for-each select="gmd:authority[1]" >
<xsl:element name="authority" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:code" >
<xsl:for-each select="gmd:code[1]" >
<xsl:element name="code" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="code" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:RS_Identifier" >
<xsl:element name="RS_Identifier" >
<xsl:for-each select="gmd:authority[1]" >
<xsl:element name="authority" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:code" >
<xsl:for-each select="gmd:code[1]" >
<xsl:element name="code" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="code" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:codeSpace[1]" >
<xsl:element name="codeSpace" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:version[1]" >
<xsl:element name="version" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_ObliqueLineAzimuth" >
<xsl:element name="MD_ObliqueLineAzimuth" >
<xsl:choose>
<xsl:when test="gmd:azimuthAngle" >
<xsl:for-each select="gmd:azimuthAngle[1]" >
<xsl:element name="azimuthAngle" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="azimuthAngle" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:azimuthMeasurePointLongitude" >
<xsl:for-each select="gmd:azimuthMeasurePointLongitude[1]" >
<xsl:element name="azimuthMeasurePointLongitude" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="azimuthMeasurePointLongitude" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_ObliqueLinePoint" >
<xsl:element name="MD_ObliqueLinePoint" >
<xsl:choose>
<xsl:when test="gmd:obliqueLineLatitude" >
<xsl:for-each select="gmd:obliqueLineLatitude[1]" >
<xsl:element name="obliqueLineLatitude" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="obliqueLineLatitude" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:obliqueLineLongitude" >
<xsl:for-each select="gmd:obliqueLineLongitude[1]" >
<xsl:element name="obliqueLineLongitude" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="obliqueLineLongitude" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_ProjectionParameters" >
<xsl:element name="MD_ProjectionParameters" >
<xsl:for-each select="gmd:zone[1]" >
<xsl:element name="zone" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:standardParallel" >
<xsl:element name="standardParallel" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:longitudeOfCentralMeridian[1]" >
<xsl:element name="longitudeOfCentralMeridian" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:latitudeOfProjectionOrigin[1]" >
<xsl:element name="latitudeOfProjectionOrigin" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:falseEasting[1]" >
<xsl:element name="falseEasting" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:falseNorthing[1]" >
<xsl:element name="falseNorthing" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:falseEastingNorthingUnits[1]" >
<xsl:element name="falseEastingNorthingUnits" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:scaleFactorAtEquator[1]" >
<xsl:element name="scaleFactorAtEquator" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:heightOfProspectivePointAboveSurface[1]" >
<xsl:element name="heightOfProspectivePointAboveSurface" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:longitudeOfProjectionCenter[1]" >
<xsl:element name="longitudeOfProjectionCenter" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:latitudeOfProjectionCenter[1]" >
<xsl:element name="latitudeOfProjectionCenter" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:scaleFactorAtCenterLine[1]" >
<xsl:element name="scaleFactorAtCenterLine" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:straightVerticalLongitudeFromPole[1]" >
<xsl:element name="straightVerticalLongitudeFromPole" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:scaleFactorAtProjectionOrigin[1]" >
<xsl:element name="scaleFactorAtProjectionOrigin" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:obliqueLineAzimuthParameter[1]" >
<xsl:element name="obliqueLineAzimuthParameter" >
<xsl:apply-templates select="gmd:MD_ObliqueLineAzimuth" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:obliqueLinePointParameter" >
<xsl:element name="obliqueLinePointParameter" >
<xsl:apply-templates select="gmd:MD_ObliqueLinePoint" />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_ContentInformation" >
</xsl:template>
<xsl:template match="gmd:MD_FeatureCatalogueDescription" >
<xsl:element name="MD_FeatureCatalogueDescription" >
<xsl:for-each select="gmd:complianceCode[1]" >
<xsl:element name="complianceCode" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:language" >
<xsl:element name="language" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:includedWithDataset" >
<xsl:for-each select="gmd:includedWithDataset[1]" >
<xsl:element name="includedWithDataset" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="includedWithDataset" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:featureTypes" >
<xsl:element name="featureTypes" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:featureCatalogueCitation" >
<xsl:for-each select="gmd:featureCatalogueCitation" >
<xsl:element name="featureCatalogueCitation" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="featureCatalogueCitation" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:CI_Series" >
<xsl:element name="CI_Series" >
<xsl:for-each select="gmd:name[1]" >
<xsl:element name="name" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:issueIdentification[1]" >
<xsl:element name="issueIdentification" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:page[1]" >
<xsl:element name="page" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:CI_Telephone" >
<xsl:element name="CI_Telephone" >
<xsl:for-each select="gmd:voice" >
<xsl:element name="voice" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:facsimile" >
<xsl:element name="facsimile" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_CoverageDescription" >
<xsl:element name="MD_CoverageDescription" >
<xsl:choose>
<xsl:when test="gmd:attributeDescription" >
<xsl:for-each select="gmd:attributeDescription[1]" >
<xsl:element name="attributeDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="attributeDescription" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:contentType" >
<xsl:for-each select="gmd:contentType[1]" >
<xsl:element name="contentType" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="contentType" >

<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:dimension" >
<xsl:element name="dimension" >
<xsl:apply-templates select="gmd:MD_RangeDimension" />
<xsl:apply-templates select="gmd:MD_Band" />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_ImageDescription" >
<xsl:element name="MD_ImageDescription" >
<xsl:choose>
<xsl:when test="gmd:attributeDescription" >
<xsl:for-each select="gmd:attributeDescription[1]" >
<xsl:element name="attributeDescription" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="attributeDescription" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:contentType" >
<xsl:for-each select="gmd:contentType[1]" >
<xsl:element name="contentType" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="contentType" >

<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:dimension" >
<xsl:element name="dimension" >
<xsl:apply-templates select="gmd:MD_RangeDimension" />
<xsl:apply-templates select="gmd:MD_Band" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:illuminationElevationAngle[1]" >
<xsl:element name="illuminationElevationAngle" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:illuminationAzimuthAngle[1]" >
<xsl:element name="illuminationAzimuthAngle" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:imagingCondition[1]" >
<xsl:element name="imagingCondition" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:imageQualityCode[1]" >
<xsl:element name="imageQualityCode" >
<xsl:apply-templates select="gmd:MD_Identifier" />
<xsl:apply-templates select="gmd:RS_Identifier" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:cloudCoverPercentage[1]" >
<xsl:element name="cloudCoverPercentage" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:processingLevelCode[1]" >
<xsl:element name="processingLevelCode" >
<xsl:apply-templates select="gmd:MD_Identifier" />
<xsl:apply-templates select="gmd:RS_Identifier" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:compressionGenerationQuantity[1]" >
<xsl:element name="compressionGenerationQuantity" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:triangulationIndicator[1]" >
<xsl:element name="triangulationIndicator" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:radiometricCalibrationDataAvailability[1]" >
<xsl:element name="radiometricCalibrationDataAvailability" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:cameraCalibrationInformationAvailability[1]" >
<xsl:element name="cameraCalibrationInformationAvailability" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:filmDistortionInformationAvailability[1]" >
<xsl:element name="filmDistortionInformationAvailability" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:lensDistortionInformationAvailability[1]" >
<xsl:element name="lensDistortionInformationAvailability" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_RangeDimension" >
<xsl:element name="MD_RangeDimension" >
<xsl:for-each select="gmd:sequenceIdentifier/gco:MemberName/gco:aName[1]" >
<xsl:element name="sequenceIdentifier" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:descriptor[1]" >
<xsl:element name="descriptor" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_Band" >
<xsl:element name="MD_Band" >
<xsl:for-each select="gmd:sequenceIdentifier/gco:MemberName/gco:aName[1]" >
<xsl:element name="sequenceIdentifier" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:descriptor[1]" >
<xsl:element name="descriptor" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:maxValue[1]" >
<xsl:element name="maxValue" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:minValue[1]" >
<xsl:element name="minValue" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:units/gml:UnitDefinition/gml:identifier[1]" >
<xsl:element name="units" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:peakResponse[1]" >
<xsl:element name="peakResponse" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:bitsPerValue[1]" >
<xsl:element name="bitsPerValue" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:toneGradation[1]" >
<xsl:element name="toneGradation" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:scaleFactor[1]" >
<xsl:element name="scaleFactor" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:offset[1]" >
<xsl:element name="offset" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_PortrayalCatalogueReference" >
<xsl:element name="MD_PortrayalCatalogueReference" >
<xsl:choose>
<xsl:when test="gmd:portrayalCatalogueCitation" >
<xsl:for-each select="gmd:portrayalCatalogueCitation" >
<xsl:element name="portrayalCatalogueCitation" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="portrayalCatalogueCitation" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_Distribution" >
<xsl:element name="MD_Distribution" >
<xsl:for-each select="gmd:distributionFormat" >
<xsl:element name="distributionFormat" >
<xsl:apply-templates select="gmd:MD_Format" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:distributor" >
<xsl:element name="distributor" >
<xsl:apply-templates select="gmd:MD_Distributor" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:transferOptions" >
<xsl:element name="transferOptions" >
<xsl:apply-templates select="gmd:MD_DigitalTransferOptions" />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_DigitalTransferOptions" >
<xsl:element name="MD_DigitalTransferOptions" >
<xsl:for-each select="gmd:unitsOfDistribution[1]" >
<xsl:element name="unitsOfDistribution" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:transferSize[1]" >
<xsl:element name="transferSize" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:onLine" >
<xsl:element name="onLine" >
<xsl:apply-templates select="gmd:CI_OnlineResource" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:offLine[1]" >
<xsl:element name="offLine" >
<xsl:apply-templates select="gmd:MD_Medium" />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_Distributor" >
<xsl:element name="MD_Distributor" >
<xsl:choose>
<xsl:when test="gmd:distributorContact" >
<xsl:for-each select="gmd:distributorContact[1]" >
<xsl:element name="distributorContact" >
<xsl:apply-templates select="gmd:CI_ResponsibleParty" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="distributorContact" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:distributionOrderProcess" >
<xsl:element name="distributionOrderProcess" >
<xsl:apply-templates select="gmd:MD_StandardOrderProcess" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:distributorFormat" >
<xsl:element name="distributorFormat" >
<xsl:apply-templates select="gmd:MD_Format" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:distributorTransferOptions" >
<xsl:element name="distributorTransferOptions" >
<xsl:apply-templates select="gmd:MD_DigitalTransferOptions" />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_Format" >
<xsl:element name="MD_Format" >
<xsl:choose>
<xsl:when test="gmd:name" >
<xsl:for-each select="gmd:name[1]" >
<xsl:element name="name" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="name" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:version" >
<xsl:for-each select="gmd:version[1]" >
<xsl:element name="version" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="version" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:amendmentNumber[1]" >
<xsl:element name="amendmentNumber" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:specification[1]" >
<xsl:element name="specification" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:fileDecompressionTechnique[1]" >
<xsl:element name="fileDecompressionTechnique" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:formatDistributor" >
<xsl:element name="formatDistributor" >
<xsl:apply-templates select="gmd:MD_Distributor" />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_Medium" >
<xsl:element name="MD_Medium" >
<xsl:for-each select="gmd:name[1]" >
<xsl:element name="name" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:density" >
<xsl:element name="density" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:densityUnits[1]" >
<xsl:element name="densityUnits" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:volumes[1]" >
<xsl:element name="volumes" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:mediumFormat" >
<xsl:element name="mediumFormat" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:mediumNote[1]" >
<xsl:element name="mediumNote" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_StandardOrderProcess" >
<xsl:element name="MD_StandardOrderProcess" >
<xsl:for-each select="gmd:fees[1]" >
<xsl:element name="fees" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:plannedAvailableDateTime[1]" >
<xsl:element name="plannedAvailableDateTime" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:orderingInstructions[1]" >
<xsl:element name="orderingInstructions" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:turnaround[1]" >
<xsl:element name="turnaround" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_MetadataExtensionInformation" >
<xsl:element name="MD_MetadataExtensionInformation" >
<xsl:for-each select="gmd:extensionOnLineResource[1]" >
<xsl:element name="extensionOnLineResource" >
<xsl:apply-templates select="gmd:CI_OnlineResource" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:extendedElementInformation" >
<xsl:element name="extendedElementInformation" >
<xsl:apply-templates select="gmd:MD_ExtendedElementInformation" />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_ReferenceSystem" >
<xsl:element name="MD_ReferenceSystem" >
<xsl:for-each select="gmd:referenceSystemIdentifier[1]" >
<xsl:element name="referenceSystemIdentifier" >
<xsl:apply-templates select="gmd:RS_Identifier" />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_Resolution" >
<xsl:element name="MD_Resolution" >
<xsl:for-each select="gmd:equivalentScale[1]" >
<xsl:element name="equivalentScale" >
<xsl:apply-templates select="gmd:MD_RepresentativeFraction" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:distance[1]" >
<xsl:element name="distance" >
<xsl:for-each select="gco:Distance[1]" >
<xsl:element name="value" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:element name="uom" >
<xsl:element name="uomLength" >
<xsl:for-each select="gco:Distance/@uom[1]" >
<xsl:element name="uomName" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:element>
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_ExtendedElementInformation" >
<xsl:element name="MD_ExtendedElementInformation" >
<xsl:choose>
<xsl:when test="gmd:name" >
<xsl:for-each select="gmd:name[1]" >
<xsl:element name="name" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="name" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:shortName[1]" >
<xsl:element name="shortName" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:domainCode[1]" >
<xsl:element name="domainCode" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:definition" >
<xsl:for-each select="gmd:definition[1]" >
<xsl:element name="definition" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="definition" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:obligation[1]" >
<xsl:element name="obligation" >
<xsl:apply-templates select="gmd:MD_ObligationCode" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:condition[1]" >
<xsl:element name="condition" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:dataType" >
<xsl:for-each select="gmd:dataType[1]" >
<xsl:element name="dataType" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="dataType" >

<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:maximumOccurrence[1]" >
<xsl:element name="maximumOccurrence" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:domainValue[1]" >
<xsl:element name="domainValue" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:parentEntity" >
<xsl:for-each select="gmd:parentEntity" >
<xsl:element name="parentEntity" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="parentEntity" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:rule" >
<xsl:for-each select="gmd:rule[1]" >
<xsl:element name="rule" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="rule" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:rationale" >
<xsl:element name="rationale" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:source" >
<xsl:for-each select="gmd:source" >
<xsl:element name="source" >
<xsl:apply-templates select="gmd:CI_ResponsibleParty" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="source" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_ApplicationSchemaInformation" >
<xsl:element name="MD_ApplicationSchemaInformation" >
<xsl:choose>
<xsl:when test="gmd:name" >
<xsl:for-each select="gmd:name[1]" >
<xsl:element name="name" >
<xsl:apply-templates select="gmd:CI_Citation" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="name" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:schemaLanguage" >
<xsl:for-each select="gmd:schemaLanguage[1]" >
<xsl:element name="schemaLanguage" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="schemaLanguage" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:constraintLanguage" >
<xsl:for-each select="gmd:constraintLanguage[1]" >
<xsl:element name="constraintLanguage" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="constraintLanguage" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:schemaAscii[1]" >
<xsl:element name="schemaAscii" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:graphicsFile[1]" >
<xsl:element name="graphicsFile" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:softwareDevelopmentFile[1]" >
<xsl:element name="softwareDevelopmentFile" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:softwareDevelopmentFileFormat[1]" >
<xsl:element name="softwareDevelopmentFileFormat" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:EX_Extent" >
<xsl:element name="EX_Extent" >
<xsl:for-each select="gmd:description[1]" >
<xsl:element name="description" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:geographicElement" >
<xsl:element name="geographicElement" >
<xsl:apply-templates select="gmd:EX_GeographicExtent" />
<xsl:apply-templates select="gmd:EX_BoundingPolygon" />
<xsl:apply-templates select="gmd:EX_GeographicBoundingBox" />
<xsl:apply-templates select="gmd:EX_GeographicDescription" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:temporalElement" >
<xsl:element name="temporalElement" >
<xsl:apply-templates select="gmd:EX_TemporalExtent" />
<xsl:apply-templates select="gmd:EX_SpatialTemporalExtent" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:verticalElement" >
<xsl:element name="verticalElement" >
<xsl:apply-templates select="gmd:EX_VerticalExtent" />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:EX_GeographicExtent" >
</xsl:template>
<xsl:template match="gmd:EX_GeographicExtent" >
<xsl:element name="EX_GeographicExtent" >
<xsl:for-each select="gmd:extentTypeCode[1]" >
<xsl:element name="extentTypeCode" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:DQ_Element" >
</xsl:template>
<xsl:template match="gmd:DQ_QuantitativeResult" >
<xsl:element name="DQ_QuantitativeResult" >
<xsl:for-each select="gmd:errorStatistic[1]" >
<xsl:element name="errorStatistic" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:value" >
<xsl:for-each select="gmd:value" >
<xsl:element name="value" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="value" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:valueType[1]" >
<xsl:element name="valueType" >
<xsl:for-each select="gco:RecordType[1]" >
<xsl:element name="recordType" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:for-each>
<xsl:element name="valueUnit" >
<xsl:choose>
<xsl:when test="gmd:valueUnit/gml:UnitDefinition/gml:identifier" >
<xsl:for-each select="gmd:valueUnit/gml:UnitDefinition/gml:identifier[1]" >
<xsl:element name="unitOfMeasure" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="unitOfMeasure" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:MD_GridSpatialRepresentation" >
<xsl:element name="MD_GridSpatialRepresentation" >
<xsl:choose>
<xsl:when test="gmd:numberOfDimensions" >
<xsl:for-each select="gmd:numberOfDimensions[1]" >
<xsl:element name="numberOfDimensions" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="numberOfDimensions" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:cellGeometry" >
<xsl:for-each select="gmd:cellGeometry[1]" >
<xsl:element name="cellGeometry" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="cellGeometry" >

<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:transformationParameterAvailability" >
<xsl:for-each select="gmd:transformationParameterAvailability[1]" >
<xsl:element name="transformationParameterAvailability" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="transformationParameterAvailability" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:element name="axisDimensionProperties" >
<xsl:choose>
<xsl:when test="gmd:axisDimensionProperties" >
<xsl:for-each select="gmd:axisDimensionProperties" >
<xsl:element name="MD_Dimension" >
<xsl:for-each select="gmd:MD_Dimension/gmd:resolution/gco:Measure[1]" >
<xsl:element name="resolution" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:MD_Dimension/gmd:dimensionName" >
<xsl:for-each select="gmd:MD_Dimension/gmd:dimensionName[1]" >
<xsl:element name="dimensionName" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="dimensionName" >

<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:MD_Dimension/gmd:dimensionSize" >
<xsl:for-each select="gmd:MD_Dimension/gmd:dimensionSize[1]" >
<xsl:element name="dimensionSize" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="dimensionSize" >

<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="MD_Dimension" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:EX_BoundingPolygon" >
<xsl:element name="EX_BoundingPolygon" >
<xsl:for-each select="gmd:extentTypeCode[1]" >
<xsl:element name="extentTypeCode" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:polygon" >
<xsl:for-each select="gmd:polygon" >
<xsl:element name="polygon" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="polygon" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:EX_GeographicBoundingBox" >
<xsl:element name="EX_GeographicBoundingBox" >
<xsl:for-each select="gmd:extentTypeCode[1]" >
<xsl:element name="extentTypeCode" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:westBoundLongitude" >
<xsl:for-each select="gmd:westBoundLongitude[1]" >
<xsl:element name="westBoundLongitude" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="westBoundLongitude" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:eastBoundLongitude" >
<xsl:for-each select="gmd:eastBoundLongitude[1]" >
<xsl:element name="eastBoundLongitude" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="eastBoundLongitude" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:southBoundLatitude" >
<xsl:for-each select="gmd:southBoundLatitude[1]" >
<xsl:element name="southBoundLatitude" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="southBoundLatitude" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:northBoundLatitude" >
<xsl:for-each select="gmd:northBoundLatitude[1]" >
<xsl:element name="northBoundLatitude" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="northBoundLatitude" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:EX_GeographicDescription" >
<xsl:element name="EX_GeographicDescription" >
<xsl:for-each select="gmd:extentTypeCode[1]" >
<xsl:element name="extentTypeCode" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:geographicIdentifier" >
<xsl:for-each select="gmd:geographicIdentifier[1]" >
<xsl:element name="geographicIdentifier" >
<xsl:apply-templates select="gmd:MD_Identifier" />
<xsl:apply-templates select="gmd:RS_Identifier" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="geographicIdentifier" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:EX_TemporalExtent" >
<xsl:element name="EX_TemporalExtent" >
<xsl:choose>
<xsl:when test="gmd:extent" >
<xsl:for-each select="gmd:extent[1]" >
<xsl:element name="extent" >
<xsl:apply-templates select="gml:TimePeriod" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="extent" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:EX_SpatialTemporalExtent" >
<xsl:element name="EX_SpatialTemporalExtent" >
<xsl:choose>
<xsl:when test="gmd:extent" >
<xsl:for-each select="gmd:extent[1]" >
<xsl:element name="extent" >
<xsl:apply-templates select="gml:TimePeriod" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="extent" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:spatialExtent" >
<xsl:for-each select="gmd:spatialExtent" >
<xsl:element name="spatialExtent" >
<xsl:apply-templates select="gmd:EX_GeographicExtent" />
<xsl:apply-templates select="gmd:EX_BoundingPolygon" />
<xsl:apply-templates select="gmd:EX_GeographicBoundingBox" />
<xsl:apply-templates select="gmd:EX_GeographicDescription" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="spatialExtent" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:EX_VerticalExtent" >
<xsl:element name="EX_VerticalExtent" >
<xsl:element name="verticalDatum" >
<xsl:element name="SC_VerticalDatum" >
<xsl:element name="datumID" >
<xsl:element name="RS_Identifier" >
<xsl:choose>
<xsl:when test="gmd:verticalCRS/gml:VerticalCRS/gml:verticalDatum/gml:VerticalDatum/gml:name" >
<xsl:for-each select="gmd:verticalCRS/gml:VerticalCRS/gml:verticalDatum/gml:VerticalDatum/gml:name[1]" >
<xsl:element name="code" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="code" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:element>
</xsl:element>
</xsl:element>
<xsl:choose>
<xsl:when test="gmd:minimumValue" >
<xsl:for-each select="gmd:minimumValue[1]" >
<xsl:element name="minimumValue" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="minimumValue" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:maximumValue" >
<xsl:for-each select="gmd:maximumValue[1]" >
<xsl:element name="maximumValue" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="maximumValue" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gmd:verticalCRS/gml:VerticalCRS/gml:verticalCS/gml:VerticalCS/gml:axis/gml:CoordinateSystemAxis/@gml:uom" >
<xsl:for-each select="gmd:verticalCRS/gml:VerticalCRS/gml:verticalCS/gml:VerticalCS/gml:axis/gml:CoordinateSystemAxis/@gml:uom[1]" >
<xsl:element name="unitOfMeasure" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="unitOfMeasure" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:CI_Citation" >
<xsl:element name="CI_Citation" >
<xsl:for-each select="gmd:responsibleParty" >
<xsl:element name="responsibleParty" >
<xsl:apply-templates select="gmd:CI_ResponsibleParty" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:ISBN[1]" >
<xsl:element name="ISBN" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:ISSN[1]" >
<xsl:element name="ISSN" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:title" >
<xsl:for-each select="gmd:title[1]" >
<xsl:element name="title" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="title" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:alternateTitle" >
<xsl:element name="alternateTitle" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:date" >
<xsl:for-each select="gmd:date" >
<xsl:element name="date" >
<xsl:apply-templates select="gmd:CI_Date" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="date" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:for-each select="gmd:edition[1]" >
<xsl:element name="edition" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:editionDate[1]" >
<xsl:element name="editionDate" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:identifier" >
<xsl:element name="identifier" >
<xsl:apply-templates select="gmd:MD_Identifier" />
<xsl:apply-templates select="gmd:RS_Identifier" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:citedResponsibleParty" >
<xsl:element name="citedResponsibleParty" >
<xsl:apply-templates select="gmd:CI_ResponsibleParty" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:presentationForm" >
<xsl:element name="presentationForm" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:series[1]" >
<xsl:element name="series" >
<xsl:apply-templates select="gmd:CI_Series" />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:otherCitationDetails[1]" >
<xsl:element name="otherCitationDetails" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:collectiveTitle[1]" >
<xsl:element name="collectiveTitle" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
<xsl:template match="gmd:ISBN" >
<xsl:element name="ISBN" >
<xsl:value-of select="." />
</xsl:element>
</xsl:template>
<xsl:template match="gmd:ISSN" >
<xsl:element name="ISSN" >
<xsl:value-of select="." />
</xsl:element>
</xsl:template>
<xsl:template match="gmd:CI_ResponsibleParty" >
<xsl:element name="CI_ResponsibleParty" >
<xsl:for-each select="gmd:individualName[1]" >
<xsl:element name="individualName" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:organisationName[1]" >
<xsl:element name="organisationName" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:positionName[1]" >
<xsl:element name="positionName" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
<xsl:for-each select="gmd:contactInfo[1]" >
<xsl:element name="contactInfo" >
<xsl:apply-templates select="gmd:CI_Contact" />
</xsl:element>
</xsl:for-each>
<xsl:choose>
<xsl:when test="gmd:role" >
<xsl:for-each select="gmd:role[1]" >
<xsl:element name="role" >
<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="role" >

<xsl:call-template name="codeListElement19115">
  <xsl:with-param name="param" select="." />
</xsl:call-template>
</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:template>
<xsl:template match="gml:TimePeriod" >
<xsl:element name="TimePeriod" >
<xsl:choose>
<xsl:when test="gml:begin" >
<xsl:for-each select="gml:begin[1]" >
<xsl:element name="begin" >
<xsl:apply-templates select="gml:TimeInstant" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="begin" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
<xsl:choose>
<xsl:when test="gml:end" >
<xsl:for-each select="gml:end[1]" >
<xsl:element name="end" >
<xsl:apply-templates select="gml:TimeInstant" />
</xsl:element>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<xsl:element name="end" >

</xsl:element>
</xsl:otherwise>
</xsl:choose>
</xsl:element>
</xsl:template>
<xsl:template match="gml:TimeInstant" >
<xsl:element name="TimeInstant" >
<xsl:for-each select="gml:timePosition[1]" >
<xsl:element name="timePosition" >
<xsl:value-of select="." />
</xsl:element>
</xsl:for-each>
</xsl:element>
</xsl:template>
</xsl:stylesheet>
