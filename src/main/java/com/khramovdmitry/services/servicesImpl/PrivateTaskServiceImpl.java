package com.khramovdmitry.services.servicesImpl;

import com.khramovdmitry.domain.PrivateTask;
import com.khramovdmitry.domain.PublicTask;
import com.khramovdmitry.repositories.PrivateTaskRepository;
import com.khramovdmitry.services.PrivateTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitry on 03.03.2017.
 */

@Service
public class PrivateTaskServiceImpl implements PrivateTaskService {

    private PrivateTaskRepository privateTaskRepository;

    @Autowired
    public void setPrivateTaskRepository(PrivateTaskRepository privateTaskRepository) {
        this.privateTaskRepository = privateTaskRepository;
    }

    @Override
    public List<PrivateTask> listAll() {
        return privateTaskRepository.findAll();
    }

    @Override
    public PrivateTask getById(int id) {
        return privateTaskRepository.findOne(id);
    }

    @Override
    public PrivateTask saveOrUpdate(PrivateTask object) {
        return privateTaskRepository.save(object);
    }

    @Override
    public void delete(int id) {
        privateTaskRepository.delete(id);
    }

    @Override
    public List<PrivateTask> listAllFinished() {
        List<PrivateTask> finishedTasks = new ArrayList<>();
        listAll().forEach(task -> {
            if (task.isDone()) {
                finishedTasks.add(task);
            }
        });
        return finishedTasks;
    }

    @Override
    public List<PrivateTask> listAllUnfinished() {
        List<PrivateTask> unfinishedTasks = new ArrayList<>();
        listAll().forEach(task -> {
            if (!task.isDone()) {
                unfinishedTasks.add(task);
            }
        });
        return unfinishedTasks;
    }

    @Override
    public void toogleTask(int id) {
        PrivateTask task = getById(id);
        if (task.isDone()) {
            task.setDone(false);
        } else {
            task.setDone(true);
        }
    }
}
