package tasks;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.http.HttpTaskManager;
import ru.yandex.praktikum.http.HttpTaskServer;
import ru.yandex.praktikum.manager.Managers;
import ru.yandex.praktikum.manager.TaskManager;
import ru.yandex.praktikum.server.KVServer;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.Subtask;
import ru.yandex.praktikum.tasks.Task;
import ru.yandex.praktikum.tasks.TaskStatus;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    private KVServer kv = new KVServer();
    private TaskManager loadToServer;
    private HttpTaskServer server;
    private HttpTaskManager fromServer;

    public HttpTaskManagerTest() throws IOException {
    }


    public void createManagerAndAddTasks() throws IOException {
        loadToServer = Managers.getDefault();
        Task task1 = new Task("Task 1", "Task 1 description", TaskStatus.NEW, 120, LocalDateTime.of(2000, 4, 1, 5, 00));
        Task task2 = new Task("Task 2", "Task 2 description", TaskStatus.IN_PROGRESS, 120, LocalDateTime.of(2000, 4, 1, 3, 0));
        Task task3 = new Task("Task 3", "Task 3 description", TaskStatus.IN_PROGRESS);

        final int taskId1 = loadToServer.createTask(task1);
        final int taskId2 = loadToServer.createTask(task2);
        final int taskId3 = loadToServer.createTask(task3);

        Epic epic1 = new Epic("Epic 1", "Epic 1 description", TaskStatus.NEW);
        Epic epic2 = new Epic("Epic 2", "Epic 2 description", TaskStatus.NEW, 120, LocalDateTime.of(2000, 12, 1, 0, 0));
        final int epicId1 = loadToServer.createEpic(epic1);
        final int epicId2 = loadToServer.createEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", TaskStatus.NEW, epicId1, 120, LocalDateTime.of(2000, 11, 1, 2, 0));
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", TaskStatus.DONE, epicId1, 120, LocalDateTime.of(2000, 11, 1, 4, 0));
        Subtask subtask3 = new Subtask("Subtask 3", "Subtask 3 description", TaskStatus.NEW, epicId1);

        final int subtaskId1 = loadToServer.createSubtask(subtask1);
        final int subtaskId2 = loadToServer.createSubtask(subtask2);
        final int subtaskId3 = loadToServer.createSubtask(subtask3);


        loadToServer.getEpic(4);
        loadToServer.getTask(1);
        loadToServer.getSubtask(6);
        loadToServer.getSubtask(8);
        loadToServer.getEpic(4);
    }


    public void loadManagerFromServer() throws IOException {
        createManagerAndAddTasks();
        taskManager = new HttpTaskManager(KVServer.PORT, true);
        fromServer = new HttpTaskManager(KVServer.PORT, true);
        server = new HttpTaskServer(fromServer);
        server.server.start();
    }

    @BeforeEach
    public void serverStart() throws IOException {
        kv.start();
        taskManager = new HttpTaskManager(KVServer.PORT, false);
    }

    @AfterEach
    public void stopServer() {
        kv.stop();
    }


    @Test
    public void checkAllTasksSaved() throws IOException {
        createManagerAndAddTasks();

        assertEquals(3, loadToServer.getTasks().size());
        assertEquals(2, loadToServer.getEpics().size());
        assertEquals(3, loadToServer.getSubtasks().size());
        assertEquals(4, loadToServer.getHistory().size());
        Task task2 = new Task(2, "Task 2", "Task 2 description", TaskStatus.IN_PROGRESS, 120, LocalDateTime.of(2000, 4, 1, 3, 0));
        assertEquals(task2, loadToServer.getTask(2));

    }

    @Test
    public void loadFromServer() throws IOException {
        loadManagerFromServer();
        assertEquals(3, fromServer.getTasks().size());
        assertEquals(2, fromServer.getEpics().size());
        assertEquals(3, fromServer.getSubtasks().size());
        assertEquals(4, fromServer.getHistory().size());

        Task task2 = new Task(2, "Task 2", "Task 2 description", TaskStatus.IN_PROGRESS, 120, LocalDateTime.of(2000, 4, 1, 3, 0));
        assertEquals(task2, loadToServer.getTask(2));
        server.server.stop(0);
    }

    @Test
    public void endpointTasksCheck() throws IOException, InterruptedException {
        loadManagerFromServer();
        System.out.println("Сервер запущен");
        Gson gson = new Gson();

        URI url = URI.create("http://localhost:8080/tasks/");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest requestGet = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGet = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        Type listType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> priorotizedTasksFromServer = gson.fromJson(responseGet.body(), listType);

        assertEquals(200, responseGet.statusCode());
        assertEquals(6, priorotizedTasksFromServer.size());


        Task taskToServer = new Task(9, "Task 9", "Task9", TaskStatus.NEW, 120, LocalDateTime.of(2023, 2, 9, 0, 0, 0));
        url = URI.create("http://localhost:8080/tasks/");
        String json = gson.toJson(taskToServer);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        client = HttpClient.newHttpClient();
        HttpRequest requestPost = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> responsePost = client.send(requestPost, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, responsePost.statusCode());

        server.server.stop(0);
    }

    @Test
    public void endpointHistoryCheck() throws IOException, InterruptedException {
        loadManagerFromServer();
        System.out.println("Сервер запущен");
        URI url = URI.create("http://localhost:8080/tasks/history");
        Gson gson = new Gson();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest requestGet = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGet = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        Type listType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> listFromServer = gson.fromJson(responseGet.body(), listType);

        assertEquals(200, responseGet.statusCode());
        assertEquals(4, listFromServer.size());

        HttpRequest requestDelete = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> responseDelete = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, responseDelete.statusCode());

        server.server.stop(0);
    }

    @Test
    public void endpointEpicSubtasksCheck() throws IOException, InterruptedException {
        loadManagerFromServer();
        System.out.println("Сервер запущен");
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic?id=4");

        Gson gson = new Gson();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest requestGet = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGet = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        Type listType = new TypeToken<List<Subtask>>() {
        }.getType();
        List<Subtask> listFromServer = gson.fromJson(responseGet.body(), listType);

        assertEquals(200, responseGet.statusCode());
        assertEquals(3, listFromServer.size());
        server.server.stop(0);
    }

    @Test
    public void handleTaskEndpointsCheck() throws IOException, InterruptedException {
        loadManagerFromServer();
        System.out.println("Сервер запущен");
        URI url = URI.create("http://localhost:8080/tasks/task");
        Gson gson = new Gson();
        Task taskToServer = new Task(9, "Task 9", "Task9", TaskStatus.NEW, 120, LocalDateTime.of(2023, 2, 9, 0, 0, 0));
        HttpClient client = HttpClient.newHttpClient();
        String json = gson.toJson(taskToServer);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest requestPost = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> responsePost = client.send(requestPost, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responsePost.statusCode());
        assertEquals(4, this.fromServer.getTasks().size());
        Task taskFromServer = gson.fromJson(responsePost.body(), Task.class);
        assertEquals(taskToServer, taskFromServer);

        taskToServer = new Task(9, "Task 9 UPDATED", "Task9 UPDATED", TaskStatus.NEW, 120, LocalDateTime.of(2023, 2, 9, 0, 0, 0));
        json = gson.toJson(taskToServer);
        body = HttpRequest.BodyPublishers.ofString(json);
        requestPost = HttpRequest.newBuilder().uri(url).POST(body).build();
        responsePost = client.send(requestPost, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responsePost.statusCode());

        url = URI.create("http://localhost:8080/tasks/task?id=9");
        HttpRequest requestGet = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGet = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGet.statusCode());
        Task taskFromServerGet = gson.fromJson(responseGet.body(), Task.class);
        assertEquals(taskToServer, taskFromServerGet);

        url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest requestGetAll = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGetAll = client.send(requestGetAll, HttpResponse.BodyHandlers.ofString());
        Type listType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> listFromServer = gson.fromJson(responseGetAll.body(), listType);
        assertEquals(200, responseGet.statusCode());
        assertEquals(4, listFromServer.size());

        url = URI.create("http://localhost:8080/tasks/task?id=9");
        HttpRequest requestDelete = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> responseDelete = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseDelete.statusCode());
        assertEquals(3, fromServer.getTasks().size());

        url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest requestDeleteAll = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> responseDeleteAll = client.send(requestDeleteAll, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseDeleteAll.statusCode());
        assertEquals(0, fromServer.getTasks().size());

        server.server.stop(0);
    }

    @Test
    public void handleSubtaskEndpointsCheck() throws IOException, InterruptedException {
        loadManagerFromServer();
        System.out.println("Сервер запущен");
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        Gson gson = new Gson();
        Subtask subtaskToServer = new Subtask(9, "Subtask 9", "Subtask 9 description", TaskStatus.NEW, 4, 120, LocalDateTime.of(2000, 11, 1, 12, 0));


        HttpClient client = HttpClient.newHttpClient();
        String json = gson.toJson(subtaskToServer);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest requestPost = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> responsePost = client.send(requestPost, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responsePost.statusCode());
        assertEquals(4, fromServer.getSubtasks().size());
        Subtask subtaskFromServer = gson.fromJson(responsePost.body(), Subtask.class);
        assertEquals(subtaskToServer, subtaskFromServer);

        subtaskToServer = new Subtask(9, "Subtask 9 UPDATED", "Subtask 9 description UPDATED", TaskStatus.NEW, 4, 120, LocalDateTime.of(2000, 11, 1, 12, 0));

        json = gson.toJson(subtaskToServer);
        body = HttpRequest.BodyPublishers.ofString(json);
        requestPost = HttpRequest.newBuilder().uri(url).POST(body).build();
        responsePost = client.send(requestPost, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responsePost.statusCode());

        url = URI.create("http://localhost:8080/tasks/subtask?id=9");
        HttpRequest requestGet = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGet = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGet.statusCode());
        Subtask subtaskFromServerGet = gson.fromJson(responseGet.body(), Subtask.class);
        assertEquals(subtaskToServer, subtaskFromServerGet);

        url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest requestGetAll = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGetAll = client.send(requestGetAll, HttpResponse.BodyHandlers.ofString());
        Type listType = new TypeToken<List<Subtask>>() {
        }.getType();
        List<Subtask> listFromServer = gson.fromJson(responseGetAll.body(), listType);
        assertEquals(200, responseGet.statusCode());
        assertEquals(4, listFromServer.size());


        url = URI.create("http://localhost:8080/tasks/subtask?id=9");
        HttpRequest requestDelete = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> responseDelete = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseDelete.statusCode());
        assertEquals(3, fromServer.getSubtasks().size());

        url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest requestDeleteAll = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> responseDeleteAll = client.send(requestDeleteAll, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseDeleteAll.statusCode());
        assertEquals(0, fromServer.getSubtasks().size());
        server.server.stop(0);
    }


    @Test
    public void handleEpicEndpointsCheck() throws IOException, InterruptedException {
        loadManagerFromServer();
        System.out.println("Сервер запущен");
        URI url = URI.create("http://localhost:8080/tasks/epic");
        Gson gson = new Gson();
        Epic epicToServer = new Epic(9, "Epic 9", "Epic 9 description", TaskStatus.NEW, 120, LocalDateTime.of(2000, 11, 1, 12, 0));


        HttpClient client = HttpClient.newHttpClient();
        String json = gson.toJson(epicToServer);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest requestPost = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> responsePost = client.send(requestPost, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responsePost.statusCode());
        assertEquals(3, fromServer.getEpics().size());
        Epic epicFromServer = gson.fromJson(responsePost.body(), Epic.class);
        assertEquals(epicToServer, epicFromServer);

        epicToServer = new Epic(9, "Epic 9 UPDATED", "Epic 9 description UPDATED", TaskStatus.NEW, 120, LocalDateTime.of(2000, 11, 1, 12, 0));


        json = gson.toJson(epicToServer);
        body = HttpRequest.BodyPublishers.ofString(json);
        requestPost = HttpRequest.newBuilder().uri(url).POST(body).build();
        responsePost = client.send(requestPost, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responsePost.statusCode());

        url = URI.create("http://localhost:8080/tasks/epic?id=9");
        HttpRequest requestGet = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGet = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGet.statusCode());
        Epic epicFromServerGet = gson.fromJson(responseGet.body(), Epic.class);
        assertEquals(epicToServer, epicFromServerGet);

        url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest requestGetAll = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseGetAll = client.send(requestGetAll, HttpResponse.BodyHandlers.ofString());
        Type listType = new TypeToken<List<Epic>>() {
        }.getType();
        List<Epic> listFromServer = gson.fromJson(responseGetAll.body(), listType);
        assertEquals(200, responseGet.statusCode());
        assertEquals(3, listFromServer.size());


        url = URI.create("http://localhost:8080/tasks/epic?id=9");
        HttpRequest requestDelete = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> responseDelete = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseDelete.statusCode());
        assertEquals(2, fromServer.getEpics().size());

        url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest requestDeleteAll = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> responseDeleteAll = client.send(requestDeleteAll, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseDeleteAll.statusCode());
        assertEquals(0, fromServer.getEpics().size());

        server.server.stop(0);
    }


}
