/*
 * Created on 16-dic-2005 by fjp
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 * 
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
/* CVS MESSAGES:
 * 
 * $Id: TestBigByteBuffer.java 8515 2006-11-06 07:30:00Z jaume $
 * $Log$
 * Revision 1.3  2006-11-06 07:29:59  jaume
 * organize imports
 *
 * Revision 1.2  2006/05/12 12:40:29  fjp
 * Driver thread-safe. Se evitan errores aleatorios que aparecían a veces.
 *
 * Revision 1.1  2006/01/18 13:01:08  fjp
 * BigByteBuffer2 que usa solo 8K y es adecuado para los índices espaciales
 *
 *
 */
package com.iver.utiles.bigfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

/**
 * @author fjp
 *
 */
public class TestBigByteBuffer {
    File f = new File("D:/Fjp/chiara/plano/vias.shp");
    FileInputStream fin;
    FileInputStream fin2;
    FileChannel channel2;

    BigByteBuffer2 bb;
    // Hacemos varias pruebas para ver si funciona
    // exactamente igual que el original
    static int numPruebas = 50000;
	
	public class MyThread extends Thread
	{

		String name;
		public MyThread(String string) {
			name = string;

		}

		public void run() {
			try {
				prueba2(name, f, numPruebas);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        
            // prueba1(f, numPruebas);
    	    TestBigByteBuffer test = new TestBigByteBuffer();
    	    test.test();

    }
    
    public void test() throws IOException
    {
        fin2 = new FileInputStream(f);
        channel2 = fin2.getChannel();
        bb = new BigByteBuffer2(channel2, 
                   FileChannel.MapMode.READ_ONLY, 1024*8);
    	MyThread th = new MyThread("T1:");
    	th.start();
    	MyThread th2 = new MyThread("T2: ");
    	th2.start();

       
    System.out.println("Fin de la prueba. " + numPruebas + " iteraciones.");

    }

    /**
     * @param name 
     * @param f
     * @param numPruebas
     * @throws Exception 
     */
    private void prueba2(String name, File f, int numPruebas) throws Exception {
        FileInputStream fin;
        fin = new FileInputStream(f);
        // Open the file and then get a channel from the stream
        FileChannel channel = fin.getChannel();
        int size = (int) channel.size();
        // Get the file's size and then map it into memory
        ByteBuffer bbCorrect = channel.map(FileChannel.MapMode.READ_ONLY, 0, size);
           // Chunkeo a 1KB
        Random rnd = new Random();
        long t1 = System.currentTimeMillis();

        for (int i=0; i < numPruebas; i++)
           {
               int pos = rnd.nextInt(size-10);
               // pos = 0;
               bbCorrect.position(pos);
               int bCorrect = bbCorrect.getInt();
//            	   bb.position(pos);               
            	   int bPrueba = bb.getInt(pos);
               if (bCorrect != bPrueba)
               {
                   System.err.println(name + "Error de lectura. " + bCorrect + " " + bPrueba);
                   throw new Exception("Error con pos=" + pos);
               }
               else
               {
                   System.out.println(name + "Correcto: pos=" + pos + " byte= " + bPrueba);
               } 
               
           }
        close(channel2, fin2, bb);
        long t2 = System.currentTimeMillis();
        System.out.println("T=" + (t2-t1) + "mseconds");
    }

    /**
     * @param f
     * @param numPruebas
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void prueba1(File f, int numPruebas) throws FileNotFoundException, IOException {
        FileInputStream fin;
        fin = new FileInputStream(f);
        // Open the file and then get a channel from the stream
        FileChannel channel = fin.getChannel();
        int size = (int) channel.size();
        // Get the file's size and then map it into memory
           ByteBuffer bbCorrect = channel.map(FileChannel.MapMode.READ_ONLY, 0, size);
           // Chunkeo a 1KB
        Random rnd = new Random();
        long t1 = System.currentTimeMillis();
        FileInputStream fin2 = new FileInputStream(f);
        FileChannel channel2 = fin2.getChannel();
        BigByteBuffer bb = new BigByteBuffer(channel2, 
                   FileChannel.MapMode.READ_ONLY, 1024*1024);
        for (int i=0; i < numPruebas; i++)
           {
               int pos = rnd.nextInt(size-10);
               // pos = 2;
               
               byte bCorrect = bbCorrect.get(pos);
               byte bPrueba = bb.get(pos);
               if (bCorrect != bPrueba)
               {
                   System.err.println("Error de lectura. " + bCorrect + " " + bPrueba);
               }
               else
               {
                   // System.out.println("Correcto: pos=" + pos + " byte= " + bPrueba);
               }
               
           }
        close(channel2, fin2, bb);
        System.gc();
        long t2 = System.currentTimeMillis();
        System.out.println("T=" + (t2-t1) + "mseconds");
    }
    
    static public synchronized void close(FileChannel channel, FileInputStream fin, BigByteBuffer2 bb) throws IOException {
        IOException ret = null;
        
        try {
            channel.close();
        } catch (IOException e) {
            ret = e;
        } finally {
            try {
                fin.close();
            } catch (IOException e1) {
                ret = e1;
            }
        }

        if (ret != null) {
            throw ret;
        }
        // else // Si todo ha ido bien, preparamos para liberar memoria.
        //     bb = null;
    }
    static public synchronized void close(FileChannel channel, FileInputStream fin, BigByteBuffer bb) throws IOException {
        IOException ret = null;
        
        try {
            channel.close();
        } catch (IOException e) {
            ret = e;
        } finally {
            try {
                fin.close();
            } catch (IOException e1) {
                ret = e1;
            }
        }

        if (ret != null) {
            throw ret;
        }
        else // Si todo ha ido bien, preparamos para liberar memoria.
            bb = null;
    }

}
