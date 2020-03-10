package net.thumbtack.ptpb.db.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
public class TaskDaoImpl implements TaskDao {

    private TaskMapper taskMapper;

    @Autowired
    public TaskDaoImpl(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> tasks = new LinkedList<>();
        taskMapper.findAll().forEach(tasks::add);
        return tasks;
    }

    @Override
    public void insert(Task task) {
        taskMapper.save(task);
    }
}
