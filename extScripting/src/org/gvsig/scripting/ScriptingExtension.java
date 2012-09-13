package org.gvsig.scripting;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.Map;

import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.gvsig.scripting.xul.XULInfoToolSupport;
import org.gvsig.scripting.xul.XULScriptablePanel;

import thinlet.script.ScriptableThinlet;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.info.gui.InfoToolViewer;

public class ScriptingExtension extends Extension {
	private static BSFManager bsfManager;

    public static final String JAVASCRIPT = ScriptableThinlet.JAVASCRIPT;
    public static final String BEANSHELL = ScriptableThinlet.BEANSHELL;
    public static final String JYTHON = ScriptableThinlet.JYTHON;
    public static final String GROOVY = ScriptableThinlet.GROOVY;

    private static ScriptingAppAccesor scriptingAppAccesor = null;


    public static ScriptingAppAccesor getScriptingAppAccesor() {
    	if (scriptingAppAccesor == null) {
    		scriptingAppAccesor =new ScriptingAppAccesor();
    	}

    	return scriptingAppAccesor;
    }



	public static BSFManager getBSFManager() {
        if(bsfManager == null){
        	bsfManager = new BSFManager(){
                public BSFEngine loadScriptingEngine(String lang) throws BSFException {
                    // if its already loaded return that
                    BSFEngine eng = (BSFEngine) loadedEngines.get(lang);
                    if (eng != null) {
                        return eng;
                    }

                    // is it a registered language?
                    String engineClassName = (String) registeredEngines.get(lang);
                    if (engineClassName == null) {
                        throw new BSFException(BSFException.REASON_UNKNOWN_LANGUAGE,
                                               "unsupported language: " + lang);
                    }

                    // create the engine and initialize it. if anything goes wrong
                    try {
                        Class engineClass = (classLoader == null)
                                ? Class.forName(engineClassName)
                                : classLoader.loadClass(engineClassName);
                        eng = (BSFEngine) engineClass.newInstance();
                        eng.initialize(this, lang, declaredBeans);
                        loadedEngines.put(lang, eng);
                        pcs.addPropertyChangeListener(eng);
                        return eng;
                    }
                    catch (Throwable t) {
                        throw new BSFException(BSFException.REASON_OTHER_ERROR,
                                               "unable to load language: " + lang, t);
                    }
                }
            };
        }
        try {
			bsfManager.declareBean("gvSIG", getScriptingAppAccesor(), ScriptingAppAccesor.class);
		} catch (BSFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return bsfManager;
	}

	public void initialize() {
		XULScriptablePanel.setBSFManager( getBSFManager() );
		InfoToolViewer.XULPanelClass = XULInfoToolSupport.class;
		initilializeIcons();
	}

	private void initilializeIcons(){
		PluginServices.getIconTheme().registerDefault(
				"scripting-console",
				ScriptingExtension.class.getResource("console.png")
			);
	}

	public void execute(String actionCommand) {
		ScriptParams params = new ScriptParams(actionCommand);

		if (params.action() == null) {
			// FIXME: FALTA mostara error
			return;
		}
		if (params.action().toLowerCase().equals("show") ) {
			show(params.getParams());
		} else if (params.action().toLowerCase().equals("run") ){
			run(params.getParams());
		}

	}

	public static void run(Map params) {
		Script script;
		try {
			script = new Script(params);
			script.run();
		} catch (BSFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void runStartupScripJython() {
		BSFEngine bsfEngine;
		try {
			bsfEngine = getBSFManager().loadScriptingEngine(JYTHON);
			String startUpFileName = ScriptingExtension.getScriptingAppAccesor().getScriptsDirectory();
			startUpFileName = startUpFileName + File.separatorChar+ "jython"+ File.separatorChar+"startup.py";
			File startupFile = new File(startUpFileName);

			if (!startupFile.exists()) {
				//throw new IOException("Startup File "+startUpFileName+" not found");
				return;
			}
			String startupCode = readFile(startupFile);
			bsfEngine.exec(startUpFileName, -1, -1, startupCode);
		} catch (BSFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String readFile(File aFile) throws IOException {
		StringBuffer fileContents = new StringBuffer();
		FileReader fileReader = new FileReader(aFile);
		int c;
		while ((c = fileReader.read()) > -1) {
			fileContents.append((char)c);
		}
		fileReader.close();
		return fileContents.toString();
	}

	public static Object show(Map params) {
		DefaultThinlet.setBSFManager(getBSFManager());
		ScriptPanel panel;
		try {
			panel = new ScriptPanel(params);
		} catch (BSFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		IWindow v = PluginServices.getMDIManager().addWindow(panel);
		v.getWindowInfo().setWidth(v.getWindowInfo().getWidth()+1);
		return panel;
	}

	private ScriptingAppAccesor newAppAccesor() {
		return new ScriptingAppAccesor();
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isVisible() {
		return true;
	}

	public class ScriptParams {
		private String paramString;
		private String action = null;
		private Hashtable params = null;

		public ScriptParams(String paramString) {
			this.paramString = paramString.trim();
		}

		public Map getParams() {
			if (this.params == null) {
				if (!this.parse()) {
					return null;
				}
			}
			return this.params;
		}

		public String action() {
			if (this.action == null) {
				if (!this.parse()) {
					return null;
				}
			}
			return this.action;
		}

		private boolean parse() {
			//transformar este formato: 'show(fileName='./pepe.xml',language='jython',top=11,left=10,width=300,height=300)'
			int primerParentesis = this.paramString.indexOf("(");
			if (primerParentesis < 1) {
				// TODO Informar error
				return false;
			}
			if (!this.paramString.endsWith(")")) {
				// TODO Informar error
				return false;
			}
			this.action = this.paramString.substring(0,primerParentesis);

			// Dividimos en 'clave=valor'

			this.params = new Hashtable();
			if (primerParentesis+1 == this.paramString.length()-1) {
				return true;
			}

			String[] listaParametros = this.paramString.substring(primerParentesis+1,this.paramString.length()-1).trim().split(",");




			for (int i=0;i < listaParametros.length; i++) {
				// Dividimos en clave y valor
				String[] parametroConjunto = listaParametros[i].trim().split("=");
				if (parametroConjunto.length != 2) {
					// TODO Informar error
					return false;
				}
				String clave = parametroConjunto[0].trim();
				String cadenaValor = parametroConjunto[1].trim();

				//Intentamos identificar y transformar el valor en un Objeto Java
				Object valor;
				if (cadenaValor.startsWith("'")) {
					if (cadenaValor.endsWith("'")) {
						// Recortamos las comillas y descodificamos por si tiene algun caracter escapado
						// FIXME arreglar el deprecated
						valor = URLDecoder.decode(cadenaValor.substring(1,cadenaValor.length()-1));
					} else {
						// TODO Informar error
						return false;
					}
				} else{
					try {
						// Si contiene el punto decimal intentamos con un Double
						if (cadenaValor.indexOf('.') >= 0) {
							valor = new Double(cadenaValor);
						} else {
							// Si no debe ser un Entero
							valor = new Integer(cadenaValor);
						}

					} catch (NumberFormatException e) {
						// No se ha podido transformar
						// TODO Informar error
						return false;
					}

				}
				// Añadimos al Map
				this.params.put(clave,valor);


			}

			return true;

		}


	}
}
