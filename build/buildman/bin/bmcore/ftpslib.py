# $Id: ftpslib.py 45 2006-05-30 17:30:18Z das-svn $
#
# Copyright (c) 2006 David Shea <david@gophernet.org>
# 
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to 
# deal in the Software without restriction, including without limitation the 
# rights to use, copy, modifiy, merge, publish, distribute, sublicense, and/or
# sell copies of the Software, and to permit persons to whom the Software is 
# furnished to do so, subject to the following conditions:
# 
# The above copyright notice and this permission notice shall be included in 
# all copies or substantial portions of the Software.
# 
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
# FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
# IN THE SOFTWARE.

"""An FTP over TLS client class.

This module is an extension to python's builtin ftplib module
providing a mechanism to access servers running FTP secured with TLS
as described in the proposed IETF standard RFC 4217.  The FTP_TLS
class provides a means of sending the additional commands necessary to
secure FTP data and control connections as well as handling the
encryption of traffic and changes between encrypted and clear states.

Since there are now additional commands to send as part of an FTP
session, the example has changed somewhat:

>>> from ftpslib import FTP_TLS, secureSocket_m2crypto
>>> ftp = FTP_TLS(secureSocket_m2crypto(), 'ftp.example.org')
'220 ProFTPD 1.2.10 Server (ProFTPD Default Installation)'
>>> ftp.auth_tls() # encrypt the control connection
'234 AUTH TLS successful'
>>> ftp.prot_p() # set any new data connections to be encrypted
'200 Protection set to Private'
>>> ftp.login('david', 'password') # login to the server
'230 User david loggin in.'
>>> ftp.retrlines('LIST') # list directory contents
total 9
drwxr-xr-x   8 root     wheel        1024 Jan  3  1994 .
drwxr-xr-x   8 root     wheel        1024 Jan  3  1994 ..
drwxr-xr-x   2 root     wheel        1024 Jan  3  1994 bin
drwxr-xr-x   2 root     wheel        1024 Jan  3  1994 etc
d-wxrwxr-x   2 ftp      wheel        1024 Sep  5 13:43 incoming
drwxr-xr-x   2 root     wheel        1024 Nov 17  1993 lib
drwxr-xr-x   6 1094     wheel        1024 Sep 13 19:07 pub
drwxr-xr-x   3 root     wheel        1024 Jan  3  1994 usr
-rw-r--r--   1 root     root          312 Aug  1  1994 welcome.msg
'226 Transfer complete'
>>> ftp.quit()
'221 Goodbye.'
>>>
"""

from ftplib import FTP

__all__ = ["FTP_TLS"]

def secureSocket(sock):
      import socket
      import httplib
      s = socket.ssl(sock, None, None)
      newsock = httplib.FakeSocket(sock, s)
      return newsock

class FTP_TLS(FTP):
   """An FTP/TLS client class.

   This class implements the following extensions to FTP:

      - AUTH
         The only form of this command supported is 'TLS'.  When sent
         using either auth_tls() or sendcmd, the control connection
         will be encrypted using TLS.
      - CCC
         Clears the encryption on the control connection.
      - PROT
         PROT P (protect) tells the server to open new data
         connections as encrypted, and PROT C (clear) tells the server
         to open new data connections unencrypted.  The initial state,
         as specified by RFC 4217, is PROT C.  RFC 4217 allows any
         command that opens a data connection to be rejected based on
         PROT settings depending on the configuration of the server.
      - PBSZ
         'PBSZ 0' must be sent before all PROT commands.  This is
         handled by the prot_p and prot_c methods, but the command
         must be sent manually if PROT is sent using sendcmd or
         voidcmd.
      - REIN
         In addition to resetting the current user and working
         direction, REIN will reset the status of the control and data
         connections to clear.
   """

   def __init__(self, host=''):
      """Create a new FTP_TLS instance.

      secureConnection is a function that takes a socket as its
      argument and returns a socket object that will send and recv
      using TLS.  If the optional host argument is provided, a
      connection will be established using connect(host).  No
      automatic login is provided, as the client must decide when and
      whether to request encrypted connections."""

      self.secureConnection = secureSocket 
      self.control_state = 0
      self.data_state = 0
      FTP.__init__(self, host)

   def connect(self, host='', port=0):
      """Connect to a host.

      See FTP.connect."""
      ret = FTP.connect(self, host, port)
      self.clear_sock = self.sock
      self.clear_file = self.file

   def _checkCommand(self, cmd):
      command = ''
      arg = ''

      try:
         command = cmd.split(' ')[0].upper()
      except IndexError:
         raise ValueError("Argument to sendcmd must not be null")

      try:
         arg = cmd.split(' ', 1)[1].upper()
      except IndexError:
         pass

      # check that the command is valid
      if cmd == 'AUTH':
         if arg not in ('TLS', 'TLS-C'):
            raise ValueError("Argument " + arg + " to AUTH not supported")
      elif cmd == 'PROT':
         if arg not in ('P', 'C'):
            raise ValueError("Argument " + arg + " to PROT not supported")

      return (command, arg)

   def _postCommand(self, command, arg):
      if command == 'AUTH':
         self.control_state = 1
         self.sock = self.secureConnection(self.clear_sock)
         self.file = self.sock.makefile('rb')
      elif command == 'CCC':
         self.control_state = 0
         self.sock = self.clear_sock
         self.file = self.clear_file
      elif command == 'PROT':
         if arg == 'P':
            self.data_state = 1
         elif arg == 'C':
            self.data_state = 0
      elif command == 'REIN':
         self.control_state = 0
         self.data_state = 0
         self.sock = self.clear_sock
         self.file = self.clear_file

   def sendcmd(self, cmd):
      """Send a command and return the response."""
      command, arg = self._checkCommand(cmd)
      ret = FTP.sendcmd(self, cmd)
      if ret[0] == '2':
         self._postCommand(command, arg)
      return ret

   def voidcmd(self, cmd):
      """Send a command and expect a response beginning with '2'."""
      command, arg = self._checkCommand(cmd)
      ret = FTP.voidcmd(self, cmd)
      # If voidcmd fails, postCommand will (correctly) not be reached
      self._postCommand(command, arg)
      return ret

   def ntransfercmd(self, cmd, rest=None):
      """Initiate a transfer over the data connection.

      See FTP.ntransfercmd."""
      conn, size = FTP.ntransfercmd(self, cmd, rest)
      if self.data_state:
         conn = self.secureConnection(conn)
      return conn, size

   def retrbinary(self, cmd, callback, blocksize=8192, rest=None):
      """Retrieve data in binary mode.

      See FTP.retrbinary."""
      # This code is copied from FTP.retrbinary
      # For whatever reason, using the file.read doesn't throw
      # an exception when the connection is closed without shutting down TLS
      self.voidcmd('TYPE I')
      conn = self.transfercmd(cmd, rest)
      fp = conn.makefile('rb')
      while 1:
         data = fp.read(blocksize)
         if not data:
            break
         callback(data)
      conn.close()
      return self.voidresp()

   def storbinary(self, cmd, fp, blocksize=8192):
		'''Store a file in binary mode.'''
		self.voidcmd('TYPE I')
		conn = self.transfercmd(cmd)
		while 1:
			buf = fp.read(blocksize)
			if not buf: break
			conn.sendall(buf)
		conn.close()
		return

   def auth_tls(self):
      """Secure the control connection using AUTH TLS."""
      return self.sendcmd('AUTH TLS')

   def ccc(self):
      """Clear encryption on the control connection."""
      return self.sendcmd('CCC')

   def prot_c(self):
      """Clear the data connection.

      This method sends both 'PBSZ 0' and 'PROT C' and returns the
      response from the PROT command."""
      self.voidcmd('PBSZ 0')
      return self.sendcmd('PROT C')

   def prot_p(self):
      """Secure the data connection."

      This method sends both 'PBSZ 0' and 'PROT P' and returns the
      response from the PROT command."""
      self.voidcmd('PBSZ 0')
      return self.sendcmd('PROT P')
def test():
    server,user,pwd=map( raw_input, ('server:','user:','pwd:') )   #'ftp.xxxx.xx','yyyy','zzzz'
    verbose=1
    import StringIO
    f=StringIO.StringIO('aaaaaaaaaaaaaaa')
    ftp = FTP_TLS(server)
    ftp.set_debuglevel( verbose )
    ftp.auth_tls();  ftp.prot_p()
    ftp.login(user,pwd)
    ftp.set_pasv( 1 )
    ftp.retrlines('LIST')
    ftp.storbinary("STOR file0.txt", f)
    #ftp.retrlines('LIST')

if __name__=='__main__':
    test()


