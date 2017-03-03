package com.khramovdmitry.services;

import com.khramovdmitry.domain.PublicTask;
import com.khramovdmitry.domain.Task;

import java.util.List;

/**
 * Created by Dmitry on 12.02.2017.
 */

public interface PublicTaskService extends BaseService<PublicTask> {

    List<PublicTask> listAllFinished();

    List<PublicTask> listAllUnfinished();

    void toogleTask(int id);
}
