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



}
