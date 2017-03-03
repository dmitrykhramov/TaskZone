package com.khramovdmitry.controllers;

import com.khramovdmitry.domain.PublicTask;
import com.khramovdmitry.domain.Response;
import com.khramovdmitry.domain.Task;
import com.khramovdmitry.domain.User;
import com.khramovdmitry.repositories.PublicTaskRepository;
import com.khramovdmitry.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * Created by Dmitry on 12.02.2017.
 */

@RestController
@RequestMapping("/api")
public class TaskController {

    private UserRepository userRepository;
    private PublicTaskRepository publicTaskRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPublicTaskRepository(PublicTaskRepository publicTaskRepository) {
        this.publicTaskRepository = publicTaskRepository;
    }

    @RequestMapping("/tasks/all")
    public List<PublicTask> listAllTasks() {
        return publicTaskRepository.findAll();
    }

    @RequestMapping("/tasks/populate")
    public String populateTasks() {
        PublicTask task = new PublicTask();
        task.setTitle("Title2");
        task.setDescription("Testing task2");
        publicTaskRepository.save(task);
        return "populated";
    }

    @RequestMapping(value = "/tasks/add", method = RequestMethod.POST)
    public ResponseEntity<?> addTask(@RequestBody PublicTask task) {
        return new ResponseEntity<>(publicTaskRepository.save(task), HttpStatus.CREATED);
    }

//    @RequestMapping("/user/{id}/tasks")
//    public Collection<PublicTask> listTasksForUser(@PathVariable("id") int id) {
//        User user = userRepository.findOne(id);
//        List<PublicTask> tasks = user.getTasks();
//        return tasks;
//    }
}
