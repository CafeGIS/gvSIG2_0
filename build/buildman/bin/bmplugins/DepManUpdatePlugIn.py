#depman update plugin

from bmbase.IPlugIn import IPlugIn
from bmbase.PlugInManager import PlugInManager
from bmcore.BMUtil import *
import os


class DepManUpdatePlugIn(IPlugIn):
	
	def __init__(self):
		IPlugIn.__init__(self)
		
		self._must_Clean=True
		self._isFromCache=False
		self._isInteractive=True
		
		self._defurl="http://downloads.gvsig.org/pub/gvSIG-desktop/buildman-repository"
		self._dependencyList = []
		
	def init(self):
		self.addGoal("update", "Update current project")
		self.addGoalOption("update","--cache", "Cache is preferred")
		self.addGoalOption("update","--remote", "Remote repository is preferred")
		self.addPreGoal("update","depman")
		self.addPreGoal("update","clean")
				
		if self._arguments.read("update"):
			self.setExecute(True)
			
		if self._arguments.read("--cache"):
			self._isInteractive=False
			self._isFromCache = True
		
		if self._arguments.read("--remote"):
			self._isInteractive=False
			self._isFromCache = False

		self._dmplugin = PlugInManager().getPlugInInstance("DepManPlugIn")
		if self._dmplugin == None:
			self.reportError("PlugIn `depman` not found")
			return
		
	def initFromXML(self,dom):
		
		defurl=self._defurl
		node = dom.getElementsByTagName("depman")[0]
		if node != None:
			if node.hasAttributes():
				if node.attributes.has_key("url"):
					defurl=node.attributes.get("url").value
			
			for i in node.childNodes:
				if i.localName=="dependencies":
					url=defurl
					#os default values
					defplatform=self._dmplugin.getOSPlatform()
					defdistribution=self._dmplugin.getOSDistribution()
					defarch=self._dmplugin.getOSArch()
					defcompiler=self._dmplugin.getOSCompiler()
	    		        
					if i.hasAttributes():
						if i.attributes.has_key("platform"):
							defplatform=i.attributes.get("platform").value
						if i.attributes.has_key("distribution"):
							defdistribution=i.attributes.get("distribution").value
						if i.attributes.has_key("architecture"):
							defarch=i.attributes.get("architecture").value
						if i.attributes.has_key("compiler"):
							defcompiler=i.attributes.get("compiler").value
						if i.attributes.has_key("url"):
							url=i.attributes.get("url").value
			        
					list_of_platforms=defplatform.split(",")
			       	#depedencies platform checking
			        #we just go on whenever host os or all matches
			        
					if len(list_of_platforms)>0 and self._dmplugin.getOSPlatform() not in list_of_platforms and "all" not in list_of_platforms:
						invalid_platform=True
					else:
						invalid_platform=False
						defplatform=self._dmplugin.getOSPlatform()
	
					del list_of_platforms[:]
			        
					if not invalid_platform:
						for j in i.childNodes:
							if j.localName=="dependency":
								dependency = DepManDependency(self._dmplugin)		
								#set default values
								dependency.platform=defplatform
								dependency.distribution=defdistribution
								dependency.arch=defarch
								dependency.compiler=defcompiler
								dependency.url = url
								if j.hasAttributes():
									if j.attributes.has_key("url"):
										dependency.url=j.attributes.get("url").value
								for h in j.childNodes:
									if h.localName=="group":
										dependency.group=h.childNodes[0].nodeValue
									if h.localName=="artifact":
										dependency.artifact=h.childNodes[0].nodeValue
									if h.localName=="version":
										dependency.version=h.childNodes[0].nodeValue
									if h.localName=="platform":
										dependency.platform=h.childNodes[0].nodeValue
									if h.localName=="distribution":
										dependency.distribution=h.childNodes[0].nodeValue
									if h.localName=="compiler":
										dependency.compiler=h.childNodes[0].nodeValue
									if h.localName=="architecture":
										dependency.arch=h.childNodes[0].nodeValue
									if h.localName=="type":
										dependency.libraryType=h.childNodes[0].nodeValue
								self._dependencyList.append(dependency)
		
	def execute(self):
		return self.update()

	def update(self):
		self.initFromXML(self._dmplugin.getNode())
		for i in self._dependencyList:
			print i
		unPackList = self.getDependencies()
		
		#once the xml is parsed and files downloaded, lets unpack them
		self.unpack(unPackList)
	
	def getDependencies(self):
			
		self._dmget = PlugInManager().getPlugInInstance("DepManGetPlugIn")
		if self._dmget == None:
			self.reportError("PlugIn `depman get` not found")
			return
		unPackList = []
		for dep in self._dependencyList:
			self._dmget.setDependency(dep)
			#prevents downloading of not matching platforms but overrided
			if not self._isInteractive:
				if self._isFromCache:
					self._dmget.setForceCache()
				else:
					self._dmget.setForceRemote()
			self._dmget.get(unPackList)
		return unPackList
	
	def unpack(self,unPackList):
		sep=os.path.sep
		dmutil = BMUtil()
		for file in unPackList:
			tmpstr=file[file.rfind(sep)+1:]
			print "* unpacking ",tmpstr
			dmutil.untargz(file,self._dmplugin.getDepManPath())
			
	def setForceCache(self):
		self._isFromCache = True
		self._isInteractive = False
		
	def setForceRemote(self):
		self._isFromCache = False
		self._isInteractive = False

		#after unpacking, the unpack list is freed
		
PlugInManager().registerPlugIn("DepManUpdatePlugIn",DepManUpdatePlugIn())

