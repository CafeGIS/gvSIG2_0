"""
	Path where plugins reside
	Each plugin should implement IPlugIn interface
	from bmbase.
"""



import os
import sys
from bmcore.BMUtil import BMUtil
import imp

def bmimport(name, paths):
	# Fast path: see if the module has already been imported.
    try:
        return sys.modules[name]
    except KeyError:
        pass

    # If any of the following calls raises an exception,
    # there's a problem we can't handle -- let the caller handle it.

    fp, pathname, description = imp.find_module(name,paths)

    try:
        return imp.load_module(name, fp, pathname, description)
    finally:
        # Since we may exit via an exception, close fp explicitly.
        if fp:
            fp.close()

util = BMUtil()
searchPaths=[os.path.join(sys.path[0],'bmplugins'),util.buildmanPlugInPath(),os.path.join(sys.path[0],'../bmplugins')]

plugList = []
for dir in searchPaths:
	if not os.path.exists(dir):
		continue
	for f in os.listdir(dir):
		if f.endswith('.py') and not f.startswith('__init__'):
			plugList.append(f[0:-3])

print plugList
for m in plugList:
	bmimport(m,searchPaths)

