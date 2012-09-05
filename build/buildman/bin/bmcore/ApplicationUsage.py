"""
	ApplicationUsage manage the options of the application
	and plugins.
"""
class ApplicationUsage:
	def __init__(self):
		self._applicationName = ""
		self._commandLineOptions = []
		self._commandLineGoals = dict()
		self._commandLineGoalsDescriptions = dict()
		self._description = ""

	def setApplicationName(self,name):
		self._applicationName = name

	def getApplicationName(self):
		return self._applicationName

	def addCommandLineGoalOption(self,goal,option,description):
		self._commandLineGoals[goal].append((option,description))

	def addCommandLineGoal(self,goal,description):
		self._commandLineGoals[goal] = []
		self._commandLineGoalsDescriptions[goal] = description

	def addCommandLineOption(self,option,description):
		self._commandLineOptions.append((option,description))

	def getCommandLineGoalDescription(self,goal):
		if goal in self._commandLineGoalsDescriptions:
			return self._commandLineGoalsDescriptions[goal]
		return None

	def getCommandLineGoals(self):
		return self._commandLineGoals

	def getCommandLineGoalOptions(self,goal):
		return self._commandLineGoals[goal]

	def getCommandLineOptionDescription(self,option):
		for v in self._commandLineOptions:
			(opt,desc) = v
			if opt == option:
				return desc
		return None

	def getCommandLineOptions(self):
		return self._commandLineOptions

	def setDescription(self, str):
		self._description = str

	def getDescription(self):
		return self._description


