/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
 */
package org.gvsig.gui.beans.graphic;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
/**
 * Componente gráfico de JFree
 * 
 * @version 05/04/2007
 * @author Nacho Brodin (brodin_ign@gva.es)
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class GraphicChartPanel extends JPanel {
	private static final long serialVersionUID = 7328137487119964665L;
	private JFreeChart         chart       = null;
	private ChartPanel         jPanelChart = null;
	private XYSeries           series[]    = new XYSeries[0];
	private XYSeriesCollection dataset     = null;

	private int                viewType    = 0;

	public GraphicChartPanel() {
		dataset = new XYSeriesCollection();

		createChart();

		initialize();

		Plot plot = this.jPanelChart.getChart().getPlot();
		plot.setOutlineStroke(new BasicStroke(1));
		plot.setOutlinePaint(Color.black);

		chart.setBackgroundPaint(Color.white);
		plot.setBackgroundPaint(new Color(245, 245, 245));
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(getChart(), BorderLayout.CENTER);
	}

	/**
	 * @return
	 */
	public ChartPanel getChart() {
		if (jPanelChart == null) {
			jPanelChart = new ChartPanel(chart);
			jPanelChart.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
		}
		return jPanelChart;
	}

	/**
	 * Creates a chart.
	 * @param dataset the dataset.
	 * @return A chart.
	 */
	private void createChart() {
		chart = ChartFactory.createXYLineChart(null, null, null, dataset,
							PlotOrientation.VERTICAL, false, true, true);

		// Definir la lista de colores
		XYPlot plot = chart.getXYPlot();
		plot.getRenderer().setSeriesPaint(0, Color.red);
		plot.getRenderer().setSeriesPaint(1, Color.green);
		plot.getRenderer().setSeriesPaint(2, Color.blue);
		plot.getRenderer().setSeriesPaint(3, Color.cyan);
		plot.getRenderer().setSeriesPaint(4, Color.black);
		plot.getRenderer().setSeriesPaint(5, Color.darkGray);
		plot.getRenderer().setSeriesPaint(6, Color.gray);
		plot.getRenderer().setSeriesPaint(7, Color.magenta);
		plot.getRenderer().setSeriesPaint(8, Color.yellow);
		plot.getRenderer().setSeriesPaint(9, Color.orange);

		Image img = new ImageIcon(getClass().getResource("images/splash.png")).getImage();
		plot.setBackgroundPaint(null);
		plot.setBackgroundImageAlpha(0.18f);
		plot.setBackgroundImage(img);
	}

	/**
	 * Asigna nuevos valores para la gráfica
	 * @param values Matriz de [número de gráficas][Valores por gráfica]
	 */
	public void setNewChart(int[][] values, String names[]) {
		series = new XYSeries[values.length];
		for (int iGraf = 0; iGraf < values.length; iGraf++) {
			series[iGraf] = new XYSeries(names[iGraf]);
			for (int i = 0; i < values[iGraf].length; i++) {
				series[iGraf].add(new Integer(i), new Integer(values[iGraf][i]));
			}
		}
		reloadGraphic();
	}

	/**
	 * Asigna nuevos valores para la gráfica
	 * @param values Matriz de [número de gráficas][Valores por gráfica]
	 */
	public void setNewChart(double[][][] values, String names[]) {
		series = new XYSeries[values.length];
		for (int iGraf = 0; iGraf < values.length; iGraf++) {
			series[iGraf] = new XYSeries(names[iGraf]);
			for (int i = 0; i < values[iGraf].length; i++) {
				series[iGraf].add(values[iGraf][i][0], values[iGraf][i][1]);
			}
		}
		reloadGraphic();
	}

	/**
	 * Define el tipo de visualizacion para la grafica.
	 * Valores: 0 - Normal, 1 - Acumulado, 2 - Logaritmico
	 * @param type
	 */
	public void setViewType(int type) {
		this.viewType = type;
		reloadGraphic();
	}

	/**
	 * Recarga los datos de la grafica dependiendo del tipo de visualizacion
	 */
	private void reloadGraphic() {
		dataset.removeAllSeries();
		switch (viewType) {
			case 0: // Normal
				for (int i = 0; i < series.length; i++)
					dataset.addSeries(series[i]);
				break;
			case 1: // Acumulado
				XYSeries[] seriesAcum = new XYSeries[series.length];
				for (int i = 0; i < series.length; i++) {
					seriesAcum[i] = new XYSeries(series[i].getKey());
					double total = 0;
					for (int j = 0; j < series[i].getItemCount(); j++) {
						total += series[i].getY(j).doubleValue();
						seriesAcum[i].add(series[i].getX(j), total);
					}
					dataset.addSeries(seriesAcum[i]);
				}
				break;
			case 2: // Logaritmico
				XYSeries[] seriesLog = new XYSeries[series.length];

				double minim = Double.MAX_VALUE;
				for (int i = 0; i < series.length; i++)
					for (int j = 0; j < series[i].getItemCount(); j++)
						if (minim > series[i].getY(j).doubleValue())
							minim = series[i].getY(j).doubleValue();
				
				for (int i = 0; i < series.length; i++) {
					seriesLog[i] = new XYSeries(series[i].getKey());
					for (int j = 0; j < series[i].getItemCount(); j++)
						seriesLog[i].add(series[i].getX(j), java.lang.Math.log(series[i].getY(j).doubleValue() - minim + 1.0));
					dataset.addSeries(seriesLog[i]);
				}
				break;
		}
		jPanelChart.repaint();
	}

	/**
	 * Limpia la gráfica
	 */
	public void cleanChart() {
		series = new XYSeries[0];
		reloadGraphic();
	}
}