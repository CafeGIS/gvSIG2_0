<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" -->
	<!-- xmlns:iso19115="http://www.isotc211.org/iso19115" -->
	<xsl:output method="xml" indent="yes" encoding="ISO-8859-1"/>
	<!-- Esta linea se modificara en funcion de la ubicacion del dtd -->
	<!-- AUTOR: María Pilar Torres Bruna                                                                  -->
	<!-- FECHA: 07/03                                                                                         -->
	<!-- DESCRIPCION:	Hoja de estilo para trasladar ficheros XML que validan el estandar FGDC- IAAA  -->
	<!-- al estandar ISO 19115.        -->
	<!-- SECCIONES TRADUCIDAS: identification information, dataqualityInfo, distribution information y constraint information -->
	<!-- MODIFICACIONES: Para la realizacion de esta hoja de estilo se ha utilizado la original (FGDC - Iso cortos) y se han   -->
	<!-- hecho las modificaciones oportunas.			-->
	<!-- Esta hoja varia con respecto a la FGDC_ISO_LARGOS en que no trasforma imagen ni añade la seccion -->
	<!--  metadata ExtensionInfo.	-->
	<!--  [jnog 5/2/2004] Se eliminan los espacios de nombre iso19115: . Tratar nombres de clases
	      [jnog 6/2/2004] Generar distributionFormat y transferOptions,language 
	      [jlacasta feb 2004 - CAda MD_KEywords debia tener su propio descriptive keywords y no compartir el mismo
	      					    CAda Md_BrowseGraphic deve tener su popio graphic overview y no compartir el mismo
	      					     CAda Md_SEcurityConstraints deve tener su popio metadataConstraints y no compartir el mismo
	      					     CAda LI_Source devuelve tener su popio source y no compartir el mismo
	      					     CAda LI_PorcessStep devuelve tener su popio porcesStep y no compartir el mismo
	       [jlacasta abril 2004 - Solo generaba 1 resouce description para todos elos posibles elementos debia generar n uno para cada uno
	                                        la citación dentro de una source de un processtep la generaba mal , no ponia CI_Citation
	                                        Faltaba toda la sección de representación espacial
	                                        habia problemas con tokens erroneos en el sistema de refencia, problemas de mayusculas y minusculas
	                                        toda la parte de distribución era un desastre solo pasaba el primer elemento y de lo que pasaba la mitad de los
	                                        campos se perdian
	                                        Generamos la sección de entidades y atributos como texto dentro de la sexxión de esuema	                                        -->
	<xsl:template match="/">
		<xsl:apply-templates select="metadata"/>
	</xsl:template>
	<xsl:template match="metadata">
		<xsl:param name="elementAsciival">
			<xsl:copy-of select="./eainfo"/>
		</xsl:param>
		<!--xsl:element name="MD_Metadata"-->
		<MD_Metadata>
			<!--	_MD_IDENTIFICATION-->
			<xsl:element name="identificationInfo">
				<xsl:apply-templates select="idinfo"/>
			</xsl:element>
			<!-- METADATA CONSTRAINTS-->
			<xsl:if test="./metainfo/metac or ./metainfo/metuc or ./metainfo/metoc or ./metainfo/metsi">
				<!--xsl:element name="metadataConstraints"-->
				<xsl:apply-templates select="metainfo"/>
				<!--/xsl:element-->
			</xsl:if>
			<!-- DATAQUALITYINFO-->
			<xsl:if test="./dataqual">
				<xsl:element name="dataQualityInfo">
					<xsl:apply-templates select="dataqual"/>
				</xsl:element>
			</xsl:if>
			<!--SPATIAL REPRESENTATION-->
			<xsl:apply-templates select="spdoinfo"/>
			<!-- METADATA MAINTENANCE-->
			<xsl:apply-templates select="metainfo/metfrd"/>
			<!--REFERENCE SYSTEM INFO-->
			<!--	ESTO HABRIA QUE PROBLARLO...-->
			<!-- El contenido del elemento refsysinfo>, valido segun -->
			<!-- el DTD FGDC-IAAA, se copia, incluyendo descendientes -->
			<!-- y atributos, en el documento ISO destino-->
			<xsl:if test="./refSysInfo">
				<xsl:element name="referenceSystemInfo">
					<xsl:for-each select="./refSysInfo">
						<xsl:apply-templates select="."/>
					</xsl:for-each>
				</xsl:element>
			</xsl:if>
			<!-- CONTENT INFORMATION -->
			<xsl:choose>
				<xsl:when test="./eainfo">
					<xsl:element name="contentInfo">
						<xsl:apply-templates select="eainfo"/>
					</xsl:element>
				</xsl:when>
				<xsl:when test="./dataqual/cloud">
					<xsl:element name="contentInfo">
						<xsl:element name="MD_ContentInformation">
							<xsl:attribute name="xsi:type">MD_ImageDescription</xsl:attribute>
							<xsl:element name="attributeDescription">
								<xsl:element name="typeName"/>
								<xsl:element name="attributeTypes">
									<xsl:element name="AttributeName"/>
									<xsl:element name="TypeName"/>
								</xsl:element>
							</xsl:element>
							<xsl:element name="contentType"/>
							<xsl:element name="cloudCoverPercentage">
								<xsl:value-of select="./dataqual/cloud"/>
							</xsl:element>
						</xsl:element>
					</xsl:element>
				</xsl:when>
			</xsl:choose>
			<!-- DISTRIBUTION INFO-->
			<!-- distinfo (FGDC) es (0..n) y DistributionInfo:MD_Distribution (ISO) es (0..1) -->
			<!-- solo una distribución pero n distribuidores por tanto debe ser 0..n, cada distribución pasa a ser un distribuidor-->
			<!--xsl:if test="distinfo"-->
			<!--xsl:for-each select="./distinfo"-->
			<xsl:element name="distributionInfo">
				<xsl:element name="MD_Distribution">
					<xsl:apply-templates select="distinfo"/>
					<!-- no se mapea distTranOps ya que interesa poder acceder -->
					<!-- a los medios tecnicos usados por el distribuidor desde -->
					<!-- el elemento Distributor:MD_Distributor (en DistributorTransferOptions:MD_DigitalTransferOptions, MD_DistributorTransferOptions)  -->
				</xsl:element>
			</xsl:element>
			<!--ApplicationSchema-->
			<xsl:element name="applicationSchemaInfo">
				<xsl:element name="MD_ApplicationSchemaInformation">
					<xsl:element name="schemaAscii">
												<!--xsl:copy-of select="./eainfo"/-->
						<!--xsl:value-of select="string($elementAsciival)"/-->
						<xsl:text>&lt;?xml version = '1.0' encoding = 'ISO-8859-1'?>
						&lt;eainfo>
						</xsl:text>
						<!--xsl:value-of select="name(xsl:copy-of select=./eainfo/)"/-->
						<xsl:for-each select="./eainfo/detailed">
							<xsl:text>&lt;detailed>
							</xsl:text>
								<xsl:text>&lt;enttyp>
								</xsl:text>
									<xsl:text>&lt;enttypl></xsl:text><xsl:value-of select="./enttyp/enttypl"/><xsl:text>&lt;/enttypl>
									</xsl:text>
									<xsl:text>&lt;enttypd></xsl:text><xsl:value-of select="./enttyp/enttypd"/><xsl:text>&lt;/enttypd>
									</xsl:text>
									<xsl:text>&lt;enttypds></xsl:text><xsl:value-of select="./enttyp/enttypds"/><xsl:text>&lt;/enttypds>
									</xsl:text>
								<xsl:text>&lt;/enttyp>
								</xsl:text>
								<xsl:for-each select="./attr">
									<xsl:text>&lt;attr>
									</xsl:text>
										<xsl:text>&lt;attrlabl></xsl:text><xsl:value-of select="./attrlabl"/><xsl:text>&lt;/attrlabl>
										</xsl:text>
										<xsl:text>&lt;attrdef></xsl:text><xsl:value-of select="./attrdef"/><xsl:text>&lt;/attrdef>
										</xsl:text>
										<xsl:text>&lt;attrdefs></xsl:text><xsl:value-of select="./attrdefs"/><xsl:text>&lt;/attrdefs>
										</xsl:text>
										<xsl:text>&lt;atttype></xsl:text><xsl:value-of select="./atttype"/><xsl:text>&lt;/atttype>
										</xsl:text>
										<xsl:text>&lt;attprecision></xsl:text><xsl:value-of select="./attprecision"/><xsl:text>&lt;/attprecision>
										</xsl:text>
										<xsl:text>&lt;attlength></xsl:text><xsl:value-of select="./attlength"/><xsl:text>&lt;/attlength>
										</xsl:text>
										<xsl:text>&lt;attnullsallowed></xsl:text><xsl:value-of select="./attnullsallowed"/><xsl:text>&lt;/attnullsallowed>
										</xsl:text>
										<xsl:text>&lt;attisprimarykey></xsl:text><xsl:value-of select="./attisprimarykey"/><xsl:text>&lt;/attisprimarykey>
										</xsl:text>
										<xsl:text>&lt;attisunique></xsl:text><xsl:value-of select="./attisunique"/><xsl:text>&lt;/attisunique>
										</xsl:text>
										<xsl:text>&lt;attisindex></xsl:text><xsl:value-of select="./attisindex"/><xsl:text>&lt;/attisindex>
										</xsl:text>
										<xsl:text>&lt;attrdomv>
										</xsl:text>
											<xsl:text>&lt;codesetd>
											</xsl:text>
												<xsl:text>&lt;codesetn></xsl:text><xsl:value-of select="./attrdomv/codesetd/codesetn"/><xsl:text>&lt;/codesetn>
												</xsl:text>
												<xsl:text>&lt;codesets></xsl:text><xsl:value-of select="./attrdomv/codesetd/codesets"/><xsl:text>&lt;/codesets>
												</xsl:text>
											<xsl:text>&lt;/codesetd>
											</xsl:text>
										<xsl:text>&lt;/attrdomv>
										</xsl:text>
										<xsl:text>&lt;attrdomv>
										</xsl:text>
											<xsl:for-each select="./attrdomv/edom">
												<xsl:text>&lt;edom>
												</xsl:text>
													<xsl:text>&lt;edomv></xsl:text><xsl:value-of select="./edomv"/><xsl:text>&lt;/edomv>
													</xsl:text>
													<xsl:text>&lt;edomvd></xsl:text><xsl:value-of select="./edomvd"/><xsl:text>&lt;/edomvd>
													</xsl:text>
													<xsl:text>&lt;edomvds></xsl:text><xsl:value-of select="./edomvds"/><xsl:text>&lt;/edomvds>
													</xsl:text>
												<xsl:text>&lt;/edom>
												</xsl:text>
											</xsl:for-each>
										<xsl:text>&lt;/attrdomv>
										</xsl:text>
										<xsl:text>&lt;attrdomv>
										</xsl:text>
											<xsl:text>&lt;rdom>
											</xsl:text>
												<xsl:text>&lt;rdommin></xsl:text><xsl:value-of select="./attrdomv/rdom/rdommin"/><xsl:text>&lt;/rdommin>
												</xsl:text>
												<xsl:text>&lt;rdommax></xsl:text><xsl:value-of select="./attrdomv/rdom/rdommax"/><xsl:text>&lt;/rdommax>
												</xsl:text>
												<xsl:text>&lt;attrunit></xsl:text><xsl:value-of select="./attrdomv/rdom/attrunit"/><xsl:text>&lt;/attrunit>
												</xsl:text>
												<xsl:text>&lt;attrmres></xsl:text><xsl:value-of select="./attrdomv/rdom/attrmres"/><xsl:text>&lt;/attrmres>
												</xsl:text>
											<xsl:text>&lt;/rdom>
											</xsl:text>
										<xsl:text>&lt;/attrdomv>
										</xsl:text>
										<xsl:text>&lt;attrdomv>
										</xsl:text>
											<xsl:text>&lt;udom></xsl:text><xsl:value-of select="./attrdomv/udom"/><xsl:text>&lt;/udom>
											</xsl:text>
										<xsl:text>&lt;/attrdomv>
										</xsl:text>
										<xsl:for-each select="./begdatea | ./enddatea">
											<xsl:if test="name(.) = 'begdatea'">
												<xsl:text>&lt;begdatea></xsl:text><xsl:value-of select="."/><xsl:text>&lt;/begdatea>
												</xsl:text>											
											</xsl:if>
											<xsl:if test="name(.) = 'enddatea'">
												<xsl:text>&lt;enddatea></xsl:text><xsl:value-of select="."/><xsl:text>&lt;/enddatea>
												</xsl:text>											
											</xsl:if>
									      </xsl:for-each>				
										<!--xsl:text>&lt;begdatea></xsl:text><xsl:value-of select="./begdatea"/><xsl:text>&lt;/begdatea>
										</xsl:text>
										<xsl:text>&lt;enddatea></xsl:text><xsl:value-of select="./enddatea"/><xsl:text>&lt;/enddatea>
										</xsl:text-->
										<xsl:text>&lt;attrvai>
										</xsl:text>
											<xsl:text>&lt;attrva></xsl:text><xsl:value-of select="./attrvai/attrva"/><xsl:text>&lt;/attrva>
											</xsl:text>
											<xsl:text>&lt;attrvae></xsl:text><xsl:value-of select="./attrvai/attrvae"/><xsl:text>&lt;/attrvae>
											</xsl:text>
										<xsl:text>&lt;/attrvai>
										</xsl:text>
										<xsl:text>&lt;attrmfrq></xsl:text><xsl:value-of select="./attrmfrq"/><xsl:text>&lt;/attrmfrq>
										</xsl:text>
									<xsl:text>&lt;/attr>
									</xsl:text>
								</xsl:for-each>
							<xsl:text>&lt;/detailed>
							</xsl:text>
						</xsl:for-each>
						<xsl:text>&lt;/eainfo>
						</xsl:text>
							
					</xsl:element>
				</xsl:element>
			</xsl:element>
			<!--/xsl:for-each-->
			<!--/xsl:if-->
			<!--METADATA EXTENSION INFO-->
			<!-- el elemento MetadataExtensionInfo:MD_MetadataExtensionInformation contiene información acerca de las extensiones de los metadatos-->
			<!--xsl:element name="metadataExtensionInfo">
				<xsl:element name="MD_MetadataExtensionInformation">
					<xsl:element name="extendedElementInformation">
						<xsl:element name="MD_ExtendedElementInformation">
							<xsl:element name="name">browseGraphic Image </xsl:element>
							<xsl:element name="shortName">bgImage </xsl:element>
							<xsl:element name="domainCode">004</xsl:element>
							<xsl:element name="definition"> Image codified in base64</xsl:element>
							<xsl:element name="obligation">
								<xsl:text>optional</xsl:text>
							</xsl:element>
							<xsl:element name="dataType">
								<xsl:text>characterString</xsl:text>
							</xsl:element>
							<xsl:element name="parentEntity">MD_BrowseGraphic</xsl:element>
							<xsl:element name="rule">New MD_Metadata Class</xsl:element>
							<xsl:element name="source"><xsl:element name="CI_ResponsibleParty">
								<xsl:element name="individualName">IAAA</xsl:element>
								<xsl:element name="organisationName">Grupo IAAA. Depto. Informática e Ingeniería de Sistemas. Universidad de Zaragoza</xsl:element>
								<xsl:element name="contactInfo"><xsl:element name="CI_Contact">
									<xsl:element name="phone"><xsl:element name="CI_Telephone">
										<xsl:element name="voice">976762134</xsl:element>
										<xsl:element name="facsimile">976761914</xsl:element>
									</xsl:element></xsl:element>
									<xsl:element name="address"><xsl:element name="CI_Address">
										<xsl:element name="deliveryPoint">María de Luna 3</xsl:element>
										<xsl:element name="city">Zaragoza</xsl:element>
										<xsl:element name="administrativeArea">Unknown</xsl:element>
										<xsl:element name="postalCode">50015</xsl:element>
										<xsl:element name="country">Spain</xsl:element>
										<xsl:element name="electronicMailAddress">iaaa@ebro.cps.unizar.es</xsl:element>
									</xsl:element></xsl:element>
								</xsl:element></xsl:element>
								<xsl:element name="role">
									<xsl:text>publisher</xsl:text>
								</xsl:element>
							</xsl:element></xsl:element>
						</xsl:element>
					</xsl:element>
				</xsl:element>
			</xsl:element-->
			<!-- mdFileID -> fileIdentifier-->
			<xsl:if test="./metainfo/mdFileID">
				<xsl:element name="fileIdentifier">
					<xsl:value-of select="./metainfo/mdFileID"/>
				</xsl:element>
			</xsl:if>
			<!-- Lenguaje del dato-->
			<xsl:if test="./metainfo/metalang !=''">
				<xsl:element name="language">
					<!--xsl:element name="isoCode"-->
					<xsl:value-of select="./metainfo/metalang"/>
					<!--/xsl:element-->
				</xsl:element>
			</xsl:if>
			<!-- hierarchyLevel-->
			<xsl:if test="./metainfo/mdHrLv">
				<xsl:element name="hierarchyLevel">
					<xsl:value-of select="./metainfo/mdHrLv"/>
				</xsl:element>
			</xsl:if>
			<!--CONTACT-->
			<xsl:element name="contact">
				<xsl:apply-templates select="metainfo/metc/cntinfo"/>
			</xsl:element>
			<!--DATESTAMP-->
			<xsl:element name="dateStamp">
				<xsl:value-of select="metainfo/metd"/>
			</xsl:element>
			<!-- 	METADATA STANDARD NAME-->
			<xsl:element name="metadataStandardName">
				<!--<xsl:value-of select="metainfo/metstdn"/>-->
				<!-- autogeneración del nombre del estándar -->
				<xsl:text>ISO 19115 Geographic Information - Metadata</xsl:text>
			</xsl:element>
			<!-- METADATA STANDARD VERSION -->
			<xsl:element name="metadataStandardVersion">
				<!--xsl:value-of select="metainfo/metstdv"/-->
				<xsl:text>http://metadata.dgiwg.org/ISO19115/ISO19115_v0_7.htm</xsl:text>
			</xsl:element>
			<xsl:element name="dataSet"/>
			<!--<xsl:apply-templates select="idinfo/accconst | idinfo/secinfo"/>-->
			<!--/xsl:element-->
		</MD_Metadata>
	</xsl:template>
	<xsl:template match="metainfo">
		<xsl:if test="./metsi">
			<xsl:element name="metadataConstraints">
				<xsl:element name="MD_SecurityConstraints">
					<xsl:if test="./metsi/metsc">
						<xsl:element name="classification">
							<xsl:value-of select="./metsi/metsc"/>
						</xsl:element>
					</xsl:if>
					<xsl:if test="./metsi/metscs">
						<xsl:element name="classificationSystem">
							<xsl:value-of select="./metsi/metscs"/>
						</xsl:element>
					</xsl:if>
					<xsl:if test="./metsi/metshd">
						<xsl:element name="handlingDescription">
							<xsl:value-of select="./metsi/metshd"/>
						</xsl:element>
					</xsl:if>
				</xsl:element>
			</xsl:element>
		</xsl:if>
		<xsl:if test="./metac or ./metoc or ./metuc">
			<xsl:element name="metadataConstraints">
				<xsl:element name="MD_LegalConstraints">
					<xsl:if test="./metac">
						<xsl:element name="accessConstraints">
							<xsl:value-of select="./metac"/>
						</xsl:element>
					</xsl:if>
					<xsl:if test="./metuc">
						<xsl:element name="useConstraints">
							<xsl:value-of select="./metuc"/>
						</xsl:element>
					</xsl:if>
					<xsl:if test="./metoc">
						<xsl:element name="otherConstraints">
							<xsl:value-of select="./metoc"/>
						</xsl:element>
					</xsl:if>
				</xsl:element>
			</xsl:element>
		</xsl:if>
	</xsl:template>
	<xsl:template match="distinfo">
		<!-- La herramienta que genera los ficheros XML (FGDC) -->
		<!-- identifica cada distribution_information con un stdorder, que contiene además -->
		<!-- uno o más elementos digform; además, los elementos fees, ordering y turnarnd -->
		<!-- se han subido al nivel del distributioninformation.-->
		<!-- Si queremos que la hoja de estilo sea mas general, debemos hacer que la -->
		<!-- salida tenga el formato indicado aunque la entrada no cumpla el formato requerido (habitual)-->
		<!-- Como en la mayoria de los casos en  el fichero de entrada todos los elementos -->
		<!-- stdorder cumplirán el formato requerido, por razones de eficiencia en este caso -->
		<!-- se generan los elementos -->
		<!-- correspondientes sin utilizar la plantilla generaDist -->
		<!--xsl:element name="MD_Distribution"-->
		<xsl:element name="distributor">
			<xsl:element name="MD_Distributor">
				<!--DISTRIBUTOR CONTACT-->
				<xsl:element name="distributorContact">
					<xsl:apply-templates select="./distrib/cntinfo"/>
				</xsl:element>
				<!--DISTRIBUTION ORDER PROCESS-->
				<!-- distorOrdPrc no tiene correspondencia directa con elementos de fgdc-->
				<!-- pero sus hijos se corresponden con hijos de stdorder (menos availabl)-->
				<xsl:apply-templates select="./stdorder"/>
				<!--DISTRIBUTOR TRANSFER OPTIONS-->
				<!-- Crea el elemento DistributorTransferOptions:MD_DigitalTransferOptions -->
				<!-- He tenido que modificar el dtd de ISO 19115 ya que -->
				<!-- en este aparece el elemento como distroTran-->
				<!--xsl:element name="distributorTransferOptions">
						<xsl:apply-templates select="./stdorder/digform/digtopt"/>
					</xsl:element-->
				<!--DISTRIBUTOR FORMAT-->
				<!-- El elemento DistributorFormat: MD_Format es obligatorio en Distributor:MD_Distributor (ISO) -->
				<!-- y no lo es en FGDC (/metadata/distinfo/stdorder/digform/digtinfo puede no existir)-->
				<!-- aunque ya hemos comprobado que existe 1 y solo 1 vez -->
				<!--xsl:element name="distributorFormat">
						<xsl:apply-templates select="./stdorder/digform/digtinfo"/>
					</xsl:element-->
			</xsl:element>
		</xsl:element>
		<!--Generar tambien transferOptions -->
		<!--xsl:element name="transferOptions"-->
		<xsl:apply-templates select="./stdorder/digform/digtopt"/>
		<!--/xsl:element-->
		<!--Generar tambien ditributionFormat -->
		<!--xsl:element name="distributionFormat"-->
		<xsl:apply-templates select="./stdorder/digform/digtinfo"/>
		<!--/xsl:element-->
		<!--/xsl:element-->
	</xsl:template>
	<xsl:template name="generaDist">
		<xsl:param name="nivel">a</xsl:param>
		<!-- Esta plantilla genera el formato de salida de MD_Distributor ISO correcto siempre que el-->
		<!-- formato de los elementos FGDC correspondientes validen el DTD. -->
		<!-- Unicamente si aparecen en el mismo elemento digtopt varios elementos offoptn se generaria -->
		<!-- un elemento DistributorTransferOptions:MD_DigitalTransferOptions vacio-->
		<xsl:for-each select="./stdorder">
			<xsl:choose>
				<!-- Cuando el elemento onlinopt se repite mas de 1 vez dentro de digtopt, -->
				<!-- siendo el numero de apariciones de digform igual a 1, y el numero de -->
				<!-- apariciones de offoptn menor o igual a 1 ... -->
				<xsl:when test="count(digform)=1 and ( count(descendant::onlinopt)&gt;=1 or count(descendant::offoptn)&gt;=1 ) ">
					<!-- el estandar FGDC no permite que exista un elemento stdorder que nocontenga ningún onlinopt -->
					<!-- ni ningún offoptn -->
					<xsl:choose>
						<xsl:when test="$nivel='a'">
							<xsl:for-each select="./digform/digtopt/onlinopt">
								<!--xsl:apply-templates select="ancestor::digform/digtinfo"/-->
								<xsl:element name="distributorFormat">
									<xsl:apply-templates select="ancestor::digform/digtinfo"/>
								</xsl:element>
								<!--  Se aplica la plantilla sobre digtinfo (solo hay 1) tantas veces como aparezca onlinopt.-->
								<!--  Dicha plantilla crea el elemento DistributorFormat: MD_Format -->
							</xsl:for-each>
							<xsl:for-each select="./digform/digtopt/offoptn">
								<!--xsl:apply-templates select="ancestor::digform/digtinfo"/-->
								<xsl:element name="distributorFormat">
									<xsl:apply-templates select="ancestor::digform/digtinfo"/>
								</xsl:element>
								<!--  Se aplica la plantilla sobre digtinfo (solo hay 1) tantas veces como aparezca offoptn.-->
								<!--  Dicha plantilla crea el elemento DistributorFormat: MD_Format -->
							</xsl:for-each>
						</xsl:when>
						<xsl:when test="$nivel='b'">
							<xsl:for-each select="./digform/digtopt/onlinopt">
								<xsl:apply-templates select="ancestor::stdorder"/>
								<!--  Se aplica la plantilla sobre el elemento stdorder actual (solo hay 1) -->
								<!--  tantas veces como aparezca onlinopt.-->
								<!--  Dicha plantilla crea el elemento distorOrdPrc -->
							</xsl:for-each>
							<xsl:for-each select="./digform/digtopt/offoptn">
								<xsl:apply-templates select="ancestor::stdorder"/>
								<!--  Se aplica la plantilla sobre el elemento stdorder actual (solo hay 1) -->
								<!--  tantas veces como aparezca offoptn.-->
								<!--  Dicha plantilla crea el elemento distorOrdPrc -->
							</xsl:for-each>
						</xsl:when>
						<xsl:when test="$nivel='c'">
							<!-- Se aplica la plantilla sobre digtopt (el elemento que se repite mas de 1 vez) -->
							<!--xsl:apply-templates select="./digform/digtopt"/-->
							<xsl:element name="distributorTransferOptions">
								<xsl:apply-templates select="./digform/digtopt"/>
							</xsl:element>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="count(digform)&gt;1">
					<xsl:choose>
						<xsl:when test="$nivel='a'">
							<xsl:for-each select="./digform">
								<!-- si aparece mas de un elemento onlinopt dentro de un digform-->
								<xsl:for-each select="descendant::onlinopt">
									<!--xsl:apply-templates select="ancestor::digform/digtinfo"/!-->
									<xsl:element name="distributorFormat">
										<xsl:apply-templates select="ancestor::digform/digtinfo"/>
									</xsl:element>
								</xsl:for-each>
								<!-- si aparece mas de un elemento offoptn dentro de un digform-->
								<xsl:for-each select="descendant::offoptn">
									<!--xsl:apply-templates select="ancestor::digform/digtinfo"/-->
									<xsl:element name="distributorFormat">
										<xsl:apply-templates select="ancestor::digform/digtinfo"/>
									</xsl:element>
								</xsl:for-each>
							</xsl:for-each>
						</xsl:when>
						<xsl:when test="$nivel='b'">
							<xsl:for-each select="./digform">
								<!-- si aparece mas de un elemento onlinopt dentro de un digform-->
								<xsl:for-each select="descendant::onlinopt">
									<xsl:apply-templates select="ancestor::stdorder"/>
								</xsl:for-each>
								<!-- si aparece mas de un elemento offoptn dentro de un digform-->
								<xsl:for-each select="descendant::offoptn">
									<xsl:apply-templates select="ancestor::stdorder"/>
								</xsl:for-each>
							</xsl:for-each>
						</xsl:when>
						<xsl:when test="$nivel='c'">
							<!-- Se aplica la plantilla sobre digtopt (solo hay 1)  -->
							<!-- tantas veces como se repita digform-->
							<!--xsl:apply-templates select="./digform/digtopt"/-->
							<xsl:element name="distributorTransferOptions">
								<xsl:apply-templates select="./stdorder/digform/digtopt"/>
							</xsl:element>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<!-- Este caso no deberia darse porque el estandar FGDC exige que los elementos -->
					<!-- stdorder contengan al menos un elemento onlinopt o un elemento offoptn -->
					<xsl:element name="distributionOrderProcess"/>
					<xsl:element name="distributorTransferOptions"/>
					<xsl:element name="distributorFormat"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="digtinfo">
		<!-- creamos un DistributorFormat: MD_Format(iso) por cada digtinfo(fgdc) -->
		<!--xsl:element name="distributorFormat"-->
		<xsl:element name="distributionFormat">
			<xsl:element name="MD_Format">
				<!-- Name y version son obligatorios.  No asi formspec y filedec-->
				<xsl:element name="name">
					<xsl:value-of select="formname"/>
				</xsl:element>
				<!--es obligatorio -->
				<xsl:element name="version">
					<!-- si el elemento formvern esta vacio hay problemas...-->
					<xsl:choose>
						<xsl:when test="./formvern!=''">
							<xsl:value-of select="formvern"/>
						</xsl:when>
						<xsl:otherwise>UNKNOWN</xsl:otherwise>
					</xsl:choose>
				</xsl:element>
				<xsl:if test="formspec">
					<xsl:element name="specification">
						<xsl:value-of select="./formspec"/>
					</xsl:element>
				</xsl:if>
				<xsl:if test="filedec">
					<xsl:element name="fileDecompressionTechnique">
						<xsl:value-of select="./filedec"/>
					</xsl:element>
				</xsl:if>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<xsl:template match="stdorder">
		<xsl:element name="distributionOrderProcess">
			<xsl:element name="MD_StandardOrderProcess">
				<!-- fees es obligatorio en FGDC -->
				<xsl:element name="fees">
					<!--xsl:element name="monetaryAmt"-->
					<!-- Hay problemas si fees esta vacio, ya que en ISO, el valor tiene que tener una longitud de al menos 1 -->
					<!--xsl:choose>
							<xsl:when test="./fees!=''"-->
					<xsl:value-of select="./fees"/>
					<!--/xsl:when>
							<xsl:otherwise>0.0</xsl:otherwise>
						</xsl:choose>
					</xsl:element>
					<xsl:element name="monetaryType">
						<xsl:element name="isoName">Euro</xsl:element>
					</xsl:element-->
				</xsl:element>
				<!-- ambos son (0..1)-->
				<xsl:if test="../availabl/timeinfo[sngdate]">
					<!-- solo interesa mapear este elemento si aparece una sola fecha (sngdate)-->
					<xsl:element name="plannedAvailableDateTime">
						<xsl:apply-templates select="../availabl/timeinfo"/>
					</xsl:element>
				</xsl:if>
				<!-- ambos son (0..1) -->
				<xsl:if test="ordering">
					<xsl:element name="orderingInstructions">
						<xsl:value-of select="./ordering"/>
					</xsl:element>
				</xsl:if>
				<!-- ambos son (0..1) -->
				<xsl:if test="turnarnd">
					<xsl:element name="turnaround">
						<xsl:value-of select="./turnarnd"/>
					</xsl:element>
				</xsl:if>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<xsl:template match="distinfo/stdorder/digform/digtopt">
		<xsl:if test="onlinopt/computer[networka]">
			<!-- solo se crea un DistributorTransferOptions:MD_DigitalTransferOptions para onlinopt en caso de que haya al menos un elemento networkr-->
			<!--xsl:element name="distributorTransferOptions"-->
			<xsl:element name="transferOptions">
				<xsl:element name="MD_DigitalTransferOptions">
					<xsl:for-each select="onlinopt">
						<xsl:if test="ancestor::digform/digtinfo[formcont]">
							<xsl:element name="unitsOfDistribution">
								<xsl:value-of select="ancestor::digform/digtinfo/formcont"/>
							</xsl:element>
						</xsl:if>
						<!-- los dos son (0..1) -->
						<xsl:if test="ancestor::digform/digtinfo[transize]">
							<xsl:element name="transferSize">
								<xsl:value-of select="ancestor::digform/digtinfo/transize"/>
							</xsl:element>
						</xsl:if>
						<xsl:for-each select="./computer/networka/networkr">
							<xsl:element name="onLine">
								<xsl:element name="CI_OnlineResource">
									<!-- tiene mas elementos segun ISO pero no corresponden con ninguno en FGDC-->
									<!-- se crea un OnLine:CI_OnLineResource por cada networkr, dado que  solo puede-->
									<!-- haber un Linkage en cada OnLine:CI_OnLineResource,pero puede haber  -->
									<!-- muchos OnLine:CI_OnLineResource -->
									<xsl:element name="linkage">
										<xsl:value-of select="."/>
									</xsl:element>
								</xsl:element>
							</xsl:element>
						</xsl:for-each>
					</xsl:for-each>
					<xsl:for-each select="offoptn">
						<xsl:apply-templates select="."/>
					</xsl:for-each>
				</xsl:element>
			</xsl:element>
		</xsl:if>
	</xsl:template>
	<xsl:template match="offoptn">
		<xsl:element name="offLine">
			<xsl:element name="MD_Medium">
				<xsl:element name="name">
					<xsl:value-of select="./offmedia"/>
				</xsl:element>
				<xsl:if test="./reccap">
					<xsl:for-each select="./reccap/recden">
						<xsl:element name="density">
							<xsl:value-of select="."/>
						</xsl:element>
					</xsl:for-each>
					<!--CREO QUE PONERLO AQUI DENTRO NO PASA NADA PERO TEER EN CUENTA QUE ANTES ESTABA FUERA-->
					<!-- recfmt es (1..n) y MediumFormat (0..n) -->
					<xsl:element name="densityUnits">
						<xsl:value-of select="./reccap/recdenu"/>
					</xsl:element>
					<xsl:for-each select="./recfmt">
						<xsl:element name="mediumFormat">
							<xsl:value-of select="."/>
						</xsl:element>
					</xsl:for-each>
				</xsl:if>
				<!-- ambos son (0..1) -->
				<xsl:if test="./compat">
					<xsl:element name="mediumNote">
						<xsl:value-of select="./compat"/>
					</xsl:element>
				</xsl:if>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<xsl:template match="idinfo">
		<xsl:element name="MD_DataIdentification">
			<!-- xsl:element name="_MD_Identification"-->
			<!-- xsl:attribute name="xsi:type">MD_DataIdentification</xsl:attribute -->
			<!--  Aqui, debemos tener cuidado con el orden correcto-->
			<!-- CITATION-->
			<xsl:element name="citation">
				<xsl:apply-templates select="citation/citeinfo"/>
			</xsl:element>
			<!-- ABSTRACT-->
			<xsl:element name="abstract">
				<xsl:value-of select="descript/abstract"/>
			</xsl:element>
			<!--PURPOSE-->
			<xsl:element name="purpose">
				<xsl:value-of select="descript/purpose"/>
			</xsl:element>
			<!--CREDIT-->
			<!-- datacred es (0..1) e Credit es (0..n) -->
			<xsl:if test="datacred">
				<xsl:element name="credit">
					<xsl:value-of select="./datacred"/>
				</xsl:element>
			</xsl:if>
			<!-- STATUS-->
			<xsl:apply-templates select="./status/progress"/>
			<!--POINT OF CONTACT-->
			<!-- idinfo/ptcontac es (0..1) e idPoc es (0..n) -->
			<!-- idPoc se podria introducir dentro del template-->
			<!-- si el template no se utilizara en mas sitios-->
			<xsl:if test="ptcontac">
				<xsl:element name="pointOfContact">
					<xsl:apply-templates select="./ptcontac/cntinfo"/>
				</xsl:element>
			</xsl:if>
			<!--RESOURCE CONSTRAINTS-->
			<xsl:if test="./accconst!='' or ./useconst!='' or ./secinfo!=''">
				<!--xsl:element name="resourceConstraints"-->
				<!--xsl:apply-templates select="./accconst | ./secinfo"/-->
				<xsl:if test="./accconst!='' or ./useconst!=''">
					<xsl:element name="resourceConstraints">
						<xsl:element name="MD_LegalConstraints">
							<xsl:if test="./accconst">
								<xsl:element name="accessConstraints">
									<xsl:value-of select="./accconst"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="./useconst">
								<xsl:element name="useConstraints">
									<xsl:value-of select="./useconst"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="./othconst">
								<xsl:element name="otherConstraints">
									<xsl:value-of select="./othconst"/>
								</xsl:element>
							</xsl:if>
						</xsl:element>
					</xsl:element>
				</xsl:if>
				<xsl:if test="./secinfo">
					<xsl:element name="resourceConstraints">
						<xsl:element name="MD_SecurityConstraints">
							<xsl:element name="classification">
								<xsl:value-of select="./secinfo/secclass"/>
							</xsl:element>
							<xsl:element name="classificationSystem">
								<xsl:value-of select="./secinfo/secsys"/>
							</xsl:element>
							<xsl:element name="handlingDescription">
								<xsl:value-of select="./secinfo/sechandl"/>
							</xsl:element>
						</xsl:element>
					</xsl:element>
				</xsl:if>
				<!--/xsl:element-->
			</xsl:if>
			<!--RESOURCE MAINTENANCE-->
			<xsl:if test="./status/update!=''">
				<xsl:apply-templates select="./status/update"/>
			</xsl:if>
			<!--DESCRIPTIVE KEYWORDS-->
			<!--En Iso, este elemento no es obligatorio, por lo que deberia asegurarme de que los valores de FGDC
			son correctos -->
			<!--xsl:if test="normalize-space(descriptiveKeywords/MD_Keywords/keyword)!=''">
				<xsl:element name="descriptiveKeywords">
					<xsl:apply-templates select="keywords/theme|keywords/place"/>			
				</xsl:element>
			</xsl:if-->
			<xsl:if test="normalize-space(keywords/theme/themekey)!='' or normalize-space(keywords/place/placekey)!='' or normalize-space(keywords/stratum/stratkey)!='' or normalize-space(keywords/temporal/tempkey)!=''">
				<!--xsl:element name="descriptiveKeywords"-->
				<xsl:apply-templates select="keywords/theme|keywords/place|keywords/stratum|keywords/temporal"/>
				<!--/xsl:element-->
			</xsl:if>
			<!--	GRAPHIC OVERVIEW -->
			<!-- browse y GraphicOverview:MD_BrowseGraphic son (0..n) -->
			<xsl:if test="./browse/browsen">
				<!--xsl:element name="graphicOverview"-->
				<!--metemos la imagen dentro de la descripcion de la informacion"-->
				<xsl:for-each select="browse">
					<xsl:element name="graphicOverview">
						<xsl:element name="MD_BrowseGraphic">
							<xsl:element name="fileName">
								<xsl:value-of select="browsen"/>
							</xsl:element>
							<xsl:element name="fileDescription">
								<xsl:value-of select="browsed"/>
								<xsl:if test="./browseem">
									<xsl:text>
%%%%%%%%%%%%%%%%%PREVIEW BASE64%%%%%%%%%%%%%%%%%
</xsl:text>
									<xsl:value-of select="browseem"/>
								</xsl:if>
							</xsl:element>
							<xsl:element name="fileType">
								<xsl:value-of select="browset"/>
							</xsl:element>
							<!-- el elemento bgImage no pertenece al estandar-->
							<!--xsl:if test="./browseem">
								<xsl:element name="bgImage">
									<xsl:value-of select="./browseem"/>
								</xsl:element>
							</xsl:if-->
						</xsl:element>
					</xsl:element>
				</xsl:for-each>
				<!--/xsl:element-->
			</xsl:if>
			<!--	SPATIAL REPRESENTATION TYPE-->
			<!-- En  el estandar fgdc el elemento indspref contiene PCDATA (sin atributos, y sin valores aconsejados)  -->
			<!-- sin embargo no podemos trasladar cualquier palabra como SpatRepTypCd porque hay una lista de valores -->
			<!-- posibles para el atributo. Por ello hago la suposicion de que el contenido spdoinfo/indspref -->
			<!-- sera textTable o video, (asi se establece en la hoja de estilo ISO_FGDC). -->
			<!-- SpatialRespresentationType es (0..n) y indspref es (0..1) en spdoinfo q es (0..1)-->
			<xsl:if test="./spdoinfo/indspref">
				<xsl:element name="	spatialRepresentationType">
					<xsl:choose>
						<xsl:when test="normalize-space(../spdoinfo/indspref)='video' or
							normalize-space(../spdoinfo/indspref)='VIDEO'">
							<xsl:text>video</xsl:text>
						</xsl:when>
						<xsl:when test="normalize-space(../spdoinfo/indspref)='textTable' or
							normalize-space(../spdoinfo/indspref)='TEXTTABLE'">
							<xsl:text>textTable</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<!--si el valor no es uno de los aconsejados, se traslada tal cual-->
							<xsl:value-of select="normalize-space(../spdoinfo/indspref)"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:element>
			</xsl:if>
			<!-- En  el estandar fgdc el elemento direct contiene PCDATA (sin atributos), pero existen unos valores  -->
			<!-- aconsejados (Vector, Point y Raster), que se corresponden con 2 de los posibles atributos de SpatialRespresentationType-->
			<!-- (vector y grid )-->
			<!-- SpatialRespresentationType es (0..n) y direct es (0..1) en spdoinfo q es (0..1)-->
			<xsl:if test="../spdoinfo/direct">
				<xsl:element name="spatialRepresentationType">
					<xsl:choose>
						<xsl:when test="normalize-space(../spdoinfo/direct)='Vector' or
							normalize-space(../spdoinfo/direct)='VECTOR'">
							<xsl:text>vector</xsl:text>
						</xsl:when>
						<xsl:when test="normalize-space(../spdoinfo/direct)='Point' or
							normalize-space(../spdoinfo/direct)='POINT'">
							<xsl:text>vector</xsl:text>
						</xsl:when>
						<xsl:when test="normalize-space(../spdoinfo/direct)='Raster' or
							normalize-space(../spdoinfo/direct)='RASTER'">
							<xsl:text>grid</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<!--si el valor no es uno de los aconsejados, se traslada tal cual-->
							<xsl:value-of select="normalize-space(../spdoinfo/direct)"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:element>
			</xsl:if>
			<!-- SPATIAL RESOLUTION-->
			<!-- esto concuerda con el fgdc version IAAA(nuestro) -->
			<!-- spdom/scale es (0..1) y SpatialResolution:MD_Resolution es (0..n)-->
			<xsl:if test="spdom/scale">
				<xsl:element name="spatialResolution">
					<xsl:element name="equivalentScale">
						<xsl:element name="MD_RepresentativeFraction">
							<xsl:element name="denominator">
								<xsl:value-of select="./spdom/scale"/>
							</xsl:element>
						</xsl:element>
					</xsl:element>
				</xsl:element>
			</xsl:if>
			<!-- LANGUAGE-->
			<xsl:choose>
				<xsl:when test="/metadata/idinfo[idlang]">
					<xsl:for-each select="./idlang">
						<xsl:element name="language">
							<!-- Por el momento supongo que el elemento sera un isoCode-->
							<!--xsl:element name="isoCode"-->
							<xsl:value-of select="./idlangkey"/>
							<!--/xsl:element-->
						</xsl:element>
					</xsl:for-each>
				</xsl:when>
				<xsl:otherwise>
					<!-- si no hay ninguna etiqueta idlang en el fich original,-->
					<!-- se crea un elemento isoCode en ingles pq es obligatoria-->
					<xsl:element name="language">
						<!--xsl:element name="isoCode"-->
						<xsl:text>eng</xsl:text>
						<!--/xsl:element-->
					</xsl:element>
				</xsl:otherwise>
			</xsl:choose>
			<!-- TOPIC CATEGORY-->
			<!-- En FGDC IAAA topicCategory equivale a tpCat, dentro de keywords-->
			<!-- quiza esta palabra haya que separarla -->
			<xsl:element name="topicCategory">
				<xsl:value-of select="./keywords/tpCat"/>
			</xsl:element>
			<!-- 	ENVIROMENT DESCRIPTION-->
			<!-- native y EnvironmentDescription son (0..1) -->
			<xsl:if test="native">
				<xsl:element name="environmentDescription">
					<xsl:value-of select="./native"/>
				</xsl:element>
			</xsl:if>
			<!--EXTENT -->
			<!-- ojo esto concuerda con el fgdc version IAAA(nuestro)-->
			<!-- spdom/bounding es (0..1) y dataidinfo/geobox es (0..n)-->
			<!-- aqui se mapea timeperd con Extent: EX_Extent-->
			<xsl:if test="./timeperd/timeinfo/sngdate/caldate!='' or ./timeperd/timeinfo/rngdates/begdate!='' or ./spdom/bounding/westbc!=''">
				<xsl:if test="./timeperd/timeinfo/sngdate/caldate!='' or ./timeperd/timeinfo/rngdates/begdate!=''">
					<xsl:element name="extent">
						<xsl:element name="EX_Extent">
							<!-- description es obligatorio-->
							<xsl:element name="description"/>
							<xsl:element name="temporalElement">
								<xsl:element name="EX_TemporalExtent">
									<xsl:element name="extent">
										<!--ESTO NO LO HE REVISADO -->
										<xsl:if test="./timeperd/timeinfo/sngdate/caldate!=''">
											<xsl:element name="instant">
												<xsl:value-of select="./timeperd/timeinfo/sngdate/caldate"/>
											</xsl:element>
										</xsl:if>
										<xsl:if test="./timeperd/timeinfo/rngdates">
											<xsl:element name="beginEnd">
												<xsl:element name="begin">
													<xsl:value-of select="./timeperd/timeinfo/rngdates/begdate"/>
												</xsl:element>
												<xsl:element name="end">
													<xsl:value-of select="./timeperd/timeinfo/rngdates/enddate"/>
												</xsl:element>
											</xsl:element>
										</xsl:if>
									</xsl:element>
								</xsl:element>
							</xsl:element>
						</xsl:element>
					</xsl:element>
				</xsl:if>
				<!--xsl:if test="spdom/bounding"-->
				<xsl:if test="./spdom/bounding/westbc!=''">
					<xsl:element name="extent">
						<xsl:element name="EX_Extent">
							<xsl:element name="geographicElement">
								<xsl:element name="EX_GeographicBoundingBox">
									<!--xsl:element name="_EX_GeographicExtent"-->
									<!--xsl:attribute name="xsi:type">EX_GeographicBoundingBox</xsl:attribute-->
									<xsl:element name="westBoundLongitude">
										<xsl:value-of select="./spdom/bounding/westbc"/>
									</xsl:element>
									<xsl:element name="eastBoundLongitude">
										<xsl:value-of select="./spdom/bounding/eastbc"/>
									</xsl:element>
									<xsl:element name="southBoundLatitude">
										<xsl:value-of select="./spdom/bounding/southbc"/>
									</xsl:element>
									<xsl:element name="northBoundLatitude">
										<xsl:value-of select="./spdom/bounding/northbc"/>
									</xsl:element>
								</xsl:element>
							</xsl:element>
						</xsl:element>
					</xsl:element>
				</xsl:if>
			</xsl:if>
			<!--ENVIROMENTAL DESCRIPTION -->
			<!-- supplinf y SupplementalInformation son (0..1) -->
			<xsl:if test="descript/supplinf">
				<xsl:element name="supplementalInformation">
					<xsl:value-of select="./descript/supplinf"/>
				</xsl:element>
			</xsl:if>
		</xsl:element>
	</xsl:template>
	<!--Añadimos doda la paster de raster y objetos vectoriales que no se transladaba antes a ISO-->
	<xsl:template match="spdoinfo">
		<xsl:if test="ptvctinf/vpfterm">
			<xsl:element name="spatialRepresentationInfo">
				<xsl:element name="MD_VectorSpatialRepresentation">
					<xsl:element name="topologyLevel">
						<xsl:value-of select="./ptvctinf/vpfterm/vpflevel"/>
					</xsl:element>
					<xsl:for-each select="./ptvctinf/vpfterm/vpfinfo">
						<xsl:element name="geometricObjects">
							<xsl:element name="MD_GeometricObjects">
								<xsl:element name="geometricObjectType">
									<xsl:value-of select="./vpftype"/>
								</xsl:element>
								<xsl:element name="geometricObjectCount">
									<xsl:value-of select="./ptvctcnt"/>
								</xsl:element>
							</xsl:element>
						</xsl:element>
					</xsl:for-each>
				</xsl:element>
			</xsl:element>
		</xsl:if>
		<xsl:if test="rastinfo">
			<xsl:element name="spatialRepresentationInfo">
				<xsl:element name="MD_GridSpatialRepresentation">
					<xsl:element name="MD_Dimension">
						<xsl:element name="dimensionName">
							<xsl:text>row</xsl:text>
						</xsl:element>
						<xsl:element name="dimensionSize">
							<xsl:value-of select="./rastinfo/rowcount"/>
						</xsl:element>
					</xsl:element>
					<xsl:element name="MD_Dimension">
						<xsl:element name="dimensionName">
							<xsl:text>column</xsl:text>
						</xsl:element>
						<xsl:element name="dimensionSize">
							<xsl:value-of select="./rastinfo/colcount"/>
						</xsl:element>
					</xsl:element>
					<xsl:element name="MD_Dimension">
						<xsl:element name="dimensionName">
							<xsl:text>vertical</xsl:text>
						</xsl:element>
						<xsl:element name="dimensionSize">
							<xsl:value-of select="./rastinfo/vrtcount"/>
						</xsl:element>
					</xsl:element>
					<xsl:element name="cellGeometry">
						<xsl:value-of select="./rastinfo/rasttype"/>
					</xsl:element>
				</xsl:element>
			</xsl:element>
		</xsl:if>
	</xsl:template>
	<xsl:template match="place | theme | stratum | temporal">
		<xsl:element name="descriptiveKeywords">
			<xsl:element name="MD_Keywords">
				<xsl:for-each select="./placekey | ./themekey |./stratkey | ./tempkey">
					<xsl:element name="keyword">
						<xsl:value-of select="."/>
					</xsl:element>
				</xsl:for-each>
				<xsl:element name="type">
					<xsl:if test="name(.) = 'place'">
						<xsl:text>place</xsl:text>
					</xsl:if>
					<xsl:if test="name(.) = 'theme'">
						<xsl:text>theme</xsl:text>
					</xsl:if>
					<xsl:if test="name(.) = 'stratum'">
						<xsl:text>stratum</xsl:text>
					</xsl:if>
					<xsl:if test="name(.) = 'temporal'">
						<xsl:text>temporal</xsl:text>
					</xsl:if>
				</xsl:element>
				<xsl:element name="thesaurusName">
					<xsl:element name="CI_Citation">
						<xsl:element name="title">
							<xsl:value-of select="./placekt|./themekt|./stratkt|./tempkt"/>
						</xsl:element>
						<!--Date:CI_Date es obligatorio y no se con que mapearlo-->
						<xsl:element name="date">
							<xsl:element name="CI_Date">
								<xsl:element name="date">0001-01-01</xsl:element>
								<xsl:element name="dateType">
									<xsl:text>publication</xsl:text>
								</xsl:element>
							</xsl:element>
						</xsl:element>
					</xsl:element>
				</xsl:element>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<!-- Plantilla que se usa solomente para mapear dataidinfo/Citation-->
	<xsl:template match="citation/citeinfo | identAuth/citeinfo">
		<xsl:element name="CI_Citation">
			<!--  TITLE -->
			<xsl:element name="title">
				<xsl:value-of select="title"/>
			</xsl:element>
			<!-- ALTERNATETITLE no tiene equivalente en FGDC -->
			<!-- DATE -->
			<!--Aqui surgen problemas cuando el valor de date no esta, es decir, tenemos algo como </pubdate> -->
			<!-- Por tanto, y aunque la solucion no parezca buena, comprobare que es distinto de vacio y si lo es,  -->
			<!-- le pondre una fecha invalida -->
			<xsl:element name="date">
				<xsl:element name="CI_Date">
					<xsl:element name="date">
						<xsl:choose>
							<xsl:when test="./pubdate!=''">
								<xsl:value-of select="pubdate"/>
							</xsl:when>
							<xsl:otherwise>0001-01-01</xsl:otherwise>
						</xsl:choose>
					</xsl:element>
					<xsl:element name="dateType">
						<xsl:text>publication</xsl:text>
					</xsl:element>
				</xsl:element>
			</xsl:element>
			<!-- EDITION -->
			<xsl:if test="edition">
				<xsl:element name="edition">
					<xsl:value-of select="./edition"/>
				</xsl:element>
			</xsl:if>
			<!-- EDITIONDATE no tiene equivalente en FGDC -->
			<!-- IDENTIFIER -->
			<xsl:if test="citId">
				<xsl:element name="identifier">
					<xsl:element name="MD_Identifier">
						<xsl:element name="code">
							<xsl:value-of select="./citId"/>
						</xsl:element>
					</xsl:element>
				</xsl:element>
			</xsl:if>
			<!-- CITEDRESPONSIBLEPARTY -->
			<xsl:for-each select="origin">
				<xsl:if test="normalize-space(.)!=''">
					<xsl:element name="citedResponsibleParty">
						<xsl:element name="CI_ResponsibleParty">
							<xsl:element name="organisationName">
								<xsl:value-of select="."/>
							</xsl:element>
							<xsl:if test="/metadata/idinfo/citation/citeinfo[onlink]">
								<xsl:element name="contactInfo">
									<xsl:element name="CI_Contact">
										<xsl:element name="onlineResource">
											<xsl:element name="CI_OnlineResource">
												<xsl:element name="linkage">
													<xsl:value-of select="../onlink"/>
												</xsl:element>
											</xsl:element>
										</xsl:element>
									</xsl:element>
								</xsl:element>
							</xsl:if>
							<xsl:element name="role">
								<xsl:text>originator</xsl:text>
							</xsl:element>
						</xsl:element>
					</xsl:element>
				</xsl:if>
			</xsl:for-each>
			<xsl:if test="/metadata/idinfo/citation/citeinfo[pubinfo]">
				<xsl:element name="citedResponsibleParty">
					<xsl:element name="CI_ResponsibleParty">
						<xsl:element name="organisationName">
							<xsl:value-of select="./pubinfo/publish"/>
						</xsl:element>
						<xsl:element name="contactInfo">
							<xsl:element name="CI_Contact">
								<xsl:element name="address">
									<xsl:element name="CI_Address">
										<xsl:element name="city">
											<xsl:value-of select="./pubinfo/pubplace"/>
										</xsl:element>
									</xsl:element>
								</xsl:element>
							</xsl:element>
						</xsl:element>
						<xsl:element name="role">
							<xsl:text>publisher</xsl:text>
						</xsl:element>
					</xsl:element>
				</xsl:element>
			</xsl:if>
			<!-- PRESENTATIONFORM -->
			<xsl:if test="geoform">
				<xsl:element name="presentationForm">
					<xsl:value-of select="./geoform"/>
				</xsl:element>
			</xsl:if>
			<!-- SERIES -->
			<xsl:if test="serinfo">
				<xsl:element name="series">
					<xsl:element name="CI_Series">
						<!-- los dos elementos de Series: CI_Series son opcionales -->
						<!-- pero son obligatorios sus correspondientes en serinfo -->
						<xsl:element name="name">
							<xsl:value-of select="./serinfo/sername"/>
						</xsl:element>
						<xsl:element name="issueIdentification">
							<xsl:value-of select="./serinfo/issue"/>
						</xsl:element>
					</xsl:element>
				</xsl:element>
			</xsl:if>
			<!-- OTHERCITATIONDETAILS-->
			<xsl:if test="othercit">
				<xsl:element name="otherCitationDetails">
					<xsl:value-of select="./othercit"/>
				</xsl:element>
			</xsl:if>
			<!-- ISBN -->
			<xsl:if test="isbn">
				<xsl:element name="ISBN">
					<xsl:value-of select="./isbn"/>
				</xsl:element>
			</xsl:if>
			<!-- ISSN -->
			<xsl:if test="issn">
				<xsl:element name="ISSN">
					<xsl:value-of select="./issn"/>
				</xsl:element>
			</xsl:if>
		</xsl:element>
		<!-- acaba CI_Citation -->
	</xsl:template>
	<!-- Plantilla que se usa solomente para mapear srccite/citeinfo-->
	<!-- Necesidad de duplicar la plantilla, ya que este elemento, es igual que todos los citation pero, poniendo en alternate title un elemento extraño, 
	es decir, que esta en una ruta independiente -->
	<xsl:template match="srccite/citeinfo">
		<xsl:element name="CI_Citation">
			<!--  TITLE -->
			<xsl:element name="title">
				<xsl:value-of select="title"/>
			</xsl:element>
			<xsl:element name="alternateTitle">
				<xsl:value-of select="../../srccitea"/>
			</xsl:element>
			<!-- DATE -->
			<xsl:element name="date">
				<xsl:element name="CI_Date">
					<xsl:element name="date">
						<xsl:choose>
							<xsl:when test="./pubdate!=''">
								<xsl:value-of select="pubdate"/>
							</xsl:when>
							<xsl:otherwise>0001-01-01</xsl:otherwise>
						</xsl:choose>
					</xsl:element>
					<xsl:element name="dateType">
						<xsl:text>publication</xsl:text>
					</xsl:element>
				</xsl:element>
			</xsl:element>
			<!-- EDITION -->
			<xsl:if test="edition">
				<xsl:element name="edition">
					<xsl:value-of select="./edition"/>
				</xsl:element>
			</xsl:if>
			<!-- EDITIONDATE no tiene equivalente en FGDC -->
			<!-- IDENTIFIER -->
			<xsl:if test="citId">
				<xsl:element name="identifier">
					<xsl:element name="MD_Identifier">
						<xsl:element name="code">
							<xsl:value-of select="./citId"/>
						</xsl:element>
					</xsl:element>
				</xsl:element>
			</xsl:if>
			<!-- CITEDRESPONSIBLEPARTY -->
			<xsl:for-each select="origin">
				<xsl:element name="citedResponsibleParty">
					<xsl:element name="CI_ResponsibleParty">
						<xsl:element name="organisationName">
							<xsl:value-of select="."/>
						</xsl:element>
						<xsl:if test="/metadata/idinfo/citation/citeinfo[onlink]">
							<xsl:element name="contactInfo">
								<xsl:element name="CI_Contact">
									<xsl:element name="onlineResource">
										<xsl:element name="CI_OnlineResource">
											<xsl:element name="linkage">
												<xsl:value-of select="../onlink"/>
											</xsl:element>
										</xsl:element>
									</xsl:element>
								</xsl:element>
							</xsl:element>
						</xsl:if>
						<xsl:element name="role">
							<xsl:text>originator</xsl:text>
						</xsl:element>
					</xsl:element>
				</xsl:element>
			</xsl:for-each>
			<xsl:if test="/metadata/idinfo/citation/citeinfo[pubinfo]">
				<xsl:element name="citedResponsibleParty">
					<xsl:element name="CI_ResponsibleParty">
						<xsl:element name="organisationName">
							<xsl:value-of select="./pubinfo/publish"/>
						</xsl:element>
						<xsl:element name="contactInfo">
							<xsl:element name="CI_Contact">
								<xsl:element name="address">
									<xsl:element name="CI_Address">
										<xsl:element name="city">
											<xsl:value-of select="./pubinfo/pubplace"/>
										</xsl:element>
									</xsl:element>
								</xsl:element>
							</xsl:element>
						</xsl:element>
						<xsl:element name="role">
							<xsl:text>publisher</xsl:text>
						</xsl:element>
					</xsl:element>
				</xsl:element>
			</xsl:if>
			<!-- PRESENTATIONFORM -->
			<xsl:if test="geoform">
				<xsl:element name="presentationForm">
					<xsl:value-of select="./geoform"/>
				</xsl:element>
			</xsl:if>
			<!-- SERIES -->
			<xsl:if test="serinfo">
				<xsl:element name="series">
					<xsl:element name="CI_Series">
						<!-- los dos elementos de Series: CI_Series son opcionales -->
						<!-- pero son obligatorios sus correspondientes en serinfo -->
						<xsl:element name="name">
							<xsl:value-of select="./serinfo/sername"/>
						</xsl:element>
						<xsl:element name="issueIdentification">
							<xsl:value-of select="./serinfo/issue"/>
						</xsl:element>
					</xsl:element>
				</xsl:element>
			</xsl:if>
			<!-- OTHERCITATIONDETAILS-->
			<xsl:if test="othercit">
				<xsl:element name="otherCitationDetails">
					<xsl:value-of select="./othercit"/>
				</xsl:element>
			</xsl:if>
			<!-- ISBN -->
			<xsl:if test="isbn">
				<xsl:element name="ISBN">
					<xsl:value-of select="./isbn"/>
				</xsl:element>
			</xsl:if>
			<!-- ISSN -->
			<xsl:if test="issn">
				<xsl:element name="ISSN">
					<xsl:value-of select="./issn"/>
				</xsl:element>
			</xsl:if>
		</xsl:element>
		<!-- acaba citation -->
	</xsl:template>
	<xsl:template match="cntinfo">
		<xsl:element name="CI_ResponsibleParty">
			<!-- Esta plantilla se utiliza para mapear Contact, IdentificationInfo: MD_Identification/idPoc y Distributor:MD_Distributor/DistributorContact -->
			<xsl:choose>
				<xsl:when test="cntorgp">
					<xsl:if test="cntorgp/cntper">
						<xsl:element name="individualName">
							<xsl:value-of select="./cntorgp/cntper"/>
						</xsl:element>
					</xsl:if>
					<xsl:element name="organisationName">
						<xsl:value-of select="./cntorgp/cntorg"/>
					</xsl:element>
				</xsl:when>
				<xsl:when test="cntperp">
					<xsl:element name="individualName">
						<xsl:value-of select="./cntperp/cntper"/>
					</xsl:element>
					<xsl:if test="cntperp/cntorg">
						<xsl:element name="organisationName">
							<xsl:value-of select="./cntperp/cntorg"/>
						</xsl:element>
					</xsl:if>
				</xsl:when>
			</xsl:choose>
			<xsl:if test="cntpos">
				<xsl:element name="positionName">
					<xsl:value-of select="./cntpos"/>
				</xsl:element>
			</xsl:if>
			<xsl:element name="contactInfo">
				<xsl:element name="CI_Contact">
					<xsl:element name="phone">
						<xsl:element name="CI_Telephone">
							<xsl:for-each select="cntvoice">
								<xsl:element name="voice">
									<xsl:value-of select="."/>
								</xsl:element>
							</xsl:for-each>
							<xsl:for-each select="cntfax">
								<xsl:element name="facsimile">
									<xsl:value-of select="."/>
								</xsl:element>
							</xsl:for-each>
						</xsl:element>
					</xsl:element>
					<!-- cntAdress (ISO) es opcional (0..1) y cntaddr (FGDC) es obligatorio (1..n) -->
					<!-- por lo que se traslada siempre pero unicamente con el valor del primer cntaddr-->
					<!-- si es que hay mas de uno-->
					<xsl:element name="address">
						<xsl:element name="CI_Address">
							<!-- address es (0..n) y DeliveryPoint es (0..n) -->
							<xsl:for-each select="cntaddr/address">
								<xsl:element name="deliveryPoint">
									<xsl:value-of select="."/>
								</xsl:element>
							</xsl:for-each>
							<xsl:element name="city">
								<xsl:value-of select="cntaddr/City"/>
							</xsl:element>
							<xsl:element name="administrativeArea">
								<xsl:value-of select="cntaddr/state"/>
							</xsl:element>
							<xsl:element name="postalCode">
								<xsl:value-of select="cntaddr/postal"/>
							</xsl:element>
							<!-- cntaddr/Country y Address:CI_Address/Country son (0..1)-->
							<xsl:if test="cntaddr/Country">
								<xsl:element name="country">
									<xsl:value-of select="./cntaddr/Country"/>
								</xsl:element>
							</xsl:if>
							<!-- ambos son (0..n) -->
							<xsl:for-each select="cntemail">
								<xsl:element name="electronicMailAddress">
									<xsl:value-of select="."/>
								</xsl:element>
							</xsl:for-each>
						</xsl:element>
					</xsl:element>
					<!-- solo tiene sentido trasladar OnLineResource:CI_OnLineResource -->
					<!-- al mapear Citation, lo que se hace en una plantilla-->
					<!-- (citeinfo) en la que no se utiliza esta plantilla-->
					<!--ambos elementos son (0..1) -->
					<xsl:if test="hours">
						<xsl:element name="hoursOfService">
							<xsl:value-of select="./hours"/>
						</xsl:element>
					</xsl:if>
					<!--ambos elementos son (0..1) -->
					<xsl:if test="cntinst">
						<xsl:element name="contactInstructions">
							<xsl:value-of select="./cntinst"/>
						</xsl:element>
					</xsl:if>
				</xsl:element>
			</xsl:element>
			<!-- Tanto Contact/PositionName como cntinfo/cntpos son opcionales (0..1) -->
			<xsl:element name="role">
				<xsl:if test="name(..) = 'metc'">
					<xsl:text>publisher</xsl:text>
				</xsl:if>
				<xsl:if test="name(..) ='ptcontac'">
					<xsl:text>pointOfContact</xsl:text>
				</xsl:if>
				<xsl:if test="name(..) ='distrib'">
					<xsl:text>Distributor</xsl:text>
				</xsl:if>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<xsl:template match="availabl/timeinfo">
		<xsl:choose>
			<xsl:when test="sngdate">
				<xsl:if test="./sngdate/caldate">
					<xsl:value-of select="normalize-space(./sngdate/caldate)"/>
				</xsl:if>
				<xsl:if test="./sngdate/time!=''">
					<xsl:text> </xsl:text>
					<xsl:value-of select="normalize-space(./sngdate/time)"/>
				</xsl:if>
			</xsl:when>
			<xsl:when test="mdattim">
				<xsl:for-each select="./mdattim/sngdate">
					<xsl:value-of select="normalize-space(./caldate)"/>
					<xsl:text> </xsl:text>
					<xsl:value-of select="normalize-space(./time)"/>
					<xsl:if test="not (position()=last())">
						<xsl:text>, </xsl:text>
					</xsl:if>
				</xsl:for-each>
			</xsl:when>
			<xsl:when test="rngdates">
				<xsl:if test="./rngdates/begdate">
					<xsl:value-of select="normalize-space(./rngdates/begdate)"/>
				</xsl:if>
				<xsl:if test="./rngdates/begtime">
					<xsl:value-of select="normalize-space(./rngdates/begtime)"/>
				</xsl:if>
				<xsl:text> - </xsl:text>
				<xsl:if test="./rngdates/enddate">
					<xsl:value-of select="normalize-space(./rngdates/enddate)"/>
				</xsl:if>
				<xsl:if test="./rngdates/endtime">
					<xsl:value-of select="normalize-space(./rngdates/endtime)"/>
				</xsl:if>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="progress">
		<!-- En  el estandar fgdc el elemento progress contiene PCDATA (sin atributos), pero existen unos valores  -->
		<!-- aconsejados (Completed, In work y Planned), que se corresponden con 3 de los posibles atributos de status-->
		<!-- (completed, onGoing y planned, respectivamente)-->
		<!-- Con el nombre de este elemento ocurren algunas cosas raras: el las tablas del manual aparece como "Status"-->
		<!-- mientras que en el dtd aparece como status-->
		<xsl:element name="status">
			<xsl:choose>
				<xsl:when test="normalize-space(.)='Completed'">
					<xsl:text>completed</xsl:text>
				</xsl:when>
				<xsl:when test="normalize-space(.)='In work'">
					<xsl:text>onGoing</xsl:text>
				</xsl:when>
				<xsl:when test="normalize-space(.)='Planned'">
					<xsl:text>planned</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<!-- si el valor no es de los aconsejados, se traslada tal cual -->
					<xsl:value-of select="."/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<xsl:template match="update">
		<xsl:element name="resourceMaintenance">
			<xsl:element name="MD_MaintenanceInformation">
				<!-- En  el estandar fgdc el elemento update contiene PCDATA (sin atributos), pero existen unos valores  -->
				<!-- aconsejados (Continually,Daily,Weekly,Monthly,Annually,Unknown,As needed,Irregular,None planned),-->
				<!-- que se corresponden con 9 de los posibles atributos de mainFreq-->
				<xsl:element name="maintenanceAndUpdateFrequency">
					<xsl:choose>
						<xsl:when test="normalize-space(.)='Continually'">
							<xsl:text>continual</xsl:text>
						</xsl:when>
						<xsl:when test="normalize-space(.)='Daily'">
							<xsl:text>daily</xsl:text>
						</xsl:when>
						<xsl:when test="normalize-space(.)='Weekly'">
							<xsl:text>weekly</xsl:text>
						</xsl:when>
						<xsl:when test="normalize-space(.)='Monthly'">
							<xsl:text>monthly</xsl:text>
						</xsl:when>
						<xsl:when test="normalize-space(.)='Annually'">
							<xsl:text>annually</xsl:text>
						</xsl:when>
						<xsl:when test="normalize-space(.)='Unknown'">
							<xsl:text>unknown</xsl:text>
						</xsl:when>
						<xsl:when test="normalize-space(.)='As needed'">
							<xsl:text>asNeeded</xsl:text>
						</xsl:when>
						<xsl:when test="normalize-space(.)='Irregular'">
							<xsl:text>irregular</xsl:text>
						</xsl:when>
						<xsl:when test="normalize-space(.)='None planned'">
							<xsl:text>notPlanned</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<!-- si el valor no es uno de los anteriores, se traslada tal cual -->
							<xsl:value-of select="."/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:element>
				<!-- metainfo/metfrd se mapea con MetadataMaintenance:MaintenanceInformation/DateOfNextUpdate -->
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<xsl:template match="metfrd">
		<xsl:element name="metadataMaintenance">
			<xsl:element name="MD_MaintenanceInformation">
				<!-- El elemento MaintenanceAndUpdateFrequency es obligatorio pero se supone-->
				<!-- que en MetadataMaintenance:MaintenanceInformation se mapea DateOfNextUpdate, y en IdentificationInfo: MD_Identification/ResourceMaintenance:MD_MaintenanceInformation-->
				<!-- solo se mapea MaintenanceAndUpdateFrequency  -->
				<xsl:element name="maintenanceAndUpdateFrequency">
					<xsl:text>unknown</xsl:text>
				</xsl:element>
				<xsl:element name="dateOfNextUpdate">
					<xsl:value-of select="."/>
				</xsl:element>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<!--xsl:template match="idinfo/accconst | idinfo/useconst | idinfo/secinfo"-->
	<!-- se crea un ResourceConstraints:MD_constraints para los dos elementos(accconst y useconst), y no uno para cada uno-->
	<!--xsl:if test="name(.)='accconst' or name(.)='useconst'">
			<xsl:element name="MD_LegalConstraints">
				<xsl:if test="name(.)='accconst'">
					<xsl:element name="accessConstraints">
							<xsl:value-of select="."/>
					</xsl:element>
				</xsl:if>
				<xsl:if test="name(.)='useconst'">
					<xsl:element name="useConstraints">
							<xsl:value-of select="."/>
					</xsl:element>
				</xsl:if-->
	<!--xsl:element name="otherConstraints">
		      			<xsl:value-of select="./othconst"/>
				</xsl:element-->
	<!--/xsl:element>
				</xsl:if>
			
				<xsl:if test="name(.)='secinfo' ">
					<xsl:element name="MD_SecurityConstraints">
						<xsl:element name="classification">
							<xsl:value-of select="./secclass"/>
						</xsl:element>
						<xsl:element name="classificationSystem">
								<xsl:value-of select="./secsys"/>
						</xsl:element>
						<xsl:element name="handlingDescription">
							<xsl:value-of select="./sechandl"/>
						</xsl:element>
					</xsl:element>
				</xsl:if>
		
	</xsl:template-->
	<xsl:template match="refSysId  | projection | ellipsoid | datum | refSystem | mdGeoRefSys | mdTempRefSys">
		<xsl:element name="RS_Identifier">
			<xsl:if test="./identAuth/citeinfo">
				<xsl:element name="authority">
					<xsl:apply-templates select="./identAuth/citeinfo"/>
				</xsl:element>
			</xsl:if>
			<xsl:element name="code">
				<xsl:value-of select="./identCode"/>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<xsl:template match="refSysInfo">
		<xsl:for-each select="./refSystem">
			<xsl:element name="MD_ReferenceSystem">
				<xsl:element name="referenceSystemIdentifier">
					<xsl:apply-templates select="./refSysId"/>
				</xsl:element>
			</xsl:element>
		</xsl:for-each>
		<!-- Elementos desaparecidos... -->
		<xsl:for-each select="./mdGeoRefSys">
			<xsl:element name="MD_ReferenceSystem">
				<xsl:element name="referenceSystemIdentifier">
					<xsl:apply-templates select="./refSysId"/>
				</xsl:element>
			</xsl:element>
		</xsl:for-each>
		<xsl:for-each select="./mdTempRefSys">
			<xsl:element name="MD_ReferenceSystem">
				<xsl:element name="referenceSystemIdentifier">
					<xsl:apply-templates select="./refSysId"/>
				</xsl:element>
			</xsl:element>
		</xsl:for-each>
		<xsl:for-each select="./mdCoRefSys">
			<xsl:element name="MD_CRS">
				<xsl:if test="./refSysId">
					<xsl:element name="referenceSystemIdentifier">
						<xsl:apply-templates select="./refSysId"/>
					</xsl:element>
				</xsl:if>
				<xsl:if test="./projection">
					<xsl:element name="projection">
						<xsl:apply-templates select="./projection"/>
					</xsl:element>
				</xsl:if>
				<xsl:if test="./ellipsoid">
					<xsl:element name="ellipsoid">
						<xsl:apply-templates select="./ellipsoid"/>
					</xsl:element>
				</xsl:if>
				<xsl:if test="./datum">
					<xsl:element name="datum">
						<xsl:apply-templates select="./datum"/>
					</xsl:element>
				</xsl:if>
				<xsl:if test="./projParas">
					<!-- COMPLETAR -->
					<xsl:element name="projectionParameters">
						<xsl:element name="MD_ProjectionParameters">
							<xsl:if test="./projParas/zone">
								<xsl:element name="zone">
									<xsl:value-of select="./projParas/zone"/>
								</xsl:element>
							</xsl:if>
							<xsl:for-each select="./projParas/stanPara">
								<xsl:element name="standardParallel">
									<xsl:value-of select="."/>
								</xsl:element>
							</xsl:for-each>
							<xsl:if test="./projParas/longCntMer">
								<xsl:element name="longitudeOfCentralMeridian">
									<xsl:value-of select="./projParas/longCntMer"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="./projParas/latProjOri">
								<xsl:element name="latitudeOfProjectionOrigin">
									<xsl:value-of select="./projParas/latProjOri"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="./projParas/falEastng">
								<xsl:element name="falseEasting">
									<xsl:value-of select="./projParas/falEastng"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="./projParas/falNorthng">
								<xsl:element name="falseNorthing">
									<xsl:value-of select="./projParas/falNorthng"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="./projParas/falENUnits">
								<xsl:element name="falseEastingNorthingUnits">
									<!--xsl:element name="name"-->
									<xsl:value-of select="./projParas/falENUnits"/>
									<!--/xsl:element-->
								</xsl:element>
							</xsl:if>
							<xsl:if test="./projParas/sclFacEqu">
								<xsl:element name="scaleFactorAtEquator">
									<xsl:value-of select="./projParas/sclFacEqu"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="./projParas/hgProsPt">
								<xsl:element name="heightOfProspectivePointAboveSurface">
									<xsl:element name="./projParas/hgProsPt"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="./projParas/longProjCnt">
								<xsl:element name="longitudeOfProjectionCenter">
									<xsl:value-of select="./projParas/longProjCnt"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="./projParas/latProjCnt">
								<xsl:element name="latitudeOfProjectionCenter">
									<xsl:value-of select="./projParas/latProjCnt"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="./projParas/sclFacCnt">
								<xsl:element name="scaleFactorAtCenterLine">
									<xsl:value-of select="./projParas/sclFacCnt"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="./projParas/stVrLongPl">
								<xsl:element name="straightVerticalLongitudeFromPole">
									<xsl:value-of select="./projParas/stVrLongPl"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="./projParas/sclFacPrOr">
								<xsl:element name="scaleFactorAtProjectionOrigin">
									<xsl:value-of select="./projParas/sclFacPrOr"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="./projParas/ObLnAziPars">
								<xsl:element name="obliqueLineAzimuthParameter">
									<xsl:element name="MD_ObliqueLineAzimuth">
										<xsl:element name="azimuthAngle">
											<xsl:value-of select="./projParas/ObLnAziPars/AziAngle"/>
										</xsl:element>
										<xsl:element name="azimuthMeasurePointLongitude">
											<xsl:value-of select="./projParas/ObLnAziPars/AziPtLong"/>
										</xsl:element>
									</xsl:element>
								</xsl:element>
							</xsl:if>
							<xsl:for-each select="./projParas/ObLnPtPars">
								<xsl:element name="obliqueLinePointParameter">
									<xsl:element name="MD_ObliqueLinePoint">
										<xsl:element name="obliqueLineLatitude">
											<xsl:value-of select="./ObLineLat"/>
										</xsl:element>
										<xsl:element name="obliqueLineLongitude">
											<xsl:value-of select="./ObLineLong"/>
										</xsl:element>
									</xsl:element>
								</xsl:element>
							</xsl:for-each>
						</xsl:element>
					</xsl:element>
				</xsl:if>
				<xsl:if test="./ellParas">
					<xsl:element name="ellipsoidParameters">
						<xsl:element name="MD_EllipsoidParameters">
							<xsl:element name="semiMajorAxis">
								<xsl:value-of select="./ellParas/semiMajAx"/>
							</xsl:element>
							<xsl:element name="axisUnits">
								<!--xsl:element name="name"-->
								<xsl:value-of select="./ellParas/axisUnits"/>
								<!--/xsl:element-->
							</xsl:element>
							<xsl:if test="./ellParas/denFlatRat">
								<xsl:element name="denominatorOfFlatteningRatio">
									<xsl:value-of select="./ellParas/denFlatRat"/>
								</xsl:element>
							</xsl:if>
						</xsl:element>
					</xsl:element>
				</xsl:if>
			</xsl:element>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="eainfo">
		<xsl:for-each select="./detailed">
			<xsl:element name="MD_FeatureCatalogueDescription">
				<xsl:element name="includedWithDataset">false</xsl:element>
				<xsl:element name="featureTypes">
					<xsl:value-of select="./enttyp/enttypl"/>
				</xsl:element>
				<xsl:element name="featureCatalogueCitation">
					<xsl:element name="CI_Citation">
						<xsl:element name="title">unknown</xsl:element>
						<xsl:element name="date">
							<xsl:element name="CI_Date">
								<xsl:element name="date">0001-01-01</xsl:element>
								<xsl:element name="dateType"/>
							</xsl:element>
						</xsl:element>
					</xsl:element>
				</xsl:element>
			</xsl:element>
		</xsl:for-each>
		<xsl:for-each select="./../dataqual/cloud">
			<xsl:element name="MD_ImageDescription">
				<!--xsl:element name="MD_ContentInformation"-->
				<!--xsl:attribute name="xsi:type">MD_ImageDescription</xsl:attribute-->
				<xsl:element name="attributeDescription">
					<xsl:element name="typeName"/>
					<xsl:element name="attributeTypes">
						<xsl:element name="AttributeName"/>
						<xsl:element name="TypeName"/>
					</xsl:element>
				</xsl:element>
				<xsl:element name="contentType"/>
				<xsl:element name="cloudCoverPercentage">
					<xsl:value-of select="../../dataqual/cloud"/>
				</xsl:element>
			</xsl:element>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="dataqual">
		<xsl:element name="DQ_DataQuality">
			<!-- Incluyo scope/level porque es obligatorio.  lo dejo vacio -->
			<xsl:element name="scope">
				<xsl:element name="level"/>
			</xsl:element>
			<xsl:if test="./lineage">
				<xsl:element name="lineage">
					<xsl:element name="LI_Lineage">
						<xsl:if test="./lineage/srcinfo">
							<!--xsl:element name="source"-->
							<xsl:for-each select="./lineage/srcinfo">
								<xsl:element name="source">
									<xsl:element name="LI_Source">
										<!--Este elemento puede mapearse o bien con typesrc o bien con srccontr. -->
										<xsl:choose>
											<xsl:when test="./typesrc != ''">
												<xsl:element name="description">
													<xsl:value-of select="./typesrc"/>
												</xsl:element>
											</xsl:when>
											<xsl:when test="./srccontr !=''">
												<xsl:element name="description">
													<xsl:value-of select="./srccontr !=''"/>
												</xsl:element>
											</xsl:when>
										</xsl:choose>
										<xsl:if test="./srcscale">
											<xsl:element name="scaleDenominator">
												<xsl:element name="MD_RepresentativeFraction">
													<xsl:element name="denominator">
														<xsl:value-of select="./srcscale"/>
													</xsl:element>
												</xsl:element>
											</xsl:element>
										</xsl:if>
										<xsl:element name="sourceCitation">
											<xsl:apply-templates select="./srccite/citeinfo"/>
										</xsl:element>
										<xsl:element name="sourceExtent">
											<xsl:element name="EX_Extent">
												<xsl:element name="description">
													<xsl:value-of select="./srctime/srccurr"/>
												</xsl:element>
												<xsl:if test="./srctime/timeinfo/sngdate/caldate!='' or ./srctime/timeinfo/rngdates">
													<xsl:element name="temporalElement">
														<xsl:element name="EX_TemporalExtent">
															<xsl:element name="extent">
																<xsl:if test="./srctime/timeinfo/sngdate/caldate!=''">
																	<xsl:element name="instant">
																		<xsl:value-of select="./srctime/timeinfo/sngdate/caldate"/>
																	</xsl:element>
																</xsl:if>
																<xsl:if test="./srctime/timeinfo/rngdates">
																	<xsl:element name="beginEnd">
																		<xsl:element name="begin">
																			<xsl:value-of select="./srctime/timeinfo/rngdates/begdate"/>
																		</xsl:element>
																		<xsl:element name="end">
																			<xsl:value-of select="./srctime/timeinfo/rngdates/enddate"/>
																		</xsl:element>
																	</xsl:element>
																</xsl:if>
															</xsl:element>
														</xsl:element>
													</xsl:element>
												</xsl:if>
											</xsl:element>
										</xsl:element>
									</xsl:element>
								</xsl:element>
							</xsl:for-each>
							<!--/xsl:element-->
						</xsl:if>
						<xsl:if test="./lineage/procstep">
							<!--xsl:element name="processStep"-->
							<xsl:for-each select="./lineage/procstep">
								<xsl:element name="processStep">
									<xsl:element name="LI_ProcessStep">
										<!--xsl:element name="LI_ProcessStep"-->
										<xsl:element name="description">
											<xsl:value-of select="./procdesc"/>
										</xsl:element>
										<xsl:element name="dateTime">
											<xsl:value-of select="./procdate"/>
											<!--lo cojo del procdate y de momento me olvido del proctime -->
										</xsl:element>
										<xsl:element name="processor">
											<xsl:apply-templates select="./proccont/cntinfo"/>
											<!-- a ver con este...-->
										</xsl:element>
										<!-- si uno de los elementos srcused o srcprod existe, deben ser introducidos en un alternateTitle.
								Generar un source implica tambien generar un title y un date, que son elementos obligatorios-->
										<xsl:if test="./srcprod!='' or ./srcused!=''">
											<xsl:element name="source">
												<xsl:element name="LI_Source">
													<xsl:element name="sourceCitation">
														<xsl:element name="CI_Citation">
															<!--alternate Title puede venir de srcused o bien de srcprod.  Sin embargo, debe ser unico -->
															<!--Genero al vez el title -->
															<xsl:choose>
																<xsl:when test="./srcused !=''">
																	<xsl:element name="title">
																		<xsl:value-of select="./srcused"/>
																	</xsl:element>
																	<xsl:element name="alternateTitle">
																		<xsl:value-of select="./srcused"/>
																	</xsl:element>
																</xsl:when>
																<xsl:when test="./srcprod != ''">
																	<xsl:element name="title">
																		<xsl:value-of select="./srcprod"/>
																	</xsl:element>
																	<xsl:element name="alternateTitle">
																		<xsl:value-of select="./srcprod"/>
																	</xsl:element>
																</xsl:when>
															</xsl:choose>
															<!-- generacion del elemento obligatorio date-->
															<xsl:element name="date">
																<xsl:element name="CI_Date">
																	<xsl:element name="date">0001-01-01</xsl:element>
																	<xsl:element name="dateType"/>
																</xsl:element>
															</xsl:element>
														</xsl:element>
													</xsl:element>
												</xsl:element>
											</xsl:element>
										</xsl:if>
									</xsl:element>
								</xsl:element>
							</xsl:for-each>
							<!--/xsl:element-->
						</xsl:if>
					</xsl:element>
				</xsl:element>
			</xsl:if>
			<xsl:element name="report">
				<xsl:element name="DQ_ConceptualConsistency">
					<!--xsl:element name="DQ_LogicalConsistency"-->
					<!--xsl:attribute name="xsi:type">DQ_ConceptualConsistency</xsl:attribute-->
					<xsl:element name="evaluationMethodDescription">
						<xsl:value-of select="./logic"/>
					</xsl:element>
					<xsl:element name="result">
						<xsl:element name="DQ_QuantitativeResult">
							<!--xsl:element name="_DQ_Result">
							<xsl:attribute name="xsi:type">DQ_QuantitativeResult</xsl:attribute-->
							<xsl:element name="valueUnit">
								<xsl:element name="name"/>
							</xsl:element>
							<xsl:element name="value">
								<xsl:element name="recordValue">
									<xsl:element name="attributes">
										<xsl:element name="AttributeName"/>
										<xsl:element name="Any"/>
									</xsl:element>
									<xsl:element name="recordType"/>
								</xsl:element>
							</xsl:element>
						</xsl:element>
					</xsl:element>
				</xsl:element>
			</xsl:element>
			<xsl:element name="report">
				<xsl:element name="DQ_QuantitativeAttributeAccuracy">
					<!--xsl:element name="DQ_ThematicAccuracy">
					<xsl:attribute name="xsi:type">DQ_QuantitativeAttributeAccuracy</xsl:attribute-->
					<xsl:element name="measureDescription">
						<xsl:value-of select="./attracc/qattracc/attracce"/>
					</xsl:element>
					<xsl:element name="evaluationMethodDescription">
						<xsl:value-of select="./attracc/attraccr"/>
					</xsl:element>
					<xsl:element name="result">
						<xsl:element name="DQ_QuantitativeResult">
							<!--xsl:element name="_DQ_Result">
							<xsl:attribute name="xsi:type">DQ_QuantitativeResult</xsl:attribute-->
							<xsl:element name="valueUnit">
								<xsl:element name="name"/>
							</xsl:element>
							<xsl:element name="value">
								<xsl:element name="recordValue">
									<xsl:element name="attributes">
										<xsl:element name="AttributeName">
											<xsl:value-of select="./attracc/qattracc/attraccv"/>
										</xsl:element>
										<xsl:element name="Any"/>
									</xsl:element>
									<xsl:element name="recordType"/>
								</xsl:element>
							</xsl:element>
						</xsl:element>
					</xsl:element>
				</xsl:element>
			</xsl:element>
			<xsl:element name="report">
				<xsl:element name="DQ_CompletenessCommission">
					<!--xsl:element name="DQ_Completeness">
					<xsl:attribute name="xsi:type">DQ_CompletenessCommission</xsl:attribute-->
					<xsl:element name="evaluationMethodDescription">
						<xsl:value-of select="./complete"/>
					</xsl:element>
					<xsl:element name="result">
						<xsl:element name="DQ_QuantitativeResult">
							<!--xsl:element name="_DQ_Result">
							<xsl:attribute name="xsi:type">DQ_QuantitativeResult</xsl:attribute-->
							<xsl:element name="valueUnit">
								<xsl:element name="name"/>
							</xsl:element>
							<xsl:element name="value">
								<xsl:element name="recordValue">
									<xsl:element name="attributes">
										<xsl:element name="AttributeName"/>
										<xsl:element name="Any"/>
									</xsl:element>
									<xsl:element name="recordType"/>
								</xsl:element>
							</xsl:element>
						</xsl:element>
					</xsl:element>
				</xsl:element>
			</xsl:element>
			<!--elementos horizontal y vertical.  En fgdc solo hay un elemento de cada, por lo que lo testeo con un if y no con un forEach -->
			<xsl:if test="./posacc/horizpa">
				<xsl:element name="report">
					<xsl:element name="DQ_AbsoluteExternalPositionalAccuracy">
						<!--xsl:element name="DQ_PositionalAccuracy">
						<xsl:attribute name="xsi:type">DQ_AbsoluteExternalPositionalAccuracy</xsl:attribute-->
						<xsl:element name="measureDescription">
							<xsl:value-of select="./posacc/horizpa/qhorizpa/horizpae"/>
						</xsl:element>
						<xsl:element name="evaluationMethodDescription">
							<xsl:value-of select="./posacc/horizpa/horizpar"/>
						</xsl:element>
						<xsl:element name="result">
							<xsl:element name="DQ_QuantitativeResult">
								<!--xsl:element name="_DQ_Result">
								<xsl:attribute name="xsi:type">DQ_QuantitativeResult</xsl:attribute-->
								<xsl:element name="valueUnit">
									<xsl:element name="name"/>
								</xsl:element>
								<xsl:element name="value">
									<xsl:element name="recordValue">
										<xsl:element name="attributes">
											<xsl:element name="AttributeName"/>
											<xsl:element name="Any"/>
										</xsl:element>
										<xsl:element name="recordType"/>
									</xsl:element>
								</xsl:element>
							</xsl:element>
						</xsl:element>
						<xsl:element name="nameOfMeasure">horizontal</xsl:element>
					</xsl:element>
				</xsl:element>
			</xsl:if>
			<xsl:if test="./posacc/vertacc">
				<xsl:element name="report">
					<xsl:element name="DQ_AbsoluteExternalPositionalAccuracy">
						<!--xsl:element name="DQ_PositionalAccuracy">
						<xsl:attribute name="xsi:type">DQ_AbsoluteExternalPositionalAccuracy</xsl:attribute-->
						<xsl:element name="measureDescription">
							<xsl:value-of select="./posacc/vertacc/qvertpa/vertacce"/>
						</xsl:element>
						<xsl:element name="evaluationMethodDescription">
							<xsl:value-of select="./posacc/vertacc/vertaccr"/>
						</xsl:element>
						<xsl:element name="result">
							<xsl:element name="DQ_QuantitativeResult">
								<!--xsl:element name="_DQ_Result">
								<xsl:attribute name="xsi:type">DQ_QuantitativeResult</xsl:attribute-->
								<xsl:element name="valueUnit">
									<xsl:element name="name"/>
								</xsl:element>
								<xsl:element name="value">
									<xsl:element name="recordValue">
										<xsl:element name="attributes">
											<xsl:element name="AttributeName"/>
											<xsl:element name="Any"/>
										</xsl:element>
										<xsl:element name="recordType"/>
									</xsl:element>
								</xsl:element>
							</xsl:element>
						</xsl:element>
						<xsl:element name="nameOfMeasure">vertical</xsl:element>
					</xsl:element>
				</xsl:element>
			</xsl:if>
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>
