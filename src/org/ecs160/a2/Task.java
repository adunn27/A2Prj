package org.ecs160.a2;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class Task {
    private String name;
    private TaskSize size;
    private String description = "";
    private Boolean isArchived = false;
    private Boolean isActive = false;
    private Set<String> tags; //TODO does order matter? Do tags need color also?
    private List<TimePeriod> allTimes; //TODO better name

    public Task(String newTaskName, TaskSize newTaskSize) {
        this.name = newTaskName;
        this.size = newTaskSize;
        this.tags = new HashSet<>();
        this.allTimes = new Vector<TimePeriod>();
    }

    public Task(String newTaskName) {
        this.name = newTaskName;
        this.size = TaskSize.S;
        this.tags = new HashSet<>();
        this.allTimes = new Vector<TimePeriod>();
    }
}
