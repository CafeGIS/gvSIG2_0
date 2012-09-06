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

import java.util.Iterator;

/**
 *
 *
 * @author Equipo de desarrollo de gvSIG
 *
 */
public interface IBaseException {

	/**
	 *  Returns the message that describes the exception.
	 *
	 *  @return The message.
	 */
	public String getMessage();

	/**
	 *  Returns the message that describes the exception, with indentation.
	 *
	 *  @param indent Quantity of blanks to insert
	 *         at the start of the message.
	 *  @return The message with indentation.
	 */
	public String getMessage(int indent);

	/**
	 *  Returns the translated message that describes the exception.
	 *
	 *  @return The translated message with indentation.
	 */
	public String getLocalizedMessage();

	/**
	 *  Returns the translated message that
	 *  describes the exception with indentation.
	 *
	 *  @param translator Instance of a class that fulfills
	 *         the IExceptionTranslator interface.
	 *         His method "getText" takes charge returning
	 *         the expression, correspondent to the key that
	 *         delivers him, translated into the configured language.
	 *  @param indent Quantity of blanks to insert
	 *         at the start of the message.
	 *  @return The translated message with indentation.
	 */
	public String getLocalizedMessage(ExceptionTranslator translator, int indent);

	/**
	 *  Crosses the exceptions chained through cause to conform
	 *  the message.
	 *
	 *  @return The compound message with all the messages
	 *          of the stack of exceptions.
	 */
	public String getMessageStack();

	/**
	 *  Crosses the exceptions chained through cause to conform
	 *  the compound message with indentation.
	 *
	 *  @param indent Quantity of blanks to insert
	 *         at the start of the messages.
	 *  @return The compound message with all the messages
	 *          of the stack of exceptions.

	 */
	public String getMessageStack(int indent);

	/**
	 *  Crosses the exceptions chained through cause
	 *  to conform the compound message in the corresponding language.
	 *
	 *  @return The translated compound message.
	 *
	 */
	public String getLocalizedMessageStack();

	/**
	 *  Crosses the exceptions chained through cause
	 *  to conform the compound message in the corresponding language.
	 *
	 *  @param translator Instance of a class that fulfills
	 *         the IExceptionTranslator interface.
	 *         His method "getText" takes charge returning
	 *         the expression, correspondent to the key that
	 *         delivers him, translated into the configured language.
	 *  @param indent Quantity of blanks to insert
	 *         at the start of the messages.
	 *  @return The translated message with indentation.
	 *
	 */
	public String getLocalizedMessageStack(ExceptionTranslator translator,
			int indent);


	/**
	 *  @return The exception's code.
	 */
	public long getCode();

	/**
	 *  @return The format string.
	 */
	public String getFormatString();

	/**
	 *  @return The message key associated to the exception.
	 */
	public String getMessageKey();

	/**
	 *  @return A iterator for the chained exceptions.
	 */
	public Iterator iterator();

}