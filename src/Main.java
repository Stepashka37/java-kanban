/**
 * Основной класс приложения. В нем имитированы действия пользователя:
 * 1) Создание 2 задач, 2 эпиков - в одном 3 позадачи, в другом 0
 * 2) Проверка на отсутствие повторов в истории
 * 3) Проверка на отствутвие удаленных задач в истории
 * 4) Проверка на отсутствие в истори подзадач уже удаленного эпика
 */

import ru.yandex.praktikum.manager.FileBackedTasksManager;
import ru.yandex.praktikum.manager.InMemoryTaskManager;
import ru.yandex.praktikum.manager.Managers;
import ru.yandex.praktikum.manager.TaskManager;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.Subtask;
import ru.yandex.praktikum.tasks.Task;
import ru.yandex.praktikum.tasks.TaskStatus;
import java.io.IOException;
import java.time.LocalDateTime;


public class Main {

    /**
     * Метод для имитации записи данных из менеджера в файл и воссоздания менеджера из файла
     */
    public static void main(String[] args) throws IOException {

        TaskManager toFile = Managers.getDefault();
        //InMemoryTaskManager toFile = new InMemoryTaskManager();
        Task task1 = new Task("Task 1", "Task 1 description", TaskStatus.NEW, 120, LocalDateTime.of(2000,4,1,5,00)  );
        Task task2 = new Task("Task 2", "Task 2 description", TaskStatus.IN_PROGRESS, 120, LocalDateTime.of(2000,4,1,3,0));
        Task task3 = new Task("Task 3", "Task 3 description", TaskStatus.IN_PROGRESS);

        final int taskId1 = toFile.createTask(task1);
        final int taskId2 = toFile.createTask(task2);
        final int taskId3 = toFile.createTask(task3);

        Epic epic1 = new Epic("Epic 1",  "Epic 1 description", TaskStatus.NEW);
        Epic epic2 = new Epic("Epic 2", "Epic 2 description", TaskStatus.NEW, 120, LocalDateTime.of(2000,12,1,0,0));
        final int epicId1 = toFile.createEpic(epic1);
        final int epicId2 = toFile.createEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", TaskStatus.NEW, epicId1, 120, LocalDateTime.of(2000,11,1,2,0));
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", TaskStatus.DONE, epicId1, 120, LocalDateTime.of(2000,11,1,4,0));
        Subtask subtask3 = new Subtask("Subtask 3", "Subtask 3 description", TaskStatus.NEW, epicId1);

        final int subtaskId1 = toFile.createSubtask(subtask1);
        final int subtaskId2 = toFile.createSubtask(subtask2);
        final int subtaskId3 = toFile.createSubtask(subtask3);


        for (Task prioritizedTask : toFile.getPrioritizedTasks()) {
            System.out.println(prioritizedTask);

        }
        System.out.println();
        //toFile.getEpic(4);
        //System.out.println(toFile.getEpic(4).getStartTime());
        //System.out.println(toFile.getEpic(4).getEndTime());
        //System.out.println(toFile.getEpic(4).getDuration());
        //toFile.getEpic(5);
        //toFile.getEpic(4);
        //toFile.getTask(2);
        //toFile.getTask(3);
        //toFile.getTask(1);
        toFile.getEpic(4);
        toFile.getSubtask(7);
        toFile.getSubtask(6);
        toFile.getSubtask(8);
        toFile.getEpic(4);

        toFile.removeAllSubtasks();
        //toFile.removeAllTasks();
        System.out.println();
        System.out.println(toFile.getHistory());
        System.out.println(toFile.getHistory());



    }

}

