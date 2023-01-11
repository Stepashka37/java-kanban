/**
 * Класс реализующий сохранение данных из менеджера в файл и восстановление менеджера из файла
 */

package ru.yandex.praktikum.manager;

import ru.yandex.praktikum.tasks.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private File file;

    FileBackedTasksManager(File file) throws IOException {
            this.file = file;
    }

    FileBackedTasksManager(){
    }

    /**
     * Метод для имитации записи данных из менеджера в файл и воссоздания менеджера из файла
     */
    public static void main(String[] args) throws IOException {
        FileBackedTasksManager toFile = new FileBackedTasksManager(new File("savedManager.csv"));
        Task task1 = new Task("Task 1", "Task 1 description", TaskStatus.NEW);
        Task task2 = new Task("Task 2", "Task 2 description", TaskStatus.IN_PROGRESS);
        final int taskId1 = toFile.createTask(task1);
        final int taskId2 = toFile.createTask(task2);


        Epic epic1 = new Epic("Epic 1",  "Epic 1 description", TaskStatus.NEW);
        Epic epic2 = new Epic("Epic 2", "Epic 2 description", TaskStatus.NEW);
        final int epicId1 = toFile.createEpic(epic1);
        final int epicId2 = toFile.createEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 description", TaskStatus.NEW, epicId1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 description", TaskStatus.DONE, epicId1);
        Subtask subtask3 = new Subtask("Subtask 3", "Subtask 3 description", TaskStatus.NEW, epicId1);

        final int subtaskId1 = toFile.createSubtask(subtask1);
        final int subtaskId2 = toFile.createSubtask(subtask2);
        final int subtaskId3 = toFile.createSubtask(subtask3);


        toFile.getTask(1);
        toFile.getSubtask(5);
        toFile.getSubtask(6);
        toFile.getSubtask(7);
        toFile.getSubtask(7);
        toFile.getSubtask(5);

        FileBackedTasksManager fromFile = loadFromFile(new File("savedManager.csv"));
        System.out.println(fromFile.getTasks());
        System.out.println();
        System.out.println(fromFile.getEpics());
        System.out.println();
        System.out.println(fromFile.getSubtasks());
        System.out.println();
        System.out.println(fromFile.getHistory());
    }

    /**
     * Метод создания таски из строки, реализована логика для создания эпиков и сабтасок
     */
    public static Task fromString(String value) {
        String[] array = value.split(",");
        Task taskToReturn = new Task(Integer.parseInt(array[0]), array[2], array[4], TaskStatus.valueOf(array[3]));

        if (array[1].equals(TaskType.EPIC.name())) {
            taskToReturn = new Epic(Integer.parseInt(array[0]), array[2], array[4], TaskStatus.valueOf(array[3]));
        } else if (array[1].equals(TaskType.SUBTASK.name())) {
            taskToReturn = new Subtask(Integer.parseInt(array[0]), array[2], array[4], TaskStatus.valueOf(array[3]),
                    Integer.parseInt(array[5]));
        }
        return taskToReturn;
    }

    /**
     * Метод записи таски в строку, отдельный случай - создание строки из сабтаски, т.к. там требуется указать айди эпика
     */
    public String toString(Task task) {
        String str = task.getId() +
                "," + task.getType() +
                "," + task.getName() +
                "," + task.getStatus() +
                "," + task.getDescription();

        if (task.getType().equals(TaskType.SUBTASK)) {
            str = ((Subtask) task).getId() +
                    "," + ((Subtask) task).getType() +
                    "," + ((Subtask) task).getName() +
                    "," + ((Subtask) task).getStatus() +
                    "," + ((Subtask) task).getDescription() +
                    "," + ((Subtask) task).getEpicId();
        }
        return str;
    }

    /**
     * Метод записи истории в строку
     */
    public static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        String[] ids = new String[history.size()];
        for (int i = 0; i < history.size(); i++) {
            ids[i] = String.valueOf(history.get(i).getId());
        }
        return String.join(",", ids);
    }

    /**
     * Метод воссоздания истории из строки
     */
    public static List<Integer> historyFromString(String value) {
        String[] idsArray = value.split(",");
        List<Integer> idsList = new LinkedList<>();
        for (int i = 0; i < idsArray.length; i++) {
            Integer id = Integer.parseInt(idsArray[i]);
            idsList.add(id);
        }
        return idsList;
    }

    /**
     * Метод сохранения текущего состояния менеджера в файл. При каждом вызове метод заново пробегается по всем трем мапам
     * и записывает в файл элементы каждой из них и затем записывает айди задач из истории просмотров
     */
    public void save() throws ManagerSaveException {
        try (Writer fileWriter = new FileWriter(file)) {

            fileWriter.write("id,type,name,status,description,epic\n");
            for (Integer id : tasks.keySet()) {
                Task taskFromMap = tasks.get(id);
                fileWriter.write(toString(taskFromMap) + "\n");
            }
            for (Integer id : epics.keySet()) {
                Epic epicFromMap = epics.get(id);
                fileWriter.write(toString(epicFromMap) + "\n");
            }
            for (Integer id : subtasks.keySet()) {
                Subtask subtaskFromMap = subtasks.get(id);
                fileWriter.write(toString(subtaskFromMap) + "\n");
            }

            fileWriter.write("\n" + historyToString(historyManager));

        } catch (IOException exp) {
            throw new ManagerSaveException("Ошибка при сохранении");
        }
    }

    /**
     * Метод создания менеджера из файла. Реализована логика наполнения мап тасок, эпиков и сабтаск и логика наполнения
     * списка истории задач (т.к. в исходном менеджере getHistory() возвращает List<Task>, а у нас метод historyFromString
     * возвращает List<Integer> пришлось еще реализовать проход по всем трем мапам по ключам, чтобы добавить нужные задачи
     * в историю)
     */
    public static FileBackedTasksManager loadFromFile(File file) throws IOException {
        if (file.exists()) {
        String absolutePath = file.getAbsolutePath();
        FileBackedTasksManager managerFromFile = new FileBackedTasksManager(file);
        String fileToString = Files.readString(Paths.get(absolutePath)); //весь файл преобразован в одну строку

        String[] tasksAndHistory = fileToString.split("\n\n");//строка поделена по пустой строке разделяющей задачи и историю

        String tasksInString = tasksAndHistory[0];//все таски в виде одной строки

        String historyInString = tasksAndHistory[1];//история просмотров в виде одной строки

        String[] tasksSeparately = tasksInString.split("\n");//все таски по отдельности

        String[] historySeparately = historyInString.split(",");//все айдишники из истории по отдельности

        for (int i = 0; i < tasksSeparately.length; i++) {
            String[] task = tasksSeparately[i].split(",");
            if (task[1].equals(TaskType.TASK.name())) {
                Task taskGen = fromString(tasksSeparately[i]);
                managerFromFile.tasks.put(taskGen.getId(), taskGen);
            } else if (task[1].equals(TaskType.EPIC.name())) {
                Epic epicGen = (Epic) fromString(tasksSeparately[i]);
                managerFromFile.epics.put(epicGen.getId(), epicGen);
            } else if (task[1].equals(TaskType.SUBTASK.name())) {
                Subtask subtaskGen = (Subtask) fromString(tasksSeparately[i]);
                managerFromFile.subtasks.put(subtaskGen.getId(), subtaskGen);
            }
        }

        for (Integer id : historyFromString(historyInString)) {
            if (managerFromFile.tasks.containsKey(id)) {
                Task task = managerFromFile.tasks.get(id);
                managerFromFile.history.add(task);
            } else if (managerFromFile.epics.containsKey(id)) {
                Epic epic = managerFromFile.epics.get(id);
                managerFromFile.history.add(epic);
            } else if (managerFromFile.subtasks.containsKey(id)) {
                Subtask subtask = managerFromFile.subtasks.get(id);
                managerFromFile.history.add(subtask);
            } else {
                System.out.println("Объекта с id " + id + " нет или он был удален");
            }
        }
            return managerFromFile;
        } else {
            System.out.println("Такого файла не существует");
            return null;
        }
    }


    @Override
    public int createTask(Task task) {
        int taskId = super.createTask(task);
        save();
        return taskId;
    }

    @Override
    public int createEpic(Epic epic) {
        int epicId = super.createEpic(epic);
        save();
        return epicId;
    }

    @Override
    public int createSubtask(Subtask subtask) {
        int subtaskId = super.createSubtask(subtask);
        save();
        return subtaskId;
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    /**
     * Класс реализующий собственное исключение
     */
    class ManagerSaveException extends RuntimeException {

        public ManagerSaveException() {
        }

        public ManagerSaveException(final String message) {
            super(message);
        }
    }
}
