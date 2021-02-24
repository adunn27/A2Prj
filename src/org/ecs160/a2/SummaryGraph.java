package org.ecs160.a2;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.CategorySeries;
import com.codename1.charts.renderers.DefaultRenderer;
import com.codename1.charts.renderers.SimpleSeriesRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.PieChart;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SummaryGraph{

    private TaskContainer allTaskData;
    private final UINavigator ui;

    private SummaryMode mode;
    private TimeSpan summaryPeriod;
    public SummaryGraph(UINavigator ui){
        this.ui = ui;
        allTaskData = ui.backend.getUnarchivedTasks();
        mode = SummaryMode.DAY; //default
    }
    public void setSummaryMode(SummaryMode m){
        mode = m;
    }

    private DefaultRenderer buildCategoryRenderer(int[] colors) {
        DefaultRenderer renderer = new DefaultRenderer();
        renderer.setLabelsTextSize(35);
        renderer.setLegendTextSize(35);
        //renderer.setMargins(new int[]{20, 30, 15, 0});
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
            while (it.hasNext()) {
                Task t = it.next();
                series.add("" + t.getName(), time);
            }
        }
        return series;
    }
    public ChartComponent createPieChart() {
        // Generate the values
        TaskContainer taskSet = getTaskSet(); //TODO change for modes
        double[] setTimes = getSetTimes(taskSet);
        int [] colors = getColorArray(setTimes.length);
        // Set up the renderer
        //int[] colors = new int[]{ColorUtil.BLUE, ColorUtil.GREEN, ColorUtil.MAGENTA, ColorUtil.YELLOW, ColorUtil.CYAN};
        DefaultRenderer renderer = buildCategoryRenderer(colors);

        renderer.setChartTitleTextSize(20);
        renderer.setDisplayValues(true);
        renderer.setShowLabels(true);
        SimpleSeriesRenderer r = renderer.getSeriesRendererAt(0);

        // Create the chart ... pass the values and renderer to the chart object.
        PieChart chart = new PieChart(buildCategoryDataset("Time Breakdown", setTimes, taskSet), renderer);

        ChartComponent c = new ChartComponent(chart);

        return c;

    }

    private int[] getColorArray(int numTasks) {
        int[] allColors = new int[]{
                ColorUtil.BLUE, ColorUtil.GREEN, ColorUtil.MAGENTA,
                ColorUtil.YELLOW, ColorUtil.CYAN, ColorUtil.LTGRAY,
                ColorUtil.GRAY
        };
        while(numTasks > allColors.length){
            int len = allColors.length;
            int[] temp = allColors;
            allColors = Arrays.copyOf(allColors, len * 2);
            System.arraycopy(temp, 0, allColors, len, len*2);
        }
        int[] result = Arrays.copyOfRange(allColors, 0, numTasks);
        return result;
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
        t.getTimeBetween(summaryPeriod.getStartTime(), summaryPeriod.getEndTime());
        return total;
    }

    private TaskContainer getTaskSet(){
        //TaskContainer result = new TaskContainer();
        LocalDateTime present = LocalDateTime.now();
        TimeSpan dummyTimeSpan = new TimeSpan(present);

        LocalDateTime start;
        LocalDateTime stop;
        if(this.mode == SummaryMode.DAY) {
            start = dummyTimeSpan.getStartOfDay(present);
            stop = dummyTimeSpan.getEndOfDay(present);
        }
       else if(this.mode == SummaryMode.WEEK) {
           start = dummyTimeSpan.getStartOfDay(present);
           stop = dummyTimeSpan.getEndOfDay(present);
       }
       else{
           start = LocalDateTime.MIN; //TODO change
           stop = present;
        }

       summaryPeriod = new TimeSpan(start);
       summaryPeriod.setEndTime(stop);

       return allTaskData.getTasksThatOccurred(start, stop);
   }
}
