from xml.dom import minidom

'''
	Defines a interface for plugins
'''
class IPlugIn:
	def __init__(self):
		self._errorMessages = []
		self._error = False
		self._id = ""
		self._goals = dict()
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
		node = xmldoc.firstChild
		self.initFromXML(node)
	
	def getExecute(self):
		return self._execute
	
	def setExecute(self,value):
		self._execute = value
		
	def run(self):
		if self._execute:
			self.execute()

	
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
		self._arguments.getApplicationUsage().addCommandLineGoal(goal,description)
	
	def addGoalOption(self,goal,option,description):
		self._goals[goal].append(option)
		self._arguments.getApplicationUsage().addCommandLineGoalOption(goal,option,description)

	def getGoals(self):
		return self._goals

