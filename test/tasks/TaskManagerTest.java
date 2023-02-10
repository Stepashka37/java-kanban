package tasks;

import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.manager.TaskManager;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.Subtask;
import ru.yandex.praktikum.tasks.Task;
import ru.yandex.praktikum.tasks.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected Task task;

    protected Epic epic;

    protected Subtask subtask;

    public void initTasksWithoutDate() {
        Task task1 = new Task("Task 1", "Task 1 description", TaskStatus.NEW);
        Task task2 = new Task("Task 2", "Task 2 description", TaskStatus.IN_PROGRESS);
        final int taskId1 = taskManager.createTask(task1);
        final int taskId2 = taskManager.createTask(task2);

        Epic epic1 = new Epic("Epic 1", "Epic 1 description", TaskStatus.NEW);
        Epic epic2 = new Epic("Epic 2", "Epic 2 description", TaskStatus.NEW);
        final int epicId1 = taskManager.createEpic(epic1);
        final int epicId2 = taskManager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", TaskStatus.NEW, epicId1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", TaskStatus.NEW, epicId1);
        Subtask subtask3 = new Subtask("Subtask 3", "Subtask 3 description", TaskStatus.NEW, epicId1);
        Subtask subtask4 = new Subtask("Subtask 4", "Subtask 4 description", TaskStatus.IN_PROGRESS, epicId2);

        final int subtaskId1 = taskManager.createSubtask(subtask1);
        final int subtaskId2 = taskManager.createSubtask(subtask2);
        final int subtaskId3 = taskManager.createSubtask(subtask3);
        final int subtaskId4 = taskManager.createSubtask(subtask4);
    }

    public void initTasksWithDate() {
        Task task1 = new Task("Task 1", "Task 1 description", TaskStatus.NEW, 120, LocalDateTime.of(2000, 4, 1, 5, 0));
        Task task2 = new Task("Task 2", "Task 2 description", TaskStatus.IN_PROGRESS, 120, LocalDateTime.of(2000, 4, 1, 3, 0));


        final int taskId1 = taskManager.createTask(task1);
        final int taskId2 = taskManager.createTask(task2);


        Epic epic1 = new Epic("Epic 1", "Epic 1 description", TaskStatus.NEW);
        Epic epic2 = new Epic("Epic 2", "Epic 2 description", TaskStatus.NEW);
        final int epicId1 = taskManager.createEpic(epic1);
        final int epicId2 = taskManager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", TaskStatus.NEW, epicId1, 120, LocalDateTime.of(2000, 11, 1, 2, 0));
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", TaskStatus.DONE, epicId1, 120, LocalDateTime.of(2000, 11, 1, 0, 0));
        Subtask subtask3 = new Subtask("Subtask 3", "Subtask 3 description", TaskStatus.NEW, epicId1);
        Subtask subtask4 = new Subtask("Subtask 4", "Subtask 4 description", TaskStatus.IN_PROGRESS, epicId2);

        final int subtaskId1 = taskManager.createSubtask(subtask1);
        final int subtaskId2 = taskManager.createSubtask(subtask2);
        final int subtaskId3 = taskManager.createSubtask(subtask3);
        final int subtaskId4 = taskManager.createSubtask(subtask4);
    }


    @Test
    public void createTaskTest() {
        Task task = new Task("Task 1", "Task 1 description", TaskStatus.NEW);
        final int taskId = taskManager.createTask(task);
        assertEquals(task, taskManager.getTask(taskId));
        assertEquals(1, taskManager.getTasks().size());
        assertEquals(1, task.getId());

    }

    @Test
    public void createEpicTest() {
        Epic epic = new Epic("Epic 1", "Epic 1 description", TaskStatus.NEW);
        final int epicId = taskManager.createEpic(epic);
        assertEquals(epic, taskManager.getEpic(epicId));
        assertEquals(1, taskManager.getEpics().size());
        assertEquals(0, taskManager.getEpicSubtasks(epicId).size());
        assertEquals(1, epic.getId());
    }

    @Test
    public void createSubtaskTest() {
        Epic epic = new Epic("Epic 1", "Epic 1 description", TaskStatus.NEW);
        final int epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Subtask 1 description", TaskStatus.NEW, epicId);
        final int subtaskId = taskManager.createSubtask(subtask);
        assertNotNull(taskManager.getEpic(epicId));
        assertEquals(subtask, taskManager.getSubtask(subtaskId));
        assertEquals(1, taskManager.getSubtasks().size());
        assertEquals(2, subtask.getId());
    }

    @Test
    public void createSubtaskWithWrongEpicIdTest() {
        Epic epic = new Epic("Epic 1", "Epic 1 description", TaskStatus.NEW);
        final int epicId = taskManager.createEpic(epic);
        final int notExistedId = 3;
        Subtask subtask = new Subtask("Subtask 1", "Subtask 1 description", TaskStatus.NEW, 3);
        assertEquals(-1, taskManager.createSubtask(subtask));
    }

    @Test
    public void createEpicAndSubtasksTest() {
        Epic epic = new Epic("Epic 1", "Epic 1 description", TaskStatus.NEW);
        final int epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Subtask 1 description", TaskStatus.NEW, epicId);
        final int subtaskId = taskManager.createSubtask(subtask);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", TaskStatus.NEW, epicId);
        final int subtask2Id = taskManager.createSubtask(subtask2);
        Epic epicWithSubtasks = taskManager.getEpic(epicId);
        assertEquals(epic, epicWithSubtasks);
    }

    @Test
    public void getTaskTest() {
        initTasksWithoutDate();
        Task task = new Task(1, "Task 1", "Task 1 description", TaskStatus.NEW);
        assertEquals(task, taskManager.getTask(1));
    }

    @Test
    public void getTaskWithWrongIdTest() {
        initTasksWithoutDate();
        assertNull(taskManager.getTask(10));
    }

    @Test
    public void getTaskFromEmptyListTest() {
        assertNull(taskManager.getTask(1));
    }

    @Test
    public void getEpicTest() {
        initTasksWithoutDate();
        Epic epic1 = new Epic(3, "Epic 1", "Epic 1 description", TaskStatus.NEW);
        assertEquals(epic1, taskManager.getEpic(3));
    }

    @Test
    public void getEpicWithWrongIdTest() {
        initTasksWithoutDate();
        assertNull(taskManager.getEpic(10));
    }

    @Test
    public void getEpicFromEmptyListTest() {
        assertNull(taskManager.getEpic(3));
    }

    @Test
    public void getSubtaskTest() {
        initTasksWithoutDate();
        Subtask subtask1 = new Subtask(5, "Subtask 1", "Subtask 1 description", TaskStatus.NEW, 3);
        assertEquals(subtask1, taskManager.getSubtask(5));
    }

    @Test
    public void getSubtaskWithWrongIdTest() {
        initTasksWithoutDate();
        assertNull(taskManager.getSubtask(10));
    }

    @Test
    public void getSubtaskFromEmptyListTest() {
        assertNull(taskManager.getSubtask(5));
    }

    @Test
    public void getTasksTest() {
        Task task1 = new Task("Task 1", "Task 1 description", TaskStatus.NEW);
        Task task2 = new Task("Task 2", "Task 2 description", TaskStatus.IN_PROGRESS);
        final int taskId1 = taskManager.createTask(task1);
        final int taskId2 = taskManager.createTask(task2);
        Task[] tasks = {task1, task2};
        assertArrayEquals(tasks, taskManager.getTasks().toArray());
    }


    @Test
    public void getNoTasksTest() {
        List<Task> tasks = taskManager.getTasks();
        assertEquals(0, tasks.size());
    }


    @Test
    public void getEpicsTest() {
        Epic epic1 = new Epic("Epic 1", "Epic 1 description", TaskStatus.NEW);
        Epic epic2 = new Epic("Epic 2", "Epic 2 description", TaskStatus.NEW);
        final int epicId1 = taskManager.createEpic(epic1);
        final int epicId2 = taskManager.createEpic(epic2);

        Epic[] epic = {epic1, epic2};
        assertArrayEquals(epic, taskManager.getEpics().toArray());

    }

    @Test
    public void getNoEpicsTest() {
        List<Epic> epics = taskManager.getEpics();
        assertEquals(0, epics.size());
    }

    @Test
    public void getSubtasksTest() {
        Epic epic1 = new Epic("Epic 1", "Epic 1 description", TaskStatus.NEW);
        Epic epic2 = new Epic("Epic 2", "Epic 2 description", TaskStatus.NEW);
        final int epicId1 = taskManager.createEpic(epic1);
        final int epicId2 = taskManager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", TaskStatus.NEW, epicId1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", TaskStatus.NEW, epicId1);
        Subtask subtask3 = new Subtask("Subtask 3", "Subtask 3 description", TaskStatus.NEW, epicId1);
        Subtask subtask4 = new Subtask("Subtask 4", "Subtask 4 description", TaskStatus.IN_PROGRESS, epicId2);

        final int subtaskId1 = taskManager.createSubtask(subtask1);
        final int subtaskId2 = taskManager.createSubtask(subtask2);
        final int subtaskId3 = taskManager.createSubtask(subtask3);
        final int subtaskId4 = taskManager.createSubtask(subtask4);

        Subtask[] subtasks = {subtask1, subtask2, subtask3, subtask4};
        assertArrayEquals(subtasks, taskManager.getSubtasks().toArray());
    }

    @Test
    public void getNoSubtasksTest() {
        List<Subtask> subtasks = taskManager.getSubtasks();
        assertEquals(0, subtasks.size());
    }

    @Test
    public void getEpicSubtasksTest() {
        initTasksWithoutDate();
        Subtask subtask4 = new Subtask(8, "Subtask 4", "Subtask 4 description", TaskStatus.IN_PROGRESS, 4);
        Subtask[] subtasks = {subtask4};
        assertArrayEquals(subtasks, taskManager.getEpicSubtasks(4).toArray());
    }

    @Test
    public void getNoEpicSubtasksTest() {
        initTasksWithoutDate();
        Epic epic3 = new Epic(9, "Epic 3", "Epic 3 description", TaskStatus.NEW);
        final int epicId3 = taskManager.createEpic(epic3);
        assertEquals(0, taskManager.getEpicSubtasks(9).size());
    }

    @Test
    public void getNotRealEpicSubtasksTest() {
        initTasksWithoutDate();
        int notRealEpicId = 100;
        assertNull(taskManager.getEpicSubtasks(notRealEpicId));
    }

    @Test
    public void removeAllTasksTest() {
        initTasksWithoutDate();
        taskManager.removeAllTasks();
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    public void removeAllEpicsTest() {
        initTasksWithoutDate();
        taskManager.removeAllEpics();
        assertEquals(0, taskManager.getEpics().size());
        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    public void removeAllSubtasksTest() {
        initTasksWithoutDate();
        taskManager.removeAllSubtasks();
        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    public void removeAllSubtaskCheckEpicStatusChangeTest() {
        initTasksWithoutDate();
        taskManager.removeAllSubtasks();
        assertEquals(TaskStatus.NEW, taskManager.getEpic(4).getStatus());
    }

    @Test
    public void deleteTaskTest() {
        initTasksWithoutDate();
        taskManager.deleteTask(1);
        assertNull(taskManager.getTask(1));
        assertEquals(1, taskManager.getTasks().size());
    }

    @Test
    public void deleteTaskWithWrongIdTest() {
        initTasksWithoutDate();
        taskManager.deleteTask(10);
        assertNull(taskManager.getTask(10));
        assertEquals(2, taskManager.getTasks().size());
    }


    @Test
    public void deleteEpicTest() {
        initTasksWithoutDate();
        taskManager.deleteEpic(3);
        assertNull(taskManager.getEpic(3));
        assertEquals(1, taskManager.getEpics().size());

    }

    @Test
    public void deleteEpicWithWrongIdTest() {
        initTasksWithoutDate();
        taskManager.deleteEpic(10);
        assertNull(taskManager.getEpic(10));
        assertEquals(2, taskManager.getEpics().size());

    }

    @Test
    public void deleteSubtaskTest() {
        initTasksWithoutDate();
        taskManager.deleteSubtask(5);
        assertNull(taskManager.getSubtask(5));
        assertEquals(3, taskManager.getSubtasks().size());
    }

    @Test
    public void deleteSubtaskWithWrongIdTest() {
        initTasksWithoutDate();
        taskManager.deleteSubtask(10);
        assertNull(taskManager.getSubtask(10));
        assertEquals(4, taskManager.getSubtasks().size());
    }

    @Test
    public void deleteSubtaskAndCheckEpicStatusTest() {
        initTasksWithoutDate();
        taskManager.deleteSubtask(8);

        assertEquals(TaskStatus.NEW, taskManager.getEpic(4).getStatus());
    }

    @Test
    public void updateTaskTest() {
        initTasksWithoutDate();
        Task taskTwoDone = new Task(2, "Task 2 updated", "Task 2 description updated", TaskStatus.DONE);
        taskManager.updateTask(taskTwoDone);
        assertEquals(taskTwoDone, taskManager.getTask(2));
    }

    @Test
    public void updateTaskWithWrongIdTest() {
        initTasksWithoutDate();
        Task taskTwoDone = new Task(3, "Task 2 updated", "Task 2 description updated", TaskStatus.DONE);
        taskManager.updateTask(taskTwoDone);
        assertNotEquals(taskTwoDone, taskManager.getTask(2));
    }

    @Test
    public void updateTaskWithEmptyListTest() {
        Task taskTwoDone = new Task(2, "Task 2 updated", "Task 2 description updated", TaskStatus.DONE);
        taskManager.updateTask(taskTwoDone);
        assertNotEquals(taskTwoDone, taskManager.getTask(2));
    }

    @Test
    public void updateEpicTest() {
        initTasksWithoutDate();
        Epic epicOneDone = new Epic(3, "Epic 1 updated", "Epic 1 description updated", TaskStatus.DONE);
        taskManager.updateEpic(epicOneDone);
        assertEquals(epicOneDone, taskManager.getEpic(3));
    }

    @Test
    public void updateEpicWithWrongIdTest() {
        initTasksWithoutDate();
        Epic epicOneDone = new Epic(2, "Epic 1 updated", "Epic 1 description updated", TaskStatus.DONE);
        taskManager.updateEpic(epicOneDone);
        assertNotEquals(epicOneDone, taskManager.getEpic(3));
    }

    @Test
    public void updateEpicWitEmptyListTest() {

        Epic epicOneDone = new Epic(3, "Epic 1 updated", "Epic 1 description updated", TaskStatus.DONE);
        taskManager.updateEpic(epicOneDone);
        assertNotEquals(epicOneDone, taskManager.getEpic(3));

    }

    @Test
    public void updateSubtaskTest() {
        initTasksWithoutDate();
        Subtask subtask1Done = new Subtask(5, "Subtask 1 updated", "Subtask 1 description updated", TaskStatus.DONE, 3);
        taskManager.updateSubtask(subtask1Done);
        assertEquals(subtask1Done, taskManager.getSubtask(5));
    }

    @Test
    public void updateSubtaskWithEmptyListTest() {
        Subtask subtask1Done = new Subtask(5, "Subtask 1 updated", "Subtask 1 description updated", TaskStatus.DONE, 3);
        taskManager.updateSubtask(subtask1Done);
        assertNotEquals(subtask1Done, taskManager.getSubtask(5));
    }

    @Test
    public void updateSubtaskWithWrongIdTest() {
        initTasksWithoutDate();
        Subtask subtask1Done = new Subtask(1, "Subtask 1 updated", "Subtask 1 description updated", TaskStatus.DONE, 3);
        taskManager.updateSubtask(subtask1Done);
        assertNotEquals(subtask1Done, taskManager.getSubtask(5));
    }

    @Test
    public void updateSubtaskAndCheckEpicStatusTest() {
        initTasksWithoutDate();
        Subtask subtask1Done = new Subtask(5, "Subtask 1 updated", "Subtask 1 description updated", TaskStatus.DONE, 3);
        taskManager.updateSubtask(subtask1Done);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpic(3).getStatus());

    }

    @Test
    public void getEmptyHistoryTest() {
        initTasksWithoutDate();
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    public void getNotEmptyHistoryTest() {
        initTasksWithoutDate();
        taskManager.getTask(1);
        taskManager.getSubtask(5);
        taskManager.getEpic(3);

        Task[] history = {taskManager.getTask(1), taskManager.getSubtask(5), taskManager.getEpic(3)};
        assertArrayEquals(history, taskManager.getHistory().toArray());
    }

    @Test
    public void tasksMatchInTimeTest() {
        Task task1 = new Task("Task 1", "Task 1 description", TaskStatus.NEW, 120, LocalDateTime.of(2000, 4, 1, 5, 0));
        Task task2 = new Task("Task 2", "Task 2 description", TaskStatus.IN_PROGRESS, 120, LocalDateTime.of(2000, 4, 1, 5, 0));

        final int taskId1 = taskManager.createTask(task1);
        final int taskId2 = taskManager.createTask(task2);
        assertEquals(1, taskManager.getTasks().size());

    }

    @Test
    public void tasksUpdatedMatchInTimeTest() {
        Task task1 = new Task("Task 1", "Task 1 description", TaskStatus.NEW, 120, LocalDateTime.of(2000, 4, 1, 3, 0));
        Task task2 = new Task("Task 2", "Task 2 description", TaskStatus.IN_PROGRESS, 120, LocalDateTime.of(2000, 4, 1, 5, 0));

        final int taskId1 = taskManager.createTask(task1);
        final int taskId2 = taskManager.createTask(task2);
        Task[] beforeUpd = {task1, task2};
        assertArrayEquals(beforeUpd, taskManager.getPrioritizedTasks().toArray());

        Task task2UPD = new Task(2, "Task 2UPD", "Task 2 UPDATED", TaskStatus.IN_PROGRESS, 120, LocalDateTime.of(2000, 4, 1, 5, 0));
        taskManager.updateTask(task2UPD);
        Task[] afterUpd = {task1, task2UPD};
        assertArrayEquals(afterUpd, taskManager.getPrioritizedTasks().toArray());

        Task task2UPDAgain = new Task(2, "Task 2UPD", "Task 2 UPDATED", TaskStatus.IN_PROGRESS, 120, LocalDateTime.of(2000, 4, 1, 1, 0));
        taskManager.updateTask(task2UPDAgain);
        Task[] afterSecondUpd = {task2UPDAgain, task1};
        assertArrayEquals(afterSecondUpd, taskManager.getPrioritizedTasks().toArray());
        assertEquals(LocalDateTime.of(2000, 4, 1, 1, 0), taskManager.getTask(2).getStartTime());
        assertEquals(LocalDateTime.of(2000, 4, 1, 3, 0), taskManager.getTask(2).getEndTime());

    }

    @Test
    public void getPrioritizedTasksTest() {
        Task task1 = new Task("Task 1", "Task 1 description", TaskStatus.NEW, 120, LocalDateTime.of(2000, 2, 1, 0, 0));
        Task task2 = new Task("Task 2", "Task 2 description", TaskStatus.IN_PROGRESS, 120, LocalDateTime.of(2000, 1, 1, 0, 0));

        final int taskId1 = taskManager.createTask(task1);
        final int taskId2 = taskManager.createTask(task2);

        Epic epic1 = new Epic("Epic 1", "Epic 1 description", TaskStatus.NEW);
        final int epicId1 = taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", TaskStatus.NEW, epicId1, 120, LocalDateTime.of(2000, 3, 1, 0, 0));
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", TaskStatus.DONE, epicId1, 120, LocalDateTime.of(2000, 3, 1, 2, 0));
        final int subtaskId1 = taskManager.createSubtask(subtask1);
        final int subtaskId2 = taskManager.createSubtask(subtask2);

        Epic epic2 = new Epic("Epic 2", "Epic 2 description", TaskStatus.NEW);
        final int epicId2 = taskManager.createEpic(epic2);
        Subtask subtask3 = new Subtask("Subtask 3", "Subtask 3 description", TaskStatus.NEW, epicId1);
        final int subtaskId3 = taskManager.createSubtask(subtask3);


        Task[] ids = {task2, task1, subtask1, subtask2, subtask3};
        assertArrayEquals(ids, taskManager.getPrioritizedTasks().toArray());

    }

    @Test
    public void getPrioritizedTasksIfDeleteTaskAndSubtaskTest() {
        Task task1 = new Task("Task 1", "Task 1 description", TaskStatus.NEW, 120, LocalDateTime.of(2000, 2, 1, 0, 0));
        Task task2 = new Task("Task 2", "Task 2 description", TaskStatus.IN_PROGRESS, 120, LocalDateTime.of(2000, 1, 1, 0, 0));

        final int taskId1 = taskManager.createTask(task1);
        final int taskId2 = taskManager.createTask(task2);

        Epic epic1 = new Epic("Epic 1", "Epic 1 description", TaskStatus.NEW);
        final int epicId1 = taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", TaskStatus.NEW, epicId1, 120, LocalDateTime.of(2000, 3, 1, 0, 0));
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", TaskStatus.DONE, epicId1, 120, LocalDateTime.of(2000, 3, 1, 2, 0));
        final int subtaskId1 = taskManager.createSubtask(subtask1);
        final int subtaskId2 = taskManager.createSubtask(subtask2);

        Epic epic2 = new Epic("Epic 2", "Epic 2 description", TaskStatus.NEW);
        final int epicId2 = taskManager.createEpic(epic2);
        Subtask subtask3 = new Subtask("Subtask 3", "Subtask 3 description", TaskStatus.NEW, epicId1);
        final int subtaskId3 = taskManager.createSubtask(subtask3);

        taskManager.deleteTask(taskId2);
        taskManager.deleteSubtask(subtaskId1);


        Task[] ids = {task1, subtask2, subtask3};
        assertArrayEquals(ids, taskManager.getPrioritizedTasks().toArray());
        assertEquals(LocalDateTime.of(2000, 3, 1, 2, 0), taskManager.getEpic(3).getStartTime());
        assertEquals(LocalDateTime.of(2000, 3, 1, 4, 0), taskManager.getEpic(3).getEndTime());

    }

    @Test
    public void getPrioritizedTasksIfDeleteEpicTest() {
        Task task1 = new Task("Task 1", "Task 1 description", TaskStatus.NEW, 120, LocalDateTime.of(2000, 2, 1, 0, 0));
        Task task2 = new Task("Task 2", "Task 2 description", TaskStatus.IN_PROGRESS, 120, LocalDateTime.of(2000, 1, 1, 0, 0));

        final int taskId1 = taskManager.createTask(task1);
        final int taskId2 = taskManager.createTask(task2);

        Epic epic1 = new Epic("Epic 1", "Epic 1 description", TaskStatus.NEW);
        final int epicId1 = taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", TaskStatus.NEW, epicId1, 120, LocalDateTime.of(2000, 3, 1, 0, 0));
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", TaskStatus.DONE, epicId1, 120, LocalDateTime.of(2000, 3, 1, 2, 0));
        final int subtaskId1 = taskManager.createSubtask(subtask1);
        final int subtaskId2 = taskManager.createSubtask(subtask2);

        Epic epic2 = new Epic("Epic 2", "Epic 2 description", TaskStatus.NEW);
        final int epicId2 = taskManager.createEpic(epic2);
        Subtask subtask3 = new Subtask("Subtask 3", "Subtask 3 description", TaskStatus.NEW, epicId2);
        final int subtaskId3 = taskManager.createSubtask(subtask3);

        taskManager.deleteEpic(epicId1);


        Task[] ids = {task2, task1, subtask3};
        assertArrayEquals(ids, taskManager.getPrioritizedTasks().toArray());

    }

    @Test
    public void removeAllTasksAndCheckPrioritizedTasks() {
        initTasksWithDate();

        taskManager.removeAllTasks();


        assertEquals(4, taskManager.getPrioritizedTasks().size());

    }

    @Test
    public void removeAllEpicsAndCheckPrioritizedTasks() {
        initTasksWithDate();

        taskManager.removeAllEpics();


        assertEquals(2, taskManager.getPrioritizedTasks().size());

    }

    @Test
    public void removeAllSubtasksAndCheckPrioritizedTasks() {
        initTasksWithDate();

        taskManager.removeAllSubtasks();


        assertEquals(2, taskManager.getPrioritizedTasks().size());

    }

    @Test
    public void epicStartAndEndTimeTest() {
        Epic epic1 = new Epic("Epic 1", "Epic 1 description", TaskStatus.NEW);
        final int epicId1 = taskManager.createEpic(epic1);


        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", TaskStatus.NEW, epicId1, 120, LocalDateTime.of(2000, 1, 1, 2, 0));
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", TaskStatus.DONE, epicId1, 120, LocalDateTime.of(2000, 1, 1, 4, 0));


        final int subtaskId1 = taskManager.createSubtask(subtask1);
        final int subtaskId2 = taskManager.createSubtask(subtask2);

        assertEquals(LocalDateTime.of(2000, 1, 1, 2, 0), taskManager.getEpic(epicId1).getStartTime());
        assertEquals(LocalDateTime.of(2000, 1, 1, 6, 0), taskManager.getEpic(epicId1).getEndTime());
        assertEquals(240, taskManager.getEpic(epicId1).getDuration());
    }

    @Test
    public void epicStartAndEndTimeTestWhenDelete() {
        Epic epic1 = new Epic("Epic 1", "Epic 1 description", TaskStatus.NEW);
        final int epicId1 = taskManager.createEpic(epic1);


        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", TaskStatus.NEW, epicId1, 120, LocalDateTime.of(2000, 1, 1, 2, 0));
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", TaskStatus.DONE, epicId1, 120, LocalDateTime.of(2000, 1, 1, 4, 0));


        final int subtaskId1 = taskManager.createSubtask(subtask1);
        final int subtaskId2 = taskManager.createSubtask(subtask2);
        taskManager.deleteSubtask(3);

        assertEquals(LocalDateTime.of(2000, 1, 1, 2, 0), taskManager.getEpic(epicId1).getStartTime());
        assertEquals(LocalDateTime.of(2000, 1, 1, 4, 0), taskManager.getEpic(epicId1).getEndTime());
        assertEquals(120, taskManager.getEpic(epicId1).getDuration());
    }

    @Test
    public void epicStartAndEndTimeTestWhenClearSubtasks() {
        Epic epic1 = new Epic("Epic 1", "Epic 1 description", TaskStatus.NEW);
        final int epicId1 = taskManager.createEpic(epic1);


        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", TaskStatus.NEW, epicId1, 120, LocalDateTime.of(2000, 1, 1, 2, 0));
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", TaskStatus.DONE, epicId1, 120, LocalDateTime.of(2000, 1, 1, 4, 0));


        final int subtaskId1 = taskManager.createSubtask(subtask1);
        final int subtaskId2 = taskManager.createSubtask(subtask2);
        taskManager.removeAllSubtasks();

        assertEquals(null, taskManager.getEpic(epicId1).getStartTime());
        assertEquals(null, taskManager.getEpic(epicId1).getEndTime());
        assertEquals(0L, taskManager.getEpic(epicId1).getDuration());
    }

    @Test
    void calculateEpicStatusEmptySubtasksListTest() {
        Epic epic1 = new Epic("Epic 1", "Epic 1 description", TaskStatus.NEW);
        final int epicId1 = taskManager.createEpic(epic1);
        assertEquals(TaskStatus.NEW, taskManager.getEpic(epicId1).getStatus());
    }

    @Test
    public void calculateEpicStatusWithNewSubtasksTest() {
        Epic epic1 = new Epic("Epic 1", "Epic 1 description", TaskStatus.NEW);
        final int epicId1 = taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", TaskStatus.NEW, epicId1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", TaskStatus.NEW, epicId1);
        Subtask subtask3 = new Subtask("Subtask 3", "Subtask 3 description", TaskStatus.NEW, epicId1);

        final int subtaskId1 = taskManager.createSubtask(subtask1);
        final int subtaskId2 = taskManager.createSubtask(subtask2);
        final int subtaskId3 = taskManager.createSubtask(subtask3);

        assertEquals(TaskStatus.NEW, taskManager.getEpic(epicId1).getStatus());

    }

    @Test
    public void calculateEpicStatusWithDoneSubtasksTest() {
        Epic epic1 = new Epic("Epic 1", "Epic 1 description", TaskStatus.NEW);
        final int epicId1 = taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", TaskStatus.DONE, epicId1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", TaskStatus.DONE, epicId1);
        Subtask subtask3 = new Subtask("Subtask 3", "Subtask 3 description", TaskStatus.DONE, epicId1);

        final int subtaskId1 = taskManager.createSubtask(subtask1);
        final int subtaskId2 = taskManager.createSubtask(subtask2);
        final int subtaskId3 = taskManager.createSubtask(subtask3);

        assertEquals(TaskStatus.DONE, taskManager.getEpic(epicId1).getStatus());

    }

    @Test
    public void calculateEpicStatusWithNewAndDoneSubtasksTest() {
        Epic epic1 = new Epic("Epic 1", "Epic 1 description", TaskStatus.NEW);
        final int epicId1 = taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", TaskStatus.NEW, epicId1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", TaskStatus.DONE, epicId1);
        Subtask subtask3 = new Subtask("Subtask 3", "Subtask 3 description", TaskStatus.DONE, epicId1);

        final int subtaskId1 = taskManager.createSubtask(subtask1);
        final int subtaskId2 = taskManager.createSubtask(subtask2);
        final int subtaskId3 = taskManager.createSubtask(subtask3);

        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpic(epicId1).getStatus());

    }

    @Test
    public void calculateEpicStatusWithInProgressSubtasksTest() {
        Epic epic1 = new Epic("Epic 1", "Epic 1 description", TaskStatus.NEW);
        final int epicId1 = taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", TaskStatus.IN_PROGRESS, epicId1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", TaskStatus.IN_PROGRESS, epicId1);
        Subtask subtask3 = new Subtask("Subtask 3", "Subtask 3 description", TaskStatus.IN_PROGRESS, epicId1);

        final int subtaskId1 = taskManager.createSubtask(subtask1);
        final int subtaskId2 = taskManager.createSubtask(subtask2);
        final int subtaskId3 = taskManager.createSubtask(subtask3);

        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpic(epicId1).getStatus());

    }

    @Test
    public void checkDontAddInPriorotizedIfCross() {
        //Создаю первую задачу - ОК
        assertEquals(1, taskManager.createTask(new Task("Task 1", "Task 1 description", TaskStatus.NEW, 60, LocalDateTime.of(2000, 1, 1, 2, 0))));
        //Создаю вторую задачу - ОК
        assertEquals(2, taskManager.createTask(new Task("Task 1", "Task 1 description", TaskStatus.NEW, 60, LocalDateTime.of(2000, 1, 1, 3, 0))));
        //Создаю третью задачу, которая пересекается с первой
        assertEquals(-1, taskManager.createTask(new Task("Task 1", "Task 1 description", TaskStatus.NEW, 60, LocalDateTime.of(2000, 1, 1, 1, 30))));
        //Ошибка, id = -1 что соотвествует ошибке - ОК, но имеется побочный эффект:
        //В task задачу не сохранили, вернули -1, но при этом (при проходе цикла) задача была добавлена в prioritizedTasks
        assertEquals(2, taskManager.getPrioritizedTasks().size());
        assertEquals(2, taskManager.getTasks().size());

    }

    @Test
    public void checkHistoryWhenRemoveAllTask() {
        initTasksWithoutDate();
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.removeAllTasks();
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    public void checkHistoryWhenRemoveAllEpics() {
        initTasksWithoutDate();
        taskManager.getEpic(3);
        taskManager.getSubtask(6);
        taskManager.getSubtask(5);
        taskManager.getEpic(4);
        taskManager.removeAllEpics();
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    public void checkHistoryWhenRemoveAllSubtasks() {
        initTasksWithoutDate();
        taskManager.getEpic(3);
        taskManager.getSubtask(6);
        taskManager.getSubtask(5);
        taskManager.getEpic(4);
        taskManager.removeAllSubtasks();
        assertEquals(2, taskManager.getHistory().size());
    }


}


