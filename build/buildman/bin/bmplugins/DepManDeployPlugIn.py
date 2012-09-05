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

from urlparse import urlparse

class Server:
    def __init__(self):
        self._repository_id = "gvsig-ftp-repository"
        self._repository_url = "downloads.gvsig.org"
        self._repository_proto = "ftp"
        self._repository_login = "maven"
        self._repository_path = "/anon/pub/gvSIG-desktop/buildman-repository"
        self._repository_passwd = ""

class DepManDeployPlugIn(IPlugIn):
	
    def __init__(self):
        IPlugIn.__init__(self)
		
        self._is_Interactive = False
        self._dependency = None
        self._server = Server()
        self._servers = []
        
        self._xmlConfigFile = "settings.xml"
		
    def setDependency(self, dependency):
		 self._dependency = dependency
		
    def init(self):
        self.addGoal("deploy", "deploys an artifact")
        self.addGoalOption("deploy", "--interactive", "ask for server or artifact deployment configuration")
        self.addPreGoal("deploy", "create")	
        self._dmplugin = PlugInManager().getPlugInInstance("DepManPlugIn")
        self._dmcreateplugin = PlugInManager().getPlugInInstance("DepManCreatePlugIn")

		
        if self._dmplugin == None:
			self.reportError("PlugIn `depman` not found")
			return False
		
        if self._arguments.read("deploy"):
			self.setExecute(True)

		
        if self._arguments.read("--interactive"):
            self._is_Interactive = True
            	
         
    def initFromXML(self, node):
        distrNodes = node.getElementsByTagName("distributionManagement")
        if distrNodes != None:
            for n in distrNodes:
                if n.localName == "distributionManagement":
    				for p in n.childNodes:
    					if p.localName == "repository":
    						repoName = None
    						repoUrl = None
    						for k in p.childNodes:
    							if k.localName == "id":
    								self.server._repository_id = k.childNodes[0].nodeValue
    							if k.localName == "name":
    								repoName = k.childNodes[0].nodeValue
    							if k.localName == "url":
    								repoUrl = urlparse(k.childNodes[0].nodeValue)
    									
    						self.server._repository_url = repo.netloc
    						self.server._repository_path = repoUrl.path
    						self.server._repository_proto = repoUrl.scheme
    						print self.server._repository_proto + "://" + self.server._repository_url + "/" + self.server._repository_path
        settingsNodes = node.getElementsByTagName("servers")
        if settingsNodes != None:
            for n in settingsNodes:
                if n.localName == "servers":
                    self._servers = []
                    for p in n.childNodes:
                        if p.localName == "server":
                            server = Server()
                            for k in p.childNodes:
                                if k.localName == "id":
                                    server._repository_id = k.childNodes[0].nodeValue
                                if k.localName == "username":
                                    server._repository_login = k.childNodes[0].nodeValue
                                if k.localName == "password":
                                    server._repository_passwd = k.childNodes[0].nodeValue
                            
                            self._servers.append(server) 
                    print self._servers               
    
    def uploadFTPFile(self, url, user, pwd, destdir, path, orig_file, filename):
        #print url, user, pwd, destdir, path, orig_file, filename
        ftp = FTP_TLS(url)
        
        #ftp.set_debuglevel( 1 )
        ftp.auth_tls();  ftp.prot_p()
        ftp.login(user, pwd)
        ftp.set_pasv(1)
        #ftp.retrlines('LIST')
        ftp.cwd(destdir)
        dirs = string.split(path, os.path.sep)
        for dir in dirs:
            #print dir
            try:
                ftp.mkd(dir)
            except:
                pass
            try:
                ftp.cwd(dir)
            except:
                pass
        f = open(orig_file, "rb")
        ftp.storbinary("STOR " + filename, f)
        f.close()
        ftp.quit()

		
    def execute(self):
        self.initFromXML(self._dmplugin.getNode())
        print self._dmplugin.getMavenPath() + os.path.sep + self._xmlConfigFile
        self.loadXMLFile(self._dmplugin.getMavenPath() + os.path.sep + self._xmlConfigFile)
        
        file_name = self._dependency.getDepManFileName()
        file_path = self._dependency.getDepManUrlPath()

        tarname = file_name + ".tar.gz"
        md5name = tarname + ".md5"
        dmfile = file_name + ".xml"
		
        dmutil = BMUtil()
        surl = self._server._repository_proto + "://" + self._server._repository_url + "/" + self._server._repository_path;
        url = self._server._repository_url
        destdir = self._server._repository_path
        login = self._server._repository_login
        pwd = self._server._repository_passwd

        if self._is_Interactive:
			tmstr = "URL [*]:"
			surl = raw_input(string.replace(tmstr, "*", url))
			if len(surl) == 0:
				surl = self._server._repository_url
			repoURL = urlparse(surl)
			self._server._repository_url = repo.netloc		
			self._server._repository_path = repoUrl.path
			self._server._repository_proto = repoUrl.scheme
			url = self._server._repository_url

			tmstr = "Login [*]:"
			login = raw_input(string.replace(tmstr, "*", login))
			if len(login) == 0:
				login = self._server._repository_login
			import getpass
			pwd = getpass.getpass()
        else:
            for s in self._servers:
                if s._repository_id == self._server._repository_id:
                    login = self._server._repository_login = s._repository_login
                    pwd = self._server._repository_passwd = s._repository_passwd
        
        #tmpdir = self._dmplugin.getDepManPath() + os.path.sep + ".cache" + os.path.sep + file_path
		tmpdir = self._dmplugin.getMavenPath()+os.path.sep+"repository"+os.path.sep+file_path

        if self._server._repository_proto == "ssh":
			dmutil.rmdir(".dmn_tmp") # Prevent for uploading bad previous compilations!
			sshtmpdir = ".dmn_tmp" + os.path.sep + file_path
			dmutil.mkdir(sshtmpdir)
			shutil.copyfile(tmpdir + os.path.sep + tarname, sshtmpdir + os.path.sep + tarname)
			shutil.copyfile(tmpdir + os.path.sep + md5name, sshtmpdir + os.path.sep + md5name)
			if self._xmlfile != "":
				shutil.copyfile(tmpdir + os.path.sep + dmfile, sshtmpdir + os.path.sep + dmfile)	
			
			#scp
			base_ssh = destdir
			url_ssh = url
			scpcommand = "scp"
			pscppath = ""
			if sys.platform == "win32":
				scpcommand = self._dmplugin.getDepManDataPath() + os.path.sep + "win32" + os.path.sep + "pscp.exe"
				scpcommand = '"' + os.path.normpath(scpcommand) + '"'
			cmdstr = scpcommand + " -r " + ".dmn_tmp" + os.path.sep + "*" + " " + login + "@" + url_ssh + ":" + base_ssh
			
			print cmdstr
			os.system(cmdstr)
			dmutil.rmdir(".dmn_tmp")
			
        elif self._server._repository_proto == "ftp":			
			#ftp
			print "* Uploading ", tarname
			self.uploadFTPFile(url, login, pwd, destdir, os.path.sep + file_path, tmpdir + os.path.sep + tarname, tarname)
			print "* Uploading ", md5name
			self.uploadFTPFile(url, login, pwd, destdir, os.path.sep + file_path, tmpdir + os.path.sep + md5name, md5name)
			if not self._is_Interactive:
				print "* Uploading ", dmfile
				self.uploadFTPFile(url, login, pwd, destdir, os.path.sep + file_path, tmpdir + os.path.sep + dmfile, dmfile)

        return True

PlugInManager().registerPlugIn("DepManDeployPlugIn", DepManDeployPlugIn())		
