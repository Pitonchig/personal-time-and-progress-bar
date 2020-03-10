package net.thumbtack.ptpb.db.task;

import java.util.List;

public interface TaskDao {
    List<Task> getAllTasks();

    void insert(Task task);
}
