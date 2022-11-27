/**
 * Основной метод приложения. В нем имитированы действия пользователя:
 * 1) Создание 2 задач, 4 эпиков, 7 подзадач.
 * 2) Далее просмотр всех созданных объектов через методы get()
 * 3) Вызывается метод getHistory() и на нем убеждаемся, что метод возвращает список из 10 последних просмотренных задач
 * 4) Затем идут тесты, которые были реализованы еще в ТЗ3 по вызову различных методов, просмотру списков задач, удалении
 * и обновлении их статусов
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
        Subtask subtask3 = new Subtask("Subtask 3", "Subtask 3, description", TaskStatus.NEW, epicId2);
        Subtask subtask4 = new Subtask("Subtask 4", "Subtask 4, description", TaskStatus.NEW, epicId1);
        Subtask subtask5 = new Subtask("Subtask 5", "Subtask 5, description", TaskStatus.DONE, epicId2);
        Subtask subtask6 = new Subtask("Subtask 6", "Subtask 6, description", TaskStatus.NEW, epicId2);
        Subtask subtask7 = new Subtask("Subtask 7", "Subtask 7, description", TaskStatus.NEW, epicId2);
        final Integer subtaskId1 = manager.createSubtask(subtask1);
        final Integer subtaskId2 = manager.createSubtask(subtask2);
        final Integer subtaskId3 = manager.createSubtask(subtask3);
        final Integer subtaskId4 = manager.createSubtask(subtask4);
        final Integer subtaskId5 = manager.createSubtask(subtask5);
        final Integer subtaskId6 = manager.createSubtask(subtask6);
        final Integer subtaskId7 = manager.createSubtask(subtask7);

        Epic epic3 = new Epic("Epic 3", "Epic 3, description", TaskStatus.NEW);
        Epic epic4 = new Epic("Epic 4", "Epic 4, description", TaskStatus.NEW);
        final int epicId3 = manager.createEpic(epic3);
        final int epicId4 = manager.createEpic(epic4);


        manager.getTask(1);
        manager.getTask(2);
        manager.getEpic(3);
        manager.getEpic(4);
        manager.getSubtask(5);
        manager.getSubtask(6);
        manager.getSubtask(7);
        manager.getSubtask(8);
        manager.getSubtask(9);
        manager.getSubtask(10);
        manager.getSubtask(11);
        manager.getEpic(12);
        manager.getEpic(13);

        List<Task> history = manager.getHistory();
        for (Task task : history) {
            System.out.println(task);
        }

        System.out.println(manager.getTasks());
        System.out.println();
        System.out.println(manager.getEpics());
        System.out.println();
        System.out.println(manager.getSubtasks());
        System.out.println();
        System.out.println(manager.getEpicSubtasks(epicId1));
        System.out.println();
        System.out.println(manager.getEpicSubtasks(epicId2));
        System.out.println();

        manager.updateTask(new Task(taskId2, "Task 2", "Task 2, description", TaskStatus.DONE));
        System.out.println(manager.getTask(2));
        System.out.println();
        epic2.setName("Epic 2.1");
        epic2.setStatus(TaskStatus.DONE); // проверка, что при ручном изменении статуса, итоговый статус эпика будет рассчитываться по статусу его подзадач
        manager.updateEpic(epic2);
        System.out.println(manager.getEpic(epicId2));
        System.out.println(manager.getEpicSubtasks(epicId2));
        System.out.println();

        manager.updateSubtask(new Subtask(subtaskId3, "Subtask 3", "Subtask 3, description", TaskStatus.DONE, epicId2));
        System.out.println(manager.getEpic(epicId2));
        System.out.println(manager.getEpicSubtasks(epicId2));
        System.out.println();

        manager.deleteSubtask(subtaskId2);
        System.out.println(manager.getEpic(epicId1));
        System.out.println(manager.getSubtasks());
        System.out.println();

        manager.deleteEpic(epicId2);
        System.out.println(manager.getEpic(epicId2));
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
        System.out.println();

    }
}
