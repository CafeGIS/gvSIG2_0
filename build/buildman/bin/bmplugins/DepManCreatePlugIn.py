#depman create plugin

from bmbase.IPlugIn import IPlugIn
from bmbase.PlugInManager import PlugInManager
from bmcore.BMUtil import *
from bmcore.ftpslib import *
from xml.dom import minidom
import string
import shutil
import os
import sys
import subprocess

class DepManCreatePlugIn(IPlugIn):
	
	def __init__(self):
		IPlugIn.__init__(self)
		
		self._path="."
		self._is_Interactive=False
		self._dependency = None
		
		self._default_ftp="downloads.gvsig.org"
		self._default_login="maven"
		self._default_destdir = "/anon/pub/gvSIG-desktop/buildman-repository"
		
		
	def init(self):
		self.addGoal("create", "Create an artifact")
		self.addGoalOption("create","--path", "Specifies source directory")
		self.addGoalOption("create","--interactive", "ask for server or artifact deployment configuration")
		self.addPreGoal("create", "depman")	
		self._dmplugin = PlugInManager().getPlugInInstance("DepManPlugIn")
		self._dmdeployplugin = PlugInManager().getPlugInInstance("DepManDeployPlugIn")

		if self._dmplugin == None:
			self.reportError("PlugIn `depman` not found")
			return False

	
		if self._arguments.read("create"):
			self.setExecute(True)
		
		args=[""]
		if self._arguments.read("--path",args):
			self._path=args[0]	

		if self._arguments.read("--interactive"):
			self._is_Interactive=True	


	def initFromXML(self,node):
		packageNodes = node.getElementsByTagName("package")
		if packageNodes != None:
			for n in packageNodes:
				if n.localName=="package":
					processPackage = True
					if n.hasAttributes():
						if n.attributes.has_key("platform"):
							values = n.attributes.get("platform").value.split(",")
							if self._dmplugin.getOSPlatform() in values:
								processPackage = True
							else:
								processPackage = False
					if processPackage:
						print "Processing for platform..."
						self._dependency = DepManDependency(self._dmplugin)
						for p in n.childNodes:
							if p.localName=="group":
								self._dependency.group=p.childNodes[0].nodeValue
							if p.localName=="artifact":
								self._dependency.artifact=p.childNodes[0].nodeValue
							if p.localName=="version":
								self._dependency.version=p.childNodes[0].nodeValue
							if p.localName=="platform":
								self._dependency.platform=p.childNodes[0].nodeValue
							if p.localName=="distribution":
								self._dependency.distribution=p.childNodes[0].nodeValue
							if p.localName=="compiler":
								self._dependency.compiler=p.childNodes[0].nodeValue
							if p.localName=="arch":
								self._dependency.arch=p.childNodes[0].nodeValue
							if p.localName=="libraryType":
								self._dependency.libraryType=p.childNodes[0].nodeValue
						
						print "Creating Artifact:" + str(self._dependency)	
	

		
	def execute(self):
		return self.create()
	
	def create(self):
	   	self.initFromXML(self._dmplugin.getNode())	
		#user questions
		if self._dependency==None:
			self._dependency = DepManDependency(self._dmplugin)
			self._is_Interactive = True
			self._dependency.raw_input()
					
			upload_response = raw_input("Upload to server? (y/n): ")
			if upload_response == "y" or upload_response == "yes":
				self._upload=True
	
		if not self._dmplugin.validateDependency(self._dependency):
			return False
				

		#artifact and md5 generation
		file_name=self._dependency.getDepManFileName()
		file_path=self._dependency.getDepManFilePath()

		tarname=file_name+".tar.gz"
		md5name=tarname+".md5"
		dmfile=file_name+".xml"
		
		dmutil = BMUtil()
		
		tmpdir=self._dmplugin.getMavenPath()+os.path.sep+"repository"+os.path.sep+file_path
		dmutil.mkdir(tmpdir)
		print tmpdir+os.path.sep+tarname,self._path
		dmutil.targz(os.path.join(tmpdir,tarname),self._path)
		#print "targz ",tmpdir+os.path.sep+tarname
		dmutil.createMD5(tmpdir+os.path.sep+tarname,tmpdir+os.path.sep+md5name)

		if not self._is_Interactive:
			shutil.copyfile(self._dmplugin.getXMLFile(),tmpdir+os.path.sep+dmfile)	
		
		self._dmdeployplugin.setDependency(self._dependency)
		print "Artifact " + tarname + " created in:\n" + tmpdir
		
		

PlugInManager().registerPlugIn("DepManCreatePlugIn",DepManCreatePlugIn())		
