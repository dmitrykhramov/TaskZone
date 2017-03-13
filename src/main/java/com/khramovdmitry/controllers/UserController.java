package com.khramovdmitry.controllers;

import com.khramovdmitry.domain.User;
import com.khramovdmitry.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * Created by Dmitry on 04.03.2017.
 */

@RestController
@RequestMapping("/rest/user")
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<?> listUsers() {
        return userService.listAll();
    }

    @RequestMapping(value = "/principal", method = RequestMethod.GET)
    public User currentUser(Principal principal) {
        return userService.findByUsername(principal.getName());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> addEmployee(@RequestBody User user, Principal principal) {
        if (!userService.findByUsername(principal.getName()).isManager()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        userService.saveOrUpdate(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    public ResponseEntity<?> updateEmployee(@RequestBody User user, Principal principal) {
        User oldUser = userService.findByUsername(principal.getName());
        oldUser.setUsername(user.getUsername());
        oldUser.setFirstName(user.getFirstName());
        oldUser.setLastName(user.getLastName());
        oldUser.setEmail(user.getEmail());

        return new ResponseEntity<>(userService.saveOrUpdate(oldUser), HttpStatus.OK);
    }

    @RequestMapping(value = "/changePass", method = RequestMethod.PUT)
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> map, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        if (userService.changePassword(user, map.get("oldPass"), map.get("newPass"))) {
            return new ResponseEntity<>("Changed", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") int id, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        if (!user.isManager() || user.getUserId() == id) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
