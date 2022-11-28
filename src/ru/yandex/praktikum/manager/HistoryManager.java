/**
 * Интерфейс содержащий методы, которые д.б. у объекта менеджера истории
 *
 * @see ru.yandex.praktikum.manager.InMemoryHistoryManager
 */

package ru.yandex.praktikum.manager;

import ru.yandex.praktikum.tasks.Task;

import java.util.List;

public interface HistoryManager {

    List<Integer> getHistory();

    void add(int id);
}
