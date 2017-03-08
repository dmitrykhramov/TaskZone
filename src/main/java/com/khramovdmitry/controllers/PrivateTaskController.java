package com.khramovdmitry.controllers;

import com.khramovdmitry.domain.PrivateTask;
import com.khramovdmitry.domain.PublicTask;
import com.khramovdmitry.domain.Task;
import com.khramovdmitry.domain.User;
import com.khramovdmitry.services.PrivateTaskService;
import com.khramovdmitry.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dmitry on 06.03.2017.
 */

@RestController
@RequestMapping("/rest/tasks/private")
public class PrivateTaskController {

    @Autowired
    private PrivateTaskService privateTaskService;

    @Autowired
    private UserService userService;

    @RequestMapping("/all")
    public List<PrivateTask> listPrivateTasks(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<PrivateTask> privateTasks = new ArrayList<>();
        for (PrivateTask task : user.getPrivateTasks()) {
            if (!task.isDone()) {
                privateTasks.add(task);
            }
        }
        return privateTasks;
    }

    @RequestMapping("/finished")
    public List<PrivateTask> listFinishedTasks(Principal principal) {

        User user = userService.findByUsername(principal.getName());
        List<PrivateTask> privateTasks = new ArrayList<>();
        for (PrivateTask task : user.getPrivateTasks()) {
            if (task.isDone()) {
                privateTasks.add(task);
            }
        }
        return privateTasks;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> addTask(@RequestBody PrivateTask task, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        task.setUser(user);
        task.setDone(false);
        Date date = new Date();
        task.setCreated(date);
        return new ResponseEntity<>(privateTaskService.saveOrUpdate(task), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateTask(@RequestBody PrivateTask task) {
        PrivateTask oldTask = privateTaskService.getById(task.getId());
        oldTask.setDescription(task.getDescription());
        oldTask.setTitle(task.getTitle());
        oldTask.setDeadline(task.getDeadline());
        return new ResponseEntity<>(privateTaskService.saveOrUpdate(oldTask), HttpStatus.OK);
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public Task editTask(@PathVariable("id") int id) {
        return privateTaskService.getById(id);
    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteTask(@PathVariable("id") int id) {
        Task taskToDelete = privateTaskService.getById(id);
        privateTaskService.delete(id);
        return new ResponseEntity<>(taskToDelete, HttpStatus.OK);
    }

    @RequestMapping("/toogle/{id}")
    public ResponseEntity<?> toogleTask(@PathVariable("id") int id) {
        privateTaskService.toogleTask(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
