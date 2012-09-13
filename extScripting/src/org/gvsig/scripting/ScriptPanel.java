package org.gvsig.scripting;

import java.awt.GridLayout;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import javax.swing.JPanel;

import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFException;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

public class ScriptPanel extends JPanel implements IWindow {

    public static final String JAVASCRIPT = ScriptingExtension.JAVASCRIPT;
    public static final String BEANSHELL = ScriptingExtension.BEANSHELL;
    public static final String JYTHON = ScriptingExtension.JYTHON;
    public static final String GROOVY = ScriptingExtension.GROOVY;

	private WindowInfo viewInfo;
	private String xmlFileName;
	private String language;
	private String title = null;
	private int viewType = WindowInfo.RESIZABLE;
	//private int viewType = ViewInfo.MODALDIALOG;
	private int top = 0;
	private int left = 0;
	private int width = -1;
	private int height = -1;
	private Map params;

	private DefaultThinlet thinlet = null;

	public ScriptPanel(Map params) throws BSFException, IOException {
		super();
		this.params = params;
		if (!params.containsKey("fileName")) {
			throw new IOException(); //FIXME !!!!
		}
		this.xmlFileName = (String)params.get("fileName");
		if (!params.containsKey("language")) {
			throw new IOException(); //FIXME !!!!
		}
		this.language = (String)params.get("language");

		this.title =(String)params.get("title");

		this.viewType = params.containsKey("viewType") ? ((Integer)params.get("viewType")).intValue(): this.viewType;

		this.top = params.containsKey("top") ? ((Integer)params.get("top")).intValue(): this.top;
		this.left = params.containsKey("left") ? ((Integer)params.get("left")).intValue(): this.left;
		this.width = params.containsKey("width") ? ((Integer)params.get("width")).intValue(): this.width;
		this.height = params.containsKey("height") ? ((Integer)params.get("height")).intValue(): this.height;
		this.initilize();
	}

	/*
	public ScriptPanel(String xmlFileName, String language) throws BSFException, IOException {
		super();
		this.xmlFileName = xmlFileName;
		this.language = language;
		this.initilize();
	}

	public ScriptPanel(String xmlFileName, String language, String title ) throws BSFException, IOException  {
		super();
		this.xmlFileName = xmlFileName;
		this.language = language;
		this.title = title;
		this.initilize();
	}

	public ScriptPanel(String xmlFileName, String language, String title,int viewType ) throws BSFException, IOException  {
		super();
		this.xmlFileName = xmlFileName;
		this.language = language;
		this.title = title;
		this.viewType = viewType;
		this.initilize();
	}
	*/

	private void initilize() throws BSFException, IOException {
		this.setLayout(new GridLayout());

		/*
		if (this.top > 0 && this.left > 0) {
			this.setLocation(this.top,this.left);
		}
		*/
		File file = new File(this.xmlFileName);
		if (!file.exists()) {
			throw new IOException("File "+ file.getAbsolutePath()+ " not found.");
		}
		if (this.title == null) {
			this.title = this.xmlFileName;
		}
		this.thinlet = ScriptPanel.getNewScriptableThinletInstance(this.language);

		ScriptingExtension.getBSFManager().declareBean("params",this.params, this.params.getClass());

		BSFEngine bsfEngine = ScriptingExtension.getBSFManager().loadScriptingEngine(this.language);

		if (this.language.equals(JYTHON)) {
			this.runStartupScripJython(bsfEngine);
		}

		this.thinlet.add(this.thinlet.parse(file));


		this.add(this.thinlet);

		this.thinlet.setVisible(true);
		this.doLayout();
		this.setVisible(true);

		/*
		if (this.width > 0 && this.height > 0) {
			this.setSize(this.width, this.height);
		}else {
			this.setSize(thinlet.getWidth() ,thinlet.getHeight()+2);
		}
		*/


	}

	public DefaultThinlet getThinlet() {
		return this.thinlet;
	}

	public WindowInfo getWindowInfo() {
        if (viewInfo == null) {
            viewInfo=new WindowInfo(this.viewType);
            viewInfo.setTitle(this.title);
            viewInfo.setWidth(this.width);
            viewInfo.setHeight(this.height);
        }
        return viewInfo;
	}

	protected static DefaultThinlet getNewScriptableThinletInstance(String language) throws BSFException {
		DefaultThinlet thinlet = new DefaultThinlet(language);
		//thinlet.getBSFManager().setClassLoader(PluginServices.getPluginServices("com.iver.cit.gvsig").getClassLoader());
		//thinlet.getBSFManager().setClassLoader(PluginServices.ge);
		return thinlet;
	}

	private void runStartupScripJython(BSFEngine bsfEngine) throws IOException, BSFException {
		String startUpFileName = ScriptingExtension.getScriptingAppAccesor().getScriptsDirectory();
		startUpFileName = startUpFileName + File.separatorChar+ "jython"+ File.separatorChar+"startup.py";
		File startupFile = new File(startUpFileName);

		if (!startupFile.exists()) {
			throw new IOException("Startup File "+startUpFileName+" not found");
			//return;
		}
		String startupCode = this.readFile(startupFile);
		bsfEngine.exec(startUpFileName, -1, -1, startupCode);
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

	public void close() {
		PluginServices.getMDIManager().closeWindow(this);
	}

	public Object getWindowProfile() {
		return WindowInfo.TOOL_PROFILE;
	}

}
