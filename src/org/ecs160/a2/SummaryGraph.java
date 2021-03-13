package org.ecs160.a2;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.CategorySeries;
import com.codename1.charts.renderers.DefaultRenderer;
import com.codename1.charts.renderers.SimpleSeriesRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.PieChart;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class SummaryGraph{

    private final TimeSpan summaryPeriod;
    private final TaskContainer taskSet;
    public SummaryGraph(TaskContainer tasks, TimeSpan timeSpan){
        this.taskSet = tasks;
        this.summaryPeriod = timeSpan;
    }
    private DefaultRenderer buildCategoryRenderer(int[] colors) {
        DefaultRenderer renderer = new DefaultRenderer();
        renderer.setLabelsTextSize(50);
        renderer.setLabelsColor(ColorUtil.BLACK);
        renderer.setMargins(new int[]{0, 0, 0, 0});
        renderer.setScale(0.90F);
        for (int color : colors) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }

    protected CategorySeries buildCategoryDataset(String title, double[] times,
                                                  TaskContainer taskSet) {
        CategorySeries series = new CategorySeries(title);
        Iterator<Task> it = taskSet.iterator();
        for (double time : times) {
            if (it.hasNext()) {
                Task t = it.next();
                series.add("" + t.getName(), time);
            }
        }
        return series;
    }

    public ChartComponent createPieChart() {
        double[] setTimes = getSetTimes(taskSet);
        int [] colors = getColorArray(setTimes.length);

        DefaultRenderer renderer = buildCategoryRenderer(colors);
        renderer.setShowLabels(true);
        renderer.setShowLegend(false);

        PieChart chart = new PieChart(buildCategoryDataset("Time Breakdown",
                                                  setTimes, taskSet), renderer);
        return new ChartComponent(chart);
    }

    private int[] getColorArray(int numTasks) {
        int[] allColors = new int[numTasks];
        Random random = new Random();
        for(int i= 0; i < numTasks; i++){
            int r = random.nextInt();
            int g = random.nextInt();
            int b = random.nextInt();

            allColors[i] = ColorUtil.argb(0,r, g, b);
        }
        return allColors;
    }

    private double[] getSetTimes(TaskContainer taskSet) {
        Iterator<Task> it = taskSet.iterator();
        ArrayList<Double> timeSet = new ArrayList<>();
        while(it.hasNext()){
            double totalTime = getTotalTimeInPeriod(it.next());
            timeSet.add(totalTime);
        }
        double[] result = new double[timeSet.size()];
        for(int i = 0; i < result.length; i++){
            result[i] = timeSet.get(i);
        }
        return result;
    }

    private double getTotalTimeInPeriod(Task t) {
        Duration d = t.getTimeBetween(summaryPeriod.getStartTime(),
                                      summaryPeriod.getEndTime());
        return (double)(d.toMillis() / 1000);
    }
}