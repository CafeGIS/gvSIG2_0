<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                                    xmlns:dc="http://purl.org/dc/elements/1.1/"
                                    xmlns:dcterms="http://purl.org/dc/terms/"
                                    xmlns:iso19115="http://www.isotc211.org/iso19115"
                                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" exclude-result-prefixes="iso19115">

  <xsl:output method="xml" indent="yes" encoding="ISO-8859-1"/>
<!--                                     xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                                    xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"-->
                                    
  <!--xsl:output doctype-system="U:/projects/s65_SERGIS/s6542_GIDC/Hojas de estilo/ISO-DC/DCMES.dtd"/-->
  <!-- this line will change if the location of the DTD changes.             -->

  <!-- Stylesheet that translates a correct ISO data into a correct DC data  -->
  <!-- Author: mapi  IAAA                                                         -->
  <!-- noviembre 2003: se modifica para que sea acorde a la ultima versión       -->
  <!-- del ISO19115      					                                              -->
  <!-- [jnog 5/2/2004] eliminar uso de namespace iso19115: . 
       [jnog 6/2/2004] Tratar nombres de clases, language
       [carlose 30/11/2004] se añade el atributo namespace al xsl:if de tratamiento del language para que procese bien el language -->

<!--xsl:strip-space elements="dc"/-->

  <xsl:template match="/">
    <xsl:apply-templates select="MD_Metadata"/>
  </xsl:template>

  <xsl:template match="MD_Metadata">

    <xsl:variable name="xsltsl-str-lower" select="'a;b;c;d;e;f;g;h;i;j;k;l;m;n;o;p;q;r;s;t;u;v;w;x;y;z'"/>
    <xsl:variable name="xsltsl-str-upper" select="'A;B;C;D;E;F;G;H;I;J;K;L;M;N;O;P;Q;R;S;T;U;V;W;X;Y;Z'"/>

    <!--xsl:element name="qualifieddc"-->
    <qualifieddc>
    	<xsl:if test="./fileIdentifier">
    		<xsl:attribute name="fileID">    	
    			<xsl:value-of select="./fileIdentifier"/>
	    	</xsl:attribute>
    	</xsl:if>

   	<xsl:if test="./language">
    		<xsl:attribute name="xml:lang" namespace="http://www.w3.org/XML/1998/namespace">
    			<xsl:choose>
				<xsl:when test="./language/*='SPANISH' or ./language/*='ESPAÑOL' or ./language/*='ESPANYOL'">
					<xsl:text>es</xsl:text>
				</xsl:when>
				<xsl:when test="./language/*='ENGLISH' or ./language/*='INGLÉS' or ./language/*='ANGLÈS'">
					<xsl:text>en</xsl:text>
				</xsl:when>
				<xsl:when test="./language/*='CATALAN' or ./language/*='CATALÁN' or ./language/*='CATALÀ'">
					<xsl:text>ca</xsl:text>
				</xsl:when>	
				<xsl:otherwise>
					<xsl:value-of select="./language"/>
				</xsl:otherwise>			
			</xsl:choose>
	    	</xsl:attribute>
    	</xsl:if>

    <!-- Element title conversion:                                           -->
    <!-- It's supposed to be more than one ocurrence in ISO (and that        -->
    <!-- means more than citation element).  For each ocurrence of           -->
    <!-- citation.title a new element dc:title will be generated             -->

      <!-- ISO CORE and ISO COMPREHENSIVE-->
      <xsl:for-each select="./identificationInfo/*/citation/*/title">
        <xsl:element name="dc:title">
          <xsl:value-of select="normalize-space(.)"/>
        </xsl:element>
      </xsl:for-each>

    <!-- Also a new element dc:title is generated for each                   -->
    <!--citation.alternateTitle element                                      -->

      <xsl:for-each select="./identificationInfo/*/citation/*/alternateTitle">
        <xsl:element name="dc:title">
          <xsl:value-of select="normalize-space(.)"/>
        </xsl:element>
      </xsl:for-each>


    <!-- creator element conversion:                                         -->
    <!-- It's assumed that at least, one of organisationName, individualName -->
    <!-- or positionName exists.  No comprobations are made.                 -->
    <!-- ISO COMPREHENSIVE -->
      <xsl:for-each select="./identificationInfo/*/citation/*/citedResponsibleParty/CI_ResponsibleParty">
        <xsl:if test="normalize-space(./role)='originator'">
          <xsl:element name="dc:creator">
            <xsl:choose>
              <xsl:when test="./organisationName">
                <xsl:value-of select="./organisationName"/>
              </xsl:when>
              <xsl:when test="./individualName">
                <xsl:value-of select="./individualName"/>
              </xsl:when>
              <xsl:when test="./positionName">
                <xsl:value-of select="./positionName"/>
              </xsl:when>
              <xsl:otherwise>
                <!-- Nothing.  This should never happen. -->
              </xsl:otherwise>
            </xsl:choose>
          </xsl:element>
        </xsl:if>
      </xsl:for-each>

      <!-- ISO CORE -->
      <xsl:for-each select="./identificationInfo/*/pointOfContact/CI_ResponsibleParty">
        <xsl:if test="normalize-space(./role)='originator'">
          <xsl:element name="dc:creator">
            <xsl:choose>
              <xsl:when test="./organisationName">
                <xsl:value-of select="./organisationName"/>
              </xsl:when>
              <xsl:when test="./individualName">
                <xsl:value-of select="./individualName"/>
              </xsl:when>
              <xsl:when test="./positionName">
                <xsl:value-of select="./positionName"/>
              </xsl:when>
              <xsl:otherwise>
                <!-- Nothing.  This should be never happen. -->
              </xsl:otherwise>
            </xsl:choose>
          </xsl:element>
        </xsl:if>
      </xsl:for-each>

    <!-- Element subject conversion                                          -->
    <!-- The data is supposed to be correct and for this reason the          -->
    <!-- verification of the correction of the value is not made.            -->
    <!-- ISO COMPREHENSIVE -->

      <xsl:for-each select="./identificationInfo/*/descriptiveKeywords/MD_Keywords">
        <xsl:if test="normalize-space(./type)='theme'">
          <xsl:element name="dc:subject">
            <xsl:value-of select="./keyword"/>
          </xsl:element>
        </xsl:if>
      </xsl:for-each>

      <!-- ISO CORE -->
      <xsl:for-each select="./identificationInfo/*/topicCategory">
        <xsl:element name="dc:subject">
          <xsl:value-of select="normalize-space(.)"/>
        </xsl:element>
      </xsl:for-each>


    <!-- description element conversion                                      -->
    <!-- For each ocurrence of abstract a new dc:description will be         -->
    <!-- generated                                                           -->
    <!-- ISO CORE E ISO COMPREHENSIVE-->
      <xsl:for-each select="./identificationInfo/*/abstract">
        <xsl:element name="dc:description">
          <xsl:value-of select="."/>
        </xsl:element>
      </xsl:for-each>

    <!-- publisher element conversion:                                       -->
    <!-- It's assumed that at least, one of organisationName, individualName -->
    <!-- or positionName exists.  No comprobations are made.                 -->

    <!-- ISO COMPREHENSIVE -->
      <xsl:for-each select="./identificationInfo/*/citation/*/citedResponsibleParty/CI_ResponsibleParty">
        <xsl:if test="normalize-space(./role)='publisher'">
          <xsl:element name="dc:publisher">
            <xsl:choose>
              <xsl:when test="./organisationName">
                <xsl:value-of select="./organisationName"/>
              </xsl:when>
              <xsl:when test="./individualName">
                <xsl:value-of select="./individualName"/>
              </xsl:when>
              <xsl:when test="./positionName">
                <xsl:value-of select="./positionName"/>
              </xsl:when>
              <xsl:otherwise>
                <!-- Nothing.  This should be never happen. -->
              </xsl:otherwise>
            </xsl:choose>
          </xsl:element>
	</xsl:if>
      </xsl:for-each>

      <xsl:for-each select="./identificationInfo/*/pointOfContact/CI_ResponsibleParty">
        <xsl:if test="normalize-space(./role)='publisher'">
          <xsl:element name="dc:publisher">
            <xsl:choose>
              <xsl:when test="./organisationName">
                <xsl:value-of select="./organisationName"/>
              </xsl:when>
              <xsl:when test="./individualName">
                <xsl:value-of select="./individualName"/>
              </xsl:when>
              <xsl:when test="./positionName">
                <xsl:value-of select="./positionName"/>
              </xsl:when>
              <xsl:otherwise>
                <!-- Nothing.  This should be never happen. -->
              </xsl:otherwise>
            </xsl:choose>
          </xsl:element>
	</xsl:if>
      </xsl:for-each>

     <!-- ISO CORE -->
     <xsl:for-each select="./contact/CI_ResponsibleParty">
       <xsl:if test="normalize-space(./role)='publisher'">
          <xsl:element name="dc:publisher">
            <xsl:choose>
              <xsl:when test="./organisationName">
                <xsl:value-of select="./organisationName"/>
              </xsl:when>
              <xsl:when test="./individualName">
                <xsl:value-of select="./individualName"/>
              </xsl:when>
              <xsl:when test="./positionName">
                <xsl:value-of select="./positionName"/>
              </xsl:when>
              <xsl:otherwise>
                <!-- Nothing.  This should be never happen. -->
              </xsl:otherwise>
            </xsl:choose>
          </xsl:element>
	</xsl:if>
      </xsl:for-each>


    <!-- contributor element conversion                                      -->
    <!-- ISO COMPREHENSIVE -->
      <xsl:for-each select="./identificationInfo/*/credit">
        <xsl:element name="dc:contributor">
          <xsl:value-of select="."/>
        </xsl:element>
      </xsl:for-each>


   <!-- ISO CORE N/A ¡¡¡GAP!!! -->

    <!-- date element conversion:  In ISO the publication date is compulsory -->
    <!-- and it can appear N times                                           -->
    <!-- ISO CORE E ISO COMPREHENSIVE-->
      <xsl:for-each select="./identificationInfo/*/citation/*/date">
        <xsl:if test="normalize-space(./*/dateType)='publication'">
          <xsl:element name="dc:date">
	    <xsl:value-of select="./*/date"/>
          </xsl:element>
        </xsl:if>
      </xsl:for-each>

    <!-- Another publication date will be generated with the value of        -->
    <!-- dateStamp.                                                          -->

      <xsl:if test="./dateStamp">
        <xsl:element name="dc:date">
          <xsl:value-of select="./dateStamp"/>
        </xsl:element>
      </xsl:if>


    <!-- element type conversion(1)                                          -->
    <!-- ISO COMPREHENSIVE -->
      <xsl:for-each select="./hierarchyLevel">
        <xsl:variable name="hierarchyLevelVar">
          <!-- it's translated to lower case -->
          <xsl:value-of select="translate(normalize-space(.),$xsltsl-str-upper, $xsltsl-str-lower)"/>
        </xsl:variable>

        <xsl:choose>
          <xsl:when test="$hierarchyLevelVar='attribute'">
            <xsl:element name="dc:type">
              <xsl:value-of select="normalize-space(.)"/>
            </xsl:element>
            <xsl:element name="dc:type">
              <xsl:text>Dataset</xsl:text>
            </xsl:element>
  	  </xsl:when>

          <xsl:when test="$hierarchyLevelVar='attributetype'">
            <xsl:element name="dc:type">
              <xsl:value-of select="normalize-space(.)"/>
            </xsl:element>
            <xsl:element name="dc:type">
              <xsl:text>Dataset</xsl:text>
            </xsl:element>
	  </xsl:when>

          <xsl:when test="$hierarchyLevelVar='collectionhardware'">
            <xsl:element name="dc:type">
              <xsl:value-of select="normalize-space(.)"/>
            </xsl:element>
            <xsl:element name="dc:type">
              <xsl:text>Dataset</xsl:text>
            </xsl:element>
          </xsl:when>

          <xsl:when test="$hierarchyLevelVar='collectionsession'">
            <xsl:element name="dc:type">
              <xsl:value-of select="normalize-space(.)"/>
            </xsl:element>
            <xsl:element name="dc:type">
              <xsl:text>Event</xsl:text>
            </xsl:element>
	  </xsl:when>

          <xsl:when test="$hierarchyLevelVar='dataset'">
            <!-- If upper and lower cases are the same, only one dataset must  -->
            <!-- be generated -->
            <xsl:element name="dc:type">
              <xsl:value-of select="normalize-space(.)"/>
            </xsl:element>
            <!--xsl:element name="dc:type">
	      <xsl:text>Dataset</xsl:text>
            </xsl:element-->
	  </xsl:when>

          <xsl:when test="$hierarchyLevelVar='series'">
            <xsl:element name="dc:type">
              <xsl:value-of select="normalize-space(.)"/>
            </xsl:element>
            <xsl:element name="dc:type">
              <xsl:text>Collection</xsl:text>
            </xsl:element>
          </xsl:when>

          <xsl:when test="$hierarchyLevelVar='nongeographicdataset'">
            <xsl:element name="dc:type">
              <xsl:value-of select="normalize-space(.)"/>
            </xsl:element>
            <xsl:element name="dc:type">
              <xsl:text>Dataset</xsl:text>
            </xsl:element>
          </xsl:when>

          <xsl:when test="$hierarchyLevelVar='dimensiongroup'">
            <xsl:element name="dc:type">
              <xsl:value-of select="normalize-space(.)"/>
            </xsl:element>
            <xsl:element name="dc:type">
              <xsl:text>Dataset</xsl:text>
            </xsl:element>
          </xsl:when>

          <xsl:when test="$hierarchyLevelVar='feature'">
            <xsl:element name="dc:type">
              <xsl:value-of select="normalize-space(.)"/>
            </xsl:element>
            <xsl:element name="dc:type">
              <xsl:text>Dataset</xsl:text>
            </xsl:element>
	  </xsl:when>

          <xsl:when test="$hierarchyLevelVar='featuretype'">
            <xsl:element name="dc:type">
              <xsl:value-of select="normalize-space(.)"/>
            </xsl:element>
            <xsl:element name="dc:type">
              <xsl:text>Dataset</xsl:text>
            </xsl:element>
          </xsl:when>

          <xsl:when test="$hierarchyLevelVar='propertytype'">
            <xsl:element name="dc:type">
              <xsl:value-of select="normalize-space(.)"/>
            </xsl:element>
            <xsl:element name="dc:type">
              <xsl:text>Dataset</xsl:text>
            </xsl:element>
	  </xsl:when>

          <xsl:when test="$hierarchyLevelVar='fieldsession'">
            <xsl:element name="dc:type">
              <xsl:value-of select="normalize-space(.)"/>
            </xsl:element>
            <xsl:element name="dc:type">
              <xsl:text>Event</xsl:text>
            </xsl:element>
	  </xsl:when>

          <xsl:when test="$hierarchyLevelVar='software'">
            <xsl:element name="dc:type">
              <xsl:value-of select="normalize-space(.)"/>
            </xsl:element>
            <xsl:element name="dc:type">
              <xsl:text>Software</xsl:text>
            </xsl:element>
          </xsl:when>

          <xsl:when test="$hierarchyLevelVar='service'">
            <xsl:element name="dc:type">
              <xsl:value-of select="normalize-space(.)"/>
            </xsl:element>
            <xsl:element name="dc:type">
              <xsl:text>Service</xsl:text>
            </xsl:element>
	  </xsl:when>

          <xsl:when test="$hierarchyLevelVar='model'">
            <xsl:element name="dc:type">
              <xsl:value-of select="normalize-space(.)"/>
            </xsl:element>
            <xsl:element name="dc:type">
              <xsl:text>Dataset</xsl:text>
            </xsl:element>
	  </xsl:when>

          <xsl:when test="$hierarchyLevelVar='tile'">
            <xsl:element name="dc:type">
              <xsl:value-of select="normalize-space(.)"/>
            </xsl:element>
            <xsl:element name="dc:type">
              <xsl:text>Dataset</xsl:text>
            </xsl:element>
	  </xsl:when>

          <xsl:otherwise>
            <!-- Nothing.  This should be never happen. -->
          </xsl:otherwise>
        </xsl:choose>
      </xsl:for-each>

    <!-- element type conversion(2)                                          -->
    <!-- ISO CORE -->
      <xsl:for-each select="./identificationInfo/*/spatialRepresentationType">
        <xsl:element name="dc:type">
          <xsl:value-of select="normalize-space(.)"/>
        </xsl:element>
      </xsl:for-each>

      <xsl:for-each select="./identificationInfo/*/citation/*/presentationForm">
        <xsl:element name="dc:type">
          <xsl:value-of select="normalize-space(.)"/>
        </xsl:element>
      </xsl:for-each>


    <!-- Format element conversion                                           -->
    <!-- ISO COMPREHENSIVE E ISO CORE -->
      <xsl:for-each select="./distributionInfo/MD_Distribution/distributionFormat/MD_Format">
         <xsl:element name="dc:format">
	   <xsl:value-of select="./name"/>
         </xsl:element>
      </xsl:for-each>

      <!--xsl:for-each select="./identificationInfo/*/resourceFormat/MD_Format">
        <xsl:element name="dc:format">
           <xsl:value-of select="./name"/>
        </xsl:element>
      </xsl:for-each-->


    <!-- identifier element conversion:                                      -->
    <!-- For this element there are five possible ways.                      -->

      <xsl:for-each select="./distributionInfo/MD_Distribution/transferOptions/MD_DigitalTransferOptions/onLine/CI_OnlineResource">
        <xsl:element name="dc:identifier">
          <xsl:if test="./linkage">
            <xsl:value-of select="./linkage"/>
          </xsl:if>
        </xsl:element>
      </xsl:for-each>

      <xsl:for-each select="./identificationInfo/*/citation/*/identifier/MD_Identifier">
        <xsl:element name="dc:identifier">
          <xsl:value-of select="./code"/>
        </xsl:element>
      </xsl:for-each>

      <xsl:for-each select="./identificationInfo/*/citation/*/ISBN">
        <xsl:variable name="isbnVar">
          <!-- it's translated to lower case -->
          <xsl:value-of select="translate(normalize-space(.),$xsltsl-str-upper, $xsltsl-str-lower)"/>
        </xsl:variable>
        <xsl:element name="dc:identifier">
          <xsl:choose>
            <xsl:when test="contains($isbnVar, 'isbn')">
              <xsl:value-of select="."/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:text>ISBN </xsl:text><xsl:value-of select="."/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:element>
      </xsl:for-each>

      <xsl:for-each select="./identificationInfo/*/citation/*/ISSN">
        <xsl:variable name="issnVar">
          <!-- it's translated to lower case -->
          <xsl:value-of select="translate(normalize-space(.),$xsltsl-str-upper, $xsltsl-str-lower)"/>
        </xsl:variable>
        <xsl:element name="dc:identifier">
          <xsl:choose>
            <xsl:when test="contains($issnVar, 'issn')">
              <xsl:value-of select="."/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:text>ISSN </xsl:text><xsl:value-of select="."/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:element>
      </xsl:for-each>

      <xsl:if test="./dataSetURI"> 
        <xsl:element name="dc:identifier">
          <xsl:value-of select="./dataSetURI"/>
        </xsl:element>
      </xsl:if>

    <!-- source element conversion:                                          -->
    <!-- ISO COMPREHENSIVE -->

      <xsl:for-each select="./dataQualityInfo/DQ_DataQuality/lineage/LI_Lineage/source/LI_Source/description">
        <xsl:element name="dc:source">
          <xsl:value-of select="."/>
        </xsl:element>
      </xsl:for-each>


   <!-- ISO CORE N/A ¡¡¡GAP!!! -->

    <!-- language element conversion.                                        -->
    <!-- ISO COMPREHENSIVE E ISO CORE -->
      <xsl:for-each select="./identificationInfo/*/language">
          <xsl:element name="dc:language">
	    <xsl:value-of select="."/>
          </xsl:element>
        <!--xsl:if test="./isoCode"-->
        <!--/xsl:if-->
        <!--xsl:if test="./isoName"-->
        <!--/xsl:if-->
        <!--xsl:if test="./otherLang"-->
        <!--/xsl:if-->
      </xsl:for-each>

    <!-- relation element conversion:                                        -->
    <!-- ISO COMPREHENSIVE -->
      <xsl:for-each select="./identificationInfo/*/citation/*/series/*/name">
        <xsl:element name="dc:relation">
          <xsl:value-of select="."/>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="./identificationInfo/*/aggregationInfo/MD_AggregateInformation/aggregateDataSetIdentifier/code">
        <xsl:element name="dc:relation">
          <xsl:value-of select="."/>
        </xsl:element>
      </xsl:for-each>

    <!-- ISO CORE N/A ¡¡¡GAP!!! -->


	<!-- dcterms:temporal; dcterms:spatial; two separted elements-->
      
      <!--  dcterms temporal --> 
      <xsl:choose>
        <xsl:when test="count(./identificationInfo/*/descriptiveKeywords/MD_Keywords[type='temporal']) > 0
            or ./identificationInfo/*/extent/*/temporalElement/*/extent/beginEnd">	
            <!-- Some temporal will contain only a keyword or something like name=The Great Depression; start=1929; end=1939; -->
	
            <xsl:if test="./identificationInfo/*/extent/*/temporalElement/*/extent/beginEnd">	
            <xsl:element name="dcterms:temporal">
            	<xsl:attribute name="xsi:type">dcterms:Period</xsl:attribute>
              <xsl:text>begin=</xsl:text>
      	        <xsl:value-of select="./identificationInfo/*/extent/*/temporalElement/*/extent/beginEnd/begin"/>
       	 <xsl:text>; </xsl:text>
     	        <xsl:text>end=</xsl:text>
 	        <xsl:value-of select="./identificationInfo/*/extent/*/temporalElement/*/extent/beginEnd/end"/>
 	        <xsl:text>;</xsl:text>
            </xsl:element>
	 </xsl:if>
	 <xsl:for-each select="./identificationInfo/*/descriptiveKeywords/MD_Keywords[type='temporal']">
 		<xsl:element name="dcterms:temporal">
 			<xsl:value-of select="./keyword"/>
 		</xsl:element>
	 </xsl:for-each>
	</xsl:when>
     </xsl:choose>
     
     <!--  dcterms spatial --> 
      <xsl:choose>
        <xsl:when test="count(./identificationInfo/*/descriptiveKeywords/MD_Keywords[type='place']) > 0
            or ./identificationInfo/*/extent/*/geographicElement/*/northBoundLatitude">	
            <!-- Some spatial will contain only a keyword or something like name=The Great Depression; start=1929; end=1939; -->
	
            <xsl:if test="./identificationInfo/*/extent/*/geographicElement/*/northBoundLatitude">	
            <xsl:element name="dcterms:spatial">
              <xsl:text>northlimit=</xsl:text>
      	        <xsl:value-of select="./identificationInfo/*/extent/*/geographicElement/*/northBoundLatitude"/>
       	 <xsl:text>; </xsl:text>
     	        <xsl:text>southlimit=</xsl:text>
 	        <xsl:value-of select="./identificationInfo/*/extent/*/geographicElement/*/southBoundLatitude"/>
 	        <xsl:text>; </xsl:text>
      	        <xsl:text>westlimit=</xsl:text>
 	        <xsl:value-of select="./identificationInfo/*/extent/*/geographicElement/*/westBoundLongitude"/>
 	        <xsl:text>; </xsl:text>
     	        <xsl:text>eastlimit=</xsl:text>
 	        <xsl:value-of select="./identificationInfo/*/extent/*/geographicElement/*/eastBoundLongitude"/>
 	        <xsl:text>; </xsl:text>
            </xsl:element>
	 </xsl:if>
	 <xsl:for-each select="./identificationInfo/*/descriptiveKeywords/MD_Keywords[type='place']">
 		<xsl:element name="dcterms:spatial">
 			<xsl:value-of select="./keyword"/>
 		</xsl:element>
	 </xsl:for-each>
	</xsl:when>
     </xsl:choose>     

    <!-- rights element conversion.                                          -->
    <!-- ISO COMPREHENSIVE -->

      <xsl:if test="./identificationInfo/*/resourceConstraints/MD_LegalConstraints/accessConstraints">
        <xsl:for-each select="./identificationInfo/*/resourceConstraints/MD_LegalConstraints/accessConstraints">
          <xsl:variable name="rightsVar">
            <!-- it's translated to lower case -->
            <xsl:value-of select="translate(normalize-space(.),$xsltsl-str-upper, $xsltsl-str-lower)"/>
          </xsl:variable>
          <xsl:element name="dc:rights">
            <xsl:choose>
              <xsl:when test="$rightsVar='copyright'">
                <xsl:text>copyright</xsl:text>
	      </xsl:when>
              <xsl:when test="$rightsVar='patent'">
                <xsl:text>patent</xsl:text>
	      </xsl:when>
              <xsl:when test="$rightsVar='patentpending'">
                <xsl:text>patentPending</xsl:text>
	      </xsl:when>
              <xsl:when test="$rightsVar='trademark'">
                <xsl:text>trademark</xsl:text>
	      </xsl:when>
              <xsl:when test="$rightsVar='license'">
                <xsl:text>license</xsl:text>
	      </xsl:when>
              <xsl:when test="$rightsVar='intellectualpropertyrights'">
                <xsl:text>intellectualPropertyRights</xsl:text>
	      </xsl:when>
              <xsl:when test="$rightsVar='restricted'">
                <xsl:text>restricted</xsl:text>
	      </xsl:when>
              <xsl:when test="$rightsVar='otherrestrictions'">
                <xsl:text>otherRestrictions</xsl:text>
	      </xsl:when>
              <xsl:otherwise>
                <!-- If the value is not in this list the data in ISO is not
                correct.  However, the value is translated to DC.-->
                <xsl:value-of select="normalize-space(.)"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:element>
        </xsl:for-each>
      </xsl:if>

      <xsl:if test="./identificationInfo/*/resourceConstraints/MD_LegalConstraints/useConstraints">
     <!-- There is, at least, one-->
        <xsl:for-each select="./identificationInfo/*/resourceConstraints/MD_LegalConstraints/useConstraints">
          <xsl:variable name="rightsVar2">
            <!-- it's translated to lower case -->
            <xsl:value-of select="translate(normalize-space(.),$xsltsl-str-upper, $xsltsl-str-lower)"/>
          </xsl:variable>
         <xsl:element name="dc:rights">
            <xsl:choose>
        	<xsl:when test="$rightsVar2='copyright'">
                  <xsl:text>copyright</xsl:text>
	        </xsl:when>
                <xsl:when test="$rightsVar2='patent'">
                  <xsl:text>patent</xsl:text>
	        </xsl:when>
                <xsl:when test="$rightsVar2='patentpending'">
                  <xsl:text>patentPending</xsl:text>
	        </xsl:when>
                <xsl:when test="$rightsVar2='trademark'">
                  <xsl:text>trademark</xsl:text>
	        </xsl:when>
                <xsl:when test="$rightsVar2='license'">
                  <xsl:text>license</xsl:text>
	        </xsl:when>
                <xsl:when test="$rightsVar2='intellectualpropertyrights'">
                  <xsl:text>intellectualPropertyRights</xsl:text>
	        </xsl:when>
                <xsl:when test="$rightsVar2='restricted'">
                  <xsl:text>restricted</xsl:text>
	        </xsl:when>
                <xsl:when test="$rightsVar2='otherrestrictions'">
                  <xsl:text>otherRestrictions</xsl:text>
	        </xsl:when>
                <xsl:otherwise>
                  <!-- If the value is not in this list the data in ISO is not
                  correct.  However, the value is translated to DC.-->
                  <xsl:value-of select="normalize-space(.)"/>
                </xsl:otherwise>
            </xsl:choose>
          </xsl:element>
        </xsl:for-each>
      </xsl:if>

      <xsl:for-each select="./identificationInfo/*/resourceConstraints/MD_LegalConstraints/otherConstraints">
        <xsl:element name="dc:rights">
          <xsl:value-of select="normalize-space(.)"/>
        </xsl:element>
      </xsl:for-each>

    <!-- ISO CORE N/A ¡¡¡GAP!!! -->
	</qualifieddc>
    <!--/xsl:element-->
  </xsl:template>
</xsl:stylesheet>