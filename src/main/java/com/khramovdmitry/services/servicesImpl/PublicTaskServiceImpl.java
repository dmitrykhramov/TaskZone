package com.khramovdmitry.services.servicesImpl;

import com.khramovdmitry.domain.PublicTask;
import com.khramovdmitry.repositories.PublicTaskRepository;
import com.khramovdmitry.services.PublicTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitry on 12.02.2017.
 */

@Service
public class PublicTaskServiceImpl implements PublicTaskService {

    private PublicTaskRepository taskRepository;

    @Autowired
    public void setTaskRepository(PublicTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<PublicTask> listAll() {
        return taskRepository.findAll();
    }

    @Override
    public PublicTask getById(int id) {
        return taskRepository.findOne(id);
    }

    @Override
    public PublicTask saveOrUpdate(PublicTask object) {
        return taskRepository.save(object);
    }

    @Override
    public void delete(int id) {
        taskRepository.delete(id);
    }


    @Override
    public List<PublicTask> listAllFinished() {
        List<PublicTask> finishedTasks = new ArrayList<>();
        listAll().forEach(task -> {
            if (task.isDone()) {
                finishedTasks.add(task);
            }
        });
        return finishedTasks;
    }

    @Override
    public List<PublicTask> listAllUnfinished() {
        List<PublicTask> unfinishedTasks = new ArrayList<>();
        listAll().forEach(task -> {
            if (!task.isDone()) {
                unfinishedTasks.add(task);
            }
        });
        return unfinishedTasks;
    }

    @Override
    public void toogleTask(int id) {
        PublicTask task = getById(id);
        if (task.isDone()) {
            task.setDone(false);
        } else {
            task.setDone(true);
        }
        saveOrUpdate(task);
    }
}
