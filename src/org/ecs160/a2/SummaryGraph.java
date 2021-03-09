package org.ecs160.a2;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.CategorySeries;
import com.codename1.charts.renderers.DefaultRenderer;
import com.codename1.charts.renderers.SimpleSeriesRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.PieChart;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class SummaryGraph{

    //private TaskContainer allTaskData;
    //private String filter;
    private TimeSpan summaryPeriod;
    private TaskContainer taskSet;
    public SummaryGraph(TaskContainer tasks, TimeSpan timeSpan){
        this.taskSet = tasks;
        this.summaryPeriod = timeSpan;
    }
    private DefaultRenderer buildCategoryRenderer(int[] colors) {
        DefaultRenderer renderer = new DefaultRenderer();
        renderer.setLabelsTextSize(50);
        renderer.setLabelsColor(ColorUtil.BLACK);
        renderer.setMargins(new int[]{0, 0, 0, 0});

        for (int color : colors) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }


    protected CategorySeries buildCategoryDataset(String title, double[] times, TaskContainer taskSet) {
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
        // Generate the values
        //TaskContainer taskSet = getTaskSet(); //TODO change for modes
        double[] setTimes = getSetTimes(taskSet);
        int [] colors = getColorArray(setTimes.length);
        // Set up the renderer
        DefaultRenderer renderer = buildCategoryRenderer(colors);

        renderer.setShowLabels(true);

        renderer.setShowLegend(false);
        SimpleSeriesRenderer r = renderer.getSeriesRendererAt(0);

        PieChart chart = new PieChart(buildCategoryDataset("Time Breakdown", setTimes, taskSet), renderer);

        ChartComponent c = new ChartComponent(chart);

        return c;

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
    private int[] doubleColorArray(int[] allColors){
        ArrayList<Integer> result = new ArrayList<Integer>(allColors.length);
        for(int i = 0; i <allColors.length; i++){
            result.add(allColors[i]);
        }
        int[] result_array = new int[result.size()];
        for(int i = 0; i < result.size(); i++){
            result_array[i] = result.get(i).intValue();
        }
        return result_array;
    }
    private double[] getSetTimes(TaskContainer taskSet) {
        Iterator<Task> it = taskSet.iterator();
        ArrayList<Double> timeSet = new ArrayList<Double>();
        while(it.hasNext()){
            double totalTime = getTotalTimeInPeriod(it.next());
            timeSet.add(totalTime);
        }
        double[] result = new double[timeSet.size()];
        for(int i = 0; i < result.length; i++){
            result[i] = timeSet.get(i).doubleValue();
        }
        return result;
    }

    private double getTotalTimeInPeriod(Task t) {
        double total = 0;
        Duration d = t.getTimeBetween(summaryPeriod.getStartTime(), summaryPeriod.getEndTime());
        total = (double)(d.toMillis() / 1000);
        return total;
    }
    /*
    private TaskContainer getTaskSet() {

        LocalDateTime present = LocalDateTime.now();

        TimeSpan dummyTimeSpan = new TimeSpan(present);

        LocalDateTime start;
        LocalDateTime stop;

        start = LocalDateTime.MIN; //TODO change
        stop = present;
        summaryPeriod = new TimeSpan(start);
        summaryPeriod.setEndTime(stop);

        TaskContainer result = allTaskData.getTasksThatOccurred(start, stop);

        //TODO filter for tags
        //return filterTaskSet(result);
        return result;
    }
*/
}