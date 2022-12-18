/** Класс реализующий узел связного списка */

package ru.yandex.praktikum.manager;

public class Node <T> {
    public Integer data;
    public Node<T> next;
    public Node<T> prev;

    public Node(Node<T> prev, Integer data, Node<T> next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}
