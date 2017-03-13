package com.khramovdmitry.services;

import com.khramovdmitry.domain.PublicTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.AFTER_METHOD;

/**
 * Created by Dmitry on 12.03.2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class PublicTaskServiceImplTest {
    
    private PublicTaskService publicTaskService;

    @Autowired
    public void setPublicTaskService(PublicTaskService publicTaskService) {
        this.publicTaskService = publicTaskService;
    }

    @Test
    public void testListAll() throws Exception {
        List<PublicTask> list = publicTaskService.listAll();
        assert list.size() == 6;
    }

    @Test
    @DirtiesContext(methodMode = AFTER_METHOD)
    public void testSaveOrUpdate() throws Exception {
        PublicTask task1 = new PublicTask();
        task1.setTitle("Make inventarization");
        PublicTask newTask = publicTaskService.saveOrUpdate(task1);
        assert newTask.getTitle() != null;
        assert newTask.getId() != null;
        assert !newTask.isDone();
    }

    @Test
    public void testDelete() throws Exception {
        List<PublicTask> list = publicTaskService.listAll();
        assert list.size() == 6;
        PublicTask task1 = new PublicTask();
        PublicTask newTask = publicTaskService.saveOrUpdate(task1);
        list = publicTaskService.listAll();
        assert list.size() == 7;
        publicTaskService.delete(newTask.getId());
        list = publicTaskService.listAll();
        assert list.size() == 6;
    }

    @Test
    public void testListAllFinished() throws Exception {
        List<PublicTask> list = publicTaskService.listAllFinished();
        assert list.size() == 3;
    }

    @Test
    public void testListAllUnfinished() throws Exception {
        List<PublicTask> list = publicTaskService.listAllUnfinished();
        assert list.size() == 3;
    }

    @Test
    public void testListLastFinished() throws Exception {
        List<PublicTask> list = publicTaskService.listLastFinished();
        assert list.size() == 2;
    }

    @Test
    @DirtiesContext(methodMode = AFTER_METHOD)
    public void testListLastFinishedAddLastTask() throws Exception {
        List<PublicTask> list = publicTaskService.listLastFinished();
        assert list.size() == 2;
        PublicTask task1 = new PublicTask();
        task1.setTitle("Make inventarization");
        Date date1 = new Date();
        task1.setFinished(date1);
        task1.setDone(true);
        publicTaskService.saveOrUpdate(task1);
        list = publicTaskService.listLastFinished();
        assert list.size() == 3;
    }

    @Test
    @DirtiesContext(methodMode = AFTER_METHOD)
    public void testListLastFinishedAddOldTask() throws Exception {
        List<PublicTask> list = publicTaskService.listLastFinished();
        assert list.size() == 2;
        PublicTask task1 = new PublicTask();
        task1.setTitle("Make inventarization");
        Date date1 = new GregorianCalendar(2017, Calendar.MARCH, 1).getTime();
        task1.setFinished(date1);
        task1.setDone(true);
        publicTaskService.saveOrUpdate(task1);
        list = publicTaskService.listLastFinished();
        assert list.size() == 2;
    }

    @Test
    @DirtiesContext(methodMode = AFTER_METHOD)
    public void testToogleTask() throws Exception {
        PublicTask task1 = new PublicTask();
        task1.setTitle("Make inventarization");
        PublicTask newTask = publicTaskService.saveOrUpdate(task1);
        assert !newTask.isDone();
        publicTaskService.toogleTask(newTask.getId());
        assert publicTaskService.getById(newTask.getId()).isDone();
    }
}
