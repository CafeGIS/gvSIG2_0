from xml.dom import minidom
from PlugInManager import *
import sys

'''
	Defines a interface for plugins
'''
class IPlugIn:
	def __init__(self):
		self._errorMessages = []
		self._error = False
		self._id = ""
		self._goals = dict()
		self._preGoals = dict()
		self._arguments = None
		self._execute = False

	def initArguments(self,arguments):
		self._arguments = arguments

	def init(self):
		pass

	def initFromXML(self,node):
		pass
	
	def execute(self):
		pass
	
	def loadXMLFile(self,xmlfile):
		xmldoc=minidom.parse(xmlfile)
		self.initFromXML(xmldoc)
	
	def getExecute(self):
		return self._execute
	
	def setExecute(self,value):
		self._execute = value
		
	def run(self):
		#try:
			if self._execute:	
				self.executePreGoals()
				print "Executing Plugin:" + str(self.__class__)
				self.execute()
		#except:
		#	self.reportError(str(sys.exc_info()[0]))

	def reportError(self,s):
		self._errorMessages.append(str(self.__class__)+":"+s)
		self._error = True

	def error(self):
		return self._error

	def getErrorMessages(self):
		return self._errorMessages

	def setId(self,id):
		self._id = id

	def getId(self):
		return self._id

	def addGoal(self,goal,description):
		self._goals[goal] = []
		self._preGoals[goal] = []
		self._arguments.getApplicationUsage().addCommandLineGoal(goal,description)
	
	def addGoalOption(self,goal,option,description):
		self._goals[goal].append(option)
		self._arguments.getApplicationUsage().addCommandLineGoalOption(goal,option,description)

	def getGoals(self):
		return self._goals

	def addPreGoal(self, goal, preGoal):
		self._preGoals[goal].append(preGoal)

	def executePreGoals(self):
		for goal in self._preGoals.keys(): 
			goals = self._preGoals[goal]
			for g in goals:
				PlugInManager().getPlugInInstanceByGoal(g).executePreGoals()
				print "---- Executing Plugin:" + str(PlugInManager().getPlugInInstanceByGoal(g).__class__)
				PlugInManager().getPlugInInstanceByGoal(g).execute()

