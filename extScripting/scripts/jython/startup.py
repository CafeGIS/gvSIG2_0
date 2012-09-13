print "*** running startup.py..."

True = 1 == 1
False = 1 == 0

import sys
from java.io import File

sys.gvSIG = gvSIG

# Add current path to path
try:
  sys.path.append(File(params.get("fileName")).getParent())
except:
  pass

# Add Scripts/jython/Libs to path
sys.path.append(gvSIG.getScriptsDirectory()+File.separatorChar+"jython"+File.separatorChar+"Lib")

# Add Scripts/jython to path
sys.path.append(gvSIG.getScriptsDirectory()+File.separatorChar+"jython")

# Add Scripts to path
sys.path.append(gvSIG.getScriptsDirectory())

print "*** startup.py... OK "