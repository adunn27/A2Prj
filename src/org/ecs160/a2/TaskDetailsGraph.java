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

    XYMultipleSeriesRenderer buildRenderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize(50);
        renderer.setChartTitleTextSize(50);
        renderer.setLabelsTextSize(50);

        renderer.setPointSize(5f);
        renderer.setMargins(new int[] { 0, 0, 15,0 });
        renderer.setMarginsColor(UITheme.WHITE);

        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(ColorUtil.CYAN);
        r.setPointStyle(PointStyle.TRIANGLE);
        r.setFillPoints(true);
        renderer.addSeriesRenderer(r);

        return renderer;
    }


    XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues,
                                         List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        addXYSeries(dataset, titles, xValues, yValues, 0);
        return dataset;
    }
    void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<double[]> xValues,
                     List<double[]> yValues, int scale) {
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            XYSeries series = new XYSeries(titles[i], scale);
            double[] xV = xValues.get(i);
            double[] yV = yValues.get(i);
            int seriesLength = xV.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
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

       // (XYSeriesRenderer) renderer.getSeriesRendererAt(0).setFillPoints(true);
        //renderer.setChartTitle(title);
        //renderer.setXTitle(xTitle);
        renderer.setYTitle("TimeSpent");
        renderer.setXAxisMin(0);
        renderer.setXAxisMax((double)numberOfDays);

        if(minDailyTime < maxDailyTime)
            renderer.setYAxisMin(minDailyTime);
        else
            renderer.setYAxisMin(0);
        renderer.setYAxisMax(maxDailyTime);
        renderer.setAxesColor(ColorUtil.BLACK);
        renderer.setLabelsColor(ColorUtil.BLACK);
        renderer.setXLabels(12);
        renderer.setYLabels(10);
        renderer.setShowGrid(true);
        renderer.setXLabelsAlign(Component.RIGHT);
        renderer.setYLabelsAlign(Component.RIGHT);

        XYMultipleSeriesDataset dataset = buildDataset(titles, x, y);

        LineChart chart = new LineChart(dataset, renderer);
        //chart.
        ChartComponent c = new ChartComponent(chart);

        return c;

    }

}