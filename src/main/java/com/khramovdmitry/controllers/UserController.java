package com.khramovdmitry.controllers;

import com.khramovdmitry.domain.User;
import com.khramovdmitry.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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

    @RequestMapping(value = "/checkuser", method = RequestMethod.GET)
    public boolean isManager(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        return user.isManager();
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addEmployee(@RequestBody User user) {
        userService.createUser(user);
        return "created";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    public String updateEmployee(@RequestBody User user) {
        userService.saveOrUpdate(user);
        return "updated";
    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> updateEmployee(@PathVariable("id") int id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(value = "/populate", method = RequestMethod.GET)
    public String populateUsers() {
        User user = new User();
        user.setEmail("antti.piironen@metropolia.fi");
        user.setFirstName("Antti");
        user.setLastName("Piironen");
        user.setPassword("admin");
        user.setUsername("admin");
        user.setManager(true);
        userService.createUser(user);

        User user1 = new User();
        user1.setEmail("dmitry.khramov@metropolia.fi");
        user1.setFirstName("Dmitry");
        user1.setLastName("Khramov");
        user1.setPassword("user");
        user1.setUsername("user");
        user1.setManager(false);
        userService.createUser(user1);
        return "user populated";
    }


}
