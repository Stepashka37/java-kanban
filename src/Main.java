/** Основной метод приложения. В нем имитированы действия пользователя:
 * 1) Создание 2 задач, 2 эпиков, в одном 2 подзадачи, в другом 1 подзадача.
 * 2) Печать всех задач, эпиков, подзадач
 * 3) Обновление задачи, подзадачи и эпика
 * 4) Удаление подзадачи, эпика */

import ru.yandex.praktikum.Manager;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.Subtask;
import ru.yandex.praktikum.tasks.Task;


public class Main {

    public static void main(String[] args) {

        Manager manager = new Manager();

        Task task1 = new Task("Task 1", "Task 1, description", "NEW");
        Task task2 = new Task("Task 2", "Task 2, description", "IN_PROGRESS");
        final int taskId1 = manager.createTask(task1);
        final int taskId2 = manager.createTask(task2);


        Epic epic1 = new Epic("Epic 1", "Epic 1, description", "NEW");
        Epic epic2 = new Epic("Epic 2", "Epic 2, description", "NEW");
        final int epicId1 = manager.createEpic(epic1);
        final int epicId2 = manager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1, description", "NEW", epicId1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2, description", "DONE", epicId1);
        Subtask subtask3 = new Subtask("Subtask 3", "Subtask 3, description", "NEW", epicId2);
        final Integer subtaskId1 = manager.createSubtask(subtask1);
        final Integer subtaskId2 = manager.createSubtask(subtask2);
        final Integer subtaskId3 = manager.createSubtask(subtask3);

        System.out.println(manager.getTasks());
        System.out.println();
        System.out.println(manager.getEpics());
        System.out.println();
        System.out.println(manager.getSubtasks());
        System.out.println();
        System.out.println(manager.getEpicSubtasks(epicId1));
        System.out.println();
        System.out.println(manager.getEpicSubtasks(epicId2));
        System .out.println();

        manager.updateTask(new Task(taskId2, "Task 2", "Task 2, description", "DONE"));
        System.out.println(manager.getTask(2));
        System.out.println();
        epic2.setName("Epic 2.1");
        epic2.setStatus("DONE"); // проверка, что при ручном изменении статуса, итоговый статус эпика будет рассчитываться по статусу его подзадач
        manager.updateEpic(epic2);
        System.out.println(manager.getEpic(epicId2));
        System.out.println(manager.getEpicSubtasks(epicId2));
        System.out.println();

        manager.updateSubtask(new Subtask(subtaskId3, "Subtask 3", "Subtask 3, description", "DONE", epicId2));
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
