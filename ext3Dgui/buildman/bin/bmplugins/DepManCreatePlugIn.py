#depman create plugin

from bmbase.IPlugIn import IPlugIn
from bmbase.PlugInManager import PlugInManager
from bmcore.BMUtil import BMUtil
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
		self._group=""
		self._artifact=""
		self._version=""
		self._platform="default"
		self._arch="default"
		self._compiler="default"
		self._ltype=""
		self._upload=False
		self._is_Interactive=True
		self._xmlfile = ""
		self._packageNode = None
		
		self._default_ssh="murray.ai2.upv.es"
		self._default_login="depman"
		self._default_destdir = "/projects/AI2/www-aliases/depman/"
		
		
	def init(self):
		self.addGoal("create", "Create an artifact")
		self.addGoalOption("create","--path", "Specifies source directory")
		self.addGoalOption("create","--group", "Specifies artifact group name")
		self.addGoalOption("create","--artifact", "Specifies artifact name")
		self.addGoalOption("create","--version", "Specifies artifact version")
		self.addGoalOption("create","--platform", "Specifies artifact OS platform")
		self.addGoalOption("create","--arch", "Specifies artifact hardware architecture")
		self.addGoalOption("create","--compiler", "Specifies artifact compiler version")
		self.addGoalOption("create","--ltype", "Specifies library type if needed")
		self.addGoalOption("create","--upload", "Whenever the artifact must be uploaded or not")
		self.addGoalOption("create","--from-xml", "Uses the given depman.xml file to create the package")
		
		isCreate = False
		if self._arguments.read("create"):
			self.setExecute(True)
			isCreate = True

		if not isCreate:
			return

		args=[""]
		use_xml=False
		if self._arguments.read("--from-xml",args):
			self._xmlfile=args[0]
			use_xml=True
			
		if use_xml:
			self.loadXMLFile(self._xmlfile)
			
			
		args=[""]
		if self._arguments.read("--path",args):
			self._path=args[0]
		
		args=[""]
		if self._arguments.read("--group",args):
			self._group=args[0]
			self._is_Interactive=False
			
		args=[""]
		if self._arguments.read("--artifact",args):
			self._artifact=args[0]
			self._is_Interactive=False	
			
		args=[""]
		if self._arguments.read("--version",args):
			self._version=args[0]
			self._is_Interactive=False	
			
			
		args=[""]
		if self._arguments.read("--platform",args):
			self._platform=args[0]
			self._is_Interactive=False	
		
		args=[""]
		if self._arguments.read("--arch",args):
			self._arch=args[0]
			self._is_Interactive=False

		args=[""]
		if self._arguments.read("--compiler",args):
			self._compiler=args[0]
			self._is_Interactive=False
			
		args=[""]
		if self._arguments.read("--ltype",args):
			self._ltype=args[0]
			self._is_Interactive=False	
			
		if self._arguments.read("--upload"):
			self._upload=True	


	def initFromXML(self,node):
		if node.localName == "create":
			if node.hasAttributes():
				if node.attributes.has_key("from-xml"):
					self._xmlfile=node.attributes.get("from-xml").value
					self.loadXMLFile(self._xmlfile)
				if node.attributes.has_key("path"):
					self._path = node.attributes.get("path").value
				if node.attributes.has_key("upload"):
					value = node.attributes.get("upload").value
					if value =="True" or value =="true":
						self._upload = True

		else:
			if node.localName == "depman":
				self._packageNode = node
				self._is_Interactive=False
		
	def execute(self):
		print "Executing Plugin:" + str(self.__class__)
		return self.create()
	
	def create(self):
		
		self._dmplugin = PlugInManager().getPlugInInstance("DepManPlugIn")
		if self._dmplugin == None:
			self.reportError("PlugIn `depman` not found")
			return False
		
		#user questions
		if self._is_Interactive:
			self._group=raw_input("Group: ")
			
			self._artifact=raw_input("Artifact: ")
			
			self._version=raw_input("Version: ")
			
			tmpstr=""
			for p in self._dmplugin.getSupportedPlatforms():
				tmpstr=tmpstr+p+" "
			print "( ",tmpstr,")"
			tmstr="Platform [*]:"
			self._platform=raw_input(string.replace(tmstr,"*",self._dmplugin.getOSPlatform()))
			if len(self._platform)==0:
				self._platform=self._dmplugin.getOSPlatform()
			
			tmpstr=""
			for p in self._dmplugin.getSupportedCompilers():
				tmpstr=tmpstr+p+" "
			print "( ",tmpstr,")"
			tmstr="Compiler [*]:"
			self._compiler=raw_input(string.replace(tmstr,"*",self._dmplugin.getOSCompiler()))
			if len(self._compiler)==0:
				self._compiler=self._dmplugin.getOSCompiler()
				
			tmpstr=""
			for p in self._dmplugin.getSupportedArchs():
				tmpstr=tmpstr+p+" "
			print "( ",tmpstr,")"
			tmstr="Architecture [*]:"
			self._arch=raw_input(string.replace(tmstr,"*",self._dmplugin.getOSArch()))
			if len(self._arch)==0:
				self._arch=self._dmplugin.getOSArch()
			
			tmpstr=""
			for p in self._dmplugin.getSupportedLibraryTypes():
				tmpstr=tmpstr+p+" "
			print "( ",tmpstr,")"
			self._ltype=raw_input("Library Type: ")
			
			upload_response = raw_input("Upload to server? (y/n): ")
			if upload_response == "y" or upload_response == "yes":
				self._upload=True
		
		if self._packageNode != None:
			for n in self._packageNode.childNodes:
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
						for p in n.childNodes:
							if p.localName=="group":
								self._group=p.childNodes[0].nodeValue
							if p.localName=="artifact":
								self._artifact=p.childNodes[0].nodeValue
							if p.localName=="version":
								self._version=p.childNodes[0].nodeValue
							if p.localName=="platform":
								self._platform=p.childNodes[0].nodeValue
							if p.localName=="compiler":
								self._compiler=p.childNodes[0].nodeValue
							if p.localName=="arch":
								self._arch=p.childNodes[0].nodeValue
							if p.localName=="libraryType":
								self._ltype=p.childNodes[0].nodeValue
							
							if p.localName =="upload":
								#TODO: Maybe upload should be an external plugin
								for k in p.childNodes:
									if k.localName == "sshserver":
										self._default_ssh = k.childNodes[0].nodeValue
									if k.localName == "destdir":
										self._default_destdir = k.childNodes[0].nodeValue
									if k.localName == "username":
										self._default_login = k.childNodes[0].nodeValue
						
		if self._group == "":
			self.reportError("Group cannot be empty")
			return False
		if self._artifact == "":
			self.reportError("Artifact cannot be empty")
			return False
		if self._version == "":
			self.reportError("Version cannog be empty")
			return 
		self._group=self._group.replace(".","/")
		if self._platform=="default":
			self._platform=self._dmplugin.getOSPlatform()
		if self._compiler=="default":
			self._compiler=self._dmplugin.getOSCompiler()
		if self._arch=="default":
			self._arch=self._dmplugin.getOSArch()
			
		
		#let's check user input consistency
		
		if self._platform not in self._dmplugin.getSupportedPlatforms():
			self.reportError("Platform not supported: " + self._platform + ". Supported platforms:" + str(self._dmplugin.getSupportedPlatforms()))
			return False
		
		if self._compiler not in self._dmplugin.getSupportedCompilers():
			self.reportError("Compiler not supported: " + self._compiler + ". Supported compilers:" +str(self._dmplugin.getSupportedCompilers()))
			return False
		
		if self._arch not in self._dmplugin.getSupportedArchs():
			self.reportError("Architecture not supported: " + self._arch + ". Supported archs:" +str(self._dmplugin.getSupportedArchs()))
			return False
		
		if self._ltype not in self._dmplugin.getSupportedLibraryTypes():
			self.reportError("Library type not supported: " + self._ltype + ". Supported libraries:" +str(self._dmplugin.getSupportedLibraryTypes()))
			return False
		
		#artifact and md5 generation
		
		file_name=self._artifact+"-"+self._version+"-"+self._platform+"-"+self._compiler+"-"+self._arch+"-"+self._ltype
		
		
		tarname=file_name+".tar.gz"
		md5name=tarname+".md5"
		dmfile=file_name+".xml"
		
		dmutil = BMUtil()
		
		tmpdir=self._dmplugin.getDepManPath()+os.path.sep+".cache"+os.path.sep+self._group+os.path.sep+self._artifact+os.path.sep+self._version+os.path.sep+self._platform+os.path.sep+self._compiler+os.path.sep+self._arch+os.path.sep+self._ltype
		
		dmutil.mkdir(tmpdir)
		print tmpdir+os.path.sep+tarname,self._path
		dmutil.targz(os.path.join(tmpdir,tarname),self._path)
		#print "targz ",tmpdir+os.path.sep+tarname
		dmutil.createMD5(tmpdir+os.path.sep+tarname,tmpdir+os.path.sep+md5name)
		if self._xmlfile != "":
			shutil.copyfile(self._xmlfile,tmpdir+os.path.sep+dmfile)	
		print "Artifact " + tarname + " created in:\n" + tmpdir
		
		if self._upload:
			dmutil.rmdir(".dmn_tmp") # Prevent for uploading bad previous compilations!
			sshtmpdir=".dmn_tmp"+os.path.sep+self._group+os.path.sep+self._artifact+os.path.sep+self._version+os.path.sep+self._platform+os.path.sep+self._compiler+os.path.sep+self._arch+os.path.sep+self._ltype
			dmutil.mkdir(sshtmpdir)
			shutil.copyfile(tmpdir+os.path.sep+tarname,sshtmpdir+os.path.sep+tarname)
			shutil.copyfile(tmpdir+os.path.sep+md5name,sshtmpdir+os.path.sep+md5name)
			if self._xmlfile != "":
				shutil.copyfile(tmpdir+os.path.sep+dmfile,sshtmpdir+os.path.sep+dmfile)	
			url = self._default_ssh;
			destdir = self._default_destdir
			login = self._default_login
			
			if self._is_Interactive:
				tmstr="SSH Server [*]:"
				url=raw_input(string.replace(tmstr,"*",self._default_ssh))
				if len(url)==0:
					url=self._default_ssh
				
				tmstr="Destination Directory [*]:"
				destdir=raw_input(string.replace(tmstr,"*",self._default_destdir))
				if len(destdir)==0:
					destdir=self._default_destdir
			
				tmstr="Login [*]:"
				login=raw_input(string.replace(tmstr,"*",self._default_login))
				if len(login)==0:
					login=self._default_login
			
			download_dir = self._group+"/"+self._artifact+"/"+self._version+"/"+self._platform+"/"+self._compiler+"/"+self._arch+"/"+self._ltype
			
			print "* Uploading ",tarname
			print "* Uploading ",md5name
			
			#scp
			base_ssh=destdir
			url_ssh=url
			scpcommand = "scp"
			pscppath = ""
			if sys.platform == "win32":
				scpcommand=self._dmplugin.getDepManDataPath()+os.path.sep+"win32"+os.path.sep+"pscp.exe"
				scpcommand  = '"'+os.path.normpath(scpcommand)+'"'
			cmdstr=scpcommand +" -r "+".dmn_tmp"+os.path.sep+"*"+" "+login+"@"+url_ssh+":"+base_ssh
			
			print cmdstr
			os.system(cmdstr)
			
			#scp
			
			dmutil.rmdir(".dmn_tmp")
			
				



PlugInManager().registerPlugIn("DepManCreatePlugIn",DepManCreatePlugIn())		
