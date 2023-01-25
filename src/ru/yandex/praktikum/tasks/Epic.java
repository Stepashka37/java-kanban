/**
 * Класс описывающий эпик
 *
 * @see manager.Manager
 */

package ru.yandex.praktikum.tasks;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

public class Epic extends Task {
    protected List<Subtask> subtasksId = new ArrayList<>();

    private LocalDateTime endTime;


    public Epic(int id, String name, String description, TaskStatus status, long duration, LocalDateTime startTime) {
        super(id, name, description, status, duration, startTime);
        endTime = startTime;
        this.type = TaskType.EPIC;
    }

    public Epic(String name, String description, TaskStatus status, long duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        endTime = startTime;
        this.type = TaskType.EPIC;
    }

    public Epic(int id, String name, String description, TaskStatus status) {
        super(id,  name, description, status);
        this.type = TaskType.EPIC;
    }

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
        this.type = TaskType.EPIC;
    }

    public void addSubtaskId(Subtask subtask) {
        subtasksId.add(subtask);
        //sortByTime();
    }

    public List<Subtask> getSubtasksId() {
        return subtasksId;
    }

    public void clearSubtasksId() {
        subtasksId.clear();
    }

    public void removeSubtask(Subtask subtask) {
        subtasksId.remove(subtask);
    }

    public void setSubtasksId(List<Subtask> subtasksId) {
        this.subtasksId = subtasksId;
    }

    public void sortByTime() {


        Comparator<Subtask> comparator = new Comparator<Subtask>() {
            @Override
            public int compare(final Subtask o1, final Subtask o2) {
                if (o1.getStartTime().isBefore(o2.getStartTime())) {
                    return -1;
                } else if (o1.getStartTime().isAfter(o2.getStartTime())) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };

        Collections.sort(subtasksId, comparator);
    this.startTime = subtasksId.get(0).getStartTime();
    this.endTime = subtasksId.get(subtasksId.size()-1).getEndTime();
    this.duration = subtasksId.stream()
            .mapToLong(Subtask::getDuration)
            .sum();
    }


    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", subtasksId=" + subtasksId +
                ", status='" + status + '\'' +
                '}';
    }


}
