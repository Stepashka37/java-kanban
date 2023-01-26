package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.manager.InMemoryTaskManager;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.Subtask;
import ru.yandex.praktikum.tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{
    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void calculateEpicStatusEmptySubtasksListTest(){
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


}
