from bmbase.IPlugIn import IPlugIn
from bmbase.PlugInManager import PlugInManager
from bmcore.BMUtil import BMUtil
from xml.dom import minidom
import os

class BuildManPlugIn(IPlugIn):
	def __init__(self):
		IPlugIn.__init__(self)
		self.reset()
	
	def getFile(self):
		return self._file
	
	def getGoalList(self):
		return self._goalList
	
	def reset(self):
		self._goalList = []
		self._file = "buildman.xml"

	def init(self):
		self.addGoal("buildman", "executes goals from a buildman file")
		self.addGoalOption("buildman","--file", "Sets the file to execute, otherwise search for buildman.xml")
		
		self._dmplugin = PlugInManager().getPlugInInstance("DepManPlugIn")
		if self._dmplugin == None:
			self.reportError("PlugIn `depman` not found")
			return
		isBuildMan = False
		while self._arguments.read("buildman"):
			self.setExecute(True)
			isBuildMan = True

		if not isBuildMan:
			return	
		
		args=[""]
		while self._arguments.read("--file",args):
			self._file = args[0]	

	def initFromXML(self,dom):	
		node = dom.getElementsByTagName("buildman")[0]
		if node != None:
			if node.localName=="buildman":
				for g in node.childNodes:
					if g.localName == "goals":
						addGoals = False
						if g.hasAttributes():
							if g.attributes.has_key("platform"):
								value = g.attributes.get("platform").value
								platforms = value.split(",")
								for platform in platforms:
									if platform == self._dmplugin.getOSPlatform():
										addGoals = True
						else:
							addGoals = True
						if addGoals:
							for goalNode in g.childNodes:
								#print goalNode.localName, " in ", PlugInManager().supportedGoals(), "?"
								if goalNode.localName=="buildman":
									if goalNode.hasAttributes():
										if goalNode.attributes.has_key("file"):
											self.loadXMLFile(goalNode.attributes.get("file").value)
								else:
									if goalNode.localName in PlugInManager().supportedGoals():
										self._goalList.append(goalNode)

	def execute(self):
		return self.buildman()

	def buildman(self):
		self.loadXMLFile(self._file)
		for goal in self._goalList:
			plugin = PlugInManager().getPlugInInstanceByGoal(goal.localName)
			plugin.initFromXML(goal)
			plugin.execute()
			if plugin.error():
				for error in plugin.getErrorMessages():
					self.reportError(error)
				return False
		return True


PlugInManager().registerPlugIn("BuildManPlugIn",BuildManPlugIn())	
		