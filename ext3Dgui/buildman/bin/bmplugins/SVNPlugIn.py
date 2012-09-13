from bmbase.IPlugIn import IPlugIn
from bmbase.PlugInManager import PlugInManager
from bmcore.BMUtil import BMUtil
import os

class SVNPlugIn(IPlugIn):
	def __init__(self):
		IPlugIn.__init__(self)
		self._options = ['co','up', 'checkout', 'update']
		self._option = ""
		self._projectPath = ""
		self._svnUrl = ""
		

	def init(self):
		self.addGoal("svn", "This plugin is able to do checkout or update of a svn repository")
		self.addGoalOption("svn","--option", "sets the option of subversion: "+ str(self._options))
		self.addGoalOption("svn","--project-path", "sets the path of the project.")
		self.addGoalOption("svn","--url", "sets the path of the url")
		execute = False		
		while self._arguments.read("svn"):
			execute = True
		if execute:
			args = [""]
			while self._arguments.read("--option",args):
				self._option = args[0]

			args = [""]
			while self._arguments.read("--project-path",args):
				self._projectPath=args[0]
		
			args = [""]
			while self._arguments.read("--url",args):
				self._svnUrl=args[0]
				
			self.setExecute(True)
			
	def initFromXML(self,node):
		if node.localName=="svn":
			if node.hasAttributes():
				if node.attributes.has_key("option"):
					self._option = node.attributes.get("option").value
				if node.attributes.has_key("project-path"):
					self._projectPath = node.attributes.get("project-path").value
				if node.attributes.has_key("url"):
					self._svnUrl = node.attributes.get("url").value
										
	def execute(self):
		if self._option=="":
			self.reportError("Missing svn option, use one of " + str(self._options))
			return False
		if self._option not in self._options:
			self.reportError("not supported svn option, use one of " + str(self._options))
			return False
		if self._projectPath=="":
			self.reportError("Missing required project path option")
			return False
		if self._option == "co" or self._option == "checkout":
			if self._svnUrl == "":
				self.reportError("Missing required svn url ption")
				return False
			
		self._projectPath = os.path.abspath(self._projectPath)
		projectName = os.path.basename(self._projectPath)
		
		print "Executing Plugin:" + str(self.__class__)
		bmutil = BMUtil()
		svnCommand = "svn " + self._option
		if self._option in ['co', 'checkout']:
			svnCommand += " " + self._svnUrl + " " + self._projectPath
		else: 
			if self._option in ['up', 'update']:
				svnCommand += " " + self._projectPath
		os.system(svnCommand)
		return True
	
PlugInManager().registerPlugIn("SVNPlugIn",SVNPlugIn())


