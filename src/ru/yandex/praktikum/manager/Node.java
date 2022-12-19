/** Класс реализующий узел связного списка */

package ru.yandex.praktikum.manager;

import ru.yandex.praktikum.tasks.Task;

public class Node  {
    public Task data;
    public Node next;
    public Node prev;

    public Node(Node prev, Task data, Node next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}
