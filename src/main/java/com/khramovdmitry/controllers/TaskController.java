package com.khramovdmitry.controllers;

import com.khramovdmitry.domain.PublicTask;
import com.khramovdmitry.domain.User;
import com.khramovdmitry.repositories.PublicTaskRepository;
import com.khramovdmitry.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

/**
 * Created by Dmitry on 12.02.2017.
 */
@RestController
@RequestMapping("/api")
public class TaskController {

//    private UserRepository userRepository;
//    private PublicTaskRepository taskRepository;
//
//    @Autowired
//    public void setUserRepository(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Autowired
//    public void setTaskRepository(PublicTaskRepository taskRepository) {
//        this.taskRepository = taskRepository;
//    }
//
//    @RequestMapping("/tasks/all")
//    public List<PublicTask> listAllTasks() {
//        return taskRepository.findAll();
//    }
//
//    @RequestMapping("/user/{id}/tasks")
//    public Collection<PublicTask> listTasksForUser(@PathVariable("id") int id) {
//        User user = userRepository.findOne(id);
//        List<PublicTask> tasks = user.getTasks();
//        return tasks;
//    }
}
