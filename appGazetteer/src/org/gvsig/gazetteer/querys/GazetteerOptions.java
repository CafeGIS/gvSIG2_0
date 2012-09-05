
/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
*
* For more information, contact:
*
*  Generalitat Valenciana
*   Conselleria d'Infraestructures i Transport
*   Av. Blasco Ibáñez, 50
*   46010 VALENCIA
*   SPAIN
*
*      +34 963862235
*   gvsig@gva.es
*      www.gvsig.gva.es
*
*    or
*
*   IVER T.I. S.A
*   Salamanca 50
*   46005 Valencia
*   Spain
*
*   +34 963163400
*   dac@iver.es
*/
package org.gvsig.gazetteer.querys;

/**
 * This class is used to manage the aspect and the
 * filter of the gazetteer.
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class GazetteerOptions {
	private AspectOptions aspect = null;
	private InteligentSearchOptions search = null;
	
	public GazetteerOptions(){
		aspect = new AspectOptions();
		search = new InteligentSearchOptions();
	}
	
	public AspectOptions getAspect() {
		return aspect;
	}

	public void setAspect(AspectOptions aspect) {
		this.aspect = aspect;
	}

	public InteligentSearchOptions getSearch() {
		return search;
	}

	public void setSearch(InteligentSearchOptions search) {
		this.search = search;
	}
	
	/**
	 * This class contains all the options to show the
	 * gazetteer search on the gvSIG view
	 * @author Jorge Piera Llodrá (piera_jor@gva.es)
	 */
	public class AspectOptions{
		private boolean goTo = false;
        private boolean keepOld = false;
        private boolean paintCurrent = false;
        
		public AspectOptions() {
			super();
		}
		
        public AspectOptions(boolean goTo, boolean keepOld, boolean paintCurrent) {
			super();
			// TODO Auto-generated constructor stub
			this.goTo = goTo;
			this.keepOld = keepOld;
			this.paintCurrent = paintCurrent;
		}
        
		public boolean isGoTo() {
			return goTo;
		}
		
		public void setGoTo(boolean goTo) {
			this.goTo = goTo;
		}
		
		public boolean isKeepOld() {
			return keepOld;
		}
		public void setKeepOld(boolean keepOld) {
			this.keepOld = keepOld;
		}
		
		public boolean isPaintCurrent() {
			return paintCurrent;
		}
		
		public void setPaintCurrent(boolean paintCurrent) {
			this.paintCurrent = paintCurrent;
		}


        
	}
	
	/**
	 * This class contains all the options to realize
	 * the inteligent gazetteer search
	 * @author Jorge Piera Llodrá (piera_jor@gva.es)
	 */
	public class InteligentSearchOptions{
		private boolean withAccents = false;

		public InteligentSearchOptions() {
			super();
		}

		public InteligentSearchOptions(boolean withAccents) {
			super();
			// TODO Auto-generated constructor stub
			this.withAccents = withAccents;
		}

		public boolean isWithAccents() {
			return withAccents;
		}

		public void setWithAccents(boolean withAccents) {
			this.withAccents = withAccents;
		}
		
	}



}
