from bmbase.IPlugIn import IPlugIn
from bmbase.PlugInManager import PlugInManager
from bmcore.BMUtil import BMUtil
import os
import sys

class CompileSolutionPlugIn(IPlugIn):
	def __init__(self):
		IPlugIn.__init__(self)
		self._targetType = "Release" # only vs7/vs8 compilers
		self._projectPath = ""
		self._install = False
		self._projectName = ""
		self._projectGenerationPath = ""
		self._supportedCompilers = dict()
		self._supportedCompilers["vs8"]="devenv.exe"
		self._supportedCompilers["vs7"]="devenv.exe"
		self._supportedCompilers["gcc4"]="make"
		self._supportedCompilers["gcc3"]="make"
		self._compiler = ""
		

	def init(self):
		self.addGoal("compile-solution", "Compiles the solution based on Makefiles or VisualStudio for Win/Linux/Mac")
		self.addGoalOption("compile-solution", "--project-path", "sets the path of the project.")
		self.addGoalOption("compile-solution", "--project-gen-path", "sets the path of the Makefiles/VS build files. Default: ${project-path}/BMCMake")
		self.addGoalOption("compile-solution", "--project-name", "sets the name of the project. If not given it uses the name of the project directory.")
		self.addGoalOption("compile-solution", "--install", "Executes target install after compilation.")
		self.addGoalOption("compile-solution", "--compiler", "Forces the compiler. If not try to determine using current platform\n" +
															"Supported Compilers:\n" + 
															str(self._supportedCompilers))
		execute = False
		if self._arguments.read("compile-solution"):
			execute = True
		if execute:
			args = [""]
			if self._arguments.read("--project-path", args):
				self._projectPath=args[0]
				
			args = [""]
			if self._arguments.read("--project-gen-path", args):
				self._projectGenerationPath=args[0]
		
			args = [""]
			if self._arguments.read("--project-name", args):
				self._projectName=args[0]

			args = [""]
			if self._arguments.read("--compiler", args):
				if args[0] in self._supportedCompilers:
					self._compiler=args[0]

			if self._arguments.read("--install"):
				self._install=True
				
			self.setExecute(True)
			
	def initFromXML(self, dom):
		node = dom.getElementsByTagName("compile-solution")[0]
		if node != None:
			if node.hasAttributes():
				if node.attributes.has_key("project-path"):
					self._projectPath = node.attributes.get("project-path").value
				if node.attributes.has_key("project-gen-path"):
					self._projectGenerationPath = node.attributes.get("project-gen-path").value
				if node.attributes.has_key("project-name"):
					self._projectName = node.attributes.get("project-name").value
				if node.attributes.has_key("compiler"):
					value = node.attributes.get("compiler").value
					if args[0] in self._supportedCompilers:
						self._compiler = value
 				if node.attributes.has_key("install"):
					value = node.attributes.get("install").value 
					if value == "True" or value == "true":
						self._install = True
										
	def execute(self):
		dmplugin = PlugInManager().getPlugInInstance("DepManPlugIn")
		if dmplugin == None:
			self.reportError("DepMan PlugIn not found")
			return False
		if self._projectPath=="":
			self.reportError("Missing required project path option")
			return False
			
		self._projectPath = os.path.abspath(self._projectPath)
		if self._compiler=="":
			self._compiler = dmplugin.getOSCompiler()
	
		if self._projectName == "":
			self._projectName = os.path.basename(self._projectPath)
			
		if self._projectGenerationPath == "":
			self._projectGenerationPath = os.path.join(self._projectPath, "BMCMake")
		else:
			self._projectGenerationPath = os.path.abspath(self._projectGenerationPath)
		
		platform = dmplugin.getOSPlatform()
		if platform == "linux" or platform == "mac":
			self.__executeUnix()
		else:
			self.__executeVS()	
		return True

	def __executeUnix(self):
		#make install
		print "CPUs Detected:" + str(self.__cpuCount())
		cmdCompiler = self._supportedCompilers[self._compiler] + " -j"+str(self.__cpuCount()) 
		if self._install:	
			cmdCompiler += " install"
		olddir = os.getcwd()
		os.chdir(self._projectGenerationPath)
		os.system(cmdCompiler)
		os.chdir(olddir)	

	def __executeVS(self):
		#devenv.exe /build Release /project INSTALL ../CMake/libjosg.sln
		cmdCompiler = self._supportedCompilers[self._compiler]
		cmdCompiler += " " + self._projectName + ".sln"
		cmdCompiler += " /build Release"
		if self._install:	
			cmdCompiler += " /project INSTALL"
		else:
			cmdCompiler += " /project ALL_BUILD"
		olddir = os.getcwd()
		os.chdir(self._projectGenerationPath)
		os.system(cmdCompiler)
		os.chdir(olddir)	
		
	def __cpuCount(self):
	    '''
	    Returns the number of CPUs in the system
	    '''
	    if sys.platform == 'win32':
	        try:
	            num = int(os.environ['NUMBER_OF_PROCESSORS'])
	        except (ValueError, KeyError):
	            pass
	    elif sys.platform == 'darwin':
	        try:
	            num = int(os.popen('sysctl -n hw.ncpu').read())
	        except ValueError:
	            pass
	    else:
	        try:
	            num = os.sysconf('SC_NPROCESSORS_ONLN')
	        except (ValueError, OSError, AttributeError):
	            pass
	        
	    if num >= 1:
	        return num
	    return 1


PlugInManager().registerPlugIn("CompileSolutionPlugIn", CompileSolutionPlugIn())


