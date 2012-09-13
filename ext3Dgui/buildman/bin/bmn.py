#!/usr/bin/env python


__authors__=["Rafael Gaitan <rgaitan@ai2.upv.es>","Pedro Jorquera <pjorquera@okode.com>","Enrique Medina <quiqueiii@gmail.com>"]
__date__="30 Nov 2007"
__copyright__="Copyright 2007 AI2/OKODE"
__license__="GPL"
__version__="1.0.0"
__URL__="https://murray.ai2.upv.es/svn/buildman"

import sys
from bmcore import *

#main
def main():
	
	arguments = ArgumentParser(sys.argv)
	arguments.getApplicationUsage().setDescription(arguments.getApplicationName() + "is a set of scripts to create automatic and continuous build systems")
	arguments.getApplicationUsage().addCommandLineOption("--help", "Gets this help")
	arguments.getApplicationUsage().addCommandLineOption("--help-all", "Gets the help for all existing tasks")
	arguments.getApplicationUsage().addCommandLineOption("--help-goal [goal]", "Gets the help for the given task")

	if len(sys.argv)<2:
		arguments.writeHelpMessages()
		return

	if arguments.read("--help"):
		arguments.writeHelpMessages()
		return

	help_goal = False
	help_all = False
	goal = [""]
	if arguments.read("--help-goal",goal):
		help_goal = True
	if arguments.read("--help-all"):
		help_all = True

	bmn = BuildMan(arguments) 

	if help_goal:
		arguments.writeGoalHelpMessages(goal[0])
		return

	if help_all:
		arguments.writeHelpMessages()
		return
	if arguments.errors():
		arguments.writeErrorMessages()
		return

	if not bmn.run():
		bmn.writeErrorMessages()
	

if __name__ == '__main__': main()

