package com.khramovdmitry.services;

import com.khramovdmitry.domain.PrivateTask;

import java.util.List;

/**
 * Created by Dmitry on 03.03.2017.
 */

public interface PrivateTaskService extends BaseService<PrivateTask> {

    List<PrivateTask> listAllFinished();

    List<PrivateTask> listAllUnfinished();

    void toogleTask(int id);
}
