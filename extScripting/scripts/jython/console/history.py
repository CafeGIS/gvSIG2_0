"""
 history.py - Handles the History of the jython console
 Copyright (C) 2001 Carlos Quiroz

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
"""
#from org.gjt.sp.jedit.gui import HistoryModel

class History:
    """
    The class history handles the history management basically wrapping the
    built-in jEdit's history capabilities
    """

    def __init__(self, console):
        self.history = []
	self.console = console
	self.index = 0
	self.last = ""

    def append(self, line):
        if line == '\n' or len(line) == 0: return
## 	if line == self.last: # avoid duplicates
## 	    self.index = len(self.history) - 1
##          return
		
	self.last = line
	self.history.append(line)
	self.index = len(self.history) - 1

    def historyUp(self, event):
        if len(self.history) > 0 and self.console.inLastLine():
	    self.console.replaceRow(self.history[self.index])
	    self.index = max(self.index - 1, 0)

    def historyDown(self, event):
        if len(self.history) > 0 and self.console.inLastLine():
	    if self.index == len(self.history) - 1:
	        self.console.replaceRow("")
	    else:
	        self.index += 1
	        self.console.replaceRow(self.history[self.index])

