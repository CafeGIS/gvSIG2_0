"""
Jython Console with Code Completion

This uses the basic Jython Interactive Interpreter.
The UI uses code from Carlos Quiroz's 'Jython Interpreter for JEdit' http://www.jedit.org
"""
from javax.swing import JFrame, JScrollPane, JWindow, JTextPane, Action, KeyStroke, WindowConstants
from javax.swing.text import JTextComponent, TextAction, SimpleAttributeSet, StyleConstants
from java.awt import Color, Font, FontMetrics, Point
from java.awt.event import  InputEvent, KeyEvent, WindowAdapter

import jintrospect
from popup import Popup
from tip import Tip
from history import History

import sys
import traceback
from code import InteractiveInterpreter
#from org.python.util import InteractiveConsole
InteractiveConsole=sys.gvSIG.classForName("org.python.util.InteractiveConsole")

__author__ = "Don Coleman <dcoleman@chariotsolutions.com>"
__cvsid__ = "$Id: console.py 5910 2006-06-20 10:03:31Z jmvivo $"

def debug(name, value=None):
    if value == None:
        print >> sys.stderr, name
    else:
        print >> sys.stderr, "%s = %s" % (name, value)


class Console:
    PROMPT = sys.ps1
    PROCESS = sys.ps2
    BANNER = ["Jython Completion Shell", InteractiveConsole.getDefaultBanner()]

    def __init__(self, frame):

        self.frame = frame # TODO do I need a reference to frame after the constructor?
        self.history = History(self)
        self.bs = 0 # what is this?

        # command buffer
        self.buffer = []
        self.locals = {"gvSIG":sys.gvSIG}

        self.interp = Interpreter(self, self.locals)
        sys.stdout = StdOutRedirector(self)

        # create a textpane
        self.output = JTextPane(keyTyped = self.keyTyped, keyPressed = self.keyPressed)
        # TODO rename output to textpane

        # CTRL UP AND DOWN don't work
        keyBindings = [
            (KeyEvent.VK_ENTER, 0, "jython.enter", self.enter),
            (KeyEvent.VK_DELETE, 0, "jython.delete", self.delete),
            (KeyEvent.VK_HOME, 0, "jython.home", self.home),
            (KeyEvent.VK_UP, 0, "jython.up", self.history.historyUp),
            (KeyEvent.VK_DOWN, 0, "jython.down", self.history.historyDown),
            (KeyEvent.VK_PERIOD, 0, "jython.showPopup", self.showPopup),
            (KeyEvent.VK_ESCAPE, 0, "jython.hide", self.hide),
            
            ('(', 0, "jython.showTip", self.showTip),
            (')', 0, "jython.hideTip", self.hideTip),
            
            #(KeyEvent.VK_UP, InputEvent.CTRL_MASK, DefaultEditorKit.upAction, self.output.keymap.getAction(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0))),
            #(KeyEvent.VK_DOWN, InputEvent.CTRL_MASK, DefaultEditorKit.downAction, self.output.keymap.getAction(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0)))
            ]
        # TODO rename newmap to keymap
        newmap = JTextComponent.addKeymap("jython", self.output.keymap)
        for (key, modifier, name, function) in keyBindings:
            newmap.addActionForKeyStroke(KeyStroke.getKeyStroke(key, modifier), ActionDelegator(name, function))

        self.output.keymap = newmap
                
        self.doc = self.output.document
        #self.panel.add(BorderLayout.CENTER, JScrollPane(self.output))
        self.__propertiesChanged()
        self.__inittext()
        self.initialLocation = self.doc.createPosition(self.doc.length-1)

	# Don't pass frame to popups. JWindows with null owners are not focusable
	# this fixes the focus problem on Win32, but make the mouse problem worse
        self.popup = Popup(None, self.output)
        self.tip = Tip(None)

        # get fontmetrics info so we can position the popup
        metrics = self.output.getFontMetrics(self.output.getFont())
        self.dotWidth = metrics.charWidth('.')
        self.textHeight = metrics.getHeight()

        # add some handles to our objects
        self.locals['console'] = self

        self.caret = self.output.getCaret()

    # TODO refactor me
    def getinput(self):
        offsets = self.__lastLine()
        text = self.doc.getText(offsets[0], offsets[1]-offsets[0])
        return text

    def getDisplayPoint(self):
        """Get the point where the popup window should be displayed"""
        screenPoint = self.output.getLocationOnScreen()
        caretPoint = self.output.caret.getMagicCaretPosition()

        # TODO use SwingUtils to do this translation
        x = screenPoint.getX() + caretPoint.getX() + self.dotWidth 
        y = screenPoint.getY() + caretPoint.getY() + self.textHeight
        return Point(int(x),int(y))

    def hide(self, event=None):
        """Hide the popup or tip window if visible"""
        if self.popup.visible:
            self.popup.hide()
        if self.tip.visible:
            self.tip.hide()

    def hideTip(self, event=None):
        self.tip.hide()
        # TODO this needs to insert ')' at caret!
        self.write(')')

    def showTip(self, event=None):
        # get the display point before writing text
        # otherwise magicCaretPosition is None
        displayPoint = self.getDisplayPoint()

        if self.popup.visible:
            self.popup.hide()
        
        line = self.getinput()
        #debug("line", line)
        # Hack 'o rama
        line = line[:-1] # remove \n
        line += '('
        #debug("line", line)

        # TODO this needs to insert '(' at caret!
        self.write('(')
        
        (name, argspec, tip) = jintrospect.getCallTipJava(line, self.locals)
        #debug("name", name)
        #debug("argspec", argspec)
        #debug("tip", tip)

        if tip:
            self.tip.setLocation(displayPoint)
            self.tip.setText(tip)
            self.tip.show()
            

    def showPopup(self, event=None):

        line = self.getinput()
        # this is silly, I have to add the '.' and the other code removes it.
        line = line[:-1] # remove \n
        line = line + '.'
        #print >> sys.stderr, "line:",line
        
        # TODO get this code into Popup
        # TODO handle errors gracefully
        try:
            list = jintrospect.getAutoCompleteList(line, self.locals)
        except Exception, e:
            # TODO handle this gracefully
            print >> sys.stderr, e
            return

        if len(list) == 0:
            #print >> sys.stderr, "list was empty"
            return

        self.popup.setLocation(self.getDisplayPoint())

        self.popup.setMethods(list)
        self.popup.show()
        self.popup.list.setSelectedIndex(0)

    def inLastLine(self, include = 1):
        """ Determines whether the cursor is in the last line """
        limits = self.__lastLine()
        caret = self.output.caretPosition
        if self.output.selectedText:
            caret = self.output.selectionStart
        if include:
            return (caret >= limits[0] and caret <= limits[1])
        else:
            return (caret > limits[0] and caret <= limits[1])

    def enter(self, event):
        """ Triggered when enter is pressed """
        offsets = self.__lastLine()
        text = self.doc.getText(offsets[0], offsets[1]-offsets[0])
        text = text[:-1] # chomp \n
        self.buffer.append(text)
        source = "\n".join(self.buffer)
        more = self.interp.runsource(source)
        if more:
            self.printOnProcess()
        else:
            self.resetbuffer()
            self.printPrompt()
        self.history.append(text)

        self.hide()

    def resetbuffer(self):
        self.buffer = []

    # home key stops after prompt
    def home(self, event):
		""" Triggered when HOME is pressed """
		if self.inLastLine():
			self.output.caretPosition = self.__lastLine()[0]
		else:
			lines = self.doc.rootElements[0].elementCount
			for i in xrange(0,lines-1):
				offsets = (self.doc.rootElements[0].getElement(i).startOffset, \
					self.doc.rootElements[0].getElement(i).endOffset)
				line = self.doc.getText(offsets[0], offsets[1]-offsets[0])
				if self.output.caretPosition >= offsets[0] and \
					self.output.caretPosition <= offsets[1]:
					if line.startswith(Console.PROMPT) or line.startswith(Console.PROCESS):
						self.output.caretPosition = offsets[0] + len(Console.PROMPT)
					else:
						self.output.caretPosition = offsets[0]

    def replaceRow(self, text):
        """ Replaces the last line of the textarea with text """
        offset = self.__lastLine()
        last = self.doc.getText(offset[0], offset[1]-offset[0])
        if last != "\n":
            self.doc.remove(offset[0], offset[1]-offset[0]-1)
        self.__addOutput(self.infoColor, text)

    # don't allow prompt to be deleted
    # this will cause problems when history can contain multiple lines
    def delete(self, event):
        """ Intercepts delete events only allowing it to work in the last line """
        if self.inLastLine():
            if self.output.selectedText:
                self.doc.remove(self.output.selectionStart, self.output.selectionEnd - self.output.selectionStart)
            elif self.output.caretPosition < self.doc.length:
                self.doc.remove(self.output.caretPosition, 1)

    # why is there a keyTyped and a keyPressed?
    def keyTyped(self, event):
        #print >> sys.stderr, "keyTyped", event.getKeyCode()
        if not self.inLastLine():
            event.consume()
        if self.bs:
            event.consume()
        self.bs=0

    def keyPressed(self, event):
        if self.popup.visible:
            self.popup.key(event)
        #print >> sys.stderr, "keyPressed", event.getKeyCode()
        if event.keyCode == KeyEvent.VK_BACK_SPACE:
            offsets = self.__lastLine()
            if not self.inLastLine(include=0):
                self.bs = 1
            else:
                self.bs = 0

    # TODO refactor me
    def write(self, text):
        self.__addOutput(self.infoColor, text)

    def printResult(self, msg):
        """ Prints the results of an operation """
        self.__addOutput(self.output.foreground, "\n" + str(msg))

    def printError(self, msg): 
        self.__addOutput(self.errorColor, "\n" + str(msg))

    def printOnProcess(self):
        """ Prints the process symbol """
        self.__addOutput(self.infoColor, "\n" + Console.PROCESS)

    def printPrompt(self):
        """ Prints the prompt """
        self.__addOutput(self.infoColor, "\n" + Console.PROMPT)
		
    def __addOutput(self, color, msg):
        """ Adds the output to the text area using a given color """
        from javax.swing.text import BadLocationException
        style = SimpleAttributeSet()

        if color:
            style.addAttribute(StyleConstants.Foreground, color)

        self.doc.insertString(self.doc.length, msg, style)
        self.output.caretPosition = self.doc.length

    def __propertiesChanged(self):
        """ Detects when the properties have changed """
        self.output.background = Color.white #jEdit.getColorProperty("jython.bgColor")
        self.output.foreground = Color.blue #jEdit.getColorProperty("jython.resultColor")
        self.infoColor = Color.black #jEdit.getColorProperty("jython.textColor")
        self.errorColor = Color.red # jEdit.getColorProperty("jython.errorColor")

        family = "Monospaced" # jEdit.getProperty("jython.font", "Monospaced")
        size = 14 #jEdit.getIntegerProperty("jython.fontsize", 14)
        style = Font.PLAIN #jEdit.getIntegerProperty("jython.fontstyle", Font.PLAIN)
        self.output.setFont(Font(family,style,size))

    def __inittext(self):
        """ Inserts the initial text with the jython banner """
        self.doc.remove(0, self.doc.length)
        for line in "\n".join(Console.BANNER):
            self.__addOutput(self.infoColor, line)
        self.printPrompt()
        self.output.requestFocus()

    def __lastLine(self):
        """ Returns the char offests of the last line """
        lines = self.doc.rootElements[0].elementCount
        offsets = (self.doc.rootElements[0].getElement(lines-1).startOffset, \
                   self.doc.rootElements[0].getElement(lines-1).endOffset)
        line = self.doc.getText(offsets[0], offsets[1]-offsets[0])
        if len(line) >= 4 and (line[0:4]==Console.PROMPT or line[0:4]==Console.PROCESS):
            return (offsets[0] + len(Console.PROMPT), offsets[1])
        return offsets


class ActionDelegator(TextAction):
	"""
		Class action delegator encapsulates a TextAction delegating the action
		event to a simple function
	"""
	def __init__(self, name, delegate):
		TextAction.__init__(self, name)
		self.delegate = delegate

	def actionPerformed(self, event):
		if isinstance(self.delegate, Action):
			self.delegate.actionPerformed(event)
		else:
			self.delegate(event)

class Interpreter(InteractiveInterpreter):
    def __init__(self, console, locals):
        InteractiveInterpreter.__init__(self, locals)
        self.console = console
        
        
    def write(self, data):
        # send all output to the textpane
        # KLUDGE remove trailing linefeed
        self.console.printError(data[:-1])
        

# redirect stdout to the textpane
class StdOutRedirector:
    def __init__(self, console):
        self.console = console
        
    def write(self, data):
        #print >> sys.stderr, ">>%s<<" % data
        if data != '\n':
            # This is a sucky hack.  Fix printResult
            self.console.printResult(data)

class JythonFrame(JFrame):
    def __init__(self):
        self.title = "Jython"
        self.size = (600, 400)
        try:
            #No queremos que se salga cuando cerremos la ventana
            ##self.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)            
            self.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
        except:
            # assume jdk < 1.4
            self.addWindowListener(KillListener())
            self.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)

class KillListener(WindowAdapter):
    """
    Handle EXIT_ON_CLOSE for jdk < 1.4
    Thanks to James Richards for this method
    """
    def windowClosed(self, evt):
        import java.lang.System as System
        System.exit(0)

def main():
    frame = JythonFrame()
    console = Console(frame)
    frame.getContentPane().add(JScrollPane(console.output))
    frame.show()
    
if __name__ == "__main__":
    main()
