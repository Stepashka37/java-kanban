/**
 * Класс описывающий эпик
 *
 * @see manager.Manager
 */

package ru.yandex.praktikum.tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    public List<Subtask> subtasksId = new ArrayList<>();
    protected LocalDateTime endTime;


    @Override
    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public Epic(int id, String name, String description, TaskStatus status, long duration, LocalDateTime startTime) {
        super(id, name, description, status, duration, startTime);
        this.endTime = startTime;
        this.type = TaskType.EPIC;
    }

    public Epic(String name, String description, TaskStatus status, long duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.endTime = startTime;
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
        if (subtasksId == null) {
            subtasksId = new ArrayList<>();
        }

        subtasksId.add(subtask);
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
    }

    public void setSubtasksId(List<Subtask> subtasksId) {
        this.subtasksId = subtasksId;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
