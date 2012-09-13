from bmbase.IPlugIn import IPlugIn
from bmbase.PlugInManager import PlugInManager
from bmcore.BMUtil import BMUtil
import os

class CreateSolutionPlugIn(IPlugIn):
	def __init__(self):
		IPlugIn.__init__(self)
		self._buildType = "Release"
		self._supportedGenerators = dict()
		self._supportedGenerators["vs8"] = "Visual Studio 8 2005"
		self._supportedGenerators["vs7"] = "Visual Studio 7 .NET 2003"
		self._supportedGenerators["unix"] = "Unix Makefiles"
		self._supportedGenerators["xcode"] = "Xcode"
		self._cmakeGenerator = ""
		self._projectPath = ""
		self._projectGenerationPath = ""
		self._installPath = ""
		self._cmakeOtherOptions = ""
		

	def init(self):
		self.addGoal("create-solution", "Creates the solution Makefiles/VisualStudio for Win/Linux/Mac based on CMake")
		self.addGoalOption("create-solution","--build-type", "sets the build path (Release/Debug). default=Release.")
		self.addGoalOption("create-solution","--generator", "selects the generator (vs8,vs7,unix,xcode).")
		self.addGoalOption("create-solution","--project-path", "sets the path of the project.")
		self.addGoalOption("create-solution","--project-gen-path", "sets the path to generate project files. default=${project-path}/CMake")
		self.addGoalOption("create-solution","--install", "sets the path to generate the product installation. default=${project-path}/../install")
		self.addGoalOption("create-solution","--cmake-options", "Adds other cmake command line options.")
		execute = False		
		if self._arguments.read("create-solution"):
			execute = True
		if execute:
			args = [""]
			if self._arguments.read("--build-type",args):
				self._buildType = args[0]
		
			args = [""]
			if self._arguments.read("--generator",args):
				if args[0] in self._supportedGenerators:
					self._cmakeGenerator = self._supportedGenerators[args[0]]

			args = [""]
			if self._arguments.read("--project-path",args):
				self._projectPath=args[0]
		
			args = [""]
			if self._arguments.read("--project-gen-path",args):
				self._projectGenerationPath=args[0]
	
			args = [""]
			if self._arguments.read("--install",args):
				self._installPath=args[0]
				
			args = [""]
			if self._arguments.read("--cmake-options",args):
				self._cmakeOtherOptions=args[0]
				
			self.setExecute(True)
			
	def initFromXML(self,node):
		if node.localName=="create-solution":
			if node.hasAttributes():
				if node.attributes.has_key("build-type"):
					self._buildType = node.attributes.get("build-type").value
				if node.attributes.has_key("generator"):
					gen = node.attributes.get("generator").value
					if gen in self._supportedGenerators:
						self._cmakeGenerator = self._supportedGenerators[gen]
				if node.attributes.has_key("project-path"):
					self._projectPath = node.attributes.get("project-path").value
				if node.attributes.has_key("project-gen-path"):
					self._projectGenerationPath = os.path.abspath(node.attributes.get("project-gen-path").value)
				if node.attributes.has_key("install"):
					self._installPath = os.path.abspath(node.attributes.get("install").value)
				if node.attributes.has_key("cmake-options"):
					self._cmakeOtherOptions = node.attributes.get("cmake-options").value
										
	def execute(self):
		if self._cmakeGenerator=="":
			self.reportError("Missing valid generator option")
			return False
		if self._projectPath=="":
			self.reportError("Missing required project path option")
			return False
			
		if self._cmakeGenerator!="" and self._projectPath!="":
			self._projectPath = os.path.abspath(self._projectPath)
			projectName = os.path.basename(self._projectPath)
			if self._projectGenerationPath=="":
				self._projectGenerationPath=os.path.abspath(os.path.join(self._projectPath,"BMCMake"))
			if self._installPath=="":
				self._installPath=os.path.abspath(self._projectPath+os.path.sep+".."+os.path.sep+projectName+"-install")
			if self._buildType=="":
				self._buildType="Release"
				
		print "Executing Plugin:" + str(self.__class__)
		bmutil = BMUtil()
		print " * Creating Project Generation Path: "+self._projectGenerationPath
		bmutil.mkdir(self._projectGenerationPath)
		olddir = os.getcwd()
		os.chdir(self._projectGenerationPath)
		if os.path.exists("CMakeCache.txt"):
			os.remove("CMakeCache.txt")
		cmakeCommand = "cmake "+self._cmakeOtherOptions+" -G\""+self._cmakeGenerator+"\" -DCMAKE_BUILD_TYPE="+self._buildType+" -DCMAKE_INSTALL_PREFIX="+self._installPath+" "+self._projectPath 
		print " * Running CMAKE: " + cmakeCommand
		os.system(cmakeCommand)
		os.chdir(olddir)
		return True

PlugInManager().registerPlugIn("CreateSolutionPlugIn",CreateSolutionPlugIn())


