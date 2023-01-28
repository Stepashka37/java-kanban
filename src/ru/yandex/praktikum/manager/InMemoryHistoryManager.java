/**
 * Класс описывающий логику менеджера истории
 *
 * @see ru.yandex.praktikum.manager.InMemoryTaskManager
 */

package ru.yandex.praktikum.manager;

import ru.yandex.praktikum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @history - список просмотренных задач
 */
public class InMemoryHistoryManager implements HistoryManager {

    private CustomLinkedList<Task> history = new CustomLinkedList<>();
    private Map<Integer, Node> idAndNode = new HashMap<>();


    /**
     * Метод для вывода списка просмотренных задач
     */
    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }


    /**
     * Метод для добавления задачи в список просмотренных, внутри реализована логика проверки, есть ли данная задача в
     * в списке (есть ли элемент в мапе). Если есть - узел имеющейся задачи вырезается и заново добавлляется в конец списка
     */
    @Override
    public void add(Task task) {
        Node node = idAndNode.get(task.getId());
        if (node != null) {
            history.removeNode(node);
        }
        Node newNode = history.linkLast(task);
        idAndNode.put(task.getId(), newNode);
    }


    /**
     * Метод для удаления задачи из списка - вырезается узел, удаляется пара ключ-значения в хеш-таблице
     */
    public void remove(int id) {
        Node node = idAndNode.get(id);
        if (node != null) {
            idAndNode.remove(id);
            history.removeNode(node);

        }
    }
}


/**
 * Класс реализующий структуру данных связный список
 */
class CustomLinkedList<T> {
    private Node head;
    private Node tail;
    private int size = 0;

    public Node getTail() {
        return tail;
    }


    /**
     * Метод для добавления  задачи в конец связного списка. При этом, задача добавляется также в мапу
     */
    public Node linkLast(Task task) {
        Node oldTail = tail;
        Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        this.size++;
        return tail;
    }


    /**
     * Метод для сохранения всех задач в ArrayList. Производится проход по всем нодам связного списка и сохранения задач
     * из них
     */
    public List<Task> getTasks() {
        List<Task> allTasks = new ArrayList<>();
        Node node = head;
        while (node != null) {
            allTasks.add(node.data);
            node = node.next;
        }
        return allTasks;
    }


    public int size() {
        return this.size;
    }


    /**
     * Метод для вырезания узла связного списка. Реализована логика для 3 случаев: вырезание головы, вырезание из середины
     * и вырезание из хвоста списка
     */
    public void removeNode(Node node) {
        if (node.next == null && node.prev == null) {
            this.head = null;
            this.tail = null;
            this.size--;
            return;
        } else if (node.prev == null) {
            head = node.next;
            node.next.prev = null;
        } else if (node.next == null) {
            tail = node.prev;
            node.prev.next = null;
        } else {
            node.next.prev = node.prev;
            node.prev.next = node.next;
        }
        this.size--;
    }
}