
import sys
from java.io import File

# Cuando se importa desde la consola no esta definida
# la variable global gvSIG
try:
  sys.gvSIG = gvSIG
except:
  gvSIG = sys.gvSIG

SaxContentHandler = gvSIG.classForName("org.xml.sax.ContentHandler")

ShapeFactory =      gvSIG.classForName("com.iver.cit.gvsig.fmap.core.ShapeFactory")
FConstant =         gvSIG.classForName("com.iver.cit.gvsig.fmap.core.v02.FConstant")
FSymbol =           gvSIG.classForName("com.iver.cit.gvsig.fmap.core.v02.FSymbol")
FGraphic =          gvSIG.classForName("com.iver.cit.gvsig.fmap.rendering.FGraphic")

LayerFactory =      gvSIG.classForName("com.iver.cit.gvsig.fmap.layers.LayerFactory")
LayerCollection =   gvSIG.classForName("com.iver.cit.gvsig.fmap.layers.layerOperations.LayerCollection")

PointListener =     gvSIG.classForName("com.iver.cit.gvsig.fmap.tools.Listeners.PointListener")
PointBehavior =     gvSIG.classForName("com.iver.cit.gvsig.fmap.tools.Behavior.PointBehavior")
PointEvent =        gvSIG.classForName("com.iver.cit.gvsig.fmap.tools.Events.PointEvent")

InfoListener =      gvSIG.classForName("com.iver.cit.gvsig.project.documents.view.toolListeners.InfoListener")


def showMessageDialog(msg):
  from javax.swing import JOptionPane

  JOptionPane.showMessageDialog(None, msg)
