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

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

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
        super(id, name, description, status);
        this.type = TaskType.EPIC;
    }

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
        this.type = TaskType.EPIC;
    }

    public void addSubtaskId(Subtask subtask) {
        subtasksId.add(subtask);
        if (subtask.getStartTime() != null && subtask.getDuration() != 0) {
            sortByTime();
        }
    }

    public List<Subtask> getSubtasksId() {
        return subtasksId;
    }

    public void clearSubtasksId() {
        subtasksId.clear();
        this.startTime = null;
        this.endTime = null;
        this.duration = 0L;
    }

    public void removeSubtask(Subtask subtask) {
        subtasksId.remove(subtask);
        if (subtask.getStartTime() != null && subtask.getDuration() != 0) {
            sortByTime();
        }
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


        this.startTime = subtasksId.stream()
                .filter(subtask -> subtask.getStartTime() != null)
                .min(comparator)
                .get()
                .getStartTime();

        this.endTime = subtasksId.stream()
                .filter(subtask -> subtask.getStartTime() != null)
                .max(comparator)
                .get()
                .getEndTime();
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
