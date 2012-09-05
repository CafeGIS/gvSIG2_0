from bmbase.IPlugIn import IPlugIn
from bmbase.PlugInManager import PlugInManager
from bmcore.BMUtil import *
from xml.dom import minidom
import os

class DepManGetPlugIn(IPlugIn):
	def __init__(self):
		IPlugIn.__init__(self)
		self._default_repository = ""
		self._dependency = None

		self._forceCache = False
		self._isInteractive = True


	def setDependency(self, dependency):
		self._dependency = dependency
		
	def setURL(self,url):
		self._url = url

	def setForceCache(self):
		self._forceCache = True
		self._isInteractive = False

	def setForceRemote(self):
		self._forceCache = False
		self._isInteractive = False
	
	def init(self):
		self.addGoal("get", "downloads an artifact if not in cache.")
		self.addGoalOption("get","--url", "URL base for the DepMan repository.")
		self.addGoalOption("get","--group", "Group namespace of the artifact.")
		self.addGoalOption("get","--artifact", "Name of the artifact to download.")
		self.addGoalOption("get","--version", "Version of the artifact.")
		self.addGoalOption("get","--platform", "Platform of the artifact.")
		self.addGoalOption("get","--distribution", "Distribution of the artifact.")
		self.addGoalOption("get","--compiler", "Selects the compiler version that was used to compile the artifact.")
		self.addGoalOption("get","--arch", "Selects the architecture of the artifact.")
		self.addGoalOption("get","--ltype", "Type of library of the artifact.")
		self.addGoalOption("get","--cache", "Forces the use of cache files without asking.")
		self.addGoalOption("get","--remote", "Forces the download of remote without the use of the cache and without asking.")

		self._dmplugin = PlugInManager().getPlugInInstance("DepManPlugIn")
		if self._dmplugin == None:
			self.reportError("PlugIn `depman` not found")
			return
		self._default_repository = self._dmplugin.getDepManPath()
	
		while self._arguments.read("get"):
			self._dependency = Dependency()
			self.setExecute(true)
		
		args=[""]
		if self._arguments.read("--url",args):
			self._dependency.url = args[0]

		args=[""]
		if self._arguments.read("--group",args):
			self._dependency.group = args[0]

		args=[""]
		if self._arguments.read("--artifact",args):
			self._dependency.artifact = args[0]

		args=[""]
		if self._arguments.read("--version",args):
			self._dependency.version = args[0]

		args=[""]
		if self._arguments.read("--platform",args):
			self._dependency.platform = args[0]

		args=[""]
		if self._arguments.read("--distribution",args):
			self._dependency.distribution = args[0]

		args=[""]
		if self._arguments.read("--compiler",args):
			self._dependency.compiler = args[0]

		args=[""]
		if self._arguments.read("--arch",args):
			self._dependency.arch = args[0]

		args=[""]
		if self._arguments.read("--ltype",args):
			self._dependency.libraryType = args[0]

		if self._arguments.read("--cache"):
			self._forceCache = True
			self._isInteractive = False

		if self._arguments.read("--remote"):
			self._forceCache = False
			self._isInteractive = False
	

	def initFromXML(self,node):
		pass
		#TODO: . . .

	def execute(self):
		if self._dependency == None:
			self.reportError("Missing dependency option")
			return False
		if self._url == "":
			self.reportError("Missing url option")
			return False
	
		if not self._dmplugin.validateDependency(self._dependency):
			return False

		return self.get()

	def get(self, unPackList = None):
		
		print "[",self._dependency.artifact,"]"
		file_name = self._dependency.getDepManFileName()
		tarname=file_name+".tar.gz"
		md5name=tarname+".md5"
		download_path = self._dependency.getDepManFilePath()
		download_dir = self._dependency.getDepManUrlPath() 
		cache_path = self._default_repository+os.path.sep+".cache"+os.path.sep+download_path

		is_snapshot = self._dependency.isSnapshot()
	
		dmutil = BMUtil()

		is_tar=True
		is_md5=True
		if not dmutil.checkUrl(self._dependency.url+"/"+download_dir+"/"+md5name):
			#print "Error: File ",baseurl+"/"+download_dir+"/"+md5name, " not found in the repository"
			is_md5=False
	
		if not dmutil.checkUrl(self._dependency.url+"/"+download_dir+"/"+tarname):
			#print "Error: File ",baseurl+"/"+download_dir+"/"+tarname, " not found in the repository"
			is_tar=False
	
		dmutil.mkdir(cache_path)
		
	
		if not os.path.isfile(cache_path+os.path.sep+md5name):
			is_cache=False
		else:
			is_cache=True

		#Once all variables have been collected, lets decide what to do with the artifact
		must_download=False
	
		if (not is_md5 or not is_tar) and not is_cache:
			print "Error: Artifact ",tarname," not found"
			return False
	
		if not is_md5 and not is_tar and is_cache:
			print "* file not found in repository, using cache..."
			must_download=False
	
		if is_md5 and is_tar  and not is_cache:
			print "* file is not in cache, downloading..."
			must_download=True
	
		if is_md5 and is_tar and is_cache:
			if is_snapshot:
				if self._isInteractive:
					repo=raw_input("What snapshot do you want to use? (cache/remote): ")
				else:
					if self._forceCache:
						repo="cache"
					else:
						repo="remote"
					
				must_download=False
			
				#in case of misspeling, using cache by default
				if repo!="cache" and repo!="remote":
					repo="cache"
			else:
				repo="remote"
		
			if repo=="remote":
				print "* file cached, checking md5..."
				dmutil.download(self._url+"/"+download_dir+"/"+md5name,cache_path+os.path.sep+md5name+".new")
			
				if dmutil.diff(cache_path+os.path.sep+md5name,cache_path+os.path.sep+md5name+".new"):
					print "* no md5 matching, re-downloading..."
					must_download=True
				else:
					print "* md5 matching succesful"
					must_download=False
				os.remove(cache_path+os.path.sep+md5name+".new")
	
		if must_download==True:
			print "URL :", self._dependency.url
			dmutil.download(self._dependency.url+"/"+download_dir+"/"+md5name,cache_path+os.path.sep+md5name)
			#print "* downloaded [",md5name,"]"
			dmutil.download(self._dependency.url+"/"+download_dir+"/"+tarname,cache_path+os.path.sep+tarname)
			#print "* downloaded[",tarname,"]"
		if unPackList != None:
			if (cache_path+os.path.sep+tarname) not in unPackList:
				unPackList.append(cache_path+os.path.sep+tarname)
		
		return True

PlugInManager().registerPlugIn("DepManGetPlugIn",DepManGetPlugIn())


