package com.faktel.features;

import com.faktel.features.piechart.PieChartPainter;
import com.faktel.mvc.Grid;

/**
 * This is a pie chart view. It accepts a grid with 2 rows. The first row
 * is the names of the parts and the second row as the amount of each part.
 * The chart is going to visualize the amounts into a pie chart relative to
 * their sum.
 * 
 * Example:
 * 
 * Apples  Oranges  Peas
 *   10       11     7
 *   
 * This is going to show a pie chart with largest share of the orages and
 * smallest share of the peas.
 * 
 * @author valentinmihov
 */
public class VerticalPieChartView extends com.faktel.mvc.View {
	private static final long serialVersionUID = 4973825408533452519L;
	
	PieChartPainter m_piechart;
	
	public VerticalPieChartView(String name) {
		super(name);
		
		m_piechart = new PieChartPainter();
		setUI(m_piechart);
	}

	@Override
	public boolean displayGrid(Grid grid) {
		String[] labels = new String[grid.size() - 1];
		String[] values = new String[grid.size() - 1];
		
		for (int i = 1;i < grid.size();i++) {
			labels[i - 1] = grid.get(i).get().get(0).toString();
			values[i - 1] = grid.get(i).get().get(1).toString();
		}
		
		m_piechart.setLabels(labels);
		
		m_piechart.setValues(normalizeValues(values));
		return true;
	}
	
	private double[] normalizeValues(Object[] values) {
		double[] result = new double[values.length];
		double sum = 0;
		
		for (int i = 0;i < values.length;i++) {
			result[i] = Double.parseDouble(values[i].toString());
			sum += result[i];
		}
		
		for (int i = 0;i < values.length;i++) {
			result[i] /= sum;
		}
		
		return result;
	}

}
