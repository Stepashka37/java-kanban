/** Класс описывающий основную логику Трекера Задач.
 *  */

package ru.yandex.praktikum;

import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.Subtask;
import ru.yandex.praktikum.tasks.Task;

import java.util.*;

public class Manager {

    /** @tasks - мапа для хранения задач
     * @subtasks - мапа для хранения подзадач
     * @epics - мапа для хранения эпиков
     * @genId = генерируемый id
     *  */
    private int genId = 0;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    /** Методы для создания задач. В качестве паараметра передается объект
     * @return - id созданной задачи */
    public int createTask(Task task) {
        final int id = ++genId;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    /** Методы для создания эпиков. В качестве паараметра передается объект
     * @return - id созданного эпика */
    public int createEpic(Epic epic) {
        final int id = ++genId;
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    /** Методы для создания подзадач. В качестве паараметра передается объект
     * Реализована логика проверки, есть ли эпик по указанному epicId
     * @return - id созданной подзадачи */
    public Integer createSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            final int id = ++genId;
            subtask.setId(id);
            subtasks.put(id, subtask);
            epic.addSubtaskId(id);
            calculateEpicStatus(epic);
            return id;
        } else {
            System.out.println("Такого эпика нет!");
            return 0;
        }
    }

    /** Метод для получения задачи по id, реализована логика проверки, есть ли такая задача в мапе */
    public Task getTask(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Нет задачи с таким id");
        }
        return tasks.get(id);
    }

    /** Метод для получения эпика по id, реализована логика проверки, есть ли такой эпик в мапе */
    public Epic getEpic(int id) {
        if (!epics.containsKey(id)) {
            System.out.println("Нет эпика с таким id");
        }
        return epics.get(id);
    }

    /** Метод для получения подзадачи по id, реализована логика проверки, есть ли такая подзадача в мапе */
    public Subtask getSubtask(int id) {
        if (!subtasks.containsKey(id)) {
            System.out.println("Нет подзадачи с таким id");
        }
        return subtasks.get(id);
    }

    /** Метод получения всех задач */
    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasksArrayList = new ArrayList<>(tasks.values());
        return tasksArrayList;
    }

    /** Метод получения всех эпиков */
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicsArrayList = new ArrayList<>(epics.values());
        return epicsArrayList;
    }

    /** Метод получения всех подзадач */
    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtasksArrayList = new ArrayList<>(subtasks.values());
        return subtasksArrayList;
    }

    /** Метод получения всех подзадач конкретного эпика */
    public ArrayList<Subtask> getEpicSubtasks(int id) {
        ArrayList<Subtask> epicSubtasksIds = new ArrayList<>();
        for (int i : epics.get(id).getSubtasksId()) {
            epicSubtasksIds.add(subtasks.get(i));

        }
        return epicSubtasksIds;
    }

    /** Метод удаления всех задач */
    public void removeAllTasks() {
        tasks.clear();
    }

    /** Метод удаления всех эпиков. При удаленни всех эпиков удаляются их подзадачи */
    public void removeAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    /** Метод удаления всех подзадач. При удалении всех подзадач пересчитывается статус всех эпиков */
    public void removeAllSubtasks() {
        subtasks.clear();
        for (Epic ep : epics.values()) {
            ep.clearSubtasksId();
            calculateEpicStatus(ep);
        }
    }

    /** Метод удаления задачи по id. Реализована логика проверки наличия */
    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Задачи с таким id нет");
            return;
        }
    }

    /** Метод удаления эпика по id. Реализована логика проверки наличия. При удалении эпика удаляются все его подзадачи */
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            for (int subsId : getEpic(id).getSubtasksId()) {
                subtasks.remove(subsId);
            }
            getEpic(id).clearSubtasksId();
            epics.remove(id);
        } else {
            System.out.println("Эпика с таким id нет");
            return;
        }
    }

    /** Метод удаления подзадачи по id. Реализована логика проверки наличия. При удалении */
    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            int epicsId = subtasks.get(id).getEpicId();
            Epic epic = getEpic(epicsId);
            subtasks.remove(id);
            epic.removeSubtask(id);
            calculateEpicStatus(epic);
        } else {
            System.out.println("Подзадачи с таким id нет");
            return;
        }
    }

    /** Метод обновления задачи */
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    /** Метод обновления эпика */
    public void updateEpic(Epic epic) { //
        epics.put(epic.getId(), epic);
        calculateEpicStatus(epic);
    }

    /** Метод определения статуса эпика  */
    public void calculateEpicStatus(Epic epic) {
        Set<String> status = new HashSet<>();

        for (int i : epic.getSubtasksId()) {
            status.add(subtasks.get(i).getStatus());
        }
        if (epic.getSubtasksId().isEmpty() || status.size() == 1 && status.contains("NEW")) {
            epic.setStatus("NEW");
            return;
        } else if (status.size() == 1 && status.contains("DONE")) {
            epic.setStatus("DONE");
            return;
        } else {
            epic.setStatus("IN_PROGRESS");
        }
    }

    /** Метод обновления подзадачи. Реализиована логика проверки наличия. При обновлении подзадачи пересчитывается статус эпика */
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId()) && epics.containsKey(subtask.getEpicId())) {
            subtasks.put(subtask.getId(), subtask);
            calculateEpicStatus(getEpic(subtask.getEpicId()));
        } else {
            System.out.println("Подзадачи или эпика с таким id нет");
            return;
        }
    }

}
