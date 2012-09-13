<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                                    xmlns:dc="http://purl.org/dc/elements/1.1/"
                                    xmlns:dcterms="http://purl.org/dc/terms/"
                                    xmlns:xsi="http://www.w3.org/2001/XMSchema-instance" exclude-result-prefixes="dc dcterms xsi">
<!-- xmlns:iso19115="http://www.isotc211.org/iso19115" -->

  <xsl:param name="_date">0000-00-00</xsl:param>

  <xsl:output indent="yes" encoding="ISO-8859-1"/>

  <!-- Stylesheet that translates a correct DC qualified data into a correct   -->
  <!-- ISO data                                                              -->

  <!-- Author: IAAA                                                          -->
  <!-- History: [jnog 5/2/2004] Elimino uso de "iso19115:" 
                [jnog 6/2/2004] Se tratan nombres de clases (CI_Date, CI_ResponsibleParty,...), language -->
  
  <xsl:template match="/">
    <xsl:apply-templates select="qualifieddc"/>
  </xsl:template>

  <xsl:template match="qualifieddc">

    <xsl:variable name="xsltsl-str-lower" select="'a;b;c;d;e;f;g;h;i;j;k;l;m;n;o;p;q;r;s;t;u;v;w;x;y;z'"/>
    <xsl:variable name="xsltsl-str-upper" select="'A;B;C;D;E;F;G;H;I;J;K;L;M;N;O;P;Q;R;S;T;U;V;W;X;Y;Z'"/>

      <!-- A file in ISO represents a resource.  -->

     <!--xsl:if test="rdf:Description"-->
     <MD_Metadata>
     <!--xsl:element name="MD_Metadata"-->
	<xsl:element name="identificationInfo">
      <xsl:element name="MD_DataIdentification">
        <xsl:element name="citation"><xsl:element name="CI_Citation">

    <!-- Element Title conversion:                                           -->
    <!-- There is no title in DC: default value "default Title"              -->
    <!-- There is only one title in DC: perfect.  It goes to citation.title  -->
    <!-- There is more than one titles in DC: the fist goes to citation.title-->
    <!-- the others to citation.alternateTitle, repeteable element           -->

	   <xsl:element name="title">
         	 <xsl:choose>
           	 <xsl:when test="./dc:title">
                <xsl:value-of select="./dc:title"/>
            </xsl:when>
            <xsl:otherwise>
              <!-- No hay title en DC.  ¿Que pongo? -->
              <xsl:text>Default Title</xsl:text>
            </xsl:otherwise>
          </xsl:choose>
          </xsl:element>
	
	
	<!--xsl:text>CON EL PUNTO Y LA BARRA</xsl:text>
	<xsl:value-of select="./dc:title"/>
	
		<xsl:text>SIN EL PUNTO: </xsl:text>
	<xsl:value-of select="dc:title"/>
	
	<xsl:text>TODO </xsl:text>
	<xsl:value-of select="."/-->	
	
	
    <!-- Element Date conversion:                                            -->
    <!-- There is no date in DC: If we can obtain the date of the application-->
    <!-- that uses this stylesheet, this date is used.  If not, the default  -->
    <!-- value 0000-00-00 is used.                                           -->
    <!-- There are one or more date in DC: the element citation.date is      -->
    <!-- repeteable, so, a new date of publication is generated for every    -->
    <!-- dc:date.                                                            -->

          <xsl:choose>
            <xsl:when test="./dc:date">
              <xsl:for-each select="./dc:date">
                <xsl:element name="date"><xsl:element name="CI_Date">
                  <xsl:element name="date">
                    <xsl:value-of select="."/>
                  </xsl:element>
                  <xsl:element name="dateType">
                      <xsl:text>publication</xsl:text>
                  </xsl:element>
                </xsl:element></xsl:element>
              </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>
              <xsl:element name="date"><xsl:element name="CI_Date">
                <xsl:element name="date">
                  <!-- If there is no date, the parameter passed from the
                  application is taken.  If there is no parameter, the default
                  value 0000-00-00 is considered-->
                  <xsl:value-of select="$_date"/>
                </xsl:element>
                <xsl:element name="dateType">
                    <xsl:text>publication</xsl:text>
                </xsl:element>                
              </xsl:element></xsl:element>
            </xsl:otherwise>
          </xsl:choose>

    <!-- Element identifier conversion (1)                                   -->
    <!-- If there are dc:identifiers that not correspond neither file, http  -->
    <!-- ftp, ISSN nor ISBN, a element citation.identifier is generated      -->

          <xsl:for-each select="./dc:identifier">
            <xsl:choose>
              <xsl:when test="contains(normalize-space(.), 'file')
                             or contains(normalize-space(.), 'http')
                             or contains(normalize-space(.), 'ftp')
                             or contains(normalize-space(.), 'www')
                             or contains(normalize-space(.), 'ISSN')
                             or contains(normalize-space(.), 'ISBN')">
              </xsl:when>
                <xsl:otherwise>
                  <xsl:element name="identifier"><xsl:element name="MD_Identifier">
                    <xsl:element name="code">
                      <xsl:value-of select="normalize-space(.)"/>
                    </xsl:element>
                  </xsl:element></xsl:element>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:for-each>


    <!-- Element creator conversion:                                         -->
    <!-- If there is no creator in DC: nothing happen.  In both standards is -->
    <!-- optional.                                                           -->
    <!-- If it appers once or more:  a new citedResponsibleParty with role   -->
    <!-- originator is created for each apparition.                          -->
    <!-- the text of the enter is written in organisationName.               -->

          <xsl:for-each select="./dc:creator">
            <xsl:element name="citedResponsibleParty"><xsl:element name="CI_ResponsibleParty">
              <xsl:element name="organisationName">
                <xsl:value-of select="."/>
              </xsl:element>
              <xsl:element name="role">
                  <xsl:text>originator</xsl:text>
              </xsl:element>
            </xsl:element></xsl:element>
          </xsl:for-each>


    <!-- Element publisher conversion:                                       -->
    <!-- If there is no publisher in DC: nothing happen.  In both standards  -->
    <!-- is optional.                                                        -->
    <!-- If it appers once or more:  a new citedResponsibleParty with role   -->
    <!-- publisher is created for each apparition.                           -->
    <!-- the text of the enter is written in organisationName.               -->


          <xsl:for-each select="./dc:publisher">
            <xsl:element name="citedResponsibleParty"><xsl:element name="CI_ResponsibleParty">
              <xsl:element name="organisationName">
                <xsl:value-of select="."/>
              </xsl:element>
              <xsl:element name="role">
                  <xsl:text>publisher</xsl:text>
              </xsl:element>              
            </xsl:element></xsl:element>
          </xsl:for-each>


    <!-- type element conversion(1):                                         -->
    <!-- If the value is equal to one code of                                -->
    <!-- CI_PresentationFormCode_CodeList, a presentationForm value is       -->
    <!-- generated.                                                          -->

          <xsl:for-each select="./dc:type">
            <xsl:variable name="typeVar">
              <!-- it's traslated to lower case -->
              <xsl:value-of select="translate(normalize-space(.),$xsltsl-str-upper, $xsltsl-str-lower)"/>
            </xsl:variable>
            <xsl:choose>
              <xsl:when test="$typeVar='documentdigital' or
                          $typeVar='documenthardcopy' or
                          $typeVar='imagedigital' or
                          $typeVar='imagehardcopy' or
                          $typeVar='mapdigital' or
                          $typeVar='maphardcopy' or
                          $typeVar='modeldigital' or
                          $typeVar='modelhardcopy' or
                          $typeVar='profiledigital' or
                          $typeVar='profilehardcopy' or
                          $typeVar='tabledigital' or
                          $typeVar='tablehardcopy' or
                          $typeVar='videodigital' or
                          $typeVar='videohardcopy'">
                <xsl:element name="presentationForm">
                    <xsl:value-of select="normalize-space(.)"/>
                </xsl:element>
              </xsl:when>
              <xsl:otherwise>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:for-each>


    <!-- relation element conversion(1):                                     -->
    <!-- If there is no dc:relation in DC, nothing happens because it's      -->
    <!-- optional.                                                           -->
    <!-- If it appears in DC, only the first ocurrence is mapped in          -->
    <!-- series.name.  The rest occurrences are lost.                        -->

          <xsl:if test="./dc:relation">
            <xsl:element name="series"><xsl:element name="CI_Series">
              <xsl:element name="name">
                <xsl:value-of select="./dc:relation"/>
              </xsl:element>
            </xsl:element></xsl:element>
          </xsl:if>

    <!-- If there are more than one title, all except the first go to        -->
    <!-- alternateTitle.                                                     -->

          <xsl:for-each select="./dc:title">
            <xsl:choose>
              <xsl:when test="position()=1">
                  <!-- The first dc:title go to citation.title -->
              </xsl:when>
              <xsl:otherwise>
                <xsl:element name="alternateTitle">
                  <xsl:value-of select="."/>
                </xsl:element>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:for-each>



    <!-- Element identifier conversion (2)                                   -->
    <!-- If there are dc:identifiers that contains the word ISSN or ISBN a   -->
    <!-- element citation.issn or citation.isbn is generated                 -->

          <xsl:for-each select="./dc:identifier">
            <xsl:if test="contains(normalize-space(.), 'ISBN')">
              <xsl:element name="ISBN">
                <xsl:value-of select="."/>
              </xsl:element>
            </xsl:if>
          </xsl:for-each>

          <xsl:for-each select="./dc:identifier">
            <xsl:if test="contains(normalize-space(.), 'ISSN')">
              <xsl:element name="ISSN">
                <xsl:value-of select="."/>
              </xsl:element>
            </xsl:if>
          </xsl:for-each>

        </xsl:element> </xsl:element><!-- De citation -->


    <!-- description element conversion:                                     -->
    <!-- An only abstract will be generated, so, if there are more than one  -->
    <!-- occurrences of dc:description, they will be concatenated with carry -->
    <!-- returns.                                                            -->

        <xsl:choose>
          <xsl:when test="./dc:description">
            <xsl:element name="abstract">
              <xsl:variable name="abstractElement"/>
              <xsl:for-each select="./dc:description">
                <xsl:value-of select="concat($abstractElement, normalize-space(.))"/>
                <xsl:if test="position()!=last()">
                  <xsl:value-of select="concat($abstractElement, '&#xA;')"/>
                </xsl:if>
              </xsl:for-each>
              <xsl:value-of select="$abstractElement"/>
            </xsl:element>
          </xsl:when>
          <xsl:otherwise>
            <xsl:element name="abstract">
              <!-- This element is compulsory, so, if there is no dc:description
              in DC, a default value is generated. -->
              <xsl:text>Default abstract</xsl:text>
            </xsl:element>
          </xsl:otherwise>
        </xsl:choose>


    <!-- contributor element conversion:                                     -->
    <!-- If there is no dc:contributor in DC, it doesn't matter because in   -->
    <!-- both languages is optional.                                         -->
    <!-- If it appears more than once, a new laber credit will be generated  -->
    <!-- for each dc:contributor.                                            -->
        <xsl:for-each select="./dc:contributor">
          <xsl:element name="credit">
             <xsl:value-of select="."/>
          </xsl:element>
        </xsl:for-each>



    <!-- Element creator conversion(2):                                      -->
    <!-- If there is no creator in DC: nothing happen.  In both standards is -->
    <!-- optional.                                                           -->
    <!-- If it appers once or more:  a new pointOfContact is created         -->
    <!-- for each apparition.                                                -->
    <!-- the text of the enter is written in organisationName.               -->

        <xsl:for-each select="./dc:creator">
          <xsl:element name="pointOfContact"><xsl:element name="CI_ResponsibleParty">
            <xsl:element name="organisationName">
              <xsl:value-of select="."/>
            </xsl:element>          
            <xsl:element name="role">
                <xsl:text>originator</xsl:text>
            </xsl:element>
          </xsl:element></xsl:element>
        </xsl:for-each>


    <!-- Element publisher conversion:                                       -->
    <!-- If there is no publisher in DC: nothing happen.  In both standards  -->
    <!-- is optional.                                                        -->
    <!-- If it appers once or more:  a new pointOfContact with role publisher-->
    <!-- is created for each apparition.                                     -->
    <!-- the text of the enter is written in organisationName.               -->

        <xsl:for-each select="./dc:publisher">
          <xsl:element name="pointOfContact"><xsl:element name="CI_ResponsibleParty">
            <xsl:element name="organisationName">
               <xsl:value-of select="."/>
            </xsl:element>
            <xsl:element name="role">
                <xsl:text>publisher</xsl:text>
            </xsl:element>
          </xsl:element></xsl:element>
        </xsl:for-each>



    <!-- rights element conversion:                                          -->
    <!-- If there is no rights in DC, nothing happens because in ISO is      -->
    <!-- optional.                                                           -->
    <!-- If there are rights, they will be mapped with useConstraints if the -->
    <!-- value is included in the restricted list.  If not, useConstraints   -->
    <!-- will be documented with otherConstraints, and the value will be     -->
    <!-- mapped with otherRestrictions.                                      -->

        <xsl:if test="./dc:rights">

          <xsl:element name="resourceConstraints">
		 <xsl:element name="MD_LegalConstraints">
		 
            <xsl:variable name="rightsISO"/>
            <xsl:for-each select="./dc:rights">
              <xsl:variable name="rightsVar">
                <!-- it's traslated to lower case -->
                <xsl:value-of select="translate(normalize-space(.),$xsltsl-str-upper, $xsltsl-str-lower)"/>
              </xsl:variable>

              <xsl:choose>
                <xsl:when test="$rightsVar='copyright'">
                  <xsl:element name="useConstraints">
                      <xsl:value-of select="normalize-space(.)"/>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$rightsVar='patent'">
                  <xsl:element name="useConstraints">
                      <xsl:value-of select="normalize-space(.)"/>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$rightsVar='patentpending'">
                  <xsl:element name="useConstraints">
                      <xsl:value-of select="normalize-space(.)"/>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$rightsVar='trademark'">
                  <xsl:element name="useConstraints">
                      <xsl:value-of select="normalize-space(.)"/>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$rightsVar='license'">
                  <xsl:element name="useConstraints">
                      <xsl:value-of select="normalize-space(.)"/>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$rightsVar='intellectualpropertyrights'">
                  <xsl:element name="useConstraints">
                      <xsl:value-of select="normalize-space(.)"/>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$rightsVar='restricted'">
                  <xsl:element name="useConstraints">
                      <xsl:value-of select="normalize-space(.)"/>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$rightsVar='otherrestrictions'">
                  <xsl:element name="useConstraints">
                      <xsl:value-of select="normalize-space(.)"/>
                  </xsl:element>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:element name="useConstraints">
                      <xsl:text>otherRestrictions</xsl:text>
                  </xsl:element>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:for-each>

            <xsl:for-each select="./dc:rights">
              <xsl:variable name="rightsVar">
                <!-- it's traslated to lower case -->
                <xsl:value-of select="translate(normalize-space(.),$xsltsl-str-upper, $xsltsl-str-lower)"/>
              </xsl:variable>
              <xsl:choose>
                <xsl:when test="$rightsVar='copyright'">
               </xsl:when>
                <xsl:when test="$rightsVar='patent'">
                </xsl:when>
                <xsl:when test="$rightsVar='patentpending'">
                </xsl:when>
                <xsl:when test="$rightsVar='trademark'">
                </xsl:when>
                <xsl:when test="$rightsVar='license'">
                </xsl:when>
                <xsl:when test="$rightsVar='intellectualpropertyrights'">
                </xsl:when>
                <xsl:when test="$rightsVar='restricted'">
                </xsl:when>
                <xsl:when test="$rightsVar='otherrestrictions'">
                </xsl:when>
                <xsl:otherwise>
                  <xsl:element name="otherConstraints">
                      <xsl:value-of select="normalize-space(.)"/>
                  </xsl:element>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:for-each>
		</xsl:element>
          </xsl:element>
        </xsl:if>



    <!-- format element conversion (1):                                      -->
    <!-- If in DC doesn't appear, nothing happens because is optional.       -->
    <!-- If it appears N times, a new distributorFormat will be generated    -->
    <!-- for each dc:format.                                                 -->

        <xsl:for-each select="./dc:format">
          <xsl:element name="resourceFormat">
          <xsl:element name="MD_Format">
            <xsl:choose>
              <xsl:when test="contains(normalize-space(.), '//')">
                <xsl:element name="name">
                  <xsl:value-of select="substring-before(normalize-space(.), '//')"/>
                </xsl:element>
                <xsl:element name="version">
                  <xsl:value-of select="substring-after(normalize-space(.), '//')"/>
                </xsl:element>
              </xsl:when>
              <xsl:otherwise>
                <xsl:element name="name">
                  <xsl:value-of select="normalize-space(.)"/>
                </xsl:element>
                <xsl:element name="version">
                  <xsl:text>Unknown</xsl:text>
                </xsl:element>
              </xsl:otherwise>
            </xsl:choose>
            </xsl:element>
          </xsl:element>
        </xsl:for-each>


    <!-- Element subject conversion:                                         -->
    <!-- If there is no dc:subject in DC nothing happens because in both     -->
    <!-- standars are optative.                                              -->
    <!-- If there are some occurrences, only the values that have not been   -->
    <!-- mapped as topicCategory will be mapped here, as a keyword.          -->
    <!-- In this case, also the element type is completed with the "theme"   -->
    <!-- value.                                                              -->

	<xsl:element name="descriptiveKeywords">
        <xsl:for-each select="./dc:subject">
            <xsl:variable name="enter">
              <!-- it's translated to lower case -->
              <xsl:value-of select="translate(normalize-space(.),$xsltsl-str-upper, $xsltsl-str-lower)"/>
            </xsl:variable>
          <xsl:choose>
            <xsl:when test="$enter='farming' or $enter=' farming' or $enter='farming ' or $enter=' farming '">
            </xsl:when>
            <xsl:when test="$enter='biota' or $enter=' biota' or $enter='biota ' or $enter=' biota '">
            </xsl:when>
            <xsl:when test="$enter='boundaries' or $enter=' boundaries' or $enter='boundaries ' or $enter=' boundaries '">
            </xsl:when>
            <xsl:when test="$enter='climatologymeteorologyatmosphere' or $enter=' climatologymeteorologyatmosphere' or $enter='climatologymeteorologyatmosphere ' or $enter=' climatologymeteorologyatmosphere '">
            </xsl:when>
            <xsl:when test="$enter='economy' or $enter=' economy' or $enter='economy ' or $enter=' economy '">
            </xsl:when>
            <xsl:when test="$enter='elevation' or $enter=' elevation' or $enter='elevation ' or $enter=' elevation '">
            </xsl:when>
            <xsl:when test="$enter='environment' or $enter=' environment' or $enter='environment ' or $enter=' environment '">
            </xsl:when>
            <xsl:when test="$enter='geoscientificinformation' or $enter=' geoscientificinformation' or $enter='geoscientificinformation ' or $enter=' geoscientificinformation '">
            </xsl:when>
            <xsl:when test="$enter='health' or $enter=' health' or $enter='health ' or $enter=' health '">
            </xsl:when>
            <xsl:when test="$enter='imagerybasemapsearthc' or $enter=' imagerybasemapsearthc' or $enter='imagerybasemapsearthc ' or $enter=' imagerybasemapsearthc '">
            </xsl:when>
            <xsl:when test="$enter='intelligencemilitary' or $enter=' intelligencemilitary' or $enter='intelligencemilitary ' or $enter=' intelligencemilitary '">
            </xsl:when>
            <xsl:when test="$enter='inlandwaters' or $enter=' inlandwaters' or $enter='inlandwaters ' or $enter=' inlandwaters '">
            </xsl:when>
            <xsl:when test="$enter='location' or $enter=' location' or $enter='location ' or $enter=' location '">
            </xsl:when>
            <xsl:when test="$enter='oceans' or $enter=' oceans' or $enter='oceans ' or $enter=' oceans '">
            </xsl:when>
            <xsl:when test="$enter='planningcadastre' or $enter=' planningcadastre' or $enter='planningcadastre ' or $enter=' planningcadastre '">
            </xsl:when>
            <xsl:when test="$enter='society' or $enter=' society' or $enter='society ' or $enter=' society '">
            </xsl:when>
            <xsl:when test="$enter='structure' or $enter=' structure' or $enter='structure ' or $enter=' structure '">
            </xsl:when>
            <xsl:when test="$enter='transportation' or $enter=' transportation' or $enter='transportation ' or $enter=' transportation '">
            </xsl:when>
            <xsl:when test="$enter='utilitiescommunication' or $enter=' utilitiescommunication' or $enter='utilitiescommunication ' or $enter=' utilitiescommunication '">
            </xsl:when>
            <xsl:otherwise>
              <xsl:element name="MD_Keywords">
                <xsl:element name="keyword">
                  <xsl:value-of select="."/>
                </xsl:element>
                <xsl:element name="type">
                    <xsl:text>theme</xsl:text>
                </xsl:element>
              </xsl:element>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:for-each>

    <!-- coverage:placename element conversion:                              -->
    <!-- For separating the different names, a template is used.  This       -->
    <!-- template separated the string by the commas.                        -->

        <xsl:for-each select="./dcterms:spatial">
		<xsl:choose>
			<xsl:when test="contains(., 'northlimit')">
			</xsl:when>
			<xsl:otherwise>
        			<xsl:element name="MD_Keywords">
         				 <xsl:element name="keyword">
           					 <xsl:value-of select="."/>
         				 </xsl:element>
          				<xsl:element name="type">
              				<xsl:text>place</xsl:text>
          				</xsl:element>
        			</xsl:element>       			
			</xsl:otherwise>
		</xsl:choose>
        </xsl:for-each>
        
        

    <!-- coverage:periodName element conversion:                             -->
    <!-- Like the previous one.                                              -->
        <xsl:for-each select="./dcterms:temporal">
		<xsl:choose>
			<xsl:when test="contains(., 'begin')">
			</xsl:when>
			<xsl:otherwise>
        			<xsl:element name="MD_Keywords">
         				 <xsl:element name="keyword">
           					 <xsl:value-of select="."/>
         				 </xsl:element>
          				<xsl:element name="type">
              				<xsl:text>temporal</xsl:text>
          				</xsl:element>
        			</xsl:element>       			
			</xsl:otherwise>
		</xsl:choose>
        </xsl:for-each>
        
     </xsl:element>

    <!-- relation element conversion(2):                                     -->
    <!-- If there is no dc:relation in DC, nothing happens because it's      -->
    <!-- optional.                                                           -->
    <!-- If it appears in DC, an aggregationInfo will be generated for each  -->
    <!-- dc:relation element.                                                -->
        <xsl:if test="./dc:relation">
        <xsl:element name="aggregationInfo">
          <xsl:for-each select="./dc:relation">
            <xsl:element name="MD_AggregateInformation">
              <xsl:element name="aggregateDataSetIdentifier">
                <xsl:element name="code">
                  <xsl:value-of select="normalize-space(.)"/>
                </xsl:element>
              </xsl:element>
              <xsl:element name="associationType">
                   <xsl:text>crossReference</xsl:text>
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
          </xsl:element>
        </xsl:if>


    <!-- type element conversion(2):                                         -->
    <!-- If there is no type in DC, nothing happens because it's not         -->
    <!-- compulsory in IsoCore.                                              -->
    <!-- If it appears more than one, a new spatialRepresentationType will   -->
    <!-- be generated for each dc:type.                                      -->

        <xsl:for-each select="./dc:type">
            <xsl:variable name="typeVar2">
              <!-- it's translated to lower case -->
              <xsl:value-of select="translate(normalize-space(.),$xsltsl-str-upper, $xsltsl-str-lower)"/>
            </xsl:variable>
            <xsl:if test="$typeVar2='vector' or
                          $typeVar2='grid' or
                          $typeVar2='texttable' or
                          $typeVar2='tin' or
                          $typeVar2='stereomodel' or
                          $typeVar2='video'">
              <xsl:element name="spatialRepresentationType">
                  <xsl:value-of select="normalize-space(.)"/>
              </xsl:element>
            </xsl:if>
        </xsl:for-each>


    <!-- language element conversion.                                        -->
        <xsl:choose>
          <xsl:when test="./dc:language">
            <xsl:for-each select="./dc:language">
              <xsl:element name="language">
                <!-- Here begin a big dissertation about the correct position
                of the value:  isoCode, isoName or otherLang.                -->
                <xsl:variable name="lang">
                  <xsl:value-of select="."/>
                </xsl:variable>
                <xsl:choose>
                  <xsl:when test="string-length($lang) = 3">
                  <!-- If the text has three letters, it's supposed to be an
                  isoCode.  -->
                    <!--xsl:element name="isoCode"-->
                      <xsl:value-of select="$lang"/>
                    <!--/xsl:element-->
                  </xsl:when>
                  <xsl:otherwise>
                    <!-- If not, it's putted in otherLang.   -->
                    <!--xsl:element name="otherLang"-->
                      <xsl:value-of select="$lang"/>
                    <!--/xsl:element-->
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:element>
            </xsl:for-each>
          </xsl:when>
          <xsl:otherwise>
              <xsl:element name="language">
                <!--xsl:element name="isoCode"-->
                  <xsl:text>eng</xsl:text>
                </xsl:element>
              <!--/xsl:element-->
          </xsl:otherwise>
        </xsl:choose>


    <!-- element subject conversion(2):                                      -->
    <!-- This element is compulsory only if hierarchyLevel=Dataset.          -->
    <!-- Hence, if no dc:subject appears in DC, it's assumed that            -->
    <!-- hierarchyLevel!=dataset.                                            -->
    <!-- As ISO-CORE:                                                        -->

        <xsl:choose>
          <xsl:when test="./dc:subject">
            <xsl:for-each select="./dc:subject">
              <xsl:variable name="enter">
                <!-- it's traslated to lower case -->
                <xsl:value-of select="translate(normalize-space(.),$xsltsl-str-upper, $xsltsl-str-lower)"/>
              </xsl:variable>

              <xsl:choose>
                <xsl:when test="$enter='farming' or $enter=' farming' or $enter='farming ' or $enter=' farming '">
                   <xsl:element name="topicCategory">
                      <xsl:text>farming</xsl:text>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$enter='biota' or $enter=' biota' or $enter='biota ' or $enter=' biota '">
                   <xsl:element name="topicCategory">
                      <xsl:text>biota</xsl:text>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$enter='boundaries' or $enter=' boundaries' or $enter='boundaries ' or $enter=' boundaries '">
                   <xsl:element name="topicCategory">
                      <xsl:text>boundaries</xsl:text>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$enter='climatologymeteorologyatmosphere' or $enter=' climatologymeteorologyatmosphere' or $enter='climatologymeteorologyatmosphere ' or $enter=' climatologymeteorologyatmosphere '">
                  <xsl:element name="topicCategory">
                      <xsl:text>climatologyMeteorologyAtmosphere</xsl:text>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$enter='economy' or $enter=' economy' or $enter='economy ' or $enter=' economy '">
                  <xsl:element name="topicCategory">
                      <xsl:text>economy</xsl:text>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$enter='elevation' or $enter=' elevation' or $enter='elevation ' or $enter=' elevation '">
                  <xsl:element name="topicCategory">
                      <xsl:text>elevation</xsl:text>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$enter='environment' or $enter=' environment' or $enter='environment ' or $enter=' environment '">
                  <xsl:element name="topicCategory">
                      <xsl:text>environment</xsl:text>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$enter='geoscientificinformation' or $enter=' geoscientificinformation' or $enter='geoscientificinformation ' or $enter=' geoscientificinformation '">
                  <xsl:element name="topicCategory">
                      <xsl:text>geoscientificInformation</xsl:text>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$enter='health' or $enter=' health' or $enter='health ' or $enter=' health '">
                  <xsl:element name="topicCategory">
                      <xsl:text>health</xsl:text>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$enter='imagerybasemapsearthcover' or $enter=' imagerybasemapsearthcover' or $enter='imagerybasemapsearthcover ' or $enter=' imagerybasemapsearthcover '">
                  <xsl:element name="topicCategory">
                      <xsl:text>imageryBaseMapsEarthCover</xsl:text>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$enter='intelligencemilitary' or $enter=' intelligencemilitary' or $enter='intelligencemilitary ' or $enter=' intelligencemilitary '">
                  <xsl:element name="topicCategory">
                      <xsl:text>intelligenceMilitary</xsl:text>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$enter='inlandwaters' or $enter=' inlandwaters' or $enter='inlandwaters ' or $enter=' inlandwaters '">
                  <xsl:element name="topicCategory">
                      <xsl:text>inlandWaters</xsl:text>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$enter='location' or $enter=' location' or $enter='location ' or $enter=' location '">
                  <xsl:element name="topicCategory">
                      <xsl:text>location</xsl:text>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$enter='oceans' or $enter=' oceans' or $enter='oceans ' or $enter=' oceans '">
                  <xsl:element name="topicCategory">
                      <xsl:text>oceans</xsl:text>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$enter='planningcadastre' or $enter=' planningcadastre' or $enter='planningcadastre ' or $enter=' planningcadastre '">
                  <xsl:element name="topicCategory">
                      <xsl:text>planningCadastre</xsl:text>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$enter='society' or $enter=' society' or $enter='society ' or $enter=' society '">
                  <xsl:element name="topicCategory">
                      <xsl:text>society</xsl:text>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$enter='structure' or $enter=' structure' or $enter='structure ' or $enter=' structure '">
                  <xsl:element name="topicCategory">
                      <xsl:text>structure</xsl:text>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$enter='transportation' or $enter=' transportation' or $enter='transportation ' or $enter=' transportation '">
                  <xsl:element name="topicCategory">
                      <xsl:text>transportation</xsl:text>
                  </xsl:element>
                </xsl:when>
                <xsl:when test="$enter='utilitiescommunication' or $enter=' utilitiescommunication' or $enter='utilitiescommunication ' or $enter=' utilitiescommunication '">
                  <xsl:element name="topicCategory">
                      <xsl:text>utilitiesCommunication</xsl:text>
                  </xsl:element>
                </xsl:when>
                <xsl:otherwise>
                  <!-- this field is compulsory only if the resource has a
                    hierarchyLevel equals to Dataset.  Hense, if the element
                    doesn't appear in the list, it's not translated to DC    -->
                </xsl:otherwise>
              </xsl:choose>
            </xsl:for-each>
          </xsl:when>
          <xsl:otherwise>
            <!-- nothing is written.  In ISO it's understood by default that
            it's a dataset. -->
          </xsl:otherwise>
        </xsl:choose>



    <!-- coverage:periodTime element conversion.                             -->
    
	<xsl:if test="./dcterms:temporal or ./dcterms:spatial">

  	   <xsl:if test="count(./dcterms:temporal[contains(., 'begin')])>0">
		<xsl:element name="extent"><xsl:element name="EX_Extent">
				<xsl:element name="temporalElement">
					<xsl:for-each select="./dcterms:temporal">
						<xsl:if test="contains(., 'begin')">
							<xsl:element name="EX_TemporalExtent">
							   <xsl:element name="extent">
                    					<xsl:element name="beginEnd">
                     						 <xsl:element name="begin">
                        							<xsl:value-of select="substring-before(substring-after(., 'begin='), ';')"/>
                      						  </xsl:element>
                      						  <xsl:element name="end">
						                        <xsl:value-of select="substring-before(substring-after(., 'end='), ';')"/>
				       		               </xsl:element>
					                    </xsl:element>
					                  </xsl:element>
							</xsl:element>
						</xsl:if>
					</xsl:for-each>
		 </xsl:element></xsl:element></xsl:element>
	     </xsl:if>
		
	     <xsl:if test="count(./dcterms:spatial[contains(., 'northlimit')])>0">
		<xsl:element name="extent"><xsl:element name="EX_Extent">
   		  <xsl:element name="geographicElement">
					<xsl:for-each select="./dcterms:spatial">
						<xsl:if test="contains(., 'northlimit')">
							<xsl:element name="EX_GeographicBoundingBox">
		                  			     <xsl:element name="westBoundLongitude">
						                    <xsl:value-of select="substring-before(substring-after(., 'westlimit='), ';')"/>
					                   </xsl:element>
					                  <xsl:element name="eastBoundLongitude">
					                    	<xsl:value-of select="substring-before(substring-after(., 'eastlimit='), ';')"/>
					                  </xsl:element>
					                  <xsl:element name="southBoundLatitude">
					                    	<xsl:value-of select="substring-before(substring-after(., 'southlimit='), ';')"/>
					                  </xsl:element>
				       	           <xsl:element name="northBoundLatitude">
					                    	<xsl:value-of select="substring-before(substring-after(., 'northlimit='), ';')"/>
					                  </xsl:element>					
							
							</xsl:element>
						</xsl:if>
					</xsl:for-each>

		   </xsl:element></xsl:element></xsl:element>
		</xsl:if>

	</xsl:if>



      </xsl:element>  <!-- _MD_Identification -->
	</xsl:element>  <!-- identificationInfo-->
    <!-- source element conversion:                                          -->
    <!-- If there is no source in DC, nothing happens because it's optional  -->
    <!-- If it appears, a new dataQualityInfo.lineage.source is generated by -->
    <!-- each occurrence.  dataQualityInfo has, also, a compulsory attribute -->
    <!-- that is, scope.  This attribute will be automatically generated with-->
    <!-- the value 'dataset'.                                                -->


      <xsl:if test="./dc:source">
        <xsl:element name="dataQualityInfo">
        <xsl:element name="DQ_DataQuality">
          <xsl:element name="scope">
            <xsl:element name="level">
                <xsl:text>dataset</xsl:text>
            </xsl:element>
          </xsl:element>
          <xsl:element name="lineage">
           <xsl:element name="LI_Lineage">
            <xsl:for-each select="./dc:source">
              <xsl:element name="source">
               <xsl:element name="LI_Source">
                <xsl:element name="description">
                  <xsl:value-of select="."/>
                </xsl:element>
                </xsl:element>
              </xsl:element>
            </xsl:for-each>
           </xsl:element> 
          </xsl:element>
        </xsl:element>
        </xsl:element>
      </xsl:if>


    <!-- format element conversion (2):                                      -->
    <!-- If in DC doesn't appear, nothing happens because is optional.       -->
    <!-- If it appears N times, a new distributionInfo will be generated     -->
    <!-- for each dc:format.                                                 -->

    <!-- Element identifier conversion (3)                                   -->
    <!-- If there are dc:identifiers that correspond with a file a element   -->
    <!-- a MD_DigitalTransferOptions is also generated                       -->


      <xsl:if test="./dc:format">
        <!-- DistributionInfo must appear only once -->
        <xsl:element name="distributionInfo">
	  <xsl:element name="MD_Distribution">
          <xsl:for-each select="./dc:format">
            <xsl:variable name="pos">
              <xsl:value-of select="position()"/>
            </xsl:variable>

            <xsl:element name="distributionFormat">
            	<xsl:element name="MD_Format">
              <xsl:choose>
                <xsl:when test="contains(normalize-space(.), '//')">
                  <xsl:element name="name">
                    <xsl:value-of select="substring-before(normalize-space(.), '//')"/>
                  </xsl:element>
                  <xsl:element name="version">
                    <xsl:value-of select="substring-after(normalize-space(.), '//')"/>
                  </xsl:element>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:element name="name">
                    <xsl:value-of select="normalize-space(.)"/>
                  </xsl:element>
                  <xsl:element name="version">
                    <xsl:text>Unknown</xsl:text>
                  </xsl:element>
                </xsl:otherwise>
              </xsl:choose>
             </xsl:element>
            </xsl:element>

          </xsl:for-each>
          <xsl:for-each select="./dc:identifier">
            <xsl:variable name="identifierVar">
              <!-- it's translated to lower case -->
              <xsl:value-of select="translate(normalize-space(.),$xsltsl-str-upper, $xsltsl-str-lower)"/>
            </xsl:variable>
            <xsl:if test="contains($identifierVar, 'file')
                          or contains($identifierVar, 'http')
                          or contains($identifierVar, 'ftp')
                          or contains($identifierVar, 'www')">
            <xsl:element name="transferOptions">
            <xsl:element name="MD_DigitalTransferOptions">
              <xsl:element name="onLine"><xsl:element name="CI_OnlineResource">
                <xsl:element name="linkage">
                  <xsl:value-of select="."/>
                </xsl:element>
              </xsl:element></xsl:element>
             </xsl:element> 
            </xsl:element>
            </xsl:if>
          </xsl:for-each>
          
 	  </xsl:element>
        </xsl:element>
      </xsl:if>


	<!-- file identifier -->
	<xsl:if test="./@fileID">
		<xsl:element name="fileIdentifier">
			<xsl:value-of select="./@fileID"/>
		</xsl:element>
	</xsl:if>
	
	
	<!-- language -->
	<xsl:if test="./@xml:lang">
		<xsl:element name="language">
			<!--xsl:element name="isoCode"-->
				<xsl:value-of select="./@xml:lang"/>
			<!--/xsl:element-->
		</xsl:element>
	</xsl:if>



    <!-- type element conversion(3) (iso-comprehensive):                     -->
    <!-- If there is no type in DC, nothing happens, it's optional.          -->
    <!-- If it appears more than one, a new hierarchyLevel will              -->
    <!-- be generated for each dc:type.                                      -->
    <!-- Also the values of DCMI Type Vocabulary are mapped into             -->
    <!-- hierarchyLevel.                                                     -->

      <xsl:for-each select="./dc:type">
        <xsl:variable name="typeVar3">
          <!-- it's translated to lower case -->
          <xsl:value-of select="translate(normalize-space(.),$xsltsl-str-upper, $xsltsl-str-lower)"/>
        </xsl:variable>
        <xsl:choose>
         <xsl:when test="$typeVar3='attribute' or
                         $typeVar3='attributetype' or
                         $typeVar3='collectionhardware' or
                         $typeVar3='collectionsession' or
                         $typeVar3='dataset' or
                         $typeVar3='series' or
                         $typeVar3='nongeographicdataset' or
                         $typeVar3='dimensiongroup' or
                         $typeVar3='feature' or
                         $typeVar3='featuretype' or
                         $typeVar3='propertytype' or
                         $typeVar3='fieldsession' or
                         $typeVar3='software' or
                         $typeVar3='service' or
                         $typeVar3='model' or
                         $typeVar3='tile'">
            <xsl:element name="hierarchyLevel">
                <xsl:value-of select="normalize-space(.)"/>
            </xsl:element>
          </xsl:when>
         <xsl:when test="$typeVar3='collection'">
            <xsl:element name="hierarchyLevel">
                <xsl:text>series</xsl:text>
            </xsl:element>
          </xsl:when>
          <xsl:when test="$typeVar3='dataset'">
            <xsl:element name="hierarchyLevel">
                  <xsl:text>dataset</xsl:text>
            </xsl:element>
          </xsl:when>
          <!--xsl:when test="$typeVar3='event'">
            <xsl:element name="hierarchyLevel">
                  <xsl:text>collectionSession</xsl:text>
            </xsl:element>
          </xsl:when-->
          <xsl:when test="$typeVar3='image'">
            <xsl:element name="hierarchyLevel">
                  <xsl:text>dataset</xsl:text>
            </xsl:element>
          </xsl:when>
          <xsl:when test="$typeVar3='service'">
            <xsl:element name="hierarchyLevel">
                  <xsl:text>service</xsl:text>
            </xsl:element>
          </xsl:when>
          <xsl:when test="$typeVar3='software'">
            <xsl:element name="hierarchyLevel">
                  <xsl:text>sotfware</xsl:text>
            </xsl:element>
          </xsl:when>
          <xsl:otherwise>
            <!-- If the value doesn't correspond with one of the values of
            the list, it doesn't matter.  hierarchyLevel is not compulsory -->
          </xsl:otherwise>
        </xsl:choose>
      </xsl:for-each>


    <!-- Element publisher conversion:                                       -->
    <!-- If there is no publisher in DC: the element contact in ISO is       -->
    <!-- compulsory.  A default value is needed.                             -->
    <!-- If it appers once or more:  a new contact with role   -->
    <!-- publisher is created for each apparition.                           -->
    <!-- the text of the enter is written in organisationName.               -->

      <xsl:choose>
        <xsl:when test="./dc:publisher">
          <xsl:for-each select="./dc:publisher">
            <xsl:element name="contact"><xsl:element name="CI_ResponsibleParty">
              <xsl:element name="organisationName">
                <xsl:value-of select="./dc:publisher"/>
              </xsl:element>            
              <xsl:element name="role">
                  <xsl:text>publisher</xsl:text>
              </xsl:element>
            </xsl:element></xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="contact"><xsl:element name="CI_ResponsibleParty">
            <xsl:element name="organisationName">
              <xsl:text>Default organisation</xsl:text>
            </xsl:element>
            <xsl:element name="role">
                <xsl:text>publisher</xsl:text>
            </xsl:element>
          </xsl:element></xsl:element>
        </xsl:otherwise>
      </xsl:choose>

    <!-- Here, a final compulsory date is generated.  The publication date   -->
    <!-- is used for that.                                                   -->

      <xsl:element name="dateStamp">
        <xsl:choose>
          <xsl:when test="./dc:date">
            <xsl:value-of select="./dc:date"/>
          </xsl:when>
          <xsl:otherwise>
            <!-- If there is no date in the DC date, the same that with the
            publication date is done. -->
            <xsl:value-of select="$_date"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:element>
      <!-- dataSetURI element.  The first URI is used.		             -->
      <xsl:if test="count(./dc:identifier[contains(.,'file') or contains(., 'www') or contains(., 'http') or contains (., 'ftp')]) > 0">
        <xsl:element name="dataSet">
          <xsl:value-of select="./dc:identifier[contains(.,'file')  or contains(., 'www') or contains(., 'http') or contains (., 'ftp')][1]"/>
        </xsl:element>
      </xsl:if>

     <!--/xsl:element-->  <!-- MD_Metadata -->

	</MD_Metadata>

   <!--/xsl:if--> <!-- of: <xsl:if test="rdf:Description"-->
  </xsl:template>

</xsl:stylesheet>