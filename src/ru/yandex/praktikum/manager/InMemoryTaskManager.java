/**
 * Класс описывающий основную логику Трекера Задач
 */

package ru.yandex.praktikum.manager;

import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.Subtask;
import ru.yandex.praktikum.tasks.Task;
import ru.yandex.praktikum.tasks.TaskStatus;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    /** @tasks - мапа для хранения задач
     * @subtasks - мапа для хранения подзадач
     * @epics - мапа для хранения эпиков
     * @genId = генерируемый id
     * @historyManager - объект класса InMemoryHistoryManager типа HistoryManager
     *  */
    private int genId = 0;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HistoryManager historyManager = Managers.getDefaultHistory();
    private List<Task> history = new ArrayList<>();


    /** Метод для вывода истории просмотров
     * @return - список последних 10 просмотренных задач */
    public List<Task> getHistory() {

        for (Integer i : historyManager.getHistory()) {
            if (tasks.containsKey(i)) {
                history.add(tasks.get(i));
            } else if (epics.containsKey(i)) {
                history.add(epics.get(i));
            } else if (subtasks.containsKey(i)) {
                history.add(subtasks.get(i));
            } else {
                System.out.println("Объекта с таким id нет");
            }
        }
            return history;
        }


    /** Метод для создания задач. В качестве паараметра передается объект
     * @return - id созданной задачи */
    @Override
    public int createTask(Task task) {
        final int id = ++genId;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    /** Метод для создания эпиков. В качестве паараметра передается объект
     * @return - id созданного эпика */
    @Override
    public int createEpic(Epic epic) {
        final int id = ++genId;
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    /**
     * Метод для создания подзадач. В качестве паараметра передается объект
     * Реализована логика проверки, есть ли эпик по указанному epicId
     *
     * @return - id созданной подзадачи
     */
    @Override
    public int createSubtask(Subtask subtask) {
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
    @Override
    public Task getTask(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Нет задачи с таким id");
        }
        final Task task = tasks.get(id);
        historyManager.add(id);
        return task;
    }

    /** Метод для получения эпика по id, реализована логика проверки, есть ли такой эпик в мапе */
    @Override
    public Epic getEpic(int id) {
        if (!epics.containsKey(id)) {
            System.out.println("Нет эпика с таким id");
        }
        final Epic epic = epics.get(id);
        historyManager.add(id);
        return epic;
    }

    /** Метод для получения подзадачи по id, реализована логика проверки, есть ли такая подзадача в мапе */
    @Override
    public Subtask getSubtask(int id) {
        if (!subtasks.containsKey(id)) {
            System.out.println("Нет подзадачи с таким id");
        }
        final Subtask subtask = subtasks.get(id);
        historyManager.add(id);
        return subtask;
    }

    /** Метод получения всех задач */
    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasksArrayList = new ArrayList<>(tasks.values());
        return tasksArrayList;
    }

    /** Метод получения всех эпиков */
    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicsArrayList = new ArrayList<>(epics.values());
        return epicsArrayList;
    }

    /** Метод получения всех подзадач */
    @Override
    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtasksArrayList = new ArrayList<>(subtasks.values());
        return subtasksArrayList;
    }

    /** Метод получения всех подзадач конкретного эпика */
    @Override
    public ArrayList<Subtask> getEpicSubtasks(int id) {
        ArrayList<Subtask> epicSubtasksIds = new ArrayList<>();
        for (int i : epics.get(id).getSubtasksId()) {
            epicSubtasksIds.add(subtasks.get(i));

        }
        return epicSubtasksIds;
    }

    /** Метод удаления всех задач */
    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    /** Метод удаления всех эпиков. При удаленни всех эпиков удаляются их подзадачи */
    @Override
    public void removeAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    /** Метод удаления всех подзадач. При удалении всех подзадач пересчитывается статус всех эпиков */
    @Override
    public void removeAllSubtasks() {
        subtasks.clear();
        for (Epic ep : epics.values()) {
            ep.clearSubtasksId();
            calculateEpicStatus(ep);
        }
    }

    /** Метод удаления задачи по id. Реализована логика проверки наличия */
    @Override
    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Задачи с таким id нет");
            return;
        }
    }

    /** Метод удаления эпика по id. Реализована логика проверки наличия. При удалении эпика удаляются все его подзадачи */
    @Override
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
    @Override
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
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    /** Метод обновления эпика */
    @Override
    public void updateEpic(Epic epic) { //
        epics.put(epic.getId(), epic);
        calculateEpicStatus(epic);
    }


    /** Метод определения статуса эпика  */
    public void calculateEpicStatus(Epic epic) {
        Set<TaskStatus> status = new HashSet<>();

        for (int i : epic.getSubtasksId()) {
            status.add(subtasks.get(i).getStatus());
        }
        if (epic.getSubtasksId().isEmpty() || status.size() == 1 && status.contains(TaskStatus.NEW)) {
            epic.setStatus(TaskStatus.NEW);
            return;
        } else if (status.size() == 1 && status.contains(TaskStatus.DONE)) {
            epic.setStatus(TaskStatus.DONE);
            return;
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    /** Метод обновления подзадачи. Реализиована логика проверки наличия. При обновлении подзадачи пересчитывается статус эпика */
    @Override
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
