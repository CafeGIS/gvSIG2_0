from bmbase.PlugInManager import PlugInManager

"""
	BuildMan manages the loading of plugins and 
	dispatch the requested tasks to the appropiate
	plug-in. Each plug-in defines its parameters and
	its help environment, so an ArgumentParse is 
	necessary in the construction. 
"""
class BuildMan:
	# constructor
	def __init__(self,arguments):
		self._arguments = arguments
		self._errorMessages = []
		self.__configurePlugins(self._arguments)

	# configure arguments of the automatic loaded plugins
	def __configurePlugins(self, arguments):
		for plugin in PlugInManager().getPlugInInstances().values():
			plugin.initArguments(arguments)
		for plugin in PlugInManager().getPlugInInstances().values():
			plugin.init()
			if plugin.error():
				self._errorMessages += plugin.getErrorMessages()

	# execute all plugins, any plugin with his arguments has the responsability of do the correct
	def run(self):
		for plugin in PlugInManager().getPlugInInstances().values():
			plugin.run()
			if plugin.error():
				self._errorMessages += plugin.getErrorMessages()
				return False
		return True	
				

	def writeErrorMessages(self):
		for error in self._errorMessages:
			print "ERROR: " + error + "\n"
	


