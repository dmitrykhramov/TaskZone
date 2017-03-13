package com.khramovdmitry.bootstrap;

import com.khramovdmitry.domain.PrivateTask;
import com.khramovdmitry.domain.PublicTask;
import com.khramovdmitry.domain.User;
import com.khramovdmitry.services.PrivateTaskService;
import com.khramovdmitry.services.PublicTaskService;
import com.khramovdmitry.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Dmitry on 12.03.2017.
 */

@Component
public class SpringDataBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private UserService userService;
    private PublicTaskService publicTaskService;
    private PrivateTaskService privateTaskService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPublicTaskService(PublicTaskService publicTaskService) {
        this.publicTaskService = publicTaskService;
    }

    @Autowired
    public void setPrivateTaskService(PrivateTaskService privateTaskService) {
        this.privateTaskService = privateTaskService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        loadUsers();
        loadPublicTasks();
        loadPrivateTasks();
    }

    private void loadPrivateTasks() {
        PrivateTask task1 = new PrivateTask();
        task1.setTitle("Private task 1");
        Date date1 = new GregorianCalendar(2017, Calendar.MARCH, 25).getTime();
        task1.setDeadline(date1);
        task1.setDescription("Some description");
        task1.setUser(userService.findByUsername("manager"));
        privateTaskService.saveOrUpdate(task1);

        PrivateTask task2 = new PrivateTask();
        task2.setTitle("Private task 2");
        Date date2 = new GregorianCalendar(2017, Calendar.MARCH, 20).getTime();
        task2.setDeadline(date2);
        task2.setDescription("Some description");
        task2.setUser(userService.findByUsername("polina"));
        privateTaskService.saveOrUpdate(task2);

        PrivateTask task3 = new PrivateTask();
        task3.setTitle("Private task 3");
        Date date3 = new GregorianCalendar(2017, Calendar.MARCH, 18).getTime();
        task3.setDeadline(date3);
        task3.setDescription("Some description");
        task3.setUser(userService.findByUsername("mikel"));
        privateTaskService.saveOrUpdate(task3);
    }

    private void loadPublicTasks() {
        PublicTask task1 = new PublicTask();
        task1.setTitle("Make inventarization");
        Date date1 = new GregorianCalendar(2017, Calendar.MARCH, 25).getTime();
        task1.setDeadline(date1);
        task1.setDescription("Some description");
        task1.setAssignee(userService.findByUsername("polina"));
        publicTaskService.saveOrUpdate(task1);

        PublicTask task2 = new PublicTask();
        task2.setTitle("Promotion event");
        Date date2 = new GregorianCalendar(2017, Calendar.MARCH, 10).getTime();
        task2.setDeadline(date2);
        task2.setDescription("Some description");
        task2.setAssignee(userService.findByUsername("mikel"));
        publicTaskService.saveOrUpdate(task2);

        PublicTask task3 = new PublicTask();
        task3.setTitle("Monthly report");
        Date date3 = new GregorianCalendar(2017, Calendar.MARCH, 20).getTime();
        task3.setDeadline(date3);
        task3.setDescription("Some description");
        task3.setAssignee(userService.findByUsername("mikel"));
        publicTaskService.saveOrUpdate(task3);

        PublicTask task4 = new PublicTask();
        task4.setTitle("Recently finished 1");
        Date date4 = new GregorianCalendar(2017, Calendar.MARCH, 10).getTime();
        Date finished4 = new GregorianCalendar(2017, Calendar.MARCH, 12).getTime();
        task4.setDeadline(date4);
        task4.setFinished(finished4);
        task4.setDone(true);
        task4.setDescription("Some description");
        task4.setAssignee(userService.findByUsername("mikel"));
        publicTaskService.saveOrUpdate(task4);

        PublicTask task5 = new PublicTask();
        task5.setTitle("Recently finished 2");
        Date date5 = new GregorianCalendar(2017, Calendar.MARCH, 10).getTime();
        Date finished5 = new GregorianCalendar(2017, Calendar.MARCH, 13).getTime();
        task5.setDeadline(date5);
        task5.setFinished(finished5);
        task5.setDone(true);
        task5.setDescription("Some description");
        task5.setAssignee(userService.findByUsername("mikel"));
        publicTaskService.saveOrUpdate(task5);

        PublicTask task6 = new PublicTask();
        task6.setTitle("History task");
        Date date6 = new GregorianCalendar(2017, Calendar.MARCH, 10).getTime();
        Date finished6 = new GregorianCalendar(2017, Calendar.MARCH, 9).getTime();
        task6.setDeadline(date6);
        task6.setDone(true);
        task6.setFinished(finished6);
        task6.setDescription("Some description");
        task6.setAssignee(userService.findByUsername("polina"));
        publicTaskService.saveOrUpdate(task6);
    }

    private void loadUsers() {

        User dmitry = new User();
        dmitry.setEmail("dmitry@lush.fi");
        dmitry.setFirstName("Dmitry");
        dmitry.setLastName("Khramov");
        dmitry.setPassword("manager");
        dmitry.setUsername("manager");
        dmitry.setManager(true);
        userService.saveOrUpdate(dmitry);

        User polina = new User();
        polina.setEmail("polina@lush.fi");
        polina.setFirstName("Polina");
        polina.setLastName("Timofeeva");
        polina.setPassword("polina");
        polina.setUsername("polina");
        polina.setManager(false);
        userService.saveOrUpdate(polina);

        User mikel = new User();
        mikel.setEmail("mikel@lush.fi");
        mikel.setFirstName("Mikel");
        mikel.setLastName("Smith");
        mikel.setPassword("mikel");
        mikel.setUsername("mikel");
        mikel.setManager(false);
        userService.saveOrUpdate(mikel);
    }
}
