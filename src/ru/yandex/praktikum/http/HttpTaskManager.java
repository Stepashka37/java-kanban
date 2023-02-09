package ru.yandex.praktikum.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.yandex.praktikum.manager.FileBackedTasksManager;
import ru.yandex.praktikum.manager.Managers;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.Subtask;
import ru.yandex.praktikum.tasks.Task;
import ru.yandex.praktikum.tasks.TaskType;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    private final Gson gson;
    private final KVTaskClient client;

    public HttpTaskManager( int port) throws IOException {
        this(port, false);
    }

    public HttpTaskManager (int port, boolean load) throws IOException {
        super(null);
        gson = Managers.getGson();
        client = new KVTaskClient(port);
        if (load) {
            load();
        }
    }

    protected void addTasks (List<? extends Task> tasks) {
        for (Task task : tasks) {
            final int id = task.getId();
            if(id > genId) {
                genId = id;
            }
            TaskType type = task.getType();
            if(type == TaskType.TASK) {
                this.tasks.put(id, task);
                prioritizedTasks.add(task);

            } else if (type == TaskType.SUBTASK) {
                subtasks.put(id, (Subtask) task);
                prioritizedTasks.add(task);

            } else if (type == TaskType.EPIC) {
                epics.put(id, (Epic) task);
            }
        }
    }

    protected void load() {
        ArrayList<Task> tasks = gson.fromJson(client.load("tasks"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        addTasks(tasks);

        ArrayList<Epic> epics = gson.fromJson(client.load("epics"), new TypeToken<ArrayList<Epic>>() {
        }.getType());
        addTasks(epics);

        ArrayList<Subtask> subtasks = gson.fromJson(client.load("subtasks"), new TypeToken<ArrayList<Subtask>>() {
        }.getType());
        addTasks(subtasks);

        List<Integer> history = gson.fromJson(client.load("history"), new TypeToken<ArrayList<Integer>>() {
        }.getType());

        for (Integer taskId : history) {
            if ( this.tasks.get(taskId) != null) {
                historyManager.add(this.tasks.get(taskId));
            } else if (this.epics.get(taskId) != null) {
                historyManager.add(this.epics.get(taskId));
            } else {
                historyManager.add(this.subtasks.get(taskId));
            }


        }
    }

    @Override
    public void save(){
        String jsonTasks = gson.toJson(new ArrayList<>(tasks.values()));
        client.put("tasks", jsonTasks);
        String jsonEpics = gson.toJson(new ArrayList<>(epics.values()));
        client.put("epics", jsonEpics);
        String jsonSubtasks = gson.toJson(new ArrayList<>(subtasks.values()));
        client.put("subtasks", jsonSubtasks);

        String jsonHistory = gson.toJson(historyManager.getHistory().stream().map(Task::getId).collect(Collectors.toList()));
        client.put("history", jsonHistory);
    }

    int getGenId(){
        return genId;
    }
}

