<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gml="http://www.opengis.net/gml" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:gmd="http://www.isotc211.org/2005/gmd" >
  <xsl:import href="externalTemplates.xsl" />
  <xsl:output method="xml" indent="yes" encoding="ISO-8859-1"/>
  <xsl:template match="/" >
    <xsl:apply-templates select="MD_Metadata" />
  </xsl:template>
  <xsl:template match="distance" >
    <xsl:element name="gco:Distance" >
      <xsl:for-each select="value" >
        <xsl:element name="gco:distance" >
          <xsl:element name="gco:Distance" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="CI_DateTypeCode" >
    <xsl:element name="gmd:CI_DateTypeCode" >
      <xsl:for-each select="creation" >
        <xsl:element name="gmd:creation" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="publication" >
        <xsl:element name="gmd:publication" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="revision" >
        <xsl:element name="gmd:revision" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="EX_TemporalElement" >
    <xsl:element name="gmd:EX_TemporalElement" >
      <xsl:choose>
        <xsl:when test="extent" >
          <xsl:for-each select="extent[1]" >
            <xsl:element name="gmd:extent" >
              <xsl:value-of select="." />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:extent" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="CI_OnLineFunctionCode" >
    <xsl:element name="gmd:CI_OnLineFunctionCode" >
      <xsl:for-each select="download" >
        <xsl:element name="gmd:download" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="information" >
        <xsl:element name="gmd:information" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="offlineAccess" >
        <xsl:element name="gmd:offlineAccess" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="order" >
        <xsl:element name="gmd:order" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="search" >
        <xsl:element name="gmd:search" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="CI_PresentationFormCode" >
    <xsl:element name="gmd:CI_PresentationFormCode" >
      <xsl:for-each select="documentDigital" >
        <xsl:element name="gmd:documentDigital" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="documentHardcopy" >
        <xsl:element name="gmd:documentHardcopy" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="imageDigital" >
        <xsl:element name="gmd:imageDigital" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="imageHardcopy" >
        <xsl:element name="gmd:imageHardcopy" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="mapDigital" >
        <xsl:element name="gmd:mapDigital" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="mapHardcopy" >
        <xsl:element name="gmd:mapHardcopy" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="modelDigital" >
        <xsl:element name="gmd:modelDigital" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="modelHardcopy" >
        <xsl:element name="gmd:modelHardcopy" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="profileDigital" >
        <xsl:element name="gmd:profileDigital" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="profileHardcopy" >
        <xsl:element name="gmd:profileHardcopy" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="tableDigital" >
        <xsl:element name="gmd:tableDigital" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="tableHardcopy" >
        <xsl:element name="gmd:tableHardcopy" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="videoDigital" >
        <xsl:element name="gmd:videoDigital" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="videoHardcopy" >
        <xsl:element name="gmd:videoHardcopy" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="CI_RoleCode" >
    <xsl:element name="gmd:CI_RoleCode" >
      <xsl:for-each select="resourceProvider" >
        <xsl:element name="gmd:resourceProvider" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="custodian" >
        <xsl:element name="gmd:custodian" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="owner" >
        <xsl:element name="gmd:owner" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="user" >
        <xsl:element name="gmd:user" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="distributor" >
        <xsl:element name="gmd:distributor" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="originator" >
        <xsl:element name="gmd:originator" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="pointOfContact" >
        <xsl:element name="gmd:pointOfContact" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="principalInvestigator" >
        <xsl:element name="gmd:principalInvestigator" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="processor" >
        <xsl:element name="gmd:processor" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="publisher" >
        <xsl:element name="gmd:publisher" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="author" >
        <xsl:element name="gmd:author" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="DQ_EvaluationMethodTypeCode" >
    <xsl:element name="gmd:DQ_EvaluationMethodTypeCode" >
      <xsl:for-each select="directInternal" >
        <xsl:element name="gmd:directInternal" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="directExternal" >
        <xsl:element name="gmd:directExternal" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="indirect" >
        <xsl:element name="gmd:indirect" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="DS_AssociationTypeCode" >
    <xsl:element name="gmd:DS_AssociationTypeCode" >
      <xsl:for-each select="crossReference" >
        <xsl:element name="gmd:crossReference" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="largerWorkCitation" >
        <xsl:element name="gmd:largerWorkCitation" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="partOfSeamlessDatabase" >
        <xsl:element name="gmd:partOfSeamlessDatabase" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="source" >
        <xsl:element name="gmd:source" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="stereoMate" >
        <xsl:element name="gmd:stereoMate" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="DS_InitiativeTypeCode" >
    <xsl:element name="gmd:DS_InitiativeTypeCode" >
      <xsl:for-each select="campaign" >
        <xsl:element name="gmd:campaign" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="collection" >
        <xsl:element name="gmd:collection" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="exercise" >
        <xsl:element name="gmd:exercise" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="experiment" >
        <xsl:element name="gmd:experiment" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="investigation" >
        <xsl:element name="gmd:investigation" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="mission" >
        <xsl:element name="gmd:mission" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="sensor" >
        <xsl:element name="gmd:sensor" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="operation" >
        <xsl:element name="gmd:operation" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="platform" >
        <xsl:element name="gmd:platform" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="process" >
        <xsl:element name="gmd:process" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="program" >
        <xsl:element name="gmd:program" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="project" >
        <xsl:element name="gmd:project" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="study" >
        <xsl:element name="gmd:study" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="task" >
        <xsl:element name="gmd:task" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="trial" >
        <xsl:element name="gmd:trial" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_CellGeometryCode" >
    <xsl:element name="gmd:MD_CellGeometryCode" >
      <xsl:for-each select="point" >
        <xsl:element name="gmd:point" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="area" >
        <xsl:element name="gmd:area" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_CharacterSetCode" >
    <xsl:element name="gmd:MD_CharacterSetCode" >
      <xsl:for-each select="ucs2" >
        <xsl:element name="gmd:ucs2" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="ucs4" >
        <xsl:element name="gmd:ucs4" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="utf7" >
        <xsl:element name="gmd:utf7" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="utf8" >
        <xsl:element name="gmd:utf8" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="utf16" >
        <xsl:element name="gmd:utf16" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="jis" >
        <xsl:element name="gmd:jis" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="shiftJIS" >
        <xsl:element name="gmd:shiftJIS" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="eucJP" >
        <xsl:element name="gmd:eucJP" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="usAscii" >
        <xsl:element name="gmd:usAscii" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="ebcdic" >
        <xsl:element name="gmd:ebcdic" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="eucKR" >
        <xsl:element name="gmd:eucKR" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="big5" >
        <xsl:element name="gmd:big5" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="GB2312" >
        <xsl:element name="gmd:GB2312" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_ClassificationCode" >
    <xsl:element name="gmd:MD_ClassificationCode" >
      <xsl:for-each select="unclassified" >
        <xsl:element name="gmd:unclassified" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="restricted" >
        <xsl:element name="gmd:restricted" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="confidential" >
        <xsl:element name="gmd:confidential" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="secret" >
        <xsl:element name="gmd:secret" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="topSecret" >
        <xsl:element name="gmd:topSecret" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_CoverageContentTypeCode" >
    <xsl:element name="gmd:MD_CoverageContentTypeCode" >
      <xsl:for-each select="image" >
        <xsl:element name="gmd:image" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="thematicClassification" >
        <xsl:element name="gmd:thematicClassification" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="physicalMeasurement" >
        <xsl:element name="gmd:physicalMeasurement" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_Metadata" >
    <gmd:MD_Metadata>
      <xsl:for-each select="fileIdentifier[1]" >
        <xsl:element name="gmd:fileIdentifier" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="language[1]" >
        <xsl:element name="gmd:language" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="characterSet[1]" >
        <xsl:element name="gmd:characterSet" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">MD_CharacterSetCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="parentIdentifier[1]" >
        <xsl:element name="gmd:parentIdentifier" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="hierarchyLevel" >
        <xsl:element name="gmd:hierarchyLevel" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">MD_ScopeCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="hierarchyLevelName" >
        <xsl:element name="gmd:hierarchyLevelName" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="contact" >
          <xsl:for-each select="contact" >
            <xsl:element name="gmd:contact" >
              <xsl:apply-templates select="CI_ResponsibleParty" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:contact" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="dateStamp" >
          <xsl:for-each select="dateStamp[1]" >
            <xsl:element name="gmd:dateStamp" >
              <xsl:element name="gco:Date" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:dateStamp" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="metadataStandardName[1]" >
        <xsl:element name="gmd:metadataStandardName" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="metadataStandardVersion[1]" >
        <xsl:element name="gmd:metadataStandardVersion" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="dataSet[1]" >
        <xsl:element name="gmd:dataSetURI" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="spatialRepresentationInfo" >
        <xsl:element name="gmd:spatialRepresentationInfo" >
          <xsl:apply-templates select="MD_SpatialRepresentation" />
          <xsl:apply-templates select="MD_Georectified" />
          <xsl:apply-templates select="MD_Georeferenceable" />
          <xsl:apply-templates select="MD_VectorSpatialRepresentation" />
          <xsl:apply-templates select="MD_GridSpatialRepresentation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="referenceSystemInfo" >
        <xsl:element name="gmd:referenceSystemInfo" >
          <xsl:apply-templates select="MD_ReferenceSystem" />
          <xsl:apply-templates select="MD_CRS" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="metadataExtensionInfo" >
        <xsl:element name="gmd:metadataExtensionInfo" >
          <xsl:apply-templates select="MD_MetadataExtensionInformation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="identificationInfo" >
          <xsl:for-each select="identificationInfo" >
            <xsl:element name="gmd:identificationInfo" >
              <xsl:apply-templates select="MD_Identification" />
              <xsl:apply-templates select="MD_DataIdentification" />
              <xsl:apply-templates select="MD_ServiceIdentification" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:identificationInfo" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="contentInfo" >
        <xsl:element name="gmd:contentInfo" >
          <xsl:apply-templates select="MD_ContentInformation" />
          <xsl:apply-templates select="MD_FeatureCatalogueDescription" />
          <xsl:apply-templates select="MD_CoverageDescription" />
          <xsl:apply-templates select="MD_ImageDescription" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="distributionInfo[1]" >
        <xsl:element name="gmd:distributionInfo" >
          <xsl:apply-templates select="MD_Distribution" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="dataQualityInfo" >
        <xsl:element name="gmd:dataQualityInfo" >
          <xsl:apply-templates select="DQ_DataQuality" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="portrayalCatalogueInfo" >
        <xsl:element name="gmd:portrayalCatalogueInfo" >
          <xsl:apply-templates select="MD_PortrayalCatalogueReference" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="metadataConstraints" >
        <xsl:element name="gmd:metadataConstraints" >
          <xsl:apply-templates select="MD_Constraints" />
          <xsl:apply-templates select="MD_LegalConstraints" />
          <xsl:apply-templates select="MD_SecurityConstraints" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="applicationSchemaInfo" >
        <xsl:element name="gmd:applicationSchemaInfo" >
          <xsl:apply-templates select="MD_ApplicationSchemaInformation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="metadataMaintenance[1]" >
        <xsl:element name="gmd:metadataMaintenance" >
          <xsl:apply-templates select="MD_MaintenanceInformation" />
        </xsl:element>
      </xsl:for-each>
    </gmd:MD_Metadata>
  </xsl:template>
  <xsl:template match="MD_DatatypeCode" >
    <xsl:element name="gmd:MD_DatatypeCode" >
      <xsl:for-each select="class" >
        <xsl:element name="gmd:class" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="codelist" >
        <xsl:element name="gmd:codelist" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="enumeration" >
        <xsl:element name="gmd:enumeration" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="codelistElement" >
        <xsl:element name="gmd:codelistElement" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="abstractClass" >
        <xsl:element name="gmd:abstractClass" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="aggregateClass" >
        <xsl:element name="gmd:aggregateClass" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="specifiedClass" >
        <xsl:element name="gmd:specifiedClass" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="datatypeClass" >
        <xsl:element name="gmd:datatypeClass" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="interfaceClass" >
        <xsl:element name="gmd:interfaceClass" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="unionClass" >
        <xsl:element name="gmd:unionClass" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="metaClass" >
        <xsl:element name="gmd:metaClass" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="typeClass" >
        <xsl:element name="gmd:typeClass" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="characterString" >
        <xsl:element name="gmd:characterString" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="integer" >
        <xsl:element name="gmd:integer" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="association" >
        <xsl:element name="gmd:association" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_DimensionTypeCode" >
    <xsl:element name="gmd:MD_DimensionTypeCode" >
      <xsl:for-each select="row" >
        <xsl:element name="gmd:row" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="column" >
        <xsl:element name="gmd:column" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="vertical" >
        <xsl:element name="gmd:vertical" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="track" >
        <xsl:element name="gmd:track" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="crossTrack" >
        <xsl:element name="gmd:crossTrack" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="line" >
        <xsl:element name="gmd:line" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="sample" >
        <xsl:element name="gmd:sample" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="time" >
        <xsl:element name="gmd:time" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_GeometricObjectTypeCode" >
    <xsl:element name="gmd:MD_GeometricObjectTypeCode" >
      <xsl:for-each select="complex" >
        <xsl:element name="gmd:complex" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="composite" >
        <xsl:element name="gmd:composite" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="curve" >
        <xsl:element name="gmd:curve" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="point" >
        <xsl:element name="gmd:point" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="solid" >
        <xsl:element name="gmd:solid" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="surface" >
        <xsl:element name="gmd:surface" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_ImagingConditionCode" >
    <xsl:element name="gmd:MD_ImagingConditionCode" >
      <xsl:for-each select="blurredImage" >
        <xsl:element name="gmd:blurredImage" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="cloud" >
        <xsl:element name="gmd:cloud" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="degradingObliquity" >
        <xsl:element name="gmd:degradingObliquity" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="fog" >
        <xsl:element name="gmd:fog" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="heavySmokeOrDust" >
        <xsl:element name="gmd:heavySmokeOrDust" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="night" >
        <xsl:element name="gmd:night" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="rain" >
        <xsl:element name="gmd:rain" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="seminDarkness" >
        <xsl:element name="gmd:seminDarkness" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="shadow" >
        <xsl:element name="gmd:shadow" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="snow" >
        <xsl:element name="gmd:snow" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="terrainMasking" >
        <xsl:element name="gmd:terrainMasking" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_KeywordTypeCode" >
    <xsl:element name="gmd:MD_KeywordTypeCode" >
      <xsl:for-each select="dicipline" >
        <xsl:element name="gmd:dicipline" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="place" >
        <xsl:element name="gmd:place" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="stratum" >
        <xsl:element name="gmd:stratum" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="temporal" >
        <xsl:element name="gmd:temporal" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="theme" >
        <xsl:element name="gmd:theme" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_MaintenanceFrequencyCode" >
    <xsl:element name="gmd:MD_MaintenanceFrequencyCode" >
      <xsl:for-each select="continual" >
        <xsl:element name="gmd:continual" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="daily" >
        <xsl:element name="gmd:daily" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="weekly" >
        <xsl:element name="gmd:weekly" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="fortnightly" >
        <xsl:element name="gmd:fortnightly" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="monthly" >
        <xsl:element name="gmd:monthly" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="quarterly" >
        <xsl:element name="gmd:quarterly" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="biannually" >
        <xsl:element name="gmd:biannually" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="annually" >
        <xsl:element name="gmd:annually" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="asNeeded" >
        <xsl:element name="gmd:asNeeded" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="irregular" >
        <xsl:element name="gmd:irregular" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="notPlanned" >
        <xsl:element name="gmd:notPlanned" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="unknown" >
        <xsl:element name="gmd:unknown" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_MediumFormatCode" >
    <xsl:element name="gmd:MD_MediumFormatCode" >
      <xsl:for-each select="cpio" >
        <xsl:element name="gmd:cpio" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="tar" >
        <xsl:element name="gmd:tar" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="highSierra" >
        <xsl:element name="gmd:highSierra" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="iso9660" >
        <xsl:element name="gmd:iso9660" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="iso9660RockRidge" >
        <xsl:element name="gmd:iso9660RockRidge" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="iso9660AppleHFS" >
        <xsl:element name="gmd:iso9660AppleHFS" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_MediumNameCode" >
    <xsl:element name="gmd:MD_MediumNameCode" >
      <xsl:for-each select="cdRom" >
        <xsl:element name="gmd:cdRom" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="dvd" >
        <xsl:element name="gmd:dvd" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="dvdRom" >
        <xsl:element name="gmd:dvdRom" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="digitalLinearTape" >
        <xsl:element name="gmd:digitalLinearTape" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="onLine" >
        <xsl:element name="gmd:onLine" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="satellite" >
        <xsl:element name="gmd:satellite" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="telephoneLink" >
        <xsl:element name="gmd:telephoneLink" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="hardcopy" >
        <xsl:element name="gmd:hardcopy" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_ObligationCode" >
    <xsl:element name="gmd:MD_ObligationCode" >
      <xsl:for-each select="mandatory" >
        <xsl:element name="gmd:mandatory" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="optional" >
        <xsl:element name="gmd:optional" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="conditional" >
        <xsl:element name="gmd:conditional" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_PixelOrientationCode" >
    <xsl:element name="gmd:MD_PixelOrientationCode" >
      <xsl:for-each select="center" >
        <xsl:element name="gmd:center" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="lowerLeft" >
        <xsl:element name="gmd:lowerLeft" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="lowerRight" >
        <xsl:element name="gmd:lowerRight" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="upperRight" >
        <xsl:element name="gmd:upperRight" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="upperLeft" >
        <xsl:element name="gmd:upperLeft" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_ProgressCode" >
    <xsl:element name="gmd:MD_ProgressCode" >
      <xsl:for-each select="completed" >
        <xsl:element name="gmd:completed" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="historicalArchive" >
        <xsl:element name="gmd:historicalArchive" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="obsolete" >
        <xsl:element name="gmd:obsolete" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="onGoing" >
        <xsl:element name="gmd:onGoing" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="planned" >
        <xsl:element name="gmd:planned" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="required" >
        <xsl:element name="gmd:required" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="underDevelopment" >
        <xsl:element name="gmd:underDevelopment" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_RestrictionCode" >
    <xsl:element name="gmd:MD_RestrictionCode" >
      <xsl:for-each select="copyright" >
        <xsl:element name="gmd:copyright" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="patent" >
        <xsl:element name="gmd:patent" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="patentPending" >
        <xsl:element name="gmd:patentPending" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="trademark" >
        <xsl:element name="gmd:trademark" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="license" >
        <xsl:element name="gmd:license" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="intellectualPropertyRights" >
        <xsl:element name="gmd:intellectualPropertyRights" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="restricted" >
        <xsl:element name="gmd:restricted" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="otherRestrictions" >
        <xsl:element name="gmd:otherRestrictions" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_ScopeCode" >
    <xsl:element name="gmd:MD_ScopeCode" >
      <xsl:for-each select="attribute" >
        <xsl:element name="gmd:attribute" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="attributeType" >
        <xsl:element name="gmd:attributeType" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="collectionHardware" >
        <xsl:element name="gmd:collectionHardware" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="collectionSession" >
        <xsl:element name="gmd:collectionSession" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="dataset" >
        <xsl:element name="gmd:dataset" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="series" >
        <xsl:element name="gmd:series" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="nonGeographicDataset" >
        <xsl:element name="gmd:nonGeographicDataset" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="dimensionGroup" >
        <xsl:element name="gmd:dimensionGroup" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="feature" >
        <xsl:element name="gmd:feature" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="featureType" >
        <xsl:element name="gmd:featureType" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="propertyType" >
        <xsl:element name="gmd:propertyType" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="fieldSession" >
        <xsl:element name="gmd:fieldSession" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="software" >
        <xsl:element name="gmd:software" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="service" >
        <xsl:element name="gmd:service" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="model" >
        <xsl:element name="gmd:model" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="tile" >
        <xsl:element name="gmd:tile" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_Identification" >
  </xsl:template>
  <xsl:template match="MD_DataIdentification" >
    <xsl:element name="gmd:MD_DataIdentification" >
      <xsl:choose>
        <xsl:when test="citation" >
          <xsl:for-each select="citation[1]" >
            <xsl:element name="gmd:citation" >
              <xsl:apply-templates select="CI_Citation" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:citation" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="abstract" >
          <xsl:for-each select="abstract[1]" >
            <xsl:element name="gmd:abstract" >
              <xsl:element name="gco:CharacterString" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:abstract" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="purpose[1]" >
        <xsl:element name="gmd:purpose" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="credit" >
        <xsl:element name="gmd:credit" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="status" >
        <xsl:element name="gmd:status" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">MD_ProgressCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="pointOfContact" >
        <xsl:element name="gmd:pointOfContact" >
          <xsl:apply-templates select="CI_ResponsibleParty" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="resourceMaintenance" >
        <xsl:element name="gmd:resourceMaintenance" >
          <xsl:apply-templates select="MD_MaintenanceInformation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="graphicOverview" >
        <xsl:element name="gmd:graphicOverview" >
          <xsl:apply-templates select="MD_BrowseGraphic" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="resourceFormat" >
        <xsl:element name="gmd:resourceFormat" >
          <xsl:apply-templates select="MD_Format" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="descriptiveKeywords" >
        <xsl:element name="gmd:descriptiveKeywords" >
          <xsl:apply-templates select="MD_Keywords" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="resourceSpecificUsage" >
        <xsl:element name="gmd:resourceSpecificUsage" >
          <xsl:apply-templates select="MD_usage" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="resourceConstraints" >
        <xsl:element name="gmd:resourceConstraints" >
          <xsl:apply-templates select="MD_Constraints" />
          <xsl:apply-templates select="MD_LegalConstraints" />
          <xsl:apply-templates select="MD_SecurityConstraints" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="aggregationInfo" >
        <xsl:element name="gmd:aggregationInfo" >
          <xsl:apply-templates select="MD_AggregateInformation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="spatialRepresentationType" >
        <xsl:element name="gmd:spatialRepresentationType" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">MD_SpatialRepresentationTypeCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="spatialResolution" >
        <xsl:element name="gmd:spatialResolution" >
          <xsl:apply-templates select="MD_Resolution" />
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="language" >
          <xsl:for-each select="language" >
            <xsl:element name="gmd:language" >
              <xsl:element name="gco:CharacterString" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:language" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="characterSet" >
        <xsl:element name="gmd:characterSet" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">MD_CharacterSetCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="topicCategory" >
        <xsl:element name="gmd:topicCategory" >
          <xsl:call-template name="putTopicCategory">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">MD_TopicCategoryCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="environmentDescription[1]" >
        <xsl:element name="gmd:environmentDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="extent" >
        <xsl:element name="gmd:extent" >
          <xsl:apply-templates select="EX_Extent" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="supplementalInformation[1]" >
        <xsl:element name="gmd:supplementalInformation" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_ServiceIdentification" >
    <xsl:element name="gmd:MD_ServiceIdentification" >
      <xsl:choose>
        <xsl:when test="citation" >
          <xsl:for-each select="citation[1]" >
            <xsl:element name="gmd:citation" >
              <xsl:apply-templates select="CI_Citation" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:citation" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="abstract" >
          <xsl:for-each select="abstract[1]" >
            <xsl:element name="gmd:abstract" >
              <xsl:element name="gco:CharacterString" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:abstract" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="purpose[1]" >
        <xsl:element name="gmd:purpose" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="credit" >
        <xsl:element name="gmd:credit" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="status" >
        <xsl:element name="gmd:status" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">MD_ProgressCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="pointOfContact" >
        <xsl:element name="gmd:pointOfContact" >
          <xsl:apply-templates select="CI_ResponsibleParty" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="resourceMaintenance" >
        <xsl:element name="gmd:resourceMaintenance" >
          <xsl:apply-templates select="MD_MaintenanceInformation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="graphicOverview" >
        <xsl:element name="gmd:graphicOverview" >
          <xsl:apply-templates select="MD_BrowseGraphic" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="resourceFormat" >
        <xsl:element name="gmd:resourceFormat" >
          <xsl:apply-templates select="MD_Format" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="descriptiveKeywords" >
        <xsl:element name="gmd:descriptiveKeywords" >
          <xsl:apply-templates select="MD_Keywords" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="resourceSpecificUsage" >
        <xsl:element name="gmd:resourceSpecificUsage" >
          <xsl:apply-templates select="MD_usage" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="resourceConstraints" >
        <xsl:element name="gmd:resourceConstraints" >
          <xsl:apply-templates select="MD_Constraints" />
          <xsl:apply-templates select="MD_LegalConstraints" />
          <xsl:apply-templates select="MD_SecurityConstraints" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="aggregationInfo" >
        <xsl:element name="gmd:aggregationInfo" >
          <xsl:apply-templates select="MD_AggregateInformation" />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_SpatialRepresentationTypeCode" >
    <xsl:element name="gmd:MD_SpatialRepresentationTypeCode" >
      <xsl:for-each select="vector" >
        <xsl:element name="gmd:vector" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="grid" >
        <xsl:element name="gmd:grid" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="textTable" >
        <xsl:element name="gmd:textTable" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="tin" >
        <xsl:element name="gmd:tin" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="stereoModel" >
        <xsl:element name="gmd:stereoModel" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="video" >
        <xsl:element name="gmd:video" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_TopicCategoryCode" >
    <xsl:element name="gmd:MD_TopicCategoryCode" >
      <xsl:for-each select="farming" >
        <xsl:element name="gmd:farming" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="biota" >
        <xsl:element name="gmd:biota" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="boundaries" >
        <xsl:element name="gmd:boundaries" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="climatologyMeteorologyAtmosphere" >
        <xsl:element name="gmd:climatologyMeteorologyAtmosphere" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="economy" >
        <xsl:element name="gmd:economy" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="elevation" >
        <xsl:element name="gmd:elevation" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="environment" >
        <xsl:element name="gmd:environment" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="geoscientificInformation" >
        <xsl:element name="gmd:geoscientificInformation" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="health" >
        <xsl:element name="gmd:health" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="imageryBaseMapsEarthCover" >
        <xsl:element name="gmd:imageryBaseMapsEarthCover" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="intelligenceMilitary" >
        <xsl:element name="gmd:intelligenceMilitary" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="inlandWaters" >
        <xsl:element name="gmd:inlandWaters" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="location" >
        <xsl:element name="gmd:location" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="oceans" >
        <xsl:element name="gmd:oceans" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="planningCadastre" >
        <xsl:element name="gmd:planningCadastre" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="society" >
        <xsl:element name="gmd:society" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="structure" >
        <xsl:element name="gmd:structure" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="transportation" >
        <xsl:element name="gmd:transportation" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="utilitiesCommunication" >
        <xsl:element name="gmd:utilitiesCommunication" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_TopologyLevelCode" >
    <xsl:element name="gmd:MD_TopologyLevelCode" >
      <xsl:for-each select="geometryOnly" >
        <xsl:element name="gmd:geometryOnly" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="topology1D" >
        <xsl:element name="gmd:topology1D" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="planarGraph" >
        <xsl:element name="gmd:planarGraph" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="fullPlanarGraph" >
        <xsl:element name="gmd:fullPlanarGraph" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="surfaceGraph" >
        <xsl:element name="gmd:surfaceGraph" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="fullSurfaceGraph" >
        <xsl:element name="gmd:fullSurfaceGraph" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="topology3D" >
        <xsl:element name="gmd:topology3D" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="fullTopology3D" >
        <xsl:element name="gmd:fullTopology3D" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="abstract" >
        <xsl:element name="gmd:abstract" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_BrowseGraphic" >
    <xsl:element name="gmd:MD_BrowseGraphic" >
      <xsl:choose>
        <xsl:when test="fileName" >
          <xsl:for-each select="fileName[1]" >
            <xsl:element name="gmd:fileName" >
              <xsl:element name="gco:CharacterString" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:fileName" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="fileDescription[1]" >
        <xsl:element name="gmd:fileDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="fileType[1]" >
        <xsl:element name="gmd:fileType" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_Keywords" >
    <xsl:element name="gmd:MD_Keywords" >
      <xsl:choose>
        <xsl:when test="keyword" >
          <xsl:for-each select="keyword" >
            <xsl:element name="gmd:keyword" >
              <xsl:element name="gco:CharacterString" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:keyword" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="type[1]" >
        <xsl:element name="gmd:type" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">MD_KeywordTypeCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="thesaurusName[1]" >
        <xsl:element name="gmd:thesaurusName" >
          <xsl:apply-templates select="CI_Citation" />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_RepresentativeFraction" >
    <xsl:element name="gmd:MD_RepresentativeFraction" >
      <xsl:choose>
        <xsl:when test="denominator" >
          <xsl:for-each select="denominator[1]" >
            <xsl:element name="gmd:denominator" >
              <xsl:element name="gco:Integer" >
                <xsl:call-template name="stringToInteger">
                  <xsl:with-param name="param" select="." />
                </xsl:call-template>
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:denominator" >
            
            <xsl:call-template name="stringToInteger">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_usage" >
    <xsl:element name="gmd:MD_Usage" >
      <xsl:choose>
        <xsl:when test="specificUsage" >
          <xsl:for-each select="specificUsage[1]" >
            <xsl:element name="gmd:specificUsage" >
              <xsl:element name="gco:CharacterString" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:specificUsage" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="usageDateTime[1]" >
        <xsl:element name="gmd:usageDateTime" >
          <xsl:element name="gco:DateTime" >
            <xsl:call-template name="convertDateTime">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="userDeterminedLimitations[1]" >
        <xsl:element name="gmd:userDeterminedLimitations" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="userContactInfo" >
          <xsl:for-each select="userContactInfo" >
            <xsl:element name="gmd:userContactInfo" >
              <xsl:apply-templates select="CI_ResponsibleParty" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:userContactInfo" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_AggregateInformation" >
    <xsl:element name="gmd:MD_AggregateInformation" >
      <xsl:for-each select="aggregateDataSetName[1]" >
        <xsl:element name="gmd:aggregateDataSetName" >
          <xsl:apply-templates select="CI_Citation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="aggregateDataSetIdentifier[1]" >
        <xsl:element name="gmd:aggregateDataSetIdentifier" >
          <xsl:apply-templates select="MD_Identifier" />
          <xsl:apply-templates select="RS_Identifier" />
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="associationType" >
          <xsl:for-each select="associationType[1]" >
            <xsl:element name="gmd:associationType" >
              <xsl:call-template name="codeListElement19139">
                <xsl:with-param name="param" select="." />
                <xsl:with-param name="namespace">gmd</xsl:with-param>
                <xsl:with-param name="listName">DS_AssociationTypeCode</xsl:with-param>
              </xsl:call-template>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:associationType" >
            
            <xsl:call-template name="codeListElement19139">
              <xsl:with-param name="param" select="." />
              <xsl:with-param name="namespace">gmd</xsl:with-param>
              <xsl:with-param name="listName">DS_AssociationTypeCode</xsl:with-param>
            </xsl:call-template>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="initiativeType[1]" >
        <xsl:element name="gmd:initiativeType" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">DS_InitiativeTypeCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_Constraints" >
    <xsl:element name="gmd:MD_Constraints" >
      <xsl:for-each select="useLimitation" >
        <xsl:element name="gmd:useLimitation" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_LegalConstraints" >
    <xsl:element name="gmd:MD_LegalConstraints" >
      <xsl:for-each select="useLimitation" >
        <xsl:element name="gmd:useLimitation" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="accessConstraints" >
        <xsl:element name="gmd:accessConstraints" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">MD_RestrictionCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="useConstraints" >
        <xsl:element name="gmd:useConstraints" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">MD_RestrictionCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="otherConstraints" >
        <xsl:element name="gmd:otherConstraints" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_SecurityConstraints" >
    <xsl:element name="gmd:MD_SecurityConstraints" >
      <xsl:for-each select="useLimitation" >
        <xsl:element name="gmd:useLimitation" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="classification" >
          <xsl:for-each select="classification[1]" >
            <xsl:element name="gmd:classification" >
              <xsl:call-template name="codeListElement19139">
                <xsl:with-param name="param" select="." />
                <xsl:with-param name="namespace">gmd</xsl:with-param>
                <xsl:with-param name="listName">MD_ClassificationCode</xsl:with-param>
              </xsl:call-template>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:classification" >
            
            <xsl:call-template name="codeListElement19139">
              <xsl:with-param name="param" select="." />
              <xsl:with-param name="namespace">gmd</xsl:with-param>
              <xsl:with-param name="listName">MD_ClassificationCode</xsl:with-param>
            </xsl:call-template>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="userNote[1]" >
        <xsl:element name="gmd:userNote" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="classificationSystem[1]" >
        <xsl:element name="gmd:classificationSystem" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="handlingDescription[1]" >
        <xsl:element name="gmd:handlingDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="DQ_DataQuality" >
    <xsl:element name="gmd:DQ_DataQuality" >
      <xsl:choose>
        <xsl:when test="scope" >
          <xsl:for-each select="scope[1]" >
            <xsl:element name="gmd:scope" >
              <xsl:apply-templates select="DQ_Scope" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:scope" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="report" >
        <xsl:element name="gmd:report" >
          <xsl:apply-templates select="DQ_Element" />
          <xsl:apply-templates select="DQ_Completeness" />
          <xsl:apply-templates select="DQ_CompletenessCommission" />
          <xsl:apply-templates select="DQ_CompletenessOmmission" />
          <xsl:apply-templates select="DQ_LogicalConsistency" />
          <xsl:apply-templates select="DQ_ConceptualConsistency" />
          <xsl:apply-templates select="DQ_DomainConsistency" />
          <xsl:apply-templates select="DQ_FormatConsistency" />
          <xsl:apply-templates select="DQ_TopologicalConsistency" />
          <xsl:apply-templates select="DQ_PositionalAccuracy" />
          <xsl:apply-templates select="DQ_AbsoluteExternalPositionalAccuracy" />
          <xsl:apply-templates select="DQ_GriddedDataPositionalAccuracy" />
          <xsl:apply-templates select="DQ_RelativeInternalPositionalAccuracy" />
          <xsl:apply-templates select="DQ_TemporalAccuracy" />
          <xsl:apply-templates select="DQ_AccuracyOfATimeMeasurement" />
          <xsl:apply-templates select="DQ_TemporalConsistency" />
          <xsl:apply-templates select="DQ_TemporalValidity" />
          <xsl:apply-templates select="DQ_ThematicAccuracy" />
          <xsl:apply-templates select="DQ_ThematicClassificationCorrectness" />
          <xsl:apply-templates select="DQ_NonQuantitativeAttributeAccuracy" />
          <xsl:apply-templates select="DQ_QuantitativeAttributeAccuracy" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="lineage[1]" >
        <xsl:element name="gmd:lineage" >
          <xsl:apply-templates select="LI_Lineage" />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="LI_Lineage" >
    <xsl:element name="gmd:LI_Lineage" >
      <xsl:for-each select="statement[1]" >
        <xsl:element name="gmd:statement" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="processStep" >
        <xsl:element name="gmd:processStep" >
          <xsl:apply-templates select="LI_ProcessStep" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="source" >
        <xsl:element name="gmd:source" >
          <xsl:apply-templates select="LI_Source" />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="LI_ProcessStep" >
    <xsl:element name="gmd:LI_ProcessStep" >
      <xsl:choose>
        <xsl:when test="description" >
          <xsl:for-each select="description[1]" >
            <xsl:element name="gmd:description" >
              <xsl:element name="gco:CharacterString" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:description" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="rationale[1]" >
        <xsl:element name="gmd:rationale" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="dateTime[1]" >
        <xsl:element name="gmd:dateTime" >
          <xsl:element name="gco:DateTime" >
            <xsl:call-template name="convertDateTime">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="processor" >
        <xsl:element name="gmd:processor" >
          <xsl:apply-templates select="CI_ResponsibleParty" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="source" >
        <xsl:element name="gmd:source" >
          <xsl:apply-templates select="LI_Source" />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="LI_Source" >
    <xsl:element name="gmd:LI_Source" >
      <xsl:for-each select="description[1]" >
        <xsl:element name="gmd:description" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="scaleDenominator[1]" >
        <xsl:element name="gmd:scaleDenominator" >
          <xsl:apply-templates select="MD_RepresentativeFraction" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="sourceReferenceSystem[1]" >
        <xsl:element name="gmd:sourceReferenceSystem" >
          <xsl:apply-templates select="MD_CRS" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="sourceCitation[1]" >
        <xsl:element name="gmd:sourceCitation" >
          <xsl:apply-templates select="CI_Citation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="sourceExtent" >
        <xsl:element name="gmd:sourceExtent" >
          <xsl:apply-templates select="EX_Extent" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="sourceStep" >
        <xsl:element name="gmd:sourceStep" >
          <xsl:apply-templates select="LI_ProcessStep" />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="DQ_Completeness" >
  </xsl:template>
  <xsl:template match="DQ_CompletenessCommission" >
    <xsl:element name="gmd:DQ_CompletenessCommission" >
      <xsl:for-each select="nameOfMeasure" >
        <xsl:element name="gmd:nameOfMeasure" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureIdentification[1]" >
        <xsl:element name="gmd:measureIdentification" >
          <xsl:element name="gmd:MD_Identifier" >
            <xsl:for-each select="authority[1]" >
              <xsl:element name="gmd:authority" >
                <xsl:apply-templates select="CI_Citation" />
              </xsl:element>
            </xsl:for-each>
            <xsl:for-each select="code[1]" >
              <xsl:element name="gmd:code" >
                <xsl:element name="gco:CharacterString" >
                  <xsl:value-of select="." />
                </xsl:element>
              </xsl:element>
            </xsl:for-each>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureDescription[1]" >
        <xsl:element name="gmd:measureDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodType[1]" >
        <xsl:element name="gmd:evaluationMethodType" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">DQ_EvaluationMethodTypeCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodDescription[1]" >
        <xsl:element name="gmd:evaluationMethodDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationProcedure[1]" >
        <xsl:element name="gmd:evaluationProcedure" >
          <xsl:apply-templates select="CI_Citation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="dateTime" >
        <xsl:element name="gmd:dateTime" >
          <xsl:element name="gco:DateTime" >
            <xsl:call-template name="convertDateTime">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="result" >
          <xsl:for-each select="result" >
            <xsl:element name="gmd:result" >
              <xsl:apply-templates select="DQ_Result" />
              <xsl:apply-templates select="DQ_ConformanceResult" />
              <xsl:apply-templates select="DQ_QuantitativeResult" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:result" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="DQ_CompletenessOmmission" >
    <xsl:element name="gmd:DQ_CompletenessOmission" >
      <xsl:for-each select="nameOfMeasure" >
        <xsl:element name="gmd:nameOfMeasure" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureIdentification[1]" >
        <xsl:element name="gmd:measureIdentification" >
          <xsl:element name="gmd:MD_Identifier" >
            <xsl:for-each select="authority[1]" >
              <xsl:element name="gmd:authority" >
                <xsl:apply-templates select="CI_Citation" />
              </xsl:element>
            </xsl:for-each>
            <xsl:for-each select="code[1]" >
              <xsl:element name="gmd:code" >
                <xsl:element name="gco:CharacterString" >
                  <xsl:value-of select="." />
                </xsl:element>
              </xsl:element>
            </xsl:for-each>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureDescription[1]" >
        <xsl:element name="gmd:measureDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodType[1]" >
        <xsl:element name="gmd:evaluationMethodType" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">DQ_EvaluationMethodTypeCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodDescription[1]" >
        <xsl:element name="gmd:evaluationMethodDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationProcedure[1]" >
        <xsl:element name="gmd:evaluationProcedure" >
          <xsl:apply-templates select="CI_Citation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="dateTime" >
        <xsl:element name="gmd:dateTime" >
          <xsl:element name="gco:DateTime" >
            <xsl:call-template name="convertDateTime">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="result" >
          <xsl:for-each select="result" >
            <xsl:element name="gmd:result" >
              <xsl:apply-templates select="DQ_Result" />
              <xsl:apply-templates select="DQ_ConformanceResult" />
              <xsl:apply-templates select="DQ_QuantitativeResult" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:result" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="DQ_LogicalConsistency" >
  </xsl:template>
  <xsl:template match="DQ_ConceptualConsistency" >
    <xsl:element name="gmd:DQ_ConceptualConsistency" >
      <xsl:for-each select="nameOfMeasure" >
        <xsl:element name="gmd:nameOfMeasure" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureIdentification[1]" >
        <xsl:element name="gmd:measureIdentification" >
          <xsl:element name="gmd:MD_Identifier" >
            <xsl:for-each select="authority[1]" >
              <xsl:element name="gmd:authority" >
                <xsl:apply-templates select="CI_Citation" />
              </xsl:element>
            </xsl:for-each>
            <xsl:for-each select="code[1]" >
              <xsl:element name="gmd:code" >
                <xsl:element name="gco:CharacterString" >
                  <xsl:value-of select="." />
                </xsl:element>
              </xsl:element>
            </xsl:for-each>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureDescription[1]" >
        <xsl:element name="gmd:measureDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodType[1]" >
        <xsl:element name="gmd:evaluationMethodType" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">DQ_EvaluationMethodTypeCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodDescription[1]" >
        <xsl:element name="gmd:evaluationMethodDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationProcedure[1]" >
        <xsl:element name="gmd:evaluationProcedure" >
          <xsl:apply-templates select="CI_Citation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="dateTime" >
        <xsl:element name="gmd:dateTime" >
          <xsl:element name="gco:DateTime" >
            <xsl:call-template name="convertDateTime">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="result" >
          <xsl:for-each select="result" >
            <xsl:element name="gmd:result" >
              <xsl:apply-templates select="DQ_Result" />
              <xsl:apply-templates select="DQ_ConformanceResult" />
              <xsl:apply-templates select="DQ_QuantitativeResult" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:result" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="DQ_DomainConsistency" >
    <xsl:element name="gmd:DQ_DomainConsistency" >
      <xsl:for-each select="nameOfMeasure" >
        <xsl:element name="gmd:nameOfMeasure" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureIdentification[1]" >
        <xsl:element name="gmd:measureIdentification" >
          <xsl:element name="gmd:MD_Identifier" >
            <xsl:for-each select="authority[1]" >
              <xsl:element name="gmd:authority" >
                <xsl:apply-templates select="CI_Citation" />
              </xsl:element>
            </xsl:for-each>
            <xsl:for-each select="code[1]" >
              <xsl:element name="gmd:code" >
                <xsl:element name="gco:CharacterString" >
                  <xsl:value-of select="." />
                </xsl:element>
              </xsl:element>
            </xsl:for-each>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureDescription[1]" >
        <xsl:element name="gmd:measureDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodType[1]" >
        <xsl:element name="gmd:evaluationMethodType" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">DQ_EvaluationMethodTypeCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodDescription[1]" >
        <xsl:element name="gmd:evaluationMethodDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationProcedure[1]" >
        <xsl:element name="gmd:evaluationProcedure" >
          <xsl:apply-templates select="CI_Citation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="dateTime" >
        <xsl:element name="gmd:dateTime" >
          <xsl:element name="gco:DateTime" >
            <xsl:call-template name="convertDateTime">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="result" >
          <xsl:for-each select="result" >
            <xsl:element name="gmd:result" >
              <xsl:apply-templates select="DQ_Result" />
              <xsl:apply-templates select="DQ_ConformanceResult" />
              <xsl:apply-templates select="DQ_QuantitativeResult" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:result" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="DQ_FormatConsistency" >
    <xsl:element name="gmd:DQ_FormatConsistency" >
      <xsl:for-each select="nameOfMeasure" >
        <xsl:element name="gmd:nameOfMeasure" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureIdentification[1]" >
        <xsl:element name="gmd:measureIdentification" >
          <xsl:element name="gmd:MD_Identifier" >
            <xsl:for-each select="authority[1]" >
              <xsl:element name="gmd:authority" >
                <xsl:apply-templates select="CI_Citation" />
              </xsl:element>
            </xsl:for-each>
            <xsl:for-each select="code[1]" >
              <xsl:element name="gmd:code" >
                <xsl:element name="gco:CharacterString" >
                  <xsl:value-of select="." />
                </xsl:element>
              </xsl:element>
            </xsl:for-each>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureDescription[1]" >
        <xsl:element name="gmd:measureDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodType[1]" >
        <xsl:element name="gmd:evaluationMethodType" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">DQ_EvaluationMethodTypeCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodDescription[1]" >
        <xsl:element name="gmd:evaluationMethodDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationProcedure[1]" >
        <xsl:element name="gmd:evaluationProcedure" >
          <xsl:apply-templates select="CI_Citation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="dateTime" >
        <xsl:element name="gmd:dateTime" >
          <xsl:element name="gco:DateTime" >
            <xsl:call-template name="convertDateTime">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="result" >
          <xsl:for-each select="result" >
            <xsl:element name="gmd:result" >
              <xsl:apply-templates select="DQ_Result" />
              <xsl:apply-templates select="DQ_ConformanceResult" />
              <xsl:apply-templates select="DQ_QuantitativeResult" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:result" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="DQ_TopologicalConsistency" >
    <xsl:element name="gmd:DQ_TopologicalConsistency" >
      <xsl:for-each select="nameOfMeasure" >
        <xsl:element name="gmd:nameOfMeasure" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureIdentification[1]" >
        <xsl:element name="gmd:measureIdentification" >
          <xsl:element name="gmd:MD_Identifier" >
            <xsl:for-each select="authority[1]" >
              <xsl:element name="gmd:authority" >
                <xsl:apply-templates select="CI_Citation" />
              </xsl:element>
            </xsl:for-each>
            <xsl:for-each select="code[1]" >
              <xsl:element name="gmd:code" >
                <xsl:element name="gco:CharacterString" >
                  <xsl:value-of select="." />
                </xsl:element>
              </xsl:element>
            </xsl:for-each>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureDescription[1]" >
        <xsl:element name="gmd:measureDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodType[1]" >
        <xsl:element name="gmd:evaluationMethodType" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">DQ_EvaluationMethodTypeCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodDescription[1]" >
        <xsl:element name="gmd:evaluationMethodDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationProcedure[1]" >
        <xsl:element name="gmd:evaluationProcedure" >
          <xsl:apply-templates select="CI_Citation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="dateTime" >
        <xsl:element name="gmd:dateTime" >
          <xsl:element name="gco:DateTime" >
            <xsl:call-template name="convertDateTime">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="result" >
          <xsl:for-each select="result" >
            <xsl:element name="gmd:result" >
              <xsl:apply-templates select="DQ_Result" />
              <xsl:apply-templates select="DQ_ConformanceResult" />
              <xsl:apply-templates select="DQ_QuantitativeResult" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:result" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="DQ_PositionalAccuracy" >
  </xsl:template>
  <xsl:template match="DQ_AbsoluteExternalPositionalAccuracy" >
    <xsl:element name="gmd:DQ_AbsoluteExternalPositionalAccuracy" >
      <xsl:for-each select="nameOfMeasure" >
        <xsl:element name="gmd:nameOfMeasure" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureIdentification[1]" >
        <xsl:element name="gmd:measureIdentification" >
          <xsl:element name="gmd:MD_Identifier" >
            <xsl:for-each select="authority[1]" >
              <xsl:element name="gmd:authority" >
                <xsl:apply-templates select="CI_Citation" />
              </xsl:element>
            </xsl:for-each>
            <xsl:for-each select="code[1]" >
              <xsl:element name="gmd:code" >
                <xsl:element name="gco:CharacterString" >
                  <xsl:value-of select="." />
                </xsl:element>
              </xsl:element>
            </xsl:for-each>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureDescription[1]" >
        <xsl:element name="gmd:measureDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodType[1]" >
        <xsl:element name="gmd:evaluationMethodType" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">DQ_EvaluationMethodTypeCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodDescription[1]" >
        <xsl:element name="gmd:evaluationMethodDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationProcedure[1]" >
        <xsl:element name="gmd:evaluationProcedure" >
          <xsl:apply-templates select="CI_Citation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="dateTime" >
        <xsl:element name="gmd:dateTime" >
          <xsl:element name="gco:DateTime" >
            <xsl:call-template name="convertDateTime">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="result" >
          <xsl:for-each select="result" >
            <xsl:element name="gmd:result" >
              <xsl:apply-templates select="DQ_Result" />
              <xsl:apply-templates select="DQ_ConformanceResult" />
              <xsl:apply-templates select="DQ_QuantitativeResult" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:result" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="DQ_GriddedDataPositionalAccuracy" >
    <xsl:element name="gmd:DQ_GriddedDataPositionalAccuracy" >
      <xsl:for-each select="nameOfMeasure" >
        <xsl:element name="gmd:nameOfMeasure" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureIdentification[1]" >
        <xsl:element name="gmd:measureIdentification" >
          <xsl:element name="gmd:MD_Identifier" >
            <xsl:for-each select="authority[1]" >
              <xsl:element name="gmd:authority" >
                <xsl:apply-templates select="CI_Citation" />
              </xsl:element>
            </xsl:for-each>
            <xsl:for-each select="code[1]" >
              <xsl:element name="gmd:code" >
                <xsl:element name="gco:CharacterString" >
                  <xsl:value-of select="." />
                </xsl:element>
              </xsl:element>
            </xsl:for-each>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureDescription[1]" >
        <xsl:element name="gmd:measureDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodType[1]" >
        <xsl:element name="gmd:evaluationMethodType" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">DQ_EvaluationMethodTypeCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodDescription[1]" >
        <xsl:element name="gmd:evaluationMethodDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationProcedure[1]" >
        <xsl:element name="gmd:evaluationProcedure" >
          <xsl:apply-templates select="CI_Citation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="dateTime" >
        <xsl:element name="gmd:dateTime" >
          <xsl:element name="gco:DateTime" >
            <xsl:call-template name="convertDateTime">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="result" >
          <xsl:for-each select="result" >
            <xsl:element name="gmd:result" >
              <xsl:apply-templates select="DQ_Result" />
              <xsl:apply-templates select="DQ_ConformanceResult" />
              <xsl:apply-templates select="DQ_QuantitativeResult" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:result" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="DQ_RelativeInternalPositionalAccuracy" >
    <xsl:element name="gmd:DQ_RelativeInternalPositionalAccuracy" >
      <xsl:for-each select="nameOfMeasure" >
        <xsl:element name="gmd:nameOfMeasure" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureIdentification[1]" >
        <xsl:element name="gmd:measureIdentification" >
          <xsl:element name="gmd:MD_Identifier" >
            <xsl:for-each select="authority[1]" >
              <xsl:element name="gmd:authority" >
                <xsl:apply-templates select="CI_Citation" />
              </xsl:element>
            </xsl:for-each>
            <xsl:for-each select="code[1]" >
              <xsl:element name="gmd:code" >
                <xsl:element name="gco:CharacterString" >
                  <xsl:value-of select="." />
                </xsl:element>
              </xsl:element>
            </xsl:for-each>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureDescription[1]" >
        <xsl:element name="gmd:measureDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodType[1]" >
        <xsl:element name="gmd:evaluationMethodType" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">DQ_EvaluationMethodTypeCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodDescription[1]" >
        <xsl:element name="gmd:evaluationMethodDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationProcedure[1]" >
        <xsl:element name="gmd:evaluationProcedure" >
          <xsl:apply-templates select="CI_Citation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="dateTime" >
        <xsl:element name="gmd:dateTime" >
          <xsl:element name="gco:DateTime" >
            <xsl:call-template name="convertDateTime">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="result" >
          <xsl:for-each select="result" >
            <xsl:element name="gmd:result" >
              <xsl:apply-templates select="DQ_Result" />
              <xsl:apply-templates select="DQ_ConformanceResult" />
              <xsl:apply-templates select="DQ_QuantitativeResult" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:result" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="DQ_TemporalAccuracy" >
  </xsl:template>
  <xsl:template match="DQ_AccuracyOfATimeMeasurement" >
    <xsl:element name="gmd:DQ_AccuracyOfATimeMeasurement" >
      <xsl:for-each select="nameOfMeasure" >
        <xsl:element name="gmd:nameOfMeasure" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureIdentification[1]" >
        <xsl:element name="gmd:measureIdentification" >
          <xsl:element name="gmd:MD_Identifier" >
            <xsl:for-each select="authority[1]" >
              <xsl:element name="gmd:authority" >
                <xsl:apply-templates select="CI_Citation" />
              </xsl:element>
            </xsl:for-each>
            <xsl:for-each select="code[1]" >
              <xsl:element name="gmd:code" >
                <xsl:element name="gco:CharacterString" >
                  <xsl:value-of select="." />
                </xsl:element>
              </xsl:element>
            </xsl:for-each>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureDescription[1]" >
        <xsl:element name="gmd:measureDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodType[1]" >
        <xsl:element name="gmd:evaluationMethodType" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">DQ_EvaluationMethodTypeCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodDescription[1]" >
        <xsl:element name="gmd:evaluationMethodDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationProcedure[1]" >
        <xsl:element name="gmd:evaluationProcedure" >
          <xsl:apply-templates select="CI_Citation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="dateTime" >
        <xsl:element name="gmd:dateTime" >
          <xsl:element name="gco:DateTime" >
            <xsl:call-template name="convertDateTime">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="result" >
          <xsl:for-each select="result" >
            <xsl:element name="gmd:result" >
              <xsl:apply-templates select="DQ_Result" />
              <xsl:apply-templates select="DQ_ConformanceResult" />
              <xsl:apply-templates select="DQ_QuantitativeResult" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:result" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="DQ_TemporalConsistency" >
    <xsl:element name="gmd:DQ_TemporalConsistency" >
      <xsl:for-each select="nameOfMeasure" >
        <xsl:element name="gmd:nameOfMeasure" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureIdentification[1]" >
        <xsl:element name="gmd:measureIdentification" >
          <xsl:element name="gmd:MD_Identifier" >
            <xsl:for-each select="authority[1]" >
              <xsl:element name="gmd:authority" >
                <xsl:apply-templates select="CI_Citation" />
              </xsl:element>
            </xsl:for-each>
            <xsl:for-each select="code[1]" >
              <xsl:element name="gmd:code" >
                <xsl:element name="gco:CharacterString" >
                  <xsl:value-of select="." />
                </xsl:element>
              </xsl:element>
            </xsl:for-each>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureDescription[1]" >
        <xsl:element name="gmd:measureDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodType[1]" >
        <xsl:element name="gmd:evaluationMethodType" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">DQ_EvaluationMethodTypeCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodDescription[1]" >
        <xsl:element name="gmd:evaluationMethodDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationProcedure[1]" >
        <xsl:element name="gmd:evaluationProcedure" >
          <xsl:apply-templates select="CI_Citation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="dateTime" >
        <xsl:element name="gmd:dateTime" >
          <xsl:element name="gco:DateTime" >
            <xsl:call-template name="convertDateTime">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="result" >
          <xsl:for-each select="result" >
            <xsl:element name="gmd:result" >
              <xsl:apply-templates select="DQ_Result" />
              <xsl:apply-templates select="DQ_ConformanceResult" />
              <xsl:apply-templates select="DQ_QuantitativeResult" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:result" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="DQ_TemporalValidity" >
    <xsl:element name="gmd:DQ_TemporalValidity" >
      <xsl:for-each select="nameOfMeasure" >
        <xsl:element name="gmd:nameOfMeasure" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureIdentification[1]" >
        <xsl:element name="gmd:measureIdentification" >
          <xsl:element name="gmd:MD_Identifier" >
            <xsl:for-each select="authority[1]" >
              <xsl:element name="gmd:authority" >
                <xsl:apply-templates select="CI_Citation" />
              </xsl:element>
            </xsl:for-each>
            <xsl:for-each select="code[1]" >
              <xsl:element name="gmd:code" >
                <xsl:element name="gco:CharacterString" >
                  <xsl:value-of select="." />
                </xsl:element>
              </xsl:element>
            </xsl:for-each>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureDescription[1]" >
        <xsl:element name="gmd:measureDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodType[1]" >
        <xsl:element name="gmd:evaluationMethodType" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">DQ_EvaluationMethodTypeCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodDescription[1]" >
        <xsl:element name="gmd:evaluationMethodDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationProcedure[1]" >
        <xsl:element name="gmd:evaluationProcedure" >
          <xsl:apply-templates select="CI_Citation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="dateTime" >
        <xsl:element name="gmd:dateTime" >
          <xsl:element name="gco:DateTime" >
            <xsl:call-template name="convertDateTime">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="result" >
          <xsl:for-each select="result" >
            <xsl:element name="gmd:result" >
              <xsl:apply-templates select="DQ_Result" />
              <xsl:apply-templates select="DQ_ConformanceResult" />
              <xsl:apply-templates select="DQ_QuantitativeResult" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:result" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="DQ_ThematicAccuracy" >
  </xsl:template>
  <xsl:template match="DQ_ThematicClassificationCorrectness" >
    <xsl:element name="gmd:DQ_ThematicClassificationCorrectness" >
      <xsl:for-each select="nameOfMeasure" >
        <xsl:element name="gmd:nameOfMeasure" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureIdentification[1]" >
        <xsl:element name="gmd:measureIdentification" >
          <xsl:element name="gmd:MD_Identifier" >
            <xsl:for-each select="authority[1]" >
              <xsl:element name="gmd:authority" >
                <xsl:apply-templates select="CI_Citation" />
              </xsl:element>
            </xsl:for-each>
            <xsl:for-each select="code[1]" >
              <xsl:element name="gmd:code" >
                <xsl:element name="gco:CharacterString" >
                  <xsl:value-of select="." />
                </xsl:element>
              </xsl:element>
            </xsl:for-each>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureDescription[1]" >
        <xsl:element name="gmd:measureDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodType[1]" >
        <xsl:element name="gmd:evaluationMethodType" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">DQ_EvaluationMethodTypeCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodDescription[1]" >
        <xsl:element name="gmd:evaluationMethodDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationProcedure[1]" >
        <xsl:element name="gmd:evaluationProcedure" >
          <xsl:apply-templates select="CI_Citation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="dateTime" >
        <xsl:element name="gmd:dateTime" >
          <xsl:element name="gco:DateTime" >
            <xsl:call-template name="convertDateTime">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="result" >
          <xsl:for-each select="result" >
            <xsl:element name="gmd:result" >
              <xsl:apply-templates select="DQ_Result" />
              <xsl:apply-templates select="DQ_ConformanceResult" />
              <xsl:apply-templates select="DQ_QuantitativeResult" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:result" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="DQ_NonQuantitativeAttributeAccuracy" >
    <xsl:element name="gmd:DQ_NonQuantitativeAttributeAccuracy" >
      <xsl:for-each select="nameOfMeasure" >
        <xsl:element name="gmd:nameOfMeasure" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureIdentification[1]" >
        <xsl:element name="gmd:measureIdentification" >
          <xsl:element name="gmd:MD_Identifier" >
            <xsl:for-each select="authority[1]" >
              <xsl:element name="gmd:authority" >
                <xsl:apply-templates select="CI_Citation" />
              </xsl:element>
            </xsl:for-each>
            <xsl:for-each select="code[1]" >
              <xsl:element name="gmd:code" >
                <xsl:element name="gco:CharacterString" >
                  <xsl:value-of select="." />
                </xsl:element>
              </xsl:element>
            </xsl:for-each>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureDescription[1]" >
        <xsl:element name="gmd:measureDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodType[1]" >
        <xsl:element name="gmd:evaluationMethodType" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">DQ_EvaluationMethodTypeCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodDescription[1]" >
        <xsl:element name="gmd:evaluationMethodDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationProcedure[1]" >
        <xsl:element name="gmd:evaluationProcedure" >
          <xsl:apply-templates select="CI_Citation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="dateTime" >
        <xsl:element name="gmd:dateTime" >
          <xsl:element name="gco:DateTime" >
            <xsl:call-template name="convertDateTime">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="result" >
          <xsl:for-each select="result" >
            <xsl:element name="gmd:result" >
              <xsl:apply-templates select="DQ_Result" />
              <xsl:apply-templates select="DQ_ConformanceResult" />
              <xsl:apply-templates select="DQ_QuantitativeResult" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:result" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="DQ_QuantitativeAttributeAccuracy" >
    <xsl:element name="gmd:DQ_QuantitativeAttributeAccuracy" >
      <xsl:for-each select="nameOfMeasure" >
        <xsl:element name="gmd:nameOfMeasure" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureIdentification[1]" >
        <xsl:element name="gmd:measureIdentification" >
          <xsl:element name="gmd:MD_Identifier" >
            <xsl:for-each select="authority[1]" >
              <xsl:element name="gmd:authority" >
                <xsl:apply-templates select="CI_Citation" />
              </xsl:element>
            </xsl:for-each>
            <xsl:for-each select="code[1]" >
              <xsl:element name="gmd:code" >
                <xsl:element name="gco:CharacterString" >
                  <xsl:value-of select="." />
                </xsl:element>
              </xsl:element>
            </xsl:for-each>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="measureDescription[1]" >
        <xsl:element name="gmd:measureDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodType[1]" >
        <xsl:element name="gmd:evaluationMethodType" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">DQ_EvaluationMethodTypeCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationMethodDescription[1]" >
        <xsl:element name="gmd:evaluationMethodDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="evaluationProcedure[1]" >
        <xsl:element name="gmd:evaluationProcedure" >
          <xsl:apply-templates select="CI_Citation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="dateTime" >
        <xsl:element name="gmd:dateTime" >
          <xsl:element name="gco:DateTime" >
            <xsl:call-template name="convertDateTime">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="result" >
          <xsl:for-each select="result" >
            <xsl:element name="gmd:result" >
              <xsl:apply-templates select="DQ_Result" />
              <xsl:apply-templates select="DQ_ConformanceResult" />
              <xsl:apply-templates select="DQ_QuantitativeResult" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:result" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="DQ_Result" >
  </xsl:template>
  <xsl:template match="DQ_ConformanceResult" >
    <xsl:element name="gmd:DQ_ConformanceResult" >
      <xsl:choose>
        <xsl:when test="specification" >
          <xsl:for-each select="specification[1]" >
            <xsl:element name="gmd:specification" >
              <xsl:apply-templates select="CI_Citation" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:specification" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="explanation" >
          <xsl:for-each select="explanation[1]" >
            <xsl:element name="gmd:explanation" >
              <xsl:element name="gco:CharacterString" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:explanation" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="pass" >
          <xsl:for-each select="pass[1]" >
            <xsl:element name="gmd:pass" >
              <xsl:element name="gco:Boolean" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:pass" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="DQ_Scope" >
    <xsl:element name="gmd:DQ_Scope" >
      <xsl:choose>
        <xsl:when test="level" >
          <xsl:for-each select="level[1]" >
            <xsl:element name="gmd:level" >
              <xsl:call-template name="codeListElement19139">
                <xsl:with-param name="param" select="." />
                <xsl:with-param name="namespace">gmd</xsl:with-param>
                <xsl:with-param name="listName">MD_ScopeCode</xsl:with-param>
              </xsl:call-template>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:level" >
            
            <xsl:call-template name="codeListElement19139">
              <xsl:with-param name="param" select="." />
              <xsl:with-param name="namespace">gmd</xsl:with-param>
              <xsl:with-param name="listName">MD_ScopeCode</xsl:with-param>
            </xsl:call-template>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="extent[1]" >
        <xsl:element name="gmd:extent" >
          <xsl:apply-templates select="EX_Extent" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="levelDescription" >
        <xsl:element name="gmd:levelDescription" >
          <xsl:apply-templates select="MD_ScopeDescription" />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_MaintenanceInformation" >
    <xsl:element name="gmd:MD_MaintenanceInformation" >
      <xsl:choose>
        <xsl:when test="maintenanceAndUpdateFrequency" >
          <xsl:for-each select="maintenanceAndUpdateFrequency[1]" >
            <xsl:element name="gmd:maintenanceAndUpdateFrequency" >
              <xsl:call-template name="codeListElement19139">
                <xsl:with-param name="param" select="." />
                <xsl:with-param name="namespace">gmd</xsl:with-param>
                <xsl:with-param name="listName">MD_MaintenanceFrequencyCode</xsl:with-param>
              </xsl:call-template>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:maintenanceAndUpdateFrequency" >
            
            <xsl:call-template name="codeListElement19139">
              <xsl:with-param name="param" select="." />
              <xsl:with-param name="namespace">gmd</xsl:with-param>
              <xsl:with-param name="listName">MD_MaintenanceFrequencyCode</xsl:with-param>
            </xsl:call-template>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="dateOfNextUpdate[1]" >
        <xsl:element name="gmd:dateOfNextUpdate" >
          <xsl:element name="gco:Date" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="userDefinedMaintenanceFrequency[1]" >
        <xsl:element name="gmd:userDefinedMaintenanceFrequency" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="updateScope" >
        <xsl:element name="gmd:updateScope" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">MD_ScopeCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="updateScopeDescription" >
        <xsl:element name="gmd:updateScopeDescription" >
          <xsl:apply-templates select="MD_ScopeDescription" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="maintenanceNote" >
        <xsl:element name="gmd:maintenanceNote" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="contact" >
        <xsl:element name="gmd:contact" >
          <xsl:apply-templates select="CI_ResponsibleParty" />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_ScopeDescription" >
    <xsl:element name="gmd:MD_ScopeDescription" >
      <xsl:for-each select="attributes[1]" >
        <xsl:element name="gmd:attributes" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="features[1]" >
        <xsl:element name="gmd:features" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="featureInstances[1]" >
        <xsl:element name="gmd:featureInstances" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="attributeInstances[1]" >
        <xsl:element name="gmd:attributeInstances" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="dataset[1]" >
        <xsl:element name="gmd:dataset" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="other[1]" >
        <xsl:element name="gmd:other" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_SpatialRepresentation" >
  </xsl:template>
  <xsl:template match="MD_Georectified" >
    <xsl:element name="gmd:MD_Georectified" >
      <xsl:choose>
        <xsl:when test="numberOfDimensions" >
          <xsl:for-each select="numberOfDimensions[1]" >
            <xsl:element name="gmd:numberOfDimensions" >
              <xsl:element name="gco:Integer" >
                <xsl:call-template name="stringToInteger">
                  <xsl:with-param name="param" select="." />
                </xsl:call-template>
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:numberOfDimensions" >
            <xsl:element name="gco:Integer" >
              <xsl:call-template name="stringToInteger">
                <xsl:with-param name="param" select="." />
              </xsl:call-template>
            </xsl:element>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="axisDimensionProperties/MD_Dimension" >
          <xsl:for-each select="axisDimensionProperties/MD_Dimension" >
            <xsl:element name="gmd:axisDimensionProperties" >
              <xsl:element name="gmd:MD_Dimension" >
                <xsl:choose>
                  <xsl:when test="dimensionName" >
                    <xsl:for-each select="dimensionName[1]" >
                      <xsl:element name="gmd:dimensionName" >
                        <xsl:call-template name="codeListElement19139">
                          <xsl:with-param name="param" select="." />
                          <xsl:with-param name="namespace">gmd</xsl:with-param>
                          <xsl:with-param name="listName">MD_DimensionNameTypeCode</xsl:with-param>
                        </xsl:call-template>
                      </xsl:element>
                    </xsl:for-each>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:element name="gmd:dimensionName" >
                      
                      <xsl:call-template name="codeListElement19139">
                        <xsl:with-param name="param" select="." />
                        <xsl:with-param name="namespace">gmd</xsl:with-param>
                        <xsl:with-param name="listName">MD_DimensionNameTypeCode</xsl:with-param>
                      </xsl:call-template>
                    </xsl:element>
                  </xsl:otherwise>
                </xsl:choose>
                <xsl:choose>
                  <xsl:when test="dimensionSize" >
                    <xsl:for-each select="dimensionSize[1]" >
                      <xsl:element name="gmd:dimensionSize" >
                        <xsl:element name="gco:Integer" >
                          <xsl:value-of select="." />
                        </xsl:element>
                      </xsl:element>
                    </xsl:for-each>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:element name="gmd:dimensionSize" >
                      
                    </xsl:element>
                  </xsl:otherwise>
                </xsl:choose>
                <xsl:element name="gmd:resolution" >
                  <xsl:for-each select="resolution[1]" >
                    <xsl:element name="gco:Measure" >
                      <xsl:attribute name="uom" >
                        <xsl:call-template name="putValue">
                          <xsl:with-param name="param" select="." />
                          <xsl:with-param name="value">meter</xsl:with-param>
                        </xsl:call-template>
                      </xsl:attribute>
                      <xsl:value-of select="." />
                    </xsl:element>
                  </xsl:for-each>
                </xsl:element>
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:axisDimensionProperties" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="cellGeometry" >
          <xsl:for-each select="cellGeometry[1]" >
            <xsl:element name="gmd:cellGeometry" >
              <xsl:call-template name="codeListElement19139">
                <xsl:with-param name="param" select="." />
                <xsl:with-param name="namespace">gmd</xsl:with-param>
                <xsl:with-param name="listName">MD_CellGeometryCode</xsl:with-param>
              </xsl:call-template>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:cellGeometry" >
            
            <xsl:call-template name="codeListElement19139">
              <xsl:with-param name="param" select="." />
              <xsl:with-param name="namespace">gmd</xsl:with-param>
              <xsl:with-param name="listName">MD_CellGeometryCode</xsl:with-param>
            </xsl:call-template>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="transformationParameterAvailability" >
          <xsl:for-each select="transformationParameterAvailability[1]" >
            <xsl:element name="gmd:transformationParameterAvailability" >
              <xsl:element name="gco:Boolean" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:transformationParameterAvailability" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="checkPointAvailability" >
          <xsl:for-each select="checkPointAvailability[1]" >
            <xsl:element name="gmd:checkPointAvailability" >
              <xsl:element name="gco:Boolean" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:checkPointAvailability" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="checkPointDescription[1]" >
        <xsl:element name="gmd:checkPointDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="cornerPoints" >
          <xsl:for-each select="cornerPoints[1]" >
            <xsl:element name="gmd:cornerPoints" >
              <xsl:value-of select="." />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:cornerPoints" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="centerPoint[1]" >
        <xsl:element name="gmd:centerPoint" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="pointInPixel" >
          <xsl:for-each select="pointInPixel[1]" >
            <xsl:element name="gmd:pointInPixel" >
              <xsl:apply-templates select="MD_PixelOrientationCode" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:pointInPixel" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="transformationDimensionDescription[1]" >
        <xsl:element name="gmd:transformationDimensionDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="transformationDimensionMapping" >
        <xsl:element name="gmd:transformationDimensionMapping" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_Georeferenceable" >
    <xsl:element name="gmd:MD_Georeferenceable" >
      <xsl:choose>
        <xsl:when test="numberOfDimensions" >
          <xsl:for-each select="numberOfDimensions[1]" >
            <xsl:element name="gmd:numberOfDimensions" >
              <xsl:element name="gco:Integer" >
                <xsl:call-template name="stringToInteger">
                  <xsl:with-param name="param" select="." />
                </xsl:call-template>
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:numberOfDimensions" >
            <xsl:element name="gco:Integer" >
              <xsl:call-template name="stringToInteger">
                <xsl:with-param name="param" select="." />
              </xsl:call-template>
            </xsl:element>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="axisDimensionProperties/MD_Dimension" >
          <xsl:for-each select="axisDimensionProperties/MD_Dimension" >
            <xsl:element name="gmd:axisDimensionProperties" >
              <xsl:element name="gmd:MD_Dimension" >
                <xsl:choose>
                  <xsl:when test="dimensionName" >
                    <xsl:for-each select="dimensionName[1]" >
                      <xsl:element name="gmd:dimensionName" >
                        <xsl:call-template name="codeListElement19139">
                          <xsl:with-param name="param" select="." />
                          <xsl:with-param name="namespace">gmd</xsl:with-param>
                          <xsl:with-param name="listName">MD_DimensionNameTypeCode</xsl:with-param>
                        </xsl:call-template>
                      </xsl:element>
                    </xsl:for-each>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:element name="gmd:dimensionName" >
                      
                      <xsl:call-template name="codeListElement19139">
                        <xsl:with-param name="param" select="." />
                        <xsl:with-param name="namespace">gmd</xsl:with-param>
                        <xsl:with-param name="listName">MD_DimensionNameTypeCode</xsl:with-param>
                      </xsl:call-template>
                    </xsl:element>
                  </xsl:otherwise>
                </xsl:choose>
                <xsl:choose>
                  <xsl:when test="dimensionSize" >
                    <xsl:for-each select="dimensionSize[1]" >
                      <xsl:element name="gmd:dimensionSize" >
                        <xsl:element name="gco:Integer" >
                          <xsl:value-of select="." />
                        </xsl:element>
                      </xsl:element>
                    </xsl:for-each>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:element name="gmd:dimensionSize" >
                      
                    </xsl:element>
                  </xsl:otherwise>
                </xsl:choose>
                <xsl:element name="gmd:resolution" >
                  <xsl:for-each select="resolution[1]" >
                    <xsl:element name="gco:Measure" >
                      <xsl:attribute name="uom" >
                        <xsl:call-template name="putValue">
                          <xsl:with-param name="param" select="." />
                          <xsl:with-param name="value">meter</xsl:with-param>
                        </xsl:call-template>
                      </xsl:attribute>
                      <xsl:value-of select="." />
                    </xsl:element>
                  </xsl:for-each>
                </xsl:element>
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:axisDimensionProperties" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="cellGeometry" >
          <xsl:for-each select="cellGeometry[1]" >
            <xsl:element name="gmd:cellGeometry" >
              <xsl:call-template name="codeListElement19139">
                <xsl:with-param name="param" select="." />
                <xsl:with-param name="namespace">gmd</xsl:with-param>
                <xsl:with-param name="listName">MD_CellGeometryCode</xsl:with-param>
              </xsl:call-template>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:cellGeometry" >
            
            <xsl:call-template name="codeListElement19139">
              <xsl:with-param name="param" select="." />
              <xsl:with-param name="namespace">gmd</xsl:with-param>
              <xsl:with-param name="listName">MD_CellGeometryCode</xsl:with-param>
            </xsl:call-template>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="transformationParameterAvailability" >
          <xsl:for-each select="transformationParameterAvailability[1]" >
            <xsl:element name="gmd:transformationParameterAvailability" >
              <xsl:element name="gco:Boolean" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:transformationParameterAvailability" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="controlPointAvailability" >
          <xsl:for-each select="controlPointAvailability[1]" >
            <xsl:element name="gmd:controlPointAvailability" >
              <xsl:element name="gco:Boolean" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:controlPointAvailability" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="orientationParameterAvailability" >
          <xsl:for-each select="orientationParameterAvailability[1]" >
            <xsl:element name="gmd:orientationParameterAvailability" >
              <xsl:element name="gco:Boolean" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:orientationParameterAvailability" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="orientationParameterDescription[1]" >
        <xsl:element name="gmd:orientationParameterDescription" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="georeferencedParameters" >
          <xsl:for-each select="georeferencedParameters[1]" >
            <xsl:element name="gmd:georeferencedParameters" >
              <xsl:element name="gco:Record" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:georeferencedParameters" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="parameterCitation" >
        <xsl:element name="gmd:parameterCitation" >
          <xsl:apply-templates select="CI_Citation" />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_VectorSpatialRepresentation" >
    <xsl:element name="gmd:MD_VectorSpatialRepresentation" >
      <xsl:for-each select="topologyLevel[1]" >
        <xsl:element name="gmd:topologyLevel" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">MD_TopologyLevelCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="geometricObjects" >
        <xsl:element name="gmd:geometricObjects" >
          <xsl:apply-templates select="MD_GeometricObjects" />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_Dimension" >
    <xsl:element name="gmd:MD_Dimension" >
      <xsl:choose>
        <xsl:when test="dimensionName" >
          <xsl:for-each select="dimensionName[1]" >
            <xsl:element name="gmd:dimensionName" >
              <xsl:call-template name="codeListElement19139">
                <xsl:with-param name="param" select="." />
                <xsl:with-param name="namespace">gmd</xsl:with-param>
                <xsl:with-param name="listName">MD_DimensionNameTypeCode</xsl:with-param>
              </xsl:call-template>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:dimensionName" >
            
            <xsl:call-template name="codeListElement19139">
              <xsl:with-param name="param" select="." />
              <xsl:with-param name="namespace">gmd</xsl:with-param>
              <xsl:with-param name="listName">MD_DimensionNameTypeCode</xsl:with-param>
            </xsl:call-template>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="dimensionSize" >
          <xsl:for-each select="dimensionSize[1]" >
            <xsl:element name="gmd:dimensionSize" >
              <xsl:element name="gco:Integer" >
                <xsl:call-template name="stringToInteger">
                  <xsl:with-param name="param" select="." />
                </xsl:call-template>
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:dimensionSize" >
            
            <xsl:call-template name="stringToInteger">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="resolution[1]" >
        <xsl:element name="gmd:resolution" >
          <xsl:element name="gco:Measure" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_GeometricObjects" >
    <xsl:element name="gmd:MD_GeometricObjects" >
      <xsl:choose>
        <xsl:when test="geometricObjectType" >
          <xsl:for-each select="geometricObjectType[1]" >
            <xsl:element name="gmd:geometricObjectType" >
              <xsl:call-template name="codeListElement19139">
                <xsl:with-param name="param" select="." />
                <xsl:with-param name="namespace">gmd</xsl:with-param>
                <xsl:with-param name="listName">MD_GeometricObjectTypeCode</xsl:with-param>
              </xsl:call-template>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:geometricObjectType" >
            
            <xsl:call-template name="codeListElement19139">
              <xsl:with-param name="param" select="." />
              <xsl:with-param name="namespace">gmd</xsl:with-param>
              <xsl:with-param name="listName">MD_GeometricObjectTypeCode</xsl:with-param>
            </xsl:call-template>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="geometricObjectCount[1]" >
        <xsl:element name="gmd:geometricObjectCount" >
          <xsl:element name="gco:Integer" >
            <xsl:call-template name="stringToInteger">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="CI_Address" >
    <xsl:element name="gmd:CI_Address" >
      <xsl:for-each select="deliveryPoint" >
        <xsl:element name="gmd:deliveryPoint" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="city[1]" >
        <xsl:element name="gmd:city" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="administrativeArea[1]" >
        <xsl:element name="gmd:administrativeArea" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="postalCode[1]" >
        <xsl:element name="gmd:postalCode" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="country[1]" >
        <xsl:element name="gmd:country" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="electronicMailAddress" >
        <xsl:element name="gmd:electronicMailAddress" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="CI_Contact" >
    <xsl:element name="gmd:CI_Contact" >
      <xsl:for-each select="phone[1]" >
        <xsl:element name="gmd:phone" >
          <xsl:apply-templates select="CI_Telephone" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="address[1]" >
        <xsl:element name="gmd:address" >
          <xsl:apply-templates select="CI_Address" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="onlineResource[1]" >
        <xsl:element name="gmd:onlineResource" >
          <xsl:apply-templates select="CI_OnlineResource" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="hoursOfService[1]" >
        <xsl:element name="gmd:hoursOfService" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="contactInstructions[1]" >
        <xsl:element name="gmd:contactInstructions" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="CI_Date" >
    <xsl:element name="gmd:CI_Date" >
      <xsl:choose>
        <xsl:when test="date" >
          <xsl:for-each select="date[1]" >
            <xsl:element name="gmd:date" >
              <xsl:element name="gco:Date" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:date" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="dateType" >
          <xsl:for-each select="dateType[1]" >
            <xsl:element name="gmd:dateType" >
              <xsl:call-template name="codeListElement19139">
                <xsl:with-param name="param" select="." />
                <xsl:with-param name="namespace">gmd</xsl:with-param>
                <xsl:with-param name="listName">CI_DateTypeCode</xsl:with-param>
              </xsl:call-template>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:dateType" >
            
            <xsl:call-template name="codeListElement19139">
              <xsl:with-param name="param" select="." />
              <xsl:with-param name="namespace">gmd</xsl:with-param>
              <xsl:with-param name="listName">CI_DateTypeCode</xsl:with-param>
            </xsl:call-template>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="CI_OnlineResource" >
    <xsl:element name="gmd:CI_OnlineResource" >
      <xsl:choose>
        <xsl:when test="linkage" >
          <xsl:for-each select="linkage[1]" >
            <xsl:element name="gmd:linkage" >
              <xsl:element name="gmd:URL" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:linkage" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="protocol[1]" >
        <xsl:element name="gmd:protocol" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="applicationProfile[1]" >
        <xsl:element name="gmd:applicationProfile" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="name[1]" >
        <xsl:element name="gmd:name" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="description[1]" >
        <xsl:element name="gmd:description" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="function[1]" >
        <xsl:element name="gmd:function" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">CI_OnLineFunctionCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="RS_ReferenceSystem" >
  </xsl:template>
  <xsl:template match="MD_EllipsoidParameters" >
    <xsl:element name="gmd:MD_EllipsoidParameters" >
      <xsl:choose>
        <xsl:when test="semiMajorAxis" >
          <xsl:for-each select="semiMajorAxis[1]" >
            <xsl:element name="gmd:semiMajorAxis" >
              <xsl:element name="gco:Real" >
                <xsl:call-template name="stringToReal">
                  <xsl:with-param name="param" select="." />
                </xsl:call-template>
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:semiMajorAxis" >
            
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="axisUnits" >
          <xsl:for-each select="axisUnits[1]" >
            <xsl:element name="gmd:axisUnits" >
              <xsl:value-of select="." />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:axisUnits" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="denominatorOfFlatteningRatio[1]" >
        <xsl:element name="gmd:denominatorOfFlatteningRatio" >
          <xsl:element name="gco:Real" >
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_Identifier" >
    <xsl:element name="gmd:MD_Identifier" >
      <xsl:for-each select="authority[1]" >
        <xsl:element name="gmd:authority" >
          <xsl:apply-templates select="CI_Citation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="code" >
          <xsl:for-each select="code[1]" >
            <xsl:element name="gmd:code" >
              <xsl:element name="gco:CharacterString" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:code" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="RS_Identifier" >
    <xsl:element name="gmd:RS_Identifier" >
      <xsl:for-each select="authority[1]" >
        <xsl:element name="gmd:authority" >
          <xsl:apply-templates select="CI_Citation" />
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="code" >
          <xsl:for-each select="code[1]" >
            <xsl:element name="gmd:code" >
              <xsl:element name="gco:CharacterString" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:code" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="codeSpace[1]" >
        <xsl:element name="gmd:codeSpace" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="version[1]" >
        <xsl:element name="gmd:version" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_ObliqueLineAzimuth" >
    <xsl:element name="gmd:MD_ObliqueLineAzimuth" >
      <xsl:choose>
        <xsl:when test="azimuthAngle" >
          <xsl:for-each select="azimuthAngle[1]" >
            <xsl:element name="gmd:azimuthAngle" >
              <xsl:element name="gco:Real" >
                <xsl:call-template name="stringToReal">
                  <xsl:with-param name="param" select="." />
                </xsl:call-template>
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:azimuthAngle" >
            
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="azimuthMeasurePointLongitude" >
          <xsl:for-each select="azimuthMeasurePointLongitude[1]" >
            <xsl:element name="gmd:azimuthMeasurePointLongitude" >
              <xsl:element name="gco:Real" >
                <xsl:call-template name="stringToReal">
                  <xsl:with-param name="param" select="." />
                </xsl:call-template>
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:azimuthMeasurePointLongitude" >
            
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_ObliqueLinePoint" >
    <xsl:element name="gmd:MD_ObliqueLinePoint" >
      <xsl:choose>
        <xsl:when test="obliqueLineLatitude" >
          <xsl:for-each select="obliqueLineLatitude[1]" >
            <xsl:element name="gmd:obliqueLineLatitude" >
              <xsl:element name="gco:Real" >
                <xsl:call-template name="stringToReal">
                  <xsl:with-param name="param" select="." />
                </xsl:call-template>
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:obliqueLineLatitude" >
            
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="obliqueLineLongitude" >
          <xsl:for-each select="obliqueLineLongitude[1]" >
            <xsl:element name="gmd:obliqueLineLongitude" >
              <xsl:element name="gco:Real" >
                <xsl:call-template name="stringToReal">
                  <xsl:with-param name="param" select="." />
                </xsl:call-template>
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:obliqueLineLongitude" >
            
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_ProjectionParameters" >
    <xsl:element name="gmd:MD_ProjectionParameters" >
      <xsl:for-each select="zone[1]" >
        <xsl:element name="gmd:zone" >
          <xsl:element name="gco:Integer" >
            <xsl:call-template name="stringToInteger">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="standardParallel" >
        <xsl:element name="gmd:standardParallel" >
          <xsl:element name="gco:Real" >
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="longitudeOfCentralMeridian[1]" >
        <xsl:element name="gmd:longitudeOfCentralMeridian" >
          <xsl:element name="gco:Real" >
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="latitudeOfProjectionOrigin[1]" >
        <xsl:element name="gmd:latitudeOfProjectionOrigin" >
          <xsl:element name="gco:Real" >
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="falseEasting[1]" >
        <xsl:element name="gmd:falseEasting" >
          <xsl:element name="gco:Real" >
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="falseNorthing[1]" >
        <xsl:element name="gmd:falseNorthing" >
          <xsl:element name="gco:Real" >
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="falseEastingNorthingUnits[1]" >
        <xsl:element name="gmd:falseEastingNorthingUnits" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="scaleFactorAtEquator[1]" >
        <xsl:element name="gmd:scaleFactorAtEquator" >
          <xsl:element name="gco:Real" >
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="heightOfProspectivePointAboveSurface[1]" >
        <xsl:element name="gmd:heightOfProspectivePointAboveSurface" >
          <xsl:element name="gco:Real" >
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="longitudeOfProjectionCenter[1]" >
        <xsl:element name="gmd:longitudeOfProjectionCenter" >
          <xsl:element name="gco:Real" >
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="latitudeOfProjectionCenter[1]" >
        <xsl:element name="gmd:latitudeOfProjectionCenter" >
          <xsl:element name="gco:Real" >
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="scaleFactorAtCenterLine[1]" >
        <xsl:element name="gmd:scaleFactorAtCenterLine" >
          <xsl:element name="gco:Real" >
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="straightVerticalLongitudeFromPole[1]" >
        <xsl:element name="gmd:straightVerticalLongitudeFromPole" >
          <xsl:element name="gco:Real" >
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="scaleFactorAtProjectionOrigin[1]" >
        <xsl:element name="gmd:scaleFactorAtProjectionOrigin" >
          <xsl:element name="gco:Real" >
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="obliqueLineAzimuthParameter[1]" >
        <xsl:element name="gmd:obliqueLineAzimuthParameter" >
          <xsl:apply-templates select="MD_ObliqueLineAzimuth" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="obliqueLinePointParameter" >
        <xsl:element name="gmd:obliqueLinePointParameter" >
          <xsl:apply-templates select="MD_ObliqueLinePoint" />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_ContentInformation" >
  </xsl:template>
  <xsl:template match="MD_FeatureCatalogueDescription" >
    <xsl:element name="gmd:MD_FeatureCatalogueDescription" >
      <xsl:for-each select="complianceCode[1]" >
        <xsl:element name="gmd:complianceCode" >
          <xsl:element name="gco:Boolean" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="language" >
        <xsl:element name="gmd:language" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="includedWithDataset" >
          <xsl:for-each select="includedWithDataset[1]" >
            <xsl:element name="gmd:includedWithDataset" >
              <xsl:element name="gco:Boolean" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:includedWithDataset" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="featureTypes" >
        <xsl:element name="gmd:featureTypes" >
          <xsl:element name="gco:LocalName" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="featureCatalogueCitation" >
          <xsl:for-each select="featureCatalogueCitation" >
            <xsl:element name="gmd:featureCatalogueCitation" >
              <xsl:apply-templates select="CI_Citation" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:featureCatalogueCitation" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="CI_Series" >
    <xsl:element name="gmd:CI_Series" >
      <xsl:for-each select="name[1]" >
        <xsl:element name="gmd:name" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="issueIdentification[1]" >
        <xsl:element name="gmd:issueIdentification" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="page[1]" >
        <xsl:element name="gmd:page" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="CI_Telephone" >
    <xsl:element name="gmd:CI_Telephone" >
      <xsl:for-each select="voice" >
        <xsl:element name="gmd:voice" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="facsimile" >
        <xsl:element name="gmd:facsimile" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_CoverageDescription" >
    <xsl:element name="gmd:MD_CoverageDescription" >
      <xsl:choose>
        <xsl:when test="attributeDescription" >
          <xsl:for-each select="attributeDescription[1]" >
            <xsl:element name="gmd:attributeDescription" >
              <xsl:element name="gco:RecordType" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:attributeDescription" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="contentType" >
          <xsl:for-each select="contentType[1]" >
            <xsl:element name="gmd:contentType" >
              <xsl:call-template name="codeListElement19139">
                <xsl:with-param name="param" select="." />
                <xsl:with-param name="namespace">gmd</xsl:with-param>
                <xsl:with-param name="listName">MD_CoverageContentTypeCode</xsl:with-param>
              </xsl:call-template>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:contentType" >
            
            <xsl:call-template name="codeListElement19139">
              <xsl:with-param name="param" select="." />
              <xsl:with-param name="namespace">gmd</xsl:with-param>
              <xsl:with-param name="listName">MD_CoverageContentTypeCode</xsl:with-param>
            </xsl:call-template>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="dimension" >
        <xsl:element name="gmd:dimension" >
          <xsl:apply-templates select="MD_RangeDimension" />
          <xsl:apply-templates select="MD_Band" />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_ImageDescription" >
    <xsl:element name="gmd:MD_ImageDescription" >
      <xsl:choose>
        <xsl:when test="attributeDescription" >
          <xsl:for-each select="attributeDescription[1]" >
            <xsl:element name="gmd:attributeDescription" >
              <xsl:element name="gco:RecordType" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:attributeDescription" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="contentType" >
          <xsl:for-each select="contentType[1]" >
            <xsl:element name="gmd:contentType" >
              <xsl:call-template name="codeListElement19139">
                <xsl:with-param name="param" select="." />
                <xsl:with-param name="namespace">gmd</xsl:with-param>
                <xsl:with-param name="listName">MD_CoverageContentTypeCode</xsl:with-param>
              </xsl:call-template>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:contentType" >
            
            <xsl:call-template name="codeListElement19139">
              <xsl:with-param name="param" select="." />
              <xsl:with-param name="namespace">gmd</xsl:with-param>
              <xsl:with-param name="listName">MD_CoverageContentTypeCode</xsl:with-param>
            </xsl:call-template>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="dimension" >
        <xsl:element name="gmd:dimension" >
          <xsl:apply-templates select="MD_RangeDimension" />
          <xsl:apply-templates select="MD_Band" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="illuminationElevationAngle[1]" >
        <xsl:element name="gmd:illuminationElevationAngle" >
          <xsl:element name="gco:Real" >
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="illuminationAzimuthAngle[1]" >
        <xsl:element name="gmd:illuminationAzimuthAngle" >
          <xsl:element name="gco:Real" >
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="imagingCondition[1]" >
        <xsl:element name="gmd:imagingCondition" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">MD_ImagingConditionCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="imageQualityCode[1]" >
        <xsl:element name="gmd:imageQualityCode" >
          <xsl:apply-templates select="MD_Identifier" />
          <xsl:apply-templates select="RS_Identifier" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="cloudCoverPercentage[1]" >
        <xsl:element name="gmd:cloudCoverPercentage" >
          <xsl:element name="gco:Real" >
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="processingLevelCode[1]" >
        <xsl:element name="gmd:processingLevelCode" >
          <xsl:apply-templates select="MD_Identifier" />
          <xsl:apply-templates select="RS_Identifier" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="compressionGenerationQuantity[1]" >
        <xsl:element name="gmd:compressionGenerationQuantity" >
          <xsl:element name="gco:Integer" >
            <xsl:call-template name="stringToInteger">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="triangulationIndicator[1]" >
        <xsl:element name="gmd:triangulationIndicator" >
          <xsl:element name="gco:Boolean" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="radiometricCalibrationDataAvailability[1]" >
        <xsl:element name="gmd:radiometricCalibrationDataAvailability" >
          <xsl:element name="gco:Boolean" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="cameraCalibrationInformationAvailability[1]" >
        <xsl:element name="gmd:cameraCalibrationInformationAvailability" >
          <xsl:element name="gco:Boolean" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="filmDistortionInformationAvailability[1]" >
        <xsl:element name="gmd:filmDistortionInformationAvailability" >
          <xsl:element name="gco:Boolean" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="lensDistortionInformationAvailability[1]" >
        <xsl:element name="gmd:lensDistortionInformationAvailability" >
          <xsl:element name="gco:Boolean" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_RangeDimension" >
    <xsl:element name="gmd:MD_RangeDimension" >
      <xsl:element name="gmd:sequenceIdentifier" >
        <xsl:element name="gco:MemberName" >
          <xsl:for-each select="sequenceIdentifier" >
            <xsl:element name="gco:aName" >
              <xsl:element name="gco:CharacterString" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
          <xsl:element name="gco:attributeType" >
          </xsl:element>
        </xsl:element>
      </xsl:element>
      <xsl:for-each select="descriptor[1]" >
        <xsl:element name="gmd:descriptor" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_Band" >
    <xsl:element name="gmd:MD_Band" >
      <xsl:element name="gmd:sequenceIdentifier" >
        <xsl:element name="gco:MemberName" >
          <xsl:for-each select="sequenceIdentifier" >
            <xsl:element name="gco:aName" >
              <xsl:element name="gco:CharacterString" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
          <xsl:element name="gco:attributeType" >
          </xsl:element>
        </xsl:element>
      </xsl:element>
      <xsl:for-each select="descriptor[1]" >
        <xsl:element name="gmd:descriptor" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="maxValue[1]" >
        <xsl:element name="gmd:maxValue" >
          <xsl:element name="gco:Real" >
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="minValue[1]" >
        <xsl:element name="gmd:minValue" >
          <xsl:element name="gco:Real" >
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:element name="gmd:units" >
        <xsl:element name="gml:UnitDefinition" >
          <xsl:attribute name="gml:id" >
            <xsl:value-of select="generate-id()" />
          </xsl:attribute>
          <xsl:choose>
            <xsl:when test="units" >
              <xsl:for-each select="units[1]" >
                <xsl:element name="gml:identifier" >
                  <xsl:attribute name="codeSpace" >
                  </xsl:attribute>
                  <xsl:value-of select="." />
                </xsl:element>
              </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>
              <xsl:element name="gml:identifier" >
                
              </xsl:element>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:element>
      </xsl:element>
      <xsl:for-each select="peakResponse[1]" >
        <xsl:element name="gmd:peakResponse" >
          <xsl:element name="gco:Real" >
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="bitsPerValue[1]" >
        <xsl:element name="gmd:bitsPerValue" >
          <xsl:element name="gco:Integer" >
            <xsl:call-template name="stringToInteger">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="toneGradation[1]" >
        <xsl:element name="gmd:toneGradation" >
          <xsl:element name="gco:Integer" >
            <xsl:call-template name="stringToInteger">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="scaleFactor[1]" >
        <xsl:element name="gmd:scaleFactor" >
          <xsl:element name="gco:Real" >
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="offset[1]" >
        <xsl:element name="gmd:offset" >
          <xsl:element name="gco:Real" >
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_PortrayalCatalogueReference" >
    <xsl:element name="gmd:MD_PortrayalCatalogueReference" >
      <xsl:choose>
        <xsl:when test="portrayalCatalogueCitation" >
          <xsl:for-each select="portrayalCatalogueCitation" >
            <xsl:element name="gmd:portrayalCatalogueCitation" >
              <xsl:apply-templates select="CI_Citation" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:portrayalCatalogueCitation" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_Distribution" >
    <xsl:element name="gmd:MD_Distribution" >
      <xsl:for-each select="distributionFormat" >
        <xsl:element name="gmd:distributionFormat" >
          <xsl:apply-templates select="MD_Format" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="distributor" >
        <xsl:element name="gmd:distributor" >
          <xsl:apply-templates select="MD_Distributor" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="transferOptions" >
        <xsl:element name="gmd:transferOptions" >
          <xsl:apply-templates select="MD_DigitalTransferOptions" />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_DigitalTransferOptions" >
    <xsl:element name="gmd:MD_DigitalTransferOptions" >
      <xsl:for-each select="unitsOfDistribution[1]" >
        <xsl:element name="gmd:unitsOfDistribution" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="transferSize[1]" >
        <xsl:element name="gmd:transferSize" >
          <xsl:element name="gco:Real" >
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="onLine" >
        <xsl:element name="gmd:onLine" >
          <xsl:apply-templates select="CI_OnlineResource" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="offLine[1]" >
        <xsl:element name="gmd:offLine" >
          <xsl:apply-templates select="MD_Medium" />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_Distributor" >
    <xsl:element name="gmd:MD_Distributor" >
      <xsl:choose>
        <xsl:when test="distributorContact" >
          <xsl:for-each select="distributorContact[1]" >
            <xsl:element name="gmd:distributorContact" >
              <xsl:apply-templates select="CI_ResponsibleParty" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:distributorContact" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="distributionOrderProcess" >
        <xsl:element name="gmd:distributionOrderProcess" >
          <xsl:apply-templates select="MD_StandardOrderProcess" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="distributorFormat" >
        <xsl:element name="gmd:distributorFormat" >
          <xsl:apply-templates select="MD_Format" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="distributorTransferOptions" >
        <xsl:element name="gmd:distributorTransferOptions" >
          <xsl:apply-templates select="MD_DigitalTransferOptions" />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_Format" >
    <xsl:element name="gmd:MD_Format" >
      <xsl:choose>
        <xsl:when test="name" >
          <xsl:for-each select="name[1]" >
            <xsl:element name="gmd:name" >
              <xsl:element name="gco:CharacterString" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:name" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="version" >
          <xsl:for-each select="version[1]" >
            <xsl:element name="gmd:version" >
              <xsl:element name="gco:CharacterString" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:version" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="amendmentNumber[1]" >
        <xsl:element name="gmd:amendmentNumber" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="specification[1]" >
        <xsl:element name="gmd:specification" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="fileDecompressionTechnique[1]" >
        <xsl:element name="gmd:fileDecompressionTechnique" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="formatDistributor" >
        <xsl:element name="gmd:formatDistributor" >
          <xsl:apply-templates select="MD_Distributor" />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_Medium" >
    <xsl:element name="gmd:MD_Medium" >
      <xsl:for-each select="name[1]" >
        <xsl:element name="gmd:name" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">MD_MediumNameCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="density" >
        <xsl:element name="gmd:density" >
          <xsl:element name="gco:Real" >
            <xsl:call-template name="stringToReal">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="densityUnits[1]" >
        <xsl:element name="gmd:densityUnits" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="volumes[1]" >
        <xsl:element name="gmd:volumes" >
          <xsl:element name="gco:Integer" >
            <xsl:call-template name="stringToInteger">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="mediumFormat" >
        <xsl:element name="gmd:mediumFormat" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">MD_MediumFormatCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="mediumNote[1]" >
        <xsl:element name="gmd:mediumNote" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_StandardOrderProcess" >
    <xsl:element name="gmd:MD_StandardOrderProcess" >
      <xsl:for-each select="fees[1]" >
        <xsl:element name="gmd:fees" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="plannedAvailableDateTime[1]" >
        <xsl:element name="gmd:plannedAvailableDateTime" >
          <xsl:element name="gco:DateTime" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="orderingInstructions[1]" >
        <xsl:element name="gmd:orderingInstructions" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="turnaround[1]" >
        <xsl:element name="gmd:turnaround" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_MetadataExtensionInformation" >
    <xsl:element name="gmd:MD_MetadataExtensionInformation" >
      <xsl:for-each select="extensionOnLineResource[1]" >
        <xsl:element name="gmd:extensionOnLineResource" >
          <xsl:apply-templates select="CI_OnlineResource" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="extendedElementInformation" >
        <xsl:element name="gmd:extendedElementInformation" >
          <xsl:apply-templates select="MD_ExtendedElementInformation" />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_ReferenceSystem" >
    <xsl:element name="gmd:MD_ReferenceSystem" >
      <xsl:for-each select="referenceSystemIdentifier[1]" >
        <xsl:element name="gmd:referenceSystemIdentifier" >
          <xsl:apply-templates select="RS_Identifier" />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_CRS" >
    <xsl:element name="gmd:MD_ReferenceSystem" >
      <xsl:for-each select="referenceSystemIdentifier[1]" >
        <xsl:element name="gmd:referenceSystemIdentifier" >
          <xsl:apply-templates select="RS_Identifier" />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_Resolution" >
    <xsl:element name="gmd:MD_Resolution" >
      <xsl:for-each select="equivalentScale[1]" >
        <xsl:element name="gmd:equivalentScale" >
          <xsl:apply-templates select="MD_RepresentativeFraction" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="distance[1]" >
        <xsl:element name="gmd:distance" >
          <xsl:for-each select="value[1]" >
            <xsl:element name="gco:Distance" >
              <xsl:for-each select="../uom/uomLength/uomName[1]" >
                <xsl:attribute name="uom" >
                  <xsl:value-of select="." />
                </xsl:attribute>
              </xsl:for-each>
              <xsl:value-of select="." />
            </xsl:element>
          </xsl:for-each>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_ExtendedElementInformation" >
    <xsl:element name="gmd:MD_ExtendedElementInformation" >
      <xsl:choose>
        <xsl:when test="name" >
          <xsl:for-each select="name[1]" >
            <xsl:element name="gmd:name" >
              <xsl:element name="gco:CharacterString" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:name" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="shortName[1]" >
        <xsl:element name="gmd:shortName" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="domainCode[1]" >
        <xsl:element name="gmd:domainCode" >
          <xsl:element name="gco:Integer" >
            <xsl:call-template name="stringToInteger">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="definition" >
          <xsl:for-each select="definition[1]" >
            <xsl:element name="gmd:definition" >
              <xsl:element name="gco:CharacterString" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:definition" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="obligation[1]" >
        <xsl:element name="gmd:obligation" >
          <xsl:apply-templates select="MD_ObligationCode" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="condition[1]" >
        <xsl:element name="gmd:condition" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="dataType" >
          <xsl:for-each select="dataType[1]" >
            <xsl:element name="gmd:dataType" >
              <xsl:call-template name="codeListElement19139">
                <xsl:with-param name="param" select="." />
                <xsl:with-param name="namespace">gmd</xsl:with-param>
                <xsl:with-param name="listName">MD_DatatypeCode</xsl:with-param>
              </xsl:call-template>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:dataType" >
            
            <xsl:call-template name="codeListElement19139">
              <xsl:with-param name="param" select="." />
              <xsl:with-param name="namespace">gmd</xsl:with-param>
              <xsl:with-param name="listName">MD_DatatypeCode</xsl:with-param>
            </xsl:call-template>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="maximumOccurrence[1]" >
        <xsl:element name="gmd:maximumOccurrence" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="domainValue[1]" >
        <xsl:element name="gmd:domainValue" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="parentEntity" >
          <xsl:for-each select="parentEntity" >
            <xsl:element name="gmd:parentEntity" >
              <xsl:element name="gco:CharacterString" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:parentEntity" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="rule" >
          <xsl:for-each select="rule[1]" >
            <xsl:element name="gmd:rule" >
              <xsl:element name="gco:CharacterString" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:rule" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="rationale" >
        <xsl:element name="gmd:rationale" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="source" >
          <xsl:for-each select="source" >
            <xsl:element name="gmd:source" >
              <xsl:apply-templates select="CI_ResponsibleParty" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:source" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_ApplicationSchemaInformation" >
    <xsl:element name="gmd:MD_ApplicationSchemaInformation" >
      <xsl:choose>
        <xsl:when test="name" >
          <xsl:for-each select="name[1]" >
            <xsl:element name="gmd:name" >
              <xsl:apply-templates select="CI_Citation" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:name" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="schemaLanguage" >
          <xsl:for-each select="schemaLanguage[1]" >
            <xsl:element name="gmd:schemaLanguage" >
              <xsl:element name="gco:CharacterString" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:schemaLanguage" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="constraintLanguage" >
          <xsl:for-each select="constraintLanguage[1]" >
            <xsl:element name="gmd:constraintLanguage" >
              <xsl:element name="gco:CharacterString" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:constraintLanguage" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="schemaAscii[1]" >
        <xsl:element name="gmd:schemaAscii" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="graphicsFile[1]" >
        <xsl:element name="gmd:graphicsFile" >
          <xsl:element name="gco:Binary" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="softwareDevelopmentFile[1]" >
        <xsl:element name="gmd:softwareDevelopmentFile" >
          <xsl:element name="gco:Binary" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="softwareDevelopmentFileFormat[1]" >
        <xsl:element name="gmd:softwareDevelopmentFileFormat" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="EX_Extent" >
    <xsl:element name="gmd:EX_Extent" >
      <xsl:for-each select="description[1]" >
        <xsl:element name="gmd:description" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="geographicElement" >
        <xsl:element name="gmd:geographicElement" >
          <xsl:apply-templates select="EX_GeographicExtent" />
          <xsl:apply-templates select="EX_BoundingPolygon" />
          <xsl:apply-templates select="EX_GeographicBoundingBox" />
          <xsl:apply-templates select="EX_GeographicDescription" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="temporalElement" >
        <xsl:element name="gmd:temporalElement" >
          <xsl:apply-templates select="EX_TemporalExtent" />
          <xsl:apply-templates select="EX_SpatialTemporalExtent" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="verticalElement" >
        <xsl:element name="gmd:verticalElement" >
          <xsl:apply-templates select="EX_VerticalExtent" />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="EX_GeographicExtent" >
  </xsl:template>
  <xsl:template match="EX_GeographicExtent" >
    <xsl:element name="gmd:EX_GeographicExtent" >
      <xsl:for-each select="extentTypeCode[1]" >
        <xsl:element name="gmd:extentTypeCode" >
          <xsl:element name="gco:Boolean" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="DQ_Element" >
  </xsl:template>
  <xsl:template match="DQ_QuantitativeResult" >
    <xsl:element name="gmd:DQ_QuantitativeResult" >
      <xsl:for-each select="valueType[1]" >
        <xsl:element name="gmd:valueType" >
          <xsl:for-each select="recordType[1]" >
            <xsl:element name="gco:RecordType" >
              <xsl:value-of select="." />
            </xsl:element>
          </xsl:for-each>
        </xsl:element>
      </xsl:for-each>
      <xsl:element name="gmd:valueUnit" >
        <xsl:element name="gml:UnitDefinition" >
          <xsl:attribute name="gml:id" >
            <xsl:value-of select="generate-id()" />
          </xsl:attribute>
          <xsl:choose>
            <xsl:when test="valueUnit/unitOfMeasure" >
              <xsl:for-each select="valueUnit/unitOfMeasure[1]" >
                <xsl:element name="gml:identifier" >
                  <xsl:attribute name="codeSpace" >
                  </xsl:attribute>
                  <xsl:value-of select="." />
                </xsl:element>
              </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>
              <xsl:element name="gml:identifier" >
                
              </xsl:element>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:element>
      </xsl:element>
      <xsl:for-each select="errorStatistic[1]" >
        <xsl:element name="gmd:errorStatistic" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="value" >
          <xsl:for-each select="value" >
            <xsl:element name="gmd:value" >
              <xsl:element name="gco:Record" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:value" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="MD_GridSpatialRepresentation" >
    <xsl:element name="gmd:MD_GridSpatialRepresentation" >
      <xsl:choose>
        <xsl:when test="numberOfDimensions" >
          <xsl:for-each select="numberOfDimensions[1]" >
            <xsl:element name="gmd:numberOfDimensions" >
              <xsl:element name="gco:Integer" >
                <xsl:call-template name="stringToInteger">
                  <xsl:with-param name="param" select="." />
                </xsl:call-template>
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:numberOfDimensions" >
            <xsl:element name="gco:Integer" >
              <xsl:call-template name="stringToInteger">
                <xsl:with-param name="param" select="." />
              </xsl:call-template>
            </xsl:element>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="axisDimensionProperties/MD_Dimension" >
          <xsl:for-each select="axisDimensionProperties/MD_Dimension" >
            <xsl:element name="gmd:axisDimensionProperties" >
              <xsl:element name="gmd:MD_Dimension" >
                <xsl:choose>
                  <xsl:when test="dimensionName" >
                    <xsl:for-each select="dimensionName[1]" >
                      <xsl:element name="gmd:dimensionName" >
                        <xsl:call-template name="codeListElement19139">
                          <xsl:with-param name="param" select="." />
                          <xsl:with-param name="namespace">gmd</xsl:with-param>
                          <xsl:with-param name="listName">MD_DimensionNameTypeCode</xsl:with-param>
                        </xsl:call-template>
                      </xsl:element>
                    </xsl:for-each>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:element name="gmd:dimensionName" >
                      
                      <xsl:call-template name="codeListElement19139">
                        <xsl:with-param name="param" select="." />
                        <xsl:with-param name="namespace">gmd</xsl:with-param>
                        <xsl:with-param name="listName">MD_DimensionNameTypeCode</xsl:with-param>
                      </xsl:call-template>
                    </xsl:element>
                  </xsl:otherwise>
                </xsl:choose>
                <xsl:choose>
                  <xsl:when test="dimensionSize" >
                    <xsl:for-each select="dimensionSize[1]" >
                      <xsl:element name="gmd:dimensionSize" >
                        <xsl:element name="gco:Integer" >
                          <xsl:value-of select="." />
                        </xsl:element>
                      </xsl:element>
                    </xsl:for-each>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:element name="gmd:dimensionSize" >
                      
                    </xsl:element>
                  </xsl:otherwise>
                </xsl:choose>
                <xsl:element name="gmd:resolution" >
                  <xsl:for-each select="resolution[1]" >
                    <xsl:element name="gco:Measure" >
                      <xsl:attribute name="uom" >
                        <xsl:call-template name="putValue">
                          <xsl:with-param name="param" select="." />
                          <xsl:with-param name="value">meter</xsl:with-param>
                        </xsl:call-template>
                      </xsl:attribute>
                      <xsl:value-of select="." />
                    </xsl:element>
                  </xsl:for-each>
                </xsl:element>
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:axisDimensionProperties" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="cellGeometry" >
          <xsl:for-each select="cellGeometry[1]" >
            <xsl:element name="gmd:cellGeometry" >
              <xsl:call-template name="codeListElement19139">
                <xsl:with-param name="param" select="." />
                <xsl:with-param name="namespace">gmd</xsl:with-param>
                <xsl:with-param name="listName">MD_CellGeometryCode</xsl:with-param>
              </xsl:call-template>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:cellGeometry" >
            
            <xsl:call-template name="codeListElement19139">
              <xsl:with-param name="param" select="." />
              <xsl:with-param name="namespace">gmd</xsl:with-param>
              <xsl:with-param name="listName">MD_CellGeometryCode</xsl:with-param>
            </xsl:call-template>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="transformationParameterAvailability" >
          <xsl:for-each select="transformationParameterAvailability[1]" >
            <xsl:element name="gmd:transformationParameterAvailability" >
              <xsl:element name="gco:Boolean" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:transformationParameterAvailability" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="EX_BoundingPolygon" >
    <xsl:element name="gmd:EX_BoundingPolygon" >
      <xsl:for-each select="extentTypeCode[1]" >
        <xsl:element name="gmd:extentTypeCode" >
          <xsl:element name="gco:Boolean" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="polygon" >
          <xsl:for-each select="polygon" >
            <xsl:element name="gmd:polygon" >
              <xsl:value-of select="." />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:polygon" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="EX_GeographicBoundingBox" >
    <xsl:element name="gmd:EX_GeographicBoundingBox" >
      <xsl:for-each select="extentTypeCode[1]" >
        <xsl:element name="gmd:extentTypeCode" >
          <xsl:element name="gco:Boolean" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="westBoundLongitude" >
          <xsl:for-each select="westBoundLongitude[1]" >
            <xsl:element name="gmd:westBoundLongitude" >
              <xsl:element name="gco:Decimal" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:westBoundLongitude" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="eastBoundLongitude" >
          <xsl:for-each select="eastBoundLongitude[1]" >
            <xsl:element name="gmd:eastBoundLongitude" >
              <xsl:element name="gco:Decimal" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:eastBoundLongitude" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="southBoundLatitude" >
          <xsl:for-each select="southBoundLatitude[1]" >
            <xsl:element name="gmd:southBoundLatitude" >
              <xsl:element name="gco:Decimal" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:southBoundLatitude" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="northBoundLatitude" >
          <xsl:for-each select="northBoundLatitude[1]" >
            <xsl:element name="gmd:northBoundLatitude" >
              <xsl:element name="gco:Decimal" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:northBoundLatitude" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="EX_GeographicDescription" >
    <xsl:element name="gmd:EX_GeographicDescription" >
      <xsl:for-each select="extentTypeCode[1]" >
        <xsl:element name="gmd:extentTypeCode" >
          <xsl:element name="gco:Boolean" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="geographicIdentifier" >
          <xsl:for-each select="geographicIdentifier[1]" >
            <xsl:element name="gmd:geographicIdentifier" >
              <xsl:apply-templates select="MD_Identifier" />
              <xsl:apply-templates select="RS_Identifier" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:geographicIdentifier" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="EX_TemporalExtent" >
    <xsl:element name="gmd:EX_TemporalExtent" >
      <xsl:choose>
        <xsl:when test="extent" >
          <xsl:for-each select="extent[1]" >
            <xsl:element name="gmd:extent" >
              <xsl:apply-templates select="TimePeriod" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:extent" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="EX_SpatialTemporalExtent" >
    <xsl:element name="gmd:EX_SpatialTemporalExtent" >
      <xsl:choose>
        <xsl:when test="extent" >
          <xsl:for-each select="extent[1]" >
            <xsl:element name="gmd:extent" >
              <xsl:apply-templates select="TimePeriod" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:extent" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="spatialExtent" >
          <xsl:for-each select="spatialExtent" >
            <xsl:element name="gmd:spatialExtent" >
              <xsl:apply-templates select="EX_GeographicExtent" />
              <xsl:apply-templates select="EX_BoundingPolygon" />
              <xsl:apply-templates select="EX_GeographicBoundingBox" />
              <xsl:apply-templates select="EX_GeographicDescription" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:spatialExtent" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="EX_VerticalExtent" >
    <xsl:element name="gmd:EX_VerticalExtent" >
      <xsl:choose>
        <xsl:when test="minimumValue" >
          <xsl:for-each select="minimumValue[1]" >
            <xsl:element name="gmd:minimumValue" >
              <xsl:element name="gco:Real" >
                <xsl:call-template name="stringToReal">
                  <xsl:with-param name="param" select="." />
                </xsl:call-template>
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:minimumValue" >
            <xsl:element name="gco:Real" >
              <xsl:call-template name="stringToReal">
                <xsl:with-param name="param" select="." />
              </xsl:call-template>
            </xsl:element>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="maximumValue" >
          <xsl:for-each select="maximumValue[1]" >
            <xsl:element name="gmd:maximumValue" >
              <xsl:element name="gco:Real" >
                <xsl:call-template name="stringToReal">
                  <xsl:with-param name="param" select="." />
                </xsl:call-template>
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:maximumValue" >
            <xsl:element name="gco:Real" >
              <xsl:call-template name="stringToReal">
                <xsl:with-param name="param" select="." />
              </xsl:call-template>
            </xsl:element>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:element name="gmd:verticalCRS" >
        <xsl:element name="gml:VerticalCRS" >
          <xsl:attribute name="gml:id" >
            <xsl:call-template name="generateIdVerticalCRS">
              <xsl:with-param name="param" select="." />
            </xsl:call-template>
          </xsl:attribute>
          <xsl:element name="gml:identifier" >
            <xsl:attribute name="codeSpace" >
            </xsl:attribute>
          </xsl:element>
          <xsl:element name="gml:scope" >
          </xsl:element>
          <xsl:element name="gml:verticalCS" >
            <xsl:element name="gml:VerticalCS" >
              <xsl:attribute name="gml:id" >
                <xsl:call-template name="generateIdVerticalCS">
                  <xsl:with-param name="param" select="." />
                </xsl:call-template>
              </xsl:attribute>
              <xsl:element name="gml:identifier" >
                <xsl:attribute name="codeSpace" >
                </xsl:attribute>
              </xsl:element>
              <xsl:element name="gml:axis" >
                <xsl:element name="gml:CoordinateSystemAxis" >
                  <xsl:choose>
                    <xsl:when test="unitOfMeasure" >
                      <xsl:for-each select="unitOfMeasure[1]" >
                        <xsl:attribute name="gml:uom" >
                          <xsl:value-of select="." />
                        </xsl:attribute>
                      </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:attribute name="gml:uom" >
                        
                      </xsl:attribute>
                    </xsl:otherwise>
                  </xsl:choose>
                  <xsl:attribute name="gml:id" >
                    <xsl:call-template name="generateIdCoordinateSystemAxis">
                      <xsl:with-param name="param" select="." />
                    </xsl:call-template>
                  </xsl:attribute>
                  <xsl:element name="gml:identifier" >
                    <xsl:attribute name="codeSpace" >
                    </xsl:attribute>
                  </xsl:element>
                  <xsl:element name="gml:axisAbbrev" >
                  </xsl:element>
                  <xsl:element name="gml:axisDirection" >
                    <xsl:attribute name="codeSpace" >
                    </xsl:attribute>
                  </xsl:element>
                </xsl:element>
              </xsl:element>
            </xsl:element>
          </xsl:element>
          <xsl:element name="gml:verticalDatum" >
            <xsl:element name="gml:VerticalDatum" >
              <xsl:attribute name="gml:id" >
                <xsl:call-template name="generateIdVerticalDatum">
                  <xsl:with-param name="param" select="." />
                </xsl:call-template>
              </xsl:attribute>
              <xsl:element name="gml:identifier" >
                <xsl:attribute name="codeSpace" >
                </xsl:attribute>
              </xsl:element>
              <xsl:choose>
                <xsl:when test="verticalDatum/SC_VerticalDatum/datumID/RS_Identifier/code" >
                  <xsl:for-each select="verticalDatum/SC_VerticalDatum/datumID/RS_Identifier/code[1]" >
                    <xsl:element name="gml:name" >
                      <xsl:value-of select="." />
                    </xsl:element>
                  </xsl:for-each>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:element name="gml:name" >
                    
                  </xsl:element>
                </xsl:otherwise>
              </xsl:choose>
              <xsl:element name="gml:scope" >
              </xsl:element>
            </xsl:element>
          </xsl:element>
        </xsl:element>
      </xsl:element>
    </xsl:element>
  </xsl:template>
  <xsl:template match="CI_Citation" >
    <xsl:element name="gmd:CI_Citation" >
      <xsl:for-each select="responsibleParty" >
        <xsl:element name="gmd:responsibleParty" >
          <xsl:apply-templates select="CI_ResponsibleParty" />
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="title" >
          <xsl:for-each select="title[1]" >
            <xsl:element name="gmd:title" >
              <xsl:element name="gco:CharacterString" >
                <xsl:value-of select="." />
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:title" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="alternateTitle" >
        <xsl:element name="gmd:alternateTitle" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="date" >
          <xsl:for-each select="date" >
            <xsl:element name="gmd:date" >
              <xsl:apply-templates select="CI_Date" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:date" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:for-each select="edition[1]" >
        <xsl:element name="gmd:edition" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="editionDate[1]" >
        <xsl:element name="gmd:editionDate" >
          <xsl:element name="gco:Date" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="identifier" >
        <xsl:element name="gmd:identifier" >
          <xsl:apply-templates select="MD_Identifier" />
          <xsl:apply-templates select="RS_Identifier" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="citedResponsibleParty" >
        <xsl:element name="gmd:citedResponsibleParty" >
          <xsl:apply-templates select="CI_ResponsibleParty" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="presentationForm" >
        <xsl:element name="gmd:presentationForm" >
          <xsl:call-template name="codeListElement19139">
            <xsl:with-param name="param" select="." />
            <xsl:with-param name="namespace">gmd</xsl:with-param>
            <xsl:with-param name="listName">CI_PresentationFormCode</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="series[1]" >
        <xsl:element name="gmd:series" >
          <xsl:apply-templates select="CI_Series" />
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="otherCitationDetails[1]" >
        <xsl:element name="gmd:otherCitationDetails" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="collectiveTitle[1]" >
        <xsl:element name="gmd:collectiveTitle" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="ISBN[1]" >
        <xsl:element name="gmd:ISBN" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="ISSN[1]" >
        <xsl:element name="gmd:ISSN" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="ISBN" >
    <xsl:element name="gmd:ISBN" >
      <xsl:element name="gco:CharacterString" >
        <xsl:value-of select="." />
      </xsl:element>
    </xsl:element>
  </xsl:template>
  <xsl:template match="ISSN" >
    <xsl:element name="gmd:ISSN" >
      <xsl:element name="gco:CharacterString" >
        <xsl:value-of select="." />
      </xsl:element>
    </xsl:element>
  </xsl:template>
  <xsl:template match="CI_ResponsibleParty" >
    <xsl:element name="gmd:CI_ResponsibleParty" >
      <xsl:for-each select="individualName[1]" >
        <xsl:element name="gmd:individualName" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="organisationName[1]" >
        <xsl:element name="gmd:organisationName" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="positionName[1]" >
        <xsl:element name="gmd:positionName" >
          <xsl:element name="gco:CharacterString" >
            <xsl:value-of select="." />
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="contactInfo[1]" >
        <xsl:element name="gmd:contactInfo" >
          <xsl:apply-templates select="CI_Contact" />
        </xsl:element>
      </xsl:for-each>
      <xsl:choose>
        <xsl:when test="role" >
          <xsl:for-each select="role[1]" >
            <xsl:element name="gmd:role" >
              <xsl:call-template name="codeListElement19139">
                <xsl:with-param name="param" select="." />
                <xsl:with-param name="namespace">gmd</xsl:with-param>
                <xsl:with-param name="listName">CI_RoleCode</xsl:with-param>
              </xsl:call-template>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gmd:role" >
            
            <xsl:call-template name="codeListElement19139">
              <xsl:with-param name="param" select="." />
              <xsl:with-param name="namespace">gmd</xsl:with-param>
              <xsl:with-param name="listName">CI_RoleCode</xsl:with-param>
            </xsl:call-template>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="TimePeriod" >
    <xsl:element name="gml:TimePeriod" >
      <xsl:attribute name="gml:id" >
        <xsl:value-of select="generate-id()" />
      </xsl:attribute>
      <xsl:choose>
        <xsl:when test="begin" >
          <xsl:for-each select="begin[1]" >
            <xsl:element name="gml:begin" >
              <xsl:apply-templates select="TimeInstant" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gml:begin" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="end" >
          <xsl:for-each select="end[1]" >
            <xsl:element name="gml:end" >
              <xsl:apply-templates select="TimeInstant" />
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="gml:end" >
            
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="TimeInstant" >
    <xsl:element name="gml:TimeInstant" >
      <xsl:attribute name="gml:id" >
        <xsl:value-of select="generate-id()" />
      </xsl:attribute>
      <xsl:for-each select="timePosition[1]" >
        <xsl:element name="gml:timePosition" >
          <xsl:value-of select="." />
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
</xsl:stylesheet>
