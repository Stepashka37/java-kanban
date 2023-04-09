package ru.yandex.praktikum.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.praktikum.manager.Managers;
import ru.yandex.praktikum.manager.TaskManager;
import ru.yandex.praktikum.tasks.Epic;
import ru.yandex.praktikum.tasks.Subtask;
import ru.yandex.praktikum.tasks.Task;
import ru.yandex.praktikum.tasks.TaskStatus;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    public static final int PORT = 8080;
    public final HttpServer server;
    private final Gson gson;
    private final TaskManager taskManager;


    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = new Gson();

        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handler);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final HttpTaskServer server = new HttpTaskServer();
        server.server.start();
        System.out.println("Сервер запущен");
        URI url = URI.create("http://localhost:8080/tasks/task");
        Gson gson1 = new Gson();
        Task task1 = new Task("Task 1", "UPD", TaskStatus.NEW, 120, LocalDateTime.parse("09:02:2023 03:00:00"));
        HttpClient client = HttpClient.newHttpClient();
        String json = gson1.toJson(task1);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    }

    private void handler(HttpExchange h) {
        try {
            System.out.println("\n/tasks/: " + h.getRequestURI());
            final String path = h.getRequestURI().getPath().substring(7);
            switch (path) {
                case "":
                    if (!h.getRequestMethod().equals("GET")) {
                        System.out.println("/ Ожидает GET-запрос, а получил: " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                        break;
                    }
                    final String response = gson.toJson(taskManager.getPrioritizedTasks());
                    sendText(h, response);
                    break;


                case "history":
                    if (!h.getRequestMethod().equals("GET")) {
                        System.out.println("/history Ожидает GET-запрос, а получил: " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                        break;
                    }
                    final String responseHistory = gson.toJson(taskManager.getHistory());
                    sendText(h, responseHistory);
                    break;

                case "task":
                    handleTask(h);
                    break;
                case "subtask":
                    handleSubtask(h);
                    break;
                case "epic":
                    handleEpic(h);
                    break;
                case "subtask/epic":
                    if (!h.getRequestMethod().equals("GET")) {
                        System.out.println("/subtask/epic Ожидает GET-запрос, а получил: " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                        break;
                    }
                    final String query = h.getRequestURI().getQuery();
                    String idParam = query.substring(3);
                    final int id = Integer.parseInt(idParam);
                    final List<Subtask> subtasksList = taskManager.getEpicSubtasks(id);
                    final String responseEpicSubtasks = gson.toJson(subtasksList);
                    System.out.println("Получили подзадачи эпика id= " + id);
                    sendText(h, responseEpicSubtasks);
                    break;
                default:
                    System.out.println("Неизвестный путь " + path);
                    break;

            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            h.close();
        }
    }

    private void handleTask(HttpExchange h) throws IOException {
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()) {
            case "GET":
                if (query == null) {
                    final List<Task> tasks = taskManager.getTasks();
                    final String responseGet = gson.toJson(tasks);
                    System.out.println("Получены все задачи");
                    sendText(h, responseGet);
                    return;
                }
                String idParamGet = query.substring(3);
                final int idGet = Integer.parseInt(idParamGet);
                final Task task = taskManager.getTask(idGet);
                final String responseGet = gson.toJson(task);
                System.out.println("Получена задача с id: " + idGet);
                sendText(h, responseGet);
                break;

            case "DELETE":
                if (query == null) {
                    taskManager.removeAllTasks();
                    System.out.println("Удалены все задачи");
                    h.sendResponseHeaders(200, 0);
                    return;
                }
                String idParamDelete = query.substring(3);
                final int idDelete = Integer.parseInt(idParamDelete);
                taskManager.deleteTask(idDelete);
                System.out.println("Удалена задача с id: " + idDelete);
                h.sendResponseHeaders(200, 0);
                break;

            case "POST":
                String json = readText(h);
                if (json.isEmpty()) {
                    System.out.println("Body задачи пустой, указывается в теле запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                final Task taskPost = gson.fromJson(json, Task.class);

                final Integer idPost = taskPost.getId();
                if (idPost != null && idPost != 0 && taskManager.getTask(idPost) != null) {
                    taskManager.updateTask(taskPost);
                    System.out.println("Обновлена задача с id: " + taskPost.getId());
                    h.sendResponseHeaders(200, 0);
                    break;
                } else {
                    taskManager.createTask(taskPost);
                    System.out.println("Создана задача с id: " + taskPost.getId());
                    final String responsePost = gson.toJson(taskPost);
                    sendText(h, responsePost);
                    break;
                }
            default:
                System.out.println("Неизвестный запрос: " + h.getRequestMethod());
                break;
        }
    }

    private void handleSubtask(HttpExchange h) throws IOException {
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()) {
            case "GET":
                if (query == null) {
                    final List<Subtask> subtasks = taskManager.getSubtasks();
                    final String responseGet = gson.toJson(subtasks);
                    System.out.println("Получены все подзадачи");
                    sendText(h, responseGet);
                    return;
                }
                String idParamGet = query.substring(3);
                final int idGet = Integer.parseInt(idParamGet);
                final Subtask subtask = taskManager.getSubtask(idGet);
                final String responseGet = gson.toJson(subtask);
                System.out.println("Получена подзадача с id: " + idGet);
                sendText(h, responseGet);
                break;

            case "DELETE":
                if (query == null) {
                    taskManager.removeAllSubtasks();
                    System.out.println("Удалены все подзадачи");
                    h.sendResponseHeaders(200, 0);
                    return;
                }
                String idParamDelete = query.substring(3);
                final int idDelete = Integer.parseInt(idParamDelete);
                taskManager.deleteSubtask(idDelete);
                System.out.println("Удалена подзадача с id: " + idDelete);
                h.sendResponseHeaders(200, 0);
                break;

            case "POST":
                String json = readText(h);
                if (json.isEmpty()) {
                    System.out.println("Body подзадачи пустой, указывается в теле запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                final Subtask subtaskPost = gson.fromJson(json, Subtask.class);
                final Integer idPost = subtaskPost.getId();
                if (idPost != null && idPost != 0 && taskManager.getSubtask(idPost) != null) {
                    taskManager.updateSubtask(subtaskPost);
                    System.out.println("Обновлена подзадача с id: " + idPost);
                    h.sendResponseHeaders(200, 0);
                    break;
                } else {
                    taskManager.createSubtask(subtaskPost);
                    System.out.println("Создана подзадача с id: " + idPost);
                    final String responsePost = gson.toJson(subtaskPost);
                    sendText(h, responsePost);
                    break;
                }
            default:
                System.out.println("Неизвестный запрос: " + h.getRequestMethod());
                break;
        }

    }

    private void handleEpic(HttpExchange h) throws IOException {
        Gson gson1 = new Gson();
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()) {
            case "GET":
                if (query == null) {
                    final List<Epic> epics = taskManager.getEpics();
                    final String responseGet = gson.toJson(epics);
                    System.out.println("Получены все эпики");
                    sendText(h, responseGet);
                    return;
                }
                String idParamGet = query.substring(3);
                final int idGet = Integer.parseInt(idParamGet);
                final Epic epic = taskManager.getEpic(idGet);
                final String responseGet = gson.toJson(epic);
                System.out.println("Получен эпик с id: " + idGet);
                sendText(h, responseGet);
                break;

            case "DELETE":
                if (query == null) {
                    taskManager.removeAllEpics();
                    System.out.println("Удалены все эпики");
                    h.sendResponseHeaders(200, 0);
                    return;
                }
                String idParamDelete = query.substring(3);
                final int idDelete = Integer.parseInt(idParamDelete);
                taskManager.deleteEpic(idDelete);
                System.out.println("Удален эпик с id: " + idDelete);
                h.sendResponseHeaders(200, 0);
                break;

            case "POST":
                String json = readText(h);
                if (json.isEmpty()) {
                    System.out.println("Body эпика пустой, указывается в теле запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                final Epic epicPost = gson.fromJson(json, Epic.class);
                final Integer idPost = epicPost.getId();
                if (idPost != null && idPost != 0 && taskManager.getEpic(idPost) != null) {
                    taskManager.updateEpic(epicPost);
                    System.out.println("Обновлен эпик с id: " + idPost);
                    h.sendResponseHeaders(200, 0);
                    break;
                } else {
                    int epicId = taskManager.createEpic(gson.fromJson(json, Epic.class));
                    System.out.println("Создан эпик с id: " + epicId);
                    final String responsePost = gson.toJson(epicPost);
                    sendText(h, responsePost);
                    break;
                }
            default:
                System.out.println("Неизвестный запрос: " + h.getRequestMethod());
                break;
        }

    }


    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    private void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}
