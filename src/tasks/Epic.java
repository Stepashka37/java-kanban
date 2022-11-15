/** Класс описывающий эпик
 * @see manager.Manager
 *  */

package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    protected List<Integer> subtasksId = new ArrayList<>();

    public Epic(int id, String name, String description, String status) {
        super(id, name, description, status);
    }

    public Epic(String name, String description, String status) {
        super(name, description, status);
    }

    public void addSubtaskId(int id) {
        subtasksId.add(id);
    }

    public List<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void clearSubtasksId() {
        subtasksId.clear();
    }

    public void removeSubtask(int id) {
        subtasksId.remove(Integer.valueOf(id));
    }

    public void setSubtasksId(List<Integer> subtasksId) {
        this.subtasksId = subtasksId;
    }


    @Override
    public String toString() {
        return "task.Epic{" +
                "name='" + name + '\'' +
                ", subtasksId=" + subtasksId +
                ", status='" + status + '\'' +
                '}';
    }
}
