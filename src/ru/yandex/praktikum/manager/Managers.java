/**
 * Утилитарный класс, в котором реализована логика по созданию менеджера задач и менеджера истории
 */
package ru.yandex.praktikum.manager;

public class Managers {

    /** Метод для создания нового объекта класса InMemoryTaskManager */
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    /** Метод для создания нового объекта класса InMemoryHistoryManager */
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
