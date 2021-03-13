package org.ecs160.a2;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.XYMultipleSeriesDataset;
import com.codename1.charts.views.LineChart;
import com.codename1.charts.models.XYSeries;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.renderers.XYSeriesRenderer;
import com.codename1.charts.views.PointStyle;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Component;

import java.util.ArrayList;
import java.util.List;

public class TaskDetailsGraph {
    private final double[] dailyTimes;
    private final int numberOfDays;
    private double maxDailyTime;

    TaskDetailsGraph(double[] times) {
        dailyTimes = convertTimesToSeconds(times);
        numberOfDays = times.length;

        calculateDailyTimeBounds();
    }
    private double[] convertTimesToSeconds(double[] times){
        double[] timesInSeconds = new double[times.length];
        for(int i = 0; i<times.length; i++){
            timesInSeconds[i] = times[i] / (1000);
        }
        return timesInSeconds;
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
        maxDailyTime = max;
    }

    private XYMultipleSeriesRenderer buildRenderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();


       // renderer.setMargins(new int[]{10,  10, 10, 10});
        renderer.setAxisTitleTextSize(50);
        renderer.setLabelsTextSize(40);
        renderer.setAxesColor(UITheme.BLACK);
        renderer.setLabelsColor(ColorUtil.BLACK);
        renderer.setShowLegend(false);

        renderer.setYTitle("Daily Time Spent (s)");

        renderer.setXLabels(0);
        //renderer.setYLabels(10);
        renderer.setYLabelsColor(0, UITheme.BLACK);

        renderer.setShowGrid(true);
        renderer.setXLabelsAlign(Component.RIGHT);
        renderer.setYLabelsAlign(Component.RIGHT);
        renderer.setYLabelsPadding(10);
        renderer.setYLabelsVerticalPadding(10);
        renderer.setPointSize(10f);
        renderer.setMarginsColor(UITheme.WHITE);
        renderer.setPanEnabled(false);

        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(UITheme.GREEN);
        r.setPointStyle(PointStyle.CIRCLE);
        r.setFillPoints(true);
        r.setLineWidth(5);
        renderer.addSeriesRenderer(r);

        return renderer;
    }


    XYMultipleSeriesDataset buildDataset(String[] titles,
                                         List<double[]> xValues,
                                         List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        addXYSeries(dataset, titles, xValues, yValues);
        return dataset;
    }
    void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles,
                     List<double[]> xValues, List<double[]> yValues) {

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
        List<double[]> xValues = new ArrayList<>();
        double[] days = new double[numberOfDays];
        for(int i = 0; i< numberOfDays; i++){
            days[i] = i;
        }
        xValues.add(days);
        return xValues;
    }

    public ChartComponent createLineChart() {
        String[] titles = new String[]{"Selected Task"};

        //random double arrays to test
        List<double[]> x = createXValues();
        List<double[]> y = new ArrayList<>();
        y.add(dailyTimes);

        XYMultipleSeriesRenderer renderer = buildRenderer();

        renderer.setXAxisMin(-0.5);
        renderer.setXAxisMax((double)numberOfDays - 0.5);

        renderer.setYAxisMin(0);
        renderer.setYAxisMax(maxDailyTime + 1);


        XYMultipleSeriesDataset dataset = buildDataset(titles, x, y);

        LineChart chart = new LineChart(dataset, renderer);
        return new ChartComponent(chart);

    }

}