/**
 * Класс описывающий эпик
 *
 * @see manager.Manager
 */

package ru.yandex.praktikum.tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    protected List<Integer> subtasksId = new ArrayList<>();


    public Epic(int id,  String name, String description, TaskStatus status) {
        super(id,  name, description, status);
        this.type = TaskType.EPIC;
    }

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
        this.type = TaskType.EPIC;
    }

    public void addSubtaskId(int id) {
        subtasksId.add(id);
    }

    public List<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void clearSubtasksId() {
        subtasksId.clear();
    }

    public void removeSubtask(int id) {
        subtasksId.remove(Integer.valueOf(id));
    }

    public void setSubtasksId(List<Integer> subtasksId) {
        this.subtasksId = subtasksId;
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
