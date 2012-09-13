from ApplicationUsage import ApplicationUsage
from bmbase.PlugInManager import PlugInManager


"""
	ArgumentParser is a helper in the task of 
	reading parameters from command line.
"""
class ArgumentParser:
	def __init__(self,argv):
		self._argv = argv
		self._argc = len(self._argv)
		self._error = False
		self._errorMessages = []
		self._applicationUsage = ApplicationUsage()
		self._applicationUsage.setApplicationName(self.getApplicationName())

	def errors(self):
		return self._error

	def writeErrorMessages(self):
		for error in self._errorMessages:
			print error + "\n"

	def writeGoalHelpMessages(self,goal):
		if goal in self._applicationUsage.getCommandLineGoals().keys():
			print "Goal supported by " + str(PlugInManager().getPlugInInstanceByGoal(goal).__class__) + " plugin:"
			print goal + "\t" + self._applicationUsage.getCommandLineGoalDescription(goal) + "\n"
			for option,value in self._applicationUsage.getCommandLineGoalOptions(goal):
				print "\t" + option + "\t" + value + "\n"
		else:
			print "Goal `" + goal +"` not supported, try --help-all option" 

	def writeHelpMessages(self):
		print "BuildMan options:"
		print "-----------------" + "\n"
		for option,value in self._applicationUsage.getCommandLineOptions():
			print option + "\t" + value + "\n"
		if len(self._applicationUsage.getCommandLineGoals().keys())>0:
			print "Supported Goals:"
			print "----------------" + "\n"
			for goal in self._applicationUsage.getCommandLineGoals().keys():
				print goal + "\t" + self._applicationUsage.getCommandLineGoalDescription(goal) + "\n"
				for option,value in self._applicationUsage.getCommandLineGoalOptions(goal):
					print "\t" + option + "\t" + value + "\n"

	def getApplicationUsage(self):
		return self._applicationUsage

	def getApplicationName(self):
		return self._argv[0]

	def isOption(self, str):
		return (len(str) > 1 and str[0]=='-')

	def argc(self):
		return self._argc

	def argv(self):
		return self._argv

	def find(self,str):
		for pos in range(1,self._argc):
			if str == self._argv[pos]:
				return pos
		return 0

	def match(self,pos,str):
		return (pos<self._argc and str==self._argv[pos])

	def containsOptions(self):
		for op in self._argv:
			if self.isOption(op):
				return True
		return False

	def remove(self,pos,num):
		if num==0: return
		removeList = []
		for i in range(pos,pos+num):
			removeList.append(self._argv[i])
		for r in removeList:
			self._argv.remove(r)
		self._argc-=num
		
		
	def read(self,str,values=[]):
		pos = self.find(str)
		if pos<=0:
			return False
		if len(values)==0:
			self.remove(pos,1)
			return True
		return self.__read(pos,str,values)

	def __read(self,pos,str,values):
		if self.match(pos,str):
			if pos+len(values)<self._argc:
				inc = 1
				for i in range(len(values)):
					values[i] = self._argv[pos+inc]
					inc+=1
				self.remove(pos,len(values)+1)
				return True
			self.__reportError("argument to `"+str+"` is missing")
			return False
		return False

	def __reportError(self, str):
		self._error = True
		self._errorMessages.append(str)
