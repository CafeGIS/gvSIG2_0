from bmbase.IPlugIn import IPlugIn
from bmbase.PlugInManager import PlugInManager
from bmcore.BMUtil import BMUtil
from xml.dom import minidom
import os
import sys

class DepManPlugIn(IPlugIn):
	def __init__(self):
		IPlugIn.__init__(self)
		self._depmanDataPath = sys.path[0]+os.path.sep+".."+os.path.sep+"plugins-data"+os.path.sep+"depman"
		self._supportedPlatforms = ["all","mac","linux","win"]
		self._supportedArchs = ["all","i386","ppc","ppc64","x86_64","universal"]
		self._supportedLibraryTypes = ["none","dynamic","static","framework"]
		self._supportedCompilers = ["all","vs6","vs7","vs8","gcc3","gcc4"]
		#tries to guess host OS values
		self._osplatform="unknown"
		self._osarch="unknown"
		self._oscompiler="unknown"
		if sys.platform=="darwin":
			self._osplatform="mac"
			self._osarch="universal"
			self._oscompiler="gcc4"
		elif sys.platform == "linux2" or sys.platform == "linux1":
			self._osplatform="linux"
			self._osarch="i386"
			self._oscompiler="gcc4"
		elif sys.platform == "win32" or sys.platform == "win64":
			self._osplatform="win"
			self._osarch="i386"
			self._oscompiler="vs8"

	def init(self):
		pass
	
	def initFromXML(self,node):
		pass

	def execute(self):
		pass

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
	
	def getDepManDataPath(self):
		return self._depmanDataPath

PlugInManager().registerPlugIn("DepManPlugIn",DepManPlugIn())


