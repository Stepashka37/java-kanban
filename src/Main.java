/**
 * Основной класс приложения. В нем имитированы действия пользователя:
 * 1) Создание 2 задач, 2 эпиков - в одном 3 позадачи, в другом 0
 * 2) Проверка на отсутствие повторов в истории
 * 3) Проверка на отствутвие удаленных задач в истории
 * 4) Проверка на отсутствие в истори подзадач уже удаленного эпика
 */

import ru.yandex.praktikum.manager.Managers;
import ru.yandex.praktikum.manager.TaskManager;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.Subtask;
import ru.yandex.praktikum.tasks.Task;
import ru.yandex.praktikum.tasks.TaskStatus;

import java.util.List;


public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();


        Task task1 = new Task("Task 1", "Task 1, description", TaskStatus.NEW);
        Task task2 = new Task("Task 2", "Task 2, description", TaskStatus.IN_PROGRESS);
        final int taskId1 = manager.createTask(task1);
        final int taskId2 = manager.createTask(task2);


        Epic epic1 = new Epic("Epic 1", "Epic 1, description", TaskStatus.NEW);
        Epic epic2 = new Epic("Epic 2", "Epic 2, description", TaskStatus.NEW);
        final int epicId1 = manager.createEpic(epic1);
        final int epicId2 = manager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1, description", TaskStatus.NEW, epicId1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2, description", TaskStatus.DONE, epicId1);
        Subtask subtask3 = new Subtask("Subtask 3", "Subtask 3, description", TaskStatus.NEW, epicId1);

        final Integer subtaskId1 = manager.createSubtask(subtask1);
        final Integer subtaskId2 = manager.createSubtask(subtask2);
        final Integer subtaskId3 = manager.createSubtask(subtask3);

        /** Проверка на то, что в истории не будут показаны повторы  */
        manager.getTask(1);
        manager.getSubtask(5);
        manager.getSubtask(6);
        manager.getSubtask(7);
        manager.getTask(1);
        manager.getSubtask(6);

        List<Task> history1 = manager.getHistory();
        for (Task task : history1) {
            System.out.println(task);
        }

        /** Проверка на то, что при удалении задачи она не будет показана в истории  */
        System.out.println();
        manager.getHistory().clear();
        manager.getTask(1);
        manager.getSubtask(5);
        manager.getSubtask(6);
        manager.getSubtask(7);
        manager.deleteTask(1);

        List<Task> history2 = manager.getHistory();
        for (Task task : history2) {
            System.out.println(task);
        }

        /** Проверка на то, что при удалении эпика с 3 подазадчами из истории удалятся как эпик, так и подзадачи*/
        System.out.println();
        manager.getHistory().clear();
        manager.getEpic(4);
        manager.getEpic(3);
        manager.getSubtask(5);
        manager.getSubtask(6);
        manager.getSubtask(7);
        manager.deleteEpic(3);

        List<Task> history3 = manager.getHistory();
        for (Task task : history3) {
            System.out.println(task);
        }




    }
}
