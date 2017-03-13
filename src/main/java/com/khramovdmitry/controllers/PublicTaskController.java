package com.khramovdmitry.controllers;

import com.khramovdmitry.domain.PublicTask;
import com.khramovdmitry.domain.Response;
import com.khramovdmitry.domain.Task;
import com.khramovdmitry.domain.User;
import com.khramovdmitry.repositories.PublicTaskRepository;
import com.khramovdmitry.repositories.UserRepository;
import com.khramovdmitry.services.PublicTaskService;
import com.khramovdmitry.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Dmitry on 12.02.2017.
 */

@RestController
@RequestMapping("/rest/tasks")
public class PublicTaskController {

    private PublicTaskService publicTaskService;
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPublicTaskService(PublicTaskService publicTaskService) {
        this.publicTaskService = publicTaskService;
    }

    @RequestMapping("/all")
    public List<PublicTask> listAllTasks() {
        return publicTaskService.listAllUnfinished();
    }

    @RequestMapping("/history")
    public List<PublicTask> listFinishedTasks() {
        return publicTaskService.listAllFinished();
    }

    @RequestMapping("/last")
    public List<PublicTask> listLastFinishedTasks() {
        return publicTaskService.listLastFinished();
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> addTask(@RequestBody PublicTask task, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        if (user.isManager()) {
            task.setDone(false);
            return new ResponseEntity<>(publicTaskService.saveOrUpdate(task), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateTask(@RequestBody PublicTask task, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        PublicTask oldTask = publicTaskService.getById(task.getId());
        if (user.isManager()) {
            oldTask.setDescription(task.getDescription());
            oldTask.setTitle(task.getTitle());
            oldTask.setAssignee(task.getAssignee());
            if (task.getDeadline() != null) {
                oldTask.setDeadline(task.getDeadline());
            }
            return new ResponseEntity<>(publicTaskService.saveOrUpdate(oldTask), HttpStatus.OK);
        } else {
            if (task.getAssignee().getFirstName().equals(user.getFirstName()) && task.getAssignee().getLastName().equals(user.getLastName())) {
                oldTask.setResponse(task.getResponse());
                return new ResponseEntity<>(publicTaskService.saveOrUpdate(oldTask), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public Task editTask(@PathVariable("id") int id) {
        return publicTaskService.getById(id);
    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteTask(@PathVariable("id") int id, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        if (user.isManager()) {
            publicTaskService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @RequestMapping("/toogle/{id}")
    public ResponseEntity<?> toogleTask(@PathVariable("id") int id, Principal principal) {
        PublicTask task = publicTaskService.getById(id);
        User user = userService.findByUsername(principal.getName());
        if (task.getAssignee().getFirstName().equals(user.getFirstName()) && task.getAssignee().getLastName().equals(user.getLastName())) {
            publicTaskService.toogleTask(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

}
