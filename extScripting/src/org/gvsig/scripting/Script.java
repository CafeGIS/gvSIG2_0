package org.gvsig.scripting;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFException;

public class Script {
	
    public static final String JAVASCRIPT = ScriptingExtension.JAVASCRIPT;
    public static final String BEANSHELL = ScriptingExtension.BEANSHELL;
    public static final String JYTHON = ScriptingExtension.JYTHON;
    public static final String GROOVY = ScriptingExtension.GROOVY;
    
	protected BSFEngine bsfEngine;
	protected String language;
	protected String fileName;
	protected Map params;
	protected String codeString;
	protected File file;	
	
	public Script(Map params) throws Throwable {
		this.params = params;
		if (!params.containsKey("fileName")) {
			//TODO:
			throw new Exception();
		}
		this.fileName = (String)params.get("fileName");
		if (!params.containsKey("language")) {
			//TODO:
			throw new Exception();
		}
		this.language = (String)params.get("language");
		this.params = params;
		this.initialize();		
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
	
	private void initialize() throws BSFException, IOException {
		bsfEngine = ScriptingExtension.getBSFManager().loadScriptingEngine(this.language);
		this.file = new File(this.fileName);
		if (!this.file.exists()) {
			//TODO:
			throw new IOException();
		}
		this.codeString = this.readFile(this.file);
		
		
	}
	
	public void run() throws Throwable {
		ScriptingExtension.getBSFManager().declareBean("params",this.params,this.params.getClass());
		
        if (JYTHON.equals(this.language)){
        	this.runStartupScripJython();
            this.bsfEngine.exec(this.fileName, -1, -1, this.codeString);
        }else{
            this.bsfEngine.eval(this.fileName, -1, -1, this.codeString);
        }
	}
	
	private void runStartupScripJython() throws IOException, BSFException {
		String startUpFileName = ScriptingExtension.getScriptingAppAccesor().getScriptsDirectory();
		startUpFileName = startUpFileName + File.separatorChar+ "jython"+ File.separatorChar+"startup.py";
		File startupFile = new File(startUpFileName);
		
		if (!startupFile.exists()) {
			throw new IOException("Startup File "+startUpFileName+" not found");
			//return;
		}
		String startupCode = this.readFile(startupFile);
		this.bsfEngine.exec(startUpFileName, -1, -1, startupCode);
	}
}
