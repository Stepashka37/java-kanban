/**
 * Класс описывающий основную логику Трекера Задач
 */

package ru.yandex.praktikum.manager;

import ru.yandex.praktikum.tasks.*;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    /**
     * @tasks - мапа для хранения задач
     * @subtasks - мапа для хранения подзадач
     * @epics - мапа для хранения эпиков
     * @genId = генерируемый id
     * @historyManager - объект класса InMemoryHistoryManager типа HistoryManager
     */
    protected int genId = 0;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    Comparator<Task> taskComparator = Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getId);
    TreeSet<Task> prioritizedTasks = new TreeSet<>(taskComparator);
    HistoryManager historyManager = Managers.getDefaultHistory();
    List<Task> history = new ArrayList<>();


    /**
     * Метод добавления таски в prioritizedTasks. Реализована логика добавления тасок без даты и длительности,
     * а также сабтасок, если в prioritizedTask только 1 эпик
     */

    private boolean checkAndSortTasks(Task task) {
        if (prioritizedTasks.isEmpty() || task.getStartTime() == null) {
            prioritizedTasks.add(task);
            return true;
        }
        if (prioritizedTasks.size() == 1 && prioritizedTasks.first().getType().equals(TaskType.EPIC)  && task.getType().equals(TaskType.SUBTASK)) {
            prioritizedTasks.add(task);
            return true;
        }

        Set<Task> tasksWithDate = prioritizedTasks.stream()
                .filter(t -> t.getStartTime() != null)
                .collect(Collectors.toSet());

        for (Task prioritizedTask : tasksWithDate) {
            if (prioritizedTask.getId() == task.getId()) {
                prioritizedTasks.remove(prioritizedTask);
                continue;
            }
            if (!task.getStartTime().isBefore(prioritizedTask.getEndTime()) || !task.getEndTime().isAfter(prioritizedTask.getStartTime())) {
                prioritizedTasks.add(task);
            } else {
                System.out.println("Задача с id " + task.getId() + " накладывается на  задачу c id " + prioritizedTask.getId() + " по времени!");
                return false;
            }

        }
        return true;
    }

    /**
     * Метод возвращающий список задач в порядке приоритета
     */
    @Override
    public List<Task> getPrioritizedTasks() {
        List<Task> tasksInList = new ArrayList<>(prioritizedTasks);
        return tasksInList;
    }


    @Override

    /** Метод для вывода истории просмотров
     * @return - список последних 10 просмотренных задач */
    public List<Task> getHistory() {

        for (Task task : historyManager.getHistory()) {
            int taskId = task.getId();
            if (tasks.containsKey(taskId)) {
                history.add(task);
            } else if (epics.containsKey(taskId)) {
                history.add(task);
            } else if (subtasks.containsKey(taskId)) {
                history.add(task);
            } else {
                System.out.println("Объекта с id " + taskId + " нет или он был удален");
            }
        }
        return history;
    }


    /**
     * Метод для создания задач. В качестве паараметра передается объект
     *
     * @return - id созданной задачи
     */
    @Override
    public int createTask(Task task) {
        final int id = ++genId;
        task.setId(id);
        if (checkAndSortTasks(task)) {

            tasks.put(id, task);

            return id;
        } else {
            return -1;
        }
    }

    /**
     * Метод для создания эпиков. В качестве паараметра передается объект
     *
     * @return - id созданного эпика
     */
    @Override
    public int createEpic(Epic epic) {
        final int id = ++genId;
        epic.setId(id);
        if (checkAndSortTasks(epic)) {

            epics.put(id, epic);


            return id;
        } else {

            return -1;
        }
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
            if (checkAndSortTasks(subtask)) {

                subtasks.put(id, subtask);
                epic.addSubtaskId(subtask);
                calculateEpicStatus(epic);
                //checkAndSortTasks(epic); //ДОБАВИЛ СТРОКУ
                return id;
            } else {
                return -1;
            }
        } else {
            System.out.println("Такого эпика нет!");
            return -1;
        }
    }


    /**
     * Метод для получения задачи по id, реализована логика проверки, есть ли такая задача в мапе
     */
    @Override
    public Task getTask(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Нет задачи с таким id");
            return null;
        }
        final Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    /**
     * Метод для получения эпика по id, реализована логика проверки, есть ли такой эпик в мапе
     */
    @Override
    public Epic getEpic(int id) {
        if (!epics.containsKey(id)) {
            System.out.println("Нет эпика с таким id");
            return null;
        }
        final Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    /**
     * Метод для получения подзадачи по id, реализована логика проверки, есть ли такая подзадача в мапе
     */
    @Override
    public Subtask getSubtask(int id) {
        if (!subtasks.containsKey(id)) {
            System.out.println("Нет подзадачи с таким id");
            return null;
        }
        final Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    /**
     * Метод получения всех задач
     */
    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasksArrayList = new ArrayList<>(tasks.values());
        return tasksArrayList;
    }

    /**
     * Метод получения всех эпиков
     */
    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicsArrayList = new ArrayList<>(epics.values());
        return epicsArrayList;
    }

    /**
     * Метод получения всех подзадач
     */
    @Override
    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtasksArrayList = new ArrayList<>(subtasks.values());
        return subtasksArrayList;
    }

    /**
     * Метод получения всех подзадач конкретного эпика
     */
    @Override
    public List<Subtask> getEpicSubtasks(int id) {
        if (epics.containsKey(id)) {

            return epics.get(id).getSubtasksId();
        }
        return null;
    }

    /**
     * Метод удаления всех задач
     */
    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    /**
     * Метод удаления всех эпиков. При удаленни всех эпиков удаляются их подзадачи
     */
    @Override
    public void removeAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    /**
     * Метод удаления всех подзадач. При удалении всех подзадач пересчитывается статус всех эпиков
     */
    @Override
    public void removeAllSubtasks() {
        subtasks.clear();
        for (Epic ep : epics.values()) {
            ep.clearSubtasksId();
            calculateEpicStatus(ep);
        }
    }

    /**
     * Метод удаления задачи по id. Реализована логика проверки наличия
     */
    @Override
    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Задачи с таким id нет");
            return;
        }
    }

    /**
     * Метод удаления эпика по id. Реализована логика проверки наличия. При удалении эпика удаляются все его подзадачи
     */
    @Override
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            for (Subtask subtask : getEpic(id).getSubtasksId()) {
                subtasks.remove(subtask.getId());
                historyManager.remove(subtask.getId());
            }
            epics.get(id).clearSubtasksId();
            epics.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Эпика с таким id нет");
            return;
        }
    }

    /**
     * Метод удаления подзадачи по id. Реализована логика проверки наличия. При удалении
     */
    @Override
    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            int epicsId = subtasks.get(id).getEpicId();
            Epic epic = epics.get(epicsId); //тут поправил
            epic.removeSubtask(subtasks.get(id));
            subtasks.remove(id);
            historyManager.remove(id);
            calculateEpicStatus(epic);
        } else {
            System.out.println("Подзадачи с таким id нет");
            return;
        }
    }


    /**
     * Метод обновления задачи
     */
    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            if (checkAndSortTasks(task)) {
                //checkAndSortTasks(task);
                tasks.put(task.getId(), task);
            } else {
                return;
            }

        } else {
            System.out.println("Задачи с таким id нет");
        }
    }

    /**
     * Метод обновления эпика
     */
    @Override
    public void updateEpic(Epic epic) { //
        if (epics.containsKey(epic.getId()) && checkAndSortTasks(epic)) {
            epics.put(epic.getId(), epic);
            calculateEpicStatus(epic);
        } else {
            System.out.println("Эпика с таким id нет");
        }
    }

    /**
     * Метод определения статуса эпика
     */
    public void calculateEpicStatus(Epic epic) {
        Set<TaskStatus> status = new HashSet<>();

        for (Subtask subtask : epic.getSubtasksId()) {

            status.add(subtasks.get(subtask.getId()).getStatus());
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

    /**
     * Метод обновления подзадачи. Реализиована логика проверки наличия. При обновлении подзадачи пересчитывается статус эпика
     */
    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId()) && epics.containsKey(subtask.getEpicId()) && checkAndSortTasks(subtask)) {
            subtasks.put(subtask.getId(), subtask);
            calculateEpicStatus(getEpic(subtask.getEpicId()));

        } else {
            System.out.println("Подзадачи или эпика с таким id нет");
            return;
        }
    }

    public void setGenId(int genId) {
        this.genId = genId;
    }
}
