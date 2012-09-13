#depman update plugin

from bmbase.IPlugIn import IPlugIn
from bmbase.PlugInManager import PlugInManager
from bmcore.BMUtil import BMUtil
import os


class DepManUpdatePlugIn(IPlugIn):
	
	def __init__(self):
		IPlugIn.__init__(self)
		
		self._must_Clean=True
		self._isFromCache=False
		self._isInteractive=True
		self._xmlfile="depman.xml"
		self._depmanNode = None
		
		self._defurl="http://murray.ai2.upv.es/depman"
		
	def init(self):
		self.addGoal("update", "Update current project")
		self.addGoalOption("update","--no-clean", "Do not perform a repository clean before update")
		self.addGoalOption("update","--cache", "Cache is preferred")
		self.addGoalOption("update","--remote", "Remote repository is preferred")
		self.addGoalOption("update","--file", "Specifies a dependency xml file")
				
		isUpdate = False
		if self._arguments.read("update"):
			self.setExecute(True)
			isUpdate = True

		if not isUpdate:
			return

		if self._arguments.read("--no-clean"):
			self._must_Clean = False
			
		if self._arguments.read("--cache"):
			self._isInteractive=False
			self._isFromCache = True
		
		if self._arguments.read("--remote"):
			self._isInteractive=False
			self._isFromCache = False
		
		args=[""]
		if self._arguments.read("--file",args):
			self._xmlfile=args[0]
		
	def initFromXML(self,node):
		if node.localName == "update":
			if node.hasAttributes():
				if node.attributes.has_key("file"):
					self.loadXMLFile(node.attributes.get("file").value)
				if node.attributes.has_key("no-clean"):
					value = node.attributes.get("no-clean").value
					if value == "True" or value == "true":
						self._must_Clean = True
				foundCache = False
				if node.attributes.has_key("cache"):
					value = node.attributes.get("cache").value
					if value == "True" or value == "true":
						self._isFromCache = True
				if node.attributes.has_key("remote") and not foundCache:
					value = node.attributes.get("remote").value
					if value == "True" or value == "true":
						self._isFromCache = False
			self._isInteractive = False
		else:	
			self._depmanNode = node
		
	def execute(self):
		print "Executing Plugin:" + str(self.__class__)
		return self.update()



	def update(self):
		
		if self._must_Clean:
			self._dmclean = PlugInManager().getPlugInInstance("DepManCleanPlugIn")
			if self._dmclean == None:
				self.reportError("PlugIn `depman clean` not found")
				return
			delete_cache = self._dmclean.getDeleteCache()
			self._dmclean.setDeleteCache(False)
			self._dmclean.clean()	
			self._dmclean.setDeleteCache(delete_cache)

		if self._depmanNode == None:
			self.loadXMLFile(self._xmlfile)
		unPackList = self.getDependencies(self._depmanNode)

		
		#once the xml is parsed and files downloaded, lets unpack them
		self.unpack(unPackList)
	
	def getDependencies(self,node):
		self._dmplugin = PlugInManager().getPlugInInstance("DepManPlugIn")
		if self._dmplugin == None:
			self.reportError("PlugIn `depman` not found")
			return
		
		self._dmget = PlugInManager().getPlugInInstance("DepManGetPlugIn")
		if self._dmget == None:
			self.reportError("PlugIn `depman get` not found")
			return
		
		group="none"
		artifact="none"
		version="0"
		platform="none"
		compiler="none"
		arch="none"
		ltype="none"
		
		#os default values
		defplatform=self._dmplugin.getOSPlatform()
		defarch=self._dmplugin.getOSArch()
		defcompiler=self._dmplugin.getOSCompiler()
		
		#hardcoded default url
		#defurl=default_url
		
		defurl=self._defurl
		
		unPackList = []
		if node.hasAttributes():
		    if node.attributes.has_key("url"):
		        defurl=node.attributes.get("url").value
		
		for i in node.childNodes:
		    if i.localName=="dependencies":
		        url=defurl
		        #os default values
		        defplatform=self._dmplugin._osplatform
		        defarch=self._dmplugin._osarch
		        defcompiler=self._dmplugin._oscompiler
		        
		        if i.hasAttributes():
		            if i.attributes.has_key("platform"):
		                defplatform=i.attributes.get("platform").value
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
		        
		        #print "Url: ",url
		        if not invalid_platform:
		            for j in i.childNodes:
		                
		                if j.localName=="dependency":
		                    #set default values
		                    platform=defplatform
		                    arch=defarch
		                    compiler=defcompiler
		                    group="none"
		                    artifact="none"
		                    version="0"
		                    ltype="none"
		                    durl = url
		                    if j.hasAttributes():
		                        if j.attributes.has_key("url"):
		                            durl=j.attributes.get("url").value
		                    
		                    for h in j.childNodes:
		                        if h.localName=="group":
		                            group=h.childNodes[0].nodeValue
		                        if h.localName=="artifact":
		                            artifact=h.childNodes[0].nodeValue
		                        if h.localName=="version":
		                            version=h.childNodes[0].nodeValue
		                        if h.localName=="platform":
		                            platform=h.childNodes[0].nodeValue
		                        if h.localName=="compiler":
		                            compiler=h.childNodes[0].nodeValue
		                        if h.localName=="architecture":
		                            arch=h.childNodes[0].nodeValue
		                        if h.localName=="type":
		                            ltype=h.childNodes[0].nodeValue
		                    self._dmget.setURL(durl)
		                    self._dmget.setGroup(group)
		                    self._dmget.setArtifact(artifact)
		                    self._dmget.setVersion(version)
		                    self._dmget.setPlatform(platform)
		                    self._dmget.setCompiler(compiler)
		                    self._dmget.setArch(arch)
		                    self._dmget.setLibraryType(ltype)
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
