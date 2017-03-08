package com.khramovdmitry.controllers;

import com.khramovdmitry.domain.PublicTask;
import com.khramovdmitry.domain.Response;
import com.khramovdmitry.services.PublicTaskService;
import com.khramovdmitry.services.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Dmitry on 07.03.2017.
 */

@RestController
@RequestMapping("/rest/response/")
public class ResponseController {

    @Autowired
    private ResponseService responseService;

    @Autowired
    private PublicTaskService publicTaskService;

    @RequestMapping("/task/{id}/read")
    public Response readResponse(@PathVariable int id) {

        PublicTask task = publicTaskService.getById(id);

        return task.getResponse();
    }

    @RequestMapping("/task/{id}/add")
    public ResponseEntity<?> readResponse(@PathVariable int id, @RequestBody Response response) {

        PublicTask task = publicTaskService.getById(id);
        response.setTask(task);
        return new ResponseEntity<>(responseService.saveOrUpdate(response), HttpStatus.OK);
    }
}
