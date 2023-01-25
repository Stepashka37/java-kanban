/**
 * Интерфейс содержащий методы, которые д.б. у объектов задач
 *
 * @see ru.yandex.praktikum.manager.InMemoryTaskManager
 */

package ru.yandex.praktikum.manager;

import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.Subtask;
import ru.yandex.praktikum.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    List<Task> getPrioritizedTasks();

    /** Метод для вывода истории просмотров
     * @return - список последних 10 просмотренных задач */
    List<Task> getHistory();

    /** Методы для создания задач. В качестве паараметра передается объект
     * @return - id созданной задачи */
    int createTask(Task task);

    /** Методы для создания эпиков. В качестве паараметра передается объект
     * @return - id созданного эпика */
    int createEpic(Epic epic);

    /**
     * Методы для создания подзадач. В качестве паараметра передается объект
     * Реализована логика проверки, есть ли эпик по указанному epicId
     *
     * @return - id созданной подзадачи
     */
    int createSubtask(Subtask subtask);

    /** Метод для получения задачи по id, реализована логика проверки, есть ли такая задача в мапе */
    Task getTask(int id);

    /** Метод для получения эпика по id, реализована логика проверки, есть ли такой эпик в мапе */
    Epic getEpic(int id);

    /** Метод для получения подзадачи по id, реализована логика проверки, есть ли такая подзадача в мапе */
    Subtask getSubtask(int id);

    /** Метод получения всех задач */
    ArrayList<Task> getTasks();

    /** Метод получения всех эпиков */
    ArrayList<Epic> getEpics();

    /** Метод получения всех подзадач */
    ArrayList<Subtask> getSubtasks();

    /**
     * Метод получения всех подзадач конкретного эпика
     */
    List<Subtask> getEpicSubtasks(int id);

    /** Метод удаления всех задач */
    void removeAllTasks();

    /** Метод удаления всех эпиков. При удаленни всех эпиков удаляются их подзадачи */
    void removeAllEpics();

    /** Метод удаления всех подзадач. При удалении всех подзадач пересчитывается статус всех эпиков */
    void removeAllSubtasks();

    /** Метод удаления задачи по id. Реализована логика проверки наличия */
    void deleteTask(int id);

    /** Метод удаления эпика по id. Реализована логика проверки наличия. При удалении эпика удаляются все его подзадачи */
    void deleteEpic(int id);

    /** Метод удаления подзадачи по id. Реализована логика проверки наличия. При удалении */
    void deleteSubtask(int id);

    /** Метод обновления задачи */
    void updateTask(Task task);

    /** Метод обновления эпика */
    void updateEpic(Epic epic);

    /** Метод обновления подзадачи. Реализиована логика проверки наличия. При обновлении подзадачи пересчитывается статус эпика */
    void updateSubtask(Subtask subtask);
}
