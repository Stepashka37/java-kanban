/**
 * Класс описывающий логику менеджера истории
 *
 * @see ru.yandex.praktikum.manager.InMemoryTaskManager
 */

package ru.yandex.praktikum.manager;

import ru.yandex.praktikum.tasks.Task;

import java.util.ArrayList;
import java.util.List;

/** @history - список 10 последних просмотренных задач */
public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> history = new ArrayList<>();

    /** Метод для вывода списка просмотренных задач */
    @Override
    public List<Task> getHistory() {
        return history;
    }

    /** Метод для добавления задачи в список просмотренных, внутри реализована логика хранения 10 последних просмотренных задач */
    @Override
    public void add(Task task) {
        history.add(task);
        int listSize = 10;
        if (history.size() > listSize) {
            history.remove(0);
        }
    }

}
