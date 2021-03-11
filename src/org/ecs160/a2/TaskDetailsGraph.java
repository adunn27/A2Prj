package org.ecs160.a2;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.XYMultipleSeriesDataset;
import com.codename1.charts.views.LineChart;
import com.codename1.charts.models.XYSeries;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.renderers.XYSeriesRenderer;
import com.codename1.charts.views.PointStyle;
import com.codename1.ui.Component;
import com.codename1.ui.Form;
import com.codename1.ui.Transform;
import java.util.ArrayList;
import java.util.List;
import com.codename1.charts.util.ColorUtil;
public class TaskDetailsGraph {
    private double[] dailyTimes;
    private int numberOfDays;
    private double maxDailyTime;
    private double minDailyTime;

    TaskDetailsGraph(double[] times) {
        dailyTimes = times;
        numberOfDays = times.length;

        calculateDailyTimeBounds();
    }
    private void calculateDailyTimeBounds(){
        double min = 0;
        double max = 0;
        for(int i = 0; i < numberOfDays; i++){
            double time = dailyTimes[i];
            if(time < min)
                min = time;
            if(time > max)
                max = time;
        }
        minDailyTime = min;
        maxDailyTime = max;
    }

    private XYMultipleSeriesRenderer buildRenderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize(50);
        renderer.setLabelsTextSize(50);
        renderer.setAxesColor(ColorUtil.BLACK);
        renderer.setShowLegend(false);

        renderer.setYTitle("Time Spent");

        renderer.setXLabels(0);
        //renderer.setYLabels(10);
        renderer.setYLabelsColor(0, ColorUtil.BLACK);

        renderer.setShowGrid(true);
        renderer.setXLabelsAlign(Component.RIGHT);
        renderer.setYLabelsAlign(Component.RIGHT);

        renderer.setPointSize(10f);
        renderer.setMarginsColor(UITheme.WHITE);
        renderer.setPanEnabled(false);

        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(ColorUtil.BLUE);
        r.setPointStyle(PointStyle.CIRCLE);
        r.setFillPoints(true);
        r.setLineWidth(5);
        renderer.addSeriesRenderer(r);

        return renderer;
    }


    XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues,
                                         List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        addXYSeries(dataset, titles, xValues, yValues);
        return dataset;
    }
    void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<double[]> xValues,
                     List<double[]> yValues) {

            XYSeries series = new XYSeries(titles[0], 0);
            double[] xV = xValues.get(0);
            double[] yV = yValues.get(0);
            int seriesLength = xV.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
    }
    private List<double[]> createXValues(){
        List<double[]> xValues = new ArrayList<double[]>();
        double[] days = new double[numberOfDays];
        for(int i = 0; i<numberOfDays; i++){
            days[i] = (double)i;
        }
        xValues.add(days);
        return xValues;
    }

    public ChartComponent createLineChart() {
        String[] titles = new String[]{"Selected Task"};

        //random double arrays to test
        List<double[]> x = createXValues();
        List<double[]> y = new ArrayList<double[]>();
        y.add(dailyTimes);

        XYMultipleSeriesRenderer renderer = buildRenderer();

        renderer.setXAxisMin(0);
        renderer.setXAxisMax((double)numberOfDays);

        if(minDailyTime < maxDailyTime)
            renderer.setYAxisMin(minDailyTime);
        else
            renderer.setYAxisMin(0);
        renderer.setYAxisMax(maxDailyTime);


        XYMultipleSeriesDataset dataset = buildDataset(titles, x, y);

        LineChart chart = new LineChart(dataset, renderer);
        //chart.
        ChartComponent c = new ChartComponent(chart);

        return c;

    }

}