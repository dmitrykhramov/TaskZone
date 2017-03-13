package com.khramovdmitry.services;

import com.khramovdmitry.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

/**
 * Created by Dmitry on 13.03.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class UserServiceImplTest {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Test
    public void findByEmail() throws Exception {
        User user = new User();
        user.setEmail("user@lush.fi");
        user.setFirstName("TestUser1");
        userService.saveOrUpdate(user);
        User foundUser = userService.findByEmail("user@lush.fi");
        assert user.getFirstName().equals(foundUser.getFirstName());
    }

    @Test
    public void findByUsername() throws Exception {
        User user = new User();
        user.setUsername("username");
        user.setFirstName("TestUser2");
        userService.saveOrUpdate(user);
        User foundUser = userService.findByUsername("username");
        assert user.getFirstName().equals(foundUser.getFirstName());
    }

    @Test
    public void testChangePasswordTrue() throws Exception {
        boolean result = userService.changePassword(userService.findByUsername("manager"), "manager", "abcd");
        System.out.println(result);
        assert result;
        assert userService.findByUsername("manager").getPassword().equals("abcd");
    }

    @Test
    public void testChangePasswordFalse() throws Exception {
        boolean result = userService.changePassword(userService.findByUsername("manager"), "password", "abcd");
        System.out.println(result);
        assert !result;
        assert userService.findByUsername("manager").getPassword().equals("manager");
    }
}
