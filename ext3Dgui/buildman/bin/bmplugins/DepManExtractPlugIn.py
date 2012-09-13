from bmbase.IPlugIn import IPlugIn
from bmbase.PlugInManager import PlugInManager
from bmcore.BMUtil import BMUtil
from xml.dom import minidom
import os

class DepManExtractPlugIn(IPlugIn):
    def __init__(self):
        IPlugIn.__init__(self)
        self._xmlFile = "depman.xml"
        self._destDir = "."
        self._filter = "*"
        self._isFromCache=False
        self._isInteractive=True
        self._depmanNode = None

    def init(self):
        self.addGoal("extract", "Extract the files of dependencies and package sections of a depman xml file.")
        self.addGoalOption("extract","--file", "Sets an alternate xml file. Default: depman.xml")
        self.addGoalOption("extract","--destdir", "Sets the destination directory of the extraction. Default: `.`")
        self.addGoalOption("extract","--filter", "Sets the the filter of the files to extract. Default: `*`")
        self.addGoalOption("extract","--cache", "Cache is preferred")
        self.addGoalOption("extract","--remote", "Remote repository is preferred")
    
        isExtract = False
        if self._arguments.read("extract"):
            self.setExecute(True)
            isExtract = True

        if not isExtract:
            return    
        
        args = [""]
        if self._arguments.read("--file", args):
            self._xmlFile = args[0]   
            
        args = [""]
        if self._arguments.read("--destdir", args):
            self._destDir = args[0]
            
        args = [""]
        if self._arguments.read("--filter", args):
            self._filter = args[0] 
            
        if self._arguments.read("--cache"):
            self._isFromCache = True
            self._isInteractive = False
        
        if self._arguments.read("--remote"):
            self._isFromCache = False
            self._isInteractive = False

    def initFromXML(self,node):
        if node.localName=="extract":
            if node.hasAttributes():
                if node.attributes.has_key("file"):
                    elf._xmlFile = node.attributes.get("file").value
                if node.attributes.has_key("destdir"):
                    elf._destDir = node.attributes.get("destdir").value                   
                if node.attributes.has_key("filter"):
                    elf._filter = node.attributes.get("filter").value    
                foundCache = False
                if node.attributes.has_key("cache"):
                    value = node.attributes.get("cache").value
                    if value == "True" or value == "true":
                        self._isFromCache = True
                        self._isInteractive = False
                if node.attributes.has_key("remote") and not foundCache:
                    value = node.attributes.get("remote").value
                    if value == "True" or value == "true":
                        self._isFromCache = False
                        self._isInteractive = False
        else:
            self._depmanNode = node
                    
    def execute(self):
        print "Executing Plugin:" + str(self.__class__)
        return self.extract()

    def extract(self):
        self._dmupdate = PlugInManager().getPlugInInstance("DepManUpdatePlugIn")
        if self._dmupdate == None:
            self.reportError("PlugIn `depman update` not found")
            return
        
        if self._depmanNode == None:
            self.loadXMLFile(self._xmlFile)
        
        if not self._isInteractive:
            if self._isFromCache:
                self._dmupdate.setForceCache()
            else:
                self._dmupdate.setForceRemote()
                
        dependency_filenamepaths = self._dmupdate.getDependencies(self._depmanNode)
        package_namepath = self.parsePackage(self._depmanNode)
        print dependency_filenamepaths, self._destDir, self._filter
        
        bmutil = BMUtil()
        for file in dependency_filenamepaths:
            bmutil.untargzFiltered(file, self._destDir, self._filter)
        
    
    def parsePackage(self,node):
        return ""


PlugInManager().registerPlugIn("DepManExtractPlugIn",DepManExtractPlugIn())


