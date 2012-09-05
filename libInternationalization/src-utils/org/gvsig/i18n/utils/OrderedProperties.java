/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 * 
 * This class is based on GNU Classpath's Properties.java
 * Copyright (C) 1998, 1999, 2000, 2001, 2002, 2003, 2004, 2005  Free Software Foundation, Inc.
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */


package org.gvsig.i18n.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PropertyResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

/**
* A set of persistent properties, which can be saved or loaded from a stream.
* A property list may also contain defaults, searched if the main list
* does not contain a property for a given key.
*
* An example of a properties file for the german language is given
* here.  This extends the example given in ListResourceBundle.
* Create a file MyResource_de.properties with the following contents
* and put it in the CLASSPATH.  (The character
* <code>\</code><code>u00e4</code> is the german umlaut)
*
* 
<pre>s1=3
s2=MeineDisk
s3=3. M\<code></code>u00e4rz 96
s4=Die Diskette ''{1}'' enth\<code></code>u00e4lt {0} in {2}.
s5=0
s6=keine Dateien
s7=1
s8=eine Datei
s9=2
s10={0,number} Dateien
s11=Das Formatieren schlug fehl mit folgender Exception: {0}
s12=FEHLER
s13=Ergebnis
s14=Dialog
s15=Auswahlkriterium
s16=1,3</pre>
*
* <p>Although this is a sub class of a hash table, you should never
* insert anything other than strings to this property, or several
* methods, that need string keys and values, will fail.  To ensure
* this, you should use the <code>get/setProperty</code> method instead
* of <code>get/put</code>.
*
* Properties are saved in the specified encoding. If no encoding is
* specified, then the ISO 8859-1 encoding is used, with Unicode escapes with
* a single <code>u</code> for any character which cannot be represented.
*
* @author Jochen Hoenicke
* @author Eric Blake (ebb9@email.byu.edu)
* @author Cesar Martinez Izquierdo (cesar.martinez@iver.es)
* @see PropertyResourceBundle
* @status updated to 1.4
*/
public class OrderedProperties extends TreeMap
{
	
/**
* The property list that contains default values for any keys not
* in this property list.
*
* @serial the default properties
*/
protected OrderedProperties defaults;

/**
* Compatible with JDK 1.0+.
*/
private static final long serialVersionUID = -5087876565919950510L;

/**
* Creates a new empty property list with no default values.
*/
public OrderedProperties()
{
	super(new StringComparator());
}

/**
* Create a new empty property list with the specified default values.
*
* @param defaults a Properties object containing the default values
*/
public OrderedProperties(OrderedProperties defaults)
{
	super(new StringComparator());
	this.defaults = defaults;
}

/**
* Adds the given key/value pair to this properties.  This calls
* the hashtable method put.
*
* @param key the key for this property
* @param value the value for this property
* @return The old value for the given key
* @see #getProperty(String)
* @since 1.2
*/
public Object setProperty(String key, String value)
{
 return put(key, value);
}

/**
* Reads a property list from an input stream.  The stream should
* have the following format: <br>
*
* An empty line or a line starting with <code>#</code> or
* <code>!</code> is ignored.  An backslash (<code>\</code>) at the
* end of the line makes the line continueing on the next line
* (but make sure there is no whitespace after the backslash).
* Otherwise, each line describes a key/value pair. <br>
*
* The chars up to the first whitespace, = or : are the key.  You
* can include these caracters in the key, if you precede them with
* a backslash (<code>\</code>). The key is followed by optional
* whitespaces, optionally one <code>=</code> or <code>:</code>,
* and optionally some more whitespaces.  The rest of the line is
* the resource belonging to the key. <br>
*
* Escape sequences <code>\t, \n, \r, \\, \", \', \!, \#, \ </code>(a
* space), and unicode characters with the
* <code>\\u</code><em>xxxx</em> notation are detected, and
* converted to the corresponding single character. <br>
*
* 
<pre># This is a comment
key     = value
k\:5      \ a string starting with space and ending with newline\n
# This is a multiline specification; note that the value contains
# no white space.
weekdays: Sunday,Monday,Tuesday,Wednesday,\\
       Thursday,Friday,Saturday
# The safest way to include a space at the end of a value:
label   = Name:\\u0020</pre>
*
* @param inStream the input stream
* @throws IOException if an error occurred when reading the input
* @throws NullPointerException if in is null
*/
public void load(InputStream inStream) throws IOException
{
 // The spec says that the file must be encoded using ISO-8859-1.
 BufferedReader reader =
   new BufferedReader(new InputStreamReader(inStream, "ISO-8859-1"));
 String line;

 while ((line = reader.readLine()) != null)
   {
     char c = 0;
     int pos = 0;
	// Leading whitespaces must be deleted first.
     while (pos < line.length()
            && Character.isWhitespace(c = line.charAt(pos)))
       pos++;

     // If empty line or begins with a comment character, skip this line.
     if ((line.length() - pos) == 0
	    || line.charAt(pos) == '#' || line.charAt(pos) == '!')
       continue;

     // The characters up to the next Whitespace, ':', or '='
     // describe the key.  But look for escape sequences.
	// Try to short-circuit when there is no escape char.
	int start = pos;
	boolean needsEscape = line.indexOf('\\', pos) != -1;
     StringBuffer key = needsEscape ? new StringBuffer() : null;
     while (pos < line.length()
            && ! Character.isWhitespace(c = line.charAt(pos++))
            && c != '=' && c != ':')
       {
         if (needsEscape && c == '\\')
           {
             if (pos == line.length())
               {
                 // The line continues on the next line.  If there
                 // is no next line, just treat it as a key with an
                 // empty value.
                 line = reader.readLine();
		    if (line == null)
		      line = "";
                 pos = 0;
                 while (pos < line.length()
                        && Character.isWhitespace(c = line.charAt(pos)))
                   pos++;
               }
             else
               {
                 c = line.charAt(pos++);
                 switch (c)
                   {
                   case 'n':
                     key.append('\n');
                     break;
                   case 't':
                     key.append('\t');
                     break;
                   case 'r':
                     key.append('\r');
                     break;
                   case 'u':
                     if (pos + 4 <= line.length())
                       {
                         char uni = (char) Integer.parseInt
                           (line.substring(pos, pos + 4), 16);
                         key.append(uni);
                         pos += 4;
                       }        // else throw exception?
                     break;
                   default:
                     key.append(c);
                     break;
                   }
               }
           }
         else if (needsEscape)
           key.append(c);
       }

     boolean isDelim = (c == ':' || c == '=');

	String keyString;
	if (needsEscape)
	  keyString = key.toString();
	else if (isDelim || Character.isWhitespace(c))
	  keyString = line.substring(start, pos - 1);
	else
	  keyString = line.substring(start, pos);

     while (pos < line.length()
            && Character.isWhitespace(c = line.charAt(pos)))
       pos++;

     if (! isDelim && (c == ':' || c == '='))
       {
         pos++;
         while (pos < line.length()
                && Character.isWhitespace(c = line.charAt(pos)))
           pos++;
       }

	// Short-circuit if no escape chars found.
	if (!needsEscape)
	  {
	    put(keyString, line.substring(pos));
	    continue;
	  }

	// Escape char found so iterate through the rest of the line.
     StringBuffer element = new StringBuffer(line.length() - pos);
     while (pos < line.length())
       {
         c = line.charAt(pos++);
         if (c == '\\')
           {
             if (pos == line.length())
               {
                 // The line continues on the next line.
                 line = reader.readLine();

		    // We might have seen a backslash at the end of
		    // the file.  The JDK ignores the backslash in
		    // this case, so we follow for compatibility.
		    if (line == null)
		      break;

                 pos = 0;
                 while (pos < line.length()
                        && Character.isWhitespace(c = line.charAt(pos)))
                   pos++;
                 element.ensureCapacity(line.length() - pos +
                                        element.length());
               }
             else
               {
                 c = line.charAt(pos++);
                 switch (c)
                   {
                   case 'n':
                     element.append('\n');
                     break;
                   case 't':
                     element.append('\t');
                     break;
                   case 'r':
                     element.append('\r');
                     break;
                   case 'u':
                     if (pos + 4 <= line.length())
                       {
                         char uni = (char) Integer.parseInt
                           (line.substring(pos, pos + 4), 16);
                         element.append(uni);
                         pos += 4;
                       }        // else throw exception?
                     break;
                   default:
                     element.append(c);
                     break;
                   }
               }
           }
         else
           element.append(c);
       }
     put(keyString, element.toString());
   }
}

/**
* Reads a property list from an input stream, using the
* provided encoding. The provided stream should be
* correctly encoded as specified, otherwise an IOException
* error will be thrown. No escape sequences are accepted
* to represent any character, and thus this method has some
* limitations and the
* format of the accepted property files is slightly
* different from
* the standard Java property files.
* 
* The main differences are:
* <ul><li>whitespaces, = or : cannot be present in
* the key.</li>
* <li>each pair key/value must be contained in a single line</li>
* </ul>
* 
* The stream should have the following format: <br>
*
* An empty line or a line starting with <code>#</code> or
* <code>!</code> is ignored.
* Otherwise, each line describes a key/value pair. <br>
*
* The chars up to the first whitespace, = or : are the key.  You
* cannot include these caracters in the key. The key is followed by optional
* whitespaces, optionally one <code>=</code> or <code>:</code>,
* and optionally some more whitespaces.  The rest of the line is
* the resource belonging to the key. <br>
*
* @param inStream the input stream
* @throws IOException if an error occurred when reading the input
* @throws NullPointerException if in is null
*/
public void load(InputStream inStream, String encoding) throws IOException {
	BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, encoding));

	String line;
	while ((line = reader.readLine()) != null) {
		int pos = 0;
		// Leading whitespaces must be deleted first.
		while (pos < line.length()
			 && Character.isWhitespace(line.charAt(pos)))
			pos++;

     	// If empty line or begins with a comment character, skip this line.
     	if ((line.length() - pos) == 0
     			|| line.charAt(pos) == '#' || line.charAt(pos) == '!')
     		continue;


     	// do something
     	String[] entry = line.substring(pos).split(" *[=: ] *", 2);
     	if (entry.length==2) {
     		put(entry[0], entry[1]);
     	}
     	else {
     		put(entry[0], "");
     	}
	}
}


/**
* Writes the key/value pairs to the given output stream, in a format
* suitable for
* {@link #load(InputStream, String) load(InputStream is, String encoding)}.
* Note that this method does not use escape sequences to represent
* characters outside the encoding range, charset-dependent
* substitution sequence will
* be generated if such character is present in the stream.
* Moreover, because there is no escape sequences, the newline (\n)
* and carriage return (\r) characters must not be used (the 
* resulting property file will be incorrect). Because of the
* same reason, whitespaces, :, =, ! and # must not be used in the
* key<br>
*
* If header is not null, this method writes a comment containing
* the header as first line to the stream.  The next line (or first
* line if header is null) contains a comment with the current date.
* Afterwards the key/value pairs are written to the stream in the
* following format.<br>
*
* Each line has the form <code>key = value</code>.
*
* Following the listing, the output stream is flushed but left open.
*
* @param out the output stream
* @param header the header written in the first line, may be null
* @throws ClassCastException if this property contains any key or
*         value that isn't a string
* @throws IOException if writing to the stream fails
* @throws NullPointerException if out is null
* @since 1.2
*/
public void store(OutputStream out, String header, String encoding) throws IOException {
	PrintWriter writer
	   = new PrintWriter(new OutputStreamWriter(out, encoding));
	if (header != null)
		writer.println("#" + header);
	writer.println ("#" + Calendar.getInstance ().getTime ());

	Iterator iter = entrySet ().iterator ();
	int i = size ();
	while (--i >= 0) {
		Entry entry = (Entry) iter.next ();
	    writer.println (entry.getKey()+"="+entry.getValue());
	}

	writer.flush ();
}



/**
* Writes the key/value pairs to the given output stream, in a format
* suitable for {@link #load(InputStream) load(InputStream)}.<br>
*
* If header is not null, this method writes a comment containing
* the header as first line to the stream.  The next line (or first
* line if header is null) contains a comment with the current date.
* Afterwards the key/value pairs are written to the stream in the
* following format.<br>
*
* Each line has the form <code>key = value</code>.  Newlines,
* Returns and tabs are written as <code>\n,\t,\r</code> resp.
* The characters <code>\, !, #, =</code> and <code>:</code> are
* preceeded by a backslash.  Spaces are preceded with a backslash,
* if and only if they are at the beginning of the key.  Characters
* that are not in the ascii range 33 to 127 are written in the
* <code>\</code><code>u</code>xxxx Form.<br>
*
* Following the listing, the output stream is flushed but left open.
*
* @param out the output stream
* @param header the header written in the first line, may be null
* @throws ClassCastException if this property contains any key or
*         value that isn't a string
* @throws IOException if writing to the stream fails
* @throws NullPointerException if out is null
* @since 1.2
*/
public void store(OutputStream out, String header) throws IOException
{
 // The spec says that the file must be encoded using ISO-8859-1.
 PrintWriter writer
   = new PrintWriter(new OutputStreamWriter(out, "ISO-8859-1"));
 if (header != null)
   writer.println("#" + header);
 writer.println ("#" + Calendar.getInstance ().getTime ());
 
 Iterator iter = entrySet ().iterator ();
 int i = size ();
 StringBuffer s = new StringBuffer (); // Reuse the same buffer.
 while (--i >= 0)
   {
     Entry entry = (Entry) iter.next ();
     formatForOutput ((String) entry.getKey (), s, true);
     s.append ('=');
     formatForOutput ((String) entry.getValue (), s, false);
     writer.println (s);
   }

 writer.flush ();
}

/**
* Gets the property with the specified key in this property list.
* If the key is not found, the default property list is searched.
* If the property is not found in the default, null is returned.
*
* @param key The key for this property
* @return the value for the given key, or null if not found
* @throws ClassCastException if this property contains any key or
*         value that isn't a string
* @see #defaults
* @see #setProperty(String, String)
* @see #getProperty(String, String)
*/
public String getProperty(String key)
{
 OrderedProperties prop = this;
 // Eliminate tail recursion.
 do
   {
     String value = (String) prop.get(key);
     if (value != null)
       return value;
     prop = prop.defaults;
   }
 while (prop != null);
 return null;
}

/**
* Gets the property with the specified key in this property list.  If
* the key is not found, the default property list is searched.  If the
* property is not found in the default, the specified defaultValue is
* returned.
*
* @param key The key for this property
* @param defaultValue A default value
* @return The value for the given key
* @throws ClassCastException if this property contains any key or
*         value that isn't a string
* @see #defaults
* @see #setProperty(String, String)
*/
public String getProperty(String key, String defaultValue)
{
 String prop = getProperty(key);
 if (prop == null)
   prop = defaultValue;
 return prop;
}

/**
* Returns an enumeration of all keys in this property list, including
* the keys in the default property list.
*
* @return an Enumeration of all defined keys
*/
public Enumeration propertyNames()
{
 // We make a new Set that holds all the keys, then return an enumeration
 // for that. This prevents modifications from ruining the enumeration,
 // as well as ignoring duplicates.
 OrderedProperties prop = this;
 Set s = new HashSet();
 // Eliminate tail recursion.
 do
   {
     s.addAll(prop.keySet());
     prop = prop.defaults;
   }
 while (prop != null);
 return Collections.enumeration(s);
}

/**
* Prints the key/value pairs to the given print stream.  This is 
* mainly useful for debugging purposes.
*
* @param out the print stream, where the key/value pairs are written to
* @throws ClassCastException if this property contains a key or a
*         value that isn't a string
* @see #list(PrintWriter)
*/
public void list(PrintStream out)
{
 PrintWriter writer = new PrintWriter (out);
 list (writer);
}

/**
* Prints the key/value pairs to the given print writer.  This is
* mainly useful for debugging purposes.
*
* @param out the print writer where the key/value pairs are written to
* @throws ClassCastException if this property contains a key or a
*         value that isn't a string
* @see #list(PrintStream)
* @since 1.1
*/
public void list(PrintWriter out)
{
 out.println ("-- listing properties --");

 Iterator iter = entrySet ().iterator ();
 int i = size ();
 while (--i >= 0)
   {
     Entry entry = (Entry) iter.next ();
     out.print ((String) entry.getKey () + "=");

     // JDK 1.3/1.4 restrict the printed value, but not the key,
     // to 40 characters, including the truncating ellipsis.
     String s = (String ) entry.getValue ();
     if (s != null && s.length () > 40)
       out.println (s.substring (0, 37) + "...");
     else
       out.println (s);
   }
 out.flush ();
}

/**
* Formats a key or value for output in a properties file.
* See store for a description of the format.
*
* @param str the string to format
* @param buffer the buffer to add it to
* @param key true if all ' ' must be escaped for the key, false if only
*        leading spaces must be escaped for the value
* @see #store(OutputStream, String)
*/
private void formatForOutput(String str, StringBuffer buffer, boolean key)
{
 if (key)
   {
     buffer.setLength(0);
     buffer.ensureCapacity(str.length());
   }
 else
   buffer.ensureCapacity(buffer.length() + str.length());
 boolean head = true;
 int size = str.length();
 for (int i = 0; i < size; i++)
   {
     char c = str.charAt(i);
     switch (c)
       {
       case '\n':
         buffer.append("\\n");
         break;
       case '\r':
         buffer.append("\\r");
         break;
       case '\t':
         buffer.append("\\t");
         break;
       case ' ':
         buffer.append(head ? "\\ " : " ");
         break;
       case '\\':
       case '!':
       case '#':
       case '=':
       case ':':
         buffer.append('\\').append(c);
         break;
       default:
         if (c < ' ' || c > '~')
           {
             String hex = Integer.toHexString(c);
             buffer.append("\\u0000".substring(0, 6 - hex.length()));
             buffer.append(hex);
           }
         else
           buffer.append(c);
       }
     if (c != ' ')
       head = key;
   }
}

} // class OrderedProperties


class StringComparator implements Comparator {
	 public int compare(Object o1, Object o2) {
		 String s1 = o1.toString();
		 String s2 = o2.toString();

		 if (s1.compareToIgnoreCase(s2)!=0) // we want case insensitive ordenation, but we still need to differenciate 'OK' from 'Ok'
			 return s1.compareToIgnoreCase(s2);
		 else
			 return -s1.compareTo(s2); // we want lower case before upper case
	 }
}