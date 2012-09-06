/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Gobernment (CIT)
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
* MA  02110-1301, USA.
*
*/

/*
 * AUTHORS (In addition to CIT):
 * 2008 {DiSiD Technologies}  {Create a BaseException as a RuntimeException}
 */

package org.gvsig.tools.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

/**
 * Adds RuntimeException nature to the BaseException.
 *
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public abstract class BaseRuntimeException extends RuntimeException implements
        IBaseException {

    private static final long serialVersionUID = 4584809667815834006L;

    // Inner delegate exception
	private DelegateBaseException exception;

    /**
     * Constructs a RuntimeBaseException with a default message format, a key to
     * find a localized message format, and a unique code to identify the
     * exception.
     *
     * @param message
     *            the default messageFormat to describe the exception
     * @param key
     *            the key to use to search a localized messageFormnata
     * @param code
     *            the unique code to identify the exception
     */
    public BaseRuntimeException(String message, String key, long code) {
        exception = new DelegateBaseException(message, key, code, this);
    }

    /**
     * Constructs a BaseException with a default message format, a key to find a
     * localized message format, and a unique code to identify the exception.
     *
     * @param message
     *            the default messageFormat to describe the exception
     * @param cause
     *            the original cause of the exception
     * @param key
     *            the key to use to search a localized messageFormnata
     * @param code
     *            the unique code to identify the exception
     */
    public BaseRuntimeException(String message, Throwable cause, String key,
            long code) {
        exception = new DelegateBaseException(message, cause, key, code, this);
    }

    public boolean equals(Object obj) {
        return exception.equals(obj);
    }

    public Throwable getCause() {
        return exception.getCause();
    }

    public long getCode() {
        return exception.getCode();
    }

    public String getFormatString() {
        return exception.getFormatString();
    }

    public String getLocalizedMessage() {
        return exception.getLocalizedMessage();
    }

    public String getLocalizedMessage(ExceptionTranslator translator,
            int indent) {
        return exception.getLocalizedMessage(translator, indent);
    }

    public String getLocalizedMessageStack() {
        return exception.getLocalizedMessageStack();
    }

    public String getLocalizedMessageStack(ExceptionTranslator translator,
            int indent) {
        return exception.getLocalizedMessageStack(translator, indent);
    }

    public String getMessage() {
        return exception.getMessage();
    }

    public String getMessage(int indent) {
        return exception.getMessage(indent);
    }

    public String getMessageKey() {
        return exception.getMessageKey();
    }

    public String getMessageStack() {
        return exception.getMessageStack();
    }

    public String getMessageStack(int indent) {
        return exception.getMessageStack(indent);
    }

    public StackTraceElement[] getStackTrace() {
        return exception.getStackTrace();
    }

    public int hashCode() {
        return exception.hashCode();
    }

    public Throwable initCause(Throwable cause) {
        return exception.initCause(cause);
    }

    public Iterator iterator() {
        return exception.iterator();
    }

    public void printStackTrace() {
        exception.printStackTrace();
    }

    public void printStackTrace(PrintStream s) {
        exception.printStackTrace(s);
    }

    public void printStackTrace(PrintWriter s) {
        exception.printStackTrace(s);
    }

    public void setCode(long code) {
        exception.setCode(code);
    }

    public void setFormatString(String formatString) {
        exception.setFormatString(formatString);
    }

    public void setMessageKey(String messageKey) {
        exception.setMessageKey(messageKey);
    }

    public void setStackTrace(StackTraceElement[] stackTrace) {
        exception.setStackTrace(stackTrace);
    }

    public String toString() {
        return exception.toString();
    }

    /**
     * Used to return a map that serves to replace in the format string the keys
     * with the corresponding values.
     *
     * @return the message values
     */
	protected Map values() {
		 return exception.getInnerValues();
	}

	protected void setValue(String name, Object value) {
		exception.setValue(name, value);
	}

    /**
     * Inner BaseException implementation to use as the inner exception.
     *
     * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
     */
    private class DelegateBaseException extends BaseException {

        private static final long serialVersionUID = 1784643169215420315L;

        private BaseRuntimeException baseException;

        public DelegateBaseException(String message, String key, long code,
                BaseRuntimeException baseException) {
            super(message, key, code);
            this.baseException = baseException;
        }

        public DelegateBaseException(String message, Throwable cause,
                String key, long code, BaseRuntimeException baseException) {
            super(message, cause, key, code);
            this.baseException = baseException;
        }

		 protected Map values() {
			return baseException.values();
		}

		 protected Map getInnerValues() {
			return super.values();
		}
    }
}