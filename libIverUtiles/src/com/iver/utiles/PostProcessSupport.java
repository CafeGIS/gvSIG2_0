/*
 * Created on 24-ago-2005
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
package com.iver.utiles;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author fjp
 *
 * Clase que recibe las llamadas que tiene que hacer cuando termine
 * la lectura de proyecto, por ejemplo. Recibiría algo así como Clase,
 * Nombre de función a invocar, Parámetros y Prioridad.
 * Lo guarda en un array, ordena por prioridad y empieza a invocar.
 */
public class PostProcessSupport
{
    private static ArrayList callList = new ArrayList();
    private static class Call
    {
        private Object obj;
        private Method method;
        private Object[] parameters;
        private int priority;
        public Call(Object obj, String strMethod, Object[] parameters, int priority)
        {
            this.obj = obj;
            this.parameters = parameters;
            Class[] classParams = new Class[parameters.length];
            for (int i=0; i< parameters.length; i++)
                classParams[i] = parameters[i].getClass();
            try {
                method = obj.getClass().getMethod(strMethod, classParams);
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                // Resulta que con getMethod no pilla bien el superinterface VectorialLegend,
                // así que lo hacemos manual
                Method[] list = obj.getClass().getMethods();
                for (int i=0; i< list.length; i++)
                {
                    // System.err.println(list[i].toString());
                    if (list[i].getName().compareTo(strMethod) == 0)
                    {
                        boolean encontrado = true;
                        Class[] params = list[i].getParameterTypes();
                        // En cada parámetro del método, miramos si
                        // el parámetro correspondiente implementa la interfaz
                        // adecuada.
                        for (int j=0; j < params.length; j++) // for de los parámetros del método.
                        {
                        	// Miramos primero si es de la clase que buscamos, y si no lo es, pasamos
                        	// a revisar sus interfaces.
                        	boolean bParamOK = false;
//                        	if (params[j].getName().compareTo(parameters[j].getClass().getName()) == 0)
                        	if ( params[j].isInstance(parameters[j]) )
                        	{
                        		bParamOK = true;
                        		continue;
                        	}
                            // Interfaces que implementa el parámetro j-ésimo que le pasamos.
                            Class[] theInterfaces = parameters[j].getClass().getInterfaces();

                            for (int k=0; k< theInterfaces.length; k++)
                            {
                                // Si alguno de estos interfaces cuadra con lo que
                                // espera el método, podemos comprobar el siguiente parámetro.
                                // Si al salir del for externo, encontrado sigue siendo true,
                                // hemos encontrado el método que buscábamos.
                                if (theInterfaces[k].getName().compareTo(params[j].getName()) == 0)
                                {
                                    bParamOK = true;
                                    break;
                                }
                            }
                            if (bParamOK == false)
                            {
                                encontrado = false;
                                break; // Vamos a por otro método
                            }
                        }
                        if (encontrado)
                        {
                            method = list[i];
                            return;
                        }
                    }
                }
                e.printStackTrace();
            }
        }

        public Object executeMethod() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
        {
            return method.invoke(obj,parameters);
        }
    }
    public static void addToPostProcess(Object obj, String strMethod, Object[] parameters, int priority)
    {
        Call newCall = new Call(obj, strMethod, parameters, priority);
        callList.add(newCall);
    }
    public static void addToPostProcess(Object obj, String strMethod, Object parameter, int priority)
    {
        Object[] parameters = new Object[1];
        parameters[0] = parameter;
        Call newCall = new Call(obj, strMethod, parameters, priority);
        callList.add(newCall);
    }

    private static void orderByPriority()
    {
        // TODO
    }
    public static void executeCalls()
    {
        // TODO: Primero deberíamos ordenar por prioridad
        // por ahora no lo hago.
        orderByPriority();

        for (int i=0; i < callList.size(); i++)
        {
            Call call = (Call) callList.get(i);
            try {
                call.executeMethod();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        clearList();
    }
    public static void clearList()
    {
        callList.clear();
    }
}
