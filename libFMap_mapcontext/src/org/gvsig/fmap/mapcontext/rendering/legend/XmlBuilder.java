package org.gvsig.fmap.mapcontext.rendering.legend;

import java.util.HashMap;
import java.util.Stack;

/**
 * Title:         XmlBuilder
 * Description:   Util class to build xml
 * @author laura
 */
public class XmlBuilder
{
  // Default size of the XML document to be generated. Used to set the initial
  // capacity of m_xml.
  private static final int DEF_XMLDOC_SIZE = 256;

  // Pad unit.
  private static final String PAD = "  ";

  
  // Number of pre-defined pad strings, one for each pad
  // level (0..NUM_PADSTRINGS-1). Should at least be set to 10 for performance.
  private static final int NUM_PADSTRINGS = 20;

  // Byte representation of '\n'.
  private static final byte[] NEWLINE_BYTES = "\n".getBytes();


  // Array of pad strings, each preceded by a '\n' character.
  private static String s_padStrings[];

  // XML-encoded newline character.
  private static String s_newlineXmlEnc;


  // XML document being built.
  private StringBuffer m_xml = new StringBuffer(DEF_XMLDOC_SIZE);

  // Scratch buffer exclusively used to encode strings.
  private StringBuffer m_encodeBuf = new StringBuffer(40);

  // Current pad string.
  private String m_pad = "";

  // If set, indentation will be added to the XML document.
  private boolean m_autoPad;

  // Stack of strings. The string at the top is the tag name of the current
  // XML block. The string immediately below the top is the tag name of the
  // XML block that immediately encloses the current XML block.
  protected Stack m_openElements = new Stack();

  // Current pad level (0, 1, 2, ...).
  private int m_padLevel = 0;

  private String encoding = "UTF-8";

  /*
   * Create the pad strings.
   */
  static
  {
    s_padStrings = new String[NUM_PADSTRINGS];
    String pad = "";

    for (int i = 0; i < NUM_PADSTRINGS; i++)
    {
      s_padStrings[i] = "\n" + pad;
      pad += PAD;
    }

    StringBuffer tmp = new StringBuffer();

    for (int i = 0; i < NEWLINE_BYTES.length; i++)
    {
      tmp.append("&#");
      tmp.append(NEWLINE_BYTES[i]);
      tmp.append(";");
    }

    s_newlineXmlEnc = tmp.toString();
  }


  /**
  * Constructor. The XML document to be generated will automatically be
  * indented.
  */
  public XmlBuilder()
  {
    this(true);
  }

  /**
  * Constructor.
  *
  * @param  autoPad  if set, the XML document to be generated will
  *                  automatically be indented.
  */
  public XmlBuilder(boolean autoPad)
  {
    m_autoPad = autoPad;
    m_padLevel = 0;
    m_pad = s_padStrings[m_padLevel];
  }

  /**
   * Reset this XmlBuilder.
   */
  public void reset()
  {
    m_padLevel = 0;
    m_pad = s_padStrings[m_padLevel];
    m_xml.setLength(0);
  }

  /**
  * Adds raw data to the xml.
  */
  public void writeRaw(String raw)
  {
    m_xml.append(raw);
  }

  /**
  * Adds a comment to the xml.
  */
  public void writeComment(String comment)
  {
    if (m_autoPad)
    {
      m_xml.append(m_pad);
    }

    m_xml.append("<!-- ");
    encode(comment, m_xml);
    m_xml.append(" -->\n");
  }

  /**
   * Writes the XML document header.
   */
  public void writeHeader()
  {
	// XML document header.
	final String HEADER = "<?xml version=\"1.0\" encoding=\""+encoding+"\"?>\n";

    m_xml.append(HEADER);
  }
  
  /**
   * Sets the encoding used in the XML. By default UTF-8 is used.
   * @param encoding
   */
  public void setEncoding(String encoding) {
	  this.encoding  = encoding;
  }
  
  /**
  * Adds a opening and closing tag with charcter data.
  */
  public void writeTag(String name, String data)
  {
    name = encode(name);

    if (m_autoPad)
    {
      m_xml.append(m_pad);
    }

    m_xml.append("<");
    m_xml.append(name);
    m_xml.append(">");
    encode(data, m_xml);
    m_xml.append("</");
    m_xml.append(name);
    m_xml.append(">");
  }

  /**
  * Adds a opening and closing tag with attributes.
  */
  public void writeTag(String name, HashMap attributes)
  {
    if (m_autoPad)
    {
      m_xml.append(m_pad);
    }

    m_xml.append("<");
    encode(name, m_xml);
    m_xml.append(" ");

    Object elems[] = attributes.keySet().toArray();
    
    for (int i = 0; i < elems.length; i++)
    {
      String nm = (String)elems[i];
      String val = (String)attributes.get(nm);

      encode(nm, m_xml);
      m_xml.append("=\"");
      encode(val, m_xml);
      m_xml.append("\" ");
    }

    m_xml.append("/>");
  }

  /**
  * Adds a opening and closing tag with an attribute and character data.
  */
  public void writeTag(String name, String attr1Name, String attr1Value
  	, String attr2Name, String attr2Value)
  {
    if (m_autoPad)
    {
      m_xml.append(m_pad);
    }

    m_xml.append("<");
    encode(name, m_xml);
    m_xml.append(" ");

    encode(attr1Name, m_xml);
    m_xml.append("=\"");
    encode(attr1Value, m_xml);
    m_xml.append("\" ");

    encode(attr2Name, m_xml);
    m_xml.append("=\"");
    encode(attr2Value, m_xml);
    m_xml.append("\" ");

    m_xml.append("/>");
  }

  /**
  * Adds a opening and closing tag with an attribute and character data.
  */
  public void writeTag(String name, String data, String attrName, String attrValue)
  {
    name = encode(name);

    if (m_autoPad)
    {
      m_xml.append(m_pad);
    }

    m_xml.append("<");
    m_xml.append(name);
    m_xml.append(" ");

    encode(attrName, m_xml);
    m_xml.append("=\"");
    encode(attrValue, m_xml);
    m_xml.append("\" ");

    m_xml.append(">");
    encode(data, m_xml);
    m_xml.append("</");
    m_xml.append(name);
    m_xml.append(">");
  }

  /**
  * Adds a opening and closing tag with two attributes and character data.
  */
  public void writeTag(String name, String data, String attr1Name, String attr1Value
                      , String attr2Name, String attr2Value)
  {
    name = encode(name);

    if (m_autoPad)
    {
      m_xml.append(m_pad);
    }

    m_xml.append("<");
    m_xml.append(name);
    m_xml.append(" ");

    encode(attr1Name, m_xml);
    m_xml.append("=\"");
    encode(attr1Value, m_xml);
    m_xml.append("\" ");

    encode(attr2Name, m_xml);
    m_xml.append("=\"");
    encode(attr2Value, m_xml);
    m_xml.append("\" ");

    m_xml.append(">");
    encode(data, m_xml);
    m_xml.append("</");
    m_xml.append(name);
    m_xml.append(">");
  }

  /**
  * Adds a opening and closing tag with attributes and character data.
  */
  public void writeTag(String name, String data, HashMap attributes)
  {
    name = encode(name);

    if (m_autoPad)
    {
      m_xml.append(m_pad);
    }

    m_xml.append("<");
    m_xml.append(name);
    m_xml.append(" ");

    Object elems[] = attributes.keySet().toArray();
    
    for (int i = 0; i < elems.length; i++)
    {
      String nm = (String)elems[i];
      String val = (String)attributes.get(nm);

      encode(nm, m_xml);
      m_xml.append("=\"");
      encode(val, m_xml);
      m_xml.append("\" ");
    }

    m_xml.append(">");
    encode(data, m_xml);
    m_xml.append("</");
    m_xml.append(name);
    m_xml.append(">");
  }

  /**
  * Writes an opening tag
  */
  public void openTag(String name)
  {
    name = encode(name);

    m_openElements.push(name);              // must be encoded!

    if (m_autoPad)
    {
      m_xml.append(m_pad);
    }

    m_xml.append("<");
    m_xml.append(name);
    m_xml.append(">");

    if (m_autoPad)
    {
      m_padLevel++;

      if (m_padLevel < NUM_PADSTRINGS)
      {
        m_pad = s_padStrings[m_padLevel];
      }
      else                                  // really deep nesting level
      {
        m_pad += PAD;
      }
    }
  }

  /**
  * Writes an opening tag with one attribute.
  */
  public void openTag(String name, String attrName, String attrValue)
  {
    name = encode(name);

    m_openElements.push(name);              // must be encoded!

    if (m_autoPad)
    {
      m_xml.append(m_pad);
    }

    m_xml.append("<");
    m_xml.append(name);
    m_xml.append(" ");

    encode(attrName, m_xml);
    m_xml.append("=\"");
    encode(attrValue, m_xml);
    m_xml.append("\" ");

    m_xml.append(">");

    if (m_autoPad)
    {
      m_padLevel++;

      if (m_padLevel < NUM_PADSTRINGS)
      {
        m_pad = s_padStrings[m_padLevel];
      }
      else                                  // really deep nesting level
      {
        m_pad += PAD;
      }
    }
  }

  /**
  * Writes an opening tag with two attributes.
  */
  public void openTag(String name, String attr1Name, String attr1Value
                      , String attr2Name, String attr2Value)
  {
    name = encode(name);

    m_openElements.push(name);              // must be encoded!

    if (m_autoPad)
    {
      m_xml.append(m_pad);
    }

    m_xml.append("<");
    m_xml.append(name);
    m_xml.append(" ");

    encode(attr1Name, m_xml);
    m_xml.append("=\"");
    encode(attr1Value, m_xml);
    m_xml.append("\" ");

    encode(attr2Name, m_xml);
    m_xml.append("=\"");
    encode(attr2Value, m_xml);
    m_xml.append("\" ");

    m_xml.append(">");

    if (m_autoPad)
    {
      m_padLevel++;

      if (m_padLevel < NUM_PADSTRINGS)
      {
        m_pad = s_padStrings[m_padLevel];
      }
      else                                  // really deep nesting level
      {
        m_pad += PAD;
      }
    }
  }

  /**
  * Writes an opening tag with attributes.
  */
  public void openTag(String name, HashMap attributes)
  {
    name = encode(name);

    m_openElements.push(name);              // must be encoded!

    if (m_autoPad)
    {
      m_xml.append(m_pad);
    }

    m_xml.append("<");
    m_xml.append(name);
    m_xml.append(" ");

    Object elems[] = attributes.keySet().toArray();
    
    for (int i = 0; i < elems.length; i++)
    {
      String nm = (String)elems[i];
      String val = (String)attributes.get(nm);

      encode(nm, m_xml);
      m_xml.append("=\"");
      encode(val, m_xml);
      m_xml.append("\" ");
    }

    m_xml.append(">");

    if (m_autoPad)
    {
      m_padLevel++;

      if (m_padLevel < NUM_PADSTRINGS)
      {
        m_pad = s_padStrings[m_padLevel];
      }
      else                                  // really deep nesting level
      {
        m_pad += PAD;
      }
    }
  }

  /**
  * Closes an open tag.
  */
  public void closeTag()
  {
    if (m_autoPad)
    {
      if (m_padLevel > 0)
      {
        m_padLevel--;
      }

      if (m_padLevel < NUM_PADSTRINGS)
      {
        m_pad = s_padStrings[m_padLevel];
      }
      else                                  // really deep nesting level
      {
        int len = m_pad.length() - PAD.length();
        if (len > 0)
        {
          m_pad = m_pad.substring(0, len);
        }
      }

      m_xml.append(m_pad);
    }

	String name = (String)m_openElements.pop();      // already encoded

    m_xml.append("</");
    m_xml.append(name);
    m_xml.append(">");
  }

  /**
  * Get the xml.
  */
  public String getXML()
  {
    return m_xml.toString();
  }

  /**
   * Encodes the funny characters (e.g., '&', '<', '\"') in an XML
   * string.
   *
   * @param  src  XML string to encode.
   * @return the encoded string.
   */
  private String encode(String src)
  {
    m_encodeBuf.setLength(0);			// clear old contents
    encode(src, m_encodeBuf);
    return m_encodeBuf.toString();
  }

  /**
   * Encodes the funny characters (e.g., '&', '<', '\"') in an XML
   * string.
   *
   * @param  src  XML string to encode.
   * @param  dst  string buffer to write the encoded XML string to.
   */
  private static final void encode(String src, StringBuffer dst)
  {
    int n = src.length();
  	
    for (int i = 0; i < n; i++)
    {
      char c = src.charAt(i);

      switch(c)
      {
      case '&':
        dst.append("&amp;");
        break;
      case '<':
        dst.append("&lt;");
        break;
      case '>':
        dst.append("&gt;");
        break;
      case '\'':
        dst.append("&apos;");
        break;
      case '\"':
        dst.append("&quot;");
        break;
      case '\n':
        dst.append(s_newlineXmlEnc);
        break;
      default:
        dst.append(c); 
      }
    }
  }
}

