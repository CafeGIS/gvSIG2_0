import unittest
from bmcore import *
from bmbase.PlugInManager import PlugInManager 

class TestBuildManPlugIn(unittest.TestCase):

	def testArguments(self):
		arguments = ArgumentParser(['bmn.py','buildman','--file','bmtests/data/buildman.xml'])
		bm = BuildMan(arguments)
		plugin = PlugInManager().getPlugInInstance("BuildManPlugIn")
		self.assertEqual('bmtests/data/buildman.xml',plugin.getFile())
		
	def testGoals(self):
		arguments = ArgumentParser(['bmn.py','buildman','--file','bmtests/data/buildman.xml'])
		bm = BuildMan(arguments)
		plugin = PlugInManager().getPlugInInstance("BuildManPlugIn")
		plugin._goalList = []
		plugin.loadXMLFile(plugin.getFile())
		goals = ['delete','svn','clean','update','create-solution','compile-solution','create']
		i = 0
		for goal in plugin.getGoalList():
			self.assertEqual(goal.localName,goals[i])
			i+=1
		
		
	def testInitFromXML(self):
		arguments = ArgumentParser(['bmn.py','buildman','--file','bmtests/data/batch-buildman.xml'])
		bm = BuildMan(arguments)
		plugin = PlugInManager().getPlugInInstance("BuildManPlugIn")
		plugin._goalList = []
		plugin.loadXMLFile(plugin.getFile())
		goals = ['delete','svn','clean','update','create-solution','compile-solution','create']
		i = 0
		for goal in plugin.getGoalList():
			self.assertEqual(goal.localName,goals[i])
			i+=1
			
	def testExecute(self):
		arguments = ArgumentParser(['bmn.py','buildman','--file','bmtests/data/batch-buildman.xml'])
		bm = BuildMan(arguments)
		plugin = PlugInManager().getPlugInInstance("BuildManPlugIn")
		plugin._goalList = []
		plugin.execute()
		