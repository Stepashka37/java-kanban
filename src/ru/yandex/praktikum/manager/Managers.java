/**
 * Утилитарный класс, в котором реализована логика по созданию менеджера задач и менеджера истории
 */
package ru.yandex.praktikum.manager;

import java.io.File;
import java.io.IOException;

public class Managers {

    /** Метод для создания нового объекта класса InMemoryTaskManager */
    public static TaskManager getDefault() throws IOException {
        TaskManager manager = FileBackedTasksManager.loadFromFile(new File("savedManager.csv"));
        return manager;
    }

    /** Метод для создания нового объекта класса InMemoryHistoryManager */
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
