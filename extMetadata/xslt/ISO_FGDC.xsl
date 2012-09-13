<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:iso19115="http://www.isotc211.org/iso19115" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<xsl:output method="xml" indent="yes" encoding="ISO-8859-1"/>
	<!-- xsl:output doctype-system="file:///D:/gente/almenara/pasarelas/xml/fgdc-std-001-1998.dtd"/ -->
	<!-- Esta linea se modificara en funcion de la ubicacion del dtd-->
	<!-- AUTOR: María Pilar Torres                                                           -->
	<!-- FECHA:  07/03                                                                             -->
	<!-- DESCRIPCION:	 Hoja de estilo para trasladar ficheros XML que validan el estandar ISO 19115              -->
	<!-- al estandar FGDC.       		-->
	<!-- SECCIONES TRADUCIDAS: identification information, dataqualityInfo, distribution information y constraint information -->
	<!-- MODIFICACIONES: Para la realizacion de esta hoja de estilo se ha utilizado la original (FGDC - Iso cortos) y se han   -->
	<!-- hecho las modificaciones oportunas.			-->
	<!-- Esta hoja varia con respecto a la ISO_FGDC_LARGOS en que no transforma imagen.	-->
	<!-- [jnog 5/2/2004] Se elimina el uso del namespace "iso19115:"
	     [jnog 6/2/2004] Se trata el nombre de clases (CI_Date, CI_ResponsibleParty,...),language 
	     [jlacasta mayo 04 Fecha de inicio y fin de los metadatos no se mostraba adecuadamente
	                                  Actualizamos la forma de pasar las imagenes del browse graphic al nuevo formato
	                                  La sección de referencia espacial se pierde entera
	                                  Se ha harreglado la sección de sistemas de referencia que se perdian 7 campos-->
	<xsl:template match="/">
		<xsl:apply-templates select="MD_Metadata"/>
	</xsl:template>
	<xsl:template match="MD_Metadata">
		<xsl:element name="metadata">
			<!-- IDENTIFICATION INFO -->
			<xsl:apply-templates select="./identificationInfo/MD_DataIdentification"/>
			<!--DATA QUALITY INFO -->
			<xsl:if test="./dataQualityInfo">
				<xsl:element name="dataqual">
					<xsl:apply-templates select="./dataQualityInfo"/>
				</xsl:element>
			</xsl:if>
			<!-- ENTITY AND ATTRIBUTE INFORMATION -->
			<xsl:if test="contentInfo">
				<xsl:element name="eainfo">
					<xsl:apply-templates select="./contentInfo"/>
				</xsl:element>
			</xsl:if>
			<!--DISTRIBUTION INFO -->
			<xsl:apply-templates select="./distributionInfo/MD_Distribution/distributor/MD_Distributor"/>
			<!-- METADATA MAINTENANCE  -->
			<xsl:element name="metainfo">
				<xsl:element name="metd">
					<xsl:value-of select="./dateStamp"/>
				</xsl:element>
				<!-- no se con que mapear metrd-->
				<!-- ambos son (0..1)-->
				<xsl:if test="metadataMaintenance/MD_MaintenanceInformation/dateOfNextUpdate">
					<xsl:element name="metrd"/>
					<xsl:element name="metfrd">
						<xsl:value-of select="./metadataMaintenance/MD_MaintenanceInformation/dateOfNextUpdate"/>
					</xsl:element>
				</xsl:if>
				<xsl:element name="metc">
					<xsl:apply-templates select="./contact/CI_ResponsibleParty"/>
				</xsl:element>
				<xsl:element name="metstdn">
					<!-- autogeneración del nombre del estándar -->
					<xsl:text>FGDC Content Standards for Digital Geospatial Metadata</xsl:text>
					<!--<xsl:value-of select="./MetadataStandardName"/>-->
				</xsl:element>
				<xsl:element name="metstdv">
					<!--xsl:value-of select="./metadataStandardVersion"/-->
					<xsl:text>FGDC-STD-001-1998</xsl:text>
				</xsl:element>
				<xsl:if test="./metadataConstraints/MD_LegalConstraints/accessConstraints">
					<xsl:element name="metac">
						<xsl:value-of select="./metadataConstraints/MD_LegalConstraints/accessConstraints"/>
					</xsl:element>
				</xsl:if>
				<xsl:if test="./metadataConstraints/MD_LegalConstraints/useConstraints">
					<xsl:element name="metuc">
						<xsl:value-of select="./metadataConstraints/MD_LegalConstraints/useConstraints"/>
					</xsl:element>
				</xsl:if>
				<xsl:if test="./metadataConstraints/MD_LegalConstraints/otherConstraints">
					<xsl:element name="metoc">
						<xsl:value-of select="./metadataConstraints/MD_LegalConstraints/otherConstraints"/>
					</xsl:element>
				</xsl:if>
				<xsl:if test="./metadataConstraints/MD_SecurityConstraints">
					<xsl:element name="metsi">
						<xsl:if test="./metadataConstraints/MD_SecurityConstraints/classificationSystem">
							<xsl:element name="metscs">
								<xsl:value-of select="./metadataConstraints/MD_SecurityConstraints/classificationSystem"/>
							</xsl:element>
						</xsl:if>
						<xsl:if test="./metadataConstraints/MD_SecurityConstraints/classification">
							<xsl:element name="metsc">
								<xsl:value-of select="./metadataConstraints/MD_SecurityConstraints/classification"/>
							</xsl:element>
						</xsl:if>
						<xsl:if test="./metadataConstraints/MD_SecurityConstraints/handlingDescription">
							<xsl:element name="metshd">
								<xsl:value-of select="./metadataConstraints/MD_SecurityConstraints/handlingDescription"/>
							</xsl:element>
						</xsl:if>
					</xsl:element>
				</xsl:if>
				<xsl:element name="metalang">
  				   <xsl:if test="./language">
				      <xsl:value-of select="./language"/>
				   </xsl:if>
				</xsl:element>
				
				<xsl:if test="./fileIdentifier">
					<xsl:element name="mdFileID">
						<xsl:value-of select="./fileIdentifier"/>
					</xsl:element>
				</xsl:if>
				
				<xsl:if test="./hierarchyLevel">
					<xsl:element name="mdHrLv">
						<xsl:value-of select="./hierarchyLevel"/>
					</xsl:element>
				</xsl:if>
			</xsl:element>
			<!-- REFERENCESYSTEMINFO, VALIDO SEGUN EL FGDCIAAA-->
			<xsl:for-each select="referenceSystemInfo">
				<xsl:apply-templates select="."/>
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	<!-- el problema es que IdentificationInfo: MD_Identification(ISO) es (1..n) mientras que idinfo(FGDC) es (0..1)       -->
	<!-- tal y como esta ahora, solo se trasladara el primer IdentificationInfo: MD_Identification si es que hay ms de uno -->
	<xsl:template match="identificationInfo/MD_DataIdentification">
		<xsl:element name="idinfo">
			<xsl:element name="citation">
				<xsl:apply-templates select="citation/CI_Citation"/>
			</xsl:element>
			<!-- ambos son obligatorios-->
			<xsl:element name="descript">
				<xsl:element name="abstract">
					<xsl:value-of select="./abstract"/>
				</xsl:element>
				<!-- purpose es obligatorio e Purpose es (0..1) -->
				<xsl:element name="purpose">
					<!-- si Purpose no aparece,el elemento purpose-->
					<!-- aparecera vacio-->
					<xsl:value-of select="./purpose"/>
				</xsl:element>
				<!-- ambos son (0..1) -->
				<xsl:if test="./supplementalInformation">
					<xsl:element name="supplinf">
						<xsl:value-of select="./supplementalInformation"/>
					</xsl:element>
				</xsl:if>
			</xsl:element>
			<!-- Esto esta bastante cambiado con respecto a lo que tenia Tere.  Habra que volver a revisarlo. -->
			<xsl:element name="timeperd">
				<xsl:choose>
					<xsl:when test="./extent">
						<xsl:apply-templates select="extent"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:element name="timeinfo">
							<xsl:element name="sngdate">
								<xsl:element name="caldate"/>
							</xsl:element>
						</xsl:element>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:element name="current"/>
			</xsl:element>
			<!--Elemento status-->
			<xsl:element name="status">
				<xsl:element name="progress">
					<!-- Por el momento, no hago ninguna comparacion, porque el valor en ISO en esta version para status es libre-->
					<xsl:value-of select="normalize-space(./status)"/>
				</xsl:element>
				<xsl:element name="update">
					<!-- Paso el valor tal cual.  Podría hacerse una comparacion para ver si es alguno de los aconsejados en fgdc pero
					como antes, si no lo era se pasaba el valor tal cual, no tiene sentido. -->
					<xsl:value-of select="normalize-space(./resourceMaintenance/MD_MaintenanceInformation/maintenanceAndUpdateFrequency)"/>
				</xsl:element>
			</xsl:element>
			<xsl:element name="spdom">
				<!-- bounding (FGDC) es (1..1) y GeographicBox:EX_GeographicBoundingBox es (0..n)-->
				<!-- solo aparecera el primero si hay varios GeographicBox:EX_GeographicBoundingBox -->
				<xsl:element name="bounding">
					<xsl:element name="westbc">
						<xsl:value-of select="./extent/*/geographicElement/*/westBoundLongitude"/>
					</xsl:element>
					<xsl:element name="eastbc">
						<xsl:value-of select="./extent/*/geographicElement/*/eastBoundLongitude"/>
					</xsl:element>
					<xsl:element name="northbc">
						<xsl:value-of select="./extent/*/geographicElement/*/northBoundLatitude"/>
					</xsl:element>
					<xsl:element name="southbc">
						<xsl:value-of select="./extent/*/geographicElement/*/southBoundLatitude"/>
					</xsl:element>
				</xsl:element>
				<!-- el elemento dsgpoly(FGDC) no se corresponde con ningun elemento de ISO-->
				<!-- spdom/scale (FGDC nuestro) es (0..1) y IdentificationInfo: MD_Identification/SpatialResolution:MD_Resolution es (0..n)-->
				<!-- solo se trasladara el primer SpatialResolution:MD_Resolution en caso de que haya mas de uno -->
				<xsl:if test="./spatialResolution/equivalentScale/MD_RepresentativeFraction">
					<xsl:element name="scale">
						<xsl:value-of select="./spatialResolution/equivalentScale/*/denominator"/>
					</xsl:element>
				</xsl:if>
				<!-- el elemento sparef no tiene equivalente en ISO -->
			</xsl:element>
			<xsl:element name="keywords">
				<xsl:element name="tpCat">
					<xsl:value-of select="./topicCategory"/>
				</xsl:element>
				<xsl:choose>
					<xsl:when test="./descriptiveKeywords/MD_Keywords/type = 'place' or ./descriptiveKeywords/MD_Keywords/keyword/type = 'theme' or ./descriptiveKeywords/MD_Keywords/keyword/type = 'stratum' or ./descriptiveKeywords/MD_Keywords/keyword/type = 'temporal' ">
						<xsl:apply-templates select="./descriptiveKeywords/MD_Keywords"/>
					</xsl:when>
					<xsl:otherwise>
						<!-- Algo tendremos que hacer..., porque keywords en FGDCIAAA es obligatoria... -->
						<xsl:element name="theme">
							<xsl:element name="themekt"/>
							<xsl:element name="themekey"/>
						</xsl:element>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:element>
			<!-- accconst (FGDC) es (1..1) y ResourceConstraints:MD_constraintss/LegMD_Constraints/acconst(ISO) es (0..n) -->
			<!-- si hay mas de una solo se traslada la primera-->
			<xsl:choose>
				<xsl:when test="./resourceConstraints/MD_LegalConstraints">
					<xsl:element name="accconst">
						<xsl:value-of select="./resourceConstraints/MD_LegalConstraints/accessConstraints"/>
					</xsl:element>
					<xsl:element name="useconst">
						<xsl:value-of select="./resourceConstraints/MD_LegalConstraints/useConstraints"/>
					</xsl:element>
					<xsl:if test="./resourceConstraints/MD_LegalConstraints/otherConstraints">
						<xsl:element name="othconst">
							<xsl:value-of select="./resourceConstraints/MD_LegalConstraints/otherConstraints"/>
						</xsl:element>
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<xsl:element name="accconst"/>
					<xsl:element name="useconst"/>
					<xsl:element name="othconst"/>
				</xsl:otherwise>
			</xsl:choose>
			<!-- ptcontac(FGDC) es (0..1) y idPoc(ISO) es (0..n) -->
			<!-- solo se traslada la primera ocurrencia    -->
			<xsl:if test="./pointOfContact/CI_ResponsibleParty">
				<xsl:element name="ptcontac">
					<xsl:apply-templates select="./pointOfContact/CI_ResponsibleParty"/>
				</xsl:element>
			</xsl:if>
			<!-- browse(FGDC) es (0..n) y GraphicOverview:MD_BrowseGraphic(ISO) es (0..n)-->
			<xsl:for-each select="./graphicOverview/MD_BrowseGraphic">
				<xsl:element name="browse">
					<!-- browse (FGDC) y  FileName(ISO) son (1..1)-->
					<xsl:element name="browsen">
						<xsl:value-of select="./fileName"/>
					</xsl:element>
					<!-- browsed (FGDC) es (1..1)y  FileDescription(ISO) es (0..1)-->
					<xsl:if test="contains(fileDescription,'%%%%%%%%%%%%%%%%%PREVIEW BASE64%%%%%%%%%%%%%%%%%
')">
						<xsl:element name="browsed">
							<xsl:value-of select="substring-before(fileDescription,'%%%%%%%%%%%%%%%%%PREVIEW BASE64%%%%%%%%%%%%%%%%%
')"/>
						</xsl:element>
						<xsl:element name="browseem">
							<xsl:value-of select="substring-after(fileDescription,'%%%%%%%%%%%%%%%%%PREVIEW BASE64%%%%%%%%%%%%%%%%%
')"/>
						</xsl:element>
					</xsl:if>
					<xsl:if test="not(contains(fileDescription,'%%%%%%%%%%%%%%%%%PREVIEW BASE64%%%%%%%%%%%%%%%%%
'))">
						<xsl:element name="browsed">
							<xsl:value-of select="fileDescription"/>
						</xsl:element>
					</xsl:if>
										<!-- browsed (FGDC) es (1..1)y  FileDescription(ISO) es (0..1)-->
					<xsl:element name="browset">
						<xsl:value-of select="fileType"/>
					</xsl:element>
					<!-- este elemento solo es valido segun el estandar FGDC-iAAA-->
					<!-- a si?  pues yo lo quito porque en el dtd FGDCIAAA que yo tengo no aparece y ademas el elemento bgImage no 
					tiene correspondiente en ISO  
					10-9-03. lo pongo pq en un ejemplo de Javi y David se ve que es necesario aunque NO pertenezca al estandar
					1-10-03 lo vuelvo a quitar, haciendo de esta una version 2 de la hoja que si lo lleva.-->
					<!--xsl:if test="./bgImage">
						<xsl:element name="browseem">
							<xsl:value-of select="./bgImage"/>
						</xsl:element>
					</xsl:if-->
				</xsl:element>
			</xsl:for-each>
			<!-- datacred(FGDC) es (0..1) y Credit(ISO) es (0..n)                             -->
			<!-- solo se trasladara la primera ocurrencia de Credit si es que hay mas de una  -->
			<xsl:if test="./credit">
				<xsl:element name="datacred">
					<xsl:value-of select="./credit"/>
				</xsl:element>
			</xsl:if>
			<!-- secinfo(FGDC) es (0..1) y ResourceConstraints:MD_constraints/SecMD_Constraints(ISO) es (0..n)-->
			<!-- solo se trasladara la primera ocurrencia de ResourceConstraints:MD_constraints/SecMD_Constraints si hay mas de una-->
			<xsl:if test="./resourceConstraints/MD_SecurityConstraints">
				<xsl:element name="secinfo">
					<!-- secsys(FGDC) es (1..1) y ClassificationSys(ISO) es (0..1)-->
					<xsl:element name="secsys">
						<xsl:value-of select="./resourceConstraints/MD_SecurityConstraints/classificationSystem"/>
					</xsl:element>
					<!-- secClassification(FGDC) es (1..1) y Classification(ISO) es (1..1)-->
					<xsl:element name="secclass">
						<xsl:value-of select="./resourceConstraints/MD_SecurityConstraints/classification"/>
					</xsl:element>
					<!-- sechandl(FGDC) es (1..1) y HandlingDescription(ISO) es (0..1)-->
					<xsl:element name="sechandl">
						<xsl:value-of select="./resourceConstraints/MD_SecurityConstraints/handlingDescription"/>
					</xsl:element>
				</xsl:element>
			</xsl:if>
			<!--- Existe tambien un metadataConstraints que habria que ver si se puede pasar a algun lado o no...  -->
			<!-- native(FGDC) es (0..1) y envirDec(ISO) es (0..1)-->
			<xsl:if test="./environmentDescription">
				<xsl:element name="native">
					<xsl:value-of select="./environmentDescription"/>
				</xsl:element>
			</xsl:if>
			<!-- Cross_Reference no tiene equivalente en ISO-->
			<!-- elemento IDLANG, propio del fgdcIAAA -->
			<xsl:if test="./language">
				<xsl:for-each select="./language">
					<xsl:element name="idlang">
						<xsl:element name="idlangkt">
							<xsl:text>ISO639</xsl:text>
						</xsl:element>
						<xsl:element name="idlangkey">
						  <xsl:value-of select="."/>
						</xsl:element>
					</xsl:element>
				</xsl:for-each>
			</xsl:if>
		</xsl:element>
		<!-- FIN DE idinfo -->
		<!-- Suponemos que habra como maximo dos SpatialRespresentationType y un solo IdentificationInfo: MD_Identification   -->
		<!-- puede trasladar a spdoinfo/direct o indpref)  -->
		<!--  NO ENTIENDO MUY BIEN PORQUE ESTA RESTRICCION ¿¿¿?????????????''-->
		<!-- El valor de SpatialRepresentationType puede llevarse o bien a spdoinfo/direct of spdoinfo/indpref.  Ambos puede -->
		<!-- decirse que son opcionales NO REPETIBLES.  Por tanto habra que vigilar que no se dupliquen.   -->
		<xsl:element name="spdoinfo">
			<xsl:element name="direct">
				<xsl:variable name="typesrt">
					<xsl:value-of select="./spatialRepresentationType"/>
				</xsl:variable>
				<xsl:choose>
					<xsl:when test="$typesrt='Malla'">
						<xsl:text>RASTER</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="./spatialRepresentationType"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:element>
			<!--xsl:choose>
				<xsl:when test="normalize-space(./spatialRepresentationType)='Vector'">
				</xsl:when>
			</xsl:choose-->
			<xsl:for-each select="../../spatialRepresentationInfo/MD_VectorSpatialRepresentation">
				<xsl:element name="ptvctinf">
					<xsl:element name="vpfterm">
						<xsl:element name="vpflevel">
					 		<xsl:value-of select="./topologyLevel"/>
						</xsl:element>
						<xsl:for-each select="./geometricObjects">
							<xsl:element name="vpfinfo">
								<xsl:element name="vpftype">
						 			<xsl:value-of select="./MD_GeometricObjects/geometricObjectType"/>
								</xsl:element>
								<xsl:element name="ptvctcnt">
						 			<xsl:value-of select="./MD_GeometricObjects/geometricObjectCount"/>
								</xsl:element>
							</xsl:element>
						</xsl:for-each>
					</xsl:element>
				</xsl:element>
			</xsl:for-each>
			<xsl:for-each select="../../spatialRepresentationInfo/MD_GridSpatialRepresentation">
				<xsl:element name="rastinfo">
					<xsl:element name="rasttype">
						<xsl:value-of select="./cellGeometry"/>
					</xsl:element>
					<xsl:for-each select="./axisDimensionProperties/MD_Dimension">
						<xsl:variable name="dim">
							<xsl:value-of select="./dimensionName"/>
						</xsl:variable>
						<xsl:if test="$dim='row'">
							<xsl:element name="rowcount">
								<xsl:value-of select="./dimensionSize"/>
							</xsl:element>
						</xsl:if>
						<xsl:if test="$dim='column'">
							<xsl:element name="colcount">
								<xsl:value-of select="./dimensionSize"/>
							</xsl:element>
						</xsl:if>
						<xsl:if test="$dim='vertical'">
							<xsl:element name="vrtcount">
								<xsl:value-of select="./dimensionSize"/>
							</xsl:element>
						</xsl:if>
					</xsl:for-each>
				</xsl:element>
			</xsl:for-each>
		</xsl:element>

		<!--xsl:variable name="numSRT">
			<xsl:value-of select="count(./spatialRepresentationType)"/>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$numSRT=0">
							</xsl:when>
			<xsl:when test="$numSRT=1">
				<xsl:element name="spdoinfo">
					<xsl:choose>
						<xsl:when test="normalize-space(./spatialRepresentationType)='vector' or
								normalize-space(./spatialRepresentationType)='grid' or
								normalize-space(./spatialRepresentationType)='tin' or
								normalize-space(./spatialRepresentationType)='VECTOR' or
								normalize-space(./spatialRepresentationType)='GRID' or
								normalize-space(./spatialRepresentationType)='TIN'">
							<xsl:element name="direct">
								<xsl:value-of select="./spatialRepresentationType"/>
							</xsl:element>
						</xsl:when>
						<xsl:otherwise>
														<xsl:element name="indspref">
								<xsl:value-of select="./spatialRepresentationType"/>
							</xsl:element>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:element>
			</xsl:when>
			<xsl:when test="$numSRT=2">
				<xsl:element name="spdoinfo">
					<xsl:variable name="SRT1">
						<xsl:value-of select="spatialRepresentationType[1]"/>
					</xsl:variable>
					<xsl:variable name="SRT2">
						<xsl:value-of select="spatialRepresentationType[2]"/>
					</xsl:variable>
					<xsl:choose>
						<xsl:when test="$SRT1='vector' or $SRT1='grid' or $SRT1='tin' or $SRT1='VECTOR' or $SRT1='GRID' or $SRT1='TIN'">
							<xsl:element name="indspref">
								<xsl:value-of select="$SRT2"/>
							</xsl:element>
							<xsl:element name="direct">
								<xsl:value-of select="$SRT1"/>
							</xsl:element>
						</xsl:when>
						<xsl:when test="$SRT2='vector' or $SRT2='grid' or $SRT2='tin' or $SRT2='VECTOR' or $SRT2='GRID' or $SRT2='TIN'">
							<xsl:element name="indspref">
								<xsl:value-of select="$SRT1"/>
							</xsl:element>
							<xsl:element name="direct">
								<xsl:value-of select="$SRT2"/>
							</xsl:element>
						</xsl:when>
						<xsl:otherwise>
														<xsl:element name="indspref">
								<xsl:value-of select="$SRT2"/>
							</xsl:element>
							<xsl:element name="direct">
								<xsl:value-of select="$SRT1"/>
							</xsl:element>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:element>
			</xsl:when>
			<xsl:otherwise>
				
				<xsl:element name="spdoinfo">
					<xsl:element name="indspref">
						<xsl:value-of select="spatialRepresentationType[1]"/>
					</xsl:element>
					<xsl:element name="direct">
						<xsl:value-of select="spatialRepresentationType[2]"/>
					</xsl:element>
				</xsl:element>
			</xsl:otherwise>
		</xsl:choose-->
	</xsl:template>
	<!-- PLANTILLA CITATION-->
	<xsl:template match="citation/CI_Citation | authority/CI_Citation | sourceCitation/CI_Citation">
		<xsl:element name="citeinfo">
			<xsl:choose>
				<xsl:when test="count(descendant::citedResponsibleParty/CI_ResponsibleParty[normalize-space(role)='originator'])>0">
					<!-- si hay al menos una aparicion de citedResponsibleParty con role originator..-->
					<xsl:for-each select="./citedResponsibleParty/CI_ResponsibleParty">
						<xsl:if test="normalize-space(./role)='originator'">
							<!-- por cada citedResponsibleParty con role originator se crea el elemento origin. -->
							<!-- el nombre del productor se toma de OrganisationName por convención,          -->
							<!-- de la misma forma que en la hoja FGDC->ISO el contenido del elemento  -->
							<!-- origin se mapeaba en OrganisationName                                        -->
							<xsl:element name="origin">
								<xsl:value-of select="./organisationName"/>
							</xsl:element>
						</xsl:if>
					</xsl:for-each>
				</xsl:when>
				<xsl:otherwise>
					<!-- el elemento origin es obligatorio.  Se crea vacio.-->
					<xsl:element name="origin"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:element name="pubdate">
				<xsl:value-of select="./date/*/date"/>
			</xsl:element>
			<!-- pubtime no tiene mapeo posible -->
			<xsl:element name="title">
				<xsl:value-of select="./title"/>
			</xsl:element>
			<xsl:if test="edition">
				<xsl:element name="edition">
					<xsl:value-of select="./edition"/>
				</xsl:element>
			</xsl:if>
			<xsl:if test="presentationForm">
				<xsl:element name="geoform">
					<xsl:value-of select="./presentationForm"/>
				</xsl:element>
			</xsl:if>
			<xsl:if test="series/CI_Series">
				<xsl:element name="serinfo">
					<xsl:element name="sername">
						<!-- Name es opcional, sername obligatorio-->
						<!-- si Name no aparece, sername aparecera vacio-->
						<xsl:value-of select="./series/*/name"/>
					</xsl:element>
					<xsl:element name="issue">
						<!-- IssueIdentification es opcional, issue obligatorio-->
						<!-- si IssueIdentification no aparece, issue aparecera vacio-->
						<xsl:value-of select="./series/*/issueIdentification"/>
					</xsl:element>
				</xsl:element>
			</xsl:if>
			<xsl:if test="count(descendant::citedResponsibleParty/CI_ResponsibleParty[normalize-space(role)='publisher'])>0">
				<!-- pubinfo solo puede aparecer una vez, de modo que se mapea con la primera  -->
				<!-- aparicion de CitedResponsibleParty:CI_ResponsibleParty con Role publisher, si hay mas de una           -->
				<xsl:for-each select="descendant::citedResponsibleParty/CI_ResponsibleParty[normalize-space(role)='publisher']">
					<xsl:if test="position()=1">
						<xsl:element name="pubinfo">
							<xsl:element name="pubplace">
								<xsl:value-of select="./contactInfo/*/address/*/city"/>
							</xsl:element>
							<xsl:element name="publish">
								<xsl:value-of select="./organisationName"/>
							</xsl:element>
						</xsl:element>
					</xsl:if>
				</xsl:for-each>
			</xsl:if>
			<xsl:if test="otherCitationDetails">
				<xsl:element name="othercit">
					<xsl:value-of select="./otherCitationDetails"/>
				</xsl:element>
			</xsl:if>
			<xsl:for-each select="./citedResponsibleParty/CI_ResponsibleParty">
				<xsl:if test="./contactInfo/*/onlineResource/CI_OnlineResource and normalize-space(./role)='originator' ">
					<!-- por cada citedResponsibleParty con Role originator que contenga cntOnLineRes-->
					<!-- se crea un elemento onlink-->
					<xsl:element name="onlink">
						<xsl:value-of select="./contactInfo/*/onlineResource/*/linkage"/>
					</xsl:element>
				</xsl:if>
			</xsl:for-each>
			<!-- Larger_work_Citation no tiene traduccion -->
			<!-- Añado elementos propios del fgdc IAAA-->
			<xsl:if test="identifier/*/code">
				<xsl:element name="citId">
					<xsl:value-of select="./identifier/*/code"/>
				</xsl:element>
			</xsl:if>
			<xsl:if test="ISBN">
				<xsl:element name="isbn">
					<xsl:value-of select="./ISBN"/>
				</xsl:element>
			</xsl:if>
			<xsl:if test="ISSN">
				<xsl:element name="issn">
					<xsl:value-of select="./ISSN"/>
				</xsl:element>
			</xsl:if>
		</xsl:element>
	</xsl:template>
	<!--  Fin plantilla CITATION-->
	<!-- Plantilla KEYWORDS-->
	<xsl:template match="descriptiveKeywords/MD_Keywords">
		<xsl:choose>
			<xsl:when test="normalize-space(./type)='theme'">
				<!-- Se realiza la suposición de q siempre va a haber al menos -->
				<!-- un elemento TypeCd con el atributo "theme", ya que el   -->
				<!-- elemento theme es obligatorio en FGDC                     -->
				<!-- En FGDC IAAA, tpCat es obligatorio aqui, que se mapea con el elemento topicCategory de ISO. -->
				<!-- Por tanto, en este lugar voy a meter todos los topicCategory separados por una coma-->
				<xsl:element name="theme">
					<xsl:element name="themekt">
						<xsl:value-of select="thesaurusName/*/title"/>
					</xsl:element>
					<xsl:for-each select="./keyword">
						<xsl:element name="themekey">
							<xsl:value-of select="."/>
						</xsl:element>
					</xsl:for-each>
				</xsl:element>
			</xsl:when>
			<xsl:when test="normalize-space(./type)='place'">
				<xsl:element name="place">
					<xsl:element name="placekt">
						<xsl:value-of select="thesaurusName/*/title"/>
					</xsl:element>
					<xsl:for-each select="./keyword">
						<xsl:element name="placekey">
							<xsl:value-of select="."/>
						</xsl:element>
					</xsl:for-each>
				</xsl:element>
			</xsl:when>
			<xsl:when test="normalize-space(./type)='stratum'">
				<xsl:element name="stratum">
					<xsl:element name="stratkt">
						<xsl:value-of select="thesaurusName/*/title"/>
					</xsl:element>
					<xsl:for-each select="./keyword">
						<xsl:element name="stratkey">
							<xsl:value-of select="."/>
						</xsl:element>
					</xsl:for-each>
				</xsl:element>
			</xsl:when>
			<xsl:when test="normalize-space(./type)='temporal'">
				<xsl:element name="temporal">
					<xsl:element name="tempkt">
						<xsl:value-of select="thesaurusName/*/title"/>
					</xsl:element>
					<xsl:for-each select="./keyword">
						<xsl:element name="tempkey">
							<xsl:value-of select="."/>
						</xsl:element>
					</xsl:for-each>
				</xsl:element>
			</xsl:when>
			<xsl:otherwise>
				<!--no mapeamos el resto de posibles atributos -->
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!--Fin de la Plantilla KEYWORDS-->
	<xsl:template match="pointOfContact/CI_ResponsibleParty | distributorContact/CI_ResponsibleParty | contact/CI_ResponsibleParty | processor/CI_ResponsibleParty">
		<xsl:element name="cntinfo">
			<xsl:choose>
				<!-- convencion:siempre que hay un OrganisationName, se genera cntorgp         -->
				<!-- 	        solo si hay un solo IndividualName se genera cntperp         -->
				<!-- si existe OrganisationName...-->
				<xsl:when test="organisationName">
					<xsl:element name="cntorgp">
						<xsl:element name="cntorg">
							<xsl:value-of select="./organisationName"/>
						</xsl:element>
						<!-- si existe ademas IndividualName...-->
						<xsl:if test="individualName">
							<xsl:element name="cntper">
								<xsl:value-of select="./individualName"/>
							</xsl:element>
						</xsl:if>
					</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<!-- si solo existe IndividualName...-->
					<xsl:choose>
						<xsl:when test="individualName">
							<xsl:element name="cntperp">
								<xsl:element name="cntper">
									<xsl:value-of select="./individualName"/>
								</xsl:element>
							</xsl:element>
						</xsl:when>
						<xsl:otherwise>
							<!-- si no existen ninguno de los dos..-->
							<xsl:element name="cntorgp">
								<xsl:element name="cntorg"/>
							</xsl:element>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:if test="positionName">
				<xsl:element name="cntpos">
					<xsl:value-of select="./positionName"/>
				</xsl:element>
			</xsl:if>
			<!--  cntaddr(FGDC) es (1..n) y ContactInfo:CI_Contact/Address:CI_Address es (0..1) -->
			<xsl:choose>
				<xsl:when test="./contactInfo/*/address/CI_Address">
					<xsl:apply-templates select="./contactInfo/*/address/CI_Address"/>
				</xsl:when>
				<xsl:otherwise>
					<!-- se crea el elemento vacio porque es obligatorio-->
					<xsl:element name="cntaddr">
						<xsl:element name="addrtype"/>
						<xsl:element name="city"/>
						<xsl:element name="state"/>
						<xsl:element name="postal"/>
					</xsl:element>
				</xsl:otherwise>
			</xsl:choose>
			<!-- cntvoice(FGDC) es (1..n) y ContactInfo:CI_Contact/Phone:CI_Telephone/Voice es (0..n)-->
			<xsl:choose>
				<xsl:when test="./contactInfo/*/phone/*/voice">
					<xsl:for-each select="./contactInfo/*/phone/*/voice">
						<xsl:element name="cntvoice">
							<xsl:value-of select="."/>
						</xsl:element>
					</xsl:for-each>
				</xsl:when>
				<xsl:otherwise>
					<xsl:element name="cntvoice"/>
				</xsl:otherwise>
			</xsl:choose>
			<!-- cntfax(FGDC) es (0..n) y ContactInfo:CI_Contact/Phone:CI_Telephone/Facsimile es (0..n)-->
			<xsl:for-each select="./contactInfo/*/phone/*/facsimile">
				<xsl:element name="cntfax">
					<xsl:value-of select="."/>
				</xsl:element>
			</xsl:for-each>
			<!-- cntemail(FGDC) es (0..n) y ContactInfo:CI_Contact/Address:CI_Address/ElectronicMailAddress es (0..n)-->
			<xsl:for-each select="./contactInfo/*/address/*/electronicMailAddress">
				<xsl:element name="cntemail">
					<xsl:value-of select="."/>
				</xsl:element>
			</xsl:for-each>
			<!-- hours(FGDC) es (0..1) y ContactInfo:CI_Contact/HoursOfService es (0..1)-->
			<xsl:if test="./contactInfo/*/hoursOfService">
				<xsl:element name="hours">
					<xsl:value-of select="./contactInfo/*/hoursOfService"/>
				</xsl:element>
			</xsl:if>
			<!-- cntinst(FGDC) es (0..1) y ContactInfo:CI_Contact/ContactInstructions es (0..1)-->
			<xsl:if test="./contactInfo/*/contactInstructions">
				<xsl:element name="cntinst">
					<xsl:value-of select="./contactInfo/*/contactInstructions"/>
				</xsl:element>
			</xsl:if>
			<!-- Role no tiene equivalente en FGDC-->
		</xsl:element>
	</xsl:template>
	<xsl:template match="address/CI_Address">
		<xsl:element name="cntaddr">
			<!-- este elemento no tiene equivalente en ISO-->
			<xsl:element name="addrtype"/>
			<!-- ambos son (0..n)-->
			<xsl:for-each select="./deliveryPoint">
				<xsl:element name="address">
					<xsl:value-of select="."/>
				</xsl:element>
			</xsl:for-each>
			<!--City(FGDC) es (1..1) y City(ISO) es (0..1) -->
			<xsl:element name="city">
				<xsl:value-of select="./city"/>
			</xsl:element>
			<!--state(FGDC) es (1.1) y AdministrativeArea(ISO) es (0..1) -->
			<xsl:element name="state">
				<xsl:value-of select="./administrativeArea"/>
			</xsl:element>
			<!--postal(FGDC) es (1.1) y PostalCode(ISO) es (0..1) -->
			<xsl:element name="postal">
				<xsl:value-of select="./postalCode"/>
			</xsl:element>
			<!-- Country(FGDC) es (0..1) y Country(iSO) es (0..1)-->
			<xsl:if test="./country">
				<xsl:element name="country">
					<xsl:value-of select="./country"/>
				</xsl:element>
			</xsl:if>
		</xsl:element>
	</xsl:template>
	<xsl:template match="MD_Distributor">
		<!-- Estoy suponiendo que hay la relacion entre los elementos MD_Format, MD_DigitalTransferOptions-->
		<!-- , MD_StandarOrderProcess es 1-1-1 en cada MD_Distributor, y que por tanto se corresponden sus contenidos-->
		<!-- segun su posicion como hijos de MD_Didtributor -->
		<xsl:element name="distinfo">
			<xsl:element name="distrib">
				<xsl:apply-templates select="./distributorContact/CI_ResponsibleParty"/>
			</xsl:element>
			<!-- resdesc no tiene equivalente en ISO-->
			<!-- distliab es obligatorio pero no tiene equivalente en ISO-->
			<xsl:element name="distliab"/>
			<!-- antes de empezar a mapear el elemento stdorder hay que comprobar -->
			<!-- que la relacion entre los elementos MD_Format, MD_DigitalTransferOption y MD_StandardOrderProcess-->
			<!-- sea 1-1-1 en cada elemento Distributor:MD_Distributor-->
			<xsl:variable name="numDistorFormat">
				<xsl:value-of select="count(./distributorFormat/MD_Format)"/>
			</xsl:variable>
			<xsl:variable name="numDistorTran">
				<xsl:value-of select="count(./distributorTransferOptions/MD_DigitalTransferOptions)"/>
			</xsl:variable>
			<xsl:choose>
				<!-- si todos los elementos Distributor:MD_Distributor cumplen las condiciones...-->
				<xsl:when test="$numDistorFormat = $numDistorTran">
					<xsl:element name="stdorder">
						<xsl:for-each select="./distributorFormat/MD_Format">
							<!-- obtengo la posicion del actual distorOrdPrc -->
							<xsl:variable name="pos">
								<xsl:value-of select="position()"/>
							</xsl:variable>
							<xsl:element name="digform">
								<xsl:element name="digtinfo">
									<!-- ambos son (1..1) -->
									<xsl:element name="formname">
										<xsl:value-of select="./name"/>
									</xsl:element>
									<!-- formvern(FGDC) es (0..1) y formatVer(ISO) es (1..1)-->
									<xsl:element name="formvern">
										<xsl:value-of select="./version"/>
									</xsl:element>
									<!-- formspec(FGDC) es (0..1) y Specification(ISO) es (0..1)-->
									<xsl:if test="specification">
										<xsl:element name="formspec">
											<xsl:value-of select="specification"/>
										</xsl:element>
									</xsl:if>
									<!-- se toma UnitsOfDistribution del elemento distroTran -->
									<!-- con la misma posicion que la de DistributorFormat: MD_Format-->
									<xsl:for-each select="../../distributorTransferOptions/MD_DigitalTransferOptions">
										<xsl:if test="position()=$pos and ./unitsOfDistribution">
											<xsl:element name="formcont">
												<xsl:value-of select="./unitsOfDistribution"/>
											</xsl:element>
										</xsl:if>
									</xsl:for-each>
									<!-- filedec(FGDC) es (0..1) y FileDecompresionTechnique(ISO) es (0..1)-->
									<xsl:if test="fileDecompresionTechnique">
										<xsl:element name="filedec">
											<xsl:value-of select="fileDecompresionTechnique"/>
										</xsl:element>
									</xsl:if>
									<!-- se toma TransferSize del elemento distroTran -->
									<!-- con la misma posicion que la de DistributorFormat: MD_Format-->
									<xsl:for-each select="../../distributorTransferOptions/MD_DigitalTransferOptions">
										<xsl:if test="position()=$pos and ./TransferSize">
											<xsl:element name="transize">
												<xsl:value-of select="./TransferSize"/>
											</xsl:element>
										</xsl:if>
									</xsl:for-each>
								</xsl:element>
								<!-- segun la suposicion comentada arriba, habra 1 DistributorTransferOptions:MD_DigitalTransferOptions por cada-->
								<!-- distorOrdPrc y se corresponderan segun su posicion como hijos de Distributor:MD_Distributor-->
								<xsl:for-each select="../../distributorTransferOptions/MD_DigitalTransferOptions">
									<xsl:if test="position()=$pos">
										<xsl:element name="digtopt">
											<xsl:if test="./online/CI_OnlineResource">
												<xsl:element name="onlinopt">
													<xsl:for-each select="./online/CI_OnlineResource">
														<xsl:element name="computer">
															<xsl:element name="networka">
																<xsl:element name="networkr">
																	<xsl:value-of select="./linkage"/>
																</xsl:element>
															</xsl:element>
														</xsl:element>
													</xsl:for-each>
												</xsl:element>
											</xsl:if>
											<xsl:if test="./offLine">
												<xsl:element name="offoptn">
													<xsl:element name="offmedia">
														<xsl:value-of select="./offLine/*/name"/>
													</xsl:element>
													<xsl:if test="./offLine/*/density">
														<xsl:element name="reccap">
															<xsl:for-each select="./offLine/*/density">
																<xsl:element name="recden">
																	<xsl:value-of select="."/>
																</xsl:element>
															</xsl:for-each>
															<xsl:element name="recdenu">
																<xsl:value-of select="./offLine/*/densityUnits"/>
															</xsl:element>
														</xsl:element>
													</xsl:if>
													<xsl:choose>
														<xsl:when test="./offLine/*/mediumFormat">
															<xsl:for-each select="./offLine/*/mediumFormat">
																<xsl:element name="recfmt">
																	<xsl:value-of select="."/>
																</xsl:element>
															</xsl:for-each>
														</xsl:when>
														<xsl:otherwise>
															<xsl:element name="recfmt"/>
														</xsl:otherwise>
													</xsl:choose>
													<xsl:if test="./offLine/*/mediumNote">
														<xsl:element name="compat">
															<xsl:value-of select="./offLine/*/mediumNote"/>
														</xsl:element>
													</xsl:if>
												</xsl:element>
											</xsl:if>
										</xsl:element>
									</xsl:if>
								</xsl:for-each>
							</xsl:element>
						</xsl:for-each>
						<xsl:for-each select="./distributionOrderProcess/MD_StandardOrderProcess">
							<xsl:if test="position()=1">
								<!-- fees(FGDC) es (1..1) y Fees(ISO) es (0..1)-->
								<xsl:element name="fees">
									<xsl:value-of select="./fees/monetaryAmt"/>
								</xsl:element>
								<!-- ordering(FGDC) es (0..1) y OrderingInstructions(ISO) es (0..1)-->
								<xsl:if test="./orderingInstructions">
									<xsl:element name="ordering">
										<xsl:value-of select="./orderingInstructions"/>
									</xsl:element>
								</xsl:if>
								<!-- turnarnd(FGDC) es (0..1) y Turnaround(ISO) es (0..1)-->
								<xsl:if test="./turnaround">
									<xsl:element name="turnarnd">
										<xsl:value-of select="./turnaround"/>
									</xsl:element>
								</xsl:if>
							</xsl:if>
						</xsl:for-each>
					</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<!--  El formato requerido para el elemento MD_Distributor en el archivo xml origen exige que -->
					<!--  dicho elemento tenga el mismo numero de hijos de tipo  MD_Format, de tipo MD_DigitalTransferOption -->
					<!--  y de tipo MD_StandarPrderProcess.Esta condición no se cumple en el fichero xml ISO origen, por lo -->
					<!--  tanto en el fichero destino no se ha creado ningun elemento stdorder. -->
				</xsl:otherwise>
			</xsl:choose>
			<!-- PlannedAvailableDateTime es (0..n) y availabl es (0..1) -->
			<!-- por tanto solo trasladamos la primera ocurrencia -->
			<xsl:if test="./distributionOrderProcess/MD_StandardOrderProcess/plannedAvailableDateTime">
				<xsl:element name="availabl">
					<xsl:element name="timeinfo">
						<xsl:element name="sngdate">
							<xsl:element name="caldate">
								<xsl:value-of select="./distributionOrderProcess/MD_StandardOrderProcess/plannedAvailableDateTime"/>
							</xsl:element>
						</xsl:element>
					</xsl:element>
				</xsl:element>
			</xsl:if>
		</xsl:element>
	</xsl:template>
	<xsl:template match="referenceSystemIdentifier/RS_Identifier | projection/RS_Identifier | ellipsoid/RS_Identifier | datum/RS_Identifier">
		<xsl:if test="./authority/CI_Citation">
			<xsl:element name="identAuth">
				<xsl:apply-templates select="./authority/CI_Citation"/>
			</xsl:element>
		</xsl:if>
		<xsl:element name="identCode">
			<xsl:value-of select="code"/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="referenceSystemInfo">
		<xsl:element name="refSysInfo">
			<xsl:for-each select="MD_ReferenceSystem/referenceSystemIdentifier/RS_Identifier">
				<!--xsl:element name="refSysInfo"-->
				<xsl:element name="refSystem">
					<xsl:element name="refSysId">
						<xsl:apply-templates select="."/>
					</xsl:element>
				</xsl:element>
				<!--/xsl:element-->
			</xsl:for-each>
			<xsl:for-each select="MD_CRS">
				<!--xsl:element name="refSysInfo"-->
				<xsl:element name="mdCoRefSys">
					<xsl:if test="referenceSystemIdentifier/RS_Identifier">
						<xsl:element name="refSysId">
							<xsl:apply-templates select="referenceSystemIdentifier/RS_Identifier"/>
						</xsl:element>
					</xsl:if>
					<xsl:if test="projection">
						<xsl:element name="projection">
							<xsl:apply-templates select="projection/RS_Identifier"/>
						</xsl:element>
					</xsl:if>
					<xsl:if test="ellipsoid">
						<xsl:element name="ellipsoid">
							<xsl:apply-templates select="ellipsoid/RS_Identifier"/>
						</xsl:element>
					</xsl:if>
					<xsl:if test="datum">
						<xsl:element name="datum">
							<xsl:apply-templates select="datum/RS_Identifier"/>
						</xsl:element>
					</xsl:if>
					<xsl:if test="projectionParameters/MD_ProjectionParameters">
						<xsl:element name="projParas">
							<xsl:if test="projectionParameters/MD_ProjectionParameters/zone">
								<xsl:element name="zone">
									<xsl:value-of select="projectionParameters/MD_ProjectionParameters/zone"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="projectionParameters/MD_ProjectionParameters/standardParallel">
								<!--  Este elemento va de 0 a 2-->
								<xsl:for-each select="projectionParameters/MD_ProjectionParameters/standardParallel">
									<xsl:element name="stanPara">
										<xsl:value-of select="."/>
									</xsl:element>
								</xsl:for-each>
							</xsl:if>
							<xsl:if test="projectionParameters/MD_ProjectionParameters/longitudeOfCentralMeridian">
								<xsl:element name="longCntMer">
									<xsl:value-of select="projectionParameters/MD_ProjectionParameters/longitudeOfCentralMeridian"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="projectionParameters/MD_ProjectionParameters/latitudeOfProjectionOrigin">
								<xsl:element name="latProjOri">
									<xsl:value-of select="projectionParameters/MD_ProjectionParameters/latitudeOfProjectionOrigin"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="projectionParameters/MD_ProjectionParameters/falseEasting">
								<xsl:element name="falEastng">
									<xsl:value-of select="projectionParameters/MD_ProjectionParameters/falseEasting"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="projectionParameters/MD_ProjectionParameters/falseNorthing">
								<xsl:element name="falNorthng">
									<xsl:value-of select="projectionParameters/MD_ProjectionParameters/falseNorthing"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="projectionParameters/MD_ProjectionParameters/falseEastingNorthingUnits">
								<xsl:element name="falENUnits">
									<xsl:value-of select="projectionParameters/MD_ProjectionParameters/falseEastingNorthingUnits"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="projectionParameters/MD_ProjectionParameters/scaleFactorAtEquator">
								<xsl:element name="sclFacEqu">
									<xsl:value-of select="projectionParameters/MD_ProjectionParameters/scaleFactorAtEquator"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="projectionParameters/MD_ProjectionParameters/heightOfProspectivePointsAboveSurface">
								<xsl:element name="hgtProsPt">
									<xsl:value-of select="projectionParameters/MD_ProjectionParameters/heightOfProspectivePointsAboveSurface"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="projectionParameters/MD_ProjectionParameters/longitudeOfProjectionCenter">
								<xsl:element name="longProjCnt">
									<xsl:value-of select="projectionParameters/MD_ProjectionParameters/longitudeOfProjectionCenter"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="projectionParameters/MD_ProjectionParameters/latitudeOfProjectionCenter">
								<xsl:element name="latProjCnt">
									<xsl:value-of select="projectionParameters/MD_ProjectionParameters/latitudeOfProjectionCenter"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="projectionParameters/MD_ProjectionParameters/scaleFactorAtCenterLine">
								<xsl:element name="sclFacCnt">
									<xsl:value-of select="projectionParameters/MD_ProjectionParameters/scaleFactorAtCenterLine"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="projectionParameters/MD_ProjectionParameters/straightVerticalLongitudeFromPole">
								<xsl:element name="stVrLongPl">
									<xsl:value-of select="projectionParameters/MD_ProjectionParameters/straightVerticalLongitudeFromPole"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="projectionParameters/MD_ProjectionParameters/scaleFactorAtProjectionOrigin">
								<xsl:element name="sclFacPrOr">
									<xsl:value-of select="projectionParameters/MD_ProjectionParameters/scaleFactorAtProjectionOrigin"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="projectionParameters/MD_ProjectionParameters/obliqueLineAzimuthParameter/MD_ObliqueLineAzimuth">
								<xsl:element name="ObLnAziPars">
									<xsl:element name="AziAngle">
										<xsl:value-of select="projectionParameters/MD_ProjectionParameters/obliqueLineAzimuthParameter/MD_ObliqueLineAzimuth/azimuthAngle"/>
									</xsl:element>
									<xsl:element name="AziPtLong">
										<xsl:value-of select="projectionParameters/MD_ProjectionParameters/obliqueLineAzimuthParameter/MD_ObliqueLineAzimuth/azimuthMeasurePointLongitude"/>
									</xsl:element>
								</xsl:element>
							</xsl:if>
							<xsl:if test="projectionParameters/MD_ProjectionParameters/obliqueLinePointParameter/MD_ObliqueLinePoint">
								<xsl:for-each select="projectionParameters/MD_ProjectionParameters/obliqueLinePointParameter/MD_ObliqueLinePoint">
									<xsl:element name="ObLnPtPars">
										<xsl:element name="ObLineLat">
											<xsl:value-of select="obliqueLineLatitude"/>
										</xsl:element>
										<xsl:element name="ObLineLong">
											<xsl:value-of select="obliqueLineLongitude"/>
										</xsl:element>
									</xsl:element>
								</xsl:for-each>
							</xsl:if>
						</xsl:element>
					</xsl:if>
					<xsl:if test="ellipsoidParameters/MD_EllipsoidParameters">
						<xsl:element name="ellParas">
							<xsl:if test="ellipsoidParameters/MD_EllipsoidParameters/semiMajorAxis">
								<xsl:element name="semiMajAx">
									<xsl:value-of select="ellipsoidParameters/MD_EllipsoidParameters/semiMajorAxis"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="ellipsoidParameters/MD_EllipsoidParameters/axisUnits">
								<xsl:element name="axisUnits">
									<xsl:value-of select="ellipsoidParameters/MD_EllipsoidParameters/axisUnits"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="ellipsoidParameters/MD_EllipsoidParameters/denominatorOfFlatteringRatio">
								<xsl:element name="denFlaRat">
									<xsl:value-of select="ellipsoidParameters/MD_EllipsoidParameters/denominatorOfFlatteringRatio"/>
								</xsl:element>
							</xsl:if>
						</xsl:element>
					</xsl:if>
				</xsl:element>
				<!--/xsl:element-->
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	<!-- REVISARRRR??????????????? -->
	<xsl:template match="extent/EX_Extent | sourceExtent/EX_Extent">
		<!--xsl:element name="timeinfo"-->
			<xsl:choose>
				<!--xsl:when test="./temporalElement/EX_TemporalExtent/extent/instant">
					<xsl:element name="sngdate">
						<xsl:element name="caldate">
							<xsl:value-of select="./temporalElement/EX_TemporalExtent/extent/instant"/>
						</xsl:element>
					</xsl:element>
				</xsl:when>
				<xsl:when test="./temporalElement/EX_TemporalExtent/extent/beginRange">
					<xsl:element name="sngdate">
						<xsl:element name="caldate">
							<xsl:value-of select="./temporalElement/EX_TemporalExtent/extent/beginRange/begin"/>
						</xsl:element>
						<xsl:element name="time">
							<xsl:value-of select="./temporalElement/EX_TemporalExtent/extent/beginRange/duration"/>
						</xsl:element>
					</xsl:element>
				</xsl:when-->
				<xsl:when test="./temporalElement/EX_TemporalExtent/beginEnd">
					<xsl:element name="timeinfo">
					<xsl:element name="rngdates">
						<xsl:element name="begdate">
							<xsl:value-of select="./temporalElement/EX_TemporalExtent/beginEnd/begin"/>
						</xsl:element>
						<xsl:element name="enddate">
							<xsl:value-of select="./temporalElement/EX_TemporalExtent/beginEnd/end"/>
						</xsl:element>
					</xsl:element>
					</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<!--xsl:element name="sngdate">
						<xsl:element name="caldate"/>
					</xsl:element-->
				</xsl:otherwise>
			</xsl:choose>
		<!--/xsl:element-->
	</xsl:template>
	<xsl:template match="dataQualityInfo">
		<!-- Scope, lineage , report -->
		<!-- SCOPE no tiene emparejamientos -->
		<!--xsl:if test="normalize-space(./DQ_DataQuality/report/DQ_ThematicAccuracy/@xsi:type)='DQ_QuantitativeAttributeAccuracy'"-->
			<xsl:element name="attracc">
				<xsl:element name="attraccr">
					<!-- esto si el tipo de thematicAccuracy es el adecuado -->
					<xsl:value-of select="./DQ_DataQuality/report/DQ_QuantitativeAttributeAccuracy/evaluationMethodDescription"/>
				</xsl:element>
				<xsl:element name="qattracc">
					<xsl:element name="attraccv">
						<!--Este se corresponde con una clase completa, value, que tiene varios elementos obligatorios. -->
						<!--Por el momento lo emparejo con attribute name... y ya veremos... -->
						<!--xsl:value-of select="./DQ_DataQuality/report/DQ_ThematicAccuracy/result/_DQ_Result/value/recordValue/attributes/AttributeName"/-->
						<xsl:value-of select="./DQ_DataQuality/report/DQ_QuantitativeAttributeAccuracy/result/DQ_QuantitativeResult/value/recordValue/attributes/AttributeName"/>
					</xsl:element>
					<xsl:element name="attracce">
						<xsl:value-of select="./DQ_DataQuality/report/DQ_QuantitativeAttributeAccuracy/measureDescription"/>
					</xsl:element>
				</xsl:element>
			</xsl:element>
		<!--/xsl:if-->
		<xsl:element name="logic">
			<!--elemento obligatorio en FGDC, asi que no compruebo si esta o no en ISO -->
			<!--xsl:value-of select="./DQ_DataQuality/report/DQ_LogicalConsistency/evaluationMethodDescription"/-->
			<xsl:value-of select="./DQ_DataQuality/report/DQ_ConceptualConsistency/evaluationMethodDescription"/>
		</xsl:element>
		<xsl:element name="complete">
			<!--xsl:value-of select="./DQ_DataQuality/report/DQ_Completeness/evaluationMethodDescription"/-->
			<xsl:value-of select="./DQ_DataQuality/report/DQ_CompletenessCommision/evaluationMethodDescription"/>
		</xsl:element>
		<!--xsl:if test="normalize-space(./DQ_DataQuality/report/DQ_PositionalAccuracy/@xsi:type) = 'DQ_AbsoluteExternalPositionalAccuracy'"-->
			<xsl:element name="posacc">
				<!--Comprobacion de que nameOfMesaure es horizontal -->
				<xsl:if test="./DQ_DataQuality/report/DQ_AbsoluteExternalPositionalAccuracy/nameOfMeasure='horizontal'">
					<xsl:element name="horizpa">
						<xsl:element name="horizpar">
							<xsl:value-of select="./DQ_DataQuality/report/DQ_AbsoluteExternalPositionalAccuracy[nameOfMeasure='horizontal']/evaluationMethodDescription"/>
						</xsl:element>
						<xsl:element name="qhorizpa">
							<xsl:element name="horizpav">
								<!--Por el momento, lo dejo vacio porque el DQ_PostionalAccuracy con el que esta emparejado es una clase y no 
						se con que elemento en concreto relacionarlo. -->
							</xsl:element>
							<xsl:element name="horizpae">
								<xsl:value-of select="./DQ_DataQuality/report/DQ_AbsoluteExternalPositionalAccuracy[nameOfMeasure='horizontal']/measureDescription"/>
							</xsl:element>
						</xsl:element>
					</xsl:element>
				</xsl:if>
				<!--Comprobacion de que nameOfMesaure es vertical -->
				<xsl:if test="./DQ_DataQuality/report/DQ_AbsoluteExternalPositionalAccuracy/nameOfMeasure = 'vertical'">
					<xsl:element name="vertacc">
						<xsl:element name="vertaccr">
							<xsl:value-of select="./DQ_DataQuality/report/DQ_AbsoluteExternalPositionalAccuracy[nameOfMeasure='vertical']/evaluationMethodDescription"/>
						</xsl:element>
						<xsl:element name="qvertpa">
							<xsl:element name="vertaccv">
								<!--Por el momento, lo dejo vacio porque el DQ_PostionalAccuracy con el que esta emparejado es una clase y no 
						se con que elemento en concreto relacionarlo. -->
							</xsl:element>
							<xsl:element name="vertacce">
								<xsl:value-of select="./DQ_DataQuality/report/DQ_AbsoluteExternalPositionalAccuracy[nameOfMeasure='vertical']/measureDescription"/>
							</xsl:element>
						</xsl:element>
					</xsl:element>
				</xsl:if>
			</xsl:element>
		<!--/xsl:if-->
		<!-- LINEAGE -->
		<xsl:if test="./DQ_DataQuality/lineage">
			<xsl:element name="lineage">
				<xsl:for-each select="./DQ_DataQuality/lineage/LI_Lineage/source/LI_Source">
					<!-- statement es el primer elemento, pero no tiene emparejamiento -->
					<xsl:element name="srcinfo">
						<xsl:element name="srccite">
							<xsl:apply-templates select="./sourceCitation/CI_Citation"/>
						</xsl:element>
						<xsl:if test="./scaleDenominator/*/denominator">
							<xsl:element name="srcscale">
								<xsl:value-of select="./scaleDenominator/*/denominator"/>
							</xsl:element>
						</xsl:if>
						<xsl:element name="typesrc">
							<!--El FGDC propone el siguiente emparejamiento -->
							<xsl:value-of select="./description"/>
						</xsl:element>
						<xsl:element name="srctime">
							<xsl:choose>
								<xsl:when test="./sourceExtent/EX_Extent">
									<xsl:apply-templates select="./sourceExtent/EX_Extent"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:element name="timeinfo">
										<xsl:element name="sngdate">
											<xsl:element name="caldate"/>
										</xsl:element>
									</xsl:element>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:element name="srccurr">
								<xsl:value-of select="./sourceExtent/description"/>
							</xsl:element>
						</xsl:element>
						<xsl:element name="srccitea">
							<!-- Emparejamiento propuesto por el FGDC -->
							<xsl:value-of select="./sourceCitation/*/alternateTitle"/>
						</xsl:element>
						<xsl:element name="srccontr">
							<xsl:value-of select="./description"/>
						</xsl:element>
					</xsl:element>
				</xsl:for-each>
				<!-- dentro de lineage, srcinfo es opcional (0..n) pero procstep es obligatorio (1..n).  Tendre que asegurar que esta... -->
				<xsl:choose>
					<xsl:when test="./DQ_DataQuality/lineage/LI_Lineage/processStep/LI_ProcessStep">
						<!--Esta en el dato iso, algo prodremos sacar. -->
						<xsl:for-each select="./DQ_DataQuality/lineage/LI_Lineage/processStep/LI_ProcessStep">
							<xsl:element name="procstep">
								<xsl:element name="procdesc">
									<xsl:value-of select="./description"/>
								</xsl:element>
								<xsl:for-each select="./source/LI_Source/sourceCitation/*/alternateTitle">
									<xsl:element name="srcused">
										<!-- En realidad no sabemos muy bien si es usada o producida...-->
										<xsl:value-of select="."/>
									</xsl:element>
								</xsl:for-each>
								<xsl:element name="procdate">
									<xsl:value-of select="./dateTime"/>
								</xsl:element>
								<xsl:if test="./dateTime">
									<xsl:element name="proctime">
										<xsl:value-of select="./dateTime"/>
									</xsl:element>
								</xsl:if>
								<xsl:for-each select="./source/LI_Source/sourceCitation/*/alternateTitle">
									<xsl:element name="srcprod">
										<!-- En realidad no sabemos muy bien si es usada o producida...-->
										<xsl:value-of select="."/>
									</xsl:element>
								</xsl:for-each>
								<xsl:element name="proccont">
									<xsl:apply-templates select="processor/CI_ResponsibleParty"/>
								</xsl:element>
							</xsl:element>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise>
						<!-- generacion de elementos obligatorios vacios -->
						<xsl:element name="procstep">
							<xsl:element name="procdesc"/>
							<xsl:element name="procdate"/>
						</xsl:element>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:element>
		</xsl:if>
		<!--Emparejamiento propuesto por el FGDC. -->
		<xsl:element name="cloud">
			<!--xsl:value-of select="./../contentInfo/MD_ContentInformation/cloudCoverPercentage"/-->
			<xsl:value-of select="./../contentInfo/MD_ImageDescription/cloudCoverPercentage"/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="contentInfo">
		<xsl:for-each select="MD_FeatureCatalogueDescription">
			<xsl:element name="detailed">
				<xsl:element name="enttyp">
					<xsl:element name="enttypl">
						<xsl:value-of select="./featureTypes"/>
					</xsl:element>
					<xsl:element name="enttypd">
						<!-- Por el momento lo dejo vacio-->
					</xsl:element>
					<xsl:element name="enttypds">
						<!-- Por el momento lo dejo vacio-->
					</xsl:element>
				</xsl:element>
				<!-- el elemento attr no es obligatorio.  Como no tiene emparejamiento con ISO no lo añado -->
			</xsl:element>
			<!--el elemento overview no lo voy a rellenar ya que es optativo y no tiene emparejamieno en ISO -->
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
