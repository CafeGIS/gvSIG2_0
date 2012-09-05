/*
 * Cresques Mapping Suite. Graphic Library for constructing mapping applications.
 *
 * Copyright (C) 2004-5.
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
 * cresques@gmail.com
 */
package org.cresques.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;


/**
 * Grupo Dxf (code, data). Auxiliar para leer ficheros dxf
 *
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 * @author "Michel Michaud" (code from)
 */
public class DxfGroup {
    /*
    def get_group(handle):
    _code = int(handle.readline())
    _dfun = get_data_type(_code)
    _data = _dfun(handle.readline())
    return (_code, _data)
     */
    private static final DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
    private static final DecimalFormat[] decimalFormats = new DecimalFormat[] {
                                                              new DecimalFormat("#0",
                                                                                dfs),
                                                              new DecimalFormat("#0.0",
                                                                                dfs),
                                                              new DecimalFormat("#0.00",
                                                                                dfs),
                                                              new DecimalFormat("#0.000",
                                                                                dfs),
                                                              new DecimalFormat("#0.0000",
                                                                                dfs),
                                                              new DecimalFormat("#0.00000",
                                                                                dfs),
                                                              new DecimalFormat("#0.000000",
                                                                                dfs),
                                                              new DecimalFormat("#0.0000000",
                                                                                dfs),
                                                              new DecimalFormat("#0.00000000",
                                                                                dfs),
                                                              new DecimalFormat("#0.000000000",
                                                                                dfs),
                                                              new DecimalFormat("#0.0000000000",
                                                                                dfs),
                                                              new DecimalFormat("#0.00000000000",
                                                                                dfs),
                                                              new DecimalFormat("#0.000000000000",
                                                                                dfs)
                                                          };
    int code;
    Object data;
    
    /**
     * Constructor por defecto.
     */
    public DxfGroup() {
        code = -1;
        data = null;
    }
    
    /**
     * Constructor habitual. Representa una entidad individual dentro del DXF
     * @param code, índice del dato dentro del DxfGroup
     * @param data, el propio dato que queda almacenado en el DxfGroup
     */
    public DxfGroup(int code, String data) {
        this.code = code;
        this.data = data;
    }
    
    /**
     * Lee una entidad del DXF y la empaqueta en un DxfGroup.
     * @param fi, BufferedReader mediante el cual accedemos al DXF
     * @return DxfGroup con la información procedente del DXF
     * @throws NumberFormatException
     * @throws IOException
     */
    public static DxfGroup read(BufferedReader fi)
                         throws NumberFormatException, IOException {
        DxfGroup grp = null;
        String txt = fi.readLine();

        if (txt != null) {
            if (!txt.equals("")) {
                grp = new DxfGroup();
                grp.code = Integer.parseInt(txt.trim());
                grp.readData(fi);
            } else {
                // Se trata de una linea en blanco y no se hace nada.
            }
        }

        return grp;
    }
    
    /**
     * Devuelve el code
     * @return
     */
    public int getCode() {
        return code;
    }
    
    /**
     * Devuelve data
     * @return
     */
    public Object getData() {
        return data;
    }
    
    /**
     * Lee un dato individual dentro de un DxfGroup
     * @param fi, BufferedReader
     * @throws IOException
     */
    private void readData(BufferedReader fi) throws IOException {
        String txt = fi.readLine().trim();

        if ((0 <= code) && (code <= 9)) {
            data = txt; //_dfun = string_data
        } else if ((10 <= code) && (code <= 59)) {
            data = new Double(Double.parseDouble(txt)); //_dfun = float_data
        } else if ((60 <= code) && (code <= 79)) {
            try {
                data = new Integer(Integer.parseInt(txt)); //_dfun = int_data // 16-bit int
            } catch (java.lang.NumberFormatException e) {
                data = new Integer((int) Double.parseDouble(txt));
            }
        } else if ((90 <= code) && (code <= 99)) {
            data = new Integer(Integer.parseInt(txt)); //_dfun = int_data // 32-bit int
        } else if (code == 100) {
            data = txt; //_dfun = unicode_data
        } else if (code == 102) {
            // Fran: Comentado para ganar velocidad.
            //System.err.println("Dxf: codigo " + code + " no implementado."); //_dfun = unicode_data
        } else if (code == 105) {
            data = txt;
            ; //_dfun = handle_data
        } else if ((110 <= code) && (code <= 139)) {
            data = new Double(Double.parseDouble(txt)); //_dfun = float_data // not in dxf spec
        } else if ((140 <= code) && (code <= 149)) { // says 147 in dxf spec
            data = new Double(Double.parseDouble(txt)); //_dfun = float_data
        } else if ((170 <= code) && (code <= 179)) { // says 175 in dxf spec
            data = new Integer(Integer.parseInt(txt)); //_dfun = int_data // 16-bit int
        } else if ((210 <= code) && (code <= 239)) {
            data = new Double(Double.parseDouble(txt)); //_dfun = float_data // del TEXT procendente de exportacion de microstation 
        } else if ((270 <= code) && (code <= 279)) {
            data = new Integer(Integer.parseInt(txt)); //_dfun = int_data // not in dxf spec
        } else if ((280 <= code) && (code <= 289)) {
            data = new Integer(Integer.parseInt(txt)); //_dfun = int_data // 8-bit int
        } else if ((290 <= code) && (code <= 299)) {
            data = new Boolean(Boolean.getBoolean(txt)); //_dfun = bool_data
        } else if ((300 <= code) && (code <= 309)) {
            data = txt; //_dfun = string_data
        } else if ((310 <= code) && (code <= 319)) {
            //_dfun = bin_data
            //throw new IOException("Dxf: codigo "+code+" no implementado.");
        } else if ((320 <= code) && (code <= 329)) {
            //_dfun = handle_data
            //throw new IOException("Dxf: codigo "+code+" no implementado.");
        } else if ((330 <= code) && (code <= 369)) {
            // Fran: Comentado para ganar velocidad.
            //System.err.println("Dxf: codigo " + code + " no implementado."); //_dfun = hex_data
        } else if ((370 <= code) && (code <= 379)) {
            data = new Integer(Integer.parseInt(txt)); //_dfun = int_data // 8-bit int
        } else if ((380 <= code) && (code <= 389)) {
            data = new Integer(Integer.parseInt(txt)); //_dfun = int_data // 8-bit int
        } else if ((390 <= code) && (code <= 399)) {
            data = txt; //_dfun = handle_data
        } else if ((400 <= code) && (code <= 409)) {
            data = new Integer(Integer.parseInt(txt)); //_dfun = int_data // 16-bit int
        } else if ((410 <= code) && (code <= 419)) {
            data = txt; //_dfun = string_data
        } else if (code == 999) {
            data = txt; //_dfun = string_data // comment
        } else if ((1000 <= code) && (code <= 1009)) {
            data = txt; //_dfun = string_data
        } else if ((1010 <= code) && (code <= 1059)) {
            data = new Double(Double.parseDouble(txt)); //_dfun = float_data
        } else if ((1060 <= code) && (code <= 1070)) {
            data = new Integer(Integer.parseInt(txt)); //_dfun = int_data // 16-bit int
        } else if (code == 1071) {
            data = new Integer(Integer.parseInt(txt)); //_dfun = int_data # 32-bit int
        } else {
            throw new IOException("DxfReader: código " + code +
                                  " desconocido.");

            //raise ValueError, "Unexpected code: %d" % code
        }

        //return _dfun
    }
    
    /**
     * Permite comparar dos objetos de la clase DxfGroup
     * @param c, code
     * @param s, data
     * @return boolean
     */
    public boolean equals(int c, String s) {
        if ((c == code) && (s.compareTo((String) data) == 0)) {
            return true;
        }

        return false;
    }
    
    /**
     * Devuelve un dato concreto en forma de String tabulado
     * @param code
     * @return String
     */
    public static String int34car(int code) {
        if (code < 10) {
            return "  " + Integer.toString(code);
        } else if (code < 100) {
            return " " + Integer.toString(code);
        } else {
            return Integer.toString(code);
        }
    }
    
    /**
     * Devuelve un dato concreto en forma de String tabulado
     * @param value
     * @return String
     */
    public static String int6car(int value) {
        String s = "     " + Integer.toString(value);

        return s.substring(s.length() - 6, s.length());
    }
    
    /**
     * Convierte a String un dato del DxfGroup
     * @param code
     * @param value
     * @return String
     */
    public static String toString(int code, String value) {
        return int34car(code) + "\r\n" + value + "\r\n";
    }
    
    /**
     * Convierte a String un dato del DxfGroup
     * @param code
     * @param value
     * @return String
     */
    public static String toString(int code, int value) {
        return int34car(code) + "\r\n" + int6car(value) + "\r\n";
    }
    
    /**
     * Convierte a String un dato del DxfGroup
     * @param code
     * @param value
     * @param decimalPartLength
     * @return String
     */
    public static String toString(int code, float value, int decimalPartLength) {
        return int34car(code) + "\r\n" +
               decimalFormats[decimalPartLength].format((double) value) +
               "\r\n";
    }
    
    /**
     * Convierte a String un dato del DxfGroup
     * @param code
     * @param value
     * @param decimalPartLength
     * @return String
     */
    public static String toString(int code, double value, int decimalPartLength) {
        return int34car(code) + "\r\n" +
               decimalFormats[decimalPartLength].format(value) + "\r\n";
    }
    
    /**
     * Convierte a String un dato del DxfGroup
     * @param code
     * @param value
     * @return String
     */
    public static String toString(int code, Object value) {
        if (value instanceof String) {
            return toString(code, (String) value);
        } else if (value instanceof Integer) {
            return toString(code, ((Integer) value).intValue());
        } else if (value instanceof Double) {
            return toString(code, ((Double) value).floatValue(), 3);
        } else if (value instanceof Double) {
            return toString(code, ((Double) value).doubleValue(), 6);
        } else {
            return toString(code, value.toString());
        }
    }
    
    /**
     * Convierte a String un dato del DxfGroup
     */
    public String toString() {
        return toString(code, data);
    }

    /**
     * jmorell: Permite rellenar los datos. Útil en la escritura de DXFs.
     * @param data The data to set.
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * jmorell: Permite rellenar los códigos. Útil en la escritura de DXFs.
     * @param code The code to set.
     */
    public void setCode(int code) {
        this.code = code;
    }
}
