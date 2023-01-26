package tasks;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.manager.FileBackedTasksManager;
import ru.yandex.praktikum.manager.InMemoryTaskManager;
import ru.yandex.praktikum.manager.Managers;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.Task;
import ru.yandex.praktikum.tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{

    private File file;

    @BeforeEach
    void setUp() throws IOException {
        file = new File("savedManager.csv");
        //taskManager = Managers.getDefault();
        taskManager = new FileBackedTasksManager(file);

    }


    @Test
    public void loadFromFileTestNotEmptyListTest() throws IOException {
        initTasksWithDate();
        FileBackedTasksManager tasksManager2 = FileBackedTasksManager.loadFromFile(file);
        final List<Task> tasks = tasksManager2.getTasks();
        assertEquals(2, tasks.size());
        file.delete();
    }

    @Test
    public void loadFromFileTestEmptyListTest() throws IOException {
        FileBackedTasksManager tasksManager2 = FileBackedTasksManager.loadFromFile(file);
        final List<Task> tasks = tasksManager2.getTasks();
        assertEquals(0, tasks.size());
        file.delete();

        //assertEquals();
    }


    @Test
    public void writeToFileEmptyEpicTest(){
        Epic epic1 = new Epic("Epic 1", "Epic 1 description", TaskStatus.NEW);
        final int epicId1 = taskManager.createEpic(epic1);

        assertEquals(epic1, taskManager.getEpic(epicId1));
        file.delete();
    }

    @Test
    public void downloadFromFileEmptyEpicTest() throws IOException {
        Epic epic1 = new Epic("Epic 1", "Epic 1 description", TaskStatus.NEW);
        final int epicId1 = taskManager.createEpic(epic1);
        FileBackedTasksManager tasksManager2 = FileBackedTasksManager.loadFromFile(file);
        assertEquals(epic1, tasksManager2.getEpic(1));
        file.delete();

    }

    @Test
    public void writeEmptyHistoryToFileTest(){
        initTasksWithDate();
        assertEquals(0, taskManager.getHistory().size());
        file.delete();
    }

    @Test
    public void downloadFromFileEmptyHistoryFromFileTest() throws IOException {
        initTasksWithDate();
        FileBackedTasksManager tasksManager2 = FileBackedTasksManager.loadFromFile(file);
        assertEquals(0, tasksManager2.getHistory().size());
        file.delete();
    }

    @Test
    public void writeToFileTasksAndEpicsTest(){
        initTasksWithDate();
        taskManager.getTask(1);
        taskManager.getEpic(3);
        taskManager.getSubtask(5);

        assertEquals(2, taskManager.getTasks().size());
        assertEquals(2, taskManager.getEpics().size());
        assertEquals(4, taskManager.getSubtasks().size());
        assertEquals(3, taskManager.getHistory().size());

        file.delete();
    }

    @Test
    public void loadFromFileTasksAndEpicsTest() throws IOException {
        initTasksWithDate();
        taskManager.getTask(1);
        taskManager.getEpic(3);
        taskManager.getSubtask(5);

        FileBackedTasksManager tasksManager2 = FileBackedTasksManager.loadFromFile(file);
        assertEquals(2, tasksManager2.getTasks().size());
        assertEquals(2, tasksManager2.getEpics().size());
        assertEquals(4, tasksManager2.getSubtasks().size());
        assertEquals(3, tasksManager2.getHistory().size());

        file.delete();

    }

}
