from java.lang import Character
from javax.swing import JWindow, JList, JScrollPane
from java.awt import Color, Dimension
from java.awt.event import KeyAdapter
from java.awt.event import KeyEvent
import string

__author__ = "Don Coleman <dcoleman@chariotsolutions.com>"
__cvsid__ = "$Id: popup.py 5782 2006-06-12 07:56:49Z jmvivo $"

class Popup(JWindow):
    """Popup window to display list of methods for completion"""
    
    MAX_HEIGHT = 300
    MIN_WIDTH = 200
    MAX_WIDTH = 400
    
    def __init__(self, frame, textComponent):
        JWindow.__init__(self, frame)
        self.textComponent = textComponent
        self.size = (200,200)
        self.list = JList(keyPressed=self.key)
        self.list.setBackground(Color(255,255,225)) # TODO move this color
        self.getContentPane().add(JScrollPane(self.list))
        self.list.setSelectedIndex(0)

        self.originalData = ""
        self.data = ""
        self.typed = ""

    def key(self, e):
        # key listener
        #print "Other Listener"
        code = e.getKeyCode()
        #print 'keychar:',e.getKeyChar()
        
        if code == KeyEvent.VK_ESCAPE:
            self.hide()

        elif code == KeyEvent.VK_ENTER or code == KeyEvent.VK_TAB:
            self.chooseSelected()
            e.consume()

        elif code == 8: # BACKSPACE
            self.typed = self.typed[:-1]
            self.data = filter(self.originalData, self.typed)
            self.list.setListData(self.data)
            self.list.setSelectedIndex(0)
                
        elif code == KeyEvent.VK_UP:
            self.previous()
            # consume event to avoid history previous
            e.consume()
            
        elif code == KeyEvent.VK_DOWN:
            self.next()
            # consume event to avoid history next
            e.consume()
            
        else:
            char = e.getKeyChar()
            if Character.isJavaLetterOrDigit(char):
                self.typed += char 
                self.data = filter(self.data, self.typed)
                self.list.setListData(self.data)
                self.list.setSelectedIndex(0)
                
    def next(self):
        index = self.list.getSelectedIndex()
        max = (self.list.getModel().getSize() - 1)
        
        if index < max:
            index += 1
            self.list.setSelectedIndex(index)
            self.list.ensureIndexIsVisible(index)
        
    def previous(self):
        index = self.list.getSelectedIndex()

        if index > 0:
            index -= 1
            self.list.setSelectedIndex(index)
            self.list.ensureIndexIsVisible(index)

    def chooseSelected(self):
        """Choose the selected value in the list"""
        value = self.list.getSelectedValue()
        startPosition = self.dotPosition + 1
        caretPosition = self.textComponent.getCaretPosition()
        self.textComponent.select(startPosition, caretPosition) 
        self.textComponent.replaceSelection(value)
        self.textComponent.setCaretPosition(startPosition + len(value))
        self.hide()

    def setMethods(self, methodList):
        methodList.sort()
        self.data = methodList
        self.originalData = methodList
        self.typed = ""
        self.list.setListData(methodList)

    def show(self):
        # when the popup becomes visible, get the cursor
        # so we know how to replace the selection
        self.dotPosition = self.textComponent.getCaretPosition()
        self.setSize(self.getPreferredSize())
        JWindow.show(self)

    def getPreferredSize(self):
        # need to add a magic amount to the size to avoid scrollbars
        # I'm sure there's a better way to do this
        MAGIC = 20
        size = self.list.getPreferredScrollableViewportSize()
        height = size.height + MAGIC
        width = size.width + MAGIC
        if height > Popup.MAX_HEIGHT:
            height = Popup.MAX_HEIGHT
        if width > Popup.MAX_WIDTH:
            width = Popup.MAX_WIDTH
        if width < Popup.MIN_WIDTH:
            widht = Popup.MIN_WIDTH
        return Dimension(width, height)

    
# this needs a list renderer that will hilight the prefix
def filter(list, prefix):
    prefix = prefix.lower()
    list = [eachItem for eachItem in list if str(eachItem).lower().startswith(prefix)]
    return list

