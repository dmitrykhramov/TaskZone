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

    @Autowired
    private PublicTaskService publicTaskService;

    @RequestMapping("/all")
    public List<PublicTask> listAllTasks() {
        return publicTaskService.listAllUnfinished();
    }

    @RequestMapping("/history")
    public List<PublicTask> listFinishedTasks() {
        return publicTaskService.listAllFinished();
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> addTask(@RequestBody PublicTask task) {
        task.setDone(false);
        System.out.println(task.getAssignee());
        return new ResponseEntity<>(publicTaskService.saveOrUpdate(task), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateTask(@RequestBody PublicTask task) {
        PublicTask oldTask = publicTaskService.getById(task.getId());
//        oldTask.setResponse(task.getResponse());
        oldTask.setDescription(task.getDescription());
        oldTask.setTitle(task.getTitle());
        oldTask.setDeadline(task.getDeadline());
        return new ResponseEntity<>(publicTaskService.saveOrUpdate(oldTask), HttpStatus.OK);
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public Task editTask(@PathVariable("id") int id) {
        return publicTaskService.getById(id);
    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteTask(@PathVariable("id") int id) {
        Task taskToDelete = publicTaskService.getById(id);
        publicTaskService.delete(id);
        return new ResponseEntity<>(taskToDelete, HttpStatus.OK);
    }

    @RequestMapping("/toogle/{id}")
    public ResponseEntity<?> toogleTask(@PathVariable("id") int id) {
        publicTaskService.toogleTask(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping("/populate")
    public String populateTasks() {
        PublicTask task = new PublicTask();
        task.setTitle("Title2");
        task.setDescription("Testing task2");
        publicTaskService.saveOrUpdate(task);
        return "populated";
    }

}
