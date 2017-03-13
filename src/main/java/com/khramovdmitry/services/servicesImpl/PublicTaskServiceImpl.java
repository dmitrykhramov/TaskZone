package com.khramovdmitry.services.servicesImpl;

import com.khramovdmitry.domain.PublicTask;
import com.khramovdmitry.domain.Task;
import com.khramovdmitry.repositories.PublicTaskRepository;
import com.khramovdmitry.services.PublicTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    private Sort sortByDeadlineAsc() {
        return new Sort(Sort.Direction.ASC, "deadline");
    }

    private Sort sortByFinishedAsc() {
        return new Sort(Sort.Direction.DESC, "finished");
    }

    @Override
    public List<PublicTask> listAll() {
        return taskRepository.findAll(sortByDeadlineAsc());
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
        taskRepository.findAll(sortByFinishedAsc()).forEach(task -> {
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
    public List<PublicTask> listLastFinished() {
        List<PublicTask> finishedTasks = listAllFinished();
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -3);
        date = cal.getTime();

        List<PublicTask> lastTasks = new ArrayList<>();
        for (PublicTask task : finishedTasks) {
            if(task.getFinished() != null && task.getFinished().after(date)) {
                lastTasks.add(task);
            }
        }
        return lastTasks;
    }

    @Override
    public void toogleTask(int id) {
        PublicTask task = getById(id);
        task.setDone(true);
        task.setFinished(new Date());
        saveOrUpdate(task);
    }
}
