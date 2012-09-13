from bmbase.IPlugIn import IPlugIn
from bmbase.PlugInManager import PlugInManager
from bmcore.BMUtil import BMUtil
import os
import sys
import shutil

class DeleteFilePlugIn(IPlugIn):
	def __init__(self):
		IPlugIn.__init__(self)
		self._path = ""

	def init(self):
		self.addGoal("delete", "deletes a file or directory")
		self.addGoalOption("delete", "--path", "Sets the file/path to be deleted")
		execute = False
		while self._arguments.read("delete"):
			execute = True
			
		if execute:
			args = [""]
			while self._arguments.read("--path",args):
				self._path = args[0]
			
			self.setExecute(True)
			
	def initFromXML(self,node):
		if node.localName=="delete":
			if node.hasAttributes():
				if node.attributes.has_key("path"):
					self._path = node.attributes.get("path").value
						
	def execute(self):
		print "Executing Plugin:" + str(self.__class__)
		if self._path == "":
			self.reportError("Missing required path option")
			
		self._path = os.path.abspath(self._path)
		util = BMUtil()
		if not os.path.exists(self._path):
			return True
		if os.path.isdir(self._path):
			print "* Removing directory " + self._path
			util.rmdir(self._path)
		else:
			print "* Removing file " + self._path
			os.remove(self._path)
			
		return True

PlugInManager().registerPlugIn("DeleteFilePlugIn",DeleteFilePlugIn())


