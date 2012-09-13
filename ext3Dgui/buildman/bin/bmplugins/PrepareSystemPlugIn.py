from bmbase.IPlugIn import IPlugIn
from bmbase.PlugInManager import PlugInManager
from bmcore.BMUtil import BMUtil
import os
import sys
import shutil

class PrepareSystemPlugIn(IPlugIn):
	def __init__(self):
		IPlugIn.__init__(self)

	def init(self):
		self.addGoal("init", "Prepares the system with the buildman user directory with an example plugin")
		while self._arguments.read("init"):
			self.setExecute(True)			

	def execute(self):
		print "Executing Plugin:" + str(self.__class__) + "\n"
		util = BMUtil()
		bmpath = util.buildmanPlugInPath()
		try:
			util.mkdir(bmpath)		
			#shutil.copy(sys.path[0]+os.path.sep+"bmplugins"+os.path.sep+"TestPlugIn.py_",util.buildmanPlugInPath()+os.path.sep+"TestPlugIn.py")
		except:
			self.reportError("Error creating buildman directory or copying test plugin")
			return False
		return True

PlugInManager().registerPlugIn("PrepareSystemPlugIn",PrepareSystemPlugIn())


