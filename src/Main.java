/**
 * Основной класс приложения. В нем имитированы действия пользователя:
 * 1) Создание 2 задач, 2 эпиков - в одном 3 позадачи, в другом 0
 * 2) Проверка на отсутствие повторов в истории
 * 3) Проверка на отствутвие удаленных задач в истории
 * 4) Проверка на отсутствие в истори подзадач уже удаленного эпика
 */

import ru.yandex.praktikum.manager.FileBackedTasksManager;
import ru.yandex.praktikum.manager.Managers;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.Subtask;
import ru.yandex.praktikum.tasks.Task;
import ru.yandex.praktikum.tasks.TaskStatus;
import java.io.IOException;



public class Main {

    /**
     * Метод для имитации записи данных из менеджера в файл и воссоздания менеджера из файла
     */
    public static void main(String[] args) throws IOException {

        FileBackedTasksManager toFile = Managers.getDefault();

        Task task1 = new Task("Task 1", "Task 1 description", TaskStatus.NEW);
        Task task2 = new Task("Task 2", "Task 2 description", TaskStatus.IN_PROGRESS);
        final int taskId1 = toFile.createTask(task1);
        final int taskId2 = toFile.createTask(task2);

        Epic epic1 = new Epic("Epic 1",  "Epic 1 description", TaskStatus.NEW);
        Epic epic2 = new Epic("Epic 2", "Epic 2 description", TaskStatus.NEW);
        final int epicId1 = toFile.createEpic(epic1);
        final int epicId2 = toFile.createEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", TaskStatus.NEW, epicId1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", TaskStatus.DONE, epicId1);
        Subtask subtask3 = new Subtask("Subtask 3", "Subtask 3 description", TaskStatus.NEW, epicId1);

        final int subtaskId1 = toFile.createSubtask(subtask1);
        final int subtaskId2 = toFile.createSubtask(subtask2);
        final int subtaskId3 = toFile.createSubtask(subtask3);

        toFile.getTask(1);
        toFile.getSubtask(5);
        toFile.getSubtask(6);
        toFile.getSubtask(7);
        toFile.getSubtask(7);
        toFile.getSubtask(5);

        System.out.println(toFile.getTasks());
        System.out.println();
        System.out.println(toFile.getEpics());
        System.out.println();
        System.out.println(toFile.getSubtasks());
        System.out.println();
        System.out.println(toFile.getHistory());
    }

}

