/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 */

/*
 * AUTHORS (In addition to CIT):
 * 2009 {Iver T.I.}   {Task}
 */

package com.iver.cit.gvsig.gui.panels.model;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;

import org.gvsig.fmap.dal.serverexplorer.wfs.WFSServerExplorer;
import org.gvsig.remoteClient.wfs.WFSFeature;
import org.gvsig.remoteClient.wfs.exceptions.WFSException;

import com.iver.utiles.StringComparator;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class WFSSelectedFeatureManager {
	private WFSServerExplorer serverExplorer = null;

	//To keep the array of features
	private Hashtable hashFeatures;
	private WFSSelectedFeature[] featuresList;
	
	private static HashMap featureManagers = new HashMap();
	
	public static WFSSelectedFeatureManager getInstance(WFSServerExplorer serverExplorer){
		if (featureManagers.containsKey(serverExplorer)){
			return (WFSSelectedFeatureManager)featureManagers.get(serverExplorer);
		}
		WFSSelectedFeatureManager selectedFeatureManager = new WFSSelectedFeatureManager(serverExplorer);
		featureManagers.put(serverExplorer, selectedFeatureManager);
		return selectedFeatureManager;
	}
	
	
	/**
	 * @param serverExplorer
	 */
	private WFSSelectedFeatureManager(WFSServerExplorer serverExplorer) {
		super();
		this.serverExplorer = serverExplorer;
	}
	
	public WFSSelectedFeature getFeatureInfo(String nameSpace, String layerName){
		if (hashFeatures == null){
			getLayerList();
		}
		WFSSelectedFeature selectedFeature = (WFSSelectedFeature)hashFeatures.get(layerName);
		try {
			WFSFeature feature = serverExplorer.getFeatureInfo(nameSpace, layerName);
			selectedFeature.setFeature(feature);
		} catch (WFSException e) {
			// The feature doesn't has fields
			e.printStackTrace();
		}		
		return selectedFeature;		
	}

	public WFSSelectedFeature[] getLayerList(){
		if (hashFeatures == null) {
			hashFeatures = new Hashtable();
			Hashtable wfsFeatures  = serverExplorer.getFeatures();

			StringComparator stringComparator = new StringComparator();
			// Set spanish rules and with case sensitive
			Collator collator = Collator.getInstance(new Locale("es_ES"));		
			stringComparator.setLocaleRules(stringComparator.new LocaleRules(true, collator));
			stringComparator.setCaseSensitive(false);

			ArrayList keysList = new ArrayList(wfsFeatures.keySet());
			Collections.sort(keysList, stringComparator);

			Iterator keys = keysList.iterator();
			featuresList = new WFSSelectedFeature[wfsFeatures.size()];

			for (int i=0 ; i<wfsFeatures.size() ; i++){
				WFSFeature feature = (WFSFeature)wfsFeatures.get(keys.next());
				WFSSelectedFeature selectedFeature = new WFSSelectedFeature(feature);
				featuresList[i] = selectedFeature;
				hashFeatures.put(selectedFeature.getName(), selectedFeature);
			}
		}
		return featuresList;
	}

	
}

