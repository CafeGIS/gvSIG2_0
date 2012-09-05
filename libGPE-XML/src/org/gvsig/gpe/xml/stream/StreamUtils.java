package org.gvsig.gpe.xml.stream;

import java.io.IOException;
import java.io.InputStream;

class StreamUtils {

    private StreamUtils() {
        // do nothing
    }

    /**
     * @param _is
     * @return Returns the encoding or UTF-8 encoding by default.
     * @throws IOException
     */
    public static String getEncoding(final InputStream _is) throws IOException {
        String encoding = "UTF-8";
        int srcCount = 0;
        char[] srcBuf = new char[128];

        // read four bytes
        int chk = 0;
        try {
            while (srcCount < 4) {
                int i = _is.read();
                if (i == -1)
                    break;
                chk = (chk << 8) | i;
                srcBuf[srcCount++] = (char) i;
            }

            if (srcCount == 4) {
                switch (chk) {
                case 0x00000FEFF:
                    encoding = "UTF-32BE";
                    srcCount = 0;
                    break;

                case 0x0FFFE0000:
                    encoding = "UTF-32LE";
                    srcCount = 0;
                    break;

                case 0x03c:
                    encoding = "UTF-32BE";
                    srcBuf[0] = '<';
                    srcCount = 1;
                    break;

                case 0x03c000000:
                    encoding = "UTF-32LE";
                    srcBuf[0] = '<';
                    srcCount = 1;
                    break;

                case 0x0003c003f:
                    encoding = "UTF-16BE";
                    srcBuf[0] = '<';
                    srcBuf[1] = '?';
                    srcCount = 2;
                    break;

                case 0x03c003f00:
                    encoding = "UTF-16LE";
                    srcBuf[0] = '<';
                    srcBuf[1] = '?';
                    srcCount = 2;
                    break;

                case 0x03c3f786d:
                    while (true) {
                        int i = _is.read();
                        if (i == -1)
                            break;
                        srcBuf[srcCount++] = (char) i;
                        if (i == '>') {
                            String s = new String(srcBuf, 0, srcCount);
                            int i0 = s.indexOf("encoding");
                            if (i0 != -1) {
                                while (s.charAt(i0) != '"' && s.charAt(i0) != '\'')
                                    i0++;
                                char deli = s.charAt(i0++);
                                int i1 = s.indexOf(deli, i0);
                                encoding = s.substring(i0, i1);
                            }
                            break;
                        }
                    }

                default:
                    if ((chk & 0x0ffff0000) == 0x0FEFF0000) {
                        encoding = "UTF-16BE";
                        srcBuf[0] = (char) ((srcBuf[2] << 8) | srcBuf[3]);
                        srcCount = 1;
                    } else if ((chk & 0x0ffff0000) == 0x0fffe0000) {
                        encoding = "UTF-16LE";
                        srcBuf[0] = (char) ((srcBuf[3] << 8) | srcBuf[2]);
                        srcCount = 1;
                    } else if ((chk & 0x0ffffff00) == 0x0EFBBBF00) {
                        encoding = "UTF-8";
                        srcBuf[0] = srcBuf[3];
                        srcCount = 1;
                    }
                }
            }
        } catch (IOException ex) {
            return null;
        }
        return encoding;
    }

}
