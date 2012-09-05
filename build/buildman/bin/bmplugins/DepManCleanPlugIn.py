from bmbase.IPlugIn import IPlugIn
from bmbase.PlugInManager import PlugInManager
from bmcore.BMUtil import BMUtil
from xml.dom import minidom
import os

class DepManCleanPlugIn(IPlugIn):
	def __init__(self):
		IPlugIn.__init__(self)
		self._delete_cache = False
		self._default_repository = ""

	def init(self):
		self.addGoal("clean", "Cleans the repository")
		self.addGoalOption("clean","--all", "Cleans also cache files")
	
		isClean = False
		if self._arguments.read("clean"):
			self.setExecute(True)
			isClean = True

		if not isClean:
			return	
		
		if self._arguments.read("--all"):
			self._delete_cache = True	

	def initFromXML(self,dom):
		node = dom.getElementsByTagName("clean")[0]
		if node != None:
			if node.localName=="clean":
				if node.hasAttributes():
					if node.attributes.has_key("all"):
						value = node.attributes.get("all").value
						if value=="True" or value == "true":
							self._delete_cache = True

	def execute(self):
		print "Executing Plugin:" + str(self.__class__)
		return self.clean()

	def clean(self):
		self._dmplugin = PlugInManager().getPlugInInstance("DepManPlugIn")
		if self._dmplugin == None:
			self.reportError("PlugIn `depman` not found")
			self.setExecute(False)
			return
		self._default_repository = self._dmplugin.getDepManPath()
		#the extra parameter "all" enables dmn to clear the cache directory
		print "* cleaning..."
		util = BMUtil()
		util.rmdir(self._default_repository+os.path.sep+"include")
		util.rmdir(self._default_repository+os.path.sep+"lib")
		util.rmdir(self._default_repository+os.path.sep+"bin")
		util.rmdir(self._default_repository+os.path.sep+"share")
		util.rmdir(self._default_repository+os.path.sep+"Frameworks")
		if self._delete_cache:
			util.rmdir(self._default_repository+os.path.sep+".cache")
		return True
	
	def setDeleteCache(self,value):
		self._delete_cache = value
		
	def getDeleteCache(self):
		return self._delete_cache
	
	def setDefaultRepository(self,defrepo):
		self._default_repository = defrepo
		
	def getDefaultRepository(self):
		return self._default_repository


		

PlugInManager().registerPlugIn("DepManCleanPlugIn",DepManCleanPlugIn())


