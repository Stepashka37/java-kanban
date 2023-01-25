/**
 * Класс описывающий подзадачу
 *
 * @see manager.Manager
 */
package ru.yandex.praktikum.tasks;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    protected int epicId;


    public Subtask(int id, String name, String description, TaskStatus status, int epicId, long duration, LocalDateTime startTime) {
        super(id, name, description, status, duration, startTime);
        this.epicId = epicId;
        this.type = TaskType.SUBTASK;
    }

    public Subtask(String name, String description, TaskStatus status, int epicId, long duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
        this.type = TaskType.SUBTASK;
    }

    public Subtask(int id, String name, String description, TaskStatus status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
        this.type = TaskType.SUBTASK;
    }

    public Subtask(String name, String description, TaskStatus status, int epicId) {
        super(name,  description, status);
        this.type = TaskType.SUBTASK;
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }



    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }


}


