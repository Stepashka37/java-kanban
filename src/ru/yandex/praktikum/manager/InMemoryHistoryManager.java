/**
 * Класс описывающий логику менеджера истории
 *
 * @see ru.yandex.praktikum.manager.InMemoryTaskManager
 */

package ru.yandex.praktikum.manager;

import ru.yandex.praktikum.tasks.Task;
import ru.yandex.praktikum.manager.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @history - список 10 последних просмотренных задач
 */
public class InMemoryHistoryManager implements HistoryManager {
    
    private CustomLinkedList<Integer> history = new CustomLinkedList<>();

    /**
     * Метод для вывода списка просмотренных задач
     */
    @Override
    public CustomLinkedList<Integer> getHistory() {
        return history;
    }

    /**
     * Метод для добавления задачи в список просмотренных, внутри реализована логика проверки, есть ли данная задача в
     * в списке. Если есть - узел имеющейся задачи вырезается, удаляется из allTasks, и заново добавлляется в конец списка
     */
    @Override
    public void add(int id) {
        if (history.getIdAndNode().containsKey(id)) {
            Node nodeToRemove = history.getIdAndNode().get(id);
            history.removeNode(nodeToRemove);
            history.getTasks().remove((Integer) id);
            history.linkLast(id);
        } else {
            history.linkLast(id);
        }
    }

    /**
     * Метод для удаления задачи из списка - вырезается узел, удаляется пара ключ-значения в хеш-таблице, удаляется
     * элемент из списка allTasks
     */
    public void remove(int id) {
        Node toRemove = history.getIdAndNode().get(id);
        history.removeNode(toRemove);
        history.getIdAndNode().remove(id);
        history.getTasks().remove((Integer) id);
    }
}

/** Класс реализующий структуру данных связный список. Помимо полей головы, хвоста и размера, есть поле встроенной мапы
 *  для быстрого поиска узла по айди элемента, а также обычный ArrayList для вывода айди всех элементов связного списка  */
class CustomLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size = 0;
    private List<Integer> allTasks = new ArrayList<>();
    private Map<Integer, Node> idAndNode = new HashMap<>();


    public Map<Integer, Node> getIdAndNode() {
        return idAndNode;
    }

    /** Метод для добавления айди задачи в конец связного списка. При этом, айди добавляется также в мапу и allTasks */
    public void linkLast(Integer id) {
        Node<T> oldTail = tail;
        Node<T> newNode = new Node<>(oldTail, id, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        allTasks.add(id);
        idAndNode.put(id, tail);
        size++;
    }

    public int size() {
        return this.size;
    }

    public List<Integer> getTasks() {
        return allTasks;
    }

    /** Метод для вырезания узла связного списка */
    public void removeNode(Node node) {
        node = null;
        this.size--;
    }


}