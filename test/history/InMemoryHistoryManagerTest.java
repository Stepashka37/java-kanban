package history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.manager.HistoryManager;
import ru.yandex.praktikum.manager.InMemoryHistoryManager;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.Subtask;
import ru.yandex.praktikum.tasks.Task;
import ru.yandex.praktikum.tasks.TaskStatus;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest  {

    HistoryManager historyManager;

    protected Task task1;
    protected Task task2;
    protected Epic epic1;
    protected Subtask subtask1;
    protected Subtask subtask2;

    @BeforeEach
    public void setUp(){
        historyManager = new InMemoryHistoryManager();
        Task task1 = new Task(1,"Task 1", "Task 1 description", TaskStatus.NEW);
        Task task2 = new Task(2, "Task 2", "Task 2 description", TaskStatus.IN_PROGRESS);
        Epic epic1 = new Epic(3, "Epic 1", "Epic 1 description", TaskStatus.NEW);
        Subtask subtask1 = new Subtask(4, "Subtask 1", "Subtask 1 description", TaskStatus.NEW, 3);
        this.task1 = task1;
        this.task2 = task2;
        this.epic1 = epic1;
        this.subtask1 = subtask1;

    }

    @Test
    public void addToHistoryTest(){
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(epic1);
        historyManager.add(subtask1);

        assertEquals(4, historyManager.getHistory().size());
    }

    @Test
    public void emptyHistoryTest(){
        assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    public void duplicatesInHistoryTest(){
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(epic1);
        historyManager.add(subtask1);

        historyManager.add(task1);
        historyManager.add(epic1);

        Task[] history = {task2, subtask1, task1, epic1};

        assertArrayEquals(history, historyManager.getHistory().toArray());
        assertEquals(history.length, historyManager.getHistory().size());
    }

    @Test
    public void removeFromBeginningTest(){
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(epic1);
        historyManager.add(subtask1);

        historyManager.remove(1);

        Task[] history = {task2, epic1, subtask1};

        assertArrayEquals(history, historyManager.getHistory().toArray());
        assertEquals(history.length, historyManager.getHistory().size());

    }

    @Test
    public void removeFromMiddleTest(){
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(epic1);
        historyManager.add(subtask1);

        historyManager.remove(2);
        historyManager.remove(3);

        Task[] history = {task1, subtask1};

        assertArrayEquals(history, historyManager.getHistory().toArray());
        assertEquals(history.length, historyManager.getHistory().size());

    }

    @Test
    public void removeFromEndTest(){
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(epic1);
        historyManager.add(subtask1);

        historyManager.remove(4);

        Task[] history = {task1, task2, epic1};

        assertArrayEquals(history, historyManager.getHistory().toArray());
        assertEquals(history.length, historyManager.getHistory().size());

    }



}
