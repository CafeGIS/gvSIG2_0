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
 */
package org.gvsig.tools.exception;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * Esta clase esta pensada para actuar como clase base para las excepciones que
 * se lanzan dentro del proyecto de gvSIG.
 *
 * Añade la implementacion necesaria para disponer de mensajes de error
 * internacionalizables, a traves del metodo getLocalizedMessage, asi como una
 * serie de metodos que nos permiten obtener los mesanes de error de la cadena
 * de excepciones enlazadas a traves de su "causa", asi como utilidades que
 * permitan recorrer de forma comoda esta cadena de excepciones por medio de un
 * Iterador.
 *
 * @author Equipo de desarrollo de gvSIG.
 *
 */
public abstract class BaseException extends Exception implements IBaseException {
	/**
	 *
	 */
	private static final long serialVersionUID = 6631799898764636572L;
	private final static String BLANKS ="                                                                                                     ";
	private static ExceptionTranslator translator = null;

	protected String messageKey;

	/**
     * TODO: remove the variable, use the Exception get/setMessage() instead.
     */
	protected String formatString;

	/**
	 * Unique code of error.
	 */
	protected long code;
	private Map values;

    /**
     * Empty constructor, don't use it anymore.
     *
     * @deprecated
     */
    public BaseException() {
    }

    /**
     * Constructs a BaseException with a default message format, a key to find a
     * localized message format, and a unique code to identify the exception.
     *
     * @param message
     *            the default messageFormat to describe the exception
     * @param key
     *            the key to use to search a localized messageFormnata
     * @param code
     *            the unique code to identify the exception
     */
    public BaseException(String message, String key, long code) {
        super(message);
        this.values = null;
        this.formatString = message;
        this.messageKey = key;
        this.code = code;
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
    public BaseException(String message, Throwable cause, String key, long code) {
        super(message, cause);
        this.formatString = message;
        this.messageKey = key;
        this.code = code;
    }

	/**
     * Returns the format string received in the parameter with its keys
     * replaced with the corresponding values of the map.
     *
     * @param formatString
     * @param values
     *            map
     * @return string formatted
     */
    private String format(String formatString, Map values) {
        if (values != null) {

            // If there is no message format, create a text with the values.
            if (formatString == null) {
                return "values = ".concat(values.toString());
            } else {
                // Replace the keys as variables with the values in the Map
                Iterator keys = values.keySet().iterator();
                String message = formatString;
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    message = replaceVariable(message, key, (String) values
                            .get(key));
                }
                return message;
            }
        }
        // Return the original format message in any other case
        return formatString;
    }

    /**
     * Method to replace message variables, not using the new String.replaceAll
     * method for Java ME CDC Compatibility.
     */
    private String replaceVariable(String text, String varName,
            String replacementString) {
        String searchString = "%(".concat(varName).concat(")");
        StringBuffer sBuffer = new StringBuffer();
        int pos = 0;
        while ((pos = text.indexOf(searchString)) != -1) {
            sBuffer.append(text.substring(0, pos) + replacementString);
            text = text.substring(pos + searchString.length());
        }
        sBuffer.append(text);
        return sBuffer.toString();
    }

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		return format(this.formatString, values());
	}

	public String getMessage(int indent) {
		return insertBlanksAtStart(format(formatString, values()),indent);
	}

	public String getLocalizedMessage() {
		return getLocalizedMessage(translator,0);
	}

	public String getLocalizedMessage(ExceptionTranslator translator, int indent) {

		String fmt;
		if (translator == null){
			translator = BaseException.translator;
		}
		if (translator == null){
			fmt = getFormatString();
		} else {
			fmt = getMessageKey();
			if (fmt == null){
				fmt = getFormatString();
			} else {
				fmt = translator.getText(fmt);
			}
		}
		return insertBlanksAtStart(format(fmt,values()),indent);
	}

	public String getMessageStack() {
		return getMessageStack(0);
	}

	public String getMessageStack(int indent) {
		Iterator iter = this.iterator();
		StringBuffer msgBuffer = new StringBuffer();
		int i = 1;
		while (iter.hasNext()){
		    Exception ex = ((Exception) iter.next());

            if (msgBuffer.length() > 0) {
                msgBuffer.append("\n");
            }

			if ( ex instanceof BaseException ) {
				BaseException bex = (BaseException) ex;
				msgBuffer.append(bex.getMessage(indent * i));
			} else {
			    msgBuffer.append(insertBlanksAtStart(ex.getMessage(), indent
                        * i));
			}

			i++;
		}
		return msgBuffer.toString();
	}


	public String getLocalizedMessageStack() {
		return getLocalizedMessageStack(BaseException.translator,0);
	}

	public String getLocalizedMessageStack(ExceptionTranslator translator,
			int indent) {
		Iterator iter = this.iterator();
        StringBuffer msgBuffer = new StringBuffer();
        Exception ex;
        while (iter.hasNext()) {
            ex = ((Exception) iter.next());
            if (msgBuffer.length() > 0) {
                msgBuffer.append("\n");
            }

            if (ex instanceof BaseException) {
                BaseException bex = (BaseException) ex;
                msgBuffer.append(bex.getLocalizedMessage(translator, indent));
            } else {
                msgBuffer.append(ex.getLocalizedMessage());
            }
        }
        return msgBuffer.toString();
	}

	/**
	 * Inserts blanks at the start of a string.
	 *
	 * @param str A string.
	 * @param len Quantity of blanks to insert at the start of str.
	 * @return A string compund by the quantity of blanks that
	 *         len indicates and str.
	 */
	static String insertBlanksAtStart(String str, int len){
        int blanksLen = len > BLANKS.length() ? BLANKS.length() : (len < 0 ? 0
                : len);

        return BLANKS.substring(0, blanksLen) + str;
	}

	public long getCode() {
		return this.code;
	}

	/**
	 * Sets the exception's code.
	 */
	public void setCode(long code) {
		this.code = code;
	}

	public String getFormatString() {
		return this.formatString;
	}

	/**
	 * Sets the format string.
	 *
	 * @param formatString
	 */
	public void setFormatString(String formatString) {
		this.formatString = formatString;
	}

	public String getMessageKey() {
		return this.messageKey;
	}

	/**
	 * Sets the property messageKey.
	 *
	 * @param messageKey
	 */
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public Iterator iterator() {
		return new BaseExceptionIterator(this);
	}

	/**
	 * @return A map that serves to replace in the format string
	 * the keys with the corresponding values.
	 */
	protected Map values() {
		if( values == null ) {
			values = new HashMap();
		}
		return values;
	}
	
	protected void setValue(String name, Object value) {
		if( values == null ) {
			values = new HashMap();
		}
		values.put(name, value);
	}

	/**
	 * Sets the property translator.
	 * @param translator It(He,She) is used to translate
	 *        the messages associated with the exceptions.
	 */
	public static void setTranslator(ExceptionTranslator translator) {
		BaseException.translator = translator;
	}

	public static void setTranslator(Object translator){
		BaseException.translator = new TranslatorWraper(translator);
	}

	public String toString(){
		return format(this.formatString, values());
	}

}

class TranslatorWraper implements ExceptionTranslator {

	private Object translator = null;
	private Method method = null;

	public TranslatorWraper(Object translator) {
		Class theClass = translator.getClass();
		String s = "";

		this.translator = translator;
		try {
			method = theClass.getMethod("getText",new Class[] { s.getClass() });
		} catch (Exception e) {
			throw new RuntimeException("El objeto translator suministrado no tiene el metodo getText apropiado.", e);
		}

	}

	public String getText(String key) {
		try {
			return (String)(method.invoke(translator,new String[] { key }));
		} catch (Exception e) {
			return key;
		}
	}

}
