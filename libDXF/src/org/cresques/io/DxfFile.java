/*
 * Cresques Mapping Suite. Graphic Library for constructing mapping applications.
 *
 * Copyright (C) 2004-5.
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
 * cresques@gmail.com
 */
package org.cresques.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.cresques.geo.Projected;
import org.cresques.px.Extent;
import org.cresques.px.IObjList;
import org.cresques.px.dxf.DxfEntityMaker;
import org.cresques.px.dxf.DxfHeaderManager;
import org.cresques.px.dxf.DxfHeaderVariables;

/**
 * Clase que representa un fichero en formato DXF. Contiene los interfaces y métodos
 * necesarios para acceder a la información almacenada en su interior.
 *
 * @author jmorell
 */
public class DxfFile extends GeoFile {

	private boolean cadFlag = true;

	long lineNr = 0;

	String buf = null;

	BufferedReader fi;
	long l = 0;
	int count = 0;
	DxfGroup grp = null;

	EntityFactory entityMaker = null;
	VarSettings headerManager;
    private boolean dxf3DFlag;

	/**
	 * Crea los objetos en el Modelo correspondiente.
	 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
	 */
	public interface EntityFactory extends Projected {

		/**
         * Permite saber si se están añadiendo elementos a un bloque
         * @param booleano que indica si se están añadiendo elementos a un bloque
		 */
        public void setAddingToBlock(boolean a);

        /**
         * Crea una nueva capa partiendo de la información almacenada en el DXF
         * @param DxfGroupVector con información para la construcción de la nueva capa
         * @throws Exception
         */
		public void createLayer(DxfGroupVector v) throws Exception ;

        /**
         * Crea una nueva polilínea partiendo de la información almacenada en el DXF
         * @param DxfGroupVector con información para la construcción de la nueva polilínea
         * @throws Exception
         */
		public void createPolyline(DxfGroupVector v) throws Exception ;

        /**
         * Añade un vértice a la polilínea que se está creando
         * @param DxfGroupVector con la información necesaria para la adición del vértice
         * @throws Exception
         */
		public void addVertex(DxfGroupVector v) throws Exception ;

        /**
         * Fin de secuencia
         * @throws Exception
         */
        public void endSeq() throws Exception ;

        /**
         * Crea una nueva LwPolyline partiendo de la información almacenada en el DXF
         * @param DxfGroupVector con información para la construcción de la nueva polilínea
         * @throws Exception
         */
		public void createLwPolyline(DxfGroupVector v) throws Exception ;

        /**
         * Crea una nueva línea partiendo de la información almacenada en el DXF
         * @param DxfGroupVector con información para la construcción de la nueva línea
         * @throws Exception
         */
        public void createLine(DxfGroupVector v) throws Exception ;

        /**
         * Crea un nuevo texto partiendo de la información almacenada en el DXF
         * @param DxfGroupVector con información para la construcción del nuevo texto
         * @throws Exception
         */
        public void createText(DxfGroupVector v) throws Exception ;

        /**
         * Crea un nuevo MText partiendo de la información almacenada en el DXF
         * @param DxfGroupVector con información para la construcción del nuevo MText
         * @throws Exception
         */
        public void createMText(DxfGroupVector v) throws Exception ;

        /**
         * Crea un nuevo punto partiendo de la información almacenada en el DXF
         * @param DxfGroupVector con información para la construcción del nuevo punto
         * @throws Exception
         */
        public void createPoint(DxfGroupVector v) throws Exception ;

        /**
         * Crea un nuevo círculo partiendo de la información almacenada en el DXF
         * @param DxfGroupVector con información para la construcción del nuevo círculo
         * @throws Exception
         */
        public void createCircle(DxfGroupVector v) throws Exception ;

        /**
         * Crea una nueva elipse partiendo de la información almacenada en el DXF
         * @param DxfGroupVector con información para la construcción de la nueva elipse
         * @throws Exception
         */
        public void createEllipse(DxfGroupVector v) throws Exception ;

        /**
         * Crea un nuevo arco partiendo de la información almacenada en el DXF
         * @param DxfGroupVector con información para la construcción del nuevo arco
         * @throws Exception
         */
        public void createArc(DxfGroupVector v) throws Exception ;

        /**
         * Crea un nuevo punto de inserción partiendo de la información almacenada en el DXF
         * @param DxfGroupVector con información para la construcción del nuevo punto de inserción
         * @throws Exception
         */
        public void createInsert(DxfGroupVector v) throws Exception ;

        /**
         * Crea un nuevo sólido 2D partiendo de la información almacenada en el DXF
         * @param DxfGroupVector con información para la construcción del nuevo sólido
         * @throws Exception
         */
        public void createSolid(DxfGroupVector v) throws Exception ;

        /**
         * Crea un nuevo Spline partiendo de la información almacenada en el DXF
         * @param DxfGroupVector con información para la construcción del nuevo Spline
         * @throws Exception
         */
        public void createSpline(DxfGroupVector v) throws Exception ;

        /**
         * Construye la definición de un nuevo atributo partiendo de la información almacenada en el DXF
         * @param DxfGroupVector con información para la construcción de la definición del nuevo atributo
         * @throws Exception
         */
        public void createAttdef(DxfGroupVector v) throws Exception ;

        /**
         * Crea un nuevo atributo partiendo de la información almacenada en el DXF
         * @param DxfGroupVector con información para la creación del nuevo atributo
         * @throws Exception
         */
        public void createAttrib(DxfGroupVector v) throws Exception ;

        /**
         * Crea un bloque
         * @param DxfGroupVector con información para la creación del nuevo elemento
         * @throws Exception
         */
        public void createBlock(DxfGroupVector v) throws Exception ;

        /**
         * Fin de la definición de un bloqe
         * @param DxfGroupVector con información referente al final de un bloque
         * @throws Exception
         */
        public void endBlk(DxfGroupVector v) throws Exception ;

        /**
         * Gestiona los bloques que no se han tratado en la primera vuelta
         */
        void testBlocks();

        /**
         * Devuelve el extent
         * @return el extent
         */
        public Extent getExtent();

        /**
         * Devuelve la lista de bloques
         * @return la lista de bloques
         */
        public Vector getBlkList();

		/**
         * Permite la gestión de los atributos almacenados en unn DXF
         * @return un Vector con la lista de atributos
		 */
        public Vector getAttributes();

        /**
         * Borra los atributos repetidos
         */
        public void depureAttributes();

        /**
         * Devuelve los objetos almacenados en el DXF
         * @return IObjList con los objetos procedentes del DXF
         */
        public IObjList getObjects();

        /**
         * Permite saber si se trata de un fichero DXF en 2D o en 3D
         * @return booleano que indica si se trata de un fichero DXF 3D
         */
        public boolean isDxf3DFile();
	};

	/**
	 * Establece el estado de las variables propias de un DXF que están definidas
	 * en la sección HEADER. Por ejemplo, la versión del DXF.
	 * @author jmorell (jose.morell@gmail.com)
	 * @version 15-dic-2004
	 */
	public interface VarSettings {

		/**
		 * Establece la versión de Autocad en la que fue generado el DXF.
		 * @param información de base
		 * @throws Exception
		 */
		public void setAcadVersion(DxfGroupVector v) throws Exception ;

		/**
		 * Devuelve la versión de Autocad en la que fue generado el DXF.
		 * @return
		 */
		public String getAcadVersion();

		/**
		 * Devuelve el estado de las variables propias de un DXF
		 * @return
		 */
		public DxfHeaderVariables getDxfHeaderVars();
        public boolean isWritedDxf3D();
        public void loadMinZFromHeader(double d);
        public void loadMaxZFromHeader(double d);
	};

    /**
     * Constructor de la clase
     * @param proj, la proyección cartográfica
     * @param name, el path absoluto hasta el fichero DXF
     * @param maker, el interface que permite la construcción de las entidades procedentes del DXF
     */
	public DxfFile(IProjection proj, String name, EntityFactory maker) {
		super(proj, name);
		entityMaker = maker;
		headerManager = new DxfHeaderManager();
	}

    /**
     * Constructor de la clase que además incorpora la capacidad de leer una porción del HEADER
     * @param proj, la proyección cartográfica
     * @param name, el path absoluto hasta el fichero DXF
     * @param maker, el interface que permite la construcción de las entidades procedentes del DXF
     * @param dxfVars, el interface que permite la lectura del HEADER de un DXF
     */
    public DxfFile(IProjection proj, String name, EntityFactory maker, VarSettings dxfVars) {
		super(proj, name);
		entityMaker = maker;
		headerManager = dxfVars;
	}

	/**
	 * Carga un fichero en formato DXF
	 * @throws Exception
	 */
	public GeoFile load() throws Exception {
		System.out.println("Dxf: Cargando " + name + " ...");
		if (ZipFileFolder.isUrl(name)) {
			ZipFileFolder zFolder = new ZipFileFolder(name);
			InputStream is = zFolder.getInputStream(name);
			return load(new InputStreamReader(is));
		} else
			return load(new FileReader(name));

	}

    /**
     * Carga un fichero en formato DXF tomando un Reader como parámetro
     * @param fr, Reader que se le pasa como parámetro
     * @return devuelve un objeto GeoFile, padre de DxfFile
     * @throws Exception
     * @throws Exception
     */
	public GeoFile load(Reader fr) throws Exception {
		System.out.println("Dxf: Cargando '"+name+"' ...");
		fi = new BufferedReader(fr);
		while ((grp = readGrp()) != null) {
			l+=2;

			if (grp.equals(0, "EOF"))		break;
			if (grp.equals(0, "SECTION"))
				readSection();
		}
		fi.close();
		extent.add(entityMaker.getExtent());
		System.out.println("Dxf: '"+name+"' cargado. ("+l+" líneas).");
		this.lineNr = l;
		return this;
	}

    /**
     * El fichero DXF se divide en grupos. Este método permite leer cada grupo individualmente
     * @return devuelve la información del DXF en forma de objetos de la clase DxfGroup
     * @throws NumberFormatException
     * @throws IOException
     */
	private DxfGroup readGrp() throws NumberFormatException, IOException {
		DxfGroup g = DxfGroup.read(fi);
		if (g != null) l += 2;
		/*if (g.code == 8)
			if (((String) g.data).length() < 1) {
				System.err.println("a que un layer no puede ser ''?");
				System.exit(1);
			}*/
		return g;
	}

    /**
     * El fichero DXF se divide en varias secciones. Este método se encarga de leer cada
     * una de ellas
     * @throws NumberFormatException
     * @throws Exception
     */
	private void readSection() throws NumberFormatException, Exception {
		while (true) {
			grp = readGrp(); System.out.print("-1:"+grp);
			if (grp.code == 2)
				if (((String) grp.data).compareTo("HEADER") == 0)
					readHeader();
				else if (((String) grp.data).compareTo("CLASSES") == 0)
					readAnySection();
				else if (((String) grp.data).compareTo("TABLES") == 0)
					readTables();
				else if (((String) grp.data).compareTo("BLOCKS") == 0)
					readBlocks();
				else if (((String) grp.data).compareTo("ENTITIES") == 0)
					readEntities();
				else if (((String) grp.data).compareTo("OBJECTS") == 0)
					readAnySection();
				else {
					System.out.println("DxfRead: Seccion "+grp.data);
					readAnySection();
				}
			else
				System.err.println("Dxf: Codigo/Seccion desconocidos" + grp);
			if (grp.equals(0, "EOF")) break;
			if (grp.equals(0, "ENDSEC")) break;
		}
	}

    /**
     * Habilita la lectura de la sección de TABLES
     * @throws NumberFormatException
     * @throws Exception
     */
	private void readTables() throws NumberFormatException, Exception {
		System.out.println("Dxf: Seccion TABLAS, linea "+ l+ "grp ="+ grp);
		int layerCnt = 0;
		String tableAct = "NONAME";

		Hashtable tables = new Hashtable();
		Vector table = new Vector();
		DxfGroupVector v = new DxfGroupVector();

		grp = readGrp();// System.out.print("0:"+grp);
		while (true) {
			if (grp.code == 0) {
				String data = (String) grp.getData();
				if (data.compareTo("ENDSEC") == 0 || data.compareTo("EOF") == 0)
					break;
				else if (data.compareTo("ENDTAB") == 0) {
					tables.put(tableAct, table);
					table = new Vector();
					grp = readGrp();// System.out.print("1:"+grp);

					/**/if (tableAct.compareTo("LAYER") == 0 && v.size()>0) {
						entityMaker.createLayer(v);
						System.out.println("Dxf: Layer "+v.getDataAsString(2));
						layerCnt++;
						v.clear();
					}/**/
					continue;
				} else {
					if (table.size()==1) {
						tableAct = v.getDataAsString(2);
						System.out.println("Dxf: Tabla "+tableAct);
					} else
						if (tableAct.compareTo("LAYER") == 0 && v.size()>0) {
							entityMaker.createLayer(v);
							System.out.println("Dxf: Layer "+v.getDataAsString(2));
							layerCnt++;
						}

					v.clear();
					v.add(grp);
				}
				while (true) {
					grp = readGrp();// System.out.print("2:"+grp);
					if (grp.code == 0)	break;
					v.add(grp);
				}
				table.add(v);
			} else {
				System.err.println("Dxf: Error de secuencia");
				grp = readGrp(); //System.out.print("3:"+grp);
			}
		}
		System.out.println("Dxf: Seccion TABLAS: " + layerCnt + " Capas. ");
	}

    /**
     * Método de lectura de sección por defecto. Se utiliza mientras se realiza la
     * implementación correspondiente
     * @throws NumberFormatException
     * @throws IOException
     */
	private void readAnySection() throws NumberFormatException, IOException {
		System.out.println("Dxf: Seccion '"+((String) grp.getData())+"', linea "+ l);
		while (true) {
			grp = readGrp();
			if (grp.equals(0, "ENDSEC")) break;
			else if (grp.equals(0, "EOF")) break;
		}
	}

	/**
	 * Primera aproximación a la implementación de la lectura del HEADER. En principio
	 * interesa que se lea la versión del DXF.
	 * Para implementar esta parte del lector se ha optado por incluir el método
	 * setAcadVersion en el interface EntityFactory. A lo mejor conviene implementar
	 * un nuevo interface VarSettings.
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	private void readHeader() throws NumberFormatException, Exception {
		System.out.println("Dxf: Seccion HEADER, linea "+ l);
		int variableCnt = 0;
		int cntVeces = 0;
		DxfGroupVector v = new DxfGroupVector();
		grp = readGrp();
		while (true) {
			if (grp.equals(0, "EOF")) {
				break;
			}
			else if (grp.code==9 || grp.code==0) {
				if (v.size() > 0) {
					String lastVariable = (String) ((DxfGroup) v.get(0)).data;
					//System.out.println(lastVariable);
					if (lastVariable.compareTo("$ACADVER") == 0) {
						//System.out.println("Aqui llega.");
						headerManager.setAcadVersion(v);
					} else if (lastVariable.compareTo("$EXTMIN") == 0) {
						if(v.hasCode(3))
							headerManager.loadMinZFromHeader(((Double)((DxfGroup) v.get(3)).data).doubleValue());
                    } else if (lastVariable.compareTo("$EXTMAX") == 0) {
                    	if(v.hasCode(3))
                    		headerManager.loadMaxZFromHeader(((Double)((DxfGroup) v.get(3)).data).doubleValue());
                    } else if (lastVariable.compareTo("ENDSEC") == 0) {
                        //System.out.println("Llega al ENDSEC.");
                        break;
					} /*else
						System.err.println("Dxf: Variable "+lastVariable+" desconocida.");*/
				}
				v.clear();
				v.add(grp);
				while (true) {
					grp = readGrp();
					if (grp.code == 9 || grp.code==0)	break;
					v.add(grp);
				}
				variableCnt++;
			}
			cntVeces++;
		}
		System.out.println("Dxf: Seccion HEADER, " + variableCnt + " variables, "+ cntVeces + " veces.");
		//System.out.println("Seccion HEADER, linea "+ l+ " (SALGO)");
		System.out.println("readHeader: ACAD Version: " + headerManager.getDxfHeaderVars().getAcadVersion());
	}

    /**
     * Permite leer la sección ENTITIES del DXF, donde se encuentran las entidades
     * geométricas del DXF que no aparecen dentro de ningún bloque
     * @throws NumberFormatException
     * @throws Exception
     */
	private void readEntities() throws NumberFormatException, Exception {
		System.out.println("Dxf: Seccion ENTITIES, linea "+ l);
		int entityCnt = 0;
		int cntVeces = 0;
		DxfGroupVector v = new DxfGroupVector();
		grp = readGrp();
		while (true) {
			if (grp.equals(0, "EOF")) break;
			else if (grp.code == 0) {
				if (v.size() > 0) {
					String lastEntity = (String) ((DxfGroup) v.get(0)).data;
					if (lastEntity.compareTo("POLYLINE") == 0) {
						entityMaker.createPolyline(v);
					} else if (lastEntity.compareTo("VERTEX") == 0) {
						entityMaker.addVertex(v);
					} else if (lastEntity.compareTo("SEQEND") == 0) {
						entityMaker.endSeq();
					} else if (lastEntity.compareTo("LWPOLYLINE") == 0) {
						entityMaker.createLwPolyline(v);
					} else if (lastEntity.compareTo("LINE") == 0) {
						entityMaker.createLine(v);
					} else if (lastEntity.compareTo("TEXT") == 0) {
						entityMaker.createText(v);
					} else if (lastEntity.compareTo("MTEXT") == 0) {
						entityMaker.createMText(v);
					} else if (lastEntity.compareTo("POINT") == 0) {
						entityMaker.createPoint(v);
					} else if (lastEntity.compareTo("CIRCLE") == 0) {
						entityMaker.createCircle(v);
					} else if (lastEntity.compareTo("ELLIPSE") == 0) {
						entityMaker.createEllipse(v);
					} else if (lastEntity.compareTo("ARC") == 0) {
						entityMaker.createArc(v);
					} else if (lastEntity.compareTo("INSERT") == 0) {
						entityMaker.createInsert(v);
					} else if (lastEntity.compareTo("SOLID") == 0) {
						entityMaker.createSolid(v);
					} else if (lastEntity.compareTo("SPLINE") == 0) {
						entityMaker.createSpline(v);
					} else if (lastEntity.compareTo("ATTRIB") == 0) {
						entityMaker.createAttrib(v);
					} else if (lastEntity.compareTo("ENDSEC") == 0) {
						break;
					} else
						System.err.println("Dxf: Entidad "+lastEntity+" desconocida.");
				}
				v.clear();
				v.add(grp);
				while (true) {
					grp = readGrp();
					if (grp.code == 0)	break;
					v.add(grp);
				}
				entityCnt++;
			}
			cntVeces++;
		}
		System.out.println("Dxf: Seccion ENTITIES, " + entityCnt + " entidades, "+ cntVeces + " veces.");
		//System.out.println("Seccion ENTITIES, linea "+ l+ " (SALGO)");
	}

    /**
     * Permite la sección BLOCKS del DXF, donde se encuentran las definiciones de los
     * bloques que componen el DXF
     * @throws NumberFormatException
     * @throws Exception
     */
	private void readBlocks() throws NumberFormatException, Exception {
		System.out.println("Dxf: Seccion BLOCKS, linea "+ l);
		int blkCnt = 0;
		int cntVeces = 0;
		DxfGroupVector v = new DxfGroupVector();
		grp = readGrp();
		while (true) {
			if (grp.equals(0, "EOF")) break;
			else if (grp.code == 0) {
				if (v.size() > 0) {
					String lastEntity = (String) ((DxfGroup) v.get(0)).data;
					if (lastEntity.compareTo("BLOCK") == 0) {
						//System.out.println("readBlocks(): Empezamos a leer un bloque");
						entityMaker.createBlock(v);
					} else if (lastEntity.compareTo("POLYLINE") == 0) {
						//System.out.println("readBlocks(): Añadimos una polilinea al bloque");
						entityMaker.createPolyline(v);
					} else if (lastEntity.compareTo("VERTEX") == 0) {
						//System.out.println("readBlocks(): Añadimos un vertice a la polilinea");
						entityMaker.addVertex(v);
					} else if (lastEntity.compareTo("SEQEND") == 0) {
						//System.out.println("readBlocks(): Cerramos una polilinea");
						entityMaker.endSeq();
					} else if (lastEntity.compareTo("LWPOLYLINE") == 0) {
						//System.out.println("readBlocks(): Añadimos una lwpolilinea al bloque");
						entityMaker.createLwPolyline(v);
					} else if (lastEntity.compareTo("LINE") == 0) {
						//System.out.println("readBlocks(): Añadimos una linea al bloque");
						entityMaker.createLine(v);
					} else if (lastEntity.compareTo("TEXT") == 0) {
						//System.out.println("readBlocks(): Añadimos un texto al bloque");
						entityMaker.createText(v);
					} else if (lastEntity.compareTo("MTEXT") == 0) {
						//System.out.println("readBlocks(): Añadimos un m-texto al bloque");
						entityMaker.createMText(v);
					} else if (lastEntity.compareTo("POINT") == 0) {
						//System.out.println("readBlocks(): Añadimos un punto al bloque");
						entityMaker.createPoint(v);
					} else if (lastEntity.compareTo("CIRCLE") == 0) {
						//System.out.println("readBlocks(): Añadimos un circulo al bloque");
						entityMaker.createCircle(v);
					} else if (lastEntity.compareTo("ARC") == 0) {
						//System.out.println("readBlocks(): Añadimos un arco al bloque");
						entityMaker.createArc(v);
					} else if (lastEntity.compareTo("INSERT") == 0) {
						//System.out.println("readBlocks(): Añadimos un insert al bloque");
						entityMaker.createInsert(v);
					} else if (lastEntity.compareTo("SOLID") == 0) {
						//System.out.println("readBlocks(): Añadimos un solido al bloque");
						entityMaker.createSolid(v);
					} else if (lastEntity.compareTo("SPLINE") == 0) {
						entityMaker.createSpline(v);
					} else if (lastEntity.compareTo("ATTDEF") == 0) {
						entityMaker.createAttdef(v);
					} else if (lastEntity.compareTo("ENDBLK") == 0) {
						//System.out.println("readBlocks(): Cerramos un bloque"+v);
						entityMaker.endBlk(v);
					} else if (lastEntity.compareTo("ENDSEC") == 0) {
						break;
					} else
						System.err.println("Dxf: Entidad de bloque "+lastEntity+" desconocida.");
				}
				v.clear();
				v.add(grp);
				while (true) {
					grp = readGrp();
					if (grp.code == 0)	break;
					v.add(grp);
				}
				blkCnt++;
			}
			cntVeces++;
		}

		entityMaker.testBlocks();
		// Cuando termina de leer la sección de bloques se asegura de que todos los campos
		// son distintos.
		//System.out.println("readBlocks(): entityMaker.getAttributes().size() = " + entityMaker.getAttributes().size());
		entityMaker.depureAttributes();
		//System.out.println("readBlocks(): entityMaker.getAttributes().size() = " + entityMaker.getAttributes().size());
		System.out.println("Dxf: Seccion BLOCKS, " + blkCnt + " elementos de bloque. "+ cntVeces + " veces.");
	}

    /**
     * Devuelve los objetos geométricos obtenidos de un DXF
     */
	public IObjList getObjects() {
		return this.entityMaker.getObjects();
	}

	/**
	 * jmorell: Método que permite salvar capas al formato DXF2000.
	 * Este método ha sido actualizado en el proceso de implementación del piloto
	 * de CAD. En este piloto debía existir soporte para elipses, y este es uno de
	 * los motivos que nos llevan a implementar ahora para DXF2000, puesto que el
	 * DXF R12 no soporta elipses.
	 * @param fName
	 * @throws IOException
	 */
	public void save(String fName) throws IOException {
		System.out.println("save: fName = " + fName);
		long t2, t1;
		t1 = getTime();
		fName = DataSource.normalize(fName);
		FileWriter fw = new FileWriter(fName);
		// COMMENTAIRES DU TRADUCTEUR
//		fw.write(DxfGroup.toString(999, Integer.toString(features.size()) + " features"));
		fw.write(DxfGroup.toString(999, "TRANSLATION BY geo.cresques.io.DxfFile"));
		fw.write(DxfGroup.toString(999, "DATE : " + (new Date()).toString()));
		writeHeader(fw);
		writeTables(fw);
		writeBlocks(fw);
		writeEntities(fw);
		writeObjects(fw);
		fw.write(DxfGroup.toString(0, "EOF"));
		fw.flush();
		fw.close();
		t2 = getTime();
		System.out.println("DxfFile.save(): Tiempo salvando: " + (t2-t1)/1000 + " seg.");
	}

	/**
	 * Escritor de la cabecera de un DXF.
	 * jmorell: Actualización del escritor de DXF de R12 a 2000.
	 * @param fw, un FileWriter para escribir ficheros
	 * @throws IOException
	 */
	public void writeHeader(FileWriter fw) throws IOException {
		fw.write(DxfGroup.toString(0, "SECTION"));
		fw.write(DxfGroup.toString(2, "HEADER"));
		fw.write(DxfGroup.toString(9, "$ACADVER"));
			//fw.write(DxfGroup.toString(1, "AC1009"));			// DXF R12
			fw.write(DxfGroup.toString(1, "AC1015"));			// DXF 2000
		fw.write(DxfGroup.toString(9, "$INSBASE"));
			fw.write(DxfGroup.toString(10, 0.0, 1));
			fw.write(DxfGroup.toString(20, 0.0, 1));
			fw.write(DxfGroup.toString(30, 0.0, 1));
		fw.write(DxfGroup.toString(9, "$EXTMIN"));
			fw.write(DxfGroup.toString(10, extent.minX(), 6));
			fw.write(DxfGroup.toString(20, extent.minY(), 6));
			if (dxf3DFlag) fw.write(DxfGroup.toString(30, extent.minX(), 6));
            else fw.write(DxfGroup.toString(30, 0.0, 6));
		fw.write(DxfGroup.toString(9, "$EXTMAX"));
			fw.write(DxfGroup.toString(10, extent.maxX(), 6));
			fw.write(DxfGroup.toString(20, extent.maxY(), 6));
            if (dxf3DFlag) fw.write(DxfGroup.toString(30, extent.maxX(), 6));
            else fw.write(DxfGroup.toString(30, 0.0, 6));
		fw.write(DxfGroup.toString(9, "$LIMMIN"));
			fw.write(DxfGroup.toString(10, extent.minX(), 6));
			fw.write(DxfGroup.toString(20, extent.minY(), 6));
		fw.write(DxfGroup.toString(9, "$LIMMAX"));
			fw.write(DxfGroup.toString(10, extent.maxX(), 6));
			fw.write(DxfGroup.toString(20, extent.maxY(), 6));
		fw.write(DxfGroup.toString(9, "$ORTHOMODE")+DxfGroup.toString(70, 0));
		fw.write(DxfGroup.toString(9, "$REGENMODE")+DxfGroup.toString(70, 1));
		fw.write(DxfGroup.toString(9, "$FILLMODE")+	DxfGroup.toString(70, 1));
		fw.write(DxfGroup.toString(9, "$QTEXTMODE")+DxfGroup.toString(70, 0));
		fw.write(DxfGroup.toString(9, "$MIRRTEXT")+	DxfGroup.toString(70, 1));
		fw.write(DxfGroup.toString(9, "$DRAGMODE")+	DxfGroup.toString(70, 2));
		fw.write(DxfGroup.toString(9, "$LTSCALE")+	DxfGroup.toString(40, 1.0, 1));
		fw.write(DxfGroup.toString(9, "$OSMODE")+	DxfGroup.toString(70, 0));
		fw.write(DxfGroup.toString(9, "$ATTMODE")+	DxfGroup.toString(70, 1));
		fw.write(DxfGroup.toString(9, "$TEXTSIZE")+	DxfGroup.toString(40, 0.2, 1));
		fw.write(DxfGroup.toString(9, "$TRACEWID")+	DxfGroup.toString(40, 0.05, 2));
		fw.write(DxfGroup.toString(9, "$TEXTSTYLE")+	DxfGroup.toString(7, "STANDARD"));
		fw.write(DxfGroup.toString(9, "$CLAYER")+	DxfGroup.toString(8, "0"));
		fw.write(DxfGroup.toString(9, "$CELTYPE")+	DxfGroup.toString(6, "CONTINUOUS"));
		fw.write(DxfGroup.toString(9, "$CECOLOR")+	DxfGroup.toString(62, 256));
		fw.write(DxfGroup.toString(9, "$DIMSCALE")+	DxfGroup.toString(40, 1.0, 1));
		fw.write(DxfGroup.toString(9, "$DIMASZ")+	DxfGroup.toString(40, 0.18, 2));
		fw.write(DxfGroup.toString(9, "$DIMEXO")+	DxfGroup.toString(40, 0.0625, 4));
		fw.write(DxfGroup.toString(9, "$DIMDLI")+	DxfGroup.toString(40, 0.38, 2));
		fw.write(DxfGroup.toString(9, "$DIMRND")+	DxfGroup.toString(40, 0.0, 1));
		fw.write(DxfGroup.toString(9, "$DIMDLE")+	DxfGroup.toString(40, 0.0, 1));
		fw.write(DxfGroup.toString(9, "$DIMEXE")+	DxfGroup.toString(40, 0.18, 2));
		fw.write(DxfGroup.toString(9, "$DIMTP")+	DxfGroup.toString(40, 0.0, 1));
		fw.write(DxfGroup.toString(9, "$DIMTM")+	DxfGroup.toString(40, 0.0, 1));
		fw.write(DxfGroup.toString(9, "$DIMTXT")+	DxfGroup.toString(40, 0.18, 2));
		fw.write(DxfGroup.toString(9, "$DIMCEN")+	DxfGroup.toString(40, 0.09, 2));
		fw.write(DxfGroup.toString(9, "$DIMTSZ")+	DxfGroup.toString(40, 0.0, 1));
		fw.write(DxfGroup.toString(9,"$DIMTOL")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$DIMLIM")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$DIMTIH")+	DxfGroup.toString(70,1));
		fw.write(DxfGroup.toString(9,"$DIMTOH")+	DxfGroup.toString(70,1));
		fw.write(DxfGroup.toString(9,"$DIMSE1")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$DIMSE2")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$DIMTAD")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$DIMZIN")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$DIMBLK")+	DxfGroup.toString(1,""));
		fw.write(DxfGroup.toString(9,"$DIMASO")+	DxfGroup.toString(70,1));
		fw.write(DxfGroup.toString(9,"$DIMSHO")+	DxfGroup.toString(70,1));
		fw.write(DxfGroup.toString(9,"$DIMPOST")+	DxfGroup.toString(1,""));
		fw.write(DxfGroup.toString(9,"$DIMAPOST")+	DxfGroup.toString(1,""));
		fw.write(DxfGroup.toString(9,"$DIMALT")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$DIMALTD")+	DxfGroup.toString(70,2));
		fw.write(DxfGroup.toString(9,"$DIMALTF")+	DxfGroup.toString(40,25.4,1));
		fw.write(DxfGroup.toString(9,"$DIMLFAC")+	DxfGroup.toString(40,1.0,1));
		fw.write(DxfGroup.toString(9,"$DIMTOFL")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$DIMTVP")+	DxfGroup.toString(40,0.0,1));
		fw.write(DxfGroup.toString(9,"$DIMTIX")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$DIMSOXD")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$DIMSAH")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$DIMBLK1")+	DxfGroup.toString(1,""));
		fw.write(DxfGroup.toString(9,"$DIMBLK2")+	DxfGroup.toString(1,""));
		fw.write(DxfGroup.toString(9,"$DIMSTYLE")+	DxfGroup.toString(2,"STANDARD"));
		fw.write(DxfGroup.toString(9,"$DIMCLRD")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$DIMCLRE")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$DIMCLRT")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$DIMTFAC")+	DxfGroup.toString(40,1.0,1));
		fw.write(DxfGroup.toString(9,"$DIMGAP")+	DxfGroup.toString(40,0.09,2));
		fw.write(DxfGroup.toString(9,"$LUNITS")+	DxfGroup.toString(70,2));
		fw.write(DxfGroup.toString(9,"$LUPREC")+	DxfGroup.toString(70,4));
		fw.write(DxfGroup.toString(9,"$AXISMODE")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$AXISUNIT"));
		fw.write(DxfGroup.toString(10,0.0,1));
		fw.write(DxfGroup.toString(20,0.0,1));
		fw.write(DxfGroup.toString(9,"$SKETCHINC")+	DxfGroup.toString(40,0.1,1));
		fw.write(DxfGroup.toString(9,"$FILLETRAD")+	DxfGroup.toString(40,0.0,1));
		fw.write(DxfGroup.toString(9,"$AUNITS")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$AUPREC")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$MENU")+		DxfGroup.toString(1,"acad"));
		fw.write(DxfGroup.toString(9,"$ELEVATION")+	DxfGroup.toString(40,0.0,1));
		fw.write(DxfGroup.toString(9,"$PELEVATION")+DxfGroup.toString(40,0.0,1));
		fw.write(DxfGroup.toString(9,"$THICKNESS")+	DxfGroup.toString(40,0.0,1));
		fw.write(DxfGroup.toString(9,"$LIMCHECK")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$BLIPMODE")+	DxfGroup.toString(70,1));
		fw.write(DxfGroup.toString(9,"$CHAMFERA")+	DxfGroup.toString(40,0.0,1));
		fw.write(DxfGroup.toString(9,"$CHAMFERB")+	DxfGroup.toString(40,0.0,1));
		fw.write(DxfGroup.toString(9,"$SKPOLY")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$TDCREATE")+	DxfGroup.toString(40,2453116.436828704,9));
		fw.write(DxfGroup.toString(9,"$TDUPDATE")+	DxfGroup.toString(40,2453116.436828704,9));
		fw.write(DxfGroup.toString(9,"$TDINDWG")+	DxfGroup.toString(40,0.0000000000,10));
		fw.write(DxfGroup.toString(9,"$TDUSRTIMER")+DxfGroup.toString(40,0.0000000000,10));
		fw.write(DxfGroup.toString(9,"$USRTIMER")+	DxfGroup.toString(70,1));
		fw.write(DxfGroup.toString(9,"$ANGBASE")+	DxfGroup.toString(50,0.0,1));
		fw.write(DxfGroup.toString(9,"$ANGDIR")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$PDMODE")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$PDSIZE")+	DxfGroup.toString(40,0.0,1));
		fw.write(DxfGroup.toString(9,"$PLINEWID")+	DxfGroup.toString(40,0.0,1));
		fw.write(DxfGroup.toString(9,"$COORDS")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$SPLFRAME")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$SPLINETYPE")+DxfGroup.toString(70,6));
		fw.write(DxfGroup.toString(9,"$SPLINESEGS")+DxfGroup.toString(70,10));
		fw.write(DxfGroup.toString(9,"$ATTDIA")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$ATTREQ")+	DxfGroup.toString(70,1));
		fw.write(DxfGroup.toString(9,"$HANDLING")+	DxfGroup.toString(70,1));
		fw.write(DxfGroup.toString(9,"$HANDSEED")+	DxfGroup.toString(5,"394B"));
		fw.write(DxfGroup.toString(9,"$SURFTAB1")+	DxfGroup.toString(70,6));
		fw.write(DxfGroup.toString(9,"$SURFTAB2")+	DxfGroup.toString(70,6));
		fw.write(DxfGroup.toString(9,"$SURFTYPE")+	DxfGroup.toString(70,6));
		fw.write(DxfGroup.toString(9,"$SURFU")+		DxfGroup.toString(70,6));
		fw.write(DxfGroup.toString(9,"$SURFV")+		DxfGroup.toString(70,6));
		fw.write(DxfGroup.toString(9,"$UCSNAME")+	DxfGroup.toString(2,""));
		fw.write(DxfGroup.toString(9,"$UCSORG"));
		fw.write(DxfGroup.toString(10,0.0,1));
		fw.write(DxfGroup.toString(20,0.0,1));
		fw.write(DxfGroup.toString(30,0.0,1));
		fw.write(DxfGroup.toString(9,"$UCSXDIR"));
		fw.write(DxfGroup.toString(10,1.0,1));
		fw.write(DxfGroup.toString(20,0.0,1));
		fw.write(DxfGroup.toString(30,0.0,1));
		fw.write(DxfGroup.toString(9,"$UCSYDIR"));
		fw.write(DxfGroup.toString(10,0.0,1));
		fw.write(DxfGroup.toString(20,1.0,1));
		fw.write(DxfGroup.toString(30,0.0,1));
		fw.write(DxfGroup.toString(9,"$PUCSNAME")+	DxfGroup.toString(2,""));
		fw.write(DxfGroup.toString(9,"$PUCSORG"));
		fw.write(DxfGroup.toString(10,0.0,1));
		fw.write(DxfGroup.toString(20,0.0,1));
		fw.write(DxfGroup.toString(30,0.0,1));
		fw.write(DxfGroup.toString(9,"$PUCSXDIR"));
		fw.write(DxfGroup.toString(10,1.0,1));
		fw.write(DxfGroup.toString(20,0.0,1));
		fw.write(DxfGroup.toString(30,0.0,1));
		fw.write(DxfGroup.toString(9,"$PUCSYDIR"));
		fw.write(DxfGroup.toString(10,0.0,1));
		fw.write(DxfGroup.toString(20,1.0,1));
		fw.write(DxfGroup.toString(30,0.0,1));
		fw.write(DxfGroup.toString(9,"$USERI1")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$USERI2")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$USERI3")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$USERI4")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$USERI5")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$USERR1")+	DxfGroup.toString(40,0.0,1));
		fw.write(DxfGroup.toString(9,"$USERR2")+	DxfGroup.toString(40,0.0,1));
		fw.write(DxfGroup.toString(9,"$USERR3")+	DxfGroup.toString(40,0.0,1));
		fw.write(DxfGroup.toString(9,"$USERR4")+	DxfGroup.toString(40,0.0,1));
		fw.write(DxfGroup.toString(9,"$USERR5")+	DxfGroup.toString(40,0.0,1));
		fw.write(DxfGroup.toString(9,"$WORLDVIEW")+	DxfGroup.toString(70,1));
		fw.write(DxfGroup.toString(9,"$SHADEDGE")+	DxfGroup.toString(70,3));
		fw.write(DxfGroup.toString(9,"$SHADEDIF")+	DxfGroup.toString(70,70));
		fw.write(DxfGroup.toString(9,"$TILEMODE")+	DxfGroup.toString(70,1));
		fw.write(DxfGroup.toString(9,"$MAXACTVP")+	DxfGroup.toString(70,16));
		fw.write(DxfGroup.toString(9,"$PINSBASE"));
		fw.write(DxfGroup.toString(10,0.0,1));
		fw.write(DxfGroup.toString(20,0.0,1));
		fw.write(DxfGroup.toString(30,0.0,1));
		fw.write(DxfGroup.toString(9,"$PLIMCHECK")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$PEXTMIN"));
		fw.write(DxfGroup.toString(10,"-1.000000E+20"));
		fw.write(DxfGroup.toString(20,"-1.000000E+20"));
		fw.write(DxfGroup.toString(30,"-1.000000E+20"));
		fw.write(DxfGroup.toString(9,"$PEXTMAX"));
		fw.write(DxfGroup.toString(10,"-1.000000E+20"));
		fw.write(DxfGroup.toString(20,"-1.000000E+20"));
		fw.write(DxfGroup.toString(30,"-1.000000E+20"));
		fw.write(DxfGroup.toString(9,"$PLIMMIN"));
		fw.write(DxfGroup.toString(10,0.0,1));
		fw.write(DxfGroup.toString(20,0.0,1));
		fw.write(DxfGroup.toString(9,"$PLIMMAX"));
		fw.write(DxfGroup.toString(10,12.0,1));
		fw.write(DxfGroup.toString(20,9.0,1));
		fw.write(DxfGroup.toString(9,"$UNITMODE")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$VISRETAIN")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$PLINEGEN")+	DxfGroup.toString(70,1));
		fw.write(DxfGroup.toString(9,"$PSLTSCALE")+	DxfGroup.toString(70,0));
		fw.write(DxfGroup.toString(9,"$TREEDEPTH")+	DxfGroup.toString(70,3020));
		fw.write(DxfGroup.toString(9,"$DWGCODEPAGE")+DxfGroup.toString(3,"ansi_1252"));
/*
		fw.write(DxfGroup.toString(9, "$ELEVATION"));
			fw.write(DxfGroup.toString(40, 0.0, 3));
		fw.write(DxfGroup.toString(9, "$LIMCHECK"));
			fw.write(DxfGroup.toString(70, 1));
		fw.write(DxfGroup.toString(9, "$LUNITS"));
			fw.write(DxfGroup.toString(70, 2));
		fw.write(DxfGroup.toString(9, "$LUPREC"));
			fw.write(DxfGroup.toString(70, 2));*/
		fw.write(DxfGroup.toString(0, "ENDSEC"));
	}

	/**
	 * Escritor de la sección TABLES de un DXF.
	 * @param fw, FileWriter
	 * @throws IOException
	 */
	public void writeTables(FileWriter fw) throws IOException {
		fw.write(DxfGroup.toString(0, "SECTION"));
		fw.write(DxfGroup.toString(2, "TABLES"));
		// esta tampoco.
		writeVPortTable(fw);
		writeLTypeTable(fw);
		writeLayerTable(fw);
		writeStyleTable(fw);
		// estas no son las provocan el pete.
		writeViewTable(fw);
		writeUCSTable(fw);
		// esta provoca el pete. Y si no se pone las tablas aparecen
		// incompletas y acad no abre el fichero ...
		writeAppidTable(fw);
		writeDimStyleTable(fw);
		// esta no provoca pete.
		writeBlockRecordTable(fw);
		fw.write(DxfGroup.toString(0, "ENDSEC"));
	}

	/**
	 * Escritor de la tabla VPORT.
	 * @param fw
	 * @throws IOException
	 */
	public void writeVPortTable(FileWriter fw) throws IOException {
		fw.write(DxfGroup.toString(0, "TABLE"));
		fw.write(DxfGroup.toString(2, "VPORT"));
		fw.write(DxfGroup.toString(5, 8));
		fw.write(DxfGroup.toString(100, "AcDbSymbolTable"));
		fw.write(DxfGroup.toString(70, 0));
		/*fw.write(DxfGroup.toString(70, 1));
		fw.write(DxfGroup.toString(0, "VPORT"));
		fw.write(DxfGroup.toString(5, 30));
		fw.write(DxfGroup.toString(100, "AcDbSymbolTableRecord"));
		fw.write(DxfGroup.toString(100, "AcDbViewportTableRecord"));
		fw.write(DxfGroup.toString(2, "*Active"));
		fw.write(DxfGroup.toString(70, 0));
		fw.write(DxfGroup.toString(10, 0.0, 4));
		fw.write(DxfGroup.toString(20, 0.0, 4));
		fw.write(DxfGroup.toString(11, 1.0, 4));
		fw.write(DxfGroup.toString(21, 1.0, 4));
		fw.write(DxfGroup.toString(12, 286.30555555555549, 10));
		fw.write(DxfGroup.toString(22, 148.5, 4));
		fw.write(DxfGroup.toString(13, 0.0, 4));
		fw.write(DxfGroup.toString(23, 0.0, 4));
		fw.write(DxfGroup.toString(14, 10.0, 4));
		fw.write(DxfGroup.toString(24, 10.0, 4));
		fw.write(DxfGroup.toString(15, 10.0, 4));
		fw.write(DxfGroup.toString(25, 10.0, 4));
		fw.write(DxfGroup.toString(16, 0.0, 4));
		fw.write(DxfGroup.toString(26, 0.0, 4));
		fw.write(DxfGroup.toString(36, 1.0, 4));
		fw.write(DxfGroup.toString(17, 0.0, 4));
		fw.write(DxfGroup.toString(27, 0.0, 4));
		fw.write(DxfGroup.toString(37, 0.0, 4));
		fw.write(DxfGroup.toString(40, 297.0, 4));
		fw.write(DxfGroup.toString(41, 1.92798353909465, 10));
		fw.write(DxfGroup.toString(42, 50.0, 4));
		fw.write(DxfGroup.toString(43, 0.0, 4));
		fw.write(DxfGroup.toString(44, 0.0, 4));
		fw.write(DxfGroup.toString(50, 0.0, 4));
		fw.write(DxfGroup.toString(51, 0.0, 4));
		fw.write(DxfGroup.toString(71, 0));
		fw.write(DxfGroup.toString(72, 100));
		fw.write(DxfGroup.toString(73, 1));
		fw.write(DxfGroup.toString(74, 3));
		fw.write(DxfGroup.toString(75, 1));
		fw.write(DxfGroup.toString(76, 1));
		fw.write(DxfGroup.toString(77, 0));
		fw.write(DxfGroup.toString(78, 0));
		fw.write(DxfGroup.toString(281, 0));
		fw.write(DxfGroup.toString(65, 1));
		fw.write(DxfGroup.toString(110, 0.0, 4));
		fw.write(DxfGroup.toString(120, 0.0, 4));
		fw.write(DxfGroup.toString(130, 0.0, 4));
		fw.write(DxfGroup.toString(111, 1.0, 4));
		fw.write(DxfGroup.toString(121, 0.0, 4));
		fw.write(DxfGroup.toString(131, 0.0, 4));
		fw.write(DxfGroup.toString(112, 0.0, 4));
		fw.write(DxfGroup.toString(122, 1.0, 4));
		fw.write(DxfGroup.toString(132, 0.0, 4));
		fw.write(DxfGroup.toString(79, 0));
		fw.write(DxfGroup.toString(146, 0.0, 4));*/
		fw.write(DxfGroup.toString(0, "ENDTAB"));
	}

	/**
	 * Escritor de la tabla LTYPE.
	 * @param fw
	 * @throws IOException
	 */
	public void writeLTypeTable(FileWriter fw) throws IOException {
		fw.write(DxfGroup.toString(0, "TABLE"));
		fw.write(DxfGroup.toString(2, "LTYPE"));
		fw.write(DxfGroup.toString(5, 5));
		fw.write(DxfGroup.toString(100, "AcDbSymbolTable"));
		fw.write(DxfGroup.toString(70, 1));
		// Aqui hay que crear un objeto DxfLType como el DxfLayer también basado en
		// el lector de DXF de Mich.
		/*DxfTABLE_LTYPE_ITEM ltype =
			new DxfTABLE_LTYPE_ITEM("CONTINUE", 0, "", 65, 0f, new float[0]);
		fw.write(ltype.toString());*/
		fw.write(DxfGroup.toString(0, "LTYPE"));
		fw.write(DxfGroup.toString(5, 14));
		fw.write(DxfGroup.toString(100, "AcDbSymbolTableRecord"));
		fw.write(DxfGroup.toString(100, "AcDbLinetypeTableRecord"));
		fw.write(DxfGroup.toString(2, "ByBlock"));
		fw.write(DxfGroup.toString(70, 0));
		fw.write(DxfGroup.toString(3, ""));
		fw.write(DxfGroup.toString(72, 65));
		fw.write(DxfGroup.toString(73, 0));
		fw.write(DxfGroup.toString(40, 0.0, 4));
		fw.write(DxfGroup.toString(0, "LTYPE"));
		fw.write(DxfGroup.toString(5, 15));
		fw.write(DxfGroup.toString(100, "AcDbSymbolTableRecord"));
		fw.write(DxfGroup.toString(100, "AcDbLinetypeTableRecord"));
		fw.write(DxfGroup.toString(2, "ByLayer"));
		fw.write(DxfGroup.toString(70, 0));
		fw.write(DxfGroup.toString(3, ""));
		fw.write(DxfGroup.toString(72, 65));
		fw.write(DxfGroup.toString(73, 0));
		fw.write(DxfGroup.toString(40, 0.0, 4));
		fw.write(DxfGroup.toString(0, "ENDTAB"));
	}

	/**
	 * Escritor de la tabla LAYER.
	 * @param fw
	 * @throws IOException
	 */
	public void writeLayerTable(FileWriter fw) throws IOException {
		fw.write(DxfGroup.toString(0, "TABLE"));
		fw.write(DxfGroup.toString(2, "LAYER"));
		fw.write(DxfGroup.toString(5, 2));
		fw.write(DxfGroup.toString(100, "AcDbSymbolTable"));
		fw.write(DxfGroup.toString(70, 1));
		//layer = new DxfLayer(layerName, 0, 131, "CONTINUOUS");
		//fw.write(layer.toString());
		//fw.write(((DxfEntityMaker) entityMaker).getLayers().toDxfString());
		fw.write(DxfGroup.toString(0, "LAYER"));
		fw.write(DxfGroup.toString(5, 10));
		fw.write(DxfGroup.toString(100, "AcDbSymbolTableRecord"));
		fw.write(DxfGroup.toString(100, "AcDbLayerTableRecord"));
		fw.write(DxfGroup.toString(2, "0"));
		fw.write(DxfGroup.toString(70, 0));
		fw.write(DxfGroup.toString(62, 7));
		fw.write(DxfGroup.toString(6, "CONTINUOUS"));
		fw.write(DxfGroup.toString(390, "F"));
		fw.write(DxfGroup.toString(0, "ENDTAB"));
	}

	/**
	 * Escritor de la tabla STYLE.
	 * @param fw
	 * @throws IOException
	 */
	public void writeStyleTable(FileWriter fw) throws IOException {
		fw.write(DxfGroup.toString(0, "TABLE"));
		fw.write(DxfGroup.toString(2, "STYLE"));
		fw.write(DxfGroup.toString(5, 3));
		fw.write(DxfGroup.toString(100, "AcDbSymbolTable"));
		fw.write(DxfGroup.toString(70, 0));
		/*fw.write(DxfGroup.toString(70, 1));
		DxfTABLE_STYLE_ITEM style =
			new DxfTABLE_STYLE_ITEM("STANDARD", 0, 0f, 1f, 0f, 0, 1.0f, "xxx.txt", "yyy.txt");
		fw.write(style.toString());*/
		fw.write(DxfGroup.toString(0, "STYLE"));
		fw.write(DxfGroup.toString(5, 11));
		fw.write(DxfGroup.toString(100, "AcDbSymbolTableRecord"));
		fw.write(DxfGroup.toString(100, "AcDbTextStyleTableRecord"));
		fw.write(DxfGroup.toString(2, "Standard"));
		fw.write(DxfGroup.toString(70, 0));
		fw.write(DxfGroup.toString(71, 0));
		fw.write(DxfGroup.toString(40, 0.0, 4));
		fw.write(DxfGroup.toString(41, 1.0, 4));
		fw.write(DxfGroup.toString(42, 2.5, 4));
		fw.write(DxfGroup.toString(50, 0.0, 4));
		fw.write(DxfGroup.toString(3, "txt"));
		fw.write(DxfGroup.toString(4, ""));
		fw.write(DxfGroup.toString(0, "ENDTAB"));
	}

	/**
	 * Escritor de la tabla VIEW.
	 * @param fw
	 * @throws IOException
	 */
	public void writeViewTable(FileWriter fw) throws IOException {
		fw.write(DxfGroup.toString(0, "TABLE"));
		fw.write(DxfGroup.toString(2, "VIEW"));
		fw.write(DxfGroup.toString(5, 6));
		fw.write(DxfGroup.toString(100, "AcDbSymbolTable"));
		fw.write(DxfGroup.toString(70, 0));
		fw.write(DxfGroup.toString(0, "ENDTAB"));
	}

	/**
	 * Escritor de la tabla UCS.
	 * @param fw
	 * @throws IOException
	 */
	public void writeUCSTable(FileWriter fw) throws IOException {
		fw.write(DxfGroup.toString(0, "TABLE"));
		fw.write(DxfGroup.toString(2, "UCS"));
		fw.write(DxfGroup.toString(5, 7));
		fw.write(DxfGroup.toString(100, "AcDbSymbolTable"));
		fw.write(DxfGroup.toString(70, 0));
		fw.write(DxfGroup.toString(0, "ENDTAB"));
	}

	/**
	 * Escritor de la tabla APPID.
	 * @param fw
	 * @throws IOException
	 */
	public void writeAppidTable(FileWriter fw) throws IOException {
		fw.write(DxfGroup.toString(0, "TABLE"));
		fw.write(DxfGroup.toString(2, "APPID"));
		fw.write(DxfGroup.toString(5, 9));
		fw.write(DxfGroup.toString(100, "AcDbSymbolTable"));
		fw.write(DxfGroup.toString(70, 1));
		fw.write(DxfGroup.toString(0, "APPID"));
		fw.write(DxfGroup.toString(5, 12));
		fw.write(DxfGroup.toString(100, "AcDbSymbolTableRecord"));
		fw.write(DxfGroup.toString(100, "AcDbRegAppTableRecord"));
		fw.write(DxfGroup.toString(2, "ACAD"));
		fw.write(DxfGroup.toString(70, 1));
		fw.write(DxfGroup.toString(0, "ENDTAB"));
	}

	/**
	 * Escritor de la tabla DIMSTYLE.
	 * @param fw
	 * @throws IOException
	 */
	public void writeDimStyleTable(FileWriter fw) throws IOException {
		fw.write(DxfGroup.toString(0, "TABLE"));
		fw.write(DxfGroup.toString(2, "DIMSTYLE"));
		fw.write(DxfGroup.toString(5, "A"));
		fw.write(DxfGroup.toString(100, "AcDbSymbolTable"));
		fw.write(DxfGroup.toString(70, 0));
		fw.write(DxfGroup.toString(100, "AcDbDimStyleTable"));
		fw.write(DxfGroup.toString(0, "ENDTAB"));
	}

	/**
	 * Escritor de la tabla BLOCK_RECORD.
	 * @param fw
	 * @throws IOException
	 */
	public void writeBlockRecordTable(FileWriter fw) throws IOException {
		fw.write(DxfGroup.toString(0, "TABLE"));
		fw.write(DxfGroup.toString(2, "BLOCK_RECORD"));
		fw.write(DxfGroup.toString(5, 1));
		fw.write(DxfGroup.toString(100, "AcDbSymbolTable"));
		fw.write(DxfGroup.toString(70, 1));
		fw.write(DxfGroup.toString(0, "BLOCK_RECORD"));
		fw.write(DxfGroup.toString(5, "1F"));
		fw.write(DxfGroup.toString(100, "AcDbSymbolTableRecord"));
		fw.write(DxfGroup.toString(100, "AcDbBlockTableRecord"));
		fw.write(DxfGroup.toString(2, "*Model_Space"));
		fw.write(DxfGroup.toString(340, "22"));
		fw.write(DxfGroup.toString(0, "BLOCK_RECORD"));
		fw.write(DxfGroup.toString(5, "1B"));
		fw.write(DxfGroup.toString(100, "AcDbSymbolTableRecord"));
		fw.write(DxfGroup.toString(100, "AcDbBlockTableRecord"));
		fw.write(DxfGroup.toString(2, "*Paper_Space"));
		fw.write(DxfGroup.toString(340, "1E"));
		fw.write(DxfGroup.toString(0, "BLOCK_RECORD"));
		fw.write(DxfGroup.toString(5, "23"));
		fw.write(DxfGroup.toString(100, "AcDbSymbolTableRecord"));
		fw.write(DxfGroup.toString(100, "AcDbBlockTableRecord"));
		fw.write(DxfGroup.toString(2, "*Paper_Space0"));
		fw.write(DxfGroup.toString(340, "26"));
		fw.write(DxfGroup.toString(0, "ENDTAB"));
	}

	/**
	 * Escritor de la sección de bloques por defecto de un DXF2000.
	 * @param fw
	 * @throws IOException
	 */
	public void writeBlocks(FileWriter fw) throws IOException {
		fw.write(DxfGroup.toString(0, "SECTION"));
		fw.write(DxfGroup.toString(2, "BLOCKS"));
		fw.write(DxfGroup.toString(0, "BLOCK"));
		fw.write(DxfGroup.toString(5, "20"));
		fw.write(DxfGroup.toString(100, "AcDbEntity"));
		fw.write(DxfGroup.toString(8, "0"));
		fw.write(DxfGroup.toString(100, "AcDbBlockBegin"));
		fw.write(DxfGroup.toString(2, "*Model_Space"));
		fw.write(DxfGroup.toString(70, 0));
		fw.write(DxfGroup.toString(10, 0.0, 4));
		fw.write(DxfGroup.toString(20, 0.0, 4));
		fw.write(DxfGroup.toString(30, 0.0, 4));
		fw.write(DxfGroup.toString(3, "*Model_Space"));
		fw.write(DxfGroup.toString(1, ""));
		fw.write(DxfGroup.toString(0, "ENDBLK"));
		fw.write(DxfGroup.toString(5, "21"));
		fw.write(DxfGroup.toString(100, "AcDbEntity"));
		fw.write(DxfGroup.toString(8, "0"));
		fw.write(DxfGroup.toString(100, "AcDbBlockEnd"));
		fw.write(DxfGroup.toString(0, "BLOCK"));
		fw.write(DxfGroup.toString(5, "1C"));
		fw.write(DxfGroup.toString(100, "AcDbEntity"));
		fw.write(DxfGroup.toString(67, 1));
		fw.write(DxfGroup.toString(8, "0"));
		fw.write(DxfGroup.toString(100, "AcDbBlockBegin"));
		fw.write(DxfGroup.toString(2, "*Paper_Space"));
		fw.write(DxfGroup.toString(70, 0));
		fw.write(DxfGroup.toString(10, 0.0, 4));
		fw.write(DxfGroup.toString(20, 0.0, 4));
		fw.write(DxfGroup.toString(30, 0.0, 4));
		fw.write(DxfGroup.toString(3, "*Paper_Space"));
		fw.write(DxfGroup.toString(1, ""));
		fw.write(DxfGroup.toString(0, "ENDBLK"));
		fw.write(DxfGroup.toString(5, "1D"));
		fw.write(DxfGroup.toString(100, "AcDbEntity"));
		fw.write(DxfGroup.toString(67, 1));
		fw.write(DxfGroup.toString(8, "0"));
		fw.write(DxfGroup.toString(100, "AcDbBlockEnd"));
		fw.write(DxfGroup.toString(0, "BLOCK"));
		fw.write(DxfGroup.toString(5, "24"));
		fw.write(DxfGroup.toString(100, "AcDbEntity"));
		fw.write(DxfGroup.toString(8, "0"));
		fw.write(DxfGroup.toString(100, "AcDbBlockBegin"));
		fw.write(DxfGroup.toString(2, "*Paper_Space0"));
		fw.write(DxfGroup.toString(70, 0));
		fw.write(DxfGroup.toString(10, 0.0, 4));
		fw.write(DxfGroup.toString(20, 0.0, 4));
		fw.write(DxfGroup.toString(30, 0.0, 4));
		fw.write(DxfGroup.toString(3, "*Paper_Space0"));
		fw.write(DxfGroup.toString(1, ""));
		fw.write(DxfGroup.toString(0, "ENDBLK"));
		fw.write(DxfGroup.toString(5, "25"));
		fw.write(DxfGroup.toString(100, "AcDbEntity"));
		fw.write(DxfGroup.toString(8, "0"));
		fw.write(DxfGroup.toString(100, "AcDbBlockEnd"));
		fw.write(DxfGroup.toString(0, "ENDSEC"));
	}

	/**
	 * Escritor de la sección ENTITIES de entidades.
	 * @param fw
	 * @throws IOException
	 */
	public void writeEntities(FileWriter fw) throws IOException {
		// ECRITURE DES FEATURES
		fw.write(DxfGroup.toString(0, "SECTION"));
		fw.write(DxfGroup.toString(2, "ENTITIES"));
		if (cadFlag) {
			fw.write(((DxfEntityMaker) entityMaker).getEntities().toDxfString());
		} else {
			//fw.write(((DxfFeatureMaker) entityMaker).getObjects().toDxfString());
		}
		fw.write(DxfGroup.toString(0, "ENDSEC"));
	}

	/**
	 * 050224, jmorell: Escritura de la sección OBJECTS según QCAD.
	 * Writes a objects section. This section is needed in VER_R13.
	 * Note that this method currently only writes a faked OBJECTS section
	 * to make the file readable by Aut*cad.
	 */
	public void writeObjects(FileWriter fw) throws IOException {
	    //int dicId, dicId2, dicId3, dicId4, dicId5;
	    //int dicId5;

	    fw.write(DxfGroup.toString(  0, "SECTION"));
	    fw.write(DxfGroup.toString(  2, "OBJECTS"));
	    fw.write(DxfGroup.toString(  0, "DICTIONARY"));
	    //fw.write(DxfGroup.toString(5, 0xC);                            // C
	    fw.write(DxfGroup.toString(  5, "C"));
	    //fw.write(DxfGroup.toString(330, 0);
	    fw.write(DxfGroup.toString(100, "AcDbDictionary"));
	    fw.write(DxfGroup.toString(280, 0));
	    fw.write(DxfGroup.toString(281, 1));
	    fw.write(DxfGroup.toString(  3, "ACAD_GROUP"));
	    //fw.write(DxfGroup.toString(350, dw.getNextHandle());          // D
	    fw.write(DxfGroup.toString(350, "D"));          // D
	    fw.write(DxfGroup.toString(  3, "ACAD_LAYOUT"));
	    fw.write(DxfGroup.toString(350, "1A"));
	    //fw.write(DxfGroup.toString(350, dw.getNextHandle()+0);        // 1A
	    fw.write(DxfGroup.toString(  3, "ACAD_MLINESTYLE"));
	    fw.write(DxfGroup.toString(350, "17"));
	    //fw.write(DxfGroup.toString(350, dw.getNextHandle()+1);        // 17
	    fw.write(DxfGroup.toString(  3, "ACAD_PLOTSETTINGS"));
	    fw.write(DxfGroup.toString(350, "19"));
	    //fw.write(DxfGroup.toString(350, dw.getNextHandle()+2);        // 19
	    fw.write(DxfGroup.toString(  3, "ACAD_PLOTSTYLENAME"));
	    fw.write(DxfGroup.toString(350, "E"));
	    //fw.write(DxfGroup.toString(350, dw.getNextHandle()+3);        // E
	    fw.write(DxfGroup.toString(  3, "AcDbVariableDictionary"));
	    //fw.write(DxfGroup.toString(350, dw.getNextHandle()));        // 2C
	    fw.write(DxfGroup.toString(350, "2C"));        // 2C
	    fw.write(DxfGroup.toString(  0, "DICTIONARY"));
	    fw.write(DxfGroup.toString(5, "D"));
	    //dw.handle();                                    // D
	    //fw.write(DxfGroup.toString(330, 0xC);
	    fw.write(DxfGroup.toString(100, "AcDbDictionary"));
	    fw.write(DxfGroup.toString(280, 0));
	    fw.write(DxfGroup.toString(281, 1));
	    fw.write(DxfGroup.toString(  0, "ACDBDICTIONARYWDFLT"));
	    fw.write(DxfGroup.toString(5, "E"));
	    //dicId4 = dw.handle();                           // E
	    //fw.write(DxfGroup.toString(330, 0xC);                       // C
	    fw.write(DxfGroup.toString(100, "AcDbDictionary"));
	    fw.write(DxfGroup.toString(281, 1));
	    fw.write(DxfGroup.toString(  3, "Normal"));
	    fw.write(DxfGroup.toString(350, "F"));
	    //fw.write(DxfGroup.toString(350, dw.getNextHandle()+5);        // F
	    fw.write(DxfGroup.toString(100, "AcDbDictionaryWithDefault"));
	    fw.write(DxfGroup.toString(340, "F"));
	    //fw.write(DxfGroup.toString(340, dw.getNextHandle()+5);        // F
	    fw.write(DxfGroup.toString(  0, "ACDBPLACEHOLDER"));
	    fw.write(DxfGroup.toString(5, "F"));
	    //dw.handle();                                    // F
	    //fw.write(DxfGroup.toString(330, dicId4);                      // E
	    fw.write(DxfGroup.toString(  0, "DICTIONARY"));
	    //dicId3 = dw.handle();                           // 17
	    fw.write(DxfGroup.toString(5, "17"));
	    //fw.write(DxfGroup.toString(330, 0xC);                       // C
	    fw.write(DxfGroup.toString(100, "AcDbDictionary"));
	    fw.write(DxfGroup.toString(280, 0));
	    fw.write(DxfGroup.toString(281, 1));
	    fw.write(DxfGroup.toString(  3, "Standard"));
	    fw.write(DxfGroup.toString(350, "18"));
	    //fw.write(DxfGroup.toString(350, dw.getNextHandle()+5);        // 18
	    fw.write(DxfGroup.toString(  0, "MLINESTYLE"));
	    fw.write(DxfGroup.toString(5, "18"));
	    //dw.handle();                                    // 18
	    //fw.write(DxfGroup.toString(330, dicId3);                      // 17
	    fw.write(DxfGroup.toString(100, "AcDbMlineStyle"));
	    fw.write(DxfGroup.toString(  2, "STANDARD"));
	    fw.write(DxfGroup.toString( 70, 0));
	    fw.write(DxfGroup.toString(  3, ""));
	    fw.write(DxfGroup.toString( 62, 256));
	    //dw.dxfReal( 51, 90.0);
	    fw.write(DxfGroup.toString( 51, 90.0, 4));
	    fw.write(DxfGroup.toString( 52, 90.0, 4));
	    fw.write(DxfGroup.toString( 71, 2));
	    fw.write(DxfGroup.toString( 49, 0.5, 4));
	    fw.write(DxfGroup.toString( 62, 256));
	    fw.write(DxfGroup.toString(  6, "BYLAYER"));
	    fw.write(DxfGroup.toString( 49, -0.5, 4));
	    fw.write(DxfGroup.toString( 62, 256));
	    fw.write(DxfGroup.toString(  6, "BYLAYER"));
	    fw.write(DxfGroup.toString(  0, "DICTIONARY"));
	    fw.write(DxfGroup.toString(5, "19"));
	    //dw.handle();                           // 17
	    //fw.write(DxfGroup.toString(330, 0xC);                       // C
	    fw.write(DxfGroup.toString(100, "AcDbDictionary"));
	    fw.write(DxfGroup.toString(280, 0));
	    fw.write(DxfGroup.toString(281, 1));
	    fw.write(DxfGroup.toString(  0, "DICTIONARY"));
	    //dicId2 = dw.handle();                           // 1A
	    fw.write(DxfGroup.toString(5, "1A"));
	    //fw.write(DxfGroup.toString(330, 0xC);
	    fw.write(DxfGroup.toString(100, "AcDbDictionary"));
	    fw.write(DxfGroup.toString(281, 1));
	    fw.write(DxfGroup.toString(  3, "Layout1"));
	    fw.write(DxfGroup.toString(350, "1E"));
	    //fw.write(DxfGroup.toString(350, dw.getNextHandle()+2);        // 1E
	    fw.write(DxfGroup.toString(  3, "Layout2"));
	    fw.write(DxfGroup.toString(350, "26"));
	    //fw.write(DxfGroup.toString(350, dw.getNextHandle()+4);        // 26
	    fw.write(DxfGroup.toString(  3, "Model"));
	    fw.write(DxfGroup.toString(350, "22"));
	    //fw.write(DxfGroup.toString(350, dw.getNextHandle()+5);        // 22

	    fw.write(DxfGroup.toString(  0, "LAYOUT"));
	    fw.write(DxfGroup.toString(5, "1E"));
	    //dw.handle();                                    // 1E
	    //fw.write(DxfGroup.toString(330, dicId2);                      // 1A
	    fw.write(DxfGroup.toString(100, "AcDbPlotSettings"));
	    fw.write(DxfGroup.toString(  1, ""));
	    fw.write(DxfGroup.toString(  2, "C:\\Program Files\\AutoCAD 2002\\plotters\\DWF ePlot (optimized for plotting).pc3"));
	    fw.write(DxfGroup.toString(  4, ""));
	    fw.write(DxfGroup.toString(  6, ""));
	    fw.write(DxfGroup.toString( 40, 0.0, 4));
	    fw.write(DxfGroup.toString( 41, 0.0, 4));
	    fw.write(DxfGroup.toString( 42, 0.0, 4));
	    fw.write(DxfGroup.toString( 43, 0.0, 4));
	    fw.write(DxfGroup.toString( 44, 0.0, 4));
	    fw.write(DxfGroup.toString( 45, 0.0, 4));
	    fw.write(DxfGroup.toString( 46, 0.0, 4));
	    fw.write(DxfGroup.toString( 47, 0.0, 4));
	    fw.write(DxfGroup.toString( 48, 0.0, 4));
	    fw.write(DxfGroup.toString( 49, 0.0, 4));
	    fw.write(DxfGroup.toString(140, 0.0, 4));
	    fw.write(DxfGroup.toString(141, 0.0, 4));
	    fw.write(DxfGroup.toString(142, 1.0, 4));
	    fw.write(DxfGroup.toString(143, 1.0, 4));
	    fw.write(DxfGroup.toString( 70, 688));
	    fw.write(DxfGroup.toString( 72, 0));
	    fw.write(DxfGroup.toString( 73, 0));
	    fw.write(DxfGroup.toString( 74, 5));
	    fw.write(DxfGroup.toString(  7, ""));
	    fw.write(DxfGroup.toString( 75, 16));
	    fw.write(DxfGroup.toString(147, 1.0, 4));
	    fw.write(DxfGroup.toString(148, 0.0, 4));
	    fw.write(DxfGroup.toString(149, 0.0, 4));
	    fw.write(DxfGroup.toString(100, "AcDbLayout"));
	    fw.write(DxfGroup.toString(  1, "Layout1"));
	    fw.write(DxfGroup.toString( 70, 1));
	    fw.write(DxfGroup.toString( 71, 1));
	    fw.write(DxfGroup.toString( 10, 0.0, 4));
	    fw.write(DxfGroup.toString( 20, 0.0, 4));
	    fw.write(DxfGroup.toString( 11, 420.0, 4));
	    fw.write(DxfGroup.toString( 21, 297.0, 4));
	    fw.write(DxfGroup.toString( 12, 0.0, 4));
	    fw.write(DxfGroup.toString( 22, 0.0, 4));
	    fw.write(DxfGroup.toString( 32, 0.0, 4));
	    fw.write(DxfGroup.toString( 14, 1.000000000000000E+20, 4));
	    fw.write(DxfGroup.toString( 24, 1.000000000000000E+20, 4));
	    fw.write(DxfGroup.toString( 34, 1.000000000000000E+20, 4));
	    fw.write(DxfGroup.toString( 15, -1.000000000000000E+20, 4));
	    fw.write(DxfGroup.toString( 25, -1.000000000000000E+20, 4));
	    fw.write(DxfGroup.toString( 35, -1.000000000000000E+20, 4));
	    fw.write(DxfGroup.toString(146, 0.0, 4));
	    fw.write(DxfGroup.toString( 13, 0.0, 4));
	    fw.write(DxfGroup.toString( 23, 0.0, 4));
	    fw.write(DxfGroup.toString( 33, 0.0, 4));
	    fw.write(DxfGroup.toString( 16, 1.0, 4));
	    fw.write(DxfGroup.toString( 26, 0.0, 4));
	    fw.write(DxfGroup.toString( 36, 0.0, 4));
	    fw.write(DxfGroup.toString( 17, 0.0, 4));
	    fw.write(DxfGroup.toString( 27, 1.0, 4));
	    fw.write(DxfGroup.toString( 37, 0.0, 4));
	    fw.write(DxfGroup.toString( 76, 0));
	    //fw.write(DxfGroup.toString(330, dw.getPaperSpaceHandle());    // 1B
	    fw.write(DxfGroup.toString(330, "1B"));
	    fw.write(DxfGroup.toString(  0, "LAYOUT"));
	    fw.write(DxfGroup.toString(5, "22"));
	    //dw.handle();                                    // 22
	    //fw.write(DxfGroup.toString(330, dicId2);                      // 1A
	    fw.write(DxfGroup.toString(100, "AcDbPlotSettings"));
	    fw.write(DxfGroup.toString(  1, ""));
	    fw.write(DxfGroup.toString(  2, "C:\\Program Files\\AutoCAD 2002\\plotters\\DWF ePlot (optimized for plotting).pc3"));
	    fw.write(DxfGroup.toString(  4, ""));
	    fw.write(DxfGroup.toString(  6, ""));
	    fw.write(DxfGroup.toString( 40, 0.0, 4));
	    fw.write(DxfGroup.toString( 41, 0.0, 4));
	    fw.write(DxfGroup.toString( 42, 0.0, 4));
	    fw.write(DxfGroup.toString( 43, 0.0, 4));
	    fw.write(DxfGroup.toString( 44, 0.0, 4));
	    fw.write(DxfGroup.toString( 45, 0.0, 4));
	    fw.write(DxfGroup.toString( 46, 0.0, 4));
	    fw.write(DxfGroup.toString( 47, 0.0, 4));
	    fw.write(DxfGroup.toString( 48, 0.0, 4));
	    fw.write(DxfGroup.toString( 49, 0.0, 4));
	    fw.write(DxfGroup.toString(140, 0.0, 4));
	    fw.write(DxfGroup.toString(141, 0.0, 4));
	    fw.write(DxfGroup.toString(142, 1.0, 4));
	    fw.write(DxfGroup.toString(143, 1.0, 4));
	    fw.write(DxfGroup.toString( 70, 1712));
	    fw.write(DxfGroup.toString( 72, 0));
	    fw.write(DxfGroup.toString( 73, 0));
	    fw.write(DxfGroup.toString( 74, 0));
	    fw.write(DxfGroup.toString(  7, ""));
	    fw.write(DxfGroup.toString( 75, 0));
	    fw.write(DxfGroup.toString(147, 1.0, 4));
	    fw.write(DxfGroup.toString(148, 0.0, 4));
	    fw.write(DxfGroup.toString(149, 0.0, 4));
	    fw.write(DxfGroup.toString(100, "AcDbLayout"));
	    fw.write(DxfGroup.toString(  1, "Model"));
	    fw.write(DxfGroup.toString( 70, 1));
	    fw.write(DxfGroup.toString( 71, 0));
	    fw.write(DxfGroup.toString( 10, 0.0, 4));
	    fw.write(DxfGroup.toString( 20, 0.0, 4));
	    fw.write(DxfGroup.toString( 11, 12.0, 4));
	    fw.write(DxfGroup.toString( 21, 9.0, 4));
	    fw.write(DxfGroup.toString( 12, 0.0, 4));
	    fw.write(DxfGroup.toString( 22, 0.0, 4));
	    fw.write(DxfGroup.toString( 32, 0.0, 4));
	    fw.write(DxfGroup.toString( 14, 0.0, 4));
	    fw.write(DxfGroup.toString( 24, 0.0, 4));
	    fw.write(DxfGroup.toString( 34, 0.0, 4));
	    fw.write(DxfGroup.toString( 15, 0.0, 4));
	    fw.write(DxfGroup.toString( 25, 0.0, 4));
	    fw.write(DxfGroup.toString( 35, 0.0, 4));
	    fw.write(DxfGroup.toString(146, 0.0, 4));
	    fw.write(DxfGroup.toString( 13, 0.0, 4));
	    fw.write(DxfGroup.toString( 23, 0.0, 4));
	    fw.write(DxfGroup.toString( 33, 0.0, 4));
	    fw.write(DxfGroup.toString( 16, 1.0, 4));
	    fw.write(DxfGroup.toString( 26, 0.0, 4));
	    fw.write(DxfGroup.toString( 36, 0.0, 4));
	    fw.write(DxfGroup.toString( 17, 0.0, 4));
	    fw.write(DxfGroup.toString( 27, 1.0, 4));
	    fw.write(DxfGroup.toString( 37, 0.0, 4));
	    fw.write(DxfGroup.toString( 76, 0));
	    //fw.write(DxfGroup.toString(330, dw.getModelSpaceHandle());    // 1F
	    fw.write(DxfGroup.toString(330, "1F"));
	    fw.write(DxfGroup.toString(  0, "LAYOUT"));
	    //dw.handle();                                    // 26
	    fw.write(DxfGroup.toString(5, "26"));
	    //fw.write(DxfGroup.toString(330, dicId2);                      // 1A
	    fw.write(DxfGroup.toString(100, "AcDbPlotSettings"));
	    fw.write(DxfGroup.toString(  1, ""));
	    fw.write(DxfGroup.toString(  2, "C:\\Program Files\\AutoCAD 2002\\plotters\\DWF ePlot (optimized for plotting).pc3"));
	    fw.write(DxfGroup.toString(  4, ""));
	    fw.write(DxfGroup.toString(  6, ""));
	    fw.write(DxfGroup.toString( 40, 0.0, 4));
	    fw.write(DxfGroup.toString( 41, 0.0, 4));
	    fw.write(DxfGroup.toString( 42, 0.0, 4));
	    fw.write(DxfGroup.toString( 43, 0.0, 4));
	    fw.write(DxfGroup.toString( 44, 0.0, 4));
	    fw.write(DxfGroup.toString( 45, 0.0, 4));
	    fw.write(DxfGroup.toString( 46, 0.0, 4));
	    fw.write(DxfGroup.toString( 47, 0.0, 4));
	    fw.write(DxfGroup.toString( 48, 0.0, 4));
	    fw.write(DxfGroup.toString( 49, 0.0, 4));
	    fw.write(DxfGroup.toString(140, 0.0, 4));
	    fw.write(DxfGroup.toString(141, 0.0, 4));
	    fw.write(DxfGroup.toString(142, 1.0, 4));
	    fw.write(DxfGroup.toString(143, 1.0, 4));
	    fw.write(DxfGroup.toString( 70, 688));
	    fw.write(DxfGroup.toString( 72, 0));
	    fw.write(DxfGroup.toString( 73, 0));
	    fw.write(DxfGroup.toString( 74, 5));
	    fw.write(DxfGroup.toString(  7, ""));
	    fw.write(DxfGroup.toString( 75, 16));
	    fw.write(DxfGroup.toString(147, 1.0, 4));
	    fw.write(DxfGroup.toString(148, 0.0, 4));
	    fw.write(DxfGroup.toString(149, 0.0, 4));
	    fw.write(DxfGroup.toString(100, "AcDbLayout"));
	    fw.write(DxfGroup.toString(  1, "Layout2"));
	    fw.write(DxfGroup.toString( 70, 1));
	    fw.write(DxfGroup.toString( 71, 2));
	    fw.write(DxfGroup.toString( 10, 0.0, 4));
	    fw.write(DxfGroup.toString( 20, 0.0, 4));
	    fw.write(DxfGroup.toString( 11, 12.0, 4));
	    fw.write(DxfGroup.toString( 21, 9.0, 4));
	    fw.write(DxfGroup.toString( 12, 0.0, 4));
	    fw.write(DxfGroup.toString( 22, 0.0, 4));
	    fw.write(DxfGroup.toString( 32, 0.0, 4));
	    fw.write(DxfGroup.toString( 14, 0.0, 4));
	    fw.write(DxfGroup.toString( 24, 0.0, 4));
	    fw.write(DxfGroup.toString( 34, 0.0, 4));
	    fw.write(DxfGroup.toString( 15, 0.0, 4));
	    fw.write(DxfGroup.toString( 25, 0.0, 4));
	    fw.write(DxfGroup.toString( 35, 0.0, 4));
	    fw.write(DxfGroup.toString(146, 0.0, 4));
	    fw.write(DxfGroup.toString( 13, 0.0, 4));
	    fw.write(DxfGroup.toString( 23, 0.0, 4));
	    fw.write(DxfGroup.toString( 33, 0.0, 4));
	    fw.write(DxfGroup.toString( 16, 1.0, 4));
	    fw.write(DxfGroup.toString( 26, 0.0, 4));
	    fw.write(DxfGroup.toString( 36, 0.0, 4));
	    fw.write(DxfGroup.toString( 17, 0.0, 4));
	    fw.write(DxfGroup.toString( 27, 1.0, 4));
	    fw.write(DxfGroup.toString( 37, 0.0, 4));
	    fw.write(DxfGroup.toString( 76, 0));
	    //fw.write(DxfGroup.toString(330, dw.getPaperSpace0Handle());   // 23
	    fw.write(DxfGroup.toString(330, "23"));
	    fw.write(DxfGroup.toString(  0, "DICTIONARY"));
	    fw.write(DxfGroup.toString(5, 0x2E));
	    //dicId5 =
	    //dw.handle();                           // 2C
	    //fw.write(DxfGroup.toString(330, 0xC);                       // C
	    fw.write(DxfGroup.toString(100, "AcDbDictionary"));
	    fw.write(DxfGroup.toString(281, 1));
	    fw.write(DxfGroup.toString(  3, "DIMASSOC"));
	    //fw.write(DxfGroup.toString(350, 0x2F);
	    //fw.write(DxfGroup.toString(350, dw.getNextHandle()+1);        // 2E
	    fw.write(DxfGroup.toString(350, 0x30));        // 2E
	    fw.write(DxfGroup.toString(  3, "HIDETEXT"));
	    //fw.write(DxfGroup.toString(350, 0x2E);
	    //fw.write(DxfGroup.toString(350, dw.getNextHandle());        // 2D
	    fw.write(DxfGroup.toString(350, 0x2F));        // 2D
	    fw.write(DxfGroup.toString(  0, "DICTIONARYVAR"));
	    fw.write(DxfGroup.toString(5, 0x2F));
	    //dw.handle();                                    // 2E
	    //fw.write(DxfGroup.toString(330, dicId5);                      // 2C
	    fw.write(DxfGroup.toString(100, "DictionaryVariables"));
	    fw.write(DxfGroup.toString(280, 0));
	    fw.write(DxfGroup.toString(  1, 2));
	    fw.write(DxfGroup.toString(  0, "DICTIONARYVAR"));
	    fw.write(DxfGroup.toString(5, 0x30));
	    //dw.handle();                                    // 2D
	    //fw.write(DxfGroup.toString(330, dicId5);                      // 2C
	    fw.write(DxfGroup.toString(100, "DictionaryVariables"));
	    fw.write(DxfGroup.toString(280, 0));
	    fw.write(DxfGroup.toString(  1, 1));
		fw.write(DxfGroup.toString(0, "ENDSEC"));
	}

    /**
     * Habilita la reproyección cartográfica
     */
	public void reProject(ICoordTrans rp) {
		System.out.println("Dxf: reproyectando ...");
		entityMaker.reProject(rp);
	}

	/* (non-Javadoc)
	 * @see org.cresques.io.GeoFile#close()
	 */
	public void close() {
		// TODO Auto-generated method stub

	}

	/**
     * Informa sobre si estamos trabajando con el modelo de datos GIS o con el de CAD
	 * @return Returns the cadFlag.
	 */
	public boolean isCadFlag() {
		return cadFlag;
	}

	/**
     * Establece si trabajamos con el modelo de datos GIS o con el de CAD
	 * @param cadFlag The cadFlag to set.
	 */
	public void setCadFlag(boolean cadFlag) {
		this.cadFlag = cadFlag;
	}

    /**
     * @return Returns the dxf3DFlag.
     */
    public boolean isDxf3DFlag() {
        return dxf3DFlag;
    }

    /**
     * @param dxf3DFlag The dxf3DFlag to set.
     */
    public void setDxf3DFlag(boolean dxf3DFlag) {
        this.dxf3DFlag = dxf3DFlag;
    }
}