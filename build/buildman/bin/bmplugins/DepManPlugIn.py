from bmbase.IPlugIn import IPlugIn
from bmbase.PlugInManager import PlugInManager
from bmcore.BMUtil import BMUtil
from xml.dom import minidom
import os
import sys
import platform
import string
import commands



class DepManPlugIn(IPlugIn):
	def __init__(self):
		IPlugIn.__init__(self)
		self._depmanDataPath = sys.path[0]+os.path.sep+".."+os.path.sep+"plugins-data"+os.path.sep+"depman"
		self._supportedPlatforms = ["all","mac","linux","win"]
		self._supportedArchs = ["all","i386","ppc","ppc64","x86_64","universal"]
		self._supportedLibraryTypes = ["none","dynamic","static","framework"]
		self._supportedCompilers = ["all","vs6","vs7","vs8","vs9","gcc3","gcc4"]
		#tries to guess host OS values
		self._osplatform="unknown"
		self._osdistribution="unknown"
		self._osarch="unknown"
		self._oscompiler="unknown"
		if sys.platform=="darwin":
			self._osplatform="mac"
			self._osarch="universal"
			self._oscompiler="gcc4"
			self._osdistribution, null, null=platform.mac_ver()
		elif sys.platform == "linux2" or sys.platform == "linux1":
			self._osplatform="linux"
			self._osarch="i386"
			self._oscompiler="gcc4"
			self._osdistribution, null, null = platform.dist()
			if self._osdistribution == "debian" or self._osdistribution == "Ubuntu":
				self._osdistribution = string.replace(commands.getoutput("lsb_release -ds")," ","-")
		elif sys.platform == "win32" or sys.platform == "win64":
			self._osplatform="win"
			self._osarch="i386"
			self._oscompiler="vs8"
			self._osdistribution,null,null,null,null = sys.getwindowsversion()

		self._getDepManPath = False
		
		
		self._file = "depman.xml"
		self._node = None


	def init(self):
		self.addGoal("depman", "depman information plugin")
		self.addGoalOption("depman","--depman-path", "returns depman path")
		self.addGoalOption("depman","--file", "changes the default file")

		if self._arguments.read("depman"):
			self.setExecute(True)

		if self._arguments.read("--depman-path"):
			self._getDepManPath = True
		
		args=[""]
		while self._arguments.read("--file",args):
			self._file = args[0]

	def initFromXML(self,node):
		self._node = node

	def execute(self):
		self.loadXMLFile(self._file)
		if self._getDepManPath:
			print self.getDepManPath()

		

		

	def getSupportedPlatforms(self):
		return self._supportedPlatforms

	def getSupportedArchs(self):
		return self._supportedArchs

	def getSupportedLibraryTypes(self):
		return self._supportedLibraryTypes

	def getSupportedCompilers(self):
		return self._supportedCompilers

	def getOSPlatform(self):
		return self._osplatform

	def getOSDistribution(self):
		return self._osdistribution

	def getOSArch(self):
		return self._osarch
	
	def getOSCompiler(self):
		return self._oscompiler

	def getDepManPath(self):
		dr = os.getenv("DEPMAN_REPO")
		if not dr is None:
			return dr;
		if sys.platform == "linux2" or sys.platform == "linux1":
			dr = os.getenv("HOME")+os.path.sep+".depman"
			return dr;
		if sys.platform == "win32" or sys.platform == "win64":
			#ex: c:\documents and setting\user\DepMan
			dr = os.getenv("USERPROFILE")+os.path.sep+"DepMan"
			return dr;
		if sys.platform == "darwin":
			dr = os.path.sep + "Developer" + os.path.sep + "DepMan"
			return dr;
		return None
	
	def getMavenPath(self):
		dr = os.getenv("M2_REPO")
		if not dr is None:
			return dr;
		if sys.platform == "linux2" or sys.platform == "linux1":
			dr = os.getenv("HOME")+os.path.sep+".m2"
			return dr;
		if sys.platform == "win32" or sys.platform == "win64":
			#ex: c:\documents and setting\user\DepMan
			dr = os.getenv("USERPROFILE")+os.path.sep+".m2"
			return dr;
		if sys.platform == "darwin":
			dr = os.getenv("HOME")+os.path.sep+".m2"
			return dr;
		return None
	
	def getDepManDataPath(self):
		return self._depmanDataPath

	def getNode(self):
		return self._node

	def validateDependency(self,dependency):
		if dependency.group == "":
			self.reportError("* Group cannot be empty")
			return False
		if dependency.artifact == "":
			self.reportError("* Artifact cannot be empty")
			return False
		if dependency.version == "":
			self.reportError("* Version cannog be empty")
			return False

		if dependency.platform not in self.getSupportedPlatforms():
			self.reportError("* Platform not supported: " + dependency.platform)
			return False
		
		if dependency.platform!="all":
	
			if dependency.compiler not in self.getSupportedCompilers():
				self.reportError("* Compiler not supported: "+ dependency.compiler)
				return False
	
			if dependency.arch not in self.getSupportedArchs():
				self.reportError("* Architecture not supported: "+ dependency.arch)
				return False
	
			if dependency.libraryType not in self.getSupportedLibraryTypes():
				self.reportError("* Library type not supported: " + dependency.libraryType)
				return False

		if dependency.platform!=self.getOSPlatform() and dependency.platform!="all":
			print "* Warning: Forced platform ",dependency.platform

		return True

	def getXMLFile(self):
		return self._file	

PlugInManager().registerPlugIn("DepManPlugIn",DepManPlugIn())


