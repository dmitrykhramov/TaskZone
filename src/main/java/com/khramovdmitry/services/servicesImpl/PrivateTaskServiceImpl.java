package com.khramovdmitry.services.servicesImpl;

import com.khramovdmitry.domain.PrivateTask;
import com.khramovdmitry.domain.PublicTask;
import com.khramovdmitry.domain.User;
import com.khramovdmitry.repositories.PrivateTaskRepository;
import com.khramovdmitry.services.PrivateTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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

    private Sort sortByDeadlineAsc() {
        return new Sort(Sort.Direction.ASC, "deadline");
    }

    @Override
    public List<PrivateTask> listAll() {
        return privateTaskRepository.findAll(sortByDeadlineAsc());
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
    public List<PrivateTask> listAllFinished(User user) {
        List<PrivateTask> finishedTasks = new ArrayList<>();
        for (PrivateTask task : user.getPrivateTasks()) {
            if (task.isDone()) {
                finishedTasks.add(task);
            }
        }
        return finishedTasks;
    }

    @Override
    public List<PrivateTask> listAllUnfinished(User user) {
        List<PrivateTask> unfinishedTasks = new ArrayList<>();
        for (PrivateTask task : user.getPrivateTasks()) {
            if (!task.isDone()) {
                unfinishedTasks.add(task);
            }
        }
        return unfinishedTasks;
    }

    @Override
    public void toogleTask(int id) {
        PrivateTask task = getById(id);
        task.setDone(true);
        saveOrUpdate(task);
    }
}
