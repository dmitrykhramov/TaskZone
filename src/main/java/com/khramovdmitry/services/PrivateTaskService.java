package com.khramovdmitry.services;

import com.khramovdmitry.domain.PrivateTask;
import com.khramovdmitry.domain.User;

import java.util.List;

/**
 * Created by Dmitry on 03.03.2017.
 */

public interface PrivateTaskService extends BaseService<PrivateTask> {

    List<PrivateTask> listAllFinished(User user);

    List<PrivateTask> listAllUnfinished(User user);

    void toogleTask(int id);
}
