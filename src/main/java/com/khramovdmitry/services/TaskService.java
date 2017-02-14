package com.khramovdmitry.services;

import com.khramovdmitry.domain.PublicTask;
import com.khramovdmitry.domain.Task;

import java.util.List;

/**
 * Created by Dmitry on 12.02.2017.
 */

public interface TaskService extends BaseService<Task> {

    List<PublicTask> listAllFinished();

    List<PublicTask> listAllUnfinished();

    List<PublicTask> listPrivateFinished();

    List<PublicTask> listPrivateUnfinished();

    void toogleTask(int id);
}
