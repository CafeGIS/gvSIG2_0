import os
import os.path
import zipfile
import difflib
import tarfile
import md5
import urllib2
import sys
import shutil
import re

class DepManDependency:
	def __init__(self, dmplugin):
		self._dmplugin = dmplugin
			
		self.group = "unknown"
		self.artifact = "unknown"
		self.version = "unknown"
		self.platform = self._dmplugin.getOSPlatform()
		self.distribution = self._dmplugin.getOSDistribution()
		self.arch=self._dmplugin.getOSArch()
		self.compiler=self._dmplugin.getOSCompiler()
		self.libraryType = "unknown"
		self.url = "unknown"

		self.dependencies = []
		
	def getDepManClassifier(self):
		classifier = self.platform
		if self.platform!="all":
			classifier=classifier+"-"+self.distribution+"-"+self.compiler+"-"+self.arch+"-"+self.libraryType
		return classifier

	def getDepManFileName(self):
		file_name=self.artifact+"-"+self.version+"-"+self.getDepManClassifier();
		return file_name
 

	def getDepManFilePath(self):
		group = group=self.group.replace(".",os.path.sep)
		file_path=group+os.path.sep+self.artifact+os.path.sep+self.version
		return file_path

	def getDepManUrlPath(self):
		group=self.group.replace(".","/")		
		file_path=group+"/"+self.artifact+"/"+self.version
		return file_path

	def isSnapshot(self):
		tstr=self.version.lower()
		if tstr.find("snapshot")!=-1:
			is_snapshot=True
		else:
			is_snapshot=False

		return is_snapshot

	def addDependency(self, dependency):
		if dependency not in self.dependencies:
			self.dependencies.append(dependency)

	def __eq__(self,other):
		if other == None:
			return False
		return (self.group==other.group and \
				self.artifact==other.artifact and \
				self.version==other.version and \
				self.platform==other.platform and \
				self.distribution==other.distribution and \
				self.arch==other.arch and \
				self.compiler==other.compiler and \
				self.libraryType==other.libraryType)

	def __ne__(self,other):
		if other == None:
			return True 
		return (self.group!=other.group or \
				self.artifact!=other.artifact or \
				self.version!=other.version or \
				self.platform!=other.platform or \
				self.distribution!=other.distribution or \
				self.arch!=other.arch or \
				self.compiler!=other.compiler or \
				self.libraryType!=other.libraryType) 

	def __str__(self):
			return self.getDepManFileName()

	def raw_input(self):
		self.group=raw_input("Group: ")
		self.artifact=raw_input("Artifact: ")
		self.version=raw_input("Version: ")
			
		tmpstr=""
		for p in self._dmplugin.getSupportedPlatforms():
			tmpstr=tmpstr+p+" "
		print "( ",tmpstr,")"
		tmstr="Platform [*]:"
		self.platform=raw_input(string.replace(tmstr,"*",self._dmplugin.getOSPlatform()))
		if len(self.platform)==0:
			self.platform=self._dmplugin.getOSPlatform()
		
		if self.platform!="all":
			tmstr="OS Distribution [*]:"
			self.distribution=raw_input(string.replace(tmstr,"*",self._dmplugin.getOSDistribution()))
			if len(self.distribution)==0:
				self.distribution=self._dmplugin.getOSDistribution()
	
			tmpstr=""
			for p in self._dmplugin.getSupportedCompilers():
				tmpstr=tmpstr+p+" "
			print "( ",tmpstr,")"
			tmstr="Compiler [*]:"
			self.compiler=raw_input(string.replace(tmstr,"*",self._dmplugin.getOSCompiler()))
			if len(self.compiler)==0:
				self.compiler=self._dmplugin.getOSCompiler()
				
			tmpstr=""
			for p in self._dmplugin.getSupportedArchs():
				tmpstr=tmpstr+p+" "
			print "( ",tmpstr,")"
			tmstr="Architecture [*]:"
			self.arch=raw_input(string.replace(tmstr,"*",self._dmplugin.getOSArch()))
			if len(self.arch)==0:
				self.arch=self._dmplugin.getOSArch()
			
			tmpstr=""
			for p in self._dmplugin.getSupportedLibraryTypes():
				tmpstr=tmpstr+p+" "
			print "( ",tmpstr,")"
			self.libraryType=raw_input("Library Type: ")

class BMUtil:
	def buildmanPath(self):
		dr = os.getenv("BUILDMAN_PATH")
		if not dr is None:
			return dr
		if sys.platform == "linux2" or sys.platform == "linux1":
			dr = os.getenv("HOME")+os.path.sep+".buildman"
		if sys.platform == "win32" or sys.platform == "win64":
			#ex: c:\documents and setting\user\DepMan
			dr = os.getenv("USERPROFILE")+os.path.sep+"BuildMan"
		if sys.platform == "darwin":
			dr = os.path.sep + "Developer" + os.path.sep + "BuildMan"
		if dr is None:
			return None
		else:
			return dr

	def buildmanPlugInPath(self):
		dr = self.buildmanPath()
		if dr is None:
			return None
		else:
			return os.path.join(dr,'plugins')


	def mkdir(self,newdir):
		"""works the way a good mkdir should :)
			- already exists, silently complete
			- regular file in the way, raise an exception
			- parent directory(ies) does not exist, make them as well
		"""
		if os.path.isdir(newdir):
			pass
		elif os.path.isfile(newdir):
			raise OSError("a file with the same name as the desired " \
				"dir, '%s', already exists." % newdir)
		else:
			head, tail = os.path.split(newdir)
			if head and not os.path.isdir(head):
				self.mkdir(head)
			if tail:
				os.mkdir(newdir)

	#recursively directory tree removing
	def rmdir(self,path):
		if not os.path.isdir(path):
			return

		shutil.rmtree(path)	
		#files=os.listdir(path)
			
		#for x in files:
		#	fullpath=os.path.join(path, x)
				
		#	if os.path.isfile(fullpath):
		#		os.remove(fullpath)
				
		#	elif os.path.isdir(fullpath):
		#		rmdir(fullpath)
		#		os.rmdir(fullpath)
				

	def unzip(self,file, dir):
		mkdir(dir)
		zfobj = zipfile.ZipFile(open(file))
		for name in zfobj.namelist():
			if name.endswith('/'):
				mkdir(os.path.join(dir, name))
			else:
				outfile = open(os.path.join(dir, name), 'wb')
				outfile.write(zfobj.read(name))
				outfile.close()

	def targz(self,file, dir):
		cwd = os.getcwd()
		os.chdir(dir)
		dirs = os.listdir(".")
		if ".svn" in dirs:
			dirs.remove(".svn")
		if ".cvsignore" in dirs:
			dirs.remove(".cvsignore")
		tar = tarfile.open(str(file),"w|gz")
		for f in dirs:	
			tar.add(f)
		tar.close()
		os.chdir(cwd)
		
		

	def untargz(self,file, dir):
		tar = tarfile.open(file)
		for tarinfo in tar:
			tar.extract(tarinfo,dir)
		tar.close()
		
	def untargzFiltered(self,file,dir,filter):
		filter=filter.replace("*","(\S)*")
		filter="^(\S)*"+filter+"$"
		#print "filter: ",filter
		tar = tarfile.open(file)
		for tarinfo in tar:
			if re.match(filter,tarinfo.name):
				print "[file] ",tarinfo.name
				tar.extract(tarinfo,dir)
						
		tar.close()

	def diff(self,f1name, f2name):
		try:
			f1 = open(f1name)
			f2 = open(f2name)
			a = f1.readlines(); f1.close()
			b = f2.readlines(); f2.close()
			for line in difflib.unified_diff(a, b):
				print line,
				if line != "":
					return True 
				return False
		except:
			return True

	#creates the given fouput md5 file from the finput file
	def createMD5(self,finput,foutput):
		#input file
		file_in=open(finput,"rb")
		md5cpm=md5.new()
			
		while True:
			data = file_in.read(8096)
			if not data:
				break
			md5cpm.update(data)
		
		file_in.close()
				
			
		md5string=md5cpm.hexdigest()
		
		file_out=open(foutput,"w")
		file_out.write(md5string)
		file_out.close()
		
		
		
	def checkUrl(self,uri):
		print "Uri:", uri
		try:
			fd=urllib2.urlopen(uri)
			fd.close()
			return True
		except:
			return False
			
		
	def download(self,uri,fout):
		try:
			fdes=urllib2.urlopen(uri)
			foutdes=open(fout,"wb")
			uinfo=fdes.info()
			size = uinfo.get('content-length')
			size=int(size)
			
			uristr=uri[uri.rfind("/")+1:]
			
			#lets read the file in 1kbyte blocks
			completed=0
			x=completed*100/size
			tstr="* downloading ["+str(x)+" %] "+uristr
			sys.stdout.write(tstr)
			while True:
				block=fdes.read(1024)
				if not block:
					break
				foutdes.write(block)
				
				completed=completed+len(block)
				x=completed*100/size
				sys.stdout.write("\r")
				tstr="* downloading ["+str(x)+" %] "+uristr
				sys.stdout.write(tstr)
				#insert write op here	
			
			foutdes.close()
			
			sys.stdout.write("\n")
			
			#print "* downloaded ",uri," [",size,"] bytes"
			return True
		except:		
			return False


