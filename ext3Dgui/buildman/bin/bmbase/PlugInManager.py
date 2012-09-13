"""
	PlugInManager is a singleton class that manages
	plugininstances, and register methods. Each
	plugin should call to register itself
"""
class PlugInManager:
	_pluginInstances = {}

	def registerPlugIn(self, id, pluginInstance):
		PlugInManager._pluginInstances[id] = pluginInstance
		pluginInstance.setId(id)

	def getPlugInInstances(self):
		return PlugInManager._pluginInstances

	def getPlugInInstance(self,id):
		return PlugInManager._pluginInstances[id]

	def getPlugInInstanceByGoal(self,goal):
		for plugin in PlugInManager._pluginInstances.values():
			if goal in plugin.getGoals().keys():
				return plugin
		return None
	
	def supportedGoals(self):
		goals = []
		for plugin in PlugInManager._pluginInstances.values():
			for g in plugin.getGoals().keys():
				goals.append(g)
		return goals

#loading plugins
from bmplugins import *


