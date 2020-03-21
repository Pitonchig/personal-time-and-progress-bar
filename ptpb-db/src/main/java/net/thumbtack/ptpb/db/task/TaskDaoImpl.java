package net.thumbtack.ptpb.db.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TaskDaoImpl implements TaskDao {

    private final TaskMapper taskMapper;

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
