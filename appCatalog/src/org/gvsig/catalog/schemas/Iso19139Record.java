package org.gvsig.catalog.schemas;

import java.net.URI;

import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.metadataxml.XMLTree;
import org.gvsig.catalog.querys.Coordinates;


public class Iso19139Record extends Record{
	 
	public  Iso19139Record() {    
		 
	 }
	 
	/**
	 * Constructor
	 * @param node Node with the answer
	 * @param serverURI Server URL. Necessary to load the image (just Geonetwork)
	 */
	    public  Iso19139Record(URI uri, XMLNode node) {        
	        super(uri,node);
	        setTitle(node.searchNodeValue(
	                "identificationInfo->MD_DataIdentification->citation->CI_Citation->title->CharacterString"));
	        setAbstract_(node.searchNodeValue("identificationInfo->MD_DataIdentification->abstract->gco:CharacterString"));
	        setPurpose(node.searchNodeValue("dataIdInfo->idPurp"));
	        setKeyWords(node.searchMultipleNodeValue(
	                "identificationInfo->MD_DataIdentification->descriptiveKeywords->MD_Keywords->keyword->gco:CharacterString"));
	        setResources(getResources("distributionInfo->MD_Distribution->transferOptions->MD_DigitalTransferOptions->onLine->CI_OnlineResource"));
	        setFileID(node.searchNodeValue("mdFileID"));
	        setImageURL(node.searchNodeValue("dataIdInfo->graphOver"));
	        
	    } 

	/**
	 * 
	 * 
	 * 
	 * @return 
	 * @param label 
	 */
	    private Resource[] getResources(String label) {        
	        XMLNode[] nodes = XMLTree.searchMultipleNode(getNode(), label);
	        Coordinates coordinates = null;
	        String srs = null;
	        
	        if (nodes == null) {
	            return null;
	        }
	        Resource[] resources = new Resource[nodes.length];
	        if (nodes.length > 0){
	        	srs = XMLTree.searchNodeValue(getNode(),"refSysInfo->MdCoRefSys->refSysID->identCode");
	        	coordinates = new Coordinates(XMLTree.searchNodeValue(getNode(),"dataIdInfo->geoBox->westBL"),
	        			XMLTree.searchNodeValue(getNode(),"dataIdInfo->geoBox->northBL"),
						XMLTree.searchNodeValue(getNode(),"dataIdInfo->geoBox->eastBL"),
						XMLTree.searchNodeValue(getNode(),"dataIdInfo->geoBox->southBL"));
	        }
	        	
	        	
	        for (int i = 0; i < resources.length; i++){
	            	resources[i] = new Resource(XMLTree.searchNodeValue(nodes[i],
	                        "linkage->URL"),
	                    XMLTree.searchNodeValue(nodes[i], "protocol->gco:CharacterString"),
	                    XMLTree.searchNodeValue(nodes[i], "name->gco:CharacterString"),
	                    XMLTree.searchNodeValue(nodes[i], "orDesc"),
	                    XMLTree.searchNodeAtribute(nodes[i], "orFunct->OnFunctCd",
	                        "value"),
						srs,	
	            		coordinates);
	            	if (resources[i].getLinkage() == null){
	            		resources[i].setLinkage("");
	            	}
	            	
	        }
	        
	        
	        return resources;
	    }

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.schemas.discoverer.Record#accept(java.net.URI, es.gva.cit.catalogClient.metadataxml.XMLNode)
	 */
	public boolean accept(URI uri, XMLNode node) {
		if (node.getName().endsWith("MD_Metadata")){
			if (node.searchNode("identificationInfo->MD_DataIdentification->citation->CI_Citation->title")!=null){
				return true;
			}
		}
		return false;
	}     
	  
}
